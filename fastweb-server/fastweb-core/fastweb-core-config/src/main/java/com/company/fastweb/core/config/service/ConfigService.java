package com.company.fastweb.core.config.service;

import java.util.List;
import java.util.Map;

/**
 * 配置服务接口
 *
 * @author FastWeb
 */
public interface ConfigService {

    /**
     * 获取配置值
     *
     * @param key 配置键
     * @return 配置值
     */
    String getConfig(String key);

    /**
     * 获取配置值（带默认值）
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    String getConfig(String key, String defaultValue);

    /**
     * 获取配置值并转换类型
     *
     * @param key 配置键
     * @param type 目标类型
     * @param <T> 泛型
     * @return 配置值
     */
    <T> T getConfig(String key, Class<T> type);

    /**
     * 获取配置值并转换类型（带默认值）
     *
     * @param key 配置键
     * @param type 目标类型
     * @param defaultValue 默认值
     * @param <T> 泛型
     * @return 配置值
     */
    <T> T getConfig(String key, Class<T> type, T defaultValue);

    /**
     * 设置配置值
     *
     * @param key 配置键
     * @param value 配置值
     * @return 是否设置成功
     */
    boolean setConfig(String key, String value);

    /**
     * 设置配置值（带描述）
     *
     * @param key 配置键
     * @param value 配置值
     * @param description 描述
     * @return 是否设置成功
     */
    boolean setConfig(String key, String value, String description);

    /**
     * 删除配置
     *
     * @param key 配置键
     * @return 是否删除成功
     */
    boolean deleteConfig(String key);

    /**
     * 批量删除配置
     *
     * @param keys 配置键列表
     * @return 删除成功的数量
     */
    int deleteConfigs(List<String> keys);

    /**
     * 检查配置是否存在
     *
     * @param key 配置键
     * @return 是否存在
     */
    boolean exists(String key);

    /**
     * 获取配置列表
     *
     * @param prefix 前缀
     * @return 配置列表
     */
    List<ConfigItem> getConfigList(String prefix);

    /**
     * 获取所有配置
     *
     * @return 配置映射
     */
    Map<String, String> getAllConfigs();

    /**
     * 刷新配置缓存
     */
    void refreshCache();

    /**
     * 清空配置缓存
     */
    void clearCache();

    /**
     * 添加配置变更监听器
     *
     * @param key 配置键
     * @param listener 监听器
     */
    void addConfigListener(String key, ConfigChangeListener listener);

    /**
     * 移除配置变更监听器
     *
     * @param key 配置键
     * @param listener 监听器
     */
    void removeConfigListener(String key, ConfigChangeListener listener);

    /**
     * 配置变更监听器
     */
    @FunctionalInterface
    interface ConfigChangeListener {
        /**
         * 配置变更回调
         *
         * @param key 配置键
         * @param oldValue 旧值
         * @param newValue 新值
         */
        void onChange(String key, String oldValue, String newValue);
    }
}