# SSO 登录问题修复总结

## 修复日期
2025-12-03

## 问题描述

用户点击 SSO 登录按钮后，输入正确的账号密码，但跳转到错误页面：
```
http://localhost:10801/#/login&error_detail=处理回调失败:+Error+exchanging+code+for+token:+400+BAD_REQUEST+-+{"error":"invalid_grant","error_description":"Incorrect+redirect_uri"}
```

**核心错误**：`invalid_grant: Incorrect redirect_uri`

## 根因分析

OAuth2 授权码流程分为两个阶段，每个阶段都需要传递 `redirect_uri` 参数，且两个阶段的值必须完全一致：

1. **阶段 1（授权请求）**：前端跳转到 Keycloak
   - 使用配置：`sso.keycloak.redirect.uri = https://mid.shxdx.com/ps-be/api/sso/callback`

2. **阶段 2（Token 交换）**：后端用 code 换取 token
   - **原错误代码**：`String redirectUri = request.getRequestURL().toString();`
   - 这会动态生成 URL（可能包含查询参数），与阶段 1 不一致
   - Keycloak 要求两个阶段的 `redirect_uri` 必须完全匹配

## 已执行的修复

### 修复 1：redirect_uri 不匹配问题（P0 - 紧急）

**修改文件**：`ps-be/src/main/java/com/jiuxi/security/sso/controller/SsoController.java`

**修改位置**：`handleCallback` 方法（约第 528 行）

**修改前代码**：
```java
String redirectUri = request.getRequestURL().toString();
KeycloakOAuth2Service.TokenResponse tokenResponse = oAuth2Service.exchangeCodeForToken(code, redirectUri);
```

**修改后代码**：
```java
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
```

**关键改进**：
- 从系统配置表读取固定的 `redirect_uri`，确保与授权阶段一致
- 提供降级逻辑，避免配置缺失导致功能完全失效
- 添加日志记录，便于问题排查

### 修复 2：错误 URL 参数格式问题（P2 - 中）

**问题**：错误重定向使用了 `#/login&error_detail` 而非正确的 `?error_detail#/login`

**解决方案**：新增 `buildErrorUrl` 辅助方法

**新增方法**：
```java
/**
 * 构建错误重定向 URL，正确处理 Hash 路由
 * 
 * @param baseUrl 基础 URL
 * @param errorDetail 错误详情
 * @return 完整的错误 URL
 */
private String buildErrorUrl(String baseUrl, String errorDetail) {
    try {
        String encodedError = java.net.URLEncoder.encode(errorDetail, "UTF-8");
        
        // 处理 Hash 路由（如 https://mid.shxdx.com/#/login）
        if (baseUrl.contains("#")) {
            String[] parts = baseUrl.split("#", 2);
            return parts[0] + "?error_detail=" + encodedError + "#" + parts[1];
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
```

**修改范围**：将所有错误重定向的 URL 拼接逻辑替换为使用 `buildErrorUrl` 方法
- OIDC 认证失败
- 未收到授权码
- 用户不存在
- 查询用户信息失败
- 时间规则验证失败
- 生成 token 失败
- 处理回调失败

**示例修改**：
```java
// 修改前
String errorUrl = properties.getRedirect().getErrorUrl() + "&error_detail=" + 
    java.net.URLEncoder.encode(errorDescription, "UTF-8");

// 修改后
String errorUrl = buildErrorUrl(properties.getRedirect().getErrorUrl(), errorDescription);
```

## 验证步骤

### 步骤 1：确认系统配置

确认数据库表 `tp_system_config` 中存在以下配置：

```sql
SELECT * FROM tp_system_config 
WHERE config_key IN (
    'sso.keycloak.server.url',
    'sso.keycloak.realm',
    'sso.keycloak.client.id',
    'sso.keycloak.redirect.uri'
);
```

**期望结果**：
| config_key | config_value |
|-----------|-------------|
| sso.keycloak.server.url | https://sso.shxdx.com |
| sso.keycloak.realm | ps-realm |
| sso.keycloak.client.id | ps-be |
| sso.keycloak.redirect.uri | https://mid.shxdx.com/ps-be/api/sso/callback |

