# API接口管理功能 - 快速开始指南

## 📋 5分钟快速上手

### 第一步: 执行数据库脚本 (1分钟)

```bash
# 进入项目目录
cd ps-be/sql

# 方式1: 使用命令行
mysql -uroot -p你的密码 你的数据库 < third_party_api_management.sql
mysql -uroot -p你的密码 你的数据库 < init_api_definition_data.sql

# 方式2: 使用Navicat等工具
# 1. 打开Navicat，连接数据库
# 2. 新建查询，复制third_party_api_management.sql内容并执行
# 3. 新建查询，复制init_api_definition_data.sql内容并执行
```

**验证**: 执行以下SQL，应该能看到5条API定义记录
```sql
SELECT * FROM tp_api_definition WHERE actived = 1;
```

### 第二步: 启动后端服务 (1分钟)

```bash
cd ps-be
mvn spring-boot:run

# 或者在IDE中直接运行主类
```

**验证**: 看到日志输出 `Started Application in xxx seconds`

### 第三步: 启动前端服务 (1分钟)

```bash
cd ps-fe
npm run dev

# 或
npm run serve
```

**验证**: 浏览器访问 http://localhost:端口号

### 第四步: 创建第一个测试应用 (1分钟)

1. 登录系统后台
2. 导航到菜单: **系统管理 > API管理 > 第三方应用**
3. 点击 **"新增"** 按钮
4. 填写表单:
   ```
   应用名称: 测试应用
   应用编码: test_app
   状态: 启用 ✓
   描述: 这是一个测试应用
   ```
5. 点击 **"保存"**
6. ⚠️ **重要**: 弹窗会显示API Key和Secret，**务必复制保存**（仅此一次）

   示例:
   ```
   API Key: a1b2c3d4-e5f6-7890-abcd-ef1234567890
   API Secret: $2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
   ```

### 第五步: 配置API权限 (1分钟)

1. 在应用列表中，找到刚创建的"测试应用"
2. 点击 **"权限配置"** 按钮
3. 勾选以下API:
   ```
   用户管理
   ☑ 查询用户信息
   ☑ 用户列表查询
   ☑ 搜索用户
   ```
4. 点击 **"保存"**

### 第六步: 测试API调用 (1分钟)

#### 方式1: 使用Postman

创建新请求:
```
方法: GET
URL: http://localhost:8080/open-api/v1/users
Headers:
  X-API-Key: a1b2c3d4-e5f6-7890-abcd-ef1234567890  (你的API Key)
  
参数:
  page: 1
  size: 10
```

点击 **Send**

#### 方式2: 使用curl

```bash
# 查询用户列表
curl -X GET "http://localhost:8080/open-api/v1/users?page=1&size=10" \
  -H "X-API-Key: a1b2c3d4-e5f6-7890-abcd-ef1234567890"

# 查询单个用户（需替换实际的人员ID）
curl -X GET "http://localhost:8080/open-api/v1/users/1234567890123456789" \
  -H "X-API-Key: a1b2c3d4-e5f6-7890-abcd-ef1234567890"

# 搜索用户
curl -X POST "http://localhost:8080/open-api/v1/users/search" \
  -H "X-API-Key: a1b2c3d4-e5f6-7890-abcd-ef1234567890" \
  -H "Content-Type: application/json" \
  -d '{"keyword":"张","page":1,"size":10}'
```

#### 预期响应示例

```json
{
  "success": true,
  "code": 0,
  "message": "操作成功",
  "data": {
    "total": 100,
    "page": 1,
    "size": 10,
    "records": [
      {
        "personId": "1234567890123456789",
        "personName": "张*",
        "personNo": "P001***",
        "sex": 1,
        "sexName": "男",
        "phone": "138****5678",
        "email": "z***@example.com",
        "deptId": "dept_001",
        "deptName": "技术部",
        "office": "工程师",
        "actived": 1,
        "createTime": "20200101120000"
      }
    ]
  }
}
```

**注意数据脱敏**: 
- ✅ 姓名: 张三 → 张*
- ✅ 手机: 13812345678 → 138****5678
- ✅ 邮箱: zhangsan@example.com → z***n@example.com
- ✅ 工号: P001234 → P001***

---

## 🎯 功能清单

### 管理后台功能

