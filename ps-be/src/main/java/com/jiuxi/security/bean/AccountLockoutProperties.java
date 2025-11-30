package com.jiuxi.security.bean;

/**
 * 账户锁定策略配置类
 * 用于配置账户登录失败锁定相关策略
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public class AccountLockoutProperties {

    /**
     * 是否启用账户锁定功能，默认启用
     */
    private boolean enabled = true;

    /**
     * 最大连续错误次数，默认5次
     * 达到此次数后账户将被锁定
     */
    private int maxAttempts = 5;

    /**
     * 锁定时长（分钟），默认30分钟
     * 临时锁定模式下，超过此时间后账户自动解锁
     */
    private int lockoutDuration = 30;

    /**
     * 累计最大错误次数，默认30次
     * 超过此次数后需要管理员手动解锁
     */
    private int maxTotalAttempts = 30;

    /**
     * 错误次数重置时间（分钟），默认60分钟
     * 成功登录后，错误次数将被重置
     * 该配置用于扩展功能，当前版本登录成功立即重置
     */
    private int resetAttemptsAfter = 60;

    /**
     * 锁定类型，默认"temporary"（临时锁定）
     * - temporary: 临时锁定，超过lockoutDuration时间后自动解锁
     * - permanent: 永久锁定，需要管理员手动解锁
     */
    private String lockoutType = "temporary";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getLockoutDuration() {
        return lockoutDuration;
    }

    public void setLockoutDuration(int lockoutDuration) {
        this.lockoutDuration = lockoutDuration;
    }

    public int getMaxTotalAttempts() {
        return maxTotalAttempts;
    }

    public void setMaxTotalAttempts(int maxTotalAttempts) {
        this.maxTotalAttempts = maxTotalAttempts;
    }

    public int getResetAttemptsAfter() {
        return resetAttemptsAfter;
    }

    public void setResetAttemptsAfter(int resetAttemptsAfter) {
        this.resetAttemptsAfter = resetAttemptsAfter;
    }

    public String getLockoutType() {
        return lockoutType;
    }

    public void setLockoutType(String lockoutType) {
        this.lockoutType = lockoutType;
    }
}
