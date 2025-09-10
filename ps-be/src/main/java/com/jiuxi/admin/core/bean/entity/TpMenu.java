package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 菜单表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:18
 */
@TableName("tp_menu")
public class TpMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单主键
     */
    @TableId
    private String menuId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单CODE，唯一，按钮权限控制使用
     */
    private String menuCode;
    /**
     * 菜单路径
     */
    private String menuUri;
    /**
     * 菜单父节点
     */
    private String menuPid;

    /**
     * 父级树节点id
     */
    private String menuTreePid;

    /**
     * 菜单归属 （2：app/1：pc）
     */
    private Integer menuSource;

    /**
     * 菜单类型:
     * SYS1901 菜单,
     * SYS1902 内部接口,
     * SYS1903 按钮,
     * SYS1904 外部接口,
     * SYS1905 外部菜单,
     * SYS1906 大屏菜单,
     * SYS1907 菜单分类节点
     */
    private String menuType;
    /**
     * 菜单图标
     */
    private String menuIcon;
    /**
     * 菜单简介
     */
    private String menuDesc;
    /**
     * 菜单排序
     */
    private Double orderIndex;
    /**
     * 是否启用
     */
    private Integer enabled;
    /**
     * 是否有效
     */
    private Integer actived;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 修改人
     */
    private String updator;
    /**
     * 扩展字段01
     */
    private String extend01;
    /**
     * 扩展字段02
     */
    private String extend02;
    /**
     * 扩展字段03
     */
    private String extend03;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuUri() {
        return menuUri;
    }

    public void setMenuUri(String menuUri) {
        this.menuUri = menuUri;
    }

    public String getMenuPid() {
        return menuPid;
    }

    public void setMenuPid(String menuPid) {
        this.menuPid = menuPid;
    }

    public Integer getMenuSource() {
        return menuSource;
    }

    public void setMenuSource(Integer menuSource) {
        this.menuSource = menuSource;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public Double getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Double orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getActived() {
        return actived;
    }

    public void setActived(Integer actived) {
        this.actived = actived;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
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

    public String getMenuTreePid() {
        return menuTreePid;
    }

    public void setMenuTreePid(String menuTreePid) {
        this.menuTreePid = menuTreePid;
    }
}
