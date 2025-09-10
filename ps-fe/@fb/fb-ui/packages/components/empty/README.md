[comment]: <> (fb-docs: docsify/fb-ui/05/empty/README.md)

# 空状态-Empty
空状态时的展示占位符。

## 基础用法
- 当页面或者控件没有数据时，用于显示的用户提示

```html run {title:'示例演示-type'}
<template>
<div>
    <fb-empty></fb-empty>
    <fb-divider></fb-divider>
    <fb-empty type="notice" text="无相关类型通知"></fb-empty>
    <fb-divider></fb-divider>
    <fb-empty type="todo" text="暂无列表数据"></fb-empty>
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
```

```html run {title:'示例演示-size'}
<template>
<div>
    <fb-empty size="s"></fb-empty>
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
.jpx-empty--s .jpx-empty__image {
    background-size: contain;
}

</style>
```

## 更多用法
```html run {title:'示例演示-插槽'}
<template>
<div>
    <fb-empty>
        <fb-button>刷新</fb-button>
    </fb-empty>

    <fb-divider>这是分隔线</fb-divider>

    <fb-empty>
        <template slot="text">
            <fb-divider>暂无数据</fb-divider>
            <fb-button type="primary" style="margin-right: 8px">刷新</fb-button>
            <fb-button>返回</fb-button>
        </template>
    </fb-empty>
</div>
</template>
```

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| size | 尺寸大小 可选值是 s、m | String | m |
| type | 控件类型 可选值是 data、notice、todo | String | data |
| text | 无数据时文字说明 | String | 暂无数据 |
