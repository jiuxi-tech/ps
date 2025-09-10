package com.jiuxi.admin.core.bean.query;


import java.io.Serializable;


/**
 * 参数配置表
 *
 * @author pand
 * @email
 * @date 2023-09-12 19:32:55
 */
public class TpParameterConfigQuery implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 参数配置
     */
    private String pmId;

    /**
     * 参数key
     */
    private String pmKey;

    /**
     * 参数名称
     */
    private String pmName;

    /**
     * 参数值
     */
    private String pmVal;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;


    public String getPmId() {
        return pmId;
    }

    public void setPmId(String pmId) {
        this.pmId = pmId;
    }


    public String getPmKey() {
        return pmKey;
    }

    public void setPmKey(String pmKey) {
        this.pmKey = pmKey;
    }


    public String getPmName() {
        return pmName;
    }

    public void setPmName(String pmName) {
        this.pmName = pmName;
    }


    public String getPmVal() {
        return pmVal;
    }

    public void setPmVal(String pmVal) {
        this.pmVal = pmVal;
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
