package com.jiuxi.module.auth.infra.persistence.base;

import com.jiuxi.module.auth.infra.persistence.exception.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * 基础仓储实现类
 * 提供统一的异常处理和日志记录
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public abstract class BaseRepositoryImpl {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * 安全执行数据库操作
     * @param operation 操作描述
     * @param entityType 实体类型
     * @param entityId 实体ID
     * @param action 具体操作
     * @return 操作结果
     */
    protected <T> T executeWithExceptionHandling(String operation, String entityType, String entityId, DatabaseOperation<T> action) {
        try {
            logger.debug("开始执行{}操作: entityType={}, entityId={}", operation, entityType, entityId);
            T result = action.execute();
            logger.debug("完成{}操作: entityType={}, entityId={}", operation, entityType, entityId);
            return result;
        } catch (OptimisticLockingFailureException e) {
            logger.warn("乐观锁冲突: operation={}, entityType={}, entityId={}", operation, entityType, entityId, e);
            throw new PersistenceException("OPTIMISTIC_LOCK_ERROR", operation, entityType, entityId, 
                    "数据已被其他用户修改，请刷新后重试", e);
        } catch (DataAccessException e) {
            logger.error("数据访问异常: operation={}, entityType={}, entityId={}", operation, entityType, entityId, e);
            throw createPersistenceException(operation, entityType, entityId, e);
        } catch (Exception e) {
            logger.error("未知异常: operation={}, entityType={}, entityId={}", operation, entityType, entityId, e);
            throw new PersistenceException("UNKNOWN_ERROR", operation, entityType, entityId, 
                    "未知错误: " + e.getMessage(), e);
        }
    }
    
    /**
     * 安全执行数据库操作（无返回值）
     * @param operation 操作描述
     * @param entityType 实体类型
     * @param entityId 实体ID
     * @param action 具体操作
     */
    protected void executeWithExceptionHandling(String operation, String entityType, String entityId, VoidDatabaseOperation action) {
        executeWithExceptionHandling(operation, entityType, entityId, () -> {
            action.execute();
            return null;
        });
    }
    
    /**
     * 根据数据访问异常创建相应的持久化异常
     */
    private PersistenceException createPersistenceException(String operation, String entityType, String entityId, DataAccessException e) {
        String message = e.getMessage();
        
        // 根据异常类型和消息内容判断具体错误
        if (message != null) {
            if (message.contains("Duplicate entry") || message.contains("duplicate key")) {
                return new PersistenceException("DUPLICATE_KEY_ERROR", operation, entityType, entityId, 
                        "数据重复，违反唯一约束", e);
            } else if (message.contains("foreign key constraint") || message.contains("violates foreign key")) {
                return new PersistenceException("FOREIGN_KEY_ERROR", operation, entityType, entityId, 
                        "违反外键约束", e);
            } else if (message.contains("cannot be null") || message.contains("not-null")) {
                return new PersistenceException("NOT_NULL_ERROR", operation, entityType, entityId, 
                        "必填字段不能为空", e);
            } else if (message.contains("timeout") || message.contains("timed out")) {
                return new PersistenceException("TIMEOUT_ERROR", operation, entityType, entityId, 
                        "数据库操作超时", e);
            }
        }
        
        // 默认数据访问异常
        return new PersistenceException("DATA_ACCESS_ERROR", operation, entityType, entityId, 
                "数据访问失败: " + message, e);
    }
    
    /**
     * 验证实体参数
     */
    protected void validateEntity(Object entity, String entityType) {
        if (entity == null) {
            throw new IllegalArgumentException(entityType + "实体不能为空");
        }
    }
    
    /**
     * 验证ID参数
     */
    protected void validateId(String id, String entityType) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException(entityType + "ID不能为空");
        }
    }
    
    /**
     * 验证租户ID参数
     */
    protected void validateTenantId(String tenantId) {
        if (tenantId == null || tenantId.trim().isEmpty()) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
    }
    
    /**
     * 数据库操作接口（有返回值）
     */
    @FunctionalInterface
    protected interface DatabaseOperation<T> {
        T execute() throws Exception;
    }
    
    /**
     * 数据库操作接口（无返回值）
     */
    @FunctionalInterface
    protected interface VoidDatabaseOperation {
        void execute() throws Exception;
    }
}