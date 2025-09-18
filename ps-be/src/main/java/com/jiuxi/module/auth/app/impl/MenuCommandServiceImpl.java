package com.jiuxi.module.auth.app.impl;

import com.jiuxi.module.auth.app.command.dto.CreateMenuCommand;
import com.jiuxi.module.auth.app.command.dto.UpdateMenuCommand;
import com.jiuxi.module.auth.app.command.dto.DeleteMenuCommand;
import com.jiuxi.module.auth.app.command.dto.MoveMenuCommand;
import com.jiuxi.module.auth.app.command.handler.MenuCommandHandler;
import com.jiuxi.module.auth.app.service.MenuCommandService;
import com.jiuxi.module.auth.domain.model.entity.Menu;
import com.jiuxi.module.auth.domain.repo.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 菜单命令服务实现
 * 实现菜单相关的变更操作
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Service
@Transactional
public class MenuCommandServiceImpl implements MenuCommandService {
    
    @Autowired
    private MenuCommandHandler menuCommandHandler;
    
    @Autowired
    private MenuRepository menuRepository;
    
    @Override
    public String createMenu(CreateMenuCommand command) {
        return menuCommandHandler.handle(command);
    }
    
    @Override
    public void updateMenu(UpdateMenuCommand command) {
        menuCommandHandler.handle(command);
    }
    
    @Override
    public void deleteMenu(DeleteMenuCommand command) {
        menuCommandHandler.handle(command);
    }
    
    @Override
    public void moveMenu(MoveMenuCommand command) {
        menuCommandHandler.handle(command);
    }
    
    @Override
    public void enableMenu(String menuId, String operator) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("菜单不存在: " + menuId));
        
        menu.enable();
        menu.setUpdator(operator);
        menu.setUpdateTime(LocalDateTime.now());
        
        menuRepository.save(menu);
    }
    
    @Override
    public void disableMenu(String menuId, String operator) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("菜单不存在: " + menuId));
        
        menu.disable();
        menu.setUpdator(operator);
        menu.setUpdateTime(LocalDateTime.now());
        
        menuRepository.save(menu);
    }
}