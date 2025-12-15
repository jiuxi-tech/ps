# 前端编码规范指南

## 文档说明

本文档总结了PS-BMP前端项目的完整编码规范，包括fb-ui组件使用、页面模板规范、Service层规范、常见错误及最佳实践。本文档旨在为开发人员（包括AI Agent）提供统一的编码参考标准。

## 目录

1. 项目架构概览
2. fb-ui组件库使用规范
3. tp-components业务组件库规范 👉 [详细文档](./tp-components-guide.md)
4. 页面模板标准（list、add、view）
5. Service层编码规范
6. 数据交互规范
7. 弹窗组件使用规范
8. 常见易错点与解决方案
9. 最佳实践清单

---

## 1. 项目架构概览

### 1.1 目录结构

```
ps-fe/
├── @fb/                          # 框架组件库
│   ├── admin-base/              # 管理后台基础模块
│   │   ├── views/               # 页面视图
│   │   │   └── sys/            # 系统管理页面
│   │   ├── service/             # 业务服务层
│   │   │   └── sys/            # 系统服务
│   │   └── components/          # 业务组件
│   ├── fb-ui/                   # UI组件库
│   │   └── packages/            # 组件包
│   │       ├── components/      # 基础组件
│   │       ├── directives/      # 指令
│   │       └── mixins/          # 混入
│   └── tp-components/           # 业务通用组件
├── src/                         # 应用源码
│   ├── views/                   # 应用视图
│   ├── components/              # 应用组件
│   └── service/                 # 应用服务
└── project.config.js            # 项目配置
```

### 1.2 技术栈

- **框架**: Vue 2.x
- **构建工具**: Rsbuild
- **UI组件库**: fb-ui（内部组件库）
- **HTTP客户端**: Axios（通过app.service封装）
- **日期处理**: dayjs
- **加密**: sm-crypto（用于密码加密）

---

## 2. fb-ui组件库使用规范

### 2.1 布局组件

#### fb-row / fb-col 栅格布局

**使用规范**：
- 栅格总共24列，通过`span`属性分配列数
- `gutter`属性控制列间距，默认从全局配置读取
- 常用布局比例：8-8-8（三等分）、12-12（二等分）、16-8（主次布局）

```vue
<fb-row>
  <fb-col span="8">
    <fb-form-item label="用户名">
      <fb-input v-model="formData.username"></fb-input>
    </fb-form-item>
  </fb-col>
  <fb-col span="8">
    <fb-form-item label="手机号">
      <fb-input v-model="formData.phone"></fb-input>
    </fb-form-item>
  </fb-col>
  <fb-col span="8">
    <fb-form-item label="性别">
      <fb-select v-model="formData.sex" :data="sexOptions"></fb-select>
    </fb-form-item>
  </fb-col>
</fb-row>
```

### 2.2 表单组件

#### fb-form 表单容器

**核心属性**：
- `ref`: 必须设置ref用于调用验证方法
- `mode`: 表单模式，可选值 `query`（查询表单）、`edit`（编辑表单）

**验证方法**：
```javascript
// 表单验证
this.$refs.fbform.validate((result) => {
  if (result === true) {
    // 验证通过，执行提交逻辑
  }
})
```

#### fb-form-item 表单项

**属性说明**：
- `label`: 字段标签名称
- `prop`: 字段名，用于表单验证
- `rule`: 验证规则数组

**验证规则示例**：
```vue
<fb-form-item label="应用名称" prop="appName" :rule="[{required: true}]">
  <fb-input v-model="formData.appName" placeholder="请输入应用名称" :maxlength="50"></fb-input>
</fb-form-item>
```

#### fb-input 输入框

**常用属性**：
- `v-model`: 双向绑定数据
- `placeholder`: 占位提示文字
- `clearable`: 是否显示清空按钮
- `maxlength`: 最大输入长度
- `type`: 输入类型（text、password等）

```vue
<fb-input v-model="formData.appName" placeholder="请输入应用名称" :maxlength="50" clearable></fb-input>
```

#### fb-select 下拉选择

**数据绑定方式**：
```vue
<fb-select v-model="formData.status" :data="statusOptions" clearable></fb-select>
```

**数据格式**：
```javascript
statusOptions: [
  { value: 1, label: '启用' },
  { value: 0, label: '禁用' }
]
```

#### fb-datepicker 日期选择器

**使用规范**：
```vue
<fb-datepicker 
  v-model="formData.expireTime" 
  placeholder="请选择过期时间（留空表示永不过期）"
  format="YYYY-MM-DD HH:mm:ss"
  clearable>
</fb-datepicker>
```

**日期格式转换**（与后端交互）：
```javascript
// 前端显示格式转后端存储格式
if (submitData.expireTime) {
  submitData.expireTime = dayjs(submitData.expireTime).format('YYYYMMDDHHmmss');
}

// 后端格式转前端显示格式
if (this.formData.expireTime) {
  this.formData.expireTime = dayjs(this.formData.expireTime, 'YYYYMMDDHHmmss').toDate();
}
```

#### fb-textarea 多行文本

```vue
<fb-textarea 
  rows="3" 
  v-model="formData.description"
  placeholder="请输入应用描述"
  :maxlength="500">
</fb-textarea>
```

### 2.3 数据展示组件

#### fb-simple-table 简单表格

**核心属性**：
- `ref`: 必须设置，用于调用表格方法
- `service`: 数据服务方法（自动调用）
- `param`: 查询参数对象
- `pk`: 主键字段名
- `columns`: 列配置数组
- `auto-load`: 是否自动加载数据
- `multiple`: 是否支持多选
- `formatters`: 数据格式化对象
- `scroll`: 滚动配置

**标准配置示例**：
```vue
<fb-simple-table
  ref="table"
  :service="table.service.list"
  :param="formData"
  :pk="table.primaryKey"
  :columns="table.columns"
  :multiple="false"
  auto-load
  :formatters="formatters"
  :scroll="{x:900, y: 330, autoHeight: true}"
  @on-row-select="handleTableSelect">
  
  <!-- 操作列插槽 -->
  <template v-slot:actions="props">
    <fb-space>
      <fb-button @on-click="handleEdit(props.row)" editor size="s">编辑</fb-button>
      <fb-button @on-click="handleDel(props.row)" danger size="s">删除</fb-button>
    </fb-space>
  </template>
  
  <!-- 自定义列插槽 -->
  <template v-slot:view="props">
    <fb-link-group>
      <fb-link :click="()=>handleView(props.row)" :label="props.row.appName" type="primary"></fb-link>
    </fb-link-group>
  </template>
</fb-simple-table>
```

