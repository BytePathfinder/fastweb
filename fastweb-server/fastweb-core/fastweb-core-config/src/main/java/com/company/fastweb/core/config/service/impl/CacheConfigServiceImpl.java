package com.company.fastweb.core.config.service.impl;

import com.company.fastweb.core.cache.service.CacheService;
import com.company.fastweb.core.common.util.JsonUtils;
import com.company.fastweb.core.config.service.ConfigItem;
import com.company.fastweb.core.config.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
    private static final String CONFIG_LIST_KEY = "config:list";
    
    // 配置变更监听器
    private final Map<String, List<ConfigChangeListener>> listeners = new ConcurrentHashMap<>();

    @Override
    public String getConfig(String key) {
        return getConfig(key, null);
    }

    @Override
    public String getConfig(String key, String defaultValue) {
        try {
            ConfigItem item = cacheService.get(CONFIG_PREFIX + key, ConfigItem.class);
            return item != null ? item.getValue() : defaultValue;
        } catch (Exception e) {
            log.error("获取配置失败: key={}", key, e);
            return defaultValue;
        }
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
            
            // 创建配置项
            ConfigItem item = ConfigItem.builder()
                .key(key)
                .value(value)
                .description(description)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .creator("system")
                .updater("system")
                .build();
            
            // 保存到缓存
            cacheService.set(CONFIG_PREFIX + key, item);
            
            // 更新配置列表
            updateConfigList(key);
            
            // 触发变更监听器
            notifyConfigChange(key, oldValue, value);
            
            log.info("配置设置成功: key={}, value={}", key, value);
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
            boolean deleted = cacheService.delete(CONFIG_PREFIX + key);
            
            if (deleted) {
                // 从配置列表中移除
                removeFromConfigList(key);
                
                // 触发变更监听器
                notifyConfigChange(key, oldValue, null);
                
                log.info("配置删除成功: key={}", key);
            }
            
            return deleted;
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
    public boolean exists(String key) {
        return cacheService.hasKey(CONFIG_PREFIX + key);
    }

    @Override
    public List<ConfigItem> getConfigList(String prefix) {
        List<ConfigItem> result = new ArrayList<>();
        try {
            Set<String> keys = cacheService.sMembers(CONFIG_LIST_KEY, String.class);
            for (String key : keys) {
                if (prefix == null || key.startsWith(prefix)) {
                    ConfigItem item = cacheService.get(CONFIG_PREFIX + key, ConfigItem.class);
                    if (item != null) {
                        result.add(item);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取配置列表失败: prefix={}", prefix, e);
        }
        return result;
    }

    @Override
    public Map<String, String> getAllConfigs() {
        Map<String, String> result = new HashMap<>();
        try {
            Set<String> keys = cacheService.sMembers(CONFIG_LIST_KEY, String.class);
            for (String key : keys) {
                String value = getConfig(key);
                if (value != null) {
                    result.put(key, value);
                }
            }
        } catch (Exception e) {
            log.error("获取所有配置失败", e);
        }
        return result;
    }

    @Override
    public void refreshCache() {
        // 基于缓存的实现，无需刷新
        log.info("配置缓存刷新完成");
    }

    @Override
    public void clearCache() {
        try {
            Set<String> keys = cacheService.sMembers(CONFIG_LIST_KEY, String.class);
            List<String> cacheKeys = new ArrayList<>();
            for (String key : keys) {
                cacheKeys.add(CONFIG_PREFIX + key);
            }
            cacheService.delete(cacheKeys);
            cacheService.delete(CONFIG_LIST_KEY);
            
            log.info("配置缓存清空完成");
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
     * 更新配置列表
     */
    private void updateConfigList(String key) {
        cacheService.sAdd(CONFIG_LIST_KEY, key);
    }

    /**
     * 从配置列表中移除
     */
    private void removeFromConfigList(String key) {
        cacheService.sRemove(CONFIG_LIST_KEY, key);
    }

    /**
     * 通知配置变更
     */
    private void notifyConfigChange(String key, String oldValue, String newValue) {
        List<ConfigChangeListener> keyListeners = listeners.get(key);
        if (keyListeners != null) {
            for (ConfigChangeListener listener : keyListeners) {
                try {
                    listener.onChange(key, oldValue, newValue);
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