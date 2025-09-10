[comment]: <> (fb-docs: docsify/fb-ui/03/checkbox/README.md)

# 多选框-Checkbox

用于在多个选项中选择多个选项。

## 基础用法

- 在一组选项中需要选中多项时使用，例如多选题的调查问卷；
- 单独使用时候可以表示某一类的两种状态的切换，和【开关-switch】类似；

```html run {title:'示例演示'}

<template>
	<div>

		单个选择：
		<fb-checkbox label="选项一" value="1"></fb-checkbox>
		<fb-checkbox label="选项二" value="2"></fb-checkbox>

		<br/><br/>
		多个选一：
		<fb-checkbox-group :data="options" value="1"/>

		<br/><br/>
		垂直排列：
		<fb-checkbox-group :data="options" value="1" vertical="true"/>

		<br/><br/>
		按钮状态：
		<fb-checkbox label="禁用" value="1" disabled="true"/>
		<fb-checkbox label="只读" value="2" readonly="true"/>


	</div>
</template>
<script>
	export default {
		data () {
			return {
				options: [
					{
						value: '1',
						label: '选择一'
					},
					{
						value: '2',
						label: '选项二'
					},
					{
						value: '3',
						label: '选项三'
					}
				],
			}
		},
	}
</script>
<style>
</style>
```

## 更多用法

### 一键全选

```html run {title:'示例演示'}

<template>
	<div>

		<fb-checkbox :value="allChecked" label="全选" @on-click="checkAll"></fb-checkbox>

		<fb-checkbox-group v-model="checked"
		                   :data="nodes"
		                   :reader="{label:'name', value:'id'}"
		                   @on-change="handleChange"
		></fb-checkbox-group>

		<br/><br/>
		选择项：{{checked}}
	</div>
</template>
<script>
	export default {
		data () {
			return {
				allChecked: false,
				checked: [],
				nodes: [
					{
						id: 1,
						name: '选项一',
					},
					{
						id: 2,
						name: '选项二',
					},
					{
						id: 3,
						name: '选项三',
					},
					{
						id: 4,
						name: '选项四',
					},
					{
						id: 5,
						name: '选项五',
					},
					{
						id: 6,
						name: '选项六',
					},
				],
			}
		},
		methods: {
			handleChange: function () {
				if (this.checked.length == 0) {
					this.allChecked = false
					return
				}

				this.allChecked = (this.checked.length === this.nodes.length ? true : 'indeterminate')
			},

			checkAll (checked, e) {
				this.checked = !checked ? [] : [1, 2, 3, 4, 5, 6]
			},
		},
	}
</script>
<style>
</style>
```

## 插槽

- 用于复杂场景下的内容控制。

```html run {title:'插槽示例'}


<template>
	<div>
		<div>


			是否喜欢？:
			<fb-checkbox v-model="c1" :label="c1?'结婚啦！':'分手啦！'">
				<template #checkbox="props">
					<fb-icon size="18" valign="middle" color="red"
					         :name="props.checked ? 'like-fill' : 'like'"
					/>
				</template>
			</fb-checkbox>

			{{ c1   }}
		</div>

		<div>
			<fb-checkbox v-model="c2" label="重要信息">
				<template #checkbox="props">
					<fb-icon size="18" valign="middle" color="#FFB500"
					         :name="props.checked ? 'selected-circle-fill' : 'circle'"
					/>
				</template>
			</fb-checkbox>

			已选： {{ c2 }}   <br>

			<fb-checkbox v-model="c3" label="不重要信息">
				<template #checkbox="props">
					<fb-icon size="18" valign="middle" color="#56D100"
					         :name="props.checked ? 'selected-circle-fill' : 'circle'"
					/>
				</template>
			</fb-checkbox>

			已选： {{ c3 }}     	<br>
		</div>

		<div>
			原始状态：
			<fb-checkbox-group v-model="cg1" :data="[
				{value: '1', label: '重要事故'},
				{value: '2', label: '重要会议'},
				{value: '3', label: '重大日程'}
			]"></fb-checkbox-group>
			<br>
			美化状态：
			<fb-checkbox-group v-model="cg1" :data="[
				{value: '1', label: '重要事故'},
				{value: '2', label: '重要会议'},
				{value: '3', label: '重大日程'}
			]">

				<template #checkbox="props">
					<fb-icon size="18" valign="middle"
					         :color="({'1': '#FFB500','2': '#65CC1D','3': '#0284FE',})[props.value]"
					         :name="props.checked ? 'selected-circle-fill' : 'circle'"
					/>
				</template>

			</fb-checkbox-group>

			<br>
			已选： {{ cg1 }}


		</div>
		 
	</div>
</template>
<script>
	export default {
		data () {
			return {
				c1: false,
				c2: false,
				c3: false,
				c4: false,
				c5: false,
				c6: false,
				cg1: [],	 
			}
		},
		methods: {
		}
	}
</script>
<style>
</style>

```

## 插槽

- fb-checkbox

| 插槽名      | 参数 | 说明    | 返回值 |
|:---------|:---|:------|-----| 
| label    |    | 控制文本  |     |
| checkbox |    | 控制选择框 |     |

- fb-checkbox-group

| 插槽名      | 参数 | 说明    | 返回值 |
|:---------|:---|:------|-----| 
| label    |    | 控制文本  |     |
| checkbox |    | 控制选择框 |     |

## 事件列表

- fb-checkbox

| 事件名       | 参数                   | 说明                       | 返回值               |
|:----------|:---------------------|:-------------------------|-------------------|
| on-click  | checked, event, node | 当点击按钮时触发，可以是方法名，或方法体     | (checked) => void |
| on-change | checked, node        | 当选择按钮发生变更时触发，可以是方法名，或方法体 | (val) => void     |

- fb-check-group

| 事件名       | 参数                                                | 说明                       | 返回值           |
|:----------|:--------------------------------------------------|:-------------------------|:--------------|
| on-change | currentValue, value, checked,  node, checkedNodes | 当选择按钮发生变更时触发，可以是方法名，或方法体 | (val) => void |

## 属性列表

- fb-checkbox

| 属性       | 说明     | 类型                      | 默认值   |
|:---------|:-------|:------------------------|:------|
| value    | 当前选择的项 | String, Number, Boolean |       |
| label    | 当前选择的项 | String, Number, Boolean | []    |
| disabled | 禁用状态   | Boolean                 | false |
| readonly | 只读状态   | Boolean                 | false |

- fb-check-group

| 属性       | 说明            | 类型                      | 默认值                              |
|:---------|:--------------|:------------------------|:---------------------------------|
| value    | 当前选择的项        | String, Number, Boolean |                                  |
| data     | 节点数据          | Array                   | []                               |
| vertical | 显示是否垂直        | Boolean                 | false                            |
| size     | 尺寸 可选值是 s、m、l | String                  | m                                |
| button   | 按钮形态          | Boolean                 | false                            |
| reader   | 取值配对          | Object                  | {value: 'value',label: 'label',} |
| disabled | 禁用状态          | Boolean                 | false                            |
| readonly | 只读状态          | Boolean                 | false                            |