**列配置规范**：
```javascript
columns: [
  {
    name: 'appName',        // 字段名
    label: '应用名称',       // 列标题
    slot: 'view',           // 使用插槽（可选）
    sortable: false,        // 是否可排序
    width: 150,             // 列宽度
  },
  {
    name: 'status',
    label: '状态',
    slot: 'status',         // 自定义渲染
    sortable: false,
    width: 80,
  },
  {
    freeze: "right",        // 冻结在右侧
    name: '',
    label: '操作',
    sortable: false,
    slot: 'actions',
    width: 160,
  }
]
```

**格式化器配置**：
```javascript
formatters: {
  status(val) {
    return val === 1 ? '启用' : '禁用';
  },
  expireTime(val) {
    if (!val || val === '' || val === null) {
      return '永不过期';
    }
    try {
      return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
    } catch (e) {
      return '永不过期';
    }
  }
}
```

**表格操作方法**：
```javascript
// 查询（返回第一页）
this.$refs.table.doSearch()

// 刷新当前页
this.$refs.table.doReload()

// 重新加载
this.$refs.table.reload()
```

#### fb-property 属性展示

**用于查看页面展示详情数据**：
```vue
<fb-property bordered label-width="140px">
  <fb-property-item label="应用名称" span="2">
    {{formData.appName}}
  </fb-property-item>
  <fb-property-item label="API Key" span="2">
    <span style="font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;">{{formData.apiKey}}</span>
    <fb-button :copy="formData.apiKey" icon="copy" size="s" style="margin-left: 12px;">复制</fb-button>
  </fb-property-item>
  <fb-property-item label="状态">
    <span :class="formData.status === 1 ? 'status-valid' : 'status-invalid'">
      {{ formData.status === 1 ? '启用' : '禁用' }}
    </span>
  </fb-property-item>
</fb-property>
```

### 2.4 页面模板组件

#### fb-page-search 查询页面容器

**插槽说明**：
- `query`: 查询条件表单区域
- `buttons`: 顶部操作按钮区域
- `actions`: 查询操作按钮区域（通常放置查询按钮）
- `table`: 数据表格区域

```vue
<fb-page-search>
  <template slot="query">
    <fb-form ref="query-form" mode="query">
      <!-- 查询条件 -->
    </fb-form>
  </template>
  
  <template slot="buttons">
    <fb-button @on-click="handleAdd" icon="add-circle">新增</fb-button>
  </template>
  
  <template slot="actions">
    <fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
  </template>
  
  <template slot="table">
    <fb-simple-table ref="table" ...></fb-simple-table>
  </template>
</fb-page-search>
```

#### fb-page-tree-table 树表页面容器

**用于左侧树+右侧表格的布局**：
```vue
<fb-page-tree-table title="机构树">
  <template slot="tree">
    <fb-tree
      ref="deptTree"
      :data="deptData"
      :reader="{value: 'id', label: 'text'}"
      @on-select-change="handleSelectChange">
    </fb-tree>
  </template>
  
  <template slot="tree-actions">
    <fb-button :icon="treeExpand ? 'tree-expansion': 'tree-closed'" @on-click="handleTreeExpand"></fb-button>
  </template>
  
  <template slot="query">
    <!-- 查询表单 -->
  </template>
  
  <template slot="buttons">
    <!-- 操作按钮 -->
  </template>
  
  <template slot="actions">
    <!-- 查询按钮 -->
  </template>
  
  <template slot="table">
    <fb-simple-table ref="table" :auto-load="false" ...></fb-simple-table>
  </template>
</fb-page-tree-table>
```

### 2.5 交互组件

#### fb-button 按钮

**属性说明**：
- `type`: 按钮类型（primary、success、warning、danger）
- `size`: 按钮尺寸（s、m、l）
- `icon`: 图标名称
- `editor`: 编辑按钮样式
- `danger`: 危险按钮样式
- `loading`: 加载状态

```vue
<fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
<fb-button @on-click="handleEdit(props.row)" editor size="s">编辑</fb-button>
<fb-button @on-click="handleDel(props.row)" danger size="s">删除</fb-button>
```

#### fb-space 间距容器

**用于按钮组排列**：
```vue
<fb-space>
  <fb-button @on-click="handleEdit(props.row)" editor size="s">编辑</fb-button>
  <fb-button @on-click="handleAuth(props.row)" editor size="s">授权</fb-button>
  <fb-button @on-click="handleDel(props.row)" danger size="s">删除</fb-button>
</fb-space>
```

#### fb-link / fb-link-group 链接

```vue
<fb-link-group>
  <fb-link :click="()=>handleView(props.row)" :label="props.row.appName" type="primary"></fb-link>
</fb-link-group>
```

---

## 3. 页面模板标准（list、add、view）

### 3.1 list.vue 列表页模板

