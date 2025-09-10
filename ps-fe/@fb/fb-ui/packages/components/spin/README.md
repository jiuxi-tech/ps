[comment]: <> (fb-docs: docsify/fb-ui/05/spin/README.md)

# 加载器-Spin

- 用于页面和控件的加载中的状态。
- 是基于loading组件的二次封装

## 基础用法

 -  嵌套在需要loading元素外层

```html run {title:'示例演示'}
<template>
<div>
    <fb-spin>
        <div class="my-content">
            测试。。。
        </div>
    </fb-spin>

    <fb-spin text="加载中" spinner="loading2">
        <div class="my-content">
            测试。。。
        </div>
    </fb-spin>

    <fb-spin text="加载中" :show="show">
        <div class="my-content">
            测试。。。
        </div>
    </fb-spin>

    <fb-card header="状态绑定">
        <fb-spin text="加载中" :show="show">
            <div class="my-content">
                测试。。。
            </div>
        </fb-spin>

        <div slot="actions">
            <fb-switch v-model="show"></fb-switch>
        </div>
    </fb-card>
    <br/>
    <br/>
    <fb-card header="带文字">
			<fb-spin
				text="加载中"
				:show="show2"
				color="red"
				text-color="orange"
			>
				<div ref="aaa" class="my-content">
					测试测试测试测试。。。
				</div>
			</fb-spin>

			<div slot="actions">
				<fb-switch v-model="show2"></fb-switch>
			</div>
	</fb-card>
    <br/>
    <fb-button @on-click="lock">锁定</fb-button>

</div>
</template>
<script>
  export default {
    data () {
      return {
        show: true,
		show2: false,
      }
    },
    methods: {
        lock () {

            this.$loading.show({
                text: '5秒后自动关闭',
                spinner: 'loading2'
            })

            setTimeout(() => {
                this.$loading.hide()
            }, 5000)

        },
    },
  }
</script>
<style>
.my-content {
    height: 80px;
    margin-top: 16px;
}
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
