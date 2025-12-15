# SSO 系统配置完整清单

## 📋 配置项总览

SQL 脚本已更新完成，现在包含 **6 个完整的 SSO 配置项**：

| 序号 | 配置键 | 配置值 | 说明 | 用途 |
|-----|--------|--------|------|------|
| 1 | `sso.keycloak.server.url` | `https://sso.shxdx.com` | Keycloak 服务器地址 | OAuth2 授权和 Token 端点的基础 URL |
| 2 | `sso.keycloak.realm` | `ps-realm` | Keycloak Realm 名称 | 租户隔离标识 |
| 3 | `sso.keycloak.client.id` | `ps-be` | Keycloak 客户端 ID | OAuth2 客户端标识 |
| 4 | `sso.keycloak.redirect.uri` | `https://mid.shxdx.com/ps-be/api/sso/callback` | SSO 登录回调地址（后端） | OAuth2 授权码流程的回调接口 |
| 5 | `sso.keycloak.callback.url` | `https://mid.shxdx.com` | 前端基础 URL | 用于构建 sso.html 中转页面 URL |
| 6 | `sso.keycloak.logout.url` | `https://mid.shxdx.com/#/login` | SSO 注销后重定向地址 | Keycloak 注销后的 `post_logout_redirect_uri` |

## 🔄 配置项详细说明

### 1. sso.keycloak.server.url
**值**: `https://sso.shxdx.com`

**作用**:
- 构建 Keycloak 授权端点 URL
- 构建 Token 交换端点 URL
- 构建用户信息端点 URL
- 构建注销端点 URL

**使用位置**:
- `SsoController.getLoginUrl()` - 构建登录 URL
- `KeycloakOAuth2Service.exchangeCodeForToken()` - Token 交换

### 2. sso.keycloak.realm
**值**: `ps-realm`

**作用**:
- Keycloak 多租户隔离标识
- 所有 OIDC 端点 URL 的必要组成部分

**URL 示例**:
```
https://sso.shxdx.com/realms/ps-realm/protocol/openid-connect/auth
https://sso.shxdx.com/realms/ps-realm/protocol/openid-connect/token
```

### 3. sso.keycloak.client.id
**值**: `ps-be`

**作用**:
- OAuth2 客户端标识
- 授权请求必需参数
- Token 交换请求必需参数

**使用场景**:
- 所有 OIDC 协议交互都需要此参数

### 4. sso.keycloak.redirect.uri ⭐
**值**: `https://mid.shxdx.com/ps-be/api/sso/callback`

**作用**:
- OAuth2 授权码流程的核心配置
- **两阶段必须使用完全相同的值**:
  - 阶段 1: 授权请求时作为查询参数
  - 阶段 2: Token 交换时作为表单参数

**关键代码位置**:
```java
// SsoController.java 第 327 行 - 授权请求
String loginUrl = String.format(
    "%s/realms/%s/protocol/openid-connect/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=openid",
    serverUrl, realm, clientId, redirectUri
);

// SsoController.java 第 528 行 - Token 交换
String redirectUri = tpSystemConfigService.getConfigValue("sso.keycloak.redirect.uri");
KeycloakOAuth2Service.TokenResponse tokenResponse = oAuth2Service.exchangeCodeForToken(code, redirectUri);
```

**Keycloak 配置要求**:
- 必须在客户端 `ps-be` 的"有效的重定向 URI"中添加此地址

### 5. sso.keycloak.callback.url ✨
**值**: `https://mid.shxdx.com`

**作用**:
- 构建 `sso.html` 中转页面的完整 URL
- 避免 Hash 路由与 URL 参数的冲突

**使用流程**:
```
后端登录成功 
  → 构建 URL: https://mid.shxdx.com/sso.html?token=xxx
  → sso.html 处理 token 参数
  → 跳转到: https://mid.shxdx.com/index.html#/sso/login?token=xxx
```

**关键代码位置**:
```java
// SsoController.java 第 606 行
String frontendUrl = tpSystemConfigService.getConfigValue("sso.keycloak.callback.url");
String redirectUrl = frontendUrl + "/sso.html?token=xxx";
```

### 6. sso.keycloak.logout.url 🚪
**值**: `https://mid.shxdx.com/#/login`

**作用**:
- Keycloak 注销后的重定向地址
- 对应 OIDC 协议的 `post_logout_redirect_uri` 参数

**使用场景**:
```java
// 用户点击注销按钮
→ 调用后端 /api/sso/logout
→ 后端构建 Keycloak 注销 URL:
  https://sso.shxdx.com/realms/ps-realm/protocol/openid-connect/logout
  ?client_id=ps-be
  &post_logout_redirect_uri=https://mid.shxdx.com/#/login
→ 前端重定向到 Keycloak 注销
→ Keycloak 注销后自动跳转回登录页
```

**注意事项**:
- 此地址可能也需要在 Keycloak 客户端的注销重定向 URI 白名单中配置
- 使用 Hash 路由格式 `#/login`

## 🔒 Keycloak 客户端配置清单

确保 Keycloak 管理控制台中 `ps-be` 客户端的以下配置正确：

### 基本设置
- **客户端 ID**: `ps-be`
- **客户端协议**: `openid-connect`
- **访问类型**: `confidential`
- **标准流程已启用**: `ON`
- **直接访问授权已启用**: `OFF`

