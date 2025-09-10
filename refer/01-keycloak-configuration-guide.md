# Keycloak Realm 和客户端配置指南

本指南详细说明如何配置Keycloak Realm和OAuth2客户端，以实现Spring Boot应用的单点登录(SSO)功能。

## 前提条件

- Keycloak服务器已启动并运行在 `http://localhost:8080`
- 管理员账户：用户名 `admin`，密码 `admin`
- System-A 运行在 `http://localhost:8081/system-a`
- System-B 运行在 `http://localhost:8082/system-b`

## 1. 访问Keycloak管理控制台

1. 打开浏览器，访问 `http://localhost:8080`
2. 点击 "Administration Console"
3. 使用管理员凭据登录：
   - 用户名：`admin`
   - 密码：`admin`

## 2. 创建新的Realm

1. 在左上角的Realm下拉菜单中，点击 "Create Realm"
2. 填写Realm信息：
   - **Realm name**: `demo`
   - **Enabled**: 确保开关为开启状态
3. 点击 "Create" 按钮

## 3. 配置System-A客户端

### 3.1 创建客户端

1. 在左侧菜单中选择 "Clients"
2. 点击 "Create client" 按钮
3. 在 "General Settings" 页面填写：
   - **Client type**: `OpenID Connect`
   - **Client ID**: `system-a-client`
   - **Name**: `System A Client`
   - **Description**: `OAuth2 client for System A application`
4. 点击 "Next"

### 3.2 配置客户端能力

在 "Capability config" 页面：
- **Client authentication**: 开启 (ON)
- **Authorization**: 关闭 (OFF)
- **Authentication flow**: 勾选以下选项
  - ✅ Standard flow
  - ✅ Direct access grants
  - ❌ Implicit flow
  - ❌ Service accounts roles
  - ❌ OAuth 2.0 Device Authorization Grant
  - ❌ OIDC CIBA Grant

点击 "Next"

### 3.3 配置登录设置

在 "Login settings" 页面填写：
- **Root URL**: `http://localhost:8081`
- **Home URL**: `http://localhost:8081/system-a`
- **Valid redirect URIs**: 
  - `http://localhost:8081/system-a/login/oauth2/code/keycloak`
  - `http://localhost:8081/system-a/*`
- **Valid post logout redirect URIs**: `http://localhost:8081/system-a/*`
- **Web origins**: `http://localhost:8081`

点击 "Save"

### 3.4 获取客户端密钥

1. 在客户端详情页面，点击 "Credentials" 标签
2. 复制 "Client secret" 的值
3. 将此密钥更新到 `system-a/src/main/resources/application.yml` 文件中：

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          keycloak:
            client-secret: [复制的客户端密钥]
```

## 4. 配置System-B客户端

### 4.1 创建客户端

重复步骤3.1，但使用以下信息：
- **Client ID**: `system-b-client`
- **Name**: `System B Client`
- **Description**: `OAuth2 client for System B application`

### 4.2 配置客户端能力

与System-A相同的配置（参考步骤3.2）

### 4.3 配置登录设置

在 "Login settings" 页面填写：
- **Root URL**: `http://localhost:8082`
- **Home URL**: `http://localhost:8082/system-b`
- **Valid redirect URIs**: 
  - `http://localhost:8082/system-b/login/oauth2/code/keycloak`
  - `http://localhost:8082/system-b/*`
- **Valid post logout redirect URIs**: `http://localhost:8082/system-b/*`
- **Web origins**: `http://localhost:8082`

### 4.4 获取客户端密钥

1. 获取System-B的客户端密钥（参考步骤3.4）
2. 将密钥更新到 `system-b/src/main/resources/application.yml` 文件中

## 5. 创建测试用户

### 5.1 创建用户

1. 在左侧菜单中选择 "Users"
2. 点击 "Add user" 按钮
3. 填写用户信息：
   - **Username**: `testuser`
   - **Email**: `testuser@example.com`
   - **First name**: `Test`
   - **Last name**: `User`
   - **Email verified**: 开启
   - **Enabled**: 开启
4. 点击 "Create"

### 5.2 设置用户密码

1. 在用户详情页面，点击 "Credentials" 标签
2. 点击 "Set password" 按钮
3. 填写密码信息：
   - **Password**: `password123`
   - **Password confirmation**: `password123`
   - **Temporary**: 关闭（如果希望用户首次登录时不需要更改密码）
