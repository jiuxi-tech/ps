package com.jiuxi.common.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ConfigService
 * @Description: 配置管理服务接口
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface ConfigService {
    
    // ==================== 基础配置操作 ====================
    
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
     * @param key          配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    String getConfig(String key, String defaultValue);
    
    /**
     * 获取配置值（指定类型）
     *
     * @param key   配置键
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return 配置值
     */
    <T> T getConfig(String key, Class<T> clazz);
    
    /**
     * 获取配置值（指定类型，带默认值）
     *
     * @param key          配置键
     * @param clazz        目标类型
     * @param defaultValue 默认值
     * @param <T>          泛型类型
     * @return 配置值
     */
    <T> T getConfig(String key, Class<T> clazz, T defaultValue);
    
    /**
     * 设置配置值
     *
     * @param key   配置键
     * @param value 配置值
     * @return 是否设置成功
     */
    boolean setConfig(String key, String value);
    
    /**
     * 设置配置值（带描述）
     *
     * @param key         配置键
     * @param value       配置值
     * @param description 配置描述
     * @return 是否设置成功
     */
    boolean setConfig(String key, String value, String description);
    
    /**
     * 设置配置值（完整信息）
     *
     * @param key         配置键
     * @param value       配置值
     * @param description 配置描述
     * @param category    配置分类
     * @param isPublic    是否公开
     * @return 是否设置成功
     */
    boolean setConfig(String key, String value, String description, String category, boolean isPublic);
    
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
    boolean existsConfig(String key);
    
    // ==================== 配置查询 ====================
    
    /**
     * 获取所有配置
     *
     * @return 配置映射
     */
    Map<String, String> getAllConfigs();
    
    /**
     * 获取公开配置
     *
     * @return 公开配置映射
     */
    Map<String, String> getPublicConfigs();
    
    /**
     * 根据前缀获取配置
     *
     * @param prefix 配置键前缀
     * @return 配置映射
     */
    Map<String, String> getConfigsByPrefix(String prefix);
    
    /**
     * 根据分类获取配置
     *
     * @param category 配置分类
     * @return 配置映射
     */
    Map<String, String> getConfigsByCategory(String category);
    
    /**
     * 搜索配置
     *
     * @param keyword 关键词
     * @return 配置列表
     */
    List<Map<String, Object>> searchConfigs(String keyword);
    
    /**
     * 分页查询配置
     *
     * @param page     页码
     * @param size     每页大小
     * @param category 分类过滤
     * @param keyword  关键词过滤
     * @return 分页结果
     */
    Map<String, Object> getConfigsPage(int page, int size, String category, String keyword);
    
    // ==================== 配置分类管理 ====================
    
    /**
     * 获取所有配置分类
     *
     * @return 分类列表
     */
    List<String> getAllCategories();
    
    /**
     * 创建配置分类
     *
     * @param category    分类名称
     * @param description 分类描述
     * @return 是否创建成功
     */
    boolean createCategory(String category, String description);
    
    /**
     * 删除配置分类
     *
     * @param category 分类名称
     * @return 是否删除成功
     */
    boolean deleteCategory(String category);
    
    /**
     * 获取分类下的配置数量
     *
     * @param category 分类名称
     * @return 配置数量
     */
    long getConfigCountByCategory(String category);
    
    // ==================== 配置模板管理 ====================
    
    /**
     * 创建配置模板
     *
     * @param templateName 模板名称
     * @param description  模板描述
     * @param configs      配置项列表
     * @return 是否创建成功
     */
    boolean createConfigTemplate(String templateName, String description, List<Map<String, Object>> configs);
    
    /**
     * 应用配置模板
     *
     * @param templateName 模板名称
     * @param override     是否覆盖现有配置
     * @return 是否应用成功
     */
    boolean applyConfigTemplate(String templateName, boolean override);
    
    /**
     * 删除配置模板
     *
     * @param templateName 模板名称
     * @return 是否删除成功
     */
    boolean deleteConfigTemplate(String templateName);
    
    /**
     * 获取所有配置模板
     *
     * @return 模板列表
     */
    List<Map<String, Object>> getAllConfigTemplates();
    
    /**
     * 获取配置模板详情
     *
     * @param templateName 模板名称
     * @return 模板详情
     */
    Map<String, Object> getConfigTemplate(String templateName);
    
    // ==================== 配置版本管理 ====================
    
    /**
     * 创建配置快照
     *
     * @param snapshotName 快照名称
     * @param description  快照描述
     * @return 快照ID
     */
    String createConfigSnapshot(String snapshotName, String description);
    
    /**
     * 恢复配置快照
     *
     * @param snapshotId 快照ID
     * @return 是否恢复成功
     */
    boolean restoreConfigSnapshot(String snapshotId);
    
    /**
     * 删除配置快照
     *
     * @param snapshotId 快照ID
     * @return 是否删除成功
     */
    boolean deleteConfigSnapshot(String snapshotId);
    
    /**
     * 获取所有配置快照
     *
     * @return 快照列表
     */
    List<Map<String, Object>> getAllConfigSnapshots();
    
    /**
     * 比较配置快照
     *
     * @param snapshotId1 快照ID1
     * @param snapshotId2 快照ID2
     * @return 比较结果
     */
    Map<String, Object> compareConfigSnapshots(String snapshotId1, String snapshotId2);
    
    // ==================== 配置导入导出 ====================
    
    /**
     * 导出配置
     *
     * @param category 分类过滤（可选）
     * @param format   导出格式（json/yaml/properties）
     * @return 导出文件路径
     */
    String exportConfigs(String category, String format);
    
    /**
     * 导入配置
     *
     * @param filePath 文件路径
     * @param override 是否覆盖现有配置
     * @return 导入结果
     */
    Map<String, Object> importConfigs(String filePath, boolean override);
    
    /**
     * 验证配置文件
     *
     * @param filePath 文件路径
     * @return 验证结果
     */
    Map<String, Object> validateConfigFile(String filePath);
    
    // ==================== 配置监听 ====================
    
    /**
     * 添加配置变更监听器
     *
     * @param key      配置键（支持通配符）
     * @param listener 监听器
     */
    void addConfigListener(String key, ConfigChangeListener listener);
    
    /**
     * 移除配置变更监听器
     *
     * @param key      配置键
     * @param listener 监听器
     */
    void removeConfigListener(String key, ConfigChangeListener listener);
    
    /**
     * 刷新配置缓存
     */
    void refreshConfigCache();
    
    /**
     * 刷新指定配置
     *
     * @param key 配置键
     */
    void refreshConfig(String key);
    
    // ==================== 配置验证 ====================
    
    /**
     * 验证配置值
     *
     * @param key   配置键
     * @param value 配置值
     * @return 验证结果
     */
    Map<String, Object> validateConfig(String key, String value);
    
    /**
     * 批量验证配置
     *
     * @param configs 配置映射
     * @return 验证结果
     */
    Map<String, Object> validateConfigs(Map<String, String> configs);
    
    /**
     * 设置配置验证规则
     *
     * @param key  配置键
     * @param rule 验证规则
     * @return 是否设置成功
     */
    boolean setConfigValidationRule(String key, Map<String, Object> rule);
    
    /**
     * 获取配置验证规则
     *
     * @param key 配置键
     * @return 验证规则
     */
    Map<String, Object> getConfigValidationRule(String key);
    
    // ==================== 配置统计 ====================
    
    /**
     * 获取配置统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getConfigStatistics();
    
    /**
     * 获取配置变更历史
     *
     * @param key       配置键
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page      页码
     * @param size      每页大小
     * @return 变更历史
     */
    Map<String, Object> getConfigChangeHistory(String key, String startTime, String endTime, int page, int size);
    
    /**
     * 获取热门配置
     *
     * @param limit 数量限制
     * @return 热门配置列表
     */
    List<Map<String, Object>> getPopularConfigs(int limit);
    
    /**
     * 获取配置使用情况
     *
     * @param key 配置键
     * @return 使用情况
     */
    Map<String, Object> getConfigUsage(String key);
    
    // ==================== 配置安全 ====================
    
    /**
     * 加密配置值
     *
     * @param key   配置键
     * @param value 配置值
     * @return 是否加密成功
     */
    boolean encryptConfig(String key, String value);
    
    /**
     * 解密配置值
     *
     * @param key 配置键
     * @return 解密后的值
     */
    String decryptConfig(String key);
    
    /**
     * 设置配置访问权限
     *
     * @param key         配置键
     * @param permissions 权限列表
     * @return 是否设置成功
     */
    boolean setConfigPermissions(String key, List<String> permissions);
    
    /**
     * 检查配置访问权限
     *
     * @param key    配置键
     * @param userId 用户ID
     * @return 是否有权限
     */
    boolean checkConfigPermission(String key, String userId);
    
    /**
     * 配置变更监听器接口
     */
    interface ConfigChangeListener {
        /**
         * 配置变更回调
         *
         * @param key      配置键
         * @param oldValue 旧值
         * @param newValue 新值
         */
        void onConfigChanged(String key, String oldValue, String newValue);
    }
}