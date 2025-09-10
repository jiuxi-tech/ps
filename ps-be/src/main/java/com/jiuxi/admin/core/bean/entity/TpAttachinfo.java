package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("tp_attachinfo")
public class TpAttachinfo implements Serializable {
    /**
     * "附件标识",
     * "attachId"
     */
    private String attachId;
    /**
     * "附件标题"
     */
    private String attachTitle;
    /**
     * 附件名
     */
    private String attachName;
    /**
     * 关联业务类别",
     * "referType",
     * "字典编码：SYS10"
     */
    private String referType;
    /**
     * "关联业务主键",
     * name = "referId"
     */
    private String referId;
    /**
     * "附件类型",
     * name = "attachTypeCode",
     * notes = "字典编码：SYS04"
     */
    private String attachTypeCode;
    /**
     * "存放路径",
     * name = "savePath",
     * notes = "文件服务器提供的唯一位置或编号
     */
    private String savePath;
    /**
     * "备用存放路径1",
     * name = "savePathBak1"
     */
    private String savePathBak1;
    /**
     * "备用存放路径2",
     * name = "savePathBak2"
     */
    private String savePathBak2;
    /**
     * "附件大小（KB）",
     * name = "attachSize"
     */
    private Double attachSize;

    /**
     * "排序号",
     * "orderIndex",
     * "一种业务类别有多个附件时，各个附件的排序号
     */
    private Integer orderIndex;

    /**
     * "扩展字段01",
     * "extend01",
     * "冗余，用于业务扩展
     */
    private String extend01;
    /**
     * "扩展字段02",
     * "extend02",
     * "冗余，用于业务扩展
     */
    private String extend02;
    /**
     * 扩展字段03",
     * "extend03",
     * "冗余，用于业务扩展
     */
    private String extend03;

    /**
     * "是否有效",
     * "isActive"
     */
    private String actived;

    /**
     * 创建人|FK:S_OG_PERSONINFO.PERSON_ID
     */
    private String creator;
    /**
     * 创建日期|格式：YYYYMMDDHHMMSS
     */
    private String createTime;
    /**
     * 修改人|FK:S_OG_PERSONINFO.PERSON_ID
     */
    private String updator;
    /**
     * 修改日期|格式：YYYYMMDDHHMMSS
     */
    private String updateTime;


    public TpAttachinfo() {
    }

    public String getAttachId() {
        return this.attachId;
    }

    public String getAttachTitle() {
        return this.attachTitle;
    }

    public String getAttachName() {
        return this.attachName;
    }

    public String getReferType() {
        return this.referType;
    }

    public String getReferId() {
        return this.referId;
    }

    public String getAttachTypeCode() {
        return this.attachTypeCode;
    }

    public String getSavePath() {
        return this.savePath;
    }

    public String getSavePathBak1() {
        return this.savePathBak1;
    }

    public String getSavePathBak2() {
        return this.savePathBak2;
    }

    public Double getAttachSize() {
        return this.attachSize;
    }

    public Integer getOrderIndex() {
        return this.orderIndex;
    }

    public void setAttachId(final String attachId) {
        this.attachId = attachId;
    }

    public void setAttachTitle(final String attachTitle) {
        this.attachTitle = attachTitle;
    }

    public void setAttachName(final String attachName) {
        this.attachName = attachName;
    }

    public void setReferType(final String referType) {
        this.referType = referType;
    }

    public void setReferId(final String referId) {
        this.referId = referId;
    }

    public void setAttachTypeCode(final String attachTypeCode) {
        this.attachTypeCode = attachTypeCode;
    }

    public void setSavePath(final String savePath) {
        this.savePath = savePath;
    }

    public void setSavePathBak1(final String savePathBak1) {
        this.savePathBak1 = savePathBak1;
    }

    public void setSavePathBak2(final String savePathBak2) {
        this.savePathBak2 = savePathBak2;
    }

    public void setAttachSize(final Double attachSize) {
        this.attachSize = attachSize;
    }

    public void setOrderIndex(final Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getActived() {
        return actived;
    }

    public void setActived(String actived) {
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
}
