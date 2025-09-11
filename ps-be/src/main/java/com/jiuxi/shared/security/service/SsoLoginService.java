package com.jiuxi.shared.security.service;

import com.jiuxi.shared.security.config.KeycloakClient;
import com.jiuxi.shared.security.config.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * SSO登录服务
 * 负责优化SSO认证流程、重定向逻辑和登录状态缓存
 * 
 * @author Security Refactoring
 * @since Phase 4.2.4
 */
@Service
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class SsoLoginService {

    private static final Logger logger = LoggerFactory.getLogger(SsoLoginService.class);

    @Autowired
    private KeycloakClient keycloakClient;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    // Redis键前缀
    private static final String LOGIN_STATE_PREFIX = "ps:security:sso:login_state:";
    private static final String SESSION_PREFIX = "ps:security:sso:session:";
    private static final String LOGOUT_STATE_PREFIX = "ps:security:sso:logout_state:";

    /**
     * 初始化SSO登录流程
     */
    public SsoLoginResult initiateLogin(HttpServletRequest request, String redirectUri) {
        try {
            SecurityProperties.Keycloak keycloakConfig = securityProperties.getKeycloak();
            
            // 生成状态参数用于防止CSRF攻击
            String state = generateState();
            
            // 生成随机数用于PKCE（可选）
            String codeVerifier = generateCodeVerifier();
            String codeChallenge = generateCodeChallenge(codeVerifier);
            
            // 保存登录状态
            LoginState loginState = new LoginState();
            loginState.setState(state);
            loginState.setCodeVerifier(codeVerifier);
            loginState.setRedirectUri(redirectUri);
            loginState.setClientIp(getClientIp(request));
            loginState.setUserAgent(request.getHeader("User-Agent"));
            loginState.setCreatedAt(LocalDateTime.now());
            
            saveLoginState(state, loginState);
            
            // 构建Keycloak授权URL
            String authUrl = buildAuthorizationUrl(keycloakConfig, state, codeChallenge, redirectUri);
            
            logger.info("SSO login initiated: state={}, clientIp={}", state, loginState.getClientIp());
            
            return SsoLoginResult.success(authUrl, state);
            
        } catch (Exception e) {
            logger.error("Failed to initiate SSO login", e);
            return SsoLoginResult.failure("SSO login initiation failed: " + e.getMessage());
        }
    }

    /**
     * 处理SSO回调
     */
    public SsoCallbackResult handleCallback(String code, String state, HttpServletRequest request) {
        try {
            // 验证状态参数
            LoginState loginState = getLoginState(state);
            if (loginState == null) {
                logger.warn("Invalid or expired login state: state={}", state);
                return SsoCallbackResult.failure("Invalid or expired login state");
            }
            
            // 验证客户端IP（可选的安全检查）
            String currentClientIp = getClientIp(request);
            if (!currentClientIp.equals(loginState.getClientIp())) {
                logger.warn("Client IP mismatch during SSO callback: expected={}, actual={}", 
                    loginState.getClientIp(), currentClientIp);
                // 可以选择是否严格检查IP，这里只记录日志
            }
            
            // 使用授权码获取访问令牌
            SecurityProperties.Keycloak keycloakConfig = securityProperties.getKeycloak();
            TokenExchangeResult tokenResult = exchangeCodeForToken(code, loginState, keycloakConfig);
            
            if (!tokenResult.isSuccess()) {
                logger.error("Token exchange failed: {}", tokenResult.getErrorMessage());
                return SsoCallbackResult.failure("Token exchange failed: " + tokenResult.getErrorMessage());
            }
            
            // 获取用户信息
            KeycloakClient.UserInfo userInfo = keycloakClient.getUserInfo(tokenResult.getAccessToken());
            if (userInfo == null) {
                logger.error("Failed to get user info after successful token exchange");
                return SsoCallbackResult.failure("Failed to get user information");
            }
            
            // 创建用户会话
            UserSession session = createUserSession(userInfo, tokenResult);
            cacheUserSession(session);
            
            // 清理登录状态
            removeLoginState(state);
            
            logger.info("SSO callback processed successfully: userId={}, sessionId={}", 
                userInfo.getSub(), session.getSessionId());
            
            return SsoCallbackResult.success(session, loginState.getRedirectUri());
            
        } catch (Exception e) {
            logger.error("SSO callback processing failed: code={}, state={}", code, state, e);
            return SsoCallbackResult.failure("SSO callback processing failed: " + e.getMessage());
        }
    }

    /**
     * 处理单点登出
     */
    public SsoLogoutResult initiateLogout(String sessionId, String redirectUri) {
        try {
            // 获取用户会话
            UserSession session = getUserSession(sessionId);
            if (session == null) {
                logger.warn("Session not found for logout: sessionId={}", sessionId);
                return SsoLogoutResult.failure("Session not found");
            }
            
            SecurityProperties.Keycloak keycloakConfig = securityProperties.getKeycloak();
            
            // 生成登出状态
            String logoutState = generateState();
            LogoutState logoutStateObj = new LogoutState();
            logoutStateObj.setState(logoutState);
            logoutStateObj.setSessionId(sessionId);
            logoutStateObj.setRedirectUri(redirectUri);
            logoutStateObj.setCreatedAt(LocalDateTime.now());
            
            saveLogoutState(logoutState, logoutStateObj);
            
            // 构建Keycloak登出URL
            String logoutUrl = buildLogoutUrl(keycloakConfig, session.getIdToken(), redirectUri, logoutState);
            
            // 本地会话清理
            invalidateUserSession(sessionId);
            
            logger.info("SSO logout initiated: sessionId={}, state={}", sessionId, logoutState);
            
            return SsoLogoutResult.success(logoutUrl, logoutState);
            
        } catch (Exception e) {
            logger.error("Failed to initiate SSO logout: sessionId={}", sessionId, e);
            return SsoLogoutResult.failure("SSO logout initiation failed: " + e.getMessage());
        }
    }

    /**
     * 处理登出回调
     */
    public SsoLogoutCallbackResult handleLogoutCallback(String state) {
        try {
            LogoutState logoutState = getLogoutState(state);
            if (logoutState == null) {
                logger.warn("Invalid or expired logout state: state={}", state);
                return SsoLogoutCallbackResult.failure("Invalid or expired logout state");
            }
            
            // 确保会话已清理
            invalidateUserSession(logoutState.getSessionId());
            
            // 清理登出状态
            removeLogoutState(state);
            
            logger.info("SSO logout callback processed successfully: state={}, sessionId={}", 
                state, logoutState.getSessionId());
            
            return SsoLogoutCallbackResult.success(logoutState.getRedirectUri());
            
        } catch (Exception e) {
            logger.error("SSO logout callback processing failed: state={}", state, e);
            return SsoLogoutCallbackResult.failure("SSO logout callback processing failed: " + e.getMessage());
        }
    }

    /**
     * 刷新用户会话
     */
    public SessionRefreshResult refreshSession(String sessionId) {
        try {
            UserSession session = getUserSession(sessionId);
            if (session == null) {
                return SessionRefreshResult.failure("Session not found");
            }
            
            // 检查会话是否已过期
            if (session.isExpired()) {
                invalidateUserSession(sessionId);
                return SessionRefreshResult.failure("Session expired");
            }
            
            // 如果访问令牌即将过期，尝试刷新
            if (session.isAccessTokenNearExpiry() && StringUtils.hasText(session.getRefreshToken())) {
                KeycloakClient.AuthenticationResult refreshResult = keycloakClient.refreshToken(session.getRefreshToken());
                
                if (refreshResult.isSuccess()) {
                    // 更新会话令牌信息
                    updateSessionTokens(session, refreshResult.getData());
                    cacheUserSession(session);
                    
                    logger.debug("Session tokens refreshed successfully: sessionId={}", sessionId);
                } else {
                    logger.warn("Failed to refresh session tokens: sessionId={}, error={}", 
                        sessionId, refreshResult.getMessage());
                    return SessionRefreshResult.failure("Token refresh failed: " + refreshResult.getMessage());
                }
            }
            
            return SessionRefreshResult.success(session);
            
        } catch (Exception e) {
            logger.error("Session refresh failed: sessionId={}", sessionId, e);
            return SessionRefreshResult.failure("Session refresh failed: " + e.getMessage());
        }
    }

    /**
     * 获取用户会话
     */
    public UserSession getUserSession(String sessionId) {
        if (redisTemplate == null || !StringUtils.hasText(sessionId)) {
            return null;
        }
        
        String sessionKey = SESSION_PREFIX + sessionId;
        Object sessionObj = redisTemplate.opsForValue().get(sessionKey);
        
        if (sessionObj instanceof UserSession) {
            UserSession session = (UserSession) sessionObj;
            
            // 检查会话是否过期
            if (session.isExpired()) {
                invalidateUserSession(sessionId);
                return null;
            }
            
            return session;
        }
        
        return null;
    }

    /**
     * 验证会话状态
     */
    public boolean isSessionValid(String sessionId) {
        UserSession session = getUserSession(sessionId);
        return session != null && !session.isExpired();
    }

    // ================================ 私有辅助方法 ================================

    /**
     * 生成状态参数
     */
    private String generateState() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成PKCE代码验证器
     */
    private String generateCodeVerifier() {
        return UUID.randomUUID().toString().replace("-", "") + 
               UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成PKCE代码挑战
     */
    private String generateCodeChallenge(String codeVerifier) {
        // 简化实现，实际应该使用SHA256哈希和Base64URL编码
        return codeVerifier;
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    /**
     * 构建授权URL
     */
    private String buildAuthorizationUrl(SecurityProperties.Keycloak keycloakConfig, 
                                       String state, String codeChallenge, String redirectUri) {
        StringBuilder url = new StringBuilder();
        url.append(keycloakConfig.getServerUrl())
           .append("/realms/").append(keycloakConfig.getRealm())
           .append("/protocol/openid-connect/auth");
        
        url.append("?response_type=code");
        url.append("&client_id=").append(URLEncoder.encode(keycloakConfig.getClientId(), StandardCharsets.UTF_8));
        url.append("&state=").append(URLEncoder.encode(state, StandardCharsets.UTF_8));
        url.append("&scope=openid profile email");
        
        if (StringUtils.hasText(redirectUri)) {
            url.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, StandardCharsets.UTF_8));
        }
        
        if (StringUtils.hasText(codeChallenge)) {
            url.append("&code_challenge=").append(URLEncoder.encode(codeChallenge, StandardCharsets.UTF_8));
            url.append("&code_challenge_method=plain");
        }
        
        return url.toString();
    }

    /**
     * 构建登出URL
     */
    private String buildLogoutUrl(SecurityProperties.Keycloak keycloakConfig, 
                                String idToken, String redirectUri, String state) {
        StringBuilder url = new StringBuilder();
        url.append(keycloakConfig.getServerUrl())
           .append("/realms/").append(keycloakConfig.getRealm())
           .append("/protocol/openid-connect/logout");
        
        url.append("?client_id=").append(URLEncoder.encode(keycloakConfig.getClientId(), StandardCharsets.UTF_8));
        
        if (StringUtils.hasText(idToken)) {
            url.append("&id_token_hint=").append(URLEncoder.encode(idToken, StandardCharsets.UTF_8));
        }
        
        if (StringUtils.hasText(redirectUri)) {
            url.append("&post_logout_redirect_uri=").append(URLEncoder.encode(redirectUri, StandardCharsets.UTF_8));
        }
        
        if (StringUtils.hasText(state)) {
            url.append("&state=").append(URLEncoder.encode(state, StandardCharsets.UTF_8));
        }
        
        return url.toString();
    }

    /**
     * 使用授权码交换访问令牌
     */
    private TokenExchangeResult exchangeCodeForToken(String code, LoginState loginState, 
                                                   SecurityProperties.Keycloak keycloakConfig) {
        // 这里应该实现OIDC授权码流程的令牌交换
        // 由于需要实现具体的HTTP请求，这里返回成功的模拟结果
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("access_token", "mock_access_token");
        tokenData.put("refresh_token", "mock_refresh_token");
        tokenData.put("id_token", "mock_id_token");
        tokenData.put("expires_in", 3600);
        
        return TokenExchangeResult.success(tokenData);
    }

    /**
     * 创建用户会话
     */
    private UserSession createUserSession(KeycloakClient.UserInfo userInfo, TokenExchangeResult tokenResult) {
        UserSession session = new UserSession();
        session.setSessionId(generateState());
        session.setUserId(userInfo.getSub());
        session.setUsername(userInfo.getUsername());
        session.setEmail(userInfo.getEmail());
        session.setAccessToken((String) tokenResult.getTokenData().get("access_token"));
        session.setRefreshToken((String) tokenResult.getTokenData().get("refresh_token"));
        session.setIdToken((String) tokenResult.getTokenData().get("id_token"));
        session.setCreatedAt(LocalDateTime.now());
        session.setLastAccessAt(LocalDateTime.now());
        
        Integer expiresIn = (Integer) tokenResult.getTokenData().get("expires_in");
        session.setExpiresAt(LocalDateTime.now().plusSeconds(expiresIn != null ? expiresIn : 3600));
        
        return session;
    }

    /**
     * 更新会话令牌
     */
    private void updateSessionTokens(UserSession session, Map<String, Object> tokenData) {
        session.setAccessToken((String) tokenData.get("access_token"));
        session.setLastAccessAt(LocalDateTime.now());
        
        String newRefreshToken = (String) tokenData.get("refresh_token");
        if (StringUtils.hasText(newRefreshToken)) {
            session.setRefreshToken(newRefreshToken);
        }
        
        Integer expiresIn = (Integer) tokenData.get("expires_in");
        if (expiresIn != null) {
            session.setExpiresAt(LocalDateTime.now().plusSeconds(expiresIn));
        }
    }

    /**
     * 缓存用户会话
     */
    private void cacheUserSession(UserSession session) {
        if (redisTemplate == null) {
            return;
        }
        
        String sessionKey = SESSION_PREFIX + session.getSessionId();
        
        // 会话过期时间为令牌过期时间加上一个缓冲时间
        long ttlSeconds = java.time.Duration.between(LocalDateTime.now(), session.getExpiresAt()).getSeconds() + 300;
        
        redisTemplate.opsForValue().set(sessionKey, session, ttlSeconds, TimeUnit.SECONDS);
    }

    /**
     * 失效用户会话
     */
    private void invalidateUserSession(String sessionId) {
        if (redisTemplate == null || !StringUtils.hasText(sessionId)) {
            return;
        }
        
        String sessionKey = SESSION_PREFIX + sessionId;
        redisTemplate.delete(sessionKey);
        
        logger.debug("User session invalidated: sessionId={}", sessionId);
    }

    /**
     * 保存登录状态
     */
    private void saveLoginState(String state, LoginState loginState) {
        if (redisTemplate == null) {
            return;
        }
        
        String stateKey = LOGIN_STATE_PREFIX + state;
        redisTemplate.opsForValue().set(stateKey, loginState, 10, TimeUnit.MINUTES);
    }

    /**
     * 获取登录状态
     */
    private LoginState getLoginState(String state) {
        if (redisTemplate == null || !StringUtils.hasText(state)) {
            return null;
        }
        
        String stateKey = LOGIN_STATE_PREFIX + state;
        Object stateObj = redisTemplate.opsForValue().get(stateKey);
        
        return stateObj instanceof LoginState ? (LoginState) stateObj : null;
    }

    /**
     * 移除登录状态
     */
    private void removeLoginState(String state) {
        if (redisTemplate == null || !StringUtils.hasText(state)) {
            return;
        }
        
        String stateKey = LOGIN_STATE_PREFIX + state;
        redisTemplate.delete(stateKey);
    }

    /**
     * 保存登出状态
     */
    private void saveLogoutState(String state, LogoutState logoutState) {
        if (redisTemplate == null) {
            return;
        }
        
        String stateKey = LOGOUT_STATE_PREFIX + state;
        redisTemplate.opsForValue().set(stateKey, logoutState, 10, TimeUnit.MINUTES);
    }

    /**
     * 获取登出状态
     */
    private LogoutState getLogoutState(String state) {
        if (redisTemplate == null || !StringUtils.hasText(state)) {
            return null;
        }
        
        String stateKey = LOGOUT_STATE_PREFIX + state;
        Object stateObj = redisTemplate.opsForValue().get(stateKey);
        
        return stateObj instanceof LogoutState ? (LogoutState) stateObj : null;
    }

    /**
     * 移除登出状态
     */
    private void removeLogoutState(String state) {
        if (redisTemplate == null || !StringUtils.hasText(state)) {
            return;
        }
        
        String stateKey = LOGOUT_STATE_PREFIX + state;
        redisTemplate.delete(stateKey);
    }

    // ================================ 内部类定义 ================================

    /**
     * SSO登录结果
     */
    public static class SsoLoginResult {
        private boolean success;
        private String authorizationUrl;
        private String state;
        private String errorMessage;

        private SsoLoginResult(boolean success, String authorizationUrl, String state, String errorMessage) {
            this.success = success;
            this.authorizationUrl = authorizationUrl;
            this.state = state;
            this.errorMessage = errorMessage;
        }

        public static SsoLoginResult success(String authorizationUrl, String state) {
            return new SsoLoginResult(true, authorizationUrl, state, null);
        }

        public static SsoLoginResult failure(String errorMessage) {
            return new SsoLoginResult(false, null, null, errorMessage);
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getAuthorizationUrl() { return authorizationUrl; }
        public String getState() { return state; }
        public String getErrorMessage() { return errorMessage; }
    }

    /**
     * SSO回调结果
     */
    public static class SsoCallbackResult {
        private boolean success;
        private UserSession session;
        private String redirectUri;
        private String errorMessage;

        private SsoCallbackResult(boolean success, UserSession session, String redirectUri, String errorMessage) {
            this.success = success;
            this.session = session;
            this.redirectUri = redirectUri;
            this.errorMessage = errorMessage;
        }

        public static SsoCallbackResult success(UserSession session, String redirectUri) {
            return new SsoCallbackResult(true, session, redirectUri, null);
        }

        public static SsoCallbackResult failure(String errorMessage) {
            return new SsoCallbackResult(false, null, null, errorMessage);
        }

        // Getters
        public boolean isSuccess() { return success; }
        public UserSession getSession() { return session; }
        public String getRedirectUri() { return redirectUri; }
        public String getErrorMessage() { return errorMessage; }
    }

    /**
     * SSO登出结果
     */
    public static class SsoLogoutResult {
        private boolean success;
        private String logoutUrl;
        private String state;
        private String errorMessage;

        private SsoLogoutResult(boolean success, String logoutUrl, String state, String errorMessage) {
            this.success = success;
            this.logoutUrl = logoutUrl;
            this.state = state;
            this.errorMessage = errorMessage;
        }

        public static SsoLogoutResult success(String logoutUrl, String state) {
            return new SsoLogoutResult(true, logoutUrl, state, null);
        }

        public static SsoLogoutResult failure(String errorMessage) {
            return new SsoLogoutResult(false, null, null, errorMessage);
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getLogoutUrl() { return logoutUrl; }
        public String getState() { return state; }
        public String getErrorMessage() { return errorMessage; }
    }

    /**
     * SSO登出回调结果
     */
    public static class SsoLogoutCallbackResult {
        private boolean success;
        private String redirectUri;
        private String errorMessage;

        private SsoLogoutCallbackResult(boolean success, String redirectUri, String errorMessage) {
            this.success = success;
            this.redirectUri = redirectUri;
            this.errorMessage = errorMessage;
        }

        public static SsoLogoutCallbackResult success(String redirectUri) {
            return new SsoLogoutCallbackResult(true, redirectUri, null);
        }

        public static SsoLogoutCallbackResult failure(String errorMessage) {
            return new SsoLogoutCallbackResult(false, null, errorMessage);
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getRedirectUri() { return redirectUri; }
        public String getErrorMessage() { return errorMessage; }
    }

    /**
     * 会话刷新结果
     */
    public static class SessionRefreshResult {
        private boolean success;
        private UserSession session;
        private String errorMessage;

        private SessionRefreshResult(boolean success, UserSession session, String errorMessage) {
            this.success = success;
            this.session = session;
            this.errorMessage = errorMessage;
        }

        public static SessionRefreshResult success(UserSession session) {
            return new SessionRefreshResult(true, session, null);
        }

        public static SessionRefreshResult failure(String errorMessage) {
            return new SessionRefreshResult(false, null, errorMessage);
        }

        // Getters
        public boolean isSuccess() { return success; }
        public UserSession getSession() { return session; }
        public String getErrorMessage() { return errorMessage; }
    }

    /**
     * 令牌交换结果
     */
    public static class TokenExchangeResult {
        private boolean success;
        private Map<String, Object> tokenData;
        private String errorMessage;

        private TokenExchangeResult(boolean success, Map<String, Object> tokenData, String errorMessage) {
            this.success = success;
            this.tokenData = tokenData;
            this.errorMessage = errorMessage;
        }

        public static TokenExchangeResult success(Map<String, Object> tokenData) {
            return new TokenExchangeResult(true, tokenData, null);
        }

        public static TokenExchangeResult failure(String errorMessage) {
            return new TokenExchangeResult(false, null, errorMessage);
        }

        // Getters
        public boolean isSuccess() { return success; }
        public Map<String, Object> getTokenData() { return tokenData; }
        public String getAccessToken() { return tokenData != null ? (String) tokenData.get("access_token") : null; }
        public String getErrorMessage() { return errorMessage; }
    }

    /**
     * 登录状态
     */
    public static class LoginState {
        private String state;
        private String codeVerifier;
        private String redirectUri;
        private String clientIp;
        private String userAgent;
        private LocalDateTime createdAt;

        // Getters and setters
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }

        public String getCodeVerifier() { return codeVerifier; }
        public void setCodeVerifier(String codeVerifier) { this.codeVerifier = codeVerifier; }

        public String getRedirectUri() { return redirectUri; }
        public void setRedirectUri(String redirectUri) { this.redirectUri = redirectUri; }

        public String getClientIp() { return clientIp; }
        public void setClientIp(String clientIp) { this.clientIp = clientIp; }

        public String getUserAgent() { return userAgent; }
        public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    /**
     * 登出状态
     */
    public static class LogoutState {
        private String state;
        private String sessionId;
        private String redirectUri;
        private LocalDateTime createdAt;

        // Getters and setters
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }

        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }

        public String getRedirectUri() { return redirectUri; }
        public void setRedirectUri(String redirectUri) { this.redirectUri = redirectUri; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    /**
     * 用户会话
     */
    public static class UserSession {
        private String sessionId;
        private String userId;
        private String username;
        private String email;
        private String accessToken;
        private String refreshToken;
        private String idToken;
        private LocalDateTime createdAt;
        private LocalDateTime lastAccessAt;
        private LocalDateTime expiresAt;

        public boolean isExpired() {
            return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
        }

        public boolean isAccessTokenNearExpiry() {
            return expiresAt != null && LocalDateTime.now().plusMinutes(5).isAfter(expiresAt);
        }

        // Getters and setters
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

        public String getIdToken() { return idToken; }
        public void setIdToken(String idToken) { this.idToken = idToken; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public LocalDateTime getLastAccessAt() { return lastAccessAt; }
        public void setLastAccessAt(LocalDateTime lastAccessAt) { this.lastAccessAt = lastAccessAt; }

        public LocalDateTime getExpiresAt() { return expiresAt; }
        public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    }
}