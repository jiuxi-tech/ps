# 登录失败邮件通知功能设计

## 一、需求背景

当用户登录时因密码输入错误次数超过系统设定阈值（默认5次）导致账户被锁定时，需要向用户关联的邮箱发送邮件通知，告知用户账户被锁定的原因、锁定时长以及后续处理方式。

## 二、业务目标

- 提升用户体验：及时通知用户账户异常情况
- 增强安全性：提醒用户可能存在的账户安全风险
- 降低运维成本：减少用户因不知账户被锁定而重复尝试登录或联系客服的情况

## 三、功能范围

### 3.1 功能边界

**包含功能：**
- 密码错误次数达到锁定阈值时触发邮件通知
- 向用户注册邮箱发送账户锁定通知邮件
- 邮件内容包含锁定原因、锁定时长、解锁方式等信息

**不包含功能：**
- 短信通知（本次仅实现邮件通知）
- 站内消息推送
- 账户解锁功能（复用现有机制）
- 邮件模板管理界面（使用固定模板）

### 3.2 约束条件

- 复用现有的账户锁定机制（AccountLockoutProperties配置）
- 复用现有的邮件服务（EmailService）
- 不改变现有登录验证流程，仅增加通知环节
- 邮件发送失败不影响账户锁定功能的执行

## 四、核心业务流程

### 4.1 账户锁定邮件通知流程

```
flowchart TD
    A[用户输入密码登录] --> B{密码是否正确?}
    B -->|正确| C[登录成功,重置错误次数]
    B -->|错误| D[累加错误次数]
    D --> E{错误次数是否达到阈值?}
    E -->|未达到| F[返回错误提示]
    E -->|达到| G[锁定账户]
    G --> H{用户是否有邮箱?}
    H -->|无| I[记录日志:无邮箱]
    H -->|有| J[发送锁定通知邮件]
    J --> K{邮件是否发送成功?}
    K -->|成功| L[记录日志:邮件发送成功]
    K -->|失败| M[记录日志:邮件发送失败]
    I --> N[返回账户锁定提示]
    L --> N
    M --> N
```

### 4.2 邮件发送触发时机

| 场景 | 触发条件 | 是否发送邮件 | 备注 |
|------|----------|-------------|------|
| 首次锁定 | 错误次数从4次增加到5次（达到maxAttempts） | 是 | 主要场景 |
| 锁定期内再次尝试 | 账户已锁定且在锁定时长内 | 否 | 避免邮件轰炸 |
| 锁定期满后再次错误 | 锁定期满，再次输错密码达到阈值 | 是 | 视为新一轮锁定 |
| 累计达到永久锁定 | 累计错误次数达到maxTotalAttempts | 是 | 邮件内容需标注"永久锁定" |

## 五、关键数据结构

### 5.1 涉及的数据表

#### 5.1.1 tp_account（账户表）
| 字段名 | 类型 | 说明 | 取值范围 |
|--------|------|------|----------|
| account_id | String | 账户ID（主键） | - |
| username | String | 用户名 | - |
| phone | String | 手机号 | - |
| locked | Integer | 是否锁定 | 0-未锁定, 1-已锁定 |
| person_id | String | 关联人员ID | - |

#### 5.1.2 tp_account_exinfo（账户扩展信息表）
| 字段名 | 类型 | 说明 | 取值范围 |
|--------|------|------|----------|
| account_id | String | 账户ID（主键） | - |
| err_count | Integer | 密码错误次数 | 0-最大值 |
| last_err_time | String | 最后错误时间 | yyyyMMddHHmmss |
| last_login_time | String | 最后登录时间 | yyyyMMddHHmmss |

#### 5.1.3 tp_person_basicinfo（人员基本信息表）
| 字段名 | 类型 | 说明 | 取值范围 |
|--------|------|------|----------|
| person_id | String | 人员ID（主键） | - |
| person_name | String | 人员姓名 | - |
| email | String | 电子邮箱 | - |
| phone | String | 手机号 | - |

### 5.2 配置参数

