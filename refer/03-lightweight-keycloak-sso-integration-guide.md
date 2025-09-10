# 轻量级 Keycloak SSO 接入方案详细规划

## 方案概述

本方案采用**反向代理 + Keycloak**的轻量级架构，实现对现有基于 Shiro 的 ps-be 系统的 SSO 接入，无需大幅修改现有代码，实现平滑过渡。

### 核心思路
- **Keycloak** 作为统一身份认证中心
- **反向代理**（Nginx/Traefik）处理 OIDC 认证流程
- **ps-be** 只需添加 JWT Token Realm 解析认证结果
- **最小化代码改动**，保持现有 Shiro 架构

## 架构设计

```
用户浏览器
    ↓
反向代理 (Nginx + auth_request)
    ↓ (认证成功后转发请求 + JWT Header)
ps-be (Shiro + JWT Realm)
    ↓
ps-fe
```

### 认证流程
1. 用户访问应用 → 反向代理检查认证状态
2. 未认证 → 重定向到 Keycloak 登录页面
3. Keycloak 认证成功 → 返回 JWT Token
4. 反向代理验证 Token → 在请求头中添加用户信息
5. ps-be 通过 JWT Realm 解析用户信息 → 创建 Shiro Subject
6. 正常访问应用

## 详细实施步骤

### 第一阶段：环境准备

#### 1.1 Keycloak 配置

**目标**：配置 Keycloak 作为身份认证中心

**步骤**：
1. 启动 Keycloak 服务（已通过 docker-compose 完成）
2. 创建 Realm（领域）
   ```bash
   # 访问 http://localhost:8180
   # 用户名：admin，密码：admin123
   ```
3. 创建 Client 配置
   - Client ID: `ps-system`
   - Client Protocol: `openid-connect`
   - Access Type: `confidential`
   - Valid Redirect URIs: `http://localhost/auth/callback`
   - Web Origins: `http://localhost`

4. 创建用户和角色
   - 创建测试用户
   - 定义角色映射
   - 配置用户属性

#### 1.2 反向代理准备

**目标**：准备 Nginx 配置，集成 OIDC 认证模块

**技术选型**：
- **Nginx + lua-resty-openidc**：轻量级 OIDC 客户端
- **Nginx + auth_request**：外部认证服务
- **Traefik + Forward Auth**：现代化反向代理

**推荐方案**：Nginx + lua-resty-openidc

### 第二阶段：反向代理配置

#### 2.1 Nginx + OIDC 配置

**文件位置**：`ps-be/reverse-proxy-config/nginx/nginx.conf`

**核心配置**：
```nginx
http {
    # 加载 OIDC 模块
    lua_package_path "/usr/local/openresty/lualib/?.lua;;";
    
    # OIDC 配置
    lua_shared_dict discovery 1m;
    lua_shared_dict jwks 1m;
    
    server {
        listen 80;
        server_name localhost;
        
        # OIDC 认证配置
        location / {
            access_by_lua_block {
                local opts = {
                    redirect_uri = "http://localhost/auth/callback",
                    discovery = "http://keycloak:8080/realms/ps-realm/.well-known/openid_configuration",
                    client_id = "ps-system",
                    client_secret = "your-client-secret",
                    scope = "openid profile email",
                    session_contents = {id_token=true}
                }
                
                local res, err = require("resty.openidc").authenticate(opts)
                if err then
                    ngx.status = 500
                    ngx.say(err)
                    ngx.exit(500)
                end
                
                -- 设置用户信息到请求头
                ngx.req.set_header("X-User-ID", res.id_token.sub)
                ngx.req.set_header("X-User-Name", res.id_token.preferred_username)
                ngx.req.set_header("X-User-Email", res.id_token.email)
                ngx.req.set_header("X-JWT-Token", res.access_token)
            }
            
            # 转发到后端服务
            proxy_pass http://ps-be:8080;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
        
        # 前端静态资源
        location /static/ {
            proxy_pass http://ps-fe:80/;
        }
    }
}
```

#### 2.2 Docker Compose 集成

**文件位置**：`ps-be/reverse-proxy-config/docker-compose.nginx.yml`

```yaml
version: '3.8'
services:
  nginx-oidc:
    image: openresty/openresty:alpine
    container_name: ps-nginx-oidc
    ports:
      - "80:80"
    volumes:
      - ./nginx/nginx.conf:/usr/local/openresty/nginx/conf/nginx.conf
      - ./nginx/lua:/usr/local/openresty/lualib/resty
    depends_on:
      - keycloak
      - ps-be
    networks:
      - sso-network
```

### 第三阶段：后端 Shiro 集成

#### 3.1 添加 JWT 依赖

**文件位置**：`ps-be/pom.xml`

```xml
<!-- JWT 支持 -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
</dependency>
```

#### 3.2 创建 JWT Realm

**文件位置**：`ps-be/src/main/java/com/example/security/JwtRealm.java`

```java
@Component
public class JwtRealm extends AuthorizingRealm {
    
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        JwtToken jwtToken = (JwtToken) token;
        String userId = jwtToken.getUserId();
        String username = jwtToken.getUsername();
        
        // 创建用户主体
        SimpleAuthenticationInfo authInfo = new SimpleAuthenticationInfo(
            username, jwtToken.getCredentials(), getName());
        
        return authInfo;
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        
        // 从数据库或缓存获取用户权限
        Set<String> roles = getUserRoles(username);
        Set<String> permissions = getUserPermissions(username);
        
        SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo();
        authInfo.setRoles(roles);
        authInfo.setStringPermissions(permissions);
        
        return authInfo;
    }
}
```

#### 3.3 创建 JWT Token

**文件位置**：`ps-be/src/main/java/com/example/security/JwtToken.java`

