package com.jiuxi.admin.core.bean.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 第三方应用实体类
 * 
 * @author API Management
 * @since 2025-01-30
 */
@Data
public class TpThirdPartyApp implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 应用ID（主键）
     */
    private String appId;
    
    /**
     * 应用名称
     */
    private String appName;
    
    /**
     * API密钥（UUID格式）
     */
    private String apiKey;
    
    /**
     * 状态（1:启用 0:禁用）
     */
    private Integer status;
    
    /**
     * 过期时间（yyyyMMddHHmmss）
     */
    private String expireTime;
    
    /**
     * 应用描述
     */
    private String description;
    
    /**
     * 联系人
     */
    private String contactPerson;
    
    /**
     * 联系电话
     */
    private String contactPhone;
    
    /**
     * 联系邮箱
     */
    private String contactEmail;
    
    /**
     * IP白名单（逗号分隔）
     */
    private String ipWhitelist;
    
    /**
     * 限流配置（次/秒）
     */
    private Integer rateLimit;
    
    /**
     * 是否有效（1:有效 0:无效）
     */
    private Integer actived;
    
    /**
     * 创建人
     */
    private String creator;
    
    /**
     * 创建时间
     */
    private String createTime;
    
    /**
     * 修改人
     */
    private String updator;
    
    /**
     * 修改时间
     */
    private String updateTime;
    
    /**
     * 租户ID
     */
    private String tenantId;
}
