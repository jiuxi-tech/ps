---
trigger: model_decision
description: 前端 Service 层编码规范
---

# 前端 Service 层编码规范

## Service 文件结构

### 文件位置
```
@fb/admin-base/service/sys/{moduleName}/index.js
```

### 基本结构

```javascript
export default {
  // 列表查询（分页）
  list(formData) {
    return app.service.get('/sys/module-name/list', {params: formData})
  },

  // 新增
  add(formData) {
    return app.service.request({
      url: '/sys/module-name/add',
      method: 'post',
      transformRequest: [
        function (data) {
          let ret = ''
          for (let it in data) {
            ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
          }
          return ret.substring(0, ret.lastIndexOf('&'))
        },
      ],
      data: formData,
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      responseType: 'json',
      timeout: 5000,
    })
  },

  // 修改
  update(formData) {
    return app.service.request({
      url: '/sys/module-name/update',
      method: 'post',
      transformRequest: [
        function (data) {
          let ret = ''
          for (let it in data) {
            ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
          }
          return ret.substring(0, ret.lastIndexOf('&'))
        },
      ],
      data: formData,
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      responseType: 'json',
      timeout: 5000,
    })
  },

  // 查看
  view(formData) {
    return app.service.get('/sys/module-name/view', {params: formData})
  },

  // 删除
  delete(formData) {
    return app.service.get('/sys/module-name/delete', {params: formData})
  }
}
```

## 请求方式规范

### GET 请求

```javascript
list(formData) {
  return app.service.get('/sys/module-name/list', {
    params: formData,
    timeout: 5000  // 默认超时
  })
}
```

**适用场景**：
- 查询操作（list、view）
- 删除操作（delete）

### POST 请求（表单格式）

```javascript
add(formData) {
  return app.service.request({
    url: '/sys/module-name/add',
    method: 'post',
    transformRequest: [
      function (data) {
        let ret = ''
        for (let it in data) {
          ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
        }
        return ret.substring(0, ret.lastIndexOf('&'))
      },
    ],
    data: formData,
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    responseType: 'json',
    timeout: 5000,
  })
}
```

**适用场景**：
- 新增操作（add）
- 修改操作（update）
- 状态更新（updateStatus）

### POST 请求（JSON 格式）

```javascript
saveApiPermissions(formData) {
  return app.service.request({
    url: '/sys/module-name/config-permissions',
    method: 'post',
    data: formData,
    headers: {'Content-Type': 'application/json'},
    responseType: 'json',
    timeout: 5000,
  })
}
```

**适用场景**：
- 复杂数据结构（如嵌套对象、数组）
- 批量操作

### 文件上传

```javascript
importExcel(formData) {
  return app.service.request({
    url: '/sys/module-name/import-excel',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' },
    timeout: 60000,  // 导入超时：1分钟
  })
}
```

### 文件下载

```javascript
exportExcel(formData) {
  return app.service.request({
    url: '/sys/module-name/export-excel',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'application/json' },
    responseType: 'blob',  // 重要：设置为blob
    timeout: 300000,  // 导出超时：5分钟
  })
}
```

## 超时时间配置规范

根据业务场景设置合理的超时时间：

| 业务场景 | 超时时间 | 说明 |
|---------|---------|------|
| 普通查询 | 5000ms | 默认超时 |
| 长列表查询 | 30000ms | 如1000条人员数据 |
| 文件导入 | 60000ms | 1分钟 |
| 文件导出 | 300000ms | 5分钟 |
| 下载模板 | 30000ms | 30秒 |

**示例**：

```javascript
org: {
  list(formData) {
    return app.service.request({
      url: '/sys/person/org/list',
      method: 'post',
      data: formData,
      timeout: 30000,  // 大数据量查询，设置30秒超时
    })
  }
}
```

## API 路径前缀规范

### 必须包含前缀

系统管理类接口必须包含 `/sys` 前缀：

```javascript
// ✅ 正确
list(formData) {
  return app.service.get('/sys/third-party-app/list', {params: formData})
}

// ❌ 错误
list(formData) {
  return app.service.get('/third-party-app/list', {params: formData})
}
```

### 路径命名规范

```
/sys/{模块名}/{操作}

示例：
/sys/third-party-app/list
/sys/third-party-app/add
/sys/third-party-app/update
/sys/third-party-app/view
/sys/third-party-app/delete
```

## 方法命名规范

### 标准 CRUD 方法

- `list`: 列表查询（分页）
- `add`: 新增
- `update`: 修改
- `view`: 查看详情
- `delete`: 删除

### 特殊方法命名

```javascript
export default {
  // 列表查询
  list(formData) {},
  
  // 新增
  add(formData) {},
  
  // 修改
  update(formData) {},
  
  // 查看
  view(formData) {},
  
  // 删除
  delete(formData) {},
  
  // 状态更新
  updateStatus(formData) {},
  
  // 重新生成密钥
  regenerateSecret(formData) {},
  
  // 获取权限列表
  getApiPermissions(formData) {},
  
  // 保存权限配置
  saveApiPermissions(formData) {},
  
  // 导出 Excel
  exportExcel(formData) {},
  
  // 导入 Excel
  importExcel(formData) {},
  
  // 下载模板
  downloadTemplate() {},
}
```

## 分模块组织

当一个模块有多个子功能时，使用嵌套对象组织：

```javascript
export default {
  org: {
    list(formData) {
      return app.service.request({
        url: '/sys/person/org/list',
        method: 'post',
        data: formData,
        timeout: 30000,
      })
    },
    add(formData) {
      return app.service.post('/sys/person/org/add', formData)
    },
  },
  
  ent: {
    list(formData) {
      return app.service.get('/sys/person/ent/list', {
        params: formData,
        timeout: 30000
      })
    },
    add(formData) {
      return app.service.post('/sys/person/ent/add', formData)
    },
  }
}
```

## 必须遵守的规则

1. **路径前缀**：系统管理类接口必须包含 `/sys` 前缀
2. **POST 表单**：必须使用 `transformRequest` 序列化
3. **POST 表单**：必须设置 `Content-Type: application/x-www-form-urlencoded`
4. **POST JSON**：必须设置 `Content-Type: application/json`
5. **文件上传**：必须设置 `Content-Type: multipart/form-data`
6. **文件下载**：必须设置 `responseType: 'blob'`
7. **超时时间**：根据业务场景设置合理的超时时间
8. **方法命名**：使用标准的 CRUD 方法名（list/add/update/view/delete）
