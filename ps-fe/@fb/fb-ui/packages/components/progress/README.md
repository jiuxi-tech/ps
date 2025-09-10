[comment]: <> (fb-docs: docsify/fb-ui/05/progress/README.md)

# 进度-Progress
用于反馈一系列操作后的结果。

## 基础用法
- 进度显示；

```html run {title:'示例演示'}
<template>
<div>
    <fb-progress percent="30"></fb-progress>
    <br/>
    <fb-progress percent="50" status="active"></fb-progress>
    <br/>
    <fb-progress percent="60" status="success"></fb-progress>
    <br/>
    <fb-progress percent="70" status="warning"></fb-progress>
    <br/>
    <fb-progress percent="80" status="danger"></fb-progress>
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
```

## 更多用法
```html run {title:'示例演示-自定义'}
<template>
<div>
    <fb-cardx header="定制" no-header-border>
        <fb-progress percent="60" status="active" stroke-width="50" strokeBgColor="transparent" :format="format" border-radius="4" text-inside>
            <div slot="text">
                66.77
            </div>
            <div slot="innerRight">
                <fb-badge count="+9" type="text" title="text"></fb-badge>
            </div>
        </fb-progress>
        <br/>
        <br/>
        <fb-progress percent="60" status="active" stroke-width="30" :format="format"></fb-progress>
        <br/>
        <br/>
        <fb-progress percent="50" status="active" stroke-width="30" stroke-color="#0ff"></fb-progress>
        <br/>
        <br/>
        <fb-progress percent="50" status="active" stroke-width="26" stroke-color="#0ff" stroke-bg-color="#000"></fb-progress>
        <br/>
        <br/>
        <fb-progress percent="60" status="active" stroke-width="30" :format="format" text-inside></fb-progress>
        <br/>
        <br/>
        <fb-progress percent="50" status="active" stroke-width="26" :stroke-color="{'0%': 'red', '50%': 'yellow', '100%': 'green'}" stroke-bg-color="#000" :show-info="false"></fb-progress>
        <br/>
        <br/>
        <fb-progress percent="50" status="active" stroke-width="26" :stroke-color="{'0%': 'red', '50%': 'yellow', '100%': 'green'}" stroke-bg-color="#000" :show-info="false">
            <div slot="text">
                <span>占比</span>
                <span style="float: right">50%</span>
            </div>
        </fb-progress>
        <br/>
        <br/>
    </fb-cardx>

    <fb-cardx header="动态改值控制颜色">
        <fb-progress :percent="percent" status="active" :stroke-width="30" :stroke-color="customColors"></fb-progress>
        <br/>
        <br/>
        <fb-progress :percent="percent" status="active" :stroke-width="30" :stroke-color="formatColor"></fb-progress>
        <br/>
        <br/>
        <div>
            <fb-button @on-click="percent += 10">+</fb-button>
            <fb-button @on-click="percent -= 10">-</fb-button>
        </div>
    </fb-cardx>

    <fb-cardx header="cirlce || dashboard">
        <fb-progress :percent="percent" type="circle" status="active" :stroke-color="customColors"></fb-progress>
        <span style="margin: 10px"></span>
        <fb-progress :percent="percent" type="circle" status="success"></fb-progress>
        <span style="margin: 10px"></span>
        <fb-progress :percent="percent" type="circle" status="warning"></fb-progress>
        <span style="margin: 10px"></span>
        <br/>
        <fb-progress :percent="percent" type="dashboard" status="active" :stroke-color="customColors"></fb-progress>
        <span style="margin: 10px"></span>
        <fb-progress :percent="percent" type="dashboard" status="active" :stroke-color="{'0%': 'red', '50%': 'yellow', '100%': 'green'}"></fb-progress>
        <span style="margin: 10px"></span>
        <fb-progress :percent="percent" type="dashboard" status="active" stroke-bg-color="#000" :stroke-color="{'0%': '#108ee9', '50%': '#87d068', '100%': 'yellow'}"></fb-progress>
    </fb-cardx>
</div>
</template>
<script>

	export default {
		data () {
			return {
				percent: 10,
				customColors: [
					{color: '#f56c6c', percent: 20},
					{color: '#e6a23c', percent: 40},
					{color: '#5cb87a', percent: 60},
					{color: '#1989fa', percent: 80},
					{color: '#6f7ad3', percent: 100}
				]
			}

		},
		methods: {
			format (percent) {
				console.log(percent)
				if (percent > 50) {
					return '好了'
				}
				return percent
			},
			formatColor (percent) {
				console.log(percent)
				if (percent > 50) {
					return 'red'
				} else {
					return 'yellow'
				}
			}
		},
	}
</script>
```

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| type | 类型，可选 line circle dashboard | String | line |
| format | 内容的模板函数 | function(percent, successPercent) | v-slot:format="percent, successPercent" | percent => percent + '%' |
| percent | 百分比 | number | 0 |
| showInfo | 是否显示进度数值或状态图标 | boolean | true |
| textInside | 进度条显示文字内置在进度条内（只在 type=line 时可用） | boolean | false |
| textStyle | 文本样式 | Object | {} |
| status | 状态，可选：success warning danger normal active(仅限 line) | String | normal |
| strokeColor | 进度条的色彩（会覆盖 status 状态颜色） Array, Function 可细致调整颜色 | String, Object, Array, Function | String |
| strokeBgColor | 进度条 outer 的色彩| String | -- |
| strokeWidth | 进度条线的宽度，单位 px | number | 10 |
| width | 环形进度条画布宽度（只在 type 为 circle 或 dashboard 时可用） | number | 126 |
| strokeLinecap |  | Enum{ 'round', 'square' } | round |
| borderRadius | line类型下边缘锐角度 | String, Number] | 0 |
| innerStyle | 进度条背景样式 | Object |  | 

## 事件列表
| 事件名 | 说明 | 返回值 |
|:-----|:----|:-----|
| on-click | 最外层div事件 | (event) => void

## 插槽选项

| 名称 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| text | 显示文字位置 | html，组件 | --
| innerRight | 显示bg 右侧位置扩展组件 业务丰富性 | html，组件 | --
