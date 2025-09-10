# 项目路由清单

> 生成时间：2024年12月19日  
> 项目：jpx-admin-ui-demo  
> 扫描范围：整个项目目录

## 概述

本文档记录了项目中所有的路由配置信息，包括主应用路由、组件包路由以及大屏应用路由。项目采用 Vue Router 3.x 进行路由管理，支持多应用架构。

## 路由配置架构

### 路由启动模式
- **启动模式**: `semiAuto` (半自动模式)
- **主路由路径**: `/main`
- **登录路径**: `/login`
- **索引路径**: `./index.js`
- **路由模式**: `hash` (默认)

### 路由白名单
```javascript
whiteList: ['/login/*', '/register', '/sso/main']
```

## 主要路由配置文件

### 1. 核心系统路由

#### @fb/fb-core/src/router/system/systemRouter.js
**系统基础路由配置**
```javascript
[
  {
    path: '/',
    name: 'SystemApplicationRoot',
    component: () => import('../../views/SystemApplicationRoot')
  },
  {
    path: '/login',
    name: 'login',
    meta: { title: '登录' },
    component: () => import('../../views/SystemApplicationLogin')
  },
  {
    path: '/login1',
    name: 'login1',
    meta: { title: '登录1' },
    component: () => import('../../views/login/SystemApplicationLogin')
  },
  {
    path: '/main1',
    name: 'main1',
    component: () => import('../../views/SystemApplicationLayout'),
    children: [
      {
        path: '404',
        name: 'notFound',
        alias: '/404',
        component: () => import('../../views/common/NotFound')
      },
      {
        path: '403',
        name: 'notAllow',
        alias: '/403',
        component: () => import('../../views/common/NotAllow')
      },
      {
        path: '/500',
        name: 'exception',
        alias: '/500',
        component: () => import('../../views/common/Exception')
      }
    ]
  },
  {
    path: '*',
    name: 'UnknowPage',
    component: () => import('../../views/common/UnknowPage')
  }
]
```

#### @fb/fb-core/src/router/demo/demoRouter.js
**演示路由配置**
```javascript
[
  {
    path: '/demo',
    children: [
      {
        path: '/button',
        component: {
          template: `<div>demo button</div>`
        }
      }
    ]
  }
]
```

### 2. 管理后台路由

#### @fb/admin-base/router/index.js
**后台管理主路由**
```javascript
[
  {
    path: '/',
    name: 'SystemApplicationRoot',
    redirect: '/main'
  },
  {
    path: '/login', // 来自 projectConfig.router.loginPath
    name: 'login',
    meta: { title: '登录' },
    component: () => import('../views/login/SystemLogin2.vue'),
    children: [
      {
        path: 'register',
        meta: { title: '注册' },
        component: () => import('../views/login/LoginRegiter.vue')
      },
      {
        path: 'forget-pass',
        meta: { title: '忘记密码' },
        component: () => import('../views/login/LoginForget.vue')
      }
    ]
  },
  {
    path: '/login2',
    meta: {
      title: '大屏登录',
      rootPath: '/login2',
      copyright: `<p>浙江省xxx</p><p>浙江省xxx</p>`
    },
    component: () => import('../views/login/MidnightBlueLogin.vue'),
    children: [
      {
        path: 'register',
        meta: { title: '注册' },
        component: () => import('../views/login/LoginRegiter.vue')
      },
      {
        path: 'forget-pass',
        meta: { title: '忘记密码' },
        component: () => import('../views/login/LoginForget.vue')
      }
    ]
  },
  {
    path: '/sso/main',
    meta: { title: '单点登陆缓冲页' },
    component: () => import('../views/common/sso/main.vue')
  },
  {
    path: '/common/document/editor',
    meta: { title: '新tabbar' },
    component: () => import('../views/common/document/editor.vue')
  },
  {
    path: '/main', // 来自 projectConfig.router.mainPath
    name: 'main',
    meta: { title: '后台1' },
    component: () => import('../views/main/SystemApplicationLayout.vue'),
    children: [
      {
        path: '/refresh',
        name: 'refresh',
        component: () => import('../views/main/common/refresh.vue')
      },
      {
        path: '404',
        name: 'notFound',
        alias: '/404',
        component: () => import('../views/main/common/NotFound.vue')
      },
      {
        path: '403',
        name: 'notAllow',
        alias: '/403',
        component: () => import('../views/main/common/NotAllow.vue')
      },
      {
        path: '/500',
        name: 'exception',
        alias: '/500',
        component: () => import('../views/main/common/Exception.vue')
      }
    ]
  },
  {
    path: '*',
    name: 'UnknowPage',
    component: () => import('../views/main/common/UnknowPage.vue')
  }
]
```

