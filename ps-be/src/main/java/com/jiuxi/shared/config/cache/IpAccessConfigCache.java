package com.jiuxi.shared.config.cache;

import com.jiuxi.admin.core.util.IpAccessControlUtil;
import com.jiuxi.admin.core.service.TpSystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * IP访问控制配置缓存管理器
 * 提供配置的缓存、更新和查询功能
 * 
 * @author ps-bmp
 * @since 2024-01-20
 */
@Component
public class IpAccessConfigCache {
    
    private static final Logger log = LoggerFactory.getLogger(IpAccessConfigCache.class);

    @Autowired
    private TpSystemConfigService tpSystemConfigService;

    // 配置缓存
    private final ConcurrentHashMap<String, String> configCache = new ConcurrentHashMap<>();
    
    // 解析后的IP规则缓存
    private volatile List<String> whitelistRules;
    private volatile List<String> blacklistRules;
    
    // 读写锁，保证配置更新的线程安全
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    // 配置键常量
    private static final String IP_ENABLED_KEY = "security.ip.enabled";
    private static final String IP_WHITELIST_KEY = "security.ip.whitelist";
    private static final String IP_BLACKLIST_KEY = "security.ip.blacklist";
    private static final String IP_LOG_ENABLED_KEY = "security.ip.log.enabled";
    private static final String IP_DENY_MESSAGE_KEY = "security.ip.deny.message";
    
    /**
     * 初始化配置缓存
     */
    @PostConstruct
    public void initCache() {
        try {
            refreshAllConfig();
            log.info("IP访问控制配置缓存初始化完成");
        } catch (Exception e) {
            log.error("IP访问控制配置缓存初始化失败", e);
        }
    }
    
