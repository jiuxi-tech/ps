package com.jiuxi.shared.security.authorization;

import com.jiuxi.security.core.service.AuthorizationService;
import com.jiuxi.shared.security.authentication.JwtAuthenticationProvider.JwtUserPrincipal;
import com.jiuxi.security.sso.principal.KeycloakUserPrincipal;
import com.jiuxi.shared.security.config.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统一权限评估器
 * 整合现有的权限验证逻辑，支持基于数据库的细粒度权限控制
 * 
 * @author Security Refactoring
 * @since Phase 4.2.2
 */
@Component
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class PermissionEvaluator implements org.springframework.security.access.PermissionEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(PermissionEvaluator.class);

    @Autowired
    private SecurityProperties securityProperties;
    
    @Autowired(required = false)
    private AuthorizationService authorizationService;
    
    @Autowired(required = false)
    private HttpServletRequest request;

    /**
     * 权限缓存，提升性能
     */
    private final ConcurrentHashMap<String, Boolean> permissionCache = new ConcurrentHashMap<>();

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String permissionStr = String.valueOf(permission);
        
        // 检查是否为排除路径
        if (isExcludedFromAuthorization(permissionStr)) {
            return true;
        }

        try {
            return evaluatePermission(authentication, targetDomainObject, permissionStr);
        } catch (Exception e) {
            logger.error("Error evaluating permission: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String permissionStr = String.valueOf(permission);
        
        // 检查是否为排除路径
        if (isExcludedFromAuthorization(permissionStr)) {
            return true;
        }

        try {
            return evaluateResourcePermission(authentication, targetId, targetType, permissionStr);
        } catch (Exception e) {
            logger.error("Error evaluating resource permission: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 评估权限
     */
    private boolean evaluatePermission(Authentication authentication, Object targetDomainObject, String permission) {
        // 1. 检查用户是否有直接权限
        if (hasDirectPermission(authentication, permission)) {
            return true;
        }

        // 2. 如果有AuthorizationService，使用数据库权限验证
        if (authorizationService != null) {
            return evaluateDatabasePermission(authentication, permission);
        }

        // 3. 基于角色的权限判断
        return hasRoleBasedPermission(authentication, permission);
    }

    /**
     * 评估资源权限
     */
    private boolean evaluateResourcePermission(Authentication authentication, Serializable targetId, 
                                             String targetType, String permission) {
        // 构造资源权限标识
        String resourcePermission = targetType + ":" + permission + ":" + targetId;
        
        return evaluatePermission(authentication, null, resourcePermission);
    }

    /**
     * 检查用户是否有直接权限
     */
    private boolean hasDirectPermission(Authentication authentication, String permission) {
        Object principal = authentication.getPrincipal();
        
        // JWT用户主体
        if (principal instanceof JwtUserPrincipal) {
            JwtUserPrincipal jwtPrincipal = (JwtUserPrincipal) principal;
            return jwtPrincipal.getPermissions() != null && 
                   jwtPrincipal.getPermissions().contains(permission);
        }
        
        // Keycloak用户主体
        if (principal instanceof KeycloakUserPrincipal) {
            KeycloakUserPrincipal keycloakPrincipal = (KeycloakUserPrincipal) principal;
            return keycloakPrincipal.hasPermission(permission);
        }
        
        return false;
    }

    /**
     * 基于数据库的权限评估
     */
    private boolean evaluateDatabasePermission(Authentication authentication, String permission) {
        try {
            // 构造缓存键
            String cacheKey = buildCacheKey(authentication, permission);
            
            // 检查缓存
            if (securityProperties.getAuthorization().isCacheEnabled()) {
                Boolean cachedResult = permissionCache.get(cacheKey);
                if (cachedResult != null) {
                    return cachedResult;
                }
            }

            // 获取当前请求路径
            String requestPath = getCurrentRequestPath();
            if (StringUtils.hasText(requestPath)) {
                permission = requestPath;
            }

            // 使用现有的AuthorizationService进行权限验证
            String token = extractToken(authentication);
            boolean hasPermission = authorizationService.authorization(token, permission);

            // 缓存结果
            if (securityProperties.getAuthorization().isCacheEnabled()) {
                permissionCache.put(cacheKey, hasPermission);
            }

            return hasPermission;

        } catch (Exception e) {
            logger.error("Database permission evaluation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 基于角色的权限判断
     */
    private boolean hasRoleBasedPermission(Authentication authentication, String permission) {
        Object principal = authentication.getPrincipal();
        List<String> roles = null;
        
        // 获取用户角色
        if (principal instanceof JwtUserPrincipal) {
            roles = ((JwtUserPrincipal) principal).getRoles();
        } else if (principal instanceof KeycloakUserPrincipal) {
            roles = ((KeycloakUserPrincipal) principal).getRoles();
        }
        
        if (roles == null || roles.isEmpty()) {
            return false;
        }

        // 角色到权限的映射逻辑
        return mapRolesToPermissions(roles, permission);
    }

    /**
     * 角色到权限的映射
     */
    private boolean mapRolesToPermissions(List<String> roles, String permission) {
        // 管理员拥有所有权限
        if (roles.contains("admin") || roles.contains("ADMIN")) {
            return true;
        }

        // 基于权限路径的角色判断
        for (String role : roles) {
            if (hasRolePermission(role, permission)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查角色是否有指定权限
     */
    private boolean hasRolePermission(String role, String permission) {
        switch (role.toLowerCase()) {
            case "manager":
                return permission.startsWith("user:") || permission.startsWith("report:");
            case "user":
                return permission.startsWith("user:read") || permission.startsWith("profile:");
            case "operator":
                return permission.startsWith("data:") || permission.startsWith("export:");
            default:
                return false;
        }
    }

    /**
     * 检查是否为排除授权的路径
     */
    private boolean isExcludedFromAuthorization(String permission) {
        String[] excludePaths = securityProperties.getAuthorization().getExcludePaths();
        if (excludePaths == null || excludePaths.length == 0) {
            return false;
        }

        return Arrays.stream(excludePaths)
                .anyMatch(excludePath -> pathMatches(permission, excludePath));
    }

    /**
     * 路径匹配检查
     */
    private boolean pathMatches(String path, String pattern) {
        if (pattern.equals("/**")) {
            return true;
        }
        
        if (pattern.endsWith("/**")) {
            String prefix = pattern.substring(0, pattern.length() - 3);
            return path.startsWith(prefix);
        }
        
        if (pattern.endsWith("/*")) {
            String prefix = pattern.substring(0, pattern.length() - 2);
            return path.startsWith(prefix) && path.indexOf('/', prefix.length()) == -1;
        }
        
        return path.equals(pattern);
    }

    /**
     * 构造缓存键
     */
    private String buildCacheKey(Authentication authentication, String permission) {
        Object principal = authentication.getPrincipal();
        String userId = "unknown";
        
        if (principal instanceof JwtUserPrincipal) {
            userId = ((JwtUserPrincipal) principal).getUserId();
        } else if (principal instanceof KeycloakUserPrincipal) {
            userId = ((KeycloakUserPrincipal) principal).getUserId();
        }
        
        return userId + ":" + permission;
    }

    /**
     * 从认证对象中提取Token
     */
    private String extractToken(Authentication authentication) {
        if (authentication instanceof com.jiuxi.shared.security.authentication.JwtAuthenticationToken) {
            return ((com.jiuxi.shared.security.authentication.JwtAuthenticationToken) authentication).getToken();
        }
        
        return null;
    }

    /**
     * 获取当前请求路径
     */
    private String getCurrentRequestPath() {
        if (request != null) {
            return request.getRequestURI();
        }
        return null;
    }

    /**
     * 清理权限缓存
     */
    public void clearPermissionCache() {
        permissionCache.clear();
    }

    /**
     * 清理特定用户的权限缓存
     */
    public void clearUserPermissionCache(String userId) {
        permissionCache.entrySet().removeIf(entry -> entry.getKey().startsWith(userId + ":"));
    }
}