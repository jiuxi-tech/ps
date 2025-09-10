[comment]: <> (fb-docs: docsify/fb-ui/04/row/README.md)

# 栅格布局 row col

24 等份布局格式 百分比布局

## 基础用法

```html run {title:'示例演示'}
<template>
<div>
    <fb-card header="基础">
        <fb-row gutter="16">
			<fb-col span="6" v-for="item in 4" :key="item">
				<fb-card>6份-{{item}} col</fb-card>
			</fb-col>
		</fb-row>
        <br/>
        <br/>
        <fb-row gutter="16" vertical-gutter="16">
			<fb-col span="6" v-for="item in 11" :key="item">
				<fb-card>6份-{{item}} col 上下左右间隔16px</fb-card>
			</fb-col>
		</fb-row>
    </fb-card>
    <fb-card header="间隔">
        <fb-row gutter="16">
			<fb-col offset="6" span="6" v-for="item in 4" :key="item">
				<fb-card>6份-{{item}} col</fb-card>
			</fb-col>
		</fb-row>
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
```

## flex用法

```html run {title:'示例演示'}
<template>
<div>
    <fb-card header="flex用法">
        <fb-row flex justify="revert" align="middle" style="height: 200px;">
			<fb-col span="6" v-for="item in 4" :key="item">
				<fb-card>6份-{{item}} col</fb-card>
			</fb-col>
		</fb-row>
        <br/>
        <br/>
        <fb-row flex gutter="16" vertical-gutter="16">
			<fb-col flex="1" span="6" v-for="item in 11" :key="item">
				<fb-card>6份-{{item}} col 上下左右间隔16px</fb-card>
			</fb-col>
		</fb-row>
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
```

## row属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| gutter | 间距  | Number, String | 0 |
| verticalGutter | 垂直间距 | Number, String | 0 |
| flex | 开启flex布局 ie10以下不支持 | Boolean | false |
| justify | flex布局 css3 justify-content | String | start |
| align | flex布局 css3 align-items | String | top |

## col属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| span | 拼接, 可选值 从 1-24, 24 的意思是一行占24格, 也就满了  | Number, String | 8 |
| offset | 向右偏移值 可选值 从 1-24, 比如 fb-col span=1 offset=23 那这个格子就在最右边了 | Number, String | undefined |
| order | flex 时 每个col的 排序值, 从小到大排列 | String, Number | undefined |
| flex | flex布局 子项布局 | String, Number | undefined |

