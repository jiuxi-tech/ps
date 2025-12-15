# PS-BE 生产环境部署文件说明

本目录包含 PS-BE 项目生产环境部署的所有必要文件和工具。

## 📂 目录结构

```
deploy/
├── config/
│   └── application-prod.yml           # 生产环境配置文件（需要修改）
│
├── README.md                          # 本文件
├── README-生产配置部署指南.md         # 详细部署指南
├── 生产配置检查清单.md                # 部署检查清单（建议打印）
├── 生产部署方案.md                    # 部署方案总览
│
├── generate-keys.sh                   # 密钥生成工具
├── validate-config.sh                 # 配置验证工具
│
├── ps.conf                            # Nginx 反向代理配置
└── restart.sh                         # 应用重启脚本
```

## 🚀 快速开始

### 步骤 1: 生成配置密钥

使用密钥生成工具自动生成所有需要的随机密钥：

```bash
chmod +x generate-keys.sh
./generate-keys.sh
```

该工具会生成：
- 数据库密码
- Redis 密码
- Keycloak 客户端密钥
- JWT 密钥
- AES 加密密钥
- 邮件密码示例

### 步骤 2: 修改配置文件

编辑生产环境配置文件：

```bash
vi config/application-prod.yml
```

将所有 `TODO` 标记的配置项修改为实际值：

#### 必须修改的配置项

1. **数据库配置**
   ```yaml
   app:
     database:
       datasource:
         url: jdbc:mariadb://你的数据库IP:3306/ps-bmp
         username: 你的数据库用户名
         password: 你的数据库密码
   ```

2. **Redis 配置**
   ```yaml
   cache:
     redis:
       host: 你的Redis IP
       password: 你的Redis密码
   ```

3. **Keycloak SSO 配置**
   ```yaml
   security:
     keycloak:
       server-url: https://你的Keycloak地址
       sso:
         client-secret: 你的客户端密钥
         redirect:
           success-url: https://你的前端地址/#/sso/login
   ```

4. **其他配置**
   - JWT 密钥
   - AES 加密密钥
   - 文件服务器地址
   - 邮件服务器配置
   - 监控服务器地址

### 步骤 3: 验证配置

使用配置验证工具检查配置文件：

```bash
chmod +x validate-config.sh
./validate-config.sh config/application-prod.yml
```

确保所有检查项都通过（绿色 ✓）。

### 步骤 4: 部署到生产服务器

参考 [README-生产配置部署指南.md](./README-生产配置部署指南.md) 进行详细部署。

简化步骤：

```bash
# 1. 创建部署目录
mkdir -p /opt/ps/{config,upload/jdfs,logs}

# 2. 上传文件
scp ps-be.jar root@prod-server:/opt/ps/
scp config/application-prod.yml root@prod-server:/opt/ps/config/

# 3. 设置权限
chmod +x /opt/ps/ps-be.jar
chmod 600 /opt/ps/config/application-prod.yml
chown root:root /opt/ps/config/application-prod.yml

# 4. 启动应用
java -jar /opt/ps/ps-be.jar \
  --spring.profiles.active=prod \
  --spring.config.additional-location=file:/opt/ps/config/ \
  -Xms2048m -Xmx4096m \
  -XX:+UseG1GC
```

## 📋 部署前检查清单

在部署前，请使用 [生产配置检查清单.md](./生产配置检查清单.md) 逐项检查：

- [ ] 所有 TODO 配置项已修改
- [ ] 配置密钥已生成并填入
- [ ] 配置文件已验证通过
- [ ] 依赖服务已确认可访问
- [ ] 文件权限已正确设置
- [ ] 安全配置已检查

## 🛠️ 工具说明

### generate-keys.sh - 密钥生成工具

**功能**：自动生成所有配置文件需要的随机密钥

**使用方法**：
```bash
./generate-keys.sh
```

**输出**：
- 在终端显示生成的密钥
- 可选择保存到文件（文件权限自动设置为 600）

**特点**：
- 生成符合强密码要求的随机密钥
- 提供完整的配置模板
- 自动设置安全权限

### validate-config.sh - 配置验证工具

**功能**：验证配置文件的完整性和正确性

**使用方法**：
```bash
./validate-config.sh /path/to/application-prod.yml
```

**检查项**：
- 文件存在性
- 文件权限（应为 600）
- YAML 语法
- TODO 项检查
- 示例值检查
- 必需配置项检查
- 密码强度检查
- 安全配置检查
- 日志配置检查
- 监控配置检查

**输出**：
- 绿色 ✓ - 检查通过
- 黄色 ⚠ - 警告（建议修复）
- 红色 ✗ - 错误（必须修复）

## 📚 文档说明

### README-生产配置部署指南.md

**内容**：
- 详细的部署步骤
- 配置文件说明
- 启动脚本模板
- 安全措施指南
- 配置更新流程
- 监控与日志
- 常见问题解答

**适用场景**：首次部署或完整部署流程参考

### 生产配置检查清单.md

**内容**：
- 部署前检查清单
- 安全检查清单
- 性能配置检查
- 监控配置检查
- 部署执行检查
- 部署后验证
- 持续运维检查

**适用场景**：部署前检查，建议打印后逐项勾选

### 生产部署方案.md

**内容**：
- 新旧部署方案对比
- 部署文档导航
- 简化部署流程（旧方案）

**适用场景**：了解部署方案演进，快速定位所需文档

## 🔐 安全提醒

### 必须执行的安全措施

1. **文件权限控制**
   ```bash
   chmod 600 /opt/ps/config/application-prod.yml
   chown root:root /opt/ps/config/application-prod.yml
   ```

2. **配置文件不纳入版本控制**
   
   在 `.gitignore` 中添加：
   ```
   application-prod.yml
   *-prod.yml
   deploy/config/
   generated-keys-*.txt
   ```

3. **定期更换密码**
   
   建议每 3-6 个月更换一次：
   - 数据库密码
   - Redis 密码
   - Keycloak 客户端密钥
   - JWT 密钥
   - AES 密钥

4. **服务器访问控制**
   - 仅允许授权人员访问
   - 使用 SSH 密钥认证
   - 配置防火墙限制访问

5. **监控文件访问**
   ```bash
   auditctl -w /opt/ps/config/application-prod.yml -p rwa -k ps_config_access
   ```

## 📞 获取帮助

### 文档顺序阅读建议

1. 首次部署：
   - README.md（本文件）→ generate-keys.sh → 修改配置 → validate-config.sh → README-生产配置部署指南.md

2. 检查验证：
   - 生产配置检查清单.md（打印后逐项检查）

3. 故障排查：
   - README-生产配置部署指南.md 的"常见问题"章节

### 相关文档链接

- [配置文件分析设计文档](../.qoder/quests/config-file-analysis.md) - 配置架构详细说明
- [API接口管理文档](../ps-be/docs/) - API 接口相关文档

## 📝 版本历史

- **v1.0** (2024-12-02) - 初始版本
  - 创建完整的生产配置文件
  - 提供密钥生成工具
  - 提供配置验证工具
  - 编写详细部署文档
  - 编写检查清单

---

**维护者**: System Admin  
**更新时间**: 2024-12-02
