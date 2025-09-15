package com.jiuxi.module.system.domain.entity;

/**
 * @ClassName: LogStatus
 * @Description: 日志状态枚举
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public enum LogStatus {
    
    /**
     * 成功状态
     */
    SUCCESS(1, "SUCCESS", "成功"),
    
    /**
     * 失败状态
     */
    FAILURE(0, "FAILURE", "失败"),
    
    /**
     * 处理中状态
     */
    PROCESSING(-1, "PROCESSING", "处理中");
    
    private final Integer code;
    private final String status;
    private final String description;
    
    LogStatus(Integer code, String status, String description) {
        this.code = code;
        this.status = status;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据代码获取枚举
     */
    public static LogStatus fromCode(Integer code) {
        for (LogStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown log status code: " + code);
    }
    
    /**
     * 根据状态字符串获取枚举
     */
    public static LogStatus fromStatus(String status) {
        for (LogStatus logStatus : values()) {
            if (logStatus.getStatus().equals(status)) {
                return logStatus;
            }
        }
        throw new IllegalArgumentException("Unknown log status: " + status);
    }
    
    /**
     * 检查是否成功
     */
    public boolean isSuccess() {
        return this == SUCCESS;
    }
    
    /**
     * 检查是否失败
     */
    public boolean isFailure() {
        return this == FAILURE;
    }
    
    /**
     * 检查是否处理中
     */
    public boolean isProcessing() {
        return this == PROCESSING;
    }
    
    /**
     * 检查是否已完成（成功或失败）
     */
    public boolean isCompleted() {
        return this == SUCCESS || this == FAILURE;
    }
}