#### 5.2.1 账户锁定配置（AccountLockoutProperties）
| 配置项 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| enabled | boolean | true | 是否启用账户锁定功能 |
| maxAttempts | int | 5 | 最大连续错误次数 |
| lockoutDuration | int | 30 | 锁定时长（分钟） |
| maxTotalAttempts | int | 30 | 累计最大错误次数 |
| lockoutType | String | temporary | 锁定类型：temporary-临时锁定，permanent-永久锁定 |

#### 5.2.2 邮件配置
| 配置项 | 类型 | 说明 |
|--------|------|------|
| spring.mail.host | String | SMTP服务器地址 |
| spring.mail.port | int | SMTP服务器端口 |
| spring.mail.username | String | 发件人邮箱 |
| spring.mail.password | String | 邮箱密码或授权码 |
| app.name | String | 应用名称（用于邮件标题） |

## 六、业务规则

### 6.1 邮件发送规则

| 规则编号 | 规则描述 | 实现方式 |
|---------|---------|---------|
| R1 | 仅在账户首次被锁定时发送邮件 | 检查错误次数是否恰好等于maxAttempts |
| R2 | **人员未配置邮箱时不发送邮件** | 查询人员信息表,检查email字段:如果为NULL、空字符串或仅包含空格,则跳过邮件发送,仅记录日志 |
| R3 | 邮件发送失败不影响账户锁定 | 邮件发送采用异步方式，失败时仅记录日志 |
| R4 | 临时锁定和永久锁定发送不同邮件内容 | 根据错误次数判断锁定类型，使用不同模板变量 |
| R5 | 邮件内容应包含锁定时间和解锁时间 | 计算解锁时间并传入邮件模板 |

### 6.2 锁定类型判定

| 锁定类型 | 判定条件 | 邮件内容差异 |
|---------|---------|-------------|
| 临时锁定 | err_count >= maxAttempts 且 err_count < maxTotalAttempts | 告知锁定时长，提示等待自动解锁 |
| 永久锁定 | err_count >= maxTotalAttempts | 告知需联系管理员解锁，不显示解锁时间 |

### 6.3 数据查询规则

| 查询目的 | 查询表 | 关联条件 | 必填字段 |
|---------|--------|---------|---------|
| 获取用户邮箱 | tp_person_basicinfo | person_id（从tp_account获取） | email |
| 获取锁定信息 | tp_account_exinfo | account_id | err_count, last_err_time |
| 获取账户状态 | tp_account | account_id | locked, person_id, username |

## 七、邮件内容设计

### 7.1 邮件主题

**格式：** `【{应用名称}】账户安全通知`

**示例：** `【九溪系统】账户安全通知`

### 7.2 邮件正文（临时锁定）

```
尊敬的用户 {用户名}，您好！

您的账户因连续输入错误密码已被临时锁定。

锁定详情：
- 错误次数：{错误次数} 次
- 锁定时间：{锁定时间}
- 预计解锁时间：{解锁时间}

安全提示：
如果这不是您本人的操作，您的账户可能存在安全风险，建议：
1. 等待系统自动解锁后，立即修改密码
2. 检查账户安全设置
3. 如有疑问，请联系系统管理员

此邮件由系统自动发送，请勿回复。

{应用名称}
{当前日期}
```

### 7.3 邮件正文（永久锁定）

```
尊敬的用户 {用户名}，您好！

您的账户因连续多次输入错误密码已被永久锁定。

锁定详情：
- 累计错误次数：{错误次数} 次
- 锁定时间：{锁定时间}

解锁说明：
账户已被永久锁定，无法自动解锁。请联系系统管理员进行手动解锁。

安全提示：
如果这不是您本人的操作，您的账户可能存在严重安全风险，请立即联系系统管理员处理。

此邮件由系统自动发送，请勿回复。

{应用名称}
{当前日期}
```

### 7.4 邮件模板变量

