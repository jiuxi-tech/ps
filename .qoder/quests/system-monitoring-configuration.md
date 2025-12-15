# 系统监控配置功能设计文档

## 一、功能概述

系统监控功能通过心跳接口 `/sys/monitor/heartbeat` 实现对多个应用实例的实时监控。该功能采用客户端-服务端架构,客户端定时上报系统运行状态,服务端接收、存储并分析监控数据,当检测到异常时触发报警机制。

### 核心价值

- 实时监控:及时发现系统资源使用异常和服务健康问题
- 主动报警:通过邮件通知相关负责人,避免故障扩大
- 集中管理:统一管理多个应用实例的监控配置和状态

### 重要说明

**同服务器部署场景**:当前设计支持客户端和服务端部署在同一个应用服务器中。在这种场景下,应用既是监控服务端(接收其他客户端心跳),也是监控客户端(上报自身状态)。配置时 `server-url` 应指向本服务的完整地址。

## 二、系统架构

### 2.1 整体架构

```
graph TB
    subgraph 客户端应用
        A[定时任务调度器] -->|每60秒| B[心跳服务]
        B --> C[收集系统信息]
        C --> D[CPU/内存/磁盘/JVM]
        C --> E[健康检查服务]
        B --> F[发送HTTP请求]
    end
    
    subgraph 监控服务端
        F -->|POST /sys/monitor/heartbeat| G[心跳接口]
        G --> H[心跳处理服务]
        H --> I[存储客户端信息]
        H --> J[缓存心跳数据]
        H --> K[报警计算服务]
        K --> L[邮件发送服务]
        
        M[离线检测定时任务] -->|每5分钟| N[扫描所有客户端]
        N --> O{是否离线}
        O -->|是| L
    end
    
    subgraph 存储层
        I --> P[(MySQL数据库)]
        J --> Q[(Redis缓存)]
    end
    
    L --> R[邮件服务器]
    R --> S[负责人邮箱]
```

### 2.2 核心组件

| 组件类别 | 组件名称 | 职责说明 |
|---------|---------|---------|
| **客户端** | MonitorClientHeartbeatSchedule | 定时任务调度，每60秒触发一次心跳上报 |
| | MonitorClientService | 构建并发送心跳信息到服务端 |
| | MonitorHealthServiceComposite | 检查各类服务健康状态（数据库、Redis、MQ等） |
| **服务端** | TpMonitorClientController | 接收心跳请求的HTTP接口 |
| | MonitorServerService | 处理心跳数据的核心业务逻辑 |
| | MonitorAlarmService | 报警计算与判定服务 |
| | MonitorSendMailService | 邮件发送服务 |
| | MonitorServerSchedule | 离线检测定时任务，每5分钟执行一次 |
| **数据存储** | TpMonitorClientService | 客户端信息的增删改查服务 |
| | MonitorCacheService | Redis缓存服务，存储心跳数据和报警状态 |

## 三、配置方式

### 3.1 客户端配置

客户端需要在应用配置文件中添加监控插件配置，配置路径为 `jiuxi.platform.plugin.monitor`。

#### 配置项说明

| 配置项 | 类型 | 是否必填 | 默认值 | 说明 |
|-------|------|---------|-------|------|
| server-url | String | 是 | 无 | 监控服务端地址，用于发送心跳请求 |
| client-id | String | 是 | 无 | 客户端唯一标识符 |
| system-desc | String | 是 | 无 | 系统描述信息，用于报警邮件中识别系统 |
| connection-timeout | Integer | 否 | 3000 | HTTP连接超时时间（毫秒） |
| read-timeout | Integer | 否 | 5000 | HTTP读取超时时间（毫秒） |
| exclude-source | List&lt;String&gt; | 否 | 空 | 排除的健康检查服务列表 |

#### 配置示例

配置文件：`application-dev-enhanced.yml`

```
jiuxi:
  platform:
    plugin:
      monitor:
        server-url: http://192.168.0.139:8082/ps-be
        client-id: ps-bmp-backend-dev
        system-desc: PS-BMP后端系统
        connection-timeout: 3000
        read-timeout: 5000
```

#### 配置激活条件

监控客户端功能的激活需要满足以下条件:

