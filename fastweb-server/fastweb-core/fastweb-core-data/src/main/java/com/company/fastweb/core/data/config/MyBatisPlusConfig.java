package com.company.fastweb.core.data.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.company.fastweb.core.data.handler.MyMetaObjectHandler;
import com.company.fastweb.core.data.interceptor.TenantLineHandlerImpl;

/**
 * MyBatis-Plus配置类
 *
 * @author FastWeb
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.company.fastweb.**.mapper")
public class MyBatisPlusConfig {

    /**
     * MyBatis-Plus插件配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(1000L); // 单页最大限制
        paginationInnerInterceptor.setOverflow(false); // 超出总页数是否返回最后一页
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        
        // 多租户插件
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandlerImpl()));
        
        return interceptor;
    }

    /**
     * 元对象字段填充处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public MetaObjectHandler metaObjectHandler() {
        return new MyMetaObjectHandler();
    }

    /**
     * 主键生成器
     * <p>使用雪花算法生成分布式唯一ID，支持高并发场景</p>
     */
    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new IdentifierGenerator() {
            private final long workerId = 1L; // 工作节点ID，生产环境建议配置化
            private final long datacenterId = 1L; // 数据中心ID
            private long sequence = 0L;
            private long lastTimestamp = -1L;
            
            private static final long WORKER_ID_BITS = 5L;
            private static final long DATACENTER_ID_BITS = 5L;
            private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
            private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
            private static final long SEQUENCE_BITS = 12L;
            
            private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
            private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
            private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
            private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
            
            @Override
            public synchronized Number nextId(Object entity) {
                long timestamp = System.currentTimeMillis();
                
                if (timestamp < lastTimestamp) {
                    throw new RuntimeException("时钟向后移动，拒绝生成ID " + (lastTimestamp - timestamp));
                }
                
                if (lastTimestamp == timestamp) {
                    sequence = (sequence + 1) & SEQUENCE_MASK;
                    if (sequence == 0) {
                        timestamp = tilNextMillis(lastTimestamp);
                    }
                } else {
                    sequence = 0L;
                }
                
                lastTimestamp = timestamp;
                
                return ((timestamp - 1288834974657L) << TIMESTAMP_LEFT_SHIFT)
                        | (datacenterId << DATACENTER_ID_SHIFT)
                        | (workerId << WORKER_ID_SHIFT)
                        | sequence;
            }
            
            private long tilNextMillis(long lastTimestamp) {
                long timestamp = System.currentTimeMillis();
                while (timestamp <= lastTimestamp) {
                    timestamp = System.currentTimeMillis();
                }
                return timestamp;
            }
        };
    }
}