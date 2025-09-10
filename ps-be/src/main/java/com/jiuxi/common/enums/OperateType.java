package com.jiuxi.common.enums;

/**
 * @ClassName: OperateType
 * @Description: 操作类型
 * @Author: 杨攀
 * @Date: 2023/10/20 16:55
 * @Copyright: 2023 www.tuxun.net Inc. All rights reserved.
 */
public enum OperateType {

    ADD("add", "新增"),
    UPDATE("update", "修改"),
    DELETE("delete", "删除"),
    LOGIN("login", "登录");

    private String code;

    private String desc;

    OperateType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
