package com.jiuxi.module.auth.interfaces.web;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.core.core.annotation.Authorization;
import com.jiuxi.module.auth.app.dto.RoleCreateDTO;
import com.jiuxi.module.auth.app.dto.RoleResponseDTO;
import com.jiuxi.module.auth.app.dto.RoleUpdateDTO;
import com.jiuxi.module.auth.app.service.RoleApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 * 采用DDD架构的角色管理接口
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@RestController
@RequestMapping("/api/v1/roles")
@Authorization
public class RoleController {

    @Autowired
    private RoleApplicationService roleApplicationService;

    /**
     * 创建角色
     */
    @PostMapping
    public JsonResponse createRole(
            @RequestBody RoleCreateDTO createDTO,
            @RequestHeader("X-User-Person-Id") String operator,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        
        try {
            String roleId = roleApplicationService.createRole(
                createDTO.getRoleCode(),
                createDTO.getRoleName(),
                createDTO.getRoleDesc(),
                operator,
                tenantId
            );
            return JsonResponse.buildSuccess(roleId);
        } catch (Exception e) {
            return JsonResponse.buildFailure("角色创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新角色信息
     */
    @PutMapping("/{roleId}")
    public JsonResponse updateRole(
            @PathVariable String roleId,
            @RequestBody RoleUpdateDTO updateDTO,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            roleApplicationService.updateRole(
                roleId,
                updateDTO.getRoleName(),
                updateDTO.getRoleDesc(),
                operator
            );
            return JsonResponse.buildSuccess("更新成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("角色更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{roleId}")
    public JsonResponse deleteRole(@PathVariable String roleId) {
        try {
            roleApplicationService.deleteRole(roleId);
            return JsonResponse.buildSuccess("删除成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("删除角色失败: " + e.getMessage());
        }
    }

    /**
     * 启用角色
     */
    @PutMapping("/{roleId}/enable")
    public JsonResponse enableRole(
            @PathVariable String roleId,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            roleApplicationService.enableRole(roleId, operator);
            return JsonResponse.buildSuccess("启用成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("启用角色失败: " + e.getMessage());
        }
    }

    /**
     * 停用角色
     */
    @PutMapping("/{roleId}/disable")
    public JsonResponse disableRole(
            @PathVariable String roleId,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            roleApplicationService.disableRole(roleId, operator);
            return JsonResponse.buildSuccess("停用成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("停用角色失败: " + e.getMessage());
        }
    }

    /**
     * 为角色分配权限
     */
    @PostMapping("/{roleId}/permissions")
    public JsonResponse assignPermissionsToRole(
            @PathVariable String roleId,
            @RequestBody List<String> permissionIds) {
        
        try {
            // 这里应该调用权限应用服务来获取权限信息
            roleApplicationService.assignPermissionsToRole(roleId, permissionIds);
            return JsonResponse.buildSuccess("权限分配成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("权限分配失败: " + e.getMessage());
        }
    }

    /**
     * 为角色分配菜单
     */
    @PostMapping("/{roleId}/menus")
    public JsonResponse assignMenusToRole(
            @PathVariable String roleId,
            @RequestBody List<String> menuIds) {
        
        try {
            // 这里应该调用菜单应用服务来获取菜单信息
            roleApplicationService.assignMenusToRole(roleId, menuIds);
            return JsonResponse.buildSuccess("菜单分配成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("菜单分配失败: " + e.getMessage());
        }
    }
}