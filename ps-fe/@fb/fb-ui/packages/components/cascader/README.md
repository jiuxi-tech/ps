[comment]: <> (fb-docs: docsify/fb-ui/03/cascader/README.md)

# 级联选择器-Cascader
 
级联选择器

## 基础用法

```html run {title:'示例演示'}
<template>
<div style="height: 260px"> 
{{value1}}
<div>
    <fb-cascader style="width: 200px" clearable v-model="value1"
                 :data="options" @on-change="changeNode"></fb-cascader>
</div>
</div>
</template>
<script>
  export default {
    data () {
      return {
        value1: [],
        options: [{
            value: 'zhinan',
            label: '指南',
            disabled: true,
            children: [{
                value: 'shejiyuanze',
                label: '设计原则',
                children: [{
                    value: 'yizhi',
                    label: '一致'
                }, {
                    value: 'fankui',
                    label: '反馈'
                }, {
                    value: 'xiaolv',
                    label: '效率'
                }, {
                    value: 'kekong',
                    label: '可控'
                }]
            }, {
                value: 'daohang',
                label: '导航',
                children: [{
                    value: 'cexiangdaohang',
                    label: '侧向导航'
                }, {
                    value: 'dingbudaohang',
                    label: '顶部导航'
                }]
            }]
        }, {
            value: 'zujian',
            label: '组件',
            children: [{
                value: 'basic',
                label: 'Basic',
                children: [{
                    value: 'layout',
                    label: 'Layout 布局'
                }, {
                    value: 'color',
                    label: 'Color 色彩'
                }, {
                    value: 'typography',
                    label: 'Typography 字体'
                }, {
                    value: 'icon',
                    label: 'Icon 图标'
                }, {
                    value: 'button',
                    label: 'Button 按钮'
                }]
            }, {
                value: 'form',
                label: 'Form',
                children: [{
                    value: 'radio',
                    label: 'Radio 单选框'
                }, {
                    value: 'checkbox',
                    label: 'Checkbox 多选框'
                }, {
                    value: 'input',
                    label: 'Input 输入框'
                }, {
                    value: 'input-number',
                    label: 'InputNumber 计数器'
                }, {
                    value: 'select',
                    label: 'Select 选择器'
                }, {
                    value: 'cascader',
                    label: 'Cascader 级联选择器'
                }, {
                    value: 'switch',
                    label: 'Switch 开关'
                }, {
                    value: 'slider',
                    label: 'Slider 滑块'
                }, {
                    value: 'time-picker',
                    label: 'TimePicker 时间选择器'
                }, {
                    value: 'date-picker',
                    label: 'DatePicker 日期选择器'
                }, {
                    value: 'datetime-picker',
                    label: 'DateTimePicker 日期时间选择器'
                }, {
                    value: 'upload',
                    label: 'Upload 上传'
                }, {
                    value: 'rate',
                    label: 'Rate 评分'
                }, {
                    value: 'form',
                    label: 'Form 表单'
                }]
            }, {
                value: 'data',
                label: 'Data',
                children: [{
                    value: 'table',
                    label: 'Table 表格'
                }, {
                    value: 'tag',
                    label: 'Tag 标签'
                }, {
                    value: 'progress',
                    label: 'Progress 进度条'
                }, {
                    value: 'tree',
                    label: 'Tree 树形控件'
                }, {
                    value: 'pagination',
                    label: 'Pagination 分页'
                }, {
                    value: 'badge',
                    label: 'Badge 标记'
                }]
            }, {
                value: 'notice',
                label: 'Notice',
                children: [{
                    value: 'alert',
                    label: 'Alert 警告'
                }, {
                    value: 'loading',
                    label: 'Loading 加载'
                }, {
                    value: 'message',
                    label: 'Message 消息提示'
                }, {
                    value: 'message-box',
                    label: 'MessageBox 弹框'
                }, {
                    value: 'notification',
                    label: 'Notification 通知'
                }]
            }, {
                value: 'navigation',
                label: 'Navigation',
                children: [{
                    value: 'menu',
                    label: 'NavMenu 导航菜单'
                }, {
                    value: 'tabs',
                    label: 'Tabs 标签页'
                }, {
                    value: 'breadcrumb',
                    label: 'Breadcrumb 面包屑'
                }, {
                    value: 'dropdown',
                    label: 'Dropdown 下拉菜单'
                }, {
                    value: 'steps',
                    label: 'Steps 步骤条'
                }]
            }, {
                value: 'others',
                label: 'Others',
                children: [{
                    value: 'dialog',
                    label: 'Dialog 对话框'
                }, {
                    value: 'tooltip',
                    label: 'Tooltip 文字提示'
                }, {
                    value: 'popover',
                    label: 'Popover 弹出框'
                }, {
                    value: 'card',
                    label: 'Card 卡片'
                }, {
                    value: 'carousel',
                    label: 'Carousel 走马灯'
                }, {
                    value: 'collapse',
                    label: 'Collapse 折叠面板'
                }]
            }]
        }, {
            value: 'ziyuan',
            label: '资源',
            children: [{
                value: 'axure',
                label: 'Axure Components'
            }, {
                value: 'sketch',
                label: 'Sketch Templates'
            }, {
                value: 'jiaohu',
                label: '组件交互文档'
            }]
        }],
      }
    },
    methods: {
        changeNode(val) {
           console.log(val, 'changeNode')
        }
    },
  }
</script>
<style>
</style>
```

