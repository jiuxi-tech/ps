package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 部门扩展信息表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:17
 */
@TableName("tp_dept_exinfo")
public class TpDeptExinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门id 外健
     */
    @TableId
    private String deptId;
    /**
     * 直接监管的上级部门id
     */
    private String supdeptId;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 条线code 字典值，各个行业条线，即部门类别，如司法条线
     */
    private String lineCode;
    /**
     * 机构坐标_经度
     */
    private String longitude;
    /**
     * 机构坐标_纬度
     */
    private String latitude;
    /**
     * 经纬度所计算的geohash码
     */
    private String geoCode;
    /**
     * 地址
     */
    private String address;
    /**
     * 部门邮箱
     */
    private String email;
    /**
     * 主管行业代码 多个行业，逗号隔开
     */
    private String direinDustryCode;
    private String direinDustryName;
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

    public String getSupdeptId() {
        return supdeptId;
    }

    public void setSupdeptId(String supdeptId) {
        this.supdeptId = supdeptId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getGeoCode() {
        return geoCode;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireinDustryCode() {
        return direinDustryCode;
    }

    public void setDireinDustryCode(String direinDustryCode) {
        this.direinDustryCode = direinDustryCode;
    }

    public String getDireinDustryName() {
        return direinDustryName;
    }

    public void setDireinDustryName(String direinDustryName) {
        this.direinDustryName = direinDustryName;
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
