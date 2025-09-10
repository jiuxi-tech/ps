package com.jiuxi.admin.core.bean.query;

import java.io.Serializable;


/**
 * 中介表
 *
 * @author pand
 * @email
 * @date 2021-05-25 17:32:41
 */
public class TpMediorgQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 中介名称（全称）
     */
    private String mediorgFullName;

    /**
     * 中介简称
     */
    private String mediorgSimpleName;

    /**
     * 机构类型
     */
    private String mediorgType;

    /**
     * 统一社会信用代码
     */
    private String mediorgUnifiedCode;

    /**
     * 所属行政区划
     */
    private String cityCode;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;


    public String getMediorgFullName() {
        return mediorgFullName;
    }

    public void setMediorgFullName(String mediorgFullName) {
        this.mediorgFullName = mediorgFullName;
    }


    public String getMediorgSimpleName() {
        return mediorgSimpleName;
    }

    public void setMediorgSimpleName(String mediorgSimpleName) {
        this.mediorgSimpleName = mediorgSimpleName;
    }


    public String getMediorgType() {
        return mediorgType;
    }

    public void setMediorgType(String mediorgType) {
        this.mediorgType = mediorgType;
    }


    public String getMediorgUnifiedCode() {
        return mediorgUnifiedCode;
    }

    public void setMediorgUnifiedCode(String mediorgUnifiedCode) {
        this.mediorgUnifiedCode = mediorgUnifiedCode;
    }


    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
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
