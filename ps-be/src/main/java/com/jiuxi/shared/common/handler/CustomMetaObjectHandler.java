package com.jiuxi.shared.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * MyBatis Plus 元数据自动填充处理器
 * 自动填充实体的审计字段
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Slf4j
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始插入时的填充...");
        
        LocalDateTime now = LocalDateTime.now();
        
        // 填充创建时间和更新时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
        
        // 填充逻辑删除字段
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
        
        // 填充版本号
        this.strictInsertFill(metaObject, "version", Integer.class, 1);
        
        // 填充审计字段（从当前上下文获取）
        String currentUserId = getCurrentUserId();
        String currentUserName = getCurrentUserName();
        
        if (currentUserId != null) {
            this.strictInsertFill(metaObject, "createBy", String.class, currentUserId);
            this.strictInsertFill(metaObject, "updateBy", String.class, currentUserId);
        }
        
        if (currentUserName != null) {
            this.strictInsertFill(metaObject, "createByName", String.class, currentUserName);
            this.strictInsertFill(metaObject, "updateByName", String.class, currentUserName);
        }
    }

    /**
     * 更新时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始更新时的填充...");
        
        // 填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        
        // 填充更新者信息
        String currentUserId = getCurrentUserId();
        String currentUserName = getCurrentUserName();
        
        if (currentUserId != null) {
            this.strictUpdateFill(metaObject, "updateBy", String.class, currentUserId);
        }
        
        if (currentUserName != null) {
            this.strictUpdateFill(metaObject, "updateByName", String.class, currentUserName);
        }
    }

    /**
     * 获取当前用户ID
     * TODO: 集成实际的用户上下文获取逻辑
     */
    private String getCurrentUserId() {
        // 这里应该从SecurityContextHolder或其他用户上下文中获取
        // 暂时返回系统用户
        return "system";
    }

    /**
     * 获取当前用户名称
     * TODO: 集成实际的用户上下文获取逻辑
     */
    private String getCurrentUserName() {
        // 这里应该从SecurityContextHolder或其他用户上下文中获取
        // 暂时返回系统用户
        return "系统";
    }
}