package com.jiuxi.security.sso.controller;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.security.core.entity.vo.AccountVO;
import com.jiuxi.security.core.service.AccountService;
import com.jiuxi.security.core.service.TopinfoSecurityCommonService;
import com.jiuxi.security.sso.config.KeycloakSsoProperties;
import com.jiuxi.security.sso.principal.KeycloakUserPrincipal;
import com.jiuxi.security.sso.service.KeycloakOAuth2Service;
import com.jiuxi.admin.core.service.TpTimeRuleService;
import com.jiuxi.admin.core.service.TpKeycloakAccountService;
import com.jiuxi.admin.core.service.TpSystemConfigService;
import com.jiuxi.admin.core.bean.entity.TpKeycloakAccount;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SSO 控制器
 * 
 * 处理 SSO 登录、登出和用户信息获取
 * 
 * @author SSO Integration
 * @since 2.2.2
 */
@RestController
@RequestMapping("/api/sso")
@ConditionalOnProperty(name = "keycloak.sso.enabled", havingValue = "true", matchIfMissing = false)
public class SsoController {
    
    private static final Logger logger = LoggerFactory.getLogger(SsoController.class);
    
    @Autowired
    private KeycloakSsoProperties properties;
    
    @Autowired
    private KeycloakOAuth2Service oAuth2Service;
    
    @Autowired
    @Qualifier("pwdAccountService")
    private AccountService accountService;
    
    @Autowired
    private TopinfoSecurityCommonService topinfoSecurityCommonService;
    
    @Autowired
    private TpTimeRuleService tpTimeRuleService;
    
    @Autowired
    private TpKeycloakAccountService tpKeycloakAccountService;
    
    @Autowired
    private TpSystemConfigService tpSystemConfigService;
    
    public SsoController() {
        System.out.println("SsoController 已创建！");
        logger.info("SsoController 已创建！");
    }
    
    /**
     * 获取当前用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        
        if (!subject.isAuthenticated()) {
            return ResponseEntity.status(401).body(createErrorResponse("用户未认证"));
        }
        
        Object principal = subject.getPrincipal();
        if (!(principal instanceof KeycloakUserPrincipal)) {
            return ResponseEntity.status(500).body(createErrorResponse("无效的用户主体类型"));
        }
        
        KeycloakUserPrincipal userPrincipal = (KeycloakUserPrincipal) principal;
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", userPrincipal.getUserId());
        userInfo.put("username", userPrincipal.getUsername());
        userInfo.put("email", userPrincipal.getEmail());
        userInfo.put("name", userPrincipal.getName());
        userInfo.put("roles", userPrincipal.getRoles());
        userInfo.put("permissions", userPrincipal.getPermissions());
        userInfo.put("attributes", userPrincipal.getAttributes());
        userInfo.put("tokenExpired", userPrincipal.isTokenExpired());
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", userInfo);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 检查认证状态
     * 
     * @return 认证状态
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAuthStatus() {
        Subject subject = SecurityUtils.getSubject();
        
        Map<String, Object> status = new HashMap<>();
        status.put("authenticated", subject.isAuthenticated());
        status.put("remembered", subject.isRemembered());
        
        if (subject.isAuthenticated()) {
            Object principal = subject.getPrincipal();
            if (principal instanceof KeycloakUserPrincipal) {
                KeycloakUserPrincipal userPrincipal = (KeycloakUserPrincipal) principal;
                status.put("username", userPrincipal.getUsername());
                status.put("tokenExpired", userPrincipal.isTokenExpired());
            }
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", status);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 测试获取用户可访问的Keycloak客户端列表
     * 
     * @return 客户端列表
     */
    @GetMapping("/test_clients")
    public JsonResponse testGetUserClients(HttpServletRequest request) {
        try {
            // 从请求头获取用户token
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return JsonResponse.buildFailure("缺少认证token");
            }
            
            String userToken = authHeader.substring(7);
            
            // 获取当前用户信息
            KeycloakUserPrincipal userPrincipal = (KeycloakUserPrincipal) request.getUserPrincipal();
            if (userPrincipal == null) {
                return JsonResponse.buildFailure("用户未认证");
            }
            
            // 使用用户token调用Keycloak Admin API获取客户端列表
            String keycloakAdminUrl = "http://localhost:8180/admin/realms/ps-realm/clients";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(userPrincipal.getToken());
            headers.set("Content-Type", "application/json");
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            try {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Object[]> response = restTemplate.exchange(
                    keycloakAdminUrl,
                    HttpMethod.GET,
                    entity,
                    Object[].class
                );
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    // 格式化数据以匹配前端期望的格式
                    List<Map<String, Object>> formattedClients = formatClientData(Arrays.asList(response.getBody()));
                    return JsonResponse.buildSuccess(formattedClients);
                }
            } catch (Exception e) {
                logger.warn("调用Keycloak Admin API失败，返回默认客户端列表: " + e.getMessage());
            }
            
