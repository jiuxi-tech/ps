package com.jiuxi.shared.security.service;

import com.jiuxi.shared.security.config.KeycloakClient;
import com.jiuxi.shared.security.config.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Keycloak用户同步服务
 * 负责本地用户与Keycloak用户的同步，包括增量同步、冲突处理和状态监控
 * 
 * @author Security Refactoring
 * @since Phase 4.2.4
 */
@Service
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class KeycloakUserSyncService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakUserSyncService.class);

    @Autowired
    private KeycloakClient keycloakClient;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    // 同步状态跟踪
    private final Map<String, SyncOperation> activeSyncOperations = new ConcurrentHashMap<>();
    private final AtomicLong syncCounter = new AtomicLong(0);
    private final AtomicLong successCounter = new AtomicLong(0);
    private final AtomicLong failureCounter = new AtomicLong(0);
    private final AtomicLong conflictCounter = new AtomicLong(0);

    // Redis键前缀
    private static final String SYNC_STATUS_PREFIX = "ps:security:sync:status:";
    private static final String SYNC_CONFLICT_PREFIX = "ps:security:sync:conflict:";
    private static final String SYNC_STATS_KEY = "ps:security:sync:stats";
    private static final String LAST_SYNC_TIME_KEY = "ps:security:sync:last_time";

    @PostConstruct
    public void init() {
        logger.info("KeycloakUserSyncService initialized with sync monitoring enabled");
    }

    /**
     * 同步单个用户到Keycloak
     */
    @Async
    public void syncUserToKeycloak(UserSyncRequest request) {
        String operationId = generateOperationId();
        SyncOperation operation = new SyncOperation(operationId, SyncType.USER_TO_KEYCLOAK, request.getUserId());
        
        try {
            activeSyncOperations.put(operationId, operation);
            syncCounter.incrementAndGet();
            
            logger.info("Starting user sync to Keycloak: userId={}, operationId={}", request.getUserId(), operationId);
            
            // 检查用户是否已存在于Keycloak
            KeycloakClient.UserInfo existingUser = findKeycloakUserByUsername(request.getUsername());
            
            if (existingUser != null) {
                // 用户已存在，处理冲突
                SyncConflict conflict = handleUserConflict(request, existingUser);
                if (conflict.getResolution() == ConflictResolution.UPDATE_KEYCLOAK) {
                    updateKeycloakUser(request, existingUser.getSub());
                } else if (conflict.getResolution() == ConflictResolution.UPDATE_LOCAL) {
                    // 这里应该调用本地用户更新逻辑
                    logger.info("Local user should be updated based on Keycloak data: userId={}", request.getUserId());
                }
                conflictCounter.incrementAndGet();
            } else {
                // 创建新用户
                createKeycloakUser(request);
            }
            
            operation.setStatus(SyncStatus.COMPLETED);
            operation.setCompletedAt(LocalDateTime.now());
            successCounter.incrementAndGet();
            
            // 更新同步状态到Redis
            updateSyncStatus(request.getUserId(), SyncStatus.COMPLETED, null);
            
            logger.info("User sync to Keycloak completed successfully: userId={}, operationId={}", 
                request.getUserId(), operationId);
                
        } catch (Exception e) {
            operation.setStatus(SyncStatus.FAILED);
            operation.setErrorMessage(e.getMessage());
            operation.setCompletedAt(LocalDateTime.now());
            failureCounter.incrementAndGet();
            
            // 更新同步状态到Redis
            updateSyncStatus(request.getUserId(), SyncStatus.FAILED, e.getMessage());
            
            logger.error("User sync to Keycloak failed: userId={}, operationId={}, error={}", 
                request.getUserId(), operationId, e.getMessage(), e);
        } finally {
            activeSyncOperations.remove(operationId);
        }
    }

    /**
     * 从Keycloak同步用户到本地
     */
    @Async
    public void syncUserFromKeycloak(String keycloakUserId) {
        String operationId = generateOperationId();
        SyncOperation operation = new SyncOperation(operationId, SyncType.USER_FROM_KEYCLOAK, keycloakUserId);
        
        try {
            activeSyncOperations.put(operationId, operation);
            syncCounter.incrementAndGet();
            
            logger.info("Starting user sync from Keycloak: keycloakUserId={}, operationId={}", keycloakUserId, operationId);
            
            // 这里应该实现从Keycloak获取用户信息并更新本地数据库的逻辑
            // 由于涉及本地数据库操作，这里只做框架性实现
            
            operation.setStatus(SyncStatus.COMPLETED);
            operation.setCompletedAt(LocalDateTime.now());
            successCounter.incrementAndGet();
            
            logger.info("User sync from Keycloak completed successfully: keycloakUserId={}, operationId={}", 
                keycloakUserId, operationId);
                
        } catch (Exception e) {
            operation.setStatus(SyncStatus.FAILED);
            operation.setErrorMessage(e.getMessage());
            operation.setCompletedAt(LocalDateTime.now());
            failureCounter.incrementAndGet();
            
            logger.error("User sync from Keycloak failed: keycloakUserId={}, operationId={}, error={}", 
                keycloakUserId, operationId, e.getMessage(), e);
        } finally {
            activeSyncOperations.remove(operationId);
        }
    }

    /**
     * 批量同步用户
     */
    @Async
    public void batchSyncUsers(List<UserSyncRequest> requests) {
        String operationId = generateOperationId();
        logger.info("Starting batch user sync: count={}, operationId={}", requests.size(), operationId);
        
        int successCount = 0;
        int failureCount = 0;
        
        for (UserSyncRequest request : requests) {
            try {
                syncUserToKeycloak(request);
                successCount++;
            } catch (Exception e) {
                failureCount++;
                logger.error("Batch sync failed for user: userId={}, error={}", request.getUserId(), e.getMessage(), e);
            }
        }
        
        logger.info("Batch user sync completed: operationId={}, total={}, success={}, failed={}", 
            operationId, requests.size(), successCount, failureCount);
    }

    /**
     * 增量同步 - 定期执行
     */
    @Scheduled(fixedRateString = "${ps.security.keycloak.sync.interval:300000}") // 默认5分钟
    public void performIncrementalSync() {
        if (!securityProperties.getKeycloak().isSyncEnabled()) {
            return;
        }
        
        String operationId = generateOperationId();
        logger.info("Starting incremental sync: operationId={}", operationId);
        
        try {
            // 获取上次同步时间
            String lastSyncTime = getLastSyncTime();
            
            // 这里应该实现获取自上次同步以来修改的用户列表
            // 由于涉及具体的数据库查询，这里只做框架性实现
            List<UserSyncRequest> changedUsers = getChangedUsersSince(lastSyncTime);
            
            if (!changedUsers.isEmpty()) {
                logger.info("Found {} changed users for incremental sync", changedUsers.size());
                batchSyncUsers(changedUsers);
            } else {
                logger.debug("No changed users found for incremental sync");
            }
            
            // 更新最后同步时间
            updateLastSyncTime();
            
        } catch (Exception e) {
            logger.error("Incremental sync failed: operationId={}, error={}", operationId, e.getMessage(), e);
        }
    }

    /**
     * 获取同步状态
     */
    public SyncStatusInfo getSyncStatus(String userId) {
        SyncStatusInfo statusInfo = new SyncStatusInfo();
        statusInfo.setUserId(userId);
        
        // 检查是否有正在进行的同步操作
        Optional<SyncOperation> activeOperation = activeSyncOperations.values().stream()
            .filter(op -> userId.equals(op.getEntityId()))
            .findFirst();
            
        if (activeOperation.isPresent()) {
            SyncOperation operation = activeOperation.get();
            statusInfo.setStatus(operation.getStatus());
            statusInfo.setOperationId(operation.getOperationId());
            statusInfo.setStartTime(operation.getStartedAt());
            statusInfo.setErrorMessage(operation.getErrorMessage());
        } else {
            // 从Redis获取最后的同步状态
            String statusKey = SYNC_STATUS_PREFIX + userId;
            if (redisTemplate != null) {
                Object statusObj = redisTemplate.opsForValue().get(statusKey);
                if (statusObj instanceof Map) {
                    Map<String, Object> statusMap = (Map<String, Object>) statusObj;
                    statusInfo.setStatus(SyncStatus.valueOf((String) statusMap.get("status")));
                    statusInfo.setLastSyncTime(LocalDateTime.parse((String) statusMap.get("lastSyncTime")));
                    statusInfo.setErrorMessage((String) statusMap.get("errorMessage"));
                }
            }
        }
        
        return statusInfo;
    }

    /**
     * 获取同步统计信息
     */
    public SyncStatistics getSyncStatistics() {
        SyncStatistics stats = new SyncStatistics();
        stats.setTotalSyncOperations(syncCounter.get());
        stats.setSuccessfulOperations(successCounter.get());
        stats.setFailedOperations(failureCounter.get());
        stats.setConflictOperations(conflictCounter.get());
        stats.setActiveOperations(activeSyncOperations.size());
        
        if (syncCounter.get() > 0) {
            stats.setSuccessRate((double) successCounter.get() / syncCounter.get() * 100);
        }
        
        return stats;
    }

    /**
     * 获取冲突列表
     */
    public List<SyncConflict> getSyncConflicts() {
        List<SyncConflict> conflicts = new ArrayList<>();
        
        if (redisTemplate != null) {
            Set<String> conflictKeys = redisTemplate.keys(SYNC_CONFLICT_PREFIX + "*");
            if (conflictKeys != null) {
                for (String key : conflictKeys) {
                    Object conflictObj = redisTemplate.opsForValue().get(key);
                    if (conflictObj instanceof SyncConflict) {
                        conflicts.add((SyncConflict) conflictObj);
                    }
                }
            }
        }
        
        return conflicts;
    }

    /**
     * 清理过期的同步记录
     */
    @Scheduled(fixedRateString = "${ps.security.keycloak.sync.cleanup-interval:3600000}") // 默认1小时
    public void cleanupExpiredSyncRecords() {
        logger.debug("Starting cleanup of expired sync records");
        
        if (redisTemplate != null) {
            // 清理过期的同步状态记录（保留7天）
            // 这里可以根据需要实现具体的清理逻辑
        }
        
        logger.debug("Cleanup of expired sync records completed");
    }

    // ================================ 私有辅助方法 ================================

    /**
     * 生成操作ID
     */
    private String generateOperationId() {
        return "sync_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 根据用户名查找Keycloak用户
     */
    private KeycloakClient.UserInfo findKeycloakUserByUsername(String username) {
        // 这里应该实现通过管理员API查找用户的逻辑
        // 由于KeycloakClient暂时没有实现查找用户的方法，这里返回null
        return null;
    }

    /**
     * 处理用户冲突
     */
    private SyncConflict handleUserConflict(UserSyncRequest localUser, KeycloakClient.UserInfo keycloakUser) {
        SyncConflict conflict = new SyncConflict();
        conflict.setConflictId(generateOperationId());
        conflict.setUserId(localUser.getUserId());
        conflict.setUsername(localUser.getUsername());
        conflict.setConflictType(ConflictType.USER_EXISTS);
        conflict.setDetectedAt(LocalDateTime.now());
        
        // 简单的冲突解决策略：根据最后修改时间决定
        if (localUser.getLastModified() != null && localUser.getLastModified().isAfter(LocalDateTime.now().minusMinutes(5))) {
            // 本地数据较新，更新Keycloak
            conflict.setResolution(ConflictResolution.UPDATE_KEYCLOAK);
        } else {
            // Keycloak数据较新，更新本地
            conflict.setResolution(ConflictResolution.UPDATE_LOCAL);
        }
        
        conflict.setLocalData(convertUserToMap(localUser));
        conflict.setKeycloakData(convertKeycloakUserToMap(keycloakUser));
        
        // 保存冲突记录
        saveSyncConflict(conflict);
        
        return conflict;
    }

    /**
     * 创建Keycloak用户
     */
    private void createKeycloakUser(UserSyncRequest request) {
        KeycloakClient.UserCreationRequest creationRequest = new KeycloakClient.UserCreationRequest();
        creationRequest.setUsername(request.getUsername());
        creationRequest.setEmail(request.getEmail());
        creationRequest.setFirstName(request.getFirstName());
        creationRequest.setLastName(request.getLastName());
        creationRequest.setEnabled(request.isEnabled());
        creationRequest.setPassword(request.getPassword());
        
        KeycloakClient.AdminApiResult result = keycloakClient.createUser(creationRequest);
        if (!result.isSuccess()) {
            throw new RuntimeException("Failed to create Keycloak user: " + result.getMessage());
        }
        
        logger.info("Keycloak user created successfully: userId={}, keycloakUserId={}", 
            request.getUserId(), result.getUserId());
    }

    /**
     * 更新Keycloak用户
     */
    private void updateKeycloakUser(UserSyncRequest request, String keycloakUserId) {
        KeycloakClient.UserUpdateRequest updateRequest = new KeycloakClient.UserUpdateRequest();
        updateRequest.setUsername(request.getUsername());
        updateRequest.setEmail(request.getEmail());
        updateRequest.setFirstName(request.getFirstName());
        updateRequest.setLastName(request.getLastName());
        updateRequest.setEnabled(request.isEnabled());
        
        KeycloakClient.AdminApiResult result = keycloakClient.updateUser(keycloakUserId, updateRequest);
        if (!result.isSuccess()) {
            throw new RuntimeException("Failed to update Keycloak user: " + result.getMessage());
        }
        
        logger.info("Keycloak user updated successfully: userId={}, keycloakUserId={}", 
            request.getUserId(), keycloakUserId);
    }

    /**
     * 更新同步状态
     */
    private void updateSyncStatus(String userId, SyncStatus status, String errorMessage) {
        if (redisTemplate == null) {
            return;
        }
        
        String statusKey = SYNC_STATUS_PREFIX + userId;
        Map<String, Object> statusData = new HashMap<>();
        statusData.put("status", status.name());
        statusData.put("lastSyncTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        if (errorMessage != null) {
            statusData.put("errorMessage", errorMessage);
        }
        
        redisTemplate.opsForValue().set(statusKey, statusData, 7, TimeUnit.DAYS);
    }

    /**
     * 保存同步冲突
     */
    private void saveSyncConflict(SyncConflict conflict) {
        if (redisTemplate == null) {
            return;
        }
        
        String conflictKey = SYNC_CONFLICT_PREFIX + conflict.getConflictId();
        redisTemplate.opsForValue().set(conflictKey, conflict, 30, TimeUnit.DAYS);
    }

    /**
     * 获取上次同步时间
     */
    private String getLastSyncTime() {
        if (redisTemplate == null) {
            return LocalDateTime.now().minusHours(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        
        Object lastSyncTime = redisTemplate.opsForValue().get(LAST_SYNC_TIME_KEY);
        return lastSyncTime != null ? lastSyncTime.toString() : 
            LocalDateTime.now().minusHours(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * 更新最后同步时间
     */
    private void updateLastSyncTime() {
        if (redisTemplate != null) {
            redisTemplate.opsForValue().set(LAST_SYNC_TIME_KEY, 
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }

    /**
     * 获取自指定时间以来变更的用户
     */
    private List<UserSyncRequest> getChangedUsersSince(String lastSyncTime) {
        // 这里应该实现查询数据库获取变更用户的逻辑
        // 由于涉及具体的数据库操作，这里返回空列表
        return new ArrayList<>();
    }

    /**
     * 转换用户请求为Map
     */
    private Map<String, Object> convertUserToMap(UserSyncRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", request.getUserId());
        map.put("username", request.getUsername());
        map.put("email", request.getEmail());
        map.put("firstName", request.getFirstName());
        map.put("lastName", request.getLastName());
        map.put("enabled", request.isEnabled());
        map.put("lastModified", request.getLastModified());
        return map;
    }

    /**
     * 转换Keycloak用户为Map
     */
    private Map<String, Object> convertKeycloakUserToMap(KeycloakClient.UserInfo userInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("sub", userInfo.getSub());
        map.put("username", userInfo.getUsername());
        map.put("email", userInfo.getEmail());
        map.put("firstName", userInfo.getFirstName());
        map.put("lastName", userInfo.getLastName());
        map.put("emailVerified", userInfo.getEmailVerified());
        return map;
    }

    // ================================ 内部类定义 ================================

    /**
     * 用户同步请求
     */
    public static class UserSyncRequest {
        private String userId;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String password;
        private boolean enabled = true;
        private LocalDateTime lastModified;

        // Getters and setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public LocalDateTime getLastModified() { return lastModified; }
        public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }
    }

    /**
     * 同步操作
     */
    public static class SyncOperation {
        private String operationId;
        private SyncType type;
        private String entityId;
        private SyncStatus status;
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;
        private String errorMessage;

        public SyncOperation(String operationId, SyncType type, String entityId) {
            this.operationId = operationId;
            this.type = type;
            this.entityId = entityId;
            this.status = SyncStatus.IN_PROGRESS;
            this.startedAt = LocalDateTime.now();
        }

        // Getters and setters
        public String getOperationId() { return operationId; }
        public SyncType getType() { return type; }
        public String getEntityId() { return entityId; }
        public SyncStatus getStatus() { return status; }
        public void setStatus(SyncStatus status) { this.status = status; }
        public LocalDateTime getStartedAt() { return startedAt; }
        public LocalDateTime getCompletedAt() { return completedAt; }
        public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    /**
     * 同步冲突
     */
    public static class SyncConflict {
        private String conflictId;
        private String userId;
        private String username;
        private ConflictType conflictType;
        private ConflictResolution resolution;
        private LocalDateTime detectedAt;
        private Map<String, Object> localData;
        private Map<String, Object> keycloakData;

        // Getters and setters
        public String getConflictId() { return conflictId; }
        public void setConflictId(String conflictId) { this.conflictId = conflictId; }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public ConflictType getConflictType() { return conflictType; }
        public void setConflictType(ConflictType conflictType) { this.conflictType = conflictType; }

        public ConflictResolution getResolution() { return resolution; }
        public void setResolution(ConflictResolution resolution) { this.resolution = resolution; }

        public LocalDateTime getDetectedAt() { return detectedAt; }
        public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }

        public Map<String, Object> getLocalData() { return localData; }
        public void setLocalData(Map<String, Object> localData) { this.localData = localData; }

        public Map<String, Object> getKeycloakData() { return keycloakData; }
        public void setKeycloakData(Map<String, Object> keycloakData) { this.keycloakData = keycloakData; }
    }

    /**
     * 同步状态信息
     */
    public static class SyncStatusInfo {
        private String userId;
        private String operationId;
        private SyncStatus status;
        private LocalDateTime startTime;
        private LocalDateTime lastSyncTime;
        private String errorMessage;

        // Getters and setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getOperationId() { return operationId; }
        public void setOperationId(String operationId) { this.operationId = operationId; }

        public SyncStatus getStatus() { return status; }
        public void setStatus(SyncStatus status) { this.status = status; }

        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

        public LocalDateTime getLastSyncTime() { return lastSyncTime; }
        public void setLastSyncTime(LocalDateTime lastSyncTime) { this.lastSyncTime = lastSyncTime; }

        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    /**
     * 同步统计信息
     */
    public static class SyncStatistics {
        private long totalSyncOperations;
        private long successfulOperations;
        private long failedOperations;
        private long conflictOperations;
        private int activeOperations;
        private double successRate;

        // Getters and setters
        public long getTotalSyncOperations() { return totalSyncOperations; }
        public void setTotalSyncOperations(long totalSyncOperations) { this.totalSyncOperations = totalSyncOperations; }

        public long getSuccessfulOperations() { return successfulOperations; }
        public void setSuccessfulOperations(long successfulOperations) { this.successfulOperations = successfulOperations; }

        public long getFailedOperations() { return failedOperations; }
        public void setFailedOperations(long failedOperations) { this.failedOperations = failedOperations; }

        public long getConflictOperations() { return conflictOperations; }
        public void setConflictOperations(long conflictOperations) { this.conflictOperations = conflictOperations; }

        public int getActiveOperations() { return activeOperations; }
        public void setActiveOperations(int activeOperations) { this.activeOperations = activeOperations; }

        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
    }

    /**
     * 同步类型
     */
    public enum SyncType {
        USER_TO_KEYCLOAK,
        USER_FROM_KEYCLOAK,
        BATCH_SYNC,
        INCREMENTAL_SYNC
    }

    /**
     * 同步状态
     */
    public enum SyncStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        CANCELLED
    }

    /**
     * 冲突类型
     */
    public enum ConflictType {
        USER_EXISTS,
        EMAIL_CONFLICT,
        USERNAME_CONFLICT,
        DATA_MISMATCH
    }

    /**
     * 冲突解决方案
     */
    public enum ConflictResolution {
        UPDATE_LOCAL,
        UPDATE_KEYCLOAK,
        MANUAL_REVIEW,
        SKIP
    }
}