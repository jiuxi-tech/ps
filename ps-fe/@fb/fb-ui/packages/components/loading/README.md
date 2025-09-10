[comment]: <> (fb-docs: docsify/fb-ui/05/loading/README.md)

# 加载-Loading
用于页面和控件的加载中的状态。
> 交互建议：
- 当页面局部处于等待异步数据加载或正在渲染过程未完成时使用；
- 给予用户及时的反馈，减少用户在等待时候的焦虑感；
## 基础用法
> 组件块内 loading 请前往 [加载器-Spin](fb-ui/spin/)

```html run {title:'示例演示'}
<template>
<div>
    <fb-loading></fb-loading>
    <fb-loading text="加载中..." position="bottom"></fb-loading>

    <div style="margin-top: 32px;">
        <fb-loading spinner="loading2"></fb-loading>
        <fb-loading text="加载中..." spinner="loading2"></fb-loading>
    </div>

    <div style="margin-top: 32px;">
        <fb-loading color="#0ff" text-color="orange"></fb-loading>
        <fb-loading text="加载中..." color="red" text-color="orange"></fb-loading>
    </div>

    <div style="margin-top: 32px;">
        <fb-button @on-click="lock">全局锁定</fb-button>
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
        lock () {
            this.$loading.show({
                text: '1秒后自动关闭',
                duration: 1000
            })

            setTimeout(() => {
                this.$loading.hide()
            }, 1000)

        },
    },
  }
</script>
<style>
</style>
```

## 更多用法
Fb 为 Vue.prototype 添加了全局方法 $loading。因此在 vue instance 中可以采用本页面中的方式调用 Loading。
- 此时调用方法为 this.$loading.show(options)
- 全局关闭 this.$loading.hide()

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| text | 显示在加载图标的加载文案 | String |  |
| lock | 锁定 | Boolean | false |
| spinner | 自定义加载图标类名(fb库内icon名称) | String |  |
| color | 显示在加载图标颜色 | String | #0284FE |
| textColor | 显示文案颜色 | String | #0284FE |
| position | 显示文案位置(right/bottom) | String | right |