            // 如果API调用失败，返回默认客户端列表
            return JsonResponse.buildSuccess(getDefaultClients());
            
        } catch (Exception e) {
            logger.error("获取Keycloak客户端列表失败", e);
            return JsonResponse.buildFailure("获取客户端列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试接口 - 不需要认证的客户端列表
     */
    @GetMapping("/test_clients_no_auth")
    public JsonResponse testGetUserClientsNoAuth() {
        try {
            // 直接返回默认客户端列表，不需要认证
            List<Map<String, Object>> defaultClients = getDefaultClients();
            return JsonResponse.buildSuccess(defaultClients);
        } catch (Exception e) {
            logger.error("获取默认客户端列表失败", e);
            return JsonResponse.buildFailure("获取客户端列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 格式化客户端数据以匹配前端期望的格式
     */
    private List<Map<String, Object>> formatClientData(List<Object> rawClients) {
        List<Map<String, Object>> formattedClients = new ArrayList<>();
        
        for (Object rawClient : rawClients) {
            if (rawClient instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> clientMap = (Map<String, Object>) rawClient;
                
                Map<String, Object> formattedClient = new HashMap<>();
                formattedClient.put("id", clientMap.get("id"));
                formattedClient.put("clientId", clientMap.get("clientId"));
                formattedClient.put("name", clientMap.get("name") != null ? clientMap.get("name") : clientMap.get("clientId"));
                formattedClient.put("description", clientMap.get("description"));
                formattedClient.put("enabled", clientMap.get("enabled") != null ? clientMap.get("enabled") : true);
                formattedClient.put("baseUrl", clientMap.get("baseUrl") != null ? clientMap.get("baseUrl") : clientMap.get("rootUrl"));
                
                // 处理图标URL
                Object attributes = clientMap.get("attributes");
                String iconUrl = null;
                if (attributes instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> attributesMap = (Map<String, Object>) attributes;
                    iconUrl = (String) attributesMap.get("icon_url");
                }
                formattedClient.put("icon", iconUrl);
                
                formattedClients.add(formattedClient);
            }
        }
        
        return formattedClients;
    }
    
    /**
     * 获取用户可访问的Keycloak客户端列表
     * 
     * @return 客户端列表
     */
    @GetMapping("/clients")
    public JsonResponse getUserClients() {
        try {
            Subject subject = SecurityUtils.getSubject();
            
            if (!subject.isAuthenticated()) {
                return JsonResponse.buildFailure("用户未认证");
            }
            
            Object principal = subject.getPrincipal();
            if (!(principal instanceof KeycloakUserPrincipal)) {
                return JsonResponse.buildFailure("无效的用户认证信息");
            }
            
            KeycloakUserPrincipal userPrincipal = (KeycloakUserPrincipal) principal;
            String accessToken = userPrincipal.getToken();
            
            if (accessToken == null || userPrincipal.isTokenExpired()) {
                return JsonResponse.buildFailure("访问令牌已过期");
            }
            
            // 调用Keycloak Admin API获取客户端列表
            List<Map<String, Object>> clients = getKeycloakClients(accessToken);
            
            return JsonResponse.buildSuccess(clients);
            
        } catch (Exception e) {
            logger.error("获取用户客户端列表失败", e);
            return JsonResponse.buildFailure("获取客户端列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取登录 URL
     * 
     * @param request HTTP 请求
     * @return 登录 URL
     */
    @GetMapping("/login-url")
    public ResponseEntity<Map<String, Object>> getLoginUrl(HttpServletRequest request) {
        System.out.println("=== getLoginUrl 方法被调用 ===");
        logger.info("收到 SSO 登录 URL 请求");
        
        // 从系统配置表获取SSO配置
        String serverUrl = tpSystemConfigService.getConfigValue("sso.keycloak.server.url");
        String realm = tpSystemConfigService.getConfigValue("sso.keycloak.realm");
        String clientId = tpSystemConfigService.getConfigValue("sso.keycloak.client.id");
        String defaultRedirectUri = tpSystemConfigService.getConfigValue("sso.keycloak.redirect.uri");
        
        logger.info("从系统配置获取的 SSO 配置 - serverUrl: {}, realm: {}, clientId: {}", 
                   serverUrl, realm, clientId);
        
        // 检查必要的配置是否存在
        if (serverUrl == null || realm == null || clientId == null) {
            logger.error("SSO配置不完整，请检查系统配置表中的SSO相关配置");
            Map<String, Object> errorResponse = createErrorResponse("SSO配置不完整，请联系管理员");
            return ResponseEntity.status(500).body(errorResponse);
        }
        
        String redirectUri = request.getParameter("redirect_uri");
        if (redirectUri == null || redirectUri.trim().isEmpty()) {
            redirectUri = defaultRedirectUri != null ? defaultRedirectUri : request.getHeader("Referer");
        }
        if (redirectUri == null || redirectUri.trim().isEmpty()) {
            redirectUri = "/";
        }
        
        // 构建 Keycloak 登录 URL
        String loginUrl = String.format(
            "%s/realms/%s/protocol/openid-connect/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=openid",
            serverUrl,
            realm,
            clientId,
            redirectUri
        );
        
        logger.info("生成的登录 URL: {}", loginUrl);
        
        Map<String, Object> data = new HashMap<>();
        data.put("loginUrl", loginUrl);
        data.put("redirectUri", redirectUri);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        
        logger.info("返回响应: {}", response);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取当前用户的Keycloak配置信息
     *
     * @return JsonResponse
     */
    @GetMapping("/keycloak-config")
    public JsonResponse getKeycloakConfig() {
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) {
                return JsonResponse.buildFailure("用户未认证");
            }

            KeycloakUserPrincipal principal = (KeycloakUserPrincipal) subject.getPrincipal();
            String userId = principal.getUserId();
            
            // 根据用户ID查询Keycloak配置信息
            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(userId);
            if (keycloakAccount == null) {
                return JsonResponse.buildFailure("未找到用户的Keycloak配置信息");
            }

            // 构建返回的配置信息（不包含敏感信息）
            Map<String, Object> config = new HashMap<>();
            config.put("kcRealm", keycloakAccount.getKcRealm());
            config.put("kcServerUrl", keycloakAccount.getKcServerUrl());
            config.put("kcClientId", keycloakAccount.getKcClientId());
            config.put("kcUsername", keycloakAccount.getKcUsername());
            config.put("kcUserId", keycloakAccount.getKcUserId());
            config.put("kcPostLogoutRedirectUri", keycloakAccount.getKcPostLogoutRedirectUri());
            
            return JsonResponse.buildSuccess(config);
        } catch (Exception e) {
            logger.error("获取Keycloak配置信息失败", e);
            return JsonResponse.buildFailure("获取配置信息失败: " + e.getMessage());
        }
    }

    /**
     * SSO单点登出
     *
     * @param request HTTP请求对象
     * @return JsonResponse
     */
    @PostMapping("/sso-logout")
    public JsonResponse ssoLogout(HttpServletRequest request) {
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) {
                return JsonResponse.buildFailure("用户未认证");
            }

            KeycloakUserPrincipal principal = (KeycloakUserPrincipal) subject.getPrincipal();
            String userId = principal.getUserId();
            
            // 获取用户的Keycloak配置信息
            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(userId);
            if (keycloakAccount == null) {
                logger.warn("用户{}未找到Keycloak配置信息，执行本地登出", userId);
                subject.logout();
                return JsonResponse.buildSuccess("本地登出成功");
            }

            // 构建Keycloak登出URL
            String keycloakServerUrl = keycloakAccount.getKcServerUrl();
            String realm = keycloakAccount.getKcRealm();
            String clientId = keycloakAccount.getKcClientId();
            String postLogoutRedirectUri = keycloakAccount.getKcPostLogoutRedirectUri();
            
            if (postLogoutRedirectUri == null || postLogoutRedirectUri.trim().isEmpty()) {
                // 默认重定向到前端首页
                postLogoutRedirectUri = "http://localhost:10801";
            }

            StringBuilder logoutUrlBuilder = new StringBuilder();
            logoutUrlBuilder.append(keycloakServerUrl)
                          .append("/realms/")
                          .append(realm)
                          .append("/protocol/openid-connect/logout");
            
            // 添加查询参数
            logoutUrlBuilder.append("?client_id=").append(clientId);
            if (postLogoutRedirectUri != null && !postLogoutRedirectUri.trim().isEmpty()) {
                logoutUrlBuilder.append("&post_logout_redirect_uri=").append(postLogoutRedirectUri);
            }
            
            String logoutUrl = logoutUrlBuilder.toString();
            logger.info("构建的Keycloak登出URL: {}", logoutUrl);

            // 执行本地登出
            subject.logout();
            
            // 返回Keycloak登出URL，前端需要重定向到此URL完成SSO登出
            Map<String, Object> result = new HashMap<>();
            result.put("logoutUrl", logoutUrl);
            result.put("message", "本地登出成功，请重定向到Keycloak完成SSO登出");
            
            return JsonResponse.buildSuccess(result);
        } catch (Exception e) {
            logger.error("SSO登出失败", e);
            return JsonResponse.buildFailure("登出失败: " + e.getMessage());
        }
    }

    /**
     * 登出
     * 
     * @param request HTTP 请求
     * @param response HTTP 响应
     * @return 登出结果
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        
        try {
            // Shiro 登出
            subject.logout();
            
            // 构建 Keycloak 登出 URL
            String logoutUrl = String.format(
                "%s/realms/%s/protocol/openid-connect/logout",
                properties.getServerUrl(),
                properties.getRealm()
            );
            
            Map<String, Object> data = new HashMap<>();
            data.put("logoutUrl", logoutUrl);
            data.put("message", "登出成功");
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", data);
            
            return ResponseEntity.ok(responseData);
            
        } catch (Exception e) {
            logger.error("登出失败", e);
            return ResponseEntity.status(500).body(createErrorResponse("登出失败: " + e.getMessage()));
        }
    }
    
    /**
     * 处理 OIDC 回调
     * 
     * @param code 授权码
     * @param state 状态参数
     * @param request HTTP 请求
     * @param response HTTP 响应
     * @return 回调处理结果
     */
    @GetMapping("/callback")
    public void handleCallback(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "error_description", required = false) String errorDescription,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        logger.info("收到 OIDC 回调请求: code={}, state={}, error={}", code, state, error);
        
        // 检查是否有错误
        if (error != null) {
            logger.error("OIDC 认证失败: {} - {}", error, errorDescription);
            String errorUrl = buildErrorUrl(properties.getRedirect().getErrorUrl(), 
                errorDescription != null ? errorDescription : error);
            response.sendRedirect(errorUrl);
            return;
        }
        
        // 检查授权码
        if (code == null || code.trim().isEmpty()) {
            logger.error("未收到授权码");
            String errorUrl = buildErrorUrl(properties.getRedirect().getErrorUrl(), "未收到授权码");
            response.sendRedirect(errorUrl);
            return;
        }
        
        try {
            // 1. 使用授权码向 Keycloak 换取访问令牌
            // 从系统配置表获取与授权请求时相同的 redirect_uri
            String redirectUri = tpSystemConfigService.getConfigValue("sso.keycloak.redirect.uri");
            if (redirectUri == null || redirectUri.trim().isEmpty()) {
                // 降级方案：从配置文件获取，并转换为后端回调地址
                redirectUri = properties.getRedirect().getSuccessUrl();
                if (redirectUri != null && redirectUri.contains("/#/sso/login")) {
                    // 将前端地址转换为后端回调地址
                    redirectUri = redirectUri.replace("/#/sso/login", "/ps-be/api/sso/callback");
                }
                logger.warn("系统配置表中未找到 sso.keycloak.redirect.uri，使用降级方案: {}", redirectUri);
            }
            
            logger.info("Token交换使用的redirect_uri: {}", redirectUri);
            KeycloakOAuth2Service.TokenResponse tokenResponse = oAuth2Service.exchangeCodeForToken(code, redirectUri);
            
            // 2. 使用访问令牌获取用户信息
            KeycloakUserPrincipal userPrincipal = oAuth2Service.getUserInfoFromToken(tokenResponse.getAccessToken());
            String keycloakUsername = userPrincipal.getUsername();
            
            logger.info("从Keycloak获取到用户信息: username={}, email={}", keycloakUsername, userPrincipal.getEmail());
            
            // 3. 在ps-be系统中校验用户名是否存在
            AccountVO accountVO = null;
            try {
                accountVO = accountService.queryAccountByUsername(keycloakUsername);
                if (accountVO == null) {
                    logger.warn("用户名 {} 在ps-be系统中不存在", keycloakUsername);
                    String errorUrl = buildErrorUrl(properties.getRedirect().getErrorUrl(), 
                        "用户名在系统中不存在: " + keycloakUsername);
                    response.sendRedirect(errorUrl);
                    return;
                }
                logger.info("在ps-be系统中找到用户: personId={}, userName={}", accountVO.getPersonId(), accountVO.getUserName());
            } catch (Exception e) {
                logger.error("查询用户信息时发生异常: {}", e.getMessage(), e);
                String errorUrl = buildErrorUrl(properties.getRedirect().getErrorUrl(), "查询用户信息失败");
                response.sendRedirect(errorUrl);
                return;
            }
            
            // 4. 验证用户登录时间规则
            try {
                // 获取用户角色列表
                List<String> roleIds = new ArrayList<>();
                if (accountVO.getRoleIds() != null && !accountVO.getRoleIds().trim().isEmpty()) {
                    roleIds = Arrays.asList(accountVO.getRoleIds().split(","));
                }
                
                // 验证登录时间规则
                TpTimeRuleService.LoginTimeValidationResult validationResult = 
                    tpTimeRuleService.validateLoginTimeWithReason(accountVO.getPersonId(), roleIds);
                
                if (!validationResult.isAllowed()) {
                    logger.warn("用户 {} 登录被时间规则拒绝: {}", keycloakUsername, validationResult.getReason());
                    String errorUrl = buildErrorUrl(properties.getRedirect().getErrorUrl(), 
                        "登录失败: " + validationResult.getReason());
                    response.sendRedirect(errorUrl);
                    return;
                }
                
                logger.info("用户 {} 通过时间规则验证", keycloakUsername);
            } catch (Exception e) {
                logger.error("验证登录时间规则时发生异常: {}", e.getMessage(), e);
                String errorUrl = buildErrorUrl(properties.getRedirect().getErrorUrl(), "登录时间验证失败");
                response.sendRedirect(errorUrl);
                return;
            }
            
            // 5. 生成ps-be系统的token
            try {
                String token = topinfoSecurityCommonService.createToken(accountVO);
                accountVO.setToken(token);
                
                logger.info("成功为用户 {} 生成token", keycloakUsername);
                
                // 登录成功，重定向到 sso.html 中转页面（避免 Hash 路由问题）
                // 从配置或系统配置表获取前端地址
                String frontendUrl = tpSystemConfigService.getConfigValue("sso.keycloak.callback.url");
                if (frontendUrl == null || frontendUrl.trim().isEmpty()) {
                    // 降级：从 success-url 中提取基础 URL
                    String successUrl = properties.getRedirect().getSuccessUrl();
                    if (successUrl != null && successUrl.contains("#")) {
                        frontendUrl = successUrl.substring(0, successUrl.indexOf("#"));
                    } else {
                        frontendUrl = "https://mid.shxdx.com";
                    }
                }
                
                // 确保 URL 不以 / 结尾
                if (frontendUrl.endsWith("/")) {
                    frontendUrl = frontendUrl.substring(0, frontendUrl.length() - 1);
                }
                
                // 构建中转页面 URL
                String redirectUrl = frontendUrl + "/sso.html?token=" + java.net.URLEncoder.encode(token, "UTF-8");
                logger.info("重定向到 SSO 中转页面: {}", redirectUrl);
                
                response.sendRedirect(redirectUrl);
                return;
                
            } catch (Exception e) {
                logger.error("生成token时发生异常: {}", e.getMessage(), e);
                String errorUrl = buildErrorUrl(properties.getRedirect().getErrorUrl(), "生成token失败");
                response.sendRedirect(errorUrl);
                return;
            }
            
        } catch (Exception e) {
            logger.error("处理回调失败", e);
            String errorUrl = buildErrorUrl(properties.getRedirect().getErrorUrl(), 
                "处理回调失败: " + e.getMessage());
            response.sendRedirect(errorUrl);
            return;
        }
    }
    
    /**
     * 重定向到登录页面
     * 
     * @param request HTTP 请求
     * @param response HTTP 响应
     * @throws IOException IO 异常
     */
    @GetMapping("/login")
    public void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectUri = request.getParameter("redirect_uri");
        if (redirectUri == null || redirectUri.trim().isEmpty()) {
            redirectUri = request.getHeader("Referer");
        }
        if (redirectUri == null || redirectUri.trim().isEmpty()) {
            redirectUri = "/";
        }
        
        String loginUrl = String.format(
            "%s/realms/%s/protocol/openid-connect/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=openid",
            properties.getServerUrl(),
            properties.getRealm(),
            properties.getClientId(),
            redirectUri
        );
        
        response.sendRedirect(loginUrl);
    }
    
    /**
     * 创建错误响应
     * 
     * @param message 错误消息
     * @return 错误响应
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
    
    /**
     * 构建错误重定向 URL，使用 sso.html 作为中转页面
     * 
     * @param baseUrl 基础 URL
     * @param errorDetail 错误详情
     * @return 完整的错误 URL
     */
    private String buildErrorUrl(String baseUrl, String errorDetail) {
        try {
            String encodedError = java.net.URLEncoder.encode(errorDetail, "UTF-8");
            
            // 如果是 Hash 路由地址，使用 sso.html 作为中转页面
            if (baseUrl.contains("#")) {
                // 提取基础 URL（# 之前的部分）
                String[] parts = baseUrl.split("#", 2);
                String frontendBaseUrl = parts[0];
                
                // 确保 URL 不以 / 结尾
                if (frontendBaseUrl.endsWith("/")) {
                    frontendBaseUrl = frontendBaseUrl.substring(0, frontendBaseUrl.length() - 1);
                }
                
                // 使用 sso.html 作为中转页面，它会自动处理 error_detail 参数并跳转到登录页
                return frontendBaseUrl + "/sso.html?error_detail=" + encodedError;
            } else if (baseUrl.contains("?")) {
                // URL 已有查询参数
                return baseUrl + "&error_detail=" + encodedError;
            } else {
                // 普通 URL
                return baseUrl + "?error_detail=" + encodedError;
            }
        } catch (Exception e) {
            logger.error("构建错误 URL 失败", e);
            return baseUrl;
        }
    }
    
    /**
     * 调用Keycloak Admin API获取客户端列表
     * 
     * @param accessToken 访问令牌
     * @return 客户端列表
     */
    private List<Map<String, Object>> getKeycloakClients(String accessToken) {
        try {
            // 构建Keycloak Admin API URL
            String adminApiUrl = String.format("%s/admin/realms/%s/clients", 
                properties.getServerUrl(), properties.getRealm());
            
            // 创建RestTemplate
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            
            // 设置请求头
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("Content-Type", "application/json");
            
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
            
            // 发送请求获取客户端列表
            ResponseEntity<java.util.List> response = restTemplate.exchange(
                adminApiUrl, 
                org.springframework.http.HttpMethod.GET, 
                entity, 
                java.util.List.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                java.util.List<Map<String, Object>> clients = response.getBody();
                
                // 过滤和格式化客户端数据，只返回需要的字段
                List<Map<String, Object>> filteredClients = new ArrayList<>();
                for (Object clientObj : clients) {
                    if (clientObj instanceof Map) {
                        Map<String, Object> client = (Map<String, Object>) clientObj;
                        Map<String, Object> filteredClient = new HashMap<>();
                        filteredClient.put("id", client.get("id"));
                        filteredClient.put("clientId", client.get("clientId"));
                        filteredClient.put("name", client.get("name"));
                        filteredClient.put("description", client.get("description"));
                        filteredClient.put("enabled", client.get("enabled"));
                        filteredClient.put("publicClient", client.get("publicClient"));
                        filteredClients.add(filteredClient);
                    }
                }
                
                return filteredClients;
            }
        } catch (Exception e) {
            logger.error("获取Keycloak客户端列表失败: {}", e.getMessage(), e);
        }
        
        // 如果请求失败，返回模拟数据作为备用
        return getDefaultClients();
    }
    
    /**
     * 获取默认客户端列表（备用数据）
     */
    private List<Map<String, Object>> getDefaultClients() {
        List<Map<String, Object>> clients = new ArrayList<>();
        
        Map<String, Object> client1 = new HashMap<>();
        client1.put("id", "ps-be");
        client1.put("clientId", "ps-be");
        client1.put("name", "后端管理系统");
        client1.put("description", "JPX3.0 后端管理系统");
        client1.put("enabled", true);
        client1.put("baseUrl", "http://localhost:10801");
        client1.put("icon", null);
        clients.add(client1);
        
        Map<String, Object> client2 = new HashMap<>();
        client2.put("id", "ps-fe");
        client2.put("clientId", "ps-fe");
        client2.put("name", "前端应用");
        client2.put("description", "JPX3.0 前端应用系统");
        client2.put("enabled", true);
        client2.put("baseUrl", "http://localhost:10801");
        client2.put("icon", null);
        clients.add(client2);
        
        Map<String, Object> client3 = new HashMap<>();
        client3.put("id", "ps-mobile");
        client3.put("clientId", "ps-mobile");
        client3.put("name", "移动端应用");
        client3.put("description", "JPX3.0 移动端应用");
        client3.put("enabled", true);
        client3.put("baseUrl", "http://localhost:8080");
        client3.put("icon", null);
        clients.add(client3);
        
        return clients;
    }
}