### 步骤 2：检查后端日志

重新测试 SSO 登录，检查后端日志中是否包含：

```
[SsoController] 生成的登录 URL: https://sso.shxdx.com/realms/ps-realm/protocol/openid-connect/auth?client_id=ps-be&redirect_uri=https://mid.shxdx.com/ps-be/api/sso/callback&...

[SsoController] 收到 OIDC 回调请求: code=xxx, state=yyy

[SsoController] Token交换使用的redirect_uri: https://mid.shxdx.com/ps-be/api/sso/callback

[KeycloakOAuth2Service] 开始使用授权码交换访问令牌: code=xxx, redirectUri=https://mid.shxdx.com/ps-be/api/sso/callback
```

**关键检查点**：
- 授权 URL 中的 `redirect_uri` 参数值
- Token 交换时使用的 `redirect_uri` 值
- 两者必须完全一致

### 步骤 3：完整登录流程测试

1. 清除浏览器缓存和 Cookie
2. 访问 `https://mid.shxdx.com/#/login`
3. 点击"使用SSO统一登录"按钮
4. 观察是否正确跳转到 Keycloak 登录页
5. 输入正确的账号密码
6. 验证是否成功跳转回 `https://mid.shxdx.com/#/sso/login?token=xxx`
7. 验证是否自动跳转到主页 `https://mid.shxdx.com/#/main`

### 步骤 4：错误场景测试

测试配置缺失情况：

1. 临时删除 `sso.keycloak.redirect.uri` 配置
2. 重新测试 SSO 登录
3. 验证是否使用降级方案（从配置文件获取）
4. 检查日志中是否有警告信息："系统配置表中未找到 sso.keycloak.redirect.uri，使用降级方案"

测试错误 URL 格式：

1. 故意输入错误的账号密码
2. 验证错误页面 URL 格式是否正确：`https://mid.shxdx.com/?error_detail=xxx#/login`
3. 验证前端能否正确解析错误信息

## 预期效果

### 修复前
- Token 交换时使用动态 URL，导致 `redirect_uri` 不匹配
- Keycloak 返回 `invalid_grant` 错误
- SSO 登录完全失败

### 修复后
- Token 交换使用固定的配置值，与授权请求一致
- Keycloak 成功验证 `redirect_uri`
- SSO 登录流程正常完成
- 错误 URL 格式正确，前端能正确解析

## 后续建议

### 短期优化

1. **验证配置完整性**
   - 在应用启动时检查必要的 SSO 配置是否完整
   - 配置缺失时给出明确的错误提示

2. **日志监控**
   - 添加 SSO 登录成功/失败的统计
   - 设置错误率告警

### 长期优化

1. **配置统一管理**
   - 考虑将 SSO 配置统一到配置文件，避免数据库和配置文件的双重维护
   - 或提供管理界面，允许在线修改 SSO 配置

2. **安全性增强**
   - 实现 state 参数的验证，防止 CSRF 攻击
   - 添加 code_challenge 支持 PKCE 流程

3. **用户体验优化**
   - 减少 SSO 回调页面的延迟时间
   - 添加登录进度提示

## 相关文件

- 修改文件：`ps-be/src/main/java/com/jiuxi/security/sso/controller/SsoController.java`
- 设计文档：`D:\projects\ps\.qoder\quests\sso-login-checking.md`
- 配置文件：`ps-be/src/main/resources/config/sec/security-dev.yml`
- 数据库表：`tp_system_config`

## 版本信息

- 修复版本：当前版本
- 修复日期：2025-12-03
- 修复人员：AI Assistant
- 测试状态：待验证

---

**注意事项**：
1. 修复后需要重新编译和部署后端服务
2. 建议先在测试环境验证，确认无误后再部署到生产环境
3. 部署前备份当前版本，以便回滚
4. 部署后密切监控日志，确保 SSO 登录功能正常
