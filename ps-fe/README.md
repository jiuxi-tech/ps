# JPX Admin UI Demo

## 项目简介

**JPX Admin UI Demo** 是基于JPX3.0开发平台构建的企业级后台管理系统和数字大屏展示系统。该项目采用模块化架构设计，提供了完整的后台管理功能、数据可视化大屏、表单设计器、日志中心、监控系统等功能模块。

项目特点：
- 🎯 **企业级**: 面向企业级应用的完整解决方案
- 🔧 **模块化**: 采用多包架构，各功能模块独立开发维护
- 🎨 **主题化**: 支持多主题切换，当前使用sea_blue主题
- 📱 **响应式**: 支持多终端适配
- 🚀 **现代化**: 使用Rsbuild等现代化构建工具

## 技术架构

### 核心技术栈

- **前端框架**: Vue.js 2.6.12
- **构建工具**: Rsbuild (现代化构建工具)
- **路由管理**: Vue Router 3.x
- **状态管理**: Vuex 3.x
- **UI组件库**: 自研 fb-ui 组件库
- **样式预处理**: Less
- **HTTP客户端**: Axios
- **加密库**: sm-crypto (国密算法)
- **开发语言**: JavaScript/ES6+

### 架构特点

1. **微前端架构**: 支持多应用集成，主应用、大屏应用独立部署
2. **插件化设计**: 基于插件机制，支持功能模块的动态加载
3. **组件化开发**: 基于Vue.js组件化思想，提供可复用的UI组件
4. **服务化架构**: 前后端分离，支持多服务端点配置
5. **主题定制**: 支持多主题切换和自定义主题

## 核心模块

### 1. fb-core (核心框架)
- **版本**: v1.1.784
- **功能**: 应用启动器、路由配置、服务配置、工具函数
- **主要依赖**: Vue、Vuex、Vue Router、Axios、Day.js、Lodash
- **特性**: 提供统一的应用启动和配置管理

### 2. fb-ui (UI组件库)
- **版本**: v1.1.786
- **功能**: 企业级UI组件库
- **特性**: 主题定制、响应式设计、表单验证
- **组件**: 按钮、表单、表格、弹窗、导航等基础组件

### 3. admin-base (管理后台基础模块)
- **版本**: v2.2.23
- **功能**: 后台管理基础功能
- **包含**: 
  - 用户认证系统（多种登录方式）
  - 权限管理（RBAC）
  - 菜单管理
  - 系统配置
  - 通用组件

### 4. tp-components (业务组件库)
- **版本**: v2.2.14
- **功能**: 业务相关组件和页面模板
- **特性**: 高度可配置的业务组件

### 5. screen-base (大屏基础模块)
- **功能**: 数据可视化大屏基础功能
- **特性**: 
  - 图表展示（基于第三方图表库）
  - 实时数据更新
  - 大屏布局管理
  - 响应式适配

### 6. 扩展功能模块
- **form-designer**: 可视化表单设计器
- **log-center-ui**: 日志中心管理界面
- **monitor-ui**: 系统监控界面
- **schedule-ui**: 任务调度管理界面
- **fb-third**: 第三方组件集成（地图、图表等）

## 项目结构

```
jpx-admin-ui-demo/
├── 📁 admin-base/              # 管理后台基础模块
│   ├── components/             # 通用组件（登录、对话框等）
│   ├── router/                # 路由配置
│   ├── service/               # API服务层
│   ├── store/                 # Vuex状态管理
│   ├── util/                  # 工具函数
│   └── views/                 # 页面视图
│       ├── login/             # 登录相关页面
│       ├── main/              # 主框架页面
│       └── sys/               # 系统管理页面
├── 📁 fb-core/                 # 核心框架
│   └── src/
│       ├── api/               # API封装
│       ├── boot/              # 启动配置
│       ├── router/            # 路由工具
│       ├── service/           # 服务配置
│       └── util/              # 核心工具
├── 📁 fb-ui/                   # UI组件库
│   └── packages/
│       ├── components/        # 基础组件
│       ├── theme/             # 主题样式
│       └── utils/             # 组件工具
├── 📁 fb-third/                # 第三方集成
├── 📁 tp-components/           # 业务组件库
├── 📁 screen-base/             # 大屏基础模块
├── 📁 screen/                  # 大屏应用
│   ├── components/            # 大屏组件
│   │   ├── charts/            # 图表组件
│   │   └── dialog/            # 弹窗组件
│   ├── router/                # 大屏路由
│   └── views/                 # 大屏页面
├── 📁 src/                     # 主应用源码
│   ├── App.vue                # 主应用组件
│   ├── main.js                # 应用入口
│   └── assets/                # 静态资源
├── 📁 form-designer/           # 表单设计器
├── 📁 log-center-ui/           # 日志中心
├── 📁 monitor-ui/              # 监控界面
├── 📁 schedule-ui/             # 调度管理
├── 📁 public/                  # 静态资源
├── 📁 mock/                    # Mock数据
├── 📄 project.config.js        # 项目配置
├── 📄 rsbuild.config.js        # 构建配置
└── 📄 package.json             # 依赖配置
```

## 环境要求

### 开发环境
- **Node.js**: >= 14.x
- **npm**: >= 6.x
- **操作系统**: Windows/macOS/Linux

### 浏览器支持
- Chrome >= 60
- Firefox >= 60
- Safari >= 12
- Edge >= 79
- IE >= 11 (有限支持)

## 快速开始

### 1. 克隆项目
```bash
git clone [项目地址]
cd jpx-admin-ui-demo
```

### 2. 安装依赖
```bash
npm install
```

