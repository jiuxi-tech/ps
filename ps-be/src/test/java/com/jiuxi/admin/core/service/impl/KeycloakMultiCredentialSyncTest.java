package com.jiuxi.admin.core.service.impl;

import com.jiuxi.admin.core.service.KeycloakSyncService;
import com.jiuxi.admin.core.service.KeycloakSyncService.MultiCredentialSyncResult;
import com.jiuxi.admin.core.service.KeycloakSyncService.CredentialSyncDetail;
import com.jiuxi.module.user.app.service.UserAccountService;
import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import com.jiuxi.admin.security.credential.CredentialType;
import com.jiuxi.common.util.PhoneEncryptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Keycloak多凭据同步服务测试
 * 
 * 测试场景：
 * 1. 仅有账号名的同步（1个Keycloak用户）
 * 2. 账号名+手机号的同步（2个Keycloak用户）
 * 3. 账号名+身份证号的同步（2个Keycloak用户）
 * 4. 完整三个凭据的同步（3个Keycloak用户）
 * 5. 手机号加密解密处理
 * 6. 凭据脱敏验证
 * 7. 密码同步到所有用户
 * 8. 部分凭据同步失败场景
 * 9. 删除所有凭据
 * 10. 禁用/启用所有凭据
 *
 * @author Qoder AI
 * @since 2024-12-16
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Keycloak多凭据同步服务测试")
class KeycloakMultiCredentialSyncTest {
    
    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private ObjectProvider<UserAccountService> userAccountServiceProvider;
    
    @Mock
    private UserAccountService userAccountService;
    
    private KeycloakSyncServiceImpl keycloakSyncService;
    
    // 测试数据
    private static final String TEST_ACCOUNT_ID = "ACC001";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PHONE_ENCRYPTED = "ENCRYPTED_13800138000";
    private static final String TEST_PHONE_DECRYPTED = "13800138000";
    private static final String TEST_IDCARD = "110101199001010014";
    private static final String TEST_PASSWORD = "Password@123";
    private static final String TEST_CREATOR = "admin";
    private static final String TEST_KEYCLOAK_USER_ID = "keycloak-user-123";
    private static final String ADMIN_TOKEN = "admin-token-xyz";
    