**标准结构**：
```vue
<template>
  <div>
    <fb-page-search>
      <!-- 查询条件 -->
      <template slot="query">
        <fb-form ref="query-form" mode="query">
          <fb-row>
            <fb-col span="16">
              <fb-form-item label="应用名称">
                <fb-input v-model="formData.appName" clearable></fb-input>
              </fb-form-item>
            </fb-col>
            <fb-col span="8">
              <fb-form-item label="状态">
                <fb-select v-model="formData.status" clearable :data="statusOptions"></fb-select>
              </fb-form-item>
            </fb-col>
          </fb-row>
        </fb-form>
      </template>

      <!-- 操作按钮 -->
      <template slot="buttons">
        <fb-button ref="buttonAdd" @on-click="handleAdd" icon="add-circle">新增</fb-button>
      </template>

      <!-- 查询按钮 -->
      <template slot="actions">
        <fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
      </template>

      <!-- 数据表格 -->
      <template slot="table">
        <fb-simple-table
          ref="table"
          :service="table.service.list"
          :param="formData"
          :pk="table.primaryKey"
          :columns="table.columns"
          :multiple="false"
          auto-load
          :formatters="formatters"
          :scroll="{x:900, y: 330, autoHeight: true}"
          @on-row-select="handleTableSelect">
          
          <template v-slot:actions="props">
            <fb-space>
              <fb-button @on-click="handleEdit(props.row)" editor size="s">编辑</fb-button>
              <fb-button @on-click="handleDel(props.row)" danger size="s">删除</fb-button>
            </fb-space>
          </template>

          <template v-slot:view="props">
            <fb-link-group>
              <fb-link :click="()=>handleView(props.row)" :label="props.row.appName" type="primary"></fb-link>
            </fb-link-group>
          </template>
        </fb-simple-table>
      </template>
    </fb-page-search>

    <!-- 弹窗组件 -->
    <tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
  </div>
</template>

<script>
import dayjs from "dayjs";

export default {
  name: 'list',
  mixins: [],
  
  mounted() {
    // 初始化逻辑
  },
  
  data() {
    return {
      formData: {
        appName: '',
        status: null,
        logDelete: 0, // 逻辑删除标记
      },
      
      statusOptions: [
        { value: 1, label: '启用' },
        { value: 0, label: '禁用' },
      ],
      
      formatters: {
        status(val) {
          return val === 1 ? '启用' : '禁用';
        },
        createTime(val) {
          if (!val || val === '' || val === null) {
            return '-';
          }
          return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
        }
      },
      
      table: {
        service: app.$svc.sys.thirdPartyApp,
        primaryKey: "appId",
        columns: [
          {
            name: 'appName',
            label: '应用名称',
            slot: 'view',
            sortable: false,
            width: 150,
          },
          {
            name: 'status',
            label: '状态',
            slot: 'status',
            sortable: false,
            width: 80,
          },
          {
            freeze: "right",
            name: '',
            label: '操作',
            sortable: false,
            slot: 'actions',
            width: 160,
          },
        ],
      },
    }
  },
  
  methods: {
    // 查询方法
    handleQuery() {
      this.$refs.table.doSearch()
    },
    
    // 新增方法
    handleAdd() {
      let param = {};
      let options = {"height": 500, "width": 700};
      this.$refs.TpDialog.show(import('./add.vue'), param, "新增", options, { action: 'add' });
    },
    
    // 修改方法
    handleEdit(row) {
      let param = {"id": row.appId, "passKey": row.passKey};
      let options = {"height": 500, "width": 700};
      this.$refs.TpDialog.show(import('./add.vue'), param, "编辑", options, { action: 'edit' });
    },
    
    // 删除方法
    handleDel(row) {
      this.$confirm('确定要删除该应用吗？删除后将无法恢复！', () => {
        this.delete(row.appId, row.passKey);
      })
    },
    
    delete(appId, passKey) {
      app.service.request('/sys/third-party-app/delete', {
        method: 'get',
        params: {"appId": appId, "passKey": passKey},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        responseType: 'json',
        timeout: 5000,
      }).then((result) => {
        if (result.code == 1) {
          this.$message.success('删除成功');
          this.$refs.table.doReload();
        } else {
          this.$message.error('删除失败: ' + result.message)
        }
      })
    },
    
    // 查看方法
    handleView(row) {
      let param = {"id": row.appId, "passKey": row.passKey}
      let options = {"height": 500, "width": 700};
      this.$refs.TpDialog.show(import('./view.vue'), param, "查看", options, { action: 'view' });
    },
    
    // 弹窗关闭回调
    closeDialog(result) {
      if (!result || !result.success) {
        return
      }
      
      if (result.action === 'add') {
        // 新增成功：重新查询（定位到第一页）
        this.$refs.table.doSearch()
      } else if (result.action === 'edit') {
        // 修改成功：刷新当前页
        this.$refs.table.doReload()
      }
    },
    
    handleTableSelect(row) {
      // 表格行选择事件
    }
  }
}
</script>

<style lang="less" scoped>
.status-valid {
  color: #52c41a;
  font-weight: bold;
}

.status-invalid {
  color: #ff4d4f;
  font-weight: bold;
}
</style>
```

**关键要点**：
1. **查询按钮**：必须调用`this.$refs.table.doSearch()`
2. **列表刷新**：新增后用`doSearch()`，修改后用`doReload()`
3. **主键字段**：table配置中必须正确设置`pk`属性
4. **删除操作**：必须使用`$confirm`二次确认
5. **meta参数传递**：通过弹窗第5个参数传递`{ action: 'add/edit/view' }`

### 3.2 add.vue 新增/编辑页模板

