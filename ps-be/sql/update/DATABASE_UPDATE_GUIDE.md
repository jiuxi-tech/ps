# 数据库更新执行指南

## 概述
本指南将帮助您正确执行组织机构历史记录表的字段更新脚本，为 `org_tree_change_history` 表添加完整树结构字段。

## 数据库连接信息
根据配置文件 `application-dev.yml`：
- **数据库地址**: alilaoba.cn:13307
- **数据库名称**: ps-bmp
- **用户名**: root
- **密码**: $D8BZ8Qmav
- **驱动**: MariaDB/MySQL

## 执行步骤

### 1. 连接数据库
使用您喜欢的数据库客户端工具连接到数据库：

**命令行方式**：
```bash
mysql -h alilaoba.cn -P 13307 -u root -p ps-bmp
# 输入密码: $D8BZ8Qmav
```

**图形化工具**：
- MySQL Workbench
- Navicat
- phpMyAdmin
- DBeaver

### 2. 验证当前表结构
在执行更新前，先查看当前表结构：

```sql
-- 检查表是否存在
SELECT COUNT(*) as table_exists 
FROM information_schema.tables 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history';

-- 查看当前表结构
DESCRIBE org_tree_change_history;

-- 检查是否已有新字段
SELECT COUNT(*) as before_full_tree_exists
FROM information_schema.columns 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history' 
AND column_name = 'before_full_tree';

SELECT COUNT(*) as after_full_tree_exists
FROM information_schema.columns 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history' 
AND column_name = 'after_full_tree';
```

### 3. 执行更新脚本
如果新字段不存在，执行以下完整脚本：

```sql
-- ========================================
-- 组织机构历史表结构更新脚本
-- 创建时间: 2024-01-27
-- 描述: 为组织机构变更历史表添加前后全节点树字段
-- ========================================

USE ps-bmp;

-- 检查表是否存在
SELECT COUNT(*) as table_exists 
FROM information_schema.tables 
WHERE table_schema = DATABASE() 
AND table_name = 'org_tree_change_history';

-- 添加前后全节点树字段
-- 如果字段不存在则添加
SET @sql = '';
SELECT COUNT(*) INTO @col_exists 
FROM information_schema.columns 
WHERE table_schema = DATABASE() 
AND table_name = 'org_tree_change_history' 
AND column_name = 'before_full_tree';

SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE `org_tree_change_history` 
     ADD COLUMN `before_full_tree` LONGTEXT COMMENT "变更前的完整组织机构节点树JSON" AFTER `after_data`',
    'SELECT "before_full_tree column already exists" as message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT COUNT(*) INTO @col_exists 
FROM information_schema.columns 
WHERE table_schema = DATABASE() 
AND table_name = 'org_tree_change_history' 
AND column_name = 'after_full_tree';

SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE `org_tree_change_history` 
     ADD COLUMN `after_full_tree` LONGTEXT COMMENT "变更后的完整组织机构节点树JSON" AFTER `before_full_tree`',
    'SELECT "after_full_tree column already exists" as message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 验证字段是否添加成功
SELECT 
    column_name,
    data_type,
    is_nullable,
    column_comment
FROM information_schema.columns 
WHERE table_schema = DATABASE() 
AND table_name = 'org_tree_change_history' 
AND column_name IN ('before_full_tree', 'after_full_tree')
ORDER BY ordinal_position;

-- 查看更新后的表结构
DESCRIBE org_tree_change_history;
```

### 4. 验证更新结果
执行完成后，应该看到类似的输出：

```
+------------------+-----------+------+-----+---------+-------+
| Field            | Type      | Null | Key | Default | Extra |
+------------------+-----------+------+-----+---------+-------+
| id               | bigint(20)| NO   | PRI | NULL    | auto_increment |
| operation_type   | enum(...) | NO   |     | NULL    |       |
| operation_time   | timestamp | NO   |     | CURRENT_TIMESTAMP |   |
| operator_id      | bigint(20)| NO   |     | NULL    |       |
| before_data      | longtext  | YES  |     | NULL    |       |
| after_data       | longtext  | YES  |     | NULL    |       |
| before_full_tree | longtext  | YES  |     | NULL    |       |  ← 新字段
| after_full_tree  | longtext  | YES  |     | NULL    |       |  ← 新字段
| version          | bigint(20)| YES  |     | 1       |       |
| dept_id          | bigint(20)| YES  |     | 0       |       |
+------------------+-----------+------+-----+---------+-------+
```

## 执行后验证

### 验证字段是否正确添加
```sql
-- 应该返回 2 行记录
SELECT 
    column_name,
    data_type,
    is_nullable,
    column_comment
FROM information_schema.columns 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history' 
AND column_name IN ('before_full_tree', 'after_full_tree')
ORDER BY ordinal_position;
```

### 测试数据插入
```sql
-- 测试插入一条包含新字段的记录
INSERT INTO org_tree_change_history (
    operation_type, 
    operator_id, 
    before_data, 
    after_data,
    before_full_tree,
    after_full_tree,
    version,
    dept_id
) VALUES (
    'CREATE', 
    1001, 
    NULL, 
    '{"test": "data"}',
    '{"tree": "before"}',
    '{"tree": "after"}',
    1,
    0
);

-- 查询刚插入的记录
SELECT * FROM org_tree_change_history ORDER BY id DESC LIMIT 1;

-- 清理测试数据
DELETE FROM org_tree_change_history WHERE operation_type = 'CREATE' AND operator_id = 1001 AND before_data IS NULL;
```

## 常见问题

### 1. 权限不足
如果遇到权限错误，确保连接的用户具有 `ALTER` 权限：
```sql
SHOW GRANTS FOR CURRENT_USER();
```

### 2. 表不存在
如果 `org_tree_change_history` 表不存在，需要先运行主表创建脚本：
```sql
-- 运行 ps-be/sql/org_tree_change_history.sql
```

### 3. 字段已存在
脚本会自动检查字段是否存在，如果已存在会显示消息而不会报错。

### 4. 连接超时
如果连接超时，可以尝试：
- 检查网络连接
- 确认数据库服务器是否正常运行
- 尝试使用不同的数据库客户端

## 回滚方案

如果需要回滚更新：

```sql
-- 删除新增的字段
ALTER TABLE org_tree_change_history DROP COLUMN before_full_tree;
ALTER TABLE org_tree_change_history DROP COLUMN after_full_tree;
```

## 执行完成确认

执行完成后，请确认：
- [ ] 新字段 `before_full_tree` 已添加
- [ ] 新字段 `after_full_tree` 已添加  
- [ ] 字段类型为 `LONGTEXT`
- [ ] 字段允许 NULL 值
- [ ] 字段注释正确显示
- [ ] 表结构无异常

执行成功后，您就可以使用新的组织机构历史记录功能了！