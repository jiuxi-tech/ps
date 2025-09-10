[comment]: <> (fb-docs: docsify/fb-ui/04/badge/README.md)

# 徽标数-Badge
用于显示需要处理的消息条数或者状态的变化

## 基础用法

一般显示在通知图标或者头像的右上角；

提供三种形态【数字】、【圆点】、【文字】三种；
- 数字：当需要吸引用户注意且需要明确指明消息数量时使用；
- 圆点：当只需要用户清楚有变化，不需要强提醒用户变化的具体内容时使用；
- 文字：只是在当前页明确表明某一种状态时使用；

```html run {title:'示例演示'}
<template>
<div style="padding-top: 10px;">
数字形态：
<fb-badge :count="9" :offset="[-2,-8]">待办</fb-badge>

<span style="padding-left:10px"/>

<fb-badge :count="9" :offset="[-2,-8]"  type="processing" >待办</fb-badge>

<span style="padding-left:10px"/>

<fb-badge :count="19" overflowCount="9" :offset="[-2,-8]">待办</fb-badge>

<br/><br/>
圆点形态：
<fb-badge :dot="true" dotColor="red" >待办</fb-badge>

<span style="padding-left:10px"/>

<fb-badge>
	<fb-avatar>待办</fb-avatar>
	<fb-icon  slot="count" name="notice" style="color: red;"></fb-icon>
</fb-badge>


<br/><br/>
文字形态：
<fb-badge dot dot-size="18" ></fb-badge>

<span style="padding-left:10px"/>

<fb-badge count="9"></fb-badge>

<span style="padding-left:10px"/>

<fb-badge :count="19" overflowCount="9"></fb-badge>


<br/><br/>
控件状态：
<fb-badge count="9" type="success" title="success"></fb-badge>

<span style="padding-left:10px"/>
<fb-badge count="9" type="processing" title="processing"></fb-badge>

<span style="padding-left:10px"/>
<fb-badge count="9" type="default" title="default"></fb-badge>

<span style="padding-left:10px"/>
<fb-badge count="9" type="error" title="error"></fb-badge>

<span style="padding-left:10px"/>
<fb-badge count="9" type="warning" title="warning"></fb-badge>

<span style="padding-left:10px"/>
<fb-badge count="9" type="text" title="text"></fb-badge>



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

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| count | 展示的数字 | Number, String |
| countSize | 展示的数字 型号 | String |
| dot | 展示的小圆点 | Boolean | false
| dotSize | 小圆点大小 | Number, String | 6 |
| dotColor | 自定义小圆点的颜色 | String |
| offset | 设置状态点的位置偏移，格式为 [x, y] | Array | () => [0, 0]
| overflowCount | 展示封顶的数字值 | Number, String | 99
| showZero | 当数值为 0 时，是否展示 Badge | Boolean | false
| type | 控件状态，可选值：success,processing,default,error,warning,text| String |
| numberStyle | 数字样式自定义 | Object |
| title | 设置鼠标放在状态点上时显示的文字 | String |