**标准结构**：
```vue
<template>
  <div class="tp-dialog">
    <div class="tp-dialog-top" style="padding-top: 0;">
      <fb-form ref="fbform">
        <fb-row>
          <fb-col span="24">
            <fb-form-item label="应用名称" prop="appName" :rule="[{required: true}]">
              <fb-input v-model="formData.appName" placeholder="请输入应用名称" :maxlength="50"></fb-input>
            </fb-form-item>
          </fb-col>
        </fb-row>

        <fb-row>
          <fb-col span="12">
            <fb-form-item label="状态" prop="status" :rule="[{required: true}]">
              <fb-select v-model="formData.status" :data="statusOptions"></fb-select>
            </fb-form-item>
          </fb-col>
          <fb-col span="12">
            <fb-form-item label="过期时间" prop="expireTime">
              <fb-datepicker 
                v-model="formData.expireTime" 
                placeholder="请选择过期时间（留空表示永不过期）"
                format="YYYY-MM-DD HH:mm:ss"
                clearable>
              </fb-datepicker>
            </fb-form-item>
          </fb-col>
        </fb-row>

        <fb-row>
          <fb-col span="24">
            <fb-form-item label="应用描述" prop="description">
              <fb-textarea 
                rows="3" 
                v-model="formData.description"
                placeholder="请输入应用描述"
                :maxlength="500">
              </fb-textarea>
            </fb-form-item>
          </fb-col>
        </fb-row>
      </fb-form>
    </div>

    <div class="tp-dialog-bottom">
      <fb-button style="margin-right: 12px" type="primary" @on-click="save">保存</fb-button>
      <fb-button @on-click="handleClose">关闭</fb-button>
    </div>
  </div>
</template>

<script>
import dayjs from "dayjs";

export default {
  name: 'add',
  mixins: [],
  
  // 接收父组件的传参
  props: {
    param: {
      type: Object,
      require: false
    },
    parentPage: {
      type: Object,
      default: null
    },
    meta: {
      type: Object,
      default: () => ({})
    }
  },
  
  created() {
    // 记录原来的默认值，用于表单重置
  },
  
  mounted() {
    this.init(this.param);
  },
  
  data() {
    return {
      service: this.$svc.sys.thirdPartyApp,
      
      statusOptions: [
        { value: 1, label: '启用' },
        { value: 0, label: '禁用' }
      ],
      
      formData: {
        appId: '',
        appName: '',
        status: 1, // 默认启用
        expireTime: '',
        description: '',
        logDelete: 0,
      },
    }
  },
  
  methods: {
    init(param) {
      if (param && param.id) {
        let appId = param.id;
        this.formData.appId = appId;
        this.view(appId, param.passKey);
      }
    },
    
    handleClose() {
      this.closeTpDialog()
    },
    
    save() {
      this.$refs.fbform.validate((result) => {
        if (result === true) {
          const submitData = { ...this.formData };
          
          // 处理日期格式
          if (submitData.expireTime) {
            submitData.expireTime = dayjs(submitData.expireTime).format('YYYYMMDDHHmmss');
          } else {
            submitData.expireTime = ''
          }
          
          if (this.formData.appId) {
            // 修改
            submitData.passKey = this.param.passKey;
            this.service.update(submitData).then((result) => {
              if (result.code == 1) {
                this.$message.success('修改成功');
                const action = this.meta?.action || 'edit'
                this.closeTpDialog({ success: true, action });
              } else {
                this.$message.error('修改失败:' + result.message)
              }
            })
          } else {
            // 新增
            this.service.add(submitData).then((result) => {
              if (result.code == 1) {
                this.$message.success('新增成功');
                const action = this.meta?.action || 'add'
                this.closeTpDialog({ success: true, action });
              } else {
                this.$message.error('新增失败: ' + result.message)
              }
            })
          }
        }
      })
    },
    
    view(appId, passKey) {
      this.service.view({"appId": appId, "passKey": passKey}).then((result) => {
        if (result.code == 1) {
          this.formData = result.data;
          
          // 将时间字符串转换为日期对象
          if (this.formData.expireTime) {
            this.formData.expireTime = dayjs(this.formData.expireTime, 'YYYYMMDDHHmmss').toDate();
          }
        } else {
          this.$message.error('查询失败: ' + result.message)
        }
      }).catch((err) => {
        console.log(err);
      })
    },
  }
}
</script>

<style lang="less" scoped>
</style>
```

**关键要点**：
1. **表单验证**：保存前必须调用`this.$refs.fbform.validate()`
2. **日期格式转换**：提交时转为`YYYYMMDDHHmmss`，显示时转为Date对象
3. **新增/修改判断**：通过`this.formData.appId`是否存在判断
4. **关闭弹窗**：成功保存时传递`{ success: true, action }`
5. **meta参数**：通过`this.meta?.action`获取操作类型

### 3.3 view.vue 查看页模板

**标准结构**：
```vue
<template>
  <div class="tp-dialog">
    <div class="tp-dialog-top">
      <fb-property bordered label-width="140px">
        <fb-property-item label="应用名称" span="2">
          {{formData.appName}}
        </fb-property-item>
        <fb-property-item label="API Key" span="2">
          <span style="font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;">{{formData.apiKey}}</span>
          <fb-button :copy="formData.apiKey" icon="copy" size="s" style="margin-left: 12px;">复制</fb-button>
        </fb-property-item>
        <fb-property-item label="状态">
          <span :class="formData.status === 1 ? 'status-valid' : 'status-invalid'">
            {{ formData.status === 1 ? '启用' : '禁用' }}
          </span>
        </fb-property-item>
        <fb-property-item label="过期时间">
          {{ formatExpireTime(formData.expireTime) }}
        </fb-property-item>
        <fb-property-item label="应用描述" span="2">
          {{formData.description || '-'}}
        </fb-property-item>
        <fb-property-item label="创建人">
          {{formData.createPersonName}}
        </fb-property-item>
        <fb-property-item label="创建时间">
          {{ formatTime(formData.createTime) }}
        </fb-property-item>
      </fb-property>
    </div>

    <div class="tp-dialog-bottom">
      <fb-button @on-click="handleClose">关闭</fb-button>
    </div>
  </div>
</template>

<script>
import dayjs from "dayjs";

export default {
  name: 'view',
  mixins: [],
  
  props: {
    param: {
      type: Object,
      require: false
    },
    parentPage: {
      type: Object,
      default: null
    },
    meta: {
      type: Object,
      default: () => ({})
    }
  },
  
  created() {},
  
  mounted() {
    this.init(this.param);
  },
  
  data() {
    return {
      service: this.$svc.sys.thirdPartyApp,
      formData: {
        appId: '',
        appName: '',
        apiKey: '',
        status: 1,
        expireTime: '',
        description: '',
        createTime: '',
        createPersonName: '',
      },
    }
  },
  
  methods: {
    init(param) {
      if (param && param.id) {
        let appId = param.id;
        this.formData.appId = appId;
        this.view(appId, param.passKey);
      }
    },
    
    handleClose() {
      this.closeTpDialog()
    },
    
    formatTime(val) {
      if (!val || val === '' || val === null) {
        return '-';
      }
      try {
        return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
      } catch (e) {
        return '-';
      }
    },
    
    formatExpireTime(val) {
      if (!val || val === '' || val === null) {
        return '永不过期';
      }
      try {
        return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
      } catch (e) {
        return '永不过期';
      }
    },
    
    view(appId, passKey) {
      this.service.view({"appId": appId, "passKey": passKey}).then((result) => {
        if (result.code == 1) {
          this.formData = result.data;
        } else {
          this.$message.error('查询失败: ' + result.message)
        }
      }).catch((err) => {
        console.log(err);
      })
    },
  }
}
</script>

<style lang="less" scoped>
.status-valid {
  color: #52c41a;
  font-weight: bold;
}

.status-invalid {
  color: #ff4d4f;
  font-weight: bold;
}
</style>
```

**关键要点**：
1. **只读展示**：使用`fb-property`组件展示数据
2. **格式化方法**：将格式化逻辑封装为methods
3. **空值处理**：显示`-`或默认文案
4. **复制功能**：使用fb-button的`copy`属性

---

## 4. Service层编码规范

### 4.1 Service文件结构

