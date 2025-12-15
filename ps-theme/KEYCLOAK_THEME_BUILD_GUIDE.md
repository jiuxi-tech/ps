# Keycloak 自定义主题打包部署文档

## 项目概述

本项目是基于 **Keycloakify v11** 的自定义登录主题，采用 React + TypeScript 开发，为中共陕西省委党校（陕西行政学院）业务中台提供统一身份认证界面。

主题名称：**ps-theme**

---

## 环境要求

### 必需环境

- **Node.js**: v18.0.0+ 或 v20.0.0+
- **npm**: 建议使用最新版本
- **Maven**: 3.1.1+（用于构建 JAR 包）
- **Java**: JDK 7+（Maven 运行需要）

### 安装 Maven（根据操作系统）

**Windows**:
```powershell
# 使用 Chocolatey
choco install openjdk
choco install maven

# 或从官网下载：https://maven.apache.org/download.cgi
```

**macOS**:
```bash
brew install maven
```

**Linux (Debian/Ubuntu)**:
```bash
sudo apt-get install maven
```

### 验证环境

```bash
node --version    # 应显示 v18+ 或 v20+
npm --version
mvn --version     # 应显示 Maven 3.1.1+
```

---

## 快速开始

### 1. 安装依赖

```bash
cd d:\projects\ps\ps-theme
npm install
```

### 2. 本地开发预览

```bash
npm run dev
```

访问 `http://localhost:5173` 查看主题预览（Storybook 模式）。

### 3. 使用 Storybook 测试

```bash
npm run storybook
```

访问 `http://localhost:6006` 在隔离环境中测试各个登录页面组件。

---

## 主题打包

### 完整打包命令

```bash
npm run build-keycloak-theme
```

该命令会执行以下步骤：
1. 运行 TypeScript 编译检查（`tsc`）
2. 使用 Vite 构建生产版本（`vite build`）
3. 调用 Keycloakify 打包工具生成 JAR 文件（`keycloakify build`）

### 打包输出

打包完成后，JAR 文件位于：

```
dist_keycloak/
├── keycloak-theme-for-kc-22-to-25.jar          # 适用于 Keycloak 22-25 版本
└── keycloak-theme-for-kc-all-other-versions.jar # 适用于其他版本
```

**默认行为**：Keycloakify 会为不同版本的 Keycloak 生成多个 JAR 文件。

### 自定义目标版本（可选）

如果只需要支持特定 Keycloak 版本，可在 `vite.config.ts` 中配置：

```typescript
keycloakify({
    // ... 其他配置
    keycloakVersionTargets: {
        "25": true,  // 仅生成 Keycloak 25 版本
        "all-other-versions": false
    }
})
```

