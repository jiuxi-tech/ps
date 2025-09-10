package com.jiuxi.module.auth.interfaces.web;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.core.core.annotation.Authorization;
import com.jiuxi.module.auth.app.dto.MenuCreateDTO;
import com.jiuxi.module.auth.app.dto.MenuUpdateDTO;
import com.jiuxi.module.auth.app.service.MenuApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜单管理控制器
 * 采用DDD架构的菜单管理接口
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@RestController
@RequestMapping("/api/v1/menus")
@Authorization
public class MenuController {

    @Autowired
    private MenuApplicationService menuApplicationService;

    /**
     * 创建菜单
     */
    @PostMapping
    public JsonResponse createMenu(
            @RequestBody MenuCreateDTO createDTO,
            @RequestHeader("X-User-Person-Id") String operator,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        
        try {
            String menuId = menuApplicationService.createMenu(
                createDTO.getMenuCode(),
                createDTO.getMenuName(),
                createDTO.getMenuTitle(),
                createDTO.getParentMenuId(),
                operator,
                tenantId
            );
            return JsonResponse.buildSuccess(menuId);
        } catch (Exception e) {
            return JsonResponse.buildFailure("菜单创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新菜单信息
     */
    @PutMapping("/{menuId}")
    public JsonResponse updateMenu(
            @PathVariable String menuId,
            @RequestBody MenuUpdateDTO updateDTO,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            menuApplicationService.updateMenu(
                menuId,
                updateDTO.getMenuName(),
                updateDTO.getMenuTitle(),
                operator
            );
            return JsonResponse.buildSuccess("更新成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("菜单更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuId}")
    public JsonResponse deleteMenu(@PathVariable String menuId) {
        try {
            menuApplicationService.deleteMenu(menuId);
            return JsonResponse.buildSuccess("删除成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("删除菜单失败: " + e.getMessage());
        }
    }

    /**
     * 移动菜单
     */
    @PutMapping("/{menuId}/move")
    public JsonResponse moveMenu(
            @PathVariable String menuId,
            @RequestParam String newParentId,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            menuApplicationService.moveMenu(menuId, newParentId, operator);
            return JsonResponse.buildSuccess("移动成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("移动菜单失败: " + e.getMessage());
        }
    }

    /**
     * 启用菜单
     */
    @PutMapping("/{menuId}/enable")
    public JsonResponse enableMenu(
            @PathVariable String menuId,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            menuApplicationService.enableMenu(menuId, operator);
            return JsonResponse.buildSuccess("启用成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("启用菜单失败: " + e.getMessage());
        }
    }

    /**
     * 停用菜单
     */
    @PutMapping("/{menuId}/disable")
    public JsonResponse disableMenu(
            @PathVariable String menuId,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            menuApplicationService.disableMenu(menuId, operator);
            return JsonResponse.buildSuccess("停用成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("停用菜单失败: " + e.getMessage());
        }
    }
}