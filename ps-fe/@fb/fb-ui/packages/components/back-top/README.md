[comment]: <> (fb-docs: docsify/fb-ui/01/back-top/README.md)

# 回到顶部-BackTop
返回页面顶部的操作按钮

## 基础用法


```html run {title:'示例演示'}
<template>
<div style="padding-top: 10px;">
<div style="position: relative">

			<div class="lalala" style="height: 600px; overflow: auto;">
				<div style="height: 1400px;"></div>
			</div>

			<fb-back-top target=".lalala" circle style="position: absolute" :bottom="100"></fb-back-top>
			<fb-back-top target=".lalala" style="position: absolute">
				<div style="font-size: 12px">回到<br/>顶部</div>
			</fb-back-top>
		</div>
</div>
</template>
<script>
  export default {
	data () {
		return {
		}
	},
  }
</script>
<style>
</style>
```

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| target | 触发滚动的对象（.xxx \ #xxx）  | String | document
| visibilityHeight | 滚动高度达到此参数值才出现 | Number | 100
| right | 控制其显示位置, 距离页面右边距 | Number | 40
| bottom | 控制其显示位置, 距离页面底部距离 | Number | 40 |
| circle | 圆形显示 | Boolean | false |
