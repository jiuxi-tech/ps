package com.jiuxi.admin.core.bean.query;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * ""
 *
 * @author pand
 * @email
 * @date 2021-05-12 13:48:38
 */
public class CJsonQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模块名
     */
    @NotBlank(message = "模块名不能为空！")
    private String mcode;

    /**
     * pc/app
     */
    @NotBlank(message = "列表类型不能为空！")
    private String fsource;

    /**
     * 模块id,业务主键
     *//*
    private String referId;

    *//**
     * 业务数据,json格式
     *//*
    private String dataDetail;

    *//**
     * 返回字段
     *//*
    private String resultCol;

    *//**
     * 查询条件字段
     *//*
    private String selectCol;*/

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;

    public String getMcode() {
        return mcode;
    }

    public void setMcode(String mcode) {
        this.mcode = mcode;
    }

    /*public String getReferId() {
        return referId;
    }

    public void setReferId(String referId) {
        this.referId = referId;
    }
*/
    public String getFsource() {
        return fsource;
    }

    public void setFsource(String fsource) {
        this.fsource = fsource;
    }

    /*public String getDataDetail() {
        return dataDetail;
    }

    public void setDataDetail(String dataDetail) {
        this.dataDetail = dataDetail;
    }

    public String getResultCol() {
        return resultCol;
    }

    public void setResultCol(String resultCol) {
        this.resultCol = resultCol;
    }

    public String getSelectCol() {
        return selectCol;
    }

    public void setSelectCol(String selectCol) {
        this.selectCol = selectCol;
    }*/

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