### 3. 启动开发服务器
```bash
# 默认开发环境（端口：10801）
npm run serve

# 指定开发环境
npm run serve-dev

# 本地环境
npm run serve-local
```

### 4. 访问应用
- **管理后台**: http://localhost:10801
- **数字大屏**: http://localhost:10801/screen.html

## 环境配置

### 环境变量

#### 开发环境 (.env.dev)
```bash
NODE_ENV=development
VUE_APP_URL=http://127.0.0.1:8088/          # 后端API地址
VUE_REPORT_URL=http://127.0.0.1:8077/       # 报表服务地址
```

#### 生产环境 (.env.prod)
```bash
NODE_ENV=production
VUE_APP_URL=http://192.168.90.77:8080/      # 生产环境API地址
```

#### 测试环境 (.env.test)
```bash
NODE_ENV=test
# 测试环境配置
```

### 项目配置 (project.config.js)

```javascript
module.exports = {
  name: 'jpx-admin',           // 系统名称
  theme: 'pc3',                    // UI框架主题
  TX_THEME: 'sea_blue',            // 项目主题
  logoTitle: 'JPX3.0开发平台',
  title: 'JPX3.0',                // 浏览器标题
  auth: 'token',                   // 认证方式
  server: {
    host: '0.0.0.0',
    port: 10801,
    https: false
  },
  // 服务配置
  service: [
    {
      name: 'service',
      target: process.env.VUE_APP_URL,
      baseURL: '/chemicalpark-manage-app'
    }
  ]
}
```

## 构建和部署

### 构建命令
```bash
# 构建生产版本
npm run build

# 预览构建结果
npm run preview

# 代码检查
npm run eslint
```

### 部署步骤

1. **构建项目**
   ```bash
   npm run build
   ```

2. **部署文件**
   - 将构建生成的 `dist` 目录内容部署到Web服务器
   - 配置Web服务器（推荐Nginx）

3. **Nginx配置示例**
   ```nginx
   server {
       listen 80;
       server_name your-domain.com;
       root /path/to/dist;
       index index.html;
       
       # 处理Vue Router的history模式
       location / {
           try_files $uri $uri/ /index.html;
       }
       
       # API代理
       location /api/ {
           proxy_pass http://backend-server:8080/;
       }
   }
   ```

## 功能特性

### 🔐 用户认证系统
- 多种登录方式（账号密码、扫码登录、短信验证）
- Token认证机制
- 密码加密传输（国密SM2/SM3/SM4）
- 单点登录(SSO)支持
- 部门选择登录

### 👥 权限管理
- 基于角色的权限控制(RBAC)
- 菜单权限管理
- 按钮级权限控制
- 数据权限隔离
- 动态路由生成

### 🎛️ 系统管理
- 用户管理
- 角色管理
- 菜单管理
- 部门管理
- 系统配置
- 字典管理

### 📊 数据可视化
- 多种图表类型支持
- 实时数据展示
- 大屏展示模式
- 响应式布局
- 自定义图表配置

### 🛠️ 开发工具
- 可视化表单设计器
- 代码生成器
- 接口文档管理
- 日志管理系统
- 系统监控
- 任务调度

### 🎨 主题定制
- 多主题支持
- 自定义主题色
- 暗黑模式支持
- 组件样式定制

## 开发规范

### 代码规范
- 使用ESLint进行代码检查
- 遵循Vue.js官方风格指南
- 组件命名采用PascalCase
- 文件命名采用kebab-case
- 使用TypeScript类型注解（推荐）

### 目录规范
```
components/     # 可复用组件
views/         # 页面组件
service/       # API服务
store/         # 状态管理
util/          # 工具函数
assets/        # 静态资源
router/        # 路由配置
```

### Git规范
- 提交信息格式：`type(scope): description`
- 类型：feat, fix, docs, style, refactor, test, chore
- 功能开发使用feature分支
- 发布使用release分支

## API接口

### 服务配置
项目支持多服务端点配置：

- **主服务**: `/chemicalpark-manage-app` - 主要业务API
- **日志服务**: `/log-center` - 日志管理API
- **报表服务**: `/report` - 报表相关API

### 请求配置
- 请求方式：POST（默认）
- 超时时间：10秒
- 支持跨域：withCredentials: true
- 支持Mock数据

## 常见问题

### Q: 如何切换主题？
A: 在 `project.config.js` 中修改 `theme` 和 `TX_THEME` 配置。

### Q: 如何添加新的功能模块？
A: 参考现有模块结构，在对应目录下创建新模块，并在主配置中注册。

### Q: 如何配置代理？
A: 在 `project.config.js` 的 `service` 数组中添加新的服务配置。

### Q: 如何启用Mock数据？
A: 在服务配置中设置 `mock` 字段为Mock服务地址。

## 更新日志

### v1.0.0 (当前版本)
- ✨ 初始版本发布
- ✨ 完成基础功能模块
- ✨ 支持多主题切换
- ✨ 集成数据可视化大屏
- ✨ 支持表单设计器
- ✨ 集成日志中心和监控系统

## 技术支持

### 联系方式
- **作者**: lincong
- **邮箱**: lincong1987@gmail.com
- **公司**: JPX

### 相关链接
- [Vue.js官方文档](https://cn.vuejs.org/)
- [Rsbuild文档](https://rsbuild.dev/)
- [JPX官网](https://www.alilaoba.cn/)


## 许可证

ISC License

---

**Copyright © 2001-2021 JPX. 保留所有权利**

> 本项目基于JPX3.0开发平台构建，为企业级应用提供完整的前端解决方案。


