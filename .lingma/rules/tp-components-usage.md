---
trigger: model_decision
description: tp-components 业务组件库使用规范，包括弹窗、上传、日期选择器等组件
---

# tp-components 业务组件库使用规范

## 一、tp-dialog 单页面弹窗

### 1. 基本使用

```vue
<template>
  <div>
    <fb-button @click="handleAdd">新增</fb-button>
    
    <!-- 弹窗组件 -->
    <tp-dialog ref="TpDialog" @close="closeDialog" />
  </div>
</template>

<script>
export default {
  methods: {
    /**
     * 打开新增弹窗
     */
    handleAdd() {
      const param = { action: 'add' }
      const options = { width: '60%' }
      const meta = { action: 'add' }
      
      this.$refs.TpDialog.show(
        import('./add.vue'),  // ✅ 推荐：使用 import 方式
        param,
        "新增XXX",
        options,
        meta
      )
    },
    
    /**
     * 弹窗关闭回调
     */
    closeDialog(result) {
      if (!result || !result.success) return
      
      if (result.action === 'add') {
        this.$refs.table.doSearch()  // 新增后回到第一页
      } else if (result.action === 'edit') {
        this.$refs.table.doReload()  // 修改后刷新当前页
      }
    }
  }
}
</script>
```

### 2. show() 方法参数

```javascript
this.$refs.TpDialog.show(
  component,   // 组件：import('./add.vue') 或 () => import('./add.vue')
  param,       // 参数：传递给子组件的数据
  title,       // 标题：弹窗标题
  options,     // 配置：{ width, height, fullscreen }
  meta         // 元数据：{ action, ... }
)
```

### 3. 响应式宽度规则

tp-dialog 会根据屏幕宽度自动调整弹窗宽度：

| 设置宽度 | 屏幕 ≤ 1440px | 屏幕 > 1440px |
|---------|--------------|--------------|
| 50% | 70% | 50% |
| 60% | 80% | 60% |
| 70% | 90% | 70% |
| 80% | 95% | 80% |
| 90% | 98% | 90% |

**推荐配置**：
- 表单页面：`{ width: '60%' }`
- 详情页面：`{ width: '70%' }`
- 复杂表单：`{ width: '80%' }`

### 4. 子组件接收参数

```vue
<template>
  <fb-form :model="formData" ref="fbform">
    <!-- 表单内容 -->
  </fb-form>
</template>

<script>
export default {
  props: {
    param: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      formData: {}
    }
  },
  mounted() {
    // 接收父组件传递的参数
    if (this.param.action === 'edit') {
      this.formData = { ...this.param }
    }
  }
}
</script>
```

### 5. 子组件关闭弹窗

```javascript
/**
 * 提交表单并关闭弹窗
 */
async handleSubmit() {
  const valid = await this.$refs.fbform.validate()
  if (!valid) return
  
  const result = await xxxService.add(this.formData)
  
  if (result.success) {
    this.$message.success(result.message || '操作成功')
    
    // 关闭弹窗并通知父组件
    this.$emit('close', {
      success: true,
      action: this.param.action  // 传递操作类型
    })
  } else {
    this.$message.error(result.message || '操作失败')
  }
}

/**
 * 取消并关闭弹窗
 */
handleCancel() {
  this.$emit('close', { success: false })
}
```

## 二、tp-dialog-tab 多标签页弹窗

### 1. 基本使用

```vue
<template>
  <div>
    <fb-button @click="handleEdit">编辑</fb-button>
    
    <tp-dialog-tab ref="TpDialogTab" @close="closeDialog" />
  </div>
</template>

<script>
export default {
  methods: {
    handleEdit() {
      const param = {
        action: 'edit',
        id: '123'
      }
      
      const tabs = [
        {
          title: '基本信息',
          component: import('./tabs/base-info.vue'),
          name: 'baseInfo'
        },
        {
          title: '详细信息',
          component: import('./tabs/detail-info.vue'),
          name: 'detailInfo'
        },
        {
          title: '附件信息',
          component: import('./tabs/file-info.vue'),
          name: 'fileInfo'
        }
      ]
      
      const options = { width: '70%' }
      
      this.$refs.TpDialogTab.show(
        tabs,
        param,
        "编辑XXX",
        options
      )
    }
  }
}
</script>
```

### 2. 标签页配置

```javascript
const tabs = [
  {
    title: '基本信息',           // Tab 标题
    component: import('./base.vue'),  // Tab 组件
    name: 'baseInfo',            // Tab 唯一标识
    closable: false              // 是否可关闭（可选，默认 false）
  }
]
```

### 3. Tab 切换确认

tp-dialog-tab 支持 Tab 切换时的确认提示：

