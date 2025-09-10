package com.jiuxi.shared.common.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 * 包含所有实体的公共字段，提供统一的数据模型基础
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Data
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

    /**
     * 创建者ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 更新者ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 创建者姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String createByName;

    /**
     * 更新者姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateByName;

    /**
     * 是否删除（0：未删除，1：已删除）
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted = 0;

    /**
     * 版本号（用于乐观锁）
     */
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version = 1;

    /**
     * 租户ID（用于多租户场景）
     */
    private String tenantId;

    /**
     * 业务状态（1：启用，0：禁用）
     */
    private Integer status = 1;

    /**
     * 排序字段
     */
    private Integer sortOrder;

    /**
     * 备注
     */
    private String remark;
}