package com.jiuxi.admin.core.bean.query;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;

/**
 * @Description: 用户查询条件
 * @ClassName: TpPersonBasicQuery
 * @Author: pand
 * @Date: 2020-11-24 16:44
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class TpPersonBasicQuery {

    /**
     * 部门id
     */
    private String deptId;
    /**
     * 查询数据的层级标识 0：本级   1：本级及下级（需要根据层级code模糊查询）
     */
    @NotNull(message = "查询数据的层级标识不能为空")
    @JsonIgnore
    private int levelFlag;

    /**
     * 部门层级
     */
    @JsonIgnore
    private String deptLevelcode;

    /**
     * 账号名称
     */
    private String username;

    /**
     * 用户名称
     */
    private String personName;

    /**
     * 性别
     */
    private Integer sex;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 职位
     */
    private String office;
    /**
     * 人员类别
     */
    @JsonIgnore
    private Integer category;
    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页记录数
     */
    private Integer size;
    
    /**
     * 选中的用户ID列表（用于导出选中用户）
     */
    private java.util.List<String> selectedUserIds;
    
    /**
     * 是否包含未激活的人员（ACTIVED=0）
     * 当物理删除开关启用时，此字段为true，表示显示所有人员包括未激活的
     */
    @JsonIgnore
    private boolean includeInactive = false;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public int getLevelFlag() {
        return levelFlag;
    }

    public void setLevelFlag(int levelFlag) {
        this.levelFlag = levelFlag;
    }

    public String getDeptLevelcode() {
        return deptLevelcode;
    }

    public void setDeptLevelcode(String deptLevelcode) {
        this.deptLevelcode = deptLevelcode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
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
    
    public java.util.List<String> getSelectedUserIds() {
        return selectedUserIds;
    }
    
    public void setSelectedUserIds(java.util.List<String> selectedUserIds) {
        this.selectedUserIds = selectedUserIds;
    }
    
    public boolean isIncludeInactive() {
        return includeInactive;
    }
    
    public void setIncludeInactive(boolean includeInactive) {
        this.includeInactive = includeInactive;
    }
}
