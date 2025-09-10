package com.jiuxi.admin.core.controller;

import com.jiuxi.admin.core.service.KeycloakSyncService;
import com.jiuxi.common.bean.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Keycloak测试控制器
 *
 * @author System
 * @since 2025-01-21
 */
@RestController
@RequestMapping("/test")
public class KeycloakTestController {

    @Autowired(required = false)
    private KeycloakSyncService keycloakSyncService;

    /**
     * 测试同步账号到Keycloak
     *
     * @param accountId 账号ID
     * @param username 用户名
     * @param password 密码
     * @param creator 创建人ID
     * @return JsonResponse
     */
    @RequestMapping("/test_sync_account_to_keycloak")
    public JsonResponse testSyncAccountToKeycloak(
            @RequestParam String accountId,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(defaultValue = "system") String creator) {
        
        if (keycloakSyncService == null) {
            return JsonResponse.buildFailure("Keycloak同步服务未启用");
        }
        
        try {
            KeycloakSyncService.KeycloakSyncResult result = keycloakSyncService.syncAccountToKeycloak(
                    accountId, username, password, creator);
            
            if (result.isSuccess()) {
                return JsonResponse.buildSuccess(result);
            } else {
                return JsonResponse.buildFailure(result.getMessage());
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("同步失败: " + e.getMessage());
        }
    }

    /**
     * 测试更新Keycloak用户
     *
     * @param accountId 账号ID
     * @param username 用户名
     * @param password 新密码
     * @param updater 更新人ID
     * @return JsonResponse
     */
    @RequestMapping("/test_update_keycloak_user")
    public JsonResponse testUpdateKeycloakUser(
            @RequestParam String accountId,
            @RequestParam String username,
            @RequestParam(required = false) String password,
            @RequestParam(defaultValue = "system") String updater) {
        
        if (keycloakSyncService == null) {
            return JsonResponse.buildFailure("Keycloak同步服务未启用");
        }
        
        try {
            KeycloakSyncService.KeycloakSyncResult result = keycloakSyncService.updateKeycloakUser(
                    accountId, username, password, updater);
            
            if (result.isSuccess()) {
                return JsonResponse.buildSuccess(result);
            } else {
                return JsonResponse.buildFailure(result.getMessage());
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("更新失败: " + e.getMessage());
        }
    }

    /**
     * 测试禁用Keycloak用户
     *
     * @param accountId 账号ID
     * @return JsonResponse
     */
    @RequestMapping("/test_disable_keycloak_user")
    public JsonResponse testDisableKeycloakUser(@RequestParam String accountId) {
        
        if (keycloakSyncService == null) {
            return JsonResponse.buildFailure("Keycloak同步服务未启用");
        }
        
        try {
            KeycloakSyncService.KeycloakSyncResult result = keycloakSyncService.disableKeycloakUser(accountId);
            
            if (result.isSuccess()) {
                return JsonResponse.buildSuccess(result);
            } else {
                return JsonResponse.buildFailure(result.getMessage());
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("禁用失败: " + e.getMessage());
        }
    }

    /**
     * 测试启用Keycloak用户
     *
     * @param accountId 账号ID
     * @return JsonResponse
     */
    @RequestMapping("/test_enable_keycloak_user")
    public JsonResponse testEnableKeycloakUser(@RequestParam String accountId) {
        
        if (keycloakSyncService == null) {
            return JsonResponse.buildFailure("Keycloak同步服务未启用");
        }
        
        try {
            KeycloakSyncService.KeycloakSyncResult result = keycloakSyncService.enableKeycloakUser(accountId);
            
            if (result.isSuccess()) {
                return JsonResponse.buildSuccess(result);
            } else {
                return JsonResponse.buildFailure(result.getMessage());
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("启用失败: " + e.getMessage());
        }
    }

    /**
     * 测试删除Keycloak用户
     *
     * @param accountId 账号ID
     * @return JsonResponse
     */
    @RequestMapping("/test_delete_keycloak_user")
    public JsonResponse testDeleteKeycloakUser(@RequestParam String accountId) {
        
        if (keycloakSyncService == null) {
            return JsonResponse.buildFailure("Keycloak同步服务未启用");
        }
        
        try {
            KeycloakSyncService.KeycloakSyncResult result = keycloakSyncService.deleteKeycloakUser(accountId);
            
            if (result.isSuccess()) {
                return JsonResponse.buildSuccess(result);
            } else {
                return JsonResponse.buildFailure(result.getMessage());
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("删除失败: " + e.getMessage());
        }
    }
}