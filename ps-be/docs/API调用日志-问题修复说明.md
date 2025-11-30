# API调用日志功能 - 问题修复说明

## 修复日期
2025-11-30

## 修复的问题

### 问题1: 请求参数字段为空

**问题描述**:  
`request_params` 字段在日志中为空，无法查看API调用的请求参数。

**原因分析**:  
- 原代码只获取了 URL 查询参数（`queryString`），只能记录 GET 请求的参数
- 对于 POST/PUT/PATCH 请求，请求体参数没有被获取

**解决方案**:  
在 `TpApiCallLogServiceImpl.java` 中新增 `getRequestParams()` 方法，支持多种方式获取参数：

1. **GET 请求参数**: 从 `request.getQueryString()` 获取
2. **POST/PUT/PATCH 请求参数**: 
   - 优先从 `request.getAttribute("requestBody")` 获取（如果有预先存储）
   - 否则从 `request.getParameterMap()` 获取表单参数

**修改文件**:
- `src/main/java/com/jiuxi/admin/core/service/impl/TpApiCallLogServiceImpl.java`

**核心代码**:
```java
private String getRequestParams(HttpServletRequest request) {
    StringBuilder params = new StringBuilder();
    
    // 1. 获取URL参数（GET参数）
    String queryString = request.getQueryString();
    if (queryString != null && !queryString.isEmpty()) {
        params.append("?").append(queryString);
    }
    
    // 2. 获取POST请求体参数
    if ("POST".equalsIgnoreCase(request.getMethod()) || 
        "PUT".equalsIgnoreCase(request.getMethod()) ||
        "PATCH".equalsIgnoreCase(request.getMethod())) {
        // ... 从 request.getAttribute 或 parameterMap 获取
    }
    
    return params.length() > 0 ? params.toString() : null;
}
```

---

### 问题2: 调用失败时日志没有被记录

**问题描述**:  
当API调用返回 403 错误（如"应用已被禁用"）时，日志没有被记录到数据库。

**原因分析**:  
在 `ApiKeyInterceptor.java` 的 `preHandle` 方法中，当验证失败时（如第66-72行），代码直接返回 `false`，没有调用日志记录方法。

**影响范围**:
- API Key 无效（401错误）
- 应用已禁用（403错误）  
- IP不在白名单（403错误）
- 无权访问API（403错误）

这些失败情况都没有记录日志。

**解决方案**:  
1. 在所有验证失败的地方都调用 `logApiCall()` 记录日志
2. 新增 `logApiCallWithoutApp()` 方法，用于记录无法获取应用信息的情况（如 API Key 无效）

**修改文件**:
- `src/main/java/com/jiuxi/admin/core/interceptor/ApiKeyInterceptor.java`

**具体修改**:

#### 修改1: API Key 无效时记录日志
```java
// 2. 验证API Key
TpThirdPartyAppVO app = tpThirdPartyAppService.validateApiKey(apiKey);
if (app == null) {
    sendErrorResponse(response, 401, "无效的API Key");
    // 记录失败日志（使用apiKey作为appId）
    logApiCallWithoutApp(apiKey, request, 401, "无效的API Key");
    return false;
}
```

#### 修改2: 新增辅助方法
```java
/**
 * 记录API调用日志（无应用信息）
 */
private void logApiCallWithoutApp(String apiKey, HttpServletRequest request, 
                                  Integer responseStatus, String errorMessage) {
    try {
        Long startTime = (Long) request.getAttribute(REQUEST_START_TIME);
        Integer responseTime = startTime != null ? 
            (int) (System.currentTimeMillis() - startTime) : null;
        
        // 使用apiKey作为appId（无效key无法获取真实appId）
        tpApiCallLogService.logApiCall(apiKey, request, responseStatus, 
                                       null, responseTime, errorMessage);
    } catch (Exception e) {
        LOGGER.error("记录API调用日志失败", e);
    }
}
```

