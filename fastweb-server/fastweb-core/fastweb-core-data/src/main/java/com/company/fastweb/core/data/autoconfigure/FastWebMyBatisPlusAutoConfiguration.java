package com.company.fastweb.core.data.autoconfigure;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.company.fastweb.core.data.mybatis.FastWebMetaObjectHandler;
import com.company.fastweb.core.data.properties.FastWebDataProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * FastWeb MyBatis-Plus 自动配置类
 */
@AutoConfiguration
@ConditionalOnClass({SqlSessionFactory.class, MybatisPlusAutoConfiguration.class})
@EnableConfigurationProperties(FastWebDataProperties.class)
@org.springframework.boot.autoconfigure.AutoConfigureAfter(FastWebDataAutoConfiguration.class)
public class FastWebMyBatisPlusAutoConfiguration {
    
    private final FastWebDataProperties properties;
    
    public FastWebMyBatisPlusAutoConfiguration(FastWebDataProperties properties) {
        this.properties = properties;
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "fastweb.data", name = "enabled", havingValue = "true", matchIfMissing = true)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 分页插件
        if (properties.getMybatisPlus().getPagination().isEnabled()) {
            // 使用反射创建分页插件，避免直接依赖可能不存在的类
            try {
                Class<?> paginationClass = Class.forName("com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor");
                Object paginationInterceptor = paginationClass.getConstructor(DbType.class).newInstance(DbType.MYSQL);
                
                // 设置最大限制
                paginationClass.getMethod("setMaxLimit", Long.class)
                    .invoke(paginationInterceptor, properties.getMybatisPlus().getPagination().getMaxLimit());
                
                // 添加到拦截器
                interceptor.getClass().getMethod("addInnerInterceptor", Class.forName("com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor"))
                    .invoke(interceptor, paginationInterceptor);
            } catch (Exception e) {
                // 记录日志但不中断启动
                System.err.println("无法创建分页插件: " + e.getMessage());
            }
        }
        
        // 多租户插件
        if (properties.getMybatisPlus().getTenant().isEnabled()) {
            try {
                // 使用反射创建租户插件
                Class<?> tenantClass = Class.forName("com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor");
                Object tenantInterceptor = tenantClass.getDeclaredConstructor().newInstance();
                
                // 创建租户处理器
                Class<?> handlerClass = Class.forName("com.company.fastweb.core.data.mybatis.FastWebTenantLineHandler");
                Object handler = handlerClass.getConstructor(String.class)
                    .newInstance(properties.getMybatisPlus().getTenant().getTenantIdColumn());
                
                // 设置租户处理器
                tenantClass.getMethod("setTenantLineHandler", Class.forName("com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler"))
                    .invoke(tenantInterceptor, handler);
                
                // 添加到拦截器
                interceptor.getClass().getMethod("addInnerInterceptor", Class.forName("com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor"))
                    .invoke(interceptor, tenantInterceptor);
            } catch (Exception e) {
                // 记录日志但不中断启动
                System.err.println("无法创建租户插件: " + e.getMessage());
            }
        }
        
        return interceptor;
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "fastweb.data", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new FastWebMetaObjectHandler());
        return globalConfig;
    }
}
