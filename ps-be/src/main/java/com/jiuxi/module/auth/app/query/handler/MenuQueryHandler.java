package com.jiuxi.module.auth.app.query.handler;

import com.jiuxi.module.auth.app.query.dto.MenuQueryDTO;
import com.jiuxi.module.auth.app.query.dto.MenuTreeQueryDTO;
import com.jiuxi.module.auth.domain.model.entity.Menu;
import com.jiuxi.module.auth.domain.repo.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单查询处理器
 * 处理所有菜单相关的查询操作（Read）
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component
@Transactional(readOnly = true)
public class MenuQueryHandler {
    
    @Autowired
    private MenuRepository menuRepository;
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 根据ID查询菜单
     * @param menuId 菜单ID
     * @return 菜单查询DTO
     */
    public MenuQueryDTO getMenuById(String menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("菜单不存在: " + menuId));
        
        return convertToQueryDTO(menu);
    }
    
    /**
     * 根据菜单编码查询菜单
     * @param menuCode 菜单编码
     * @param tenantId 租户ID
     * @return 菜单查询DTO
     */
    public MenuQueryDTO getMenuByCode(String menuCode, String tenantId) {
        Menu menu = menuRepository.findByMenuCode(menuCode, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("菜单不存在: " + menuCode));
        
        return convertToQueryDTO(menu);
    }
    
    /**
     * 获取菜单树
     * @param tenantId 租户ID
     * @return 菜单树查询DTO列表
     */
    public List<MenuTreeQueryDTO> getMenuTree(String tenantId) {
        List<Menu> menus = menuRepository.getMenuTree(tenantId);
        return menus.stream()
                .map(this::convertToTreeQueryDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取子菜单列表
     * @param parentMenuId 父菜单ID
     * @param tenantId 租户ID
     * @return 菜单查询DTO列表
     */
    public List<MenuQueryDTO> getChildMenus(String parentMenuId, String tenantId) {
        List<Menu> childMenus = menuRepository.getChildMenus(parentMenuId, tenantId);
        return childMenus.stream()
                .map(this::convertToQueryDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 批量获取菜单
     * @param menuIds 菜单ID列表
     * @param tenantId 租户ID
     * @return 菜单查询DTO列表
     */
    public List<MenuQueryDTO> getMenusByIds(List<String> menuIds, String tenantId) {
        List<Menu> menus = menuRepository.findByIds(menuIds, tenantId);
        return menus.stream()
                .map(this::convertToQueryDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 检查菜单编码是否存在
     * @param menuCode 菜单编码
     * @param tenantId 租户ID
     * @return 是否存在
     */
    public boolean isMenuCodeExists(String menuCode, String tenantId) {
        return menuRepository.findByMenuCode(menuCode, tenantId).isPresent();
    }
    
    /**
     * 将Menu实体转换为MenuQueryDTO
     * @param menu 菜单实体
     * @return 菜单查询DTO
     */
    private MenuQueryDTO convertToQueryDTO(Menu menu) {
        if (menu == null) {
            return null;
        }
        
        MenuQueryDTO dto = new MenuQueryDTO();
        dto.setMenuId(menu.getMenuId());
        dto.setMenuCode(menu.getMenuCode());
        dto.setMenuName(menu.getMenuName());
        dto.setMenuTitle(menu.getMenuTitle());
        dto.setParentMenuId(menu.getParentMenuId());
        dto.setMenuPath(menu.getMenuPath());
        dto.setMenuLevel(menu.getMenuLevel());
        dto.setMenuType(menu.getMenuType() != null ? menu.getMenuType().getCode() : null);
        dto.setMenuUri(menu.getMenuUri());
        dto.setMenuIcon(menu.getMenuIcon());
        dto.setComponent(menu.getComponent());
        dto.setStatus(menu.getStatus() != null ? menu.getStatus().getCode() : null);
        dto.setVisible(menu.getVisible());
        dto.setKeepAlive(menu.getKeepAlive());
        dto.setExternal(menu.getExternal());
        dto.setLeaf(menu.getLeaf());
        dto.setOrderIndex(menu.getOrderIndex());
        dto.setCreator(menu.getCreator());
        dto.setCreateTime(menu.getCreateTime() != null ? menu.getCreateTime().format(DATE_TIME_FORMATTER) : null);
        dto.setUpdator(menu.getUpdator());
        dto.setUpdateTime(menu.getUpdateTime() != null ? menu.getUpdateTime().format(DATE_TIME_FORMATTER) : null);
        dto.setTenantId(menu.getTenantId());
        
        return dto;
    }
    
    /**
     * 将Menu实体转换为MenuTreeQueryDTO
     * @param menu 菜单实体
     * @return 菜单树查询DTO
     */
    private MenuTreeQueryDTO convertToTreeQueryDTO(Menu menu) {
        if (menu == null) {
            return null;
        }
        
        MenuTreeQueryDTO dto = new MenuTreeQueryDTO();
        dto.setMenuId(menu.getMenuId());
        dto.setMenuCode(menu.getMenuCode());
        dto.setMenuName(menu.getMenuName());
        dto.setMenuTitle(menu.getMenuTitle());
        dto.setParentMenuId(menu.getParentMenuId());
        dto.setMenuPath(menu.getMenuPath());
        dto.setMenuLevel(menu.getMenuLevel());
        dto.setMenuType(menu.getMenuType() != null ? menu.getMenuType().getCode() : null);
        dto.setMenuUri(menu.getMenuUri());
        dto.setMenuIcon(menu.getMenuIcon());
        dto.setComponent(menu.getComponent());
        dto.setStatus(menu.getStatus() != null ? menu.getStatus().getCode() : null);
        dto.setVisible(menu.getVisible());
        dto.setKeepAlive(menu.getKeepAlive());
        dto.setExternal(menu.getExternal());
        dto.setLeaf(menu.getLeaf());
        dto.setOrderIndex(menu.getOrderIndex());
        dto.setCreator(menu.getCreator());
        dto.setCreateTime(menu.getCreateTime() != null ? menu.getCreateTime().format(DATE_TIME_FORMATTER) : null);
        dto.setUpdator(menu.getUpdator());
        dto.setUpdateTime(menu.getUpdateTime() != null ? menu.getUpdateTime().format(DATE_TIME_FORMATTER) : null);
        dto.setTenantId(menu.getTenantId());
        
        // 处理子菜单
        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            dto.setChildren(menu.getChildren().stream()
                    .map(this::convertToTreeQueryDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
}