```java
public class JwtToken implements AuthenticationToken {
    private String userId;
    private String username;
    private String email;
    private String token;
    
    public JwtToken(String userId, String username, String email, String token) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.token = token;
    }
    
    @Override
    public Object getPrincipal() {
        return username;
    }
    
    @Override
    public Object getCredentials() {
        return token;
    }
    
    // getters and setters
}
```

#### 3.4 创建认证过滤器

**文件位置**：`ps-be/src/main/java/com/example/security/JwtAuthenticationFilter.java`

```java
@Component
public class JwtAuthenticationFilter extends BasicHttpAuthenticationFilter {
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // 从请求头获取用户信息
        String userId = httpRequest.getHeader("X-User-ID");
        String username = httpRequest.getHeader("X-User-Name");
        String email = httpRequest.getHeader("X-User-Email");
        String jwtToken = httpRequest.getHeader("X-JWT-Token");
        
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(username)) {
            // 创建 JWT Token
            JwtToken token = new JwtToken(userId, username, email, jwtToken);
            
            try {
                // 执行登录
                Subject subject = getSubject(request, response);
                subject.login(token);
                return true;
            } catch (Exception e) {
                log.error("JWT authentication failed", e);
            }
        }
        
        return false;
    }
}
```

#### 3.5 更新 Shiro 配置

**文件位置**：`ps-be/src/main/java/com/example/config/ShiroConfig.java`

```java
@Configuration
public class ShiroConfig {
    
    @Bean
    public JwtRealm jwtRealm() {
        return new JwtRealm();
    }
    
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        
        // 添加多个 Realm
        List<Realm> realms = Arrays.asList(
            jwtRealm(),           // JWT Realm（优先）
            originalShiroRealm()  // 原有 Shiro Realm（兼容）
        );
        manager.setRealms(realms);
        
        return manager;
    }
    
    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean factory = new ShiroFilterFactoryBean();
        factory.setSecurityManager(securityManager());
        
        // 添加自定义过滤器
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", new JwtAuthenticationFilter());
        factory.setFilters(filters);
        
        // 配置过滤规则
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/api/**", "jwt");  // API 接口使用 JWT 认证
        filterMap.put("/login", "anon");  // 登录页面允许匿名访问
        filterMap.put("/**", "authc");    // 其他页面需要认证
        
        factory.setFilterChainDefinitionMap(filterMap);
        
        return factory;
    }
}
```

### 第四阶段：前端适配

#### 4.1 移除前端登录逻辑

**目标**：前端不再处理登录，由反向代理统一处理

**修改文件**：
- `ps-fe/src/router/index.js` - 移除登录路由守卫
- `ps-fe/src/views/login/` - 保留但不使用登录组件
- `ps-fe/src/service/auth.js` - 简化认证服务

#### 4.2 适配认证状态检查

**文件位置**：`ps-fe/src/service/auth.js`

```javascript
// 简化的认证服务
export const authService = {
  // 检查认证状态（通过后端 API）
  async checkAuthStatus() {
    try {
      const response = await api.get('/api/auth/status');
      return response.data;
    } catch (error) {
      // 认证失败，重定向到登录
      window.location.href = '/auth/login';
      return null;
    }
  },
  
  // 登出（重定向到 Keycloak 登出）
  logout() {
    window.location.href = '/auth/logout';
  }
};
```

### 第五阶段：测试和部署

#### 5.1 本地测试环境

**启动顺序**：
1. 启动 Keycloak：`docker-compose up keycloak`
2. 启动后端服务：`docker-compose up ps-be`
3. 启动前端服务：`docker-compose up ps-fe`
4. 启动反向代理：`docker-compose up nginx-oidc`

**测试步骤**：
1. 访问 `http://localhost` → 应该重定向到 Keycloak 登录
2. 使用测试账号登录 → 应该返回到应用首页
3. 检查后端日志 → 确认 JWT 认证成功
4. 测试权限控制 → 确认角色和权限正常

#### 5.2 生产环境部署

**配置调整**：
- 更新域名和证书配置
- 配置 Keycloak 生产环境参数
- 调整 Nginx 性能参数
- 配置日志和监控

## 优势分析

### 技术优势
1. **最小化改动**：现有 Shiro 代码基本不变
2. **平滑过渡**：支持渐进式迁移
3. **高性能**：反向代理层面处理认证，减少后端压力
4. **标准化**：基于 OIDC 标准，便于集成其他系统

### 业务优势
1. **统一认证**：一套账号体系管理所有应用
2. **单点登录**：用户体验提升
3. **安全增强**：集中的身份管理和审计
4. **扩展性强**：便于接入新的应用系统

## 风险和注意事项

### 技术风险
1. **反向代理单点故障**：需要考虑高可用部署
2. **Token 安全**：需要配置 HTTPS 和 Token 过期策略
3. **性能影响**：每次请求都需要验证 Token

### 解决方案
1. **高可用部署**：多实例 + 负载均衡
2. **安全加固**：HTTPS + Token 刷新机制
3. **性能优化**：Token 缓存 + 连接池优化

## 后续优化方向

### 短期优化
1. 完善错误处理和日志记录
2. 添加健康检查和监控
3. 优化 Token 缓存策略

### 长期规划
1. 微服务架构下的服务间认证
2. 细粒度权限控制（RBAC/ABAC）
3. 多租户支持
4. API 网关集成

## 总结

本方案通过反向代理 + Keycloak 的架构，实现了对现有 Shiro 系统的轻量级 SSO 改造。核心优势是**最小化代码改动**，通过在架构层面的调整实现 SSO 功能，为后续的系统演进奠定了良好的基础。

整个方案分为 5 个阶段实施，每个阶段都有明确的目标和可验证的结果，确保改造过程的可控性和安全性。