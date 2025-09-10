[comment]: <> (fb-docs: docsify/fb-ui/03/tree-select/README.md)

# 选择器-TreeSelect
点击从控件弹出框中选择目标选项，然后选择弹窗消失，选择的内容显示在框中。

## 基础用法

- 下拉树选择框

从空间弹出框中弹出的树中选择单个或多个选项。

1. 当选项多时（超过20项），建议使用带搜索的下拉框；

```html run {title:'示例演示'}
<template>
<div>
控件类型：
<br/><br/>
<fb-tree-select :data="nodes" placeholder="下拉树只读" width="30%" readonly></fb-tree-select>
<br/><br/>
<fb-tree-select :data="nodes" placeholder="下拉树禁用" width="30%" disabled></fb-tree-select>
<br/><br/>
<fb-tree-select :data="nodes" placeholder="下拉树单选框"/>
<br/><br/>
<fb-tree-select :data="nodes" placeholder="不带内置'清除'按钮" :clearable="false"/>
<br/><br/>
<fb-tree-select :data="nodes" placeholder="下拉树多选框" multiple/>

<br/><br/>
控件样式：

<br/><br/>
<fb-tree-select :data="nodes" placeholder="自定义宽度：30%" style="width: 30%"></fb-tree-select>
<fb-tree-select :data="nodes" placeholder="自定义宽度：150px" style="width: 150px"></fb-tree-select>

</div>
</template>
<script>
  export default {
    data () {
      return {
      	nodes:[
			{
				'value': '1',
				'label': '1',
				'expand': true,
				'checked': false,
				'children': [
					{
						'value': '1-1',
						'label': '1-1',
						'expand': true,
						'checked': false,
						'children': [
							{
								'value': '1-1-1',
								'label': '1-1-1',
								'expand': true,
								'checked': false,
								'children': [],
							},
						],
					},
				],
			},
		],
      }
    },
  }
</script>
<style>
</style>
```



## 更多用法

### 显示格式化

```html run {title:'试一试'}
<template>
<div style="height:140px">
<fb-tree-select v-model="selected"
	:placeholder="'请选择'"
	:data="nodes"
	:header-format="headerFormat"
	style="width: 200px"
/>
<br/><br/>
当前选择值：{{selected}}
</div>
</template>
<script>
  export default {
    data () {
      return {
        selected:"",
      	nodes:[
			{
				'value': '1',
				'label': '1',
				'desc': '第一级',
				'expand': true,
				'checked': false,
				'children': [
					{
						'value': '1-1',
						'label': '1-1',
						'desc': '第二级',
						'expand': true,
						'checked': false,
						'children': [
							{
								'value': '1-1-1',
								'label': '1-1-1',
								'desc': '第三级',
								'expand': true,
								'checked': false,
								'children': [],
							},
						],
					},
				],
			},
		],
      }
    },
	methods: {
		headerFormat (node) {
			if(node && node.label){
				return `${node.label} - ${node.desc}`
			}
			return "";
		},
	},
  }
</script>
<style>
</style>
```

### 默认选中

```html run {title:'试一试'}
<template>
<div style="height:140px">
<fb-tree-select v-model="selected"
	:placeholder="'请选择'"
	:data="nodes"
	style="width: 200px"
/>
<br/><br/>
当前选择值：{{selected}}
</div>
</template>
<script>
  export default {
    data () {
      return {
      	selected:"1-1",
      	nodes:[
			{
				'value': '1',
				'label': '1',
				'desc': '第一级',
				'expand': true,
				'checked': false,
				'children': [
					{
						'value': '1-1',
						'label': '1-1',
						'desc': '第二级',
						'expand': true,
						'checked': false,
						'children': [
							{
								'value': '1-1-1',
								'label': '1-1-1',
								'desc': '第三级',
								'expand': true,
								'checked': false,
								'children': [],
							},
						],
					},
				],
			},
		],
      }
    },
  }
</script>
<style>
</style>
```

### 手动选中

```html run {title:'通过绑定值选择'}
<template>
<div style="height:140px">
<fb-tree-select v-model="selected"
	:placeholder="'请选择'"
	:data="nodes"
	style="width: 200px"
/>
<br/><br/>
当前选择值：{{selected}}
<br/><br/>
<fb-button @on-click="handleClick">点击选中 1-1</fb-button>

</div>
</template>
<script>
  export default {
    data () {
      return {
      	selected:"",
      	nodes:[
			{
				'value': '1',
				'label': '1',
				'desc': '第一级',
				'expand': true,
				'checked': false,
				'children': [
					{
						'value': '1-1',
						'label': '1-1',
						'desc': '第二级',
						'expand': true,
						'checked': false,
						'children': [
							{
								'value': '1-1-1',
								'label': '1-1-1',
								'desc': '第三级',
								'expand': true,
								'checked': false,
								'children': [],
							},
						],
					},
				],
			},
		],
      }
    },
	methods: {
		handleClick (e) {
			this.selected = "1-1"
		},
	},
  }
</script>
<style>
</style>
```

