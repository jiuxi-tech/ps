package com.jiuxi.platform.captcha.infrastructure.cache;

import com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge;
import org.springframework.stereotype.Repository;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 验证码缓存存储库
 * 
 * @author DDD Refactor  
 * @date 2025-09-11
 */
@Repository
public class CaptchaCacheRepository {
    
    private final ConcurrentHashMap<String, CaptchaChallenge> challengeCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> ticketCache = new ConcurrentHashMap<>();
    
    // 增强功能：统计信息
    private final AtomicLong totalChallengesGenerated = new AtomicLong(0);
    private final AtomicLong totalChallengesVerified = new AtomicLong(0);
    private final AtomicLong totalChallengesExpired = new AtomicLong(0);
    private final AtomicLong totalTicketsGenerated = new AtomicLong(0);
    private final AtomicLong totalTicketsConsumed = new AtomicLong(0);
    private final AtomicLong totalVerificationFailures = new AtomicLong(0);
    
    // 增强功能：失败统计（用于安全监控）
    private final ConcurrentHashMap<String, AtomicLong> ipFailureCount = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> ipLastFailureTime = new ConcurrentHashMap<>();
    
    // 增强功能：读写锁优化性能
    private final ReadWriteLock statisticsLock = new ReentrantReadWriteLock();
    
    // 配置参数
    private static final int MAX_FAILURE_COUNT = 5;
    private static final int FAILURE_BLOCK_MINUTES = 15;
    private static final int MAX_CACHE_SIZE = 10000;
    
    /**
     * 保存验证码挑战
     */
    public void saveChallenge(CaptchaChallenge challenge) {
        // 检查缓存大小，防止内存溢出
        if (challengeCache.size() >= MAX_CACHE_SIZE) {
            cleanupExpired();
            // 如果清理后仍然太大，移除最旧的一些条目
            if (challengeCache.size() >= MAX_CACHE_SIZE) {
                challengeCache.entrySet().stream()
                    .limit(MAX_CACHE_SIZE / 10) // 移除10%的条目
                    .map(Map.Entry::getKey)
                    .forEach(challengeCache::remove);
            }
        }
        
        challengeCache.put(challenge.getChallengeId(), challenge);
        totalChallengesGenerated.incrementAndGet();
    }
    
    /**
     * 获取验证码挑战
     */
    public CaptchaChallenge getChallenge(String challengeId) {
        CaptchaChallenge challenge = challengeCache.get(challengeId);
        if (challenge != null && challenge.isExpired()) {
            challengeCache.remove(challengeId);
            totalChallengesExpired.incrementAndGet();
            return null;
        }
        return challenge;
    }
    
    /**
     * 删除验证码挑战
     */
    public void removeChallenge(String challengeId) {
        challengeCache.remove(challengeId);
    }
    
    /**
     * 保存票据
     */
    public void saveTicket(String ticket, int expireMinutes) {
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(expireMinutes);
        ticketCache.put(ticket, expireTime);
        totalTicketsGenerated.incrementAndGet();
    }
    
    /**
     * 验证票据
     */
    public boolean verifyTicket(String ticket) {
        LocalDateTime expireTime = ticketCache.get(ticket);
        if (expireTime == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(expireTime)) {
            ticketCache.remove(ticket);
            return false;
        }
        
        return true;
    }
    
    /**
     * 使用票据（一次性使用）
     */
    public boolean consumeTicket(String ticket) {
        LocalDateTime expireTime = ticketCache.remove(ticket);
        if (expireTime == null) {
            return false;
        }
        
        boolean isValid = !LocalDateTime.now().isAfter(expireTime);
        if (isValid) {
            totalTicketsConsumed.incrementAndGet();
        }
        return isValid;
    }
    
    /**
     * 清理过期数据
     */
    public void cleanupExpired() {
        LocalDateTime now = LocalDateTime.now();
        
        // 清理过期的挑战
        long expiredChallenges = challengeCache.entrySet().stream()
            .filter(entry -> entry.getValue().isExpired())
            .peek(entry -> challengeCache.remove(entry.getKey()))
            .count();
        totalChallengesExpired.addAndGet(expiredChallenges);
        
        // 清理过期的票据
        ticketCache.entrySet().removeIf(entry -> 
            now.isAfter(entry.getValue()));
            
        // 清理过期的IP失败记录
        cleanupExpiredIpFailures();
    }
    
    /**
     * 自动清理过期数据（定时任务）
     */
    @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    public void scheduledCleanup() {
        cleanupExpired();
    }
    
