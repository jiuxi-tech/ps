# PS-BE 项目配置文件分析与生产配置设计

## 一、项目背景

PS-BE（党校业务中台后端系统）是一个基于 Spring Boot 的企业级应用系统，采用分层配置架构管理不同环境的配置信息。当前系统版本为 2.2.2-SNAPSHOT，已部署开发环境和测试环境，现需基于开发配置创建一份完整的生产环境配置。

## 二、现有配置架构分析

### 2.1 配置文件层次结构

系统采用三层配置架构，配置优先级从高到低依次为：

| 层次 | 文件类型 | 作用 | 示例 |
|------|---------|------|------|
| 主配置层 | application.yml | 环境切换、配置导入控制 | 指定 active profile |
| 基础模板层 | application-base.yml | 全环境通用配置模板 | 定义配置属性结构 |
| 环境配置层 | application-{env}.yml | 环境特定配置覆盖 | dev/test/prod 配置 |
| 专项配置层 | database/cache/security-{env}.yml | 分类专项配置（可选） | 向后兼容的旧配置 |

### 2.2 配置目录结构

```
ps-be/src/main/resources/
├── application.yml                          # 主配置文件
├── config/
│   ├── application-base.yml                 # 基础配置模板
│   ├── env/                                 # 环境配置目录
│   │   ├── dev/
│   │   │   ├── application-dev.yml          # 开发环境基础配置
│   │   │   └── application-dev-enhanced.yml # 开发环境增强配置
│   │   ├── test/
│   │   │   └── application-test.yml         # 测试环境配置
│   │   └── prod/
│   │       └── application-prod.yml         # 生产环境配置（待完善）
│   ├── db/                                  # 数据库配置（向后兼容）
│   │   ├── database-dev.yml
│   │   ├── database-test.yml
│   │   └── database-prod.yml
│   ├── cache/                               # 缓存配置（向后兼容）
│   │   ├── cache-dev.yml
│   │   ├── cache-test.yml
│   │   └── cache-prod.yml
│   ├── sec/                                 # 安全配置（向后兼容）
│   │   ├── security-dev.yml
│   │   ├── security-test.yml
│   │   ├── security-prod.yml
│   │   └── security-audit-dev.yml
│   └── log/
│       └── logback-spring.xml               # 日志配置
```

### 2.3 配置命名空间设计

系统采用新旧配置并存的策略，逐步迁移至统一配置架构：

| 命名空间 | 配置类型 | 使用场景 | 状态 |
|---------|---------|---------|------|
| app.* | 新配置架构 | 统一配置管理 | 推荐使用 |
| topinfo.* | 旧配置架构 | MyBatis、安全等传统配置 | 向后兼容 |
| spring.* | Spring Boot 原生配置 | 框架基础配置 | 持续使用 |
| keycloak.* | Keycloak SSO 配置 | 单点登录集成 | 持续使用 |
| jiuxi.platform.* | 监控插件配置 | 系统监控 | 持续使用 |

## 三、开发环境配置分析

### 3.1 应用基础配置

#### 服务器配置

| 配置项 | 开发环境值 | 说明 |
|--------|-----------|------|
| 服务端口 | 8082 | Tomcat 监听端口 |
| 上下文路径 | /ps-be | 应用访问路径 |
| 最大线程数 | 1000 | Tomcat 最大工作线程 |
| 请求体大小限制 | 10MB | HTTP POST 数据最大值 |
| 请求头大小限制 | 10MB | HTTP Header 最大值 |
| 字符编码 | UTF-8 | URI 编码格式 |

#### 文件上传配置

| 配置项 | 值 | 说明 |
|--------|-----|------|
| 最大文件大小 | 50MB | 单个文件最大值 |
| 最大请求大小 | 100MB | 整个请求最大值 |
| 上传目录 | /upload | 文件存储目录 |
| 允许文件类型 | 图片、文档、压缩包等 | 白名单机制 |

### 3.2 数据库配置分析

#### 数据源配置

| 配置项 | 开发环境值 | 说明 |
|--------|-----------|------|
| 驱动类 | org.mariadb.jdbc.Driver | MariaDB JDBC 驱动 |
| 数据库地址 | jdbc:mariadb://alilaoba.cn:13307/ps-bmp | 开发数据库连接 |
| 用户名 | root | 数据库用户 |
| 密码 | $D8BZ8Qmav | 数据库密码（明文） |

#### 连接池配置

| 配置项 | 开发环境值 | 生产环境建议 | 说明 |
|--------|-----------|-------------|------|
| 初始连接数 | 5 | 20 | 应用启动时创建的连接数 |
| 最小空闲连接 | 5 | 20 | 保持的最小空闲连接数 |
| 最大活动连接 | 500 | 500 | 最大并发连接数 |
| 最大等待时间 | 默认 | 60000ms | 获取连接最大等待时间 |

### 3.3 缓存配置分析

#### Redis 配置

| 配置项 | 开发环境值 | 生产环境建议 | 说明 |
|--------|-----------|-------------|------|
| 主机地址 | localhost | 环境变量 | Redis 服务器地址 |
| 端口 | 6379 | 环境变量 | Redis 服务端口 |
| 密码 | 空 | 环境变量 | Redis 认证密码 |
| 数据库索引 | 0 | 环境变量 | Redis DB 编号 |
| 连接超时 | 3000ms | 10000ms | 连接建立超时时间 |
| 最大活动连接 | 8 | 100 | 连接池最大连接数 |

### 3.4 安全配置分析

#### 认证排除路径

