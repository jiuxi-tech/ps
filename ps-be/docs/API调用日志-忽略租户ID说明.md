# API调用日志功能 - 忽略租户ID说明

## 修改日期
2025-11-30

## 修改目的
API调用日志(`tp_api_call_log`)作为全局审计日志，需要记录所有租户的API调用情况，因此在增删改查操作中忽略 `TENANT_ID` 参数，实现跨租户查询。

## 修改内容

### 1. Mapper XML 修改
**文件**: `src/main/resources/mapper/admin/TpApiCallLogMapper.xml`

#### 修改点：
- ✅ 移除 SQL 查询中的 `TENANT_ID` 字段
- ✅ 移除分页查询中的租户ID过滤条件
- ✅ 移除插入语句中的 `TENANT_ID` 字段

**修改前**：
```xml
<select id="getPage">
    SELECT ... TENANT_ID FROM tp_api_call_log
    <where>
        ...
        <if test="query.tenantId != null and query.tenantId != ''">
            AND TENANT_ID = #{query.tenantId}
        </if>
    </where>
</select>
```

**修改后**：
```xml
<select id="getPage">
    SELECT ... FROM tp_api_call_log
    <where>
        ...
        <!-- 不再过滤 TENANT_ID -->
    </where>
</select>
```

### 2. Service 实现类修改
**文件**: `src/main/java/com/jiuxi/admin/core/service/impl/TpApiCallLogServiceImpl.java`

#### 修改点：
- ✅ 注释掉 `queryPage` 方法中的 `query.setTenantId(tenantId)` 代码
- ✅ 移除 `logApiCall` 方法中设置租户ID的代码
- ✅ 更新方法注释，说明忽略租户ID

**修改前**：
```java
public IPage<TpApiCallLogVO> queryPage(TpApiCallLogQuery query, String tenantId) {
    query.setTenantId(tenantId);  // 设置租户ID过滤
    // ...
}
```

**修改后**：
```java
public IPage<TpApiCallLogVO> queryPage(TpApiCallLogQuery query, String tenantId) {
    // 忽略租户ID，不设置过滤条件
    // query.setTenantId(tenantId);
    // ...
}
```

### 3. Controller 修改
**文件**: `src/main/java/com/jiuxi/admin/core/controller/pc/TpApiCallLogController.java`

#### 修改点：
- ✅ 更新方法注释，说明忽略租户ID
- ✅ 添加代码注释，说明 jwtpid 仅用于 buildPassKey

**修改后**：
```java
/**
 * API调用日志列表（分页查询）
 * 注：忽略租户ID，查询所有日志
 */
@RequestMapping("/list")
public JsonResponse list(TpApiCallLogQuery query, String jwtpid) {
    // jwtpid 仅用于 buildPassKey，不作为查询条件
    IPage<TpApiCallLogVO> page = tpApiCallLogService.queryPage(query, jwtpid);
    return JsonResponse.buildSuccess(page).buildPassKey(jwtpid, PASS_KEY);
}
```

## 功能说明

### 查询功能
- **列表查询**: 不再按租户ID过滤，返回所有租户的API调用日志
- **详情查询**: 不限制租户，任何有权限的用户都可以查看日志详情

### 插入功能
- **日志记录**: 不再设置 `TENANT_ID` 字段
- **数据库字段**: `TENANT_ID` 字段保留为 NULL

### 数据库表结构
表 `tp_api_call_log` 中的 `TENANT_ID` 字段依然保留，但在应用层面不再使用：
- 保留字段：便于后续如需启用租户隔离
- NULL 值：新记录的 TENANT_ID 为 NULL
- 不影响查询：查询时不过滤此字段

## 影响范围

### 1. 数据可见性
- ✅ **前端列表**: 显示所有租户的API调用日志
- ✅ **前端详情**: 可查看任何租户的日志详情
- ✅ **开放API**: 不受租户限制

### 2. 权限控制
- API调用日志功能仍需要登录认证（`@Authorization`）
- PassKey验证机制保持不变
- 只是移除了租户级别的数据隔离

### 3. 数据统计
- 统计数据包含所有租户
- 便于全局监控和分析

## 优点

1. **全局审计**: 管理员可以查看所有租户的API调用情况
2. **便于监控**: 统一监控所有第三方应用的API调用
3. **问题排查**: 跨租户问题更容易发现和定位
4. **数据分析**: 便于进行全局的API调用统计和分析

## 注意事项

1. ⚠️ **权限管理**: 确保只有系统管理员或有相应权限的用户才能访问API调用日志
2. ⚠️ **数据安全**: 日志中可能包含敏感信息，注意访问控制
3. ⚠️ **性能考虑**: 跨租户查询数据量可能较大，建议：
   - 设置合理的分页大小
   - 必须指定时间范围查询
   - 考虑数据归档策略

## 后续优化建议

1. **分页优化**: 限制单次查询的最大数据量
2. **时间范围**: 强制要求指定时间范围（如最近7天、30天）
3. **数据归档**: 定期归档历史日志数据
4. **权限细化**: 可以考虑根据角色控制可查看的日志范围

## 回滚方案

如需恢复租户隔离功能，按以下步骤操作：

1. 取消 `TpApiCallLogServiceImpl.java` 中 `query.setTenantId(tenantId)` 的注释
2. 恢复 `TpApiCallLogMapper.xml` 中的 TENANT_ID 过滤条件
3. 恢复 `logApiCall` 方法中设置租户ID的代码
4. 重新编译部署

## 测试建议

1. ✅ 测试列表查询是否返回所有租户的数据
2. ✅ 测试详情查看是否不受租户限制
3. ✅ 测试新插入的日志 TENANT_ID 是否为 NULL
4. ✅ 验证权限控制是否仍然有效（需要登录）
5. ✅ 测试分页查询性能

## 编译和部署

修改完成后执行以下步骤：

```bash
# 1. 编译项目
cd d:\projects\ps\ps-be
mvn clean compile -DskipTests

# 2. 打包
mvn package -DskipTests

# 3. 重启应用
# 停止旧进程，启动新进程
```

## 验证步骤

1. 登录系统
2. 访问 API调用日志列表页面
3. 验证是否显示所有租户的日志
4. 查看日志详情，确认可以访问
5. 检查新产生的日志记录，确认 TENANT_ID 为 NULL
