package com.jiuxi.admin.core.bean.vo;

import java.util.Date;

/**
 * @ClassName: TpOperateLogVO
 * @Description: 操作日志表VO
 * @Author: System
 * @Date: 2024-01-17
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
public class TpOperateLogVO {

    /**
     * 日志ID
     */
    private String logId;

    /**
     * 人员ID
     */
    private String personId;

    /**
     * 人员姓名
     */
    private String personName;

    /**
     * 模块代码
     */
    private String moduleCode;

    /**
     * 操作时间
     */
    private Date operterTime;

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
     * 操作浏览器
     */
    private String operterBrowser;

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
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updator;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 扩展字段1
     */
    private String extend01;

    /**
     * 扩展字段2
     */
    private String extend02;

    /**
     * 扩展字段3
     */
    private String extend03;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 来源
     */
    private String source;

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

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public Date getOperterTime() {
        return operterTime;
    }

    public void setOperterTime(Date operterTime) {
        this.operterTime = operterTime;
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

    public String getOperterBrowser() {
        return operterBrowser;
    }

    public void setOperterBrowser(String operterBrowser) {
        this.operterBrowser = operterBrowser;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getExtend01() {
        return extend01;
    }

    public void setExtend01(String extend01) {
        this.extend01 = extend01;
    }

    public String getExtend02() {
        return extend02;
    }

    public void setExtend02(String extend02) {
        this.extend02 = extend02;
    }

    public String getExtend03() {
        return extend03;
    }

    public void setExtend03(String extend03) {
        this.extend03 = extend03;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}