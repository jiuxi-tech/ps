[comment]: <> (fb-docs: docsify/fb-ui/03/select/README.md)

# 选择器-Select
点击从控件弹出框中选择目标选项，然后选择弹窗消失，选择的内容显示在框中。

## 基础用法

- 下拉单选框

从控件弹出框中选择单个目标选项
1. 当选项少时（少于5项），建议直接使用【单选框-Radio】控件平铺出来；
2. 当选项多时（超过20项），建议使用带搜索的下拉框；


- 下拉多选框

从控件弹出框中选择多个目标选项

1. 当选项少时（少于5项），建议直接使用【多选框-Checkbox】控件平铺出来；
2. 当选项多时（超过20项），建议使用带搜索的下拉框；

```html run {title:'示例演示'}
<template>
<div>
控件类型：
<br/><br/>
<fb-select :data="nodes" placeholder="下拉框只读" width="30%" readonly></fb-select>
<br/><br/>
<fb-select :data="nodes" placeholder="下拉框禁用" width="30%" disabled></fb-select>
<br/><br/>
<fb-select :data="nodes" placeholder="下拉单选框"/>
<br/><br/>
<fb-select :data="nodes" placeholder="不带内置'清除'按钮" :clearable="false"/>
<br/><br/>
<fb-select :data="nodes" placeholder="带内置搜索框" filterable/>
<br/><br/>
<fb-select :data="nodes" placeholder="下拉多选框" multiple/>

<br/><br/>
控件样式：

<br/><br/>
<fb-select :data="nodes" placeholder="自定义宽度：30%" style="width: 30%"></fb-select>
<fb-select :data="nodes" placeholder="自定义宽度：150px" style="width: 150px"></fb-select>

</div>
</template>
<script>
  export default {
    data () {
      return {
      	nodes:[
			{value: true,label: '是',},
			{value: false,	label: '否',},
		]
      }
    },
  }
</script>
<style>
</style>
```



## 更多用法

### 级联选择框

```html run {title:'试一试'}
<template>
<div style="height:100px">
<fb-select
	   :placeholder="'第一级'"
	   :data="first"
	   :reader="{value:'id', label: 'name'}"
	   clearable
	   @on-change="handleChangeFirst"
	   style="width: 200px"
/>
<fb-select v-show="second.length > 0"
	   :placeholder="'第二级'"
	   :data="second"
	   :reader="{value:'id', label: 'name'}"
	   clearable
	   @on-change="handleChangeSecond"
	   style="width: 200px"
/>
<fb-select v-show="third.length > 0"
	   :placeholder="'第三级'"
	   :data="third"
	   :reader="{value:'id', label: 'name'}"
	   clearable
	   @on-change="handleChangeThird"
	   style="width: 200px"
/>
<br/><br/>
当前选择值：{{value}}
</div>
</template>
<script>
  export default {
    data () {
      return {
      	value:'',
      	first:[
			{id: "1", name:"选项1",},
			{id: "2", name:"选项2",},
      	],
      	second:[],
      	third:[],
      }
    },
	methods: {
		handleChangeFirst (value) {
			this.second  = []
			if(value){
				this.value = value
				this.second = {
					"1":[
						{id: "1-1", name:"选项1-1",},
						{id: "1-2", name:"选项1-2",},
					],
					"2":[
						{id: "2-1", name:"选项2-1",},
						{id: "2-2", name:"选项2-2",},
					],
				}[value]
			}
		},
		handleChangeSecond (value) {
			this.third  = []
			if(value){
				this.value = value
				this.third = {
					"1-1":[
						{id: "1-1-1", name:"选项1-1-1",},
						{id: "1-1-2", name:"选项1-1-2",},
					],
					"1-2":[
						{id: "1-2-1", name:"选项1-2-1",},
						{id: "1-2-2", name:"选项1-2-2",},
					],
					"2-1":[
						{id: "2-1-1", name:"选项2-1-1",},
						{id: "2-1-2", name:"选项2-1-2",},
					],
					"2-2":[
						{id: "2-2-1", name:"选项2-2-1",},
						{id: "2-2-2", name:"选项2-2-2",},
					],
				}[value]
			}
		},
		handleChangeThird (value) {
			if(value){
				this.value = value
			}
		},
	},
  }
</script>
<style>
</style>
```

### 动态加载数据

