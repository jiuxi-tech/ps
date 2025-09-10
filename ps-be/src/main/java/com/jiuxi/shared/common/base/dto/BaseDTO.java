package com.jiuxi.shared.common.base.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 基础数据传输对象
 * 所有DTO的基类
 */
@Data
public abstract class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;
}