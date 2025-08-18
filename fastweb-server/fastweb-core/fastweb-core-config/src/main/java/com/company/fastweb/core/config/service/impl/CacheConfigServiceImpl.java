package com.company.fastweb.core.config.service.impl;

import com.company.fastweb.core.cache.service.CacheService;
import com.company.fastweb.core.common.util.JsonUtils;
import com.company.fastweb.core.config.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 基于缓存的配置服务实现
 *
 * @author FastWeb
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheConfigServiceImpl implements ConfigService {

    private final CacheService cacheService;

    private static final String CONFIG_PREFIX = "config:";
    private static final String CONFIG_DESC_PREFIX = "config:desc:";
    private static final Duration CONFIG_EXPIRE = Duration.ofHours(24);

    // 配置变更监听器
    private final Map<String, List<ConfigChangeListener>> listeners = new ConcurrentHashMap<>();

    @Override
    public String getConfig(String key) {
        return cacheService.get(CONFIG_PREFIX + key, String.class);
    }

    @Override
    public String getConfig(String key, String defaultValue) {
        String value = getConfig(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public <T> T getConfig(String key, Class<T> type) {
        String value = getConfig(key);
        return convertValue(value, type);
    }

    @Override
    public <T> T getConfig(String key, Class<T> type, T defaultValue) {
        T value = getConfig(key, type);
        return value != null ? value : defaultValue;
    }

    @Override
    public boolean setConfig(String key, String value) {
        return setConfig(key, value, null);
    }

    @Override
    public boolean setConfig(String key, String value, String description) {
        try {
            String oldValue = getConfig(key);
            
            // 设置配置值
            cacheService.set(CONFIG_PREFIX + key, value, CONFIG_EXPIRE);
            
            // 设置描述（如果有）
            if (description != null && !description.trim().isEmpty()) {
                cacheService.set(CONFIG_DESC_PREFIX + key, description, CONFIG_EXPIRE);
            }
            
            // 触发变更监听器
            notifyConfigChange(key, oldValue, value);
            
            log.info("配置设置成功: key={}, value={}", key, value);
            return true;
        } catch (Exception e) {
            log.error("配置设置失败: key={}, value={}", key, value, e);
            return false;
        }
    }

    @Override
    public boolean deleteConfig(String key) {
        try {
            String oldValue = getConfig(key);
            
            // 删除配置值和描述
            cacheService.delete(CONFIG_PREFIX + key);
            cacheService.delete(CONFIG_DESC_PREFIX + key);
            
            // 触发变更监听器
            notifyConfigChange(key, oldValue, null);
            
            log.info("配置删除成功: key={}", key);
            return true;
        } catch (Exception e) {
            log.error("配置删除失败: key={}", key, e);
            return false;
        }
    }

    @Override
    public int deleteConfigs(List<String> keys) {
        int count = 0;
        for (String key : keys) {
            if (deleteConfig(key)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean hasConfig(String key) {
        return cacheService.hasKey(CONFIG_PREFIX + key);
    }

    @Override
    public Map<String, String> getConfigs(String prefix) {
        Map<String, String> configs = new HashMap<>();
        try {
            Set<String> keys = cacheService.keys(CONFIG_PREFIX + prefix + "*");
            for (String key : keys) {
                String configKey = key.substring(CONFIG_PREFIX.length());
                String value = cacheService.get(key, String.class);
                if (value != null) {
                    configs.put(configKey, value);
                }
            }
        } catch (Exception e) {
            log.error("获取配置列表失败: prefix={}", prefix, e);
        }
        return configs;
    }

    @Override
    public Map<String, String> getAllConfigs() {
        return getConfigs("");
    }

    @Override
    public void refreshCache() {
        // 基于缓存的实现，不需要特殊的刷新操作
        log.info("配置缓存刷新完成");
    }

    @Override
    public void clearCache() {
        try {
            Set<String> configKeys = cacheService.keys(CONFIG_PREFIX + "*");
            Set<String> descKeys = cacheService.keys(CONFIG_DESC_PREFIX + "*");
            
            if (!configKeys.isEmpty()) {
                cacheService.delete(configKeys);
            }
            if (!descKeys.isEmpty()) {
                cacheService.delete(descKeys);
            }
            
            log.info("配置缓存清空完成: configs={}, descriptions={}", configKeys.size(), descKeys.size());
        } catch (Exception e) {
            log.error("清空配置缓存失败", e);
        }
    }

    @Override
    public void addConfigListener(String key, ConfigChangeListener listener) {
        listeners.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(listener);
        log.debug("添加配置监听器: key={}", key);
    }

    @Override
    public void removeConfigListener(String key, ConfigChangeListener listener) {
        List<ConfigChangeListener> keyListeners = listeners.get(key);
        if (keyListeners != null) {
            keyListeners.remove(listener);
            if (keyListeners.isEmpty()) {
                listeners.remove(key);
            }
        }
        log.debug("移除配置监听器: key={}", key);
    }

    /**
     * 通知配置变更
     */
    private void notifyConfigChange(String key, String oldValue, String newValue) {
        List<ConfigChangeListener> keyListeners = listeners.get(key);
        if (keyListeners != null && !keyListeners.isEmpty()) {
            for (ConfigChangeListener listener : keyListeners) {
                try {
                    listener.onConfigChange(key, oldValue, newValue);
                } catch (Exception e) {
                    log.error("配置变更监听器执行失败: key={}", key, e);
                }
            }
        }
    }

    /**
     * 类型转换
     */
    @SuppressWarnings("unchecked")
    private <T> T convertValue(String value, Class<T> type) {
        if (value == null) {
            return null;
        }

        if (type == String.class) {
            return (T) value;
        }

        try {
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

            // 复杂类型使用JSON反序列化
            return JsonUtils.parseObject(value, type);
        } catch (Exception e) {
            log.error("配置值类型转换失败: value={}, type={}", value, type.getName(), e);
            return null;
        }
    }
}