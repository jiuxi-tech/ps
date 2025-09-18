package com.jiuxi.module.auth.app.service;

import com.jiuxi.module.auth.app.query.dto.MenuQueryDTO;
import com.jiuxi.module.auth.app.query.dto.MenuTreeQueryDTO;

import java.util.List;

/**
 * 菜单查询服务接口
 * 定义菜单相关的查询操作接口
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public interface MenuQueryService {
    
    /**
     * 根据ID查询菜单
     * @param menuId 菜单ID
     * @return 菜单查询DTO
     */
    MenuQueryDTO getMenuById(String menuId);
    
    /**
     * 根据菜单编码查询菜单
     * @param menuCode 菜单编码
     * @param tenantId 租户ID
     * @return 菜单查询DTO
     */
    MenuQueryDTO getMenuByCode(String menuCode, String tenantId);
    
    /**
     * 获取菜单树
     * @param tenantId 租户ID
     * @return 菜单树查询DTO列表
     */
    List<MenuTreeQueryDTO> getMenuTree(String tenantId);
    
    /**
     * 获取子菜单列表
     * @param parentMenuId 父菜单ID
     * @param tenantId 租户ID
     * @return 菜单查询DTO列表
     */
    List<MenuQueryDTO> getChildMenus(String parentMenuId, String tenantId);
    
    /**
     * 批量获取菜单
     * @param menuIds 菜单ID列表
     * @param tenantId 租户ID
     * @return 菜单查询DTO列表
     */
    List<MenuQueryDTO> getMenusByIds(List<String> menuIds, String tenantId);
    
    /**
     * 检查菜单编码是否存在
     * @param menuCode 菜单编码
     * @param tenantId 租户ID
     * @return 是否存在
     */
    boolean isMenuCodeExists(String menuCode, String tenantId);
}