[comment]: <> (fb-docs: docsify/fb-ui/05/result/README.md)

# 结果页-Result
用于反馈一系列操作后的结果。

## 基础用法
- 当需要明确反馈，且反馈内容较为复杂时使用；
- 提供两类结果反馈，一种是操作流程反馈，包括“成功反馈”、“失败反馈”等

```html run {title:'示例演示'}
<template>
<div>
    <fb-result
        type="success"
        message="恭喜注册成功"
        describe="现在去登录系统完善企业信息，系统将在3秒后跳转到登录页"
    >
    </fb-result>
    <br/>
    <br/>
    <fb-result type="error" message="提交失败">
        <template v-slot:describe>
            <p>结果页主要用于展示当前操作的结果，</p>
            <p>并需要额外展示复杂内容，</p>
            <p>在下面这个区域展示； 如果只是展示简单的结果反馈，</p>
            <p>请使用全局提示-Message。</p>
        </template>
    </fb-result>
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
    <fb-result type="" message="自定义" iconSize="60" iconName="notice" iconBg="#000">
        <template v-slot:describe>
            <p>结果页主要用于展示当前操作的结果，</p>
            <p>并需要额外展示复杂内容，</p>
        </template>
    </fb-result>
</div>
</template>
```

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| type | 2种类型 可选值是 success、error、img | String | success |
| imgSrc | type=img 生效引入图片路径 | String, Object | -- |
| message | 结果文字提示 | String |  |
| describe | 结果文字描述 | String |  |
| iconSize | 图标大小 | String, Number | 90 |
| iconName | 图标名字 | String | selected |
| iconBg | 图标背景色 | String |  |
