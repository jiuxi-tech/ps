# PS-BMP 管理端 UI API 文档

## 概述

本文档描述了 PS-BMP 管理端 UI 项目中的 API 接口规范、服务配置和使用方法。项目采用多服务架构，支持主业务服务、日志服务和报表服务。

## 服务配置

### 服务端点配置

项目在 `project.config.js` 中配置了多个服务端点：

```javascript
service: [
  // 主业务服务
  {
    name: 'service',
    target: process.env.VUE_APP_URL,
    baseURL: '/chemicalpark-manage-app',
    method: 'post',
    timeout: 10000,
    withCredentials: true
  },
  // 日志服务
  {
    name: 'logService',
    target: process.env.VUE_APP_LOG_URL,
    baseURL: '/log-center',
    method: 'post',
    timeout: 10000,
    withCredentials: true
  },
  // 报表服务
  {
    name: 'thirdService',
    target: process.env.VUE_APP_REPORT_URL,
    baseURL: '/report',
    method: 'get',
    timeout: 10000,
    withCredentials: true
  }
]
```

### 环境配置

#### 开发环境
```bash
VUE_APP_URL=http://127.0.0.1:8088/
VUE_REPORT_URL=http://127.0.0.1:8077/
```

#### 生产环境
```bash
VUE_APP_URL=http://192.168.90.77:8080/
```

## API 调用方式

### 全局服务调用

项目通过 `Application` 框架自动创建全局服务实例，可通过以下方式调用：

```javascript
// 在 Vue 组件中调用
this.$svc.service.post('/api/endpoint', data)
this.$svc.logService.post('/log/query', params)
this.$svc.thirdService.get('/report/export', { params })

// 在 JavaScript 模块中调用
window.app.$svc.service.post('/api/endpoint', data)
```

### 请求配置

所有服务请求都包含以下默认配置：
- **超时时间**: 10秒
- **跨域支持**: withCredentials: true
- **默认请求方式**: POST（主服务和日志服务），GET（报表服务）

## 主业务服务 API

### 基础路径
`/chemicalpark-manage-app`

### 认证相关

#### 用户登录
```javascript
// POST /chemicalpark-manage-app/auth/login
this.$svc.service.post('/auth/login', {
  username: 'admin',
  password: 'encrypted_password',
  loginType: 'password' // password, sms, qrcode
})
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": 1,
      "username": "admin",
      "nickname": "管理员",
      "avatar": "/avatar/admin.jpg",
      "roles": ["admin"],
      "permissions": ["user:create", "user:update"]
    },
    "expires": 7200
  }
}
```

#### 获取用户信息
```javascript
// POST /chemicalpark-manage-app/auth/userinfo
this.$svc.service.post('/auth/userinfo')
```

#### 用户登出
```javascript
// POST /chemicalpark-manage-app/auth/logout
this.$svc.service.post('/auth/logout')
```

#### 刷新Token
```javascript
// POST /chemicalpark-manage-app/auth/refresh
this.$svc.service.post('/auth/refresh', {
  refreshToken: 'refresh_token_here'
})
```

### 用户管理

#### 获取用户列表
```javascript
// POST /chemicalpark-manage-app/user/list
this.$svc.service.post('/user/list', {
  page: 1,
  size: 10,
  keyword: '搜索关键词',
  status: 1, // 1:启用 0:禁用
  roleId: 2
})
```

**响应示例**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "total": 100,
    "page": 1,
    "size": 10,
    "records": [
      {
        "id": 1,
        "username": "admin",
        "nickname": "管理员",
        "email": "admin@example.com",
        "phone": "13800138000",
        "status": 1,
        "createTime": "2024-01-01 10:00:00",
        "roles": ["admin"]
      }
    ]
  }
}
```

#### 创建用户
```javascript
// POST /chemicalpark-manage-app/user/create
this.$svc.service.post('/user/create', {
  username: 'newuser',
  password: 'encrypted_password',
  nickname: '新用户',
  email: 'newuser@example.com',
  phone: '13800138001',
  roleIds: [2, 3],
  status: 1
})
```

#### 更新用户
```javascript
// POST /chemicalpark-manage-app/user/update
this.$svc.service.post('/user/update', {
  id: 1,
  nickname: '更新的昵称',
  email: 'updated@example.com',
  phone: '13800138002',
  roleIds: [2],
  status: 1
})
```

#### 删除用户
```javascript
// POST /chemicalpark-manage-app/user/delete
this.$svc.service.post('/user/delete', {
  ids: [1, 2, 3]
})
```

### 角色管理

#### 获取角色列表
```javascript
// POST /chemicalpark-manage-app/role/list
this.$svc.service.post('/role/list', {
  page: 1,
  size: 10,
  keyword: '角色名称'
})
```

#### 获取角色权限
```javascript
// POST /chemicalpark-manage-app/role/permissions
this.$svc.service.post('/role/permissions', {
  roleId: 1
})
```

#### 分配角色权限
```javascript
// POST /chemicalpark-manage-app/role/assign-permissions
this.$svc.service.post('/role/assign-permissions', {
  roleId: 1,
  permissionIds: [1, 2, 3, 4]
})
```

### 菜单管理

#### 获取菜单树
```javascript
// POST /chemicalpark-manage-app/menu/tree
this.$svc.service.post('/menu/tree')
```

**响应示例**:
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "系统管理",
      "path": "/system",
      "icon": "el-icon-setting",
      "sort": 1,
      "children": [
        {
          "id": 2,
          "name": "用户管理",
          "path": "/system/user",
          "icon": "el-icon-user",
          "sort": 1,
          "children": []
        }
      ]
    }
  ]
}
```

