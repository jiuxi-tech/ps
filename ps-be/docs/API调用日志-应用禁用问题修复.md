# API调用日志 - 应用禁用时日志记录问题修复

## 修复日期
2025-11-30

## 问题描述

**现象**:  
当使用已禁用应用的 API Key 调用接口时：
```bash
http://localhost:10801/ps-be/open-api/v1/users/19727458741780480001
Authorization: 32bfc578bed3410cbb5d9c489e60d75a

返回: {"code":403,"success":false,"message":"应用已被禁用！"}
```

但数据库表 `tp_api_call_log` 中没有记录这次失败的调用。

## 问题原因

### 代码执行流程分析

1. **ApiKeyInterceptor.preHandle()** 第66行调用 `validateApiKey(apiKey)`
2. **TpThirdPartyAppServiceImpl.validateApiKey()** 第374-376行检查应用状态
   ```java
   if (app.getStatus() == null || app.getStatus() != 1) {
       throw new TopinfoRuntimeException(403, "应用已被禁用！");
   }
   ```
3. **异常被抛出**，在 ApiKeyInterceptor 第109行被 catch
4. **问题所在**: catch 块中**没有记录日志**，直接返回错误响应

### 代码缺陷

```java
// 原代码 - 第109行
} catch (TopinfoRuntimeException e) {
    sendErrorResponse(response, e.getErrcode(), e.getMessage());
    return false;  // 直接返回，没有记录日志！
}
```

## 解决方案

### 修改策略

将 `validateApiKey` 的调用包装在 try-catch 块中，在异常时立即记录日志。

### 修改代码

**文件**: `src/main/java/com/jiuxi/admin/core/interceptor/ApiKeyInterceptor.java`

**修改前**（第65-72行）:
```java
// 2. 验证API Key
TpThirdPartyAppVO app = tpThirdPartyAppService.validateApiKey(apiKey);
if (app == null) {
    sendErrorResponse(response, 401, "无效的API Key");
    logApiCallWithoutApp(apiKey, request, 401, "无效的API Key");
    return false;
}
```

**修改后**（第65-82行）:
```java
// 2. 验证API Key
TpThirdPartyAppVO app = null;
try {
    app = tpThirdPartyAppService.validateApiKey(apiKey);
} catch (TopinfoRuntimeException e) {
    // validateApiKey 内部验证失败，记录日志
    sendErrorResponse(response, e.getErrcode(), e.getMessage());
    logApiCallWithoutApp(apiKey, request, e.getErrcode(), e.getMessage());
    return false;
}

if (app == null) {
    sendErrorResponse(response, 401, "无效的API Key");
    // 记录失败日志（使用apiKey作为appId）
    logApiCallWithoutApp(apiKey, request, 401, "无效的API Key");
    return false;
}
```

### 修改说明

1. **添加 try-catch**：捕获 `validateApiKey` 抛出的业务异常
2. **记录日志**：使用 `logApiCallWithoutApp()` 记录失败日志
3. **使用 apiKey 作为 appId**：因为应用被禁用，无法获取真实的 appId

## validateApiKey 抛出的异常情况

`TpThirdPartyAppServiceImpl.validateApiKey()` 会在以下情况抛出异常：

| 情况 | 状态码 | 错误消息 | 代码行 |
|------|--------|---------|--------|
| API Key 为空 | 401 | "API Key不能为空！" | 365 |
| API Key 无效 | 401 | "无效的API Key！" | 370 |
| **应用已禁用** | 403 | "应用已被禁用！" | 375 |
| **应用已过期** | 403 | "应用已过期！" | 382 |
| 其他异常 | 401 | "验证API Key失败！" | 393 |

现在所有这些情况都会被正确记录到日志中。

## 日志记录情况对比

### 修复前

