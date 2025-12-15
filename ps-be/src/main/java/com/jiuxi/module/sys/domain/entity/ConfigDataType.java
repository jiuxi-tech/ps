package com.jiuxi.module.sys.domain.entity;

/**
 * @ClassName: ConfigDataType
 * @Description: 配置数据类型枚举
 * @Author: Qdd
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public enum ConfigDataType {
    
    /**
     * 字符串类型
     */
    STRING("STRING", "字符串"),
    
    /**
     * 整数类型
     */
    INTEGER("INTEGER", "整数"),
    
    /**
     * 布尔类型
     */
    BOOLEAN("BOOLEAN", "布尔值"),
    
    /**
     * 浮点数类型
     */
    DOUBLE("DOUBLE", "浮点数"),
    
    /**
     * JSON对象类型
     */
    JSON("JSON", "JSON对象"),
    
    /**
     * 数组类型
     */
    ARRAY("ARRAY", "数组"),
    
    /**
     * 日期类型
     */
    DATE("DATE", "日期"),
    
    /**
     * 文件路径类型
     */
    FILE_PATH("FILE_PATH", "文件路径"),
    
    /**
     * URL类型
     */
    URL("URL", "URL地址");
    
    private final String code;
    private final String description;
    
    ConfigDataType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据代码获取枚举
     */
    public static ConfigDataType fromCode(String code) {
        for (ConfigDataType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown config data type code: " + code);
    }
    
    /**
     * 检查是否为数值类型
     */
    public boolean isNumeric() {
        return this == INTEGER || this == DOUBLE;
    }
    
    /**
     * 检查是否为复杂类型
     */
    public boolean isComplex() {
        return this == JSON || this == ARRAY;
    }
    
    /**
     * 获取默认值
     */
    public String getDefaultValue() {
        switch (this) {
            case STRING:
            case FILE_PATH:
            case URL:
                return "";
            case INTEGER:
                return "0";
            case DOUBLE:
                return "0.0";
            case BOOLEAN:
                return "false";
            case JSON:
                return "{}";
            case ARRAY:
                return "[]";
            case DATE:
                return "1970-01-01";
            default:
                return "";
        }
    }
}