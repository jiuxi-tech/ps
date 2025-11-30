# API接口管理功能 - 部署指南

## 1. 数据库部署

### 1.1 执行建表SQL

在数据库中依次执行以下SQL脚本:

```bash
# 1. 创建4张表
mysql -u用户名 -p密码 数据库名 < ps-be/sql/third_party_api_management.sql

# 2. 初始化API清单数据
mysql -u用户名 -p密码 数据库名 < ps-be/sql/init_api_definition_data.sql
```

### 1.2 验证表结构

```sql
-- 查看表是否创建成功
SHOW TABLES LIKE 'tp_%party%';

-- 预期结果应该包含:
-- tp_third_party_app
-- tp_api_definition
-- tp_app_api_permission
-- tp_api_call_log

-- 查看API清单数据
SELECT api_code, api_name, api_path, http_method, category 
FROM tp_api_definition 
WHERE actived = 1 
ORDER BY category, order_index;
```

## 2. 应用配置

### 2.1 拦截器注册

拦截器已在 `WebMvcConfig.java` 中自动注册，拦截路径: `/open-api/**`

验证配置类:
- 文件路径: `com.jiuxi.shared.config.web.config.WebMvcConfig`
- 拦截器: `ApiKeyInterceptor`

### 2.2 应用配置文件

在 `application.yml` 中确认配置（可选）:

```yaml
# 第三方API配置（可选，使用默认值）
open-api:
  enabled: true  # 是否启用
  rate-limit:
    enabled: false  # 暂不启用限流（可后期开启）
    global-qps: 10000
```

## 3. 前端部署

### 3.1 路由配置

已在路由配置中添加，路径: `ps-fe/@fb/admin-base/router/index.js`

菜单路径:
- 应用列表: `/sys/third-party-app/list`
- 新增应用: `/sys/third-party-app/add`
- 编辑应用: `/sys/third-party-app/edit/:id`
- 查看详情: `/sys/third-party-app/view/:id`
- 配置权限: `/sys/third-party-app/permission/:id`
- 查看密钥: `/sys/third-party-app/secret/:id`

### 3.2 菜单配置

需要在系统菜单管理中添加以下菜单项:

| 菜单名称 | 菜单路径 | 父级菜单 | 图标 | 排序 |
|---------|---------|---------|------|------|
| API管理 | - | 系统管理 | api | 90 |
| 第三方应用 | /sys/third-party-app/list | API管理 | application | 1 |
| API日志 | /sys/api-call-log/list | API管理 | file-text | 2 |

## 4. 功能验证

### 4.1 启动应用

```bash
# 后端启动
cd ps-be
mvn spring-boot:run

# 前端启动
cd ps-fe
npm run dev
```

### 4.2 创建测试应用

1. 登录系统后台
2. 进入菜单: 系统管理 > API管理 > 第三方应用
3. 点击"新增应用"按钮
4. 填写应用信息:
   - 应用名称: 测试应用
   - 应用编码: test_app
   - 状态: 启用
   - 其他字段: 根据需要填写
5. 点击"保存"
6. **重要**: 保存成功后会显示API Key和Secret，务必复制保存（仅显示一次）

### 4.3 配置API权限

1. 在应用列表中，点击"权限配置"按钮
2. 勾选需要授权的API，如:
   - ☑ 查询用户信息
   - ☑ 用户列表查询
3. 点击"保存"

### 4.4 测试API调用

使用PostMan或curl测试:

```bash
# 1. 查询单个用户信息
curl -X GET "http://localhost:8080/open-api/v1/users/{人员ID}" \
  -H "X-API-Key: {你的API_Key}"

# 2. 查询用户列表
curl -X GET "http://localhost:8080/open-api/v1/users?page=1&size=20" \
  -H "X-API-Key: {你的API_Key}"

# 3. 搜索用户
curl -X POST "http://localhost:8080/open-api/v1/users/search" \
  -H "X-API-Key: {你的API_Key}" \
  -H "Content-Type: application/json" \
  -d '{"keyword":"张","page":1,"size":20}'
```

