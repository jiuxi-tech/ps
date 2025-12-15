# tp-components 业务组件库使用规范

## 📋 目录

- [1. 组件库概述](#1-组件库概述)
- [2. tp-dialog - 单页面弹窗](#2-tp-dialog---单页面弹窗)
- [3. tp-dialog-tab - 多标签页弹窗](#3-tp-dialog-tab---多标签页弹窗)
- [4. tp-dialog-flow-tab - 流程标签页弹窗](#4-tp-dialog-flow-tab---流程标签页弹窗)
- [5. tp-dialog-step-tab - 步骤标签页弹窗](#5-tp-dialog-step-tab---步骤标签页弹窗)
- [6. tp-upload - 文件上传组件](#6-tp-upload---文件上传组件)
- [7. tp-upload-path - 路径上传组件](#7-tp-upload-path---路径上传组件)
- [8. tp-datepicker - 日期选择器](#8-tp-datepicker---日期选择器)
- [9. 常见错误和陷阱](#9-常见错误和陷阱)

---

## 1. 组件库概述

### 1.1 tp-components 是什么？

`tp-components` 是基于 `fb-ui` 封装的**业务组件库**，专门为项目业务场景设计：

| 基础库 | 业务库 | 关系 |
|--------|--------|------|
| fb-ui  | tp-components | tp-components 在 fb-ui 基础上增加业务逻辑 |
| fb-dialog | tp-dialog | 增加动态组件加载、响应式尺寸 |
| fb-upload | tp-upload | 增加附件回显、自动压缩 |
| fb-datepicker | tp-datepicker | 增加格式自动转换、数据库格式支持 |

### 1.2 组件列表

| 组件名称 | 用途 | 使用场景 |
|---------|------|---------|
| **tp-dialog** | 单页面弹窗 | 新增、修改、查看详情 |
| **tp-dialog-tab** | 多标签页弹窗 | 复杂表单分Tab填写（可切换） |
| **tp-dialog-flow-tab** | 流程标签页弹窗 | 流程式操作（步骤条可点击切换） |
| **tp-dialog-step-tab** | 步骤标签页弹窗 | 步骤式操作（只能上下步） |
| **tp-upload** | 文件上传 | 头像、图片、附件上传 |
| **tp-upload-path** | 路径上传 | 基于路径的文件上传 |
| **tp-datepicker** | 日期选择器 | 日期时间选择，自动格式转换 |

### 1.3 导入方式

```javascript
// 组件已全局注册，直接使用即可
// 在模板中声明组件
<template>
  <div>
    <tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
    <tp-upload v-model="formData.fileList" :service="$svc.sys.file"></tp-upload>
  </div>
</template>
```

---

## 2. tp-dialog - 单页面弹窗

### 2.1 基本使用

**适用场景**：新增、修改、查看等单页面操作

```vue
<template>
  <div>
    <!-- 1. 声明组件 -->
    <tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>

    <!-- 2. 触发按钮 -->
    <fb-button @click="handleAdd">新增</fb-button>
  </div>
</template>

<script>
export default {
  methods: {
    // 3. 打开弹窗
    handleAdd() {
      let param = {};  // 传递给子组件的参数
      let options = {
        height: 500,
        width: 700
      };
      
      // ✅ 推荐：使用 import 方式（动态加载）
      this.$refs.TpDialog.show(
        import('./add.vue'),  // 组件路径
        param,                // 参数
        "新增第三方应用",      // 标题
        options,              // 弹窗配置
        { action: 'add' }     // meta 元数据
      );
    },

    // 4. 关闭回调
    closeDialog(param) {
      if (param && param.refresh) {
        this.doSearch();  // 刷新列表
      }
    }
  }
}
</script>
```

### 2.2 show 方法详解

```javascript
/**
 * @param url - 组件路径（支持三种方式）
 * @param param - 传递给子组件的参数对象
 * @param title - 弹窗标题
 * @param options - 弹窗配置项
 * @param meta - 元数据（可选）
 */
this.$refs.TpDialog.show(url, param, title, options, meta)
```

#### 参数说明

| 参数 | 类型 | 说明 | 示例 |
|------|------|------|------|
| **url** | String/Function/Promise | 组件路径 | 见下方三种方式 |
| **param** | Object | 子组件参数 | `{ id: '123', type: 'edit' }` |
| **title** | String | 弹窗标题 | `"新增第三方应用"` |
| **options** | Object | 弹窗配置 | `{ height: 500, width: 700 }` |
| **meta** | Object | 元数据 | `{ action: 'add', readonly: false }` |

#### 三种组件加载方式

```javascript
// 方式1：字符串路径（⚠️ 将在 2.x 废弃，不推荐）
this.$refs.TpDialog.show('/sys/third-party-app/add.vue', param, "新增");

// 方式2：import 函数（✅ 推荐，支持代码分割）
this.$refs.TpDialog.show(import('./add.vue'), param, "新增");

// 方式3：直接传入 Promise（高级用法）
const component = import('./add.vue');
this.$refs.TpDialog.show(component, param, "新增");
```

#### options 配置项

```javascript
let options = {
  height: 500,              // 弹窗高度（默认：响应式）
  width: 700,               // 弹窗宽度（默认：响应式）
  top: '10vh',              // 距离顶部距离（默认：15vh）
  overflow: 'hidden',       // 溢出处理（默认：hidden）
  canFullScreen: true,      // 是否可全屏（默认：true）
  callBack: (param) => {}   // 自定义关闭回调
};
```

### 2.3 响应式尺寸规则

⚠️ **重要**：如果不指定 `width` 和 `height`，弹窗会根据屏幕宽度自动调整：

| 屏幕宽度 | 弹窗宽度 | 弹窗高度 |
|---------|---------|---------|
| < 1440px | 800px | 556px |
| 1440px ~ 1599px | 864px | 588px |
| 1600px ~ 1919px | 960px | 656px |
| ≥ 1920px | 1152px | 796px |

```javascript
// ❌ 错误：硬编码小尺寸在大屏幕上显示很小
let options = { height: 400, width: 600 };

// ✅ 正确：不指定尺寸，让组件自动响应
let options = {};

// ✅ 正确：特殊场景才指定固定尺寸
let options = { height: 800, width: 1200 };  // 超大表单
```

### 2.4 子组件接收参数

```vue
<!-- add.vue -->
<template>
  <div>
    <fb-form>
      <fb-input v-model="formData.name"></fb-input>
    </fb-form>
    
    <fb-button @click="handleSave">保存</fb-button>
    <fb-button @click="handleCancel">取消</fb-button>
  </div>
</template>

<script>
export default {
  props: {
    param: Object,      // ✅ 接收父组件传入的 param
    parentPage: Object, // ✅ 接收父组件实例（tp-dialog 自动传入）
  },
  
  mounted() {
    // 获取父组件传入的参数
    if (this.param.id) {
      this.loadDetail(this.param.id);
    }
  },
  
  methods: {
    handleSave() {
      // 保存成功后关闭弹窗，并通知父组件刷新
      this.parentPage.closeTpDialog({ refresh: true });
    },
    
    handleCancel() {
      // 直接关闭弹窗
      this.parentPage.closeTpDialog();
    }
  }
}
</script>
```

### 2.5 完整示例

```vue
<!-- list.vue -->
<template>
  <div>
    <!-- 列表操作按钮 -->
    <fb-button @click="handleAdd">新增</fb-button>
    <fb-button @click="handleEdit(row)">修改</fb-button>
    <fb-button @click="handleView(row)">查看</fb-button>
    
    <!-- 弹窗组件 -->
    <tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
  </div>
</template>

<script>
export default {
  methods: {
    // 新增
    handleAdd() {
      let param = {};
      let options = { height: 500, width: 700 };
      this.$refs.TpDialog.show(
        import('./add.vue'), 
        param, 
        "新增第三方应用", 
        options,
        { action: 'add' }
      );
    },
    
    // 修改
    handleEdit(row) {
      let param = { id: row.id };
      let options = { height: 500, width: 700 };
      this.$refs.TpDialog.show(
        import('./add.vue'), 
        param, 
        "修改第三方应用", 
        options,
        { action: 'edit' }
      );
    },
    
    // 查看
    handleView(row) {
      let param = { id: row.id };
      let options = { height: 500, width: 700 };
      this.$refs.TpDialog.show(
        import('./view.vue'), 
        param, 
        "查看第三方应用", 
        options,
        { action: 'view' }
      );
    },
    
    // 关闭回调
    closeDialog(param) {
      if (param && param.refresh) {
        if (param.action === 'add') {
          this.doSearch();  // 新增后刷新列表
        } else if (param.action === 'edit') {
          this.doReload();  // 修改后重新加载当前页
        }
      }
    }
  }
}
</script>
```

---

## 3. tp-dialog-tab - 多标签页弹窗

### 3.1 基本使用

**适用场景**：复杂表单需要分Tab填写，Tab之间可以自由切换

```vue
<template>
  <div>
    <!-- 1. 声明组件 -->
    <tp-dialog-tab ref="TpDialogTab" @closeTpDialog="closeDialogTab"></tp-dialog-tab>

    <!-- 2. 触发按钮 -->
    <fb-button @click="handleAdd">新增</fb-button>
  </div>
</template>

<script>
export default {
  methods: {
    handleAdd() {
      // 3. 定义 Tab 数组
      let tabArry = [
        {
          url: import('./add-basicinfo.vue'),  // ✅ 推荐：使用 import
          label: '基本信息',
          icon: 'chart-line',
        },
        {
          url: import('./add-exinfo.vue'),
          label: '扩展信息',
          icon: 'progressbar',
        }
      ];
      
      let param = {};
      let options = { height: 600, width: 900 };
      
      // 4. 打开多Tab弹窗
      this.$refs.TpDialogTab.show(tabArry, param, '新增人员', options);
    },
    
    closeDialogTab(param) {
      if (param && param.refresh) {
        this.doSearch();
      }
    }
  }
}
</script>
```

### 3.2 Tab 配置

```javascript
let tabArry = [
  {
    url: import('./add-basicinfo.vue'),  // 组件路径
    label: '基本信息',                    // Tab 标签文字
    icon: 'chart-line',                  // Tab 图标（可选）
  },
  {
    url: import('./add-exinfo.vue'),
    label: '扩展信息',
    icon: 'progressbar',
  }
];
```

### 3.3 Tab 切换确认

⚠️ **重要**：默认开启 Tab 切换确认，如果用户修改了数据未保存，切换 Tab 时会提示

```javascript
let options = {
  tabChangeConfirm: true,  // 默认：true，开启切换确认
  // tabChangeConfirm: false,  // 关闭切换确认
};

this.$refs.TpDialogTab.show(tabArry, param, '新增', options);
```

**工作原理**：

组件通过监听子页面的 `updateCount` 数据来判断是否修改：

```javascript
// 子页面需要在 data 中声明
data() {
  return {
    updateCount: 0,  // ✅ 必须声明此变量
  }
},
watch: {
  formData: {
    handler() {
      this.updateCount++;  // 数据变化时递增
    },
    deep: true
  }
}
```

### 3.4 跨 Tab 数据传递

**场景**：第一个 Tab 填写的数据，需要在第二个 Tab 中使用

```vue
<!-- add-basicinfo.vue -->
<script>
export default {
  props: {
    parentPage: Object,  // tp-dialog-tab 实例
  },
  methods: {
    handleNext() {
      // 保存数据到父组件
      this.parentPage.setPageParam({
        personName: this.formData.personName,
        personId: this.formData.personId,
      });
      
      // 切换到下一个 Tab
      this.parentPage.tabIndex++;
    }
  }
}
</script>
```

```vue
<!-- add-exinfo.vue -->
<script>
export default {
  props: {
    parentPage: Object,
  },
  mounted() {
    // 获取上一个 Tab 传递的数据
    let prevData = this.parentPage.getPageParam();
    console.log(prevData.personName);  // 获取人员姓名
  }
}
</script>
```

### 3.5 完整示例

```vue
<!-- list.vue -->
<template>
  <div>
    <fb-button @click="handleAdd">新增人员</fb-button>
    <tp-dialog-tab ref="TpDialogTab" @closeTpDialog="closeDialogTab"></tp-dialog-tab>
  </div>
</template>

<script>
export default {
  methods: {
    handleAdd() {
      let tabArry = [
        {
          url: import('./add-basicinfo.vue'),
          label: '人员基本信息',
          icon: 'chart-line',
        },
        {
          url: import('./add-exinfo.vue'),
          label: '人员扩展信息',
          icon: 'progressbar',
        }
      ];
      
      let param = {};
      let options = {
        height: 600,
        width: 900,
        tabChangeConfirm: true,  // 开启 Tab 切换确认
      };
      
      this.$refs.TpDialogTab.show(tabArry, param, '新增人员', options);
    },
    
    closeDialogTab(param) {
      if (param && param.refresh) {
        this.doSearch();
      }
    }
  }
}
</script>
```

---

## 4. tp-dialog-flow-tab - 流程标签页弹窗

### 4.1 与 tp-dialog-tab 的区别

| 特性 | tp-dialog-tab | tp-dialog-flow-tab |
|------|---------------|-------------------|
| **Tab 切换方式** | 点击 Tab 切换 | 点击步骤条切换 |
| **适用场景** | 平级的多个表单 | 有先后顺序的流程 |
| **Tab 切换确认** | 支持 | 支持 |
| **UI 展示** | 普通 Tab 标签 | 步骤条（fb-steps） |

### 4.2 使用方式

```javascript
// 使用方式与 tp-dialog-tab 完全相同
let tabArry = [
  {
    url: import('./step1.vue'),
    label: '基本信息',
    icon: 'chart-line',
  },
  {
    url: import('./step2.vue'),
    label: '审核信息',
    icon: 'progressbar',
  }
];

this.$refs.TpDialogFlowTab.show(tabArry, param, '流程审批', options);
```

---

## 5. tp-dialog-step-tab - 步骤标签页弹窗

### 5.1 与 flow-tab 的区别

| 特性 | tp-dialog-flow-tab | tp-dialog-step-tab |
|------|-------------------|-------------------|
| **步骤条可点击** | ✅ 是 | ❌ 否 |
| **Tab 切换确认** | 支持 | 不支持（无效） |
| **切换方式** | 点击步骤条 | 只能通过 next()/prev() |

### 5.2 使用方式

```vue
<template>
  <tp-dialog-step-tab ref="TpDialogStepTab" @closeTpDialog="closeDialog"></tp-dialog-step-tab>
</template>

<script>
export default {
  methods: {
    handleAdd() {
      let tabArry = [
        {
          url: import('./step1.vue'),
          label: '第一步',
          icon: 'chart-line',
        },
        {
          url: import('./step2.vue'),
          label: '第二步',
          icon: 'progressbar',
        }
      ];
      
      this.$refs.TpDialogStepTab.show(tabArry, param, '分步操作', options);
    }
  }
}
</script>
```

**子组件中控制步骤切换**：

```vue
<script>
export default {
  props: {
    parentPage: Object,
  },
  methods: {
    handleNext() {
      // 下一步
      this.parentPage.next();
    },
    handlePrev() {
      // 上一步
      this.parentPage.prev();
    }
  }
}
</script>
```

---

## 6. tp-upload - 文件上传组件

### 6.1 三种视图模式

| 视图模式 | view 属性值 | 适用场景 | 截图 |
|---------|------------|---------|------|
| **列表模式** | `list` | 附件上传（支持多个） | 文件列表 |
| **头像模式** | `avatar` | 头像上传（单个） | 圆形头像 |
| **图片模式** | `image` | 图片上传（支持多个） | 图片预览 |

### 6.2 基本使用

```vue
<template>
  <div>
    <!-- 列表模式 -->
    <tp-upload 
      view="list"
      v-model="formData.fileList"
      :service="$svc.sys.file"
      :param="{ referType: 'SYS1014' }"
      :refer-id="formData.id"
      multiple
      :max-length="5"
    ></tp-upload>

    <!-- 头像模式 -->
    <tp-upload 
      view="avatar"
      v-model="formData.avatar"
      :service="$svc.sys.file"
      :param="{ referType: 'SYS1014' }"
      :refer-id="formData.id"
      :accept="'.png,.jpeg,.jpg'"
      :avatar-size="120"
      :avatar-circle="true"
    ></tp-upload>

    <!-- 图片模式 -->
    <tp-upload 
      view="image"
      v-model="formData.images"
      :service="$svc.sys.file"
      :param="{ referType: 'SYS1014' }"
      :refer-id="formData.id"
      multiple
      :max-length="10"
    ></tp-upload>
  </div>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        id: '',
        fileList: [],  // 附件列表
        avatar: [],    // 头像
        images: [],    // 图片列表
      }
    }
  }
}
</script>
```

### 6.3 属性详解

| 属性 | 类型 | 默认值 | 说明 |
|------|------|-------|------|
| **view** | String | `'list'` | 视图模式：`list`/`avatar`/`image` |
| **v-model** | Array | `[]` | 文件列表（双向绑定） |
| **service** | Object | - | 上传服务（必填，通常为 `$svc.sys.file`） |
| **param** | Object | `{}` | 附加参数（如 `referType`） |
| **referId** | String | `''` | 关联ID（用于附件回显） |
| **accept** | String | `''` | 文件类型限制（如 `.png,.jpg`） |
| **multiple** | Boolean | `false` | 是否允许多选 |
| **maxLength** | Number | `1` | 最大文件数 |
| **readonly** | Boolean | `false` | 是否只读 |
| **quality** | Number | `0.7` | 图片压缩质量（0-1） |
| **maxWidth** | Number | `2000` | 图片最大宽度 |
| **maxHeight** | Number | `2000` | 图片最大高度 |

### 6.4 referType 和 referId

⚠️ **重要**：附件上传需要提供 `referType` 和 `referId`

```javascript
// referType：附件类型（业务模块代码）
// referId：关联业务主键

// 新增时（referId 为空）
:param="{ referType: 'SYS1014' }"
:refer-id=""

// 修改时（referId 为业务主键）
:param="{ referType: 'SYS1014' }"
:refer-id="formData.id"
```

**常见 referType 值**：

| 业务模块 | referType 值 |
|---------|-------------|
| 第三方应用 | `SYS1014` |
| 人员信息 | `SYS1001` |
| 部门信息 | `SYS1002` |

### 6.5 附件回显

**场景**：修改时，需要显示已上传的附件

```vue
<template>
  <tp-upload 
    v-model="formData.fileList"
    :service="$svc.sys.file"
    :param="{ referType: 'SYS1014' }"
    :refer-id="formData.id"
  ></tp-upload>
</template>

<script>
export default {
  mounted() {
    if (this.param.id) {
      this.loadDetail();
    }
  },
  methods: {
    async loadDetail() {
      let res = await this.$svc.sys.thirdPartyApp.getDetail(this.param.id);
      
      this.formData.id = res.data.id;
      this.formData.name = res.data.name;
      
      // ✅ 关键：设置 referId 后，组件会自动查询附件
      // formData.fileList 会自动被赋值
    }
  }
}
</script>
```

### 6.6 事件监听

```vue
<tp-upload 
  v-model="formData.fileList"
  :service="$svc.sys.file"
  @on-start="handleStart"
  @on-progress="handleProgress"
  @on-success="handleSuccess"
  @on-error="handleError"
  @on-remove="handleRemove"
></tp-upload>

<script>
export default {
  methods: {
    handleStart(info) {
      console.log('开始上传', info);
    },
    handleProgress(info) {
      console.log('上传进度', info.percent);
    },
    handleSuccess(info) {
      console.log('上传成功', info);
    },
    handleError(info) {
      console.log('上传失败', info);
    },
    handleRemove(info) {
      console.log('移除文件', info);
    }
  }
}
</script>
```

---

## 7. tp-upload-path - 路径上传组件

### 7.1 与 tp-upload 的区别

| 特性 | tp-upload | tp-upload-path |
|------|-----------|----------------|
| **附件存储方式** | 数据库（referId） | 路径（relaPath） |
| **适用场景** | 普通业务附件 | 基于文件路径的场景 |

### 7.2 使用方式

```vue
<tp-upload-path 
  view="avatar"
  v-model="formData.fileList"
  :service="$svc.sys.file"
  :param="{ downFileName: 'demo.docx' }"
  rela-path="/upload/temp/"
  multiple
></tp-upload-path>
```

---

## 8. tp-datepicker - 日期选择器

### 8.1 基本使用

```vue
<template>
  <div>
    <!-- 单个日期 -->
    <tp-datepicker 
      v-model="formData.birthday"
      format="YYYY-MM-DD"
      value-format="YYYYMMDD"
    ></tp-datepicker>

    <!-- 日期范围 -->
    <tp-datepicker 
      v-model="formData.dateRange"
      mode="range"
      format="YYYY-MM-DD"
      value-format="YYYYMMDD"
      max-range="30D"
    ></tp-datepicker>

    <!-- 日期时间 -->
    <tp-datepicker 
      v-model="formData.expireTime"
      format="YYYY-MM-DD HH:mm:ss"
      value-format="YYYYMMDDHHmmss"
    ></tp-datepicker>
  </div>
</template>
```

### 8.2 属性详解

| 属性 | 类型 | 默认值 | 说明 |
|------|------|-------|------|
| **v-model** | String/Array | - | 绑定的日期值 |
| **format** | String | `'YYYY-MM-DD'` | 显示格式 |
| **value-format** | String | - | 提交格式（数据库格式） |
| **mode** | String | - | 模式：`range` 为日期范围 |
| **min-date** | Date/String | - | 最小日期 |
| **max-date** | Date/String | - | 最大日期 |
| **max-range** | String | - | 最大范围（如 `30D`、`1M`、`2Y`） |
| **readonly** | Boolean | `false` | 是否只读 |
| **disabled** | Boolean | `false` | 是否禁用 |
| **clearable** | Boolean | `true` | 是否可清空 |

### 8.3 格式说明

⚠️ **重要**：`format` 和 `value-format` 的区别

| 属性 | 作用 | 格式示例 | 值示例 |
|------|------|---------|-------|
| **format** | 显示格式（界面展示） | `YYYY-MM-DD` | `2024-12-01` |
| **value-format** | 提交格式（数据库存储） | `YYYYMMDD` | `20241201` |

```vue
<tp-datepicker 
  v-model="formData.birthday"
  format="YYYY-MM-DD"           <!-- 界面显示：2024-12-01 -->
  value-format="YYYYMMDD"       <!-- 数据库存储：20241201 -->
></tp-datepicker>
```

**支持的 value-format 格式**：

- `YYYYMMDD`：20241201
- `YYYYMMDDHHmmss`：20241201153045
- `YYYYMMDDHH`：⚠️ **不支持**（只能是完整的秒或只到日）

### 8.4 日期范围限制

```vue
<!-- 限制日期范围 -->
<tp-datepicker 
  v-model="formData.dateRange"
  mode="range"
  :min-date="new Date(2021, 03, 01)"
  :max-date="new Date(2021, 03, 15)"
  max-range="7D"
></tp-datepicker>
```

**max-range 格式**：

- `7D`：7天
- `1M`：1个月
- `2Y`：2年

### 8.5 完整示例（表单中使用）

```vue
<template>
  <fb-form>
    <fb-row>
      <fb-col span="12">
        <fb-form-item label="生日">
          <tp-datepicker 
            v-model="formData.birthday"
            format="YYYY-MM-DD"
            value-format="YYYYMMDD"
          ></tp-datepicker>
        </fb-form-item>
      </fb-col>
      
      <fb-col span="12">
        <fb-form-item label="有效期">
          <tp-datepicker 
            v-model="formData.expireTime"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYYMMDDHHmmss"
          ></tp-datepicker>
        </fb-form-item>
      </fb-col>
    </fb-row>
  </fb-form>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        birthday: '',      // 存储格式：20241201
        expireTime: '',    // 存储格式：20241201153045
      }
    }
  },
  
  methods: {
    async handleSubmit() {
      let submitData = {
        birthday: this.formData.birthday,        // 20241201
        expireTime: this.formData.expireTime,    // 20241201153045
      };
      
      // ✅ 已经是数据库格式，直接提交
      await this.$svc.sys.person.add(submitData);
    }
  }
}
</script>
```

---

## 9. 常见错误和陷阱

### 9.1 tp-dialog 相关

#### ❌ 错误1：使用字符串路径

```javascript
// ❌ 错误：字符串路径将在 2.x 废弃
this.$refs.TpDialog.show('/sys/third-party-app/add.vue', param, "新增");

// ✅ 正确：使用 import
this.$refs.TpDialog.show(import('./add.vue'), param, "新增");
```

#### ❌ 错误2：子组件未接收 parentPage

```vue
<!-- ❌ 错误：未声明 props -->
<script>
export default {
  methods: {
    handleSave() {
      // 报错：this.parentPage 未定义
      this.parentPage.closeTpDialog({ refresh: true });
    }
  }
}
</script>

<!-- ✅ 正确：声明 props -->
<script>
export default {
  props: {
    param: Object,
    parentPage: Object,  // ✅ 必须声明
  },
  methods: {
    handleSave() {
      this.parentPage.closeTpDialog({ refresh: true });
    }
  }
}
</script>
```

#### ❌ 错误3：硬编码小尺寸

```javascript
// ❌ 错误：在 1920px 屏幕上显示 600x400 很小
let options = { height: 400, width: 600 };

// ✅ 正确：不指定尺寸，自动响应
let options = {};
```

### 9.2 tp-dialog-tab 相关

#### ❌ 错误4：未声明 updateCount

```javascript
// ❌ 错误：Tab 切换确认不生效
data() {
  return {
    formData: {}
    // 缺少 updateCount
  }
}

// ✅ 正确：必须声明 updateCount
data() {
  return {
    formData: {},
    updateCount: 0,  // ✅ 必须
  }
},
watch: {
  formData: {
    handler() {
      this.updateCount++;
    },
    deep: true
  }
}
```

#### ❌ 错误5：跨 Tab 传递数据错误

```javascript
// ❌ 错误：直接修改 param（param 是只读的）
this.param.personName = 'xxx';

// ✅ 正确：使用 setPageParam/getPageParam
this.parentPage.setPageParam({ personName: 'xxx' });
let data = this.parentPage.getPageParam();
```

### 9.3 tp-upload 相关

#### ❌ 错误6：未提供 service

```vue
<!-- ❌ 错误：缺少 service -->
<tp-upload v-model="formData.fileList"></tp-upload>

<!-- ✅ 正确：必须提供 service -->
<tp-upload 
  v-model="formData.fileList"
  :service="$svc.sys.file"
></tp-upload>
```

#### ❌ 错误7：未提供 referType

```vue
<!-- ❌ 错误：缺少 referType -->
<tp-upload 
  v-model="formData.fileList"
  :service="$svc.sys.file"
  :refer-id="formData.id"
></tp-upload>

<!-- ✅ 正确：必须提供 referType -->
<tp-upload 
  v-model="formData.fileList"
  :service="$svc.sys.file"
  :param="{ referType: 'SYS1014' }"
  :refer-id="formData.id"
></tp-upload>
```

#### ❌ 错误8：头像模式使用多选

```vue
<!-- ❌ 错误：头像模式不应该多选 -->
<tp-upload 
  view="avatar"
  v-model="formData.avatar"
  :service="$svc.sys.file"
  multiple
  :max-length="5"
></tp-upload>

<!-- ✅ 正确：头像模式单选 -->
<tp-upload 
  view="avatar"
  v-model="formData.avatar"
  :service="$svc.sys.file"
  :max-length="1"
></tp-upload>
```

### 9.4 tp-datepicker 相关

#### ❌ 错误9：value-format 格式错误

```vue
<!-- ❌ 错误：不支持 YYYYMMDDHH -->
<tp-datepicker 
  v-model="formData.expireTime"
  value-format="YYYYMMDDHH"
></tp-datepicker>

<!-- ✅ 正确：只支持完整格式 -->
<tp-datepicker 
  v-model="formData.expireTime"
  value-format="YYYYMMDDHHmmss"
></tp-datepicker>
```

#### ❌ 错误10：日期范围未使用 mode="range"

```vue
<!-- ❌ 错误：缺少 mode="range" -->
<tp-datepicker 
  v-model="formData.dateRange"
  format="YYYY-MM-DD"
></tp-datepicker>

<!-- ✅ 正确：日期范围必须指定 mode -->
<tp-datepicker 
  v-model="formData.dateRange"
  mode="range"
  format="YYYY-MM-DD"
  value-format="YYYYMMDD"
></tp-datepicker>
```

---

## 📚 相关文档

- [fb-ui 组件库规范](./fb-ui-components.md)
- [前端编码规范总览](./overview.md)
- [日期时间格式标准](../data-standards/date-time-format.md)
- [API 请求响应规范](../api-contracts/request-response.md)

---

**最后更新**：2024-12-01  
**维护者**：前端开发团队
