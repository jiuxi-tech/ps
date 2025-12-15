# PS-BE 生产环境配置部署指南

## 📋 概述

本指南说明如何使用外置配置文件 `application-prod.yml` 部署 PS-BE 生产环境。

### 配置策略

- **配置文件**: `application-prod.yml`（唯一外置配置文件）
- **敏感信息**: 直接在配置文件中使用明文（通过文件权限保护）
- **部署简单性**: 无需环境变量脚本，一个配置文件搞定
- **安全性**: 通过文件权限（600）和服务器访问控制确保安全

## 🗂️ 部署目录结构

```
/opt/ps/
├── ps-be.jar                    # 应用 JAR 包
├── config/
│   └── application-prod.yml     # 外置生产配置（唯一配置文件）
├── upload/                      # 文件上传目录
│   └── jdfs/                    # 文件服务存储目录
└── logs/                        # 日志目录
    ├── ps-bmp-backend.log       # 应用日志
    └── heapdump.hprof           # 堆转储文件（OOM时生成）
```

## 📝 部署前准备

### 1. 修改配置文件

编辑 `config/application-prod.yml`，将所有 `TODO` 标记的配置项修改为实际值：

#### 必须修改的配置项

```yaml
# 数据库配置
app:
  database:
    datasource:
      url: jdbc:mariadb://192.168.1.100:3306/ps-bmp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
      username: ps_user
      password: YourActualDatabasePassword  # ⚠️ 修改为实际密码

# Redis 配置
  cache:
    redis:
      host: 192.168.1.101
      password: YourActualRedisPassword     # ⚠️ 修改为实际密码

# Keycloak SSO 配置
  security:
    keycloak:
      server-url: https://sso.example.com   # ⚠️ 修改为实际地址
      sso:
        client-secret: YourActualClientSecret  # ⚠️ 修改为实际密钥
        redirect:
          success-url: https://app.example.com/#/sso/login  # ⚠️ 修改为实际前端地址
          error-url: https://app.example.com/#/login
      admin:
        username: admin                     # ⚠️ 修改为实际用户名
        password: YourActualAdminPassword   # ⚠️ 修改为实际密码

# JWT 配置
    jwt:
      secret: YourActualJwtSecretKey2024    # ⚠️ 修改为实际密钥

# 文件服务配置
    file-service:
      remote:
        server-url: http://192.168.1.102:8080  # ⚠️ 修改为实际文件服务器地址

# 加密配置
    encryption:
      aes-key: YourActualAesKey2024         # ⚠️ 修改为实际密钥

# 邮件配置
spring:
  mail:
    host: smtp.example.com                  # ⚠️ 修改为实际SMTP服务器
    username: notify@example.com            # ⚠️ 修改为实际邮箱
    password: YourActualMailPassword        # ⚠️ 修改为实际密码

# 文件服务器配置
topinfo:
  jdfs:
    client:
      server:
        server-host: 192.168.1.102          # ⚠️ 修改为实际地址

# 监控配置
app:
  monitor:
    server-url: http://your-monitor-server:port/ps-be  # ⚠️ 修改为实际监控服务地址

# 监控插件邮件配置
jiuxi:
  platform:
    plugin:
      monitor:
        mail-config:
          user: notify@example.com          # ⚠️ 修改为实际邮箱
          password: YourActualMailPassword  # ⚠️ 修改为实际密码
          smtp: smtp.example.com            # ⚠️ 修改为实际SMTP服务器
```

### 2. 验证依赖服务

确保以下服务可访问：

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

## 🚀 部署步骤

### 步骤 1: 创建部署目录

```bash
mkdir -p /opt/ps/{config,upload/jdfs,logs}
```

### 步骤 2: 上传文件

```bash
# 上传 JAR 包
scp ps-be.jar root@prod-server:/opt/ps/

# 上传配置文件
scp config/application-prod.yml root@prod-server:/opt/ps/config/
```