```html run {title:'selectNodeByValue方法：选择一个'}
<template>
<div style="height:140px">
<fb-tree-select v-model="selected"
	:placeholder="'请选择'"
	:data="nodes"
	style="width: 200px"
	@on-select-change="handleSelectChange"
	ref="selectTree"
/>
<br/><br/>
当前选择值：{{selected}}
<br/><br/>
<fb-button @on-click="handleClick">点击选中 1-1-1</fb-button>

</div>
</template>
<script>
  export default {
    data () {
      return {
      	selected:"",
      	nodes:[
			{
				'value': '1',
				'label': '1',
				'desc': '第一级',
				'expand': true,
				'checked': false,
				'children': [
					{
						'value': '1-1',
						'label': '1-1',
						'desc': '第二级',
						'expand': true,
						'checked': false,
						'children': [
							{
								'value': '1-1-1',
								'label': '1-1-1',
								'desc': '第三级',
								'expand': true,
								'checked': false,
								'children': [],
							},
						],
					},
				],
			},
		],
      }
    },
	methods: {
		handleClick (e) {
			this.$refs.selectTree.selectNodeByValue('1-1-1')
		},
		handleSelectChange (node){
			this.$message.success("当前选择项："+node.desc,{align:'top'})
		},
	},
  }
</script>
<style>
</style>
```

```html run {title:'checkNodesByValue方法：选择多个'}
<template>
<div style="height:140px">
<fb-tree-select v-model="selected"
	:placeholder="'请选择'"
	:data="nodes"
	style="width: 200px"
	@on-check-change="handleCheckChange"
	ref="selectTree"
	multiple
/>
<br/><br/>
当前选择值：{{selected}}
<br/><br/>
<fb-button @on-click="handleClick">点击选中 1-1-1</fb-button>

</div>
</template>
<script>
  export default {
    data () {
      return {
      	selected:[],
      	nodes:[
			{
				'value': '1',
				'label': '1',
				'desc': '第一级',
				'expand': true,
				'checked': false,
				'children': [
					{
						'value': '1-1',
						'label': '1-1',
						'desc': '第二级',
						'expand': true,
						'checked': false,
						'children': [
							{
								'value': '1-1-1',
								'label': '1-1-1',
								'desc': '第三级',
								'expand': true,
								'checked': false,
								'children': [],
							},
						],
					},
				],
			},
		],
      }
    },
	methods: {
		handleClick (e) {
			this.$refs.selectTree.checkNodesByValue(['1-1-1'])
		},
		handleCheckChange (nodes){
			this.$message.success("当前选中项："+ nodes.length +"个",{align:'top'})
		},
	},
  }
</script>
<style>
</style>
```

### 动态加载

```html run {title:'试一试'}
<template>
<div style="height:140px">
<fb-tree-select v-model="selected"
	:placeholder="'请选择'"
	:data="nodes"
	style="width: 200px"
/>
<br/><br/>
当前选择值：{{selected}}
<br/><br/>
<fb-button @on-click="handleClick">点击加载</fb-button>

</div>
</template>
<script>
  export default {
    data () {
      return {
      	selected:"",
      	nodes:[],
      	data:[
			{
				'value': '1',
				'label': '1',
				'desc': '第一级',
				'expand': true,
				'checked': false,
				'children': [
					{
						'value': '1-1',
						'label': '1-1',
						'desc': '第二级',
						'expand': true,
						'checked': false,
						'children': [
							{
								'value': '1-1-1',
								'label': '1-1-1',
								'desc': '第三级',
								'expand': true,
								'checked': false,
								'children': [],
							},
						],
					},
				],
			},
		],
      }
    },
	methods: {
		handleClick (e) {
			this.nodes = this.data
			this.$message.success("加载完成",{align:'top'})
		},
	},
  }
</script>
<style>
</style>
```

### 父子节点联运

