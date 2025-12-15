package com.jiuxi.admin.core.bean.query;

/**
 * @ClassName: TpEntBasicQuery
 * @Description: 企业查询
 * @Author: Ypp
 * @Date: 2020/11/27 13:43
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TpEntBasicQuery {

    /**
     * 企业名称
     */
    private String entFullName;

    /**
     * 生产地行政区划标识
     */
    private String entAddrCode;

    /** 行政区划code */
    private String  cityCode;

    /** 法定代表人 */
    private String  legalRepr;

    /** 所属行业，字典编码SYS16 */
    private String  industryTypeCode;

    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页记录数
     */
    private Integer size;

    public String getEntFullName() {
        return entFullName;
    }

    public void setEntFullName(String entFullName) {
        this.entFullName = entFullName;
    }

    public String getEntAddrCode() {
        return entAddrCode;
    }

    public void setEntAddrCode(String entAddrCode) {
        this.entAddrCode = entAddrCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getLegalRepr() {
        return legalRepr;
    }

    public void setLegalRepr(String legalRepr) {
        this.legalRepr = legalRepr;
    }

    public String getIndustryTypeCode() {
        return industryTypeCode;
    }

    public void setIndustryTypeCode(String industryTypeCode) {
        this.industryTypeCode = industryTypeCode;
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
}