## 允许选择非叶子节点

```html run {title:'示例演示'}
<template>
<div style="height: 260px"> 
{{value1}}
<div>
    <fb-cascader style="width: 600px" clearable v-model="value1"
                :props="{ changeOnSelect: true, expandTrigger: 'click', }"
                 :data="options" @on-change="changeNode"></fb-cascader>
</div>
</div>
</template>
<script>
  export default {
    data () {
      return {
        value1: [],
        options: [{
            value: 'zhinan',
            label: '指南',
            disabled: true,
            children: [{
                value: 'shejiyuanze',
                label: '设计原则',
                children: [{
                    value: 'yizhi',
                    label: '一致'
                }, {
                    value: 'fankui',
                    label: '反馈'
                }, {
                    value: 'xiaolv',
                    label: '效率'
                }, {
                    value: 'kekong',
                    label: '可控'
                }]
            }, {
                value: 'daohang',
                label: '导航',
                children: [{
                    value: 'cexiangdaohang',
                    label: '侧向导航'
                }, {
                    value: 'dingbudaohang',
                    label: '顶部导航'
                }]
            }]
        }, {
            value: 'zujian',
            label: '组件',
            children: [{
                value: 'basic',
                label: 'Basic',
                children: [{
                    value: 'layout',
                    label: 'Layout 布局'
                }, {
                    value: 'color',
                    label: 'Color 色彩'
                }, {
                    value: 'typography',
                    label: 'Typography 字体'
                }, {
                    value: 'icon',
                    label: 'Icon 图标'
                }, {
                    value: 'button',
                    label: 'Button 按钮'
                }]
            }, {
                value: 'form',
                label: 'Form',
                children: [{
                    value: 'radio',
                    label: 'Radio 单选框'
                }, {
                    value: 'checkbox',
                    label: 'Checkbox 多选框'
                }, {
                    value: 'input',
                    label: 'Input 输入框'
                }, {
                    value: 'input-number',
                    label: 'InputNumber 计数器'
                }, {
                    value: 'select',
                    label: 'Select 选择器'
                }, {
                    value: 'cascader',
                    label: 'Cascader 级联选择器'
                }, {
                    value: 'switch',
                    label: 'Switch 开关'
                }, {
                    value: 'slider',
                    label: 'Slider 滑块'
                }, {
                    value: 'time-picker',
                    label: 'TimePicker 时间选择器'
                }, {
                    value: 'date-picker',
                    label: 'DatePicker 日期选择器'
                }, {
                    value: 'datetime-picker',
                    label: 'DateTimePicker 日期时间选择器'
                }, {
                    value: 'upload',
                    label: 'Upload 上传'
                }, {
                    value: 'rate',
                    label: 'Rate 评分'
                }, {
                    value: 'form',
                    label: 'Form 表单'
                }]
            }, {
                value: 'data',
                label: 'Data',
                children: [{
                    value: 'table',
                    label: 'Table 表格'
                }, {
                    value: 'tag',
                    label: 'Tag 标签'
                }, {
                    value: 'progress',
                    label: 'Progress 进度条'
                }, {
                    value: 'tree',
                    label: 'Tree 树形控件'
                }, {
                    value: 'pagination',
                    label: 'Pagination 分页'
                }, {
                    value: 'badge',
                    label: 'Badge 标记'
                }]
            }, {
                value: 'notice',
                label: 'Notice',
                children: [{
                    value: 'alert',
                    label: 'Alert 警告'
                }, {
                    value: 'loading',
                    label: 'Loading 加载'
                }, {
                    value: 'message',
                    label: 'Message 消息提示'
                }, {
                    value: 'message-box',
                    label: 'MessageBox 弹框'
                }, {
                    value: 'notification',
                    label: 'Notification 通知'
                }]
            }, {
                value: 'navigation',
                label: 'Navigation',
                children: [{
                    value: 'menu',
                    label: 'NavMenu 导航菜单'
                }, {
                    value: 'tabs',
                    label: 'Tabs 标签页'
                }, {
                    value: 'breadcrumb',
                    label: 'Breadcrumb 面包屑'
                }, {
                    value: 'dropdown',
                    label: 'Dropdown 下拉菜单'
                }, {
                    value: 'steps',
                    label: 'Steps 步骤条'
                }]
            }, {
                value: 'others',
                label: 'Others',
                children: [{
                    value: 'dialog',
                    label: 'Dialog 对话框'
                }, {
                    value: 'tooltip',
                    label: 'Tooltip 文字提示'
                }, {
                    value: 'popover',
                    label: 'Popover 弹出框'
                }, {
                    value: 'card',
                    label: 'Card 卡片'
                }, {
                    value: 'carousel',
                    label: 'Carousel 走马灯'
                }, {
                    value: 'collapse',
                    label: 'Collapse 折叠面板'
                }]
            }]
        }, {
            value: 'ziyuan',
            label: '资源',
            children: [{
                value: 'axure',
                label: 'Axure Components'
            }, {
                value: 'sketch',
                label: 'Sketch Templates'
            }, {
                value: 'jiaohu',
                label: '组件交互文档'
            }]
        }],
      }
    },
    methods: {
        changeNode(val) {
           console.log(val, 'changeNode')
        }
    },
  }
</script>
<style>
</style>
```

