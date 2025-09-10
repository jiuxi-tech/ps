# PS-BMP 管理端 UI 开发文档

## 项目概述

PS-BMP 管理端 UI 是基于 Vue.js 2.6 和 JPX3.0 开发平台构建的企业级前端应用。项目采用模块化架构，支持多应用、多主题，提供完整的后台管理功能和数据可视化大屏。

### 基本信息
- **项目名称**: ps-bmp-admin-ui
- **版本**: v1.0.0
- **作者**: lincong <lincong1987@gmail.com>
- **许可证**: ISC
- **开发平台**: JPX3.0

## 技术栈

### 核心技术
- **前端框架**: Vue.js 2.6.12
- **路由管理**: Vue Router 3.x
- **状态管理**: Vuex 3.x
- **构建工具**: Rsbuild (现代化构建工具)
- **样式预处理**: Less
- **HTTP客户端**: Axios
- **加密库**: sm-crypto

### 开发工具
- **代码检查**: ESLint
- **模拟数据**: MockJS
- **包管理**: npm
- **版本控制**: Git

## 项目架构

### 目录结构
```
ps-bmp-admin-ui/
├── @fb/                        # 框架组件包目录
│   ├── admin-base/             # 管理后台基础模块
│   ├── fb-assets/              # 静态资源包
│   ├── fb-core/                # 核心框架
│   ├── fb-third/               # 第三方组件集成
│   ├── fb-ui/                  # UI组件库
│   ├── form-designer/          # 表单设计器
│   ├── log-center-ui/          # 日志中心
│   ├── monitor-ui/             # 监控界面
│   ├── schedule-ui/            # 任务调度
│   ├── screen-base/            # 大屏基础模块
│   └── tp-components/          # 业务组件库
├── src/                        # 主应用源码
│   ├── App.vue                 # 主应用组件
│   ├── main.js                 # 应用入口
│   ├── assets/                 # 静态资源
│   ├── router/                 # 路由配置
│   ├── store/                  # 状态管理
│   └── views/                  # 页面组件
├── screen/                     # 大屏应用
│   ├── ScreenApp.vue           # 大屏主组件
│   ├── main.js                 # 大屏入口
│   ├── assets/                 # 大屏静态资源
│   ├── components/             # 大屏组件
│   ├── router/                 # 大屏路由
│   └── views/                  # 大屏页面
├── public/                     # 公共静态资源
├── mock/                       # Mock数据
├── tasks/                      # 项目任务和文档
├── .env.dev                    # 开发环境配置
├── .env.prod                   # 生产环境配置
├── .env.test                   # 测试环境配置
├── project.config.js           # 项目配置
├── rsbuild.config.js           # 构建配置
└── package.json                # 依赖配置
```

### 模块化架构

项目采用模块化架构，主要模块包括：

#### 1. 核心模块 (@fb/fb-core)
- 应用启动器和配置管理
- 路由配置和权限控制
- 服务配置和请求拦截
- 工具函数和通用逻辑

#### 2. UI组件库 (@fb/fb-ui)
- 企业级UI组件
- 主题定制支持
- 响应式设计
- 表单验证组件

#### 3. 管理后台基础 (@fb/admin-base)
- 用户认证系统
- 权限管理(RBAC)
- 菜单管理
- 系统配置
- 通用后台组件

#### 4. 业务组件 (@fb/tp-components)
- 业务相关组件
- 页面模板
- 可配置组件

#### 5. 扩展功能模块
- **form-designer**: 可视化表单设计器
- **log-center-ui**: 日志中心管理界面
- **monitor-ui**: 系统监控界面
- **schedule-ui**: 任务调度管理界面
- **screen-base**: 数据可视化大屏基础功能

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
cd ps-bmp-admin-ui
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

### 环境变量文件

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

主要配置项说明：

```javascript
module.exports = {
  name: 'ps-bmp-admin',           // 系统名称
  theme: 'pc3',                   // UI框架主题
  TX_THEME: 'sea_blue',           // 项目主题
  logoTitle: 'JPX3.0',            // Logo标题
  title: 'JPX3.0',                // 浏览器标题
  auth: 'token',                  // 认证方式
  
  // 服务器配置
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
  ],
  
  // 路由配置
  router: {
    progress: true,                 // 启用进度条
    whiteList: ['/login/*', '/register', '/sso/main'],
    startMode: 'semiAuto',          // 启动模式
    indexPath: './index.js',        // 主路由配置
    mainPath: '/main',              // 一级路由路径
    loginPath: '/login',            // 登录页面
    adminPath: 'index.html'         // 后台页面路径
  }
}
```

## 开发指南

### 代码规范

#### 1. 文件命名
- 组件文件：使用 PascalCase，如 `UserProfile.vue`
- 工具文件：使用 camelCase，如 `dateUtils.js`
- 样式文件：使用 kebab-case，如 `user-profile.less`

#### 2. 组件开发
```vue
<template>
  <div class="component-name">
    <!-- 模板内容 -->
  </div>
</template>

<script>
export default {
  name: 'ComponentName',
  props: {
    // 属性定义
  },
  data() {
    return {
      // 数据定义
    }
  },
  methods: {
    // 方法定义
  }
}
</script>

<style lang="less" scoped>
.component-name {
  // 样式定义
}
</style>
```

