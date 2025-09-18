package com.jiuxi.module.auth.intf.web.controller.command;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.module.auth.app.dto.PermissionCreateDTO;
import com.jiuxi.module.auth.app.dto.PermissionUpdateDTO;
import com.jiuxi.module.auth.app.service.PermissionApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 权限命令控制器
 * 采用CQRS架构的权限管理命令接口
 * 
 * @author DDD Refactor
 * @date 2025-09-19
 */
@RestController("authPermissionCommandController")
@RequestMapping("/api/v1/auth/permissions")
@Authorization
public class PermissionCommandController {

    @Autowired
    private PermissionApplicationService permissionApplicationService;

    /**
     * 创建权限
     */
    @PostMapping
    public JsonResponse createPermission(
            @RequestBody PermissionCreateDTO createDTO,
            @RequestHeader("X-User-Person-Id") String operator,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        
        try {
            String permissionId = permissionApplicationService.createPermission(
                createDTO.getPermissionCode(),
                createDTO.getPermissionName(),
                createDTO.getPermissionDesc(),
                operator,
                tenantId
            );
            return JsonResponse.buildSuccess(permissionId);
        } catch (Exception e) {
            return JsonResponse.buildFailure("权限创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新权限信息
     */
    @PutMapping("/{permissionId}")
    public JsonResponse updatePermission(
            @PathVariable String permissionId,
            @RequestBody PermissionUpdateDTO updateDTO,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            permissionApplicationService.updatePermission(
                permissionId,
                updateDTO.getPermissionName(),
                updateDTO.getPermissionDesc(),
                operator
            );
            return JsonResponse.buildSuccess("更新成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("权限更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{permissionId}")
    public JsonResponse deletePermission(@PathVariable String permissionId) {
        try {
            permissionApplicationService.deletePermission(permissionId);
            return JsonResponse.buildSuccess("删除成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("删除权限失败: " + e.getMessage());
        }
    }

    /**
     * 启用权限
     */
    @PutMapping("/{permissionId}/enable")
    public JsonResponse enablePermission(
            @PathVariable String permissionId,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            permissionApplicationService.enablePermission(permissionId, operator);
            return JsonResponse.buildSuccess("启用成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("启用权限失败: " + e.getMessage());
        }
    }

    /**
     * 停用权限
     */
    @PutMapping("/{permissionId}/disable")
    public JsonResponse disablePermission(
            @PathVariable String permissionId,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            permissionApplicationService.disablePermission(permissionId, operator);
            return JsonResponse.buildSuccess("停用成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("停用权限失败: " + e.getMessage());
        }
    }
}