# PS-BMP 组件开发指南

## 概述

本文档详细介绍了 PS-BMP 管理端 UI 项目中的组件开发规范、最佳实践和使用指南。项目采用模块化组件架构，提供了丰富的基础组件和业务组件。

## 组件架构

### 组件分层

```
组件架构
├── 基础组件层 (@fb/fb-ui)
│   ├── 布局组件 (Layout, Container, Grid)
│   ├── 表单组件 (Input, Select, DatePicker)
│   ├── 数据展示 (Table, Pagination, Tree)
│   ├── 反馈组件 (Message, Dialog, Loading)
│   └── 导航组件 (Menu, Breadcrumb, Tabs)
├── 业务组件层 (@fb/tp-components)
│   ├── 业务表单组件
│   ├── 数据列表组件
│   ├── 图表组件
│   └── 页面模板组件
├── 功能模块层
│   ├── 用户管理组件 (@fb/admin-base)
│   ├── 表单设计器 (@fb/form-designer)
│   ├── 日志中心 (@fb/log-center-ui)
│   ├── 监控界面 (@fb/monitor-ui)
│   └── 大屏组件 (@fb/screen-base)
└── 应用组件层
    ├── 页面组件 (src/views)
    └── 应用特定组件 (src/components)
```

## 基础组件 (@fb/fb-ui)

### 布局组件

#### FbContainer - 容器组件
```vue
<template>
  <fb-container>
    <fb-header>头部内容</fb-header>
    <fb-main>主要内容</fb-main>
    <fb-footer>底部内容</fb-footer>
  </fb-container>
</template>
```

**属性**:
- `direction`: 布局方向，可选值 `horizontal` | `vertical`
- `gutter`: 间距，默认值 `8`

#### FbRow / FbCol - 栅格组件
```vue
<template>
  <fb-row :gutter="16">
    <fb-col :span="8">
      <div>列1</div>
    </fb-col>
    <fb-col :span="8">
      <div>列2</div>
    </fb-col>
    <fb-col :span="8">
      <div>列3</div>
    </fb-col>
  </fb-row>
</template>
```

**FbRow 属性**:
- `gutter`: 栅格间隔，默认值来自全局配置
- `type`: 布局模式，可选值 `flex`
- `justify`: flex 主轴对齐方式
- `align`: flex 交叉轴对齐方式

**FbCol 属性**:
- `span`: 栅格占据的列数
- `offset`: 栅格左侧的间隔格数
- `push`: 栅格向右移动格数
- `pull`: 栅格向左移动格数

### 表单组件

#### FbForm - 表单容器
```vue
<template>
  <fb-form
    ref="form"
    :model="formData"
    :rules="rules"
    label-width="100px"
  >
    <fb-form-item label="用户名" prop="username">
      <fb-input v-model="formData.username" placeholder="请输入用户名" />
    </fb-form-item>
    <fb-form-item label="密码" prop="password">
      <fb-input
        v-model="formData.password"
        type="password"
        placeholder="请输入密码"
      />
    </fb-form-item>
    <fb-form-item>
      <fb-button type="primary" @click="submitForm">提交</fb-button>
      <fb-button @click="resetForm">重置</fb-button>
    </fb-form-item>
  </fb-form>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          // 提交表单
          console.log('表单验证通过')
        }
      })
    },
    resetForm() {
      this.$refs.form.resetFields()
    }
  }
}
</script>
```

#### FbInput - 输入框组件
```vue
<template>
  <div>
    <!-- 基础输入框 -->
    <fb-input v-model="value1" placeholder="请输入内容" />
    
    <!-- 密码输入框 -->
    <fb-input v-model="value2" type="password" show-password />
    
    <!-- 带图标的输入框 -->
    <fb-input v-model="value3" prefix-icon="el-icon-search" />
    
    <!-- 可清空的输入框 -->
    <fb-input v-model="value4" clearable />
    
    <!-- 文本域 -->
    <fb-input
      v-model="value5"
      type="textarea"
      :rows="4"
      placeholder="请输入内容"
    />
  </div>
</template>
```