1. **配置存在**:必须配置 `jiuxi.platform.plugin.monitor.server-url` 属性
2. **定时任务启用**:Spring 定时任务调度器必须启用(通过 `@EnableScheduling` 注解)
3. **组件扫描**:监控客户端相关类需要被 Spring 扫描到

这些条件通过以下机制保证:
- `@ConditionalOnProperty(prefix = "jiuxi.platform.plugin.monitor", name = "server-url")` 确保配置存在时才加载
- `@EnableScheduling` 在 MonitorClientAutoConfiguration 中启用定时任务
- `@ComponentScan` 扫描监控客户端包路径

### 3.2 服务端配置

服务端需要配置报警阈值和通知方式，配置存储在数据库表 `tp_monitor_config` 中。

#### 数据库配置表结构

| 字段名 | 类型 | 说明 |
|-------|------|------|
| config_id | String | 主键 |
| cpu_threshold | BigDecimal | CPU报警阈值（百分比，如80表示80%） |
| memory_threshold | BigDecimal | 内存报警阈值（百分比） |
| disk_threshold | BigDecimal | 磁盘报警阈值（百分比） |
| offline_threshold | Integer | 离线报警阈值（分钟） |
| send_mail | Integer | 是否发送邮件（1：是，0：否） |
| principal | String | 系统负责人（多个逗号分隔） |
| mobile | String | 负责人手机号 |
| email | String | 负责人邮箱（多个逗号分隔） |

#### 配置接口

| 接口路径 | 请求方式 | 说明 |
|---------|---------|------|
| /sys/monitor-config/view | GET | 查看当前监控配置 |
| /sys/monitor-config/update | POST | 更新监控配置 |

#### 配置特点

- 全局唯一：系统中只存在一条配置记录
- 缓存优化：配置数据缓存在Redis中，避免频繁查询数据库
- 动态生效：修改配置后立即生效，无需重启服务

## 四、心跳上报流程

### 4.1 客户端心跳上报流程

```
sequenceDiagram
    participant S as 定时调度器
    participant C as MonitorClientService
    participant U as CommonServerInfoUtil
    participant H as HealthServiceComposite
    participant A as HTTP客户端
    participant E as 监控服务端

    S->>C: 每60秒触发heartbeat()
    C->>C: 检查配置是否为空
    C->>U: 收集服务器基础信息
    U-->>C: CPU/内存/磁盘/JVM数据
    C->>C: 获取MAC地址
    C->>C: 获取部署路径
    C->>C: 设置clientId和systemDesc
    C->>H: 获取不健康的服务列表
    H-->>C: 返回异常服务清单
    C->>C: 构建ClientHeartbeatInfo对象
    C->>A: 发送POST请求
    A->>E: POST /sys/monitor/heartbeat
    E-->>A: 返回处理结果
    A-->>C: 响应成功/失败
    C->>C: 记录日志
```

### 4.2 心跳数据结构

心跳信息包含以下核心数据：

| 数据类别 | 字段名 | 说明 |
|---------|-------|------|
| **客户端标识** | clientId | 客户端唯一标识 |
| | systemDesc | 系统描述 |
| | applicationId | 应用ID |
| | applicationName | 应用名称 |
| | macAddr | MAC地址 |
| | absolutePath | 部署绝对路径 |
| **服务器信息** | cpu | CPU使用率、核心数等 |
| | mem | 内存使用情况 |
| | jvm | JVM内存、线程等信息 |
| | sys | 操作系统信息 |
| | sysFiles | 磁盘使用情况列表 |
| **健康状态** | notHealthServer | 不健康的服务列表 |
| **时间戳** | createTime | 信息生成时间 |
| | lastUpdateTime | 最后更新时间 |

### 4.3 服务端处理流程

