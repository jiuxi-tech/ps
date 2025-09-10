package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 单位/部门/网格/其他基本信息表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:17
 */
@TableName("tp_dept_basicinfo")
public class TpDeptBasicinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
    @TableId
    private String deptId;
    /**
     * 部门父节点
     */
    private String pdeptId;
    /**
     * 部门层级编码 单位部门层级code以三位数字递增，自动生成。可以根据此字段统计本级及下级
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
     * 部门类型 单位or部门or网格or其他，其他是为了满足公众等没有部门的用户分类使用，归为政府编制下
     */
    private String deptType;
    /**
     * 部门简介
     */
    private String deptDesc;
    /**
     * 部门排序
     */
    private Double orderIndex;
    /**
     * 部门类别 政府or企业
     */
    private Integer category;
    /**
     * 行政区划code
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
     * 所属机构（单位）id 政府存机构ID，企业存单位id，存在分公司的，存所在分公司单位id
     */
    private String ascnId;
    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 是否叶子节点
     */
    private Integer leaf;

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
     * 租户id
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

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getLeaf() {
        return leaf;
    }

    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
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
}