**标准位置**：`@fb/admin-base/service/sys/{moduleName}/index.js`

**基本结构**：
```javascript
export default {
  // 列表查询（分页）
  list(formData) {
    return app.service.get('/sys/third-party-app/list', {params: formData})
  },

  // 新增
  add(formData) {
    return app.service.request({
      url: '/sys/third-party-app/add',
      method: 'post',
      transformRequest: [
        function (data) {
          let ret = ''
          for (let it in data) {
            ret += encodeURIComponent(it) + '=' +
              encodeURIComponent(data[it]) + '&'
          }
          ret = ret.substring(0, ret.lastIndexOf('&'))
          return ret
        },
      ],
      data: formData,
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      responseType: 'json',
      timeout: 5000,
    })
  },

  // 修改
  update(formData) {
    return app.service.request({
      url: '/sys/third-party-app/update',
      method: 'post',
      transformRequest: [
        function (data) {
          let ret = ''
          for (let it in data) {
            ret += encodeURIComponent(it) + '=' +
              encodeURIComponent(data[it]) + '&'
          }
          ret = ret.substring(0, ret.lastIndexOf('&'))
          return ret
        },
      ],
      data: formData,
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      responseType: 'json',
      timeout: 5000,
    })
  },

  // 查看
  view(formData) {
    return app.service.get('/sys/third-party-app/view', {params: formData})
  },

  // 删除
  delete(formData) {
    return app.service.get('/sys/third-party-app/delete', {params: formData})
  }
}
```

### 4.2 请求方式规范

#### GET请求
```javascript
list(formData) {
  return app.service.get('/sys/module/list', {params: formData})
}
```

#### POST请求（表单格式）
```javascript
add(formData) {
  return app.service.request({
    url: '/sys/module/add',
    method: 'post',
    transformRequest: [
      function (data) {
        let ret = ''
        for (let it in data) {
          ret += encodeURIComponent(it) + '=' +
            encodeURIComponent(data[it]) + '&'
        }
        ret = ret.substring(0, ret.lastIndexOf('&'))
        return ret
      },
    ],
    data: formData,
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    responseType: 'json',
    timeout: 5000,
  })
}
```

#### POST请求（JSON格式）
```javascript
saveApiPermissions(formData) {
  return app.service.request({
    url: '/sys/third-party-app/config-permissions',
    method: 'post',
    data: formData,
    headers: {'Content-Type': 'application/json'},
    responseType: 'json',
    timeout: 5000,
  })
}
```

#### 文件上传（FormData）
```javascript
importExcel(formData) {
  return app.service.request({
    url: '/sys/person/import-excel',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' },
    timeout: 60000, // 导入可能需要更长时间
  })
}
```

#### 文件下载（Blob）
```javascript
exportExcel(formData) {
  return app.service.request({
    url: '/sys/person/export-excel',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'application/json' },
    responseType: 'blob', // 重要：设置为blob以处理文件下载
    timeout: 300000, // 导出超时时间：5分钟
  })
}
```

### 4.3 超时时间配置规范

**根据业务场景设置合理的超时时间**：

| 业务场景 | 超时时间 | 说明 |
|---------|---------|------|
| 普通查询 | 5000ms | 默认超时 |
| 长列表查询（大数据量） | 30000ms | 如1000条人员数据 |
| 文件导入 | 60000ms | 1分钟 |
| 文件导出 | 300000ms | 5分钟 |
| 文件下载模板 | 30000ms | 30秒 |

**示例**：
```javascript
// 普通请求
timeout: 5000,

// 长耗时列表查询
org: {
  list(formData) {
    return app.service.request({
      url: '/sys/person/org/list',
      method: 'post',
      data: formData,
      timeout: 30000, // 30秒
    })
  }
},

// 文件导出
exportExcel(formData) {
  return app.service.request({
    url: '/sys/person/export-excel',
    method: 'post',
    data: formData,
    responseType: 'blob',
    timeout: 300000, // 5分钟
  })
}
```

### 4.4 API路径前缀规范

**重要规则**：第三方应用相关API必须添加`/sys`前缀

```javascript
// 正确
list(formData) {
  return app.service.get('/sys/third-party-app/list', {params: formData})
}

// 错误（缺少/sys前缀）
list(formData) {
  return app.service.get('/third-party-app/list', {params: formData})
}
```

---

## 5. 数据交互规范

### 5.1 响应数据结构

**标准响应格式**：
```javascript
{
  code: 1,          // 1表示成功，其他表示失败
  message: '操作成功',
  data: {
    // 具体业务数据
  }
}
```

**列表响应格式**：
```javascript
{
  code: 1,
  message: '查询成功',
  data: {
    total: 100,    // 总记录数
    records: []    // 数据列表
  }
}
```

### 5.2 响应处理规范

```javascript
this.service.add(submitData).then((result) => {
  if (result.code == 1) {
    this.$message.success('新增成功');
    this.closeTpDialog({ success: true, action: 'add' });
  } else {
    this.$message.error('新增失败: ' + result.message)
  }
}).catch((err) => {
  console.log(err);
  this.$message.error('请求失败，请稍后重试')
})
```

**关键要点**：
1. **判断成功**：使用`result.code == 1`
2. **提示消息**：成功用`success`，失败用`error`
3. **异常捕获**：使用`.catch()`处理网络异常
4. **返回参数**：成功时传递`{ success: true, action }`

### 5.3 日期格式处理

**后端存储格式**：`YYYYMMDDHHmmss`（如：20241231235959）
**前端显示格式**：`YYYY-MM-DD HH:mm:ss`（如：2024-12-31 23:59:59）

**提交时转换**：
```javascript
if (submitData.expireTime) {
  submitData.expireTime = dayjs(submitData.expireTime).format('YYYYMMDDHHmmss');
} else {
  submitData.expireTime = ''
}
```

**回显时转换**：
```javascript
// 转为Date对象（用于datepicker）
if (this.formData.expireTime) {
  this.formData.expireTime = dayjs(this.formData.expireTime, 'YYYYMMDDHHmmss').toDate();
}

// 转为格式化字符串（用于显示）
formatTime(val) {
  if (!val || val === '' || val === null) {
    return '-';
  }
  try {
    return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
  } catch (e) {
    return '-';
  }
}
```

### 5.4 文件上传下载处理