```
sequenceDiagram
    participant H as 心跳接口
    participant S as MonitorServerService
    participant D as 数据库服务
    participant C as 缓存服务
    participant A as 报警服务
    participant M as 邮件服务

    H->>H: 接收ClientHeartbeatInfo
    H->>S: handleHeartbeat()
    S->>S: 获取客户端IP地址
    S->>S: 生成或验证clientId
    S->>D: 查询客户端是否存在
    alt 客户端不存在
        D-->>S: 返回null
        S->>D: 新增客户端信息
    else 客户端已存在
        D-->>S: 返回客户端信息
    end
    S->>C: 存储心跳数据到Redis
    S->>A: 计算报警事件
    A->>A: 检查CPU/内存/磁盘阈值
    A->>A: 检查服务健康状态
    A->>A: 判断是否达到报警条件
    alt 存在报警事件且未发送过邮件
        A->>M: 发送报警邮件
        M->>M: 组装邮件内容
        M->>M: 发送邮件
        M-->>A: 返回发送结果
        A->>C: 记录已发送邮件缓存
    end
    S-->>H: 处理完成
    H-->>H: 返回成功响应
```

## 五、报警机制

### 5.1 报警触发条件

系统监控支持多种报警类型，每种类型都有独立的触发条件和判定逻辑。

#### 资源使用率报警

| 报警类型 | 判定条件 | 持续时间要求 |
|---------|---------|-------------|
| CPU报警 | CPU使用率 > cpu_threshold | 连续5分钟超过阈值 |
| 内存报警 | 内存使用率 > memory_threshold | 连续5分钟超过阈值 |
| 磁盘报警 | 任意磁盘使用率 > disk_threshold | 连续5分钟超过阈值 |

#### 服务健康报警

| 服务类型 | 判定条件 |
|---------|---------|
| 单数据源 | 健康检查失败 |
| 动态数据源 | 健康检查失败 |
| Redis | 健康检查失败 |
| MQ | 健康检查失败 |
| MQTT | 健康检查失败 |

#### 离线报警

| 判定条件 | 检测周期 |
|---------|---------|
| 距离上次心跳时间超过5分钟 | 每5分钟扫描一次 |

### 5.2 报警计算算法

报警采用"连续超阈值"算法，避免偶发性波动引起误报。

#### 算法流程

```
flowchart TD
    A[接收心跳数据] --> B{资源是否超阈值?}
    B -->|是| C[缓存中增加报警计数]
    B -->|否| D[清除缓存中的报警计数]
    C --> E{报警计数对应时间>=5分钟?}
    E -->|是| F{12小时内是否已发送邮件?}
    F -->|否| G[触发报警，发送邮件]
    F -->|是| H[跳过报警]
    E -->|否| I[继续监控，等待下次心跳]
    D --> I
    G --> J[记录已发送邮件缓存12小时]
    G --> K[清除报警计数缓存]
```

#### 缓存机制

| 缓存类型 | Redis Key | 过期时间 | 说明 |
|---------|-----------|---------|------|
| 心跳数据 | jiuxi:platform:plugin:monitor:heartbeat:{clientId} | 30天 | 存储最新的完整心跳信息 |
| 报警计数 | jiuxi:platform:plugin:monitor:heartbeat_alarm:{clientId}:{source} | 5分钟 | 记录资源持续超阈值的次数 |
| 已发送邮件 | jiuxi:platform:plugin:monitor:client:send_mail:{clientId}:{source} | 12小时 | 防止重复发送报警邮件 |
| 客户端基本信息 | jiuxi:platform:plugin:monitor:client:baseinfo:{clientId} | 30天 | 缓存客户端基本信息 |
| 监控配置 | jiuxi:platform:plugin:monitor:alarm:config | 永久 | 缓存报警阈值配置 |

### 5.3 邮件报警内容

邮件报警采用简洁明了的格式，便于负责人快速了解问题。

#### 邮件内容格式

```
【应用名称】系统描述：备注信息，报警类型1，当前值：XX%；报警类型2；报警类型3。请及时处理！
```

#### 示例

```
【ps-bmp-backend】PS-BMP后端系统：生产环境服务器，CPU报警，当前值：85%；内存报警，当前值：92%。请及时处理！
```

### 5.4 离线检测机制

服务端通过定时任务检测客户端是否离线。

#### 检测流程

