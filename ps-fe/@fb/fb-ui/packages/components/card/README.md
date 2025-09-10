[comment]: <> (fb-docs: docsify/fb-ui/04/card/README.md)

# 卡片-Card

用于展示各类内容，常用与首页内容模块的展示。

## 基础用法

一般包含标题及内容；

- 标题可以承载一般标题及【标签页-tabs】控件；
- 内容可以承载列表、图表以及自定义内容；

```html run {title:'示例演示', open: false, row:false}
<template>
<div>
	<fb-card header="这里是标题区域">
		<template slot="actions">
			这里是操作区域
		</template>
		<div>
			这里是内容区域
		</div>
	</fb-card>
	<br/>
	<fb-card>
		<div slot="title">
			这里是标题区域
		</div>
		<div slot="actions">
			这里是操作区域
		</div>
		<div>
			这里是内容区域
		</div>
	</fb-card>
	<br/>
	<fb-card height="200">
		<div slot="title">
			这里是标题区域
		</div>
		<div slot="actions">
			这里是操作区域
		</div>
		<div>
			我是有高度的
		</div>
	</fb-card>
	<br/>
	<fb-card no-border shadow="on">
		<div slot="title">
			无边框，开启阴影
		</div>
		<div slot="actions">
			这里是操作区域
		</div>
		<div>
			这里是内容区域
		</div>
	</fb-card>
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

### 与栅格结合

```html run {title:'试一试', open: false, row:false}
<template>
<div>
	<fb-row>
		<fb-col>
			<fb-card no-border shadow="on">
				<div slot="title">
					与栅格结合  1
				</div>
				<div slot="actions">
					<fb-button type="link">更多</fb-button>
				</div>
				<div>
					<ul>
						<li v-for="i in [1,2,3,4,5]">关于什么什么的经验分享</li>
					</ul>
				</div>
			</fb-card>

		</fb-col>
		<fb-col>
			<fb-card no-border shadow="on">
				<div slot="title">
					与栅格结合  2
				</div>
				<div slot="actions">
					<fb-button type="link">更多</fb-button>
				</div>
				<div>
					<ul>
						<li v-for="i in [1,2,3,4,5]">关于什么什么的经验分享</li>
					</ul>
				</div>
			</fb-card>
		</fb-col>
		<fb-col>
			<fb-card no-border shadow="on">
				<div slot="title">
					与栅格结合  3
				</div>
				<div slot="actions">
					<fb-button type="link">更多</fb-button>
				</div>
				<div>
					<ul>
						<li v-for="i in [1,2,3,4,5]">关于什么什么的经验分享</li>
					</ul>
				</div>
			</fb-card>
		</fb-col>
	</fb-row>
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


### 筛选卡片

```html run {title:'示例演示', open: false, row:false}
<template>
<div>
	<fb-card header="标题区域">
		<template slot="actions">
			<fb-radio-group
				v-model="group"
				button
				:data="[{value: '1', label: '本年'}, {value: '2', label: '本月'}, {value: '3', label: '本季'}]"
			/>
		</template>
		<div>
			<ul v-if="group == '1'">
				<li v-for="i in [11,12,13,14,15]">{{i}}-关于什么什么的经验分享</li>
			</ul>
			<ul v-if="group == '2'">
				<li v-for="i in [21,22,23,24,25]">{{i}}-关于什么什么的经验分享</li>
			</ul>
			<ul v-if="group == '3'">
				<li v-for="i in [31,32,33,34,35]">{{i}}-关于什么什么的经验分享</li>
			</ul>
		</div>
	</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	group:"1"
      }
    },
  }
</script>
<style>
</style>
```


### bodyStyle 滚动条

```html run {title:'示例演示', open: false, row:false}
<template>
<div>
	<fb-card header="标题区域" height="100" :body-style="{overflow: 'auto'}">
		<template slot="actions">
			<fb-radio-group
				v-model="group"
				button
				:data="[{value: '1', label: '本年'}, {value: '2', label: '本月'}, {value: '3', label: '本季'}]"
			/>
		</template>
		<div>
			<ul v-if="group == '1'">
				<li v-for="i in [11,12,13,14,15]">{{i}}-关于什么什么的经验分享</li>
			</ul>
			<ul v-if="group == '2'">
				<li v-for="i in [21,22,23,24,25]">{{i}}-关于什么什么的经验分享</li>
			</ul>
			<ul v-if="group == '3'">
				<li v-for="i in [31,32,33,34,35]">{{i}}-关于什么什么的经验分享</li>
			</ul>
		</div>
	</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	group:"1"
      }
    },
  }
</script>
<style>
</style>
```


## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| header | 标题 | String, Number |
| shadow | 可选值：on, off, hover | String | hover
| height | 高度，包含标题 | String, Number |
| noBorder | 是否显示边框 | Boolean | false
| noPadding | 是否显示内边距 | Boolean | false
| bodyStyle | card-body样式 | Object | 例：{overflow: 'auto'}
