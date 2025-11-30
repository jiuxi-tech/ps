# API调用日志重构完成总结

## 重构目标

将 `response_status` 字段统一为**业务状态码**，不再区分 HTTP 状态码和业务状态码。

## 状态码定义

| 状态码 | 含义 | 场景示例 |
|--------|------|----------|
| 1 | 业务成功 | 查询用户成功返回数据 |
| -1 | 业务失败 | 用户不存在、参数错误等 |
| 401 | 认证失败 | API Key无效、不存在 |
| 403 | 权限不足 | 应用禁用、应用过期、IP不在白名单、无权访问API |
| 500 | 服务器错误 | 系统异常 |

## 已完成的代码修改

### 1. Entity 层 ✅
- `TpApiCallLog.java` - 移除 businessCode 字段
- `TpApiCallLogVO.java` - 移除 businessCode 字段
- `TpApiCallLogMapper.xml` - 移除 BUSINESS_CODE 字段

### 2. Service 层 ✅
- `TpApiCallLogService.java` - 方法签名改为6个参数
- `TpApiCallLogServiceImpl.java` - 使用 businessStatus 替代 responseStatus 和 businessCode

方法签名：
```java
void logApiCall(String appId, String appName, HttpServletRequest request,
                Integer businessStatus, Integer responseTime, String errorMessage);
```

### 3. Interceptor 层 ✅
- `ApiKeyInterceptor.java` - 所有调用统一使用 businessStatus
- `ApiResponseBodyAdvice.java` - 提取业务状态码到 businessStatus attribute

### 4. 编译验证 ✅
- 无编译错误
- 所有方法调用参数正确

## 需要执行的数据库操作

执行脚本：`sql/update/20251130_api_log_refactor_v2.sql`

主要操作：
1. 删除 business_code 字段（如果之前添加了）
2. 更新 response_status 字段注释

```sql
-- 删除 business_code 字段的索引（如果存在）
DROP INDEX IF EXISTS `idx_business_code` ON `tp_api_call_log`;

-- 删除 business_code 字段（如果存在）
ALTER TABLE `tp_api_call_log` 
DROP COLUMN IF EXISTS `business_code`;

-- 更新 response_status 字段注释
ALTER TABLE `tp_api_call_log` 
MODIFY COLUMN `response_status` INTEGER DEFAULT NULL 
COMMENT '业务状态码（1:成功, -1:失败, 401:认证失败, 403:权限不足, 500:服务器错误）';
```

## 待测试场景

- [ ] API Key 无效（401）
- [ ] 应用禁用（403）
- [ ] 应用过期（403）
- [ ] IP 不在白名单（403）
- [ ] 无权访问 API（403）
- [ ] 业务成功（1）
- [ ] 业务失败（-1，如用户不存在）

## 关键设计点

1. **未知应用记录**：当 API Key 无效时
   - appId = "UNKNOWN"（固定值）
   - appName = "未知应用(apiKey)"（包含实际的apiKey值）
   - 这样设计的原因：
     - app_id 字段长度为 VARCHAR(19)，apiKey 长度为 32，无法直接存储
     - 使用固定值 "UNKNOWN" 方便查询统计
     - apiKey 信息保存在 appName 中，方便排查问题

2. **业务状态码提取**：通过 ResponseBodyAdvice 拦截响应
   - JsonResponse.code → businessStatus attribute

3. **所有场景都记录**：包括认证失败、权限不足等

## 下一步操作

1. ✅ 代码重构完成
2. ⚠️ 执行数据库更新脚本
3. ⏳ 启动应用测试
4. ⏳ 验证所有场景的日志记录
