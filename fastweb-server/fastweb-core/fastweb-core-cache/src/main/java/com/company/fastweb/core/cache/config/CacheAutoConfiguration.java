package com.company.fastweb.core.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;
import org.springframework.cache.support.SimpleCacheManager;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 缓存自动配置
 *
 * @author FastWeb
 */
@Slf4j
@AutoConfiguration
@EnableCaching
@ConditionalOnProperty(prefix = "fastweb.cache", name = "enabled", havingValue = "true")
@EnableConfigurationProperties({FastWebCacheProperties.class})
public class CacheAutoConfiguration {

    /**
     * Redis 相关配置（当 Redis 类存在时才启用）
     */
    @Configuration
    @RequiredArgsConstructor
    @ConditionalOnClass({RedisTemplate.class, RedisConnectionFactory.class})
    public static class RedisConfiguration {

        private final FastWebCacheProperties fastWebCacheProperties;
        private final RedisConnectionFactory redisConnectionFactory;

        /**
         * Redis模板配置
         */
        @Bean
        @ConditionalOnMissingBean(name = "redisTemplate")
        public RedisTemplate<String, Object> redisTemplate() {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory);

            // 配置Jackson序列化器
            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = createJackson2JsonRedisSerializer();

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
        public StringRedisTemplate stringRedisTemplate() {
            StringRedisTemplate template = new StringRedisTemplate();
            template.setConnectionFactory(redisConnectionFactory);
            return template;
        }

        /**
         * Redis缓存管理器
         */
        @Bean
        @ConditionalOnMissingBean(CacheManager.class)
        public CacheManager redisCacheManager() {
            return createRedisCacheManager();
        }

        private RedisCacheManager createRedisCacheManager() {
            RedisCacheConfiguration defaultConfig = createRedisCacheConfiguration(fastWebCacheProperties.getConfigs().get("default"));

            Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
            if (!CollectionUtils.isEmpty(fastWebCacheProperties.getConfigs())) {
                fastWebCacheProperties.getConfigs().forEach((name, config) -> {
                    if (config.getRedis() != null && config.getRedis().isEnabled()) {
                        cacheConfigurations.put(name, createRedisCacheConfiguration(config));
                    }
                });
            }

            RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory)
                    .cacheDefaults(defaultConfig)
                    .withInitialCacheConfigurations(cacheConfigurations)
                    .build();
            
            log.info("FastWeb Redis Cache Manager configured");
            return redisCacheManager;
        }

        private RedisCacheConfiguration createRedisCacheConfiguration(FastWebCacheProperties.CacheConfig config) {
            long ttl = config != null ? config.getExpireTime() : 3600;
            return RedisCacheConfiguration.defaultCacheConfig()
                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(createJackson2JsonRedisSerializer()))
                    .entryTtl(Duration.ofSeconds(ttl))
                    .disableCachingNullValues();
        }

        private Jackson2JsonRedisSerializer<Object> createJackson2JsonRedisSerializer() {
            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                    new Jackson2JsonRedisSerializer<>(Object.class);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                    ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            objectMapper.registerModule(new JavaTimeModule());

            jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
            return jackson2JsonRedisSerializer;
        }
    }

    /**
     * Caffeine 本地缓存配置
     */
    @RequiredArgsConstructor
    @ConditionalOnClass({Caffeine.class, CaffeineCache.class})
    public static class LocalCacheConfiguration {

        private final FastWebCacheProperties fastWebCacheProperties;

        /**
         * 缓存管理器配置 - 优先本地缓存，其次 Redis 缓存
         */
        @Bean
        @Primary
        @ConditionalOnMissingBean
        public CacheManager cacheManager() {
            List<CacheManager> cacheManagers = new java.util.ArrayList<>();

            // 优先添加 Caffeine 本地缓存
            if (!CollectionUtils.isEmpty(fastWebCacheProperties.getConfigs())) {
                List<CaffeineCache> caffeineCaches = fastWebCacheProperties.getConfigs().values().stream()
                        .filter(config -> config.getLocal() != null && config.getLocal().isEnabled())
                        .map(this::createCaffeineCache)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                if (!caffeineCaches.isEmpty()) {
                    SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
                    simpleCacheManager.setCaches(caffeineCaches);
                    cacheManagers.add(simpleCacheManager); // Caffeine作为一级缓存
                    log.info("FastWeb Caffeine Cache Manager configured with caches: {}", 
                            caffeineCaches.stream().map(CaffeineCache::getName).toList());
                }
            }

            // 尝试添加 Redis 缓存（如果可用）
            try {
                Class.forName("org.springframework.data.redis.core.RedisTemplate");
                // Redis 可用，但需要运行时检查是否有 RedisConnectionFactory
                // 这里暂时不添加，由 RedisConfiguration 处理
                log.info("Redis classes available, Redis cache may be configured separately");
            } catch (ClassNotFoundException e) {
                log.info("Redis classes not available, using Caffeine cache only");
            }

            if (cacheManagers.isEmpty()) {
                // 如果没有任何缓存管理器，创建一个空的本地缓存管理器
                log.warn("No cache managers configured, creating minimal Caffeine cache manager");
                SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
                simpleCacheManager.setCaches(List.of());
                cacheManagers.add(simpleCacheManager);
            }

            CompositeCacheManager compositeCacheManager = new CompositeCacheManager(cacheManagers.toArray(new CacheManager[0]));
            compositeCacheManager.setFallbackToNoOpCache(true);
            
            log.info("FastWeb Composite Cache Manager configured with {} managers", cacheManagers.size());
            return compositeCacheManager;
        }

        private CaffeineCache createCaffeineCache(FastWebCacheProperties.CacheConfig cacheConfig) {
            FastWebCacheProperties.LocalCacheConfig localConfig = cacheConfig.getLocal();
            Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder()
                    .initialCapacity(localConfig.getInitialCapacity())
                    .maximumSize(localConfig.getMaximumSize())
                    .expireAfterWrite(Duration.ofSeconds(cacheConfig.getExpireTime()));
            return new CaffeineCache(cacheConfig.getName(), caffeineBuilder.build());
        }
    }
}