### 有效的重定向 URI
```
https://mid.shxdx.com/ps-be/api/sso/callback
https://mid.shxdx.com/*
http://192.168.0.139/*  (如果需要内网访问)
```

### 有效的注销后重定向 URI (如果需要)
```
https://mid.shxdx.com/#/login
https://mid.shxdx.com/*
```

### Web Origins
```
https://mid.shxdx.com
http://192.168.0.139  (如果需要内网访问)
```

### 客户端密钥
- 在"凭据"标签中查看并复制 Secret
- 确保与后端配置文件 `security-dev.yml` 中的 `client-secret` 一致
- 当前值: `xMXvDGzby4Z48szob7i2fuZlZy5Wlqrh`

## 📝 SQL 脚本执行

### 执行方式
```sql
-- 方式 1: 完整执行（推荐）
source /path/to/sso_config_fix.sql

-- 方式 2: 手动执行
-- 复制 SQL 内容到数据库客户端执行
```

### 执行结果验证
脚本会自动输出两个查询结果：

**1. 配置完整性检查**
```
+----------+--------------+------------+
| check_type | config_count | status     |
+----------+--------------+------------+
| 配置检查 | 6            | ✓ 配置完整 |
+----------+--------------+------------+
```

**2. 配置详情展示**
```
+--------------------------------+-----------------------------------------------+---------------------------+---------------------+
| 配置项                         | 配置值                                        | 说明                      | 更新时间            |
+--------------------------------+-----------------------------------------------+---------------------------+---------------------+
| sso.keycloak.callback.url      | https://mid.shxdx.com                         | 前端基础URL（用于SSO...   | 2025-12-03 10:30:00 |
| sso.keycloak.client.id         | ps-be                                         | Keycloak 客户端 ID        | 2025-12-03 10:30:00 |
| sso.keycloak.logout.url        | https://mid.shxdx.com/#/login                 | SSO注销后重定向地址       | 2025-12-03 10:30:00 |
| sso.keycloak.realm             | ps-realm                                      | Keycloak Realm 名称       | 2025-12-03 10:30:00 |
| sso.keycloak.redirect.uri      | https://mid.shxdx.com/ps-be/api/sso/callback  | SSO 登录回调地址（后端）  | 2025-12-03 10:30:00 |
| sso.keycloak.server.url        | https://sso.shxdx.com                         | Keycloak 服务器地址       | 2025-12-03 10:30:00 |
+--------------------------------+-----------------------------------------------+---------------------------+---------------------+
```

## ⚠️ 重要提示

1. **配置实时生效**: 修改系统配置表后无需重启应用
2. **Keycloak 同步**: 修改回调地址后，必须同步更新 Keycloak 客户端配置
3. **redirect_uri 一致性**: 授权和 Token 交换两阶段必须使用完全相同的 `redirect_uri`
4. **环境差异**: 
   - 开发环境: `http://localhost:10801`
   - 测试环境: `http://test-server`
   - 生产环境: `https://mid.shxdx.com`
5. **安全性**: 客户端密钥 (`client-secret`) 应妥善保管，生产环境使用环境变量

## 🔧 故障排查

### 问题 1: invalid_grant: Incorrect redirect_uri
**原因**: Token 交换时的 `redirect_uri` 与授权时不一致

**解决**: 
- 检查系统配置表中 `sso.keycloak.redirect.uri` 的值
- 确认代码从配置表读取而非动态生成

### 问题 2: 注销后无法跳转
**原因**: `post_logout_redirect_uri` 未在 Keycloak 白名单中

**解决**:
- 在 Keycloak 客户端配置中添加注销重定向 URI
- 或在客户端设置中启用"强制注销后重定向"

### 问题 3: sso.html 404 错误
**原因**: 前端基础 URL 配置错误或文件不存在

**解决**:
- 检查 `sso.keycloak.callback.url` 配置是否正确
- 确认 `public/sso.html` 文件已部署

## 📊 配置依赖关系图

```
┌─────────────────────────────────────────────────┐
│           SSO 登录完整流程                       │
└─────────────────────────────────────────────────┘
                       │
        ┌──────────────┴──────────────┐
        ↓                             ↓
┌───────────────┐            ┌────────────────┐
│ 1. 获取登录URL │            │ 4. 登录成功回调 │
└───────────────┘            └────────────────┘
        │                             │
        │ 使用:                       │ 使用:
        │ • server.url                │ • redirect.uri ⚠️
        │ • realm                     │ • callback.url
        │ • client.id                 │
        │ • redirect.uri ⚠️           │
        ↓                             ↓
┌───────────────┐            ┌────────────────┐
│ 2. 跳转Keycloak│            │ 5. 跳转sso.html │
└───────────────┘            └────────────────┘
        │                             │
        ↓                             ↓
┌───────────────┐            ┌────────────────┐
│ 3. 用户登录认证 │            │ 6. 跳转Vue路由  │
└───────────────┘            └────────────────┘

注: ⚠️ 标记的配置项在两个阶段必须完全一致
```

---

**文档版本**: 1.0  
**创建日期**: 2025-12-03  
**最后更新**: 2025-12-03  
**维护人员**: SSO 集成小组