#### FbSelect - 选择器组件
```vue
<template>
  <div>
    <!-- 基础选择器 -->
    <fb-select v-model="value1" placeholder="请选择">
      <fb-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </fb-select>
    
    <!-- 多选选择器 -->
    <fb-select v-model="value2" multiple placeholder="请选择">
      <fb-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </fb-select>
    
    <!-- 可搜索的选择器 -->
    <fb-select v-model="value3" filterable placeholder="请选择">
      <fb-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </fb-select>
  </div>
</template>

<script>
export default {
  data() {
    return {
      value1: '',
      value2: [],
      value3: '',
      options: [
        { value: '1', label: '选项1' },
        { value: '2', label: '选项2' },
        { value: '3', label: '选项3' }
      ]
    }
  }
}
</script>
```

### 数据展示组件

#### FbTable - 表格组件
```vue
<template>
  <div>
    <fb-table
      :data="tableData"
      :loading="loading"
      stripe
      border
      @selection-change="handleSelectionChange"
    >
      <fb-table-column type="selection" width="55" />
      <fb-table-column prop="id" label="ID" width="80" />
      <fb-table-column prop="name" label="姓名" />
      <fb-table-column prop="email" label="邮箱" />
      <fb-table-column prop="status" label="状态">
        <template slot-scope="scope">
          <fb-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </fb-tag>
        </template>
      </fb-table-column>
      <fb-table-column label="操作" width="200">
        <template slot-scope="scope">
          <fb-button size="mini" @click="handleEdit(scope.row)">编辑</fb-button>
          <fb-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
          >
            删除
          </fb-button>
        </template>
      </fb-table-column>
    </fb-table>
    
    <!-- 分页组件 -->
    <fb-pagination
      :current-page="currentPage"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      selectedRows: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const response = await this.$svc.service.post('/user/list', {
          page: this.currentPage,
          size: this.pageSize
        })
        this.tableData = response.data.records
        this.total = response.data.total
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    handleEdit(row) {
      // 编辑逻辑
    },
    handleDelete(row) {
      this.$confirm('确认删除该记录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 删除逻辑
      })
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.loadData()
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.loadData()
    }
  }
}
</script>
```

### 反馈组件

#### 消息提示
```javascript
// 成功消息
this.$message.success('操作成功')

// 警告消息
this.$message.warning('请注意')

// 错误消息
this.$message.error('操作失败')

// 信息消息
this.$message.info('提示信息')
```

#### 确认对话框
```javascript
this.$confirm('确认执行此操作？', '提示', {
  confirmButtonText: '确定',
  cancelButtonText: '取消',
  type: 'warning'
}).then(() => {
  // 确认后的操作
}).catch(() => {
  // 取消后的操作
})
```

#### 加载状态
```javascript
// 全屏加载
const loading = this.$loading({
  lock: true,
  text: '加载中...',
  spinner: 'el-icon-loading',
  background: 'rgba(0, 0, 0, 0.7)'
})

// 关闭加载
loading.close()
```

## 业务组件 (@fb/tp-components)

### Page Mixin

项目提供了 Page Mixin，包含常用的页面逻辑：

```javascript
// 在组件中使用
export default {
  mixins: [Page],
  data() {
    return {
      // 组件数据
    }
  },
  methods: {
    // Page Mixin 提供的方法
    // this.showLoading() - 显示加载
    // this.hideLoading() - 隐藏加载
    // this.showMessage(message, type) - 显示消息
    // this.confirm(message) - 确认对话框
  }
}
```

### 业务表单组件

