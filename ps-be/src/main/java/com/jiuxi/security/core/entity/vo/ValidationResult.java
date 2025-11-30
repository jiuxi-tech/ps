package com.jiuxi.security.core.entity.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证结果数据模型
 * 用于封装密码验证和其他验证操作的结果
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public class ValidationResult {

    /**
     * 验证是否通过
     */
    private boolean valid;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 详细信息，用于存储额外的验证结果数据
     */
    private Map<String, Object> details;

    /**
     * 私有构造函数，通过静态工厂方法创建实例
     */
    private ValidationResult(boolean valid, String errorCode, String errorMessage) {
        this.valid = valid;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.details = new HashMap<>();
    }

    /**
     * 创建成功的验证结果
     * 
     * @return 验证通过的结果对象
     */
    public static ValidationResult success() {
        return new ValidationResult(true, null, null);
    }

    /**
     * 创建失败的验证结果
     * 
     * @param errorCode 错误代码
     * @param errorMessage 错误消息
     * @return 验证失败的结果对象
     */
    public static ValidationResult failure(String errorCode, String errorMessage) {
        return new ValidationResult(false, errorCode, errorMessage);
    }

    /**
     * 添加详细信息
     * 
     * @param key 键
     * @param value 值
     * @return 当前对象，支持链式调用
     */
    public ValidationResult addDetail(String key, Object value) {
        this.details.put(key, value);
        return this;
    }

    /**
     * 获取详细信息中的值
     * 
     * @param key 键
     * @return 对应的值
     */
    public Object getDetail(String key) {
        return this.details.get(key);
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "valid=" + valid +
                ", errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", details=" + details +
                '}';
    }
}