## 搜索节点-单选搜索-不能异步使用

```html run {title:'示例演示'}
<template>
<div style="height: 260px"> 
{{value1}}
<div>
    <fb-cascader style="width: 600px" clearable v-model="value1"
                :props="{ multiple: false }" filterable
                 :data="options" @on-change="changeNode"></fb-cascader>
</div>
</div>
</template>
<script>
  export default {
    data () {
      return {
        value1: [],
        options: [{
            value: 'zhinan',
            label: '指南',
            disabled: true,
            children: [{
                value: 'shejiyuanze',
                label: '设计原则',
                children: [{
                    value: 'yizhi',
                    label: '一致'
                }, {
                    value: 'fankui',
                    label: '反馈'
                }, {
                    value: 'xiaolv',
                    label: '效率'
                }, {
                    value: 'kekong',
                    label: '可控'
                }]
            }, {
                value: 'daohang',
                label: '导航',
                children: [{
                    value: 'cexiangdaohang',
                    label: '侧向导航'
                }, {
                    value: 'dingbudaohang',
                    label: '顶部导航'
                }]
            }]
        }, {
            value: 'zujian',
            label: '组件',
            children: [{
                value: 'basic',
                label: 'Basic',
                children: [{
                    value: 'layout',
                    label: 'Layout 布局'
                }, {
                    value: 'color',
                    label: 'Color 色彩'
                }, {
                    value: 'typography',
                    label: 'Typography 字体'
                }, {
                    value: 'icon',
                    label: 'Icon 图标'
                }, {
                    value: 'button',
                    label: 'Button 按钮'
                }]
            }, {
                value: 'form',
                label: 'Form',
                children: [{
                    value: 'radio',
                    label: 'Radio 单选框'
                }, {
                    value: 'checkbox',
                    label: 'Checkbox 多选框'
                }, {
                    value: 'input',
                    label: 'Input 输入框'
                }, {
                    value: 'input-number',
                    label: 'InputNumber 计数器'
                }, {
                    value: 'select',
                    label: 'Select 选择器'
                }, {
                    value: 'cascader',
                    label: 'Cascader 级联选择器'
                }, {
                    value: 'switch',
                    label: 'Switch 开关'
                }, {
                    value: 'slider',
                    label: 'Slider 滑块'
                }, {
                    value: 'time-picker',
                    label: 'TimePicker 时间选择器'
                }, {
                    value: 'date-picker',
                    label: 'DatePicker 日期选择器'
                }, {
                    value: 'datetime-picker',
                    label: 'DateTimePicker 日期时间选择器'
                }, {
                    value: 'upload',
                    label: 'Upload 上传'
                }, {
                    value: 'rate',
                    label: 'Rate 评分'
                }, {
                    value: 'form',
                    label: 'Form 表单'
                }]
            }, {
                value: 'data',
                label: 'Data',
                children: [{
                    value: 'table',
                    label: 'Table 表格'
                }, {
                    value: 'tag',
                    label: 'Tag 标签'
                }, {
                    value: 'progress',
                    label: 'Progress 进度条'
                }, {
                    value: 'tree',
                    label: 'Tree 树形控件'
                }, {
                    value: 'pagination',
                    label: 'Pagination 分页'
                }, {
                    value: 'badge',
                    label: 'Badge 标记'
                }]
            }, {
                value: 'notice',
                label: 'Notice',
                children: [{
                    value: 'alert',
                    label: 'Alert 警告'
                }, {
                    value: 'loading',
                    label: 'Loading 加载'
                }, {
                    value: 'message',
                    label: 'Message 消息提示'
                }, {
                    value: 'message-box',
                    label: 'MessageBox 弹框'
                }, {
                    value: 'notification',
                    label: 'Notification 通知'
                }]
            }, {
                value: 'navigation',
                label: 'Navigation',
                children: [{
                    value: 'menu',
                    label: 'NavMenu 导航菜单'
                }, {
                    value: 'tabs',
                    label: 'Tabs 标签页'
                }, {
                    value: 'breadcrumb',
                    label: 'Breadcrumb 面包屑'
                }, {
                    value: 'dropdown',
                    label: 'Dropdown 下拉菜单'
                }, {
                    value: 'steps',
                    label: 'Steps 步骤条'
                }]
            }, {
                value: 'others',
                label: 'Others',
                children: [{
                    value: 'dialog',
                    label: 'Dialog 对话框'
                }, {
                    value: 'tooltip',
                    label: 'Tooltip 文字提示'
                }, {
                    value: 'popover',
                    label: 'Popover 弹出框'
                }, {
                    value: 'card',
                    label: 'Card 卡片'
                }, {
                    value: 'carousel',
                    label: 'Carousel 走马灯'
                }, {
                    value: 'collapse',
                    label: 'Collapse 折叠面板'
                }]
            }]
        }, {
            value: 'ziyuan',
            label: '资源',
            children: [{
                value: 'axure',
                label: 'Axure Components'
            }, {
                value: 'sketch',
                label: 'Sketch Templates'
            }, {
                value: 'jiaohu',
                label: '组件交互文档'
            }]
        }],
      }
    },
    methods: {
        changeNode(val) {
           console.log(val, 'changeNode')
        }
    },
  }
</script>
<style>
</style>
```