#### 文件上传
```javascript
handleImportExcel() {
  // 创建文件输入元素
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = '.xlsx,.xls'
  input.style.display = 'none'

  input.onchange = (event) => {
    const file = event.target.files[0]
    if (!file) {
      return
    }

    // 验证文件类型
    const allowedTypes = [
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      'application/vnd.ms-excel',
    ]
    if (!allowedTypes.includes(file.type)) {
      this.$message.error('请选择Excel文件（.xlsx或.xls格式）')
      return
    }

    // 验证文件大小（限制为10MB）
    if (file.size > 10 * 1024 * 1024) {
      this.$message.error('文件大小不能超过10MB')
      return
    }

    this.importLoading = true

    // 创建FormData对象
    const formData = new FormData()
    formData.append('file', file)
    formData.append('deptId', this.selectNode.deptId)

    // 调用导入API
    app.$svc.sys.person.importExcel(formData).then((response) => {
      if (response.code === 1) {
        this.$message.success('导入成功')
        this.handleQuery()
      } else {
        this.$message.error('导入失败: ' + response.message)
      }
    }).finally(() => {
      this.importLoading = false
      document.body.removeChild(input)
    })
  }

  document.body.appendChild(input)
  input.click()
}
```

#### 文件下载
```javascript
handleExportExcel() {
  this.exportLoading = true

  const exportParams = {
    ...this.formData,
  }

  app.$svc.sys.person.exportExcel(exportParams).then((response) => {
    // 创建下载链接
    const blob = new Blob([response.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    })

    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url

    // 从响应头获取文件名
    let fileName = '导出文件.xlsx'
    if (response.headers && response.headers['content-disposition']) {
      const contentDisposition = response.headers['content-disposition']
      const fileNameMatch = contentDisposition.match(/filename=(.+)/)
      if (fileNameMatch) {
        fileName = decodeURIComponent(fileNameMatch[1])
      }
    }

    link.download = fileName
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)

    this.$message.success('导出成功')
  }).catch((error) => {
    console.error('导出失败:', error)
    this.$message.error('导出失败，请稍后重试')
  }).finally(() => {
    this.exportLoading = false
  })
}
```

---

## 6. 弹窗组件使用规范

### 6.1 tp-dialog 单页面弹窗

**组件引用**：
```vue
<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
```

**打开弹窗**：
```javascript
// 新增
handleAdd() {
  let param = {};
  let options = {"height": 500, "width": 700};
  this.$refs.TpDialog.show(
    import('./add.vue'),   // 弹窗页面组件
    param,                  // 传递参数
    "新增",                 // 弹窗标题
    options,                // 弹窗配置
    { action: 'add' }      // meta元数据
  );
}

// 修改
handleEdit(row) {
  let param = {"id": row.appId, "passKey": row.passKey};
  let options = {"height": 500, "width": 700};
  this.$refs.TpDialog.show(
    import('./add.vue'), 
    param, 
    "编辑", 
    options, 
    { action: 'edit' }
  );
}

// 查看
handleView(row) {
  let param = {"id": row.appId, "passKey": row.passKey}
  let options = {"height": 500, "width": 700};
  this.$refs.TpDialog.show(
    import('./view.vue'), 
    param, 
    "查看", 
    options, 
    { action: 'view' }
  );
}
```

**关闭弹窗**（在弹窗页面中）：
```javascript
// 直接关闭
handleClose() {
  this.closeTpDialog()
}

// 成功保存后关闭
save() {
  // ... 保存逻辑
  if (result.code == 1) {
    this.$message.success('保存成功');
    const action = this.meta?.action || 'add'
    this.closeTpDialog({ success: true, action });
  }
}
```

**关闭回调处理**（在列表页面中）：
```javascript
closeDialog(result) {
  if (!result || !result.success) {
    // 直接关闭或操作失败，不刷新列表
    return
  }
  
  if (result.action === 'add') {
    // 新增成功：重新查询（定位到第一页）
    this.$refs.table.doSearch()
  } else if (result.action === 'edit') {
    // 修改成功：刷新当前页
    this.$refs.table.doReload()
  }
  // 查看操作（action === 'view'）不刷新列表
}
```

### 6.2 tp-dialog-tab 多标签页弹窗

```javascript
let param = {
  'id': row.personId,
  'deptId': row.deptId,
  'passKey': row.passKey,
}
let tabArry = [
  {
    url: import('./add-basicinfo.vue'),
    label: '人员基本信息',
    icon: 'chart-line',
  },
  {
    url: import('./add-exinfo.vue'),
    label: '人员扩展信息',
    icon: "progressbar"
  }
]

this.$refs.TpDialogTab.show(tabArry, param, '新增', {
  callBack: () => {
    // 回调逻辑
  },
})
```

### 6.3 弹窗尺寸规范

**常用尺寸**：
```javascript
// 小弹窗（简单表单）
{"height": 300, "width": 500}

// 中等弹窗（标准表单）
{"height": 500, "width": 700}

// 大弹窗（复杂表单/权限配置）
{"height": 600, "width": 900}

// 特大弹窗（数据选择）
{"height": 700, "width": 1200}
```

---

## 7. 常见易错点与解决方案

### 7.1 列表刷新问题

**错误做法**：
```javascript
// 新增、修改、删除后都用doReload()
closeDialog(result) {
  this.$refs.table.doReload()  // ❌ 新增后应该回到第一页
}
```

**正确做法**：
```javascript
closeDialog(result) {
  if (!result || !result.success) {
    return
  }
  
  if (result.action === 'add') {
    // 新增成功：重新查询（回到第一页）✅
    this.$refs.table.doSearch()
  } else if (result.action === 'edit') {
    // 修改成功：刷新当前页 ✅
    this.$refs.table.doReload()
  }
}
```

### 7.2 日期格式转换遗漏

**错误做法**：
```javascript
// 直接提交Date对象
save() {
  this.service.add(this.formData)  // ❌ 后端无法识别Date对象
}
```

**正确做法**：
```javascript
save() {
  const submitData = { ...this.formData };
  
  // 转换日期格式 ✅
  if (submitData.expireTime) {
    submitData.expireTime = dayjs(submitData.expireTime).format('YYYYMMDDHHmmss');
  } else {
    submitData.expireTime = ''
  }
  
  this.service.add(submitData)
}
```

### 7.3 表单验证缺失

**错误做法**：
```javascript
save() {
  // 直接提交，不验证 ❌
  this.service.add(this.formData)
}
```

