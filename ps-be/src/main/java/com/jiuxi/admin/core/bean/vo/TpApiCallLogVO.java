package com.jiuxi.admin.core.bean.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * API调用日志VO
 *
 * @author system
 * @date 2025-11-30
 */
public class TpApiCallLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    private String logId;
    
    /**
     * 应用ID
     */
    private String appId;
    
    /**
     * 应用名称
     */
    private String appName;
    
    /**
     * API ID
     */
    private String apiId;
    
    /**
     * API路径
     */
    private String apiPath;
    
    /**
     * HTTP方法
     */
    private String httpMethod;
    
    /**
     * 请求IP
     */
    private String requestIp;
    
    /**
     * 请求参数
     */
    private String requestParams;
    
    /**
     * 响应状态码（业务状态码：1:成功, -1:失败, 401/403/500等）
     */
    private Integer responseStatus;
    
    /**
     * 响应时间（毫秒）
     */
    private Integer responseTime;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 调用时间
     */
    private String callTime;
    
    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 数据密钥
     */
    private String passKey;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TpApiCallLogVO that = (TpApiCallLogVO) o;
        return logId.equals(that.logId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logId);
    }
}