## 搜索节点-多选搜索-不能异步使用

```html run {title:'示例演示'}
<template>
<div style="height: 460px">  
{{value1}}
<fb-card header="多选搜索 + 不拼接路径值 + collapse-tags">
    {{value1}}
    <fb-cascader style="width: 600px" clearable filterable collapse-tags
                 v-model="value1" :props="{ multiple: true, emitPath: false }"
                 :data="options"></fb-cascader>
</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
        value1: [],
        options: [{
            value: 'zhinan',
            label: '指南',
            disabled: true,
            children: [{
                value: 'shejiyuanze',
                label: '设计原则',
                children: [{
                    value: 'yizhi',
                    label: '一致'
                }, {
                    value: 'fankui',
                    label: '反馈'
                }, {
                    value: 'xiaolv',
                    label: '效率'
                }, {
                    value: 'kekong',
                    label: '可控'
                }]
            }, {
                value: 'daohang',
                label: '导航',
                children: [{
                    value: 'cexiangdaohang',
                    label: '侧向导航'
                }, {
                    value: 'dingbudaohang',
                    label: '顶部导航'
                }]
            }]
        }, {
            value: 'zujian',
            label: '组件',
            children: [{
                value: 'basic',
                label: 'Basic',
                children: [{
                    value: 'layout',
                    label: 'Layout 布局'
                }, {
                    value: 'color',
                    label: 'Color 色彩'
                }, {
                    value: 'typography',
                    label: 'Typography 字体'
                }, {
                    value: 'icon',
                    label: 'Icon 图标'
                }, {
                    value: 'button',
                    label: 'Button 按钮'
                }]
            }, {
                value: 'form',
                label: 'Form',
                children: [{
                    value: 'radio',
                    label: 'Radio 单选框'
                }, {
                    value: 'checkbox',
                    label: 'Checkbox 多选框'
                }, {
                    value: 'input',
                    label: 'Input 输入框'
                }, {
                    value: 'input-number',
                    label: 'InputNumber 计数器'
                }, {
                    value: 'select',
                    label: 'Select 选择器'
                }, {
                    value: 'cascader',
                    label: 'Cascader 级联选择器'
                }, {
                    value: 'switch',
                    label: 'Switch 开关'
                }, {
                    value: 'slider',
                    label: 'Slider 滑块'
                }, {
                    value: 'time-picker',
                    label: 'TimePicker 时间选择器'
                }, {
                    value: 'date-picker',
                    label: 'DatePicker 日期选择器'
                }, {
                    value: 'datetime-picker',
                    label: 'DateTimePicker 日期时间选择器'
                }, {
                    value: 'upload',
                    label: 'Upload 上传'
                }, {
                    value: 'rate',
                    label: 'Rate 评分'
                }, {
                    value: 'form',
                    label: 'Form 表单'
                }]
            }, {
                value: 'data',
                label: 'Data',
                children: [{
                    value: 'table',
                    label: 'Table 表格'
                }, {
                    value: 'tag',
                    label: 'Tag 标签'
                }, {
                    value: 'progress',
                    label: 'Progress 进度条'
                }, {
                    value: 'tree',
                    label: 'Tree 树形控件'
                }, {
                    value: 'pagination',
                    label: 'Pagination 分页'
                }, {
                    value: 'badge',
                    label: 'Badge 标记'
                }]
            }, {
                value: 'notice',
                label: 'Notice',
                children: [{
                    value: 'alert',
                    label: 'Alert 警告'
                }, {
                    value: 'loading',
                    label: 'Loading 加载'
                }, {
                    value: 'message',
                    label: 'Message 消息提示'
                }, {
                    value: 'message-box',
                    label: 'MessageBox 弹框'
                }, {
                    value: 'notification',
                    label: 'Notification 通知'
                }]
            }, {
                value: 'navigation',
                label: 'Navigation',
                children: [{
                    value: 'menu',
                    label: 'NavMenu 导航菜单'
                }, {
                    value: 'tabs',
                    label: 'Tabs 标签页'
                }, {
                    value: 'breadcrumb',
                    label: 'Breadcrumb 面包屑'
                }, {
                    value: 'dropdown',
                    label: 'Dropdown 下拉菜单'
                }, {
                    value: 'steps',
                    label: 'Steps 步骤条'
                }]
            }, {
                value: 'others',
                label: 'Others',
                children: [{
                    value: 'dialog',
                    label: 'Dialog 对话框'
                }, {
                    value: 'tooltip',
                    label: 'Tooltip 文字提示'
                }, {
                    value: 'popover',
                    label: 'Popover 弹出框'
                }, {
                    value: 'card',
                    label: 'Card 卡片'
                }, {
                    value: 'carousel',
                    label: 'Carousel 走马灯'
                }, {
                    value: 'collapse',
                    label: 'Collapse 折叠面板'
                }]
            }]
        }, {
            value: 'ziyuan',
            label: '资源',
            children: [{
                value: 'axure',
                label: 'Axure Components'
            }, {
                value: 'sketch',
                label: 'Sketch Templates'
            }, {
                value: 'jiaohu',
                label: '组件交互文档'
            }]
        }],
      }
    },
    methods: {
        changeNode(val) {
           console.log(val, 'changeNode')
        }
    },
  }
</script>
<style>
</style>
```

