package com.jiuxi.admin.core.bean.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 应用API权限关联实体类
 * 
 * @author API Management
 * @since 2025-01-30
 */
@Data
public class TpAppApiPermission implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 权限ID（主键）
     */
    private String permissionId;
    
    /**
     * 应用ID
     */
    private String appId;
    
    /**
     * API ID
     */
    private String apiId;
    
    /**
     * 创建人
     */
    private String creator;
    
    /**
     * 创建时间
     */
    private String createTime;
}
