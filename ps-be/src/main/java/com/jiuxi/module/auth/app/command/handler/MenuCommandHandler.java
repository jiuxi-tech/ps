package com.jiuxi.module.auth.app.command.handler;

import com.jiuxi.module.auth.app.command.dto.CreateMenuCommand;
import com.jiuxi.module.auth.app.command.dto.UpdateMenuCommand;
import com.jiuxi.module.auth.app.command.dto.DeleteMenuCommand;
import com.jiuxi.module.auth.app.command.dto.MoveMenuCommand;
import com.jiuxi.module.auth.domain.aggregate.MenuAggregate;
import com.jiuxi.module.auth.domain.model.entity.Menu;
import com.jiuxi.module.auth.domain.model.entity.MenuType;
import com.jiuxi.module.auth.domain.model.vo.MenuCode;
import com.jiuxi.module.auth.domain.model.vo.MenuPath;
import com.jiuxi.module.auth.domain.model.vo.TenantId;
import com.jiuxi.module.auth.domain.repo.MenuRepository;
import com.jiuxi.module.auth.domain.service.MenuDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * 菜单命令处理器
 * 处理所有菜单相关的变更操作（Create, Update, Delete）
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component
@Transactional
public class MenuCommandHandler {
    
    @Autowired
    private MenuRepository menuRepository;
    
    @Autowired
    private MenuDomainService menuDomainService;
    
    /**
     * 处理创建菜单命令
     * @param command 创建菜单命令
     * @return 菜单ID
     */
    public String handle(CreateMenuCommand command) {
        // 验证参数
        validateCreateCommand(command);
        
        // 确定菜单类型
        MenuType menuType = StringUtils.hasText(command.getMenuType()) 
            ? MenuType.fromCode(command.getMenuType()) 
            : menuDomainService.getDefaultMenuType();
        
        // 创建菜单聚合
        MenuCode menuCode = new MenuCode(command.getMenuCode());
        TenantId tenantId = new TenantId(command.getTenantId());
        
        MenuAggregate menuAggregate = MenuAggregate.create(
            menuCode, 
            command.getMenuName(), 
            menuType, 
            tenantId, 
            command.getOperator()
        );
        
        Menu menu = menuAggregate.getMenu();
        menu.setMenuId(UUID.randomUUID().toString());
        menu.setMenuTitle(command.getMenuTitle());
        menu.setParentMenuId(command.getParentMenuId());
        menu.setMenuUri(command.getMenuUri());
        menu.setMenuIcon(command.getMenuIcon());
        menu.setComponent(command.getComponent());
        menu.setVisible(command.getVisible());
        menu.setKeepAlive(command.getKeepAlive());
        menu.setExternal(command.getExternal());
        menu.setOrderIndex(command.getOrderIndex());
        
        // 设置菜单路径
        if (StringUtils.hasText(command.getMenuPath())) {
            MenuPath menuPath = new MenuPath(command.getMenuPath());
            menuAggregate.setMenuPath(menuPath, command.getOperator());
        }
        
        // 处理父菜单关系
        if (StringUtils.hasText(command.getParentMenuId())) {
            Optional<Menu> parentMenuOpt = menuRepository.findById(command.getParentMenuId());
            if (parentMenuOpt.isPresent()) {
                Menu parentMenu = parentMenuOpt.get();
                menu.setMenuLevel(parentMenu.getMenuLevel() + 1);
                
                // 添加到父菜单
                MenuAggregate parentAggregate = new MenuAggregate(parentMenu);
                parentAggregate.addChild(menuAggregate, command.getOperator());
                menuRepository.save(parentMenu);
            } else {
                throw new IllegalArgumentException("父菜单不存在: " + command.getParentMenuId());
            }
        } else {
            menu.setMenuLevel(1);
        }
        
        // 业务规则验证
        menuDomainService.validateForCreate(menu, command.getTenantId());
        
        // 保存菜单
        Menu savedMenu = menuRepository.save(menu);
        
        return savedMenu.getMenuId();
    }
    
