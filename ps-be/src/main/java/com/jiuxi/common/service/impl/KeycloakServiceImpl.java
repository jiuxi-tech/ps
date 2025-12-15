package com.jiuxi.common.service.impl;

import com.jiuxi.common.service.KeycloakService;
import com.jiuxi.common.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * @ClassName: KeycloakServiceImpl
 * @Description: Keycloak集成服务实现类
 * @Author: Qdd
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
@Service
public class KeycloakServiceImpl implements KeycloakService {
    
    private static final Logger logger = LoggerFactory.getLogger(KeycloakServiceImpl.class);
    
    @Value("${keycloak.server-url:http://192.168.0.139:18080}")
    private String keycloakServerUrl;
    
    @Value("${keycloak.realm:ps-realm}")
    private String realm;
    
    @Value("${keycloak.client-id:ps-be}")
    private String clientId;
    
    @Value("${keycloak.client-secret:}")
    private String clientSecret;
    
    @Value("${keycloak.admin-username:admin}")
    private String adminUsername;
    
    @Value("${keycloak.admin-password:admin123}")
    private String adminPassword;
    
    private String adminAccessToken;
    private long tokenExpiryTime;
    
    @Override
    public Map<String, Object> authenticate(String username, String password) {
        try {
            String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "password");
            formData.add("client_id", clientId);
            if (clientSecret != null && !clientSecret.isEmpty()) {
                formData.add("client_secret", clientSecret);
            }
            formData.add("username", username);
            formData.add("password", password);
            
            String response = HttpClientUtil.postForm(tokenUrl, formData);
            return HttpClientUtil.fromJson(response, Map.class);
        } catch (Exception e) {
            logger.error("用户认证失败: {}", username, e);
            return null;
        }
    }
    
    @Override
    public Map<String, Object> refreshToken(String refreshToken) {
        try {
            String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "refresh_token");
            formData.add("client_id", clientId);
            if (clientSecret != null && !clientSecret.isEmpty()) {
                formData.add("client_secret", clientSecret);
            }
            formData.add("refresh_token", refreshToken);
            
            String response = HttpClientUtil.postForm(tokenUrl, formData);
            return HttpClientUtil.fromJson(response, Map.class);
        } catch (Exception e) {
            logger.error("刷新Token失败", e);
            return null;
        }
    }
    
    @Override
    public boolean logout(String refreshToken) {
        try {
            String logoutUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/logout";
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", clientId);
            if (clientSecret != null && !clientSecret.isEmpty()) {
                formData.add("client_secret", clientSecret);
            }
            formData.add("refresh_token", refreshToken);
            
            HttpClientUtil.postForm(logoutUrl, formData);
            return true;
        } catch (Exception e) {
            logger.error("注销失败", e);
            return false;
        }
    }
    
    @Override
    public boolean validateToken(String accessToken) {
        try {
            String userInfoUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo";
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(accessToken);
            HttpClientUtil.get(userInfoUrl, headers);
            return true;
        } catch (Exception e) {
            logger.debug("Token验证失败", e);
            return false;
        }
    }
    
    @Override
    public Map<String, Object> getUserInfo(String accessToken) {
        try {
            String userInfoUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo";
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(accessToken);
            String response = HttpClientUtil.get(userInfoUrl, headers);
            return HttpClientUtil.fromJson(response, Map.class);
        } catch (Exception e) {
            logger.error("获取用户信息失败", e);
            return null;
        }
    }
    
    @Override
    public boolean createUser(Map<String, Object> userInfo) {
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return false;
            }
            
            String createUserUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users";
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(adminToken);
            
            HttpClientUtil.post(createUserUrl, userInfo, headers);
            return true;
        } catch (Exception e) {
            logger.error("创建用户失败", e);
            return false;
        }
    }
    
    @Override
    public boolean updateUser(String userId, Map<String, Object> userInfo) {
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return false;
            }
            
            String updateUserUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users/" + userId;
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(adminToken);
            
            HttpClientUtil.put(updateUserUrl, userInfo, headers);
            return true;
        } catch (Exception e) {
            logger.error("更新用户失败: {}", userId, e);
            return false;
        }
    }
    
    @Override
    public boolean deleteUser(String userId) {
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return false;
            }
            
            String deleteUserUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users/" + userId;
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(adminToken);
            
            HttpClientUtil.delete(deleteUserUrl, headers);
            return true;
        } catch (Exception e) {
            logger.error("删除用户失败: {}", userId, e);
            return false;
        }
    }
    
    @Override
    public boolean setUserEnabled(String userId, boolean enabled) {
        try {
            Map<String, Object> userUpdate = new HashMap<>();
            userUpdate.put("enabled", enabled);
            return updateUser(userId, userUpdate);
        } catch (Exception e) {
            logger.error("设置用户状态失败: {}", userId, e);
            return false;
        }
    }
    
    @Override
    public boolean resetUserPassword(String userId, String newPassword, boolean temporary) {
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return false;
            }
            
            String resetPasswordUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users/" + userId + "/reset-password";
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(adminToken);
            
            Map<String, Object> passwordData = new HashMap<>();
            passwordData.put("type", "password");
            passwordData.put("value", newPassword);
            passwordData.put("temporary", temporary);
            
            HttpClientUtil.put(resetPasswordUrl, passwordData, headers);
            return true;
        } catch (Exception e) {
            logger.error("重置用户密码失败: {}", userId, e);
            return false;
        }
    }
    
    @Override
    public List<Map<String, Object>> getUsers(String search, Integer first, Integer max) {
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return Collections.emptyList();
            }
            
            Map<String, Object> params = new HashMap<>();
            if (search != null && !search.isEmpty()) {
                params.put("search", search);
            }
            if (first != null) {
                params.put("first", first);
            }
            if (max != null) {
                params.put("max", max);
            }
            
            String getUsersUrl = HttpClientUtil.buildUrlWithParams(
                keycloakServerUrl + "/admin/realms/" + realm + "/users", params);
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(adminToken);
            
            String response = HttpClientUtil.get(getUsersUrl, headers);
            return HttpClientUtil.fromJson(response, List.class);
        } catch (Exception e) {
            logger.error("获取用户列表失败", e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public Map<String, Object> getUserByUsername(String username) {
        try {
            List<Map<String, Object>> users = getUsers(username, 0, 1);
            return users.isEmpty() ? null : users.get(0);
        } catch (Exception e) {
            logger.error("根据用户名查找用户失败: {}", username, e);
            return null;
        }
    }
    
    @Override
    public Map<String, Object> getUserByEmail(String email) {
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return null;
            }
            
            Map<String, Object> params = new HashMap<>();
            params.put("email", email);
            params.put("exact", true);
            
            String getUsersUrl = HttpClientUtil.buildUrlWithParams(
                keycloakServerUrl + "/admin/realms/" + realm + "/users", params);
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(adminToken);
            
            String response = HttpClientUtil.get(getUsersUrl, headers);
            List<Map<String, Object>> users = HttpClientUtil.fromJson(response, List.class);
            return users.isEmpty() ? null : users.get(0);
        } catch (Exception e) {
            logger.error("根据邮箱查找用户失败: {}", email, e);
            return null;
        }
    }
    
    @Override
    public List<Map<String, Object>> getUserRoles(String userId) {
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return Collections.emptyList();
            }
            
            String getUserRolesUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(adminToken);
            
            String response = HttpClientUtil.get(getUserRolesUrl, headers);
            return HttpClientUtil.fromJson(response, List.class);
        } catch (Exception e) {
            logger.error("获取用户角色失败: {}", userId, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public boolean assignRolesToUser(String userId, List<String> roleNames) {
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return false;
            }
            
            // 获取角色信息
            List<Map<String, Object>> roles = new ArrayList<>();
            for (String roleName : roleNames) {
                Map<String, Object> role = getRoleByName(roleName);
                if (role != null) {
                    roles.add(role);
                }
            }
            
            if (roles.isEmpty()) {
                return false;
            }
            
            String assignRolesUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(adminToken);
            
            HttpClientUtil.post(assignRolesUrl, roles, headers);
            return true;
        } catch (Exception e) {
            logger.error("分配角色失败: {}", userId, e);
            return false;
        }
    }
    
    @Override
    public boolean removeRolesFromUser(String userId, List<String> roleNames) {
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return false;
            }
            
            // 获取角色信息
            List<Map<String, Object>> roles = new ArrayList<>();
            for (String roleName : roleNames) {
                Map<String, Object> role = getRoleByName(roleName);
                if (role != null) {
                    roles.add(role);
                }
            }
            
            if (roles.isEmpty()) {
                return false;
            }
            
            String removeRolesUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(adminToken);
            
            HttpClientUtil.delete(removeRolesUrl, headers);
            return true;
        } catch (Exception e) {
            logger.error("移除角色失败: {}", userId, e);
            return false;
        }
    }
    
    @Override
    public List<Map<String, Object>> getAllRoles() {
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return Collections.emptyList();
            }
            
            String getRolesUrl = keycloakServerUrl + "/admin/realms/" + realm + "/roles";
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(adminToken);
            
            String response = HttpClientUtil.get(getRolesUrl, headers);
            return HttpClientUtil.fromJson(response, List.class);
        } catch (Exception e) {
            logger.error("获取所有角色失败", e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public boolean createRole(String roleName, String description) {
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return false;
            }
            
            String createRoleUrl = keycloakServerUrl + "/admin/realms/" + realm + "/roles";
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(adminToken);
            
            Map<String, Object> roleData = new HashMap<>();
            roleData.put("name", roleName);
            roleData.put("description", description);
            
            HttpClientUtil.post(createRoleUrl, roleData, headers);
            return true;
        } catch (Exception e) {
            logger.error("创建角色失败: {}", roleName, e);
            return false;
        }
    }
    
    @Override
    public boolean deleteRole(String roleName) {
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return false;
            }
            
            String deleteRoleUrl = keycloakServerUrl + "/admin/realms/" + realm + "/roles/" + roleName;
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(adminToken);
            
            HttpClientUtil.delete(deleteRoleUrl, headers);
            return true;
        } catch (Exception e) {
            logger.error("删除角色失败: {}", roleName, e);
            return false;
        }
    }
    
    // 其他方法的实现...
    @Override
    public List<Map<String, Object>> getGroups() {
        // 实现获取用户组列表
        return Collections.emptyList();
    }
    
    @Override
    public boolean createGroup(String groupName, Map<String, Object> attributes) {
        // 实现创建用户组
        return false;
    }
    
    @Override
    public boolean deleteGroup(String groupId) {
        // 实现删除用户组
        return false;
    }
    
    @Override
    public boolean addUserToGroup(String userId, String groupId) {
        // 实现将用户加入组
        return false;
    }
    
    @Override
    public boolean removeUserFromGroup(String userId, String groupId) {
        // 实现将用户从组中移除
        return false;
    }
    
    @Override
    public List<Map<String, Object>> getUserSessions(String userId) {
        // 实现获取用户会话
        return Collections.emptyList();
    }
    
    @Override
    public boolean logoutSession(String sessionId) {
        // 实现注销用户会话
        return false;
    }
    
    @Override
    public boolean logoutAllUserSessions(String userId) {
        // 实现注销用户所有会话
        return false;
    }
    
    @Override
    public List<Map<String, Object>> getClients() {
        // 实现获取客户端列表
        return Collections.emptyList();
    }
    
    @Override
    public Map<String, Object> getClient(String clientId) {
        // 实现获取客户端详情
        return null;
    }
    
    @Override
    public boolean createClient(Map<String, Object> clientInfo) {
        // 实现创建客户端
        return false;
    }
    
    @Override
    public boolean updateClient(String clientId, Map<String, Object> clientInfo) {
        // 实现更新客户端
        return false;
    }
    
    @Override
    public boolean deleteClient(String clientId) {
        // 实现删除客户端
        return false;
    }
    
    @Override
    public Map<String, Object> getRealmInfo() {
        // 实现获取Realm信息
        return null;
    }
    
    @Override
    public boolean updateRealmConfig(Map<String, Object> realmConfig) {
        // 实现更新Realm配置
        return false;
    }
    
    @Override
    public List<Map<String, Object>> getIdentityProviders() {
        // 实现获取身份提供者列表
        return Collections.emptyList();
    }
    
    @Override
    public boolean createIdentityProvider(Map<String, Object> providerInfo) {
        // 实现创建身份提供者
        return false;
    }
    
    @Override
    public boolean deleteIdentityProvider(String providerId) {
        // 实现删除身份提供者
        return false;
    }
    
    /**
     * 获取管理员访问令牌
     */
    private String getAdminAccessToken() {
        try {
            // 检查token是否过期
            if (adminAccessToken != null && System.currentTimeMillis() < tokenExpiryTime) {
                return adminAccessToken;
            }
            
            String tokenUrl = keycloakServerUrl + "/realms/master/protocol/openid-connect/token";
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "password");
            formData.add("client_id", "admin-cli");
            formData.add("username", adminUsername);
            formData.add("password", adminPassword);
            
            String response = HttpClientUtil.postForm(tokenUrl, formData);
            Map<String, Object> tokenData = HttpClientUtil.fromJson(response, Map.class);
            
            adminAccessToken = (String) tokenData.get("access_token");
            Integer expiresIn = (Integer) tokenData.get("expires_in");
            tokenExpiryTime = System.currentTimeMillis() + (expiresIn * 1000L) - 60000; // 提前1分钟过期
            
            return adminAccessToken;
        } catch (Exception e) {
            logger.error("获取管理员访问令牌失败", e);
            return null;
        }
    }
    
    /**
     * 根据角色名获取角色信息
     */
    private Map<String, Object> getRoleByName(String roleName) {
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return null;
            }
            
            String getRoleUrl = keycloakServerUrl + "/admin/realms/" + realm + "/roles/" + roleName;
            HttpHeaders headers = HttpClientUtil.createBearerTokenHeaders(adminToken);
            
            String response = HttpClientUtil.get(getRoleUrl, headers);
            return HttpClientUtil.fromJson(response, Map.class);
        } catch (Exception e) {
            logger.error("获取角色信息失败: {}", roleName, e);
            return null;
        }
    }
}