package com.jiuxi.module.sys.domain.valueobject;

import java.util.Objects;

/**
 * 配置值对象
 * 封装配置值的业务规则和类型转换逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class ConfigValue {
    
    /**
     * 配置值最大长度
     */
    private static final int MAX_LENGTH = 2000;
    
    private final String value;
    private final String dataType;
    
    /**
     * 构造函数
     * @param value 配置值
     * @param dataType 数据类型（STRING, NUMBER, BOOLEAN, JSON等）
     */
    public ConfigValue(String value, String dataType) {
        if (value == null) {
            throw new IllegalArgumentException("配置值不能为null");
        }
        
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("配置值长度不能超过" + MAX_LENGTH + "个字符");
        }
        
        this.value = value;
        this.dataType = dataType != null ? dataType.toUpperCase() : "STRING";
        
        // 验证值是否符合指定的数据类型
        validateDataType();
    }
    
    /**
     * 创建字符串类型配置值
     * @param value 字符串值
     * @return 配置值对象
     */
    public static ConfigValue ofString(String value) {
        return new ConfigValue(value, "STRING");
    }
    
    /**
     * 创建数字类型配置值
     * @param value 数字值
     * @return 配置值对象
     */
    public static ConfigValue ofNumber(Number value) {
        return new ConfigValue(value.toString(), "NUMBER");
    }
    
    /**
     * 创建布尔类型配置值
     * @param value 布尔值
     * @return 配置值对象
     */
    public static ConfigValue ofBoolean(Boolean value) {
        return new ConfigValue(value.toString(), "BOOLEAN");
    }
    
    /**
     * 创建JSON类型配置值
     * @param value JSON字符串
     * @return 配置值对象
     */
    public static ConfigValue ofJson(String value) {
        return new ConfigValue(value, "JSON");
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDataType() {
        return dataType;
    }
    
    /**
     * 获取字符串值
     * @return 字符串值
     */
    public String asString() {
        return value;
    }
    
    /**
     * 获取整数值
     * @return 整数值
     */
    public Integer asInteger() {
        if (!"NUMBER".equals(dataType)) {
            throw new IllegalStateException("配置值不是数字类型");
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("配置值无法转换为整数: " + value);
        }
    }
    
    /**
     * 获取长整数值
     * @return 长整数值
     */
    public Long asLong() {
        if (!"NUMBER".equals(dataType)) {
            throw new IllegalStateException("配置值不是数字类型");
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("配置值无法转换为长整数: " + value);
        }
    }
    
    /**
     * 获取浮点数值
     * @return 浮点数值
     */
    public Double asDouble() {
        if (!"NUMBER".equals(dataType)) {
            throw new IllegalStateException("配置值不是数字类型");
        }
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("配置值无法转换为浮点数: " + value);
        }
    }
    
    /**
     * 获取布尔值
     * @return 布尔值
     */
    public Boolean asBoolean() {
        if (!"BOOLEAN".equals(dataType)) {
            throw new IllegalStateException("配置值不是布尔类型");
        }
        return Boolean.valueOf(value);
    }
    
    /**
     * 检查是否为空值
     * @return 是否为空
     */
    public boolean isEmpty() {
        return value == null || value.trim().isEmpty();
    }
    
    /**
     * 验证数据类型
     */
    private void validateDataType() {
        switch (dataType) {
            case "NUMBER":
                try {
                    Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("配置值不是有效的数字: " + value);
                }
                break;
            case "BOOLEAN":
                if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
                    throw new IllegalArgumentException("配置值不是有效的布尔值: " + value);
                }
                break;
            case "JSON":
                // 简单的JSON格式验证
                if (!value.trim().startsWith("{") && !value.trim().startsWith("[")) {
                    throw new IllegalArgumentException("配置值不是有效的JSON格式: " + value);
                }
                break;
            case "STRING":
            default:
                // 字符串类型无需额外验证
                break;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigValue that = (ConfigValue) o;
        return Objects.equals(value, that.value) && Objects.equals(dataType, that.dataType);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value, dataType);
    }
    
    @Override
    public String toString() {
        return value + " (" + dataType + ")";
    }
}