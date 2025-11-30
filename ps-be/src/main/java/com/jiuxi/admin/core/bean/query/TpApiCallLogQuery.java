package com.jiuxi.admin.core.bean.query;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * API调用日志查询条件
 *
 * @author system
 * @date 2025-11-30
 */
public class TpApiCallLogQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * API路径
     */
    private String apiPath;

    /**
     * HTTP方法
     */
    private String httpMethod;

    /**
     * 响应状态码
     */
    private Integer responseStatus;

    /**
     * 请求IP
     */
    private String requestIp;

    /**
     * 开始时间（14位YYYYMMDDHHmmss格式）
     */
    private String startTime;

    /**
     * 结束时间（14位YYYYMMDDHHmmss格式）
     */
    private String endTime;

    /**
     * 租户ID
     */
    @JsonIgnore
    private String tenantId;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
