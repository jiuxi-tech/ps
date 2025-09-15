package com.jiuxi.module.sys.domain.entity;

import java.time.LocalDateTime;

/**
 * 系统日志实体
 * 表示系统中的操作日志，使用DDD模式设计
 * 
 * @author System Management (Enhanced by DDD Refactor)
 * @date 2025-09-06
 * @version 2.0 - Enhanced with DDD value objects and domain logic
 */
public class SystemLog {
    
    /**
     * 日志ID
     */
    private String logId;
    
    /**
     * 日志类型枚举
     */
    private LogType logTypeEnum;
    
    /**
     * 日志级别枚举
     */
    private LogLevel logLevelEnum;
    
    /**
     * 日志状态枚举
     */
    private LogStatus logStatusEnum;
    
    /**
     * 日志类型（兼容性字段）
     */
    private String logType;
    
    /**
     * 日志级别（兼容性字段）
     */
    private String logLevel;
    
    /**
     * 状态（兼容性字段）
     */
    private String status;
    
    /**
     * 模块名称
     */
    private String moduleName;
    
    /**
     * 操作名称
     */
    private String operationName;
    
    /**
     * 操作描述
     */
    private String operationDesc;
    
    /**
     * 请求方法
     */
    private String requestMethod;
    
    /**
     * 请求URL
     */
    private String requestUrl;
    
    /**
     * 请求参数
     */
    private String requestParams;
    
    /**
     * 响应结果
     */
    private String responseResult;
    
    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 操作人ID
     */
    private String operatorId;
    
    /**
     * 操作人姓名
     */
    private String operatorName;
    
    /**
     * 客户端IP
     */
    private String clientIp;
    
    /**
     * 用户代理
     */
    private String userAgent;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    /**
     * 默认构造函数
     */
    public SystemLog() {
        this.logId = java.util.UUID.randomUUID().toString();
        this.createTime = LocalDateTime.now();
        this.logStatusEnum = LogStatus.NORMAL;
        this.status = LogStatus.NORMAL.getCode();
    }
    
    /**
     * 构造函数
     * @param logType 日志类型
     * @param logLevel 日志级别
     * @param moduleName 模块名称
     * @param operationName 操作名称
     */
    public SystemLog(LogType logType, LogLevel logLevel, String moduleName, String operationName) {
        this();
        this.logTypeEnum = logType;
        this.logLevelEnum = logLevel;
        this.moduleName = moduleName;
        this.operationName = operationName;
        // 同步兼容性字段
        if (logType != null) {
            this.logType = logType.getCode();
        }
        if (logLevel != null) {
            this.logLevel = logLevel.getCode();
        }
    }
    
    // Getters and Setters
    public String getLogId() {
        return logId;
    }
    
    public void setLogId(String logId) {
        this.logId = logId;
    }
    
    public LogType getLogTypeEnum() {
        return logTypeEnum;
    }
    
    public void setLogTypeEnum(LogType logTypeEnum) {
        this.logTypeEnum = logTypeEnum;
        if (logTypeEnum != null) {
            this.logType = logTypeEnum.getCode();
        }
    }
    
    public LogLevel getLogLevelEnum() {
        return logLevelEnum;
    }
    
    public void setLogLevelEnum(LogLevel logLevelEnum) {
        this.logLevelEnum = logLevelEnum;
        if (logLevelEnum != null) {
            this.logLevel = logLevelEnum.getCode();
        }
    }
    
    public LogStatus getLogStatusEnum() {
        return logStatusEnum;
    }
    
    public void setLogStatusEnum(LogStatus logStatusEnum) {
        this.logStatusEnum = logStatusEnum;
        if (logStatusEnum != null) {
            this.status = logStatusEnum.getCode();
        }
    }
    
    public String getLogType() {
        return logType;
    }
    
    public void setLogType(String logType) {
        this.logType = logType;
    }
    
    public String getLogLevel() {
        return logLevel;
    }
    
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getModuleName() {
        return moduleName;
    }
    
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    
    public String getOperationName() {
        return operationName;
    }
    
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
    
    public String getOperationDesc() {
        return operationDesc;
    }
    
    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }
    
    public String getRequestMethod() {
        return requestMethod;
    }
    
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
    
    public String getRequestUrl() {
        return requestUrl;
    }
    
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
    
    public String getRequestParams() {
        return requestParams;
    }
    
    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }
    
    public String getResponseResult() {
        return responseResult;
    }
    
    public void setResponseResult(String responseResult) {
        this.responseResult = responseResult;
    }
    
    public Long getExecutionTime() {
        return executionTime;
    }
    
    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    
    public String getOperatorName() {
        return operatorName;
    }
    
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    
    public String getClientIp() {
        return clientIp;
    }
    
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemLog systemLog = (SystemLog) o;
        return java.util.Objects.equals(logId, systemLog.logId);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(logId);
    }
    
    @Override
    public String toString() {
        return "SystemLog{" +
                "logId='" + logId + '\'' +
                ", logType='" + logType + '\'' +
                ", logLevel='" + logLevel + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", operationName='" + operationName + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}