    /**
     * 处理更新菜单命令
     * @param command 更新菜单命令
     */
    public void handle(UpdateMenuCommand command) {
        // 验证参数
        validateUpdateCommand(command);
        
        // 查找现有菜单
        Menu existingMenu = menuRepository.findById(command.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("菜单不存在: " + command.getMenuId()));
        
        MenuAggregate menuAggregate = new MenuAggregate(existingMenu);
        
        // 更新菜单信息
        existingMenu.setMenuName(command.getMenuName());
        existingMenu.setMenuTitle(command.getMenuTitle());
        existingMenu.setMenuUri(command.getMenuUri());
        existingMenu.setMenuIcon(command.getMenuIcon());
        existingMenu.setComponent(command.getComponent());
        existingMenu.setUpdator(command.getOperator());
        existingMenu.setUpdateTime(LocalDateTime.now());
        
        // 处理可选字段更新
        if (command.getVisible() != null) {
            if (command.getVisible()) {
                existingMenu.show();
            } else {
                existingMenu.hide();
            }
        }
        
        if (command.getKeepAlive() != null) {
            if (command.getKeepAlive()) {
                existingMenu.enableKeepAlive();
            } else {
                existingMenu.disableKeepAlive();
            }
        }
        
        if (command.getExternal() != null && command.getExternal()) {
            existingMenu.setAsExternal();
        }
        
        if (command.getOrderIndex() != null) {
            existingMenu.setOrderIndex(command.getOrderIndex());
        }
        
        // 设置菜单路径
        if (StringUtils.hasText(command.getMenuPath())) {
            MenuPath menuPath = new MenuPath(command.getMenuPath());
            menuAggregate.setMenuPath(menuPath, command.getOperator());
        }
        
        // 业务规则验证
        menuDomainService.validateForUpdate(existingMenu);
        
        // 保存菜单
        menuRepository.save(existingMenu);
    }
    
    /**
     * 处理删除菜单命令
     * @param command 删除菜单命令
     */
    public void handle(DeleteMenuCommand command) {
        // 验证参数
        validateDeleteCommand(command);
        
        // 查找现有菜单
        Menu existingMenu = menuRepository.findById(command.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("菜单不存在: " + command.getMenuId()));
        
        // 业务规则验证
        menuDomainService.validateForDelete(command.getMenuId());
        
        // 删除菜单
        menuRepository.deleteById(command.getMenuId());
    }
    
    /**
     * 处理移动菜单命令
     * @param command 移动菜单命令
     */
    public void handle(MoveMenuCommand command) {
        // 验证参数
        validateMoveCommand(command);
        
        // 查找现有菜单
        Menu existingMenu = menuRepository.findById(command.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("菜单不存在: " + command.getMenuId()));
        
        MenuAggregate menuAggregate = new MenuAggregate(existingMenu);
        
        // 查找新父菜单
        MenuAggregate newParentAggregate = null;
        if (StringUtils.hasText(command.getNewParentId())) {
            Menu newParentMenu = menuRepository.findById(command.getNewParentId())
                    .orElseThrow(() -> new IllegalArgumentException("新父菜单不存在: " + command.getNewParentId()));
            newParentAggregate = new MenuAggregate(newParentMenu);
        }
        
        // 移动菜单
        menuAggregate.moveToParent(newParentAggregate, command.getOperator());
        
        // 保存菜单
        menuRepository.save(existingMenu);
        if (newParentAggregate != null) {
            menuRepository.save(newParentAggregate.getMenu());
        }
    }
    
    // 验证方法
    private void validateCreateCommand(CreateMenuCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("创建菜单命令不能为空");
        }
        // 可以添加更多的业务验证逻辑
    }
    
    private void validateUpdateCommand(UpdateMenuCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("更新菜单命令不能为空");
        }
        // 可以添加更多的业务验证逻辑
    }
    
    private void validateDeleteCommand(DeleteMenuCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("删除菜单命令不能为空");
        }
        // 可以添加更多的业务验证逻辑
    }
    
    private void validateMoveCommand(MoveMenuCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("移动菜单命令不能为空");
        }
        
        if (command.getMenuId().equals(command.getNewParentId())) {
            throw new IllegalArgumentException("菜单不能移动到自己下面");
        }
        // 可以添加更多的业务验证逻辑
    }
}