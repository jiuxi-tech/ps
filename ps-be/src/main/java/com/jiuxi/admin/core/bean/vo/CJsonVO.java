package com.jiuxi.admin.core.bean.vo;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * ""
 *
 * @author pand
 * @email
 * @date 2021-05-12 13:48:38
 */
public class CJsonVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 模块表单id
     */
    @NotBlank(message = "fid不能为空！")
    private String fid;

    /**
     * 主键
     */
    private String id;

    /**
     * 模块名
     */
    private String mcode;

    /**
     * 模块id,业务主键
     */
    private String referId;

    /**
     * 业务数据,json格式
     */
    private String dataDetail;

    /**
     * 有效标志
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
     * 更新人
     */
    private String updator;

    /**
     * 更新时间
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

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMcode() {
        return mcode;
    }

    public void setMcode(String mcode) {
        this.mcode = mcode;
    }

    public String getReferId() {
        return referId;
    }

    public void setReferId(String referId) {
        this.referId = referId;
    }


    public String getDataDetail() {
        return dataDetail;
    }

    public void setDataDetail(String dataDetail) {
        this.dataDetail = dataDetail;
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

}