## 搜索节点 不拼接路径value

```html run {title:'示例演示'}
<template>
<div style="height: 260px">  
{{value1}}
<fb-cascader style="width: 600px" clearable v-model="value1" 
            :props="{ multiple: false, expandTrigger: 'click', emitPath: false }"
            :data="options"></fb-cascader>
</div>
</template>
<script>
  export default {
    data () {
      return {
        value1: [],
        options: [{
            value: 'zhinan',
            label: '指南',
            disabled: true,
            children: [{
                value: 'shejiyuanze',
                label: '设计原则',
                children: [{
                    value: 'yizhi',
                    label: '一致'
                }, {
                    value: 'fankui',
                    label: '反馈'
                }, {
                    value: 'xiaolv',
                    label: '效率'
                }, {
                    value: 'kekong',
                    label: '可控'
                }]
            }, {
                value: 'daohang',
                label: '导航',
                children: [{
                    value: 'cexiangdaohang',
                    label: '侧向导航'
                }, {
                    value: 'dingbudaohang',
                    label: '顶部导航'
                }]
            }]
        }, {
            value: 'zujian',
            label: '组件',
            children: [{
                value: 'basic',
                label: 'Basic',
                children: [{
                    value: 'layout',
                    label: 'Layout 布局'
                }, {
                    value: 'color',
                    label: 'Color 色彩'
                }, {
                    value: 'typography',
                    label: 'Typography 字体'
                }, {
                    value: 'icon',
                    label: 'Icon 图标'
                }, {
                    value: 'button',
                    label: 'Button 按钮'
                }]
            }, {
                value: 'form',
                label: 'Form',
                children: [{
                    value: 'radio',
                    label: 'Radio 单选框'
                }, {
                    value: 'checkbox',
                    label: 'Checkbox 多选框'
                }, {
                    value: 'input',
                    label: 'Input 输入框'
                }, {
                    value: 'input-number',
                    label: 'InputNumber 计数器'
                }, {
                    value: 'select',
                    label: 'Select 选择器'
                }, {
                    value: 'cascader',
                    label: 'Cascader 级联选择器'
                }, {
                    value: 'switch',
                    label: 'Switch 开关'
                }, {
                    value: 'slider',
                    label: 'Slider 滑块'
                }, {
                    value: 'time-picker',
                    label: 'TimePicker 时间选择器'
                }, {
                    value: 'date-picker',
                    label: 'DatePicker 日期选择器'
                }, {
                    value: 'datetime-picker',
                    label: 'DateTimePicker 日期时间选择器'
                }, {
                    value: 'upload',
                    label: 'Upload 上传'
                }, {
                    value: 'rate',
                    label: 'Rate 评分'
                }, {
                    value: 'form',
                    label: 'Form 表单'
                }]
            }, {
                value: 'data',
                label: 'Data',
                children: [{
                    value: 'table',
                    label: 'Table 表格'
                }, {
                    value: 'tag',
                    label: 'Tag 标签'
                }, {
                    value: 'progress',
                    label: 'Progress 进度条'
                }, {
                    value: 'tree',
                    label: 'Tree 树形控件'
                }, {
                    value: 'pagination',
                    label: 'Pagination 分页'
                }, {
                    value: 'badge',
                    label: 'Badge 标记'
                }]
            }, {
                value: 'notice',
                label: 'Notice',
                children: [{
                    value: 'alert',
                    label: 'Alert 警告'
                }, {
                    value: 'loading',
                    label: 'Loading 加载'
                }, {
                    value: 'message',
                    label: 'Message 消息提示'
                }, {
                    value: 'message-box',
                    label: 'MessageBox 弹框'
                }, {
                    value: 'notification',
                    label: 'Notification 通知'
                }]
            }, {
                value: 'navigation',
                label: 'Navigation',
                children: [{
                    value: 'menu',
                    label: 'NavMenu 导航菜单'
                }, {
                    value: 'tabs',
                    label: 'Tabs 标签页'
                }, {
                    value: 'breadcrumb',
                    label: 'Breadcrumb 面包屑'
                }, {
                    value: 'dropdown',
                    label: 'Dropdown 下拉菜单'
                }, {
                    value: 'steps',
                    label: 'Steps 步骤条'
                }]
            }, {
                value: 'others',
                label: 'Others',
                children: [{
                    value: 'dialog',
                    label: 'Dialog 对话框'
                }, {
                    value: 'tooltip',
                    label: 'Tooltip 文字提示'
                }, {
                    value: 'popover',
                    label: 'Popover 弹出框'
                }, {
                    value: 'card',
                    label: 'Card 卡片'
                }, {
                    value: 'carousel',
                    label: 'Carousel 走马灯'
                }, {
                    value: 'collapse',
                    label: 'Collapse 折叠面板'
                }]
            }]
        }, {
            value: 'ziyuan',
            label: '资源',
            children: [{
                value: 'axure',
                label: 'Axure Components'
            }, {
                value: 'sketch',
                label: 'Sketch Templates'
            }, {
                value: 'jiaohu',
                label: '组件交互文档'
            }]
        }],
      }
    },
    methods: {
        changeNode(val) {
           console.log(val, 'changeNode')
        }
    },
  }
</script>
<style>
</style>
```

