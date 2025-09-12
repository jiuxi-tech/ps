package com.jiuxi.shared.security.controller;

import com.jiuxi.shared.common.base.response.BaseResponse;
import com.jiuxi.shared.security.config.TokenService;
import com.jiuxi.shared.security.config.TokenStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 令牌管理控制器
 * 提供令牌管理和监控的API接口
 * 
 * @author Security Refactoring
 * @since Phase 4.2.3
 */
@RestController
@RequestMapping("/api/security/tokens")
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class TokenManagementController {

    private static final Logger logger = LoggerFactory.getLogger(TokenManagementController.class);

    @Autowired
    private TokenService tokenService;
    
    @Autowired(required = false)
    private TokenStorageManager tokenStorageManager;

    /**
     * 生成令牌
     */
    @PostMapping("/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<TokenService.TokenInfo>> generateToken(
            @RequestBody TokenGenerationRequest request) {
        
        try {
            TokenService.UserInfo userInfo = new TokenService.UserInfo();
            userInfo.setUserId(request.getUserId());
            userInfo.setUsername(request.getUsername());
            userInfo.setEmail(request.getEmail());
            userInfo.setName(request.getName());
            userInfo.setRoles(request.getRoles());
            userInfo.setPermissions(request.getPermissions());
            
            TokenService.TokenInfo tokenInfo = tokenService.generateToken(userInfo, request.getCustomClaims());
            
            return ResponseEntity.ok(BaseResponse.success(tokenInfo));

        } catch (Exception e) {
            logger.error("Error generating token", e);
            return ResponseEntity.badRequest()
                .body(BaseResponse.error("Token generation failed: " + e.getMessage()));
        }
    }

    /**
     * 验证令牌
     */
    @PostMapping("/validate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<Map<String, Object>>> validateToken(
            @RequestBody TokenValidationRequest request) {
        
        try {
            TokenService.TokenValidationResult result = tokenService.validateToken(request.getToken());
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", result.isValid());
            response.put("message", result.getMessage());
            
            if (result.isValid() && result.getTokenInfo() != null) {
                response.put("tokenInfo", result.getTokenInfo());
            }
            
            return ResponseEntity.ok(BaseResponse.success(response));

        } catch (Exception e) {
            logger.error("Error validating token", e);
            return ResponseEntity.badRequest()
                .body(BaseResponse.error("Token validation failed: " + e.getMessage()));
        }
    }

    /**
     * 刷新令牌
     */
    @PostMapping("/refresh")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<BaseResponse<TokenService.TokenInfo>> refreshToken(
            @RequestBody TokenRefreshRequest request) {
        
        try {
            TokenService.TokenInfo tokenInfo = tokenService.refreshToken(request.getRefreshToken());
            
            return ResponseEntity.ok(BaseResponse.success(tokenInfo));

        } catch (Exception e) {
            logger.error("Error refreshing token", e);
            return ResponseEntity.badRequest()
                .body(BaseResponse.error("Token refresh failed: " + e.getMessage()));
        }
    }

    /**
     * 撤销令牌
     */
    @PostMapping("/revoke")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<String>> revokeToken(
            @RequestBody TokenRevocationRequest request) {
        
        try {
            tokenService.revokeToken(request.getToken());
            
            return ResponseEntity.ok(BaseResponse.success("Token revoked successfully"));

        } catch (Exception e) {
            logger.error("Error revoking token", e);
            return ResponseEntity.badRequest()
                .body(BaseResponse.error("Token revocation failed: " + e.getMessage()));
        }
    }

    /**
     * 撤销用户的所有令牌
     */
    @PostMapping("/revoke-user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<String>> revokeUserTokens(@PathVariable String userId) {
        
        try {
            tokenService.revokeUserTokens(userId);
            
            if (tokenStorageManager != null) {
                tokenStorageManager.revokeUserTokens(userId);
            }
            
            return ResponseEntity.ok(BaseResponse.success("All user tokens revoked successfully"));

        } catch (Exception e) {
            logger.error("Error revoking user tokens for user: {}", userId, e);
            return ResponseEntity.badRequest()
                .body(BaseResponse.error("User tokens revocation failed: " + e.getMessage()));
        }
    }

    /**
     * 获取令牌统计信息
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<Object>> getTokenStatistics() {
        
        try {
            if (tokenStorageManager != null) {
                TokenStorageManager.TokenStatistics stats = tokenStorageManager.getTokenStatistics();
                return ResponseEntity.ok(BaseResponse.success(stats));
            } else {
                Map<String, String> message = new HashMap<>();
                message.put("message", "Token storage manager not available (Redis not configured)");
                return ResponseEntity.ok(BaseResponse.success(message));
            }

        } catch (Exception e) {
            logger.error("Error retrieving token statistics", e);
            return ResponseEntity.badRequest()
                .body(BaseResponse.error("Failed to retrieve token statistics: " + e.getMessage()));
        }
    }

    /**
     * 清理过期令牌
     */
    @PostMapping("/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<String>> cleanupExpiredTokens() {
        
        try {
            tokenService.cleanupExpiredTokens();
            
            if (tokenStorageManager != null) {
                tokenStorageManager.cleanupExpiredTokens();
            }
            
            return ResponseEntity.ok(BaseResponse.success("Expired tokens cleanup completed"));

        } catch (Exception e) {
            logger.error("Error during token cleanup", e);
            return ResponseEntity.badRequest()
                .body(BaseResponse.error("Token cleanup failed: " + e.getMessage()));
        }
    }

    // ================================ 请求/响应类 ================================

    /**
     * 令牌生成请求
     */
    public static class TokenGenerationRequest {
        private String userId;
        private String username;
        private String email;
        private String name;
        private java.util.List<String> roles;
        private java.util.List<String> permissions;
        private Map<String, Object> customClaims;

        // Getters and setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public java.util.List<String> getRoles() { return roles; }
        public void setRoles(java.util.List<String> roles) { this.roles = roles; }

        public java.util.List<String> getPermissions() { return permissions; }
        public void setPermissions(java.util.List<String> permissions) { this.permissions = permissions; }

        public Map<String, Object> getCustomClaims() { return customClaims; }
        public void setCustomClaims(Map<String, Object> customClaims) { this.customClaims = customClaims; }
    }

    /**
     * 令牌验证请求
     */
    public static class TokenValidationRequest {
        private String token;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }

    /**
     * 令牌刷新请求
     */
    public static class TokenRefreshRequest {
        private String refreshToken;

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }

    /**
     * 令牌撤销请求
     */
    public static class TokenRevocationRequest {
        private String token;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}