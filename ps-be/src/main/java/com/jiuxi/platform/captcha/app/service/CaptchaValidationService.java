package com.jiuxi.platform.captcha.app.service;

import com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaCoordinate;
import com.jiuxi.platform.captcha.infrastructure.cache.CaptchaCacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 验证码验证服务
 * 增强验证失败处理和统计功能
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class CaptchaValidationService {
    
    private static final Logger logger = LoggerFactory.getLogger(CaptchaValidationService.class);
    
    private final CaptchaCacheRepository cacheRepository;
    
    // 验证统计信息
    private final ConcurrentHashMap<String, ValidationStatistics> validationStats = new ConcurrentHashMap<>();
    
    @Autowired
    public CaptchaValidationService(CaptchaCacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }
    
    /**
     * 验证验证码答案
     */
    public ValidationResult verifyAnswer(String challengeId, double userX, double userY, String clientIp) {
        // 检查IP是否被阻止
        if (cacheRepository.isIpBlocked(clientIp)) {
            logger.warn("IP {} 因失败次数过多被阻止访问", clientIp);
            return ValidationResult.blocked("IP地址因失败次数过多被暂时阻止");
        }
        
        // 获取挑战
        CaptchaChallenge challenge = cacheRepository.getChallenge(challengeId);
        if (challenge == null) {
            logger.warn("验证码挑战不存在或已过期: {}", challengeId);
            cacheRepository.recordVerificationFailure(clientIp);
            return ValidationResult.failure("验证码不存在或已过期");
        }
        
        // 检查挑战状态
        if (challenge.isExpired()) {
            logger.info("验证码挑战已过期: {}", challengeId);
            cacheRepository.removeChallenge(challengeId);
            cacheRepository.recordVerificationFailure(clientIp);
            return ValidationResult.failure("验证码已过期");
        }
        
        if (challenge.isCompleted()) {
            logger.warn("验证码挑战已完成，不能重复验证: {}", challengeId);
            cacheRepository.recordVerificationFailure(clientIp);
            return ValidationResult.failure("验证码已使用");
        }
        
        if (challenge.hasExceededMaxAttempts()) {
            logger.warn("验证码挑战超过最大尝试次数: {}", challengeId);
            cacheRepository.removeChallenge(challengeId);
            cacheRepository.recordVerificationFailure(clientIp);
            return ValidationResult.failure("验证码尝试次数已用完");
        }
        
        // 执行验证
        CaptchaCoordinate userAnswer = new CaptchaCoordinate((int)userX, (int)userY);
        boolean isCorrect = challenge.verifyAnswer(userAnswer);
        
        if (isCorrect) {
            // 验证成功
            logger.info("验证码验证成功: {} from IP: {}", challengeId, clientIp);
            cacheRepository.recordVerificationSuccess(clientIp);
            
            // 生成票据
            String ticket = challenge.generateTicket();
            if (ticket != null) {
                cacheRepository.saveTicket(ticket, 10); // 票据有效期10分钟
            }
            
            // 移除已完成的挑战
            cacheRepository.removeChallenge(challengeId);
            
            // 更新验证统计
            updateValidationStatistics(challengeId, clientIp, true);
            
            return ValidationResult.success(ticket);
        } else {
            // 验证失败
            logger.warn("验证码验证失败: {} from IP: {}, 剩余尝试次数: {}", 
                challengeId, clientIp, challenge.getRemainingAttempts());
            
            cacheRepository.recordVerificationFailure(clientIp);
            
            // 更新验证统计
            updateValidationStatistics(challengeId, clientIp, false);
            
            // 如果尝试次数用完，移除挑战
            if (challenge.hasExceededMaxAttempts()) {
                cacheRepository.removeChallenge(challengeId);
                return ValidationResult.failure("验证失败，验证码已失效");
            }
            
            return ValidationResult.failure(String.format("验证失败，还有 %d 次尝试机会", 
                challenge.getRemainingAttempts()));
        }
    }
    
    /**
     * 验证票据
     */
    public boolean verifyTicket(String ticket) {
        return cacheRepository.verifyTicket(ticket);
    }
    
    /**
     * 使用票据（一次性）
     */
    public boolean consumeTicket(String ticket) {
        boolean consumed = cacheRepository.consumeTicket(ticket);
        if (consumed) {
            logger.debug("票据已使用: {}", ticket);
        } else {
            logger.warn("票据使用失败（不存在或已过期）: {}", ticket);
        }
        return consumed;
    }
    
    /**
     * 更新验证统计信息
     */
    private void updateValidationStatistics(String challengeId, String clientIp, boolean success) {
        String key = clientIp != null ? clientIp : "unknown";
        validationStats.computeIfAbsent(key, k -> new ValidationStatistics()).record(success);
    }
    
    /**
     * 获取IP验证统计
     */
    public ValidationStatistics getValidationStatistics(String clientIp) {
        return validationStats.getOrDefault(clientIp, new ValidationStatistics());
    }
    
    /**
     * 清理过期的验证统计
     */
    public void cleanupExpiredStatistics() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24); // 保留24小时内的统计
        validationStats.entrySet().removeIf(entry -> 
            entry.getValue().getLastAttemptTime().isBefore(threshold));
    }
    
    /**
     * 验证结果
     */
    public static class ValidationResult {
        private final boolean success;
        private final String message;
        private final String ticket;
        private final ValidationStatus status;
        
        public enum ValidationStatus {
            SUCCESS, FAILURE, BLOCKED, EXPIRED
        }
        
        private ValidationResult(boolean success, String message, String ticket, ValidationStatus status) {
            this.success = success;
            this.message = message;
            this.ticket = ticket;
            this.status = status;
        }
        
        public static ValidationResult success(String ticket) {
            return new ValidationResult(true, "验证成功", ticket, ValidationStatus.SUCCESS);
        }
        
        public static ValidationResult failure(String message) {
            return new ValidationResult(false, message, null, ValidationStatus.FAILURE);
        }
        
        public static ValidationResult blocked(String message) {
            return new ValidationResult(false, message, null, ValidationStatus.BLOCKED);
        }
        
        public static ValidationResult expired(String message) {
            return new ValidationResult(false, message, null, ValidationStatus.EXPIRED);
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getTicket() { return ticket; }
        public ValidationStatus getStatus() { return status; }
    }
    
    /**
     * 验证统计信息
     */
    public static class ValidationStatistics {
        private final AtomicInteger totalAttempts = new AtomicInteger(0);
        private final AtomicInteger successfulAttempts = new AtomicInteger(0);
        private final AtomicInteger failedAttempts = new AtomicInteger(0);
        private volatile LocalDateTime firstAttemptTime;
        private volatile LocalDateTime lastAttemptTime;
        
        public void record(boolean success) {
            LocalDateTime now = LocalDateTime.now();
            
            if (firstAttemptTime == null) {
                firstAttemptTime = now;
            }
            lastAttemptTime = now;
            
            totalAttempts.incrementAndGet();
            if (success) {
                successfulAttempts.incrementAndGet();
            } else {
                failedAttempts.incrementAndGet();
            }
        }
        
        public int getTotalAttempts() { return totalAttempts.get(); }
        public int getSuccessfulAttempts() { return successfulAttempts.get(); }
        public int getFailedAttempts() { return failedAttempts.get(); }
        public LocalDateTime getFirstAttemptTime() { return firstAttemptTime; }
        public LocalDateTime getLastAttemptTime() { return lastAttemptTime; }
        
        public double getSuccessRate() {
            int total = totalAttempts.get();
            return total > 0 ? (double)successfulAttempts.get() / total : 0.0;
        }
        
        @Override
        public String toString() {
            return String.format("ValidationStatistics{total=%d, success=%d, failed=%d, successRate=%.2f%%}",
                totalAttempts.get(), successfulAttempts.get(), failedAttempts.get(), getSuccessRate() * 100);
        }
    }
}