```html run {title:'试一试'}
<template>
<div style="height:140px">
<fb-tree-select v-model="selected"
	:placeholder="'请选择'"
	:data="nodes"
	:do-check="'s'"
	:do-un-check="'p'"
	style="width: 200px"
	multiple
/>
<br/><br/>
当前选择值：{{selected}}

</div>
</template>
<script>
  export default {
    data () {
      return {
      	selected:"",
      	nodes:[
			{
				'value': '1',
				'label': '1',
				'desc': '第一级',
				'expand': true,
				'checked': false,
				'children': [
					{
						'value': '1-1',
						'label': '1-1',
						'desc': '第二级',
						'expand': true,
						'checked': false,
						'children': [
							{
								'value': '1-1-1',
								'label': '1-1-1',
								'desc': '第三级',
								'expand': true,
								'checked': false,
								'children': [],
							},
						],
					},
				],
			},
		],
      }
    },
  }
</script>
<style>
</style>
```


### 过滤节点 + collapseTags

```html run {title:'试一试'}
<template>
<div style="height:140px">
<fb-tree-select v-model="selected"
	:placeholder="'请选择'"
	:data="nodes"
	:do-check="'s'"
	:do-un-check="'p'"
	style="width: 200px"
	multiple
    filterable
    collapseTags
/>
<br/><br/>
当前选择值：{{selected}}

</div>
</template>
<script>
  export default {
    data () {
      return {
      	selected:"",
      	nodes:[
			{
				'value': '1',
				'label': '1',
				'desc': '第一级',
				'expand': true,
				'checked': false,
				'children': [
					{
						'value': '1-1',
						'label': '1-1',
						'desc': '第二级',
						'expand': true,
						'checked': false,
						'children': [
							{
								'value': '1-1-1',
								'label': '1-1-1',
								'desc': '第三级',
								'expand': true,
								'checked': false,
								'children': [],
							},
						],
					},
                    {
						'value': '1-2',
						'label': '1-2',
						'desc': '第二级',
						'expand': true,
						'checked': false,
						'children': [
							{
								'value': '1-2-1',
								'label': '1-2-1',
								'desc': '第三级',
								'expand': true,
								'checked': false,
								'children': [],
							},
						],
					},
				],
			},
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
| on-change | 当选择值发生变更时触发，可以是方法名，或方法体 | (val) => void|
| on-select-change | 当选择项发生变更时触发，可以是方法名，或方法体 | (node) => void|
| on-check-change | 当选择项发生变更时触发，可以是方法名，或方法体 | (nodes) => void|
| on-blur | 当失去焦点时触发，可以是方法名，或方法体 | (event) => void|
| on-focus | 当获取焦点时触发，可以是方法名，或方法体 | (event) => void|
| on-click | 当鼠标指针点击按钮时触发，可以是方法名，或方法体 | (event) => void|
| on-finished | 当选择项发生变更时触发，可以是方法名，或方法体 | (val) => void|
| on-data-load | 当数据通过网站加载时触发，可以是方法名，或方法体 | (event) => void|
| on-data-update | 当加载数据发生变更时触发，可以是方法名，或方法体 | () => void|

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 绑定值 | String, Number, Array ||
| placeholder | 占位提示 | String ||
| disabled | 禁用状态 | Boolean | false|
| readonly | 只读状态 | Boolean | false|
| maxHeight | 下拉的最大高度 | String, Number | 320|
| clearable | 是否可清除 | Boolean | true|
| data | 节点数据 | Array | []|
| reader | 取值配对 | Object | {value: 'value',label: 'label',}|
| showIcon | 是否显示图标 | Boolean | true|
| showTitle | 是否显示tooltip | Boolean | true|
| multiple | 是否允许多选 | Boolean | false|
| url | 网络获取节点数据 | String ||
| service | 服务获取节点数据 | Object, Array, Function ||
| param | 查询参数 | Object, Array, Function ||
| dataFilter | 网络数据过滤器 | Function ||
| loadData |  | Function ||
| twiceClickSelected | 二次点击时，是否继续保持选中状态 |Boolean | true|
| headerFormat | 头部格式化 | Function ||
| doCheck | 被勾选后的情况，"p" 表示操作会影响父级节点； "s" 表示操作会影响子级节点。 | String | ps|
| doUnCheck | 取消勾选后的情况，"p" 表示操作会影响父级节点；"s" 表示操作会影响子级节点。 | String | ps|
| listStyle | 下拉面板样式 | Object | {} |
| collapseTags | 多选合并tags | Boolean | false |
| filterable | 搜索框检索 | Boolean | false |
| filterPlaceholder | 搜索框提示符 | String | 输入标题进行过滤 |
| filterNodeMethod | 过滤回调函数 | Function | 过滤label |
| onlyLeaf | 只有叶子结点的选择模式 | Boolean | false |
| leafName | 叶子结点 字段名称，根据数据字段判断是否可以选中（onlyLeaf: true 时生效） | String | |
| noHalf | 去除半选的选择模式 | Boolean | false |

