[comment]: <> (fb-docs: docsify/fb-ui/04/avatar/README.md)

# 头像-Avatar
用来表示事物或者用户，支持图片及图标

## 基础用法

提供三种尺寸的头像供不同场景选择

```html run {title:'示例演示'}
<template>
<div>
控件类型：
<fb-avatar text="文本"/>
<fb-avatar icon="user"/>
<fb-avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" alt="图片"/>
<fb-avatar icon="user" circle alt="圆形显示"/>

<br/><br/>
扩展类型：
<fb-avatar>
	<fb-icon name="user"/>
</fb-avatar>
<fb-avatar style="color: #fde3cf; backgroundColor: #f56a00">
自定义
</fb-avatar>

<br/><br/>
控件样式：
<fb-avatar icon="user" color="yellow"/>
<fb-avatar icon="user" style="backgroundColor:#87d068" />
<fb-avatar text="文本" color="yellow"/>
<fb-avatar text="文本" style="backgroundColor:#87d068" />

<br/><br/>
控件大小：
<fb-space size="16">
	<fb-avatar size="s" icon="user"/>
	<fb-avatar size="m" icon="user"/>
	<fb-avatar size="l" icon="user"/>
	<fb-avatar size="xl" icon="user"/>
</fb-space>

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
| icon | 内部显示的小图标 | String   |
| text | 内部显示的文本 | String, Number |
| src | 内部显示的图版地址 | String |
| alt | 指定图像不能正常显示（网速慢、图片链接错误）后显示的替换文本 | String |
| size | 尺寸 可选值是 s、m、l、xl | String, Number | m
| circle | 是否圆形 | Boolean | false
| color | 字体颜色 | String |
| backgroundColor | 背景颜色 | String |
