package com.jiuxi.shared.security.filter;

import com.jiuxi.shared.security.authentication.JwtAuthenticationToken;
import com.jiuxi.shared.security.config.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * JWT认证过滤器
 * 负责从HTTP请求中提取JWT Token并进行认证
 * 
 * @author Security Refactoring
 * @since Phase 4.2.2
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;
    private final SecurityProperties securityProperties;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, 
                                 SecurityProperties securityProperties) {
        this.authenticationManager = authenticationManager;
        this.securityProperties = securityProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 检查是否需要跳过认证
        if (shouldSkipAuthentication(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 提取JWT Token
            String token = extractToken(request);
            
            if (StringUtils.hasText(token)) {
                // 创建认证Token
                JwtAuthenticationToken authToken = new JwtAuthenticationToken(token);
                
                // 执行认证
                Authentication authentication = authenticationManager.authenticate(authToken);
                
                // 设置认证结果到SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                logger.debug("JWT authentication successful for token: {}...", 
                    token.length() > 10 ? token.substring(0, 10) : token);
            } else {
                // 没有Token的情况
                if (securityProperties.getAuthentication().isEnabled()) {
                    logger.debug("No JWT token found in request to: {}", request.getRequestURI());
                    // 如果认证是必需的，但没有token，让后续的认证逻辑处理
                    SecurityContextHolder.clearContext();
                }
            }
            
        } catch (AuthenticationException e) {
            logger.error("JWT authentication failed: {}", e.getMessage());
            
            // 清除SecurityContext
            SecurityContextHolder.clearContext();
            
            // 可以选择是否立即返回错误，还是继续传递给后续处理
            if (securityProperties.getAuthentication().isEnabled()) {
                handleAuthenticationFailure(request, response, e);
                return;
            }
        } catch (Exception e) {
            logger.error("JWT authentication error: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }
        
        // 继续过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * 从HTTP请求中提取JWT Token
     */
    private String extractToken(HttpServletRequest request) {
        // 1. 从Authorization头提取
        String token = extractTokenFromHeader(request);
        if (StringUtils.hasText(token)) {
            return token;
        }
        
        // 2. 从请求参数提取（不推荐，但兼容某些场景）
        token = request.getParameter("token");
        if (StringUtils.hasText(token)) {
            return token;
        }
        
        // 3. 从自定义头提取（如果配置了）
        String customHeader = securityProperties.getJwt().getHeader();
        if (StringUtils.hasText(customHeader) && !customHeader.equals("Authorization")) {
            token = request.getHeader(customHeader);
            if (StringUtils.hasText(token)) {
                // 移除可能的前缀
                String prefix = securityProperties.getJwt().getPrefix();
                if (StringUtils.hasText(prefix) && token.startsWith(prefix)) {
                    return token.substring(prefix.length()).trim();
                }
                return token;
            }
        }
        
        return null;
    }

    /**
     * 从Authorization头提取Token
     */
    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(securityProperties.getJwt().getHeader());
        if (!StringUtils.hasText(header)) {
            return null;
        }
        
        String prefix = securityProperties.getJwt().getPrefix();
        if (StringUtils.hasText(prefix)) {
            if (header.startsWith(prefix)) {
                return header.substring(prefix.length()).trim();
            } else {
                // 如果头存在但没有正确的前缀，可能不是JWT token
                return null;
            }
        } else {
            // 如果没有配置前缀，直接返回头的值
            return header;
        }
    }

    /**
     * 检查是否应该跳过认证
     */
    private boolean shouldSkipAuthentication(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        
        // 检查排除路径
        String[] excludePaths = securityProperties.getAuthentication().getExcludePaths();
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
     * 处理认证失败
     */
    private void handleAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, 
                                           AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        
        String errorMessage = String.format(
            "{\"code\":%d,\"message\":\"%s\",\"path\":\"%s\",\"timestamp\":%d}",
            HttpServletResponse.SC_UNAUTHORIZED,
            "认证失败：" + exception.getMessage(),
            request.getRequestURI(),
            System.currentTimeMillis()
        );
        
        response.getWriter().write(errorMessage);
        response.getWriter().flush();
    }

    /**
     * 检查是否已经认证
     */
    private boolean isAlreadyAuthenticated() {
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        return existingAuth != null && existingAuth.isAuthenticated();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 如果认证被完全禁用，则不应该执行此过滤器
        if (!securityProperties.getAuthentication().isEnabled()) {
            return true;
        }
        
        // 如果已经认证，可以选择跳过（但通常JWT是无状态的，每次都需要验证）
        return false;
    }
}