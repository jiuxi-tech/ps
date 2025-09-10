[comment]: <> (fb-docs: docsify/fb-ui/05/message-box/README.md)

# 全局提示确认框-MessageBox
全局展示的操作反馈信息，并确认后消失，常用于删除钱确认操作
## 基础用法
- 提供全局接口：$msgbox，$alert，$confirm 


## $msgbox
```html run {title:'示例演示'}
<template>
<div>
 <div>
    <fb-button @on-click="info">$msgbox</fb-button>
 </div>

 <div style="height: 300px;"></div>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
    methods: {
        info () {
            this.$msgbox({
                type: '',
                title: '提示123',
                message: '提示333333333333',
                showOkButton: true,
                callback() {
                    alert('确定 确认 回调')   
                },
                fallback() {
                    alert('取消回调')
                },
                icon: 'AI'
            })
        },
    },
  }
</script>
<style>
</style>
```


## $alert
```html run {title:'示例演示'}
<template>
<div>
 <div>
    <fb-button @on-click="info">$alert</fb-button>
 </div>

 <div style="height: 800px;"></div>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
    methods: {
        info () {
            this.$alert(`
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            你确定要跟骚艳结婚嘛?\n
            
            
        
                        `, () => {
                        this.$message.success('恭喜')
                    })
        },
    },
  }
</script>
<style>
</style>
```

## $confirm
```html run {title:'示例演示'}
<template>
<div>
 <div>
    <fb-button @on-click="info">$confirm</fb-button>
 </div>

 <div style="height: 300px;"></div>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
    methods: {
        info () {
            this.$confirm('请再次确认是否要跟骚艳结婚?', () => {
                this.$message.success('再次恭喜')
            }, () => {
                this.$message.error('骚艳默默在你心里留下了一滴泪')
            }, {
                title: '测试'
            })
        },
    },
  }
</script>
<style>
</style>
```


## 属性列表

| 属性 | 说明 | 类型 | 可选值 | 默认值 |
|:-----|:----|:-----|:-------|:-------|
| title | 标题 | String |  |  |
| align | 对齐 | String | | right |
| message | 消息 | String, Number |  |  |
| width | 宽度 | String, Number |  | 420 |
| type | 类型 | String |  |  |
| showOkButton | 显示确定按钮 | Boolean |  | true |
| showConfirmButton | 显示确认按钮 | Boolean |  | false |
| confirmButtonText | 确认按钮文字 | String |  | 确认 |
| showCancelButton | 显示取消按钮 | Boolean |  | false |
| cancelButtonText | 取消按钮文字 | String |  | 取消 |
| showClose | 显示右上角 关闭图标 | Boolean |  | false |
| fallback | 取消回调 | Function |  |  |
| callback | 确定 确认 回调 | Function |  |  |
| icon | 右侧标题icon | String |  | info |