### 步骤 3: 设置文件权限（⚠️ 重要）

```bash
# 设置 JAR 包可执行权限
chmod +x /opt/ps/ps-be.jar

# 设置配置文件权限为仅所有者可读写（非常重要！）
chmod 600 /opt/ps/config/application-prod.yml
chown root:root /opt/ps/config/application-prod.yml

# 验证权限
ls -l /opt/ps/config/application-prod.yml
# 应显示：-rw------- 1 root root
```

### 步骤 4: 验证配置文件

```bash
# 检查 YAML 语法（需要安装 Python）
python3 -c "import yaml; yaml.safe_load(open('/opt/ps/config/application-prod.yml'))"

# 检查配置文件中是否还有未修改的 TODO
grep -n "TODO" /opt/ps/config/application-prod.yml
```

### 步骤 5: 启动应用

```bash
# 直接启动（前台运行，用于测试）
java -jar /opt/ps/ps-be.jar \
  --spring.profiles.active=prod \
  --spring.config.additional-location=file:/opt/ps/config/

# 后台启动（生产环境）
nohup java -jar /opt/ps/ps-be.jar \
  --spring.profiles.active=prod \
  --spring.config.additional-location=file:/opt/ps/config/ \
  -Xms2048m -Xmx4096m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=100 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/opt/ps/logs/heapdump.hprof \
  > /opt/ps/logs/ps-be.log 2>&1 &

# 记录进程 ID
echo $! > /opt/ps/ps-be.pid
```

### 步骤 6: 验证部署

```bash
# 查看日志
tail -f /opt/ps/logs/ps-be.log

# 检查进程
ps aux | grep ps-be.jar

# 测试健康检查端点
curl http://localhost:8080/ps-be/actuator/health

# 检查端口监听
netstat -tlnp | grep 8080
```

## 🔧 启动脚本（推荐）

创建启动脚本 `/opt/ps/start.sh`：

```bash
#!/bin/bash
# PS-BE 生产环境启动脚本

APP_HOME="/opt/ps"
APP_NAME="ps-be"
JAR_FILE="${APP_HOME}/${APP_NAME}.jar"
CONFIG_DIR="${APP_HOME}/config"
LOG_DIR="${APP_HOME}/logs"
PID_FILE="${APP_HOME}/${APP_NAME}.pid"

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
  > "${LOG_DIR}/${APP_NAME}.log" 2>&1 &

# 保存 PID
echo $! > "${PID_FILE}"
echo "应用启动成功，PID: $(cat ${PID_FILE})"
echo "日志文件: ${LOG_DIR}/${APP_NAME}.log"
```

创建停止脚本 `/opt/ps/stop.sh`：

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

使用脚本：

```bash
# 启动
/opt/ps/start.sh

# 停止
/opt/ps/stop.sh

# 重启
/opt/ps/stop.sh && /opt/ps/start.sh
```

## 🔐 安全措施

### 1. 文件权限控制（必须）

```bash
# 设置配置文件为仅所有者可读写
chmod 600 /opt/ps/config/application-prod.yml
chown root:root /opt/ps/config/application-prod.yml

# 验证权限
ls -l /opt/ps/config/application-prod.yml
# 应显示：-rw------- 1 root root
```

### 2. 配置文件不纳入版本控制

在项目的 `.gitignore` 中添加：

```
# 生产配置文件
application-prod.yml
*-prod.yml
deploy/config/
```

### 3. 服务器访问控制

- 仅允许授权运维人员访问服务器
- 使用 SSH 密钥认证，禁用密码登录
- 启用防火墙，限制 SSH 访问 IP

```bash
# 示例：配置防火墙仅允许特定 IP 访问 SSH
ufw allow from 192.168.1.0/24 to any port 22
ufw enable
```

### 4. 定期更换密码