```vue
<script>
export default {
  data() {
    return {
      formData: {},
      hasChanged: false  // 标记表单是否修改
    }
  },
  watch: {
    formData: {
      handler() {
        this.hasChanged = true
      },
      deep: true
    }
  },
  methods: {
    /**
     * Tab 切换前的确认
     * 返回 Promise，resolve(true) 允许切换，resolve(false) 取消切换
     */
    beforeTabSwitch() {
      if (!this.hasChanged) {
        return Promise.resolve(true)
      }
      
      return this.$confirm('当前页面有未保存的修改，确定要离开吗？', '提示', {
        type: 'warning'
      }).then(() => {
        return true
      }).catch(() => {
        return false
      })
    }
  }
}
</script>
```

### 4. 跨 Tab 数据传递

tp-dialog-tab 支持跨 Tab 数据传递：

```javascript
// Tab1 中设置共享数据
this.$emit('setSharedData', { userId: '123' })

// Tab2 中获取共享数据
mounted() {
  this.$emit('getSharedData', (data) => {
    console.log(data.userId)  // '123'
  })
}
```

### 5. Tab 动态关闭

```javascript
// 关闭指定 Tab
this.$emit('closeTab', 'tabName')

// 关闭当前 Tab
this.$emit('closeCurrentTab')
```

## 三、tp-dialog-flow-tab 流程标签页弹窗

### 1. 基本使用

```vue
<script>
export default {
  methods: {
    handleAdd() {
      const tabs = [
        {
          title: '第一步',
          component: import('./steps/step1.vue'),
          name: 'step1'
        },
        {
          title: '第二步',
          component: import('./steps/step2.vue'),
          name: 'step2'
        },
        {
          title: '第三步',
          component: import('./steps/step3.vue'),
          name: 'step3'
        }
      ]
      
      this.$refs.TpDialogFlowTab.show(
        tabs,
        {},
        "新增流程",
        { width: '70%' }
      )
    }
  }
}
</script>
```

### 2. 特性

- **步骤条导航**：顶部显示步骤条，可点击跳转
- **自由切换**：可以点击步骤条跳转到任意步骤
- **流程控制**：支持 next()、prev() 方法控制流程

### 3. 步骤组件

```vue
<template>
  <div>
    <fb-form :model="formData" ref="fbform">
      <!-- 表单内容 -->
    </fb-form>
    
    <div style="text-align: center; margin-top: 20px;">
      <fb-button @click="handlePrev" v-if="currentStep > 0">上一步</fb-button>
      <fb-button type="primary" @click="handleNext">下一步</fb-button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      formData: {},
      currentStep: 0
    }
  },
  methods: {
    async handleNext() {
      const valid = await this.$refs.fbform.validate()
      if (!valid) return
      
      // 保存当前步骤数据
      this.$emit('setSharedData', this.formData)
      
      // 跳转到下一步
      this.$emit('next')
    },
    
    handlePrev() {
      this.$emit('prev')
    }
  }
}
</script>
```

## 四、tp-dialog-step-tab 步骤标签页弹窗

### 1. 基本使用

使用方式与 `tp-dialog-flow-tab` 相同。

### 2. 区别

| 特性 | tp-dialog-flow-tab | tp-dialog-step-tab |
|------|-------------------|-------------------|
| 步骤条 | 可点击跳转 | 不可点击 |
| 切换方式 | 步骤条点击 + next()/prev() | 仅 next()/prev() |
| 使用场景 | 允许自由跳转的流程 | 严格顺序的流程 |

### 3. 步骤控制

```javascript
// 下一步
this.$emit('next')

// 上一步
this.$emit('prev')

// 跳转到指定步骤（仅 tp-dialog-flow-tab 支持）
this.$emit('goToStep', 2)
```

## 五、tp-upload 文件上传

### 1. list 视图（列表模式）

```vue
<template>
  <fb-form-item label="附件" prop="fileList">
    <tp-upload
      v-model="formData.fileList"
      :limit="5"
      :file-size="10"
      list-type="list"
      accept=".pdf,.doc,.docx"
    />
  </fb-form-item>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        fileList: []  // [{ name: 'file.pdf', url: 'http://...' }]
      }
    }
  }
}
</script>
```

### 2. image 视图（图片模式）

```vue
<template>
  <fb-form-item label="图片" prop="imageList">
    <tp-upload
      v-model="formData.imageList"
      :limit="3"
      :file-size="5"
      list-type="image"
      accept="image/*"
    />
  </fb-form-item>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        imageList: []  // [{ name: 'img.jpg', url: 'http://...' }]
      }
    }
  }
}
</script>
```

### 3. avatar 视图（头像模式）

```vue
<template>
  <fb-form-item label="头像" prop="avatar">
    <tp-upload
      v-model="formData.avatar"
      :file-size="2"
      list-type="avatar"
      accept="image/*"
    />
  </fb-form-item>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        avatar: ""  // 单个文件路径字符串
      }
    }
  }
}
</script>
```

### 4. 参数说明

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| v-model | Array/String | - | list/image 模式使用 Array，avatar 模式使用 String |
| limit | Number | 3 | 最大上传数量 |
| file-size | Number | 10 | 文件大小限制（MB） |
| list-type | String | 'list' | 视图模式：list/image/avatar |
| accept | String | '*' | 接受的文件类型 |
| upload-url | String | '/sys/common/upload' | 上传接口地址 |

