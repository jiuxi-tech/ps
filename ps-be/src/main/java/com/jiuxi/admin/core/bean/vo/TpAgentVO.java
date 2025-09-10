package com.jiuxi.admin.core.bean.vo;

import com.jiuxi.core.core.validator.group.AddGroup;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author yangp
 * @email
 * @date 2021-03-24 16:04:29
 */
public class TpAgentVO implements Serializable {


    private static final long serialVersionUID = -8852487880643926626L;

    /**
     * 代办id
     */
    private String agId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = AddGroup.class)
    private String agTitle;

    /**
     * 消息类型，o2o/o2e/e2e
     * <p>
     * o2o的时候：
     * 接收消息对象agTo : 单位/部门id/个人id
     * 消息范围 agScope :  本级/本级及下级/个人
     * o2e的时候：
     * 接收消息对象agTo :  企业id
     * 消息范围 agScope :  群发（all）
     * e2e的时候：
     * 接收消息对象agTo : 单位id/部门id/个人id
     * 消息范围 agScope :  本级/本级及下级/个人
     */
    @NotBlank(message = "消息类型不能为空", groups = AddGroup.class)
    private String agType;

    /**
     * 范围，即单发还是群发（all/行业/本级/个人/本级及下级单位）
     */
    @NotBlank(message = "范围不能为空", groups = AddGroup.class)
    private String agScope;

    /**
     * 发件人
     */
    @NotBlank(message = "发件人不能为空", groups = AddGroup.class)
    private String agFrom;

    /**
     * 发件人名称
     */
    @NotBlank(message = "发件人名称不能为空", groups = AddGroup.class)
    private String agFromName;

    /**
     * 消息收件人
     */
    @NotBlank(message = "消息收件人不能为空", groups = AddGroup.class)
    private String agTo;

    /**
     * 消息收件人名称
     */
    @NotBlank(message = "消息收件人名称不能为空", groups = AddGroup.class)
    private String agToName;

    /**
     * 消息内容
     */
    private String agContext;

    /**
     * 业务id
     */
    @NotBlank(message = "业务id不能为空", groups = AddGroup.class)
    private String moduleId;

    /**
     * 业务模块
     */
    @NotBlank(message = "业务模块不能为空", groups = AddGroup.class)
    private String moduleType;

    /**
     * 是否有效
     */
    private Integer actived;

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
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 修改人
     */
    private String updator;


    public String getAgId() {
        return agId;
    }

    public void setAgId(String agId) {
        this.agId = agId;
    }


    public String getAgTitle() {
        return agTitle;
    }

    public void setAgTitle(String agTitle) {
        this.agTitle = agTitle;
    }


    public String getAgType() {
        return agType;
    }

    public void setAgType(String agType) {
        this.agType = agType;
    }


    public String getAgScope() {
        return agScope;
    }

    public void setAgScope(String agScope) {
        this.agScope = agScope;
    }


    public String getAgFrom() {
        return agFrom;
    }

    public void setAgFrom(String agFrom) {
        this.agFrom = agFrom;
    }


    public String getAgFromName() {
        return agFromName;
    }

    public void setAgFromName(String agFromName) {
        this.agFromName = agFromName;
    }


    public String getAgTo() {
        return agTo;
    }

    public void setAgTo(String agTo) {
        this.agTo = agTo;
    }


    public String getAgToName() {
        return agToName;
    }

    public void setAgToName(String agToName) {
        this.agToName = agToName;
    }


    public String getAgContext() {
        return agContext;
    }

    public void setAgContext(String agContext) {
        this.agContext = agContext;
    }


    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }


    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }


    public Integer getActived() {
        return actived;
    }

    public void setActived(Integer actived) {
        this.actived = actived;
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


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

}
