package com.jiuxi.platform.captcha.domain.entity;

import com.jiuxi.platform.captcha.domain.valueobject.CaptchaCoordinate;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 验证码挑战实体
 * 表示一次验证码挑战的完整信息
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class CaptchaChallenge {
    
    private String challengeId;
    private CaptchaType captchaType;
    private String backgroundImageData;
    private String puzzleImageData;
    private CaptchaCoordinate correctPosition;
    private double tolerance;
    private LocalDateTime createTime;
    private LocalDateTime expireTime;
    private boolean completed;
    private boolean verified;
    private int attemptCount;
    private int maxAttempts;
    private Map<String, Object> metadata;
    
    // 构造函数
    public CaptchaChallenge() {
        this.challengeId = generateChallengeId();
        this.createTime = LocalDateTime.now();
        this.completed = false;
        this.verified = false;
        this.attemptCount = 0;
        this.maxAttempts = 3; // 默认最大尝试次数
        this.metadata = new HashMap<>();
    }
    
    public CaptchaChallenge(CaptchaType captchaType) {
        this();
        this.captchaType = captchaType;
        this.tolerance = captchaType.getDefaultTolerance();
        this.expireTime = this.createTime.plusSeconds(captchaType.getDefaultExpirationSeconds());
    }
    
    /**
     * 生成唯一的挑战ID
     */
    private String generateChallengeId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * 验证用户提交的答案
     */
    public boolean verifyAnswer(CaptchaCoordinate userAnswer) {
        if (isExpired() || isCompleted() || hasExceededMaxAttempts()) {
            return false;
        }
        
        attemptCount++;
        
        // 对于滑块验证码，只验证X坐标
        if (correctPosition != null && captchaType == CaptchaType.SLIDER) {
            if (correctPosition.isXWithinTolerance(userAnswer, tolerance)) {
                this.verified = true;
                this.completed = true;
                return true;
            }
        } else if (correctPosition != null && correctPosition.isWithinTolerance(userAnswer, tolerance)) {
            // 其他类型验证码使用原有的X+Y验证逻辑
            this.verified = true;
            this.completed = true;
            return true;
        }
        
        if (attemptCount >= maxAttempts) {
            this.completed = true;
        }
        
        return false;
    }
    
    /**
     * 验证角度答案（用于旋转验证码）
     */
    public boolean verifyRotationAnswer(double userAngle, double correctAngle) {
        if (isExpired() || isCompleted() || hasExceededMaxAttempts()) {
            return false;
        }
        
        attemptCount++;
        
        double angleDifference = Math.abs(userAngle - correctAngle);
        // 处理角度的循环性质（例如 359° 和 1° 的差异应该是 2°）
        angleDifference = Math.min(angleDifference, 360 - angleDifference);
        
        if (angleDifference <= tolerance) {
            this.verified = true;
            this.completed = true;
            return true;
        }
        
        if (attemptCount >= maxAttempts) {
            this.completed = true;
        }
        
        return false;
    }
    
    /**
     * 检查是否过期
     */
    public boolean isExpired() {
        return expireTime != null && LocalDateTime.now().isAfter(expireTime);
    }
    
    /**
     * 检查是否超过最大尝试次数
     */
    public boolean hasExceededMaxAttempts() {
        return attemptCount >= maxAttempts;
    }
    
    /**
     * 检查是否可以继续尝试
     */
    public boolean canAttempt() {
        return !isExpired() && !isCompleted() && !hasExceededMaxAttempts();
    }
    
    /**
     * 获取剩余尝试次数
     */
    public int getRemainingAttempts() {
        return Math.max(0, maxAttempts - attemptCount);
    }
    
    /**
     * 获取剩余有效时间（秒）
     */
    public long getRemainingSeconds() {
        if (expireTime == null) {
            return -1;
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expireTime)) {
            return 0;
        }
        
        return java.time.Duration.between(now, expireTime).getSeconds();
    }
    
    /**
     * 设置元数据
     */
    public void setMetadata(String key, Object value) {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        metadata.put(key, value);
    }
    
    /**
     * 获取元数据
     */
    public Object getMetadata(String key) {
        return metadata != null ? metadata.get(key) : null;
    }
    
    /**
     * 生成票据（验证成功后返回给客户端）
     */
    public String generateTicket() {
        if (!verified) {
            throw new IllegalStateException("只有验证成功的挑战才能生成票据");
        }
        
        // 简单的票据格式：challengeId + 时间戳 + 随机值
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().substring(0, 8);
        return challengeId + "_" + timestamp + "_" + random;
    }
    
    // Getters and Setters
    public String getChallengeId() { return challengeId; }
    public void setChallengeId(String challengeId) { this.challengeId = challengeId; }
    
    public CaptchaType getCaptchaType() { return captchaType; }
    public void setCaptchaType(CaptchaType captchaType) { this.captchaType = captchaType; }
    
    public String getBackgroundImageData() { return backgroundImageData; }
    public void setBackgroundImageData(String backgroundImageData) { this.backgroundImageData = backgroundImageData; }
    
    public String getPuzzleImageData() { return puzzleImageData; }
    public void setPuzzleImageData(String puzzleImageData) { this.puzzleImageData = puzzleImageData; }
    
    public CaptchaCoordinate getCorrectPosition() { return correctPosition; }
    public void setCorrectPosition(CaptchaCoordinate correctPosition) { this.correctPosition = correctPosition; }
    
    public double getTolerance() { return tolerance; }
    public void setTolerance(double tolerance) { this.tolerance = tolerance; }
    
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    
    public LocalDateTime getExpireTime() { return expireTime; }
    public void setExpireTime(LocalDateTime expireTime) { this.expireTime = expireTime; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    
    public int getAttemptCount() { return attemptCount; }
    public void setAttemptCount(int attemptCount) { this.attemptCount = attemptCount; }
    
    public int getMaxAttempts() { return maxAttempts; }
    public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    @Override
    public String toString() {
        return String.format("CaptchaChallenge{challengeId='%s', captchaType=%s, verified=%s, attemptCount=%d, expired=%s}", 
                challengeId, captchaType, verified, attemptCount, isExpired());
    }
}