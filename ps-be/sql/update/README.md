# 数据库更新脚本说明

## 脚本执行顺序

请按照文件名中的日期顺序执行SQL脚本。

## 最新更新

### 20251130_add_last_call_time.sql
**更新日期**: 2025-11-30
**影响表**: tp_third_party_app
**更新内容**: 添加最后调用时间字段 `last_call_time`

**执行方式**:
```bash
# 方式1: 使用MySQL命令行
mysql -u root -p your_database < 20251130_add_last_call_time.sql

# 方式2: 登录MySQL后执行
mysql -u root -p your_database
source 20251130_add_last_call_time.sql
```

**验证**:
```sql
-- 查看字段是否添加成功
DESCRIBE tp_third_party_app;

-- 应该能看到 last_call_time 字段
```

## 注意事项

1. 执行脚本前请先备份数据库
2. 脚本使用了 `IF NOT EXISTS` 语法，可以安全重复执行
3. 如遇到权限问题，请联系DBA处理
