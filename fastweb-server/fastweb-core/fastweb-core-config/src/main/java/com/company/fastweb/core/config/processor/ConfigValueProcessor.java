package com.company.fastweb.core.config.processor;

import com.company.fastweb.core.config.annotation.ConfigValue;
import com.company.fastweb.core.config.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * 配置值处理器
 * 自动注入配置值到标注了@ConfigValue的字段
 *
 * @author FastWeb
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConfigValueProcessor implements BeanPostProcessor {

    private final ConfigService configService;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        
        // 处理所有字段
        ReflectionUtils.doWithFields(clazz, field -> {
            ConfigValue annotation = field.getAnnotation(ConfigValue.class);
            if (annotation != null) {
                processConfigValue(bean, field, annotation);
            }
        });
        
        return bean;
    }

    /**
     * 处理配置值注入
     */
    private void processConfigValue(Object bean, Field field, ConfigValue annotation) {
        try {
            String key = annotation.key();
            String defaultValue = annotation.defaultValue();
            
            // 获取配置值
            Object value = getConfigValue(key, field.getType(), defaultValue);
            
            // 设置字段值
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, bean, value);
            
            // 如果需要自动刷新，添加监听器
            if (annotation.autoRefresh()) {
                configService.addConfigListener(key, (configKey, oldValue, newValue) -> {
                    try {
                        Object newVal = convertValue(newValue, field.getType(), defaultValue);
                        ReflectionUtils.setField(field, bean, newVal);
                        log.debug("配置值自动刷新: key={}, newValue={}", configKey, newValue);
                    } catch (Exception e) {
                        log.error("配置值自动刷新失败: key={}", configKey, e);
                    }
                });
            }
            
            log.debug("配置值注入成功: key={}, value={}, field={}.{}", 
                key, value, bean.getClass().getSimpleName(), field.getName());
                
        } catch (Exception e) {
            log.error("配置值注入失败: key={}, field={}.{}", 
                annotation.key(), bean.getClass().getSimpleName(), field.getName(), e);
        }
    }

    /**
     * 获取配置值
     */
    private Object getConfigValue(String key, Class<?> type, String defaultValue) {
        if (defaultValue.isEmpty()) {
            return configService.getConfig(key, type);
        } else {
            Object defaultVal = convertValue(defaultValue, type, null);
            return configService.getConfig(key, type, defaultVal);
        }
    }

    /**
     * 类型转换
     */
    @SuppressWarnings("unchecked")
    private <T> T convertValue(String value, Class<T> type, String defaultValue) {
        if (value == null) {
            value = defaultValue;
        }
        
        if (value == null) {
            return null;
        }
        
        if (type == String.class) {
            return (T) value;
        }
        
        if (type == Integer.class || type == int.class) {
            return (T) Integer.valueOf(value);
        }
        
        if (type == Long.class || type == long.class) {
            return (T) Long.valueOf(value);
        }
        
        if (type == Double.class || type == double.class) {
            return (T) Double.valueOf(value);
        }
        
        if (type == Boolean.class || type == boolean.class) {
            return (T) Boolean.valueOf(value);
        }
        
        throw new IllegalArgumentException("不支持的配置值类型: " + type.getSimpleName());
    }
}