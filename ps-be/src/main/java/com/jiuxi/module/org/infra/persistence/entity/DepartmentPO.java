package com.jiuxi.module.org.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 部门持久化对象
 * 对应数据库表 tp_dept_basicinfo
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@TableName("tp_dept_basicinfo")
public class DepartmentPO {
    
    /**
     * 部门ID
     */
    @TableId
    private String deptId;
    
    /**
     * 父部门ID
     */
    private String pdeptId;
    
    /**
     * 部门层级编码
     */
    private String deptLevelcode;
    
    /**
     * 部门编号
     */
    private String deptNo;
    
    /**
     * 部门全称
     */
    private String deptFullName;
    
    /**
     * 部门简称
     */
    private String deptSimpleName;
    
    /**
     * 部门类型
     */
    private String deptType;
    
    /**
     * 部门描述
     */
    private String deptDesc;
    
    /**
     * 排序索引
     */
    private Double orderIndex;
    
    /**
     * 部门类别 (0政府 1企业)
     */
    private Integer category;
    
    /**
     * 行政区划代码
     */
    private String cityCode;
    
    /**
     * 负责人姓名
     */
    private String principalName;
    
    /**
     * 负责人电话
     */
    private String principalTel;
    
    /**
     * 所属机构ID
     */
    private String ascnId;
    
    /**
     * 是否叶子节点
     */
    private Integer leaf;
    
    /**
     * 是否启用
     */
    private Integer enabled;
    
    /**
     * 是否有效
     */
    private Integer actived;
    
    /**
     * 创建人
     */
    private String creator;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新人
     */
    private String updator;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
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
     * 扩展字段04
     */
    private String extend04;
    
    /**
     * 扩展字段05
     */
    private String extend05;
    
    // Getters and Setters
    public String getDeptId() {
        return deptId;
    }
    
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    
    public String getPdeptId() {
        return pdeptId;
    }
    
    public void setPdeptId(String pdeptId) {
        this.pdeptId = pdeptId;
    }
    
    public String getDeptLevelcode() {
        return deptLevelcode;
    }
    
    public void setDeptLevelcode(String deptLevelcode) {
        this.deptLevelcode = deptLevelcode;
    }
    
    public String getDeptNo() {
        return deptNo;
    }
    
    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }
    
    public String getDeptFullName() {
        return deptFullName;
    }
    
    public void setDeptFullName(String deptFullName) {
        this.deptFullName = deptFullName;
    }
    
    public String getDeptSimpleName() {
        return deptSimpleName;
    }
    
    public void setDeptSimpleName(String deptSimpleName) {
        this.deptSimpleName = deptSimpleName;
    }
    
    public String getDeptType() {
        return deptType;
    }
    
    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }
    
    public String getDeptDesc() {
        return deptDesc;
    }
    
    public void setDeptDesc(String deptDesc) {
        this.deptDesc = deptDesc;
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
    
    public String getCityCode() {
        return cityCode;
    }
    
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
    
    public String getPrincipalName() {
        return principalName;
    }
    
    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }
    
    public String getPrincipalTel() {
        return principalTel;
    }
    
    public void setPrincipalTel(String principalTel) {
        this.principalTel = principalTel;
    }
    
    public String getAscnId() {
        return ascnId;
    }
    
    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }
    
    public Integer getLeaf() {
        return leaf;
    }
    
    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }
    
    public Integer getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
    
    public Integer getActived() {
        return actived;
    }
    
    public void setActived(Integer actived) {
        this.actived = actived;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public void setUpdator(String updator) {
        this.updator = updator;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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
    
    public String getExtend04() {
        return extend04;
    }
    
    public void setExtend04(String extend04) {
        this.extend04 = extend04;
    }
    
    public String getExtend05() {
        return extend05;
    }
    
    public void setExtend05(String extend05) {
        this.extend05 = extend05;
    }
    
    /**
     * 获取左值（用于嵌套集合模型的树形查询优化）
     * 使用extend01字段存储
     */
    public Integer getLeftValue() {
        try {
            return extend01 != null ? Integer.parseInt(extend01) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * 设置左值（用于嵌套集合模型的树形查询优化）
     * 使用extend01字段存储
     */
    public void setLeftValue(Integer leftValue) {
        this.extend01 = leftValue != null ? leftValue.toString() : null;
    }
    
    /**
     * 获取右值（用于嵌套集合模型的树形查询优化）
     * 使用extend02字段存储
     */
    public Integer getRightValue() {
        try {
            return extend02 != null ? Integer.parseInt(extend02) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * 设置右值（用于嵌套集合模型的树形查询优化）
     * 使用extend02字段存储
     */
    public void setRightValue(Integer rightValue) {
        this.extend02 = rightValue != null ? rightValue.toString() : null;
    }
}