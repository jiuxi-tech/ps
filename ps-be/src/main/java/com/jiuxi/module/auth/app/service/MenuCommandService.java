package com.jiuxi.module.auth.app.service;

import com.jiuxi.module.auth.app.command.dto.CreateMenuCommand;
import com.jiuxi.module.auth.app.command.dto.UpdateMenuCommand;
import com.jiuxi.module.auth.app.command.dto.DeleteMenuCommand;
import com.jiuxi.module.auth.app.command.dto.MoveMenuCommand;

/**
 * 菜单命令服务接口
 * 定义菜单相关的变更操作接口
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public interface MenuCommandService {
    
    /**
     * 创建菜单
     * @param command 创建菜单命令
     * @return 菜单ID
     */
    String createMenu(CreateMenuCommand command);
    
    /**
     * 更新菜单
     * @param command 更新菜单命令
     */
    void updateMenu(UpdateMenuCommand command);
    
    /**
     * 删除菜单
     * @param command 删除菜单命令
     */
    void deleteMenu(DeleteMenuCommand command);
    
    /**
     * 移动菜单
     * @param command 移动菜单命令
     */
    void moveMenu(MoveMenuCommand command);
    
    /**
     * 启用菜单
     * @param menuId 菜单ID
     * @param operator 操作者
     */
    void enableMenu(String menuId, String operator);
    
    /**
     * 停用菜单
     * @param menuId 菜单ID
     * @param operator 操作者
     */
    void disableMenu(String menuId, String operator);
}