# user模块接口说明文档

## 1. 获取当前用户信息接口

**URL**: `/api/v1/users/me`
**方法**: GET
**描述**: 登录成功后获取当前用户信息
**请求头**:
- X-User-Dept-Id (可选): 用户部门ID
- X-User-Person-Id (可选): 用户人员ID
- X-User-Role-Ids (可选): 用户角色ID列表
- X-User-City-Code (可选): 用户城市代码

**响应**: 
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    // PersonVO对象
  }
}
```

## 2. 创建用户接口

**URL**: `/api/v1/users`
**方法**: POST
**描述**: 创建新用户
**请求头**:
- X-User-Person-Id: 操作员ID
- X-Tenant-Id: 租户ID

**请求体**:
```json
{
  // UserCreateDTO对象
}
```

**响应**: 
```json
{
  "code": 0,
  "msg": "success",
  "data": "personId"
}
```

## 3. 更新用户信息接口

**URL**: `/api/v1/users/{personId}`
**方法**: PUT
**描述**: 更新指定用户的信息
**路径参数**:
- personId: 用户ID

**请求头**:
- X-User-Person-Id: 操作员ID
- X-Tenant-Id: 租户ID

**请求体**:
```json
{
  // UserUpdateDTO对象
}
```

**响应**: 
```json
{
  "code": 0,
  "msg": "success",
  "data": "更新成功"
}
```

## 4. 查看用户详情接口

**URL**: `/api/v1/users/{personId}`
**方法**: GET
**描述**: 获取指定用户的详细信息
**路径参数**:
- personId: 用户ID

**响应**: 
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    // UserResponseDTO对象
  }
}
```

## 5. 根据用户名查询用户接口

**URL**: `/api/v1/users/username/{username}`
**方法**: GET
**描述**: 根据用户名查询用户信息
**路径参数**:
- username: 用户名

**请求头**:
- X-Tenant-Id: 租户ID

**响应**: 
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    // UserResponseDTO对象
  }
}
```

## 6. 根据部门查询用户列表接口

**URL**: `/api/v1/users/departments/{deptId}`
**方法**: GET
**描述**: 根据部门ID查询该部门下的所有用户
**路径参数**:
- deptId: 部门ID

**响应**: 
```json
{
  "code": 0,
  "msg": "success",
  "data": [
    // UserResponseDTO对象列表
  ]
}
```

## 7. 删除用户接口

**URL**: `/api/v1/users/{personId}`
**方法**: DELETE
**描述**: 删除指定用户
**路径参数**:
- personId: 用户ID

**请求头**:
- X-User-Person-Id: 操作员ID

**响应**: 
```json
{
  "code": 0,
  "msg": "success",
  "data": "删除成功"
}
```

## 8. 批量删除用户接口

**URL**: `/api/v1/users/batch`
**方法**: DELETE
**描述**: 批量删除用户
**请求体**:
```json
[
  "personId1",
  "personId2"
]
```

**请求头**:
- X-User-Person-Id: 操作员ID

**响应**: 
```json
{
  "code": 0,
  "msg": "success",
  "data": "批量删除成功"
}
```

## 9. 激活用户接口

**URL**: `/api/v1/users/{personId}/activate`
**方法**: PUT
**描述**: 激活指定用户
**路径参数**:
- personId: 用户ID

**请求头**:
- X-User-Person-Id: 操作员ID

**响应**: 
```json
{
  "code": 0,
  "msg": "success",
  "data": "激活成功"
}
```

## 10. 停用用户接口

**URL**: `/api/v1/users/{personId}/deactivate`
**方法**: PUT
**描述**: 停用指定用户
**路径参数**:
- personId: 用户ID

**请求头**:
- X-User-Person-Id: 操作员ID

**响应**: 
```json
{
  "code": 0,
  "msg": "success",
  "data": "停用成功"
}
```

## 11. 分页查询用户列表接口

**URL**: `/api/v1/users/search`
**方法**: POST
**描述**: 分页查询用户列表
**请求头**:
- X-Tenant-Id: 租户ID

**请求体**:
```json
{
  // UserQueryDTO对象
}
```

**响应**: 
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "data": [
      // UserResponseDTO对象列表
    ],
    "total": 0,
    "current": 1,
    "size": 10,
    "pages": 1
  }
}
```