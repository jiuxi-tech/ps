-- ========================================
-- API清单初始数据
-- 创建时间: 2025-01-30
-- 描述: 初始化系统可供第三方调用的API列表
-- ========================================

-- 插入API定义初始数据
INSERT INTO `tp_api_definition` (`api_id`, `api_code`, `api_name`, `api_path`, `http_method`, `category`, `description`, `is_sensitive`, `require_secret`, `status`, `order_index`, `actived`, `creator`, `create_time`) 
VALUES
-- 用户管理类API
('1', 'api_user_query', '查询用户信息', '/open-api/v1/users/{personId}', 'GET', '用户管理', '根据人员ID查询单个用户的脱敏信息', 1, 0, 1, 1.0, 1, 'system', '20250130000000'),
('2', 'api_user_list', '用户列表查询', '/open-api/v1/users', 'GET', '用户管理', '分页查询用户列表，支持按部门筛选', 1, 0, 1, 2.0, 1, 'system', '20250130000000'),
('3', 'api_user_search', '搜索用户', '/open-api/v1/users/search', 'POST', '用户管理', '根据关键词搜索用户信息', 1, 0, 1, 3.0, 1, 'system', '20250130000000'),

-- 组织管理类API
('4', 'api_dept_query', '查询部门信息', '/open-api/v1/departments/{deptId}', 'GET', '组织管理', '根据部门ID查询部门详细信息', 0, 0, 1, 11.0, 1, 'system', '20250130000000'),
('5', 'api_dept_tree', '部门树查询', '/open-api/v1/departments/tree', 'GET', '组织管理', '查询完整的部门树形结构', 0, 0, 1, 12.0, 1, 'system', '20250130000000'),
('6', 'api_dept_list', '部门列表查询', '/open-api/v1/departments', 'GET', '组织管理', '分页查询部门列表', 0, 0, 1, 13.0, 1, 'system', '20250130000000');

-- ========================================
-- 查询验证
-- ========================================

-- 查看所有API定义
-- SELECT * FROM tp_api_definition WHERE actived = 1 ORDER BY category, order_index;

-- 按分类统计API数量
-- SELECT category, COUNT(*) as api_count FROM tp_api_definition WHERE actived = 1 GROUP BY category;
