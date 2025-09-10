[comment]: <> (fb-docs: docsify/fb-ui/03/transfer/README.md)

# 穿梭框-Transfer

## 基础用法

```html run {title:'示例演示'}
<template>
<div>
<fb-transfer :data="sourceData"/>
</div>
</template>
<script>
  export default {
    data () {
      return {
		sourceData: [
			{value: "1", label: `禁用`,description: `内容1`,disabled:true},
			{value: "2", label: `只读`,description: `内容2`,readonly:true},
			{value: "3", label: `标题3`,description: `内容3`,},
			{value: "4", label: `标题4`,description: `内容4`,},
			{value: "5", label: `标题5`,description: `内容5`,},
			{value: "6", label: `标题6`,description: `内容6`,},
			{value: "7", label: `标题7`,description: `内容7`,},
			{value: "8", label: `标题8`,description: `内容8`,},
			{value: "9", label: `标题9`,description: `内容9`,},
		],
      }
    },
  }
</script>
<style>
</style>
```

## 更多用法

### 设置标题

```html run {title:'试一试'}
<template>
<div>
<fb-transfer :data="sourceData"
	:titles="['来源列表', '目标列表']"
/>
</div>
</template>
<script>
  export default {
    data () {
      return {
		sourceData: [
			{value: "1", label: `标题1`,description: `内容1`,},
			{value: "2", label: `标题2`,description: `内容2`,},
			{value: "3", label: `标题3`,description: `内容3`,},
			{value: "4", label: `标题4`,description: `内容4`,},
			{value: "5", label: `标题5`,description: `内容5`,},
			{value: "6", label: `标题6`,description: `内容6`,},
			{value: "7", label: `标题7`,description: `内容7`,},
			{value: "8", label: `标题8`,description: `内容8`,},
			{value: "9", label: `标题9`,description: `内容9`,},
		],
      }
    },
  }
</script>
<style>
</style>
```

### 默认选中

```html run {title:'试一试'}
<template>
<div>
<fb-transfer :data="sourceData"
	:target-keys="targetKeys"
	:selected-keys="selectedKeys"
/>
</div>
</template>
<script>
  export default {
    data () {
      return {
			sourceData: [
				{value: "1", label: `标题1`,description: `内容1`,},
				{value: "2", label: `标题2`,description: `内容2`,},
				{value: "3", label: `标题3`,description: `内容3`,},
				{value: "4", label: `标题4`,description: `内容4`,},
				{value: "5", label: `标题5`,description: `内容5`,},
				{value: "6", label: `标题6`,description: `内容6`,},
				{value: "7", label: `标题7`,description: `内容7`,},
				{value: "8", label: `标题8`,description: `内容8`,},
				{value: "9", label: `标题9`,description: `内容9`,},
			],
			selectedKeys: ['1', '4'],
			targetKeys: ['3', '5'],
      }
    },
  }
</script>
<style>
</style>
```

### 全选操作

```html run {title:'试一试'}
<template>
<div>
<fb-transfer :data="sourceData"
	show-fast-buttons
	@on-change="handleChange"
/>
</div>
</template>
<script>
  export default {
    data () {
      return {
			sourceData: [
				{value: "1", label: `标题1`,description: `内容1`,},
				{value: "2", label: `标题2`,description: `内容2`,},
				{value: "3", label: `标题3`,description: `内容3`,},
				{value: "4", label: `标题4`,description: `内容4`,},
				{value: "5", label: `标题5`,description: `内容5`,},
				{value: "6", label: `标题6`,description: `内容6`,},
				{value: "7", label: `标题7`,description: `内容7`,},
				{value: "8", label: `标题8`,description: `内容8`,},
				{value: "9", label: `标题9`,description: `内容9`,},
			],
      }
    },
	methods: {
		handleChange (targetKeys, targetList, sourceKeys, sourceList) {
			this.$message.success("触发事件：on-change，请查看日志",{align:'top'})
			console.log("目标选中项：",targetKeys)
			console.log("目标所有项：",targetList)
			console.log("来源选中项：",sourceKeys)
			console.log("来源所有项：",sourceList)
		},
	}
  }
</script>
<style>
</style>
```

### 自定义样式

```html run {title:'试一试'}
<template>
<div>
<fb-transfer
	:data="sourceData"
	:list-style="{height: '200px', width: '300px'}"
/>
</div>
</template>
<script>
  export default {
    data () {
      return {
			sourceData: [
				{value: "1", label: `标题1`,description: `内容1`,},
				{value: "2", label: `标题2`,description: `内容2`,},
				{value: "3", label: `标题3`,description: `内容3`,},
				{value: "4", label: `标题4`,description: `内容4`,},
				{value: "5", label: `标题5`,description: `内容5`,},
				{value: "6", label: `标题6`,description: `内容6`,},
				{value: "7", label: `标题7`,description: `内容7`,},
				{value: "8", label: `标题8`,description: `内容8`,},
				{value: "9", label: `标题9`,description: `内容9`,},
			],
      }
    },
  }
</script>
<style>
</style>
```

### 自定义选项

```html run {title:'试一试'}
<template>
<div>
<fb-transfer
	:data="sourceData"
>
	<template v-slot:source="{node}">
		左{{node.value}} - {{node.label}}
	</template>

	<template v-slot:target="{node}">
		右{{node.value}} - {{node.label}}
	</template>

</fb-transfer>
</div>
</template>
<script>
  export default {
    data () {
      return {
			sourceData: [
				{value: "1", label: `标题1`,description: `内容1`,},
				{value: "2", label: `标题2`,description: `内容2`,},
				{value: "3", label: `标题3`,description: `内容3`,},
				{value: "4", label: `标题4`,description: `内容4`,},
				{value: "5", label: `标题5`,description: `内容5`,},
				{value: "6", label: `标题6`,description: `内容6`,},
				{value: "7", label: `标题7`,description: `内容7`,},
				{value: "8", label: `标题8`,description: `内容8`,},
				{value: "9", label: `标题9`,description: `内容9`,},
			],
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
| on-change | 当选择项发生变更时触发，可以是方法名，或方法体 | (targetKeys, targetList, sourceKeys, sourceList) => void

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| reader | 取值配对 | Object | {value: 'value',label: 'label',}
| data | 节点数据 | Array | []
| titles | 显示标题 | Array |
| buttons | 显示按钮 | Array |
| list-style | 列表样式 | Object |
| selected-keys | 来源列选中项 | Array |
| target-keys | 目标列选项 | Array |
| show-fast-buttons | 是否显示全选按钮 | Boolean |