    /**
     * 刷新所有配置
     */
    public void refreshAllConfig() {
        lock.writeLock().lock();
        try {
            // 刷新基础配置
            refreshConfig(IP_ENABLED_KEY);
            refreshConfig(IP_LOG_ENABLED_KEY);
            refreshConfig(IP_DENY_MESSAGE_KEY);
            
            // 刷新IP规则配置
            refreshConfig(IP_WHITELIST_KEY);
            refreshConfig(IP_BLACKLIST_KEY);
            
            // 解析IP规则
            parseIpRules();
            
            log.info("IP访问控制配置缓存刷新完成");
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * 刷新单个配置项
     */
    public void refreshConfig(String configKey) {
        try {
            String configValue = tpSystemConfigService.getConfigValue(configKey);
            if (configValue != null) {
                configCache.put(configKey, configValue);
            } else {
                configCache.remove(configKey);
            }
            
            // 如果是IP规则配置，需要重新解析
            if (IP_WHITELIST_KEY.equals(configKey) || IP_BLACKLIST_KEY.equals(configKey)) {
                lock.writeLock().lock();
                try {
                    parseIpRules();
                } finally {
                    lock.writeLock().unlock();
                }
            }
            
            log.debug("配置项 {} 缓存已更新: {}", configKey, configValue);
        } catch (Exception e) {
            log.error("刷新配置项 {} 失败", configKey, e);
        }
    }
    
    /**
     * 解析IP规则
     */
    private void parseIpRules() {
        // 解析白名单
        String whitelistConfig = configCache.get(IP_WHITELIST_KEY);
        if (whitelistConfig != null && !whitelistConfig.trim().isEmpty()) {
            whitelistRules = Arrays.stream(whitelistConfig.split("\\n"))
                    .map(String::trim)
                    .filter(rule -> !rule.isEmpty())
                    .collect(Collectors.toList());
        } else {
            whitelistRules = Collections.emptyList();
        }
        
        // 解析黑名单
        String blacklistConfig = configCache.get(IP_BLACKLIST_KEY);
        if (blacklistConfig != null && !blacklistConfig.trim().isEmpty()) {
            blacklistRules = Arrays.stream(blacklistConfig.split("\\n"))
                    .map(String::trim)
                    .filter(rule -> !rule.isEmpty())
                    .collect(Collectors.toList());
        } else {
            blacklistRules = Collections.emptyList();
        }
        
        log.debug("IP规则解析完成 - 白名单: {} 条, 黑名单: {} 条", 
                whitelistRules.size(), blacklistRules.size());
    }
    
    /**
     * 获取配置值
     */
    public String getConfigValue(String configKey) {
        lock.readLock().lock();
        try {
            return configCache.get(configKey);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * 获取配置值（带默认值）
     */
    public String getConfigValue(String configKey, String defaultValue) {
        String value = getConfigValue(configKey);
        return value != null ? value : defaultValue;
    }
    
    /**
     * 获取布尔配置值
     */
    public boolean getBooleanConfig(String configKey, boolean defaultValue) {
        String value = getConfigValue(configKey);
        if (value == null) {
            return defaultValue;
        }
        return "true".equalsIgnoreCase(value) || "1".equals(value);
    }
    
    /**
     * 检查IP访问控制是否启用
     */
    public boolean isIpAccessControlEnabled() {
        return getBooleanConfig(IP_ENABLED_KEY, false);
    }
    
    /**
     * 检查IP访问日志是否启用
     */
    public boolean isIpAccessLogEnabled() {
        return getBooleanConfig(IP_LOG_ENABLED_KEY, true);
    }
    
    /**
     * 获取IP拒绝访问消息
     */
    public String getIpDenyMessage() {
        return getConfigValue(IP_DENY_MESSAGE_KEY, "您的IP地址无权访问此系统");
    }
    
    /**
     * 获取白名单规则
     */
    public List<String> getWhitelistRules() {
        lock.readLock().lock();
        try {
            return whitelistRules;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * 获取黑名单规则
     */
    public List<String> getBlacklistRules() {
        lock.readLock().lock();
        try {
            return blacklistRules;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * 检查IP是否被允许访问
     */
    public boolean isIpAllowed(String clientIp) {
        if (!isIpAccessControlEnabled()) {
            return true;
        }
        
        lock.readLock().lock();
        try {
            // 获取原始配置值
            String whitelistConfig = configCache.get(IP_WHITELIST_KEY);
            String blacklistConfig = configCache.get(IP_BLACKLIST_KEY);
            
            // 先检查黑名单（优先级更高）
            if (blacklistConfig != null && !blacklistConfig.trim().isEmpty()) {
                if (IpAccessControlUtil.isIpMatched(clientIp, blacklistConfig)) {
                    return false;
                }
            }
            
            // 再检查白名单
            if (whitelistConfig != null && !whitelistConfig.trim().isEmpty()) {
                return IpAccessControlUtil.isIpMatched(clientIp, whitelistConfig);
            }
            
            // 如果没有配置白名单，默认允许
            return true;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * 获取匹配的规则信息
     */
    public IpMatchResult getIpMatchResult(String clientIp) {
        if (!isIpAccessControlEnabled()) {
            return new IpMatchResult(true, null, null, "IP访问控制未启用");
        }
        
        lock.readLock().lock();
        try {
            // 获取原始配置值
            String whitelistConfig = configCache.get(IP_WHITELIST_KEY);
            String blacklistConfig = configCache.get(IP_BLACKLIST_KEY);
            
            // 先检查黑名单
            if (blacklistConfig != null && !blacklistConfig.trim().isEmpty()) {
                if (IpAccessControlUtil.isIpMatched(clientIp, blacklistConfig)) {
                    return new IpMatchResult(false, blacklistConfig, "BLACKLIST", "IP在黑名单中");
                }
            }
            
            // 再检查白名单
            if (whitelistConfig != null && !whitelistConfig.trim().isEmpty()) {
                if (IpAccessControlUtil.isIpMatched(clientIp, whitelistConfig)) {
                    return new IpMatchResult(true, whitelistConfig, "WHITELIST", "IP在白名单中");
                } else {
                    return new IpMatchResult(false, null, null, "IP不在白名单中");
                }
            }
            
            // 没有配置规则，默认允许
            return new IpMatchResult(true, null, null, "无IP访问限制");
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * IP匹配结果
     */
    public static class IpMatchResult {
        private final boolean allowed;
        private final String matchedRule;
        private final String ruleType;
        private final String reason;
        
        public IpMatchResult(boolean allowed, String matchedRule, String ruleType, String reason) {
            this.allowed = allowed;
            this.matchedRule = matchedRule;
            this.ruleType = ruleType;
            this.reason = reason;
        }
        
        public boolean isAllowed() {
            return allowed;
        }
        
        public String getMatchedRule() {
            return matchedRule;
        }
        
        public String getRuleType() {
            return ruleType;
        }
        
        public String getReason() {
            return reason;
        }
    }

    public void refreshCache() {
        refreshAllConfig();
    }
}