    /**
     * 清理过期的IP失败记录
     */
    private void cleanupExpiredIpFailures() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(FAILURE_BLOCK_MINUTES);
        ipLastFailureTime.entrySet().removeIf(entry -> {
            if (entry.getValue().isBefore(threshold)) {
                ipFailureCount.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }
    
    /**
     * 记录验证失败
     */
    public void recordVerificationFailure(String clientIp) {
        totalVerificationFailures.incrementAndGet();
        
        if (clientIp != null && !clientIp.isEmpty()) {
            ipFailureCount.computeIfAbsent(clientIp, k -> new AtomicLong(0)).incrementAndGet();
            ipLastFailureTime.put(clientIp, LocalDateTime.now());
        }
    }
    
    /**
     * 检查IP是否被阻止
     */
    public boolean isIpBlocked(String clientIp) {
        if (clientIp == null || clientIp.isEmpty()) {
            return false;
        }
        
        AtomicLong failureCount = ipFailureCount.get(clientIp);
        if (failureCount == null || failureCount.get() < MAX_FAILURE_COUNT) {
            return false;
        }
        
        LocalDateTime lastFailure = ipLastFailureTime.get(clientIp);
        if (lastFailure == null) {
            return false;
        }
        
        return lastFailure.plusMinutes(FAILURE_BLOCK_MINUTES).isAfter(LocalDateTime.now());
    }
    
    /**
     * 重置IP失败计数
     */
    public void resetIpFailureCount(String clientIp) {
        if (clientIp != null && !clientIp.isEmpty()) {
            ipFailureCount.remove(clientIp);
            ipLastFailureTime.remove(clientIp);
        }
    }
    
    /**
     * 记录验证成功
     */
    public void recordVerificationSuccess(String clientIp) {
        totalChallengesVerified.incrementAndGet();
        
        // 验证成功后重置该IP的失败计数
        if (clientIp != null && !clientIp.isEmpty()) {
            resetIpFailureCount(clientIp);
        }
    }
    
    /**
     * 批量操作：获取多个挑战
     */
    public Map<String, CaptchaChallenge> getChallenges(java.util.List<String> challengeIds) {
        Map<String, CaptchaChallenge> result = new ConcurrentHashMap<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (String id : challengeIds) {
            CaptchaChallenge challenge = challengeCache.get(id);
            if (challenge != null) {
                if (challenge.isExpired()) {
                    challengeCache.remove(id);
                    totalChallengesExpired.incrementAndGet();
                } else {
                    result.put(id, challenge);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 获取缓存健康状态
     */
    public CacheHealthStatus getHealthStatus() {
        statisticsLock.readLock().lock();
        try {
            double memoryUsageRatio = (double)(challengeCache.size() + ticketCache.size()) / MAX_CACHE_SIZE;
            long totalOperations = totalChallengesGenerated.get() + totalTicketsGenerated.get();
            long failureOperations = totalVerificationFailures.get() + totalChallengesExpired.get();
            double failureRate = totalOperations > 0 ? (double)failureOperations / totalOperations : 0.0;
            
            CacheHealthStatus.HealthLevel level;
            if (memoryUsageRatio > 0.9 || failureRate > 0.5) {
                level = CacheHealthStatus.HealthLevel.CRITICAL;
            } else if (memoryUsageRatio > 0.7 || failureRate > 0.2) {
                level = CacheHealthStatus.HealthLevel.WARNING;
            } else {
                level = CacheHealthStatus.HealthLevel.HEALTHY;
            }
            
            return new CacheHealthStatus(level, memoryUsageRatio, failureRate, 
                challengeCache.size(), ticketCache.size(), ipFailureCount.size());
        } finally {
            statisticsLock.readLock().unlock();
        }
    }
    
    /**
     * 获取缓存统计信息
     */
    public CacheStatistics getStatistics() {
        statisticsLock.readLock().lock();
        try {
            return new CacheStatistics(
                challengeCache.size(), 
                ticketCache.size(),
                totalChallengesGenerated.get(),
                totalChallengesVerified.get(),
                totalChallengesExpired.get(),
                totalTicketsGenerated.get(),
                totalTicketsConsumed.get(),
                totalVerificationFailures.get(),
                ipFailureCount.size()
            );
        } finally {
            statisticsLock.readLock().unlock();
        }
    }
    
    /**
     * 获取详细统计报告
     */
    public String getDetailedStatisticsReport() {
        CacheStatistics stats = getStatistics();
        CacheHealthStatus health = getHealthStatus();
        
        StringBuilder report = new StringBuilder();
        report.append("=== 验证码缓存统计报告 ===\n");
        report.append(String.format("缓存状态: %s\n", health.getLevel()));
        report.append(String.format("当前挑战数: %d\n", stats.getChallengeCount()));
        report.append(String.format("当前票据数: %d\n", stats.getTicketCount()));
        report.append(String.format("总生成挑战数: %d\n", stats.getTotalChallengesGenerated()));
        report.append(String.format("总验证成功数: %d\n", stats.getTotalChallengesVerified()));
        report.append(String.format("总过期挑战数: %d\n", stats.getTotalChallengesExpired()));
        report.append(String.format("总生成票据数: %d\n", stats.getTotalTicketsGenerated()));
        report.append(String.format("总消费票据数: %d\n", stats.getTotalTicketsConsumed()));
        report.append(String.format("总验证失败数: %d\n", stats.getTotalVerificationFailures()));
        report.append(String.format("被限制IP数: %d\n", stats.getBlockedIpCount()));
        report.append(String.format("内存使用率: %.2f%%\n", health.getMemoryUsageRatio() * 100));
        report.append(String.format("失败率: %.2f%%\n", health.getFailureRate() * 100));
        
        return report.toString();
    }
    
    /**
     * 缓存统计信息
     */
    public static class CacheStatistics {
        private final int challengeCount;
        private final int ticketCount;
        private final long totalChallengesGenerated;
        private final long totalChallengesVerified;
        private final long totalChallengesExpired;
        private final long totalTicketsGenerated;
        private final long totalTicketsConsumed;
        private final long totalVerificationFailures;
        private final int blockedIpCount;
        
        public CacheStatistics(int challengeCount, int ticketCount, 
                              long totalChallengesGenerated, long totalChallengesVerified,
                              long totalChallengesExpired, long totalTicketsGenerated,
                              long totalTicketsConsumed, long totalVerificationFailures,
                              int blockedIpCount) {
            this.challengeCount = challengeCount;
            this.ticketCount = ticketCount;
            this.totalChallengesGenerated = totalChallengesGenerated;
            this.totalChallengesVerified = totalChallengesVerified;
            this.totalChallengesExpired = totalChallengesExpired;
            this.totalTicketsGenerated = totalTicketsGenerated;
            this.totalTicketsConsumed = totalTicketsConsumed;
            this.totalVerificationFailures = totalVerificationFailures;
            this.blockedIpCount = blockedIpCount;
        }
        
        // Getters
        public int getChallengeCount() { return challengeCount; }
        public int getTicketCount() { return ticketCount; }
        public long getTotalChallengesGenerated() { return totalChallengesGenerated; }
        public long getTotalChallengesVerified() { return totalChallengesVerified; }
        public long getTotalChallengesExpired() { return totalChallengesExpired; }
        public long getTotalTicketsGenerated() { return totalTicketsGenerated; }
        public long getTotalTicketsConsumed() { return totalTicketsConsumed; }
        public long getTotalVerificationFailures() { return totalVerificationFailures; }
        public int getBlockedIpCount() { return blockedIpCount; }
        
        @Override
        public String toString() {
            return String.format("CacheStatistics{challenges=%d, tickets=%d, generated=%d, verified=%d, expired=%d, failures=%d, blockedIps=%d}", 
                challengeCount, ticketCount, totalChallengesGenerated, totalChallengesVerified, 
                totalChallengesExpired, totalVerificationFailures, blockedIpCount);
        }
    }
    
    /**
     * 缓存健康状态
     */
    public static class CacheHealthStatus {
        public enum HealthLevel {
            HEALTHY, WARNING, CRITICAL
        }
        
        private final HealthLevel level;
        private final double memoryUsageRatio;
        private final double failureRate;
        private final int currentChallenges;
        private final int currentTickets;
        private final int blockedIps;
        
        public CacheHealthStatus(HealthLevel level, double memoryUsageRatio, double failureRate,
                                int currentChallenges, int currentTickets, int blockedIps) {
            this.level = level;
            this.memoryUsageRatio = memoryUsageRatio;
            this.failureRate = failureRate;
            this.currentChallenges = currentChallenges;
            this.currentTickets = currentTickets;
            this.blockedIps = blockedIps;
        }
        
        // Getters
        public HealthLevel getLevel() { return level; }
        public double getMemoryUsageRatio() { return memoryUsageRatio; }
        public double getFailureRate() { return failureRate; }
        public int getCurrentChallenges() { return currentChallenges; }
        public int getCurrentTickets() { return currentTickets; }
        public int getBlockedIps() { return blockedIps; }
        
        public boolean isHealthy() { return level == HealthLevel.HEALTHY; }
        public boolean needsAttention() { return level != HealthLevel.HEALTHY; }
        
        @Override
        public String toString() {
            return String.format("CacheHealthStatus{level=%s, memoryRatio=%.2f%%, failureRate=%.2f%%, challenges=%d, tickets=%d, blockedIps=%d}",
                level, memoryUsageRatio * 100, failureRate * 100, currentChallenges, currentTickets, blockedIps);
        }
    }
}