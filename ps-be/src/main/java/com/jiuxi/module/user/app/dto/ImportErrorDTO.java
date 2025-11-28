package com.jiuxi.module.user.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 导入错误详情DTO
 * 用于记录导入过程中的错误信息
 * 
 * @author User Import Refactor
 * @date 2025-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportErrorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 错误行号
     */
    private Integer row;

    /**
     * 错误字段
     */
    private String field;

    /**
     * 字段值
     */
    private String value;

    /**
     * 错误信息
     */
    private String message;
}
