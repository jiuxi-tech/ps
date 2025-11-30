-- ========================================
-- API定义初始化数据
-- 创建时间: 2025-01-30
-- 描述: 初始化可供第三方调用的API清单
-- ========================================

-- 清空旧数据（可选，谨慎使用）
-- DELETE FROM tp_api_definition;

-- 插入API定义数据
INSERT INTO `tp_api_definition` 
    (`api_id`, `api_code`, `api_name`, `api_path`, `http_method`, `category`, `description`, `is_sensitive`, `require_secret`, `status`, `order_index`, `actived`, `creator`, `create_time`) 
VALUES
    -- 用户管理API
    ('1', 'api_user_query', '查询用户信息', '/open-api/v1/users/{personId}', 'GET', '用户管理', '根据人员ID查询单个用户的脱敏信息', 1, 0, 1, 1, 1, 'system', '20250130000000'),
    ('2', 'api_user_list', '用户列表查询', '/open-api/v1/users', 'GET', '用户管理', '分页查询用户列表，支持部门筛选', 1, 0, 1, 2, 1, 'system', '20250130000000'),
    ('3', 'api_user_search', '搜索用户', '/open-api/v1/users/search', 'POST', '用户管理', '根据关键词和部门搜索用户', 1, 0, 1, 3, 1, 'system', '20250130000000'),
    
    -- 组织管理API（预留）
    ('4', 'api_dept_query', '查询部门信息', '/open-api/v1/departments/{deptId}', 'GET', '组织管理', '根据部门ID查询部门信息', 0, 0, 1, 11, 1, 'system', '20250130000000'),
    ('5', 'api_dept_tree', '部门树查询', '/open-api/v1/departments/tree', 'GET', '组织管理', '查询完整的部门树结构', 0, 0, 1, 12, 1, 'system', '20250130000000');

-- 查看插入结果
SELECT * FROM tp_api_definition WHERE actived = 1 ORDER BY category, order_index;
