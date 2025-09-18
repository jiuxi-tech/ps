package com.jiuxi.module.auth.infra.persistence.exception;

/**
 * 持久化层异常
 * 统一的持久化操作异常处理
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class PersistenceException extends RuntimeException {
    
    private final String errorCode;
    private final String operation;
    private final String entityType;
    private final String entityId;
    
    public PersistenceException(String message) {
        super(message);
        this.errorCode = "PERSISTENCE_ERROR";
        this.operation = "UNKNOWN";
        this.entityType = "UNKNOWN";
        this.entityId = null;
    }
    
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "PERSISTENCE_ERROR";
        this.operation = "UNKNOWN";
        this.entityType = "UNKNOWN";
        this.entityId = null;
    }
    
    public PersistenceException(String errorCode, String operation, String entityType, String entityId, String message) {
        super(buildMessage(operation, entityType, entityId, message));
        this.errorCode = errorCode;
        this.operation = operation;
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    public PersistenceException(String errorCode, String operation, String entityType, String entityId, String message, Throwable cause) {
        super(buildMessage(operation, entityType, entityId, message), cause);
        this.errorCode = errorCode;
        this.operation = operation;
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    private static String buildMessage(String operation, String entityType, String entityId, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("持久化操作失败: ");
        sb.append("操作=").append(operation);
        sb.append(", 实体类型=").append(entityType);
        if (entityId != null) {
            sb.append(", 实体ID=").append(entityId);
        }
        sb.append(", 错误信息=").append(message);
        return sb.toString();
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public String getEntityId() {
        return entityId;
    }
    
    // 静态工厂方法
    public static PersistenceException saveError(String entityType, String entityId, Throwable cause) {
        return new PersistenceException("SAVE_ERROR", "SAVE", entityType, entityId, 
                "保存实体失败", cause);
    }
    
    public static PersistenceException updateError(String entityType, String entityId, Throwable cause) {
        return new PersistenceException("UPDATE_ERROR", "UPDATE", entityType, entityId, 
                "更新实体失败", cause);
    }
    
    public static PersistenceException deleteError(String entityType, String entityId, Throwable cause) {
        return new PersistenceException("DELETE_ERROR", "DELETE", entityType, entityId, 
                "删除实体失败", cause);
    }
    
    public static PersistenceException findError(String entityType, String entityId, Throwable cause) {
        return new PersistenceException("FIND_ERROR", "FIND", entityType, entityId, 
                "查找实体失败", cause);
    }
    
    public static PersistenceException queryError(String entityType, String condition, Throwable cause) {
        return new PersistenceException("QUERY_ERROR", "QUERY", entityType, condition, 
                "查询实体失败", cause);
    }
    
    public static PersistenceException conversionError(String entityType, String direction, Throwable cause) {
        return new PersistenceException("CONVERSION_ERROR", "CONVERT", entityType, direction, 
                "对象转换失败", cause);
    }
}