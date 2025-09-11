package com.jiuxi.shared.security.authentication;

import com.jiuxi.security.sso.principal.KeycloakUserPrincipal;
import com.jiuxi.security.sso.service.KeycloakJwtService;
import com.jiuxi.shared.security.config.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Keycloak认证提供者
 * 负责验证Keycloak JWT Token并创建Spring Security的Authentication对象
 * 
 * @author Security Refactoring
 * @since Phase 4.2.2
 */
@Component
@ConditionalOnProperty(name = "ps.security.keycloak.enabled", havingValue = "true")
public class KeycloakAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakAuthenticationProvider.class);

    @Autowired
    private SecurityProperties securityProperties;
    
    @Autowired
    private KeycloakJwtService keycloakJwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof JwtAuthenticationToken)) {
            return null;
        }

        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        String token = jwtAuth.getToken();

        if (!StringUtils.hasText(token)) {
            throw new BadCredentialsException("Keycloak JWT token is empty");
        }

        try {
            // 使用Keycloak JWT服务验证和解析Token
            KeycloakUserPrincipal principal = keycloakJwtService.verifyAndParseToken(token);
            
            if (principal == null) {
                throw new BadCredentialsException("Invalid Keycloak JWT token");
            }

            // 检查token是否过期
            if (principal.isTokenExpired()) {
                throw new BadCredentialsException("Keycloak JWT token has expired");
            }

            return createSuccessAuthentication(principal, token);

        } catch (RuntimeException e) {
            logger.error("Keycloak JWT authentication failed: {}", e.getMessage());
            throw new BadCredentialsException("Keycloak authentication failed", e);
        }
    }

    /**
     * 创建认证成功的Authentication对象
     */
    private Authentication createSuccessAuthentication(KeycloakUserPrincipal principal, String token) {
        // 创建GrantedAuthority集合
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // 添加角色权限（以ROLE_前缀）
        if (principal.getRoles() != null) {
            authorities.addAll(
                principal.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList())
            );
        }
        
        // 添加具体权限
        if (principal.getPermissions() != null) {
            authorities.addAll(
                principal.getPermissions().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList())
            );
        }
        
        // 创建自定义的Keycloak认证Token
        KeycloakAuthenticationToken authToken = new KeycloakAuthenticationToken(token, principal, authorities);
        authToken.setAuthenticated(true);
        
        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 支持JWT认证Token，但优先级高于普通JWT认证提供者
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * Keycloak认证令牌
     * 扩展JwtAuthenticationToken以支持Keycloak特性
     */
    public static class KeycloakAuthenticationToken extends JwtAuthenticationToken {

        private final KeycloakUserPrincipal keycloakPrincipal;

        public KeycloakAuthenticationToken(String token) {
            super(token);
            this.keycloakPrincipal = null;
        }

        public KeycloakAuthenticationToken(String token, KeycloakUserPrincipal principal, 
                                         List<GrantedAuthority> authorities) {
            super(token, principal, authorities);
            this.keycloakPrincipal = principal;
        }

        @Override
        public Object getPrincipal() {
            return keycloakPrincipal != null ? keycloakPrincipal : super.getPrincipal();
        }

        public KeycloakUserPrincipal getKeycloakPrincipal() {
            return keycloakPrincipal;
        }

        /**
         * 检查用户是否具有指定角色
         */
        public boolean hasRole(String role) {
            return keycloakPrincipal != null && keycloakPrincipal.hasRole(role);
        }

        /**
         * 检查用户是否具有指定权限
         */
        public boolean hasPermission(String permission) {
            return keycloakPrincipal != null && keycloakPrincipal.hasPermission(permission);
        }

        /**
         * 获取用户属性
         */
        public Object getAttribute(String name) {
            if (keycloakPrincipal != null && keycloakPrincipal.getAttributes() != null) {
                return keycloakPrincipal.getAttributes().get(name);
            }
            return null;
        }
    }
}