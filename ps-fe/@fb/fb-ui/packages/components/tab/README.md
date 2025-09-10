[comment]: <> (fb-docs: docsify/fb-ui/02/tabs/README.md)

# 标签页-Tabs
用于页面内容的选项卡切换。

## 基础用法

提供了以下几种标签
- 带关闭标签：位于页面顶部，用于切换各个页面；
- 一般标签：一般位于卡片/容器及内容页面的内部，是最通用的Tabs；


```html run {title:'示例演示'}
<template>
<div>
	<fb-tabs>
		<fb-tab label="首页" icon="home">
			首页内容区域
		</fb-tab>
		<fb-tab label="标签2">
			标签2内容区域
		</fb-tab>
		<fb-tab label="标签3">
			标签3内容区域
		</fb-tab>
		<fb-tab label="标签4" disabled>
			标签4内容区域
		</fb-tab>
	</fb-tabs>
	<br/>
	<fb-tabs type="card">
		<fb-tab label="首页" icon="home">
			首页内容区域
		</fb-tab>
		<fb-tab label="标签2">
			标签2内容区域
		</fb-tab>
		<fb-tab label="标签3">
			标签3内容区域
		</fb-tab>
		<fb-tab label="标签4" disabled>
			标签4内容区域
		</fb-tab>
	</fb-tabs>
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

## 更多用法

### 关闭标签

```html run {title:'试一试'}
<template>
<div>
	<fb-tabs closable @on-tab-remove="handleTabRemove">
		<fb-tab v-for="(item, index) in tabs" :label="item.label" :name="item.name">
			<p>{{item}}</p>
		</fb-tab>
	</fb-tabs>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	tabs:[]
      }
    },
    mounted() {
			setTimeout(() => {
				this.tabs.push({label: `tab1`,name: `tab1`})
				this.tabs.push({label: `tab2`,name: `tab2`})
				this.tabs.push({label: `tab3`,name: `tab3`})
				this.tabs.push({label: `tab4`,name: `tab4`})
				this.tabs.push({label: `tab5`,name: `tab5`})
			}, 100)
    },
	methods: {
		handleTabRemove (tab,index) {
			this.tabs.splice(index, 1)
		},
	}
  }
</script>
<style>
</style>
```

## 事件列表

- fb-tabs

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-click | 当鼠标指针点击标签时触发，可以是方法名，或方法体 | (tab,index) => void
| on-tab-change | data绑定值发生变化时 | (tabs) => void
| on-tabs-data-change | data绑定值发生变化时 | (tabs) => void 
| on-tab-remove | 当标签关闭时触发，可以是方法名，或方法体 | (tab,index) => void
| on-tab-choose | 当标签选择时触发，可以是方法名，或方法体 | (tab,index) => void
| on-tab-dblclick | 当鼠标指针双击标签时触发，可以是方法名，或方法体 | (tab,index) => void

## 属性列表

- fb-tab

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| name | 标签名称 | String, Number |
| label | 显示内容 | String, Number |
| icon | 左侧显示的小图标 | String |
| disabled | 禁用状态 | Boolean | false
| transition | 进入/离开过渡效果 | String | tab-fade


- fb-tabs

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 当前选中的标签 | Number, String | 1
| type | card | String |
| closable | 是否显示关闭 | Boolean | false
| transition | 进入/离开过渡效果 | String | tab-fade
