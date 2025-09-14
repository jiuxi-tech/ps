package com.jiuxi.module.user.intf.web.controller.command;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import com.jiuxi.module.user.app.command.handler.UserCommandHandler;
import com.jiuxi.module.user.app.command.dto.UserCreateCommand;
import com.jiuxi.module.user.app.command.dto.UserUpdateCommand;
import com.jiuxi.module.user.app.command.dto.UserDeleteCommand;
import com.jiuxi.module.user.app.command.dto.UserActivateCommand;
import com.jiuxi.module.user.app.command.dto.UserDeactivateCommand;
import com.jiuxi.module.user.domain.model.vo.UserCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户命令控制器
 * 处理用户相关的命令操作（创建、更新、删除、激活、停用等）
 */
@RestController
@RequestMapping("/api/v1/users")
@Authorization
@Slf4j
public class UserCommandController {

    @Autowired
    private UserCommandHandler userCommandHandler;

    /**
     * 创建用户
     */
    @PostMapping
    public JsonResponse createUser(
            @Validated(AddGroup.class) @RequestBody UserCreateCommand command,
            @RequestHeader("X-User-Person-Id") String operator,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        
        try {
            log.info("Creating user: username={}, operator={}, tenantId={}", 
                    command.getUsername(), operator, tenantId);
            
            command.setOperator(operator);
            command.setTenantId(tenantId);
            
            String personId = userCommandHandler.handleCreateUser(command);
            log.info("User created successfully: personId={}", personId);
            return JsonResponse.buildSuccess(personId);
        } catch (IllegalArgumentException e) {
            log.warn("User creation validation failed: {}", e.getMessage());
            return JsonResponse.buildFailure("用户创建失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("User creation error: {}", e.getMessage(), e);
            return JsonResponse.buildFailure("用户创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{personId}")
    @Authorization(businessKey = "personId")
    public JsonResponse updateUser(
            @PathVariable String personId,
            @Validated(UpdateGroup.class) @RequestBody UserUpdateCommand command,
            @RequestHeader("X-User-Person-Id") String operator,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        
        try {
            command.setPersonId(personId);
            command.setOperator(operator);
            command.setTenantId(tenantId);
            
            userCommandHandler.handleUpdateUser(command);
            return JsonResponse.buildSuccess("更新成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("用户更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{personId}")
    @Authorization(businessKey = "personId")
    public JsonResponse deleteUser(
            @PathVariable String personId,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            UserDeleteCommand command = new UserDeleteCommand();
            command.setPersonId(personId);
            command.setOperator(operator);
            
            userCommandHandler.handleDeleteUser(command);
            return JsonResponse.buildSuccess("删除成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("删除用户失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    public JsonResponse deleteUsers(
            @RequestBody java.util.List<String> personIds,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            for (String personId : personIds) {
                UserDeleteCommand command = new UserDeleteCommand();
                command.setPersonId(personId);
                command.setOperator(operator);
                
                userCommandHandler.handleDeleteUser(command);
            }
            return JsonResponse.buildSuccess("批量删除成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 激活用户
     */
    @PutMapping("/{personId}/activate")
    @Authorization(businessKey = "personId")
    public JsonResponse activateUser(
            @PathVariable String personId,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            UserActivateCommand command = new UserActivateCommand();
            command.setPersonId(personId);
            command.setOperator(operator);
            
            userCommandHandler.handleActivateUser(command);
            return JsonResponse.buildSuccess("激活成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("激活用户失败: " + e.getMessage());
        }
    }

    /**
     * 停用用户
     */
    @PutMapping("/{personId}/deactivate")
    @Authorization(businessKey = "personId")
    public JsonResponse deactivateUser(
            @PathVariable String personId,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            UserDeactivateCommand command = new UserDeactivateCommand();
            command.setPersonId(personId);
            command.setOperator(operator);
            
            userCommandHandler.handleDeactivateUser(command);
            return JsonResponse.buildSuccess("停用成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("停用用户失败: " + e.getMessage());
        }
    }
}