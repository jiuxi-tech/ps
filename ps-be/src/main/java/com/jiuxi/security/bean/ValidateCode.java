package com.jiuxi.security.bean;

/**
 * @Description: 验证码
 * @ClassName: ValidateCode
 * @Author: pdd
 * @Date: 2020-08-26 11:19
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class ValidateCode {

    private int length = 6;
    /**
     * 默认失效时间300秒
     */
    private int expireIn = 300;

    public int getLength() {
        return length;
    }

    public void setLength(int lenght) {
        this.length = lenght;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }
}