| 变量名 | 数据来源 | 格式示例 | 用途 |
|--------|---------|---------|------|
| userName | tp_account.username | zhangsan | 用户标识 |
| personName | tp_person_basicinfo.person_name | 张三 | 用户姓名（可选） |
| errCount | tp_account_exinfo.err_count | 5 | 错误次数 |
| lockTime | 当前时间 | 2025-01-15 14:30:00 | 锁定时间 |
| unlockTime | lockTime + lockoutDuration | 2025-01-15 15:00:00 | 解锁时间（仅临时锁定） |
| maxAttempts | 配置项 | 5 | 锁定阈值 |
| lockoutDuration | 配置项 | 30 | 锁定时长（分钟） |
| appName | 配置项 | 九溪系统 | 应用名称 |

## 八、系统交互设计

### 8.1 组件职责划分

| 组件 | 职责 | 输入 | 输出 |
|------|------|------|------|
| AccountExinfoDBServiceImpl | 记录登录失败次数，触发锁定 | accountId, loginSuccess | 无 |
| AccountLockNotificationService | 判断是否需要发送邮件，组装邮件内容 | accountId | 邮件发送结果 |
| EmailService | 执行邮件发送 | to, subject, content | boolean（成功/失败） |
| PersonBasicinfoMapper | 查询用户邮箱 | personId | email |

### 8.2 调用流程

```
sequenceDiagram
    participant User as 用户
    participant Login as 登录服务
    participant Exinfo as AccountExinfoDBServiceImpl
    participant Notify as AccountLockNotificationService
    participant Person as PersonBasicinfoMapper
    participant Email as EmailService

    User->>Login: 输入密码登录
    Login->>Exinfo: 验证密码并记录结果
    
    alt 密码错误
        Exinfo->>Exinfo: 累加错误次数
        
        alt 达到锁定阈值
            Exinfo->>Exinfo: 锁定账户
            Exinfo->>Notify: 触发锁定通知(accountId)
            Notify->>Notify: 查询账户信息
            Notify->>Person: 查询用户邮箱(personId)
            Person-->>Notify: 返回邮箱
            
            alt 邮箱存在
                Notify->>Notify: 组装邮件内容
                Notify->>Email: 异步发送邮件
                Email-->>Notify: 返回发送结果
                Notify->>Notify: 记录发送日志
            else 邮箱不存在
                Notify->>Notify: 记录日志：用户无邮箱
            end
            
            Notify-->>Exinfo: 通知完成
        end
        
        Exinfo-->>Login: 返回错误信息
        Login-->>User: 返回账户锁定提示
    end
```

### 8.3 异常处理策略

| 异常场景 | 处理方式 | 影响范围 |
|---------|---------|---------|
| **人员未配置邮箱** | **记录INFO级别日志,优雅跳过发送,这是正常业务场景,不是异常** | 仅邮件通知功能 |
| 邮箱格式无效 | 记录WARN日志，跳过发送 | 仅邮件通知功能 |
| 邮件服务未配置 | 记录WARN日志，跳过发送 | 仅邮件通知功能 |
| 邮件发送失败 | 记录ERROR日志，不重试 | 仅邮件通知功能 |
| 查询用户信息失败 | 记录ERROR日志，跳过发送 | 仅邮件通知功能 |
| 配置参数异常 | 使用默认值，记录WARN | 可能影响通知内容准确性 |

**异常处理原则：** 所有邮件通知相关异常都不应影响账户锁定功能的正常执行。

## 九、技术实现要点

### 9.1 核心服务接口设计

#### 9.1.1 AccountLockNotificationService（账户锁定通知服务）

**服务职责：** 处理账户锁定时的邮件通知逻辑

**核心方法：**

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| notifyAccountLocked | accountId: String | void | 发送账户锁定通知 |
| shouldSendNotification | accountId: String, errCount: Integer | boolean | 判断是否需要发送通知 |
| buildEmailContent | lockInfo: AccountLockInfo | EmailContent | 构建邮件内容 |

**方法详细说明：**

**notifyAccountLocked(accountId)**
- 功能：触发账户锁定邮件通知
- 前置条件：账户已被锁定
- 处理流程：
  1. 查询账户信息（username, personId, locked状态）
  2. 查询锁定详情（errCount, lastErrTime）
  3. 判断是否需要发送通知（shouldSendNotification）
  4. 查询用户邮箱地址
  5. **验证邮箱有效性：如果email为NULL、空字符串或仅包含空格，记录日志后直接返回，不再执行后续步骤**
  6. 组装邮件内容（buildEmailContent）
  7. 异步调用邮件服务发送邮件
  8. 记录发送日志