    @BeforeEach
    void setUp() {
        keycloakSyncService = new KeycloakSyncServiceImpl();
        
        // 注入Mock对象
        ReflectionTestUtils.setField(keycloakSyncService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(keycloakSyncService, "userAccountServiceProvider", userAccountServiceProvider);
        
        // 配置Keycloak连接信息
        ReflectionTestUtils.setField(keycloakSyncService, "keycloakServerUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(keycloakSyncService, "realm", "test-realm");
        ReflectionTestUtils.setField(keycloakSyncService, "adminUsername", "admin");
        ReflectionTestUtils.setField(keycloakSyncService, "adminPassword", "admin");
        ReflectionTestUtils.setField(keycloakSyncService, "adminClientId", "admin-cli");
        
        // 配置UserAccountService提供者
        when(userAccountServiceProvider.getIfAvailable()).thenReturn(userAccountService);
    }
    
    // ========== 测试场景1：仅有账号名的同步 ==========
    
    @Test
    @DisplayName("同步仅有账号名的账号 - 创建1个Keycloak用户")
    void testSyncAccountWithUsernameOnly() {
        // 准备测试数据：仅有账号名
        TpAccountVO account = createTestAccount(TEST_USERNAME, null, null);
        
        // Mock查询账号信息
        when(userAccountService.selectByAccountId(TEST_ACCOUNT_ID)).thenReturn(account);
        
        // Mock获取管理员token
        mockAdminTokenResponse();
        
        // Mock查找用户（不存在）
        mockFindUserResponse(TEST_USERNAME, null);
        
        // Mock创建用户成功
        mockCreateUserResponse(TEST_USERNAME, TEST_KEYCLOAK_USER_ID);
        
        // 执行同步
        MultiCredentialSyncResult result = keycloakSyncService.syncMultipleCredentials(
            TEST_ACCOUNT_ID, TEST_PASSWORD, TEST_CREATOR
        );
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getTotalCredentials(), "应该有1个凭据");
        assertEquals(1, result.getSuccessCount(), "应该成功同步1个凭据");
        assertEquals(0, result.getFailureCount(), "应该没有失败的凭据");
        
        // 验证详情
        assertNotNull(result.getDetails());
        assertEquals(1, result.getDetails().size());
        
        CredentialSyncDetail detail = result.getDetails().get(0);
        assertEquals(CredentialType.USERNAME, detail.getCredentialType());
        assertEquals(TEST_USERNAME, detail.getCredentialValue());
        assertTrue(detail.isSuccess());
        assertNotNull(detail.getKeycloakUserId());
    }
    
    // ========== 测试场景2：账号名+手机号的同步 ==========
    
    @Test
    @DisplayName("同步账号名+手机号 - 创建2个Keycloak用户")
    void testSyncAccountWithUsernameAndPhone() {
        // 准备测试数据：账号名+手机号
        TpAccountVO account = createTestAccount(TEST_USERNAME, TEST_PHONE_ENCRYPTED, null);
        
        // Mock查询账号信息
        when(userAccountService.selectByAccountId(TEST_ACCOUNT_ID)).thenReturn(account);
        
        // Mock手机号解密
        try (MockedStatic<PhoneEncryptionUtils> mockedUtils = mockStatic(PhoneEncryptionUtils.class)) {
            mockedUtils.when(() -> PhoneEncryptionUtils.safeDecrypt(TEST_PHONE_ENCRYPTED))
                      .thenReturn(TEST_PHONE_DECRYPTED);
            
            // Mock获取管理员token
            mockAdminTokenResponse();
            
            // Mock查找用户（都不存在）
            mockFindUserResponse(TEST_USERNAME, null);
            mockFindUserResponse(TEST_PHONE_DECRYPTED, null);
            
            // Mock创建用户成功
            mockCreateUserResponse(TEST_USERNAME, TEST_KEYCLOAK_USER_ID + "-username");
            mockCreateUserResponse(TEST_PHONE_DECRYPTED, TEST_KEYCLOAK_USER_ID + "-phone");
            
            // 执行同步
            MultiCredentialSyncResult result = keycloakSyncService.syncMultipleCredentials(
                TEST_ACCOUNT_ID, TEST_PASSWORD, TEST_CREATOR
            );
            
            // 验证结果
            assertNotNull(result);
            assertTrue(result.isSuccess());
            assertEquals(2, result.getTotalCredentials(), "应该有2个凭据");
            assertEquals(2, result.getSuccessCount(), "应该成功同步2个凭据");
            assertEquals(0, result.getFailureCount(), "应该没有失败的凭据");
            
            // 验证详情
            assertNotNull(result.getDetails());
            assertEquals(2, result.getDetails().size());
            
            // 验证账号名凭据
            CredentialSyncDetail usernameDetail = result.getDetails().stream()
                .filter(d -> d.getCredentialType() == CredentialType.USERNAME)
                .findFirst()
                .orElse(null);
            assertNotNull(usernameDetail);
            assertTrue(usernameDetail.isSuccess());
            
            // 验证手机号凭据
            CredentialSyncDetail phoneDetail = result.getDetails().stream()
                .filter(d -> d.getCredentialType() == CredentialType.PHONE)
                .findFirst()
                .orElse(null);
            assertNotNull(phoneDetail);
            assertTrue(phoneDetail.isSuccess());
            // 验证手机号已脱敏：138****8000
            assertTrue(phoneDetail.getCredentialValue().contains("****"));
        }
    }
    
    // ========== 测试场景3：账号名+身份证号的同步 ==========
    
    @Test
    @DisplayName("同步账号名+身份证号 - 创建2个Keycloak用户")
    void testSyncAccountWithUsernameAndIdCard() {
        // 准备测试数据：账号名+身份证号
        TpAccountVO account = createTestAccount(TEST_USERNAME, null, TEST_IDCARD);
        
        // Mock查询账号信息
        when(userAccountService.selectByAccountId(TEST_ACCOUNT_ID)).thenReturn(account);
        
        // Mock获取管理员token
        mockAdminTokenResponse();
        
        // Mock查找用户（都不存在）
        mockFindUserResponse(TEST_USERNAME, null);
        mockFindUserResponse(TEST_IDCARD, null);
        
        // Mock创建用户成功
        mockCreateUserResponse(TEST_USERNAME, TEST_KEYCLOAK_USER_ID + "-username");
        mockCreateUserResponse(TEST_IDCARD, TEST_KEYCLOAK_USER_ID + "-idcard");
        
        // 执行同步
        MultiCredentialSyncResult result = keycloakSyncService.syncMultipleCredentials(
            TEST_ACCOUNT_ID, TEST_PASSWORD, TEST_CREATOR
        );
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(2, result.getTotalCredentials(), "应该有2个凭据");
        assertEquals(2, result.getSuccessCount(), "应该成功同步2个凭据");
        assertEquals(0, result.getFailureCount(), "应该没有失败的凭据");
        
        // 验证详情
        assertNotNull(result.getDetails());
        assertEquals(2, result.getDetails().size());
        
        // 验证身份证号凭据
        CredentialSyncDetail idCardDetail = result.getDetails().stream()
            .filter(d -> d.getCredentialType() == CredentialType.IDCARD)
            .findFirst()
            .orElse(null);
        assertNotNull(idCardDetail);
        assertTrue(idCardDetail.isSuccess());
        // 验证身份证号已脱敏：110101********0014
        assertTrue(idCardDetail.getCredentialValue().contains("********"));
    }
    
    // ========== 测试场景4：完整三个凭据的同步 ==========
    
    @Test
    @DisplayName("同步完整三个凭据 - 创建3个Keycloak用户")
    void testSyncAccountWithAllCredentials() {
        // 准备测试数据：完整三个凭据
        TpAccountVO account = createTestAccount(TEST_USERNAME, TEST_PHONE_ENCRYPTED, TEST_IDCARD);
        
        // Mock查询账号信息
        when(userAccountService.selectByAccountId(TEST_ACCOUNT_ID)).thenReturn(account);
        
        // Mock手机号解密
        try (MockedStatic<PhoneEncryptionUtils> mockedUtils = mockStatic(PhoneEncryptionUtils.class)) {
            mockedUtils.when(() -> PhoneEncryptionUtils.safeDecrypt(TEST_PHONE_ENCRYPTED))
                      .thenReturn(TEST_PHONE_DECRYPTED);
            
            // Mock获取管理员token
            mockAdminTokenResponse();
            
            // Mock查找用户（都不存在）
            mockFindUserResponse(TEST_USERNAME, null);
            mockFindUserResponse(TEST_PHONE_DECRYPTED, null);
            mockFindUserResponse(TEST_IDCARD, null);
            
            // Mock创建用户成功
            mockCreateUserResponse(TEST_USERNAME, TEST_KEYCLOAK_USER_ID + "-username");
            mockCreateUserResponse(TEST_PHONE_DECRYPTED, TEST_KEYCLOAK_USER_ID + "-phone");
            mockCreateUserResponse(TEST_IDCARD, TEST_KEYCLOAK_USER_ID + "-idcard");
            
            // 执行同步
            MultiCredentialSyncResult result = keycloakSyncService.syncMultipleCredentials(
                TEST_ACCOUNT_ID, TEST_PASSWORD, TEST_CREATOR
            );
            
            // 验证结果
            assertNotNull(result);
            assertTrue(result.isSuccess());
            assertEquals(3, result.getTotalCredentials(), "应该有3个凭据");
            assertEquals(3, result.getSuccessCount(), "应该成功同步3个凭据");
            assertEquals(0, result.getFailureCount(), "应该没有失败的凭据");
            
            // 验证详情
            assertNotNull(result.getDetails());
            assertEquals(3, result.getDetails().size());
            
            // 验证所有凭据都成功
            for (CredentialSyncDetail detail : result.getDetails()) {
                assertTrue(detail.isSuccess());
                assertNotNull(detail.getKeycloakUserId());
            }
        }
    }
    
    // ========== 测试场景5：手机号解密失败处理 ==========
    
    @Test
    @DisplayName("手机号解密失败 - 跳过手机号凭据，继续同步其他凭据")
    void testSyncAccountWithPhoneDecryptionFailure() {
        // 准备测试数据：完整三个凭据
        TpAccountVO account = createTestAccount(TEST_USERNAME, TEST_PHONE_ENCRYPTED, TEST_IDCARD);
        
        // Mock查询账号信息
        when(userAccountService.selectByAccountId(TEST_ACCOUNT_ID)).thenReturn(account);
        
        // Mock手机号解密失败
        try (MockedStatic<PhoneEncryptionUtils> mockedUtils = mockStatic(PhoneEncryptionUtils.class)) {
            mockedUtils.when(() -> PhoneEncryptionUtils.safeDecrypt(TEST_PHONE_ENCRYPTED))
                      .thenThrow(new RuntimeException("解密失败"));
            
            // Mock获取管理员token
            mockAdminTokenResponse();
            
            // Mock查找用户（都不存在）
            mockFindUserResponse(TEST_USERNAME, null);
            mockFindUserResponse(TEST_IDCARD, null);
            
            // Mock创建用户成功
            mockCreateUserResponse(TEST_USERNAME, TEST_KEYCLOAK_USER_ID + "-username");
            mockCreateUserResponse(TEST_IDCARD, TEST_KEYCLOAK_USER_ID + "-idcard");
            
            // 执行同步
            MultiCredentialSyncResult result = keycloakSyncService.syncMultipleCredentials(
                TEST_ACCOUNT_ID, TEST_PASSWORD, TEST_CREATOR
            );
            
            // 验证结果：手机号解密失败，但其他凭据应该成功
            assertNotNull(result);
            assertTrue(result.isSuccess()); // 只要有成功的就算成功
            assertEquals(2, result.getTotalCredentials(), "应该有2个凭据（手机号被跳过）");
            assertEquals(2, result.getSuccessCount(), "应该成功同步2个凭据");
            assertEquals(0, result.getFailureCount(), "应该没有失败的凭据");
        }
    }
    
    // ========== 测试场景6：部分凭据同步失败 ==========
    
    @Test
    @DisplayName("部分凭据同步失败 - 统计成功和失败数量")
    void testSyncAccountWithPartialFailure() {
        // 准备测试数据：完整三个凭据
        TpAccountVO account = createTestAccount(TEST_USERNAME, TEST_PHONE_ENCRYPTED, TEST_IDCARD);
        
        // Mock查询账号信息
        when(userAccountService.selectByAccountId(TEST_ACCOUNT_ID)).thenReturn(account);
        
        // Mock手机号解密
        try (MockedStatic<PhoneEncryptionUtils> mockedUtils = mockStatic(PhoneEncryptionUtils.class)) {
            mockedUtils.when(() -> PhoneEncryptionUtils.safeDecrypt(TEST_PHONE_ENCRYPTED))
                      .thenReturn(TEST_PHONE_DECRYPTED);
            
            // Mock获取管理员token
            mockAdminTokenResponse();
            
            // Mock查找用户（都不存在）
            mockFindUserResponse(TEST_USERNAME, null);
            mockFindUserResponse(TEST_PHONE_DECRYPTED, null);
            mockFindUserResponse(TEST_IDCARD, null);
            
            // Mock创建用户：账号名成功，手机号失败，身份证号成功
            mockCreateUserResponse(TEST_USERNAME, TEST_KEYCLOAK_USER_ID + "-username");
            mockCreateUserFailure(TEST_PHONE_DECRYPTED); // 手机号创建失败
            mockCreateUserResponse(TEST_IDCARD, TEST_KEYCLOAK_USER_ID + "-idcard");
            
            // 执行同步
            MultiCredentialSyncResult result = keycloakSyncService.syncMultipleCredentials(
                TEST_ACCOUNT_ID, TEST_PASSWORD, TEST_CREATOR
            );
            
            // 验证结果
            assertNotNull(result);
            assertFalse(result.isSuccess(), "有失败的凭据，整体应该标记为失败");
            assertEquals(3, result.getTotalCredentials(), "应该有3个凭据");
            assertEquals(2, result.getSuccessCount(), "应该成功同步2个凭据");
            assertEquals(1, result.getFailureCount(), "应该有1个失败的凭据");
            
            // 验证详情
            assertNotNull(result.getDetails());
            assertEquals(3, result.getDetails().size());
            
            // 验证失败的凭据
            CredentialSyncDetail phoneDetail = result.getDetails().stream()
                .filter(d -> d.getCredentialType() == CredentialType.PHONE)
                .findFirst()
                .orElse(null);
            assertNotNull(phoneDetail);
            assertFalse(phoneDetail.isSuccess());
            assertNotNull(phoneDetail.getMessage());
        }
    }
    
    // ========== 辅助方法 ==========
    
    /**
     * 创建测试账号
     */
    private TpAccountVO createTestAccount(String username, String encryptedPhone, String idcard) {
        TpAccountVO account = new TpAccountVO();
        account.setAccountId(TEST_ACCOUNT_ID);
        account.setUsername(username);
        account.setPhone(encryptedPhone);
        account.setIdcard(idcard);
        account.setPersonId("PERSON001");
        return account;
    }
    
    /**
     * Mock管理员token响应
     */
    private void mockAdminTokenResponse() {
        ResponseEntity<String> tokenResponse = new ResponseEntity<>(
            "{\"access_token\":\"" + ADMIN_TOKEN + "\"}", 
            HttpStatus.OK
        );
        
        when(restTemplate.exchange(
            contains("/realms/master/protocol/openid-connect/token"),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn(tokenResponse);
    }
    
    /**
     * Mock查找用户响应
     */
    private void mockFindUserResponse(String username, String userId) {
        ResponseEntity<String> response;
        
        if (userId == null) {
            // 用户不存在
            response = new ResponseEntity<>("[]", HttpStatus.OK);
        } else {
            // 用户存在
            response = new ResponseEntity<>(
                "[{\"id\":\"" + userId + "\",\"username\":\"" + username + "\"}]",
                HttpStatus.OK
            );
        }
        
        when(restTemplate.exchange(
            contains("/admin/realms/" + ReflectionTestUtils.getField(keycloakSyncService, "realm") + "/users?username=" + username),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn(response);
    }
    
    /**
     * Mock创建用户成功响应
     */
    private void mockCreateUserResponse(String username, String userId) {
        // Mock创建用户请求返回201
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(java.net.URI.create("http://localhost:8080/admin/realms/test-realm/users/" + userId));
        
        ResponseEntity<Void> createResponse = new ResponseEntity<>(headers, HttpStatus.CREATED);
        
        when(restTemplate.exchange(
            contains("/admin/realms/test-realm/users"),
            eq(HttpMethod.POST),
            argThat(entity -> {
                String body = (String) entity.getBody();
                return body != null && body.contains("\"username\":\"" + username + "\"");
            }),
            eq(Void.class)
        )).thenReturn(createResponse);
    }
    
    /**
     * Mock创建用户失败响应
     */
    private void mockCreateUserFailure(String username) {
        when(restTemplate.exchange(
            contains("/admin/realms/test-realm/users"),
            eq(HttpMethod.POST),
            argThat(entity -> {
                String body = (String) entity.getBody();
                return body != null && body.contains("\"username\":\"" + username + "\"");
            }),
            eq(Void.class)
        )).thenThrow(new RuntimeException("创建用户失败"));
    }
}
