package com.jiuxi.module.auth.infra.persistence.assembler;

import com.jiuxi.module.auth.domain.model.entity.Menu;
import com.jiuxi.module.auth.domain.model.entity.MenuStatus;
import com.jiuxi.module.auth.domain.model.entity.MenuType;
import com.jiuxi.module.auth.infra.persistence.entity.MenuPO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单持久化装配器
 * 负责领域对象与持久化对象之间的转换
 * 实现统一的转换规范和异常处理
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component("authMenuPersistenceAssembler")
public class MenuPersistenceAssembler {
    
    /**
     * 将Menu领域对象转换为MenuPO持久化对象
     * @param menu 菜单领域对象
     * @return 菜单持久化对象
     */
    public MenuPO toPO(Menu menu) {
        if (menu == null) {
            return null;
        }
        
        try {
            MenuPO menuPO = new MenuPO();
            menuPO.setId(menu.getMenuId());
            menuPO.setMenuCode(menu.getMenuCode());
            menuPO.setMenuName(menu.getMenuName());
            menuPO.setMenuTitle(menu.getMenuTitle());
            menuPO.setParentMenuId(menu.getParentMenuId());
            menuPO.setMenuPath(menu.getMenuPath());
            menuPO.setMenuLevel(menu.getMenuLevel());
            menuPO.setMenuType(menu.getMenuType() != null ? menu.getMenuType().getCode() : null);
            menuPO.setMenuUri(menu.getMenuUri());
            menuPO.setMenuIcon(menu.getMenuIcon());
            menuPO.setComponent(menu.getComponent());
            menuPO.setStatus(menu.getStatus() != null ? menu.getStatus().getCode() : null);
            menuPO.setVisible(menu.getVisible());
            menuPO.setKeepAlive(menu.getKeepAlive());
            menuPO.setExternal(menu.getExternal());
            menuPO.setLeaf(menu.getLeaf());
            menuPO.setOrderIndex(menu.getOrderIndex());
            menuPO.setCreator(menu.getCreator());
            menuPO.setCreateTime(menu.getCreateTime());
            menuPO.setUpdator(menu.getUpdator());
            menuPO.setUpdateTime(menu.getUpdateTime());
            menuPO.setTenantId(menu.getTenantId());
            
            return menuPO;
        } catch (Exception e) {
            throw new RuntimeException("Menu领域对象转换为持久化对象失败", e);
        }
    }
    
    /**
     * 将MenuPO持久化对象转换为Menu领域对象
     * @param menuPO 菜单持久化对象
     * @return 菜单领域对象
     */
    public Menu toEntity(MenuPO menuPO) {
        if (menuPO == null) {
            return null;
        }
        
        try {
            Menu menu = new Menu();
            menu.setMenuId(menuPO.getId());
            menu.setMenuCode(menuPO.getMenuCode());
            menu.setMenuName(menuPO.getMenuName());
            menu.setMenuTitle(menuPO.getMenuTitle());
            menu.setParentMenuId(menuPO.getParentMenuId());
            menu.setMenuPath(menuPO.getMenuPath());
            menu.setMenuLevel(menuPO.getMenuLevel());
            
            // 安全转换枚举类型
            if (menuPO.getMenuType() != null) {
                try {
                    menu.setMenuType(MenuType.fromCode(menuPO.getMenuType()));
                } catch (IllegalArgumentException e) {
                    // 记录警告日志，使用默认值
                    menu.setMenuType(MenuType.MENU);
                }
            }
            
            menu.setMenuUri(menuPO.getMenuUri());
            menu.setMenuIcon(menuPO.getMenuIcon());
            menu.setComponent(menuPO.getComponent());
            
            // 安全转换状态枚举
            if (menuPO.getStatus() != null) {
                try {
                    menu.setStatus(MenuStatus.fromCode(menuPO.getStatus()));
                } catch (IllegalArgumentException e) {
                    // 记录警告日志，使用默认值
                    menu.setStatus(MenuStatus.ACTIVE);
                }
            }
            
            menu.setVisible(menuPO.getVisible());
            menu.setKeepAlive(menuPO.getKeepAlive());
            menu.setExternal(menuPO.getExternal());
            menu.setLeaf(menuPO.getLeaf());
            menu.setOrderIndex(menuPO.getOrderIndex());
            menu.setCreator(menuPO.getCreator());
            menu.setCreateTime(menuPO.getCreateTime());
            menu.setUpdator(menuPO.getUpdator());
            menu.setUpdateTime(menuPO.getUpdateTime());
            menu.setTenantId(menuPO.getTenantId());
            
            return menu;
        } catch (Exception e) {
            throw new RuntimeException("MenuPO持久化对象转换为领域对象失败: " + menuPO.getId(), e);
        }
    }
    
    /**
     * 批量转换PO列表为实体列表
     * @param menuPOs 持久化对象列表
     * @return 领域对象列表
     */
    public List<Menu> toEntityList(List<MenuPO> menuPOs) {
        if (menuPOs == null || menuPOs.isEmpty()) {
            return List.of();
        }
        
        return menuPOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 批量转换实体列表为PO列表
     * @param menus 领域对象列表
     * @return 持久化对象列表
     */
    public List<MenuPO> toPOList(List<Menu> menus) {
        if (menus == null || menus.isEmpty()) {
            return List.of();
        }
        
        return menus.stream()
                .map(this::toPO)
                .collect(Collectors.toList());
    }
    
    /**
     * 更新MenuPO对象，保持ID不变
     * @param target 目标持久化对象
     * @param source 源领域对象
     */
    public void updatePO(MenuPO target, Menu source) {
        if (target == null || source == null) {
            throw new IllegalArgumentException("目标对象和源对象都不能为空");
        }
        
        try {
            // 保持主键不变
            String originalId = target.getId();
            
            // 复制属性
            MenuPO newPO = toPO(source);
            if (newPO != null) {
                // 复制所有字段
                target.setMenuCode(newPO.getMenuCode());
                target.setMenuName(newPO.getMenuName());
                target.setMenuTitle(newPO.getMenuTitle());
                target.setParentMenuId(newPO.getParentMenuId());
                target.setMenuPath(newPO.getMenuPath());
                target.setMenuLevel(newPO.getMenuLevel());
                target.setMenuType(newPO.getMenuType());
                target.setMenuUri(newPO.getMenuUri());
                target.setMenuIcon(newPO.getMenuIcon());
                target.setComponent(newPO.getComponent());
                target.setStatus(newPO.getStatus());
                target.setVisible(newPO.getVisible());
                target.setKeepAlive(newPO.getKeepAlive());
                target.setExternal(newPO.getExternal());
                target.setLeaf(newPO.getLeaf());
                target.setOrderIndex(newPO.getOrderIndex());
                target.setCreator(newPO.getCreator());
                target.setCreateTime(newPO.getCreateTime());
                target.setUpdator(newPO.getUpdator());
                target.setUpdateTime(newPO.getUpdateTime());
                target.setTenantId(newPO.getTenantId());
                
                // 恢复原始ID
                target.setId(originalId);
            }
        } catch (Exception e) {
            throw new RuntimeException("更新MenuPO对象失败", e);
        }
    }
}