- 异常处理：所有异常仅记录日志，不抛出

**shouldSendNotification(accountId, errCount)**
- 功能：判断是否需要发送邮件通知
- 判断逻辑：
  1. 错误次数是否恰好等于maxAttempts（首次锁定）
  2. 或者错误次数是否恰好等于maxTotalAttempts（永久锁定）
- 返回值：true-需要发送，false-不发送

**buildEmailContent(lockInfo)**
- 功能：根据锁定信息构建邮件内容
- 输入参数对象AccountLockInfo包含：
  - username：用户名
  - personName：人员姓名（可选）
  - errCount：错误次数
  - lastErrTime：最后错误时间
  - lockoutType：锁定类型（temporary/permanent）
  - lockoutDuration：锁定时长（分钟）
  - maxAttempts：锁定阈值
- 输出对象EmailContent包含：
  - subject：邮件主题
  - content：邮件正文
  - isHtml：是否HTML格式

### 9.2 集成点设计

#### 9.2.1 在AccountExinfoDBServiceImpl中集成

**集成位置：** saveOrUpdate方法，锁定账户之后

**集成方式：**

在现有锁定逻辑后增加通知调用：

- 原有逻辑位置：errCount >= maxAttempts - 1 时执行锁定
- 集成代码位置：执行锁定SQL后，立即调用通知服务
- 调用方式：异步调用，使用注解@Async或显式提交到线程池

**伪代码示例：**

```
方法 saveOrUpdate(accountId, now, errCount):
    更新错误次数到数据库
    
    如果 errCount >= maxAttempts - 1:
        执行锁定账户SQL
        记录日志：账户已锁定
        
        // 新增：触发邮件通知
        调用 accountLockNotificationService.notifyAccountLocked(accountId)
```

#### 9.2.2 邮件服务选择

**优先使用：** commonEmailService（通用邮件服务）

**服务Bean名称：** `commonEmailService`

**调用方法：** `sendSimpleEmail(to, subject, content)` 或 `sendHtmlEmail(to, subject, htmlContent)`

### 9.3 数据查询策略

#### 9.3.1 用户邮箱查询

**查询路径：** accountId → personId → email

**SQL查询语句（伪代码）：**

```
查询步骤一：根据accountId查询personId
SELECT person_id, username 
FROM tp_account 
WHERE account_id = #{accountId} 
  AND actived = 1

查询步骤二：根据personId查询email
SELECT email, person_name 
FROM tp_person_basicinfo 
WHERE person_id = #{personId} 
  AND actived = 1
```

**优化建议：** 使用JOIN一次性查询所需信息

```
优化查询（一次性获取所有信息）：
SELECT a.username, a.person_id, p.email, p.person_name
FROM tp_account a
LEFT JOIN tp_person_basicinfo p ON a.person_id = p.person_id
WHERE a.account_id = #{accountId}
  AND a.actived = 1
  AND p.actived = 1
```

**邮箱有效性判断逻辑：**

查询到email字段后，需进行如下判断：
- email == null → 无邮箱，不发送
- email.trim().isEmpty() → 空字符串或仅空格，不发送
- 否则 → 邮箱有效，可发送

#### 9.3.2 锁定信息查询

**查询表：** tp_account_exinfo

**查询SQL（伪代码）：**

```
SELECT err_count, last_err_time
FROM tp_account_exinfo
WHERE account_id = #{accountId}
```

### 9.4 异步处理设计

**异步方式：** 使用Spring的@Async注解

**线程池配置：**

| 配置项 | 建议值 | 说明 |
|--------|--------|------|
| corePoolSize | 2 | 核心线程数 |
| maxPoolSize | 5 | 最大线程数 |
| queueCapacity | 100 | 队列容量 |
| threadNamePrefix | account-lock-notify- | 线程名前缀 |

**使用方式：**

