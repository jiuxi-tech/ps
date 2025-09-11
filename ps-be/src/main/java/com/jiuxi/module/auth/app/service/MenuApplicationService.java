package com.jiuxi.module.auth.app.service;

import com.jiuxi.module.auth.domain.entity.Menu;
import com.jiuxi.module.auth.domain.repo.MenuRepository;
import com.jiuxi.module.auth.domain.service.MenuDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 菜单应用服务
 * 负责菜单相关的应用逻辑和事务协调
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Service
@Transactional
public class MenuApplicationService {
    
    private final MenuRepository menuRepository;
    private final MenuDomainService menuDomainService;
    
    public MenuApplicationService(MenuRepository menuRepository, 
                                MenuDomainService menuDomainService) {
        this.menuRepository = menuRepository;
        this.menuDomainService = menuDomainService;
    }
    
    /**
     * 创建菜单
     * @param menuCode 菜单编码
     * @param menuName 菜单名称
     * @param menuTitle 菜单标题
     * @param parentMenuId 父菜单ID
     * @param operator 操作者
     * @param tenantId 租户ID
     * @return 菜单ID
     */
    public String createMenu(String menuCode, String menuName, String menuTitle, 
                           String parentMenuId, String operator, String tenantId) {
        // 创建菜单实体
        Menu menu = new Menu(menuCode, menuName, menuDomainService.getDefaultMenuType());
        menu.setMenuTitle(menuTitle);
        menu.setMenuId(UUID.randomUUID().toString());
        menu.setParentMenuId(parentMenuId);
        menu.setCreator(operator);
        menu.setCreateTime(LocalDateTime.now());
        menu.setTenantId(tenantId);
        
        // 设置菜单层级
        if (parentMenuId != null && !parentMenuId.isEmpty()) {
            Optional<Menu> parentMenuOpt = menuRepository.findById(parentMenuId);
            if (parentMenuOpt.isPresent()) {
                menu.setMenuLevel(parentMenuOpt.get().getMenuLevel() + 1);
            }
        } else {
            menu.setMenuLevel(1);
        }
        
        // 业务规则验证
        menuDomainService.validateForCreate(menu, tenantId);
        
        // 保存菜单
        Menu savedMenu = menuRepository.save(menu);
        
        return savedMenu.getMenuId();
    }
    
    /**
     * 更新菜单
     * @param menuId 菜单ID
     * @param menuName 菜单名称
     * @param menuTitle 菜单标题
     * @param operator 操作者
     */
    public void updateMenu(String menuId, String menuName, String menuTitle, String operator) {
        // 查找现有菜单
        Optional<Menu> existingMenuOpt = menuRepository.findById(menuId);
        if (existingMenuOpt.isEmpty()) {
            throw new IllegalArgumentException("菜单不存在: " + menuId);
        }
        
        Menu existingMenu = existingMenuOpt.get();
        
        // 更新菜单信息
        existingMenu.setMenuName(menuName);
        existingMenu.setMenuTitle(menuTitle);
        existingMenu.setUpdator(operator);
        existingMenu.setUpdateTime(LocalDateTime.now());
        
        // 业务规则验证
        menuDomainService.validateForUpdate(existingMenu);
        
        // 保存菜单
        menuRepository.save(existingMenu);
    }
    
    /**
     * 删除菜单
     * @param menuId 菜单ID
     */
    public void deleteMenu(String menuId) {
        // 查找现有菜单
        Optional<Menu> existingMenuOpt = menuRepository.findById(menuId);
        if (existingMenuOpt.isEmpty()) {
            throw new IllegalArgumentException("菜单不存在: " + menuId);
        }
        
        Menu existingMenu = existingMenuOpt.get();
        
        // 业务规则验证
        menuDomainService.validateForDelete(menuId);
        
        // 删除菜单
        menuRepository.deleteById(menuId);
    }
    
