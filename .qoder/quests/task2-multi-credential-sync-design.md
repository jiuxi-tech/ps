# 任务2：多凭据Keycloak同步实现 - 详细设计

## 一、设计概述

### 1.1 目标

扩展KeycloakSyncService，支持一个本地账号根据其凭据信息（username、phone、idcard）同步创建1-3个Keycloak用户。

### 1.2 核心原则

1. **单向推送**：中台→Keycloak，不从Keycloak同步回来
2. **username识别**：Keycloak用户通过username字段识别，无需存储用户ID
3. **属性追溯**：所有Keycloak用户的attributes存储完整账号信息（便于追溯）
4. **密码同步**：三个用户使用相同密码，密码修改时全部同步

## 二、数据结构设计

### 2.1 Keycloak用户属性映射

每个Keycloak用户的属性构建规则：

| Keycloak属性 | 主账号用户 | 手机号用户 | 身份证号用户 |
|-------------|-----------|-----------|-------------|
| username | tp_account.USERNAME | tp_account.PHONE（解密） | tp_account.IDCARD |
| password | tp_account.USERPWD | 相同 | 相同 |
| email | tp_person.EMAIL | 相同 | 相同 |
| firstName | tp_person.PERSON_NAME | 相同 | 相同 |
| enabled | true | true | true |
| emailVerified | true | true | true |

### 2.2 Keycloak用户自定义属性（attributes）

所有用户包含相同的attributes（用于追溯和管理）：

```java
Map<String, Object> attributes = new HashMap<>();
attributes.put("accountId", Collections.singletonList(accountId));
attributes.put("personId", Collections.singletonList(personId));
attributes.put("tenantId", Collections.singletonList(tenantId));
attributes.put("personNo", Collections.singletonList(personNo));
attributes.put("USERNAME", Collections.singletonList(username));          // 原始账号名
attributes.put("PHONE", Collections.singletonList(phoneDecrypted));       // 解密后的手机号
attributes.put("IDCARD", Collections.singletonList(idcard));              // 身份证号
attributes.put("credentialType", Collections.singletonList("PHONE"));     // 当前用户的凭据类型
```

### 2.3 凭据类型枚举

```java
public enum CredentialType {
    USERNAME("账号名"),
    PHONE("手机号"),
    IDCARD("身份证号");
    
    private final String description;
    
    CredentialType(String description) {
        this.description = description;
    }
}
```

## 三、方法设计

### 3.1 接口扩展

在KeycloakSyncService接口中新增方法：

```java
/**
 * 同步账号的多个凭据到Keycloak
 * 根据账号信息创建1-3个Keycloak用户
 *
 * @param accountId 账号ID
 * @param creator 创建人ID
 * @return 同步结果（包含所有创建的用户信息）
 */
MultiCredentialSyncResult syncMultipleCredentials(String accountId, String creator);

/**
 * 更新账号所有凭据的密码
 * 同步更新所有关联Keycloak用户的密码
 *
 * @param accountId 账号ID
 * @param newPassword 新密码（明文）
 * @param updater 更新人ID
 * @return 同步结果
 */
MultiCredentialSyncResult updatePasswordForAllCredentials(String accountId, String newPassword, String updater);

/**
 * 删除账号的所有凭据
 * 删除账号关联的所有Keycloak用户
 *
 * @param accountId 账号ID
 * @return 同步结果
 */
MultiCredentialSyncResult deleteAllCredentials(String accountId);

/**
 * 启用账号的所有凭据
 *
 * @param accountId 账号ID
 * @return 同步结果
 */
MultiCredentialSyncResult enableAllCredentials(String accountId);

/**
 * 禁用账号的所有凭据
 *
 * @param accountId 账号ID
 * @return 同步结果
 */
MultiCredentialSyncResult disableAllCredentials(String accountId);
```

### 3.2 返回结果类设计

```java
/**
 * 多凭据同步结果
 */
public class MultiCredentialSyncResult {
    private boolean success;                // 整体是否成功
    private String message;                 // 整体消息
    private int totalCredentials;           // 总凭据数（1-3）
    private int successCount;               // 成功数量
    private int failureCount;               // 失败数量
    private List<CredentialSyncDetail> details;  // 每个凭据的详细结果
    
    public static class CredentialSyncDetail {
        private CredentialType credentialType;
        private String credentialValue;      // 凭据值（脱敏后）
        private boolean success;
        private String keycloakUserId;
        private String message;
        private Exception exception;
    }
}
```

## 四、核心实现逻辑

### 4.1 syncMultipleCredentials实现流程