## 懒加载 lazy lazyLoad
<div style="padding: 20px; box-shadow: 0px 0px 6px #cccccc">
    通过lazy开启动态加载，并通过lazyload来设置加载数据源的方法。lazyload方法有两个参数，第一个参数node为当前点击的节点，第二个resolve为数据加载完成的回调(必须调用)。为了更准确的显示节点的状态，还可以对节点数据添加是否为叶子节点的标志位 (默认字段为leaf，可通过props.leaf修改)，否则会简单的以有无子节点来判断是否为叶子节点。
</div>

```html run {title:'示例演示'}
<template>
<div style="height: 260px">  
{{value6}}
<fb-cascader style="width: 600px" clearable @on-change="changeNode"
			v-model="value6" :props="props2" ></fb-cascader>
</div>
</template>
<script>
  export default {
    data () {
        let id = 0;
      return {
        value6: [],
        props2: {
            lazy: true,
            lazyLoad (node, resolve) {
                const { level } = node;
                console.log(level, node)
                setTimeout(() => {
                    const nodes = Array.from({ length: level + 1 })
                        .map(item => ({
                            value: ++id,
                            label: `选项${id}`,
                            leaf: level >= 2 // 是否为叶子节点
                        }));
                    // 通过调用resolve将子节点数据返回，通知组件数据加载完成
                    resolve(nodes);
                }, 1000);
            }
        },
      }
    },
    methods: {
        changeNode(val) {
           console.log(val, 'changeNode')
        }
    },
  }
</script>
<style>
</style>
```




