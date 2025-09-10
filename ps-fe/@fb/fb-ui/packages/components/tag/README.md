[comment]: <> (fb-docs: docsify/fb-ui/04/tag/README.md)

# 标签-Tag

用于标记及分类的小标签。

## 基础用法

用户标记事物的属性和维度；明确的进行分类；

```html run {title:'示例演示'}
<template>
<div>
控件类型：
<fb-tag label="文本"></fb-tag>
<fb-tag label="可删除" closable></fb-tag>
<fb-tag label="圆角" closable round></fb-tag>
<fb-tag label="圆角" round></fb-tag>


<br/><br/>
控件形态：
<fb-tag closable>默认</fb-tag>
<fb-tag type="info" closable>消息</fb-tag>
<fb-tag type="primary" closable>主要</fb-tag>
<fb-tag type="danger" closable>危险</fb-tag>
<fb-tag type="warn" closable>警告</fb-tag>
<fb-tag type="success" closable>成功</fb-tag>



<br/><br/>
填充效果：
<fb-tag type="primary" effect="dark" closable>主要</fb-tag>
<fb-tag type="primary" effect="plain" closable>主要</fb-tag>
<fb-tag type="primary" effect="light" closable>主要</fb-tag>
<fb-tag type="danger" effect="dark" closable>危险</fb-tag>
<fb-tag type="danger" effect="plain" closable>危险</fb-tag>
<fb-tag type="danger" effect="light" closable>危险</fb-tag>


<br/><br/>
控件颜色：
<fb-tag color="#28a745">简单</fb-tag>
<fb-tag color="#272452" closable>可删除</fb-tag>
<fb-tag color="#BF7158" closable round>圆角</fb-tag>

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

### 选择标签

```html run {title:'试一试'}
<template>
<div style="height:100px">
<fb-tag type="primary"
	@on-click="handleClick"
	@on-close="handleClose"
	closable>文本</fb-tag>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	num:0,
      }
    },
	methods: {
		handleClick (tag) {
			this.$message.success("我被点击了 "+(++this.num)+" 次",{align:'top'})
		},
		handleClose (tag) {
			this.$message.success("我被点击了关闭",{align:'top'})
		},
	},
  }
</script>
<style>
</style>
```

### 一组标签

```html run {title:'试一试'}
<template>
<div style="height:100px">
<fb-tags
	 :data="data"
	 @on-remove="handleRemove"
	 ></fb-tags>
</div>
</template>
<script>
  export default {
	data () {
		return {
			data: [
				{label: '标签1',value: 1,type: 'primary',closable: true,round: true,},
				{label: '标签2',value: 2,type: 'danger',},
				{label: '标签3',value: 3,type: 'warn',closable: true,effect: 'plain',},
				{label: '标签4',value: 4,color: 'orange',closable: true,},
				{label: '标签5',value: 5,type: 'primary',effect: 'plain'},
				{label: '标签6',value: 6,type: 'primary',effect: 'light'},
			],
		}
	},
	methods: {
		handleRemove (value, tag) {
			this.$message.success(tag.label +"被关闭了",{align:'top'})
		},
	},
  }
</script>
<style>
</style>
```

## 事件列表

- fb-tag

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-close | 当鼠标指针点击关闭图标时触发，可以是方法名，或方法体 | (event) => void
| on-click | 当鼠标指针点击文本触发，可以是方法名，或方法体 | (event, tag) => void

- fb-tags

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-remove | 当关闭标签时触发，可以是方法名，或方法体 | (event) => void
| on-click | 当鼠标指针点击文本触发，可以是方法名，或方法体 | (event, tag) => void


## 属性列表

- fb-tag

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 当前值 | String, Number |
| label | 显示的文本 | String|
| round | 是否圆角显示 | Boolean | false
| closable | 是否显示关闭 | Boolean | false
| type | 控件形态，可选值：default,info,primary,danger,warn,success| String | default
| color | 背景颜色 | String |
| effect | 填充效果，可选值：dark, light, plain | String |dark

- fb-tags

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 当前值 | String, Number |
| data | 节点数据 | Array |
| reader | 取值配对 | Object | {value: 'value',label: 'label',}
| vertical | 是否垂直 | Boolean | false
| disabled | 禁用状态 | Boolean  | false
| readonly | 只读状态 | Boolean  | false

## 插槽

fb-tags

| 名称 | 说明     | 默认值 |
| :--- | :------- | :----- |
| node | 节点插槽 |        |

```vue
<fb-tags :data="data">

  			<template v-slot:node="{node}">
						<fb-button round type="primary">{{ node.label }}</fb-button>
				</template>

</fb-tags>
```

