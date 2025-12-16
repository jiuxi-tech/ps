package com.jiuxi.admin.core.service.impl;

import com.jiuxi.admin.core.bean.entity.TpKeycloakAccount;
import com.jiuxi.admin.core.service.KeycloakSyncService;
import com.jiuxi.admin.core.service.TpKeycloakAccountService;
import com.jiuxi.admin.core.service.TpSystemConfigService;
import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.admin.security.credential.CredentialType;
import com.jiuxi.common.util.PhoneEncryptionUtils;
import com.jiuxi.module.user.app.service.UserAccountService;
import com.jiuxi.module.user.app.service.UserPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Keycloak同步服务实现类
 *
 * @author System
 * @since 2025-01-21
 */
@Slf4j
@Service
public class KeycloakSyncServiceImpl implements KeycloakSyncService {

    @Autowired
    private TpKeycloakAccountService tpKeycloakAccountService;

    @Autowired
    private TpSystemConfigService tpSystemConfigService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired(required = false)
    private ObjectProvider<UserAccountService> userAccountServiceProvider;

    @Autowired(required = false)
    private ObjectProvider<UserPersonService> userPersonServiceProvider;

    @Value("${keycloak.server-url:http://localhost:8080}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm:ps-realm}")
    private String keycloakRealm;

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
        return tpSystemConfigService.getConfigValue("keycloak.admin.password", "cotticotti");
    }

    @Override
    public KeycloakSyncResult syncAccountToKeycloak(String accountId, String username, String password, String creator) {
        try {
            log.info("开始同步账号到Keycloak: accountId={}, username={}", accountId, username);

            // 1. 获取管理员访问令牌
            String adminToken = getAdminAccessToken();
            if (!StringUtils.hasText(adminToken)) {
                return KeycloakSyncResult.failure("获取Keycloak管理员令牌失败");
            }

            // 2. 检查用户是否已存在
            String existingUserId = findUserByUsername(adminToken, username);
            if (StringUtils.hasText(existingUserId)) {
                log.warn("用户已存在于Keycloak中: username={}, userId={}", username, existingUserId);
                // 更新本地Keycloak账号记录
                boolean localResult = tpKeycloakAccountService.createOrUpdateKeycloakAccount(accountId, username, password, creator);
                if (localResult) {
                    // 更新Keycloak用户ID
                    TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
                    if (keycloakAccount != null) {
                        keycloakAccount.setKcUserId(existingUserId);
                        tpKeycloakAccountService.updateById(keycloakAccount);
                    }

                    // 推送人员姓名、工号、邮箱到Keycloak（已存在用户也做属性更新）
                    Map<String, Object> extra = buildPersonExtras(accountId);
                    String email = (String) extra.getOrDefault("email", null);
                    String firstName = (String) extra.getOrDefault("firstName", null);
                    Map<String, Object> attributes = (Map<String, Object>) extra.getOrDefault("attributes", Collections.emptyMap());
                    boolean attrUpdate = updateKeycloakUserInfo(adminToken, existingUserId, username, null, email, firstName, attributes);
                    if (!attrUpdate) {
                        log.warn("Keycloak用户属性更新失败(存在用户): accountId={}, userId={}", accountId, existingUserId);
                    }
                    return KeycloakSyncResult.success("用户已存在，本地记录已更新", existingUserId);
                } else {
                    return KeycloakSyncResult.failure("更新本地Keycloak账号记录失败");
                }
            }

            // 3. 创建新用户
            Map<String, Object> extra = buildPersonExtras(accountId);
            String email = (String) extra.getOrDefault("email", null);
            String firstName = (String) extra.getOrDefault("firstName", null);
            Map<String, Object> attributes = (Map<String, Object>) extra.getOrDefault("attributes", Collections.emptyMap());
            String keycloakUserId = createKeycloakUser(adminToken, username, password, email, firstName, attributes);
            if (!StringUtils.hasText(keycloakUserId)) {
                return KeycloakSyncResult.failure("在Keycloak中创建用户失败");
            }

            // 4. 保存本地Keycloak账号记录
            boolean localResult = tpKeycloakAccountService.createOrUpdateKeycloakAccount(accountId, username, password, creator);
            if (!localResult) {
                log.error("本地Keycloak账号记录保存失败，但Keycloak用户已创建: userId={}", keycloakUserId);
                return KeycloakSyncResult.failure("本地记录保存失败，但Keycloak用户已创建");
            }

            // 5. 更新Keycloak用户ID
            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount != null) {
                keycloakAccount.setKcUserId(keycloakUserId);
                tpKeycloakAccountService.updateById(keycloakAccount);
            }

            log.info("账号同步到Keycloak成功: accountId={}, username={}, keycloakUserId={}", accountId, username, keycloakUserId);
            return KeycloakSyncResult.success("账号同步成功", keycloakUserId);

        } catch (Exception e) {
            log.error("同步账号到Keycloak失败: accountId={}, username={}, error={}", accountId, username, e.getMessage(), e);
            return KeycloakSyncResult.failure("同步失败: " + e.getMessage(), e);
        }
    }

    @Override
    public KeycloakSyncResult updateKeycloakUser(String accountId, String username, String password, String updater) {
        try {
            log.info("开始更新Keycloak用户: accountId={}, username={}", accountId, username);

            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount == null || !StringUtils.hasText(keycloakAccount.getKcUserId())) {
                log.warn("本地未记录kcUserId，尝试按用户名在Keycloak中查找: accountId={}, username={}", accountId, username);
            }

            String adminToken = getAdminAccessToken();
            if (!StringUtils.hasText(adminToken)) {
                return KeycloakSyncResult.failure("获取Keycloak管理员令牌失败");
            }

            // 读取人员信息，推送邮箱/姓名及自定义属性
            Map<String, Object> extra = buildPersonExtras(accountId);
            String email = (String) extra.getOrDefault("email", null);
            String firstName = (String) extra.getOrDefault("firstName", null);
            Map<String, Object> attributes = (Map<String, Object>) extra.getOrDefault("attributes", Collections.emptyMap());

            // 先确保Keycloak端存在用户，并校准kcUserId
            String existingUserId = findUserByUsername(adminToken, username);
            if (!StringUtils.hasText(existingUserId)) {
                log.warn("Keycloak中未找到该用户名对应的用户，准备创建: username={}", username);
                String createdUserId = createKeycloakUser(adminToken, username, 
                        // 如果未提供密码，则使用本地记录中的密码或生成新密码
                        (StringUtils.hasText(password) ? password : null), 
                        email, firstName, attributes);
                if (!StringUtils.hasText(createdUserId)) {
                    return KeycloakSyncResult.failure("在Keycloak中创建用户失败");
                }
                // 更新本地记录的kcUserId
                TpKeycloakAccount refreshed = tpKeycloakAccountService.getByAccountId(accountId);
                if (refreshed != null) {
                    refreshed.setKcUserId(createdUserId);
                    tpKeycloakAccountService.updateById(refreshed);
                }
                // 创建后，无需再次update基本信息（已携带email/firstName/attributes），但如有密码且之前为null需重置
                if (StringUtils.hasText(password)) {
                    // 已在create时设置密码，无需重复
                    log.info("Keycloak用户创建完成并已设置密码: accountId={}, userId={}", accountId, createdUserId);
                }
                // 更新本地账号记录（用户名/密码等）
                boolean localResultAfterCreate = tpKeycloakAccountService.createOrUpdateKeycloakAccount(accountId, username, password, updater);
                if (!localResultAfterCreate) {
                    log.warn("Keycloak用户创建成功，但本地记录更新失败: accountId={}", accountId);
                }
                return KeycloakSyncResult.success("用户创建并完成信息同步", createdUserId);
            }

            // 如本地kcUserId与Keycloak查到的不一致，进行校准
            if (keycloakAccount != null && StringUtils.hasText(keycloakAccount.getKcUserId())
                    && !existingUserId.equals(keycloakAccount.getKcUserId())) {
                log.warn("本地kcUserId与Keycloak不一致，进行校准: local={}, remote={}", keycloakAccount.getKcUserId(), existingUserId);
                keycloakAccount.setKcUserId(existingUserId);
                tpKeycloakAccountService.updateById(keycloakAccount);
            }

            // 执行信息更新（用户名/邮箱/姓名/属性；密码非空则重置）
            boolean updateResult = updateKeycloakUserInfo(adminToken, existingUserId, username, password, email, firstName, attributes);
            if (!updateResult) {
                return KeycloakSyncResult.failure("更新Keycloak用户信息失败");
            }

            // 更新本地记录
            boolean localResult = tpKeycloakAccountService.createOrUpdateKeycloakAccount(accountId, username, password, updater);
            if (!localResult) {
                log.warn("Keycloak用户更新成功，但本地记录更新失败: accountId={}", accountId);
            }

            log.info("Keycloak用户更新成功: accountId={}, username={}", accountId, username);
            return KeycloakSyncResult.success("用户更新成功", existingUserId);

        } catch (Exception e) {
            log.error("更新Keycloak用户失败: accountId={}, username={}, error={}", accountId, username, e.getMessage(), e);
            return KeycloakSyncResult.failure("更新失败: " + e.getMessage(), e);
        }
    }

    @Override
    public KeycloakSyncResult disableKeycloakUser(String accountId) {
        try {
            log.info("开始禁用Keycloak用户: accountId={}", accountId);

            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount == null || !StringUtils.hasText(keycloakAccount.getKcUserId())) {
                return KeycloakSyncResult.failure("未找到对应的Keycloak账号记录");
            }

            String adminToken = getAdminAccessToken();
            if (!StringUtils.hasText(adminToken)) {
                return KeycloakSyncResult.failure("获取Keycloak管理员令牌失败");
            }

            boolean disableResult = setKeycloakUserEnabled(adminToken, keycloakAccount.getKcUserId(), false);
            if (!disableResult) {
                return KeycloakSyncResult.failure("禁用Keycloak用户失败");
            }

            // 更新本地记录
            tpKeycloakAccountService.disableKeycloakAccount(accountId);

            log.info("Keycloak用户禁用成功: accountId={}", accountId);
            return KeycloakSyncResult.success("用户禁用成功", keycloakAccount.getKcUserId());

        } catch (Exception e) {
            log.error("禁用Keycloak用户失败: accountId={}, error={}", accountId, e.getMessage(), e);
            return KeycloakSyncResult.failure("禁用失败: " + e.getMessage(), e);
        }
    }

    @Override
    public KeycloakSyncResult enableKeycloakUser(String accountId) {
        try {
            log.info("开始启用Keycloak用户: accountId={}", accountId);

            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount == null || !StringUtils.hasText(keycloakAccount.getKcUserId())) {
                return KeycloakSyncResult.failure("未找到对应的Keycloak账号记录");
            }

            String adminToken = getAdminAccessToken();
            if (!StringUtils.hasText(adminToken)) {
                return KeycloakSyncResult.failure("获取Keycloak管理员令牌失败");
            }

            boolean enableResult = setKeycloakUserEnabled(adminToken, keycloakAccount.getKcUserId(), true);
            if (!enableResult) {
                return KeycloakSyncResult.failure("启用Keycloak用户失败");
            }

            // 更新本地记录
            tpKeycloakAccountService.enableKeycloakAccount(accountId);

            log.info("Keycloak用户启用成功: accountId={}", accountId);
            return KeycloakSyncResult.success("用户启用成功", keycloakAccount.getKcUserId());

        } catch (Exception e) {
            log.error("启用Keycloak用户失败: accountId={}, error={}", accountId, e.getMessage(), e);
            return KeycloakSyncResult.failure("启用失败: " + e.getMessage(), e);
        }
    }

    @Override
    public KeycloakSyncResult deleteKeycloakUser(String accountId) {
        try {
            log.info("开始删除Keycloak用户: accountId={}", accountId);

            TpKeycloakAccount keycloakAccount = tpKeycloakAccountService.getByAccountId(accountId);
            if (keycloakAccount == null || !StringUtils.hasText(keycloakAccount.getKcUserId())) {
                return KeycloakSyncResult.failure("未找到对应的Keycloak账号记录");
            }

            String adminToken = getAdminAccessToken();
            if (!StringUtils.hasText(adminToken)) {
                return KeycloakSyncResult.failure("获取Keycloak管理员令牌失败");
            }

            boolean deleteResult = deleteKeycloakUserById(adminToken, keycloakAccount.getKcUserId());
            if (!deleteResult) {
                return KeycloakSyncResult.failure("删除Keycloak用户失败");
            }

            // 删除本地记录
            tpKeycloakAccountService.removeById(keycloakAccount.getId());

            log.info("Keycloak用户删除成功: accountId={}", accountId);
            return KeycloakSyncResult.success("用户删除成功", keycloakAccount.getKcUserId());

        } catch (Exception e) {
            log.error("删除Keycloak用户失败: accountId={}, error={}", accountId, e.getMessage(), e);
            return KeycloakSyncResult.failure("删除失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取管理员访问令牌
     */
    private String getAdminAccessToken() {
        try {
            String tokenUrl = keycloakServerUrl + "/realms/master/protocol/openid-connect/token";

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
        } catch (Exception e) {
            log.error("获取Keycloak管理员令牌失败: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据用户名查找用户
     */
    private String findUserByUsername(String adminToken, String username) {
        try {
            String usersUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users?username=" + username + "&exact=true";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(usersUrl, HttpMethod.GET, request, List.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && !response.getBody().isEmpty()) {
                Map<String, Object> user = (Map<String, Object>) response.getBody().get(0);
                return (String) user.get("id");
            }
        } catch (Exception e) {
            log.error("查找Keycloak用户失败: username={}, error={}", username, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 创建Keycloak用户
     */
    private String createKeycloakUser(String adminToken, String username, String password) {
        try {
            return createKeycloakUser(adminToken, username, password, null, null, Collections.emptyMap());
        } catch (Exception e) {
            log.error("创建Keycloak用户失败: username={}, error={}", username, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 更新Keycloak用户信息
     */
    private boolean updateKeycloakUserInfo(String adminToken, String userId, String username, String password, String email, String firstName, Map<String, Object> attributes) {
        try {
            String userUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users/" + userId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> userRepresentation = new HashMap<>();
            userRepresentation.put("username", username);
            userRepresentation.put("enabled", true);
            if (StringUtils.hasText(email)) {
                userRepresentation.put("email", email);
                userRepresentation.put("emailVerified", true);
            }
            if (StringUtils.hasText(firstName)) {
                userRepresentation.put("firstName", firstName);
            }
            if (attributes != null && !attributes.isEmpty()) {
                userRepresentation.put("attributes", attributes);
            }

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(userRepresentation, headers);
            ResponseEntity<String> response = restTemplate.exchange(userUrl, HttpMethod.PUT, request, String.class);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                // 如果提供了新密码，则重置密码
                if (StringUtils.hasText(password)) {
                    return resetKeycloakUserPassword(adminToken, userId, password);
                }
                return true;
            }
        } catch (Exception e) {
            log.error("更新Keycloak用户信息失败: userId={}, username={}, error={}", userId, username, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 创建Keycloak用户（含邮箱、姓名、属性）
     */
    private String createKeycloakUser(String adminToken, String username, String password, String email, String firstName, Map<String, Object> attributes) {
        String usersUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> userRepresentation = new HashMap<>();
        userRepresentation.put("username", username);
        userRepresentation.put("enabled", true);
        if (StringUtils.hasText(email)) {
            userRepresentation.put("email", email);
            userRepresentation.put("emailVerified", true);
        }
        if (StringUtils.hasText(firstName)) {
            userRepresentation.put("firstName", firstName);
        }
        if (attributes != null && !attributes.isEmpty()) {
            userRepresentation.put("attributes", attributes);
        }

        // 设置密码（有值时）
        if (StringUtils.hasText(password)) {
            Map<String, Object> credential = new HashMap<>();
            credential.put("type", "password");
            credential.put("value", password);
            credential.put("temporary", false);
            userRepresentation.put("credentials", Arrays.asList(credential));
        }

        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(userRepresentation, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(usersUrl, request, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                String location = response.getHeaders().getFirst("Location");
                if (StringUtils.hasText(location)) {
                    return location.substring(location.lastIndexOf('/') + 1);
                }
            } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                // 已存在，回查ID
                String existId = findUserByUsername(adminToken, username);
                if (StringUtils.hasText(existId)) {
                    return existId;
                }
            }
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            HttpStatus status = e.getStatusCode();
            String body = e.getResponseBodyAsString();
            log.error("创建Keycloak用户失败: username={}, status={}, body={}", username, status.value(), body);

            if (status == HttpStatus.CONFLICT) {
                // 已存在，回查ID
                String existId = findUserByUsername(adminToken, username);
                if (StringUtils.hasText(existId)) {
                    return existId;
                }
            }
            if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
                // 500未知错误，尝试最小化创建再补充更新
                String minimalId = createKeycloakUserMinimal(adminToken, username);
                if (StringUtils.hasText(minimalId)) {
                    // 密码与信息补充
                    boolean pwdOk = true;
                    if (StringUtils.hasText(password)) {
                        pwdOk = resetKeycloakUserPassword(adminToken, minimalId, password);
                    }
                    boolean infoOk = updateKeycloakUserInfo(adminToken, minimalId, username, null, email, firstName, attributes);
                    if (pwdOk && infoOk) {
                        return minimalId;
                    }
                }
            }
        } catch (Exception e) {
            log.error("创建Keycloak用户失败: username={}, error={}", username, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 最小化创建Keycloak用户（仅用户名与启用），用于服务端500回退场景
     */
    private String createKeycloakUserMinimal(String adminToken, String username) {
        try {
            String usersUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> userRepresentation = new HashMap<>();
            userRepresentation.put("username", username);
            userRepresentation.put("enabled", true);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(userRepresentation, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(usersUrl, request, String.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                String location = response.getHeaders().getFirst("Location");
                if (StringUtils.hasText(location)) {
                    return location.substring(location.lastIndexOf('/') + 1);
                }
            } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                String existId = findUserByUsername(adminToken, username);
                if (StringUtils.hasText(existId)) {
                    return existId;
                }
            }
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            log.error("最小化创建Keycloak用户失败: username={}, status={}, body={}", username, e.getStatusCode().value(), e.getResponseBodyAsString());
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                String existId = findUserByUsername(adminToken, username);
                if (StringUtils.hasText(existId)) {
                    return existId;
                }
            }
        } catch (Exception e) {
            log.error("最小化创建Keycloak用户失败: username={}, error={}", username, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 构建人员额外信息（邮箱、姓名、工号、自定义属性）
     */
    private Map<String, Object> buildPersonExtras(String accountId) {
        Map<String, Object> result = new HashMap<>();
        try {
            UserAccountService userAccountService = userAccountServiceProvider.getIfAvailable();
            UserPersonService userPersonService = userPersonServiceProvider.getIfAvailable();
            if (userAccountService == null || userPersonService == null) {
                log.warn("无法加载用户服务，跳过人员扩展信息构建: accountId={}", accountId);
                return result;
            }
            TpAccountVO accountVO = userAccountService.selectByAccountId(accountId);
            if (accountVO == null || !StringUtils.hasText(accountVO.getPersonId())) {
                log.warn("未找到账号或人员ID为空，跳过扩展信息: accountId={}", accountId);
                return result;
            }
            TpPersonBasicinfoVO person = userPersonService.getPersonBasicinfo(accountVO.getPersonId());
            if (person == null) {
                log.warn("未找到人员信息，跳过扩展信息: personId={}", accountVO.getPersonId());
                return result;
            }
            String email = person.getEmail();
            String personName = person.getPersonName();
            String personNo = person.getPersonNo();
            String username = accountVO.getUsername();
            String phone = accountVO.getPhone();
            String idcard = accountVO.getIdcard();

            result.put("email", email);
            result.put("firstName", personName);

            Map<String, Object> attributes = new HashMap<>();
            if (StringUtils.hasText(personName)) {
                attributes.put("PERSON_NAME", Collections.singletonList(personName));
            }
            if (StringUtils.hasText(personNo)) {
                attributes.put("PERSON_NO", Collections.singletonList(personNo));
            }
            if (StringUtils.hasText(email)) {
                attributes.put("EMAIL", Collections.singletonList(email));
            }
            if (StringUtils.hasText(username)) {
                attributes.put("USERNAME", Collections.singletonList(username));
            }
            // 添加手机号（需要解密）
            if (StringUtils.hasText(phone)) {
                try {
                    String decryptedPhone = PhoneEncryptionUtils.safeDecrypt(phone);
                    if (StringUtils.hasText(decryptedPhone)) {
                        attributes.put("PHONE", Collections.singletonList(decryptedPhone));
                    }
                } catch (Exception ex) {
                    log.warn("手机号解密失败，跳过手机号属性: accountId={}", accountId);
                }
            }
            // 添加身份证号
            if (StringUtils.hasText(idcard)) {
                attributes.put("IDCARD", Collections.singletonList(idcard));
            }
            result.put("attributes", attributes);
        } catch (Exception e) {
            log.warn("构建人员扩展信息失败: accountId={}, error={}", accountId, e.getMessage());
        }
        return result;
    }

    /**
     * 重置Keycloak用户密码
     */
    private boolean resetKeycloakUserPassword(String adminToken, String userId, String password) {
        try {
            String passwordUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users/" + userId + "/reset-password";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> credential = new HashMap<>();
            credential.put("type", "password");
            credential.put("value", password);
            credential.put("temporary", false);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(credential, headers);
            ResponseEntity<String> response = restTemplate.exchange(passwordUrl, HttpMethod.PUT, request, String.class);

            return response.getStatusCode() == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            log.error("重置Keycloak用户密码失败: userId={}, error={}", userId, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 设置Keycloak用户启用/禁用状态
     */
    private boolean setKeycloakUserEnabled(String adminToken, String userId, boolean enabled) {
        try {
            String userUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users/" + userId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> userRepresentation = new HashMap<>();
            userRepresentation.put("enabled", enabled);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(userRepresentation, headers);
            ResponseEntity<String> response = restTemplate.exchange(userUrl, HttpMethod.PUT, request, String.class);

            return response.getStatusCode() == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            log.error("设置Keycloak用户状态失败: userId={}, enabled={}, error={}", userId, enabled, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 删除Keycloak用户
     */
    private boolean deleteKeycloakUserById(String adminToken, String userId) {
        try {
            String userUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users/" + userId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(userUrl, HttpMethod.DELETE, request, String.class);

            return response.getStatusCode() == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            log.error("删除Keycloak用户失败: userId={}, error={}", userId, e.getMessage(), e);
        }
        return false;
    }

    // ==================== 多凭据同步方法 ====================

    @Override
    public MultiCredentialSyncResult syncMultipleCredentials(String accountId, String password, String creator) {
        try {
            log.info("开始多凭据同步: accountId={}", accountId);

            // 1. 查询账号信息
            UserAccountService userAccountService = userAccountServiceProvider.getIfAvailable();
            if (userAccountService == null) {
                return MultiCredentialSyncResult.failure("无法加载用户服务");
            }

            TpAccountVO account = userAccountService.selectByAccountId(accountId);
            if (account == null) {
                return MultiCredentialSyncResult.failure("账号不存在: accountId=" + accountId);
            }

            // 2. 获取管理员token
            String adminToken = getAdminAccessToken();
            if (!StringUtils.hasText(adminToken)) {
                return MultiCredentialSyncResult.failure("获取Keycloak管理员令牌失败");
            }

            // 3. 构建人员扩展信息
            Map<String, Object> personExtras = buildPersonExtras(accountId);
            String email = (String) personExtras.getOrDefault("email", null);
            String firstName = (String) personExtras.getOrDefault("firstName", null);

            // 4. 构建公共attributes
            Map<String, Object> commonAttributes = buildCommonAttributes(account, personExtras);

            // 5. 收集需要同步的凭据
            List<CredentialInfo> credentials = collectCredentials(account);
            log.info("账号凭据收集完成: accountId={}, 凭据数量={}", accountId, credentials.size());

            // 6. 批量创建Keycloak用户
            MultiCredentialSyncResult result = new MultiCredentialSyncResult();
            result.setTotalCredentials(credentials.size());

            for (CredentialInfo credentialInfo : credentials) {
                try {
                    // 6.1 添加凭据类型到attributes
                    Map<String, Object> attributes = new HashMap<>(commonAttributes);
                    attributes.put("credentialType",
                            Collections.singletonList(credentialInfo.getType().name()));

                    // 6.2 检查用户是否已存在
                    String existingUserId = findUserByUsername(adminToken, credentialInfo.getValue());
                    if (StringUtils.hasText(existingUserId)) {
                        // 用户已存在，更新信息
                        log.info("Keycloak用户已存在，执行更新: type={}, username={}",
                                credentialInfo.getType(), maskCredential(credentialInfo.getValue(), credentialInfo.getType()));
                        boolean updated = updateKeycloakUserInfo(adminToken, existingUserId,
                                credentialInfo.getValue(), password, email, firstName, attributes);
                        result.addDetail(new CredentialSyncDetail(
                                credentialInfo.getType(),
                                maskCredential(credentialInfo.getValue(), credentialInfo.getType()),
                                updated,
                                existingUserId,
                                updated ? "用户已存在，更新成功" : "用户已存在，更新失败",
                                null
                        ));
                    } else {
                        // 创建新用户
                        log.info("创建Keycloak用户: type={}, username={}",
                                credentialInfo.getType(), maskCredential(credentialInfo.getValue(), credentialInfo.getType()));
                        String userId = createKeycloakUser(adminToken,
                                credentialInfo.getValue(),
                                password,
                                email,
                                firstName,
                                attributes);

                        result.addDetail(new CredentialSyncDetail(
                                credentialInfo.getType(),
                                maskCredential(credentialInfo.getValue(), credentialInfo.getType()),
                                StringUtils.hasText(userId),
                                userId,
                                StringUtils.hasText(userId) ? "用户创建成功" : "用户创建失败",
                                null
                        ));
                    }
                } catch (Exception e) {
                    log.error("同步凭据失败: type={}, error={}", credentialInfo.getType(), e.getMessage(), e);
                    result.addDetail(new CredentialSyncDetail(
                            credentialInfo.getType(),
                            maskCredential(credentialInfo.getValue(), credentialInfo.getType()),
                            false,
                            null,
                            "同步失败: " + e.getMessage(),
                            e
                    ));
                }
            }

            // 7. 统计结果
            result.calculateStatistics();
            log.info("多凭据同步完成: accountId={}, 总数={}, 成功={}, 失败={}",
                    accountId, result.getTotalCredentials(), result.getSuccessCount(), result.getFailureCount());
            return result;

        } catch (Exception e) {
            log.error("多凭据同步失败: accountId={}, error={}", accountId, e.getMessage(), e);
            return MultiCredentialSyncResult.failure("同步失败: " + e.getMessage());
        }
    }

    @Override
    public MultiCredentialSyncResult updatePasswordForAllCredentials(String accountId, String newPassword, String updater) {
        try {
            log.info("开始更新所有凭据密码: accountId={}", accountId);

            // 1. 查询账号信息
            UserAccountService userAccountService = userAccountServiceProvider.getIfAvailable();
            if (userAccountService == null) {
                return MultiCredentialSyncResult.failure("无法加载用户服务");
            }

            TpAccountVO account = userAccountService.selectByAccountId(accountId);
            if (account == null) {
                return MultiCredentialSyncResult.failure("账号不存在: accountId=" + accountId);
            }

            // 2. 获取管理员token
            String adminToken = getAdminAccessToken();
            if (!StringUtils.hasText(adminToken)) {
                return MultiCredentialSyncResult.failure("获取Keycloak管理员令牌失败");
            }

            // 3. 收集所有凭据
            List<CredentialInfo> credentials = collectCredentials(account);
            log.info("收集凭据完成: accountId={}, 凭据数量={}", accountId, credentials.size());

            // 4. 批量更新密码
            MultiCredentialSyncResult result = new MultiCredentialSyncResult();
            result.setTotalCredentials(credentials.size());

            for (CredentialInfo credentialInfo : credentials) {
                try {
                    // 4.1 查找Keycloak用户
                    String userId = findUserByUsername(adminToken, credentialInfo.getValue());
                    if (!StringUtils.hasText(userId)) {
                        log.warn("Keycloak用户不存在，跳过密码更新: username={}",
                                maskCredential(credentialInfo.getValue(), credentialInfo.getType()));
                        result.addDetail(new CredentialSyncDetail(
                                credentialInfo.getType(),
                                maskCredential(credentialInfo.getValue(), credentialInfo.getType()),
                                false,
                                null,
                                "Keycloak用户不存在",
                                null
                        ));
                        continue;
                    }

                    // 4.2 重置密码
                    boolean updated = resetKeycloakUserPassword(adminToken, userId, newPassword);
                    result.addDetail(new CredentialSyncDetail(
                            credentialInfo.getType(),
                            maskCredential(credentialInfo.getValue(), credentialInfo.getType()),
                            updated,
                            userId,
                            updated ? "密码更新成功" : "密码更新失败",
                            null
                    ));
                } catch (Exception e) {
                    log.error("更新凭据密码失败: type={}, error={}", credentialInfo.getType(), e.getMessage(), e);
                    result.addDetail(new CredentialSyncDetail(
                            credentialInfo.getType(),
                            maskCredential(credentialInfo.getValue(), credentialInfo.getType()),
                            false,
                            null,
                            "更新失败: " + e.getMessage(),
                            e
                    ));
                }
            }

            // 5. 统计结果
            result.calculateStatistics();
            log.info("所有凭据密码更新完成: accountId={}, 总数={}, 成功={}, 失败={}",
                    accountId, result.getTotalCredentials(), result.getSuccessCount(), result.getFailureCount());
            return result;

        } catch (Exception e) {
            log.error("更新所有凭据密码失败: accountId={}, error={}", accountId, e.getMessage(), e);
            return MultiCredentialSyncResult.failure("更新失败: " + e.getMessage());
        }
    }

    @Override
    public MultiCredentialSyncResult deleteAllCredentials(String accountId) {
        return performOperationOnAllCredentials(accountId, "delete", "删除");
    }

    @Override
    public MultiCredentialSyncResult enableAllCredentials(String accountId) {
        return performOperationOnAllCredentials(accountId, "enable", "启用");
    }

    @Override
    public MultiCredentialSyncResult disableAllCredentials(String accountId) {
        return performOperationOnAllCredentials(accountId, "disable", "禁用");
    }

    /**
     * 对所有凭据执行操作（删除/启用/禁用）
     */
    private MultiCredentialSyncResult performOperationOnAllCredentials(String accountId, String operation, String operationName) {
        try {
            log.info("开始{}所有凭据: accountId={}", operationName, accountId);

            // 1. 查询账号信息
            UserAccountService userAccountService = userAccountServiceProvider.getIfAvailable();
            if (userAccountService == null) {
                return MultiCredentialSyncResult.failure("无法加载用户服务");
            }

            TpAccountVO account = userAccountService.selectByAccountId(accountId);
            if (account == null) {
                return MultiCredentialSyncResult.failure("账号不存在: accountId=" + accountId);
            }

            // 2. 获取管理员token
            String adminToken = getAdminAccessToken();
            if (!StringUtils.hasText(adminToken)) {
                return MultiCredentialSyncResult.failure("获取Keycloak管理员令牌失败");
            }

            // 3. 收集所有凭据
            List<CredentialInfo> credentials = collectCredentials(account);
            log.info("收集凭据完成: accountId={}, 凭据数量={}", accountId, credentials.size());

            // 4. 批量执行操作
            MultiCredentialSyncResult result = new MultiCredentialSyncResult();
            result.setTotalCredentials(credentials.size());

            for (CredentialInfo credentialInfo : credentials) {
                try {
                    // 4.1 查找Keycloak用户
                    String userId = findUserByUsername(adminToken, credentialInfo.getValue());
                    if (!StringUtils.hasText(userId)) {
                        log.warn("Keycloak用户不存在，跳过{}操作: username={}",
                                operationName, maskCredential(credentialInfo.getValue(), credentialInfo.getType()));
                        result.addDetail(new CredentialSyncDetail(
                                credentialInfo.getType(),
                                maskCredential(credentialInfo.getValue(), credentialInfo.getType()),
                                false,
                                null,
                                "Keycloak用户不存在",
                                null
                        ));
                        continue;
                    }

                    // 4.2 执行操作
                    boolean success = false;
                    switch (operation) {
                        case "delete":
                            success = deleteKeycloakUserById(adminToken, userId);
                            break;
                        case "enable":
                            success = setKeycloakUserEnabled(adminToken, userId, true);
                            break;
                        case "disable":
                            success = setKeycloakUserEnabled(adminToken, userId, false);
                            break;
                    }

                    result.addDetail(new CredentialSyncDetail(
                            credentialInfo.getType(),
                            maskCredential(credentialInfo.getValue(), credentialInfo.getType()),
                            success,
                            userId,
                            success ? operationName + "成功" : operationName + "失败",
                            null
                    ));
                } catch (Exception e) {
                    log.error("{}凭据失败: type={}, error={}", operationName, credentialInfo.getType(), e.getMessage(), e);
                    result.addDetail(new CredentialSyncDetail(
                            credentialInfo.getType(),
                            maskCredential(credentialInfo.getValue(), credentialInfo.getType()),
                            false,
                            null,
                            operationName + "失败: " + e.getMessage(),
                            e
                    ));
                }
            }

            // 5. 统计结果
            result.calculateStatistics();
            log.info("{}所有凭据完成: accountId={}, 总数={}, 成功={}, 失败={}",
                    operationName, accountId, result.getTotalCredentials(), result.getSuccessCount(), result.getFailureCount());
            return result;

        } catch (Exception e) {
            log.error("{}所有凭据失败: accountId={}, error={}", operationName, accountId, e.getMessage(), e);
            return MultiCredentialSyncResult.failure(operationName + "失败: " + e.getMessage());
        }
    }

    /**
     * 收集账号的所有凭据
     */
    private List<CredentialInfo> collectCredentials(TpAccountVO account) {
        List<CredentialInfo> credentials = new ArrayList<>();

        // 1. 账号名（必有）
        if (StringUtils.hasText(account.getUsername())) {
            credentials.add(new CredentialInfo(
                    CredentialType.USERNAME,
                    account.getUsername()
            ));
        }

        // 2. 手机号（需要解密）
        if (StringUtils.hasText(account.getPhone())) {
            try {
                String decryptedPhone = PhoneEncryptionUtils.safeDecrypt(account.getPhone());
                if (StringUtils.hasText(decryptedPhone)) {
                    credentials.add(new CredentialInfo(
                            CredentialType.PHONE,
                            decryptedPhone
                    ));
                }
            } catch (Exception e) {
                log.warn("手机号解密失败，跳过手机号凭据: accountId={}", account.getAccountId());
            }
        }

        // 3. 身份证号
        if (StringUtils.hasText(account.getIdcard())) {
            credentials.add(new CredentialInfo(
                    CredentialType.IDCARD,
                    account.getIdcard()
            ));
        }

        return credentials;
    }

    /**
     * 构建公共attributes
     */
    private Map<String, Object> buildCommonAttributes(TpAccountVO account, Map<String, Object> personExtras) {
        Map<String, Object> attributes = new HashMap<>();

        // 基本信息
        if (StringUtils.hasText(account.getAccountId())) {
            attributes.put("accountId", Collections.singletonList(account.getAccountId()));
        }
        if (StringUtils.hasText(account.getPersonId())) {
            attributes.put("personId", Collections.singletonList(account.getPersonId()));
        }
        if (StringUtils.hasText(account.getTenantId())) {
            attributes.put("tenantId", Collections.singletonList(account.getTenantId()));
        }

        // 凭据信息（用于追溯）
        if (StringUtils.hasText(account.getUsername())) {
            attributes.put("USERNAME", Collections.singletonList(account.getUsername()));
        }
        if (StringUtils.hasText(account.getPhone())) {
            try {
                String decryptedPhone = PhoneEncryptionUtils.safeDecrypt(account.getPhone());
                if (StringUtils.hasText(decryptedPhone)) {
                    attributes.put("PHONE", Collections.singletonList(decryptedPhone));
                }
            } catch (Exception e) {
                log.warn("手机号解密失败，跳过手机号属性: accountId={}", account.getAccountId());
            }
        }
        if (StringUtils.hasText(account.getIdcard())) {
            attributes.put("IDCARD", Collections.singletonList(account.getIdcard()));
        }

        // 人员信息
        Map<String, Object> personAttrs = (Map<String, Object>) personExtras.getOrDefault("attributes", Collections.emptyMap());
        attributes.putAll(personAttrs);

        return attributes;
    }

    /**
     * 凭据脱敏
     */
    private String maskCredential(String credential, CredentialType type) {
        if (!StringUtils.hasText(credential)) {
            return "";
        }

        switch (type) {
            case PHONE:
                // 手机号：138****1234
                if (credential.length() == 11) {
                    return credential.substring(0, 3) + "****" + credential.substring(7);
                }
                return credential;

            case IDCARD:
                // 身份证号：420106********1234
                if (credential.length() == 18) {
                    return credential.substring(0, 6) + "********" + credential.substring(14);
                } else if (credential.length() == 15) {
                    return credential.substring(0, 6) + "*******" + credential.substring(13);
                }
                return credential;

            case USERNAME:
            default:
                // 账号名不脱敏
                return credential;
        }
    }

    /**
     * 凭据信息类
     */
    private static class CredentialInfo {
        private final CredentialType type;
        private final String value;

        public CredentialInfo(CredentialType type, String value) {
            this.type = type;
            this.value = value;
        }

        public CredentialType getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }
}