在AccountLockNotificationService的notifyAccountLocked方法上添加@Async注解，确保邮件发送不阻塞登录验证流程。

### 9.5 日志记录规范

#### 9.5.1 日志级别

| 场景 | 日志级别 | 示例 |
|------|---------|------|
| 邮件发送成功 | INFO | 账户锁定通知邮件发送成功: accountId={}, email={} |
| **人员未配置邮箱** | **INFO** | **账户锁定通知跳过: 人员未配置邮箱, accountId={}, username={}** |
| 邮箱格式无效 | WARN | 账户锁定通知跳过: 邮箱格式无效, accountId={}, email={} |
| 邮件服务未配置 | WARN | 账户锁定通知跳过: 邮件服务未配置, accountId={} |
| 邮件发送失败 | ERROR | 账户锁定通知邮件发送失败: accountId={}, email={}, error={} |
| 查询用户信息失败 | ERROR | 查询用户邮箱失败: accountId={}, error={} |
| 邮件内容组装失败 | ERROR | 组装邮件内容失败: accountId={}, error={} |

#### 9.5.2 日志内容要求

- 必须包含accountId，便于问题追踪
- 对于发送成功的日志，包含目标邮箱（脱敏处理：zh***@163.com）
- 对于异常日志，包含异常信息和堆栈跟踪
- 记录关键决策点，如"是否发送通知"的判断结果

## 十、国际化支持

### 10.1 国际化资源文件

**文件位置：** `src/main/resources/i18n/`

**新增消息Key：**

| Key | 中文（messages_zh.properties） | 英文（messages_en.properties） |
|-----|-------------------------------|-------------------------------|
| account.lockout.email.subject | 账户安全通知 | Account Security Notice |
| account.lockout.email.greeting | 尊敬的用户 {0}，您好！ | Dear user {0}, |
| account.lockout.email.temporary.body | 您的账户因连续输入错误密码已被临时锁定。 | Your account has been temporarily locked due to consecutive password failures. |
| account.lockout.email.permanent.body | 您的账户因连续多次输入错误密码已被永久锁定。 | Your account has been permanently locked due to multiple consecutive password failures. |
| account.lockout.email.unlock.time | 预计解锁时间：{0} | Expected unlock time: {0} |
| account.lockout.email.contact.admin | 请联系系统管理员进行手动解锁。 | Please contact the system administrator for manual unlock. |
| account.lockout.email.security.tip | 如果这不是您本人的操作，您的账户可能存在安全风险。 | If this was not you, your account may be at security risk. |

### 10.2 消息格式化

使用MessageSource进行消息格式化，支持占位符替换。

**使用示例（伪代码）：**

```
获取国际化消息：
subject = messageSource.getMessage("account.lockout.email.subject", null, locale)
greeting = messageSource.getMessage("account.lockout.email.greeting", 
                                    new Object[]{userName}, locale)
```

## 十一、配置管理

### 11.1 邮件通知开关配置

**配置项：** `topinfo.security.authentication.account-lockout.notification.enabled`

**配置说明：**

| 配置项 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| enabled | boolean | true | 是否启用账户锁定邮件通知 |

**配置示例（application.yml）：**

```yaml
topinfo:
  security:
    authentication:
      account-lockout:
        enabled: true
        max-attempts: 5
        lockout-duration: 30
        max-total-attempts: 30
        lockout-type: temporary
        notification:
          enabled: true
```

### 11.2 配置优先级

1. 如果account-lockout.enabled为false，则不触发邮件通知
2. 如果notification.enabled为false，则跳过邮件通知（账户仍会锁定）
3. 如果邮件服务未配置（spring.mail.*），邮件通知自动降级为日志记录

## 十二、性能考虑

### 12.1 性能指标

| 指标 | 目标值 | 说明 |
|------|--------|------|
| 通知触发耗时 | < 50ms | 从锁定到调用通知服务的耗时 |
| 邮件发送耗时 | < 3s | 邮件实际发送时间（异步执行，不影响登录响应） |
| 数据库查询次数 | ≤ 2次 | 查询用户信息和锁定信息 |

### 12.2 性能优化措施