```java
public MultiCredentialSyncResult syncMultipleCredentials(String accountId, String creator) {
    // 1. 查询账号信息
    TpAccountVO account = userAccountService.selectByAccountId(accountId);
    if (account == null) {
        return MultiCredentialSyncResult.failure("账号不存在: accountId=" + accountId);
    }
    
    // 2. 获取管理员token
    String adminToken = getAdminAccessToken();
    if (!StringUtils.hasText(adminToken)) {
        return MultiCredentialSyncResult.failure("获取Keycloak管理员令牌失败");
    }
    
    // 3. 构建人员扩展信息（邮箱、姓名等）
    Map<String, Object> personExtras = buildPersonExtras(accountId);
    String email = (String) personExtras.getOrDefault("email", null);
    String firstName = (String) personExtras.getOrDefault("firstName", null);
    
    // 4. 构建公共attributes
    Map<String, Object> commonAttributes = buildCommonAttributes(account, personExtras);
    
    // 5. 收集需要同步的凭据
    List<CredentialInfo> credentials = collectCredentials(account);
    
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
                boolean updated = updateKeycloakUserInfo(adminToken, existingUserId, 
                    credentialInfo.getValue(), null, email, firstName, attributes);
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
                String userId = createKeycloakUser(adminToken, 
                    credentialInfo.getValue(), 
                    account.getUserpwd(),  // 明文密码
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
    return result;
}
```

### 4.2 collectCredentials辅助方法

```java
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
    
    log.info("账号凭据收集完成: accountId={}, 凭据数量={}", account.getAccountId(), credentials.size());
    return credentials;
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
```

### 4.3 updatePasswordForAllCredentials实现

```java
public MultiCredentialSyncResult updatePasswordForAllCredentials(
        String accountId, String newPassword, String updater) {
    // 1. 查询账号信息
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
    
    // 4. 批量更新密码
    MultiCredentialSyncResult result = new MultiCredentialSyncResult();
    result.setTotalCredentials(credentials.size());
    
    for (CredentialInfo credentialInfo : credentials) {
        try {
            // 4.1 查找Keycloak用户
            String userId = findUserByUsername(adminToken, credentialInfo.getValue());
            if (!StringUtils.hasText(userId)) {
                log.warn("Keycloak用户不存在，跳过密码更新: username={}", credentialInfo.getValue());
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
    return result;
}
```

### 4.4 凭据脱敏方法

```java
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
```

## 五、现有方法改造

### 5.1 修改UserAccountServiceImpl中的同步调用

#### 5.1.1 accountAdd方法改造

**原有代码**：
```java
// 发布Keycloak同步事件，在事务提交后异步处理
if (null != keycloakSyncService) {
    KeycloakSyncEvent keycloakEvent = new KeycloakSyncEvent(accountId, vo.getUsername(), denUserpwd, "system");
    applicationContext.publishEvent(keycloakEvent);
}
```

**改造后**：
```java
// 发布Keycloak同步事件，在事务提交后异步处理（多凭据）
if (null != keycloakSyncService) {
    MultiCredentialSyncEvent keycloakEvent = new MultiCredentialSyncEvent(accountId, denUserpwd, "system");
    applicationContext.publishEvent(keycloakEvent);
}
```

#### 5.1.2 密码修改方法改造

**changePwd、resetPwd等方法改造**：

**原有代码**：
```java
KeycloakSyncService.KeycloakSyncResult syncResult =
    keycloakSyncService.updateKeycloakUser(accountVO.getAccountId(), accountVO.getUsername(), userpwd, operator);
```

**改造后**：
```java
MultiCredentialSyncResult syncResult =
    keycloakSyncService.updatePasswordForAllCredentials(accountVO.getAccountId(), userpwd, operator);
```

## 六、事件处理

### 6.1 新增MultiCredentialSyncEvent

```java
/**
 * 多凭据Keycloak同步事件
 */
public class MultiCredentialSyncEvent extends ApplicationEvent {
    private final String accountId;
    private final String password;
    private final String creator;
    
    public MultiCredentialSyncEvent(String accountId, String password, String creator) {
        super(accountId);
        this.accountId = accountId;
        this.password = password;
        this.creator = creator;
    }
    
    // Getters
}
```

### 6.2 事件监听器