建议每 3-6 个月更换一次：
- 数据库密码
- Redis 密码
- Keycloak 客户端密钥
- JWT 密钥
- AES 密钥
- 邮件密码

### 5. 监控文件访问

```bash
# 监控配置文件的访问日志
auditctl -w /opt/ps/config/application-prod.yml -p rwa -k ps_config_access

# 查看访问日志
ausearch -k ps_config_access
```

## 🔄 配置更新流程

### 修改配置

1. 备份当前配置：

```bash
cp /opt/ps/config/application-prod.yml \
   /opt/ps/config/application-prod.yml.$(date +%Y%m%d_%H%M%S)
```

2. 修改配置文件：

```bash
vi /opt/ps/config/application-prod.yml
```

3. 验证配置语法：

```bash
python3 -c "import yaml; yaml.safe_load(open('/opt/ps/config/application-prod.yml'))"
```

4. 重启应用：

```bash
/opt/ps/stop.sh
/opt/ps/start.sh
```

5. 验证应用启动：

```bash
tail -f /opt/ps/logs/ps-be.log
curl http://localhost:8080/ps-be/actuator/health
```

## 📊 监控与日志

### 查看日志

```bash
# 实时查看日志
tail -f /opt/ps/logs/ps-bmp-backend.log

# 搜索错误日志
grep ERROR /opt/ps/logs/ps-bmp-backend.log

# 查看最近 100 行日志
tail -n 100 /opt/ps/logs/ps-bmp-backend.log
```

### 健康检查

```bash
# 应用健康检查
curl http://localhost:8080/ps-be/actuator/health

# Prometheus 指标
curl http://localhost:8080/ps-be/actuator/prometheus
```

### 日志归档

日志会自动滚动，保留策略：
- 单个日志文件最大 100MB
- 保留最近 30 天的日志
- 总日志大小上限 10GB

## ❓ 常见问题

### Q1: 应用启动失败，提示找不到配置

**问题**: `Could not resolve placeholder 'app.database.datasource.url'`

**解决方案**:
1. 检查 `--spring.config.additional-location` 参数是否正确
2. 检查配置文件路径是否存在
3. 检查配置文件权限是否正确（应该是 600）

### Q2: 数据库连接失败

**问题**: `Could not get JDBC Connection`

**解决方案**:
1. 检查数据库地址、用户名、密码是否正确
2. 检查数据库服务是否启动
3. 检查网络连接和防火墙规则
4. 使用 `telnet` 测试数据库端口是否可访问

### Q3: Redis 连接失败

**问题**: `Unable to connect to Redis`

**解决方案**:
1. 检查 Redis 地址、端口、密码是否正确
2. 检查 Redis 服务是否启动
3. 使用 `redis-cli` 测试连接

### Q4: SSO 登录失败

**问题**: `Invalid client credentials`

**解决方案**:
1. 检查 Keycloak 服务器地址是否可访问
2. 检查客户端密钥是否正确
3. 检查 Realm 和客户端 ID 是否正确
4. 检查前端重定向地址是否正确

### Q5: 如何查看配置是否生效？

**解决方案**:

启动时启用 debug 模式查看配置加载：

```bash
java -jar ps-be.jar \
  --spring.profiles.active=prod \
  --spring.config.additional-location=file:/opt/ps/config/ \
  --debug
```

查看日志中的 "Loaded config file" 信息。

## 📚 参考资料

- [Spring Boot 配置文档](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [配置文件分析设计文档](../.qoder/quests/config-file-analysis.md)
- [生产部署方案](./生产部署方案.md)

## ⚠️ 重要提醒

1. **必须修改所有 TODO 标记的配置项**
2. **必须设置配置文件权限为 600**
3. **不要将配置文件提交到 Git**
4. **定期更换密码（建议 3-6 个月）**
5. **定期备份配置文件**
6. **监控应用日志和性能指标**

---

**文档版本**: v1.0  
**更新时间**: 2024-12-02  
**维护者**: System Admin
