package com.company.fastweb.core.data.datasource.aspect;

import com.company.fastweb.core.data.datasource.DataSourceContextHolder;
import com.company.fastweb.core.data.datasource.annotation.DS;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 数据源切换切面
 */
@Aspect
@Order(-1) // 保证该AOP在@Transactional之前执行
public class DSAspect {

    @Pointcut("@annotation(com.company.fastweb.core.data.datasource.annotation.DS) " +
            "|| @within(com.company.fastweb.core.data.datasource.annotation.DS)")
    public void dataSourcePointCut() {
    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String dsName = determineDataSource(point);
        
        try {
            if (StringUtils.hasText(dsName)) {
                DataSourceContextHolder.setDataSourceName(dsName);
            }
            return point.proceed();
        } finally {
            // 清空数据源名称
            DataSourceContextHolder.clearDataSourceName();
        }
    }

    /**
     * 确定数据源名称
     *
     * @param point 切点
     * @return 数据源名称
     */
    private String determineDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();
        Method method = signature.getMethod();
        
        // 优先从方法上获取
        DS dsAnnotation = AnnotationUtils.findAnnotation(method, DS.class);
        if (dsAnnotation != null && StringUtils.hasText(dsAnnotation.value())) {
            return dsAnnotation.value();
        }
        
        // 其次从类上获取
        dsAnnotation = AnnotationUtils.findAnnotation(targetClass, DS.class);
        if (dsAnnotation != null && StringUtils.hasText(dsAnnotation.value())) {
            return dsAnnotation.value();
        }
        
        return null;
    }
}