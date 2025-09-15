package com.jiuxi.module.sys.domain.entity;

/**
 * 日志状态枚举
 * 定义日志记录的状态
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public enum LogStatus {
    
    /**
     * 正常状态
     */
    NORMAL("NORMAL", "正常"),
    
    /**
     * 成功状态
     */
    SUCCESS("SUCCESS", "成功"),
    
    /**
     * 失败状态
     */
    FAILURE("FAILURE", "失败"),
    
    /**
     * 异常状态
     */
    EXCEPTION("EXCEPTION", "异常"),
    
    /**
     * 已处理状态
     */
    PROCESSED("PROCESSED", "已处理"),
    
    /**
     * 已归档状态
     */
    ARCHIVED("ARCHIVED", "已归档"),
    
    /**
     * 处理中状态（兼容system模块）
     */
    PROCESSING("PROCESSING", "处理中");
    
    private final String code;
    private final String description;
    
    LogStatus(String code, String description) {
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
     * 根据代码获取日志状态
     * @param code 代码
     * @return 日志状态
     */
    public static LogStatus fromCode(String code) {
        for (LogStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown log status code: " + code);
    }
    
    /**
     * 判断是否为成功状态
     * @return 是否成功
     */
    public boolean isSuccess() {
        return this == SUCCESS || this == NORMAL;
    }
    
    /**
     * 判断是否为失败状态
     * @return 是否失败
     */
    public boolean isFailure() {
        return this == FAILURE || this == EXCEPTION;
    }
    
    /**
     * 判断是否为最终状态
     * @return 是否最终状态
     */
    public boolean isFinalStatus() {
        return this == PROCESSED || this == ARCHIVED;
    }
    
    /**
     * 检查是否处理中（兼容system模块）
     */
    public boolean isProcessing() {
        return this == PROCESSING;
    }
    
    /**
     * 检查是否已完成（成功或失败）（兼容system模块）
     */
    public boolean isCompleted() {
        return this == SUCCESS || this == FAILURE || this == PROCESSED;
    }
    
    /**
     * 根据Integer代码获取枚举（兼容system模块）
     */
    public static LogStatus fromCode(Integer code) {
        if (code == null) {
            return NORMAL;
        }
        // 兼容system模块的数字代码
        switch (code) {
            case 1:
                return SUCCESS;
            case 0:
                return FAILURE;
            case -1:
                return PROCESSING;
            default:
                return NORMAL;
        }
    }
    
    /**
     * 获取Integer代码（兼容system模块）
     */
    public Integer getIntegerCode() {
        switch (this) {
            case SUCCESS:
                return 1;
            case FAILURE:
                return 0;
            case PROCESSING:
                return -1;
            default:
                return null;
        }
    }
}