[comment]: <> (fb-docs: docsify/fb-ui/04/text/README.md)

# 文本-Text

## 基础用法

```html run {title:'示例演示'}
<template>
<div>

控件形态：
<fb-text>普通文本</fb-text>

<br/><br/>
控件大小：
<fb-text size="s">小</fb-text>
<fb-text size="m">中</fb-text>
<fb-text size="l">大</fb-text>
<fb-text size="xl">超大</fb-text>
<fb-text size="xxl">超超大</fb-text>
<fb-text size="xxxl">超超超大</fb-text>

<br/><br/>
控件状态：
<fb-text type="primary">主要</fb-text>
<fb-text type="success">成功</fb-text>
<fb-text type="warning">警告</fb-text>
<fb-text type="info">消息</fb-text>
<fb-text type="danger">危险</fb-text>

<br/><br/>
自定义：
<fb-text color="red">主要</fb-text>
<fb-text color="red" bold>加粗</fb-text>
<fb-text color="red" weight="bolder">加粗</fb-text>
<fb-text color="orange" width="87px" ellipsis>显示省略号，超出范围了</fb-text>


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

## 事件列表

| 事件名   | 说明     | 返回值 |      |
| :------- | :------- | :----- | :--- |
| on-click | 单击事件 |        |      |



## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| size | 尺寸 可选值是 s、m、l、xl、xxl、xxxl, 数字、%、px、s,m,l等 | String, Number ||
| type | 状态，可选值：primary，success，warning，info，danger| String ||
| color | 字体颜色 | String ||
| ellipsis | 文本超出是否显示省略号 | Boolean | false|
| width | 控件宽度，支持 px | String, Number ||
| bold | 文本粗体字符 | Boolean | false|
| weight | [文本的粗细](https://www.w3school.com.cn/cssref/pr_font-weight.asp) ，优先级 > bold| String, Number | false|
| align | 水平对齐方式 textAlign | String ||
| family | 字体类型 | String ||
| mr | marginRight 右边距                                           | String, Number |        |
| ml | marginLeft 左边距 | String, Number ||

