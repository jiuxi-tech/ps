[comment]: <> (fb-docs: docsify/fb-ui/05/alert/README.md)

# 警告提示-Alert
用比较明显的方式展现需要用户明确关注的信息

## 基础用法

当某个页面需要向用户显示重要的警告信息或者温馨提示时使用；

- 按照提醒的重要程度可以选择是一直显示，还是可手动关闭，但不会主动消失，始终展现；
- 提供四种不同属性的提示，“消息提示”、“成功提示”、“警告提示”、“错误提示”；


```html run {title:'示例演示'}
<template>
<div>
	<fb-card header="普通用法">
		<fb-alert message="消息提示" type="primary"></fb-alert>
		<br/>
		<fb-alert message="成功提示" type="success"></fb-alert>
		<br/>
		<fb-alert message="警告提示" type="warning"></fb-alert>
		<br/>
		<fb-alert message="错误提示" type="danger"></fb-alert>
		<br/>
	</fb-card>

	<fb-card header="可关闭">
		<fb-alert message="消息提示" type="primary" closable></fb-alert>
		<br/>
		<fb-alert message="成功提示" type="success" closable></fb-alert>
		<br/>
		<fb-alert message="警告提示" type="warning" closable></fb-alert>
		<br/>
		<fb-alert message="错误提示" type="danger" closable></fb-alert>
		<br/>
	</fb-card>


	<fb-card header="带描述信息">
		<fb-alert message="成功提示" type="success"
			description="此处展示描述信息"
		></fb-alert>
		<br/>
	</fb-card>

	<fb-card header="带边框">
		<fb-alert message="消息提示" type="primary" bordered></fb-alert>
		<br/>
		<fb-alert message="消息提示" type="primary"
			description="此处展示描述信息"
		bordered></fb-alert>
		<br/>
	</fb-card>
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

### 自定义

```html run {title:'试一试'}
<template>
<div>
	<fb-alert type="primary" closable>
		<template slot="message">
			消息区域
		</template>
		<template slot="description">
			描述区域
		</template>
		<template slot="actions">
			操作区域
		</template>
	</fb-alert>
	<br/>
	<fb-alert message="消息提示" type="primary" closable>
		<fb-link slot="actions" label="查看详情" type="primary"></fb-link>
	</fb-alert>
	<br/>
	<fb-alert message="成功提示" type="success">
		<template slot="actions">
			<fb-icon name="like-fill" color="red"></fb-icon>
			<fb-icon name="like-fill" color="red"></fb-icon>
			<fb-icon name="like-fill" color="red"></fb-icon>
			<fb-icon name="like-fill" color="red"></fb-icon>
			<fb-icon name="like-fill" color="red"></fb-icon>

			<fb-tag type="success" style="margin-left: 8px">很有精神</fb-tag>
			<fb-link label="查看详情" type="primary"></fb-link>
		</template>
	</fb-alert>
	<br/>
	<fb-alert message="警告提示" type="warning">
		<fb-link slot="actions" label="查看详情" icon="like" type="primary"></fb-link>
	</fb-alert>
	<br/>
	<fb-alert message="错误提示" type="danger" closable>
		<fb-link slot="actions" label="查看详情" icon="like" type="primary"></fb-link>
		<fb-link slot="actions" label="查看详情" icon="like" type="success"></fb-link>
		<fb-link slot="actions" label="查看详情" icon="like" type="danger"></fb-link>
	</fb-alert>
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
<style>
</style>
```


## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| type | 类型，可选值：primary,success,warning,danger | String | primary
| message | 消息 | String, Number |
| description | 描述 | String, Number |
| closable | 是否可关闭 | Boolean | false
| show | 是否显示 | Boolean | true
| bordered | 是否包含边框 | Boolean | false
| icon | 自定义图标 | String |
