package com.jiuxi.security.bean;

/**
 * @ClassName: Authentication
 * @Description: 认证
 * @Author: 杨攀
 * @Date: 2020/5/25 14:08
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class Authentication {

    /**
     * 认证排查的请求
     */
    private String[] excludePaths = {};

    /**
     * 密码登陆错误次数，默认5，即错误5次被冻结，30分钟后可重试一次。
     */
    private int errCount = 5;

    /**
     * 自动解除账号冻结时间，默认: 单位分钟，30分钟，即账号冻结30分钟之后，可以再次尝试登陆。
     */
    private int deblocking = 30;

    /**
     * 自动解除账号冻结，允许错误的最大次数。默认30，即账号被冻结后，还可以每半个小时试一次，最大次数不超过 maxErrCount - errCount 次。即默认 25 次。
     */
    private int maxErrCount = 30;

    /**
     * 默认密码负责的规则校验
     * // 8位以上，包含数字，字母，特殊符号
     * ^(?=.*\d)(?=.*[a-z])(?=.*[~!@#$%^&*])[\da-zA-Z~!@#$%^&*]{8,}$
     *
     * // 8位以上，包含数字，字母或特殊符号
     * ^(?![0-9]+$)(?![a-zA-Z]+$)(?![\W]+$)[a-zA-Z0-9\W]{8,}$
     */
    private String regular = "^(?=.*\\d)(?=.*[a-z])(?=.*[~!@#$%^&*])[\\da-zA-Z~!@#$%^&*]{8,}$";

    /**
     * token 刷新过期时间间隔，单位分钟，默认： 2 小时
     */
    private int tokenTimeOut = 120;

    /**
     * 是否校验当前token已经退出登录，默认false
     */
    private boolean checkLogoutToken = false;

    public String[] getExcludePaths() {
        return excludePaths;
    }

    public void setExcludePaths(String[] excludePaths) {
        this.excludePaths = excludePaths;
    }

    public int getTokenTimeOut() {
        return tokenTimeOut;
    }

    public void setTokenTimeOut(int tokenTimeOut) {
        this.tokenTimeOut = tokenTimeOut;
    }

    public int getErrCount() {
        return errCount;
    }

    public void setErrCount(int errCount) {
        this.errCount = errCount;
    }

    public int getDeblocking() {
        return deblocking;
    }

    public void setDeblocking(int deblocking) {
        this.deblocking = deblocking;
    }

    public int getMaxErrCount() {
        return maxErrCount;
    }

    public void setMaxErrCount(int maxErrCount) {
        this.maxErrCount = maxErrCount;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public boolean isCheckLogoutToken() {
        return checkLogoutToken;
    }

    public void setCheckLogoutToken(boolean checkLogoutToken) {
        this.checkLogoutToken = checkLogoutToken;
    }
}
