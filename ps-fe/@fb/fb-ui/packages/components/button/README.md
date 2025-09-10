[comment]: <> (fb-docs:docsify/fb-ui/01/button/README.md)

# 按钮-Button
按钮是用于开始一个操作。

## 基础用法

提供了以下几种不同类型的按钮；
-	主按钮：用于一个页面上的最主要且为主要业务导向的按钮，一个页面上`只能有一个`主按钮；
-	次按钮：用于除却主按钮之外的同级业务按钮；

提供了以下几种不同状态的按钮；
-	危险按钮：删除/移动/修改权限等危险操作时使用，一般需要二次确认才能执行操作；
-	禁用按钮：用于当前受限不可用时使用，一般需要文案解释；
-	加载按钮：用于异步操作等待反馈时使用，也可避免多次提交；  

### 主按钮

```html run {title:'示例演示'}
<template>
<div>
按钮类型：
	<fb-button type="primary">主按钮</fb-button>
	<br/>
	<br/>
图标按钮：
	<fb-button type="primary" icon="search" ></fb-button>
	<fb-button type="primary" icon="search" >图标在左</fb-button>
	<fb-button type="primary">图标在右
		<fb-icon name="search" size="l"></fb-icon>
	</fb-button>
	<br/>
	<br/>

按钮尺寸：
	<fb-button type="primary" size="l">大</fb-button>
	<fb-button type="primary" size="m">中</fb-button>
	<fb-button type="primary" size="s">小</fb-button>
	<br/>
	<br/>

圆角按钮：
	<fb-button type="primary" round>我是圆角</fb-button>
	<fb-button type="primary" icon="fb-icon-information"  round></fb-button>
	<fb-button type="primary" icon="fb-icon-information"  round>我是圆角</fb-button>
	<br/>
	<br/>

按钮状态：
	<fb-button  type="primary" disabled>不可用状态</fb-button>
	<fb-button  type="primary" loading>加载中状态</fb-button>
	<fb-button  type="primary" danger>危险状态</fb-button>
	<fb-button  type="primary" disabled loading danger>禁用-加载-危险状态</fb-button>
	<fb-button  type="primary" loading danger>加载-危险状态</fb-button>
	<fb-button  type="primary" disabled loading>禁用-加载状态</fb-button>
	<fb-button  type="primary" disabled danger>禁用-危险状态</fb-button>
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

### 次按钮

```html run {title:'示例演示'}
<template>
<div>
按钮类型：
	<fb-button>次按钮</fb-button>
	<br/>
	<br/>
图标按钮：
	<fb-button icon="search" ></fb-button>
	<fb-button icon="search" >图标在左</fb-button>
	<fb-button>图标在右
		<fb-icon name="search" size="l"></fb-icon>
	</fb-button>
	<br/>
	<br/>

按钮尺寸：
	<fb-button size="l">大</fb-button>
	<fb-button size="m">中</fb-button>
	<fb-button size="s">小</fb-button>
	<br/>
	<br/>

圆角按钮：
	<fb-button round>我是圆角</fb-button>
	<fb-button icon="fb-icon-information"  round></fb-button>
	<fb-button icon="fb-icon-information"  round>我是圆角</fb-button>
	<br/>
	<br/>

按钮状态：
	<fb-button  disabled>不可用状态</fb-button>
	<fb-button  loading>加载中状态</fb-button>
	<fb-button  danger>危险状态</fb-button>
	<fb-button  disabled loading danger>禁用-加载-危险状态</fb-button>
	<fb-button  loading danger>加载-危险状态</fb-button>
	<fb-button  disabled loading>禁用-加载状态</fb-button>
	<fb-button  disabled danger>禁用-危险状态</fb-button>
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

## 更多用法

### 宽度自适应
long属性，可以设置按钮的宽度100%

```html run {title:'试一试'}
<template>
<div>
	<fb-button @on-click="handleClick" :long="long">{{longtxt}}</fb-button>
	<br/>
	<br/>
	<fb-button @on-click="handleClick" :long="long" type="primary" >{{longtxt}}</fb-button>
</div>
</template>
<script>
  export default {
    data () {
      return {
		long: true,
		longtxt:"点击缩小",
      }
    },
	methods: {
		handleClick () {
			this.long = !this.long
			this.longtxt = this.long?"点击缩小":"点击放大"
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
| on-click | 当鼠标指针点击按钮时触发，可以是方法名，或方法体 | (event) => void

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| type | 类型，可选值是 primary、link、default | String  | default|
| size | 尺寸 可选值是 s、m、l | String | m|
| long | 宽度，是否显示为100% | Boolean   | false|
| icon | 内部左侧显示的小图标 | String   ||
| round | 边框为圆角 | Boolean | false|
| loading | 加载状态 | Boolean | false|
| danger | 危险状态 | Boolean | false|
| disabled | 禁用状态 | Boolean  | false|
| autofocus | 自动获取焦点 |  ||
| appendIcon | 右边图标 | String ||

