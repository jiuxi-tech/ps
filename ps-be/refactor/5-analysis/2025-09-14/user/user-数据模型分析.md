# user模块数据模型分析报告

## 1. 领域模型分析

### 1.1 聚合根 - User
**职责**: 管理用户基本信息、账户信息、联系信息等，确保用户数据的一致性和完整性

**属性**:
- personId (String): 用户ID，聚合根标识
- deptId (String): 部门ID
- profile (UserProfile): 用户基本信息
- account (UserAccount): 用户账户信息
- contactInfo (ContactInfo): 联系信息
- status (UserStatus): 用户状态
- category (UserCategory): 用户类别
- creator (String): 创建人
- createTime (LocalDateTime): 创建时间
- updator (String): 更新人
- updateTime (LocalDateTime): 更新时间
- tenantId (String): 租户ID

**行为**:
- createAccount(): 创建用户账户
- updateProfile(): 更新用户资料
- activate(): 激活用户
- deactivate(): 禁用用户
- isActive(): 检查用户是否激活
- hasAccount(): 检查用户是否有账户

### 1.2 实体 - UserProfile
**职责**: 用户基本信息

**属性**:
- personName (String): 用户姓名
- personNo (String): 用户编号
- gender (String): 性别
- birthDate (LocalDate): 出生日期
- idCard (String): 身份证号
- address (String): 地址
- avatar (String): 头像

### 1.3 实体 - UserAccount
**职责**: 用户账户信息

**属性**:
- accountId (String): 账户ID
- username (String): 用户名
- password (String): 密码
- personId (String): 关联用户ID
- status (AccountStatus): 账户状态
- lastLoginTime (LocalDateTime): 最后登录时间
- loginCount (Integer): 登录次数

**行为**:
- updatePassword(): 更新密码
- recordLogin(): 记录登录

### 1.4 值对象 - ContactInfo
**职责**: 用户联系信息

**属性**:
- phone (PhoneNumber): 电话号码
- email (Email): 邮箱
- emergencyContact (String): 紧急联系人
- emergencyPhone (String): 紧急联系电话

### 1.5 值对象 - PhoneNumber
**职责**: 电话号码

**属性**:
- countryCode (String): 国家代码
- areaCode (String): 区号
- number (String): 电话号码

### 1.6 值对象 - Email
**职责**: 邮箱地址

**属性**:
- address (String): 邮箱地址

### 1.7 枚举 - UserStatus
**职责**: 用户状态

**值**:
- ACTIVE: 激活
- INACTIVE: 未激活
- LOCKED: 锁定
- DELETED: 已删除

### 1.8 枚举 - AccountStatus
**职责**: 账户状态

**值**:
- ACTIVE: 激活
- INACTIVE: 未激活
- LOCKED: 锁定
- EXPIRED: 过期

### 1.9 枚举 - UserCategory
**职责**: 用户类别

**值**:
- PERSON: 个人用户
- ORGANIZATION: 组织用户

## 2. 持久化模型分析

### 2.1 UserPO (用户持久化对象)
**映射关系**: 对应User聚合根

**属性**:
- personId (String): 用户ID
- deptId (String): 部门ID
- personName (String): 用户姓名
- personNo (String): 用户编号
- gender (String): 性别
- birthDate (Date): 出生日期
- idCard (String): 身份证号
- address (String): 地址
- avatar (String): 头像
- phoneCountryCode (String): 电话国家代码
- phoneAreaCode (String): 电话区号
- phoneNumber (String): 电话号码
- email (String): 邮箱
- emergencyContact (String): 紧急联系人
- emergencyPhone (String): 紧急联系电话
- status (String): 用户状态
- category (String): 用户类别
- creator (String): 创建人
- createTime (Date): 创建时间
- updator (String): 更新人
- updateTime (Date): 更新时间
- tenantId (String): 租户ID

### 2.2 AccountPO (账户持久化对象)
**映射关系**: 对应UserAccount实体

**属性**:
- accountId (String): 账户ID
- username (String): 用户名
- password (String): 密码
- personId (String): 关联用户ID
- status (String): 账户状态
- lastLoginTime (Date): 最后登录时间
- loginCount (Integer): 登录次数

## 3. 数据访问模式分析

### 3.1 查询模式
1. **根据ID查询**: findById(personId)
2. **根据用户名查询**: findByUsername(username, tenantId)
3. **分页查询**: findUsers(tenantId, deptId, keyword, current, size)
4. **统计查询**: countUsers(tenantId, deptId, keyword)
5. **根据部门查询**: findByDeptId(deptId)

### 3.2 更新模式
1. **保存用户**: save(User)
2. **删除用户**: deleteById(personId)

### 3.3 复杂查询
1. **多条件组合查询**: 支持按租户、部门、关键字等多条件组合查询
2. **分页支持**: 支持分页参数(current, size)

## 4. 数据一致性分析

### 4.1 聚合根边界
- User聚合根包含UserProfile、UserAccount、ContactInfo等实体和值对象
- 聚合根确保内部数据的一致性
- 跨聚合根的操作通过领域事件实现最终一致性

### 4.2 事务边界
- 单个User聚合根的操作在一个事务内完成
- 跨聚合根的操作通过事件驱动实现最终一致性

## 5. 性能优化建议

### 5.1 查询优化
1. **索引优化**: 
   - personId字段建立主键索引
   - username字段建立唯一索引
   - tenantId、deptId字段建立普通索引
   - 创建复合索引支持分页查询

2. **查询分离**: 
   - 读写分离，查询操作可路由到只读数据库
   - 复杂查询可考虑使用物化视图

### 5.2 缓存策略
1. **热点数据缓存**: 
   - 用户基本信息缓存
   - 用户账户信息缓存

2. **查询结果缓存**: 
   - 分页查询结果缓存
   - 部门用户列表缓存

## 6. 数据模型验证
- [x] 聚合根设计合理，边界清晰
- [x] 实体和值对象职责明确
- [x] 持久化模型与领域模型映射关系清晰
- [x] 数据访问模式满足业务需求
- [x] 数据一致性得到保障