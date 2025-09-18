package com.jiuxi.module.auth.app.assembler;

import com.jiuxi.module.auth.domain.model.entity.Menu;
import com.jiuxi.module.auth.app.dto.MenuResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单装配器
 * 负责Menu实体和MenuResponseDTO之间的转换
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Component("menuAppAssembler")
public class MenuAssembler {
    
    /**
     * 将Menu实体转换为MenuResponseDTO
     * @param menu 菜单实体
     * @return 菜单响应DTO
     */
    public MenuResponseDTO toResponseDTO(Menu menu) {
        if (menu == null) {
            return null;
        }
        
        MenuResponseDTO dto = new MenuResponseDTO();
        dto.setMenuId(menu.getMenuId());
        dto.setMenuCode(menu.getMenuCode());
        dto.setMenuName(menu.getMenuName());
        dto.setMenuTitle(menu.getMenuTitle());
        dto.setParentMenuId(menu.getParentMenuId());
        dto.setMenuPath(menu.getMenuPath());
        dto.setMenuLevel(menu.getMenuLevel());
        dto.setMenuType(menu.getMenuType());
        dto.setMenuUri(menu.getMenuUri());
        dto.setMenuIcon(menu.getMenuIcon());
        dto.setComponent(menu.getComponent());
        dto.setStatus(menu.getStatus());
        dto.setVisible(menu.isVisible());
        dto.setKeepAlive(menu.needKeepAlive());
        dto.setExternal(menu.isExternal());
        dto.setLeaf(menu.isLeaf());
        dto.setOrderIndex(menu.getOrderIndex());
        dto.setCreator(menu.getCreator());
        dto.setCreateTime(menu.getCreateTime());
        dto.setUpdator(menu.getUpdator());
        dto.setUpdateTime(menu.getUpdateTime());
        dto.setTenantId(menu.getTenantId());
        
        // 转换子菜单
        if (menu.getChildren() != null) {
            List<MenuResponseDTO> childrenDTOs = menu.getChildren().stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            dto.setChildren(childrenDTOs);
        }
        
        return dto;
    }
}