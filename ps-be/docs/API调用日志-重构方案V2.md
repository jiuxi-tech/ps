# API调用日志记录功能重构方案 V2

## 一、设计调整

### 原设计问题
- 区分HTTP状态码和业务状态码过于复杂
- response_status存HTTP状态码，business_code存业务状态码，容易混淆

### 新设计方案
**将 `response_status` 字段统一为业务状态码**，含义如下：

| 状态码 | 含义 | 场景示例 |
|--------|------|----------|
| 1 | 业务成功 | 查询用户成功返回数据 |
| -1 | 业务失败 | 用户不存在、参数错误等 |
| 401 | 认证失败 | API Key无效、不存在 |
| 403 | 权限不足 | 应用禁用、应用过期、IP不在白名单、无权访问API |
| 500 | 服务器错误 | 系统异常 |

### HTTP状态码说明
- HTTP层面统一返回对应的状态码（200/401/403/500）
- 日志中只记录业务状态码，更直观

## 二、代码修改方案

### 1. 删除 business_code 字段（如果已添加）

```sql
-- 如果之前添加了 business_code 字段，执行以下SQL删除
ALTER TABLE `tp_api_call_log` DROP COLUMN `business_code`;
DROP INDEX `idx_business_code` ON `tp_api_call_log`;
```

### 2. Service 接口调整 ✅

**TpApiCallLogService.java**
```java
/**
 * 记录API调用日志
 *
 * @param appId 应用ID（如果apiKey不存在，传"UNKNOWN"）
 * @param appName 应用名称（如果apiKey不存在，传"未知应用(apiKey)"）
 * @param request HTTP请求
 * @param businessStatus 业务状态码（1:成功, -1:失败, 401/403/500等）
 * @param responseTime 响应时间（毫秒）
 * @param errorMessage 错误信息
 */
void logApiCall(String appId, String appName, HttpServletRequest request,
               Integer businessStatus, Integer responseTime, String errorMessage);
```

**注意**：
- 如果 API Key 不存在或无效，appId 使用 "UNKNOWN"（固定值）
- appName 使用"未知应用(apiKey)"，将 apiKey 包含在名称中方便排查
- 这样设计的原因：
  - app_id 字段长度为 VARCHAR(19)，无法存储 32 位的 apiKey
  - 使用固定值方便查询统计所有未知应用的调用

### 3. 各场景的业务状态码

#### 场景1: API Key无效
```java
logApiCallWithoutApp(apiKey, request, 401, "无效的API Key");
```
- businessStatus = 401

#### 场景2: 应用禁用
```java
logApiCallWithoutApp(apiKey, request, 403, "应用已被禁用！");
```
- businessStatus = 403

#### 场景3: 应用过期
```java
logApiCallWithoutApp(apiKey, request, 403, "应用已过期！");
```
- businessStatus = 403

#### 场景4: IP不在白名单
```java
logApiCall(app.getAppId(), app.getAppName(), request, 403, "IP地址不在白名单中");
```
- businessStatus = 403

#### 场景5: 无权访问API
```java
logApiCall(app.getAppId(), app.getAppName(), request, 403, "无权访问此API");
```
- businessStatus = 403

#### 场景6: 业务成功
```java
// 从 ResponseBodyAdvice 获取
businessStatus = 1
```

#### 场景7: 业务失败（用户不存在）
```java
// 从 ResponseBodyAdvice 获取
businessStatus = -1
```

## 三、实现步骤

### 步骤1: 回滚 Entity 类 ✅

移除 businessCode 字段，只保留 responseStatus

- ✅ TpApiCallLog.java
- ✅ TpApiCallLogVO.java
- ✅ TpApiCallLogMapper.xml

### 步骤2: 修改 Service 方法签名 ✅

将参数从 `responseStatus, businessCode` 改为只有 `businessStatus`

- ✅ TpApiCallLogService.java
- ✅ TpApiCallLogServiceImpl.java

### 步骤3: 修改 ResponseBodyAdvice ✅

直接存储业务状态码到 "businessStatus" attribute

- ✅ ApiResponseBodyAdvice.java
- ✅ 常量名从 BUSINESS_CODE_ATTRIBUTE 改为 BUSINESS_STATUS_ATTRIBUTE
- ✅ 变量名从 businessCode 改为 businessStatus

### 步骤4: 修改 ApiKeyInterceptor ✅

所有调用改为传递业务状态码

- ✅ 使用 BUSINESS_STATUS_ATTRIBUTE 常量
- ✅ logApiCall 方法调用改为6个参数
- ✅ logApiCallWithoutApp 方法调用改为6个参数
- ✅ afterCompletion 方法中获取 businessStatus

### 步骤5: 数据库调整 ⚠️

执行 SQL 脚本：`sql/update/20251130_api_log_refactor_v2.sql`

- ⚠️ 删除 business_code 字段（如果之前添加了）
- ⚠️ 更新 response_status 字段注释

### 步骤6: 测试验证 ⏳

需要测试所有场景：

- [ ] API Key 无效（401）
- [ ] 应用禁用（403）
- [ ] 应用过期（403）
- [ ] IP 不在白名单（403）
- [ ] 无权访问 API（403）
- [ ] 业务成功（1）
- [ ] 业务失败（-1）

## 四、优势

1. **简化理解** - 只有一个状态码字段，含义清晰
2. **易于查询** - 直接按业务状态码统计成功率
3. **统一规范** - 所有场景使用同一套状态码体系
4. **未知应用记录** - API Key 无效时也能记录日志，appName 显示为"未知应用"

## 五、已完成的工作

### 代码重构完成情况

**后端代码** - 100% 完成

1. ✅ Entity 层：移除 businessCode 字段
2. ✅ Service 层：方法签名调整，参数简化
3. ✅ Interceptor 层：统一使用 businessStatus
4. ✅ ResponseBodyAdvice：提取业务状态码
5. ✅ 编译验证：无编译错误

**数据库脚本** - 已创建

1. ✅ 创建 V2 方案 SQL 脚本
2. ⚠️ 需要执行数据库更新

**下一步工作**

1. 执行数据库更新脚本
2. 启动应用进行测试
3. 验证所有场景的日志记录