```
flowchart TD
    A[定时任务每5分钟执行] --> B[查询监控配置]
    B --> C{配置是否存在?}
    C -->|否| D[结束检测]
    C -->|是| E[查询所有客户端列表]
    E --> F[遍历每个客户端]
    F --> G[从缓存获取心跳数据]
    G --> H{心跳数据是否存在?}
    H -->|否| I[跳过该客户端]
    H -->|是| J{当前时间 - 心跳时间 > 5分钟?}
    J -->|否| K[客户端在线]
    J -->|是| L{12小时内是否发送过离线邮件?}
    L -->|是| M[跳过报警]
    L -->|否| N[发送离线报警邮件]
    N --> O[记录已发送邮件缓存]
    F --> P{是否还有客户端?}
    P -->|是| F
    P -->|否| Q[检测完成]
```

## 六、数据存储

### 6.1 数据库表设计

#### tp_monitor_client 客户端信息表

| 字段名 | 类型 | 说明 |
|-------|------|------|
| client_id | VARCHAR(36) | 客户端唯一标识（主键） |
| application_id | VARCHAR(100) | 应用ID |
| application_name | VARCHAR(100) | 应用名称 |
| system_desc | VARCHAR(200) | 系统描述 |
| mac_addr | VARCHAR(50) | MAC地址 |
| absolute_path | VARCHAR(500) | 部署绝对路径 |
| ip_address | VARCHAR(50) | IP地址 |
| remark | VARCHAR(500) | 备注信息 |
| tenant_id | VARCHAR(36) | 租户ID |
| actived | INTEGER | 是否有效（1：有效，0：无效） |
| creator | VARCHAR(36) | 创建人 |
| create_time | DATETIME | 创建时间 |
| updator | VARCHAR(36) | 修改人 |
| update_time | DATETIME | 修改时间 |

#### tp_monitor_config 监控配置表

| 字段名 | 类型 | 说明 |
|-------|------|------|
| config_id | VARCHAR(36) | 配置ID（主键） |
| cpu_threshold | DECIMAL | CPU报警阈值 |
| memory_threshold | DECIMAL | 内存报警阈值 |
| disk_threshold | DECIMAL | 磁盘报警阈值 |
| offline_threshold | INTEGER | 离线报警阈值（分钟） |
| send_mail | INTEGER | 是否发送邮件（1：是，0：否） |
| principal | VARCHAR(200) | 系统负责人 |
| mobile | VARCHAR(50) | 负责人手机号 |
| email | VARCHAR(500) | 负责人邮箱（多个逗号分隔） |
| tenant_id | VARCHAR(36) | 租户ID |
| actived | INTEGER | 是否有效 |
| creator | VARCHAR(36) | 创建人 |
| create_time | DATETIME | 创建时间 |
| updator | VARCHAR(36) | 修改人 |
| update_time | DATETIME | 修改时间 |

### 6.2 Redis缓存策略

#### 缓存分层

| 层级 | 用途 | 特点 |
|-----|------|------|
| 热数据层 | 心跳数据、报警计数 | 高频读写，短期存储 |
| 温数据层 | 客户端基本信息、配置 | 中频读取，长期存储 |
| 防重层 | 已发送邮件记录 | 防止重复报警 |

#### 缓存更新策略

| 数据类型 | 更新时机 | 更新方式 |
|---------|---------|---------|
| 心跳数据 | 每次接收心跳 | 全量覆盖 |
| 客户端信息 | 首次接收心跳 | 首次写入，后续读取 |
| 监控配置 | 修改配置时 | 主动更新缓存 |
| 报警计数 | 每次心跳检测 | 增量更新或清除 |

## 七、接口清单

### 7.1 心跳接口

#### POST /sys/monitor/heartbeat

**接口说明**：接收客户端心跳数据

**请求头**：
- Content-Type: application/json
- token: 固定的认证令牌

**请求体**：ClientHeartbeatInfo对象（JSON格式）

**响应**：
```
{
  "success": true,
  "message": "操作成功",
  "data": null
}
```

### 7.2 客户端管理接口

| 接口路径 | 请求方式 | 是否需要授权 | 说明 |
|---------|---------|------------|------|
| /sys/monitor/list | POST | 是 | 查询客户端列表（分页） |
| /sys/monitor/view | GET | 否 | 查看客户端详情 |
| /sys/monitor/view-heartbeat | GET | 否 | 查看客户端最新心跳信息 |
| /sys/monitor/update | POST | 否 | 更新客户端信息 |
| /sys/monitor/delete | POST | 否 | 删除客户端 |