#### @fb/admin-base/router/sys/index/index.js
**系统管理模块路由**
```javascript
[
  {
    path: '/sys/index/index',
    meta: { title: '字典管理' },
    name: 'index',
    component: () => import('../../../views/sys/index/index-old.vue')
  },
  {
    path: '/sys/index/index2',
    meta: {},
    name: 'index2',
    component: () => import('../../../views/sys/index/index2.vue')
  },
  {
    path: '/sys/index/index3',
    name: 'index3',
    meta: {},
    component: () => import('../../../views/sys/index/index3.vue')
  },
  {
    path: '/sys/index/index-m',
    name: 'index-m',
    meta: {},
    component: () => import('../../../views/sys/index/index-m.vue')
  }
]
```

### 3. 大屏应用路由

#### @fb/screen-base/src/router/index.js
**大屏基础路由**
```javascript
[
  {
    path: '/',
    redirect: '/index'
  },
  {
    path: '*',
    meta: { title: '404' },
    component: {
      template: `<div>页面不存在</div>`
    }
  },
  {
    path: '/index',
    component: () => import('../views/index.vue')
  },
  {
    path: '/test',
    component: () => import('../views/test.vue')
  },
  // 温岭留存
  {
    path: '/monitor-warn/wenling/zhfxts',
    component: () => import('../views/monitor-warn/wenling/zhfxts.vue')
  },
  {
    path: '/monitor-warn/wenling/znjcyj',
    component: () => import('../views/monitor-warn/wenling/znjcyj.vue')
  },
  {
    path: '/monitor-warn/wenling/znjcyj-ent',
    component: () => import('../views/monitor-warn/wenling/znjcyj-ent.vue')
  },
  // 主题测试页
  {
    path: '/theme/midnight-blue',
    component: () => import('../views/theme/midnight-blue.vue')
  },
  {
    path: '/theme/dark-blue',
    component: () => import('../views/theme/DarkBlue.vue')
  },
  {
    path: '/theme/sky-blue',
    component: () => import('../views/theme/SkyBlue.vue')
  },
  {
    path: '/theme/royal-blue',
    component: () => import('../views/theme/RoyalBlue.vue')
  },
  {
    path: '/theme/navy-blue',
    component: () => import('../views/theme/NavyBlue.vue')
  },
  {
    path: '/theme/cornflower-blue',
    component: () => import('../views/theme/CornflowerBlue.vue')
  },
  {
    path: '/test1',
    component: () => import('../views/text1.vue')
  }
]
```

#### screen/router/index.js
**大屏应用路由**
```javascript
[
  {
    path: '/',
    redirect: '/temp1'
  },
  {
    path: '/index',
    meta: { title: '浙江省概况' },
    component: () => import('../views/index.vue')
  },
  {
    path: '/temp1',
    meta: { title: '浙江省概况' },
    component: () => import('../views/index.vue')
  },
  {
    path: '*',
    meta: { title: '404' },
    component: {
      template: `<div>页面不存在</div>`
    }
  }
]
```

### 4. 项目根路由

#### src/router/index.js
**项目主路由（空配置）**
```javascript
let index = []
export default index
```

## 组件包路由结构

