package com.jiuxi.admin.core.bean.vo;

import java.io.Serializable;

/**
 * 行政区划表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:18
 */
public class TpCityVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 行政区划id
     */
    private String cityId;
    /**
     * 行政区划代码
     */
    private String cityCode;
    /**
     * 行政区划名称
     */
    private String cityName;
    /**
     * 行政区划全称
     */
    private String cityFullName;
    /**
     * 行政区划简称
     */
    private String citySimpleName;
    /**
     * 上级行政区划ID
     */
    private String pcityId;
    /**
     * 用于统计的上级代码
     */
    private String statCode;
    /**
     * 排序
     */
    private Double orderIndex;
    /**
     * 是否启用
     */
    private Integer enabled;
    /**
     * 是否有效
     */
    private Integer actived;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 区划层级
     */
    private String cityLevel;
    /**
     * 创建时间
     */
    private String createTime;
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
     * 扩展字段04
     */
    private String extend04;
    /**
     * 扩展字段05
     */
    private String extend05;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityFullName() {
        return cityFullName;
    }

    public void setCityFullName(String cityFullName) {
        this.cityFullName = cityFullName;
    }

    public String getCitySimpleName() {
        return citySimpleName;
    }

    public void setCitySimpleName(String citySimpleName) {
        this.citySimpleName = citySimpleName;
    }

    public String getPcityId() {
        return pcityId;
    }

    public void setPcityId(String pcityId) {
        this.pcityId = pcityId;
    }

    public String getStatCode() {
        return statCode;
    }

    public void setStatCode(String statCode) {
        this.statCode = statCode;
    }

    public Double getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Double orderIndex) {
        this.orderIndex = orderIndex;
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

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(String cityLevel) {
        this.cityLevel = cityLevel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