### 7.3 监控配置接口

| 接口路径 | 请求方式 | 是否需要授权 | 说明 |
|---------|---------|------------|------|
| /sys/monitor-config/view | GET | 是 | 查看监控配置 |
| /sys/monitor-config/update | POST | 是 | 更新监控配置 |

## 八、异常处理

### 8.1 客户端异常处理

| 异常场景 | 处理策略 | 日志级别 |
|---------|---------|---------|
| 配置为空 | 跳过心跳发送，记录错误日志 | ERROR |
| 网络连接失败 | 捕获异常，记录错误日志，等待下次重试 | ERROR |
| 服务端响应超时 | 捕获异常，记录错误日志 | ERROR |
| 心跳信息构建失败 | 跳过发送，记录错误日志 | ERROR |

### 8.2 服务端异常处理

| 异常场景 | 处理策略 | 日志级别 |
|---------|---------|---------|
| 心跳数据为空 | 返回失败响应 | WARN |
| 数据库操作失败 | 捕获异常，记录日志，继续处理后续逻辑 | ERROR |
| 缓存操作失败 | 捕获异常，记录日志，不影响主流程 | ERROR |
| 邮件发送失败 | 不记录已发送缓存，5分钟后重试 | ERROR |

### 8.3 离线检测异常处理

| 异常场景 | 处理策略 |
|---------|---------|
| 配置不存在 | 跳过检测，记录INFO日志 |
| 心跳数据为空 | 跳过该客户端，可能是手工删除缓存 |
| 解析时间异常 | 捕获异常，跳过该客户端，记录错误日志 |

## 九、性能优化策略

### 9.1 客户端优化

| 优化项 | 策略 | 效果 |
|-------|------|------|
| 心跳频率 | 固定60秒间隔 | 平衡实时性和服务器压力 |
| HTTP超时 | 连接超时3秒，读取超时5秒 | 避免长时间阻塞 |
| 异常处理 | 快速失败，不阻塞主流程 | 保证客户端应用稳定性 |

### 9.2 服务端优化

| 优化项 | 策略 | 效果 |
|-------|------|------|
| 缓存优先 | 优先从Redis读取，减少数据库查询 | 提升响应速度 |
| 批量处理 | 离线检测批量查询客户端 | 减少数据库交互次数 |
| 异步邮件 | 邮件发送不阻塞主流程 | 提升接口响应速度 |
| 防重机制 | 12小时内不重复发送相同报警 | 减少邮件发送量 |

### 9.3 缓存优化

| 优化项 | 策略 | 效果 |
|-------|------|------|
| 合理过期时间 | 心跳30天，报警计数5分钟 | 平衡存储空间和数据有效性 |
| 懒加载 | 首次访问时加载，后续使用缓存 | 减少不必要的查询 |
| 主动更新 | 配置修改时主动更新缓存 | 保证数据一致性 |

## 十、安全机制

### 10.1 认证机制

| 认证方式 | 应用场景 | 说明 |
|---------|---------|------|
| Token认证 | 心跳接口 | 客户端发送心跳时携带固定token |
| JWT认证 | 管理接口 | 需要用户登录后的JWT令牌 |

### 10.2 数据安全

| 安全措施 | 说明 |
|---------|------|
| 客户端唯一标识 | 通过MAC地址、部署路径、应用名称生成MD5，保证唯一性 |
| IP地址记录 | 记录客户端IP地址，便于追踪和审计 |
| 租户隔离 | 支持多租户场景，数据按租户隔离 |

## 十一、扩展性设计

### 11.1 支持的监控资源类型

当前支持的监控资源：
- CPU使用率
- 内存使用率
- 磁盘使用率
- 单数据源健康状态
- 动态数据源健康状态
- Redis健康状态
- MQ健康状态
- MQTT健康状态
- 系统离线状态

### 11.2 扩展点

