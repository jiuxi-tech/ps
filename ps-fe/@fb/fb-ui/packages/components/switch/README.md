[comment]: <> (fb-docs: docsify/fb-ui/03/switch/README.md)

# 开关-Switch
用于两种状态之间的快速切换。

## 基础用法

- 需要表示两种状态之间快速切换时使用；
- 多在设置页面使用，切换之后直接触发状态的改变，不需要提交保存等操作；
- 表单中不建议使用；


```html run {title:'示例演示'}
<template>
<div>
普通用法：
<fb-switch/>

<br/><br/>
控件大小：
<fb-switch size="s"/>
<fb-switch size="m"/>
<fb-switch size="l"/>

<br/><br/>
控件状态：
<fb-switch disabled :value="false"/>
<fb-switch disabled :value="true"/>
<fb-switch readonly :value="false"/>
<fb-switch readonly :value="true"/>

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

### 自定义显示

```html run {title:'试一试'}
<template>
<div>
	<fb-switch @on-change="handleChange">
		<span slot="open">开</span>
		<span slot="close">关</span>
	</fb-switch>
  <br/>
  <br/>
  <br/>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
	methods: {
		handleChange (val) {
			val += "";
			this.$message.success("当前选中值：" +val,{align:'top'})
		},
	},
  }
</script>
<style>
</style>
```

### 自定义值

```html run {title:'试一试'}
<template>
<div>
	<fb-switch @on-change="handleChange" :true-value="1" :false-value="0">
		<span slot="open">开</span>
		<span slot="close">关</span>
	</fb-switch>
  <br/>
  <br/>
  <br/>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
	methods: {
		handleChange (val) {
			val += "";
			this.$message.success("当前选中值：" +val,{align:'top'})
		},
	},
  }
</script>
<style>
</style>
```

### 切换前确认

```html run {title:'试一试'}
<template>
<div style="height:200px">
	<fb-switch :confirm="handleConfirm">
		<span slot="open">开</span>
		<span slot="close">关</span>
	</fb-switch>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
	methods: {
		handleConfirm (checked) {
			return new Promise((resolve, reject) => {
				this.$confirm('真的要选么》？', () => {
					resolve()
				})
			})
		},
	},
  }
</script>
<style>
</style>
```




## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-change | 当状态变化时触发，可以是方法名，或方法体 | (val) => void

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 默认值 | Boolean, String, Number | false
| size | 尺寸 可选值是 s、m、l | String | m
| disabled | 禁用状态 | Boolean | false
| readonly | 只读状态 | Boolean | false
| confirm | 需要确认，请返回一个promise | Function |
| trueValue | 打开状态下的值 | Boolean, String, Number |true
| falseValue | 关闭状态下的值 | Boolean, String, Number |false
