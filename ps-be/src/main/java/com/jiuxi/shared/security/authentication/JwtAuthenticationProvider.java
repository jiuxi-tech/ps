package com.jiuxi.shared.security.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jiuxi.shared.security.config.JwtSecurityConfig.JwtUtils;
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

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JWT认证提供者
 * 负责验证JWT Token并创建Spring Security的Authentication对象
 * 
 * @author Security Refactoring
 * @since Phase 4.2.2
 */
@Component
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    @Autowired
    private SecurityProperties securityProperties;
    
    @Autowired
    private Algorithm jwtAlgorithm;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    private JWTVerifier jwtVerifier;

    @PostConstruct
    public void init() {
        // 初始化JWT验证器
        this.jwtVerifier = JWT.require(jwtAlgorithm)
            .acceptLeeway(securityProperties.getJwt().getClockSkew())
            .build();
        
        logger.info("JWT Authentication Provider initialized");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof JwtAuthenticationToken)) {
            return null;
        }

        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        String token = jwtAuth.getToken();

        if (!StringUtils.hasText(token)) {
            throw new BadCredentialsException("JWT token is empty");
        }

        try {
            // 检查缓存
            Object cachedResult = jwtUtils.getCachedTokenResult(token);
            if (cachedResult instanceof JwtUserPrincipal) {
                JwtUserPrincipal cachedPrincipal = (JwtUserPrincipal) cachedResult;
                if (!cachedPrincipal.isExpired()) {
                    return createSuccessAuthentication(cachedPrincipal, token);
                } else {
                    // 移除过期的缓存
                    jwtUtils.removeCachedTokenResult(token);
                }
            }

            // 验证并解析Token
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            JwtUserPrincipal principal = parseTokenToPrincipal(decodedJWT, token);

            // 缓存解析结果
            jwtUtils.cacheTokenResult(token, principal);

            return createSuccessAuthentication(principal, token);

        } catch (JWTVerificationException e) {
            logger.error("JWT verification failed: {}", e.getMessage());
            throw new BadCredentialsException("Invalid JWT token", e);
        } catch (Exception e) {
            logger.error("Error authenticating JWT token: {}", e.getMessage());
            throw new BadCredentialsException("JWT authentication failed", e);
        }
    }

    /**
     * 解析JWT Token为用户主体
     */
    private JwtUserPrincipal parseTokenToPrincipal(DecodedJWT decodedJWT, String token) {
        JwtUserPrincipal principal = new JwtUserPrincipal();
        
        // 基本用户信息
        principal.setUserId(decodedJWT.getSubject());
        principal.setUsername(decodedJWT.getClaim("preferred_username").asString());
        principal.setEmail(decodedJWT.getClaim("email").asString());
        principal.setName(decodedJWT.getClaim("name").asString());
        principal.setToken(token);
        
        // 设置过期时间
        Date expiresAt = decodedJWT.getExpiresAt();
        if (expiresAt != null) {
            principal.setExpirationTime(expiresAt.getTime());
        }
        
        // 解析角色信息
        List<String> roles = extractRoles(decodedJWT);
        principal.setRoles(roles);
        
        // 解析权限信息
        List<String> permissions = extractPermissions(decodedJWT);
        principal.setPermissions(permissions);
        
        // 设置扩展属性
        Map<String, Object> attributes = new HashMap<>();
        decodedJWT.getClaims().forEach((key, claim) -> {
            if (!isStandardClaim(key)) {
                attributes.put(key, claim.as(Object.class));
            }
        });
        principal.setAttributes(attributes);
        
        return principal;
    }

    /**
     * 提取角色信息
     */
    @SuppressWarnings("unchecked")
    private List<String> extractRoles(DecodedJWT decodedJWT) {
        List<String> roles = new ArrayList<>();
        
        // 从roles声明提取
        List<String> rolesFromClaim = decodedJWT.getClaim("roles").asList(String.class);
        if (rolesFromClaim != null) {
            roles.addAll(rolesFromClaim);
        }
        
        // 从scope提取（如果有）
        String scope = decodedJWT.getClaim("scope").asString();
        if (StringUtils.hasText(scope)) {
            String[] scopes = scope.split(" ");
            for (String s : scopes) {
                if (s.startsWith("role:")) {
                    roles.add(s.substring(5));
                }
            }
        }
        
        return roles;
    }

    /**
     * 提取权限信息
     */
    private List<String> extractPermissions(DecodedJWT decodedJWT) {
        List<String> permissions = new ArrayList<>();
        
        // 从permissions声明提取
        List<String> permsFromClaim = decodedJWT.getClaim("permissions").asList(String.class);
        if (permsFromClaim != null) {
            permissions.addAll(permsFromClaim);
        }
        
        // 从scope提取权限
        String scope = decodedJWT.getClaim("scope").asString();
        if (StringUtils.hasText(scope)) {
            String[] scopes = scope.split(" ");
            for (String s : scopes) {
                if (s.startsWith("perm:")) {
                    permissions.add(s.substring(5));
                } else if (!s.startsWith("role:")) {
                    // 其他scope作为权限处理
                    permissions.add(s);
                }
            }
        }
        
        return permissions;
    }

    /**
     * 检查是否为标准JWT声明
     */
    private boolean isStandardClaim(String claimName) {
        return Arrays.asList(
            "iss", "sub", "aud", "exp", "nbf", "iat", "jti",
            "preferred_username", "email", "name", "given_name", "family_name",
            "roles", "permissions", "scope", "email_verified"
        ).contains(claimName);
    }

    /**
     * 创建认证成功的Authentication对象
     */
    private Authentication createSuccessAuthentication(JwtUserPrincipal principal, String token) {
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
        
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(token, principal, authorities);
        authToken.setAuthenticated(true);
        
        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * JWT用户主体类
     */
    public static class JwtUserPrincipal {
        private String userId;
        private String username;
        private String email;
        private String name;
        private String token;
        private List<String> roles;
        private List<String> permissions;
        private Map<String, Object> attributes;
        private long expirationTime;

        public boolean isExpired() {
            return expirationTime > 0 && System.currentTimeMillis() > expirationTime;
        }

        // Getters and setters
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public List<String> getPermissions() {
            return permissions;
        }

        public void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        public long getExpirationTime() {
            return expirationTime;
        }

        public void setExpirationTime(long expirationTime) {
            this.expirationTime = expirationTime;
        }
    }
}