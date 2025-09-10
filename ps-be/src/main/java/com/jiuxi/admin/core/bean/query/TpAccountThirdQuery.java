package com.jiuxi.admin.core.bean.query;

// import com.jiuxi.easyexcel.bean.query.DownloadQuery;

import java.io.Serializable;


/**
 * 合作方管理表
 *
 * @author pand
 * @email
 * @date 2022-04-20 15:02:39
 */
public class TpAccountThirdQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * app key,主键
     */
    private String appKey;

    /**
     * app secret
     */
    private String appSecret;

    /**
     * 合作方名称
     */
    private String partnerName;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;


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