## FbCascader属性列表

| 属性 | 说明 | 类型 | 可选值 | 默认值 |
|:-----|:----|:-----|:-------|:-------|
| value / v-model | 选中项绑定值 | - | - | - |
| data | 可选项数据源，键名可通过 Props 属性配置 | array | - | - |
| props | 配置选项，具体见下表 | object | - | - |
| size | 尺寸 | string | s/m/l | - |
| placeholder | 输入框占位文本 | object | - | 请选择 |
| disabled | 是否禁用 | boolean | - | false |
| clearable |是否支持清空选项 | boolean | - | false |
| show-all-levels |输入框中是否显示选中值的完整路径 | boolean | - | true |
| collapse-tags |多选模式下是否折叠Tag | boolean | - | false |
| separator |选项分隔符 | string | - | 斜杠' / ' |
| filterable |是否可搜索选项 | boolean | - | false |
| debounce | 搜索关键词输入的去抖延迟，毫秒 | number | - | 300 |
| before-filter | 是否支持清空选项 | function(value) | - | - |
| popper-class | 自定义浮层类名 | string | - | - |

## FbCascader事件列表

| 事件名 | 说明 | 返回值 |  |
| :------- | :------- | :----- | :--- |
| on-change | 当选中节点变化时触发 | 选中节点的值 |  |
| on-expand-change | 当展开节点发生变化时触发 | 各父级选项值组成的数组 |  |
| on-blur | 当失去焦点时触发 | (event: Event) |  |
| on-focus | 当获得焦点时触发 | (event: Event) |  |
| on-visible-change | 下拉框出现/隐藏时触发 | 出现则为 true，隐藏则为 false |  |
| on-remove-tag | 在多选模式下，移除Tag时触发 | 移除的Tag对应的节点的值 |  |

