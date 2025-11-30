# API调用日志记录功能重构说明

## 一、重构目标

1. **分离HTTP状态码和业务状态码** - HTTP 200 但业务失败（code=-1）的情况也要正确记录业务状态
2. **记录未知应用** - API Key不存在或无效的请求，显示为"未知应用"
3. **简化日志记录接口** - 统一日志记录方法签名，减少参数获取复杂度

## 二、数据库变更

### 1. 执行SQL脚本

```sql
-- 文件: sql/update/20251130_add_business_code.sql
ALTER TABLE `tp_api_call_log` 
ADD COLUMN `business_code` INTEGER DEFAULT NULL COMMENT '业务状态码（1:成功, -1:失败, NULL:未返回）' AFTER `response_status`;

CREATE INDEX `idx_business_code` ON `tp_api_call_log`(`business_code`);
```

### 2. 字段说明

- `response_status`: HTTP状态码（200/401/403/500等）
- `business_code`: 业务状态码
  - `1`: 业务处理成功
  - `-1`: 业务处理失败（如用户不存在、参数错误）
  - `NULL`: 请求未到达业务层（如认证失败、权限不足）

## 三、代码变更

### 1. 实体类 (TpApiCallLog.java)

✅ 已更新 - 添加 `businessCode` 字段

### 2. VO类 (TpApiCallLogVO.java)

✅ 已更新 - 添加 `businessCode` 字段

### 3. Mapper XML (TpApiCallLogMapper.xml)

✅ 已更新 - SELECT 和 INSERT 语句中添加 `BUSINESS_CODE` 字段

### 4. Service接口 (TpApiCallLogService.java)

✅ 已更新 - 修改方法签名：
```java
void logApiCall(String appId, String appName, HttpServletRequest request, 
                Integer responseStatus, Integer businessCode,
                Integer responseTime, String errorMessage);
```

### 5. Service实现 (TpApiCallLogServiceImpl.java)

✅ 已更新 - 实现新的方法签名，直接接收 `appName` 而不是从 request 中获取

### 6. ResponseBodyAdvice (ApiResponseBodyAdvice.java)

✅ 已更新 - 添加 `BUSINESS_CODE_ATTRIBUTE`，同时存储业务状态码和错误信息

### 7. 拦截器 (ApiKeyInterceptor.java)

需要修改以下部分：

#### 7.1 添加常量

```java
private static final String APP_NAME_ATTRIBUTE = "appName";
```

#### 7.2 修改 preHandle 方法

在第 110-111 行附近，存储应用信息时：

```java
// 5. 将应用信息存入request，供后续使用
request.setAttribute(APP_ID_ATTRIBUTE, app.getAppId());
request.setAttribute(APP_NAME_ATTRIBUTE, app.getAppName());  // 新增
request.setAttribute(APP_INFO_ATTRIBUTE, app);
```

#### 7.3 修改 after Completion 方法

在第 130-150 行附近：

```java
@Override
public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    try {
        String appId = (String) request.getAttribute(APP_ID_ATTRIBUTE);
        String appName = (String) request.getAttribute(APP_NAME_ATTRIBUTE);
        
        LOGGER.info("afterCompletion 被调用，appId={}, appName={}, URI={}", appId, appName, request.getRequestURI());
        
        if (StrUtil.isNotBlank(appId)) {
            Long startTime = (Long) request.getAttribute(REQUEST_START_TIME);
            Integer responseTime = startTime != null ? (int) (System.currentTimeMillis() - startTime) : null;
            Integer responseStatus = response.getStatus();
            
            // 获取业务状态码
            Integer businessCode = (Integer) request.getAttribute(BUSINESS_CODE_ATTRIBUTE);
            
            // 优先使用业务错误信息，其次使用异常信息
            String errorMessage = (String) request.getAttribute(BUSINESS_ERROR_ATTRIBUTE);
            if (StrUtil.isBlank(errorMessage) && ex != null) {
                errorMessage = ex.getMessage();
            }
            
            LOGGER.info("准备记录日志：responseStatus={}, businessCode={}, errorMessage={}", 
                responseStatus, businessCode, errorMessage);
            
            // 调用新的日志记录方法
            tpApiCallLogService.logApiCall(appId, appName, request, responseStatus, businessCode, responseTime, errorMessage);
        } else {
            LOGGER.warn("afterCompletion 中 appId 为空，无法记录日志，URI={}", request.getRequestURI());
        }
    } catch (Exception e) {
        LOGGER.error("记录API调用日志失败", e);
    }
}
```

