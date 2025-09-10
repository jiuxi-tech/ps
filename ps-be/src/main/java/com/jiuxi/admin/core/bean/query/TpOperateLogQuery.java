package com.jiuxi.admin.core.bean.query;

import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @ClassName: TpOperateLogQuery
 * @Description: 操作日志表查询对象
 * @Author: System
 * @Date: 2024-01-17
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
public class TpOperateLogQuery {

    /**
     * 日志ID
     */
    private String logId;

    /**
     * 人员ID
     */
    private String personId;

    /**
     * 模块代码
     */
    private String moduleCode;

    /**
     * 操作时间开始
     */
    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    private Date operterTimeStart;

    /**
     * 操作时间结束
     */
    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    private Date operterTimeEnd;

    /**
     * 操作类型
     */
    private String operterType;

    /**
     * 操作资源ID
     */
    private String operterRid;

    /**
     * 操作IP
     */
    private String operterIp;

    /**
     * 用户名
     */
    private String username;

    /**
     * 归属ID
     */
    private String ascnId;

    /**
     * 用户类别
     */
    private String category;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 是否激活
     */
    private String actived;

    /**
     * 当前页
     */
    private Integer current = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public Date getOperterTimeStart() {
        return operterTimeStart;
    }

    public void setOperterTimeStart(Date operterTimeStart) {
        this.operterTimeStart = operterTimeStart;
    }

    public Date getOperterTimeEnd() {
        return operterTimeEnd;
    }

    public void setOperterTimeEnd(Date operterTimeEnd) {
        this.operterTimeEnd = operterTimeEnd;
    }

    public String getOperterType() {
        return operterType;
    }

    public void setOperterType(String operterType) {
        this.operterType = operterType;
    }

    public String getOperterRid() {
        return operterRid;
    }

    public void setOperterRid(String operterRid) {
        this.operterRid = operterRid;
    }

    public String getOperterIp() {
        return operterIp;
    }

    public void setOperterIp(String operterIp) {
        this.operterIp = operterIp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAscnId() {
        return ascnId;
    }

    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getActived() {
        return actived;
    }

    public void setActived(String actived) {
        this.actived = actived;
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