package com.jiuxi.module.integration.app.service.impl;

import com.jiuxi.admin.core.service.KeycloakSyncService;
import com.jiuxi.module.integration.app.service.KeycloakIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Keycloak集成应用服务实现类（适配器模式）
 * @author DDD重构
 * @date 2025-09-12
 */
@Service
public class KeycloakIntegrationServiceImpl implements KeycloakIntegrationService {

    @Autowired
    private KeycloakSyncService keycloakSyncService;

    @Override
    public KeycloakSyncService.KeycloakSyncResult syncAccountToKeycloak(String accountId, String username, String password, String creator) {
        return keycloakSyncService.syncAccountToKeycloak(accountId, username, password, creator);
    }

    @Override
    public KeycloakSyncService.KeycloakSyncResult updateKeycloakUser(String accountId, String username, String password, String updater) {
        return keycloakSyncService.updateKeycloakUser(accountId, username, password, updater);
    }

    @Override
    public KeycloakSyncService.KeycloakSyncResult disableKeycloakUser(String accountId) {
        return keycloakSyncService.disableKeycloakUser(accountId);
    }

    @Override
    public KeycloakSyncService.KeycloakSyncResult enableKeycloakUser(String accountId) {
        return keycloakSyncService.enableKeycloakUser(accountId);
    }

    @Override
    public KeycloakSyncService.KeycloakSyncResult deleteKeycloakUser(String accountId) {
        return keycloakSyncService.deleteKeycloakUser(accountId);
    }
}