| 优化措施 | 说明 | 预期收益 |
|---------|------|---------|
| 异步发送邮件 | 使用@Async注解 | 不阻塞登录流程，响应时间减少90% |
| SQL查询优化 | 使用JOIN合并查询 | 减少数据库交互次数，查询时间减少50% |
| 邮件内容缓存 | 缓存邮件模板 | 减少模板解析时间 |
| 连接池复用 | 复用SMTP连接 | 减少连接建立耗时 |

## 十三、安全考虑

### 13.1 安全风险

| 风险 | 影响 | 防护措施 |
|------|------|---------|
| 邮箱信息泄露 | 低 | 日志中邮箱脱敏处理 |
| 邮件轰炸 | 中 | 同一账户仅在首次锁定时发送，锁定期内不重复发送 |
| 邮件伪造 | 中 | 使用官方SMTP服务器，配置SPF/DKIM |
| 敏感信息泄露 | 低 | 邮件内容不包含密码、密保问题等敏感信息 |

### 13.2 隐私保护

- 邮件内容不包含完整身份证号、密码等敏感信息
- 日志中邮箱地址脱敏：仅显示前2位和@后缀（如：zh***@163.com）
- 邮件仅发送到用户注册邮箱，不支持自定义邮箱地址

## 十四、测试要点

### 14.1 功能测试场景

| 测试场景 | 前置条件 | 操作步骤 | 预期结果 |
|---------|---------|---------|---------|
| 临时锁定邮件发送 | 人员已配置有效邮箱，错误次数为4次 | 再次输入错误密码 | 账户被锁定，收到临时锁定邮件 |
| 永久锁定邮件发送 | 人员已配置有效邮箱，错误次数为29次 | 再次输入错误密码 | 账户被永久锁定，收到永久锁定邮件 |
| **人员未配置邮箱(NULL)** | **email字段为NULL，错误次数为4次** | **再次输入错误密码** | **账户被锁定，不发送邮件，记录INFO日志** |
| **人员邮箱为空字符串** | **email字段为空字符串，错误次数为4次** | **再次输入错误密码** | **账户被锁定，不发送邮件，记录INFO日志** |
| **人员邮箱仅为空格** | **email字段为"   "，错误次数为4次** | **再次输入错误密码** | **账户被锁定，不发送邮件，记录INFO日志** |
| 邮箱格式无效 | email格式错误(如"invalid-email"),错误次数为4次 | 再次输入错误密码 | 账户被锁定，不发送邮件，记录WARN日志 |
| 邮件服务未配置 | 未配置spring.mail.* | 触发账户锁定 | 账户被锁定，不发送邮件，记录WARN日志 |
| 锁定期内再次尝试 | 账户已锁定，在锁定时长内 | 再次输入密码 | 不发送邮件 |
| 锁定期满后再次锁定 | 锁定期满，错误次数为4次 | 再次输入错误密码 | 账户再次被锁定，收到新的锁定邮件 |

### 14.2 性能测试场景

| 测试场景 | 测试指标 | 预期结果 |
|---------|---------|---------|
| 登录响应时间影响 | 登录接口响应时间 | 增加≤50ms |
| 并发锁定场景 | 100个账户同时被锁定 | 邮件正常发送，系统无异常 |
| 邮件服务故障 | 邮件服务器不可用 | 不影响账户锁定功能 |

### 14.3 安全测试场景

| 测试场景 | 测试方法 | 预期结果 |
|---------|---------|---------|
| 邮件轰炸防护 | 同一账户连续触发锁定 | 仅首次发送邮件 |
| 邮箱信息泄露 | 检查日志内容 | 邮箱地址已脱敏 |
| 邮件内容安全 | 检查邮件内容 | 不包含敏感信息 |

## 十五、部署说明

### 15.1 环境配置检查清单

| 检查项 | 检查内容 | 必需 |
|--------|---------|------|
| 邮件服务配置 | spring.mail.* 配置项是否完整 | 是 |
| SMTP服务器连通性 | 能否连接到SMTP服务器 | 是 |
| 账户锁定配置 | account-lockout配置是否正确 | 是 |
| 数据库表结构 | tp_person_basicinfo.email字段是否存在 | 是 |
| 异步线程池配置 | 线程池配置是否合理 | 建议配置 |

