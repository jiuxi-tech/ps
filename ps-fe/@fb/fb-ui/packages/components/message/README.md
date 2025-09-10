[comment]: <> (fb-docs: docsify/fb-ui/05/message/README.md)

# 全局提示-Message
全局展示的操作反馈信息
## 基础用法
- 提供五种全局操作反馈信息，“成功信息”、“提示信息”、“警告信息”、“错误信息”、“加载信息”；
- 页面居中显示并自动消失，是一种不打断用户操作的轻量级提示方式；

```html run {title:'示例演示'}
<template>
<div>
 <div>
    top
    <fb-button @on-click="$message.success('xxxx！', {align: 'top'})">Success</fb-button>

    <fb-button @on-click="$message.info('xxxx！', {align: 'top'})">Info</fb-button>

    <fb-button @on-click="$message.loading('xxxx！', {align: 'top'})">loading</fb-button>

    <fb-button @on-click="$message.warn('xxxx！', {align: 'top'})">Warn</fb-button>

    <fb-button @on-click="$message.error('xxxx！', {align: 'top'})">Error</fb-button>
 </div>

 <div style="height: 300px;"></div>

    <div>
       center
       <fb-button @on-click="success">Success</fb-button>

        <fb-button @on-click="info">Info</fb-button>

        <fb-button @on-click="loading">loading</fb-button>

        <fb-button @on-click="warn">Warn</fb-button>

        <fb-button @on-click="error">Error</fb-button>
    </div>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
    methods: {
        success () {
            this.$message.success('这是一条成功消息，会自动消失！')
        },
        info () {
            this.$message.info('这是一条成功消息，会自动消失！')
        },
        loading () {
            this.$message.loading('这是一条成功消息，会自动消失！')
        },
        warn () {
            this.$message.warn('这是一条成功消息，会自动消失！')
        },
        error () {
            this.$message('这是一条错误消息，会自动消失！',{
                type: 'error',
                duration: 500
            })
        },
    },
  }
</script>
<style>
</style>
```

## 更多用法
Fb 为 Vue.prototype 添加了全局方法 $message。因此在 vue instance 中可以采用本页面中的方式调用 Message。
- 此时调用方法为 Message(text, options)。我们也为每个 type 定义了各自的方法，如 Message.success(text, options)

## 属性列表

| 属性 | 说明 | 类型 | 可选值 | 默认值 |
|:-----|:----|:-----|:-------|:-------|
| message | 消息文字 | String, Number, Error |  |  |
| type | 类型 | String | success/warn/info/error/loading | info |
| duration | 显示时间, 毫秒 | Number |  | 3000 |
| align | 显示位置 | String | top/center | center |
