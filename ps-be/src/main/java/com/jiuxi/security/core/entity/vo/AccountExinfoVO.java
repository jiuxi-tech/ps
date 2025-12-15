package com.jiuxi.security.core.entity.vo;

import com.jiuxi.shared.common.base.vo.BaseVO;

import java.io.Serializable;

/**
 * @Description: 账号扩展表信息
 * @ClassName: AccountExinfoVO
 * @Author: pdd
 * @Date: 2020-09-08 15:27
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class AccountExinfoVO extends BaseVO implements Serializable {

    /**
     * 账号id
     */
    private String accountId;

    /**
     * 错误次数
     */
    private Integer errCount;

    /**
     * 最后一次登陆密码错误时间
     */
    private String lastErrTime;

    /**
     * 最后一次登陆时间
     */
    private String lastLoginTime;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Integer getErrCount() {
        return errCount;
    }

    public void setErrCount(Integer errCount) {
        this.errCount = errCount;
    }

    public String getLastErrTime() {
        return lastErrTime;
    }

    public void setLastErrTime(String lastErrTime) {
        this.lastErrTime = lastErrTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