### 15.2 配置项验证

**开发环境示例配置：**

```
spring:
  mail:
    host: smtp.163.com
    port: 465
    username: example@163.com
    password: your_password_or_auth_code
    properties:
      mail:
        smtp:
          auth: true
          ssl:
            enable: true

topinfo:
  security:
    authentication:
      account-lockout:
        enabled: true
        max-attempts: 5
        lockout-duration: 30
        notification:
          enabled: true
```

### 15.3 部署步骤

1. 确认邮件服务配置正确（测试发送邮件功能）
2. 确认数据库表结构完整（检查email字段）
3. 部署新版本代码
4. 验证账户锁定功能正常
5. 触发测试账户锁定，验证邮件发送
6. 检查日志，确认无异常
7. 监控邮件发送成功率

### 15.4 回滚方案

如果邮件通知功能出现问题：

1. 通过配置项关闭邮件通知：`notification.enabled=false`
2. 账户锁定功能仍正常工作
3. 修复问题后重新启用

## 十六、运维监控

### 16.1 监控指标

| 指标名称 | 指标说明 | 告警阈值 |
|---------|---------|---------|
| 邮件发送成功率 | 发送成功次数 / 发送总次数 | < 90% |
| 邮件发送失败次数 | 单位时间内发送失败的次数 | > 10次/小时 |
| 用户无邮箱比例 | 触发通知但无邮箱的用户比例 | > 30% |
| 邮件发送平均耗时 | 邮件发送的平均耗时 | > 5秒 |

### 16.2 日志查询

**查询成功发送的邮件：**
```
grep "账户锁定通知邮件发送成功" application.log
```

**查询发送失败的邮件：**
```
grep "账户锁定通知邮件发送失败" application.log
```

**查询未配置邮箱的用户：**
```
grep "人员未配置邮箱" application.log
```

### 16.3 问题排查清单

| 问题现象 | 可能原因 | 排查步骤 |
|---------|---------|---------|
| 邮件未收到 | 1. **人员未配置邮箱**<br>2. 邮箱地址错误<br>3. 邮件被拦截<br>4. 邮件服务配置错误 | 1. **检查数据库tp_person_basicinfo.email字段是否为NULL或空**<br>2. 查看日志确认是否有"人员未配置邮箱"提示<br>3. 检查邮件服务连通性<br>4. 验证邮箱格式是否正确 |
| 邮件发送失败 | 1. SMTP服务器不可用<br>2. 认证失败<br>3. 网络问题 | 1. 检查SMTP服务器状态<br>2. 验证用户名密码<br>3. 检查网络连接 |
| 邮件内容错误 | 1. 模板错误<br>2. 数据查询错误 | 1. 检查邮件内容组装逻辑<br>2. 检查数据库数据 |

## 十七、后续优化方向

### 17.1 功能增强

| 优化项 | 优先级 | 说明 |
|--------|--------|------|
| 邮件模板管理 | 低 | 支持在管理界面配置邮件模板 |
| 短信通知 | 中 | 增加短信通知渠道 |
| 站内消息 | 中 | 增加站内消息推送 |
| 通知历史记录 | 低 | 记录所有通知发送历史 |
| 邮件重试机制 | 中 | 发送失败时支持重试 |

### 17.2 性能优化

| 优化项 | 优先级 | 说明 |
|--------|--------|------|
| 邮件批量发送 | 低 | 合并发送，减少SMTP连接 |
| 邮件队列化 | 中 | 使用消息队列解耦 |
| 模板缓存 | 中 | 缓存邮件模板，减少解析时间 |

### 17.3 体验优化

| 优化项 | 优先级 | 说明 |
|--------|--------|------|
| HTML邮件模板 | 中 | 使用HTML模板提升邮件美观度 |
| 多语言支持 | 低 | 根据用户语言偏好发送不同语言邮件 |
| 个性化内容 | 低 | 根据用户信息定制邮件内容 |
