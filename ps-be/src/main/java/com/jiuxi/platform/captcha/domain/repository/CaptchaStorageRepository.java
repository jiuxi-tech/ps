package com.jiuxi.platform.captcha.domain.repository;

import com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 验证码存储仓储接口
 * 支持分布式存储策略
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public interface CaptchaStorageRepository {
    
    /**
     * 保存验证码挑战
     */
    void saveChallenge(CaptchaChallenge challenge);
    
    /**
     * 获取验证码挑战
     */
    CaptchaChallenge getChallenge(String challengeId);
    
    /**
     * 删除验证码挑战
     */
    void removeChallenge(String challengeId);
    
    /**
     * 批量获取验证码挑战
     */
    Map<String, CaptchaChallenge> getChallenges(List<String> challengeIds);
    
    /**
     * 保存票据
     */
    void saveTicket(String ticket, int expireMinutes);
    
    /**
     * 验证票据
     */
    boolean verifyTicket(String ticket);
    
    /**
     * 使用票据（一次性）
     */
    boolean consumeTicket(String ticket);
    
    /**
     * 清理过期数据
     */
    void cleanupExpired();
    
    /**
     * 记录验证失败
     */
    void recordVerificationFailure(String clientIp);
    
    /**
     * 检查IP是否被阻止
     */
    boolean isIpBlocked(String clientIp);
    
    /**
     * 记录验证成功
     */
    void recordVerificationSuccess(String clientIp);
    
    /**
     * 获取存储统计信息
     */
    StorageStatistics getStatistics();
    
    /**
     * 检查存储连接状态
     */
    boolean isHealthy();
    
    /**
     * 存储统计信息
     */
    class StorageStatistics {
        private final long totalChallenges;
        private final long totalTickets;
        private final long totalFailures;
        private final double hitRate;
        private final long memoryUsage;
        
        public StorageStatistics(long totalChallenges, long totalTickets, long totalFailures, 
                               double hitRate, long memoryUsage) {
            this.totalChallenges = totalChallenges;
            this.totalTickets = totalTickets;
            this.totalFailures = totalFailures;
            this.hitRate = hitRate;
            this.memoryUsage = memoryUsage;
        }
        
        public long getTotalChallenges() { return totalChallenges; }
        public long getTotalTickets() { return totalTickets; }
        public long getTotalFailures() { return totalFailures; }
        public double getHitRate() { return hitRate; }
        public long getMemoryUsage() { return memoryUsage; }
        
        @Override
        public String toString() {
            return String.format("StorageStatistics{challenges=%d, tickets=%d, failures=%d, hitRate=%.2f%%, memory=%d}",
                totalChallenges, totalTickets, totalFailures, hitRate * 100, memoryUsage);
        }
    }
}