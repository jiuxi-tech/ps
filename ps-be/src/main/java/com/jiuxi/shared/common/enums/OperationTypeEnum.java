package com.jiuxi.shared.common.enums;

/**
 * 操作类型枚举
 * 定义系统中的各种操作类型
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public enum OperationTypeEnum {

    /** 查询操作 */
    QUERY("QUERY", "查询", "查询数据操作", "blue"),
    
    /** 创建操作 */
    CREATE("CREATE", "创建", "创建新数据操作", "green"),
    
    /** 更新操作 */
    UPDATE("UPDATE", "更新", "更新已有数据操作", "orange"),
    
    /** 删除操作 */
    DELETE("DELETE", "删除", "删除数据操作", "red"),
    
    /** 批量删除操作 */
    BATCH_DELETE("BATCH_DELETE", "批量删除", "批量删除多条数据操作", "red"),
    
    /** 导入操作 */
    IMPORT("IMPORT", "导入", "导入数据操作", "cyan"),
    
    /** 导出操作 */
    EXPORT("EXPORT", "导出", "导出数据操作", "purple"),
    
    /** 登录操作 */
    LOGIN("LOGIN", "登录", "用户登录操作", "green"),
    
    /** 登出操作 */
    LOGOUT("LOGOUT", "登出", "用户登出操作", "gray"),
    
    /** 启用操作 */
    ENABLE("ENABLE", "启用", "启用功能或数据操作", "green"),
    
    /** 禁用操作 */
    DISABLE("DISABLE", "禁用", "禁用功能或数据操作", "red"),
    
    /** 审核操作 */
    AUDIT("AUDIT", "审核", "审核数据操作", "blue"),
    
    /** 审核通过操作 */
    APPROVE("APPROVE", "审核通过", "审核通过操作", "green"),
    
    /** 审核拒绝操作 */
    REJECT("REJECT", "审核拒绝", "审核拒绝操作", "red"),
    
    /** 授权操作 */
    AUTHORIZE("AUTHORIZE", "授权", "权限授权操作", "blue"),
    
    /** 取消授权操作 */
    REVOKE("REVOKE", "取消授权", "取消权限授权操作", "orange"),
    
    /** 重置操作 */
    RESET("RESET", "重置", "重置数据或配置操作", "orange"),
    
    /** 同步操作 */
    SYNC("SYNC", "同步", "数据同步操作", "cyan"),
    
    /** 备份操作 */
    BACKUP("BACKUP", "备份", "数据备份操作", "purple"),
    
    /** 恢复操作 */
    RESTORE("RESTORE", "恢复", "数据恢复操作", "purple"),
    
    /** 配置操作 */
    CONFIG("CONFIG", "配置", "系统配置操作", "blue"),
    
    /** 部署操作 */
    DEPLOY("DEPLOY", "部署", "应用部署操作", "green"),
    
    /** 监控操作 */
    MONITOR("MONITOR", "监控", "系统监控操作", "cyan"),
    
    /** 清理操作 */
    CLEAN("CLEAN", "清理", "数据清理操作", "orange"),
    
    /** 测试操作 */
    TEST("TEST", "测试", "功能测试操作", "blue"),
    
    /** 其他操作 */
    OTHER("OTHER", "其他", "其他未分类操作", "gray");

    private final String code;
    private final String name;
    private final String description;
    private final String color;

    OperationTypeEnum(String code, String name, String description, String color) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    /**
     * 根据代码获取枚举
     */
    public static OperationTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (OperationTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举
     */
    public static OperationTypeEnum fromName(String name) {
        if (name == null) {
            return null;
        }
        for (OperationTypeEnum type : values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断是否为写操作
     */
    public boolean isWriteOperation() {
        return this == CREATE || this == UPDATE || this == DELETE || this == BATCH_DELETE 
               || this == IMPORT || this == ENABLE || this == DISABLE || this == APPROVE 
               || this == REJECT || this == AUTHORIZE || this == REVOKE || this == RESET
               || this == SYNC || this == BACKUP || this == RESTORE || this == CONFIG
               || this == DEPLOY || this == CLEAN;
    }

    /**
     * 判断是否为读操作
     */
    public boolean isReadOperation() {
        return this == QUERY || this == EXPORT || this == MONITOR;
    }

    /**
     * 判断是否为危险操作
     */
    public boolean isDangerousOperation() {
        return this == DELETE || this == BATCH_DELETE || this == DISABLE 
               || this == REJECT || this == REVOKE || this == RESET || this == CLEAN;
    }

    /**
     * 判断是否为认证相关操作
     */
    public boolean isAuthOperation() {
        return this == LOGIN || this == LOGOUT || this == AUTHORIZE || this == REVOKE;
    }

    /**
     * 判断是否为审核相关操作
     */
    public boolean isAuditOperation() {
        return this == AUDIT || this == APPROVE || this == REJECT;
    }

    /**
     * 获取操作风险级别（1-5，数字越大风险越高）
     */
    public int getRiskLevel() {
        if (isDangerousOperation()) {
            return 5;
        } else if (isWriteOperation() && !isAuditOperation()) {
            return 3;
        } else if (isAuditOperation()) {
            return 4;
        } else if (isReadOperation()) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s", code, name, description);
    }
}