```html run {title:'试一试'}
<template>
<div style="height:130px">
<fb-select v-model="selected"
   :placeholder="'请选择'"
   :service="xxService.list"
   :dataFilter="dataFilter"
   :reader="{value:'userId', label: 'userName'}"
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
			dataFilter:function(json){
				return json;
			},
			xxService: {
				list () {
						return new Promise((resolve, reject) => {
							setTimeout(() => {
								resolve(
									{
										'data': [
											{userId: '11',userName: '选项11',userGander: '1',userTel: '16722299',},
											{userId: '12',userName: '选项12',userGander: '1',userTel: '16722299',},
											{userId: '13',userName: '选项13',userGander: '1',userTel: '16722299',},
										]
									},
								)
							}, 1000)
						})
				},
			},
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
<fb-select v-model="selected"
	:placeholder="'请选择'"
	:data="nodes"
	style="width: 200px"
/>
<br/><br/>
当前选择值：{{selected}}
<br/><br/>
<fb-button @on-click="handleClick">点击选中 否</fb-button>

</div>
</template>
<script>
  export default {
    data () {
      return {
      	selected:"",
      	nodes:[
			{value: true,label: '是',},
			{value: false,	label: '否',},
		],
      }
    },
	methods: {
		handleClick (e) {
			this.selected = false
		},
	},
  }
</script>
<style>
</style>
```


### 多选折叠选项

```html run {title:'试一试'}
<template>
<div style="height:130px">
<fb-select v-model="selected"
   :placeholder="'请选择'"
   :service="xxService.list"
   :dataFilter="dataFilter"
   :reader="{value:'userId', label: 'userName'}"
   style="width: 200px"
    multiple
	collapse-tags
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
			dataFilter:function(json){
				return json;
			},
			xxService: {
				list () {
						return new Promise((resolve, reject) => {
							setTimeout(() => {
								resolve(
									{
										'data': [
											{userId: '11',userName: '选项11',userGander: '1',userTel: '16722299',},
											{userId: '12',userName: '选项12',userGander: '1',userTel: '16722299',},
											{userId: '13',userName: '选项13',userGander: '1',userTel: '16722299',},
										]
									},
								)
							}, 1000)
						})
				},
			},
      }
	},
  }
</script>
<style>
</style>
```

## 事件列表

| 事件名 | 参数 | 说明 | 返回值 |
|:-------|:----|:-------|--------|
| on-change | 单选模式下 value, node<br />多选模式下 values, nodes | 当选择项发生变更时触发，可以是方法名，或方法体 | (val) => void|
| on-search | searchValue | 当搜索值发生变更时触发，可以是方法名，或方法体 | (val) => void|
| on-blur |  | 当失去焦点时触发，可以是方法名，或方法体 | (event) => void|
| on-focus |  | 当获取焦点时触发，可以是方法名，或方法体 | (event) => void|
| on-click |  | 当鼠标指针点击按钮时触发，可以是方法名，或方法体 | (event) => void|
| on-finished | currentValue | 当选择项发生变更时触发，可以是方法名，或方法体 | (val) => void|
| on-data-load | data | 当数据通过网站加载时触发，可以是方法名，或方法体 | (event) => void|
| on-data-update |  |  | |
| ~~on-pager-change~~ |  |  | |

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 绑定值 | String, Number, Array ||
| placeholder | 占位提示 | String ||
| disabled | 禁用状态 | Boolean | false|
| readonly | 只读状态 | Boolean | false|
| filterable | 是否显示搜索框 | Boolean | false|
| clearable | 是否可清除 | Boolean | true|
| multiple | 是否允许多选 | Boolean | false|
| multipleLimit | 最多选几个, 0不限制 | Number | 0|
| data | 节点数据 | Array | []|
| url | 网络获取节点数据 | String ||
| service | 服务获取节点数据 | Object, Array, Function ||
| param | 查询参数 | Object, Array, Function ||
| dataFilter | 网络数据过滤器 | Function ||
| reader | 取值配对 | Object | {value: 'value',label: 'label',}|
| autoLoad | 自动加载数据 | Boolean | true|
| size | 尺寸 可在 s m l xl 间切换 | String | m |
| showPager |  |  | |
| pager |  |  | |
| autoPager |  |  | |
| selectOptionClass | 选项扩展类名 | Object, Array | |
| selectOptionStyle | 选项扩展样式 | Object, Array | |
| collapseTags | 多选合并tags | Boolean | false |
