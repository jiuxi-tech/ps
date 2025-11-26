# user模块接口测试脚本

## 1. 获取当前用户信息接口测试脚本

```bash
# 正常获取用户信息
curl -X GET "http://192.168.0.139:8082/api/v1/users/me" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-User-Dept-Id: {DEPT_ID}" \
  -H "X-User-Person-Id: {PERSON_ID}" \
  -H "X-User-Role-Ids: {ROLE_IDS}" \
  -H "X-User-City-Code: {CITY_CODE}"

# 无认证信息访问
curl -X GET "http://192.168.0.139:8082/api/v1/users/me"
```

## 2. 创建用户接口测试脚本

```bash
# 正常创建用户
curl -X POST "http://192.168.0.139:8082/api/v1/users" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-User-Person-Id: {OPERATOR_ID}" \
  -H "X-Tenant-Id: {TENANT_ID}" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "profile": {
      "personName": "测试用户",
      "personNo": "TEST001"
    },
    "deptId": "DEPT001"
  }'

# 创建用户数据验证失败
curl -X POST "http://192.168.0.139:8082/api/v1/users" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-User-Person-Id: {OPERATOR_ID}" \
  -H "X-Tenant-Id: {TENANT_ID}" \
  -d '{
    "username": "",
    "password": "password123"
  }'
```

## 3. 更新用户信息接口测试脚本

```bash
# 正常更新用户信息
curl -X PUT "http://192.168.0.139:8082/api/v1/users/{PERSON_ID}" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-User-Person-Id: {OPERATOR_ID}" \
  -H "X-Tenant-Id: {TENANT_ID}" \
  -d '{
    "personName": "更新后的用户",
    "phone": "13800138000"
  }'

# 更新不存在的用户
curl -X PUT "http://192.168.0.139:8082/api/v1/users/NON_EXISTENT_ID" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-User-Person-Id: {OPERATOR_ID}" \
  -H "X-Tenant-Id: {TENANT_ID}" \
  -d '{
    "personName": "更新后的用户",
    "phone": "13800138000"
  }'
```

## 4. 查看用户详情接口测试脚本

```bash
# 正常查看用户详情
curl -X GET "http://192.168.0.139:8082/api/v1/users/{PERSON_ID}" \
  -H "Authorization: Bearer {JWT_TOKEN}"

# 查看不存在的用户详情
curl -X GET "http://192.168.0.139:8082/api/v1/users/NON_EXISTENT_ID" \
  -H "Authorization: Bearer {JWT_TOKEN}"
```

## 5. 根据用户名查询用户接口测试脚本

```bash
# 正常根据用户名查询用户
curl -X GET "http://192.168.0.139:8082/api/v1/users/username/testuser" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-Tenant-Id: {TENANT_ID}"

# 查询不存在的用户名
curl -X GET "http://192.168.0.139:8082/api/v1/users/username/nonexistent" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-Tenant-Id: {TENANT_ID}"
```

## 6. 根据部门查询用户列表接口测试脚本

```bash
# 正常根据部门查询用户列表
curl -X GET "http://192.168.0.139:8082/api/v1/users/departments/{DEPT_ID}" \
  -H "Authorization: Bearer {JWT_TOKEN}"

# 查询不存在的部门
curl -X GET "http://192.168.0.139:8082/api/v1/users/departments/NON_EXISTENT_DEPT" \
  -H "Authorization: Bearer {JWT_TOKEN}"
```

## 7. 删除用户接口测试脚本

```bash
# 正常删除用户
curl -X DELETE "http://192.168.0.139:8082/api/v1/users/{PERSON_ID}" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-User-Person-Id: {OPERATOR_ID}"

# 删除不存在的用户
curl -X DELETE "http://192.168.0.139:8082/api/v1/users/NON_EXISTENT_ID" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-User-Person-Id: {OPERATOR_ID}"
```

## 8. 批量删除用户接口测试脚本

```bash
# 正常批量删除用户
curl -X DELETE "http://192.168.0.139:8082/api/v1/users/batch" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-User-Person-Id: {OPERATOR_ID}" \
  -d '["{PERSON_ID_1}", "{PERSON_ID_2}"]'

# 批量删除部分不存在的用户
curl -X DELETE "http://192.168.0.139:8082/api/v1/users/batch" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-User-Person-Id: {OPERATOR_ID}" \
  -d '["{PERSON_ID_1}", "NON_EXISTENT_ID"]'
```

## 9. 激活用户接口测试脚本

```bash
# 正常激活用户
curl -X PUT "http://192.168.0.139:8082/api/v1/users/{PERSON_ID}/activate" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-User-Person-Id: {OPERATOR_ID}"

# 激活不存在的用户
curl -X PUT "http://192.168.0.139:8082/api/v1/users/NON_EXISTENT_ID/activate" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-User-Person-Id: {OPERATOR_ID}"
```

## 10. 停用用户接口测试脚本

```bash
# 正常停用用户
curl -X PUT "http://192.168.0.139:8082/api/v1/users/{PERSON_ID}/deactivate" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-User-Person-Id: {OPERATOR_ID}"

# 停用不存在的用户
curl -X PUT "http://192.168.0.139:8082/api/v1/users/NON_EXISTENT_ID/deactivate" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-User-Person-Id: {OPERATOR_ID}"
```

## 11. 分页查询用户列表接口测试脚本

```bash
# 正常分页查询用户列表
curl -X POST "http://192.168.0.139:8082/api/v1/users/search" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-Tenant-Id: {TENANT_ID}" \
  -d '{
    "current": 1,
    "size": 10,
    "personName": "测试"
  }'

# 分页查询无结果
curl -X POST "http://192.168.0.139:8082/api/v1/users/search" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "X-Tenant-Id: {TENANT_ID}" \
  -d '{
    "current": 1,
    "size": 10,
    "personName": "不存在的用户"
  }'
```