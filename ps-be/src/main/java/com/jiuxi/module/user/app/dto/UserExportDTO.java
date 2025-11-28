package com.jiuxi.module.user.app.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户导出DTO
 * 用于导出用户数据到Excel文件
 * 
 * @author User Import Refactor
 * @date 2025-11-27
 */
@Data
public class UserExportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账号名
     */
    private String username;

    /**
     * 姓名
     */
    private String personName;

    /**
     * 性别（男/女）
     */
    private String sexName;

    /**
     * 部门全路径
     */
    private String deptFullPath;

    /**
     * 参加工作时间
     */
    private String partWorkDate;

    /**
     * 职务职级
     * @deprecated 已废弃，请使用 zwzj 字段
     */
    @Deprecated
    private String rank;

    /**
     * 职称名称
     * @deprecated 已废弃，请使用 zhicheng 字段
     */
    @Deprecated
    private String titleName;

    /**
     * 职务职级（新字段）
     */
    private String zwzj;

    /**
     * 职称（新字段）
     */
    private String zhicheng;

    /**
     * 身份证号码
     */
    private String idcard;
}
