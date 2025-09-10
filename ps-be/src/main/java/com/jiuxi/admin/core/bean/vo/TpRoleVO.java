package com.jiuxi.admin.core.bean.vo;

import com.jiuxi.core.core.validator.group.AddGroup;
import com.jiuxi.core.core.validator.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

/**
 * 角色表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:17
 */
public class TpRoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @NotBlank(message = "人员id不能为空", groups = UpdateGroup.class)
    private String roleId;
    /**
     * 角色名称
     */
    @NotBlank(message = "人员id不能为空", groups = AddGroup.class)
    private String roleName;
    /**
     * 角色类型 ，即谁来使用该角色，默认谁创建的谁使用，如政府创建政府角色，企业创建企业角色，或者政府给企业创建的
     * 0：政府
     * 1：企业
     */
    private Integer roleType;
    /**
     * 角色描述
     */
    private String roleDesc;
    /**
     * 角色排序
     */
    private Double orderIndex;
    /**
     * 角色类别 政府角色or企业角色 0政府 1企业 2其他第三方
     */
    private Integer category;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 创建人所在单位id
     */
    private String ascnId;
    /**
     * 创建人的角色
     */
    private String createRole;
    private String createRoleName;
    /**
     * 是否有效
     */
    private Integer actived;
    /**
     * 创建人
     */
    @NotBlank(message = "创建人不能为空", groups = UpdateGroup.class)
    private String creator;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改人
     */
    private String updator;
    /**
     * 修改时间
     */
    private String updateTime;
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
     * 数据密钥
     */
    private String passKey;

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public Double getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Double orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getActived() {
        return actived;
    }

    public void setActived(Integer actived) {
        this.actived = actived;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    public String getAscnId() {
        return ascnId;
    }

    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
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

    public String getCreateRole() {
        return createRole;
    }

    public void setCreateRole(String createRole) {
        this.createRole = createRole;
    }

    public String getCreateRoleName() {
        return createRoleName;
    }

    public void setCreateRoleName(String createRoleName) {
        this.createRoleName = createRoleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TpRoleVO tpRoleVO = (TpRoleVO) o;
        return roleId.equals(tpRoleVO.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId);
    }
}
