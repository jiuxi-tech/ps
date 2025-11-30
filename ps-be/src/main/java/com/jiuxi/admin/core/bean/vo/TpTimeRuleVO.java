package com.jiuxi.admin.core.bean.vo;

import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 登录时间段控制规则表VO
 *
 * @author system
 * @date 2025-01-20
 */
public class TpTimeRuleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID（主键）
     */
    @NotBlank(message = "规则ID不能为空", groups = UpdateGroup.class)
    private String id;

    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String ruleName;

    /**
     * 规则启动时间（yyyyMMddHHmmss格式）
     */
    @NotBlank(message = "规则启动时间不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String startTime;

    /**
     * 规则结束时间（yyyyMMddHHmmss格式）
     */
    @NotBlank(message = "规则结束时间不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String endTime;

    /**
     * 规则状态（0：禁用，1：启用）
     */
    @NotNull(message = "规则状态不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer status;

    /**
     * 是否允许登录：1-允许，0-拒绝
     */
    @NotNull(message = "是否允许登录不能为空", groups = {AddGroup.class, UpdateGroup.class})
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

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 密钥字段
     */
    private String passKey;

    // 状态名称（用于前端显示）
    private String statusName;

    // 规则类型名称（用于前端显示）
    private String allowLoginName;

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

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getAllowLoginName() {
        return allowLoginName;
    }

    public void setAllowLoginName(String allowLoginName) {
        this.allowLoginName = allowLoginName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreateUserName() {
        return creatorName;
    }

    public void setCreateUserName(String createUserName) {
        this.creatorName = createUserName;
    }

    public String getUpdateUserName() {
        return creatorName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.creatorName = updateUserName;
    }

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
    }
}