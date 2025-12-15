package com.jiuxi.common.service.impl;

import com.jiuxi.common.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @ClassName: ConfigServiceImpl
 * @Description: 配置管理服务实现类
 * @Author: Qdd
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;
    
    // 内存存储（生产环境应使用数据库）
    private final Map<String, ConfigItem> configs = new ConcurrentHashMap<>();
    private final Map<String, String> categories = new ConcurrentHashMap<>();
    private final Map<String, List<Map<String, Object>>> templates = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> snapshots = new ConcurrentHashMap<>();
    private final Map<String, List<ConfigChangeListener>> listeners = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> validationRules = new ConcurrentHashMap<>();
    private final Map<String, List<Map<String, Object>>> changeHistory = new ConcurrentHashMap<>();
    private final Map<String, Long> configUsage = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        // 初始化默认配置分类
        categories.put("system", "系统配置");
        categories.put("database", "数据库配置");
        categories.put("cache", "缓存配置");
        categories.put("security", "安全配置");
        categories.put("notification", "通知配置");
        categories.put("file", "文件配置");
        categories.put("integration", "集成配置");
        
        // 初始化默认配置
        initDefaultConfigs();
    }
    
    private void initDefaultConfigs() {
        setConfig("system.name", "PS-BMP系统", "系统名称", "system", true);
        setConfig("system.version", "1.0.0", "系统版本", "system", true);
        setConfig("system.maintenance", "false", "维护模式", "system", false);
        setConfig("cache.enabled", "true", "缓存启用状态", "cache", false);
        setConfig("cache.ttl", "3600", "缓存过期时间（秒）", "cache", false);
        setConfig("security.session.timeout", "1800", "会话超时时间（秒）", "security", false);
        setConfig("notification.email.enabled", "true", "邮件通知启用", "notification", false);
        setConfig("file.upload.maxSize", "10485760", "文件上传最大大小（字节）", "file", false);
    }
    
    // ==================== 基础配置操作 ====================
    
    @Override
    public String getConfig(String key) {
        recordConfigUsage(key);
        ConfigItem item = configs.get(key);
        return item != null ? item.getValue() : null;
    }
    
    @Override
    public String getConfig(String key, String defaultValue) {
        String value = getConfig(key);
        return value != null ? value : defaultValue;
    }
    
    @Override
    public <T> T getConfig(String key, Class<T> clazz) {
        String value = getConfig(key);
        if (value == null) {
            return null;
        }
        return convertValue(value, clazz);
    }
    
    @Override
    public <T> T getConfig(String key, Class<T> clazz, T defaultValue) {
        T value = getConfig(key, clazz);
        return value != null ? value : defaultValue;
    }
    
    @Override
    public boolean setConfig(String key, String value) {
        return setConfig(key, value, null, "default", false);
    }
    
    @Override
    public boolean setConfig(String key, String value, String description) {
        return setConfig(key, value, description, "default", false);
    }
    
    @Override
    public boolean setConfig(String key, String value, String description, String category, boolean isPublic) {
        try {
            String oldValue = getConfig(key);
            ConfigItem item = new ConfigItem(key, value, description, category, isPublic);
            configs.put(key, item);
            
            // 记录变更历史
            recordConfigChange(key, oldValue, value);
            
            // 触发监听器
            notifyConfigChange(key, oldValue, value);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean deleteConfig(String key) {
        try {
            String oldValue = getConfig(key);
            configs.remove(key);
            
            // 记录变更历史
            recordConfigChange(key, oldValue, null);
            
            // 触发监听器
            notifyConfigChange(key, oldValue, null);
            
            return true;
        } catch (Exception e) {
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
    public boolean existsConfig(String key) {
        return configs.containsKey(key);
    }
    
    // ==================== 配置查询 ====================
    
    @Override
    public Map<String, String> getAllConfigs() {
        return configs.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getValue()
                ));
    }
    
    @Override
    public Map<String, String> getPublicConfigs() {
        return configs.entrySet().stream()
                .filter(entry -> entry.getValue().isPublic())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getValue()
                ));
    }
    
    @Override
    public Map<String, String> getConfigsByPrefix(String prefix) {
        return configs.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getValue()
                ));
    }
    
    @Override
    public Map<String, String> getConfigsByCategory(String category) {
        return configs.entrySet().stream()
                .filter(entry -> category.equals(entry.getValue().getCategory()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getValue()
                ));
    }
    
    @Override
    public List<Map<String, Object>> searchConfigs(String keyword) {
        return configs.entrySet().stream()
                .filter(entry -> entry.getKey().contains(keyword) || 
                        (entry.getValue().getDescription() != null && entry.getValue().getDescription().contains(keyword)))
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    ConfigItem item = entry.getValue();
                    result.put("key", entry.getKey());
                    result.put("value", item.getValue());
                    result.put("description", item.getDescription());
                    result.put("category", item.getCategory());
                    result.put("isPublic", item.isPublic());
                    result.put("createTime", item.getCreateTime());
                    result.put("updateTime", item.getUpdateTime());
                    return result;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> getConfigsPage(int page, int size, String category, String keyword) {
        List<Map<String, Object>> allConfigs = configs.entrySet().stream()
                .filter(entry -> {
                    boolean categoryMatch = category == null || category.equals(entry.getValue().getCategory());
                    boolean keywordMatch = keyword == null || entry.getKey().contains(keyword) ||
                            (entry.getValue().getDescription() != null && entry.getValue().getDescription().contains(keyword));
                    return categoryMatch && keywordMatch;
                })
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    ConfigItem item = entry.getValue();
                    result.put("key", entry.getKey());
                    result.put("value", item.getValue());
                    result.put("description", item.getDescription());
                    result.put("category", item.getCategory());
                    result.put("isPublic", item.isPublic());
                    result.put("createTime", item.getCreateTime());
                    result.put("updateTime", item.getUpdateTime());
                    return result;
                })
                .collect(Collectors.toList());
        
        int total = allConfigs.size();
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);
        
        List<Map<String, Object>> records = start < total ? allConfigs.subList(start, end) : new ArrayList<>();
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", total);
        result.put("current", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);
        
        return result;
    }
    
    // ==================== 配置分类管理 ====================
    
    @Override
    public List<String> getAllCategories() {
        return new ArrayList<>(categories.keySet());
    }
    
    @Override
    public boolean createCategory(String category, String description) {
        categories.put(category, description);
        return true;
    }
    
    @Override
    public boolean deleteCategory(String category) {
        categories.remove(category);
        return true;
    }
    
    @Override
    public long getConfigCountByCategory(String category) {
        return configs.values().stream()
                .filter(item -> category.equals(item.getCategory()))
                .count();
    }
    
    // ==================== 配置模板管理 ====================
    
    @Override
    public boolean createConfigTemplate(String templateName, String description, List<Map<String, Object>> configs) {
        templates.put(templateName, configs);
        return true;
    }
    
    @Override
    public boolean applyConfigTemplate(String templateName, boolean override) {
        List<Map<String, Object>> templateConfigs = templates.get(templateName);
        if (templateConfigs == null) {
            return false;
        }
        
        for (Map<String, Object> config : templateConfigs) {
            String key = (String) config.get("key");
            String value = (String) config.get("value");
            String description = (String) config.get("description");
            String category = (String) config.get("category");
            Boolean isPublic = (Boolean) config.get("isPublic");
            
            if (override || !existsConfig(key)) {
                setConfig(key, value, description, category, isPublic != null ? isPublic : false);
            }
        }
        
        return true;
    }
    
    @Override
    public boolean deleteConfigTemplate(String templateName) {
        templates.remove(templateName);
        return true;
    }
    
    @Override
    public List<Map<String, Object>> getAllConfigTemplates() {
        return templates.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> template = new HashMap<>();
                    template.put("name", entry.getKey());
                    template.put("configCount", entry.getValue().size());
                    return template;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> getConfigTemplate(String templateName) {
        List<Map<String, Object>> configs = templates.get(templateName);
        if (configs == null) {
            return null;
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("name", templateName);
        result.put("configs", configs);
        return result;
    }
    
    // ==================== 配置版本管理 ====================
    
    @Override
    public String createConfigSnapshot(String snapshotName, String description) {
        String snapshotId = UUID.randomUUID().toString();
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("id", snapshotId);
        snapshot.put("name", snapshotName);
        snapshot.put("description", description);
        snapshot.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        snapshot.put("configs", new HashMap<>(getAllConfigs()));
        
        snapshots.put(snapshotId, snapshot);
        return snapshotId;
    }
    
    @Override
    public boolean restoreConfigSnapshot(String snapshotId) {
        Map<String, Object> snapshot = snapshots.get(snapshotId);
        if (snapshot == null) {
            return false;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, String> snapshotConfigs = (Map<String, String>) snapshot.get("configs");
        
        // 清空现有配置
        configs.clear();
        
        // 恢复配置
        for (Map.Entry<String, String> entry : snapshotConfigs.entrySet()) {
            setConfig(entry.getKey(), entry.getValue());
        }
        
        return true;
    }
    
    @Override
    public boolean deleteConfigSnapshot(String snapshotId) {
        snapshots.remove(snapshotId);
        return true;
    }
    
    @Override
    public List<Map<String, Object>> getAllConfigSnapshots() {
        return snapshots.values().stream()
                .map(snapshot -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", snapshot.get("id"));
                    result.put("name", snapshot.get("name"));
                    result.put("description", snapshot.get("description"));
                    result.put("createTime", snapshot.get("createTime"));
                    @SuppressWarnings("unchecked")
                    Map<String, String> configs = (Map<String, String>) snapshot.get("configs");
                    result.put("configCount", configs.size());
                    return result;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> compareConfigSnapshots(String snapshotId1, String snapshotId2) {
        Map<String, Object> snapshot1 = snapshots.get(snapshotId1);
        Map<String, Object> snapshot2 = snapshots.get(snapshotId2);
        
        if (snapshot1 == null || snapshot2 == null) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, String> configs1 = (Map<String, String>) snapshot1.get("configs");
        @SuppressWarnings("unchecked")
        Map<String, String> configs2 = (Map<String, String>) snapshot2.get("configs");
        
        Map<String, Object> result = new HashMap<>();
        result.put("added", new ArrayList<>());
        result.put("removed", new ArrayList<>());
        result.put("modified", new ArrayList<>());
        
        // 实现比较逻辑（简化版）
        return result;
    }
    
    // ==================== 配置导入导出 ====================
    
    @Override
    public String exportConfigs(String category, String format) {
        // 简化实现，返回模拟文件路径
        return "/tmp/configs_export_" + System.currentTimeMillis() + "." + format;
    }
    
    @Override
    public Map<String, Object> importConfigs(String filePath, boolean override) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("imported", 0);
        result.put("skipped", 0);
        result.put("errors", new ArrayList<>());
        return result;
    }
    
    @Override
    public Map<String, Object> validateConfigFile(String filePath) {
        Map<String, Object> result = new HashMap<>();
        result.put("valid", true);
        result.put("errors", new ArrayList<>());
        result.put("warnings", new ArrayList<>());
        return result;
    }
    
    // ==================== 配置监听 ====================
    
    @Override
    public void addConfigListener(String key, ConfigChangeListener listener) {
        listeners.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(listener);
    }
    
    @Override
    public void removeConfigListener(String key, ConfigChangeListener listener) {
        List<ConfigChangeListener> keyListeners = listeners.get(key);
        if (keyListeners != null) {
            keyListeners.remove(listener);
        }
    }
    
    @Override
    public void refreshConfigCache() {
        // 刷新缓存逻辑
        if (redisTemplate != null) {
            // 清除Redis缓存
            Set<String> keys = redisTemplate.keys("config:*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
    }
    
    @Override
    public void refreshConfig(String key) {
        if (redisTemplate != null) {
            redisTemplate.delete("config:" + key);
        }
    }
    
    // ==================== 配置验证 ====================
    
    @Override
    public Map<String, Object> validateConfig(String key, String value) {
        Map<String, Object> result = new HashMap<>();
        result.put("valid", true);
        result.put("errors", new ArrayList<>());
        
        Map<String, Object> rule = validationRules.get(key);
        if (rule != null) {
            // 实现验证逻辑
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> validateConfigs(Map<String, String> configs) {
        Map<String, Object> result = new HashMap<>();
        result.put("valid", true);
        result.put("errors", new HashMap<>());
        
        for (Map.Entry<String, String> entry : configs.entrySet()) {
            Map<String, Object> validation = validateConfig(entry.getKey(), entry.getValue());
            if (!(Boolean) validation.get("valid")) {
                result.put("valid", false);
                @SuppressWarnings("unchecked")
                Map<String, Object> errors = (Map<String, Object>) result.get("errors");
                errors.put(entry.getKey(), validation.get("errors"));
            }
        }
        
        return result;
    }
    
    @Override
    public boolean setConfigValidationRule(String key, Map<String, Object> rule) {
        validationRules.put(key, rule);
        return true;
    }
    
    @Override
    public Map<String, Object> getConfigValidationRule(String key) {
        return validationRules.get(key);
    }
    
    // ==================== 配置统计 ====================
    
    @Override
    public Map<String, Object> getConfigStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalConfigs", configs.size());
        stats.put("publicConfigs", configs.values().stream().mapToLong(item -> item.isPublic() ? 1 : 0).sum());
        stats.put("categories", categories.size());
        stats.put("templates", templates.size());
        stats.put("snapshots", snapshots.size());
        
        Map<String, Long> categoryStats = configs.values().stream()
                .collect(Collectors.groupingBy(ConfigItem::getCategory, Collectors.counting()));
        stats.put("categoryStats", categoryStats);
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getConfigChangeHistory(String key, String startTime, String endTime, int page, int size) {
        List<Map<String, Object>> history = changeHistory.getOrDefault(key, new ArrayList<>());
        
        int total = history.size();
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);
        
        List<Map<String, Object>> records = start < total ? history.subList(start, end) : new ArrayList<>();
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", total);
        result.put("current", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);
        
        return result;
    }
    
    @Override
    public List<Map<String, Object>> getPopularConfigs(int limit) {
        return configUsage.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    Map<String, Object> config = new HashMap<>();
                    config.put("key", entry.getKey());
                    config.put("usage", entry.getValue());
                    ConfigItem item = configs.get(entry.getKey());
                    if (item != null) {
                        config.put("value", item.getValue());
                        config.put("description", item.getDescription());
                    }
                    return config;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> getConfigUsage(String key) {
        Map<String, Object> usage = new HashMap<>();
        usage.put("key", key);
        usage.put("accessCount", configUsage.getOrDefault(key, 0L));
        usage.put("lastAccess", "N/A"); // 简化实现
        return usage;
    }
    
    // ==================== 配置安全 ====================
    
    @Override
    public boolean encryptConfig(String key, String value) {
        // 简化实现，实际应使用加密算法
        return setConfig(key, "[ENCRYPTED]" + value);
    }
    
    @Override
    public String decryptConfig(String key) {
        String value = getConfig(key);
        if (value != null && value.startsWith("[ENCRYPTED]")) {
            return value.substring(11); // 简化实现
        }
        return value;
    }
    
    @Override
    public boolean setConfigPermissions(String key, List<String> permissions) {
        // 简化实现
        return true;
    }
    
    @Override
    public boolean checkConfigPermission(String key, String userId) {
        // 简化实现，默认有权限
        return true;
    }
    
    // ==================== 私有辅助方法 ====================
    
    @SuppressWarnings("unchecked")
    private <T> T convertValue(String value, Class<T> clazz) {
        if (clazz == String.class) {
            return (T) value;
        } else if (clazz == Integer.class || clazz == int.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == Long.class || clazz == long.class) {
            return (T) Long.valueOf(value);
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            return (T) Boolean.valueOf(value);
        } else if (clazz == Double.class || clazz == double.class) {
            return (T) Double.valueOf(value);
        }
        return null;
    }
    
    private void recordConfigUsage(String key) {
        configUsage.merge(key, 1L, Long::sum);
    }
    
    private void recordConfigChange(String key, String oldValue, String newValue) {
        Map<String, Object> change = new HashMap<>();
        change.put("key", key);
        change.put("oldValue", oldValue);
        change.put("newValue", newValue);
        change.put("changeTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        change.put("operator", "system"); // 实际应获取当前用户
        
        changeHistory.computeIfAbsent(key, k -> new ArrayList<>()).add(change);
    }
    
    private void notifyConfigChange(String key, String oldValue, String newValue) {
        List<ConfigChangeListener> keyListeners = listeners.get(key);
        if (keyListeners != null) {
            for (ConfigChangeListener listener : keyListeners) {
                try {
                    listener.onConfigChanged(key, oldValue, newValue);
                } catch (Exception e) {
                    // 记录日志
                }
            }
        }
        
        // 通配符监听器
        List<ConfigChangeListener> wildcardListeners = listeners.get("*");
        if (wildcardListeners != null) {
            for (ConfigChangeListener listener : wildcardListeners) {
                try {
                    listener.onConfigChanged(key, oldValue, newValue);
                } catch (Exception e) {
                    // 记录日志
                }
            }
        }
    }
    
    /**
     * 配置项内部类
     */
    private static class ConfigItem {
        private String key;
        private String value;
        private String description;
        private String category;
        private boolean isPublic;
        private String createTime;
        private String updateTime;
        
        public ConfigItem(String key, String value, String description, String category, boolean isPublic) {
            this.key = key;
            this.value = value;
            this.description = description;
            this.category = category;
            this.isPublic = isPublic;
            this.createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            this.updateTime = this.createTime;
        }
        
        // Getters and Setters
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        
        public String getValue() { return value; }
        public void setValue(String value) {
            this.value = value;
            this.updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public boolean isPublic() { return isPublic; }
        public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
        
        public String getCreateTime() { return createTime; }
        public void setCreateTime(String createTime) { this.createTime = createTime; }
        
        public String getUpdateTime() { return updateTime; }
        public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
    }
}