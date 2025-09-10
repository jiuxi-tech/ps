package com.jiuxi.security.core.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuxi.core.bean.BaseVO;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 合作方信息
 * @ClassName: AccountThredVO
 * @Author: pand
 * @Date: 2020-08-27 09:37
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class AccountThirdVO extends BaseVO implements Serializable {

    private static final long serialVersionUID = -5731609031276947720L;

    /**
     * appKey
     */
    @JsonIgnore
    private String appKey;

    /**
     * app_secret
     */
    private String appSecret;

    /**
     * 合作方名称
     */
    private String partnerName;

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
}
