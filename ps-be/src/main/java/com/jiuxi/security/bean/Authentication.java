package com.jiuxi.security.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: Authentication
 * @Description: 认证配置类
 * @Author: 杨攀
 * @Date: 2020/5/25 14:08
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class Authentication {

    private static final Logger LOGGER = LoggerFactory.getLogger(Authentication.class);

    /**
     * 认证排查的请求
     */
    private String[] excludePaths = {};

    /**
     * 密码策略配置
     */
    private PasswordPolicyProperties passwordPolicy = new PasswordPolicyProperties();

    /**
     * 账户锁定策略配置
     */
    private AccountLockoutProperties accountLockout = new AccountLockoutProperties();

    /**
     * 密码登陆错误次数，默认5，即错误5次被冻结，30分钟后可重试一次。
     * @deprecated 请使用 {@link #accountLockout} 中的 maxAttempts 替代
     * 该字段将在下一个主版本中移除
     */
    @Deprecated
    private int errCount = 5;

    /**
     * 自动解除账号冻结时间，默认: 单位分钟，30分钟，即账号冻结30分钟之后，可以再次尝试登陆。
     * @deprecated 请使用 {@link #accountLockout} 中的 lockoutDuration 替代
     * 该字段将在下一个主版本中移除
     */
    @Deprecated
    private int deblocking = 30;

    /**
     * 自动解除账号冻结，允许错误的最大次数。默认30，即账号被冻结后，还可以每半个小时试一次，最大次数不超过 maxErrCount - errCount 次。即默认 25 次。
     * @deprecated 请使用 {@link #accountLockout} 中的 maxTotalAttempts 替代
     * 该字段将在下一个主版本中移除
     */
    @Deprecated
    private int maxErrCount = 30;

    /**
     * 默认密码复杂度规则校验正则表达式
     * // 8位以上，包含数字，字母，特殊符号
     * ^(?=.*\d)(?=.*[a-z])(?=.*[~!@#$%^&*])[\da-zA-Z~!@#$%^&*]{8,}$
     *
     * // 8位以上，包含数字，字母或特殊符号
     * ^(?![0-9]+$)(?![a-zA-Z]+$)(?![\W]+$)[a-zA-Z0-9\W]{8,}$
     * @deprecated 请使用 {@link #passwordPolicy} 中的复杂度配置替代
     * 该字段将在下一个主版本中移除
     */
    @Deprecated
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

    /**
     * @deprecated 请使用 {@link #getAccountLockout()} 中的 getMaxAttempts() 替代
     */
    @Deprecated
    public int getErrCount() {
        logDeprecationWarning("errCount", "accountLockout.maxAttempts");
        return errCount;
    }

    /**
     * @deprecated 请使用 {@link #getAccountLockout()} 中的 setMaxAttempts() 替代
     */
    @Deprecated
    public void setErrCount(int errCount) {
        logDeprecationWarning("errCount", "accountLockout.maxAttempts");
        this.errCount = errCount;
        // 同步到新配置
        this.accountLockout.setMaxAttempts(errCount);
    }

    /**
     * @deprecated 请使用 {@link #getAccountLockout()} 中的 getLockoutDuration() 替代
     */
    @Deprecated
    public int getDeblocking() {
        logDeprecationWarning("deblocking", "accountLockout.lockoutDuration");
        return deblocking;
    }

    /**
     * @deprecated 请使用 {@link #getAccountLockout()} 中的 setLockoutDuration() 替代
     */
    @Deprecated
    public void setDeblocking(int deblocking) {
        logDeprecationWarning("deblocking", "accountLockout.lockoutDuration");
        this.deblocking = deblocking;
        // 同步到新配置
        this.accountLockout.setLockoutDuration(deblocking);
    }

    /**
     * @deprecated 请使用 {@link #getAccountLockout()} 中的 getMaxTotalAttempts() 替代
     */
    @Deprecated
    public int getMaxErrCount() {
        logDeprecationWarning("maxErrCount", "accountLockout.maxTotalAttempts");
        return maxErrCount;
    }

    /**
     * @deprecated 请使用 {@link #getAccountLockout()} 中的 setMaxTotalAttempts() 替代
     */
    @Deprecated
    public void setMaxErrCount(int maxErrCount) {
        logDeprecationWarning("maxErrCount", "accountLockout.maxTotalAttempts");
        this.maxErrCount = maxErrCount;
        // 同步到新配置
        this.accountLockout.setMaxTotalAttempts(maxErrCount);
    }

    /**
     * @deprecated 请使用 {@link #getPasswordPolicy()} 中的复杂度配置替代
     */
    @Deprecated
    public String getRegular() {
        logDeprecationWarning("regular", "passwordPolicy.complexity");
        return regular;
    }

    /**
     * @deprecated 请使用 {@link #getPasswordPolicy()} 中的复杂度配置替代
     */
    @Deprecated
    public void setRegular(String regular) {
        logDeprecationWarning("regular", "passwordPolicy.complexity");
        this.regular = regular;
        // 如果设置了自定义正则，同步到新配置
        this.passwordPolicy.getComplexity().setCustomRegex(regular);
    }

    /**
     * 记录配置废弃警告日志
     */
    private void logDeprecationWarning(String oldConfig, String newConfig) {
        LOGGER.warn("检测到使用已废弃的配置项 'topinfo.security.authentication.{}', " +
                "请迁移至 'topinfo.security.authentication.{}'。" +
                "旧配置将在下一个主版本中移除。", oldConfig, newConfig);
    }

    public PasswordPolicyProperties getPasswordPolicy() {
        return passwordPolicy;
    }

    public void setPasswordPolicy(PasswordPolicyProperties passwordPolicy) {
        this.passwordPolicy = passwordPolicy;
    }

    public AccountLockoutProperties getAccountLockout() {
        return accountLockout;
    }

    public void setAccountLockout(AccountLockoutProperties accountLockout) {
        this.accountLockout = accountLockout;
    }

    public boolean isCheckLogoutToken() {
        return checkLogoutToken;
    }

    public void setCheckLogoutToken(boolean checkLogoutToken) {
        this.checkLogoutToken = checkLogoutToken;
    }
}
