package com.jiuxi.platform.captcha.app.service;

import com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge;
import com.jiuxi.platform.captcha.domain.service.CaptchaGenerator;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaCoordinate;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaType;
import com.jiuxi.platform.captcha.infrastructure.cache.CaptchaCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 验证码应用服务
 * 统一的验证码业务处理服务
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class CaptchaApplicationService {
    
    private static final Logger logger = LoggerFactory.getLogger(CaptchaApplicationService.class);
    
    private final List<CaptchaGenerator> captchaGenerators;
    private final CaptchaCacheRepository cacheRepository;
    private final CaptchaValidationService validationService;
    
    @Autowired
    public CaptchaApplicationService(List<CaptchaGenerator> captchaGenerators,
                                   CaptchaCacheRepository cacheRepository,
                                   CaptchaValidationService validationService) {
        this.captchaGenerators = captchaGenerators;
        this.cacheRepository = cacheRepository;
        this.validationService = validationService;
    }
    
    /**
     * 生成验证码挑战
     */
    public CaptchaResponse generateCaptcha(String captchaTypeCode) {
        try {
            CaptchaType captchaType = CaptchaType.fromCode(captchaTypeCode);
            CaptchaGenerator generator = findGenerator(captchaType);
            
            if (generator == null) {
                return CaptchaResponse.error("不支持的验证码类型: " + captchaTypeCode);
            }
            
            CaptchaChallenge challenge = generator.generateChallenge(captchaType);
            cacheRepository.saveChallenge(challenge);
            
            return CaptchaResponse.success(challenge);
            
        } catch (Exception e) {
            return CaptchaResponse.error("生成验证码失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证验证码答案
     */
    public VerificationResponse verifyAnswer(String challengeId, Double x, Double y) {
        return verifyAnswer(challengeId, x, y, null);
    }
    
    /**
     * 验证验证码答案（带IP信息）
     */
    public VerificationResponse verifyAnswer(String challengeId, Double x, Double y, String clientIp) {
        try {
            // 使用新的验证服务
            CaptchaValidationService.ValidationResult result = 
                validationService.verifyAnswer(challengeId, x, y, clientIp);
            
            if (result.isSuccess()) {
                logger.info("验证码验证成功: {} from IP: {}", challengeId, clientIp);
                return VerificationResponse.success(result.getTicket());
            } else {
                logger.warn("验证码验证失败: {} from IP: {} - {}", challengeId, clientIp, result.getMessage());
                return VerificationResponse.fail(result.getMessage());
            }
            
        } catch (Exception e) {
            logger.error("验证码验证异常: {} from IP: {}", challengeId, clientIp, e);
            return VerificationResponse.fail("验证过程出错: " + e.getMessage());
        }
    }
    
    /**
     * 验证旋转角度（用于旋转验证码）
     */
    public VerificationResponse verifyRotation(String challengeId, Double angle) {
        try {
            CaptchaChallenge challenge = cacheRepository.getChallenge(challengeId);
            if (challenge == null) {
                return VerificationResponse.fail("验证码不存在或已过期");
            }
            
            if (!challenge.canAttempt()) {
                return VerificationResponse.fail("验证码已过期或超过最大尝试次数");
            }
            
            // 从元数据中获取正确角度
            Double correctAngle = (Double) challenge.getMetadata("correctAngle");
            if (correctAngle == null) {
                return VerificationResponse.fail("验证码数据异常");
            }
            
            boolean verified = challenge.verifyRotationAnswer(angle, correctAngle);
            
            if (verified) {
                String ticket = challenge.generateTicket();
                cacheRepository.saveTicket(ticket, 30);
                return VerificationResponse.success(ticket);
            } else {
                return VerificationResponse.fail("验证失败，剩余尝试次数: " + challenge.getRemainingAttempts());
            }
            
        } catch (Exception e) {
            return VerificationResponse.fail("验证过程出错: " + e.getMessage());
        }
    }
    
    /**
     * 验证票据（不消费）
     */
    public boolean verifyTicket(String ticket) {
        return validationService.verifyTicket(ticket);
    }
    
    /**
     * 使用票据（一次性使用）
     */
    public boolean consumeTicket(String ticket) {
        return validationService.consumeTicket(ticket);
    }
    
    /**
     * 获取验证码统计信息
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        CaptchaCacheRepository.CacheStatistics cacheStats = cacheRepository.getStatistics();
        stats.put("cacheStatistics", cacheStats);
        
        // 添加健康状态信息
        CaptchaCacheRepository.CacheHealthStatus healthStatus = cacheRepository.getHealthStatus();
        stats.put("healthStatus", healthStatus);
        
        Map<String, Object> generators = new HashMap<>();
        for (CaptchaGenerator generator : captchaGenerators) {
            generators.put(generator.getGeneratorName(), Map.of(
                "supportedTypes", getSupportedTypes(generator)
            ));
        }
        stats.put("generators", generators);
        
        return stats;
    }
    
    /**
     * 获取缓存健康状态
     */
    public CaptchaCacheRepository.CacheHealthStatus getHealthStatus() {
        return cacheRepository.getHealthStatus();
    }
    
    /**
     * 获取详细统计报告
     */
    public String getDetailedStatisticsReport() {
        return cacheRepository.getDetailedStatisticsReport();
    }
    
    /**
     * 检查IP是否被阻止
     */
    public boolean isIpBlocked(String clientIp) {
        return cacheRepository.isIpBlocked(clientIp);
    }
    
    /**
     * 获取IP验证统计
     */
    public CaptchaValidationService.ValidationStatistics getIpValidationStatistics(String clientIp) {
        return validationService.getValidationStatistics(clientIp);
    }
    
    /**
     * 重置IP失败计数
     */
    public void resetIpFailureCount(String clientIp) {
        cacheRepository.resetIpFailureCount(clientIp);
        logger.info("已重置IP失败计数: {}", clientIp);
    }
    
    /**
     * 定期清理过期数据
     */
    @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    public void cleanupExpiredData() {
        try {
            cacheRepository.cleanupExpired();
            validationService.cleanupExpiredStatistics();
            logger.debug("过期数据清理完成");
        } catch (Exception e) {
            logger.error("清理过期验证码数据失败", e);
        }
    }
    
    /**
     * 查找支持指定类型的生成器
     */
    private CaptchaGenerator findGenerator(CaptchaType captchaType) {
        return captchaGenerators.stream()
                .filter(generator -> generator.supports(captchaType))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 获取生成器支持的类型
     */
    private java.util.List<String> getSupportedTypes(CaptchaGenerator generator) {
        java.util.List<String> types = new java.util.ArrayList<>();
        for (CaptchaType type : CaptchaType.values()) {
            if (generator.supports(type)) {
                types.add(type.getCode());
            }
        }
        return types;
    }
    
    /**
     * 验证码响应类
     */
    public static class CaptchaResponse {
        private boolean success;
        private String message;
        private String challengeId;
        private String captchaType;
        private String backgroundImage;
        private String puzzleImage;
        private Map<String, Object> metadata;
        
        private CaptchaResponse() {}
        
        public static CaptchaResponse success(CaptchaChallenge challenge) {
            CaptchaResponse response = new CaptchaResponse();
            response.success = true;
            response.message = "验证码生成成功";
            response.challengeId = challenge.getChallengeId();
            response.captchaType = challenge.getCaptchaType().getCode();
            response.backgroundImage = challenge.getBackgroundImageData();
            response.puzzleImage = challenge.getPuzzleImageData();
            response.metadata = challenge.getMetadata();
            return response;
        }
        
        public static CaptchaResponse error(String message) {
            CaptchaResponse response = new CaptchaResponse();
            response.success = false;
            response.message = message;
            return response;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getChallengeId() { return challengeId; }
        public String getCaptchaType() { return captchaType; }
        public String getBackgroundImage() { return backgroundImage; }
        public String getPuzzleImage() { return puzzleImage; }
        public Map<String, Object> getMetadata() { return metadata; }
    }
    
    /**
     * 验证响应类
     */
    public static class VerificationResponse {
        private boolean success;
        private String message;
        private String ticket;
        
        private VerificationResponse() {}
        
        public static VerificationResponse success(String ticket) {
            VerificationResponse response = new VerificationResponse();
            response.success = true;
            response.message = "验证成功";
            response.ticket = ticket;
            return response;
        }
        
        public static VerificationResponse fail(String message) {
            VerificationResponse response = new VerificationResponse();
            response.success = false;
            response.message = message;
            return response;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getTicket() { return ticket; }
    }
}