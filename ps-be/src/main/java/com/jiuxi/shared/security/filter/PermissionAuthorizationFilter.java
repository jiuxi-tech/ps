package com.jiuxi.shared.security.filter;

import com.jiuxi.shared.security.authorization.PermissionEvaluator;
import com.jiuxi.shared.security.config.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * 权限授权过滤器
 * 负责在请求处理前进行细粒度的权限检查
 * 
 * @author Security Refactoring
 * @since Phase 4.2.2
 */
public class PermissionAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(PermissionAuthorizationFilter.class);

    private final SecurityProperties securityProperties;
    private final PermissionEvaluator permissionEvaluator;

    public PermissionAuthorizationFilter(SecurityProperties securityProperties,
                                       PermissionEvaluator permissionEvaluator) {
        this.securityProperties = securityProperties;
        this.permissionEvaluator = permissionEvaluator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

        // 检查是否需要跳过授权
        if (shouldSkipAuthorization(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 获取当前认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            // 如果没有认证信息，让后续的安全框架处理
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 执行权限检查
            if (!hasPermissionToAccess(request, authentication)) {
                handleAccessDenied(request, response);
                return;
            }

            logger.debug("Permission check passed for user: {} accessing: {}", 
                getUserIdentifier(authentication), request.getRequestURI());
            
        } catch (Exception e) {
            logger.error("Error during permission evaluation: {}", e.getMessage());
            handleAuthorizationError(request, response, e);
            return;
        }

        // 权限检查通过，继续处理请求
        filterChain.doFilter(request, response);
    }

    /**
     * 检查用户是否有权限访问当前资源
     */
    private boolean hasPermissionToAccess(HttpServletRequest request, Authentication authentication) {
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        
        // 构造权限标识
        String permission = buildPermissionIdentifier(requestURI, httpMethod);
        
        // 如果没有权限评估器，使用默认策略
        if (permissionEvaluator == null) {
            return hasDefaultPermission(authentication, permission);
        }
        
        // 使用权限评估器进行详细检查
        return permissionEvaluator.hasPermission(authentication, request, permission);
    }

    /**
     * 构造权限标识符
     */
    private String buildPermissionIdentifier(String requestURI, String httpMethod) {
        // 移除上下文路径和版本信息
        String cleanURI = cleanRequestURI(requestURI);
        
        // 根据HTTP方法构造权限
        String operation = mapHttpMethodToOperation(httpMethod);
        
        // 构造权限字符串：resource:operation 或 直接使用URI
        if ("read".equals(operation)) {
            return cleanURI; // 读操作使用URI作为权限
        } else {
            return cleanURI + ":" + operation;
        }
    }

    /**
     * 清理请求URI
     */
    private String cleanRequestURI(String requestURI) {
        // 移除应用上下文路径
        if (requestURI.startsWith("/ps-be")) {
            requestURI = requestURI.substring(6);
        }
        
        // 移除版本前缀
        if (requestURI.startsWith("/v1") || requestURI.startsWith("/v2")) {
            requestURI = requestURI.substring(3);
        }
        
        // 移除API前缀
        if (requestURI.startsWith("/api")) {
            requestURI = requestURI.substring(4);
        }
        
        // 确保以/开头
        if (!requestURI.startsWith("/")) {
            requestURI = "/" + requestURI;
        }
        
        return requestURI;
    }

    /**
     * 将HTTP方法映射到操作类型
     */
    private String mapHttpMethodToOperation(String httpMethod) {
        switch (httpMethod.toUpperCase()) {
            case "GET":
                return "read";
            case "POST":
                return "create";
            case "PUT":
                return "update";
            case "DELETE":
                return "delete";
            case "PATCH":
                return "update";
            case "HEAD":
            case "OPTIONS":
                return "read";
            default:
                return "access";
        }
    }

    /**
     * 默认权限检查（当没有权限评估器时）
     */
    private boolean hasDefaultPermission(Authentication authentication, String permission) {
        // 超级管理员有所有权限
        if (hasAdminRole(authentication)) {
            return true;
        }
        
        // 基于URI的简单权限检查
        return hasBasicAccess(authentication, permission);
    }

    /**
     * 检查是否为管理员角色
     */
    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> 
                    "ROLE_ADMIN".equals(auth.getAuthority()) || 
                    "ROLE_ADMINISTRATOR".equals(auth.getAuthority()) ||
                    "admin".equals(auth.getAuthority())
                );
    }

    /**
     * 基本访问权限检查
     */
    private boolean hasBasicAccess(Authentication authentication, String permission) {
        // 简单的基于路径的权限检查
        if (permission.startsWith("/user") || permission.startsWith("/profile")) {
            return authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().contains("USER") || 
                                     auth.getAuthority().contains("user"));
        }
        
        if (permission.startsWith("/admin")) {
            return hasAdminRole(authentication);
        }
        
        // 默认允许访问（可根据需要调整）
        return true;
    }

    /**
     * 检查是否应该跳过授权检查
     */
    private boolean shouldSkipAuthorization(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        
        // 检查授权是否被禁用
        if (!securityProperties.getAuthorization().isEnabled()) {
            return true;
        }
        
        // 检查排除路径
        String[] excludePaths = securityProperties.getAuthorization().getExcludePaths();
        if (excludePaths != null) {
            return Arrays.stream(excludePaths)
                    .anyMatch(pattern -> pathMatches(requestURI, pattern));
        }
        
        return false;
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
     * 处理访问被拒绝的情况
     */
    private void handleAccessDenied(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userInfo = getUserIdentifier(auth);
        
        logger.warn("Access denied for user: {} accessing: {} {}", 
            userInfo, request.getMethod(), request.getRequestURI());
        
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        
        String errorMessage = String.format(
            "{\"code\":%d,\"message\":\"%s\",\"path\":\"%s\",\"timestamp\":%d}",
            HttpServletResponse.SC_FORBIDDEN,
            "权限不足，无法访问该资源",
            request.getRequestURI(),
            System.currentTimeMillis()
        );
        
        response.getWriter().write(errorMessage);
        response.getWriter().flush();
    }

    /**
     * 处理授权过程中的错误
     */
    private void handleAuthorizationError(HttpServletRequest request, HttpServletResponse response,
                                        Exception exception) throws IOException {
        logger.error("Authorization error for request: {} {}", 
            request.getMethod(), request.getRequestURI(), exception);
        
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json;charset=UTF-8");
        
        String errorMessage = String.format(
            "{\"code\":%d,\"message\":\"%s\",\"path\":\"%s\",\"timestamp\":%d}",
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            "权限验证过程中发生错误",
            request.getRequestURI(),
            System.currentTimeMillis()
        );
        
        response.getWriter().write(errorMessage);
        response.getWriter().flush();
    }

    /**
     * 获取用户标识符
     */
    private String getUserIdentifier(Authentication authentication) {
        if (authentication == null) {
            return "anonymous";
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof com.jiuxi.shared.security.authentication.JwtAuthenticationProvider.JwtUserPrincipal) {
            return ((com.jiuxi.shared.security.authentication.JwtAuthenticationProvider.JwtUserPrincipal) principal).getUsername();
        }
        
        if (principal instanceof com.jiuxi.security.sso.principal.KeycloakUserPrincipal) {
            return ((com.jiuxi.security.sso.principal.KeycloakUserPrincipal) principal).getUsername();
        }
        
        return String.valueOf(principal);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 如果授权被完全禁用，则不应该执行此过滤器
        return !securityProperties.getAuthorization().isEnabled();
    }
}