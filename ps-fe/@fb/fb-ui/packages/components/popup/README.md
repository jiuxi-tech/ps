[comment]: <> (fb-docs: docsify/fb-ui/04/popup/README.md)

# 气泡卡片-Popover
鼠标点击/移入元素时，弹出气泡式的卡片浮层展示隐藏的信息。

## 基础用法

当目标元素有进一步展示信息的需求时使用；
用法与【文字提示-Tooltips】类似，区别是气泡卡片可以承载更多的复杂内容，包括链接按钮标签等；
``
- 一般用于但不仅限于地图模块的气泡卡片信息展示；
- 提供了四个方向12种不同位置的气泡供选择；

```html run {title:'示例演示'}
<template>
<div>

<div style="position: relative; border: 1px solid #666; height: 80px; width: 200px">
<fb-popup-picker :value="true" position="bottomLeft">
	<fb-card>
		<p>左下</p>
	</fb-card>
</fb-popup-picker>
</div>
<br/>
<div style="position: relative; border: 1px solid #666; height: 80px; width: 200px">
<fb-popup-picker :value="true" position="bottomCenter">
	<fb-card>
		<p>中下</p>
	</fb-card>
</fb-popup-picker>
</div>
<br/>
<div style="position: relative; border: 1px solid #666; height: 80px; width: 200px">
<fb-popup-picker :value="true" position="bottomRight">
	<fb-card>
		<p>右下</p>
	</fb-card>
</fb-popup-picker>
</div>
<br/>
<div style="position: relative; border: 1px solid #666; height: 80px; width: 200px">
<fb-popup-picker :value="true" position="topLeft">
	<fb-card>
		<p>左上</p>
	</fb-card>
</fb-popup-picker>
</div>
<br/>
<div style="position: relative; border: 1px solid #666; height: 80px; width: 200px">
<fb-popup-picker :value="true" position="topCenter">
	<fb-card>
		<p>中上</p>
	</fb-card>
</fb-popup-picker>
</div>
<br/>
<div style="position: relative; border: 1px solid #666; height: 80px; width: 200px">
<fb-popup-picker :value="true" position="topRight">
	<fb-card>
		<p>右上</p>
	</fb-card>
</fb-popup-picker>
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

## 更多用法

### 点击显示/关闭

```html run {title:'试一试'}
<template>
<div>

<div style="position: relative; border: 1px solid #666; height: 120px; width: 200px">
<fb-button @on-click="show = !show">点击这里</fb-button>
<fb-popup-picker v-model="show" position="bottomLeft">
	<fb-card>
		<p>左下</p>
		<p>空白处点击关闭</p>
	</fb-card>
</fb-popup-picker>
</div>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	show:false
      }
    },
  }
</script>
<style>
</style>
```

### 展示下拉

```html run {title:'试一试'}
<template>
<div>
	<div style="position: relative; border: 1px solid #666; height: 280px; width: 200px">
		<fb-popup-select v-model="show">
			<fb-button slot="header" @on-click="show = !show">点击这里</fb-button>
			<fb-card slot="picker" header="列展示" style="width: 168px">
				<template slot="actions">
					<fb-button type="link">重置</fb-button>
				</template>
				<fb-container max-height="180px" overflow="auto">
					<fb-checkbox-group
						:data="columns.map(column=>{column.value = column.name; return  column})"
						@on-change="handleChange"
						vertical
					></fb-checkbox-group>
				</fb-container>
			</fb-card>
		</fb-popup-select>
	</div>
	<br/>
	选择项：{{checked}}
</div>
</template>
<script>
  export default {
    data () {
      return {
      	show:false,
		checked: [],
		columns: [
			{name: 'personName',label: '用户名称',sortable: false,width: 120,align: 'left',},
			{name: 'personNo',label: '人员编号',sortable: false,width: 100,align: 'left',},
			{name: 'sex',label: '性别',sortable: false,align: 'left',slot: 'tags',},
			{name: 'phone',label: '手机号码',sortable: false,align: 'left',width: 220,slot: 'tags',},
			{name: '_op',label: '操作',sortable: false,align: 'left',slot: 'actions',},
		],
      }
    },
	methods: {
		handleChange (chks,value,checked) {
			if(checked){
				this.checked.push(value);
			}else{
				let index = this.checked.indexOf(value)
				this.checked.splice(index,1)
			}
		},
   },
  }
</script>
<style>
</style>
```

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 显示 管控 点击其他位置消失 | Boolean | false
| position | 显示位置，可选值：bottomLeft,bottomCenter,bottomRight,topLeft,topCenter,topRight | String | bottomLeft
| trigger | 触发方式，可选值：clickOnly | String |