#### TpForm - 增强表单组件
```vue
<template>
  <tp-form
    ref="form"
    :model="formData"
    :config="formConfig"
    @submit="handleSubmit"
  />
</template>

<script>
export default {
  data() {
    return {
      formData: {
        name: '',
        email: '',
        role: ''
      },
      formConfig: {
        labelWidth: '100px',
        items: [
          {
            prop: 'name',
            label: '姓名',
            type: 'input',
            required: true,
            rules: [
              { required: true, message: '请输入姓名' }
            ]
          },
          {
            prop: 'email',
            label: '邮箱',
            type: 'input',
            required: true,
            rules: [
              { required: true, message: '请输入邮箱' },
              { type: 'email', message: '请输入正确的邮箱格式' }
            ]
          },
          {
            prop: 'role',
            label: '角色',
            type: 'select',
            options: [
              { label: '管理员', value: 'admin' },
              { label: '用户', value: 'user' }
            ]
          }
        ],
        buttons: [
          { text: '提交', type: 'primary', action: 'submit' },
          { text: '重置', action: 'reset' }
        ]
      }
    }
  },
  methods: {
    handleSubmit(data) {
      console.log('表单数据:', data)
    }
  }
}
</script>
```

### 数据列表组件

#### TpList - 增强列表组件
```vue
<template>
  <tp-list
    ref="list"
    :config="listConfig"
    @selection-change="handleSelectionChange"
  />
</template>

<script>
export default {
  data() {
    return {
      listConfig: {
        api: '/user/list',
        columns: [
          { prop: 'id', label: 'ID', width: 80 },
          { prop: 'name', label: '姓名' },
          { prop: 'email', label: '邮箱' },
          {
            prop: 'status',
            label: '状态',
            formatter: (row) => row.status === 1 ? '启用' : '禁用'
          }
        ],
        actions: [
          {
            text: '编辑',
            type: 'primary',
            handler: this.handleEdit
          },
          {
            text: '删除',
            type: 'danger',
            handler: this.handleDelete,
            confirm: '确认删除？'
          }
        ],
        toolbar: [
          {
            text: '新增',
            type: 'primary',
            icon: 'el-icon-plus',
            handler: this.handleAdd
          },
          {
            text: '批量删除',
            type: 'danger',
            handler: this.handleBatchDelete
          }
        ],
        search: {
          items: [
            {
              prop: 'keyword',
              label: '关键词',
              type: 'input',
              placeholder: '请输入姓名或邮箱'
            },
            {
              prop: 'status',
              label: '状态',
              type: 'select',
              options: [
                { label: '全部', value: '' },
                { label: '启用', value: 1 },
                { label: '禁用', value: 0 }
              ]
            }
          ]
        }
      }
    }
  },
  methods: {
    handleAdd() {
      // 新增逻辑
    },
    handleEdit(row) {
      // 编辑逻辑
    },
    handleDelete(row) {
      // 删除逻辑
    },
    handleBatchDelete() {
      // 批量删除逻辑
    },
    handleSelectionChange(selection) {
      // 选择变化处理
    }
  }
}
</script>
```

## 管理后台组件 (@fb/admin-base)

### 用户管理组件

#### UserSelector - 用户选择器
```vue
<template>
  <user-selector
    v-model="selectedUsers"
    :multiple="true"
    :show-avatar="true"
    placeholder="请选择用户"
  />
</template>

<script>
export default {
  data() {
    return {
      selectedUsers: []
    }
  }
}
</script>
```

#### RoleSelector - 角色选择器
```vue
<template>
  <role-selector
    v-model="selectedRole"
    :filter-permissions="['user:create']"
    placeholder="请选择角色"
  />
</template>
```

### 权限组件

#### PermissionTree - 权限树
```vue
<template>
  <permission-tree
    v-model="selectedPermissions"
    :show-checkbox="true"
    :default-expand-all="true"
  />
</template>
```

#### HasPermission - 权限指令
```vue
<template>
  <div>
    <!-- 使用指令控制显示 -->
    <fb-button v-has-permission="'user:create'">新增用户</fb-button>
    
    <!-- 使用组件控制显示 -->
    <has-permission permission="user:delete">
      <fb-button type="danger">删除用户</fb-button>
    </has-permission>
  </div>
</template>
```

