package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 登录时间段控制规则表
 *
 * @author system
 * @date 2025-01-20
 */
@TableName("tp_time_rule")
public class TpTimeRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID（主键）
     */
    @TableId
    private String id;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则启动时间（yyyyMMddHHmmss格式）
     */
    private String startTime;

    /**
     * 规则结束时间（yyyyMMddHHmmss格式）
     */
    private String endTime;

    /**
     * 规则状态（0：禁用，1：启用）
     */
    private Integer status;

    /**
     * 是否允许登录：1-允许，0-拒绝
     */
    private Integer allowLogin;

    /**
     * 角色ID串（逗号分隔）
     */
    private String roleIds;

    /**
     * 角色名称串（逗号分隔）
     */
    private String roleNames;

    /**
     * 人员ID串（逗号分隔）
     */
    private String userIds;

    /**
     * 人员名称串（逗号分隔）
     */
    private String userNames;

    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 创建时间（yyyyMMddHHmmss格式）
     */
    private String createTime;

    /**
     * 修改人ID
     */
    private String updator;

    /**
     * 修改时间（yyyyMMddHHmmss格式）
     */
    private String updateTime;

    /**
     * 是否有效（1：有效，0：无效）
     */
    private Integer actived;

    /**
     * 逻辑删除（0：未删除，1：已删除）
     */
    private Integer logDelete;

    /**
     * 扩展字段01
     */
    private String extend01;

    /**
     * 扩展字段02
     */
    private String extend02;

    /**
     * 扩展字段03
     */
    private String extend03;

    // Getter and Setter methods

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAllowLogin() {
        return allowLogin;
    }

    public void setAllowLogin(Integer allowLogin) {
        this.allowLogin = allowLogin;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getActived() {
        return actived;
    }

    public void setActived(Integer actived) {
        this.actived = actived;
    }

    public Integer getLogDelete() {
        return logDelete;
    }

    public void setLogDelete(Integer logDelete) {
        this.logDelete = logDelete;
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