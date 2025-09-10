# Keycloak 模块使用示例

## 基本使用

### 1. 获取 Keycloak 实例

```javascript
// 在 Vue 组件中使用
export default {
  async mounted() {
    try {
      // 获取 Keycloak 实例
      const kc = await this.$kc();
      
      if (kc.authenticated) {
        console.log('用户已认证');
        console.log('用户信息:', kc.tokenParsed);
      } else {
        console.log('用户未认证');
        await kc.login();
      }
    } catch (error) {
      console.error('Keycloak 初始化失败:', error);
    }
  }
}
```

### 2. 登出功能

```javascript
export default {
  methods: {
    async logout() {
      try {
        const kc = await this.$kc();
        await kc.logout();
      } catch (error) {
        console.error('登出失败:', error);
      }
    }
  }
}
```

## 配置管理

### 1. 设置 Keycloak 服务器配置

```javascript
import { setKeycloakConfig } from '@fb/fb-core/src/util/keycloak';

// 在应用启动前设置配置
setKeycloakConfig({
  url: 'https://your-keycloak-server.com/',
  realm: 'your-realm',
  clientId: 'your-client-id'
});
```

### 2. 设置初始化配置

```javascript
import { setKeycloakInitConfig } from '@fb/fb-core/src/util/keycloak';

// 设置初始化行为
setKeycloakInitConfig({
  onLoad: 'login-required', // 强制登录
  checkLoginIframe: true,   // 启用 iframe 检查
  silentCheckSsoRedirectUri: window.location.origin + '/custom-sso-check.html'
});
```

### 3. 获取当前配置

```javascript
import { getKeycloakConfig, getKeycloakInitConfig } from '@fb/fb-core/src/util/keycloak';

// 获取当前 Keycloak 配置
const config = getKeycloakConfig();
console.log('当前配置:', config);

// 获取当前初始化配置
const initConfig = getKeycloakInitConfig();
console.log('初始化配置:', initConfig);
```

## 高级用法

### 1. 动态环境配置

```javascript
// 根据环境动态设置配置
if (process.env.NODE_ENV === 'development') {
  setKeycloakConfig({
    url: 'http://localhost:8180/',
    realm: 'dev-realm',
    clientId: 'dev-client'
  });
} else if (process.env.NODE_ENV === 'production') {
  setKeycloakConfig({
    url: 'https://prod-keycloak.company.com/',
    realm: 'prod-realm',
    clientId: 'prod-client'
  });
}
```

### 2. 重置实例（用于测试）

```javascript
import { resetKeycloakInstance } from '@fb/fb-core/src/util/keycloak';

// 在测试中重置实例
beforeEach(() => {
  resetKeycloakInstance();
});
```

### 3. 获取用户信息和令牌

```javascript
import { getUserInfo, getToken, isAuthenticated } from '@fb/fb-core/src/util/keycloak';

// 检查认证状态
const authenticated = await isAuthenticated();

if (authenticated) {
  // 获取用户信息
  const userInfo = await getUserInfo();
  console.log('用户信息:', userInfo);
  
  // 获取访问令牌
  const token = await getToken();
  console.log('访问令牌:', token);
}
```

## 注意事项

1. **配置时机**: 配置方法应在 Keycloak 实例初始化之前调用，通常在应用启动时设置。

2. **单例模式**: `$kc()` 方法返回的是单例实例，多次调用返回同一个实例。

3. **异步处理**: 所有方法都是异步的，需要使用 `await` 或 `.then()` 处理。

4. **错误处理**: 建议在使用时添加适当的错误处理逻辑。

5. **配置更改**: 如果在实例已初始化后更改配置，需要调用 `resetKeycloakInstance()` 重置实例。