[comment]: <> (fb-docs: docsify/fb-ui/03/radio-group/README.md)

# 单选按钮-RadioButton

用于在多个选项中选择单个选项。

## 基础用法

- 用法与【标签页-Tabs】类似，可作为切换多个内容；
- 与【标签页-Tabs】不同的是，多作为二级选项来切换，如图表中的本年、本月、本周之间的切换；

### 单选按钮-RadioButton

```html run {title:'示例演示'}

<template>
	<div>
		按钮形态：
		<fb-radio-group button :data="options" value="1"/>

		<br/><br/>
		按钮尺寸：
		<fb-radio-group button :data="options" value="1" size="s"/>
		<fb-radio-group button :data="options" value="1" size="m"/>
		<fb-radio-group button :data="options" value="1" size="l"/>

		<br/><br/>
		垂直排列：
		<fb-radio-group button :data="options" value="1" vertical="true"/>

		<br/><br/>
		左右撑开：
		<fb-card>
			<fb-radio-group button long :data="options" value="1" />
		</fb-card>

		<br/><br/>
		按钮禁用：
		<fb-radio-group button :data="options" value="1" disabled="true"/>

		<br/><br/>
		按钮只读：
		<fb-radio-group button :data="options" value="1" readonly="true"/>

		<br/><br/>
		按钮分割：
		<fb-radio-group button :data="options" value="1" radio-space="8"/>

		<br/><br/>
		垂直排列分割：
		<fb-radio-group button :data="options" value="1" vertical radio-space="8"/>

	</div>
</template>
<script>
	export default {
		data () {
			return {
				options: [
					{
						value: '1',
						label: '本年'
					},
					{
						value: '2',
						label: '本月'
					},
					{
						value: '3',
						label: '本季'
					}
				]
			}
		},
	}
</script>
<style>
</style>
```

## 更多用法

### 表单切换

```html run {title:'示例演示'}

<template>
	<div>

		<fb-radio-group button :data="options" value="1" @on-change="handleChange"/>

		<div style="padding-top:10px">
			<div v-if="group == '1'">本年区域</div>
			<div v-if="group == '2'">本月区域</div>
			<div v-if="group == '3'">本季区域</div>
		</div>

	</div>
</template>
<script>
	export default {
		data () {
			return {
				options: [
					{
						value: '1',
						label: '本年'
					},
					{
						value: '2',
						label: '本月'
					},
					{
						value: '3',
						label: '本季'
					}
				],
				group: "1"
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

| 事件名       | 参数           | 说明                       | 返回值           |
|:----------|:-------------|:-------------------------|:--------------|
| on-change | value, radio | 当选择按钮发生变更时触发，可以是方法名，或方法体 | (val) => void |
| input     | value, radio |                          |               |

## 属性列表

| 属性         | 说明                                           | 类型                        | 默认值                              |
|:-----------|:---------------------------------------------|:--------------------------|:---------------------------------|
| value      | 当前选择的项                                       | String, Number, Boolean   |                                  |
| data       | 节点数据<br />{label, value, disabled, readonly} | Array                     | []                               |
| vertical   | 显示是否垂直                                       | Boolean                   | false                            |
| size       | 尺寸大小 可选值是 s、m、l                              | String                    | m                                |
| button     | 按钮形态                                         | Boolean                   | false                            |
| long       | 左右撑开                                         | Boolean                   | false                            |
| reader     | 取值配对                                         | Object                    | {value: 'value',label: 'label',} |
| disabled   | 禁用状态                                         | Boolean                   | false                            |
| readonly   | 只读状态                                         | Boolean                   | false                            |
| radioSpace | radio 间距                                     | Number, String            | 0                                |
| service    | 服务获取节点数据                                     | [Object, Array, Function] | undefined                        |
| param      | 查询参数                                         | [Object, Array, Function] | undefined                        |
| dataFilter | 网络数据过滤器                                      | Function                  | (data) {    return (data) }      |
| autoLoad   | 自动加载数据                                       | Boolean                   | true                             |
|            |                                              |                           |                                  |
|            |                                              |                           |                                  |

## 插槽

| 名称   | 说明   | 默认值 |
|:-----|:-----|:----|
| node | 节点插槽 |     |

```vue

<fb-radio-group :data="[]">

	<template v-slot:node="{node}">
		<fb-button round type="primary">{{node.label}}</fb-button>
	</template>

	</fb-tags>
```

