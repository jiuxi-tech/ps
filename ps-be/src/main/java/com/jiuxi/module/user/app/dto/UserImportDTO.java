package com.jiuxi.module.user.app.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户导入DTO
 * 用于接收Excel文件解析后的用户数据
 * 
 * @author User Import Refactor
 * @date 2025-11-27
 */
@Data
public class UserImportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Excel行号（用于错误定位）
     */
    private Integer rowNumber;

    /**
     * 账号名（必填）
     */
    private String username;

    /**
     * 初始密码（必填）
     */
    private String password;

    /**
     * 姓名（必填）
     */
    private String personName;

    /**
     * 性别（男/女，可选）
     */
    private String sex;

    /**
     * 部门层级路径（必填，如"AAA部>BBB部>CCC办公室"）
     */
    private String deptPath;

    /**
     * 参加工作时间（支持 YYYY、YYYY-MM、YYYY-MM-DD 三种格式，可选）
     */
    private String partWorkDate;

    /**
     * 职务职级（可选）
     * @deprecated 已废弃，请使用 zwzj 字段
     */
    @Deprecated
    private String rank;

    /**
     * 职称名称（可选）
     * @deprecated 已废弃，请使用 zhicheng 字段
     */
    @Deprecated
    private String titleName;

    /**
     * 职务职级（新字段，可选）
     */
    private String zwzj;

    /**
     * 职称（新字段，可选）
     */
    private String zhicheng;

    /**
     * 身份证号码（可选）
     */
    private String idcard;
}
