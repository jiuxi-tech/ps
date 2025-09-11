package com.jiuxi.shared.security.config;

import com.jiuxi.shared.security.config.SecurityProperties.Keycloak;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
// Retry functionality removed to maintain compatibility
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 统一Keycloak客户端
 * 负责与Keycloak服务器的所有交互，提供连接池管理、重试机制和性能优化
 * 
 * @author Security Refactoring
 * @since Phase 4.2.4
 */
@Component
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class KeycloakClient {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakClient.class);

    @Autowired
    private SecurityProperties securityProperties;

    private RestTemplate restTemplate;
    private String adminAccessToken;
    private long tokenExpiryTime;
    
    // 连接池统计
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong successfulRequests = new AtomicLong(0);
    private final AtomicLong failedRequests = new AtomicLong(0);
    private final AtomicLong retryCount = new AtomicLong(0);

    @PostConstruct
    public void init() {
        // 初始化RestTemplate连接池
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        
        Keycloak keycloakConfig = securityProperties.getKeycloak();
        
        // 配置连接池参数
        factory.setConnectTimeout(keycloakConfig.getConnectionTimeout());
        factory.setReadTimeout(keycloakConfig.getReadTimeout());
        factory.setConnectionRequestTimeout(keycloakConfig.getConnectionRequestTimeout());
        
        // 创建RestTemplate实例
        this.restTemplate = new RestTemplate(factory);
        
        logger.info("KeycloakClient initialized with connection pool settings: " +
            "connectTimeout={}, readTimeout={}, connectionRequestTimeout={}", 
            keycloakConfig.getConnectionTimeout(), 
            keycloakConfig.getReadTimeout(), 
            keycloakConfig.getConnectionRequestTimeout());
    }

    /**
     * 用户认证
     */
    public AuthenticationResult authenticate(String username, String password) {
        totalRequests.incrementAndGet();
        
        try {
            Keycloak keycloakConfig = securityProperties.getKeycloak();
            String tokenUrl = keycloakConfig.getServerUrl() + "/realms/" + keycloakConfig.getRealm() + "/protocol/openid-connect/token";
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "password");
            formData.add("client_id", keycloakConfig.getClientId());
            if (StringUtils.hasText(keycloakConfig.getClientSecret())) {
                formData.add("client_secret", keycloakConfig.getClientSecret());
            }
            formData.add("username", username);
            formData.add("password", password);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                successfulRequests.incrementAndGet();
                return AuthenticationResult.success(response.getBody());
            } else {
                failedRequests.incrementAndGet();
                return AuthenticationResult.failure("Authentication failed: Invalid response");
            }
            
        } catch (HttpClientErrorException e) {
            failedRequests.incrementAndGet();
            logger.debug("Authentication failed for user: {}, status: {}, body: {}", 
                username, e.getStatusCode(), e.getResponseBodyAsString());
            return AuthenticationResult.failure("Authentication failed: " + e.getMessage());
            
        } catch (Exception e) {
            failedRequests.incrementAndGet();
            logger.error("Authentication error for user: {}", username, e);
            return AuthenticationResult.failure("Authentication error: " + e.getMessage());
        }
    }

    /**
     * 刷新令牌
     */
    public AuthenticationResult refreshToken(String refreshToken) {
        totalRequests.incrementAndGet();
        
        try {
            Keycloak keycloakConfig = securityProperties.getKeycloak();
            String tokenUrl = keycloakConfig.getServerUrl() + "/realms/" + keycloakConfig.getRealm() + "/protocol/openid-connect/token";
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "refresh_token");
            formData.add("client_id", keycloakConfig.getClientId());
            if (StringUtils.hasText(keycloakConfig.getClientSecret())) {
                formData.add("client_secret", keycloakConfig.getClientSecret());
            }
            formData.add("refresh_token", refreshToken);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                successfulRequests.incrementAndGet();
                return AuthenticationResult.success(response.getBody());
            } else {
                failedRequests.incrementAndGet();
                return AuthenticationResult.failure("Token refresh failed: Invalid response");
            }
            
        } catch (HttpClientErrorException e) {
            failedRequests.incrementAndGet();
            logger.debug("Token refresh failed, status: {}, body: {}", 
                e.getStatusCode(), e.getResponseBodyAsString());
            return AuthenticationResult.failure("Token refresh failed: " + e.getMessage());
            
        } catch (Exception e) {
            failedRequests.incrementAndGet();
            logger.error("Token refresh error", e);
            return AuthenticationResult.failure("Token refresh error: " + e.getMessage());
        }
    }

    /**
     * 用户注销
     */
    public boolean logout(String refreshToken) {
        totalRequests.incrementAndGet();
        
        try {
            Keycloak keycloakConfig = securityProperties.getKeycloak();
            String logoutUrl = keycloakConfig.getServerUrl() + "/realms/" + keycloakConfig.getRealm() + "/protocol/openid-connect/logout";
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", keycloakConfig.getClientId());
            if (StringUtils.hasText(keycloakConfig.getClientSecret())) {
                formData.add("client_secret", keycloakConfig.getClientSecret());
            }
            formData.add("refresh_token", refreshToken);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(logoutUrl, request, String.class);
            
            boolean success = response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK;
            if (success) {
                successfulRequests.incrementAndGet();
            } else {
                failedRequests.incrementAndGet();
            }
            return success;
            
        } catch (Exception e) {
            failedRequests.incrementAndGet();
            logger.error("Logout error", e);
            return false;
        }
    }

    /**
     * 验证令牌
     */
    public boolean validateToken(String accessToken) {
        totalRequests.incrementAndGet();
        
        try {
            Keycloak keycloakConfig = securityProperties.getKeycloak();
            String userInfoUrl = keycloakConfig.getServerUrl() + "/realms/" + keycloakConfig.getRealm() + "/protocol/openid-connect/userinfo";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);
            
            boolean valid = response.getStatusCode() == HttpStatus.OK;
            if (valid) {
                successfulRequests.incrementAndGet();
            } else {
                failedRequests.incrementAndGet();
            }
            return valid;
            
        } catch (Exception e) {
            failedRequests.incrementAndGet();
            logger.debug("Token validation failed", e);
            return false;
        }
    }

    /**
     * 获取用户信息
     */
    public UserInfo getUserInfo(String accessToken) {
        totalRequests.incrementAndGet();
        
        try {
            Keycloak keycloakConfig = securityProperties.getKeycloak();
            String userInfoUrl = keycloakConfig.getServerUrl() + "/realms/" + keycloakConfig.getRealm() + "/protocol/openid-connect/userinfo";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                successfulRequests.incrementAndGet();
                return UserInfo.fromKeycloakResponse(response.getBody());
            } else {
                failedRequests.incrementAndGet();
                return null;
            }
            
        } catch (Exception e) {
            failedRequests.incrementAndGet();
            logger.error("Get user info error", e);
            return null;
        }
    }

    /**
     * 创建用户（管理员API）
     */
    public AdminApiResult createUser(UserCreationRequest userRequest) {
        totalRequests.incrementAndGet();
        
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                failedRequests.incrementAndGet();
                return AdminApiResult.failure("Failed to obtain admin token");
            }
            
            Keycloak keycloakConfig = securityProperties.getKeycloak();
            String createUserUrl = keycloakConfig.getServerUrl() + "/admin/realms/" + keycloakConfig.getRealm() + "/users";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> userRepresentation = buildUserRepresentation(userRequest);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(userRepresentation, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(createUserUrl, request, String.class);
            
            if (response.getStatusCode() == HttpStatus.CREATED) {
                successfulRequests.incrementAndGet();
                String location = response.getHeaders().getFirst("Location");
                String userId = location != null ? location.substring(location.lastIndexOf('/') + 1) : null;
                return AdminApiResult.success("User created successfully", userId);
            } else {
                failedRequests.incrementAndGet();
                return AdminApiResult.failure("User creation failed");
            }
            
        } catch (Exception e) {
            failedRequests.incrementAndGet();
            logger.error("Create user error", e);
            return AdminApiResult.failure("User creation error: " + e.getMessage());
        }
    }

    /**
     * 更新用户（管理员API）
     */
    public AdminApiResult updateUser(String userId, UserUpdateRequest userRequest) {
        totalRequests.incrementAndGet();
        
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                failedRequests.incrementAndGet();
                return AdminApiResult.failure("Failed to obtain admin token");
            }
            
            Keycloak keycloakConfig = securityProperties.getKeycloak();
            String updateUserUrl = keycloakConfig.getServerUrl() + "/admin/realms/" + keycloakConfig.getRealm() + "/users/" + userId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> userRepresentation = buildUserUpdateRepresentation(userRequest);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(userRepresentation, headers);
            ResponseEntity<String> response = restTemplate.exchange(updateUserUrl, HttpMethod.PUT, request, String.class);
            
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                successfulRequests.incrementAndGet();
                return AdminApiResult.success("User updated successfully", userId);
            } else {
                failedRequests.incrementAndGet();
                return AdminApiResult.failure("User update failed");
            }
            
        } catch (Exception e) {
            failedRequests.incrementAndGet();
            logger.error("Update user error: userId={}", userId, e);
            return AdminApiResult.failure("User update error: " + e.getMessage());
        }
    }

    /**
     * 删除用户（管理员API）
     */
    public AdminApiResult deleteUser(String userId) {
        totalRequests.incrementAndGet();
        
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                failedRequests.incrementAndGet();
                return AdminApiResult.failure("Failed to obtain admin token");
            }
            
            Keycloak keycloakConfig = securityProperties.getKeycloak();
            String deleteUserUrl = keycloakConfig.getServerUrl() + "/admin/realms/" + keycloakConfig.getRealm() + "/users/" + userId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(deleteUserUrl, HttpMethod.DELETE, request, String.class);
            
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                successfulRequests.incrementAndGet();
                return AdminApiResult.success("User deleted successfully", userId);
            } else {
                failedRequests.incrementAndGet();
                return AdminApiResult.failure("User deletion failed");
            }
            
        } catch (Exception e) {
            failedRequests.incrementAndGet();
            logger.error("Delete user error: userId={}", userId, e);
            return AdminApiResult.failure("User deletion error: " + e.getMessage());
        }
    }

    /**
     * 获取连接池统计信息
     */
    public ConnectionPoolStats getConnectionPoolStats() {
        ConnectionPoolStats stats = new ConnectionPoolStats();
        stats.setTotalRequests(totalRequests.get());
        stats.setSuccessfulRequests(successfulRequests.get());
        stats.setFailedRequests(failedRequests.get());
        stats.setRetryCount(retryCount.get());
        stats.setSuccessRate(totalRequests.get() > 0 ? 
            (double) successfulRequests.get() / totalRequests.get() * 100 : 0.0);
        return stats;
    }

    /**
     * 健康检查
     */
    public boolean isHealthy() {
        try {
            Keycloak keycloakConfig = securityProperties.getKeycloak();
            String healthUrl = keycloakConfig.getServerUrl() + "/health";
            
            HttpEntity<String> request = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<String> response = restTemplate.exchange(healthUrl, HttpMethod.GET, request, String.class);
            
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            logger.debug("Keycloak health check failed", e);
            return false;
        }
    }

    // ================================ 私有辅助方法 ================================

    /**
     * 获取管理员访问令牌
     */
    private synchronized String getAdminAccessToken() {
        try {
            // 检查token是否过期
            if (adminAccessToken != null && System.currentTimeMillis() < tokenExpiryTime) {
                return adminAccessToken;
            }
            
            Keycloak keycloakConfig = securityProperties.getKeycloak();
            String tokenUrl = keycloakConfig.getServerUrl() + "/realms/master/protocol/openid-connect/token";
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "password");
            formData.add("client_id", "admin-cli");
            formData.add("username", keycloakConfig.getAdminUsername());
            formData.add("password", keycloakConfig.getAdminPassword());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                adminAccessToken = (String) response.getBody().get("access_token");
                Integer expiresIn = (Integer) response.getBody().get("expires_in");
                tokenExpiryTime = System.currentTimeMillis() + (expiresIn * 1000L) - 60000; // 提前1分钟过期
                
                logger.debug("Admin access token refreshed, expires in {} seconds", expiresIn);
                return adminAccessToken;
            }
            
        } catch (Exception e) {
            logger.error("Failed to obtain admin access token", e);
        }
        
        return null;
    }

    /**
     * 构建用户创建请求表示
     */
    private Map<String, Object> buildUserRepresentation(UserCreationRequest userRequest) {
        Map<String, Object> userRepresentation = new HashMap<>();
        userRepresentation.put("username", userRequest.getUsername());
        userRepresentation.put("enabled", userRequest.isEnabled());
        userRepresentation.put("emailVerified", userRequest.isEmailVerified());
        
        if (StringUtils.hasText(userRequest.getEmail())) {
            userRepresentation.put("email", userRequest.getEmail());
        }
        
        if (StringUtils.hasText(userRequest.getFirstName())) {
            userRepresentation.put("firstName", userRequest.getFirstName());
        }
        
        if (StringUtils.hasText(userRequest.getLastName())) {
            userRepresentation.put("lastName", userRequest.getLastName());
        }
        
        // 设置密码
        if (StringUtils.hasText(userRequest.getPassword())) {
            Map<String, Object> credential = new HashMap<>();
            credential.put("type", "password");
            credential.put("value", userRequest.getPassword());
            credential.put("temporary", userRequest.isTemporaryPassword());
            userRepresentation.put("credentials", Arrays.asList(credential));
        }
        
        // 设置属性
        if (userRequest.getAttributes() != null && !userRequest.getAttributes().isEmpty()) {
            userRepresentation.put("attributes", userRequest.getAttributes());
        }
        
        return userRepresentation;
    }

    /**
     * 构建用户更新请求表示
     */
    private Map<String, Object> buildUserUpdateRepresentation(UserUpdateRequest userRequest) {
        Map<String, Object> userRepresentation = new HashMap<>();
        
        if (userRequest.getUsername() != null) {
            userRepresentation.put("username", userRequest.getUsername());
        }
        
        if (userRequest.getEnabled() != null) {
            userRepresentation.put("enabled", userRequest.getEnabled());
        }
        
        if (userRequest.getEmail() != null) {
            userRepresentation.put("email", userRequest.getEmail());
        }
        
        if (userRequest.getFirstName() != null) {
            userRepresentation.put("firstName", userRequest.getFirstName());
        }
        
        if (userRequest.getLastName() != null) {
            userRepresentation.put("lastName", userRequest.getLastName());
        }
        
        if (userRequest.getEmailVerified() != null) {
            userRepresentation.put("emailVerified", userRequest.getEmailVerified());
        }
        
        if (userRequest.getAttributes() != null) {
            userRepresentation.put("attributes", userRequest.getAttributes());
        }
        
        return userRepresentation;
    }

    // ================================ 内部类定义 ================================

    /**
     * 认证结果
     */
    public static class AuthenticationResult {
        private boolean success;
        private String message;
        private Map<String, Object> data;
        private Exception exception;

        private AuthenticationResult(boolean success, String message, Map<String, Object> data, Exception exception) {
            this.success = success;
            this.message = message;
            this.data = data;
            this.exception = exception;
        }

        public static AuthenticationResult success(Map<String, Object> data) {
            return new AuthenticationResult(true, "Authentication successful", data, null);
        }

        public static AuthenticationResult failure(String message) {
            return new AuthenticationResult(false, message, null, null);
        }

        public static AuthenticationResult failure(String message, Exception exception) {
            return new AuthenticationResult(false, message, null, exception);
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Map<String, Object> getData() { return data; }
        public Exception getException() { return exception; }
    }

    /**
     * 管理员API结果
     */
    public static class AdminApiResult {
        private boolean success;
        private String message;
        private String userId;
        private Exception exception;

        private AdminApiResult(boolean success, String message, String userId, Exception exception) {
            this.success = success;
            this.message = message;
            this.userId = userId;
            this.exception = exception;
        }

        public static AdminApiResult success(String message, String userId) {
            return new AdminApiResult(true, message, userId, null);
        }

        public static AdminApiResult failure(String message) {
            return new AdminApiResult(false, message, null, null);
        }

        public static AdminApiResult failure(String message, Exception exception) {
            return new AdminApiResult(false, message, null, exception);
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getUserId() { return userId; }
        public Exception getException() { return exception; }
    }

    /**
     * 用户信息
     */
    public static class UserInfo {
        private String sub;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private Boolean emailVerified;
        private Map<String, Object> attributes;

        public static UserInfo fromKeycloakResponse(Map<String, Object> response) {
            UserInfo userInfo = new UserInfo();
            userInfo.sub = (String) response.get("sub");
            userInfo.username = (String) response.get("preferred_username");
            userInfo.email = (String) response.get("email");
            userInfo.firstName = (String) response.get("given_name");
            userInfo.lastName = (String) response.get("family_name");
            userInfo.emailVerified = (Boolean) response.get("email_verified");
            userInfo.attributes = new HashMap<>(response);
            return userInfo;
        }

        // Getters and setters
        public String getSub() { return sub; }
        public void setSub(String sub) { this.sub = sub; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public Boolean getEmailVerified() { return emailVerified; }
        public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }

        public Map<String, Object> getAttributes() { return attributes; }
        public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }
    }

    /**
     * 用户创建请求
     */
    public static class UserCreationRequest {
        private String username;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
        private boolean enabled = true;
        private boolean emailVerified = false;
        private boolean temporaryPassword = false;
        private Map<String, List<String>> attributes;

        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public boolean isEmailVerified() { return emailVerified; }
        public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }

        public boolean isTemporaryPassword() { return temporaryPassword; }
        public void setTemporaryPassword(boolean temporaryPassword) { this.temporaryPassword = temporaryPassword; }

        public Map<String, List<String>> getAttributes() { return attributes; }
        public void setAttributes(Map<String, List<String>> attributes) { this.attributes = attributes; }
    }

    /**
     * 用户更新请求
     */
    public static class UserUpdateRequest {
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private Boolean enabled;
        private Boolean emailVerified;
        private Map<String, List<String>> attributes;

        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }

        public Boolean getEmailVerified() { return emailVerified; }
        public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }

        public Map<String, List<String>> getAttributes() { return attributes; }
        public void setAttributes(Map<String, List<String>> attributes) { this.attributes = attributes; }
    }

    /**
     * 连接池统计信息
     */
    public static class ConnectionPoolStats {
        private long totalRequests;
        private long successfulRequests;
        private long failedRequests;
        private long retryCount;
        private double successRate;

        // Getters and setters
        public long getTotalRequests() { return totalRequests; }
        public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }

        public long getSuccessfulRequests() { return successfulRequests; }
        public void setSuccessfulRequests(long successfulRequests) { this.successfulRequests = successfulRequests; }

        public long getFailedRequests() { return failedRequests; }
        public void setFailedRequests(long failedRequests) { this.failedRequests = failedRequests; }

        public long getRetryCount() { return retryCount; }
        public void setRetryCount(long retryCount) { this.retryCount = retryCount; }

        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
    }
}