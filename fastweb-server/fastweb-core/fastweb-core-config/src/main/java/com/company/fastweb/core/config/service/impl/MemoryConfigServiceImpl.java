package com.company.fastweb.core.config.service.impl;

import com.company.fastweb.core.common.util.JsonUtils;
import com.company.fastweb.core.config.service.ConfigItem;
import com.company.fastweb.core.config.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 基于内存的配置服务实现
 *
 * @author FastWeb
 */
@Slf4j
@Service
public class MemoryConfigServiceImpl implements ConfigService {

    // 配置存储
    private final Map<String, ConfigItem> configs = new ConcurrentHashMap<>();
    
    // 配置变更监听器
    private final Map<String, List<ConfigChangeListener>> listeners = new ConcurrentHashMap<>();

    @Override
    public String getConfig(String key) {
        ConfigItem item = configs.get(key);
        return item != null ? item.getValue() : null;
    }

    @Override
    public String getConfig(String key, String defaultValue) {
        ConfigItem item = configs.get(key);
        return item != null ? item.getValue() : defaultValue;
    }

    @Override
    public <T> T getConfig(String key, Class<T> type) {
        return getConfig(key, type, null);
    }

    @Override
    public <T> T getConfig(String key, Class<T> type, T defaultValue) {
        String value = getConfig(key);
        if (value == null) {
            return defaultValue;
        }
        
        try {
            return convertValue(value, type);
        } catch (Exception e) {
            log.error("配置值类型转换失败: key={}, value={}, type={}", key, value, type.getSimpleName(), e);
            return defaultValue;
        }
    }

    @Override
    public boolean setConfig(String key, String value) {
        return setConfig(key, value, null);
    }

    @Override
    public boolean setConfig(String key, String value, String description) {
        try {
            // 获取旧值
            String oldValue = getConfig(key);
            
            // 创建或更新配置项
            ConfigItem item = configs.get(key);
            if (item == null) {
                item = ConfigItem.builder()
                    .key(key)
                    .value(value)
                    .description(description)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .creator("system")
                    .updater("system")
                    .build();
            } else {
                item.setValue(value);
                item.setDescription(description);
                item.setUpdateTime(LocalDateTime.now());
                item.setUpdater("system");
            }
            
            configs.put(key, item);
            
            // 触发变更监听器
            notifyConfigChange(key, oldValue, value);
            
            log.debug("配置设置成功: key={}, value={}", key, value);
            return true;
            
        } catch (Exception e) {
            log.error("设置配置失败: key={}, value={}", key, value, e);
            return false;
        }
    }

    @Override
    public boolean deleteConfig(String key) {
        try {
            String oldValue = getConfig(key);
            ConfigItem removed = configs.remove(key);
            
            if (removed != null) {
                // 触发变更监听器
                notifyConfigChange(key, oldValue, null);
                log.debug("配置删除成功: key={}", key);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            log.error("删除配置失败: key={}", key, e);
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
        return configs.containsKey(key);
    }

    @Override
    public Map<String, String> getConfigs(String prefix) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, ConfigItem> entry : configs.entrySet()) {
            String key = entry.getKey();
            if (prefix == null || key.startsWith(prefix)) {
                result.put(key, entry.getValue().getValue());
            }
        }
        return result;
    }

    @Override
    public Map<String, String> getAllConfigs() {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, ConfigItem> entry : configs.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getValue());
        }
        return result;
    }

    @Override
    public void refreshCache() {
        // 内存实现无需刷新
        log.debug("配置缓存刷新完成");
    }

    @Override
    public void clearCache() {
        configs.clear();
        log.info("配置缓存清空完成");
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
        if (keyListeners != null) {
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
        
        // 尝试JSON反序列化
        return JsonUtils.parseObject(value, type);
    }
}