| 失败类型 | 状态码 | 是否记录 | appId |
|---------|--------|---------|-------|
| API Key 无效 | 401 | ✅ 已有 | apiKey |
| **应用已禁用** | 403 | ❌ **缺失** | - |
| **应用已过期** | 403 | ❌ **缺失** | - |
| IP 不在白名单 | 403 | ✅ 已有 | 真实 appId |
| 无权访问 API | 403 | ✅ 已有 | 真实 appId |

### 修复后

| 失败类型 | 状态码 | 是否记录 | appId |
|---------|--------|---------|-------|
| API Key 无效 | 401 | ✅ | apiKey |
| **应用已禁用** | 403 | ✅ **修复** | apiKey |
| **应用已过期** | 403 | ✅ **修复** | apiKey |
| IP 不在白名单 | 403 | ✅ | 真实 appId |
| 无权访问 API | 403 | ✅ | 真实 appId |

## 测试验证

### 测试场景1: 应用已禁用

#### 准备工作
```sql
-- 禁用一个应用
UPDATE tp_third_party_app 
SET status = 0 
WHERE api_key = '32bfc578bed3410cbb5d9c489e60d75a';
```

#### 执行请求
```bash
curl --location 'http://localhost:10801/ps-be/open-api/v1/users/19727458741780480001' \
--header 'Authorization: 32bfc578bed3410cbb5d9c489e60d75a'
```

#### 期望响应
```json
{
  "code": 403,
  "success": false,
  "message": "应用已被禁用！"
}
```

#### 验证日志
```sql
SELECT 
    log_id,
    app_id,
    app_name,
    api_path,
    http_method,
    response_status,
    error_message,
    call_time
FROM tp_api_call_log
WHERE error_message = '应用已被禁用！'
ORDER BY call_time DESC
LIMIT 5;
```

**期望结果**:
- 应该有一条新记录
- `app_id` = '32bfc578bed3410cbb5d9c489e60d75a' (apiKey)
- `response_status` = 403
- `error_message` = '应用已被禁用！'
- `api_path` = '/ps-be/open-api/v1/users/19727458741780480001'
- `http_method` = 'GET'

---

### 测试场景2: 应用已过期

#### 准备工作
```sql
-- 设置应用过期（设置为昨天）
UPDATE tp_third_party_app 
SET expire_time = '20251129000000',
    status = 1
WHERE api_key = '32bfc578bed3410cbb5d9c489e60d75a';
```

#### 执行请求
```bash
curl --location 'http://localhost:10801/ps-be/open-api/v1/users/19727458741780480001' \
--header 'Authorization: 32bfc578bed3410cbb5d9c489e60d75a'
```

#### 期望响应
```json
{
  "code": 403,
  "success": false,
  "message": "应用已过期！"
}
```

#### 验证日志
```sql
SELECT 
    log_id,
    app_id,
    error_message,
    response_status,
    call_time
FROM tp_api_call_log
WHERE error_message = '应用已过期！'
ORDER BY call_time DESC
LIMIT 5;
```

**期望结果**:
- 应该有一条新记录
- `error_message` = '应用已过期！'
- `response_status` = 403

---

### 测试场景3: 正常应用（对比测试）

#### 准备工作
```sql
-- 恢复应用为正常状态
UPDATE tp_third_party_app 
SET status = 1,
    expire_time = NULL  -- NULL 表示永不过期
WHERE api_key = '32bfc578bed3410cbb5d9c489e60d75a';
```

#### 执行请求
```bash
curl --location 'http://localhost:10801/ps-be/open-api/v1/users/19727458741780480001' \
--header 'Authorization: 32bfc578bed3410cbb5d9c489e60d75a'
```

#### 期望结果
- 返回 200 成功或业务数据
- 日志正常记录，`response_status` = 200
- `error_message` = NULL

## 统计分析

### 查询禁用应用的调用统计
```sql
SELECT 
    DATE_FORMAT(STR_TO_DATE(call_time, '%Y%m%d%H%i%s'), '%Y-%m-%d %H:00:00') as hour,
    COUNT(*) as call_count
FROM tp_api_call_log
WHERE error_message = '应用已被禁用！'
  AND call_time >= DATE_FORMAT(NOW() - INTERVAL 7 DAY, '%Y%m%d000000')
GROUP BY hour
ORDER BY hour DESC;
```

