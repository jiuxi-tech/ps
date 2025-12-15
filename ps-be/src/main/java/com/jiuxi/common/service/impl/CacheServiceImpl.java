package com.jiuxi.common.service.impl;

import com.jiuxi.common.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: CacheServiceImpl
 * @Description: 缓存服务实现类
 * @Author: Qdd
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
@Service
public class CacheServiceImpl implements CacheService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    // 分布式锁释放脚本
    private static final String UNLOCK_SCRIPT = 
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "    return redis.call('del', KEYS[1]) " +
            "else " +
            "    return 0 " +
            "end";
    
    // ========== 基础操作 ==========
    
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    @Override
    public void set(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return (T) value;
    }
    
    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
    
    @Override
    public void delete(String... keys) {
        if (keys != null && keys.length > 0) {
            redisTemplate.delete(Arrays.asList(keys));
        }
    }
    
    @Override
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    @Override
    public void expire(String key, Duration timeout) {
        redisTemplate.expire(key, timeout);
    }
    
    @Override
    public Duration getExpire(String key) {
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire != null ? Duration.ofSeconds(expire) : null;
    }
    
    // ========== 模式匹配操作 ==========
    
    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }
    
    @Override
    public void deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
    
    // ========== Hash操作 ==========
    
    @Override
    public void hSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T hGet(String key, String field, Class<T> clazz) {
        Object value = redisTemplate.opsForHash().get(key, field);
        if (value == null) {
            return null;
        }
        return (T) value;
    }
    
    @Override
    public Map<String, Object> hGetAll(String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            result.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return result;
    }
    
    @Override
    public void hDelete(String key, String... fields) {
        redisTemplate.opsForHash().delete(key, (Object[]) fields);
    }
    
    @Override
    public boolean hExists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }
    
    @Override
    public Set<String> hKeys(String key) {
        Set<Object> keys = redisTemplate.opsForHash().keys(key);
        Set<String> stringKeys = new HashSet<>();
        for (Object k : keys) {
            stringKeys.add(k.toString());
        }
        return stringKeys;
    }
    
    @Override
    public long hSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }
    
    // ========== List操作 ==========
    
    @Override
    public void lPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }
    
    @Override
    public void rPush(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T lPop(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForList().leftPop(key);
        if (value == null) {
            return null;
        }
        return (T) value;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T rPop(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForList().rightPop(key);
        if (value == null) {
            return null;
        }
        return (T) value;
    }
    
    @Override
    public long lSize(String key) {
        Long size = redisTemplate.opsForList().size(key);
        return size != null ? size : 0;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> lRange(String key, long start, long end, Class<T> clazz) {
        List<Object> values = redisTemplate.opsForList().range(key, start, end);
        if (values == null) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>();
        for (Object value : values) {
            result.add((T) value);
        }
        return result;
    }
    
    // ========== Set操作 ==========
    
    @Override
    public void sAdd(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }
    
    @Override
    public void sRemove(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }
    
    @Override
    public boolean sIsMember(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> sMembers(String key, Class<T> clazz) {
        Set<Object> members = redisTemplate.opsForSet().members(key);
        if (members == null) {
            return new HashSet<>();
        }
        Set<T> result = new HashSet<>();
        for (Object member : members) {
            result.add((T) member);
        }
        return result;
    }
    
    @Override
    public long sSize(String key) {
        Long size = redisTemplate.opsForSet().size(key);
        return size != null ? size : 0;
    }
    
    // ========== 分布式锁操作 ==========
    
    @Override
    public boolean tryLock(String lockKey, String requestId, Duration expireTime) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime);
        return Boolean.TRUE.equals(result);
    }
    
    @Override
    public boolean releaseLock(String lockKey, String requestId) {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(UNLOCK_SCRIPT);
        script.setResultType(Long.class);
        
        Long result = redisTemplate.execute(script, Collections.singletonList(lockKey), requestId);
        return Long.valueOf(1).equals(result);
    }
    
    // ========== 计数器操作 ==========
    
    @Override
    public long increment(String key) {
        Long result = redisTemplate.opsForValue().increment(key);
        return result != null ? result : 0;
    }
    
    @Override
    public long increment(String key, long delta) {
        Long result = redisTemplate.opsForValue().increment(key, delta);
        return result != null ? result : 0;
    }
    
    @Override
    public long decrement(String key) {
        Long result = redisTemplate.opsForValue().decrement(key);
        return result != null ? result : 0;
    }
    
    @Override
    public long decrement(String key, long delta) {
        Long result = redisTemplate.opsForValue().decrement(key, delta);
        return result != null ? result : 0;
    }
    
    // ========== 缓存统计 ==========
    
    @Override
    public Map<String, Object> getCacheInfo() {
        Map<String, Object> info = new HashMap<>();
        try {
            Properties properties = redisTemplate.getConnectionFactory().getConnection().info();
            for (String key : properties.stringPropertyNames()) {
                info.put(key, properties.getProperty(key));
            }
        } catch (Exception e) {
            info.put("error", "Failed to get cache info: " + e.getMessage());
        }
        return info;
    }
    
    @Override
    public void flushAll() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
    
    @Override
    public long getDbSize() {
        Long size = redisTemplate.getConnectionFactory().getConnection().dbSize();
        return size != null ? size : 0;
    }
}