## 大屏组件 (@fb/screen-base)

### 图表组件

#### ScreenChart - 大屏图表
```vue
<template>
  <screen-chart
    :type="'line'"
    :data="chartData"
    :options="chartOptions"
    :auto-resize="true"
  />
</template>

<script>
export default {
  data() {
    return {
      chartData: {
        xAxis: ['1月', '2月', '3月', '4月', '5月'],
        series: [
          {
            name: '销售额',
            data: [120, 200, 150, 80, 70]
          }
        ]
      },
      chartOptions: {
        title: {
          text: '月度销售趋势'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        }
      }
    }
  }
}
</script>
```

### 数据卡片

#### DataCard - 数据展示卡片
```vue
<template>
  <data-card
    :title="'总用户数'"
    :value="1234"
    :unit="'人'"
    :trend="'up'"
    :trend-value="'12%'"
    :icon="'el-icon-user'"
    :color="'#409EFF'"
  />
</template>
```

## 自定义组件开发

### 组件开发规范

#### 1. 组件命名
- 使用 PascalCase 命名
- 组件名应该具有描述性
- 避免与HTML元素冲突

```javascript
// 好的命名
UserProfile.vue
DataTable.vue
SearchForm.vue

// 不好的命名
user.vue
table.vue
form.vue
```

