package com.jiuxi.admin.core.service.impl;

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
 * Keycloak多凭据操作测试
 * 
 * 测试场景：
 * 1. 密码同步到所有凭据
 * 2. 删除所有凭据
 * 3. 启用所有凭据
 * 4. 禁用所有凭据
 * 5. 凭据不存在的场景处理
 *
 * @author Qoder AI
 * @since 2024-12-16
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Keycloak多凭据操作测试")
class KeycloakMultiCredentialOperationsTest {
    
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
    private static final String NEW_PASSWORD = "NewPassword@456";
    private static final String TEST_UPDATER = "admin";
    private static final String ADMIN_TOKEN = "admin-token-xyz";
    private static final String KEYCLOAK_USER_ID_USERNAME = "kc-user-username-123";
    private static final String KEYCLOAK_USER_ID_PHONE = "kc-user-phone-456";
    private static final String KEYCLOAK_USER_ID_IDCARD = "kc-user-idcard-789";
    
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
    
    // ========== 测试场景1：密码同步到所有凭据 ==========
    
    @Test
    @DisplayName("更新所有凭据的密码 - 同步更新3个Keycloak用户")
    void testUpdatePasswordForAllCredentials() {
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
            
            // Mock查找用户（都存在）
            mockFindUserResponse(TEST_USERNAME, KEYCLOAK_USER_ID_USERNAME);
            mockFindUserResponse(TEST_PHONE_DECRYPTED, KEYCLOAK_USER_ID_PHONE);
            mockFindUserResponse(TEST_IDCARD, KEYCLOAK_USER_ID_IDCARD);
            
            // Mock重置密码成功
            mockResetPasswordResponse(KEYCLOAK_USER_ID_USERNAME, true);
            mockResetPasswordResponse(KEYCLOAK_USER_ID_PHONE, true);
            mockResetPasswordResponse(KEYCLOAK_USER_ID_IDCARD, true);
            
            // 执行密码更新
            MultiCredentialSyncResult result = keycloakSyncService.updatePasswordForAllCredentials(
                TEST_ACCOUNT_ID, NEW_PASSWORD, TEST_UPDATER
            );
            
            // 验证结果
            assertNotNull(result);
            assertTrue(result.isSuccess());
            assertEquals(3, result.getTotalCredentials(), "应该有3个凭据");
            assertEquals(3, result.getSuccessCount(), "应该成功更新3个凭据的密码");
            assertEquals(0, result.getFailureCount(), "应该没有失败的凭据");
            
            // 验证所有凭据都成功
            for (CredentialSyncDetail detail : result.getDetails()) {
                assertTrue(detail.isSuccess());
                assertNotNull(detail.getKeycloakUserId());
                assertTrue(detail.getMessage().contains("密码更新成功") || 
                          detail.getMessage().contains("successfully"));
            }
        }
    }
    
    @Test
    @DisplayName("更新密码时部分凭据不存在 - 跳过不存在的凭据")
    void testUpdatePasswordWithNonExistentCredentials() {
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
            
            // Mock查找用户：账号名存在，手机号不存在，身份证号存在
            mockFindUserResponse(TEST_USERNAME, KEYCLOAK_USER_ID_USERNAME);
            mockFindUserResponse(TEST_PHONE_DECRYPTED, null); // 不存在
            mockFindUserResponse(TEST_IDCARD, KEYCLOAK_USER_ID_IDCARD);
            
            // Mock重置密码成功
            mockResetPasswordResponse(KEYCLOAK_USER_ID_USERNAME, true);
            mockResetPasswordResponse(KEYCLOAK_USER_ID_IDCARD, true);
            
            // 执行密码更新
            MultiCredentialSyncResult result = keycloakSyncService.updatePasswordForAllCredentials(
                TEST_ACCOUNT_ID, NEW_PASSWORD, TEST_UPDATER
            );
            
            // 验证结果
            assertNotNull(result);
            assertFalse(result.isSuccess(), "有凭据不存在，整体应该标记为失败");
            assertEquals(3, result.getTotalCredentials(), "应该有3个凭据");
            assertEquals(2, result.getSuccessCount(), "应该成功更新2个凭据的密码");
            assertEquals(1, result.getFailureCount(), "应该有1个失败的凭据（不存在）");
            
            // 验证不存在的凭据
            CredentialSyncDetail phoneDetail = result.getDetails().stream()
                .filter(d -> d.getCredentialType() == CredentialType.PHONE)
                .findFirst()
                .orElse(null);
            assertNotNull(phoneDetail);
            assertFalse(phoneDetail.isSuccess());
            assertTrue(phoneDetail.getMessage().contains("不存在") || 
                      phoneDetail.getMessage().contains("not found"));
        }
    }
    
    // ========== 测试场景2：删除所有凭据 ==========
    
    @Test
    @DisplayName("删除所有凭据 - 删除3个Keycloak用户")
    void testDeleteAllCredentials() {
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
            
            // Mock查找用户（都存在）
            mockFindUserResponse(TEST_USERNAME, KEYCLOAK_USER_ID_USERNAME);
            mockFindUserResponse(TEST_PHONE_DECRYPTED, KEYCLOAK_USER_ID_PHONE);
            mockFindUserResponse(TEST_IDCARD, KEYCLOAK_USER_ID_IDCARD);
            
            // Mock删除用户成功
            mockDeleteUserResponse(KEYCLOAK_USER_ID_USERNAME, true);
            mockDeleteUserResponse(KEYCLOAK_USER_ID_PHONE, true);
            mockDeleteUserResponse(KEYCLOAK_USER_ID_IDCARD, true);
            
            // 执行删除
            MultiCredentialSyncResult result = keycloakSyncService.deleteAllCredentials(TEST_ACCOUNT_ID);
            
            // 验证结果
            assertNotNull(result);
            assertTrue(result.isSuccess());
            assertEquals(3, result.getTotalCredentials(), "应该有3个凭据");
            assertEquals(3, result.getSuccessCount(), "应该成功删除3个凭据");
            assertEquals(0, result.getFailureCount(), "应该没有失败的凭据");
            
            // 验证所有凭据都成功删除
            for (CredentialSyncDetail detail : result.getDetails()) {
                assertTrue(detail.isSuccess());
                assertTrue(detail.getMessage().contains("删除成功") || 
                          detail.getMessage().contains("successfully deleted"));
            }
        }
    }
    
    @Test
    @DisplayName("删除不存在的凭据 - 应该标记为失败")
    void testDeleteNonExistentCredentials() {
        // 准备测试数据：仅有账号名
        TpAccountVO account = createTestAccount(TEST_USERNAME, null, null);
        
        // Mock查询账号信息
        when(userAccountService.selectByAccountId(TEST_ACCOUNT_ID)).thenReturn(account);
        
        // Mock获取管理员token
        mockAdminTokenResponse();
        
        // Mock查找用户（不存在）
        mockFindUserResponse(TEST_USERNAME, null);
        
        // 执行删除
        MultiCredentialSyncResult result = keycloakSyncService.deleteAllCredentials(TEST_ACCOUNT_ID);
        
        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getTotalCredentials(), "应该有1个凭据");
        assertEquals(0, result.getSuccessCount(), "没有成功删除的凭据");
        assertEquals(1, result.getFailureCount(), "应该有1个失败的凭据");
        
        // 验证详情
        CredentialSyncDetail detail = result.getDetails().get(0);
        assertFalse(detail.isSuccess());
        assertTrue(detail.getMessage().contains("不存在") || 
                  detail.getMessage().contains("not found"));
    }
    
    // ========== 测试场景3：启用所有凭据 ==========
    
    @Test
    @DisplayName("启用所有凭据 - 启用3个Keycloak用户")
    void testEnableAllCredentials() {
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
            
            // Mock查找用户（都存在）
            mockFindUserResponse(TEST_USERNAME, KEYCLOAK_USER_ID_USERNAME);
            mockFindUserResponse(TEST_PHONE_DECRYPTED, KEYCLOAK_USER_ID_PHONE);
            mockFindUserResponse(TEST_IDCARD, KEYCLOAK_USER_ID_IDCARD);
            
            // Mock启用用户成功
            mockEnableUserResponse(KEYCLOAK_USER_ID_USERNAME, true);
            mockEnableUserResponse(KEYCLOAK_USER_ID_PHONE, true);
            mockEnableUserResponse(KEYCLOAK_USER_ID_IDCARD, true);
            
            // 执行启用
            MultiCredentialSyncResult result = keycloakSyncService.enableAllCredentials(TEST_ACCOUNT_ID);
            
            // 验证结果
            assertNotNull(result);
            assertTrue(result.isSuccess());
            assertEquals(3, result.getTotalCredentials(), "应该有3个凭据");
            assertEquals(3, result.getSuccessCount(), "应该成功启用3个凭据");
            assertEquals(0, result.getFailureCount(), "应该没有失败的凭据");
            
            // 验证所有凭据都成功启用
            for (CredentialSyncDetail detail : result.getDetails()) {
                assertTrue(detail.isSuccess());
                assertTrue(detail.getMessage().contains("启用成功") || 
                          detail.getMessage().contains("successfully enabled"));
            }
        }
    }
    
    // ========== 测试场景4：禁用所有凭据 ==========
    
    @Test
    @DisplayName("禁用所有凭据 - 禁用3个Keycloak用户")
    void testDisableAllCredentials() {
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
            
            // Mock查找用户（都存在）
            mockFindUserResponse(TEST_USERNAME, KEYCLOAK_USER_ID_USERNAME);
            mockFindUserResponse(TEST_PHONE_DECRYPTED, KEYCLOAK_USER_ID_PHONE);
            mockFindUserResponse(TEST_IDCARD, KEYCLOAK_USER_ID_IDCARD);
            
            // Mock禁用用户成功
            mockDisableUserResponse(KEYCLOAK_USER_ID_USERNAME, true);
            mockDisableUserResponse(KEYCLOAK_USER_ID_PHONE, true);
            mockDisableUserResponse(KEYCLOAK_USER_ID_IDCARD, true);
            
            // 执行禁用
            MultiCredentialSyncResult result = keycloakSyncService.disableAllCredentials(TEST_ACCOUNT_ID);
            
            // 验证结果
            assertNotNull(result);
            assertTrue(result.isSuccess());
            assertEquals(3, result.getTotalCredentials(), "应该有3个凭据");
            assertEquals(3, result.getSuccessCount(), "应该成功禁用3个凭据");
            assertEquals(0, result.getFailureCount(), "应该没有失败的凭据");
            
            // 验证所有凭据都成功禁用
            for (CredentialSyncDetail detail : result.getDetails()) {
                assertTrue(detail.isSuccess());
                assertTrue(detail.getMessage().contains("禁用成功") || 
                          detail.getMessage().contains("successfully disabled"));
            }
        }
    }
    
    // ========== 测试场景5：账号不存在的处理 ==========
    
    @Test
    @DisplayName("账号不存在 - 应该返回失败结果")
    void testOperationsWithNonExistentAccount() {
        // Mock查询账号信息（账号不存在）
        when(userAccountService.selectByAccountId(TEST_ACCOUNT_ID)).thenReturn(null);
        
        // 执行同步
        MultiCredentialSyncResult syncResult = keycloakSyncService.syncMultipleCredentials(
            TEST_ACCOUNT_ID, TEST_PASSWORD, "admin"
        );
        
        // 验证结果
        assertNotNull(syncResult);
        assertFalse(syncResult.isSuccess());
        assertTrue(syncResult.getMessage().contains("账号不存在") || 
                  syncResult.getMessage().contains("not found") ||
                  syncResult.getMessage().contains("null"));
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
            contains("/admin/realms/test-realm/users?username=" + username),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn(response);
    }
    
    /**
     * Mock重置密码响应
     */
    private void mockResetPasswordResponse(String userId, boolean success) {
        if (success) {
            ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            when(restTemplate.exchange(
                contains("/admin/realms/test-realm/users/" + userId + "/reset-password"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Void.class)
            )).thenReturn(response);
        } else {
            when(restTemplate.exchange(
                contains("/admin/realms/test-realm/users/" + userId + "/reset-password"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Void.class)
            )).thenThrow(new RuntimeException("重置密码失败"));
        }
    }
    
    /**
     * Mock删除用户响应
     */
    private void mockDeleteUserResponse(String userId, boolean success) {
        if (success) {
            ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            when(restTemplate.exchange(
                contains("/admin/realms/test-realm/users/" + userId),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Void.class)
            )).thenReturn(response);
        } else {
            when(restTemplate.exchange(
                contains("/admin/realms/test-realm/users/" + userId),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Void.class)
            )).thenThrow(new RuntimeException("删除用户失败"));
        }
    }
    
    /**
     * Mock启用用户响应
     */
    private void mockEnableUserResponse(String userId, boolean success) {
        if (success) {
            ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            when(restTemplate.exchange(
                eq("http://localhost:8080/admin/realms/test-realm/users/" + userId),
                eq(HttpMethod.PUT),
                argThat(entity -> {
                    String body = (String) entity.getBody();
                    return body != null && body.contains("\"enabled\":true");
                }),
                eq(Void.class)
            )).thenReturn(response);
        } else {
            when(restTemplate.exchange(
                eq("http://localhost:8080/admin/realms/test-realm/users/" + userId),
                eq(HttpMethod.PUT),
                argThat(entity -> {
                    String body = (String) entity.getBody();
                    return body != null && body.contains("\"enabled\":true");
                }),
                eq(Void.class)
            )).thenThrow(new RuntimeException("启用用户失败"));
        }
    }
    
    /**
     * Mock禁用用户响应
     */
    private void mockDisableUserResponse(String userId, boolean success) {
        if (success) {
            ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            when(restTemplate.exchange(
                eq("http://localhost:8080/admin/realms/test-realm/users/" + userId),
                eq(HttpMethod.PUT),
                argThat(entity -> {
                    String body = (String) entity.getBody();
                    return body != null && body.contains("\"enabled\":false");
                }),
                eq(Void.class)
            )).thenReturn(response);
        } else {
            when(restTemplate.exchange(
                eq("http://localhost:8080/admin/realms/test-realm/users/" + userId),
                eq(HttpMethod.PUT),
                argThat(entity -> {
                    String body = (String) entity.getBody();
                    return body != null && body.contains("\"enabled\":false");
                }),
                eq(Void.class)
            )).thenThrow(new RuntimeException("禁用用户失败"));
        }
    }
}
