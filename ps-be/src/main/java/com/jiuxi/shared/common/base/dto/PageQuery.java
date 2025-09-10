package com.jiuxi.shared.common.base.dto;

import lombok.Data;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 分页查询参数
 */
@Data
public class PageQuery extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 页码（从1开始）
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 1000, message = "每页大小不能超过1000")
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String sortBy;

    /**
     * 排序方向（ASC/DESC）
     */
    private String sortDirection = "DESC";
}