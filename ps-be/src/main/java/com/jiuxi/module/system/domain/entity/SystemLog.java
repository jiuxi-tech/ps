package com.jiuxi.module.system.domain.entity;

import java.time.LocalDateTime;

/**
 * @ClassName: SystemLog
 * @Description: 系统日志管理领域实体
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public class SystemLog {
    
    /**
     * 日志ID
     */
    private String logId;
    
    /**
     * 日志类型
     */
    private LogType logType;
    
    /**
     * 日志级别
     */
    private LogLevel logLevel;
    
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
     * 操作状态
     */
    private LogStatus status;
    
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
     * 默认构造函数
     */
    public SystemLog() {
        this.createTime = LocalDateTime.now();
        this.status = LogStatus.SUCCESS;
    }
    
    /**
     * 构造函数
     */
    public SystemLog(LogType logType, LogLevel logLevel, String moduleName, String operationName) {
        this();
        this.logType = logType;
        this.logLevel = logLevel;
        this.moduleName = moduleName;
        this.operationName = operationName;
    }
    
    // 领域方法
    
    /**
     * 标记为成功
     */
    public void markAsSuccess() {
        this.status = LogStatus.SUCCESS;
        this.errorMessage = null;
    }
    
    /**
     * 标记为失败
     */
    public void markAsFailure(String errorMessage) {
        this.status = LogStatus.FAILURE;
        this.errorMessage = errorMessage;
    }
    
    /**
     * 设置执行时间
     */
    public void setExecutionTime(long startTime) {
        this.executionTime = System.currentTimeMillis() - startTime;
    }
    
    /**
     * 设置操作人信息
     */
    public void setOperator(String operatorId, String operatorName) {
        this.operatorId = operatorId;
        this.operatorName = operatorName;
    }
    
    /**
     * 设置请求信息
     */
    public void setRequestInfo(String method, String url, String params) {
        this.requestMethod = method;
        this.requestUrl = url;
        this.requestParams = params;
    }
    
    /**
     * 设置客户端信息
     */
    public void setClientInfo(String clientIp, String userAgent) {
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }
    
    /**
     * 检查是否为错误日志
     */
    public boolean isError() {
        return this.logLevel == LogLevel.ERROR;
    }
    
    /**
     * 检查是否为操作日志
     */
    public boolean isOperationLog() {
        return this.logType == LogType.OPERATION;
    }
    
    /**
     * 检查是否执行成功
     */
    public boolean isSuccess() {
        return this.status == LogStatus.SUCCESS;
    }
    
    // Getter和Setter方法
    
    public String getLogId() {
        return logId;
    }
    
    public void setLogId(String logId) {
        this.logId = logId;
    }
    
    public LogType getLogType() {
        return logType;
    }
    
    public void setLogType(LogType logType) {
        this.logType = logType;
    }
    
    public LogLevel getLogLevel() {
        return logLevel;
    }
    
    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
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
    
    public LogStatus getStatus() {
        return status;
    }
    
    public void setStatus(LogStatus status) {
        this.status = status;
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
}