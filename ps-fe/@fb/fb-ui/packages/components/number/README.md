[comment]: <> (fb-docs: docsify/fb-ui/05/number/README.md)

# 计数器 fb-number
展示数字

## 基础用法

```html run {title:'示例演示'}
<template>
<div>
    <fb-page-header title="数字">


        <fb-card header="一般">

            <fb-number ref="n2000"  :start="10000" :end="end" :duration="1000"></fb-number>

            <fb-button @on-click="end = 0; $nextTick(()=>{end = 2000})">end = 0; $nextTick(()=>{end = 2000})</fb-button>

            <fb-button @on-click="$refs.n2000.play()">$refs.n2000.play()</fb-button>

        </fb-card>


        <fb-card header="前后缀">

            <fb-number   :end="80000" prepend="人数：" append="(个)"></fb-number>


        </fb-card>


        <fb-card header="小数点">
            <fb-divider position="left">需要配置	decimal 小数点样式	decimals 小数位数</fb-divider>

            <fb-number   :end="80000.2222"  decimal="-" :decimals="2" ></fb-number>

        </fb-card>


        <fb-card header="千分位分割">

            <fb-number @on-click="$refs.n2000.play()" separator="*" :end="800000000.2222"    ></fb-number>

        </fb-card>

    </fb-page-header>
</div>
</template>
<script>
  export default {
    data () {
      return {
        end: 80000
      }
    },
    methods: {

    },
  }
</script>
```

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| start | 开始数  | Number | 0 |
| end | 结尾数  | Number | 0 |
| duration | 动画时长  | Number | 0 |
| auto | 自动播放  | Boolean | true |
| decimals | 小数位数  | Number | 0 |
| decimal | 小数点  | String | . |
| separator | 千分位分隔符  | String | , |
| prepend | 前缀  | String |  |
| append | 后缀  | String |  | 
| useEasing | 缓动函数  | Boolean | true |
| easingFn | 动画曲率  | Function | (t, b, c, d) {return c * (-Math.pow(2, -10 * t / d) + 1) * 1024 / 1023 + b} |
| formatter | 格式化  | Function | ()=>{} |
| noPlay | 停止动画  | Boolean | false |

## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-click | 点击事件 | () => void

