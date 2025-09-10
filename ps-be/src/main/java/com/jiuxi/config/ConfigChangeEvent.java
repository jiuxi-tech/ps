package com.jiuxi.config;

import org.springframework.context.ApplicationEvent;

/**
 * 配置变更事件
 * 当系统配置发生变更时发布此事件
 * 
 * @author ps-bmp
 * @since 2024-01-20
 */
public class ConfigChangeEvent extends ApplicationEvent {

    private final String configKey;
    private final String oldValue;
    private final String newValue;
    private final String operation; // ADD, UPDATE, DELETE
    
    /**
     * 构造函数
     * 
     * @param source 事件源
     * @param configKey 配置键
     * @param oldValue 旧值
     * @param newValue 新值
     * @param operation 操作类型
     */
    public ConfigChangeEvent(Object source, String configKey, String oldValue, String newValue, String operation) {
        super(source);
        this.configKey = configKey;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.operation = operation;
    }
    
    /**
     * 获取配置键
     */
    public String getConfigKey() {
        return configKey;
    }
    
    /**
     * 获取旧值
     */
    public String getOldValue() {
        return oldValue;
    }
    
    /**
     * 获取新值
     */
    public String getNewValue() {
        return newValue;
    }
    
    /**
     * 获取操作类型
     */
    public String getOperation() {
        return operation;
    }
    
    @Override
    public String toString() {
        return "ConfigChangeEvent{" +
                "configKey='" + configKey + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }
}