### 系统配置

#### 获取系统配置
```javascript
// POST /chemicalpark-manage-app/config/get
this.$svc.service.post('/config/get', {
  keys: ['system.title', 'system.logo']
})
```

#### 更新系统配置
```javascript
// POST /chemicalpark-manage-app/config/update
this.$svc.service.post('/config/update', {
  'system.title': 'PS-BMP管理系统',
  'system.logo': '/logo.png'
})
```

## 日志服务 API

### 基础路径
`/log-center`

### 日志查询

#### 查询操作日志
```javascript
// POST /log-center/operation/query
this.$svc.logService.post('/operation/query', {
  page: 1,
  size: 20,
  startTime: '2024-01-01 00:00:00',
  endTime: '2024-01-31 23:59:59',
  userId: 1,
  module: 'user',
  operation: 'create'
})
```

**响应示例**:
```json
{
  "code": 200,
  "data": {
    "total": 50,
    "records": [
      {
        "id": 1,
        "userId": 1,
        "username": "admin",
        "module": "user",
        "operation": "create",
        "description": "创建用户",
        "ip": "192.168.1.100",
        "userAgent": "Mozilla/5.0...",
        "createTime": "2024-01-01 10:00:00",
        "params": "{\"username\":\"newuser\"}",
        "result": "success"
      }
    ]
  }
}
```

#### 查询系统日志
```javascript
// POST /log-center/system/query
this.$svc.logService.post('/system/query', {
  page: 1,
  size: 20,
  level: 'ERROR', // DEBUG, INFO, WARN, ERROR
  startTime: '2024-01-01 00:00:00',
  endTime: '2024-01-31 23:59:59',
  keyword: '关键词'
})
```

#### 查询登录日志
```javascript
// POST /log-center/login/query
this.$svc.logService.post('/login/query', {
  page: 1,
  size: 20,
  userId: 1,
  loginType: 'password',
  status: 'success', // success, failed
  startTime: '2024-01-01 00:00:00',
  endTime: '2024-01-31 23:59:59'
})
```

### 日志统计

#### 获取日志统计
```javascript
// POST /log-center/statistics/overview
this.$svc.logService.post('/statistics/overview', {
  startTime: '2024-01-01',
  endTime: '2024-01-31'
})
```

## 报表服务 API

### 基础路径
`/report`

### 报表管理

#### 获取报表列表
```javascript
// GET /report/list
this.$svc.thirdService.get('/list', {
  params: {
    page: 1,
    size: 10,
    category: 'business'
  }
})
```

#### 生成报表
```javascript
// GET /report/generate
this.$svc.thirdService.get('/generate', {
  params: {
    reportId: 1,
    format: 'pdf', // pdf, excel, word
    startTime: '2024-01-01',
    endTime: '2024-01-31'
  }
})
```

#### 导出报表
```javascript
// GET /report/export
this.$svc.thirdService.get('/export', {
  params: {
    reportId: 1,
    format: 'excel'
  },
  responseType: 'blob'
}).then(response => {
  // 处理文件下载
  const blob = new Blob([response.data])
  const url = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = 'report.xlsx'
  link.click()
})
```

## 通用响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // 具体数据
  },
  "timestamp": 1640995200000
}
```

### 错误响应
```json
{
  "code": 400,
  "message": "参数错误",
  "data": null,
  "timestamp": 1640995200000,
  "errors": [
    {
      "field": "username",
      "message": "用户名不能为空"
    }
  ]
}
```

### 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 参数错误 |
| 401 | 未授权，需要登录 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 请求拦截器

项目在 `@fb/admin-base/util/serviceConfig.js` 中配置了请求和响应拦截器：

### 请求拦截
```javascript
// 自动添加认证头
config.headers.Authorization = `Bearer ${token}`