#### 7.4 修改 logApiCall 方法

在第 200-210 行附近：

```java
private void logApiCall(String appId, String appName, HttpServletRequest request, Integer responseStatus, 
                       String errorMessage) {
    try {
        Long startTime = (Long) request.getAttribute(REQUEST_START_TIME);
        Integer responseTime = startTime != null ? (int) (System.currentTimeMillis() - startTime) : null;
        
        // 业务状态码在这里为NULL（因为是拦截器内部记录，未到达业务层）
        tpApiCallLogService.logApiCall(appId, appName, request, responseStatus, null, responseTime, errorMessage);
    } catch (Exception e) {
        LOGGER.error("记录API调用日志失败", e);
    }
}
```

#### 7.5 修改 logApiCallWithoutApp 方法

在第 215-230 行附近：

```java
private void logApiCallWithoutApp(String apiKey, HttpServletRequest request, Integer responseStatus, 
                                  String errorMessage) {
    try {
        LOGGER.info("开始记录无应用信息API调用日志，apiKey={}, URI={}, responseStatus={}, errorMessage={}",
            apiKey, request.getRequestURI(), responseStatus, errorMessage);
        
        Long startTime = (Long) request.getAttribute(REQUEST_START_TIME);
        Integer responseTime = startTime != null ? (int) (System.currentTimeMillis() - startTime) : null;
        
        // 使用apiKey作为appId，"未知应用"作为appName
        // 业务状态码为NULL（未到达业务层）
        tpApiCallLogService.logApiCall(apiKey, "未知应用", request, responseStatus, null, responseTime, errorMessage);
        
        LOGGER.info("无应用信息API调用日志记录完成");
    } catch (Exception e) {
        LOGGER.error("记录API调用日志失败，apiKey={}, error={}", apiKey, e.getMessage(), e);
    }
}
```

## 四、日志记录场景说明

### 场景1: API Key无效
- `app_id`: API Key值
- `app_name`: "未知应用"
- `response_status`: 401
- `business_code`: NULL
- `error_message`: "无效的API Key"

### 场景2: 应用禁用/过期
- `app_id`: API Key值或真实应用ID
- `app_name`: 真实应用名称或"未知应用"
- `response_status`: 403
- `business_code`: NULL
- `error_message`: "应用已被禁用！"或"应用已过期！"

### 场景3: 业务成功
- `app_id`: 真实应用ID
- `app_name`: 真实应用名称
- `response_status`: 200
- `business_code`: 1
- `error_message`: NULL

### 场景4: 业务失败（如用户不存在）
- `app_id`: 真实应用ID
- `app_name`: 真实应用名称
- `response_status`: 200
- `business_code`: -1
- `error_message`: "用户不存在"

## 五、测试验证

### 1. 执行数据库脚本
```bash
mysql -u root -p < sql/update/20251130_add_business_code.sql
```

### 2. 重启应用

### 3. 测试场景

#### 测试1: 无效API Key
```bash
curl -X GET "http://localhost:10801/ps-be/open-api/v1/users" \
  -H "Authorization: invalid_key"
```
期望日志：`app_name='未知应用'`, `business_code=NULL`

#### 测试2: 用户不存在
```bash
curl -X GET "http://localhost:10801/ps-be/open-api/v1/users/99999" \
  -H "Authorization: b4a4b6a002b64502b2cc715a6ddec8cc"
```
期望日志：`app_name='给停车应用使用'`, `response_status=200`, `business_code=-1`, `error_message='用户不存在'`

#### 测试3: 查询成功
```bash
curl -X GET "http://localhost:10801/ps-be/open-api/v1/users" \
  -H "Authorization: b4a4b6a002b64502b2cc715a6ddec8cc"
```
期望日志：`app_name='给停车应用使用'`, `response_status=200`, `business_code=1`, `error_message=NULL`

## 六、注意事项

1. **数据库字段兼容性** - `business_code` 允许NULL，不影响现有数据
2. **性能影响** - 新增字段和索引对性能影响极小
3. **日志查询** - 可以根据 `business_code` 字段统计业务成功率
4. **前端展示** - 列表和详情页面需要增加业务状态码显示

## 七、后续优化建议

1. 前端列表增加业务状态筛选
2. 统计报表中分别统计HTTP成功率和业务成功率
3. 考虑异步记录日志，提升性能