| 扩展点 | 说明 | 扩展方式 |
|-------|------|---------|
| 新增监控资源 | 增加新的监控指标类型 | 在MonitorServerConstant中添加新的资源类型常量 |
| 自定义报警算法 | 修改报警触发条件 | 在MonitorAlarmService中调整计算逻辑 |
| 多种通知方式 | 除邮件外增加短信、钉钉等 | 扩展通知服务接口 |
| 健康检查扩展 | 增加新的服务健康检查 | 实现HealthService接口并注册到Composite |

## 十二、监控指标说明

### 12.1 系统资源指标

| 指标类别 | 具体指标 | 单位 | 说明 |
|---------|---------|------|------|
| CPU | 使用率 | % | 系统CPU使用百分比 |
| | 核心数 | 个 | CPU物理核心数和逻辑核心数 |
| 内存 | 总内存 | MB | 系统总内存大小 |
| | 已用内存 | MB | 已使用的内存大小 |
| | 使用率 | % | 内存使用百分比 |
| JVM | 堆内存总量 | MB | JVM堆内存最大值 |
| | 堆内存已用 | MB | JVM堆内存已使用量 |
| | 线程数 | 个 | JVM当前线程总数 |
| 磁盘 | 磁盘总容量 | GB | 每个分区的总容量 |
| | 已用容量 | GB | 每个分区已使用容量 |
| | 使用率 | % | 每个分区使用百分比 |

### 12.2 服务健康指标

| 服务类型 | 健康判定标准 |
|---------|-------------|
| 单数据源 | 数据库连接正常，可执行简单查询 |
| 动态数据源 | 所有配置的数据源均可连接 |
| Redis | Redis连接正常，可执行ping命令 |
| MQ | MQ连接正常，可发送测试消息 |
| MQTT | MQTT连接正常，可订阅主题 |

## 十三、使用建议

### 13.1 阈值配置建议

| 监控指标 | 建议阈值 | 说明 |
|---------|---------|------|
| CPU使用率 | 80% | 超过80%表示CPU压力较大 |
| 内存使用率 | 85% | 超过85%可能影响性能 |
| 磁盘使用率 | 80% | 超过80%需要清理空间 |
| 离线阈值 | 5分钟 | 5分钟未收到心跳判定为离线 |

### 13.2 报警接收人配置

- 邮箱字段支持配置多个邮箱地址，使用英文逗号分隔
- 建议配置团队共享邮箱和关键负责人邮箱
- 定期检查邮箱配置是否有效

### 13.3 部署建议

#### 同服务器部署模式(当前场景)

当监控客户端和服务端部署在同一应用中时:
- `server-url` 配置为本服务完整地址,如:`http://localhost:8082/ps-be`
- 应用既接收其他客户端心跳,也上报自身状态
- 适用于单体应用或集群中的每个节点

#### 分离部署模式

- 监控服务端独立部署,客户端分布在各个被监控应用中
- 确保网络连通性,防火墙开放必要端口
- 客户端配置统一指向监控服务端地址

#### 通用建议

- Redis缓存建议配置持久化,避免数据丢失
- 定期检查监控数据,确保监控系统正常运行
- 启动后检查日志,确认定时任务是否正常执行

### 13.4 故障排查

| 问题现象 | 可能原因 | 排查方法 |
|---------|---------|---------|
| 未收到心跳 | 1. 客户端配置错误 <br> 2. 网络不通 <br> 3. 服务端异常 <br> 4. 定时任务未启用 | 1. 检查客户端日志,搜索"监控客户端" <br> 2. 测试网络连通性 <br> 3. 检查服务端日志和状态 <br> 4. 确认@EnableScheduling注解存在 |
| 未触发报警 | 1. 未达到报警条件 <br> 2. 邮件配置错误 <br> 3. 已发送过邮件 | 1. 查看心跳数据是否正常 <br> 2. 检查监控配置 <br> 3. 检查Redis缓存 |
| 频繁报警 | 阈值配置过低 | 根据实际情况调整阈值 |
| 客户端心跳未生效 | 1. server-url未配置 <br> 2. 定时任务未启用 <br> 3. 配置文件未生效 | 1. 检查jiuxi.platform.plugin.monitor.server-url配置 <br> 2. 搜索日志"监控客户端自动配置类初始化" <br> 3. 确认spring.profiles.active配置正确 |