详细配置参考：[Keycloakify 文档](https://docs.keycloakify.dev/features/compiler-options/keycloakversiontargets)

---

## 主题配置说明

### 主题信息（vite.config.ts）

```typescript
keycloakify({
    accountThemeImplementation: "none",  // 不生成账户主题
    themeName: "ps-theme",               // 主题名称，需与 Keycloak 配置一致
    extraThemeProperties: [
        "parent=keycloak",               // 继承 Keycloak 默认样式
    ]
})
```

### 主要文件结构

```
src/login/
├── Template.tsx              # 自定义页面模板（党政风格）
├── KcPage.tsx               # 登录页面路由配置
├── KcContext.ts             # Keycloak 上下文类型定义
├── i18n.ts                  # 国际化配置
├── pages/
│   └── HomePage.tsx         # 主页自定义组件（info.ftl）
└── resources/
    ├── css/
    │   └── login.css        # 自定义样式（党政风格 + 主页 + 注销页）
    └── messages/
        └── messages_zh_CN.properties  # 中文翻译文件

theme/ps-theme/login/
└── logout.ftl               # 注销页 FreeMarker 模板（自动跳转）
```

---

## 部署到 Keycloak

### 方法一：直接复制 JAR 文件

1. 将 JAR 文件复制到 Keycloak 部署目录：

```bash
# Standalone 模式
cp dist_keycloak/keycloak-theme-for-kc-22-to-25.jar \
   /path/to/keycloak/standalone/deployments/

# Docker 容器（挂载卷）
docker cp dist_keycloak/keycloak-theme-for-kc-22-to-25.jar \
   keycloak:/opt/keycloak/providers/
```

2. 重启 Keycloak 服务：

```bash
# Standalone
/path/to/keycloak/bin/kc.sh restart

# Docker
docker restart keycloak
```

### 方法二：Docker Compose 挂载（推荐）

在 `docker-compose.yml` 中配置：

```yaml
services:
  keycloak:
    image: quay.io/keycloak/keycloak:25.0
    volumes:
      - ./ps-theme/dist_keycloak/keycloak-theme-for-kc-22-to-25.jar:/opt/keycloak/providers/ps-theme.jar
    # ... 其他配置
```

### 方法三：Keycloak 管理控制台启用主题

1. 登录 Keycloak 管理控制台（默认：`http://localhost:8080/admin`）
2. 选择你的 **Realm**（如 `ps-realm`）
3. 进入 **Realm Settings** > **Themes** 标签
4. 在 **Login Theme** 下拉框中选择 **ps-theme**
5. 点击 **Save**

---

## 主题验证

### 检查主题是否加载

1. 访问 Keycloak 登录页面：
   ```
   http://localhost:8080/realms/{your-realm}/account
   ```

2. 检查页面标题是否为：
   ```
   统一身份认证 - 中共陕西省委党校（陕西行政学院）
   ```

3. 检查页面样式是否应用党政风格（红色横幅、金色装饰等）

### 主页主题验证

访问 Keycloak 主页（info.ftl）页面：
```
http://localhost:8080/realms/{your-realm}/protocol/openid-connect/auth?client_id=account&redirect_uri=http://localhost:8080/realms/{your-realm}/account/&response_type=code&scope=openid
```

登录后应该看到：
- 党政风格的主页布局
- 用户信息卡片（用户名、邮箱）
- 系统信息卡片（Realm 名称）
- 快捷入口（账户管理、修改密码、安全设置、退出登录）

### 注销页验证

1. 访问注销 URL：
   ```
   http://localhost:8080/realms/{your-realm}/protocol/openid-connect/logout
   ```

2. 应该看到自动跳转页面，包含：
   - “正在退出登录，稍候...”文案
   - 加载动画（旋转圆圈）
   - 自动跳转到登录页

**注意**：注销功能需要在 Keycloak Client 配置中设置 `Valid Post Logout Redirect URIs`。

详细实现说明请查看：[LOGOUT_IMPLEMENTATION_GUIDE.md](./LOGOUT_IMPLEMENTATION_GUIDE.md)

### 浏览器控制台检查

打开浏览器开发者工具（F12），确认：
- 无 CSS 加载错误
- 无 JavaScript 报错
- 字体、图标正常显示

---

## 常见问题

### Q1: 打包时提示 "mvn command not found"

**解决**：安装 Maven 并确保 `mvn` 命令在系统 PATH 中：

```bash
# Windows PowerShell
$env:Path += ";C:\Program Files\Apache\maven\bin"

# Linux/macOS
export PATH=$PATH:/usr/local/maven/bin
```

### Q2: JAR 文件上传后主题不显示

**排查步骤**：
1. 检查 JAR 文件是否正确放置在 `providers/` 或 `deployments/` 目录
2. 检查 Keycloak 日志是否有主题加载错误
3. 确认 Realm 的 Login Theme 已选择 **ps-theme**
4. 尝试清除浏览器缓存

### Q3: 样式显示不正常

**检查**：
1. 确认 `resources/css/styles.css` 已正确打包
2. 检查浏览器控制台是否有 CSS 加载失败
3. 验证 `parent=keycloak` 配置是否正确

### Q4: 中文翻译未生效

**确认**：
1. 文件路径：`src/login/resources/messages/messages_zh_CN.properties`
2. 文件编码为 **UTF-8**
3. Keycloak Realm 语言设置为中文（zh-CN）

---

## 持续集成（CI/CD）

### GitHub Actions 自动发布

项目已配置 GitHub Actions 工作流（`.github/workflows/ci.yaml`），每次推送新版本时自动构建并发布 JAR 文件。

**发布新版本**：

1. 更新 `package.json` 中的版本号：
   ```json
   {
     "version": "1.0.1"
   }
   ```

2. 提交并推送到 GitHub：
   ```bash
   git add package.json
   git commit -m "chore: bump version to 1.0.1"
   git push origin main
   ```

3. JAR 文件将自动发布到 GitHub Releases

**启用工作流**：
- 进入 GitHub 仓库 **Settings** > **Actions** > **Workflow permissions**
- 选择 **Read and write permissions**

---

## 开发调试

### 修改样式后热更新

```bash
npm run dev
```

编辑 `src/login/resources/css/styles.css`，保存后浏览器自动刷新。

### 修改组件代码

编辑 `src/login/Template.tsx` 或 `KcPage.tsx`，使用 Storybook 实时预览：

```bash
npm run storybook
```

### 代码格式化

```bash
npm run format
```

使用 Prettier 格式化所有代码文件。

---

## 技术栈

- **React 18.2** - UI 框架
- **TypeScript 5.2** - 类型安全
- **Vite 5** - 构建工具
- **Keycloakify 11.9** - Keycloak 主题框架
- **Storybook 8** - 组件开发环境

---

## 相关文档

- [Keycloakify 官方文档](https://docs.keycloakify.dev)
- [Keycloak 主题开发指南](https://www.keycloak.org/docs/latest/server_development/#_themes)
- [Vite 配置文档](https://vitejs.dev/config/)

---

## 联系与支持

如有问题或建议，请联系项目维护团队。
