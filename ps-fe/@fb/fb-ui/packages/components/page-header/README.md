[comment]: <> (fb-docs: docsify/fb-ui/02/page-header/README.md)

# 页头-PageHeader

位于页面顶部，一般是页面的主体内容，包括标题、内容，也可以展示页面级操作。

## 基础用法

- 一般在弹窗中出现，信息内容过多，抽出主体内容让用户更清楚当前页面的层级关系；

```html run {title:'示例演示'}
<template>
<div>
	<fb-page-header title="标题描述" subTitle="副标题">
	主体内容
	</fb-page-header>

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
| title | 显示的标题 | String, Number |
| subTitle | 显示的副标题 | String, Number |
