package com.jiuxi.common.bean;


import com.jiuxi.common.util.CommonTreeUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: ResponseCode
 * @Description: 树形控件节点格式
 * @Author: Ypp
 * @Date: 2020/1/10 11:31
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TreeNode implements Serializable, CommonTreeUtil.Tree<TreeNode> {

    /**
     * 菜单id
     */
    private String id;
    /**
     * 字典编码
     */
    private String value;

    /**
     * 父级菜单id
     */
    private String pid;

    /**
     * 菜单名称
     */
    private String text;
    /**
     * 名称
     */
    private String label;

    /**
     * 是否叶子节点
     */
    private boolean leaf;

    /**
     * 是否展开
     */
    private boolean expand;

    /**
     * 图标
     */
    private String icon;

    /**
     * 是否选中
     */
    private boolean checked;

    /**
     * 是否禁用（禁用则不能选择）
     */
    private boolean disabled;

    /**
     * 额外信息1
     */
    private String extend;
    private String extend01;

    /**
     * 额外信息2
     */
    private String extend02;

    /**
     * 额外信息3
     */
    private String extend03;

    /**
     * 排序号
     */
    private BigDecimal orderNo;

    /**
     * 下级节点
     */
    private List<TreeNode> children = new ArrayList<>();

    @Override
    public Object getTreeId() {
        return this.id;
    }

    @Override
    public Object getTreePid() {
        return this.pid;
    }

    @Override
    public List<TreeNode> getTreeChildren() {
        return this.children;
    }

    @Override
    public void setTreeChildren(List<TreeNode> list) {
        this.children = list;
    }

    @Override
    public void setTreeLeaf(boolean bool) {
        this.leaf = bool;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean getExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getExtend01() {
        return extend01;
    }

    public void setExtend01(String extend01) {
        this.extend01 = extend01;
    }

    public String getExtend02() {
        return extend02;
    }

    public void setExtend02(String extend02) {
        this.extend02 = extend02;
    }

    public String getExtend03() {
        return extend03;
    }

    public void setExtend03(String extend03) {
        this.extend03 = extend03;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void addChildren(TreeNode treeNode) {
        children.add(treeNode);
    }

    public void addChildrens(List<TreeNode> list) {
        children.addAll(list);
    }

    public void cleanChildren() {
        children.clear();
    }

    @Override
    public BigDecimal getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(BigDecimal orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TreeNode treeNode = (TreeNode) o;
        return Objects.equals(id, treeNode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
