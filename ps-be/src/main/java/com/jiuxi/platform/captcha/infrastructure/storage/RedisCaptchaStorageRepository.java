package com.jiuxi.platform.captcha.infrastructure.storage;

import com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge;
import com.jiuxi.platform.captcha.domain.repository.CaptchaStorageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis验证码存储实现
 * 支持分布式部署
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Repository
@ConditionalOnProperty(name = "captcha.storage.type", havingValue = "redis", matchIfMissing = false)
public class RedisCaptchaStorageRepository implements CaptchaStorageRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(RedisCaptchaStorageRepository.class);
    
    private static final String CHALLENGE_KEY_PREFIX = "captcha:challenge:";
    private static final String TICKET_KEY_PREFIX = "captcha:ticket:";
    private static final String FAILURE_KEY_PREFIX = "captcha:failure:";
    private static final String STATS_KEY = "captcha:stats";
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public RedisCaptchaStorageRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public void saveChallenge(CaptchaChallenge challenge) {
        try {
            String key = CHALLENGE_KEY_PREFIX + challenge.getChallengeId();
            String value = objectMapper.writeValueAsString(challenge);
            
            // 设置过期时间为挑战的过期时间
            long ttl = ChronoUnit.SECONDS.between(LocalDateTime.now(), challenge.getExpireTime());
            if (ttl > 0) {
                redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
                
                // 更新统计
                incrementStatistic("challenges_generated");
                
                logger.debug("验证码挑战已保存到Redis: {} (TTL: {}秒)", challenge.getChallengeId(), ttl);
            } else {
                logger.warn("验证码挑战已过期，不保存到Redis: {}", challenge.getChallengeId());
            }
        } catch (Exception e) {
            logger.error("保存验证码挑战到Redis失败: {}", challenge.getChallengeId(), e);
            throw new RuntimeException("Redis存储失败", e);
        }
    }
    
    @Override
    public CaptchaChallenge getChallenge(String challengeId) {
        try {
            String key = CHALLENGE_KEY_PREFIX + challengeId;
            String value = (String) redisTemplate.opsForValue().get(key);
            
            if (value == null) {
                logger.debug("验证码挑战未找到: {}", challengeId);
                return null;
            }
            
            CaptchaChallenge challenge = objectMapper.readValue(value, CaptchaChallenge.class);
            
            // 检查是否过期
            if (challenge.isExpired()) {
                redisTemplate.delete(key);
                incrementStatistic("challenges_expired");
                logger.debug("验证码挑战已过期并删除: {}", challengeId);
                return null;
            }
            
            logger.debug("验证码挑战已从Redis获取: {}", challengeId);
            return challenge;
            
        } catch (Exception e) {
            logger.error("从Redis获取验证码挑战失败: {}", challengeId, e);
            return null;
        }
    }
    
    @Override
    public void removeChallenge(String challengeId) {
        try {
            String key = CHALLENGE_KEY_PREFIX + challengeId;
            Boolean deleted = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(deleted)) {
                logger.debug("验证码挑战已从Redis删除: {}", challengeId);
            }
        } catch (Exception e) {
            logger.error("从Redis删除验证码挑战失败: {}", challengeId, e);
        }
    }
    
    @Override
    public Map<String, CaptchaChallenge> getChallenges(List<String> challengeIds) {
        if (challengeIds == null || challengeIds.isEmpty()) {
            return new HashMap<>();
        }
        
        try {
            List<String> keys = challengeIds.stream()
                .map(id -> CHALLENGE_KEY_PREFIX + id)
                .collect(Collectors.toList());
            
            List<Object> values = redisTemplate.opsForValue().multiGet(keys);
            Map<String, CaptchaChallenge> result = new HashMap<>();
            
            for (int i = 0; i < challengeIds.size(); i++) {
                String challengeId = challengeIds.get(i);
                Object value = values.get(i);
                
                if (value != null) {
                    try {
                        CaptchaChallenge challenge = objectMapper.readValue((String) value, CaptchaChallenge.class);
                        if (!challenge.isExpired()) {
                            result.put(challengeId, challenge);
                        } else {
                            removeChallenge(challengeId);
                        }
                    } catch (Exception e) {
                        logger.warn("解析验证码挑战失败: {}", challengeId, e);
                    }
                }
            }
            
            return result;
            
        } catch (Exception e) {
            logger.error("批量获取验证码挑战失败", e);
            return new HashMap<>();
        }
    }
    
    @Override
    public void saveTicket(String ticket, int expireMinutes) {
        try {
            String key = TICKET_KEY_PREFIX + ticket;
            String value = LocalDateTime.now().plusMinutes(expireMinutes).toString();
            
            redisTemplate.opsForValue().set(key, value, expireMinutes, TimeUnit.MINUTES);
            incrementStatistic("tickets_generated");
            
            logger.debug("验证码票据已保存到Redis: {} (过期时间: {}分钟)", ticket, expireMinutes);
            
        } catch (Exception e) {
            logger.error("保存验证码票据到Redis失败: {}", ticket, e);
            throw new RuntimeException("Redis存储失败", e);
        }
    }
    
    @Override
    public boolean verifyTicket(String ticket) {
        try {
            String key = TICKET_KEY_PREFIX + ticket;
            String value = (String) redisTemplate.opsForValue().get(key);
            
            if (value == null) {
                return false;
            }
            
            LocalDateTime expireTime = LocalDateTime.parse(value);
            boolean valid = LocalDateTime.now().isBefore(expireTime);
            
            if (!valid) {
                redisTemplate.delete(key);
            }
            
            return valid;
            
        } catch (Exception e) {
            logger.error("验证票据失败: {}", ticket, e);
            return false;
        }
    }
    
    @Override
    public boolean consumeTicket(String ticket) {
        try {
            String key = TICKET_KEY_PREFIX + ticket;
            String value = (String) redisTemplate.opsForValue().get(key);
            
            if (value == null) {
                return false;
            }
            
            // 使用Redis的原子操作删除票据
            Boolean deleted = redisTemplate.delete(key);
            
            if (Boolean.TRUE.equals(deleted)) {
                LocalDateTime expireTime = LocalDateTime.parse(value);
                boolean valid = LocalDateTime.now().isBefore(expireTime);
                
                if (valid) {
                    incrementStatistic("tickets_consumed");
                    logger.debug("验证码票据已使用: {}", ticket);
                }
                
                return valid;
            }
            
            return false;
            
        } catch (Exception e) {
            logger.error("使用票据失败: {}", ticket, e);
            return false;
        }
    }
    
    @Override
    public void cleanupExpired() {
        try {
            // Redis的TTL机制会自动清理过期数据
            // 这里可以额外清理一些统计信息或日志
            logger.debug("Redis存储清理任务执行（依赖TTL自动清理）");
            
        } catch (Exception e) {
            logger.error("Redis清理任务执行失败", e);
        }
    }
    
    @Override
    public void recordVerificationFailure(String clientIp) {
        if (clientIp == null || clientIp.isEmpty()) {
            return;
        }
        
        try {
            String key = FAILURE_KEY_PREFIX + clientIp;
            String countKey = key + ":count";
            String timeKey = key + ":time";
            
            // 递增失败计数
            redisTemplate.opsForValue().increment(countKey);
            redisTemplate.expire(countKey, 15, TimeUnit.MINUTES);
            
            // 更新最后失败时间
            redisTemplate.opsForValue().set(timeKey, LocalDateTime.now().toString(), 15, TimeUnit.MINUTES);
            
            incrementStatistic("verification_failures");
            
        } catch (Exception e) {
            logger.error("记录验证失败失败: {}", clientIp, e);
        }
    }
    
    @Override
    public boolean isIpBlocked(String clientIp) {
        if (clientIp == null || clientIp.isEmpty()) {
            return false;
        }
        
        try {
            String countKey = FAILURE_KEY_PREFIX + clientIp + ":count";
            String value = (String) redisTemplate.opsForValue().get(countKey);
            
            if (value == null) {
                return false;
            }
            
            int failureCount = Integer.parseInt(value);
            return failureCount >= 5; // 最大失败次数
            
        } catch (Exception e) {
            logger.error("检查IP阻止状态失败: {}", clientIp, e);
            return false;
        }
    }
    
    @Override
    public void recordVerificationSuccess(String clientIp) {
        if (clientIp == null || clientIp.isEmpty()) {
            return;
        }
        
        try {
            // 验证成功后清除失败记录
            String key = FAILURE_KEY_PREFIX + clientIp;
            redisTemplate.delete(key + ":count");
            redisTemplate.delete(key + ":time");
            
            incrementStatistic("challenges_verified");
            
        } catch (Exception e) {
            logger.error("记录验证成功失败: {}", clientIp, e);
        }
    }
    
    @Override
    public StorageStatistics getStatistics() {
        try {
            Map<Object, Object> stats = redisTemplate.opsForHash().entries(STATS_KEY);
            
            long challenges = getLongValue(stats, "challenges_generated");
            long tickets = getLongValue(stats, "tickets_generated");
            long failures = getLongValue(stats, "verification_failures");
            
            // 简单的命中率计算
            long verified = getLongValue(stats, "challenges_verified");
            double hitRate = challenges > 0 ? (double) verified / challenges : 0.0;
            
            // Redis内存使用情况需要通过INFO命令获取
            long memoryUsage = getRedisMemoryUsage();
            
            return new StorageStatistics(challenges, tickets, failures, hitRate, memoryUsage);
            
        } catch (Exception e) {
            logger.error("获取Redis存储统计失败", e);
            return new StorageStatistics(0, 0, 0, 0.0, 0);
        }
    }
    
    @Override
    public boolean isHealthy() {
        try {
            // 执行简单的ping命令测试连接
            redisTemplate.execute((org.springframework.data.redis.core.RedisCallback<Object>) connection -> {
                connection.ping();
                return null;
            });
            return true;
            
        } catch (Exception e) {
            logger.error("Redis健康检查失败", e);
            return false;
        }
    }
    
    /**
     * 递增统计数据
     */
    private void incrementStatistic(String key) {
        try {
            redisTemplate.opsForHash().increment(STATS_KEY, key, 1);
        } catch (Exception e) {
            logger.warn("更新统计信息失败: {}", key, e);
        }
    }
    
    /**
     * 获取长整型值
     */
    private long getLongValue(Map<Object, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return 0;
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * 获取Redis内存使用情况
     */
    private long getRedisMemoryUsage() {
        try {
            return (Long) redisTemplate.execute((org.springframework.data.redis.core.RedisCallback<Long>) connection -> {
                Properties info = connection.info("memory");
                String usedMemory = info.getProperty("used_memory");
                return usedMemory != null ? Long.parseLong(usedMemory) : 0L;
            });
        } catch (Exception e) {
            logger.warn("获取Redis内存使用情况失败", e);
            return 0;
        }
    }
}