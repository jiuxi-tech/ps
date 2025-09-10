[comment]: <> (fb-docs: docsify/fb-ui/03/timepicker/README.md)

# 时间选择-TimePicker

## 基础用法 

```html run {title:'示例演示'}
<template>
<div style="height: 300px">
<fb-card>
    已选时间： {{time}}

    <fb-timepicker v-model="time"></fb-timepicker>
</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
        time: null,
      }
    },
  }
</script>
<style>
</style>
```

## 与datepicker一起
```html run {title:'示例演示'}
<template>
<div style="height: 300px">
<fb-card header="与datepicker一起">
	已选时间： {{time}}
    <fb-datepicker v-model="date" style="width: 120px"></fb-datepicker>
    <fb-timepicker v-model="time" style="width: 120px"></fb-timepicker>
</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
        date: null,
        time: null,
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
| on-clear |  |
| on-change | 当输入框内容变化时触发，可以是方法名，或方法体 | (val) => void



## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 当前值 | Date, Array, Number, String |
| placeholder | 提示文字 | String |
| icon | 右侧图标 | String  | waiting
| disabled | 禁用 | Boolean  | false
| clearable | 清除按钮 | Boolean  | false
| readonly | 只读 | Boolean  | false
| format | 格式化 | String  | HH:mm:ss
| hourStep | 小时步数 | Number  | 1
| minuteStep | 分钟步数 | Number  | 1
| secondStep | 秒步数 | Number  | 1
| disabledHours | 禁用小时 | Function  | 
| disabledMinutes | 禁用分钟 | Function  | 
| disabledSeconds | 禁用秒 | Function  | 