### 路由目录结构
```
@fb/
├── admin-base/
│   └── router/
│       ├── index.js                    # 主路由配置
│       └── sys/                        # 系统管理路由
│           ├── accountthrid/
│           ├── city/
│           ├── cron/
│           ├── custom/
│           ├── dept/
│           ├── dict/
│           ├── ent/
│           ├── index/
│           │   └── index.js           # 系统首页路由
│           ├── inter/
│           ├── licence/
│           ├── logger/
│           ├── menu/
│           ├── parameterconfig/
│           ├── person/
│           ├── report/
│           ├── role/
│           └── third/
├── fb-core/
│   └── src/
│       └── router/
│           ├── demo/
│           │   └── demoRouter.js       # 演示路由
│           └── system/
│               └── systemRouter.js     # 系统基础路由
├── form-designer/
│   └── src/
│       └── router/
│           └── sys/
│               └── custom/
├── log-center-ui/
│   └── src/
│       └── router/
│           └── sys/
│               └── tpoperatelog/
├── monitor-ui/
│   └── src/
│       └── router/                     # 监控模块路由
├── schedule-ui/
│   └── src/
│       └── router/                     # 调度模块路由
├── screen-base/
│   └── src/
│       └── router/
│           └── index.js               # 大屏基础路由
└── tp-components/
    └── src/
        └── router/                     # 通用组件路由
```

## 路由工具和配置

### 路由工具函数
- **@fb/fb-core/src/util/routerTool.js**: 路由处理工具
  - `autoRouter()`: 自动路由处理
  - `injectRouter()`: 路由注入
  - `handle()`: 路由处理
  - `initRoutes()`: 路由初始化
  - `flatFilesToRouteArr()`: 文件扁平化为路由数组

- **@fb/fb-core/src/util/routerConfig.js**: 路由配置工具
  - `setRouterBefore()`: 设置路由前置守卫

### 路由生成模式
项目支持多种路由生成模式：
1. **manual**: 手动模式
2. **semiAutoWhite**: 半自动白名单模式
3. **semiAuto**: 半自动模式（当前使用）
4. **routerBeforeEach**: 守卫前置生成模式

## 路由使用统计

### Vue Router 使用情况
- **导航方法使用**:
  - `$router.push()`: 程序化导航
  - `$router.replace()`: 替换当前路由
  - `$router.back()`: 返回上一页
  - `$router.go()`: 历史记录导航

- **路由加密解密**:
  - `$router.encrypt()`: 参数加密
  - `$router.decrypt()`: 参数解密

- **组件内使用**:
  - `<router-view>`: 路由视图组件
  - `<router-link>`: 路由链接组件

### 路由元信息使用
- `meta.title`: 页面标题
- `meta.whiteList`: 白名单标识
- `meta.copyright`: 版权信息
- `meta.rootPath`: 根路径
- `meta.hijack`: 登录劫持函数

## 特殊路由配置

### 错误页面路由
- **404**: `/404` - 页面不存在
- **403**: `/403` - 无权限访问
- **500**: `/500` - 服务器错误
- **通配符**: `*` - 未知页面

### 登录相关路由
- **主登录**: `/login`
- **大屏登录**: `/login2`
- **注册**: `/login/register`
- **忘记密码**: `/login/forget-pass`
- **单点登录**: `/sso/main`

### 主题路由
- `/theme/midnight-blue`: 午夜蓝主题
- `/theme/dark-blue`: 深蓝主题
- `/theme/sky-blue`: 天蓝主题
- `/theme/royal-blue`: 皇家蓝主题
- `/theme/navy-blue`: 海军蓝主题
- `/theme/cornflower-blue`: 车矢菊蓝主题

## 路由配置建议

### 最佳实践
1. **路由命名**: 使用有意义的路由名称
2. **路径设计**: 遵循RESTful风格
3. **元信息**: 合理使用meta字段
4. **懒加载**: 使用动态import进行组件懒加载
5. **路由守卫**: 合理使用路由守卫进行权限控制

### 维护建议
1. **定期更新**: 定期更新路由清单文档
2. **路由测试**: 确保所有路由可正常访问
3. **性能优化**: 优化路由加载性能
4. **文档同步**: 保持路由文档与代码同步

---

**注意**: 本清单基于当前项目结构生成，如有路由变更请及时更新此文档。