预期响应:

```json
{
  "success": true,
  "code": 0,
  "message": "操作成功",
  "data": {
    "personId": "xxx",
    "personName": "张*",
    "phone": "138****5678",
    "email": "z***@example.com",
    "deptName": "技术部",
    "sex": 1,
    "sexName": "男"
  }
}
```

## 5. 常见问题排查

### 5.1 401 Unauthorized

**问题**: 请求返回401错误
**原因**: API Key无效或缺失
**解决**: 
1. 检查请求头是否包含 `X-API-Key`
2. 确认API Key是否正确
3. 检查应用是否已启用（status=1, actived=1）

### 5.2 403 Forbidden

**问题**: 请求返回403错误
**原因**: 
1. IP不在白名单中
2. 应用无权访问此API

**解决**:
1. 检查IP白名单配置
2. 进入权限配置页面，勾选对应的API权限

### 5.3 返回数据未脱敏

**问题**: 返回的手机号、邮箱等未脱敏
**原因**: 数据脱敏工具类未生效
**解决**: 检查 `DataMaskUtil` 是否正确调用

### 5.4 日志未记录

**问题**: API调用日志表中无数据
**原因**: 
1. 拦截器未生效
2. 异步方法配置问题

**解决**:
1. 检查拦截器是否注册: `WebMvcConfig.addInterceptors()`
2. 检查 `@Async` 注解是否生效
3. 查看应用日志中是否有错误信息

## 6. 性能优化建议

### 6.1 数据库优化

```sql
-- 定期清理历史日志（保留90天）
DELETE FROM tp_api_call_log 
WHERE call_time < DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 90 DAY), '%Y%m%d%H%i%s');

-- 或使用定时任务
-- 每天凌晨2点执行清理
```

### 6.2 缓存优化

对于不常变化的数据，建议添加缓存:
- API定义数据（tp_api_definition）
- 应用信息（tp_third_party_app）
- 权限关联（tp_app_api_permission）

### 6.3 限流配置

生产环境建议启用限流:

```yaml
open-api:
  rate-limit:
    enabled: true
    global-qps: 10000  # 全局QPS
    ip-qps: 50  # 单IP QPS
```

## 7. 安全加固

### 7.1 生产环境检查清单

- [ ] 所有应用均配置IP白名单
- [ ] 敏感API已标记（is_sensitive=1）
- [ ] 定期更换API Secret
- [ ] 启用HTTPS
- [ ] 启用请求限流
- [ ] 配置日志告警
- [ ] 禁用不必要的API

### 7.2 监控告警

建议监控以下指标:
- API调用失败率（401/403错误）
- API响应时间（P95/P99）
- 异常调用频率
- 日志表增长速度

## 8. 后续扩展

### 8.1 新增API

1. 在 `tp_api_definition` 表中插入新API定义
2. 实现对应的Controller接口
3. 在前端权限配置页面会自动显示新API

### 8.2 新增应用

直接在前端管理页面操作即可，无需修改代码。

## 9. 备份恢复

### 9.1 数据备份

```bash
# 备份4张表的数据
mysqldump -u用户名 -p密码 数据库名 \
  tp_third_party_app \
  tp_api_definition \
  tp_app_api_permission \
  tp_api_call_log \
  > api_management_backup_$(date +%Y%m%d).sql
```

### 9.2 数据恢复

```bash
mysql -u用户名 -p密码 数据库名 < api_management_backup_20250130.sql
```

## 10. 联系支持

如遇到问题，请查阅以下文档:
- 设计文档: `.qoder/quests/api-interface-management.md`
- 第三方接入文档: `ps-be/docs/第三方应用接入文档.md`
- 功能测试说明: `ps-be/docs/功能测试说明.md`
