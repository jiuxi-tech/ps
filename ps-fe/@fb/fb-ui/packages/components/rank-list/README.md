[comment]: <> (fb-docs: docsify/fb-ui/04/ranklist/README.md)

# 评分列表-RankList

用于展示列表，有单条滚动和首位相接滚动效果，常用于评分相关功能展示

## 基础用法
```html run {title:'示例演示'}
<template>
<div>
<fb-card header="切换 forceStart">
    <fb-switch v-model="forceStart"></fb-switch>
    <FbRankList :force-start="forceStart" style="width:500px;height:300px">
        <FbRankListItem v-for="(item, index) in 5" :height="50" >
            <fb-container relative>
                <fb-tag label="自定义" color="#0284fe"></fb-tag>
                <fb-text color="#111">lalalal {{ index }}</fb-text>
            </fb-container>
        </FbRankListItem>
    </FbRankList>
</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
        forceStart: true,
        configNum: {
            rowNum: 3,
            carousel: 'single',
        },
      }
    },
  }
</script>
<style>
</style>
```

## scroll + pause-on-hover
```html run {title:'示例演示'}
<template>
<div>
<fb-card header="scroll + pause-on-hover">
    <FbRankList :forceStart="true" scroll pause-on-hover style="width:500px;height:300px">
        <FbRankListItem v-for="(item, index) in 5" style="height: 50px">{{ item }}</FbRankListItem>
    </FbRankList>
</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
        forceStart: true,
        configNum: {
            rowNum: 3,
            carousel: 'single',
        },
      }
    },
  }
</script>
<style>
</style>
```


## 错落高度
```html run {title:'示例演示'}
<template>
<div>
<fb-card header="错落高度">
    <FbRankList style="width:500px;height:300px">
        <FbRankListItem v-for="(item, index) in 5" :height="index%2 ? 30 : 90" >
            <fb-container relative>
                <fb-tag label="自定义" color="#0284fe"></fb-tag>
                <fb-text color="#111">lalalal {{ index }}</fb-text>
                <fb-text size="22">height: {{ index%2 ? 30 : 90 }}</fb-text>
            </fb-container>
        </FbRankListItem>
    </FbRankList>
</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
        forceStart: true,
        configNum: {
            rowNum: 3,
            carousel: 'single',
        },
      }
    },
  }
</script>
<style>
</style>
```

## rowNum 均分
```html run {title:'示例演示'}
<template>
<div>
<FbRankList :config="configNum" style="width:500px;height:150px">
    <FbRankListItem v-for="(item, index) in 5">
        <fb-container relative height="30px">
            <fb-tag :label="index" color="#0284fe"></fb-tag>
            <fb-text color="#111">lalalal {{ index }}</fb-text>
        </fb-container>
    </FbRankListItem>
</FbRankList>
</div>
</template>
<script>
  export default {
    data () {
      return {
        forceStart: true,
        configNum: {
            rowNum: 3,
            carousel: 'single',
        },
      }
    },
  }
</script>
<style>
</style>
```




## FbRankList 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| config | 配置项 | Object | {} | 
| scroll | 开启首尾相接 | Boolean | false |
| scrollDirection | 开启水平效果（vertical/horizontal） | String | vertical |
| scrollSpeed | 滚动时速度 | Number | 20 |
| pauseOnHover | 是否触碰停止动画 | Boolean | false |
| forceStart | 强制动画，会重复追加数据到动画滚动位置，哪怕只有一条 | Boolean | true |
| waitTime | 动画开始时间 | Number | 2000 |

## config 配置项列表
| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| data | 数据数组，会提供默认排名样式 | Array | [{label: xxx, value: xxx}] |
| rowNum | 容器内均分几行数据，默认不开启，给数字后可以整页翻转列表 | Number | 0 |
| carousel | 翻页模式（'single' /  'page'） | String | single |
| sort | 排名（根据data中value排名） | Number | true | 

## FbRankListItem 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| height | 容器高度 | Number | |

## FbRankListItem 事件列表

| 事件名 | 说明 | 返回值 |  |
| :------- | :------- | :----- | :--- |
| on-click | 单击事件 | e |  |