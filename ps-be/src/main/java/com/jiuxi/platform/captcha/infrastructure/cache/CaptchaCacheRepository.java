package com.jiuxi.platform.captcha.infrastructure.cache;

import com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

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
    
    /**
     * 保存验证码挑战
     */
    public void saveChallenge(CaptchaChallenge challenge) {
        challengeCache.put(challenge.getChallengeId(), challenge);
    }
    
    /**
     * 获取验证码挑战
     */
    public CaptchaChallenge getChallenge(String challengeId) {
        CaptchaChallenge challenge = challengeCache.get(challengeId);
        if (challenge != null && challenge.isExpired()) {
            challengeCache.remove(challengeId);
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
        
        return !LocalDateTime.now().isAfter(expireTime);
    }
    
    /**
     * 清理过期数据
     */
    public void cleanupExpired() {
        LocalDateTime now = LocalDateTime.now();
        
        // 清理过期的挑战
        challengeCache.entrySet().removeIf(entry -> 
            entry.getValue().isExpired());
        
        // 清理过期的票据
        ticketCache.entrySet().removeIf(entry -> 
            now.isAfter(entry.getValue()));
    }
    
    /**
     * 获取缓存统计信息
     */
    public CacheStatistics getStatistics() {
        return new CacheStatistics(challengeCache.size(), ticketCache.size());
    }
    
    /**
     * 缓存统计信息
     */
    public static class CacheStatistics {
        private final int challengeCount;
        private final int ticketCount;
        
        public CacheStatistics(int challengeCount, int ticketCount) {
            this.challengeCount = challengeCount;
            this.ticketCount = ticketCount;
        }
        
        public int getChallengeCount() { return challengeCount; }
        public int getTicketCount() { return ticketCount; }
        
        @Override
        public String toString() {
            return String.format("CacheStatistics{challenges=%d, tickets=%d}", challengeCount, ticketCount);
        }
    }
}