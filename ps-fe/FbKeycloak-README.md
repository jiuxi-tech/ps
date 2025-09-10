# FbKeycloak 组件使用指南

## 概述

FbKeycloak 是一个基于 Vue 2.6.12 的 Keycloak 认证状态管理容器组件，提供完整的认证流程管理、状态监控和错误处理功能。

## 组件位置

```
@fb/admin-base/components/FbKeycloak.vue
```

## 基本用法

```vue
<template>
  <div>
    <FbKeycloak>
      <template #authenticated="{ user, token, keycloak }">
        <div>
          <h1>欢迎，{{ user.name }}！</h1>
          <p>您的角色：{{ user.roles?.join(', ') }}</p>
          <button @click="handleLogout">退出登录</button>
        </div>
      </template>
    </FbKeycloak>
  </div>
</template>

<script>
import FbKeycloak from '@fb/admin-base/components/FbKeycloak.vue'

export default {
  components: {
    FbKeycloak
  },
  methods: {
    handleLogout() {
      this.$refs.keycloak.logout()
    }
  }
}
</script>
```

## 组件属性 (Props)

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `autoLogin` | Boolean | `true` | 是否自动登录 |
| `keycloakConfig` | Object | `{}` | Keycloak 配置参数 |
| `initConfig` | Object | `{}` | Keycloak 初始化配置 |
| `showLoading` | Boolean | `true` | 是否显示加载状态 |
| `maxRetries` | Number | `3` | 最大重试次数 |

## 插槽 (Slots)

### authenticated

认证成功后渲染的内容插槽。

**作用域参数：**
- `user`: 用户信息对象
- `token`: 访问令牌
- `keycloak`: Keycloak 实例

## 事件 (Events)

| 事件名 | 参数 | 说明 |
|--------|------|------|
| `authenticated` | `{ user, token, keycloak }` | 认证成功时触发 |
| `unauthenticated` | - | 认证失效时触发 |
| `error` | `{ error, message, retryCount }` | 认证错误时触发 |
| `logout` | - | 退出登录时触发 |

## 核心功能

- ✅ Keycloak 认证状态管理
- ✅ 实时认证状态监控
- ✅ 自动登录和重试机制
- ✅ 可视化认证异常处理
- ✅ 响应式设计支持
- ✅ 完善的错误处理机制

## 注意事项

1. **依赖要求**: 确保已正确配置 Keycloak 工具模块
2. **样式定制**: 可以通过 CSS 变量或覆盖样式来定制外观
3. **性能优化**: 组件会自动管理认证状态监控，无需手动处理
4. **错误监控**: 建议监听 `error` 事件进行错误日志记录
5. **安全考虑**: 不要在客户端存储敏感的令牌信息