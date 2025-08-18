package com.company.fastweb.core.cache.config;

import com.company.fastweb.core.cache.service.CacheService;
import com.company.fastweb.core.cache.service.impl.RedisCacheServiceImpl;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存自动配置
 *
 * @author FastWeb
 */
@Slf4j
@AutoConfiguration
@EnableCaching
@ConditionalOnClass(RedisConnectionFactory.class)
@ComponentScan(basePackages = "com.company.fastweb.core.cache")
@EnableConfigurationProperties(CacheProperties.class)
@RequiredArgsConstructor
public class CacheAutoConfiguration {

    private final CacheProperties cacheProperties;

    /**
     * Redis模板配置
     */
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 配置Jackson序列化器
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = 
            new Jackson2JsonRedisSerializer<>(Object.class);
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, 
            ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.registerModule(new JavaTimeModule());
        
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // String序列化器
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        
        log.info("FastWeb Redis Template configured");
        return template;
    }

    /**
     * StringRedisTemplate配置
     */
    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
    }

    /**
     * Redisson客户端配置
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.redis.redisson", name = "enabled", havingValue = "true")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + cacheProperties.getHost() + ":" + cacheProperties.getPort())
                .setPassword(cacheProperties.getPassword())
                .setDatabase(cacheProperties.getDatabase())
                .setConnectionPoolSize(cacheProperties.getPool().getMaxActive())
                .setConnectionMinimumIdleSize(cacheProperties.getPool().getMinIdle())
                .setIdleConnectionTimeout(cacheProperties.getPool().getIdleTimeout())
                .setConnectTimeout(cacheProperties.getConnectTimeout())
                .setTimeout(cacheProperties.getTimeout());

        return Redisson.create(config);
    }

    /**
     * 缓存管理器配置
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class)))
                .entryTtl(Duration.ofMinutes(cacheProperties.getDefaultTtl()))
                .disableCachingNullValues();

        // 设置不同缓存的过期时间
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("user", config.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("dict", config.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("config", config.entryTtl(Duration.ofHours(2)));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    /**
     * 缓存服务
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheService cacheService(RedisTemplate<String, Object> redisTemplate) {
        log.info("FastWeb Cache Service initialized");
        return new RedisCacheServiceImpl(redisTemplate);
    }
}