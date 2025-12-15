---
trigger: model_decision
description: 前端页面开发规范，包括 list/add/view 页面模板
---

# 前端页面开发规范

## list.vue 列表页规范

### 必须使用的组件结构

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
```

### data 结构规范

```javascript
data() {
  return {
    formData: {
      // 查询字段
      logDelete: 0,  // 必须：逻辑删除标记
    },
    
    table: {
      service: app.$svc.sys.moduleName,  // 必须：数据服务
      primaryKey: "xxxId",                // 必须：主键字段
      columns: [],                        // 必须：列配置
    },
    
    formatters: {
      // 数据格式化方法
    }
  }
}
```

### 必须实现的方法

```javascript
methods: {
  // 查询方法（必须）
  handleQuery() {
    this.$refs.table.doSearch()
  },
  
  // 新增方法（必须）
  handleAdd() {
    let param = {};
    let options = { height: 500, width: 700 };
    this.$refs.TpDialog.show(import('./add.vue'), param, "新增", options, { action: 'add' });
  },
  
  // 修改方法（必须）
  handleEdit(row) {
    let param = { id: row.xxxId, passKey: row.passKey };
    let options = { height: 500, width: 700 };
    this.$refs.TpDialog.show(import('./add.vue'), param, "修改", options, { action: 'edit' });
  },
  
  // 删除方法（必须）
  handleDel(row) {
    this.$confirm('确定要删除吗？删除后将无法恢复！', () => {
      this.delete(row.xxxId, row.passKey);
    })
  },
  
  // 查看方法（可选）
  handleView(row) {
    let param = { id: row.xxxId, passKey: row.passKey };
    let options = { height: 500, width: 700 };
    this.$refs.TpDialog.show(import('./view.vue'), param, "查看", options, { action: 'view' });
  },
  
  // 弹窗关闭回调（必须）
  closeDialog(result) {
    if (!result || !result.success) return;
    
    if (result.action === 'add') {
      this.$refs.table.doSearch()  // 新增后回到第一页
    } else if (result.action === 'edit') {
      this.$refs.table.doReload()  // 修改后刷新当前页
    }
  }
}
```

## add.vue 新增/编辑页规范

### 必须使用的组件结构

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
```

### props 定义规范（必须）

```javascript
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
}
```

### 必须实现的方法

```javascript
methods: {
  // 初始化方法（必须）
  init(param) {
    if (param && param.id) {
      this.formData.xxxId = param.id;
      this.view(param.id, param.passKey);
    }
  },
  
  // 保存方法（必须）
  save() {
    this.$refs.fbform.validate((result) => {
      if (result === true) {
        const submitData = { ...this.formData };
        
        // 日期格式转换（必须）
        if (submitData.expireTime) {
          submitData.expireTime = dayjs(submitData.expireTime).format('YYYYMMDDHHmmss');
        }
        
        if (this.formData.xxxId) {
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
  
  // 查看方法（修改时必须）
  view(id, passKey) {
    this.service.view({ xxxId: id, passKey: passKey }).then((result) => {
      if (result.code == 1) {
        this.formData = result.data;
        
        // 日期回显转换（必须）
        if (this.formData.expireTime) {
          this.formData.expireTime = dayjs(this.formData.expireTime, 'YYYYMMDDHHmmss').toDate();
        }
      } else {
        this.$message.error('查询失败: ' + result.message)
      }
    })
  },
  
  // 关闭方法（必须）
  handleClose() {
    this.closeTpDialog()
  }
}
```

## view.vue 查看页规范

### 必须使用的组件结构

```vue
<template>
  <div class="tp-dialog">
    <div class="tp-dialog-top">
      <fb-property bordered label-width="140px">
        <fb-property-item label="字段名" span="2">
          {{ formData.fieldName || '-' }}
        </fb-property-item>
      </fb-property>
    </div>
    
    <div class="tp-dialog-bottom">
      <fb-button @on-click="handleClose">关闭</fb-button>
    </div>
  </div>
</template>
```

### 必须实现的方法

```javascript
methods: {
  // 初始化方法（必须）
  init(param) {
    if (param && param.id) {
      this.formData.xxxId = param.id;
      this.view(param.id, param.passKey);
    }
  },
  
  // 格式化时间（必须）
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
  
  // 查看方法（必须）
  view(id, passKey) {
    this.service.view({ xxxId: id, passKey: passKey }).then((result) => {
      if (result.code == 1) {
        this.formData = result.data;
      } else {
        this.$message.error('查询失败: ' + result.message)
      }
    })
  },
  
  // 关闭方法（必须）
  handleClose() {
    this.closeTpDialog()
  }
}
```

## 命名规范

### 文件命名
- 列表页：`list.vue`
- 新增/编辑页：`add.vue`
- 查看页：`view.vue`

### 变量命名
- 表单数据：`formData`
- 表格配置：`table`
- 服务引用：`service`
- 选项数据：`xxxOptions`（如 `statusOptions`）

### 方法命名
- 查询：`handleQuery`
- 新增：`handleAdd`
- 修改：`handleEdit`
- 删除：`handleDel`
- 查看：`handleView`
- 关闭：`handleClose`
- 弹窗回调：`closeDialog` / `closeDialogTab`

## 必须遵守的规则

1. **必须引入 dayjs**：`import dayjs from "dayjs"`
2. **必须设置 ref**：表单必须设置 `ref="fbform"`，表格必须设置 `ref="table"`
3. **必须验证表单**：保存前调用 `this.$refs.fbform.validate()`
4. **必须转换日期**：提交时转为 `YYYYMMDDHHmmss`，回显时转为 `Date` 对象
5. **必须传递 meta**：弹窗必须传递 `{ action: 'add/edit/view' }`
6. **必须区分刷新**：新增用 `doSearch()`，修改用 `doReload()`
7. **必须删除确认**：删除操作必须使用 `$confirm` 二次确认
8. **必须处理空值**：查看页显示数据时使用 `|| '-'` 处理空值
