package com.jiuxi.security.core.entity.vo;

import java.time.LocalDateTime;

/**
 * 账户锁定信息数据模型
 * 用于封装账户锁定状态和相关信息
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public class LockoutInfo {

    /**
     * 账户是否被锁定
     */
    private boolean locked;

    /**
     * 锁定时间
     */
    private LocalDateTime lockoutTime;

    /**
     * 解锁时间（临时锁定时有效）
     */
    private LocalDateTime unlockTime;

    /**
     * 失败次数
     */
    private int failureCount;

    /**
     * 剩余可尝试次数
     */
    private int remainingAttempts;

    /**
     * 锁定类型：temporary=临时锁定, permanent=永久锁定
     */
    private String lockoutType;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 最后一次错误时间
     */
    private LocalDateTime lastErrorTime;

    public LockoutInfo() {
    }

    public LockoutInfo(boolean locked) {
        this.locked = locked;
    }

    /**
     * 创建未锁定状态的信息
     */
    public static LockoutInfo unlocked() {
        return new LockoutInfo(false);
    }

    /**
     * 创建锁定状态的信息
     */
    public static LockoutInfo locked(LocalDateTime lockoutTime, LocalDateTime unlockTime, String lockoutType) {
        LockoutInfo info = new LockoutInfo(true);
        info.setLockoutTime(lockoutTime);
        info.setUnlockTime(unlockTime);
        info.setLockoutType(lockoutType);
        return info;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getLockoutTime() {
        return lockoutTime;
    }

    public void setLockoutTime(LocalDateTime lockoutTime) {
        this.lockoutTime = lockoutTime;
    }

    public LocalDateTime getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(LocalDateTime unlockTime) {
        this.unlockTime = unlockTime;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public void setRemainingAttempts(int remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }

    public String getLockoutType() {
        return lockoutType;
    }

    public void setLockoutType(String lockoutType) {
        this.lockoutType = lockoutType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getLastErrorTime() {
        return lastErrorTime;
    }

    public void setLastErrorTime(LocalDateTime lastErrorTime) {
        this.lastErrorTime = lastErrorTime;
    }

    @Override
    public String toString() {
        return "LockoutInfo{" +
                "locked=" + locked +
                ", lockoutTime=" + lockoutTime +
                ", unlockTime=" + unlockTime +
                ", failureCount=" + failureCount +
                ", remainingAttempts=" + remainingAttempts +
                ", lockoutType='" + lockoutType + '\'' +
                ", accountId='" + accountId + '\'' +
                ", lastErrorTime=" + lastErrorTime +
                '}';
    }
}
