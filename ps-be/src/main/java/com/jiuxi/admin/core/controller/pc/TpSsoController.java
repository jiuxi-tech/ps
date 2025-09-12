package com.jiuxi.admin.core.controller.pc;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.admin.core.service.TpKeycloakAccountService;
import com.jiuxi.module.user.app.service.UserAccountService;
import com.jiuxi.admin.core.service.TpSystemConfigService;
import com.jiuxi.admin.core.service.TpAccountService;
import com.jiuxi.admin.core.bean.entity.TpKeycloakAccount;
import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import com.jiuxi.security.sso.config.KeycloakSsoProperties;
import com.jiuxi.security.core.holder.SessionHolder;
import com.jiuxi.common.bean.SessionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * SSO 用户信息控制器
 * 
 * 提供 SSO 用户相关的接口服务
 * 
 * @author System
 * @since 2025-01-21
 */
@RestController
@RequestMapping("/sso")
public class TpSsoController {
    
    private static final Logger logger = LoggerFactory.getLogger(TpSsoController.class);
    
    @Autowired
    private TpKeycloakAccountService tpKeycloakAccountService;
    
    @Autowired
    private TpAccountService tpAccountService;
    
    @Autowired
    private UserAccountService userAccountService;
    
    @Autowired
    private TpSystemConfigService tpSystemConfigService;
    
    @Autowired
    private KeycloakSsoProperties keycloakSsoProperties;
    
    @Autowired
    private RestTemplate restTemplate;
    
    /**
     * 获取Keycloak服务器地址
     */
    private String getKeycloakServerUrl() {
        return tpSystemConfigService.getConfigValue("keycloak.server-url", "http://localhost:18080");
    }
    
    /**
     * 获取Keycloak Realm
     */
    private String getKeycloakRealm() {
        return tpSystemConfigService.getConfigValue("keycloak.realm", "ps-realm");
    }
    
    /**
     * 获取Keycloak管理员客户端ID
     */
    private String getAdminClientId() {
        return tpSystemConfigService.getConfigValue("keycloak.admin.client-id", "admin-cli");
    }
    
    /**
     * 获取Keycloak管理员用户名
     */
    private String getAdminUsername() {
        return tpSystemConfigService.getConfigValue("keycloak.admin.username", "admin");
    }
    
    /**
     * 获取Keycloak管理员密码
     */
    private String getAdminPassword() {
        return tpSystemConfigService.getConfigValue("keycloak.admin.password", "admin123");
    }
    
    /**
     * 获取用户状态信息
     * @return 用户状态信息
     */
    @GetMapping("/user/status")
    public JsonResponse getUserStatus() {
        logger.info("开始获取用户状态信息");
        
        try {
            // 从SessionHolder中获取当前用户的SessionVO
            SessionVO sessionVO = SessionHolder.get();
            if (sessionVO == null || !StringUtils.hasText(sessionVO.getPersonId())) {
                logger.warn("用户未登录或SessionVO为空，返回未登录状态");
                return JsonResponse.buildFailure("用户未登录");
            }
            
            String personId = sessionVO.getPersonId();
            logger.debug("从token中获取到personId: {}", personId);
            
            // 通过personId获取账号信息
            TpAccountVO accountVO = userAccountService.accountView(personId);
            if (accountVO == null) {
                logger.warn("根据personId未找到账号信息，personId: {}", personId);
                return JsonResponse.buildFailure("账号信息不存在");
            }
            
            String accountId = accountVO.getAccountId();
            logger.debug("通过personId获取到accountId: {}", accountId);
            
            // 查询用户的SSO账号信息
            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount == null) {
                logger.info("用户未绑定SSO账号，accountId: {}", accountId);
                Map<String, Object> userStatus = new HashMap<>();
                userStatus.put("authenticated", false);
                userStatus.put("keycloakBound", false);
                userStatus.put("message", "用户未绑定SSO账号");
                userStatus.put("personId", personId);
                userStatus.put("accountId", accountId);
                return JsonResponse.buildSuccess(userStatus);
            }
            
            logger.debug("找到SSO账号信息，accountId: {}, SSO用户ID: {}, SSO用户名: {}", 
                        accountId, keycloakAccount.getKcUserId(), keycloakAccount.getKcUsername());
            
            // 获取管理员访问令牌
            logger.debug("正在获取SSO管理员访问令牌");
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                logger.error("无法获取SSO管理员令牌，accountId: {}", accountId);
                return JsonResponse.buildFailure("无法获取SSO管理员令牌");
            }
            logger.debug("成功获取SSO管理员访问令牌");
            
