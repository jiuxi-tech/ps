package com.jiuxi.admin.core.bean.query;

import java.io.Serializable;

/**
 * 字典表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:18
 */
public class TpDictionaryQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典编码
     */
    private String dicCode;
    /**
     * 字典名称
     */
    private String dicName;
    /**
     * 父字典code
     */
    private String pdicId;

    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页记录数
     */
    private Integer size;

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getPdicId() {
        return pdicId;
    }

    public void setPdicId(String pdicId) {
        this.pdicId = pdicId;
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
