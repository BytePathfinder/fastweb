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
 * 自动注入@ConfigValue注解的字段
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
            ConfigValue configValue = field.getAnnotation(ConfigValue.class);
            if (configValue != null) {
                processConfigValueField(bean, field, configValue);
            }
        });
        
        return bean;
    }

    /**
     * 处理配置值字段
     */
    private void processConfigValueField(Object bean, Field field, ConfigValue configValue) {
        try {
            String key = configValue.key();
            String defaultValue = configValue.defaultValue();
            boolean required = configValue.required();
            boolean autoRefresh = configValue.autoRefresh();

            // 获取配置值
            Object value = getConfigValue(key, field.getType(), defaultValue, required);
            
            if (value != null) {
                // 设置字段值
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, bean, value);
                
                log.debug("配置值注入成功: bean={}, field={}, key={}, value={}", 
                    bean.getClass().getSimpleName(), field.getName(), key, value);
                
                // 如果需要自动刷新，添加监听器
                if (autoRefresh) {
                    configService.addConfigListener(key, (k, oldVal, newVal) -> {
                        try {
                            Object newValue = convertValue(newVal, field.getType(), defaultValue);
                            ReflectionUtils.setField(field, bean, newValue);
                            log.info("配置值自动刷新: bean={}, field={}, key={}, oldValue={}, newValue={}", 
                                bean.getClass().getSimpleName(), field.getName(), k, oldVal, newVal);
                        } catch (Exception e) {
                            log.error("配置值自动刷新失败: bean={}, field={}, key={}", 
                                bean.getClass().getSimpleName(), field.getName(), k, e);
                        }
                    });
                }
            }
        } catch (Exception e) {
            log.error("配置值注入失败: bean={}, field={}, key={}", 
                bean.getClass().getSimpleName(), field.getName(), configValue.key(), e);
        }
    }

    /**
     * 获取配置值
     */
    private Object getConfigValue(String key, Class<?> type, String defaultValue, boolean required) {
        // 先尝试从配置服务获取
        Object value = configService.getConfig(key, type);
        
        if (value == null && !defaultValue.isEmpty()) {
            // 使用默认值
            value = convertValue(defaultValue, type, null);
        }
        
        if (value == null && required) {
            throw new IllegalStateException("必需的配置项不存在: " + key);
        }
        
        return value;
    }

    /**
     * 类型转换
     */
    @SuppressWarnings("unchecked")
    private Object convertValue(String value, Class<?> type, String defaultValue) {
        if (value == null) {
            value = defaultValue;
        }
        
        if (value == null) {
            return null;
        }

        if (type == String.class) {
            return value;
        }

        try {
            if (type == Integer.class || type == int.class) {
                return Integer.valueOf(value);
            }
            if (type == Long.class || type == long.class) {
                return Long.valueOf(value);
            }
            if (type == Double.class || type == double.class) {
                return Double.valueOf(value);
            }
            if (type == Boolean.class || type == boolean.class) {
                return Boolean.valueOf(value);
            }
            
            // 其他类型暂不支持
            log.warn("不支持的配置值类型: {}", type.getName());
            return value;
        } catch (Exception e) {
            log.error("配置值类型转换失败: value={}, type={}", value, type.getName(), e);
            return null;
        }
    }
}