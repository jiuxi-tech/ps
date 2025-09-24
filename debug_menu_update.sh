#!/bin/bash

# 修复后的菜单更新请求
# 主要修复：
# 1. 将字符串 "null" 改为空值或移除
# 2. 确保数据类型正确
# 3. 只传递必要的字段

curl 'http://localhost:10801/ps-be/sys/menu/update' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6' \
  -H 'Connection: keep-alive' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -H 'Origin: http://localhost:10801' \
  -H 'Referer: http://localhost:10801/' \
  -H 'token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhNTc0YWMxYWI1ODE3OWY5MTA0NjA4NDE0ODllNTQwODQzYzFlYzMzY2ZhZWY3ZjJhZWMyNTcxMGI1Mzc3YzQ5NjhmNGJiMmE3ODJjMGNkNzNjMjgwZTg1YWNjYjM4YzVkMTBlNmExMDM1NTA1OTM2YWFmOGY1YmU5ZjcxYzU2ZTIzYWM0YmQzNmI3NDg1YWI1NjRkYjdiYzI2YzdjZjdlZTc4NWMzMTBkYTE2MjFmODEyMGZlZGQwYzNlM2U4MmFjYzk2ZGU2NWUzNjBhZWY1NTNkNDhkODY1MjQ2MmY5OGE2MjdkNmZhNWE1YjdjNDg0Mjk5OTIzMDA5OGRiMWIyZGZmYTg0ZmM1YWNhMDMwM2ExY2YxYmY1ZWU2YWMxMzUyYzY5N2U2NDQ1ZGNjYzRjYTMyNDg1OTJmYjcyNGQwNGEyYTg4NzgzNDk1ZTAwZWQxNDNmOWQ4YjA0NDA5ZWM4NmIyYzUyMTJjYmZkZWY0MmNjNjkxMzg5MzRjZDA4NzM4ZmVlYjhmNWEzMzgwNWI4ZDRlNzIxNDEyMmJkYTYyMTg0Y2NhNWY3MjQzNjllMzQ0Mzk1ZWYzMTBmN2YwN2NjMzM1ODA1MDBhZTc2YmI4ZDMwNTAyN2E4Mjk0MjJmNGI1YjgxODI4Mjk2MDI1ZDRiYzM0YjFmZjVkMDIyNTI3YmEyMTk0Y2JlMzc2YmFhZmJhOTViOWFmN2U4ZjE4NTk4OTgxMTA5NmU0OTI4ODJhNjk1NmZmZTE1M2Q2YWQwMzYiLCJpc3MiOiJjb20uaml1eGkiLCJleHAiOjE3NTg3MzM5NDksInZlcnNpb24iOiJ2MyIsImlhdCI6MTc1ODcyNjc0OSwianRpIjoiNWU2Y2RmYzU2ZGEzNGYwZjg0MmU1NjdlZWQ1MWI1MWUifQ.zm9eJtRBt5Jrji71fJMiDR-EQXx8nsa8o56Br2zi-Q4' \
  --data-urlencode 'menuId=1956271355557052416' \
  --data-urlencode 'menuName=备份与恢复' \
  --data-urlencode 'menuCode=OPS_DB_BACKUP' \
  --data-urlencode 'menuUri=/sys/database-backup/list' \
  --data-urlencode 'menuPid=1956055706947813376' \
  --data-urlencode 'menuTreePid=1956055706947813376' \
  --data-urlencode 'menuSource=1' \
  --data-urlencode 'menuType=SYS1901' \
  --data-urlencode 'orderIndex=0.0' \
  --data-urlencode 'enabled=1' \
  --data-urlencode 'actived=1' \
  --data-urlencode 'creator=1111111111111111111' \
  --data-urlencode 'createTime=20250815162651' \
  --data-urlencode 'updateTime=20250905003945' \
  -v

echo ""
echo "===== 修复说明 ====="
echo "1. 移除了所有 'null' 字符串值的字段"
echo "2. 将 orderIndex 改为 0.0 (Double 类型)"  
echo "3. 使用 --data-urlencode 确保正确编码"
echo "4. 保留了必需的字段用于验证"