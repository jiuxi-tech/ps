package com.jiuxi.shared.security.authorization;

import com.jiuxi.shared.security.authentication.JwtAuthenticationProvider.JwtUserPrincipal;
import com.jiuxi.security.sso.principal.KeycloakUserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 自定义角色投票器
 * 支持基于JWT和Keycloak用户主体的角色验证
 * 
 * @author Security Refactoring
 * @since Phase 4.2.2
 */
@Component
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class RoleVoter implements AccessDecisionVoter<Object> {

    private static final Logger logger = LoggerFactory.getLogger(RoleVoter.class);
    
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String ROLE_ATTRIBUTE_PREFIX = "ROLE_";

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute.getAttribute() != null && 
               attribute.getAttribute().startsWith(ROLE_ATTRIBUTE_PREFIX);
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
                
                // 检查角色权限
                if (hasRole(authentication, attribute.getAttribute())) {
                    return ACCESS_GRANTED;
                }
            }
        }
        
        return result;
    }

    /**
     * 检查用户是否具有指定角色
     */
    private boolean hasRole(Authentication authentication, String role) {
        // 移除ROLE_前缀以获取实际角色名
        String roleWithoutPrefix = role;
        if (role.startsWith(ROLE_PREFIX)) {
            roleWithoutPrefix = role.substring(ROLE_PREFIX.length());
        }
        
        try {
            // 1. 检查Spring Security的GrantedAuthority
            if (hasGrantedAuthority(authentication, role)) {
                return true;
            }
            
            // 2. 检查JWT用户主体中的角色
            if (hasJwtRole(authentication, roleWithoutPrefix)) {
                return true;
            }
            
            // 3. 检查Keycloak用户主体中的角色
            if (hasKeycloakRole(authentication, roleWithoutPrefix)) {
                return true;
            }
            
        } catch (Exception e) {
            logger.error("Error checking role {}: {}", role, e.getMessage());
        }
        
        return false;
    }

    /**
     * 检查Spring Security GrantedAuthority中是否有角色
     */
    private boolean hasGrantedAuthority(Authentication authentication, String role) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            return false;
        }
        
        for (GrantedAuthority authority : authorities) {
            String authorityName = authority.getAuthority();
            if (role.equals(authorityName)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 检查JWT用户主体中是否有角色
     */
    private boolean hasJwtRole(Authentication authentication, String role) {
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof JwtUserPrincipal)) {
            return false;
        }
        
        JwtUserPrincipal jwtPrincipal = (JwtUserPrincipal) principal;
        List<String> roles = jwtPrincipal.getRoles();
        
        if (roles == null || roles.isEmpty()) {
            return false;
        }
        
        // 支持大小写不敏感的角色匹配
        return roles.stream().anyMatch(r -> 
            r.equalsIgnoreCase(role) || 
            r.equalsIgnoreCase(role.toLowerCase()) ||
            r.equalsIgnoreCase(role.toUpperCase())
        );
    }

    /**
     * 检查Keycloak用户主体中是否有角色
     */
    private boolean hasKeycloakRole(Authentication authentication, String role) {
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof KeycloakUserPrincipal)) {
            return false;
        }
        
        KeycloakUserPrincipal keycloakPrincipal = (KeycloakUserPrincipal) principal;
        
        // 使用Keycloak主体的hasRole方法
        return keycloakPrincipal.hasRole(role) || 
               keycloakPrincipal.hasRole(role.toLowerCase()) ||
               keycloakPrincipal.hasRole(role.toUpperCase());
    }

    /**
     * 检查用户是否具有任意指定角色
     */
    public boolean hasAnyRole(Authentication authentication, String... roles) {
        if (authentication == null || !authentication.isAuthenticated() || 
            roles == null || roles.length == 0) {
            return false;
        }
        
        for (String role : roles) {
            if (hasRole(authentication, role)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 检查用户是否具有所有指定角色
     */
    public boolean hasAllRoles(Authentication authentication, String... roles) {
        if (authentication == null || !authentication.isAuthenticated() || 
            roles == null || roles.length == 0) {
            return false;
        }
        
        for (String role : roles) {
            if (!hasRole(authentication, role)) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * 获取用户的所有角色
     */
    public List<String> getUserRoles(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof JwtUserPrincipal) {
            return ((JwtUserPrincipal) principal).getRoles();
        }
        
        if (principal instanceof KeycloakUserPrincipal) {
            return ((KeycloakUserPrincipal) principal).getRoles();
        }
        
        return null;
    }
}