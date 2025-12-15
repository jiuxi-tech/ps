package com.jiuxi.admin.bean;

import com.jiuxi.common.bean.TreeNode;

import java.util.Objects;

/**
 * @Description: 菜单树节点
 * @ClassName: MenuTreeNode
 * @Author: pdd
 * @Date: 2020-12-08 09:19
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class MenuTreeNode extends TreeNode {

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单CODE，唯一，按钮权限控制使用，对于menuCode
     */
    private String code;

    /**
     * 菜单类型 （按钮、菜单、接口）
     * SYS1901	菜单
     * SYS1902	接口
     * SYS1903	按钮
     */
    private String type;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuTreeNode menuTreeNode = (MenuTreeNode) o;
        return Objects.equals(getId(), menuTreeNode.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