## FbCascader Slots

| 属性 | 说明 |
|:-----|:----|
| default | 自定义备选项的节点内容，参数为 { node, data }，分别为当前节点的 Node 对象和数据 |
| empty | 无匹配选项时的内容 |


## FbCascaderPanel属性列表

| 属性 | 说明 | 类型 | 可选值 | 默认值 |
|:-----|:----|:-----|:-------|:-------|
| value / v-model | 选中项绑定值 | - | - | - |
| data | 可选项数据源，键名可通过 Props 属性配置 | array | - | - |
| props | 配置选项，具体见下表 | object | - | - |

## Props 属性列表

| 属性 | 说明 | 类型 | 可选值 | 默认值 |
|:-----|:----|:-----|:-------|:-------|
| expandTrigger | 次级菜单的展开方式 | string | click / hover | click |
| multiple | 是否多选 | boolean | - | false |
| checkStrictly | 是否严格的遵守父子节点不互相关联 | boolean | - | false |
| emitPath | 在选中节点改变时，是否返回由该节点所在的各级菜单的值所组成的数组，若设置 false，则只返回该节点的值 | boolean | - | true |
| lazy | 是否动态加载子节点，需与 lazyLoad 方法结合使用 | boolean | - | false |
| lazyLoad | 加载动态数据的方法，仅在 lazy 为 true 时有效 | function(node, resolve)，node为当前点击的节点，resolve为数据加载完成的回调(必须调用) | - | - |
| value | 指定选项的值为选项对象的某个属性值 | string | - | 'value' |
| label | 指定选项标签为选项对象的某个属性值 | string | - | 'label' |
| children | 指定选项的子选项为选项对象的某个属性值 | string | - | 'children' |
| disabled | 指定选项的禁用为选项对象的某个属性值 | string | - | 'disabled' |
| leaf | 指定选项的叶子节点的标志位为选项对象的某个属性值 | string | - | 'leaf' |