    /**
     * 移动菜单
     * @param menuId 菜单ID
     * @param newParentId 新父菜单ID
     * @param operator 操作者
     */
    public void moveMenu(String menuId, String newParentId, String operator) {
        // 查找现有菜单
        Optional<Menu> existingMenuOpt = menuRepository.findById(menuId);
        if (existingMenuOpt.isEmpty()) {
            throw new IllegalArgumentException("菜单不存在: " + menuId);
        }
        
        Menu existingMenu = existingMenuOpt.get();
        
        // 业务规则验证
        menuDomainService.validateForMove(menuId, newParentId);
        
        // 更新父菜单ID
        existingMenu.setParentMenuId(newParentId);
        existingMenu.setUpdator(operator);
        existingMenu.setUpdateTime(LocalDateTime.now());
        
        // 重新计算菜单层级
        if (newParentId != null && !newParentId.isEmpty()) {
            Optional<Menu> parentMenuOpt = menuRepository.findById(newParentId);
            if (parentMenuOpt.isPresent()) {
                existingMenu.setMenuLevel(parentMenuOpt.get().getMenuLevel() + 1);
            }
        } else {
            existingMenu.setMenuLevel(1);
        }
        
        // 保存菜单
        menuRepository.save(existingMenu);
    }
    
    /**
     * 启用菜单
     * @param menuId 菜单ID
     * @param operator 操作者
     */
    public void enableMenu(String menuId, String operator) {
        Optional<Menu> menuOpt = menuRepository.findById(menuId);
        if (menuOpt.isEmpty()) {
            throw new IllegalArgumentException("菜单不存在: " + menuId);
        }
        
        Menu menu = menuOpt.get();
        menu.enable();
        menu.setUpdator(operator);
        menu.setUpdateTime(LocalDateTime.now());
        
        menuRepository.save(menu);
    }
    
    /**
     * 停用菜单
     * @param menuId 菜单ID
     * @param operator 操作者
     */
    public void disableMenu(String menuId, String operator) {
        Optional<Menu> menuOpt = menuRepository.findById(menuId);
        if (menuOpt.isEmpty()) {
            throw new IllegalArgumentException("菜单不存在: " + menuId);
        }
        
        Menu menu = menuOpt.get();
        menu.disable();
        menu.setUpdator(operator);
        menu.setUpdateTime(LocalDateTime.now());
        
        menuRepository.save(menu);
    }
    
    /**
     * 根据ID获取菜单
     * @param menuId 菜单ID
     * @return 菜单对象
     */
    @Transactional(readOnly = true)
    public Menu getMenuById(String menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("菜单不存在: " + menuId));
    }
    
    /**
     * 根据菜单编码获取菜单
     * @param menuCode 菜单编码
     * @param tenantId 租户ID
     * @return 菜单对象
     */
    @Transactional(readOnly = true)
    public Menu getMenuByCode(String menuCode, String tenantId) {
        return menuRepository.findByMenuCode(menuCode, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("菜单不存在: " + menuCode));
    }
    
    /**
     * 获取菜单树
     * @param tenantId 租户ID
     * @return 菜单树列表
     */
    @Transactional(readOnly = true)
    public List<Menu> getMenuTree(String tenantId) {
        return menuRepository.getMenuTree(tenantId);
    }
    
    /**
     * 获取子菜单列表
     * @param parentMenuId 父菜单ID
     * @param tenantId 租户ID
     * @return 子菜单列表
     */
    @Transactional(readOnly = true)
    public List<Menu> getChildMenus(String parentMenuId, String tenantId) {
        return menuRepository.getChildMenus(parentMenuId, tenantId);
    }
    
    /**
     * 批量获取菜单
     * @param menuIds 菜单ID列表
     * @param tenantId 租户ID
     * @return 菜单列表
     */
    @Transactional(readOnly = true)
    public List<Menu> getMenusByIds(List<String> menuIds, String tenantId) {
        return menuRepository.findByIds(menuIds, tenantId);
    }
    
    
    /**
     * 批量删除菜单
     * @param menuIds 菜单ID列表
     */
    public void deleteMenus(List<String> menuIds) {
        for (String menuId : menuIds) {
            deleteMenu(menuId);
        }
    }
    
    /**
     * 检查菜单编码是否存在
     * @param menuCode 菜单编码
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Transactional(readOnly = true)
    public boolean isMenuCodeExists(String menuCode, String tenantId) {
        return menuRepository.findByMenuCode(menuCode, tenantId).isPresent();
    }
}