**已有的日志记录点**:
- ✅ IP 不在白名单（第78-80行） - 已有日志记录
- ✅ 无权访问API（第92-95行） - 已有日志记录  
- ✅ 成功调用（`afterCompletion` 方法） - 已有日志记录

**新增的日志记录点**:
- ✅ API Key 无效（第67-71行） - **新增**

---

## 修改总结

### 修改的文件

1. **TpApiCallLogServiceImpl.java**
   - ✅ 修改 `logApiCall()` 方法，调用新增的 `getRequestParams()` 方法
   - ✅ 新增 `getRequestParams()` 方法，支持GET和POST参数获取
   - ✅ 参数长度限制保持500字符

2. **ApiKeyInterceptor.java**
   - ✅ 在 API Key 无效时记录日志
   - ✅ 新增 `logApiCallWithoutApp()` 方法

### 功能改进

#### 1. 请求参数记录更完整
- **GET 请求**: 记录 URL 查询参数
- **POST/PUT/PATCH 请求**: 记录表单参数或请求体
- **参数格式**: `?param1=value1&param2=value2` 或 `Params: param1=value1&param2=value2`
- **长度限制**: 超过500字符截断并添加 `...`

#### 2. 失败日志记录更完整
现在所有的API调用失败情况都会被记录：

| 失败类型 | 状态码 | 是否记录日志 | appId 来源 |
|---------|--------|------------|-----------|
| 缺少 API Key | 401 | ❌ (请求未进入拦截器) | - |
| API Key 无效 | 401 | ✅ **新增** | 使用 apiKey |
| 应用已禁用 | 403 | ✅ **新增** | 实际 appId |
| IP 不在白名单 | 403 | ✅ 已有 | 实际 appId |
| 无权访问 API | 403 | ✅ 已有 | 实际 appId |
| 调用成功 | 200 | ✅ 已有 | 实际 appId |
| 服务器错误 | 500 | ✅ 已有 | 实际 appId |

---

## 测试建议

### 测试场景1: 请求参数记录

#### GET 请求测试
```bash
curl --location 'http://localhost:10801/ps-be/open-api/v1/users?deptId=123&page=1&size=10' \
--header 'Authorization: valid-api-key'
```

**期望结果**: `request_params` 字段应包含 `?deptId=123&page=1&size=10`

#### POST 请求测试
```bash
curl --location 'http://localhost:10801/ps-be/open-api/v1/users/search' \
--header 'Authorization: valid-api-key' \
--header 'Content-Type: application/json' \
--data '{
  "keyword": "张三",
  "page": 1,
  "size": 20
}'
```

**期望结果**: `request_params` 字段应包含请求体参数信息

---

### 测试场景2: 失败日志记录

#### 测试1: API Key 无效
```bash
curl --location 'http://localhost:10801/ps-be/open-api/v1/users' \
--header 'Authorization: invalid-api-key'
```

**期望结果**:
- 返回 401 错误
- 数据库应记录一条日志，`app_id` 为 `invalid-api-key`，`error_message` 为 "无效的API Key"

#### 测试2: 应用已禁用
```bash
# 先在数据库中禁用一个应用（status=0）
curl --location 'http://localhost:10801/ps-be/open-api/v1/users' \
--header 'Authorization: disabled-app-api-key'
```

**期望结果**:
- 返回 403 错误，消息为 "应用已被禁用"
- 数据库应记录一条日志，`response_status` 为 403，`error_message` 为 "应用已被禁用"

#### 测试3: IP 不在白名单
```bash
# 在数据库中为应用设置 IP 白名单（不包含当前IP）
curl --location 'http://localhost:10801/ps-be/open-api/v1/users' \
--header 'Authorization: valid-api-key'
```

**期望结果**:
- 返回 403 错误
- 数据库应记录一条日志，`error_message` 为 "IP地址不在白名单中"

---

## 数据库验证

