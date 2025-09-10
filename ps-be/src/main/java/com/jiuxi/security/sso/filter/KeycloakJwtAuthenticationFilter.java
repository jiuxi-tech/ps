package com.jiuxi.security.sso.filter;

import com.jiuxi.security.sso.config.KeycloakSsoProperties;
import com.jiuxi.security.sso.token.KeycloakJwtToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Keycloak JWT 认证过滤器
 * 
 * 负责从请求中提取 JWT Token 并进行认证
 * 
 * @author SSO Integration
 * @since 2.2.2
 */
@Component
public class KeycloakJwtAuthenticationFilter extends AuthenticatingFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(KeycloakJwtAuthenticationFilter.class);
    
    /**
     * Authorization 头名称
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";
    
    /**
     * Bearer Token 前缀
     */
    private static final String BEARER_PREFIX = "Bearer ";
    
    @Autowired
    private KeycloakSsoProperties properties;
    
    /**
     * 创建认证 Token
     * 
     * @param request 请求
     * @param response 响应
     * @return 认证 Token
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // 从 Authorization 头获取 JWT Token
        String token = getJwtTokenFromRequest(httpRequest);
        
        if (StringUtils.hasText(token)) {
            logger.debug("Found JWT token in Authorization header");
            return new KeycloakJwtToken(token);
        }
        
        // 从 Nginx 代理头获取用户信息（备用方案）
        String userId = httpRequest.getHeader(properties.getUserHeader().getUserIdHeader());
        String username = httpRequest.getHeader(properties.getUserHeader().getUsernameHeader());
        
        if (StringUtils.hasText(userId) && StringUtils.hasText(username)) {
            logger.debug("Found user info in proxy headers: {} ({})", username, userId);
            // 创建一个特殊的 Token 表示通过代理认证
            return new KeycloakJwtToken("proxy-authenticated", username);
        }
        
        logger.debug("No JWT token or proxy authentication found in request");
        return null;
    }
    
    /**
     * 从请求中提取 JWT Token
     * 
     * @param request HTTP 请求
     * @return JWT Token 字符串
     */
    private String getJwtTokenFromRequest(HttpServletRequest request) {
        // 从 Authorization 头获取
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length()).trim();
        }
        
        // 从查询参数获取（不推荐，但作为备用）
        String tokenParam = request.getParameter("access_token");
        if (StringUtils.hasText(tokenParam)) {
            return tokenParam;
        }
        
        return null;
    }
    
    /**
     * 当认证失败时的处理
     * 
     * @param token 认证 Token
     * @param e 认证异常
     * @param request 请求
     * @param response 响应
     * @return 是否继续处理
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, 
                                   ServletRequest request, ServletResponse response) {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        logger.error("JWT authentication failed for request: {} {}, error: {}", 
                    httpRequest.getMethod(), httpRequest.getRequestURI(), e.getMessage());
        
        try {
            // 返回 401 未授权
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write(
                "{\"error\":\"unauthorized\",\"message\":\"Authentication failed: " + 
                e.getMessage().replace("\"", "\\\"") + "\"}"
            );
            httpResponse.getWriter().flush();
        } catch (IOException ioException) {
            logger.error("Error writing authentication failure response", ioException);
        }
        
        return false; // 停止处理
    }
    
    /**
     * 当认证成功时的处理
     * 
     * @param token 认证 Token
     * @param subject 主体
     * @param request 请求
     * @param response 响应
     * @return 是否继续处理
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, 
                                   ServletRequest request, ServletResponse response) throws Exception {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        logger.debug("JWT authentication successful for request: {} {}", 
                    httpRequest.getMethod(), httpRequest.getRequestURI());
        
        return true; // 继续处理
    }
    
    /**
     * 判断是否允许访问
     * 
     * @param request 请求
     * @param response 响应
     * @param mappedValue 映射值
     * @return 是否允许访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 检查是否已经认证
        Subject subject = getSubject(request, response);
        return subject.isAuthenticated();
    }
    
    /**
     * 当访问被拒绝时的处理
     * 
     * @param request 请求
     * @param response 响应
     * @return 是否继续处理
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 检查是否能创建认证Token
        AuthenticationToken token = createToken(request, response);
        if (token == null) {
            // 如果无法创建Token（比如匿名访问），直接拒绝访问
            // 让Shiro的过滤器链决定是否允许匿名访问
            return false;
        }
        
        // 尝试执行登录
        return executeLogin(request, response);
    }
    
    /**
     * 预处理请求
     * 
     * @param request 请求
     * @param response 响应
     * @return 是否继续处理
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        System.out.println("=== KeycloakJwtAuthenticationFilter.preHandle 被调用 ===");
        System.out.println("请求路径: " + httpRequest.getRequestURI());
        System.out.println("请求方法: " + httpRequest.getMethod());
        System.out.println("Authorization头: " + httpRequest.getHeader("Authorization"));
        
        // 对于 OPTIONS 请求，直接放行
        if ("OPTIONS".equals(httpRequest.getMethod())) {
            System.out.println("OPTIONS请求，直接放行");
            return true;
        }
        
        boolean result = super.preHandle(request, response);
        System.out.println("super.preHandle返回结果: " + result);
        return result;
    }
}