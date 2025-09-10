[comment]: <> (fb-docs: docsify/fb-ui/03/input-number/README.md)

# 数字文本框-InputNumber
数字文本框


### 基础用法

```html run {title:'示例演示'}
<template>
    <fb-card header="基础用法 and 尺寸">
        <fb-container>
            <fb-input-number v-model="num" :min="1" :max="10" size="m" @on-change="changeNum"></fb-input-number>
        </fb-container>
        <fb-container mt="10px">
            <fb-input-number v-model="num" :min="1" :max="10" controls-position="right"></fb-input-number>
        </fb-container>

        <fb-container mt="10px">
            <fb-input-number v-model="num" :min="1" :max="10" size="l"></fb-input-number>
        </fb-container>
        <fb-container mt="10px">
            <fb-input-number v-model="num" :min="1" :max="10" controls-position="right" size="l"></fb-input-number>
        </fb-container>


        <fb-container mt="10px">
            <fb-input-number v-model="num" :min="1" :max="10" size="s"></fb-input-number>
        </fb-container>
        <fb-container mt="10px">
            <fb-input-number v-model="num" :min="1" :max="10" controls-position="right" size="s"></fb-input-number>
        </fb-container>
    </fb-card>

</template>
<script>
  export default {
    data () {
      return {
         num: 1,
      }
    },
    methods: {
        changeNum(val) {
            console.log(val)
        }
	},
  }
</script>
<style>
</style>
```

### 禁用状态 and 步数

```html run {title:'示例演示'}
<template>
	<fb-card header="禁用状态 and 步数">
        <fb-container>
            <fb-input-number v-model="num1" size="m" disabled></fb-input-number>
        </fb-container>
        <fb-container mt="10px">
            <fb-input-number v-model="num1" :step="2"></fb-input-number>
        </fb-container>
    </fb-card>

</template>
<script>
  export default {
    data () {
      return {
        num1: 0,
      }
    },
  }
</script>
<style>
</style>
```

### 严格步数 and 精度

```html run {title:'示例演示'}
<template>
	<div>
        <fb-card header="严格步数">
            <fb-container>
                step-strictly属性接受一个Boolean。如果这个属性被设置为true，则只能输入步数的倍数。
            </fb-container>
            <fb-container mt="10px">
                <fb-input-number v-model="num2" :step="2" step-strictly></fb-input-number>
            </fb-container>
        </fb-card>

        <fb-card header="精度">
            <fb-container>
                设置 precision 属性可以控制数值精度，接收一个 Number。
            </fb-container>
            <fb-container mt="10px">
                <fb-input-number v-model="num3" :precision="2" :step="0.01" :max="10"></fb-input-number>
            </fb-container>
        </fb-card>
    </div>

</template>
<script>
  export default {
    data () {
      return {
        num2: 10,
        num3: 1
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
| value / v-model | 绑定值 | number  | 0 |
| min | 设置计数器允许的最小值 | number | -Infinity |
| max | 设置计数器允许的最大值 | number | Infinity |
| step | 计数器步长 | number | 1 |
| step-strictly | 是否只能输入 step 的倍数 | boolean | false |
| precision | 数值精度 | number | - |
| size | 计数器尺寸 | string | m |
| disabled | 是否禁用 | boolean | false |
| controls | 是否使用控制按钮 | boolean | true |
| controls-position | 控制按钮位置 | string | - |
| name | 原生属性 | string | - |
| placeholder | 输入框默认 placeholder | string | - |



## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-change | 当绑定值变化时触发 | 当前值
| on-blur | 在组件 Input 失去焦点时触发 | (event: Event)
| on-focus | 在组件 Input 获得焦点时触发 | (event: Event)


