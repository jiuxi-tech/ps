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
    ARCHIVED("ARCHIVED", "已归档");
    
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
}