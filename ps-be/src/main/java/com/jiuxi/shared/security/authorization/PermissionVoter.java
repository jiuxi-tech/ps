package com.jiuxi.shared.security.authorization;

import com.jiuxi.shared.security.authentication.JwtAuthenticationProvider.JwtUserPrincipal;
import com.jiuxi.security.sso.principal.KeycloakUserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 自定义权限投票器
 * 支持基于JWT和Keycloak用户主体的细粒度权限验证
 * 
 * @author Security Refactoring
 * @since Phase 4.2.2
 */
@Component
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class PermissionVoter implements AccessDecisionVoter<Object> {

    private static final Logger logger = LoggerFactory.getLogger(PermissionVoter.class);
    
    private static final String PERMISSION_ATTRIBUTE_PREFIX = "PERM_";
    private static final String HAS_PERMISSION_ATTRIBUTE_PREFIX = "hasPermission";

    @Autowired(required = false)
    private PermissionEvaluator customPermissionEvaluator;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        String attr = attribute.getAttribute();
        return attr != null && (
            attr.startsWith(PERMISSION_ATTRIBUTE_PREFIX) ||
            attr.startsWith(HAS_PERMISSION_ATTRIBUTE_PREFIX)
        );
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ACCESS_DENIED;
        }

        int result = ACCESS_ABSTAIN;
        
        for (ConfigAttribute attribute : attributes) {
            if (supports(attribute)) {
                result = ACCESS_DENIED;
                
                // 检查权限
                if (hasPermission(authentication, object, attribute.getAttribute())) {
                    return ACCESS_GRANTED;
                }
            }
        }
        
        return result;
    }

    /**
     * 检查用户是否具有指定权限
     */
    private boolean hasPermission(Authentication authentication, Object object, String permissionAttribute) {
        try {
            String permission = extractPermission(permissionAttribute);
            if (permission == null) {
                return false;
            }

            // 1. 优先使用自定义权限评估器
            if (customPermissionEvaluator != null) {
                return customPermissionEvaluator.hasPermission(authentication, object, permission);
            }

            // 2. 检查用户主体中的直接权限
            if (hasDirectPermission(authentication, permission)) {
                return true;
            }

            // 3. 基于角色的权限推导
            if (hasRoleBasedPermission(authentication, permission)) {
                return true;
            }

        } catch (Exception e) {
            logger.error("Error checking permission {}: {}", permissionAttribute, e.getMessage());
        }
        
        return false;
    }

    /**
     * 从权限属性中提取权限字符串
     */
    private String extractPermission(String permissionAttribute) {
        if (permissionAttribute.startsWith(PERMISSION_ATTRIBUTE_PREFIX)) {
            return permissionAttribute.substring(PERMISSION_ATTRIBUTE_PREFIX.length());
        }
        
        if (permissionAttribute.startsWith(HAS_PERMISSION_ATTRIBUTE_PREFIX)) {
            // 处理 hasPermission('user:read') 格式
            int start = permissionAttribute.indexOf('\'');
            int end = permissionAttribute.lastIndexOf('\'');
            if (start != -1 && end != -1 && start < end) {
                return permissionAttribute.substring(start + 1, end);
            }
        }
        
        return permissionAttribute;
    }

    /**
     * 检查用户是否有直接权限
     */
    private boolean hasDirectPermission(Authentication authentication, String permission) {
        Object principal = authentication.getPrincipal();
        
        // JWT用户主体
        if (principal instanceof JwtUserPrincipal) {
            JwtUserPrincipal jwtPrincipal = (JwtUserPrincipal) principal;
            List<String> permissions = jwtPrincipal.getPermissions();
            if (permissions != null) {
                return permissions.contains(permission) ||
                       permissions.stream().anyMatch(p -> matchesPermissionPattern(p, permission));
            }
        }
        
        // Keycloak用户主体
        if (principal instanceof KeycloakUserPrincipal) {
            KeycloakUserPrincipal keycloakPrincipal = (KeycloakUserPrincipal) principal;
            return keycloakPrincipal.hasPermission(permission);
        }
        
        return false;
    }

    /**
     * 基于角色的权限推导
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

        // 基于角色映射权限
        return mapRolesToPermissions(roles, permission);
    }

    /**
     * 角色到权限的映射
     */
    private boolean mapRolesToPermissions(List<String> roles, String permission) {
        for (String role : roles) {
            if (roleHasPermission(role, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查角色是否包含指定权限
     */
    private boolean roleHasPermission(String role, String permission) {
        String roleLower = role.toLowerCase();
        
        // 管理员拥有所有权限
        if ("admin".equals(roleLower) || "administrator".equals(roleLower)) {
            return true;
        }
        
        // 基于权限模式的角色权限映射
        switch (roleLower) {
            case "manager":
                return permission.matches("user:(read|write|update)") ||
                       permission.matches("report:(read|export)") ||
                       permission.matches("dashboard:.*");
            
            case "user":
                return permission.matches("user:read") ||
                       permission.matches("profile:(read|update)") ||
                       permission.matches("self:.*");
            
            case "operator":
                return permission.matches("data:(read|write)") ||
                       permission.matches("export:.*") ||
                       permission.matches("system:(monitor|status)");
            
            case "viewer":
                return permission.matches(".*:read") ||
                       permission.matches("dashboard:view");
            
            default:
                return false;
        }
    }

    /**
     * 权限模式匹配
     */
    private boolean matchesPermissionPattern(String pattern, String permission) {
        // 支持通配符匹配
        if (pattern.equals("*")) {
            return true;
        }
        
        if (pattern.endsWith("*")) {
            String prefix = pattern.substring(0, pattern.length() - 1);
            return permission.startsWith(prefix);
        }
        
        if (pattern.startsWith("*")) {
            String suffix = pattern.substring(1);
            return permission.endsWith(suffix);
        }
        
        // 支持正则表达式匹配
        try {
            return permission.matches(pattern);
        } catch (Exception e) {
            // 如果正则表达式有问题，则进行精确匹配
            return permission.equals(pattern);
        }
    }

    /**
     * 检查用户是否具有任意指定权限
     */
    public boolean hasAnyPermission(Authentication authentication, String... permissions) {
        if (authentication == null || !authentication.isAuthenticated() || 
            permissions == null || permissions.length == 0) {
            return false;
        }
        
        for (String permission : permissions) {
            if (hasPermission(authentication, null, permission)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 检查用户是否具有所有指定权限
     */
    public boolean hasAllPermissions(Authentication authentication, String... permissions) {
        if (authentication == null || !authentication.isAuthenticated() || 
            permissions == null || permissions.length == 0) {
            return false;
        }
        
        for (String permission : permissions) {
            if (!hasPermission(authentication, null, permission)) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * 获取用户的所有权限
     */
    public List<String> getUserPermissions(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof JwtUserPrincipal) {
            return ((JwtUserPrincipal) principal).getPermissions();
        }
        
        if (principal instanceof KeycloakUserPrincipal) {
            return ((KeycloakUserPrincipal) principal).getPermissions();
        }
        
        return null;
    }
}