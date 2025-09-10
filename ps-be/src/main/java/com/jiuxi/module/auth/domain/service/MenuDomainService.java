package com.jiuxi.module.auth.domain.service;

import com.jiuxi.module.auth.domain.entity.Menu;
import com.jiuxi.module.auth.domain.entity.MenuType;
import org.springframework.stereotype.Service;

/**
 * 菜单领域服务
 * 处理菜单相关的业务规则和复杂逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Service
public class MenuDomainService {
    
    /**
     * 获取默认菜单类型
     * @return 默认菜单类型
     */
    public MenuType getDefaultMenuType() {
        return MenuType.MENU;
    }
    
    /**
     * 验证菜单创建的业务规则
     * @param menu 菜单
     * @param tenantId 租户ID
     */
    public void validateForCreate(Menu menu, String tenantId) {
        // 验证菜单编码唯一性
        // 这里应该调用仓储接口检查菜单编码是否已存在
        // 为简化示例，暂时留空
        
        // 验证菜单名称长度
        if (menu.getMenuName() != null && menu.getMenuName().length() > 50) {
            throw new IllegalArgumentException("菜单名称长度不能超过50个字符");
        }
        
        // 验证菜单标题长度
        if (menu.getMenuTitle() != null && menu.getMenuTitle().length() > 100) {
            throw new IllegalArgumentException("菜单标题长度不能超过100个字符");
        }
    }
    
    /**
     * 验证菜单更新的业务规则
     * @param menu 菜单
     */
    public void validateForUpdate(Menu menu) {
        // 验证菜单名称长度
        if (menu.getMenuName() != null && menu.getMenuName().length() > 50) {
            throw new IllegalArgumentException("菜单名称长度不能超过50个字符");
        }
        
        // 验证菜单标题长度
        if (menu.getMenuTitle() != null && menu.getMenuTitle().length() > 100) {
            throw new IllegalArgumentException("菜单标题长度不能超过100个字符");
        }
    }
    
    /**
     * 验证菜单删除的业务规则
     * @param menuId 菜单ID
     */
    public void validateForDelete(String menuId) {
        // 验证菜单是否被角色引用
        // 这里应该调用角色服务检查是否有角色关联此菜单
        // 为简化示例，暂时留空
    }
    
    /**
     * 验证菜单移动的业务规则
     * @param menuId 菜单ID
     * @param newParentId 新父菜单ID
     */
    public void validateForMove(String menuId, String newParentId) {
        // 验证不能将菜单移动到自己或自己的子菜单下
        // 为简化示例，暂时留空
    }
}