package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @ClassName: TpOperateLog
 * @Description: 操作日志表
 * @Author: System
 * @Date: 2024-01-17
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@TableName("tp_operate_log")
public class TpOperateLog {

    /**
     * 日志ID
     */
    @TableId("LOG_ID")
    private String logId;

    /**
     * 人员ID
     */
    @TableField("PERSON_ID")
    private String personId;

    /**
     * 人员姓名
     */
    @TableField("PERSON_NAME")
    private String personName;

    /**
     * 模块代码
     */
    @TableField("MODULE_CODE")
    private String moduleCode;

    /**
     * 操作时间
     */
    @TableField("OPERTER_TIME")
    private Date operterTime;

    /**
     * 操作类型
     */
    @TableField("OPERTER_TYPE")
    private String operterType;

    /**
     * 操作资源ID
     */
    @TableField("OPERTER_RID")
    private String operterRid;

    /**
     * 操作IP
     */
    @TableField("OPERTER_IP")
    private String operterIp;

    /**
     * 操作浏览器
     */
    @TableField("OPERTER_BROWSER")
    private String operterBrowser;

    /**
     * 用户名
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 归属ID
     */
    @TableField("ASCN_ID")
    private String ascnId;

    /**
     * 用户类别
     */
    @TableField("CATEGORY")
    private String category;

    /**
     * 城市代码
     */
    @TableField("CITY_CODE")
    private String cityCode;

    /**
     * 是否激活
     */
    @TableField("ACTIVED")
    private String actived;

    /**
     * 创建人
     */
    @TableField("CREATOR")
    private String creator;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("UPDATOR")
    private String updator;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 扩展字段1
     */
    @TableField("EXTEND01")
    private String extend01;

    /**
     * 扩展字段2
     */
    @TableField("EXTEND02")
    private String extend02;

    /**
     * 扩展字段3
     */
    @TableField("EXTEND03")
    private String extend03;

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
}