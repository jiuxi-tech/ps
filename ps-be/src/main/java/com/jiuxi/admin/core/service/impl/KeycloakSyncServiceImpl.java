package com.jiuxi.admin.core.service.impl;

import com.jiuxi.admin.core.bean.entity.TpKeycloakAccount;
import com.jiuxi.admin.core.service.KeycloakSyncService;
import com.jiuxi.admin.core.service.TpKeycloakAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Keycloak同步服务实现类
 *
 * @author System
 * @since 2025-01-21
 */
@Slf4j
@Service
public class KeycloakSyncServiceImpl implements KeycloakSyncService {

    @Autowired
    private TpKeycloakAccountService tpKeycloakAccountService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${keycloak.server-url:http://localhost:8080}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm:ps-realm}")
    private String keycloakRealm;

    @Value("${keycloak.admin.client-id:admin-cli}")
    private String adminClientId;

    @Value("${keycloak.admin.username:admin}")
    private String adminUsername;

    @Value("${keycloak.admin.password:admin123}")
    private String adminPassword;

    @Override
    public KeycloakSyncResult syncAccountToKeycloak(String accountId, String username, String password, String creator) {
        try {
            log.info("开始同步账号到Keycloak: accountId={}, username={}", accountId, username);

            // 1. 获取管理员访问令牌
            String adminToken = getAdminAccessToken();
            if (!StringUtils.hasText(adminToken)) {
                return KeycloakSyncResult.failure("获取Keycloak管理员令牌失败");
            }

            // 2. 检查用户是否已存在
            String existingUserId = findUserByUsername(adminToken, username);
            if (StringUtils.hasText(existingUserId)) {
                log.warn("用户已存在于Keycloak中: username={}, userId={}", username, existingUserId);
                // 更新本地Keycloak账号记录
                boolean localResult = tpKeycloakAccountService.createOrUpdateKeycloakAccount(accountId, username, password, creator);
                if (localResult) {
                    // 更新Keycloak用户ID
                    TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
                    if (keycloakAccount != null) {
                        keycloakAccount.setKcUserId(existingUserId);
                        tpKeycloakAccountService.updateById(keycloakAccount);
                    }
                    return KeycloakSyncResult.success("用户已存在，本地记录已更新", existingUserId);
                } else {
                    return KeycloakSyncResult.failure("更新本地Keycloak账号记录失败");
                }
            }

            // 3. 创建新用户
            String keycloakUserId = createKeycloakUser(adminToken, username, password);
            if (!StringUtils.hasText(keycloakUserId)) {
                return KeycloakSyncResult.failure("在Keycloak中创建用户失败");
            }

            // 4. 保存本地Keycloak账号记录
            boolean localResult = tpKeycloakAccountService.createOrUpdateKeycloakAccount(accountId, username, password, creator);
            if (!localResult) {
                log.error("本地Keycloak账号记录保存失败，但Keycloak用户已创建: userId={}", keycloakUserId);
                return KeycloakSyncResult.failure("本地记录保存失败，但Keycloak用户已创建");
            }

            // 5. 更新Keycloak用户ID
            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount != null) {
                keycloakAccount.setKcUserId(keycloakUserId);
                tpKeycloakAccountService.updateById(keycloakAccount);
            }

            log.info("账号同步到Keycloak成功: accountId={}, username={}, keycloakUserId={}", accountId, username, keycloakUserId);
            return KeycloakSyncResult.success("账号同步成功", keycloakUserId);

        } catch (Exception e) {
            log.error("同步账号到Keycloak失败: accountId={}, username={}, error={}", accountId, username, e.getMessage(), e);
            return KeycloakSyncResult.failure("同步失败: " + e.getMessage(), e);
        }
    }

