package com.jiuxi.module.auth.domain.repo;

import com.jiuxi.module.auth.domain.entity.Menu;
import java.util.List;
import java.util.Optional;

/**
 * 菜单仓储接口
 * 定义菜单实体的持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public interface MenuRepository {
    
    /**
     * 保存菜单
     * @param menu 菜单实体
     * @return 保存后的菜单
     */
    Menu save(Menu menu);
    
    /**
     * 根据ID查找菜单
     * @param menuId 菜单ID
     * @return 菜单Optional
     */
    Optional<Menu> findById(String menuId);
    
    /**
     * 根据ID删除菜单
     * @param menuId 菜单ID
     */
    void deleteById(String menuId);
    
    /**
     * 根据菜单编码查找菜单
     * @param menuCode 菜单编码
     * @param tenantId 租户ID
     * @return 菜单Optional
     */
    Optional<Menu> findByMenuCode(String menuCode, String tenantId);
    
    /**
     * 获取菜单树
     * @param tenantId 租户ID
     * @return 菜单树列表
     */
    List<Menu> getMenuTree(String tenantId);
    
    /**
     * 获取子菜单列表
     * @param parentMenuId 父菜单ID
     * @param tenantId 租户ID
     * @return 子菜单列表
     */
    List<Menu> getChildMenus(String parentMenuId, String tenantId);
}