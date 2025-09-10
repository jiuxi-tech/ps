package com.jiuxi.security.core.entity.vo;

import com.jiuxi.core.bean.BaseVO;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description: 验证码定义
 * @ClassName: ValidateCode
 * @Author: pand
 * @Date: 2020-08-26 11:06
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class ValidateCodeVO extends BaseVO implements Serializable {

    private static final long serialVersionUID = 7968888596129307790L;

    /**
     * 验证码的值
     */
    private String code;

    /**
     * 验证码过期时间
     */
    private LocalDateTime expireTime;

    public ValidateCodeVO(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ValidateCodeVO(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
