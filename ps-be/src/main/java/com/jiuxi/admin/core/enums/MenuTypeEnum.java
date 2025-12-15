package com.jiuxi.admin.core.enums;

/**
 * 菜单分类
 *
 * @author jiuxx
 * @date 2024/5/16 13:46
 */
public enum MenuTypeEnum {

    SYS1901("SYS1901", "菜单"),
    SYS1902("SYS1902", "内部接口"),
    SYS1903("SYS1903", "按钮"),
    SYS1904("SYS1904", "外部接口"),
    SYS1905("SYS1905", "外部菜单"),
    SYS1906("SYS1906", "大屏菜单"),
    SYS1907("SYS1907", "菜单分类节点");

    private String code;

    private String desc;

    MenuTypeEnum(String code, String desc) {
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
