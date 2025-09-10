[comment]: <> (fb-docs: docsify/fb-ui/03/switch-accordion/README.md)

# 开关手风琴-SwitchAccordion

## 基础用法

```html run {title:'示例演示'}
<template>
<div>
	<fb-switch-accordion title="仅文字">
		<fb-form>
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="11111">
						<fb-input></fb-input>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="3333">
						<fb-input></fb-input>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="4444">
						<fb-input></fb-input>
					</fb-form-item>
				</fb-col>
			</fb-row>
		</fb-form>
	</fb-switch-accordion>
	<p></p>
	<fb-switch-accordion title="带图标" icon="search">
		<fb-form>
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="11111">
						<fb-input></fb-input>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="3333">
						<fb-input></fb-input>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="4444">
						<fb-input></fb-input>
					</fb-form-item>
				</fb-col>
			</fb-row>
		</fb-form>
	</fb-switch-accordion>
	<p></p>
	<fb-switch-accordion title="默认打开" :value="true">
		<fb-form>
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="11111">
						<fb-input></fb-input>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="3333">
						<fb-input></fb-input>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="4444">
						<fb-input></fb-input>
					</fb-form-item>
				</fb-col>
			</fb-row>
		</fb-form>
	</fb-switch-accordion>
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



## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-change | 当状态变化时触发，可以是方法名，或方法体 | (val) => void

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| icon | 标题图标 | String |
| title | 标题 | String |
| value |当前值  | Boolean, String, Number |false
| bodyStyle | 样式 | String, Array, Object |