// 自动添加时间戳
config.params = {
  ...config.params,
  _t: Date.now()
}
```

### 响应拦截
```javascript
// 统一错误处理
if (response.data.code !== 200) {
  // 显示错误消息
  this.$message.error(response.data.message)
  
  // 处理特殊错误码
  if (response.data.code === 401) {
    // 跳转到登录页
    this.$router.push('/login')
  }
}
```

## Mock 数据

项目支持 Mock 数据，配置在服务配置中：

```javascript
service: [
  {
    name: 'service',
    target: process.env.VUE_APP_URL,
    baseURL: '/chemicalpark-manage-app',
    mock: 'http://192.168.0.46:3000/api/open/plugin/export-full?type=json&pid=11&status=all&token=xxx'
  }
]
```

### Mock 数据示例

在 `mock/local/` 目录下创建 Mock 数据文件：

```javascript
// mock/local/user.js
module.exports = {
  'POST /chemicalpark-manage-app/user/list': {
    code: 200,
    message: '查询成功',
    data: {
      total: 2,
      records: [
        {
          id: 1,
          username: 'admin',
          nickname: '管理员',
          email: 'admin@example.com',
          status: 1
        },
        {
          id: 2,
          username: 'user',
          nickname: '普通用户',
          email: 'user@example.com',
          status: 1
        }
      ]
    }
  }
}
```

## 错误处理

### 网络错误处理
```javascript
this.$svc.service.post('/api/endpoint', data)
  .then(response => {
    // 处理成功响应
  })
  .catch(error => {
    if (error.response) {
      // 服务器响应错误
      console.error('Response Error:', error.response.data)
    } else if (error.request) {
      // 网络错误
      console.error('Network Error:', error.request)
    } else {
      // 其他错误
      console.error('Error:', error.message)
    }
  })
```

### 全局错误处理
```javascript
// 在 main.js 中配置全局错误处理
Vue.config.errorHandler = (err, vm, info) => {
  console.error('Vue Error:', err)
  // 发送错误到日志服务
  window.app.$svc.logService.post('/error/report', {
    error: err.message,
    stack: err.stack,
    component: vm.$options.name,
    info: info
  })
}
```

## 安全配置

### Token 配置
```javascript
// project.config.js
token: {
  type: 'common', // jwt, common
  time: 1000,
  path: '/getToken'
}
```

### 密码加密
```javascript
// 系统配置
sysconfig: {
  passwordEncryption: true,
  publicKey: '04be3415fd3a7231fa23e4cfdf6f857b0f3137e75692f7b5011d459afc0cdd7741676dca32ca6489cfe0f0fd43b5e9f9f0f77c7997630ba1142c725178a9181558'
}
```

### HTTPS 配置
```javascript
// 开发服务器 HTTPS 配置
server: {
  host: '0.0.0.0',
  port: 10801,
  https: true // 启用 HTTPS
}
```

## 性能优化

### 请求缓存
```javascript
// 使用请求缓存
this.$svc.service.post('/api/endpoint', data, {
  cache: true,
  cacheTime: 300000 // 5分钟缓存
})
```

### 请求去重
```javascript
// 自动去重相同请求
this.$svc.service.post('/api/endpoint', data, {
  cancelDuplicate: true
})
```

### 分页优化
```javascript
// 使用虚拟滚动处理大量数据
this.$svc.service.post('/user/list', {
  page: 1,
  size: 50, // 适当增加页面大小
  virtual: true // 启用虚拟滚动
})
```

## 调试工具

### 开启调试模式
```javascript
// 在开发环境开启详细日志
if (process.env.NODE_ENV === 'development') {
  // 开启请求日志
  axios.interceptors.request.use(config => {
    console.log('Request:', config)
    return config
  })
  
  // 开启响应日志
  axios.interceptors.response.use(response => {
    console.log('Response:', response)
    return response
  })
}
```

### API 测试工具

推荐使用以下工具进行 API 测试：
- **Postman**: 接口测试和文档生成
- **Insomnia**: 轻量级 REST 客户端
- **curl**: 命令行工具
- **浏览器开发者工具**: 网络面板调试

## 版本控制

### API 版本管理
```javascript
// 在请求头中指定 API 版本
config.headers['API-Version'] = 'v1'
```

### 向后兼容
```javascript
// 处理不同版本的响应格式
if (response.data.version === 'v2') {
  // 处理 v2 格式
} else {
  // 处理 v1 格式
}
```

---

**更新时间**: 2024年12月
**文档版本**: v1.0.0

> 本文档将随着项目的发展持续更新，请关注最新版本。