#### 2. 组件结构
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
  computed: {
    // 计算属性
  },
  watch: {
    // 监听器
  },
  created() {
    // 生命周期钩子
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

#### 3. Props 定义
```javascript
props: {
  // 基础类型
  title: String,
  count: Number,
  visible: Boolean,
  items: Array,
  config: Object,
  
  // 带默认值
  size: {
    type: String,
    default: 'medium'
  },
  
  // 带验证
  status: {
    type: String,
    validator: value => ['success', 'warning', 'error'].includes(value)
  },
  
  // 必需属性
  data: {
    type: Array,
    required: true
  }
}
```

#### 4. 事件定义
```javascript
methods: {
  handleClick() {
    // 触发事件
    this.$emit('click', {
      timestamp: Date.now(),
      target: this
    })
  },
  
  handleChange(value) {
    // 支持 v-model
    this.$emit('input', value)
    this.$emit('change', value)
  }
}
```

### 组件通信

#### 1. 父子组件通信
```vue
<!-- 父组件 -->
<template>
  <child-component
    :prop-data="parentData"
    @child-event="handleChildEvent"
  />
</template>

<!-- 子组件 -->
<template>
  <div @click="handleClick">
    {{ propData }}
  </div>
</template>

<script>
export default {
  props: ['propData'],
  methods: {
    handleClick() {
      this.$emit('child-event', 'data from child')
    }
  }
}
</script>
```

#### 2. 兄弟组件通信
```javascript
// 使用事件总线
// eventbus/index.js
import Vue from 'vue'
export default new Vue()

// 组件A
import EventBus from '@/eventbus'
export default {
  methods: {
    sendMessage() {
      EventBus.$emit('message', 'Hello from A')
    }
  }
}

// 组件B
import EventBus from '@/eventbus'
export default {
  created() {
    EventBus.$on('message', (data) => {
      console.log('Received:', data)
    })
  },
  beforeDestroy() {
    EventBus.$off('message')
  }
}
```

#### 3. 跨级组件通信
```javascript
// 使用 provide/inject
// 祖先组件
export default {
  provide() {
    return {
      theme: this.theme,
      updateTheme: this.updateTheme
    }
  },
  data() {
    return {
      theme: 'light'
    }
  },
  methods: {
    updateTheme(newTheme) {
      this.theme = newTheme
    }
  }
}

// 后代组件
export default {
  inject: ['theme', 'updateTheme'],
  methods: {
    changeTheme() {
      this.updateTheme('dark')
    }
  }
}
```

### 组件样式

#### 1. 样式作用域
```vue
<style lang="less" scoped>
/* 组件内部样式 */
.component-name {
  color: #333;
  
  .item {
    padding: 10px;
  }
}
</style>

<style lang="less">
/* 全局样式 */
.global-class {
  font-size: 14px;
}
</style>
```

#### 2. 深度选择器
```vue
<style lang="less" scoped>
.component-name {
  /* 修改子组件样式 */
  /deep/ .child-component {
    background: #f5f5f5;
  }
  
  /* 或者使用 >>> */
  >>> .another-child {
    border: 1px solid #ddd;
  }
}
</style>
```

#### 3. CSS 变量
```vue
<style lang="less" scoped>
.component-name {
  --primary-color: #409EFF;
  --border-radius: 4px;
  
  background: var(--primary-color);
  border-radius: var(--border-radius);
}
</style>
```

### 组件测试

#### 1. 单元测试
```javascript
// tests/unit/components/UserProfile.spec.js
import { shallowMount } from '@vue/test-utils'
import UserProfile from '@/components/UserProfile.vue'

describe('UserProfile.vue', () => {
  it('renders user name when passed', () => {
    const name = 'John Doe'
    const wrapper = shallowMount(UserProfile, {
      propsData: { name }
    })
    expect(wrapper.text()).toMatch(name)
  })
  
  it('emits event when button clicked', () => {
    const wrapper = shallowMount(UserProfile)
    wrapper.find('button').trigger('click')
    expect(wrapper.emitted().click).toBeTruthy()
  })
})
```

#### 2. 集成测试
```javascript
// tests/integration/UserManagement.spec.js
import { mount } from '@vue/test-utils'
import UserManagement from '@/views/UserManagement.vue'

describe('UserManagement Integration', () => {
  it('loads user list on mount', async () => {
    const wrapper = mount(UserManagement)
    await wrapper.vm.$nextTick()
    expect(wrapper.vm.users.length).toBeGreaterThan(0)
  })
})
```

### 组件文档

#### 1. 组件注释
```vue
<template>
  <!-- 用户资料组件 -->
  <div class="user-profile">
    <!-- 用户头像 -->
    <img :src="avatar" :alt="name" class="avatar" />
    <!-- 用户信息 -->
    <div class="info">
      <h3>{{ name }}</h3>
      <p>{{ email }}</p>
    </div>
  </div>
</template>

<script>
/**
 * 用户资料组件
 * @description 显示用户的基本信息，包括头像、姓名和邮箱
 * @author 开发者姓名
 * @version 1.0.0
 */
export default {
  name: 'UserProfile',
  props: {
    /**
     * 用户姓名
     * @type {String}
     * @required
     */
    name: {
      type: String,
      required: true
    },
    /**
     * 用户邮箱
     * @type {String}
     */
    email: String,
    /**
     * 用户头像URL
     * @type {String}
     * @default '/default-avatar.png'
     */
    avatar: {
      type: String,
      default: '/default-avatar.png'
    }
  },
  /**
   * 组件事件
   * @event click 点击组件时触发
   * @event avatar-click 点击头像时触发
   */
  emits: ['click', 'avatar-click']
}
</script>
```

#### 2. README 文档
```markdown
# UserProfile 组件

## 描述
用户资料展示组件，用于显示用户的基本信息。

## 使用方法

```vue
<template>
  <user-profile
    :name="user.name"
    :email="user.email"
    :avatar="user.avatar"
    @click="handleUserClick"
  />
</template>
```

## 属性

| 属性名 | 类型 | 必需 | 默认值 | 描述 |
|--------|------|------|--------|------|
| name | String | 是 | - | 用户姓名 |
| email | String | 否 | - | 用户邮箱 |
| avatar | String | 否 | '/default-avatar.png' | 用户头像URL |

## 事件

| 事件名 | 参数 | 描述 |
|--------|------|------|
| click | event | 点击组件时触发 |
| avatar-click | event | 点击头像时触发 |

## 示例

### 基础用法
```vue
<user-profile name="张三" email="zhangsan@example.com" />
```

### 自定义头像
```vue
<user-profile
  name="李四"
  email="lisi@example.com"
  avatar="/custom-avatar.jpg"
/>
```
```

## 性能优化

### 1. 组件懒加载
```javascript
// 路由懒加载
const UserProfile = () => import('@/components/UserProfile.vue')

// 组件懒加载
components: {
  UserProfile: () => import('@/components/UserProfile.vue')
}
```

### 2. 函数式组件
```vue
<template functional>
  <div class="simple-component">
    <h3>{{ props.title }}</h3>
    <p>{{ props.content }}</p>
  </div>
</template>

<script>
export default {
  functional: true,
  props: ['title', 'content']
}
</script>
```

### 3. 组件缓存
```vue
<template>
  <keep-alive>
    <component :is="currentComponent" />
  </keep-alive>
</template>
```

### 4. 虚拟滚动
```vue
<template>
  <virtual-list
    :data="items"
    :item-height="50"
    :visible-count="10"
  >
    <template v-slot="{ item }">
      <div class="item">{{ item.name }}</div>
    </template>
  </virtual-list>
</template>
```

## 最佳实践

### 1. 组件设计原则
- **单一职责**: 每个组件只负责一个功能
- **可复用性**: 设计通用的、可配置的组件
- **可维护性**: 保持代码简洁、易读
- **性能优化**: 避免不必要的渲染和计算

### 2. 组件拆分
```vue
<!-- 不好的做法：一个组件包含太多功能 -->
<template>
  <div class="user-management">
    <!-- 搜索表单 -->
    <form>...</form>
    <!-- 用户列表 -->
    <table>...</table>
    <!-- 分页组件 -->
    <pagination>...</pagination>
    <!-- 用户详情弹窗 -->
    <dialog>...</dialog>
  </div>
</template>

<!-- 好的做法：拆分为多个组件 -->
<template>
  <div class="user-management">
    <user-search-form @search="handleSearch" />
    <user-list :data="users" @edit="handleEdit" />
    <pagination v-model="currentPage" :total="total" />
    <user-detail-dialog v-model="showDialog" :user="selectedUser" />
  </div>
</template>
```

### 3. 状态管理
```javascript
// 组件内部状态
export default {
  data() {
    return {
      // 只存储组件相关的状态
      loading: false,
      visible: false
    }
  },
  computed: {
    // 从 store 获取全局状态
    users() {
      return this.$store.state.users
    }
  }
}
```

### 4. 错误处理
```vue
<template>
  <div class="component">
    <div v-if="error" class="error">
      {{ error.message }}
    </div>
    <div v-else-if="loading" class="loading">
      加载中...
    </div>
    <div v-else>
      <!-- 正常内容 -->
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      error: null
    }
  },
  async created() {
    try {
      this.loading = true
      await this.loadData()
    } catch (error) {
      this.error = error
    } finally {
      this.loading = false
    }
  }
}
</script>
```

## 常见问题

### Q: 如何在组件中使用全局服务？
A: 通过 `this.$svc` 访问全局服务实例。

### Q: 如何实现组件的双向绑定？
A: 使用 `v-model` 指令，组件需要接收 `value` prop 并触发 `input` 事件。

### Q: 如何优化大列表的渲染性能？
A: 使用虚拟滚动、分页加载或 `v-show` 替代 `v-if`。

### Q: 如何处理组件的异步数据？
A: 在 `created` 或 `mounted` 钩子中加载数据，使用 loading 状态。

### Q: 如何实现组件的主题切换？
A: 使用 CSS 变量或动态类名，结合全局主题状态。

---

**更新时间**: 2024年12月
**文档版本**: v1.0.0

> 本文档将随着项目的发展持续更新，请关注最新版本。