```java
@Async
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void handleMultiCredentialKeycloakSync(MultiCredentialSyncEvent event) {
    if (null != keycloakSyncService) {
        try {
            LOGGER.info("开始异步同步账号到Keycloak（多凭据）: accountId={}", event.getAccountId());
            
            MultiCredentialSyncResult syncResult = keycloakSyncService.syncMultipleCredentials(
                    event.getAccountId(), event.getCreator());
            
            LOGGER.info("Keycloak多凭据同步结果: accountId={}, success={}, successCount={}/{}, message={}", 
                    event.getAccountId(), 
                    syncResult.isSuccess(), 
                    syncResult.getSuccessCount(), 
                    syncResult.getTotalCredentials(),
                    syncResult.getMessage());
                    
        } catch (Exception e) {
            LOGGER.error("Keycloak多凭据同步失败: accountId={}, error={}", event.getAccountId(), e.getMessage(), e);
        }
    }
}
```

## 七、测试场景

### 7.1 单元测试用例

| 测试场景 | 测试内容 | 预期结果 |
|---------|---------|---------|
| 测试1 | 仅有账号名的账号同步 | 创建1个Keycloak用户 |
| 测试2 | 账号名+手机号的账号同步 | 创建2个Keycloak用户 |
| 测试3 | 账号名+身份证号的账号同步 | 创建2个Keycloak用户 |
| 测试4 | 完整三个凭据的账号同步 | 创建3个Keycloak用户 |
| 测试5 | 手机号加密解密 | 手机号正确解密并同步 |
| 测试6 | 凭据脱敏 | 日志中凭据正确脱敏 |
| 测试7 | 密码同步到所有用户 | 3个用户密码全部更新 |
| 测试8 | 部分凭据同步失败 | 其他凭据继续同步 |
| 测试9 | 删除所有凭据 | 3个Keycloak用户全部删除 |
| 测试10 | 禁用所有凭据 | 3个Keycloak用户全部禁用 |

### 7.2 集成测试

1. 创建完整账号 → 验证Keycloak中创建了3个用户
2. 使用手机号登录 → 验证SSO跳转正常
3. 使用身份证号登录 → 验证SSO跳转正常
4. 修改密码 → 验证3个用户密码同步
5. 删除手机号 → 验证对应Keycloak用户删除

## 八、性能优化

### 8.1 批量操作优化

```java
// 使用CompletableFuture并发创建Keycloak用户
List<CompletableFuture<CredentialSyncDetail>> futures = credentials.stream()
    .map(credential -> CompletableFuture.supplyAsync(() -> {
        // 创建Keycloak用户的逻辑
        return createKeycloakUserForCredential(adminToken, credential, ...);
    }))
    .collect(Collectors.toList());

// 等待所有完成
CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
```

### 8.2 错误处理策略

1. **部分失败继续**：一个凭据同步失败不影响其他凭据
2. **重试机制**：网络异常时自动重试（最多3次）
3. **日志记录**：记录每个凭据的同步结果
4. **告警通知**：失败率超过50%时发送告警

## 九、向后兼容

### 9.1 保留原有方法

原有的单账号同步方法保持不变，供老代码继续使用：
- `syncAccountToKeycloak(accountId, username, password, creator)`
- `updateKeycloakUser(accountId, username, password, updater)`

### 9.2 渐进式迁移

1. **阶段1**：新增多凭据方法，老代码继续使用单账号方法
2. **阶段2**：新功能使用多凭据方法
3. **阶段3**：逐步迁移老代码到多凭据方法
4. **阶段4**：废弃单账号方法（标记@Deprecated）

## 十、监控与日志

### 10.1 日志规范

```java
// INFO级别：正常同步流程
LOGGER.info("开始多凭据同步: accountId={}, 凭据数量={}", accountId, credentials.size());
LOGGER.info("凭据同步成功: type={}, username={}, userId={}", type, maskedUsername, userId);

// WARN级别：可恢复的异常
LOGGER.warn("Keycloak用户已存在，执行更新: username={}", maskedUsername);
LOGGER.warn("手机号解密失败，跳过手机号凭据: accountId={}", accountId);

// ERROR级别：同步失败
LOGGER.error("凭据同步失败: type={}, username={}, error={}", type, maskedUsername, e.getMessage(), e);
```

### 10.2 监控指标

| 指标 | 说明 | 告警阈值 |
|------|------|---------|
| 同步成功率 | successCount / totalCredentials | <90% |
| 平均同步时长 | 单个账号平均耗时 | >5秒 |
| 失败次数 | 1小时内失败次数 | >10次 |
| 手机号解密失败率 | 手机号解密失败次数/总次数 | >5% |

---

**设计完成时间**：2024-12-16  
**设计人**：Qoder AI  
**版本**：1.0.0
