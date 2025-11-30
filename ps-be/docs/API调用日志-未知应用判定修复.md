# API调用日志 - 未知应用判定修复

## 问题描述

之前的实现中，当应用过期或禁用时，日志记录显示为"未知应用"，这是不正确的。

### 错误场景
```
应用名称: 未知应用(e6da9972646247fcbace4eb9eee424f5)
响应状态: 403
错误信息: 应用已过期！
```

这个 API Key 对应的应用是存在的，只是过期了，不应该显示为"未知应用"。

## 正确的判定逻辑

| 场景 | appId | appName | 说明 |
|------|-------|---------|------|
| API Key 不存在 | "UNKNOWN" | "未知应用(apiKey)" | 数据库中查不到该 API Key |
| 应用存在但禁用 | 真实的appId | 真实的appName | 应用状态 status != 1 |
| 应用存在但过期 | 真实的appId | 真实的appName | expireTime < now |

## 修复方案

### 1. 添加新方法

在 `TpThirdPartyAppService` 中添加 `getByApiKey` 方法，用于查询应用信息但不验证状态：

```java
/**
 * 根据API Key查询应用信息（不验证状态）
 *
 * @param apiKey API密钥
 * @return 应用信息，如果不存在返回null
 */
TpThirdPartyAppVO getByApiKey(String apiKey);
```

### 2. 修改拦截器逻辑

在 `ApiKeyInterceptor.preHandle()` 中，当 `validateApiKey` 抛出异常时：

```java
} catch (TopinfoRuntimeException e) {
    // validateApiKey 内部验证失败
    sendErrorResponse(response, e.getErrcode(), e.getMessage());
    
    // 尝试查询应用信息，如果能查到说明是应用存在但状态不正常（禁用/过期）
    TpThirdPartyAppVO appInfo = tpThirdPartyAppService.getByApiKey(apiKey);
    if (appInfo != null) {
        // 应用存在，记录真实的应用信息
        logApiCall(appInfo.getAppId(), appInfo.getAppName(), request, e.getErrcode(), e.getMessage());
    } else {
        // 应用不存在，记录为未知应用
        logApiCallWithoutApp(apiKey, request, e.getErrcode(), e.getMessage());
    }
    return false;
}
```

### 3. 实现说明

**getByApiKey 实现**：
- 只查询数据库，不做任何状态验证
- 如果找到应用，返回 VO 对象
- 如果找不到，返回 null
- 不抛出异常

**判定流程**：
1. `validateApiKey` 抛出异常（403 应用过期/禁用，或 401 无效）
2. 调用 `getByApiKey` 查询应用信息
3. 如果查到应用 → 使用真实应用信息记录日志
4. 如果查不到应用 → 使用"未知应用"记录日志

## 修复后的效果

### 应用过期场景
```
应用名称: 测试应用
响应状态: 403
错误信息: 应用已过期！
```

### 应用禁用场景
```
应用名称: 测试应用
响应状态: 403
错误信息: 应用已被禁用！
```

### API Key 不存在场景
```
应用名称: 未知应用(invalidApiKey123456789012)
响应状态: 401
错误信息: 无效的API Key！
```

## 相关文件

1. **Service 接口**
   - `TpThirdPartyAppService.java` - 添加 getByApiKey 方法定义

2. **Service 实现**
   - `TpThirdPartyAppServiceImpl.java` - 实现 getByApiKey 方法

3. **拦截器**
   - `ApiKeyInterceptor.java` - 修改异常处理逻辑

## 测试验证

需要测试以下场景：

- [x] API Key 不存在 → 显示"未知应用"
- [ ] 应用存在但已禁用 → 显示真实应用名称
- [ ] 应用存在但已过期 → 显示真实应用名称
- [ ] 应用正常但 IP 不在白名单 → 显示真实应用名称
- [ ] 应用正常但无 API 权限 → 显示真实应用名称
