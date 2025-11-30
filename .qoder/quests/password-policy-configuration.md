# 密码策略管理配置化设计文档

## 一、需求概述

### 1.1 需求背景
根据功能缺失分析，需要对以下密码策略管理功能进行梳理和配置化改造：
- 配置密码长度、字符类型组合、禁止弱密码
- 配置密码错误次数上限及锁定时间

### 1.2 需求目标
分析现有密码策略实现情况，对硬编码的策略值进行配置化改造，使其可通过YAML配置文件进行灵活配置和调整。

## 二、现状分析

### 2.1 密码长度与字符类型验证

#### 2.1.1 已实现的功能

**硬编码位置汇总：**

| 配置项 | 当前位置 | 当前值 | 说明 |
|--------|---------|--------|------|
| 密码最小长度 | SecurityConstants.Password.MIN_LENGTH | 6 | 常量定义 |
| 密码最大长度 | SecurityConstants.Password.MAX_LENGTH | 20 | 常量定义 |
| 密码验证最小长度 | ValidationUtil.validatePassword | 6 | 业务逻辑硬编码 |
| 密码强度检查最小长度 | EncryptUtil.isStrongPassword | 8 | 工具类硬编码 |
| 密码强度评分最小长度 | EncryptUtil.getPasswordStrength | 6 | 工具类硬编码 |
| 用户创建DTO验证 | UserCreateDTO | 6-20 | 注解硬编码 |
| 修改密码DTO验证 | ChangePasswordRequest | 6-20 | 注解硬编码 |
| 密码正则表达式 | ApiConstants.REGEX_PASSWORD | ^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$ | 常量硬编码 |
| 密码复杂度正则 | Authentication.regular | ^(?=.*\\d)(?=.*[a-z])(?=.*[~!@#$%^&*])[\\da-zA-Z~!@#$%^&*]{8,}$ | 配置类默认值 |
| 默认密码正则 | PwdRegularUtils.DDFAULT_REGULAR | ^(?![0-9]+$)(?![a-zA-Z]+$)(?![\\W]+$)[a-zA-Z0-9\\W]{8,}$ | 工具类硬编码 |

**验证提示消息：**
- 国际化资源文件已配置：
  - `validation.password.weak`: 密码强度不够，必须包含大小写字母和数字，长度至少8位
  - `validation.password.length`: 密码长度必须在{min}到{max}位之间

#### 2.1.2 存在的问题

1. **密码长度限制分散且不一致**
   - 最小长度在不同位置有6位和8位两种标准
   - 缺乏统一的配置源

2. **密码复杂度要求硬编码**
   - 存在多个不同的正则表达式，标准不统一
   - 无法根据安全等级要求灵活调整
   - 正则表达式分散在多个类中

3. **弱密码检测规则固定**
   - 字符类型要求（大写、小写、数字、特殊字符）硬编码
   - 无法配置是否必须包含特定字符类型
   - 缺少常见弱密码黑名单机制

### 2.2 密码错误次数与账户锁定

#### 2.2.1 已实现的功能

**硬编码位置汇总：**

| 配置项 | 当前位置 | 当前值 | 说明 |
|--------|---------|--------|------|
| 密码最大重试次数 | SecurityConstants.Password.MAX_RETRY_COUNT | 5 | 常量定义 |
| 账户锁定时间（分钟） | SecurityConstants.Password.LOCK_TIME_MINUTES | 30 | 常量定义 |
| 密码错误次数限制 | Authentication.errCount | 5 | 配置类默认值 |
| 自动解锁时间（分钟） | Authentication.deblocking | 30 | 配置类默认值 |
| 最大累计错误次数 | Authentication.maxErrCount | 30 | 配置类默认值 |

**实现机制：**
- 数据库表：`tp_account_exinfo` 存储账户扩展信息
  - `err_count`: 密码错误次数记录
  - `last_err_time`: 最后一次错误时间
  - `last_login_time`: 最后一次登录时间
- 数据库表：`tp_account` 账户主表
  - `locked`: 账户锁定状态标识（1=锁定，0=未锁定）
- 业务逻辑：`PwdAccountServiceImpl.queryAccount`
  - 登录前检查账户是否被禁用或锁定
  - 密码验证失败时通过事件机制记录错误次数
  - 错误次数达到限制时自动锁定账户
  - 锁定30分钟后允许再次尝试
  - 累计错误次数超过30次需管理员解锁

**锁定策略：**
1. 连续错误5次：账户锁定30分钟
2. 30分钟内再次登录：提示剩余时间后重试
3. 30分钟外且累计错误次数小于30次：允许重新尝试
4. 累计错误次数超过30次：账户永久锁定，需管理员解锁

#### 2.2.2 存在的问题

1. **配置分散**
   - SecurityConstants常量类中有定义但未实际使用
   - Authentication配置类中有默认值
   - 两处定义值不一致，容易混淆

2. **缺少YAML配置映射**
   - Authentication配置类虽然存在配置属性，但未在YAML文件中明确配置
   - 依赖配置类的默认值，不够直观

3. **锁定策略不够灵活**
   - 30分钟解锁时间和30次最大错误次数可配置，但未暴露到YAML
   - 缺少锁定策略类型选择（临时锁定、永久锁定、IP锁定等）

### 2.3 密码有效期管理

#### 2.3.1 已实现的功能

**配置位置：**
- 通过 `TpSystemConfigService` 动态配置服务读取
  - `password.validity.months`: 密码有效期（月数），默认3个月
  - `password.expiry.reminder.days`: 密码过期提醒天数，默认7天

**实现机制：**
- 数据库表：`tp_account`
  - `last_password_change_time`: 上次密码修改时间
- 业务逻辑：`PwdAccountServiceImpl.checkPasswordExpiry`
  - 登录时检查密码是否过期
  - 过期直接锁定账户，要求管理员重置
  - 在提醒期内强制引导用户修改密码

#### 2.3.2 优势
- 已实现配置化，通过系统配置表动态管理
- 无需修改代码即可调整密码有效期策略

## 三、配置化改造方案

### 3.1 配置结构设计

#### 3.1.1 YAML配置结构

在 `topinfo.security.authentication` 配置节点下增加密码策略配置：

```yaml
topinfo:
  security:
    authentication:
      # 密码策略配置
      password-policy:
        # 密码长度配置
        length:
          min: 8                    # 最小长度
          max: 20                   # 最大长度
        
        # 密码复杂度配置
        complexity:
          enabled: true             # 是否启用复杂度验证
          require-uppercase: true   # 是否必须包含大写字母
          require-lowercase: true   # 是否必须包含小写字母
          require-digit: true       # 是否必须包含数字
          require-special: false    # 是否必须包含特殊字符
          allowed-special-chars: "@$!%*?&" # 允许的特殊字符集
          custom-regex: ""          # 自定义正则表达式（优先级最高）
        
        # 弱密码检测
        weak-password:
          enabled: true             # 是否启用弱密码检测
          blacklist:                # 弱密码黑名单
            - "123456"
            - "password"
            - "admin123"
            - "12345678"
          check-username-similarity: true  # 是否检查与用户名相似度
          min-strength-level: 2     # 最低密码强度等级（1=弱，2=中，3=强）
      
      # 账户锁定策略配置
      account-lockout:
        enabled: true               # 是否启用账户锁定功能
        max-attempts: 5             # 最大连续错误次数
        lockout-duration: 30        # 锁定时长（分钟）
        max-total-attempts: 30      # 累计最大错误次数（超过需管理员解锁）
        reset-attempts-after: 60    # 错误次数重置时间（分钟，成功登录后重置）
        lockout-type: "temporary"   # 锁定类型：temporary=临时锁定, permanent=永久锁定
```

#### 3.1.2 不同环境配置示例

**开发环境配置（security-dev.yml）：**
```yaml
topinfo:
  security:
    authentication:
      password-policy:
        length:
          min: 6                    # 开发环境降低要求
          max: 20
        complexity:
          enabled: true
          require-uppercase: false  # 开发环境不强制大写
          require-lowercase: true
          require-digit: true
          require-special: false
        weak-password:
          enabled: true
          min-strength-level: 1     # 开发环境允许弱密码
      account-lockout:
        enabled: true
        max-attempts: 10            # 开发环境放宽限制
        lockout-duration: 10
```

**生产环境配置（security-prod.yml）：**
```yaml
topinfo:
  security:
    authentication:
      password-policy:
        length:
          min: 10                   # 生产环境更严格
          max: 32
        complexity:
          enabled: true
          require-uppercase: true
          require-lowercase: true
          require-digit: true
          require-special: true     # 生产环境强制特殊字符
        weak-password:
          enabled: true
          min-strength-level: 3     # 生产环境要求强密码
      account-lockout:
        enabled: true
        max-attempts: 3             # 生产环境更严格
        lockout-duration: 60
```

### 3.2 配置类设计

#### 3.2.1 密码策略配置类

```
PasswordPolicyProperties（新增配置类）
├── LengthConfig（长度配置）
│   ├── min: Integer
│   └── max: Integer
├── ComplexityConfig（复杂度配置）
│   ├── enabled: Boolean
│   ├── requireUppercase: Boolean
│   ├── requireLowercase: Boolean
│   ├── requireDigit: Boolean
│   ├── requireSpecial: Boolean
│   ├── allowedSpecialChars: String
│   └── customRegex: String
└── WeakPasswordConfig（弱密码检测配置）
    ├── enabled: Boolean
    ├── blacklist: List<String>
    ├── checkUsernameSimilarity: Boolean
    └── minStrengthLevel: Integer
```

#### 3.2.2 账户锁定策略配置类

```
AccountLockoutProperties（新增配置类）
├── enabled: Boolean
├── maxAttempts: Integer
├── lockoutDuration: Integer
├── maxTotalAttempts: Integer
├── resetAttemptsAfter: Integer
└── lockoutType: String
```

#### 3.2.3 配置类集成

修改 `Authentication` 配置类，集成新的配置对象：

```
Authentication（修改现有配置类）
├── excludePaths: String[]
├── passwordPolicy: PasswordPolicyProperties（新增）
├── accountLockout: AccountLockoutProperties（新增）
├── tokenTimeOut: int
├── checkLogoutToken: boolean
├── @Deprecated errCount: int（保留兼容性，标记废弃）
├── @Deprecated deblocking: int（保留兼容性，标记废弃）
├── @Deprecated maxErrCount: int（保留兼容性，标记废弃）
└── @Deprecated regular: String（保留兼容性，标记废弃）
```

### 3.3 配置验证服务设计

#### 3.3.1 密码验证服务接口

```
PasswordValidationService（新增服务接口）
├── validatePasswordLength(password: String): ValidationResult
├── validatePasswordComplexity(password: String): ValidationResult
├── checkWeakPassword(password: String, username: String): ValidationResult
├── validatePassword(password: String, username: String): ValidationResult
└── getPasswordStrengthLevel(password: String): Integer
```

#### 3.3.2 账户锁定服务接口

```
AccountLockoutService（新增服务接口）
├── recordLoginFailure(accountId: String): void
├── recordLoginSuccess(accountId: String): void
├── isAccountLocked(accountId: String): Boolean
├── getLockoutInfo(accountId: String): LockoutInfo
├── unlockAccount(accountId: String): void
└── shouldLockAccount(accountId: String, failureCount: Integer): Boolean
```

#### 3.3.3 验证结果数据模型

```
ValidationResult（验证结果）
├── valid: Boolean
├── errorCode: String
├── errorMessage: String
└── details: Map<String, Object>

LockoutInfo（锁定信息）
├── locked: Boolean
├── lockoutTime: LocalDateTime
├── unlockTime: LocalDateTime
├── failureCount: Integer
├── remainingAttempts: Integer
└── lockoutType: String
```

### 3.4 改造影响范围

#### 3.4.1 需要修改的代码文件

| 文件路径 | 改造内容 | 优先级 |
|---------|---------|--------|
| SecurityConstants.java | 标记密码相关常量为@Deprecated，引导使用配置 | 高 |
| Authentication.java | 集成新的配置对象，标记旧字段为废弃 | 高 |
| ValidationUtil.java | 使用配置化的密码策略替代硬编码 | 高 |
| EncryptUtil.java | 使用配置化的密码策略替代硬编码 | 高 |
| PwdRegularUtils.java | 使用配置化的正则表达式 | 高 |
| UserCreateDTO.java | 使用动态验证注解或改为编程式验证 | 中 |
| ChangePasswordRequest.java | 使用动态验证注解或改为编程式验证 | 中 |
| PwdAccountServiceImpl.java | 使用配置化的锁定策略 | 高 |
| AccountExinfoDBServiceImpl.java | 使用配置化的错误次数和锁定时间 | 高 |

#### 3.4.2 需要新增的代码文件

| 文件路径 | 说明 |
|---------|------|
| PasswordPolicyProperties.java | 密码策略配置类 |
| AccountLockoutProperties.java | 账户锁定策略配置类 |
| PasswordValidationService.java | 密码验证服务接口 |
| PasswordValidationServiceImpl.java | 密码验证服务实现 |
| AccountLockoutService.java | 账户锁定服务接口 |
| AccountLockoutServiceImpl.java | 账户锁定服务实现 |

### 3.5 兼容性策略

#### 3.5.1 向后兼容

- 保留 `Authentication` 配置类中的旧字段（errCount、deblocking、maxErrCount、regular）
- 标记为 `@Deprecated` 并添加注释说明迁移路径
- 提供配置迁移逻辑：如果新配置不存在，降级使用旧配置
- 在日志中输出警告信息，提示使用新配置

#### 3.5.2 迁移提示

```
配置迁移提示消息示例：
WARN - 检测到使用已废弃的配置项 'topinfo.security.authentication.errCount'，
       请迁移至 'topinfo.security.authentication.account-lockout.max-attempts'。
       旧配置将在下一个主版本中移除。
```

### 3.6 国际化消息补充

需要在国际化资源文件中补充以下消息：

| 消息键 | 中文 | 英文 |
|-------|------|------|
| validation.password.too-short | 密码长度不能少于{min}位 | Password length must be at least {min} characters |
| validation.password.too-long | 密码长度不能超过{max}位 | Password length cannot exceed {max} characters |
| validation.password.require-uppercase | 密码必须包含大写字母 | Password must contain uppercase letters |
| validation.password.require-lowercase | 密码必须包含小写字母 | Password must contain lowercase letters |
| validation.password.require-digit | 密码必须包含数字 | Password must contain digits |
| validation.password.require-special | 密码必须包含特殊字符 | Password must contain special characters |
| validation.password.in-blacklist | 密码过于简单，请使用更复杂的密码 | Password is too simple, please use a more complex password |
| validation.password.similar-username | 密码不能与用户名相似 | Password cannot be similar to username |
| account.lockout.temporary | 登录失败次数过多，账户已被锁定{duration}分钟 | Account locked for {duration} minutes due to too many login failures |
| account.lockout.permanent | 登录失败次数过多，账户已被永久锁定，请联系管理员 | Account permanently locked due to too many login failures, please contact administrator |

## 四、实施路线

### 4.1 实施阶段

#### 阶段一：配置结构搭建（1-2天）
1. 创建配置类：PasswordPolicyProperties、AccountLockoutProperties
2. 修改 Authentication 配置类集成新配置
3. 在 YAML 文件中添加配置示例
4. 编写配置验证单元测试

#### 阶段二：密码验证服务实现（2-3天）
1. 实现 PasswordValidationService 接口及实现类
2. 重构 ValidationUtil、EncryptUtil、PwdRegularUtils
3. 编写密码验证单元测试
4. 集成到登录和修改密码流程

#### 阶段三：账户锁定服务实现（2-3天）
1. 实现 AccountLockoutService 接口及实现类
2. 重构 PwdAccountServiceImpl、AccountExinfoDBServiceImpl
3. 编写账户锁定单元测试
4. 集成到登录流程

#### 阶段四：国际化与文档（1天）
1. 补充国际化消息
2. 编写配置迁移文档
3. 更新系统配置说明文档

#### 阶段五：测试与上线（2-3天）
1. 功能测试（不同配置组合）
2. 兼容性测试（验证旧配置降级逻辑）
3. 性能测试（验证配置读取性能）
4. 生产环境配置评估与调整

### 4.2 风险评估

| 风险项 | 风险等级 | 应对措施 |
|-------|---------|---------|
| 旧代码依赖硬编码值 | 中 | 通过单元测试全面覆盖，保留兼容性逻辑 |
| 配置复杂度增加 | 低 | 提供合理的默认值，编写详细的配置文档 |
| 性能影响 | 低 | 配置启动时加载并缓存，避免运行时频繁读取 |
| 多环境配置不一致 | 中 | 提供配置检查工具，环境部署前验证配置 |

## 五、配置示例与说明

### 5.1 推荐配置

#### 5.1.1 标准安全配置（适用于生产环境）

```yaml
topinfo:
  security:
    authentication:
      password-policy:
        length:
          min: 10
          max: 32
        complexity:
          enabled: true
          require-uppercase: true
          require-lowercase: true
          require-digit: true
          require-special: true
          allowed-special-chars: "@$!%*?&#^"
        weak-password:
          enabled: true
          blacklist:
            - "123456"
            - "password"
            - "admin123"
            - "12345678"
            - "qwerty"
          check-username-similarity: true
          min-strength-level: 3
      account-lockout:
        enabled: true
        max-attempts: 3
        lockout-duration: 60
        max-total-attempts: 10
        lockout-type: "temporary"
```

#### 5.1.2 宽松配置（适用于开发/测试环境）

```yaml
topinfo:
  security:
    authentication:
      password-policy:
        length:
          min: 6
          max: 20
        complexity:
          enabled: true
          require-uppercase: false
          require-lowercase: true
          require-digit: true
          require-special: false
        weak-password:
          enabled: false
          min-strength-level: 1
      account-lockout:
        enabled: true
        max-attempts: 10
        lockout-duration: 5
```

### 5.2 配置说明

#### 5.2.1 密码长度配置说明
- `min`: 建议不低于8位，生产环境建议10位以上
- `max`: 建议不超过64位，平衡安全性与用户体验

#### 5.2.2 密码复杂度配置说明
- 生产环境建议同时启用大写、小写、数字和特殊字符要求
- 开发环境可适当放宽要求便于测试
- `custom-regex` 优先级最高，配置后将忽略其他复杂度规则

#### 5.2.3 弱密码检测配置说明
- `blacklist` 应包含常见弱密码，定期更新
- `check-username-similarity` 防止密码与用户名相同或相似
- `min-strength-level`: 1=弱（允许简单密码）、2=中（推荐）、3=强（高安全要求）

#### 5.2.4 账户锁定配置说明
- `max-attempts`: 连续错误次数限制，生产环境建议3-5次
- `lockout-duration`: 临时锁定时长，建议30-60分钟
- `max-total-attempts`: 累计错误次数上限，超过后需管理员介入
- `lockout-type`: temporary=自动解锁，permanent=需管理员解锁

## 六、总结

### 6.1 改造价值

1. **提升灵活性**：通过YAML配置文件灵活调整密码策略，无需修改代码
2. **增强安全性**：支持更细粒度的密码复杂度控制和弱密码检测
3. **改善运维**：不同环境可使用不同安全等级的配置
4. **提高可维护性**：统一配置源，消除硬编码分散问题

### 6.2 实施建议

1. **优先实施高优先级改造**：先完成核心验证逻辑的配置化
2. **保持兼容性**：确保旧配置降级逻辑正常工作
3. **充分测试**：覆盖各种配置组合和边界情况
4. **文档先行**：提前编写配置迁移指南和最佳实践文档

### 6.3 后续优化方向

1. 支持密码策略的运行时动态调整（通过管理后台）
2. 增加密码历史记录检查（禁止使用最近N次使用过的密码）
3. 支持基于IP的锁定策略（防止暴力破解）
4. 提供密码策略合规性报告（审计功能）
