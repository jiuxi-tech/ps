package com.jiuxi.module.integration.app.service;

import com.jiuxi.admin.core.service.KeycloakSyncService;

/**
 * Keycloak集成应用服务接口
 * @author DDD重构
 * @date 2025-09-12
 */
public interface KeycloakIntegrationService {

    /**
     * 同步账号到Keycloak
     *
     * @param accountId 账号ID
     * @param username 用户名
     * @param password 明文密码
     * @param creator 创建人ID
     * @return 同步结果
     */
    KeycloakSyncService.KeycloakSyncResult syncAccountToKeycloak(String accountId, String username, String password, String creator);

    /**
     * 更新Keycloak中的用户信息
     *
     * @param accountId 账号ID
     * @param username 用户名
     * @param password 新密码（可选）
     * @param updater 更新人ID
     * @return 同步结果
     */
    KeycloakSyncService.KeycloakSyncResult updateKeycloakUser(String accountId, String username, String password, String updater);

    /**
     * 禁用Keycloak中的用户
     *
     * @param accountId 账号ID
     * @return 同步结果
     */
    KeycloakSyncService.KeycloakSyncResult disableKeycloakUser(String accountId);

    /**
     * 启用Keycloak中的用户
     *
     * @param accountId 账号ID
     * @return 同步结果
     */
    KeycloakSyncService.KeycloakSyncResult enableKeycloakUser(String accountId);

    /**
     * 删除Keycloak中的用户
     *
     * @param accountId 账号ID
     * @return 同步结果
     */
    KeycloakSyncService.KeycloakSyncResult deleteKeycloakUser(String accountId);
}