            // 通过Keycloak REST API获取用户信息
            logger.debug("正在通过Keycloak REST API获取用户信息，SSO用户ID: {}", keycloakAccount.getKcUserId());
            Map<String, Object> keycloakUserInfo = getKeycloakUserInfo(adminToken, keycloakAccount.getKcUserId());
            if (keycloakUserInfo == null) {
                logger.warn("SSO用户不存在或已被删除，账号ID: {}, SSO用户ID: {}", 
                           accountId, keycloakAccount.getKcUserId());
                Map<String, Object> userStatus = new HashMap<>();
                userStatus.put("authenticated", false);
                userStatus.put("keycloakBound", true);
                userStatus.put("message", "SSO用户不存在或已被删除");
                return JsonResponse.buildSuccess(userStatus);
            }
            
            logger.debug("成功获取SSO用户信息，账号ID: {}, 用户状态: enabled={}, emailVerified={}", 
                        accountId, keycloakUserInfo.get("enabled"), keycloakUserInfo.get("emailVerified"));
            
            // 构建用户状态响应
            Map<String, Object> userStatus = new HashMap<>();
            userStatus.put("authenticated", true);
            userStatus.put("keycloakBound", true);
            userStatus.put("keycloakUserId", keycloakAccount.getKcUserId());
            userStatus.put("keycloakUsername", keycloakAccount.getKcUsername());
            userStatus.put("enabled", keycloakUserInfo.get("enabled"));
            userStatus.put("emailVerified", keycloakUserInfo.get("emailVerified"));
            userStatus.put("firstName", keycloakUserInfo.get("firstName"));
            userStatus.put("lastName", keycloakUserInfo.get("lastName"));
            userStatus.put("email", keycloakUserInfo.get("email"));
            userStatus.put("createdTimestamp", keycloakUserInfo.get("createdTimestamp"));
            
