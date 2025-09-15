package com.jiuxi.platform.captcha.infrastructure.config;

import com.jiuxi.platform.captcha.app.service.CaptchaAdapterService;
import com.jiuxi.platform.captcha.app.service.CaptchaApplicationService;
import com.jiuxi.platform.captcha.app.service.CaptchaService;
import com.jiuxi.platform.captcha.domain.service.CaptchaGenerator;
import com.jiuxi.platform.captcha.infrastructure.cache.CaptchaCacheRepository;
import com.jiuxi.platform.captcha.infrastructure.generator.ConcatCaptchaGenerator;
import com.jiuxi.platform.captcha.infrastructure.generator.RotateCaptchaGenerator;
import com.jiuxi.platform.captcha.infrastructure.generator.SliderCaptchaGenerator;
import com.jiuxi.platform.captcha.infrastructure.generator.ResourceSliderCaptchaGenerator;
import com.jiuxi.platform.captcha.infrastructure.storage.RedisCaptchaStorageRepository;
import com.jiuxi.platform.captcha.app.service.CaptchaValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration
@ComponentScan({"com.jiuxi.platform.captcha.app.controller"})
public class TopinfoCaptchaPlatformAutoConfiguration {

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 验证码缓存仓储
     */
    @Bean
    @ConditionalOnMissingBean(CaptchaCacheRepository.class)
    public CaptchaCacheRepository captchaCacheRepository() {
        // 如果Redis不可用，使用内存存储
        if (redisTemplate != null) {
            // 创建适配器将RedisCaptchaStorageRepository适配为CaptchaCacheRepository
            RedisCaptchaStorageRepository redisRepo = new RedisCaptchaStorageRepository(redisTemplate);
            return new CaptchaCacheRepository() {
                @Override
                public void saveChallenge(com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge challenge) {
                    redisRepo.saveChallenge(challenge);
                }
                
                @Override
                public com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge getChallenge(String challengeId) {
                    return redisRepo.getChallenge(challengeId);
                }
                
                @Override
                public void removeChallenge(String challengeId) {
                    redisRepo.removeChallenge(challengeId);
                }
                
                @Override
                public java.util.Map<String, com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge> getChallenges(java.util.List<String> challengeIds) {
                    return redisRepo.getChallenges(challengeIds);
                }
                
                @Override
                public void saveTicket(String ticket, int expireMinutes) {
                    redisRepo.saveTicket(ticket, expireMinutes);
                }
                
                @Override
                public boolean verifyTicket(String ticket) {
                    return redisRepo.verifyTicket(ticket);
                }
                
                @Override
                public boolean consumeTicket(String ticket) {
                    return redisRepo.consumeTicket(ticket);
                }
                
                @Override
                public void cleanupExpired() {
                    redisRepo.cleanupExpired();
                }
                
                @Override
                public void recordVerificationFailure(String clientIp) {
                    redisRepo.recordVerificationFailure(clientIp);
                }
                
                @Override
                public boolean isIpBlocked(String clientIp) {
                    return redisRepo.isIpBlocked(clientIp);
                }
                
                @Override
                public void recordVerificationSuccess(String clientIp) {
                    redisRepo.recordVerificationSuccess(clientIp);
                }
                
                @Override
                public com.jiuxi.platform.captcha.infrastructure.cache.CaptchaCacheRepository.CacheStatistics getStatistics() {
                    com.jiuxi.platform.captcha.domain.repository.CaptchaStorageRepository.StorageStatistics redisStats = redisRepo.getStatistics();
                    return new com.jiuxi.platform.captcha.infrastructure.cache.CaptchaCacheRepository.CacheStatistics(
                        0, // challengeCount
                        0, // ticketCount
                        redisStats.getTotalChallenges(), // totalChallengesGenerated
                        0, // totalChallengesVerified
                        0, // totalChallengesExpired
                        redisStats.getTotalTickets(), // totalTicketsGenerated
                        0, // totalTicketsConsumed
                        redisStats.getTotalFailures(), // totalVerificationFailures
                        0  // blockedIpCount
                    );
                }
                
                @Override
                public com.jiuxi.platform.captcha.infrastructure.cache.CaptchaCacheRepository.CacheHealthStatus getHealthStatus() {
                    return super.getHealthStatus();
                }
                
                @Override
                public String getDetailedStatisticsReport() {
                    return super.getDetailedStatisticsReport();
                }
                
                @Override
                public void resetIpFailureCount(String clientIp) {
                    super.resetIpFailureCount(clientIp);
                }
            };
        } else {
            return new CaptchaCacheRepository();
        }
    }

    /**
     * 验证码验证服务
     */
    @Bean
    @ConditionalOnMissingBean(CaptchaValidationService.class)
    public CaptchaValidationService captchaValidationService(CaptchaCacheRepository cacheRepository) {
        return new CaptchaValidationService(cacheRepository);
    }

    /**
     * 验证码应用服务
     */
    @Bean
    @ConditionalOnMissingBean(CaptchaApplicationService.class)
    public CaptchaApplicationService captchaApplicationService(
            List<CaptchaGenerator> captchaGenerators,
            CaptchaCacheRepository cacheRepository,
            CaptchaValidationService validationService) {
        return new CaptchaApplicationService(captchaGenerators, cacheRepository, validationService);
    }

    /**
     * 拼接验证码生成器
     */
    @Bean
    @ConditionalOnMissingBean(name = "concatCaptchaGenerator")
    public ConcatCaptchaGenerator concatCaptchaGenerator() {
        return new ConcatCaptchaGenerator();
    }

    /**
     * 旋转验证码生成器
     */
    @Bean
    @ConditionalOnMissingBean(name = "rotateCaptchaGenerator")
    public RotateCaptchaGenerator rotateCaptchaGenerator() {
        return new RotateCaptchaGenerator();
    }

    /**
     * 滑块验证码生成器
     */
    @Bean
    @ConditionalOnMissingBean(name = "sliderCaptchaGenerator")
    public SliderCaptchaGenerator sliderCaptchaGenerator() {
        return new SliderCaptchaGenerator();
    }

    /**
     * 基于资源目录图片的滑块验证码生成器
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "resourceSliderCaptchaGenerator")
    public ResourceSliderCaptchaGenerator resourceSliderCaptchaGenerator() {
        return new ResourceSliderCaptchaGenerator();
    }

    /**
     * 验证码服务接口实现
     */
    @Bean
    @ConditionalOnMissingBean(CaptchaService.class)
    public CaptchaService captchaService(CaptchaApplicationService captchaApplicationService) {
        return new CaptchaAdapterService(captchaApplicationService);
    }
}