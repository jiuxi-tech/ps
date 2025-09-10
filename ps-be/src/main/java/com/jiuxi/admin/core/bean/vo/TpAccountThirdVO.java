package com.jiuxi.admin.core.bean.vo;

import com.jiuxi.core.core.annotation.SensitiveInfo;
import com.jiuxi.core.core.enums.SensitiveType;

import java.io.Serializable;

/**
 * 合作方管理表
 *
 * @author pand
 * @email
 * @date 2022-04-20 15:02:39
 */
public class TpAccountThirdVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * app key，主键
     */
    private String appKey;

    /**
     * app secret
     */
    @SensitiveInfo(type = SensitiveType.OTHER, start = 2, end = 30)
    private String appSecret;

    @SensitiveInfo(type = SensitiveType.OTHER, start = 2, end = 30)
    private String appSecretHide;

    private String appSecretShow;

    private String showLabel;

    private boolean showSecret = false;

    /**
     * 合作方名称
     */
    private String partnerName;

    /**
     * 合作方描述
     */
    private String partnerDesc;

    /**
     * 是否有效
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
     * 修改人
     */
    private String updator;

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

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }


    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }


    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }


    public String getPartnerDesc() {
        return partnerDesc;
    }

    public void setPartnerDesc(String partnerDesc) {
        this.partnerDesc = partnerDesc;
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

    public String getAppSecretHide() {
        return appSecretHide;
    }

    public void setAppSecretHide(String appSecretHide) {
        this.appSecretHide = appSecretHide;
    }

    public String getAppSecretShow() {
        return appSecretShow;
    }

    public void setAppSecretShow(String appSecretShow) {
        this.appSecretShow = appSecretShow;
    }

    public String getShowLabel() {
        return showLabel;
    }

    public void setShowLabel(String showLabel) {
        this.showLabel = showLabel;
    }

    public boolean isShowSecret() {
        return showSecret;
    }

    public void setShowSecret(boolean showSecret) {
        this.showSecret = showSecret;
    }
}