| 功能模块 | 路径 | 说明 |
|---------|------|------|
| 应用列表 | /sys/third-party-app/list | 查看所有第三方应用 |
| 新增应用 | /sys/third-party-app/add | 创建新应用并生成API Key |
| 编辑应用 | /sys/third-party-app/edit/:id | 修改应用信息 |
| 查看详情 | /sys/third-party-app/view/:id | 查看应用详细信息 |
| 权限配置 | /sys/third-party-app/permission/:id | 勾选授权的API |
| 查看密钥 | /sys/third-party-app/secret/:id | 查看API Key和Secret |
| 重置密钥 | - | 重新生成API Key和Secret |
| 删除应用 | - | 逻辑删除应用 |
| 日志查询 | /sys/api-call-log/list | 查看API调用日志 |

### 开放API接口

| API | 路径 | 方法 | 说明 |
|-----|------|------|------|
| 查询用户信息 | /open-api/v1/users/{personId} | GET | 查询单个用户 |
| 用户列表 | /open-api/v1/users | GET | 分页查询用户列表 |
| 搜索用户 | /open-api/v1/users/search | POST | 关键词搜索用户 |

---

## 🔍 常见场景

### 场景1: 为外部系统A提供用户查询接口

1. 创建应用: "外部系统A"，编码: "external_system_a"
2. 获取并保存API Key
3. 配置权限: 勾选"查询用户信息"、"用户列表查询"
4. 提供给外部系统对接人员: API Key + 接口文档
5. 外部系统在请求时携带 `X-API-Key` Header

### 场景2: IP白名单限制

编辑应用时，在"IP白名单"字段填写:
```
192.168.1.100,192.168.1.101,192.168.1.102
```

只有这些IP能访问，其他IP会返回403错误。

### 场景3: 设置过期时间

编辑应用时，选择"过期时间": 2025-12-31 23:59:59

过期后API Key自动失效，需要续期或重置密钥。

### 场景4: 查看调用日志

1. 进入菜单: 系统管理 > API管理 > API日志
2. 筛选条件:
   - 应用名称: 外部系统A
   - 时间范围: 2025-01-01 ~ 2025-01-31
3. 查看调用详情: 请求参数、响应状态、响应时间等

---

## ⚠️ 注意事项

### 安全相关

1. **API Secret仅显示一次**: 创建或重置应用时，Secret只显示一次，务必保存
2. **不要在前端暴露API Key**: API Key应该在后端服务中使用
3. **定期更换密钥**: 建议每6个月重置一次API Secret
4. **配置IP白名单**: 生产环境务必配置IP白名单
5. **最小权限原则**: 只授予应用必需的API权限

### 性能相关

1. **合理分页**: 查询列表时，size不要超过100
2. **避免频繁调用**: 建议添加客户端缓存
3. **日志清理**: 定期清理90天前的调用日志

### 开发相关

1. **测试环境**: 先在测试环境验证，再部署生产
2. **错误处理**: 客户端应正确处理401、403、429等错误码
3. **超时设置**: 建议设置30秒超时时间

---

## 🆘 遇到问题？

### 返回401错误

**原因**: API Key无效
**解决**: 
- 检查请求头是否包含 `X-API-Key`
- 确认API Key拼写正确（复制粘贴，避免手打）
- 检查应用状态是否为"启用"

### 返回403错误

**原因1**: IP不在白名单
**解决**: 编辑应用，添加客户端IP到白名单

**原因2**: 无权限访问此API
**解决**: 进入权限配置，勾选对应API

### 数据未脱敏

**原因**: 调用的不是开放API接口
**解决**: 确认URL路径是 `/open-api/v1/...`

### 日志未记录

**原因**: 拦截器未生效或异步配置问题
**解决**: 
1. 检查请求路径是否匹配 `/open-api/**`
2. 查看应用日志中的错误信息
3. 确认数据库中tp_api_call_log表存在

---

## 📚 更多文档

- **设计文档**: `.qoder/quests/api-interface-management.md`
- **部署指南**: `ps-be/docs/API接口管理-部署指南.md`
- **第三方接入文档**: `ps-be/docs/第三方应用接入文档.md`
- **功能测试说明**: `ps-be/docs/功能测试说明.md`

---

## ✅ 完成检查清单

- [ ] 数据库表创建成功（4张表）
- [ ] API清单数据初始化成功（5条记录）
- [ ] 后端服务启动成功
- [ ] 前端服务启动成功
- [ ] 创建测试应用成功
- [ ] 获取并保存API Key
- [ ] 配置API权限成功
- [ ] API调用测试成功
- [ ] 返回数据已脱敏
- [ ] 调用日志已记录

**恭喜！API接口管理功能已成功部署！** 🎉
