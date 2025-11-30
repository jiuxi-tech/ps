# API接口管理功能 - 后端接口测试文档

## 1. 第三方应用管理接口

### 1.1 查询应用列表（分页）

**请求**
```
GET /sys/third-party-app/list?page=1&size=20
```

**响应**
```json
{
  "success": true,
  "data": {
    "records": [
      {
        "appId": "1234567890",
        "appName": "外部系统A",
        "apiKey": "abc12345***",
        "status": 1,
        "expireTime": "20251231235959",
        "createTime": "20250130120000"
      }
    ],
    "total": 1,
    "current": 1,
    "size": 20
  }
}
```

### 1.2 新增应用

**请求**
```
POST /sys/third-party-app/add
Content-Type: application/json

{
  "appName": "测试应用",
  "description": "测试应用描述",
  "contactPerson": "张三",
  "contactPhone": "13800138000",
  "contactEmail": "test@example.com",
  "status": 1,
  "ipWhitelist": "192.168.1.1,192.168.1.2",
  "rateLimit": 100
}
```

**响应**
```json
{
  "success": true,
  "data": {
    "appId": "1234567890",
    "apiKey": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "apiSecret": "secret_xxxxxxxxxxxxxxxx",
    "message": "应用创建成功，请妥善保管API密钥，密钥仅显示一次"
  }
}
```

### 1.3 查看应用详情

**请求**
```
GET /sys/third-party-app/view?appId=1234567890
```

### 1.4 修改应用

**请求**
```
POST /sys/third-party-app/update
Content-Type: application/json

{
  "appId": "1234567890",
  "appName": "修改后的应用名称",
  "description": "修改后的描述",
  "status": 1
}
```

### 1.5 删除应用

**请求**
```
POST /sys/third-party-app/delete?appId=1234567890
```

### 1.6 重置密钥

**请求**
```
POST /sys/third-party-app/regenerate-secret?appId=1234567890
```

**响应**
```json
{
  "success": true,
  "data": "new_secret_xxxxxxxxxxxxxxxx"
}
```

### 1.7 配置应用API权限

**请求**
```
POST /sys/third-party-app/config-permissions
Content-Type: application/json

{
  "appId": "1234567890",
  "apiIds": ["1", "2", "3"]
}
```

### 1.8 查询应用已授权API

**请求**
```
GET /sys/third-party-app/app-apis?appId=1234567890
```

**响应**
```json
{
  "success": true,
  "data": [
    {
      "apiId": "1",
      "apiCode": "api_user_query",
      "apiName": "查询用户信息",
      "apiPath": "/open-api/v1/users/{personId}",
      "httpMethod": "GET",
      "category": "用户管理"
    }
  ]
}
```

### 1.9 查询所有可用API清单

**请求**
```
GET /sys/third-party-app/available-apis
```

---

## 2. 开放API接口（需要API Key验证）

### 2.1 查询单个用户信息

**请求**
```
GET /open-api/v1/users/{personId}
Authorization: Bearer {apiKey}
```

**响应**
```json
{
  "success": true,
  "data": {
    "personId": "1234567890",
    "personName": "张*",
    "personNo": "P001***",
    "sex": 1,
    "sexName": "男",
    "phone": "138****5678",
    "email": "z***n@example.com",
    "office": "工程师",
    "deptId": "dept001",
    "deptName": "技术部",
    "actived": 1,
    "createTime": "20200101120000"
  }
}
```

### 2.2 查询用户列表

**请求**
```
GET /open-api/v1/users?deptId=dept001&page=1&size=20
Authorization: Bearer {apiKey}
```

**响应**
```json
{
  "success": true,
  "data": {
    "total": 100,
    "page": 1,
    "size": 20,
    "records": [
      {
        "personId": "1234567890",
        "personName": "张*",
        "phone": "138****5678",
        "deptName": "技术部"
      }
    ]
  }
}
```

### 2.3 搜索用户

**请求**
```
POST /open-api/v1/users/search
Authorization: Bearer {apiKey}
Content-Type: application/json

{
  "keyword": "张",
  "deptId": "dept001",
  "page": 1,
  "size": 20
}
```

---

## 3. API调用日志查询接口

### 3.1 查询调用日志（分页）

**请求**
```
GET /sys/api-call-log/list?appId=1234567890&page=1&size=20
```

**响应**
```json
{
  "success": true,
  "data": {
    "records": [
      {
        "logId": "log001",
        "appName": "测试应用",
        "apiPath": "/open-api/v1/users/123",
        "httpMethod": "GET",
        "requestIp": "192.168.1.100",
        "responseStatus": 200,
        "responseTime": 150,
        "callTime": "20250130120000"
      }
    ],
    "total": 1000,
    "current": 1,
    "size": 20
  }
}
```

### 3.2 查询调用统计

**请求**
```
GET /sys/api-call-log/statistics?appId=1234567890&startTime=20250101000000&endTime=20250131235959
```

**响应**
```json
{
  "success": true,
  "data": {
    "totalCalls": 10000,
    "successCalls": 9800,
    "failedCalls": 200,
    "avgResponseTime": 120,
    "successRate": 98.0
  }
}
```

---

## 4. 错误码说明

| 错误码 | HTTP状态 | 说明 |
|-------|---------|------|
| 401 | Unauthorized | API Key无效或过期 |
| 403 | Forbidden | 无权限访问该API |
| 404 | Not Found | 资源不存在 |
| 429 | Too Many Requests | 请求过于频繁，触发限流 |
| 500 | Internal Server Error | 服务器内部错误 |

---

## 5. 测试步骤

### 步骤1: 创建第三方应用

```bash
curl -X POST http://localhost:8082/sys/third-party-app/add \
  -H "Content-Type: application/json" \
  -d '{
    "appName": "测试应用",
    "status": 1,
    "rateLimit": 100
  }'
```

记录返回的`apiKey`。

### 步骤2: 配置API权限

```bash
curl -X POST http://localhost:8082/sys/third-party-app/config-permissions \
  -H "Content-Type: application/json" \
  -d '{
    "appId": "{刚创建的appId}",
    "apiIds": ["1", "2", "3"]
  }'
```

### 步骤3: 调用开放API

```bash
curl -X GET http://localhost:8082/open-api/v1/users/{personId} \
  -H "Authorization: Bearer {apiKey}"
```

### 步骤4: 查看调用日志

```bash
curl -X GET http://localhost:8082/sys/api-call-log/list?appId={appId}
```

---

## 6. 注意事项

1. **API Key安全**: 创建应用后，API Key和Secret仅显示一次，请妥善保管
2. **IP白名单**: 生产环境建议配置IP白名单，限制访问来源
3. **限流配置**: 默认限流为100次/秒，可根据实际需求调整
4. **数据脱敏**: 开放API返回的用户信息均已脱敏处理
5. **日志保留**: 调用日志默认保留90天

---

## 7. 数据库初始化

在使用前，需要执行以下SQL脚本：

1. `ps-be/sql/third_party_api_management.sql` - 创建4张数据表
2. `ps-be/sql/api_definition_init_data.sql` - 初始化API清单数据