### 5. 数据格式

```javascript
// list 和 image 模式
formData.fileList = [
  {
    name: "文件名.pdf",
    url: "http://domain.com/files/xxx.pdf"
  }
]

// avatar 模式
formData.avatar = "http://domain.com/files/avatar.jpg"
```

## 六、tp-upload-path 路径上传

### 1. 基本使用

```vue
<template>
  <fb-form-item label="附件路径" prop="filePath">
    <tp-upload-path
      v-model="formData.filePath"
      :limit="5"
      :file-size="10"
    />
  </fb-form-item>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        filePath: ""  // 多个文件路径用逗号分隔
      }
    }
  }
}
</script>
```

### 2. 数据格式

```javascript
// 单个文件
formData.filePath = "files/2024/12/xxx.pdf"

// 多个文件（逗号分隔）
formData.filePath = "files/2024/12/xxx.pdf,files/2024/12/yyy.doc"
```

### 3. 与 tp-upload 的区别

| 特性 | tp-upload | tp-upload-path |
|------|-----------|---------------|
| 数据格式 | 对象数组 | 字符串（逗号分隔） |
| 使用场景 | 新系统 | 兼容旧系统数据格式 |

## 七、tp-datepicker 日期选择器

### 1. 基本使用

```vue
<template>
  <fb-form-item label="生效时间" prop="effectTime">
    <tp-datepicker
      v-model="formData.effectTime"
      :value-format="'YYYY-MM-DD HH:mm:ss'"
      :time-picker-options="{ format: 'HH:mm:ss' }"
      placeholder="请选择生效时间"
    />
  </fb-form-item>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        effectTime: ""  // YYYY-MM-DD HH:mm:ss 格式
      }
    }
  }
}
</script>
```

### 2. 自动格式转换

tp-datepicker 会自动将以下类型转换为字符串：

```javascript
// Date 对象 → 字符串
formData.effectTime = new Date()
// 自动转换为：2024-12-01 14:30:45

// 时间戳 → 字符串
formData.effectTime = 1701417045000
// 自动转换为：2024-12-01 14:30:45

// 已经是字符串 → 保持不变
formData.effectTime = "2024-12-01 14:30:45"
```

### 3. 日期范围选择

```vue
<template>
  <fb-form-item label="时间范围" prop="timeRange">
    <tp-datepicker
      v-model="formData.timeRange"
      type="datetimerange"
      range-separator="至"
      start-placeholder="开始时间"
      end-placeholder="结束时间"
      :value-format="'YYYY-MM-DD HH:mm:ss'"
    />
  </fb-form-item>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        timeRange: []  // ['2024-12-01 00:00:00', '2024-12-31 23:59:59']
      }
    }
  }
}
</script>
```

## 八、必须遵守的规则

1. **弹窗组件必须设置 ref**：`<tp-dialog ref="TpDialog" />`
2. **弹窗必须监听 close 事件**：`@close="closeDialog"`
3. **使用 import 方式加载组件**：`import('./add.vue')`
4. **弹窗宽度使用百分比**：推荐 60%、70%、80%
5. **子组件关闭弹窗传递结果**：`this.$emit('close', { success: true, action: 'add' })`
6. **tp-upload 必须设置 limit 和 file-size**
7. **tp-datepicker 必须设置 value-format**：`'YYYY-MM-DD HH:mm:ss'`
8. **list/image 模式使用数组**：`fileList: []`
9. **avatar 模式使用字符串**：`avatar: ""`

## 九、常见错误和解决方案

### 1. ❌ 错误：未设置弹窗 ref

```vue
<!-- ❌ 错误 -->
<tp-dialog @close="closeDialog" />
```

```vue
<!-- ✅ 正确 -->
<tp-dialog ref="TpDialog" @close="closeDialog" />
```

### 2. ❌ 错误：未监听 close 事件

```vue
<!-- ❌ 错误 -->
<tp-dialog ref="TpDialog" />
```

```vue
<!-- ✅ 正确 -->
<tp-dialog ref="TpDialog" @close="closeDialog" />
```

### 3. ❌ 错误：子组件未传递结果

```javascript
// ❌ 错误
this.$emit('close')
```

```javascript
// ✅ 正确
this.$emit('close', { success: true, action: 'add' })
```

### 4. ❌ 错误：avatar 模式使用数组

```javascript
// ❌ 错误
formData: {
  avatar: []
}
```

```javascript
// ✅ 正确
formData: {
  avatar: ""
}
```

### 5. ❌ 错误：未设置 value-format

```vue
<!-- ❌ 错误 -->
<tp-datepicker v-model="formData.effectTime" />
```

```vue
<!-- ✅ 正确 -->
<tp-datepicker
  v-model="formData.effectTime"
  :value-format="'YYYY-MM-DD HH:mm:ss'"
/>
```
