package com.jiuxi.module.user.intf.web.controller;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import com.jiuxi.module.user.app.service.UserApplicationService;
import com.jiuxi.module.user.intf.web.dto.ChangePasswordRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户账户管理控制器
 * 处理用户账户相关操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@RestController
@RequestMapping("/api/v1/user-accounts")
@Authorization
@Slf4j
public class UserAccountController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "personId";

    @Autowired
    private UserApplicationService userApplicationService;

    /**
     * 重置用户密码
     */
    @PutMapping("/{personId}/reset-password")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse resetPassword(
            @PathVariable String personId,
            @RequestParam String newPassword,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            log.info("Resetting password for user: personId={}, operator={}", personId, operator);
            userApplicationService.resetPassword(personId, newPassword, operator);
            log.info("Password reset successfully for user: personId={}", personId);
            return JsonResponse.buildSuccess("密码重置成功");
        } catch (IllegalArgumentException e) {
            log.warn("Password reset validation failed: {}", e.getMessage());
            return JsonResponse.buildFailure("密码重置失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("Password reset error: {}", e.getMessage(), e);
            return JsonResponse.buildFailure("密码重置失败: " + e.getMessage());
        }
    }

    /**
     * 修改用户密码（当前登录用户）
     */
    @PutMapping("/change-password")
    @IgnoreAuthorization
    public JsonResponse changePassword(
            @Validated @RequestBody ChangePasswordRequest request,
            @RequestHeader("X-User-Person-Id") String personId) {
        
        try {
            // 验证新密码和确认密码是否一致
            if (!request.isPasswordMatch()) {
                return JsonResponse.buildFailure("新密码和确认密码不一致");
            }
            
            log.info("User changing password: personId={}", personId);
            // 这里需要验证旧密码的逻辑，目前简化处理，直接重置
            // TODO: 添加旧密码验证逻辑
            userApplicationService.resetPassword(personId, request.getNewPassword(), personId);
            log.info("Password changed successfully: personId={}", personId);
            return JsonResponse.buildSuccess("密码修改成功");
        } catch (IllegalArgumentException e) {
            log.warn("Password change validation failed: {}", e.getMessage());
            return JsonResponse.buildFailure("密码修改失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("Password change error: {}", e.getMessage(), e);
            return JsonResponse.buildFailure("密码修改失败: " + e.getMessage());
        }
    }

    /**
     * 锁定/解锁账户
     */
    @PutMapping("/{personId}/lock")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse lockAccount(
            @PathVariable String personId,
            @RequestParam Boolean locked,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            if (locked) {
                userApplicationService.deactivateUser(personId, operator);
                return JsonResponse.buildSuccess("账户已锁定");
            } else {
                userApplicationService.activateUser(personId, operator);
                return JsonResponse.buildSuccess("账户已解锁");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("账户状态修改失败: " + e.getMessage());
        }
    }

    /**
     * 启用/禁用账户
     */
    @PutMapping("/{personId}/enable")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse enableAccount(
            @PathVariable String personId,
            @RequestParam Boolean enabled,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            if (enabled) {
                userApplicationService.activateUser(personId, operator);
                return JsonResponse.buildSuccess("账户已启用");
            } else {
                userApplicationService.deactivateUser(personId, operator);
                return JsonResponse.buildSuccess("账户已禁用");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("账户状态修改失败: " + e.getMessage());
        }
    }
}