#### 3. 路由配置
```javascript
// router/index.js
export default {
  path: '/module',
  name: 'ModuleName',
  component: () => import('@/views/ModuleName.vue'),
  meta: {
    title: '模块名称',
    requiresAuth: true
  }
}
```

#### 4. 服务调用
```javascript
// 使用全局服务
this.$svc.service.post('/api/endpoint', data)
  .then(response => {
    // 处理响应
  })
  .catch(error => {
    // 处理错误
  })
```

### 主题定制

项目支持多主题切换，主题配置在 `project.config.js` 中：

```javascript
// 主题配置
theme: 'pc3',           // 主题名称
TX_THEME: 'sea_blue',   //项目主题
```

可用主题：
- `pc3`: 默认主题
- `classic_blue`: 经典蓝主题
- `royal-blue`: 皇家蓝主题
- `sea_blue`: 海蓝主题

### 权限管理

项目采用基于角色的访问控制(RBAC)：

#### 1. 路由权限
```javascript
// 路由白名单配置
whiteList: ['/login/*', '/register', '/sso/main']

// 路由守卫
beforeEach(to, from, next) {
  // 权限验证逻辑
}
```

#### 2. 组件权限
```vue
<template>
  <div v-if="hasPermission('user:create')">
    <!-- 需要权限的内容 -->
  </div>
</template>
```

### 状态管理

使用 Vuex 进行状态管理：

```javascript
// store/modules/user.js
export default {
  namespaced: true,
  state: {
    userInfo: null
  },
  mutations: {
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
    }
  },
  actions: {
    async getUserInfo({ commit }) {
      // 获取用户信息
    }
  }
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

#### 1. 构建项目
```bash
npm run build
```

#### 2. 部署文件
将构建生成的 `dist` 目录内容部署到Web服务器

#### 3. Nginx配置示例
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
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
    
    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

## 功能特性

### 🔐 用户认证系统
- 多种登录方式（账号密码、扫码登录、短信验证）
- Token认证机制
- 密码加密（支持非对称加密）
- 单点登录(SSO)支持

### 👥 权限管理
- 基于角色的访问控制(RBAC)
- 菜单权限控制
- 按钮级权限控制
- 数据权限控制

### 🎨 界面特性
- 响应式设计
- 多主题支持
- 国际化支持
- 暗黑模式

### 📊 数据可视化
- 图表组件集成
- 数据大屏支持
- 实时数据更新
- 自适应布局

### 🔧 开发工具
- 可视化表单设计器
- 代码生成器
- Mock数据支持
- 热重载开发

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

### 接口调用示例
```javascript
// 主服务调用
this.$svc.service.post('/user/list', {
  page: 1,
  size: 10
})

// 日志服务调用
this.$svc.logService.post('/log/query', {
  startTime: '2024-01-01',
  endTime: '2024-01-31'
})

// 报表服务调用
this.$svc.thirdService.get('/report/export', {
  params: { id: 123 }
})
```

## 常见问题

### Q: 如何切换主题？
A: 在 `project.config.js` 中修改 `theme` 和 `TX_THEME` 配置。

### Q: 如何添加新的功能模块？
A: 参考现有模块结构，在对应目录下创建新模块，并在主配置中注册。

### Q: 如何配置代理？
A: 在 `project.config.js` 的 `service` 数组中添加新的服务配置。

### Q: 如何启用Mock数据？
A: 在服务配置中设置 `mock` 字段为Mock服务地址。

### Q: 如何解决跨域问题？
A: 配置代理服务器或在后端设置CORS头。

### Q: 如何优化构建性能？
A: 使用Rsbuild的代码分割、懒加载等特性。

## 调试指南

### 开发工具
- 使用Vue DevTools进行组件调试
- 使用浏览器开发者工具进行网络调试
- 使用ESLint进行代码质量检查

### 日志配置
```javascript
// project.config.js
logger: {
  enabled: true,
  name: 'main',
  pattern: '%d{yyyy-MM-dd HH:mm:ss,SSS} [%c] %-5p - %m{1}%n',
  show: false
}
```

### 错误处理
```javascript
// 全局错误处理
Vue.config.errorHandler = (err, vm, info) => {
  console.error('Vue Error:', err)
  console.error('Component:', vm)
  console.error('Info:', info)
}
```

## 性能优化

### 1. 代码分割
```javascript
// 路由懒加载
const UserProfile = () => import('@/views/UserProfile.vue')
```

### 2. 组件懒加载
```javascript
// 组件懒加载
components: {
  LazyComponent: () => import('@/components/LazyComponent.vue')
}
```

### 3. 图片优化
- 使用WebP格式
- 图片懒加载
- 图片压缩

### 4. 缓存策略
- HTTP缓存
- 浏览器缓存
- CDN缓存

## 更新日志

### v1.0.0 (当前版本)
- ✨ 初始版本发布
- ✨ 完成基础功能模块
- ✨ 支持多主题切换
- ✨ 集成数据可视化大屏
- ✨ 支持表单设计器
- ✨ 集成日志中心和监控系统
- ✨ 迁移到Rsbuild构建工具

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

**Copyright © 2001-2025 JPX3.0. 保留所有权利**

> 本项目基于JPX3.0开发平台构建，为企业级应用提供完整的前端解决方案。