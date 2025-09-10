[comment]: <> (fb-docs: docsify/fb-ui/03/icon-select/README.md)

# 图标选择器 icon-select

## 基础用法

```html run {title:'示例演示'}
<template>
<div>
		 value: {{ icon }}
			<fb-divider></fb-divider>
			<fb-icon-select v-model="icon" :position="position"></fb-icon-select>

			<fb-radio-group
				v-model="position"
				:data="[
			 	{label: 'bottomLeft', value: 'bottomLeft'},
			 	{label: 'bottomCenter', value: 'bottomCenter'},
			 	{label: 'bottomRight', value: 'bottomRight'},
			 	{label: 'topLeft', value: 'topLeft'},
			 	{label: 'topCenter', value: 'topCenter'},
			 	{label: 'topRight', value: 'topRight'},
			 ]"

			></fb-radio-group>

</div>
</template>
<script>
  export default {
    data () {
      return {
        icon: 'search',
				position: 'bottomLeft',
      }
    },
  }
</script>
<style>
</style>
```

 

## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-change | 图标改变事件 | (event) => void|

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| label | 按钮显示名 | String | 图标选择 |
| size | 尺寸 可选值是 s、m、l | String |m|
| disabled | 禁用 | Boolean |false|
| clearable | 可清除 | Boolean | false|
| readonly | 只读 | Boolean | false |
| position | 面板位置 | String | bottomLeft |
|  |  |  | |

