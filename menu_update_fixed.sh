#!/bin/bash

# 修复后的菜单更新请求
# 主要修复：
# 1. 移除所有字符串 "null" 值
# 2. 修复 orderIndex 数据类型 (Double)
# 3. 修复 menuUri 双重编码问题
# 4. 只传递非空且必要的字段

echo "=== 开始测试菜单更新接口 ==="
echo "正在发送修复后的请求..."
echo ""

curl 'http://localhost:10801/ps-be/sys/menu/update' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6' \
  -H 'Connection: keep-alive' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -H 'Origin: http://localhost:10801' \
  -H 'Referer: http://localhost:10801/' \
  -H 'token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhNTc0YWMxYWI1ODE3OWY5MTA0NjA4NDE0ODllNTQwODQzYzFlYzMzY2ZhZWY3ZjJhZWMyNTcxMGI1Mzc3YzQ5NjhmNGJiMmE3ODJjMGNkNzNjMjgwZTg1YWNjYjM4YzVkMTBlNmExMDM1NTA1OTM2YWFmOGY1YmU5ZjcxYzU2ZTIzYWM0YmQzNmI3NDg1YWI1NjRkYjdiYzI2YzdjZjdlZTc4NWMzMTBkYTE2MjFmODEyMGZlZGQwYzNlM2U4MmFjYzk2ZGU2NWUzNjBhZWY1NTNkNDhkODY1MjQ2MmY5OGE2MjdkNmZhNWE1YjdjNDg0Mjk5OTIzMDA5OGRiMWIyZGZmYTg0ZmM1YWNhMDMwM2ExY2YxYmY1ZWU2YWMxMzUyYzY5N2U2NDQ1ZGNjYzRjYTMyNDg1OTJmYjcyNGQwNGEyYTg4NzgzNDk1ZTAwZWQxNDNmOWQ4YjA0NDA5ZWM4NmIyYzUyMTJjYmZkZWY0MmNjNjkxMzg5MzRjZDA4NzM4ZmVlYjhmNWEzMzgwNWI4ZDRlNzIxNDEyMmJkYTYyMTg0Y2NhNWY3MjQzNjllMzQ0Mzk1ZWYzMTBmN2YwN2NjMzM1ODA1MDBhZTc2YmI4ZDMwNTAyN2E4Mjk0MjJmNGI1YjgxODI4Mjk2MDI1ZDRiYzM0YjFmZjVkMDIyNTI3YmEyMTk0Y2JlMzc2YmFhZmJhOTViOWFmN2U4ZjE4NTk4OTgxMTA5NmU0OTI4ODJhNjk1NmZmZTE1M2Q2YWQwMzYiLCJpc3MiOiJjb20uaml1eGkiLCJleHAiOjE3NTg3MzM5NDksInZlcnNpb24iOiJ2MyIsImlhdCI6MTc1ODcyNjc0OSwianRpIjoiNWU2Y2RmYzU2ZGEzNGYwZjg0MmU1NjdlZWQ1MWI1MWUifQ.zm9eJtRBt5Jrji71fJMiDR-EQXx8nsa8o56Br2zi-Q4' \
  --data 'menuId=1956271355557052416&menuName=%E5%A4%87%E4%BB%BD%E4%B8%8E%E6%81%A2%E5%A4%8D&menuCode=OPS_DB_BACKUP&menuUri=%2Fsys%2Fdatabase-backup%2Flist&menuPid=1956055706947813376&menuTreePid=1956055706947813376&menuSource=1&menuType=SYS1901&orderIndex=0.0&enabled=1&actived=1&creator=1111111111111111111&createTime=20250815162651&updateTime=20250905003945' \
  -v

echo ""
echo "=== 修复说明 ==="
echo "1. ✅ 移除了所有 'null' 字符串值的字段"
echo "2. ✅ orderIndex 改为 0.0 (Double 类型)"  
echo "3. ✅ menuUri 修复双重编码问题: /sys/database-backup/list"
echo "4. ✅ 保留所有必需的验证字段"
echo "5. ✅ 移除空值字段: menuIcon, menuDesc, tenantId, updator, extend01-03, checked"
echo ""
echo "=== 原始问题分析 ==="
echo "错误代码 607 = 数据验证失败 (VALIDATION_FAILED)"
echo "主要问题："
echo "  - 字符串 'null' 值导致参数绑定失败"
echo "  - menuUri 被双重 URL 编码"
echo "  - orderIndex 数据类型不匹配"