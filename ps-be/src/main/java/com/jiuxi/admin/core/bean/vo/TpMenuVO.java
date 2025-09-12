package com.jiuxi.admin.core.bean.vo;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jiuxi.common.serializer.UrlJsonSerializer;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 菜单表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:18
 */
public class TpMenuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单主键
     */
    @NotBlank(message = "菜单id不能为空", groups = UpdateGroup.class)
    private String menuId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String menuName;

    /**
     * 菜单CODE，唯一，按钮权限控制使用，对于menuCode
     */
    private String menuCode;
    /**
     * 菜单路径
     */
    @JsonSerialize(using = UrlJsonSerializer.class)
    private String menuUri;
    /**
     * 菜单父节点
     */
    private String menuPid;

    /**
     * 父级树节点id
     */
    @NotBlank(message = "父级树节点id不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String menuTreePid;

    /**
     * 菜单归属 （2：app/1：pc）
     */
    @NotNull(message = "菜单归属不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer menuSource;
    private String menuSourceName;
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
    @NotBlank(message = "菜单类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String menuType;
    private String menuTypeName;
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
    @NotNull(message = "菜单排序不能为空", groups = {AddGroup.class, UpdateGroup.class})
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

    /**
     * 是否选中
     */
    private Boolean checked;

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

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
    
    /**
     * 处理字符串参数的setter方法
     * 用于处理前端传递'null'字符串的情况
     */
    @JsonSetter("checked")
    public void setChecked(String checked) {
        if ("null".equals(checked) || checked == null || checked.trim().isEmpty()) {
            this.checked = null;
        } else {
            this.checked = Boolean.valueOf(checked);
        }
    }

    public String getMenuSourceName() {
        return menuSourceName;
    }

    public void setMenuSourceName(String menuSourceName) {
        this.menuSourceName = menuSourceName;
    }

    public String getMenuTypeName() {
        return menuTypeName;
    }

    public void setMenuTypeName(String menuTypeName) {
        this.menuTypeName = menuTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TpMenuVO tpMenuVO = (TpMenuVO) o;
        return Objects.equals(menuId, tpMenuVO.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId);
    }

    public String getMenuTreePid() {
        return menuTreePid;
    }

    public void setMenuTreePid(String menuTreePid) {
        this.menuTreePid = menuTreePid;
    }
}
