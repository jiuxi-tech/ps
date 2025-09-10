package com.jiuxi.shared.common.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础DTO类
 * 包含所有DTO的公共字段，用于数据传输
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Data
public abstract class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

    /**
     * 创建者ID
     */
    private String createBy;

    /**
     * 更新者ID
     */
    private String updateBy;

    /**
     * 创建者姓名
     */
    private String createByName;

    /**
     * 更新者姓名
     */
    private String updateByName;

    /**
     * 版本号（用于乐观锁）
     */
    private Integer version;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 业务状态（1：启用，0：禁用）
     */
    private Integer status;

    /**
     * 排序字段
     */
    private Integer sortOrder;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}