# 注销页面自动跳转实现说明

## 背景

Keycloak 的注销页面(`logout.ftl`)不属于标准的 Login Theme 页面类型，因此无法直接通过 React 组件在 Keycloakify 中实现。

## 实现方案

### 方案一:创建自定义 FreeMarker 模板(推荐)

在主题构建后，手动添加 `logout.ftl` 模板文件到 JAR 包中。

#### 步骤

1. 构建主题 JAR 包:
   ```bash
   npm run build-keycloak-theme
   ```

2. 解压 JAR 包:
   ```bash
   cd dist_keycloak
   mkdir temp
   cd temp
   jar -xf ../keycloak-theme-for-kc-22-to-25.jar
   ```

3. 创建 `logout.ftl` 文件:
   ```bash
   mkdir -p theme/ps-theme/login
   ```

4. 在 `theme/ps-theme/login/logout.ftl` 中添加以下内容:

```ftl
<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>正在退出登录...</title>
  <style>
    body {
      font-family: Microsoft YaHei, SimSun, sans-serif;
      background: linear-gradient(180deg, #fde7d8, #d10000, #ffad99 35%, #fbe6d7 65%, #fdf4ed);
      display: flex;
      align-items: center;
      justify-content: center;
      min-height: 100vh;
      margin: 0;
    }
    .logout-container {
      background: white;
      border-radius: 16px;
      padding: 40px;
      box-shadow: 0 4px 12px rgba(192, 0, 0, 0.2);
      text-align: center;
      max-width: 400px;
    }
    .logout-message {
      font-size: 18px;
      color: #333;
      margin-bottom: 30px;
    }
    .logout-loader {
      width: 40px;
      height: 40px;
      border: 4px solid #f3f3f3;
      border-top: 4px solid #C00000;
      border-radius: 50%;
      animation: spin 1s linear infinite;
      margin: 0 auto;
    }
    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }
  </style>
</head>
<body>
  <div class="logout-container">
    <p class="logout-message">正在退出登录，稍候...</p>
    <div class="logout-loader"></div>
  </div>

  <script>
    (function() {
      try {
        // 构建 OIDC 注销 URL
        var realmUrl = "${url.realm}";
        var logoutUrl = realmUrl + "/protocol/openid-connect/logout";
        
        // 准备查询参数
        var params = new URLSearchParams();
        
        // 添加 id_token_hint(如果存在)
        <#if idTokenHint??>
        params.append("id_token_hint", "${idTokenHint}");
        </#if>
        
        // 添加 post_logout_redirect_uri
        var postLogoutUri = "${url.login!''}";
        if (!postLogoutUri) {
          postLogoutUri = window.location.origin;
        }
        params.append("post_logout_redirect_uri", postLogoutUri);
        
        // 拼接完整 URL 并跳转
        var fullLogoutUrl = logoutUrl + "?" + params.toString();
        window.location.replace(fullLogoutUrl);
      } catch (error) {
        // 降级策略:直接跳转到登录页
        console.error("注销出错:", error);
        setTimeout(function() {
          window.location.replace("${url.login!'/'}"|| window.location.origin);
        }, 3000);
      }
    })();
  </script>
</body>
</html>
```

5. 重新打包 JAR:
   ```bash
   jar -cf ../ps-theme-with-logout.jar *
   cd ../..
   ```

6. 部署更新后的 JAR 包到 Keycloak

### 方案二:通过应用层处理注销

在前端应用中处理注销逻辑,不依赖 Keycloak 主题:

```javascript
// 在你的前端应用中
function handleLogout() {
  const keycloakUrl = 'https://your-keycloak-domain';
  const realm = 'ps-realm';
  const idToken = keycloak.idToken; // 从 Keycloak JS 适配器获取
  
  const logoutUrl = `${keycloakUrl}/realms/${realm}/protocol/openid-connect/logout`;
  const redirectUri = encodeURIComponent(window.location.origin + '/login');
  
  window.location.href = `${logoutUrl}?id_token_hint=${idToken}&post_logout_redirect_uri=${redirectUri}`;
}
```

## 主页功能已实现

主页主题覆盖功能已通过 React 组件成功实现:
- 页面组件:`src/login/pages/HomePage.tsx`
- 路由配置:`src/login/KcPage.tsx` 中的 `info.ftl` case
- 样式定义:`src/login/resources/css/login.css` 中的主页样式

## 测试验证

### 主页测试
访问 `/realms/{your-realm}/account/` 应该看到定制的主页。

### 注销测试
1. 如果使用方案一,访问注销 URL 会自动跳转
2. 如果使用方案二,在应用中调用 `handleLogout()` 函数

## 注意事项

1. `idTokenHint` 参数对于 OIDC 标准注销很重要,确保在注销时能够获取到
2. `post_logout_redirect_uri` 必须在 Keycloak Client 配置的 "Valid Post Logout Redirect URIs" 中配置
3. 使用 `window.location.replace()` 而不是 `window.location.href` 可以避免浏览器历史记录堆积

## 后续改进

如果需要在 Keycloakify 项目中集成 FreeMarker 模板,可以考虑:
1. 使用 Keycloakify 的自定义页面扩展功能
2. 通过构建脚本自动将 `.ftl` 文件注入到 JAR 包中
3. 升级到支持更多页面类型的 Keycloakify 版本
