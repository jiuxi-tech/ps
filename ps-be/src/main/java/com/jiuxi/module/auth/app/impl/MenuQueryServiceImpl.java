package com.jiuxi.module.auth.app.impl;

import com.jiuxi.module.auth.app.query.dto.MenuQueryDTO;
import com.jiuxi.module.auth.app.query.dto.MenuTreeQueryDTO;
import com.jiuxi.module.auth.app.query.handler.MenuQueryHandler;
import com.jiuxi.module.auth.app.service.MenuQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单查询服务实现
 * 实现菜单相关的查询操作
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Service
@Transactional(readOnly = true)
public class MenuQueryServiceImpl implements MenuQueryService {
    
    @Autowired
    private MenuQueryHandler menuQueryHandler;
    
    @Override
    public MenuQueryDTO getMenuById(String menuId) {
        return menuQueryHandler.getMenuById(menuId);
    }
    
    @Override
    public MenuQueryDTO getMenuByCode(String menuCode, String tenantId) {
        return menuQueryHandler.getMenuByCode(menuCode, tenantId);
    }
    
    @Override
    public List<MenuTreeQueryDTO> getMenuTree(String tenantId) {
        return menuQueryHandler.getMenuTree(tenantId);
    }
    
    @Override
    public List<MenuQueryDTO> getChildMenus(String parentMenuId, String tenantId) {
        return menuQueryHandler.getChildMenus(parentMenuId, tenantId);
    }
    
    @Override
    public List<MenuQueryDTO> getMenusByIds(List<String> menuIds, String tenantId) {
        return menuQueryHandler.getMenusByIds(menuIds, tenantId);
    }
    
    @Override
    public boolean isMenuCodeExists(String menuCode, String tenantId) {
        return menuQueryHandler.isMenuCodeExists(menuCode, tenantId);
    }
}