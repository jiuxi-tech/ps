[comment]: <> (fb-docs: docsify/fb-ui/03/tree/README.md)

# 树-Tree

## 基础用法

```html run {title:'通用树'}
<template>
<div>
	<fb-tree :data="nodes"/>
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

```html run {title:'多选树'}
<template>
<div>
	<fb-tree :data="nodes" multiple doCheck="ps" doUnCheck="ps"/>
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

### 手动选中

```html run {title:'selectNodeByValue方法：选择一个'}
<template>
<div style="height:160px">
<fb-tree v-model="selected"
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
			this.selected = node.value
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
<div style="height:160px">
<fb-tree v-model="selected"
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
			this.selected = nodes.map(row => row.value);
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
<div style="height:170px">
<fb-tree v-model="selected"
	:placeholder="'请选择'"
	:data="nodes"
	style="width: 200px"
	@on-select-change="handleSelectChange"
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
		handleSelectChange (node){
			this.selected = node.value
		},
	},
  }
</script>
<style>
</style>
```

### 过滤树

```html run {title:'试一试'}
<template>
<div style="height:370px">
<fb-card header="filter">
    <fb-input v-model="filterText" placeholder="输入标题进行过滤"></fb-input>
    <fb-divider></fb-divider>
    <fb-tree
        ref="treeFFF"
        :data="data"
        :filter-node-method="filterNode"
    ></fb-tree>

</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
        filterText: '',
      	selected:"",
      	nodes:[],
      	data:[
			{
				'value': '1',
				'label': '1',
				'desc': '第一级',
				'expand': false,
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
            {
				'value': '2',
				'label': '2',
				'desc': '第一级',
				'expand': false,
				'checked': false,
				'children': [
					{
						'value': '2-1',
						'label': '2-1',
						'desc': '第二级',
						'expand': true,
						'checked': false,
					},
                    {
						'value': '2-2',
						'label': '2-2',
						'desc': '第二级',
						'expand': true,
						'checked': false,
					},
				],
			},
		],
      }
    },
    watch: {
		filterText(val) {
			this.$refs.treeFFF.filter(val);
		}
	},
	methods: {
        filterNode(value, data) {
			// console.log(...arguments)
			if (!value) return true
			return data.label.indexOf(value) !== -1
		},
		handleClick (e) {
			this.nodes = this.data
			this.$message.success("加载完成",{align:'top'})
		},
		handleSelectChange (node){
			this.selected = node.value
		},
	},
  }
</script>
<style>
</style>
```

### 插槽节点--灵活的布局

```html run {title:'试一试'}
<template>
<div style="height:370px">
<fb-card header="slot">
    <fb-tree
        ref="treeFFF"
        :data="data"
        :show-icon="false"
    >
       <template #node="props">
            <fb-container>
                <fb-icon name="surveillance-camera-fill" color="green"></fb-icon>
                {{ props.node.label }} (0/100)
            </fb-container>
        </template> 
    </fb-tree>
</fb-card>
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
		handleSelectChange (node){
			this.selected = node.value
		},
	},
  }
</script>
<style>
</style>
```

### 动态加载--loadData
> loadData 承接函数 触发 (node, callback) => {service.then(res => callback(data)}
>
> 用回调函数承接异步数据
>
> 节点属性 dataLoaded:true 同时children:[], 树节点不会有 +号 显示


```html run {title:'试一试'}
<template>
<div style="height:370px">
<fb-card header="local data select"">
    <fb-tree
        ref="tree1"
        :data="data1"
        :load-data="loadData"
        @on-select-change="handleSelectChange"
    ></fb-tree>
</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	selected:"",
      	nodes:[],
      	data1: [
            {
                label: '中国',
                children: [],
            },
        ],
      }
    },
	methods: {
        loadData(item, callback) {
			setTimeout(() => {
				const data = [
					{
						label: '浙江',
						dataLoaded: true,
						children: [],
					}, {
						label: `children_${this.uid++}`,
						dataLoaded: false,
						children: null,
					},
				]

				if (this.uid > 2) {
					callback([])
				} else {
					callback(data)
				}

				// doSth

				setTimeout(()=>{

					debugger

					item.label = "asdas"

				}, 3000)

			}, 1000)
		},
		handleClick (e) {
			this.nodes = this.data
			this.$message.success("加载完成",{align:'top'})
		},
		handleSelectChange (node){
			this.selected = node.value
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
| on-select-change | 当选择项发生变更时触发，可以是方法名，或方法体 | (node) => void
| on-check-change | 当选择项发生变更时触发，可以是方法名，或方法体 | (nodes) => void
| on-data-change | 当加载数据发生变更时触发，可以是方法名，或方法体 | () => void
| on-data-update | 当加载数据发生变更时触发，可以是方法名，或方法体 | () => void
| on-data-load | 当数据通过网站加载时触发，可以是方法名，或方法体 | (event) => void

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| reader | 取值配对 | Object | {value: 'value',label: 'label',}
| showIcon | 是否显示图标 | Boolean | true
| showTitle | 是否显示tooltip | Boolean | true
| showLine | 是否显示网络线 | Boolean | false
| inline |  | Boolean | false
| multiple | 是否允许多选 | Boolean | false
| radio | 是否显示单选 | Boolean | false
| radioGroup | 单选分组范围 | String | level
| data | 节点数据 | Array | []
| url | 网络获取节点数据 | String |
| service | 服务获取节点数据 | Object, Array, Function |
| param | 查询参数 | Object, Array, Function |
| dataFilter | 网络数据过滤器 | Function |
| loadData |  | Function |
| height | 高度 | String, Number |
| twiceClickSelected | 二次点击时，是否继续保持选中状态 |Boolean | true
| twiceClickExpand | 二次点击时，是否展开子集 | Boolean | false
| doCheck | 被勾选后的情况，"p" 表示操作会影响父级节点； "s" 表示操作会影响子级节点。 | String | ps
| doUnCheck | 取消勾选后的情况，"p" 表示操作会影响父级节点；"s" 表示操作会影响子级节点。 | String | ps
| filterNodeMethod | 过滤回调函数 (配合实例filter使用) | Function | (value, node) => {}
| onlyLeaf | 只有叶子结点的选择模式 | Boolean | false |
| leafName | 叶子结点 字段名称，根据数据字段判断是否可以选中（onlyLeaf: true 时生效） | String | |
| noHalf | 去除半选的选择模式 | Boolean | false | 

## 插槽选项

| 名称 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------| 
| node | 节点容器位置 | html，组件 |  
