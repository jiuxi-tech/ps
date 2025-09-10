[comment]: <> (fb-docs: docsify/fb-ui/03/radio/README.md)

# 单选框-Radio

用于单选。

## 基础用法

- 在多个选项中选中单个；
- 与【下拉单选】不同的是，选项全部平铺出来，直接可见；

```html run {title:'示例演示'}
<template>
<div>

单个选择：
<fb-radio label="选项一"  value="1"></fb-radio>
<fb-radio label="选项二"  value="2"></fb-radio>

<br/><br/>
多个选一：
<fb-radio-group	 :data="options" value="1"/>

<br/><br/>
垂直排列：
<fb-radio-group	 :data="options" value="1" vertical="true"/>

<br/><br/>
按钮状态：
<fb-radio label="禁用"  value="1" disabled="true"/>
<fb-radio label="只读"  value="2" readonly="true"/>


</div>
</template>
<script>
  export default {
    data () {
      return {
      	 options: [{value: '1', label: '选择一'}, {value: '2', label: '选项二'}, {value: '3', label: '选项三'}],
      }
    },
  }
</script>
<style>
</style>
```

## 更多用法

### 点击事件

```html run {title:'示例演示'}
<template>
<div>

<fb-radio v-model="checked" :label="longtxt"  value="1" @on-click="handleClick" ></fb-radio>

</div>
</template>
<script>
  export default {
    data () {
      return {
		longtxt: "点击我",
		checked: false,
		time:0,
      }
    },
	methods: {
		handleClick (checked,e) {
			this.time +=1;
			this.longtxt = "点击"+(this.time)+"次"
			this.$message.success(this.longtxt+"： "+checked,{align:'top'})
		},
	},
  }
</script>
<style>
</style>
```

### 选项切换

```html run {title:'示例演示'}
<template>
<div>

<fb-radio-group	 :data="options"	value="1" @on-change="handleChange" />

<div style="padding-top:10px" >
	<div  v-if="group == '1'">本年区域</div>
	<div  v-if="group == '2'">本月区域</div>
	<div  v-if="group == '3'">本季区域</div>
</div>

</div>
</template>
<script>
  export default {
    data () {
      return {
      	 options: [{value: '1', label: '本年'}, {value: '2', label: '本月'}, {value: '3', label: '本季'}],
      	 group:"1"
      }
    },
	methods: {
		handleChange (value) {
			this.group = value
		},
	},
  }
</script>
<style>
</style>
```

## 事件列表

- fb-radio

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-click | 当点击按钮时触发，可以是方法名，或方法体 | (checked) => void

- fb-radio-group

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-change | 当选择按钮发生变更时触发，可以是方法名，或方法体 | (val) => void

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 当前选择的项 | String, Number, Boolean |
| label | 当前选择的项 | String, Number, Boolean | []
| disabled | 禁用状态 | Boolean  | false
| readonly | 只读状态 | Boolean  | false

## 插槽



| 名称  | 说明     | 默认值 |
| :---- | :------- | :----- |
| label | 名称插槽 |        |

```vue
<fb-radio  >

  			<template slot="label">
						<fb-button round type="primary">111222</fb-button>
				</template>

</fb-tags>
```

