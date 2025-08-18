package com.company.fastweb.core.data.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.company.fastweb.core.data.handler.JsonMapTypeHandler;
import com.company.fastweb.core.data.properties.FastWebDataProperties;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MyBatis-Plus 企业级配置类
 * 提供完整的 MyBatis-Plus 插件配置和企业级功能
 *
 * @author FastWeb
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@MapperScan("com.company.fastweb.**.mapper")
@EnableTransactionManagement
@EnableConfigurationProperties(FastWebDataProperties.class)
@ConditionalOnClass(SqlSessionFactory.class)
public class MyBatisPlusConfig {

    private final FastWebDataProperties dataProperties;

    @PostConstruct
    public void init() {
        log.info("MyBatis-Plus 配置初始化完成");
        log.info("分页插件配置 - 默认页大小: {}, 最大页大小: {}", 
                dataProperties.getMybatisPlus().getPage().getDefaultSize(),
                dataProperties.getMybatisPlus().getPage().getMaxSize());
        if (dataProperties.getMybatisPlus().getTenant().isEnabled()) {
            log.info("多租户插件已启用 - 租户字段: {}", 
                    dataProperties.getMybatisPlus().getTenant().getTenantIdColumn());
        }
    }

    /**
     * MyBatis-Plus 拦截器配置
     * 包含分页、多租户、乐观锁、防全表更新删除、SQL性能规范检查等插件
     */
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(
            @Autowired(required = false) TenantLineHandler tenantLineHandler) {
        
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 1. 多租户插件（如果启用且存在处理器）
        if (dataProperties.getMybatisPlus().getTenant().isEnabled() && tenantLineHandler != null) {
            TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor(tenantLineHandler);
            interceptor.addInnerInterceptor(tenantInterceptor);
            log.info("多租户插件已加载");
        }
        
        // 2. 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setDbType(DbType.MYSQL);
        paginationInterceptor.setMaxLimit(dataProperties.getMybatisPlus().getPage().getMaxSize());
        paginationInterceptor.setOverflow(false);
        interceptor.addInnerInterceptor(paginationInterceptor);
        log.info("分页插件已加载");
        
        // 3. 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        log.info("乐观锁插件已加载");
        
        // 4. 防全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        log.info("防全表更新删除插件已加载");
        
        // 5. SQL性能规范插件
        interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        log.info("SQL性能规范插件已加载");
        
        return interceptor;
    }

    /**
     * JSON Map 类型处理器
     * 用于处理JSON字段与Map对象的转换
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "fastweb.data", name = "enable-json-type-handler", havingValue = "true", matchIfMissing = true)
    public JsonMapTypeHandler jsonMapTypeHandler() {
        log.info("JSON Map 类型处理器已注册");
        return new JsonMapTypeHandler();
    }

    /**
     * 注册自定义类型处理器到 SqlSessionFactory
     */
    @PostConstruct
    public void registerTypeHandlers() {
        // 类型处理器会通过 @MappedTypes 和 @MappedJdbcTypes 自动注册
        log.debug("自定义类型处理器注册完成");
    }
}