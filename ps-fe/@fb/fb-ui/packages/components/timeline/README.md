[comment]: <> (fb-docs: docsify/fb-ui/04/timeline/README.md)

# 时间轴-Timeline

用于时间线的展示，流程展示

## 基础用法

```html run {title:'示例演示'}
<template>
<div>
<fb-timeline>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">
        <template #dot>
            <span>
                <fb-icon size="16" name="loading2"></fb-icon>
            </span>
        </template>
        Create a services site
    </fb-timeline-item>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Solve initial network problems</fb-timeline-item>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Technical testing</fb-timeline-item>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Network problems being solved</fb-timeline-item>
</fb-timeline>
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

## mode='alternate'

```html run {title:'示例演示'}
<template>
<div>
<fb-timeline mode="alternate">
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">
        <template #dot>
            <span>
                <fb-icon size="16" name="loading2"></fb-icon>
            </span>
        </template>
        Create a services site
    </fb-timeline-item>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Solve initial network problems</fb-timeline-item>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Technical testing</fb-timeline-item>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Network problems being solved</fb-timeline-item>
</fb-timeline>
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

## mode='right'

```html run {title:'示例演示'}
<template>
<div>
<fb-timeline mode="right">
    <fb-timeline-item dot-color="red">Create a services site 2015-09-01</fb-timeline-item>
    <fb-timeline-item dot-color="orange">Solve initial network problems 2015-09-01</fb-timeline-item>
    <fb-timeline-item dot-color="yellow">Technical testing 2015-09-01</fb-timeline-item>
    <fb-timeline-item dot-color="blue">Network problems being solved 2015-09-01</fb-timeline-item>
</fb-timeline>
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

## horizontal 水平模式

```html run {title:'示例演示'}
<template>
<div>
<p>水平模式下 注意宽度超出给一个容器包裹</p>
<fb-timeline horizontal>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">
        <template #dot>
            <span>
                <fb-icon size="16" name="loading2"></fb-icon>
            </span>
        </template>
        Create a services site
    </fb-timeline-item>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Solve initial network problems</fb-timeline-item>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Network problemsNetwork problems being solved</fb-timeline-item>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Technical testing</fb-timeline-item>
</fb-timeline>
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

## horizontal mode='top'

```html run {title:'示例演示'}
<template>
<div>
<p>水平模式下 注意宽度超出给一个容器包裹</p>
<fb-container style="overflow: auto; height: 140px">
    <fb-timeline horizontal mode='top'>
        <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">
            <template #dot>
            <span>
                <fb-icon size="16" name="loading2"></fb-icon>
            </span>
            </template>
            Create a services site
        </fb-timeline-item>
        <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Solve initial network problems</fb-timeline-item>
        <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Network problemsNetwork problems being solved</fb-timeline-item>
        <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Technical testing</fb-timeline-item>
        <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Technical testing</fb-timeline-item>
        <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Technical testing</fb-timeline-item>
        <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Technical testing</fb-timeline-item>
        <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Technical testing</fb-timeline-item>
        <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Technical testing</fb-timeline-item>
        <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Technical testing</fb-timeline-item>
    </fb-timeline>
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

## horizontal mode='alternate'

```html run {title:'示例演示'}
<template>
<div>
<p>水平模式下 注意宽度超出给一个容器包裹</p>
<fb-timeline horizontal mode='alternate'>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">
        <template #dot>
            <span>
                <fb-icon size="16" name="loading2"></fb-icon>
            </span>
        </template>
        Create a services site
    </fb-timeline-item>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Solve initial network problems</fb-timeline-item>
    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Network problemsNetwork problems being solved</fb-timeline-item>

    <fb-timeline-item describe="这里是描述文字" time="2022-07-14  20:12:59">Technical testing</fb-timeline-item>
</fb-timeline>
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



## FbTimeline属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| mode | 模式 (left/right/alternate) | String ||
| reverse | 反转时间轴 | Boolean | false |
| horizontal | 开启水平效果 （mode 为top/bottom/alternate） | Boolean | false |

## FbTimelineItem属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| describe | 说明（同时为插槽 #describe） | String/slot ||
| time | 时间（同时为插槽 #time） | String/slot ||
| dotColor | 指定圆圈颜色 blue, red, green，或自定义的色值 | String | blue |
| position | 自定义节点位置（left/right）（开启水平效果top/bottom） | String | |
| dot | 节点插槽 | slot ||

## FbTimelineItem事件列表

| 事件名 | 说明 | 返回值 |  |
| :------- | :------- | :----- | :--- |
| on-click | 单击事件 | e |  |