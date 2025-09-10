package com.jiuxi.admin.core.bean.vo;

import com.jiuxi.core.core.validator.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 表单设计表 表单设计表
 *
 * @author pand
 * @email
 * @date 2021-05-11 11:22:40
 */
public class TpCustomFormVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 表单id
     */
    private String fid;

    /**
     * 模块id
     */
    @NotBlank(message = "模块id不能为空！", groups = {UpdateGroup.class})
    private String mid;

    /**
     * 表单归属 app/pc
     */
    @NotBlank(message = "表单归属不能为空！", groups = {UpdateGroup.class})
    private String fSource;

    /**
     * 表单类型 list/edit/view
     */
    @NotBlank(message = "表单类型不能为空！", groups = {UpdateGroup.class})
    private String ftype;

    /**
     * 表单字段
     */
    @NotBlank(message = "表单字段不能为空！", groups = {UpdateGroup.class})
    private String fjson;

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


    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }


    public String getFSource() {
        return fSource;
    }

    public void setFSource(String fSource) {
        this.fSource = fSource;
    }


    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }


    public String getFjson() {
        return fjson;
    }

    public void setFjson(String fjson) {
        this.fjson = fjson;
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