### 查询最近的失败日志
```sql
SELECT 
    log_id,
    app_id,
    app_name,
    api_path,
    http_method,
    request_ip,
    request_params,  -- 检查是否有值
    response_status,
    response_time,
    error_message,
    call_time
FROM tp_api_call_log
WHERE response_status >= 400  -- 失败的请求
ORDER BY call_time DESC
LIMIT 20;
```

### 检查请求参数是否记录
```sql
SELECT 
    log_id,
    api_path,
    http_method,
    request_params,
    call_time
FROM tp_api_call_log
WHERE request_params IS NOT NULL
ORDER BY call_time DESC
LIMIT 10;
```

### 统计各类错误的记录情况
```sql
SELECT 
    response_status,
    error_message,
    COUNT(*) as count
FROM tp_api_call_log
WHERE response_status >= 400
GROUP BY response_status, error_message
ORDER BY response_status, count DESC;
```

---

## 注意事项

### 1. 请求体读取限制
⚠️ **HTTP 请求体只能读取一次**

当前实现优先从 `request.getAttribute("requestBody")` 获取预先存储的请求体。如果需要在其他地方读取请求体，需要：

1. 使用 `HttpServletRequestWrapper` 缓存请求体
2. 在拦截器或过滤器中预先读取并存储到 attribute

**推荐方案**（生产环境）:
创建一个 `CachedBodyHttpServletRequest` 包装类：

```java
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
    private byte[] cachedBody;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedBodyServletInputStream(this.cachedBody);
    }
    
    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }
}
```

### 2. 日志存储大小
当前限制请求参数最大500字符，如需调整：

```java
// TpApiCallLogServiceImpl.java 第112行附近
if (requestParams != null && requestParams.length() > 500) {
    requestParams = requestParams.substring(0, 500) + "...";  // 可调整限制
}
```

### 3. 敏感信息脱敏
⚠️ 请求参数可能包含敏感信息（密码、token等），建议添加脱敏处理：

```java
private String maskSensitiveData(String params) {
    if (params == null) return null;
    
    // 脱敏密码
    params = params.replaceAll("(password=)[^&]*", "$1***");
    // 脱敏token
    params = params.replaceAll("(token=)[^&]*", "$1***");
    // 其他敏感字段...
    
    return params;
}
```

---

## 回滚方案

如果修改导致问题，可以回滚：

### 回滚步骤

1. 恢复 `TpApiCallLogServiceImpl.java`:
```java
// 将 getRequestParams() 改回原来的简单实现
String queryString = request.getQueryString();
if (queryString != null && queryString.length() > 500) {
    queryString = queryString.substring(0, 500) + "...";
}
log.setRequestParams(queryString);
```

2. 恢复 `ApiKeyInterceptor.java`:
```java
// 移除第70行的日志记录调用
// logApiCallWithoutApp(apiKey, request, 401, "无效的API Key");

// 删除 logApiCallWithoutApp() 方法
```

3. 重新编译部署

---

## 后续优化建议

1. ✅ **完成**: 支持 POST 请求参数获取
2. ✅ **完成**: 记录所有失败的API调用
3. 📋 **建议**: 实现请求体缓存机制（`HttpServletRequestWrapper`）
4. 📋 **建议**: 添加敏感信息脱敏处理
5. 📋 **建议**: 使用 `@Async` 异步记录日志，避免影响主请求性能
6. 📋 **建议**: 添加日志统计报表功能（成功率、响应时间分布等）

---

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

---

## 验证清单

- [ ] GET 请求的参数能正常记录
- [ ] POST 请求的参数能正常记录
- [ ] API Key 无效时日志被记录
- [ ] 应用禁用时日志被记录
- [ ] IP 白名单检查失败时日志被记录
- [ ] 成功调用时日志正常记录
- [ ] 请求参数超过500字符时正确截断
- [ ] 日志中的时间戳正确
- [ ] 日志中的响应时间计算正确
