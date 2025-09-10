[comment]: <> (fb-docs: docsify/fb-ui/03/sign/README.md)

# 手写签名-Sign
在页面中用鼠标签名，返回当前图像的base64编码 

## 基础用法
```html run {title:'示例演示'}
<template>
	<div>
		<fb-card header="直接签名">
            <fb-sign v-model="sign1"></fb-sign>
			<fb-card header="base64 预览">
				<fb-textarea v-model="sign1" rows="10"></fb-textarea>
			</fb-card>

            <fb-card header="图像预览">
                <img :src="sign1" v-if="sign1" alt=""/>
			</fb-card>
		</fb-card>
	</div>
</template>
<script>
  export default {
    data () {
      return {
		sign1: '',
      }
    },
    methods: {
    }
  }
</script>
```

## 签名区域宽高
```html run {title:'示例演示'}
<template>
	<div>
		<fb-sign :height="200" :width="400"></fb-sign>
	</div>
</template>
<script>
  export default {
    data () {
      return {
		sign1: '',
      }
    },
    methods: {
    }
  }
</script>
```

## 更多配置展示
```html run {title:'示例演示'}
<template>
	<div>
		<fb-sign v-model="sign3" :line-color="lineColor" :line-width="lineWidth">
            <template slot="header">
                我是头部
                <fb-icon name="click-fill"/>
                <fb-button @on-click="$message('asd')">按钮</fb-button>
            </template>
        </fb-sign>
        <fb-space>
            <fb-container>
                颜色选择：
                <fb-color-picker v-model="lineColor"></fb-color-picker>
            </fb-container>
            <fb-container>
                线宽选择：
                <fb-input-number v-model="lineWidth" :max="10" :min="1"></fb-input-number>
            </fb-container>
        </fb-space>
	</div>
</template>
<script>
  export default {
    data () {
      return {
		sign2: '',
        sign3: '',

        lineWidth: 3,
        lineColor: '#000000',
      }
    },
    methods: {
    }
  }
</script>
```

 

## 事件列表

| 事件名 | 参数 | 说明 | 返回值 |
|:-------|:----|:-------|--------|
| on-confirm |  | 确认按钮事件 | |
| on-reset |  | 重置按钮事件 | |
 

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 绑定值 | String | '' |
| width | 画布宽度 | Number |600|
| height | 画布高度 | Number |300| 
| lineWidth | 线宽 | Number, String |2|
| lineColor | 线颜色 | String |'#000000'|
| backgroundColor | 背景色 | String |''|
| quality | 图像输出质量 | Number |0.6|
| showAction | 显示操作区 | Boolean | true |
 
