[comment]: <> (fb-docs: docsify/fb-ui/04/container/README.md)

# 容器-Container

用于展示各类内容，常用与首页内容模块的展示。

## 基础用法

```html run {title:'示例演示', open: false, row:false}
<template>
<div>
<fb-container>
	这里是内容区域
</fb-container>
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

### 数据统计展示

```html run {title:'试一试', open: false, row:false}
<template>
<div>
	<fb-container :background="colors.grey_1" padding="16px">
		<fb-row>
			<fb-col>
				<fb-container :background="colors.white" padding="12px">
					<fb-row>
						<fb-col span="24">
							<fb-container align="center" valign="center" height="20px">
								<fb-badge dot-size="12" dot :offset="[4, -2]" :dotColor="dotColor"></fb-badge>
								<fb-text :color="colors.text_emphasize">{{dotText}}</fb-text>
							</fb-container>
						</fb-col>
					</fb-row>
					<fb-row gutter="12">
						<fb-col span="12">
							<fb-container align="center" height="60px" :background="colors.red_1" radius="4px" mt="12px">
								<fb-container pt="6px">
									<fb-text :color="colors.text_emphasize">今日预警</fb-text>
								</fb-container>
								<fb-text :color="colors.text_emphasize" size="xl" bold>{{todayNum}}</fb-text>
							</fb-container>
						</fb-col>
						<fb-col span="12">
							<fb-container align="center" height="60px" :background="colors.red_1" radius="4px" mt="12px">
								<fb-container pt="6px">
									<fb-text :color="colors.text_emphasize">超期未处理</fb-text>
								</fb-container>
								<fb-text :color="colors.text_emphasize" size="xl" bold>{{noNum}}</fb-text>
							</fb-container>
						</fb-col>
					</fb-row>
				</fb-container>
			</fb-col>
		</fb-row>
	</fb-container>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	dotColor:"orange",
      	dotText:'橙色预警',
      	todayNum:"10",
      	noNum:"2"
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
| on-click | 当鼠标指针点击控件时触发，可以是方法名，或方法体 | (event) => void


## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| ellipsis | 文本超出是否显示省略号 | Boolean | false
| width | 元素宽度 | String |
| height | 元素高度 | String |
| margin | 简写[所有外边距](https://www.w3school.com.cn/cssref/pr_margin.asp)属性 | String |
| ml | 元素左外边距:marginLeft | String |
| mr | 元素右外边距:marginRight | String |
| mb | 元素底外边距:marginBottom | String |
| mt | 元素上外边距:marginTop | String |
| padding | 简写[所有内边距](https://www.w3school.com.cn/cssref/pr_padding.asp)属性 | String |
| pl | 元素左内边距:paddingLeft | String |
| pr | 元素右内边距:paddingRight | String |
| pb | 元素底内边距:paddingBottom | String |
| pt | 元素上内边距:paddingTop | String |
| align | 水平对齐方式 textAlign | String |
| valign | 垂直对齐方式，可选值：center | String |
| position | 定位方式 | String |
| display | 元素生成的框的类型 | String | block
| inline | 是否行内块元素，优先级 > display | Boolean | false
| top | 定位元素的上外边距边界与其包含块上边界之间的偏移 | String |
| left | 定位元素左外边距边界与其包含块左边界之间的偏移 | String |
| right | 定位元素右外边距边界与其包含块右边界之间的偏移 | String |
| bottom | 定位元素下外边距边界与其包含块下边界之间的偏移 | String |
| radius | 圆角 | String |
| color | 文本的颜色 | String |
| background | 简写所有的背景属性 | String |
| border | 简写所有的边框属性 | String |
| zIndex | 元素的堆叠顺序 | String |
| overflow | 规定当内容溢出元素框时发生的事情 | String |
| overflowX | 是否对内容的左/右边缘进行裁剪 - 如果溢出元素内容区域的话 | String |
| overflowY | 是否对内容的上/下边缘进行裁剪 - 如果溢出元素内容区域的话 | String |
| maxHeight | 元素的最大高度 | String |
| minHeight | 元素的最小高度 | String |
| maxWidth | 元素的最大宽度 | String |
| minWidth | 元素的最小宽度 | String |
| cursor | 显示的光标的[类型（形状）](https://www.w3school.com.cn/cssref/pr_class_cursor.asp) | String |