### 查询各类失败原因统计
```sql
SELECT 
    CASE 
        WHEN response_status = 401 THEN '认证失败'
        WHEN response_status = 403 AND error_message LIKE '%禁用%' THEN '应用禁用'
        WHEN response_status = 403 AND error_message LIKE '%过期%' THEN '应用过期'
        WHEN response_status = 403 AND error_message LIKE '%IP%' THEN 'IP限制'
        WHEN response_status = 403 AND error_message LIKE '%无权%' THEN '权限不足'
        ELSE '其他错误'
    END as error_type,
    COUNT(*) as count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM tp_api_call_log WHERE response_status >= 400), 2) as percentage
FROM tp_api_call_log
WHERE response_status >= 400
  AND call_time >= DATE_FORMAT(NOW() - INTERVAL 7 DAY, '%Y%m%d000000')
GROUP BY error_type
ORDER BY count DESC;
```

## 相关修改文件

1. **ApiKeyInterceptor.java** - 主要修改
   - 第65-82行：添加 try-catch 捕获 validateApiKey 异常
   - 第119-123行：简化外层异常处理

## 编译和部署

```bash
# 1. 编译项目
cd d:\projects\ps\ps-be
mvn clean compile -DskipTests

# 2. 打包
mvn package -DskipTests

# 3. 重启应用
# 停止旧进程，启动新进程
```

## 验证清单

- [ ] 应用禁用时日志被正确记录
- [ ] 应用过期时日志被正确记录  
- [ ] API Key 无效时日志被正确记录（已有功能，确认不受影响）
- [ ] IP 白名单检查失败时日志正常（已有功能，确认不受影响）
- [ ] 权限检查失败时日志正常（已有功能，确认不受影响）
- [ ] 成功调用时日志正常（已有功能，确认不受影响）
- [ ] 日志中的 app_id 字段正确（应用禁用/过期时为 apiKey）
- [ ] 日志中的 error_message 正确
- [ ] 日志中的 response_status 正确（403）

## 后续优化建议

1. **区分 appId 类型**：在数据库中可以添加一个字段标识 `app_id` 是真实ID还是apiKey
   ```sql
   ALTER TABLE tp_api_call_log 
   ADD COLUMN app_id_type VARCHAR(20) DEFAULT 'real' 
   COMMENT 'app_id类型：real-真实ID, apikey-API Key';
   ```

2. **优化日志查询**：对于使用 apiKey 作为 appId 的记录，可以通过关联查询获取真实应用信息
   ```sql
   SELECT 
       l.*,
       a.app_id as real_app_id,
       a.app_name
   FROM tp_api_call_log l
   LEFT JOIN tp_third_party_app a ON l.app_id = a.api_key
   WHERE l.response_status = 403;
   ```

3. **添加监控告警**：当应用被禁用但仍有调用请求时，发送告警通知管理员

## 注意事项

⚠️ **appId 字段含义变化**

在以下情况下，`tp_api_call_log.app_id` 字段存储的是 **apiKey** 而不是真实的 appId：
- API Key 无效
- 应用已禁用
- 应用已过期

这是因为在验证失败时，无法获取到真实的 appId，但仍需要记录是哪个 apiKey 发起的请求。

如需获取真实的 appId，可以通过以下方式：
```sql
SELECT 
    l.log_id,
    l.app_id as stored_app_id,
    a.app_id as real_app_id,
    a.app_name,
    l.error_message
FROM tp_api_call_log l
LEFT JOIN tp_third_party_app a 
    ON (l.app_id = a.app_id OR l.app_id = a.api_key)
WHERE l.response_status = 403
  AND l.error_message IN ('应用已被禁用！', '应用已过期！');
```
