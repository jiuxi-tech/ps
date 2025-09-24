# 菜单更新接口参数绑定失败问题修复

## 问题分析
错误代码：607 - "数据验证失败" (VALIDATION_FAILED)

### 原始请求中的问题：
1. **字符串 "null" 值**：`tenantId=null`, `updator=null` 等会导致 Spring 参数绑定失败
2. **双重URL编码**：`menuUri=%252Fsys%252Fdatabase-backup%252Flist` (应该是单层编码)
3. **数据类型问题**：`orderIndex=0` 应该是 `0.0` (Double类型)

## 修复后的请求数据

```bash
curl 'http://localhost:10801/ps-be/sys/menu/update' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -H 'token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhNTc0YWMxYWI1ODE3OWY5MTA0NjA4NDE0ODllNTQwODQzYzFlYzMzY2ZhZWY3ZjJhZWMyNTcxMGI1Mzc3YzQ5NjhmNGJiMmE3ODJjMGNkNzNjMjgwZTg1YWNjYjM4YzVkMTBlNmExMDM1NTA1OTM2YWFmOGY1YmU5ZjcxYzU2ZTIzYWM0YmQzNmI3NDg1YWI1NjRkYjdiYzI2YzdjZjdlZTc4NWMzMTBkYTE2MjFmODEyMGZlZGQwYzNlM2U4MmFjYzk2ZGU2NWUzNjBhZWY1NTNkNDhkODY1MjQ2MmY5OGE2MjdkNmZhNWE1YjdjNDg0Mjk5OTIzMDA5OGRiMWIyZGZmYTg0ZmM1YWNhMDMwM2ExY2YxYmY1ZWU2YWMxMzUyYzY5N2U2NDQ1ZGNjYzRjYTMyNDg1OTJmYjcyNGQwNGEyYTg4NzgzNDk1ZTAwZWQxNDNmOWQ4YjA0NDA5ZWM4NmIyYzUyMTJjYmZkZWY0MmNjNjkxMzg5MzRjZDA4NzM4ZmVlYjhmNWEzMzgwNWI4ZDRlNzIxNDEyMmJkYTYyMTg0Y2NhNWY3MjQzNjllMzQ0Mzk1ZWYzMTBmN2YwN2NjMzM1ODA1MDBhZTc2YmI4ZDMwNTAyN2E4Mjk0MjJmNGI1YjgxODI4Mjk2MDI1ZDRiYzM0YjFmZjVkMDIyNTI3YmEyMTk0Y2JlMzc2YmFhZmJhOTViOWFmN2U4ZjE4NTk4OTgxMTA5NmU0OTI4ODJhNjk1NmZmZTE1M2Q2YWQwMzYiLCJpc3MiOiJjb20uaml1eGkiLCJleHAiOjE3NTg3MzM5NDksInZlcnNpb24iOiJ2MyIsImlhdCI6MTc1ODcyNjc0OSwianRpIjoiNWU2Y2RmYzU2ZGEzNGYwZjg0MmU1NjdlZWQ1MWI1MWUifQ.zm9eJtRBt5Jrji71fJMiDR-EQXx8nsa8o56Br2zi-Q4' \
  -d 'menuId=1956271355557052416&menuName=%E5%A4%87%E4%BB%BD%E4%B8%8E%E6%81%A2%E5%A4%8D&menuCode=OPS_DB_BACKUP&menuUri=%2Fsys%2Fdatabase-backup%2Flist&menuPid=1956055706947813376&menuTreePid=1956055706947813376&menuSource=1&menuType=SYS1901&orderIndex=0.0&enabled=1&actived=1&creator=1111111111111111111&createTime=20250815162651&updateTime=20250905003945'
```

## 修复要点对比

| 字段 | 原始值 | 修复后 | 说明 |
|------|-------|--------|------|
| menuUri | `%252Fsys%252Fdatabase-backup%252Flist` | `%2Fsys%2Fdatabase-backup%2Flist` | 移除双重编码 |
| orderIndex | `0` | `0.0` | 确保Double类型 |
| tenantId | `null` | 移除 | 避免字符串"null" |
| updator | `null` | 移除 | 避免字符串"null" |
| extend01-03 | `null` | 移除 | 避免字符串"null" |
| checked | `null` | 移除 | 避免字符串"null" |

## 前端代码修复建议

如果这是前端代码问题，建议在前端处理：

```javascript
// 修复前端发送逻辑
const formData = {
  menuId: "1956271355557052416",
  menuName: "备份与恢复",
  menuCode: "OPS_DB_BACKUP",
  menuUri: "/sys/database-backup/list", // 不要双重编码
  menuPid: "1956055706947813376",
  menuTreePid: "1956055706947813376",
  menuSource: 1,
  menuType: "SYS1901",
  orderIndex: 0.0, // 确保是数字类型
  enabled: 1,
  actived: 1,
  creator: "1111111111111111111",
  createTime: "20250815162651",
  updateTime: "20250905003945"
  // 移除所有值为 null 的字段
};

// 清理空值
Object.keys(formData).forEach(key => {
  if (formData[key] === null || formData[key] === undefined || formData[key] === 'null') {
    delete formData[key];
  }
});
```

## 根本解决方案

1. **后端增强验证**：在 `SystemMenuCommandController.update` 方法中添加更详细的参数验证日志
2. **前端数据清理**：在发送请求前清理所有空值和"null"字符串
3. **统一数据格式**：确保前后端对数据类型的一致性约定