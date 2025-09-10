[comment]: <> (fb-docs: docsify/fb-ui/01/link/README.md)

# 链接-Link

用于外链且不重要的备选链接。

## 基础用法

### 链接

```html run {title:'示例演示'}
<template>
<div>
按钮类型：
	<fb-link label="链接按钮"></fb-link>
	<br/>
	<br/>

图标链接：
	<fb-link icon="search" action></fb-link>
	<fb-link label="图标在左" icon="search"></fb-link>
	<br/>
	<br/>

链接状态：
	<fb-link label="主要" type="primary"></fb-link>
	<fb-link label="危险" type="danger"></fb-link>
	<fb-link label="成功" type="success"></fb-link>
	<fb-link label="警告" type="warn"></fb-link>
	<fb-link label="禁用" disabled href="http://www.baidu.com"></fb-link>
	<br/>
	<br/>

链接大小：
	<fb-link label="较小" size="xs">大</fb-link>
	<fb-link label="小" size="s"></fb-link>
	<fb-link label="中" size="m"></fb-link>
	<fb-link label="大" size="l"></fb-link>
	<fb-link label="较大" size="xl"></fb-link>
	<fb-link label="超大" size="xxl"></fb-link>
	<br/>
	<br/>

定义颜色：
	<fb-link label="红色" color="red"></fb-link>
	<fb-link label="橙色" color="orange"></fb-link>
	<fb-link label="蓝色" color="blue"></fb-link>
	<fb-link label="黄色" color="yellow"></fb-link>
	<br/>
	<br/>

下  划  线：
	<fb-link label="有下划线" underline="on"></fb-link>
	<fb-link label="无下划线" underline="off"></fb-link>
	<fb-link label="移上来就有" underline="hover"></fb-link>
	<br/>
	<br/>

消息提示：
	<fb-link label="查看提示" title="我是消息提示" underline="hover"></fb-link>
	<br/>
	<br/>

链接跳转：
	<fb-link label="打开百度" href="http://www.baidu.com"></fb-link>
	<fb-link label="新窗口打开" href="http://www.baidu.com" target="blank"></fb-link>
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
  }
</script>
<style>
</style>
```

### 链接组

```html run {title:'示例演示'}
<template>
<div>
	<fb-card header="默认分隔符">
		<fb-link-group>
			<fb-link label="查看"></fb-link>
			<fb-link label="修改"></fb-link>
			<fb-link label="测试"></fb-link>
		</fb-link-group>
	</fb-card>
	<fb-card header="定制分隔符">
		<fb-link-group separator="--">
			<fb-link label="查看"></fb-link>
			<fb-link label="修改"></fb-link>
			<fb-link label="测试"></fb-link>
		</fb-link-group>
	</fb-card>
	<fb-card header="个性分隔符">
		<fb-link-group separator="/">
			<fb-link label="查看"></fb-link>
			<fb-link label="修改" separator="~~"></fb-link>
			<fb-link label="测试"></fb-link>
		</fb-link-group>
	</fb-card>
	<fb-card header="动态绑定">
		<fb-link-group :data="links"></fb-link-group>
	</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
				links: [
					{
						icon: 'left-circle',
						to: '/xxx',
						title: '仅图标',
						color: 'red',
					},
					{
						label: '路由跳转',
						to: '/xxx',
						color: 'green',
					},
					{
						label: '分隔符',
						to: '/xxx',
						color: 'red',
						separator:'/',
					},
					{
						label: '链接跳转',
						href: 'http://www.baidu.com',
						target:"_blank",
						color: 'blue',
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

### 调用方法

click属性，可以设置链接点击时的回调方法

```html run {title:'试一试'}
<template>
<div>
在表格外：
	<br/>
	<br/>
	1. 绑定方法体：<fb-link label="点击这里" :click="()=>handleClick()"></fb-link>
	<br/>
	<br/>
	2. 绑定方法名：<fb-link label="点击这里" :click="handleClick"></fb-link>
	<br/>
	<br/>
	<br/>
在表格里：
	<br/>
	<br/>
	1. 绑定方法体：<fb-link label="点击这里" :click="()=>handleClick()"></fb-link>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	i:0,
      }
    },
	methods: {
		handleClick () {
			this.$message.success('你点中了我 '+(++this.i)+' 次！', {time: 1000})
		}
	},
  }
</script>
<style>
</style>
```

## 属性列表

| 属性 | 说明 | 类型 | 默认值 | 通用链接 | 操作链接 | 网页跳转 | 路由跳转 | 链接组 |
|:-----|:----|:-----|:-------| ------- | -------- | ------- | ------- | ------ |
| label | 显示的文本 | String | null | √ | √ | √ | √ |
| action | 表示为一个动作 | String | null |  | √ |  |  |
| icon | 内部左侧显示的小图标 | String | null | √ | √ | √ | √ |
| size | 尺寸大小 可选值是 xs、s、m、l、xl、xxl | m |  | √ | √ | √ | √ |
| type | 显示类型，可选值是 primary、danger、success、warn | String | null | √ | √ | √ | √ |
| title | 提示的消息文本 | String | null | √ | √ | √ | √ |
| color | 显示的颜色 | String | null | √ | √ | √ | √ |
| underline | 下划线形式，可选值是：on,off,hover| String | hover | √ | √ | √ | √ |
| disabled | 禁用状态 | Boolean | false | √ | √ | √ | √ |
| click | 点击时的回调方法，可以是方法名，或方法体 | () => void |  | √ | √ | |  |  |
| href | 要跳转的网页地址 | String | null |  |  | √ |  |
| target | 跳转的目标，如：_blank,_parent,_self,_top | String | null |  |  | √ |  |
| to | 目标路由的请求地址，该值可以是一个字符串，也可以是动态绑定的描述目标位置的对象 | String, Object | null |  |  |  | √ |
| tag | 渲染后的标签类型 | String | a |  |  |  | √ |
| replace | 当点击时，会调用 router.replace() 而不是 router.push()，导航不会留下 history 记录 | Boolean | false |  |  |  | √ |
| append | 在当前 (相对) 路径前添加基路径 | Boolean | false |  |  |  | √ |
| separator | 控件的分隔符，仅在链接组中有效 | String | null |  |  |  |  | √
