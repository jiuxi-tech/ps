#!/bin/bash

# 使用 JSON 格式的菜单更新请求（如果后端支持）
curl 'http://localhost:10801/ps-be/sys/menu/update' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Content-Type: application/json' \
  -H 'token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhNTc0YWMxYWI1ODE3OWY5MTA0NjA4NDE0ODllNTQwODQzYzFlYzMzY2ZhZWY3ZjJhZWMyNTcxMGI1Mzc3YzQ5NjhmNGJiMmE3ODJjMGNkNzNjMjgwZTg1YWNjYjM4YzVkMTBlNmExMDM1NTA1OTM2YWFmOGY1YmU5ZjcxYzU2ZTIzYWM0YmQzNmI3NDg1YWI1NjRkYjdiYzI2YzdjZjdlZTc4NWMzMTBkYTE2MjFmODEyMGZlZGQwYzNlM2U4MmFjYzk2ZGU2NWUzNjBhZWY1NTNkNDhkODY1MjQ2MmY5OGE2MjdkNmZhNWE1YjdjNDg0Mjk5OTIzMDA5OGRiMWIyZGZmYTg0ZmM1YWNhMDMwM2ExY2YxYmY1ZWU2YWMxMzUyYzY5N2U2NDQ1ZGNjYzRjYTMyNDg1OTJmYjcyNGQwNGEyYTg4NzgzNDk1ZTAwZWQxNDNmOWQ4YjA0NDA5ZWM4NmIyYzUyMTJjYmZkZWY0MmNjNjkxMzg5MzRjZDA4NzM4ZmVlYjhmNWEzMzgwNWI4ZDRlNzIxNDEyMmJkYTYyMTg0Y2NhNWY3MjQzNjllMzQ0Mzk1ZWYzMTBmN2YwN2NjMzM1ODA1MDBhZTc2YmI4ZDMwNTAyN2E4Mjk0MjJmNGI1YjgxODI4Mjk2MDI1ZDRiYzM0YjFmZjVkMDIyNTI3YmEyMTk0Y2JlMzc2YmFhZmJhOTViOWFmN2U4ZjE4NTk4OTgxMTA5NmU0OTI4ODJhNjk1NmZmZTE1M2Q2YWQwMzYiLCJpc3MiOiJjb20uaml1eGkiLCJleHAiOjE3NTg3MzM5NDksInZlcnNpb24iOiJ2MyIsImlhdCI6MTc1ODcyNjc0OSwianRpIjoiNWU2Y2RmYzU2ZGEzNGYwZjg0MmU1NjdlZWQ1MWI1MWUifQ.zm9eJtRBt5Jrji71fJMiDR-EQXx8nsa8o56Br2zi-Q4' \
  -d '{
    "menuId": "1956271355557052416",
    "menuName": "备份与恢复", 
    "menuCode": "OPS_DB_BACKUP",
    "menuUri": "/sys/database-backup/list",
    "menuPid": "1956055706947813376",
    "menuTreePid": "1956055706947813376", 
    "menuSource": 1,
    "menuType": "SYS1901",
    "orderIndex": 0.0,
    "enabled": 1,
    "actived": 1,
    "creator": "1111111111111111111",
    "createTime": "20250815162651",
    "updateTime": "20250905003945"
  }' \
  -v

echo ""
echo "注意：这个请求使用 JSON 格式，但原始接口可能不支持，仅作为参考"