**正确做法**：
```javascript
save() {
  // 先验证再提交 ✅
  this.$refs.fbform.validate((result) => {
    if (result === true) {
      this.service.add(this.formData)
    }
  })
}
```

### 7.4 删除操作无确认

**错误做法**：
```javascript
handleDel(row) {
  // 直接删除，无确认 ❌
  this.delete(row.appId, row.passKey);
}
```

**正确做法**：
```javascript
handleDel(row) {
  // 二次确认 ✅
  this.$confirm('确定要删除该应用吗？删除后将无法恢复！', () => {
    this.delete(row.appId, row.passKey);
  })
}
```

### 7.5 Service调用路径错误

**错误做法**：
```javascript
// 缺少/sys前缀 ❌
list(formData) {
  return app.service.get('/third-party-app/list', {params: formData})
}
```

**正确做法**：
```javascript
// 正确添加/sys前缀 ✅
list(formData) {
  return app.service.get('/sys/third-party-app/list', {params: formData})
}
```

### 7.6 超时时间配置不当

**错误做法**：
```javascript
// 大数据量查询使用默认超时 ❌
org: {
  list(formData) {
    return app.service.request({
      url: '/sys/person/org/list',
      method: 'post',
      data: formData,
      timeout: 5000,  // ❌ 1000条数据可能超时
    })
  }
}
```

**正确做法**：
```javascript
// 根据数据量设置合理超时 ✅
org: {
  list(formData) {
    return app.service.request({
      url: '/sys/person/org/list',
      method: 'post',
      data: formData,
      timeout: 30000,  // ✅ 30秒，适合大数据量
    })
  }
}
```

### 7.7 表格主键配置错误

**错误做法**：
```javascript
table: {
  service: app.$svc.sys.thirdPartyApp,
  primaryKey: "id",  // ❌ 主键字段名错误
  columns: [...]
}
```

**正确做法**：
```javascript
table: {
  service: app.$svc.sys.thirdPartyApp,
  primaryKey: "appId",  // ✅ 正确的主键字段名
  columns: [...]
}
```

### 7.8 meta参数传递遗漏

**错误做法**：
```javascript
// 不传meta参数 ❌
this.$refs.TpDialog.show(import('./add.vue'), param, "新增", options);
```

**正确做法**：
```javascript
// 传递meta参数 ✅
this.$refs.TpDialog.show(import('./add.vue'), param, "新增", options, { action: 'add' });
```

### 7.9 空值处理不当

**错误做法**：
```javascript
// 直接显示undefined或null ❌
<fb-property-item label="应用描述">
  {{formData.description}}
</fb-property-item>
```

**正确做法**：
```javascript
// 空值显示默认文案 ✅
<fb-property-item label="应用描述">
  {{formData.description || '-'}}
</fb-property-item>
```

### 7.10 字段隐藏不规范

**项目特定规则**：在third-party-app模块中，IP白名单字段需在前端各页面（add.vue、list.vue、view.vue）中移除显示，但保留formData结构兼容性；API Secret仅前端删除展示逻辑，后端均不作处理。

**正确做法**：
```javascript
// formData保留字段定义，确保兼容性 ✅
formData: {
  appId: '',
  appName: '',
  ipWhitelist: '',  // ✅ 保留字段，但不在UI中显示
  status: 1,
}

// 页面中注释掉字段显示 ✅
// <fb-form-item label="IP白名单">
//   <fb-input v-model="formData.ipWhitelist"></fb-input>
// </fb-form-item>
```

---

## 8. 最佳实践清单

### 8.1 页面开发检查清单

**list.vue（列表页）**：
- [ ] 使用`fb-page-search`或`fb-page-tree-table`作为页面容器
- [ ] 查询表单设置`mode="query"`
- [ ] 表格设置`ref="table"`
- [ ] 表格配置正确的`pk`（主键字段）
- [ ] 查询按钮调用`this.$refs.table.doSearch()`
- [ ] 新增/修改/删除按钮有权限控制`v-permission`
- [ ] 删除操作有`$confirm`二次确认
- [ ] 弹窗传递`meta`参数区分操作类型
- [ ] `closeDialog`回调正确处理刷新逻辑（新增用doSearch，修改用doReload）
- [ ] 时间字段使用formatters格式化显示

**add.vue（新增/编辑页）**：
- [ ] 定义`props`: `param`, `parentPage`, `meta`
- [ ] 表单设置`ref="fbform"`
- [ ] 必填字段设置`:rule="[{required: true}]"`
- [ ] 保存前调用`this.$refs.fbform.validate()`
- [ ] 新增/修改逻辑通过`this.formData.appId`判断
- [ ] 日期字段提交前转换为`YYYYMMDDHHmmss`格式
- [ ] 成功保存后传递`{ success: true, action }`
- [ ] 编辑时调用`view`方法回显数据
- [ ] 回显时日期字段转换为Date对象

**view.vue（查看页）**：
- [ ] 使用`fb-property`展示数据
- [ ] 定义`props`: `param`, `parentPage`, `meta`
- [ ] 空值显示默认文案（如`'-'`、`'永不过期'`）
- [ ] 时间字段使用格式化方法显示
- [ ] 状态字段使用样式类显示（如`.status-valid`）
- [ ] 关闭按钮调用`this.closeTpDialog()`

### 8.2 Service开发检查清单

- [ ] 文件位置：`@fb/admin-base/service/sys/{moduleName}/index.js`
- [ ] 导出默认对象：`export default {}`
- [ ] 列表查询方法名：`list`
- [ ] 新增方法名：`add`
- [ ] 修改方法名：`update`
- [ ] 查看方法名：`view`
- [ ] 删除方法名：`delete`
- [ ] API路径包含正确前缀（如`/sys/`）
- [ ] POST请求配置`transformRequest`序列化
- [ ] 设置正确的`Content-Type`
- [ ] 配置合理的`timeout`时间
- [ ] 文件上传使用`multipart/form-data`
- [ ] 文件下载使用`responseType: 'blob'`

### 8.3 数据处理检查清单

- [ ] 日期提交时转换为`YYYYMMDDHHmmss`
- [ ] 日期回显时转换为Date对象或格式化字符串
- [ ] 空值处理（显示默认文案）
- [ ] 数字字段类型正确（不要传字符串）
- [ ] 下拉选项数据格式：`{value, label}`
- [ ] 响应判断使用`result.code == 1`
- [ ] 错误信息显示`result.message`
- [ ] 异常捕获使用`.catch()`