开发环境排除路径（宽松）包括所有路径的临时开放，生产环境排除路径（严格）仅包含：
- /static/** - 静态资源
- /sys/captcha/** - 验证码接口
- /sys/file/preview-pdf - PDF 预览
- /api/health - 健康检查
- /actuator/health - Spring Actuator 健康检查

#### 密码策略配置

| 配置项 | 开发环境 | 生产环境建议 |
|--------|---------|-------------|
| 最小长度 | 6 | 8 |
| 必须包含大写字母 | false | true |
| 必须包含小写字母 | true | true |
| 必须包含数字 | true | true |
| 必须包含特殊字符 | false | true |
| 最小强度等级 | 1（弱） | 3（强） |

#### 账户锁定策略

| 配置项 | 开发环境 | 生产环境建议 |
|--------|---------|-------------|
| 最大错误次数 | 10 | 5 |
| 锁定时长 | 10分钟 | 30分钟 |
| 累计最大错误 | 30 | 30 |

### 3.5 日志配置分析

#### 开发环境日志级别

| 组件 | 日志级别 |
|------|---------|
| 根日志 | INFO |
| 业务代码 | DEBUG |
| MyBatis | DEBUG |
| Druid 连接池 | DEBUG |

#### 生产环境日志级别建议

| 组件 | 日志级别 |
|------|---------|
| 根日志 | WARN |
| 业务代码 | INFO |
| MyBatis | WARN |
| Druid 连接池 | WARN |

## 四、生产环境配置设计

### 4.1 生产环境配置原则

#### 安全性原则

敏感配置管理策略（根据实际部署场景选择）：

**方案A：直接在配置文件中使用明文（推荐用于外置配置场景）**
- 适用于 application-prod-enhanced.yml 作为外置配置文件的场景
- 通过文件权限控制保护敏感信息
- 配置文件不纳入版本控制
- 部署简单，维护成本低

**方案B：使用环境变量**
- 适用于需要配置文件纳入版本控制的场景
- 通过 env.sh 或 systemd EnvironmentFile 提供敏感值
- 配置与敏感值分离
- 需要额外维护环境变量文件

**方案C：使用密钥管理服务**
- 适用于高安全要求场景
- 使用 HashiCorp Vault、AWS Secrets Manager 等
- 提供加密、审计、自动轮换等高级功能
- 系统复杂度和维护成本显著增加

**其他安全原则：**
- 仅暴露必要的接口和端点
- 严格的认证排除路径控制
- 强制复杂密码策略
- 启用 SSL/TLS 加密传输

#### 性能原则

增大数据库连接池和 Redis 连接池，启用 HTTP 响应压缩，降低日志级别减少 IO 开销，增大初始连接数减少启动延迟。

#### 稳定性原则

支持集群部署避免单点故障，合理设置各类超时时间防止资源耗尽，配置熔断保护防止雪崩效应，暴露健康检查端点支持负载均衡，启用 Prometheus 监控及时发现问题。

#### 可维护性原则

统一使用环境变量便于配置管理，结构化日志输出便于日志分析，提供详细配置说明降低维护成本，配置文件纳入版本管理可追溯变更历史。

### 4.2 生产环境核心配置设计

#### 服务器配置

| 配置项 | 生产值 | 设计依据 |
|--------|-------|---------|
| 服务端口 | 8080 | 标准 HTTP 端口，便于 Nginx 反向代理 |
| 上下文路径 | /ps-be | 保持与开发环境一致 |
| 最大线程数 | 2000 | 提升并发处理能力 |
| 最大连接数 | 10000 | 支持高并发场景 |
| 连接超时 | 20000ms | 防止连接长时间挂起 |
| HTTP 响应压缩 | 启用 | 减少网络传输量 |
| 最小压缩大小 | 1024 字节 | 小于此值不压缩 |

#### 数据源配置

| 配置项 | 生产值 | 设计依据 |
|--------|-------|---------|
| 驱动类 | org.mariadb.jdbc.Driver | 保持一致 |
| 数据库地址 | ${DB_URL} | 环境变量，必须外部提供 |
| 用户名 | ${DB_USERNAME} | 环境变量，必须外部提供 |
| 密码 | ${DB_PASSWORD} | 环境变量，必须外部提供 |

#### 连接池配置

| 配置项 | 生产值 | 设计依据 |
|--------|-------|---------|
| 初始连接数 | 20 | 预分配连接，减少启动延迟 |
| 最小空闲连接 | 20 | 保持充足的空闲连接应对突发流量 |
| 最大活动连接 | 500 | 支持高并发 |
| 最大等待时间 | 60000ms | 防止无限等待 |

#### Druid 监控与安全配置

| 配置项 | 生产值 | 设计依据 |
|--------|-------|---------|
| 监控页面启用 | false | 生产环境禁用 Web 监控界面 |
| SQL 防火墙启用 | true | 防止 SQL 注入攻击 |
| 允许 DELETE | false | 禁止删除操作，通过业务逻辑控制 |

#### Redis 配置

| 配置项 | 生产值 | 设计依据 |
|--------|-------|---------|
| 主机地址 | ${REDIS_HOST} | 环境变量，必须外部提供 |
| 端口 | ${REDIS_PORT:6379} | 环境变量，默认 6379 |
| 密码 | ${REDIS_PASSWORD} | 环境变量，必须外部提供 |
| 连接超时 | 10000ms | 增加到10秒，应对网络波动 |
| 最大活动连接 | 100 | 大幅提升连接池容量 |

#### Redisson 配置

| 配置项 | 生产值 | 设计依据 |
|--------|-------|---------|
| 配置文件 | redisson-cluster.yml | 生产环境使用集群模式，支持高可用 |
| 集群节点 | ${REDIS_CLUSTER_NODES} | 环境变量，Redis 集群节点列表 |

#### 认证排除路径（严格控制）

生产环境仅排除以下路径：
- /static/**
- /sys/captcha/**
- /sys/file/preview-pdf
- /api/health
- /actuator/health

#### 密码策略配置（强化）

| 配置项 | 生产值 |
|--------|-------|
| 最小长度 | 8 |
| 必须包含大写字母 | true |
| 必须包含小写字母 | true |
| 必须包含数字 | true |
| 必须包含特殊字符 | true |
| 最小强度等级 | 3（强） |

#### 账户锁定策略（强化）

| 配置项 | 生产值 |
|--------|-------|
| 最大错误次数 | 5 |
| 锁定时长 | 30分钟 |
| 累计最大错误 | 30 |
| 锁定类型 | temporary |

#### Keycloak SSO 配置

| 配置项 | 生产值 |
|--------|-------|
| SSO 启用 | ${KEYCLOAK_SSO_ENABLED:true} |
| 服务器地址 | ${KEYCLOAK_SERVER_URL} |
| Realm | ${KEYCLOAK_REALM:ps-realm} |
| 客户端ID | ${KEYCLOAK_CLIENT_ID:ps-be} |
| 客户端密钥 | ${KEYCLOAK_CLIENT_SECRET} |
| 登录成功重定向 | ${FRONTEND_URL}/#/sso/login |
| JWT 验证受众 | true |

#### 文件服务配置

| 配置项 | 生产值 |
|--------|-------|
| 服务模式 | server |
| 服务器地址 | ${FILE_SERVER_HOST} |
| Netty 端口 | ${FILE_SERVER_NETTY_PORT:8877} |
| Tomcat 端口 | ${FILE_SERVER_TOMCAT_PORT:8080} |

#### JVM 参数配置建议

| 参数 | 生产值 |
|------|-------|
| 初始堆内存 | -Xms2048m |
| 最大堆内存 | -Xmx4096m |
| 垃圾收集器 | -XX:+UseG1GC |
| GC 暂停时间 | -XX:MaxGCPauseMillis=100 |
| 堆转储 | -XX:+HeapDumpOnOutOfMemoryError |

### 4.3 生产环境必需的环境变量清单

#### 数据库相关（必需）

- DB_URL：数据库连接地址
- DB_USERNAME：数据库用户名
- DB_PASSWORD：数据库密码

#### Redis 相关（必需）

- REDIS_HOST：Redis 主机地址
- REDIS_PASSWORD：Redis 密码
- REDIS_PORT：Redis 端口（可选，默认6379）
- REDIS_DATABASE：Redis 数据库索引（可选，默认0）

#### Keycloak SSO 相关（必需）

- KEYCLOAK_SERVER_URL：Keycloak 服务器地址
- KEYCLOAK_CLIENT_SECRET：客户端密钥
- KEYCLOAK_ADMIN_USERNAME：管理员用户名
- KEYCLOAK_ADMIN_PASSWORD：管理员密码
- FRONTEND_URL：前端应用地址

#### 文件服务相关（必需）

- FILE_SERVER_HOST：文件服务器地址

#### 邮件服务相关（必需）

- MAIL_HOST：SMTP 服务器地址
- MAIL_USERNAME：邮箱账号
- MAIL_PASSWORD：邮箱密码/授权码

#### 监控相关（必需）

- MONITOR_SERVER_URL：监控服务端点

## 五、生产环境配置文件结构设计

### 5.1 application-prod-enhanced.yml 结构

此文件基于新的统一配置架构（app.* 命名空间），提供生产环境的完整配置覆盖，包含以下主要部分：

#### 应用基础配置

配置应用名称、版本、描述、文件上传、国际化、监控等基础信息，使用环境变量配置监控服务端点和客户端标识。

#### 数据库配置

配置数据源连接信息（使用环境变量）、连接池参数（生产优化）、MyBatis 配置（禁用性能拦截器）、Druid 监控（禁用Web界面，启用SQL防火墙）。

#### 缓存配置

配置 Redis 连接信息（使用环境变量）、连接池参数（生产优化）、集群/哨兵模式支持、本地缓存（EhCache）、分布式锁、缓存策略（启用预热）。

#### 安全配置

配置基础安全开关、认证排除路径（严格控制）、密码策略（强化）、账户锁定策略（强化）、Keycloak SSO（使用环境变量）、JWT 配置、文件服务（使用服务器模式）、加密配置。

#### Spring Boot 集成配置

配置服务器参数（端口、线程、压缩）、数据源映射（引用 app.database 配置）、Redis 映射（引用 app.cache.redis 配置）、缓存配置、Redisson 配置。

#### 传统配置兼容

配置 topinfo.mybatis、topinfo.security、topinfo.jdfs、keycloak、jiuxi.platform 等命名空间，保持向后兼容。

#### 日志配置

配置生产环境日志级别（精简）、日志文件路径和格式、日志滚动和保留策略。

#### Actuator 监控

配置暴露端点（仅健康检查和 Prometheus）、健康详情（不显示）、Prometheus 指标导出。

### 5.2 向后兼容的专项配置文件

#### database-prod.yml

保持现有结构，配置 spring.datasource 和 topinfo.mybatis，使用环境变量配置敏感信息，启用 SQL 防火墙。

#### cache-prod.yml

保持现有结构，配置 spring.redis 和 spring.redisson，使用环境变量配置 Redis 连接信息，使用集群配置文件。

#### security-prod.yml

保持现有结构，配置 topinfo.security、topinfo.jdfs、keycloak、spring.mail，使用环境变量配置敏感信息，严格控制认证排除路径。

#### security-audit-prod.yml（新增）

配置生产环境的安全审计体系，包括审计功能开关、异步审计、保留天数、实时告警、事件过滤等，日志级别精简。

### 5.3 配置文件优先级和加载顺序

配置加载顺序（后加载的覆盖先加载的）：
1. application.yml（主配置，指定 prod profile）
2. application-base.yml（基础模板，当前被注释）
3. application-prod-enhanced.yml（新架构完整配置）
4. database-prod.yml（向后兼容，可选）
5. cache-prod.yml（向后兼容，可选）
6. security-prod.yml（向后兼容，可选）
7. security-audit-prod.yml（审计配置，可选）

推荐策略：优先使用 application-prod-enhanced.yml 作为唯一配置文件，向后兼容的专项配置文件仅在需要独立管理时使用。

## 六、生产环境配置文件部署策略

### 6.1 配置文件外置说明

**核心问题解答：生产环境 JAR 包是否只需要外置一个配置文件？**

**答案：是的，完全可以。**

#### 推荐方案：直接在外置配置文件中使用明文（简单高效）

**建议的配置文件名：**

| 文件名 | 优点 | 缺点 | 推荐度 |
|--------|------|------|--------|
| application-prod.yml | ✅ 简洁<br>✅ 符合 Spring Boot 规范 | ⚠️ 可能与 JAR 内配置同名 | ⭐⭐⭐⭐ |
| application.yml | ✅ 最简洁 | ⚠️ 容易混淆 | ⭐⭐ |
| prod.yml | ✅ 极简 | ❌ 需要指定配置名 | ⭐⭐⭐ |
| ps-prod.yml | ✅ 项目标识<br>✅ 简洁 | ⚠️ 需要指定配置名 | ⭐⭐⭐⭐⭐ |

**最佳选择：application-prod.yml**
- 符合 Spring Boot 命名规范
- 简洁易懂
- 自动加载（当 profile 为 prod 时）

**部署目录结构：**
```
/opt/ps/
├── ps-be.jar                    # 应用 JAR 包
├── config/
│   └── application-prod.yml     # 外置生产配置（简洁命名）
└── logs/                        # 日志目录
```

**application-prod.yml 直接包含所有配置值：**

```yaml
# 服务器配置
server:
  port: 8080
  servlet:
    context-path: /ps-be
  tomcat:
    max-threads: 2000
    max-connections: 10000
  compression:
    enabled: true

# 数据库配置（直接使用明文）
spring:
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://192.168.1.100:3306/ps-bmp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: ps_user
    password: YourActualDatabasePassword    # 直接写明文密码
    druid:
      initial-size: 20
      min-idle: 20
      max-active: 500
      # ... 其他配置

# Redis 配置（直接使用明文）
  redis:
    host: 192.168.1.101
    port: 6379
    password: YourActualRedisPassword      # 直接写明文密码
    database: 0
    timeout: 10000ms
    jedis:
      pool:
        max-active: 100

# Keycloak SSO 配置（直接使用明文）
keycloak:
  server-url: https://sso.example.com
  realm: ps-realm
  sso:
    enabled: true
    client-id: ps-be
    client-secret: YourActualClientSecret  # 直接写明文密钥
    redirect:
      success-url: https://app.example.com/#/sso/login
      error-url: https://app.example.com/#/login

# 邮件配置（直接使用明文）
spring:
  mail:
    host: smtp.example.com
    port: 587
    username: notify@example.com
    password: YourActualMailPassword       # 直接写明文密码

# ... 其他配置
```

**启动命令（非常简单）：**

```bash
java -jar /opt/ps/ps-be.jar \
  --spring.profiles.active=prod \
  --spring.config.additional-location=file:/opt/ps/config/ \
  -Xms2048m -Xmx4096m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=100 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/opt/ps/logs/heapdump.hprof \
  > /opt/ps/logs/ps-be.log 2>&1 &
```

**优点：**
- ✅ **部署极其简单**：不需要 env.sh，不需要环境变量
- ✅ **维护成本低**：只需维护一个配置文件
- ✅ **配置直观**：所有配置一目了然
- ✅ **不需要学习环境变量**：降低运维门槛
- ✅ **适合外置配置场景**：配置文件本身就是外置的，不会打包到 JAR 中

**安全性考虑：**

虽然是明文密码，但通过以下措施可以确保安全：

1. **严格的文件权限控制**：
   ```bash
   # 设置为仅所有者可读写
   chmod 600 /opt/ps/config/application-prod.yml
   chown root:root /opt/ps/config/application-prod.yml
   
   # 验证权限
   ls -l /opt/ps/config/application-prod.yml
   # 应显示：-rw------- 1 root root
   ```

2. **配置文件不纳入版本控制**：
   - 不将 application-prod.yml 提交到 Git
   - 在 .gitignore 中添加：`application-prod.yml` 或 `*-prod.yml`

3. **服务器访问控制**：
   - 仅允许授权运维人员访问服务器
   - 使用 SSH 密钥认证，禁用密码登录
   - 启用防火墙，限制 SSH 访问 IP

4. **定期更换密码**：
   - 每 3-6 个月更换一次数据库密码
   - 更换后修改配置文件并重启应用

5. **监控文件访问**：
   ```bash
   # 监控配置文件的访问日志
   auditctl -w /opt/ps/config/application-prod.yml -p rwa -k ps_config_access
   ```

**安全级别评估：**

对于中小型项目和可控的服务器环境，这种方案的安全性**完全足够**：

- ✅ 配置文件是外置的，不会打包到 JAR 中
- ✅ 文件权限 600 确保只有 root 可读
- ✅ 服务器本身有访问控制机制
- ✅ 相比 env.sh，没有实质安全差异（env.sh 也是明文）

**与 env.sh 方案的对比：**

| 特性 | 直接明文配置 | env.sh + 占位符 |
|------|---------------|------------------|
| 密码是否明文 | 是（配置文件中） | 是（env.sh 中） |
| 部署复杂度 | 低（一个文件） | 中（两个文件） |
| 启动复杂度 | 低（直接启动） | 中（需要 source） |
| 维护成本 | 低 | 中 |
| 配置与值分离 | 否 | 是 |
| 适合版本控制 | 否 | 是（配置文件） |
| 实际安全性 | **相同**（都是明文） | **相同**（都是明文） |

**结论：**

对于 **application-prod.yml 作为外置配置文件** 的场景：
- 直接使用明文密码是 **完全可行** 且 **更简单** 的方案
- 与 env.sh 相比，安全性没有实质差异（都是明文存储）
- 通过文件权限和服务器访问控制可以保证足够的安全性
- 只有当需要将配置文件纳入版本控制时，才需要使用 env.sh

#### 替代方案：使用更简洁的文件名

**如果想要更简洁的文件名，可以使用：**

**方案 1：ps-prod.yml（最推荐）**

```bash
/opt/ps/
├── ps-be.jar
├── config/
│   └── ps-prod.yml              # 项目名 + 环境，清晰简洁
└── logs/
```

启动命令：
```bash
java -jar ps-be.jar \
  --spring.profiles.active=prod \
  --spring.config.name=ps-prod \
  --spring.config.additional-location=file:/opt/ps/config/
```

**方案 2：prod.yml（极简）**

```bash
/opt/ps/
├── ps-be.jar
├── config/
│   └── prod.yml                 # 最简洁
└── logs/
```

启动命令：
```bash
java -jar ps-be.jar \
  --spring.profiles.active=prod \
  --spring.config.name=prod \
  --spring.config.additional-location=file:/opt/ps/config/
```

**文件名选择建议：**

| 文件名 | 命令行复杂度 | 可读性 | 推荐场景 |
|--------|------------|------|----------|
| application-prod.yml | 低（自动加载） | 好 | 通用场景 |
| ps-prod.yml | 中（需要 config.name） | 很好 | 多项目部署 |
| prod.yml | 中（需要 config.name） | 一般 | 个人喜好 |

**最终推荐：**
- 如果追求简单：使用 `application-prod.yml`（无需额外参数）
- 如果追求简洁：使用 `ps-prod.yml`（有项目标识）

#### 方案一：仅外置 application-prod-enhanced.yml（推荐）

**可行性：** 完全可行

**前提条件：**
1. JAR 包内必须包含以下文件：
   - application.yml（主配置文件）
   - config/application-base.yml（基础模板，如果启用）
   - config/log/logback-spring.xml（日志配置）

2. 外置 application-prod-enhanced.yml 必须包含：
   - 所有生产环境特定配置
   - Spring Boot 原生配置（server、spring.datasource、spring.redis 等）
   - 新架构配置（app.* 命名空间）
   - 向后兼容配置（topinfo.*、keycloak.*、jiuxi.platform.* 等）

**配置加载机制：**
```
Spring Boot 配置加载顺序（后加载的覆盖先加载的）：
1. JAR 内 application.yml（指定 spring.profiles.active=prod）
2. JAR 内 application-base.yml（如果在 application.yml 中导入）
3. JAR 内 application-prod.yml（Spring Boot 自动加载）
4. 外置 application-prod-enhanced.yml（通过 --spring.config.additional-location 加载）
```

**部署目录结构：**
```
/opt/ps/
├── ps-be.jar                              # 应用 JAR 包
├── config/
│   └── application-prod-enhanced.yml      # 外置生产配置（唯一外置文件）
├── logs/                                  # 日志目录
└── env.sh                                 # 环境变量脚本
```

**优点：**
- 配置集中管理，单一配置文件
- 部署简单，只需维护一个外置配置
- 配置清晰，避免多文件间的优先级混淆

**缺点：**
- 配置文件较大（包含所有配置项）
- 需要确保配置完整性（遗漏配置会导致启动失败）

#### 方案二：外置多个专项配置文件（传统方式）

**可行性：** 完全可行

**部署目录结构：**
```
/opt/ps/
├── ps-be.jar                              # 应用 JAR 包
├── config/
│   ├── env/
│   │   └── prod/
│   │       └── application-prod-enhanced.yml  # 主配置
│   ├── db/
│   │   └── database-prod.yml              # 数据库配置
│   ├── cache/
│   │   └── cache-prod.yml                 # 缓存配置
│   └── sec/
│       ├── security-prod.yml              # 安全配置
│       └── security-audit-prod.yml        # 审计配置
├── logs/                                  # 日志目录
└── env.sh                                 # 环境变量脚本
```

**优点：**
- 配置分类清晰，便于分工维护
- 单个文件较小，易于理解
- 与开发环境配置结构一致

**缺点：**
- 需要维护多个配置文件
- 配置优先级需要理解 Spring Boot 加载机制
- 配置分散，可能遗漏某些文件

#### 方案三：混合方案（灵活）

**部署目录结构：**
```
/opt/ps/
├── ps-be.jar
├── config/
│   ├── application-prod-enhanced.yml      # 主配置（包含大部分配置）
│   └── security-prod.yml                  # 敏感配置独立管理（可选）
├── logs/
└── env.sh
```

**适用场景：**
- 需要独立管理某些敏感配置（如安全配置）
- 配置文件权限管理要求不同

### 6.2 配置文件与 JAR 包的关系

#### JAR 包内必需文件

| 文件路径 | 文件名 | 是否必需 | 说明 |
|---------|-------|---------|------|
| 根目录 | application.yml | 是 | 主配置文件，指定 active profile |
| config/ | application-base.yml | 可选 | 基础模板（当前被注释，可以不包含） |
| config/log/ | logback-spring.xml | 是 | 日志配置文件 |
| config/cache/ | ehcache-prod.xml | 是 | EhCache 本地缓存配置 |
| redis/ | redisson-cluster.yml | 是 | Redisson 集群配置（如使用集群模式） |

#### 外置配置文件要求

**最小化外置配置（推荐）：**
- 仅需外置 application-prod-enhanced.yml
- 该文件必须是完整的生产配置（包含所有必需配置项）

**完整性检查清单：**
```
必需配置项：
✓ server.* - 服务器配置
✓ spring.datasource.* - 数据源配置
✓ spring.redis.* - Redis 配置
✓ spring.cache.* - 缓存配置
✓ app.* - 应用自定义配置
✓ topinfo.* - 向后兼容配置
✓ keycloak.* - SSO 配置
✓ jiuxi.platform.* - 监控配置
✓ logging.* - 日志配置
```

### 6.3 部署前准备

#### 环境变量准备

创建环境变量配置脚本 /opt/ps/env.sh，包含所有必需的敏感配置：

```bash
#!/bin/bash
# 生产环境环境变量配置

# 数据库配置
export DB_URL="jdbc:mariadb://192.168.1.100:3306/ps-bmp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai"
export DB_USERNAME="ps_user"
export DB_PASSWORD="YourSecurePassword"

# Redis 配置
export REDIS_HOST="192.168.1.101"
export REDIS_PORT="6379"
export REDIS_PASSWORD="YourRedisPassword"
export REDIS_DATABASE="0"

# Keycloak SSO 配置
export KEYCLOAK_SERVER_URL="https://sso.example.com"
export KEYCLOAK_CLIENT_SECRET="YourClientSecret"
export KEYCLOAK_ADMIN_USERNAME="admin"
export KEYCLOAK_ADMIN_PASSWORD="YourAdminPassword"
export FRONTEND_URL="https://app.example.com"

# 文件服务器配置
export FILE_SERVER_HOST="192.168.1.102"

# 邮件服务配置
export MAIL_HOST="smtp.example.com"
export MAIL_USERNAME="notify@example.com"
export MAIL_PASSWORD="YourMailPassword"

# 监控配置
export MONITOR_SERVER_URL="https://monitor.example.com/ps-be"
```

设置文件权限：
```bash
chmod 600 /opt/ps/env.sh
```

#### 配置文件准备

1. 创建配置目录：
```bash
mkdir -p /opt/ps/config
```

2. 上传 application-prod-enhanced.yml 到 /opt/ps/config/ 目录

3. 验证配置文件语法：
```bash
# 检查 YAML 语法
python -c "import yaml; yaml.safe_load(open('/opt/ps/config/application-prod-enhanced.yml'))"
```

4. 确认所有环境变量占位符已正确引用：
```bash
# 检查环境变量引用
grep -o '\${[^}]*}' /opt/ps/config/application-prod-enhanced.yml
```

#### 依赖服务准备

验证所有依赖服务可访问：

```bash
# 测试数据库连接
telnet 192.168.1.100 3306

# 测试 Redis 连接
redis-cli -h 192.168.1.101 -p 6379 -a YourRedisPassword ping

# 测试 Keycloak 服务
curl -I https://sso.example.com

# 测试文件服务器
telnet 192.168.1.102 8877

# 测试邮件服务
telnet smtp.example.com 587
```

### 6.4 部署步骤

#### 步骤1：创建部署目录
```bash
mkdir -p /opt/ps/{config,logs}
```

#### 步骤2：上传文件
```bash
# 上传 JAR 包
scp ps-be.jar root@prod-server:/opt/ps/

# 上传配置文件
scp application-prod-enhanced.yml root@prod-server:/opt/ps/config/

# 上传环境变量脚本
scp env.sh root@prod-server:/opt/ps/
```

#### 步骤3：设置权限
```bash
chmod +x /opt/ps/ps-be.jar
chmod 600 /opt/ps/env.sh
chmod 644 /opt/ps/config/application-prod-enhanced.yml
```

#### 步骤4：验证环境变量
```bash
source /opt/ps/env.sh
echo $DB_URL
echo $REDIS_HOST
```

#### 步骤5：启动应用

使用以下命令启动应用（仅外置一个配置文件）：

```bash
source /opt/ps/env.sh
java -jar /opt/ps/ps-be.jar \
  --spring.profiles.active=prod \
  --spring.config.additional-location=file:/opt/ps/config/ \
  -Xms2048m -Xmx4096m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=100 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/opt/ps/logs/heapdump.hprof \
  -Xlog:gc*:file=/opt/ps/logs/gc.log:time \
  > /opt/ps/logs/ps-be.log 2>&1 &
```

**关键参数说明：**
- `--spring.profiles.active=prod`：激活生产环境配置
- `--spring.config.additional-location=file:/opt/ps/config/`：指定外置配置文件目录（Spring Boot 会自动加载该目录下的 application-prod-enhanced.yml）
- 注意：使用 `additional-location` 而非 `location`，这样可以保留 JAR 内配置并叠加外置配置

**替代方案（如果使用 location）：**
```bash
--spring.config.location=classpath:/,file:/opt/ps/config/
```
这会从 classpath（JAR 内）和外置目录同时加载配置。

### 6.5 启动脚本封装

创建启动脚本 /opt/ps/start.sh：

```bash
#!/bin/bash
# PS-BE 生产环境启动脚本

APP_HOME="/opt/ps"
APP_NAME="ps-be"
JAR_FILE="${APP_HOME}/${APP_NAME}.jar"
CONFIG_DIR="${APP_HOME}/config"
LOG_DIR="${APP_HOME}/logs"
PID_FILE="${APP_HOME}/${APP_NAME}.pid"

# 加载环境变量
if [ -f "${APP_HOME}/env.sh" ]; then
    source "${APP_HOME}/env.sh"
    echo "环境变量加载成功"
else
    echo "错误：环境变量文件不存在"
    exit 1
fi

# 检查是否已运行
if [ -f "${PID_FILE}" ]; then
    OLD_PID=$(cat "${PID_FILE}")
    if ps -p "${OLD_PID}" > /dev/null 2>&1; then
        echo "应用已在运行，PID: ${OLD_PID}"
        exit 1
    fi
fi

# 启动应用
echo "启动 ${APP_NAME}..."
nohup java -jar "${JAR_FILE}" \
  --spring.profiles.active=prod \
  --spring.config.additional-location=file:${CONFIG_DIR}/ \
  -Xms2048m -Xmx4096m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=100 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=${LOG_DIR}/heapdump.hprof \
  -Xlog:gc*:file=${LOG_DIR}/gc.log:time \
  > "${LOG_DIR}/${APP_NAME}.log" 2>&1 &

# 保存 PID
echo $! > "${PID_FILE}"
echo "应用启动成功，PID: $(cat ${PID_FILE})"
echo "日志文件: ${LOG_DIR}/${APP_NAME}.log"
```

创建停止脚本 /opt/ps/stop.sh：

```bash
#!/bin/bash
# PS-BE 生产环境停止脚本

APP_HOME="/opt/ps"
APP_NAME="ps-be"
PID_FILE="${APP_HOME}/${APP_NAME}.pid"

if [ ! -f "${PID_FILE}" ]; then
    echo "应用未运行"
    exit 1
fi

PID=$(cat "${PID_FILE}")

if ! ps -p "${PID}" > /dev/null 2>&1; then
    echo "应用未运行"
    rm -f "${PID_FILE}"
    exit 1
fi

echo "停止应用，PID: ${PID}"
kill -15 "${PID}"

# 等待优雅关闭
for i in {1..30}; do
    if ! ps -p "${PID}" > /dev/null 2>&1; then
        echo "应用已停止"
        rm -f "${PID_FILE}"
        exit 0
    fi
    sleep 1
done

# 强制关闭
echo "强制停止应用"
kill -9 "${PID}"
rm -f "${PID_FILE}"
echo "应用已强制停止"
```

设置脚本权限：
```bash
chmod +x /opt/ps/start.sh
chmod +x /opt/ps/stop.sh
```

### 6.6 配置文件热更新

**注意：** 外置配置文件修改后需要重启应用才能生效。

更新配置的步骤：

1. 修改外置配置文件
```bash
vi /opt/ps/config/application-prod-enhanced.yml
```

2. 验证配置语法
```bash
python -c "import yaml; yaml.safe_load(open('/opt/ps/config/application-prod-enhanced.yml'))"
```

3. 重启应用
```bash
/opt/ps/stop.sh
/opt/ps/start.sh
```

**动态刷新支持：**
如需支持配置动态刷新，可以集成 Spring Cloud Config 或使用 Actuator 的 refresh 端点（需要额外配置）。

### 6.7 配置文件完整性验证

为了确保外置的 application-prod-enhanced.yml 文件包含所有必需配置，可以使用以下检查清单：

#### 必需配置项检查

```yaml
# 1. 服务器配置
server:
  port: 8080
  servlet:
    context-path: /ps-be
  tomcat:
    max-threads: 2000
    max-connections: 10000
  compression:
    enabled: true

# 2. 数据源配置
spring:
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    druid:
      initial-size: 20
      min-idle: 20
      max-active: 500

# 3. Redis 配置
  redis:
    host: ${REDIS_HOST}
    port: ${REDIS_PORT}
    password: ${REDIS_PASSWORD}
    database: ${REDIS_DATABASE}
    timeout: 10000ms
    jedis:
      pool:
        max-active: 100

# 4. 缓存配置
  cache:
    type: ehcache
    ehcache:
      config: classpath:config/cache/ehcache-prod.xml

# 5. Redisson 配置
  redisson:
    config: classpath:redis/redisson-cluster.yml

# 6. 应用自定义配置
app:
  info:
    name: ps-be
    version: 2.2.2-SNAPSHOT
  database:
    datasource:
      url: ${DB_URL}
  cache:
    redis:
      host: ${REDIS_HOST}
  security:
    keycloak:
      server-url: ${KEYCLOAK_SERVER_URL}

# 7. 向后兼容配置
topinfo:
  mybatis:
    datasource-config:
      url: ${DB_URL}
  security:
    enable: true
  jdfs:
    client:
      model: server

# 8. Keycloak SSO 配置
keycloak:
  server-url: ${KEYCLOAK_SERVER_URL}
  realm: ${KEYCLOAK_REALM:ps-realm}
  sso:
    enabled: ${KEYCLOAK_SSO_ENABLED:true}
    client-id: ${KEYCLOAK_CLIENT_ID:ps-be}
    client-secret: ${KEYCLOAK_CLIENT_SECRET}

# 9. 监控配置
jiuxi:
  platform:
    plugin:
      monitor:
        server-url: ${MONITOR_SERVER_URL}

# 10. 日志配置
logging:
  level:
    root: WARN
    com.jiuxi: INFO
  file:
    name: ${LOG_PATH:/opt/ps/logs}/ps-bmp-backend.log
```

#### 自动化验证脚本

创建验证脚本 /opt/ps/validate-config.sh：

```bash
#!/bin/bash
# 配置文件完整性验证脚本

CONFIG_FILE="/opt/ps/config/application-prod-enhanced.yml"

echo "=== 验证配置文件完整性 ==="

# 1. 验证文件存在
if [ ! -f "${CONFIG_FILE}" ]; then
    echo "✗ 配置文件不存在"
    exit 1
fi
echo "✓ 配置文件存在"

# 2. 验证 YAML 语法
if ! python3 -c "import yaml; yaml.safe_load(open('${CONFIG_FILE}'))" 2>/dev/null; then
    echo "✗ YAML 语法错误"
    exit 1
fi
echo "✓ YAML 语法正确"

# 3. 检查必需配置项
REQUIRED_KEYS=(
    "server.port"
    "spring.datasource.url"
    "spring.redis.host"
    "app.info.name"
    "keycloak.server-url"
    "logging.level.root"
)

for key in "${REQUIRED_KEYS[@]}"; do
    if ! grep -q "${key}" "${CONFIG_FILE}"; then
        echo "✗ 缺少必需配置：${key}"
        exit 1
    fi
done
echo "✓ 必需配置项齐全"

# 4. 检查环境变量引用
ENV_VARS=$(grep -o '\${[^}]*}' "${CONFIG_FILE}" | sort -u)
if [ -n "${ENV_VARS}" ]; then
    echo "发现环境变量引用："
    echo "${ENV_VARS}"
    
    # 加载环境变量
    source /opt/ps/env.sh
    
    # 验证必需环境变量
    REQUIRED_ENV_VARS=(
        "DB_URL"
        "DB_USERNAME"
        "DB_PASSWORD"
        "REDIS_HOST"
        "REDIS_PASSWORD"
        "KEYCLOAK_SERVER_URL"
        "KEYCLOAK_CLIENT_SECRET"
    )
    
    for env_var in "${REQUIRED_ENV_VARS[@]}"; do
        if [ -z "${!env_var}" ]; then
            echo "✗ 缺少必需环境变量：${env_var}"
            exit 1
        fi
    done
    echo "✓ 必需环境变量已设置"
fi

echo "\n=== 配置验证通过 ==="
exit 0
```

设置执行权限：
```bash
chmod +x /opt/ps/validate-config.sh
```

执行验证：
```bash
/opt/ps/validate-config.sh
```

### 6.8 配置文件备份与版本管理

#### 配置备份策略

1. 创建备份目录：
```bash
mkdir -p /opt/ps/config-backup
```

2. 备份配置文件：
```bash
cp /opt/ps/config/application-prod-enhanced.yml \
   /opt/ps/config-backup/application-prod-enhanced.yml.$(date +%Y%m%d_%H%M%S)
```

3. 自动备份脚本：
```bash
#!/bin/bash
# 配置文件备份脚本
BACKUP_DIR="/opt/ps/config-backup"
CONFIG_FILE="/opt/ps/config/application-prod-enhanced.yml"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

cp "${CONFIG_FILE}" "${BACKUP_DIR}/application-prod-enhanced.yml.${TIMESTAMP}"

# 保留最近 10 个备份
ls -t "${BACKUP_DIR}"/*.yml.* | tail -n +11 | xargs rm -f

echo "配置备份完成：${BACKUP_DIR}/application-prod-enhanced.yml.${TIMESTAMP}"
```

#### 配置版本管理

建议将配置文件纳入 Git 版本控制：

1. 初始化 Git 仓库：
```bash
cd /opt/ps/config
git init
git add application-prod-enhanced.yml
git commit -m "Initial production configuration"
```

2. 配置更新后提交：
```bash
git add application-prod-enhanced.yml
git commit -m "Update database connection pool settings"
```

3. 查看配置变更历史：
```bash
git log --oneline
```

4. 回滚到之前的版本：
```bash
git checkout <commit-id> application-prod-enhanced.yml
```

## 七、常见问题解答

### 7.1 配置文件相关问题

#### 问题1：只外置一个配置文件是否足够？

**答：** 是的，只要 application-prod-enhanced.yml 包含所有必需配置项，就可以仅依靠这一个外置文件启动应用。但需要注意：

1. JAR 包内必须包含：
   - application.yml（指定 spring.profiles.active=prod）
   - config/log/logback-spring.xml（日志配置）
   - config/cache/ehcache-prod.xml（EhCache 配置）
   - redis/redisson-cluster.yml（Redisson 集群配置，如使用）

2. 外置的 application-prod-enhanced.yml 必须包含：
   - 所有 Spring Boot 原生配置（server, spring.datasource, spring.redis 等）
   - 所有自定义配置（app.*, topinfo.*, keycloak.*, jiuxi.platform.* 等）
   - 所有日志配置（logging.*）

3. 所有敏感信息通过环境变量提供

#### 问题2：JAR 包内的 application.yml 需要如何配置？

**答：** JAR 包内的 application.yml 只需要包含最小配置：

```yaml
spring:
  profiles:
    active: prod  # 指定默认环境
  config:
    import:
      - "classpath:config/env/${spring.profiles.active}/application-${spring.profiles.active}.yml"
      # 可选：导入向后兼容的专项配置
      - "optional:classpath:config/db/database-${spring.profiles.active}.yml"
      - "optional:classpath:config/cache/cache-${spring.profiles.active}.yml"
      - "optional:classpath:config/sec/security-${spring.profiles.active}.yml"

app:
  info:
    name: ps-be
    version: 2.2.2-SNAPSHOT
```

或者更简化（不导入任何配置，完全依赖外置配置）：

```yaml
spring:
  profiles:
    active: prod

app:
  info:
    name: ps-be
    version: 2.2.2-SNAPSHOT
```

#### 问题3：不外置配置文件，只依赖 JAR 包内配置可以吗？

**答：** 可以，但不推荐。如果将所有配置（包括敏感信息）都打包到 JAR 内：

**优点：**
- 部署简单，只需上传 JAR 包
- 不需要额外配置文件

**缺点：**
- 敏感信息暴露风险（JAR 可被解压查看）
- 配置修改需要重新打包
- 不符合安全最佳实践
- 不符合 12-Factor App 原则

**推荐做法：** 将通用配置打包到 JAR 内，将环境特定和敏感配置外置。

#### 问题4：外置配置文件优先级怎么确定？

**答：** Spring Boot 配置加载优先级（从低到高）：

1. JAR 内 application.yml
2. JAR 内 application-{profile}.yml
3. 外置 config/application.yml
4. 外置 config/application-{profile}.yml
5. 外置 application.yml（当前目录）
6. 外置 application-{profile}.yml（当前目录）
7. 命令行参数
8. 环境变量

**关键点：**
- 后加载的配置会覆盖先加载的配置
- 环境变量优先级最高
- 使用 `--spring.config.additional-location` 可以添加额外配置位置
- 使用 `--spring.config.location` 会替换默认配置位置

#### 问题5：如何验证配置文件是否被加载？

**答：** 有几种方法：

1. 启动时启用 debug 模式：
```bash
java -jar ps-be.jar --spring.profiles.active=prod --debug
```

2. 查看日志输出，搜索 "Loaded config file"：
```bash
grep "Loaded config file" /opt/ps/logs/ps-be.log
```

3. 使用 Actuator 端点查看配置：
```bash
curl http://localhost:8080/ps-be/actuator/configprops
```

4. 使用 Actuator 查看环境属性：
```bash
curl http://localhost:8080/ps-be/actuator/env
```

#### 问题6：配置文件修改后如何生效？

**答：** 默认情况下，配置文件修改后必须重启应用才能生效。

**重启流程：**
```bash
/opt/ps/stop.sh
/opt/ps/start.sh
```

**支持动态刷新（需要额外配置）：**

1. 使用 Spring Cloud Config
2. 使用 Actuator refresh 端点（仅支持部分配置）

### 7.2 环境变量相关问题

验证应用启动成功，检查日志文件是否正常输出，测试健康检查端点 /actuator/health，测试数据库连接，测试 Redis 连接，测试 SSO 登录，测试文件上传下载，测试邮件发送。

### 6.5 监控配置

配置 Prometheus 监控抓取，配置日志收集（ELK/Loki），配置告警规则，配置负载均衡健康检查。

## 七、生产配置安全检查清单

### 7.1 敏感信息检查

确认所有密码使用环境变量，确认所有密钥使用环境变量，确认配置文件中无明文密码，确认环境变量配置脚本权限为 600。

### 7.2 认证授权检查

确认认证排除路径仅包含必要路径，确认密码策略为强密码要求，确认账户锁定策略已启用，确认 JWT 验证完整性（过期、签发者、受众）。

### 7.3 网络安全检查

确认数据库连接按需启用 SSL，确认 Redis 连接按需启用 SSL，确认 Keycloak 使用 HTTPS，确认邮件服务启用加密传输。

### 7.4 功能安全检查

确认 Druid 监控界面已禁用，确认 DevTools 热重载已禁用，确认 SQL 防火墙已启用，确认 DELETE 操作已禁用。

## 八、配置优化建议

### 8.1 性能优化

根据实际负载调整数据库连接池大小，根据实际负载调整 Redis 连接池大小，启用缓存预热功能加快启动，调整 JVM 堆内存大小匹配服务器资源。

### 8.2 稳定性优化

配置健康检查超时时间，配置连接超时时间防止资源耗尽，配置重试机制应对网络抖动，配置熔断保护防止雪崩。

### 8.3 可维护性优化

使用配置管理工具（如 Spring Cloud Config），使用密钥管理服务（如 Vault），配置动态刷新支持，建立配置变更审计机制。

## 九、常见问题与解决方案

### 9.1 环境变量未生效

问题：应用启动时提示找不到环境变量
解决方案：确认环境变量在应用启动前已加载，使用 source 命令加载环境变量脚本，检查环境变量名称拼写是否正确。

### 9.2 数据库连接失败

问题：应用启动时无法连接数据库
解决方案：检查数据库地址、用户名、密码是否正确，检查数据库服务是否启动，检查网络连接和防火墙规则，检查数据库最大连接数限制。

### 9.3 Redis 连接失败

问题：应用启动时无法连接 Redis
解决方案：检查 Redis 地址、端口、密码是否正确，检查 Redis 服务是否启动，检查网络连接和防火墙规则，检查 Redis 最大客户端连接数限制。

### 9.4 SSO 登录失败

问题：用户无法通过 Keycloak 登录
解决方案：检查 Keycloak 服务器地址是否可访问，检查客户端密钥是否正确，检查 Realm 和客户端ID是否正确，检查前端重定向地址是否正确。

### 9.5 文件上传失败

问题：文件上传报错或超时
解决方案：检查文件服务器地址和端口是否正确，检查文件大小是否超过限制，检查文件服务器存储空间是否充足，检查网络带宽是否足够。

## 十、总结

本设计文档完成了 PS-BE 项目配置文件的全面分析，基于开发环境配置设计了一套完整的生产环境配置方案。生产配置遵循安全性、性能、稳定性和可维护性四大原则，所有敏感信息通过环境变量外部化管理，连接池和缓存参数针对生产负载优化，认证授权策略严格控制，日志输出精简高效。

配置方案采用新架构（application-prod-enhanced.yml）和旧架构（专项配置文件）并存策略，确保向后兼容性的同时推进配置规范化。提供了详细的环境变量清单、部署指南、安全检查清单和常见问题解决方案，为生产环境部署提供全面指导。

建议在部署前充分测试配置有效性，部署后持续监控系统运行状态，根据实际负载动态调整配置参数，定期审查安全配置确保系统安全。