    @Override
    public KeycloakSyncResult updateKeycloakUser(String accountId, String username, String password, String updater) {
        try {
            log.info("开始更新Keycloak用户: accountId={}, username={}", accountId, username);

            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount == null || !StringUtils.hasText(keycloakAccount.getKcUserId())) {
                return KeycloakSyncResult.failure("未找到对应的Keycloak账号记录");
            }

            String adminToken = getAdminAccessToken();
            if (!StringUtils.hasText(adminToken)) {
                return KeycloakSyncResult.failure("获取Keycloak管理员令牌失败");
            }

            boolean updateResult = updateKeycloakUserInfo(adminToken, keycloakAccount.getKcUserId(), username, password);
            if (!updateResult) {
                return KeycloakSyncResult.failure("更新Keycloak用户信息失败");
            }

            // 更新本地记录
            boolean localResult = tpKeycloakAccountService.createOrUpdateKeycloakAccount(accountId, username, password, updater);
            if (!localResult) {
                log.warn("Keycloak用户更新成功，但本地记录更新失败: accountId={}", accountId);
            }

            log.info("Keycloak用户更新成功: accountId={}, username={}", accountId, username);
            return KeycloakSyncResult.success("用户更新成功", keycloakAccount.getKcUserId());

        } catch (Exception e) {
            log.error("更新Keycloak用户失败: accountId={}, username={}, error={}", accountId, username, e.getMessage(), e);
            return KeycloakSyncResult.failure("更新失败: " + e.getMessage(), e);
        }
    }

    @Override
    public KeycloakSyncResult disableKeycloakUser(String accountId) {
        try {
            log.info("开始禁用Keycloak用户: accountId={}", accountId);

            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount == null || !StringUtils.hasText(keycloakAccount.getKcUserId())) {
                return KeycloakSyncResult.failure("未找到对应的Keycloak账号记录");
            }

            String adminToken = getAdminAccessToken();
            if (!StringUtils.hasText(adminToken)) {
                return KeycloakSyncResult.failure("获取Keycloak管理员令牌失败");
            }

            boolean disableResult = setKeycloakUserEnabled(adminToken, keycloakAccount.getKcUserId(), false);
            if (!disableResult) {
                return KeycloakSyncResult.failure("禁用Keycloak用户失败");
            }

            // 更新本地记录
            tpKeycloakAccountService.disableKeycloakAccount(accountId);

            log.info("Keycloak用户禁用成功: accountId={}", accountId);
            return KeycloakSyncResult.success("用户禁用成功", keycloakAccount.getKcUserId());

        } catch (Exception e) {
            log.error("禁用Keycloak用户失败: accountId={}, error={}", accountId, e.getMessage(), e);
            return KeycloakSyncResult.failure("禁用失败: " + e.getMessage(), e);
        }
    }

    @Override
    public KeycloakSyncResult enableKeycloakUser(String accountId) {
        try {
            log.info("开始启用Keycloak用户: accountId={}", accountId);

            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount == null || !StringUtils.hasText(keycloakAccount.getKcUserId())) {
                return KeycloakSyncResult.failure("未找到对应的Keycloak账号记录");
            }

            String adminToken = getAdminAccessToken();
            if (!StringUtils.hasText(adminToken)) {
                return KeycloakSyncResult.failure("获取Keycloak管理员令牌失败");
            }

            boolean enableResult = setKeycloakUserEnabled(adminToken, keycloakAccount.getKcUserId(), true);
            if (!enableResult) {
                return KeycloakSyncResult.failure("启用Keycloak用户失败");
            }

            // 更新本地记录
            tpKeycloakAccountService.enableKeycloakAccount(accountId);

            log.info("Keycloak用户启用成功: accountId={}", accountId);
            return KeycloakSyncResult.success("用户启用成功", keycloakAccount.getKcUserId());

        } catch (Exception e) {
            log.error("启用Keycloak用户失败: accountId={}, error={}", accountId, e.getMessage(), e);
            return KeycloakSyncResult.failure("启用失败: " + e.getMessage(), e);
        }
    }

    @Override
    public KeycloakSyncResult deleteKeycloakUser(String accountId) {
        try {
            log.info("开始删除Keycloak用户: accountId={}", accountId);

            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount == null || !StringUtils.hasText(keycloakAccount.getKcUserId())) {
                return KeycloakSyncResult.failure("未找到对应的Keycloak账号记录");
            }

            String adminToken = getAdminAccessToken();
            if (!StringUtils.hasText(adminToken)) {
                return KeycloakSyncResult.failure("获取Keycloak管理员令牌失败");
            }

            boolean deleteResult = deleteKeycloakUserById(adminToken, keycloakAccount.getKcUserId());
            if (!deleteResult) {
                return KeycloakSyncResult.failure("删除Keycloak用户失败");
            }

            // 删除本地记录
            tpKeycloakAccountService.removeById(keycloakAccount.getId());

            log.info("Keycloak用户删除成功: accountId={}", accountId);
            return KeycloakSyncResult.success("用户删除成功", keycloakAccount.getKcUserId());

        } catch (Exception e) {
            log.error("删除Keycloak用户失败: accountId={}, error={}", accountId, e.getMessage(), e);
            return KeycloakSyncResult.failure("删除失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取管理员访问令牌
     */
    private String getAdminAccessToken() {
        try {
            String tokenUrl = keycloakServerUrl + "/realms/master/protocol/openid-connect/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "password");
            params.add("client_id", adminClientId);
            params.add("username", adminUsername);
            params.add("password", adminPassword);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("access_token");
            }
        } catch (Exception e) {
            log.error("获取Keycloak管理员令牌失败: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据用户名查找用户
     */
    private String findUserByUsername(String adminToken, String username) {
        try {
            String usersUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users?username=" + username;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(usersUrl, HttpMethod.GET, request, List.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && !response.getBody().isEmpty()) {
                Map<String, Object> user = (Map<String, Object>) response.getBody().get(0);
                return (String) user.get("id");
            }
        } catch (Exception e) {
            log.error("查找Keycloak用户失败: username={}, error={}", username, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 创建Keycloak用户
     */
    private String createKeycloakUser(String adminToken, String username, String password) {
        try {
            String usersUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> userRepresentation = new HashMap<>();
            userRepresentation.put("username", username);
            userRepresentation.put("enabled", true);
            userRepresentation.put("emailVerified", true);

            // 设置密码
            Map<String, Object> credential = new HashMap<>();
            credential.put("type", "password");
            credential.put("value", password);
            credential.put("temporary", false);
            userRepresentation.put("credentials", Arrays.asList(credential));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(userRepresentation, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(usersUrl, request, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                // 从Location头获取用户ID
                String location = response.getHeaders().getFirst("Location");
                if (StringUtils.hasText(location)) {
                    return location.substring(location.lastIndexOf('/') + 1);
                }
            }
        } catch (Exception e) {
            log.error("创建Keycloak用户失败: username={}, error={}", username, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 更新Keycloak用户信息
     */
    private boolean updateKeycloakUserInfo(String adminToken, String userId, String username, String password) {
        try {
            String userUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users/" + userId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> userRepresentation = new HashMap<>();
            userRepresentation.put("username", username);
            userRepresentation.put("enabled", true);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(userRepresentation, headers);
            ResponseEntity<String> response = restTemplate.exchange(userUrl, HttpMethod.PUT, request, String.class);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                // 如果提供了新密码，则重置密码
                if (StringUtils.hasText(password)) {
                    return resetKeycloakUserPassword(adminToken, userId, password);
                }
                return true;
            }
        } catch (Exception e) {
            log.error("更新Keycloak用户信息失败: userId={}, username={}, error={}", userId, username, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 重置Keycloak用户密码
     */
    private boolean resetKeycloakUserPassword(String adminToken, String userId, String password) {
        try {
            String passwordUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users/" + userId + "/reset-password";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> credential = new HashMap<>();
            credential.put("type", "password");
            credential.put("value", password);
            credential.put("temporary", false);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(credential, headers);
            ResponseEntity<String> response = restTemplate.exchange(passwordUrl, HttpMethod.PUT, request, String.class);

            return response.getStatusCode() == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            log.error("重置Keycloak用户密码失败: userId={}, error={}", userId, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 设置Keycloak用户启用/禁用状态
     */
    private boolean setKeycloakUserEnabled(String adminToken, String userId, boolean enabled) {
        try {
            String userUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users/" + userId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> userRepresentation = new HashMap<>();
            userRepresentation.put("enabled", enabled);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(userRepresentation, headers);
            ResponseEntity<String> response = restTemplate.exchange(userUrl, HttpMethod.PUT, request, String.class);

            return response.getStatusCode() == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            log.error("设置Keycloak用户状态失败: userId={}, enabled={}, error={}", userId, enabled, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 删除Keycloak用户
     */
    private boolean deleteKeycloakUserById(String adminToken, String userId) {
        try {
            String userUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users/" + userId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(userUrl, HttpMethod.DELETE, request, String.class);

            return response.getStatusCode() == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            log.error("删除Keycloak用户失败: userId={}, error={}", userId, e.getMessage(), e);
        }
        return false;
    }
}