4. 点击 "Save"

## 6. 配置Realm设置（可选）

### 6.1 登录设置

1. 在左侧菜单中选择 "Realm settings"
2. 点击 "Login" 标签
3. 根据需要调整以下设置：
   - **User registration**: 是否允许用户自注册
   - **Forgot password**: 是否启用忘记密码功能
   - **Remember me**: 是否启用记住我功能
   - **Login with email**: 是否允许使用邮箱登录

### 6.2 令牌设置

1. 点击 "Tokens" 标签
2. 调整令牌生命周期：
   - **Access token lifespan**: 访问令牌有效期（默认5分钟）
   - **Refresh token lifespan**: 刷新令牌有效期（默认30分钟）
   - **SSO session idle**: SSO会话空闲超时（默认30分钟）
   - **SSO session max**: SSO会话最大时长（默认10小时）

## 7. 测试SSO功能

### 7.1 启动应用

确保以下服务正在运行：
- Keycloak: `http://localhost:8080`
- System-A: `http://localhost:8081/system-a`
- System-B: `http://localhost:8082/system-b`

### 7.2 测试单点登录

1. **首次登录**：
   - 访问 `http://localhost:8081/system-a`
   - 系统会重定向到Keycloak登录页面
   - 使用测试用户凭据登录：
     - 用户名：`testuser`
     - 密码：`password123`
   - 登录成功后会重定向回System-A

2. **测试SSO**：
   - 在同一浏览器中访问 `http://localhost:8082/system-b`
   - 应该自动登录，无需再次输入凭据

3. **测试登出**：
   - 在任一系统中点击登出
   - 访问另一个系统时应该需要重新登录

## 8. 故障排除

### 8.1 常见问题

1. **重定向URI不匹配**：
   - 检查客户端配置中的 "Valid redirect URIs" 是否正确
   - 确保URI格式完全匹配，包括端口号和路径

2. **客户端密钥错误**：
   - 确认application.yml中的client-secret与Keycloak中的一致
   - 重新生成客户端密钥并更新配置

3. **CORS错误**：
   - 检查客户端配置中的 "Web origins" 设置
   - 确保包含正确的应用域名和端口

4. **用户无法登录**：
   - 确认用户已启用且邮箱已验证
   - 检查用户密码是否正确设置
   - 确认用户属于正确的Realm

### 8.2 日志检查

1. **Keycloak日志**：
   ```bash
   docker logs keycloak-server
   ```

2. **应用日志**：
   - 检查Spring Boot应用的控制台输出
   - 查看OAuth2相关的错误信息

### 8.3 配置验证

1. **验证Realm配置**：
   - 访问 `http://localhost:8080/realms/demo/.well-known/openid_configuration`
   - 确认端点配置正确

2. **验证客户端配置**：
   - 在Keycloak管理控制台中检查客户端设置
   - 确认所有URL和设置正确

## 9. 高级配置

### 9.1 自定义登录主题

1. 在Realm设置中选择 "Themes" 标签
2. 选择或自定义登录主题
3. 配置品牌和样式

### 9.2 多因素认证

1. 在Realm设置中选择 "Authentication" 标签
2. 配置认证流程
3. 启用OTP或其他多因素认证方法

### 9.3 用户联邦

1. 在左侧菜单中选择 "User federation"
2. 配置LDAP或其他用户存储
3. 设置用户同步策略

## 10. 安全最佳实践

1. **客户端密钥管理**：
   - 定期轮换客户端密钥
   - 使用环境变量存储敏感信息
   - 避免在代码中硬编码密钥

2. **令牌管理**：
   - 设置合理的令牌过期时间
   - 启用令牌刷新机制
   - 实施令牌撤销策略

3. **网络安全**：
   - 在生产环境中使用HTTPS
   - 配置适当的CORS策略
   - 实施网络访问控制

4. **监控和审计**：
   - 启用Keycloak事件日志
   - 监控异常登录活动
   - 定期审查用户权限

---

**注意**：本指南基于Keycloak 23.0.1版本。不同版本的界面和配置选项可能略有差异。在生产环境中部署前，请确保进行充分的测试和安全评估。