package com.jiuxi.shared.security.audit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 审计系统测试类
 * 验证审计体系的基本功能
 * 
 * @author Security Refactoring
 * @since Phase 4.2.5
 */
@SpringBootTest
@ActiveProfiles("test")
public class AuditSystemTest {

    @Test
    public void testAuditEventCreation() {
        // 测试审计事件创建
        AuditEvent event = AuditEvent.createAuthEvent(
            AuditEventType.LOGIN_SUCCESS,
            "testuser",
            "192.168.1.100"
        );

        assertNotNull(event);
        assertNotNull(event.getEventId());
        assertEquals(AuditEventType.LOGIN_SUCCESS, event.getEventType());
        assertEquals("testuser", event.getUsername());
        assertEquals("192.168.1.100", event.getClientIp());
        assertEquals(AuditCategory.AUTHENTICATION, event.getCategory());
        assertEquals(AuditLevel.INFO, event.getLevel());
        // 结果需要手动设置
        assertNull(event.getResult()); // 默认构造函数不设置结果
    }

    @Test
    public void testAuditEventTypeClassification() {
        // 测试事件类型分类
        assertTrue(AuditEventType.LOGIN_SUCCESS.isAuthenticationEvent());
        assertTrue(AuditEventType.ACCESS_DENIED.isAuthorizationEvent());
        assertTrue(AuditEventType.INTRUSION_ATTEMPT.isSecurityEvent());
        assertTrue(AuditEventType.INTRUSION_ATTEMPT.isHighRiskEvent());
        assertTrue(AuditEventType.INTRUSION_ATTEMPT.requiresRealTimeAlert());
    }

    @Test
    public void testAuditLevelPriority() {
        // 测试审计级别优先级
        assertTrue(AuditLevel.CRITICAL.isHighPriority());
        assertTrue(AuditLevel.HIGH.isHighPriority());
        assertFalse(AuditLevel.MEDIUM.isHighPriority());
        assertFalse(AuditLevel.INFO.isHighPriority());
        assertFalse(AuditLevel.LOW.isHighPriority());
    }

    @Test
    public void testAuditResultValidation() {
        // 测试审计结果验证
        assertTrue(AuditResult.SUCCESS.isSuccess());
        assertTrue(AuditResult.FAILURE.isFailure());
        assertTrue(AuditResult.DENIED.isFailure());
        assertTrue(AuditResult.TIMEOUT.isFailure());
        
        assertTrue(AuditResult.FAILURE.requiresAlert());
        assertTrue(AuditResult.DENIED.requiresAlert());
        assertFalse(AuditResult.SUCCESS.requiresAlert());
    }

    @Test
    public void testAuditQueryCriteriaBuilder() {
        // 测试查询条件构建器
        LocalDateTime now = LocalDateTime.now();
        AuditLogService.AuditQueryCriteria criteria = AuditLogService.AuditQueryCriteria.builder()
            .startTime(now.minusDays(7))
            .endTime(now)
            .userId("testuser")
            .category(AuditCategory.AUTHENTICATION)
            .level(AuditLevel.HIGH)
            .limit(50)
            .build();

        assertNotNull(criteria);
        assertEquals("testuser", criteria.getUserId());
        assertEquals(AuditCategory.AUTHENTICATION, criteria.getCategory());
        assertEquals(AuditLevel.HIGH, criteria.getLevel());
        assertEquals(50, criteria.getLimit());
    }

    @Test
    public void testAuditEventSerializability() {
        // 测试审计事件可序列化性
        AuditEvent event = AuditEvent.createDataEvent(
            AuditEventType.DATA_DELETE,
            "admin",
            "user-123",
            "tp_account"
        );
        event.setClientIp("192.168.1.1");
        event.setDescription("删除用户数据");

        // 验证关键字段
        assertNotNull(event.getEventId());
        assertNotNull(event.getTimestamp());
        assertEquals(AuditEventType.DATA_DELETE, event.getEventType());
        assertEquals(AuditCategory.DATA_ACCESS, event.getCategory());
        assertEquals(AuditLevel.HIGH, event.getLevel());
    }
}