[comment]: <> (fb-docs: docsify/fb-ui/03/slider/README.md)

# 滑块-Slider

## 基础用法

```html run {title:'试一试'}
<template>
<div>
<fb-card header="基础用法">
    <div class="block">
        <span class="demonstration">默认</span>
        <fb-slider v-model="value1" @input="changeInput"></fb-slider>
    </div>
    <div class="block">
        <span class="demonstration">自定义初始值</span>
        <fb-slider v-model="value2" :min="100" :max="360"></fb-slider>
    </div>
    <div class="block">
        <span class="demonstration">隐藏 Tooltip</span>
        <fb-slider v-model="value3" :show-tooltip="false"></fb-slider>
    </div>
    <div class="block">
        <span class="demonstration">格式化 Tooltip</span>
        <fb-slider v-model="value4" :format-tooltip="formatTooltip"></fb-slider>
    </div>
    <div class="block">
        <span class="demonstration">禁用</span>
        <fb-slider v-model="value5" disabled></fb-slider>
    </div>
</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
		value1: 0,
        value2: 180,
        value3: 36,
        value4: 48,
        value5: 42,
      }
    },
    methods: {
        formatTooltip(val) {
            return val / 100;
        },
        changeInput(val) {
            console.log(val)
        },
        changeRange(val) {
            console.log(val)
        },
    }
  }
</script>
<style>
</style>
```

## 间断点
```html run {title:'试一试'}
<template>
<div>
<fb-card header="间断点">
    <div class="block">
        <span class="demonstration">不显示间断点</span>
        <fb-slider
            v-model="value6"
            :step="10">
        </fb-slider>
    </div>
    <div class="block">
        <span class="demonstration">显示间断点</span>
        <fb-slider
            v-model="value7"
            :step="10"
            show-stops>
        </fb-slider>
    </div>
</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
		value6: 0,
        value7: 0,
      }
    },
  }
</script>
<style>
</style>
```

## 范围选择 && 竖向

```html run {title:'试一试'}
<template>
<div>
<fb-card header="范围选择 && 竖向">
    <fb-slider
        v-model="val1"
        @on-change="changeRange"
        range
        show-stops
        :max="10">
    </fb-slider>

    <div class="block">
        <fb-slider
            v-model="val2"
            vertical
            height="200px">
        </fb-slider>
    </div>
</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
		val1: [4, 8],
        val2: 0,
      }
    },
    methods: {
        formatTooltip(val) {
            return val / 100;
        },
        changeInput(val) {
            console.log(val)
        },
        changeRange(val) {
            console.log(val)
        },
    }
  }
</script>
<style>
</style>
```

### 展示标记

```html run {title:'试一试'}
<template>
<div>
<fb-card header="展示标记">
    <fb-slider
        v-model="value"
        range
        :marks="marks">
    </fb-slider>
</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
		value: [30, 60],
        marks: {
            0: '0°C',
            8: '8°C',
            37: '37°C',
            50: {
                style: {
                    color: '#1989FA'
                },
                label: this.$createElement('strong', '50%')
            }
        },
      }
    },
    methods: {
        formatTooltip(val) {
            return val / 100;
        },
        changeInput(val) {
            console.log(val)
        },
        changeRange(val) {
            console.log(val)
        },
    }
  }
</script>
<style>
</style>
```



## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value / v-model | 绑定值 | number | 0
| min | 最小值 | number | 0
| max | 最大值 | number | 100
| disabled | 是否禁用 | boolean | false
| step | 步长 | number | 1
| show-stops | 是否显示间断点 | boolean | false
| show-tooltip | 是否显示 tooltip | boolean | true
| format-tooltip | 格式化 tooltip message | function(value) | —
| range | 是否为范围选择 | boolean | false
| vertical | 是否竖向模式 | boolean | false
| height | Slider 高度，竖向模式时必填 | string | —
| label | 屏幕阅读器标签 | string | —
| tooltip-class | tooltip 的自定义类名 | string | —
| marks | 标记， key 的类型必须为 number 且取值在闭区间 [min, max] 内，每个标记可以单独设置样式 | object | —

## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-change | 	值改变时触发（使用鼠标拖曳时，只在松开鼠标后触发） | 改变后的值
| input | 	数据改变时触发（使用鼠标拖曳时，活动过程实时触发） | 改变后的值

