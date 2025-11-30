package com.jiuxi.admin.core.bean.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * API定义实体类
 * 
 * @author API Management
 * @since 2025-01-30
 */
@Data
public class TpApiDefinition implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * API ID（主键）
     */
    private String apiId;
    
    /**
     * API编码（唯一）
     */
    private String apiCode;
    
    /**
     * API名称
     */
    private String apiName;
    
    /**
     * API路径
     */
    private String apiPath;
    
    /**
     * HTTP方法
     */
    private String httpMethod;
    
    /**
     * API分类
     */
    private String category;
    
    /**
     * API描述
     */
    private String description;
    
    /**
     * 是否敏感接口（1:是 0:否）
     */
    private Integer isSensitive;
    
    /**
     * 是否需要签名验证（1:是 0:否）
     */
    private Integer requireSecret;
    
    /**
     * 状态（1:启用 0:禁用）
     */
    private Integer status;
    
    /**
     * 排序序号
     */
    private Double orderIndex;
    
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
}