            logger.info("成功获取用户状态信息，账号ID: {}, SSO用户名: {}, 状态: authenticated=true", 
                       accountId, keycloakAccount.getKcUsername());
            return JsonResponse.buildSuccess(userStatus);
        } catch (Exception e) {
            logger.error("获取用户状态失败，错误信息: {}", e.getMessage(), e);
            return JsonResponse.buildFailure("获取用户状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取SSO管理员访问令牌
     * 
     * @return 访问令牌
     */
    private String getAdminAccessToken() {
        try {
            // 使用master realm获取管理员令牌
            String tokenUrl = getKeycloakServerUrl() + "/realms/master/protocol/openid-connect/token";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "password");
            params.add("client_id", getAdminClientId());
        params.add("username", getAdminUsername());
        params.add("password", getAdminPassword());
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("access_token");
            }
            
            return null;
        } catch (Exception e) {
            logger.error("获取SSO管理员令牌失败", e);
            return null;
        }
    }
    
    /**
     * 通过Keycloak REST API获取用户信息
     * 
     * @param adminToken 管理员访问令牌
     * @param userId 用户ID
     * @return 用户信息
     */
    private Map<String, Object> getKeycloakUserInfo(String adminToken, String userId) {
        try {
            String userUrl = getKeycloakServerUrl() + "/admin/realms/" + getKeycloakRealm() + "/users/" + userId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(userUrl, HttpMethod.GET, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
            
            return null;
        } catch (Exception e) {
            logger.error("获取SSO用户信息失败", e);
            return null;
        }
    }
    
    /**
     * 获取用户可访问的客户端列表
     * 
     * @return JsonResponse
     */
    @GetMapping("/user/clients")
    public JsonResponse getUserClients() {
        try {
            // 从SessionHolder中获取当前用户的SessionVO
            SessionVO sessionVO = SessionHolder.get();
            if (sessionVO == null || !StringUtils.hasText(sessionVO.getPersonId())) {
                return JsonResponse.buildFailure("用户未登录");
            }
            
            String personId = sessionVO.getPersonId();
            // 通过personId获取账号信息
            TpAccountVO accountVO = userAccountService.accountView(personId);
            if (accountVO == null) {
                return JsonResponse.buildFailure("账号信息不存在");
            }
            
            String accountId = accountVO.getAccountId();
            // 查询用户的SSO账号信息
            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount == null) {
                return JsonResponse.buildFailure("用户未绑定SSO账号");
            }
            
            // 获取管理员访问令牌
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return JsonResponse.buildFailure("无法获取SSO管理员令牌");
            }
            
            // 通过Keycloak REST API获取客户端列表
            List<Map<String, Object>> clients = getKeycloakClients(adminToken);
            
            return JsonResponse.buildSuccess(clients);
        } catch (Exception e) {
            logger.error("获取用户客户端列表失败", e);
            return JsonResponse.buildFailure("获取客户端列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 通过Keycloak REST API获取客户端列表
     * 
     * @param adminToken 管理员访问令牌
     * @return 客户端列表
     */
    private List<Map<String, Object>> getKeycloakClients(String adminToken) {
        try {
            String clientsUrl = getKeycloakServerUrl() + "/admin/realms/" + getKeycloakRealm() + "/clients";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            
            ResponseEntity<List> response = restTemplate.exchange(clientsUrl, HttpMethod.GET, request, List.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("获取Keycloak客户端列表失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取用户角色列表
     * 
     * @return JsonResponse
     */
    @GetMapping("/user/roles")
    public JsonResponse getUserRoles() {
        try {
            // 从SessionHolder中获取当前用户的SessionVO
            SessionVO sessionVO = SessionHolder.get();
            if (sessionVO == null || !StringUtils.hasText(sessionVO.getPersonId())) {
                return JsonResponse.buildFailure("用户未登录");
            }
            
            String personId = sessionVO.getPersonId();
            // 通过personId获取账号信息
            TpAccountVO accountVO = userAccountService.accountView(personId);
            if (accountVO == null) {
                return JsonResponse.buildFailure("账号信息不存在");
            }
            
            String accountId = accountVO.getAccountId();
            // 查询用户的SSO账号信息
            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount == null) {
                return JsonResponse.buildFailure("用户未绑定SSO账号");
            }
            
            // 获取管理员访问令牌
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return JsonResponse.buildFailure("无法获取SSO管理员令牌");
            }
            
            // 通过Keycloak REST API获取用户角色
            List<Map<String, Object>> roles = getKeycloakUserRoles(adminToken, keycloakAccount.getKcUserId());
            
            return JsonResponse.buildSuccess(roles);
        } catch (Exception e) {
            logger.error("获取用户角色失败", e);
            return JsonResponse.buildFailure("获取用户角色失败: " + e.getMessage());
        }
    }
    
    /**
     * 通过Keycloak REST API获取用户角色
     * 
     * @param adminToken 管理员访问令牌
     * @param userId 用户ID
     * @return 用户角色列表
     */
    private List<Map<String, Object>> getKeycloakUserRoles(String adminToken, String userId) {
        try {
            String rolesUrl = getKeycloakServerUrl() + "/admin/realms/" + getKeycloakRealm() + "/users/" + userId + "/role-mappings/realm";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            
            ResponseEntity<List> response = restTemplate.exchange(rolesUrl, HttpMethod.GET, request, List.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("获取SSO用户角色失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取用户可访问的资源列表
     * 
     * @return JsonResponse
     */
    @GetMapping("/user/resources")
    public JsonResponse getUserResources() {
        try {
            // 从SessionHolder中获取当前用户的SessionVO
            SessionVO sessionVO = SessionHolder.get();
            if (sessionVO == null || !StringUtils.hasText(sessionVO.getPersonId())) {
                return JsonResponse.buildFailure("用户未登录");
            }
            
            String personId = sessionVO.getPersonId();
            // 通过personId获取账号信息
            TpAccountVO accountVO = userAccountService.accountView(personId);
            if (accountVO == null) {
                return JsonResponse.buildFailure("账号信息不存在");
            }
            
            String accountId = accountVO.getAccountId();
            // 查询用户的SSO账号信息
            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount == null) {
                return JsonResponse.buildFailure("用户未绑定SSO账号");
            }
            
            // 获取管理员访问令牌
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return JsonResponse.buildFailure("无法获取SSO管理员令牌");
            }
            
            // 通过Keycloak REST API获取用户资源
            List<Map<String, Object>> resources = getKeycloakUserResources(adminToken, keycloakAccount.getKcUserId());
            
            return JsonResponse.buildSuccess(resources);
        } catch (Exception e) {
            logger.error("获取用户资源失败", e);
            return JsonResponse.buildFailure("获取用户资源失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试接口：显示当前token中的用户信息
     */


    @GetMapping("/test_token_info")
    public JsonResponse getTokenInfo() {
        try {
            // 从SessionHolder获取SessionVO
            SessionVO sessionVO = SessionHolder.get();
            if (sessionVO == null) {
                return JsonResponse.buildFailure("SessionVO为空");
            }

            Map<String, Object> tokenInfo = new HashMap<>();
            tokenInfo.put("accountId", sessionVO.getAccountId());
            tokenInfo.put("personId", sessionVO.getPersonId());
            tokenInfo.put("username", sessionVO.getPersonName());
            tokenInfo.put("tenantId", sessionVO.getTenantId());
            
            // 检查是否存在Keycloak绑定
            String accountId = sessionVO.getAccountId();
            if (accountId != null) {
                TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
                tokenInfo.put("keycloakBound", keycloakAccount != null);
                if (keycloakAccount != null) {
                    tokenInfo.put("kcUsername", keycloakAccount.getKcUsername());
                    tokenInfo.put("kcUserId", keycloakAccount.getKcUserId());
                }
            } else {
                tokenInfo.put("keycloakBound", false);
            }
            
            return JsonResponse.buildSuccess(tokenInfo);
            
        } catch (Exception e) {
            logger.error("获取token信息失败", e);
            return JsonResponse.buildFailure("获取token信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 通过Keycloak REST API获取用户资源
     * 
     * @param adminToken 管理员访问令牌
     * @param userId 用户ID
     * @return 用户资源列表
     */
    private List<Map<String, Object>> getKeycloakUserResources(String adminToken, String userId) {
        try {
            String resourcesUrl = getKeycloakServerUrl() + "/admin/realms/" + getKeycloakRealm() + "/users/" + userId + "/role-mappings/realm";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            
            ResponseEntity<List> response = restTemplate.exchange(resourcesUrl, HttpMethod.GET, request, List.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("获取SSO用户资源失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 通过Keycloak REST API注销用户的所有会话
     * 
     * @param adminToken 管理员访问令牌
     * @param userId 用户ID
     * @return 是否注销成功
     */
    private boolean logoutKeycloakUser(String adminToken, String userId) {
        try {
            String logoutUrl = getKeycloakServerUrl() + "/admin/realms/" + getKeycloakRealm() + "/users/" + userId + "/logout";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            
            ResponseEntity<Void> response = restTemplate.postForEntity(logoutUrl, request, Void.class);
            
            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK) {
                logger.info("成功通过Keycloak REST API注销用户会话，用户ID: {}", userId);
                return true;
            } else {
                logger.warn("Keycloak REST API注销返回非成功状态码: {}, 用户ID: {}", response.getStatusCode(), userId);
                return false;
            }
        } catch (Exception e) {
            logger.error("通过Keycloak REST API注销用户会话失败，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * SSO用户退出
     * @return JsonResponse
     */
    @PostMapping("/user/logout")
    public JsonResponse userLogout() {
        logger.info("开始执行SSO用户退出");
        
        try {
            // 从SessionHolder中获取当前用户的SessionVO
            SessionVO sessionVO = SessionHolder.get();
            if (sessionVO == null || !StringUtils.hasText(sessionVO.getPersonId())) {
                logger.warn("用户未登录或SessionVO为空，无需退出");
                return JsonResponse.buildFailure("用户未登录");
            }
            
            String personId = sessionVO.getPersonId();
            logger.debug("从token中获取到personId: {}", personId);
            
            // 通过personId获取账号信息
            TpAccountVO accountVO = userAccountService.accountView(personId);
            if (accountVO == null) {
                logger.warn("根据personId未找到账号信息，personId: {}", personId);
                return JsonResponse.buildFailure("账号信息不存在");
            }
            
            String accountId = accountVO.getAccountId();
            logger.debug("通过personId获取到accountId: {}", accountId);
            
            // 查询用户的SSO账号信息
            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount == null) {
                logger.info("用户未绑定SSO账号，执行本地退出，accountId: {}", accountId);
                Map<String, Object> result = new HashMap<>();
                result.put("logoutType", "local");
                result.put("message", "本地退出成功");
                return JsonResponse.buildSuccess(result);
            }
            
            logger.debug("找到SSO账号信息，accountId: {}, SSO用户ID: {}, SSO用户名: {}", 
                        accountId, keycloakAccount.getKcUserId(), keycloakAccount.getKcUsername());
            
            // 获取管理员访问令牌
            logger.debug("正在获取SSO管理员访问令牌");
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                logger.error("无法获取SSO管理员令牌，执行本地退出，accountId: {}", accountId);
                Map<String, Object> result = new HashMap<>();
                result.put("logoutType", "local");
                result.put("message", "无法连接Keycloak，执行本地退出");
                return JsonResponse.buildSuccess(result);
            }
            logger.debug("成功获取SSO管理员访问令牌");
            
            // 通过Keycloak REST API注销用户的所有会话
            logger.debug("正在通过Keycloak REST API注销用户会话，SSO用户ID: {}", keycloakAccount.getKcUserId());
            boolean logoutSuccess = logoutKeycloakUser(adminToken, keycloakAccount.getKcUserId());
            
            Map<String, Object> result = new HashMap<>();
            if (logoutSuccess) {
                result.put("logoutType", "sso");
                result.put("message", "SSO退出成功，用户会话已从Keycloak注销");
                logger.info("成功通过Keycloak REST API注销用户会话，accountId: {}, SSO用户ID: {}", 
                           accountId, keycloakAccount.getKcUserId());
            } else {
                result.put("logoutType", "partial");
                result.put("message", "Keycloak注销失败，但本地会话已清除");
                logger.warn("Keycloak REST API注销失败，但继续执行本地退出，accountId: {}, SSO用户ID: {}", 
                           accountId, keycloakAccount.getKcUserId());
            }
            
            logger.info("SSO用户退出处理完成，accountId: {}", accountId);
            return JsonResponse.buildSuccess(result);
            
        } catch (Exception e) {
            logger.error("SSO用户退出失败", e);
            return JsonResponse.buildFailure("退出失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取Keycloak全局会话列表
     * 
     * @param page 页码
     * @param size 每页大小
     * @param search 搜索关键词
     * @return JsonResponse
     */
    @GetMapping("/admin/session/list")
    public JsonResponse getSessionList(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "20") int size,
                                     @RequestParam(required = false) String search) {
        try {
            logger.info("获取Keycloak全局会话列表，page: {}, size: {}, search: {}", page, size, search);
            
            // 获取管理员访问令牌
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return JsonResponse.buildFailure("无法获取管理员访问令牌");
            }
            
            // 获取会话列表
            List<Map<String, Object>> sessions = getKeycloakSessions(adminToken);
            
            // 过滤和搜索
            if (StringUtils.hasText(search)) {
                sessions = sessions.stream()
                    .filter(session -> {
                        String username = (String) session.get("username");
                        String clientId = (String) session.get("clientId");
                        String ipAddress = (String) session.get("ipAddress");
                        return (username != null && username.toLowerCase().contains(search.toLowerCase())) ||
                               (clientId != null && clientId.toLowerCase().contains(search.toLowerCase())) ||
                               (ipAddress != null && ipAddress.contains(search));
                    })
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // 分页处理
            int total = sessions.size();
            int start = (page - 1) * size;
            int end = Math.min(start + size, total);
            
            List<Map<String, Object>> pagedSessions = sessions.subList(start, end);
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", pagedSessions);
            result.put("total", total);
            result.put("current", page);
            result.put("size", size);
            result.put("pages", (int) Math.ceil((double) total / size));
            
            return JsonResponse.buildSuccess(result);
            
        } catch (Exception e) {
            logger.error("获取SSO全局会话列表失败", e);
            return JsonResponse.buildFailure("获取会话列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 强制注销SSO全局会话
     * 
     * @param sessionId 会话ID
     * @return JsonResponse
     */
    @PostMapping("/admin/session/logout")
    public JsonResponse logoutSession(@RequestBody Map<String, String> request) {
        try {
            String sessionId = request.get("sessionId");
            if (!StringUtils.hasText(sessionId)) {
                return JsonResponse.buildFailure("会话ID不能为空");
            }
            
            logger.info("强制注销SSO会话，sessionId: {}", sessionId);
            
            // 获取管理员访问令牌
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return JsonResponse.buildFailure("无法获取管理员访问令牌");
            }
            
            // 注销会话
            boolean success = logoutKeycloakSession(adminToken, sessionId);
            
            if (success) {
                logger.info("成功注销SSO会话，sessionId: {}", sessionId);
                return JsonResponse.buildSuccess("会话注销成功");
            } else {
                logger.warn("注销SSO会话失败，sessionId: {}", sessionId);
                return JsonResponse.buildFailure("会话注销失败");
            }
            
        } catch (Exception e) {
            logger.error("强制注销SSO会话失败", e);
            return JsonResponse.buildFailure("注销会话失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量强制注销SSO会话
     * 
     * @param request 包含会话ID列表的请求
     * @return JsonResponse
     */
    @PostMapping("/admin/session/batch-logout")
    public JsonResponse batchLogoutSessions(@RequestBody Map<String, List<String>> request) {
        try {
            List<String> sessionIds = request.get("sessionIds");
            if (sessionIds == null || sessionIds.isEmpty()) {
                return JsonResponse.buildFailure("会话ID列表不能为空");
            }
            
            logger.info("批量强制注销SSO会话，sessionIds: {}", sessionIds);
            
            // 获取管理员访问令牌
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return JsonResponse.buildFailure("无法获取管理员访问令牌");
            }
            
            int successCount = 0;
            int failCount = 0;
            
            for (String sessionId : sessionIds) {
                try {
                    boolean success = logoutKeycloakSession(adminToken, sessionId);
                    if (success) {
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    logger.error("注销会话失败，sessionId: {}", sessionId, e);
                    failCount++;
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("total", sessionIds.size());
            result.put("success", successCount);
            result.put("fail", failCount);
            
            logger.info("批量注销SSO会话完成，总数: {}, 成功: {}, 失败: {}", 
                       sessionIds.size(), successCount, failCount);
            
            return JsonResponse.buildSuccess(result);
            
        } catch (Exception e) {
            logger.error("批量强制注销SSO会话失败", e);
            return JsonResponse.buildFailure("批量注销会话失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取SSO会话列表
     * 
     * @param adminToken 管理员访问令牌
     * @return 会话列表
     */
    private List<Map<String, Object>> getKeycloakSessions(String adminToken) {
        try {
            List<Map<String, Object>> allSessions = new ArrayList<>();
            
            // 首先获取所有用户
            String usersUrl = getKeycloakServerUrl() + "/admin/realms/" + getKeycloakRealm() + "/users";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<List> usersResponse = restTemplate.exchange(usersUrl, HttpMethod.GET, entity, List.class);
            
            if (usersResponse.getStatusCode() == HttpStatus.OK && usersResponse.getBody() != null) {
                List<Map<String, Object>> users = usersResponse.getBody();
                
                // 为每个用户获取其会话
                for (Map<String, Object> user : users) {
                    String userId = (String) user.get("id");
                    if (StringUtils.hasText(userId)) {
                        List<Map<String, Object>> userSessions = getUserSessions(adminToken, userId);
                        if (userSessions != null && !userSessions.isEmpty()) {
                            // 为每个会话添加用户信息
                            for (Map<String, Object> session : userSessions) {
                                session.put("userId", userId);
                                session.put("username", user.get("username"));
                                session.put("email", user.get("email"));
                                session.put("firstName", user.get("firstName"));
                                session.put("lastName", user.get("lastName"));
                                enrichSessionData(adminToken, session);
                            }
                            allSessions.addAll(userSessions);
                        }
                    }
                }
            }
            
            return allSessions;
            
        } catch (Exception e) {
            logger.error("获取SSO会话列表失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取特定用户的会话列表
     * 
     * @param adminToken 管理员访问令牌
     * @param userId 用户ID
     * @return 用户会话列表
     */
    private List<Map<String, Object>> getUserSessions(String adminToken, String userId) {
        try {
            String url = getKeycloakServerUrl() + "/admin/realms/" + getKeycloakRealm() + "/users/" + userId + "/sessions";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            
            return new ArrayList<>();
            
        } catch (Exception e) {
            logger.warn("获取用户会话失败，用户ID: " + userId, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 丰富会话数据，添加用户信息
     * 
     * @param adminToken 管理员访问令牌
     * @param session 会话数据
     */
    private void enrichSessionData(String adminToken, Map<String, Object> session) {
        try {
            String userId = (String) session.get("userId");
            if (StringUtils.hasText(userId)) {
                Map<String, Object> userInfo = getKeycloakUserInfo(adminToken, userId);
                if (userInfo != null) {
                    session.put("username", userInfo.get("username"));
                    session.put("email", userInfo.get("email"));
                    session.put("firstName", userInfo.get("firstName"));
                    session.put("lastName", userInfo.get("lastName"));
                }
            }
            
            // 处理客户端信息
            Map<String, Object> clients = (Map<String, Object>) session.get("clients");
            if (clients != null && !clients.isEmpty()) {
                session.put("clientId", clients.keySet().iterator().next());
            }
            
            // 格式化时间
            Long start = (Long) session.get("start");
            if (start != null) {
                session.put("loginTime", new Date(start));
            }
            
            Long lastAccess = (Long) session.get("lastAccess");
            if (lastAccess != null) {
                session.put("lastAccessTime", new Date(lastAccess));
            }
            
        } catch (Exception e) {
            logger.warn("丰富会话数据失败", e);
        }
    }
    
    /**
     * 注销SSO会话
     * 
     * @param adminToken 管理员访问令牌
     * @param sessionId 会话ID
     * @return 是否成功
     */
    private boolean logoutKeycloakSession(String adminToken, String sessionId) {
        try {
            String url = getKeycloakServerUrl() + "/admin/realms/" + getKeycloakRealm() + "/sessions/" + sessionId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
            
            return response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK;
            
        } catch (Exception e) {
            logger.error("注销SSO会话失败，sessionId: {}", sessionId, e);
            return false;
        }
    }

    // ==================== Keycloak 客户端管理接口 ====================

    /**
     * 获取Keycloak客户端列表
     * 
     * @param page 页码
     * @param size 每页大小
     * @param search 搜索关键词
     * @return 客户端列表
     */
    @GetMapping("/admin/client/list")
    public JsonResponse getClientList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return JsonResponse.buildFailure("获取管理员访问令牌失败");
            }
            
            String url = getKeycloakServerUrl() + "/admin/realms/" + getKeycloakRealm() + "/clients";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Map<String, Object>> clients = response.getBody();
                
                // 过滤搜索条件
                if (StringUtils.hasText(search)) {
                    clients = clients.stream()
                        .filter(client -> {
                            String clientId = (String) client.get("clientId");
                            String name = (String) client.get("name");
                            return (clientId != null && clientId.toLowerCase().contains(search.toLowerCase())) ||
                                   (name != null && name.toLowerCase().contains(search.toLowerCase()));
                        })
                        .collect(java.util.stream.Collectors.toList());
                }
                
                // 手动分页
                int total = clients.size();
                int start = (page - 1) * size;
                int end = Math.min(start + size, total);
                
                List<Map<String, Object>> pageData = clients.subList(start, end);
                
                Map<String, Object> result = new HashMap<>();
                result.put("records", pageData);
                result.put("total", total);
                result.put("current", page);
                result.put("size", size);
                result.put("pages", (int) Math.ceil((double) total / size));
                
                return JsonResponse.buildSuccess(result);
            } else {
                return JsonResponse.buildFailure("获取客户端列表失败");
            }
            
        } catch (Exception e) {
            logger.error("获取客户端列表失败", e);
            return JsonResponse.buildFailure("获取客户端列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取单个Keycloak客户端信息
     * 
     * @param clientId 客户端ID
     * @return 客户端信息
     */
    @GetMapping("/admin/client/get")
    public JsonResponse getClient(@RequestParam String clientId) {
        
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return JsonResponse.buildFailure("获取管理员访问令牌失败");
            }
            
            String url = getKeycloakServerUrl() + "/admin/realms/" + getKeycloakRealm() + "/clients/" + clientId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return JsonResponse.buildSuccess(response.getBody());
            } else {
                return JsonResponse.buildFailure("获取客户端信息失败");
            }
            
        } catch (Exception e) {
            logger.error("获取客户端信息失败，clientId: {}", clientId, e);
            return JsonResponse.buildFailure("获取客户端信息失败: " + e.getMessage());
        }
    }

    /**
     * 保存Keycloak客户端信息（新增或更新）
     * 
     * @param clientData 客户端数据
     * @return 保存结果
     */
    @PostMapping("/admin/client/save")
    public JsonResponse saveClient(@RequestBody Map<String, Object> clientData) {
        
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return JsonResponse.buildFailure("获取管理员访问令牌失败");
            }
            
            String clientId = (String) clientData.get("id");
            boolean isUpdate = StringUtils.hasText(clientId);
            
            String url;
            HttpMethod method;
            
            if (isUpdate) {
                // 更新客户端
                url = getKeycloakServerUrl() + "/admin/realms/" + getKeycloakRealm() + "/clients/" + clientId;
                method = HttpMethod.PUT;
            } else {
                // 新增客户端
                url = getKeycloakServerUrl() + "/admin/realms/" + getKeycloakRealm() + "/clients";
                method = HttpMethod.POST;
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(clientData, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.NO_CONTENT) {
                return JsonResponse.buildSuccess(isUpdate ? "客户端更新成功" : "客户端创建成功");
            } else {
                return JsonResponse.buildFailure("保存客户端失败");
            }
            
        } catch (Exception e) {
            logger.error("保存客户端失败", e);
            return JsonResponse.buildFailure("保存客户端失败: " + e.getMessage());
        }
    }

    /**
     * 删除Keycloak客户端
     * 
     * @param clientId 客户端ID
     * @return 删除结果
     */
    @DeleteMapping("/admin/client/delete")
    public JsonResponse deleteClient(@RequestParam String clientId) {
        
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return JsonResponse.buildFailure("获取管理员访问令牌失败");
            }
            
            String url = getKeycloakServerUrl() + "/admin/realms/" + getKeycloakRealm() + "/clients/" + clientId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                return JsonResponse.buildSuccess("客户端删除成功");
            } else {
                return JsonResponse.buildFailure("删除客户端失败");
            }
            
        } catch (Exception e) {
            logger.error("删除客户端失败，clientId: {}", clientId, e);
            return JsonResponse.buildFailure("删除客户端失败: " + e.getMessage());
        }
    }

    /**
     * 获取Keycloak用户事件列表
     * 
     * @param page 页码
     * @param size 每页大小
     * @param username 用户名（可选）
     * @param type 事件类型（可选）
     * @param clientId 客户端ID（可选）
     * @param dateFrom 开始时间（可选）
     * @param dateTo 结束时间（可选）
     * @return 用户事件列表
     */
    @GetMapping("/admin/user-event/list")
    public JsonResponse getUserEventList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo) {
        
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return JsonResponse.buildFailure("获取管理员访问令牌失败");
            }
            
            // 构建查询参数
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(getKeycloakServerUrl())
                     .append("/admin/realms/")
                     .append(getKeycloakRealm())
                     .append("/events");
            
            List<String> params = new ArrayList<>();
            
            // 计算偏移量
            int offset = (page - 1) * size;
            params.add("first=" + offset);
            params.add("max=" + size);
            
            if (StringUtils.hasText(username)) {
                params.add("user=" + username);
            }
            if (StringUtils.hasText(type)) {
                params.add("type=" + type);
            }
            if (StringUtils.hasText(clientId)) {
                params.add("client=" + clientId);
            }
            if (StringUtils.hasText(dateFrom)) {
                params.add("dateFrom=" + dateFrom);
            }
            if (StringUtils.hasText(dateTo)) {
                params.add("dateTo=" + dateTo);
            }
            
            if (!params.isEmpty()) {
                urlBuilder.append("?").append(String.join("&", params));
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<List> response = restTemplate.exchange(
                urlBuilder.toString(), HttpMethod.GET, entity, List.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Map<String, Object>> events = response.getBody();
                if (events == null) {
                    events = new ArrayList<>();
                }
                
                // 尝试获取总数 - 由于Keycloak API限制，我们需要通过其他方式获取总数
                // 这里我们先获取一个较大的数据集来估算总数
                int totalCount = getTotalUserEventCount(adminToken, username, type, clientId, dateFrom, dateTo);
                
                // 构建分页结果
                Map<String, Object> result = new HashMap<>();
                result.put("records", events);
                result.put("total", totalCount);
                result.put("current", page);
                result.put("size", size);
                result.put("pages", (totalCount + size - 1) / size);
                
                return JsonResponse.buildSuccess(result);
            } else {
                return JsonResponse.buildFailure("获取用户事件列表失败");
            }
            
        } catch (Exception e) {
            logger.error("获取用户事件列表失败", e);
            return JsonResponse.buildFailure("获取用户事件列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户事件总数
     * 由于Keycloak API不直接提供总数，这里通过获取大量数据来估算
     */
    private int getTotalUserEventCount(String adminToken, String username, String type, 
                                     String clientId, String dateFrom, String dateTo) {
        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(getKeycloakServerUrl())
                    .append("/admin/realms/")
                    .append(getKeycloakRealm())
                    .append("/events");
            
            List<String> params = new ArrayList<>();
            // 获取大量数据来计算总数，设置一个较大的max值
            params.add("first=0");
            params.add("max=10000"); // 设置一个较大的值来获取更多数据
            
            if (StringUtils.hasText(username)) {
                params.add("user=" + username);
            }
            if (StringUtils.hasText(type)) {
                params.add("type=" + type);
            }
            if (StringUtils.hasText(clientId)) {
                params.add("client=" + clientId);
            }
            if (StringUtils.hasText(dateFrom)) {
                params.add("dateFrom=" + dateFrom);
            }
            if (StringUtils.hasText(dateTo)) {
                params.add("dateTo=" + dateTo);
            }
            
            if (!params.isEmpty()) {
                urlBuilder.append("?").append(String.join("&", params));
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<List> response = restTemplate.exchange(
                urlBuilder.toString(), HttpMethod.GET, entity, List.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Map<String, Object>> events = response.getBody();
                return events != null ? events.size() : 0;
            }
            
            return 0;
        } catch (Exception e) {
            logger.warn("获取用户事件总数失败，使用默认值: " + e.getMessage());
            return 0;
        }
    }

    /**
     * 获取Keycloak管理员事件列表
     * 
     * @param page 页码
     * @param size 每页大小
     * @param authUser 操作用户（可选）
     * @param operationType 操作类型（可选）
     * @param resourceType 资源类型（可选）
     * @param dateFrom 开始时间（可选）
     * @param dateTo 结束时间（可选）
     * @return 管理员事件列表
     */
    @GetMapping("/admin/admin-event/list")
    public JsonResponse getAdminEventList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String authUser,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String resourceType,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo) {
        
        try {
            String adminToken = getAdminAccessToken();
            if (adminToken == null) {
                return JsonResponse.buildFailure("获取管理员访问令牌失败");
            }
            
            // 构建查询参数
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(getKeycloakServerUrl())
                     .append("/admin/realms/")
                     .append(getKeycloakRealm())
                     .append("/admin-events");
            
            List<String> params = new ArrayList<>();
            
            // 计算偏移量
            int offset = (page - 1) * size;
            params.add("first=" + offset);
            params.add("max=" + size);
            
            if (StringUtils.hasText(authUser)) {
                params.add("authUser=" + authUser);
            }
            if (StringUtils.hasText(operationType)) {
                params.add("operationType=" + operationType);
            }
            if (StringUtils.hasText(resourceType)) {
                params.add("resourceType=" + resourceType);
            }
            if (StringUtils.hasText(dateFrom)) {
                params.add("dateFrom=" + dateFrom);
            }
            if (StringUtils.hasText(dateTo)) {
                params.add("dateTo=" + dateTo);
            }
            
            if (!params.isEmpty()) {
                urlBuilder.append("?").append(String.join("&", params));
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<List> response = restTemplate.exchange(
                urlBuilder.toString(), HttpMethod.GET, entity, List.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Map<String, Object>> events = response.getBody();
                if (events == null) {
                    events = new ArrayList<>();
                }
                
                // 构建分页结果
                Map<String, Object> result = new HashMap<>();
                result.put("records", events);
                result.put("total", events.size()); // Keycloak API不直接返回总数，这里使用当前页数据量
                result.put("current", page);
                result.put("size", size);
                result.put("pages", (events.size() + size - 1) / size);
                
                return JsonResponse.buildSuccess(result);
            } else {
                return JsonResponse.buildFailure("获取管理员事件列表失败");
            }
            
        } catch (Exception e) {
            logger.error("获取管理员事件列表失败", e);
            return JsonResponse.buildFailure("获取管理员事件列表失败: " + e.getMessage());
        }
    }
}