### 8.4 组件使用检查清单

**表单组件**：
- [ ] `fb-input`设置`maxlength`限制长度
- [ ] `fb-select`绑定`:data`属性
- [ ] `fb-datepicker`设置`format`格式
- [ ] `fb-textarea`设置`rows`和`maxlength`

**表格组件**：
- [ ] 列配置包含`name`、`label`
- [ ] 操作列使用`slot="actions"`
- [ ] 自定义列使用`v-slot`
- [ ] 冻结列设置`freeze`属性
- [ ] 列宽度合理分配

**弹窗组件**：
- [ ] 弹窗组件设置`ref`
- [ ] 弹窗页面定义`props`
- [ ] 弹窗标题语义明确
- [ ] 弹窗尺寸合理设置
- [ ] 传递`meta`参数
- [ ] 关闭回调正确处理

### 8.5 代码质量检查清单

- [ ] 组件命名使用PascalCase
- [ ] 方法命名语义清晰（如`handleAdd`、`handleEdit`）
- [ ] 变量命名符合驼峰规则
- [ ] 代码缩进统一（2空格或Tab）
- [ ] 注释清晰（关键逻辑加注释）
- [ ] 无console.log残留（除必要日志）
- [ ] 异常处理完善（try-catch或.catch()）
- [ ] 加载状态管理（loading变量）
- [ ] 按钮防重复点击（disabled或loading）

---

## 附录：常用代码片段

### A1. 标准列表页骨架
```vue
<template>
  <div>
    <fb-page-search>
      <template slot="query">
        <fb-form ref="query-form" mode="query">
          <!-- 查询条件 -->
        </fb-form>
      </template>
      <template slot="buttons">
        <fb-button @on-click="handleAdd" icon="add-circle">新增</fb-button>
      </template>
      <template slot="actions">
        <fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
      </template>
      <template slot="table">
        <fb-simple-table ref="table" ...></fb-simple-table>
      </template>
    </fb-page-search>
    <tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
  </div>
</template>

<script>
export default {
  name: 'list',
  data() {
    return {
      formData: {},
      table: {
        service: app.$svc.sys.module,
        primaryKey: "id",
        columns: []
      }
    }
  },
  methods: {
    handleQuery() {
      this.$refs.table.doSearch()
    },
    handleAdd() {
      this.$refs.TpDialog.show(import('./add.vue'), {}, "新增", {height: 500, width: 700}, {action: 'add'});
    },
    closeDialog(result) {
      if (!result || !result.success) return
      if (result.action === 'add') {
        this.$refs.table.doSearch()
      } else if (result.action === 'edit') {
        this.$refs.table.doReload()
      }
    }
  }
}
</script>
```

### A2. 标准新增/编辑页骨架
```vue
<template>
  <div class="tp-dialog">
    <div class="tp-dialog-top" style="padding-top: 0;">
      <fb-form ref="fbform">
        <!-- 表单字段 -->
      </fb-form>
    </div>
    <div class="tp-dialog-bottom">
      <fb-button type="primary" @on-click="save">保存</fb-button>
      <fb-button @on-click="handleClose">关闭</fb-button>
    </div>
  </div>
</template>

<script>
import dayjs from "dayjs";

export default {
  name: 'add',
  props: {
    param: { type: Object, require: false },
    meta: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      service: this.$svc.sys.module,
      formData: {}
    }
  },
  mounted() {
    if (this.param && this.param.id) {
      this.view(this.param.id, this.param.passKey);
    }
  },
  methods: {
    save() {
      this.$refs.fbform.validate((result) => {
        if (result === true) {
          const submitData = { ...this.formData };
          // 数据处理
          if (this.formData.id) {
            this.service.update(submitData).then(this.handleSuccess)
          } else {
            this.service.add(submitData).then(this.handleSuccess)
          }
        }
      })
    },
    handleSuccess(result) {
      if (result.code == 1) {
        this.$message.success('操作成功');
        const action = this.meta?.action || 'add'
        this.closeTpDialog({ success: true, action });
      } else {
        this.$message.error('操作失败: ' + result.message)
      }
    },
    handleClose() {
      this.closeTpDialog()
    },
    view(id, passKey) {
      this.service.view({id, passKey}).then((result) => {
        if (result.code == 1) {
          this.formData = result.data;
        }
      })
    }
  }
}
</script>
```

### A3. 标准Service骨架
```javascript
export default {
  list(formData) {
    return app.service.get('/sys/module/list', {params: formData})
  },

  add(formData) {
    return app.service.request({
      url: '/sys/module/add',
      method: 'post',
      transformRequest: [
        function (data) {
          let ret = ''
          for (let it in data) {
            ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
          }
          return ret.substring(0, ret.lastIndexOf('&'))
        },
      ],
      data: formData,
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      responseType: 'json',
      timeout: 5000,
    })
  },

  update(formData) {
    return app.service.request({
      url: '/sys/module/update',
      method: 'post',
      transformRequest: [
        function (data) {
          let ret = ''
          for (let it in data) {
            ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
          }
          return ret.substring(0, ret.lastIndexOf('&'))
        },
      ],
      data: formData,
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      responseType: 'json',
      timeout: 5000,
    })
  },

  view(formData) {
    return app.service.get('/sys/module/view', {params: formData})
  },

  delete(formData) {
    return app.service.get('/sys/module/delete', {params: formData})
  }
}
```

---

## 文档版本

- **版本号**: v1.0.0
- **创建日期**: 2025年1月
- **适用项目**: PS-BMP前端管理系统
- **维护状态**: 持续更新

## 总结

本文档涵盖了PS-BMP前端项目的完整编码规范，包括：
1. fb-ui组件库的正确使用方法
2. 标准页面模板（list、add、view）的开发规范
3. Service层的编码规范和请求配置
4. 数据交互、日期处理、文件上传下载等常见场景
5. 弹窗组件的使用和参数传递
6. 常见易错点及正确解决方案
7. 完整的开发检查清单

遵循本规范可确保代码质量一致性、减少常见错误、提高开发效率。建议开发人员（包括AI Agent）在编码前仔细阅读相关章节，并使用检查清单进行自查。
