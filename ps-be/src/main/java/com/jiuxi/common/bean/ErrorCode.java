package com.jiuxi.common.bean;

import java.io.Serializable;

/**
 * @ClassName: ResponseCode
 * @Description: 错误码code
 * @Author: Ypp
 * @Date: 2020/1/10 11:30
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public enum ErrorCode implements Serializable {

    SUCCESS(1, "成功"),

    ERROR(-1, "失败"),

    PARAM_ERROR(-3, "参数错误"),

    // 认证相关错误码
    TOKEN_ERROR(-44, "token过期，凭证验签失败！"),
    AUTHHORIZONTAL_ERROR(-45, "水平越权验证失败！"),
    UNAUTHORIZED(-46, "未授权访问"),
    FORBIDDEN(-47, "访问被禁止"),
    LOGIN_REQUIRED(-48, "请先登录"),

    // 业务相关错误码
    DATA_NOT_FOUND(-50, "数据不存在"),
    DATA_ALREADY_EXISTS(-51, "数据已存在"),
    DATA_IN_USE(-52, "数据正在使用中，无法删除"),
    OPERATION_NOT_ALLOWED(-53, "操作不被允许"),
    STATUS_ERROR(-54, "状态错误"),

    // 文件相关错误码
    FILE_NOT_FOUND(-60, "文件不存在"),
    FILE_UPLOAD_FAILED(-61, "文件上传失败"),
    FILE_TYPE_NOT_SUPPORTED(-62, "不支持的文件类型"),
    FILE_SIZE_EXCEEDED(-63, "文件大小超出限制"),

    // 网络相关错误码
    NETWORK_ERROR(-70, "网络错误"),
    TIMEOUT_ERROR(-71, "请求超时"),
    SERVICE_UNAVAILABLE(-72, "服务不可用"),

    // 数据库相关错误码
    DATABASE_ERROR(-80, "数据库操作失败"),
    DUPLICATE_KEY_ERROR(-81, "数据重复"),
    FOREIGN_KEY_ERROR(-82, "外键约束错误"),

    // 验证相关错误码
    VALIDATION_FAILED(-90, "数据验证失败"),
    CAPTCHA_ERROR(-91, "验证码错误"),
    PASSWORD_ERROR(-92, "密码错误"),
    ACCOUNT_LOCKED(-93, "账户已锁定"),
    ACCOUNT_DISABLED(-94, "账户已禁用"),

    // 系统相关错误码
    SYTEM_LOGIN_NO_ROLE_ERROR(-98, "未分配角色，请联系管理员！"),
    UNSELECTDEPT_ERROR(-99, "未选择部门登陆"),
    SYTEM_ERROR(-100, "系统错误，请联系管理员！"),
    CONFIG_ERROR(-101, "系统配置错误"),
    LICENSE_ERROR(-102, "许可证错误");

    private int code;

    private String msg;


    private ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }
}
