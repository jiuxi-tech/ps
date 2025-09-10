[comment]: <> (fb-docs: docsify/fb-ui/04/steps/README.md)

# 步骤条-Steps
用于引导用户按照流程完成任务的导航条

## 基础用法

当任务比较复杂且存在先后关系时，将其分解成一系列的步骤，从而简化任务；

- 提供横向及纵向布局类型的大小两种尺寸的四种步骤条供选择；

```html run {title:'示例演示'}
<template>
<div>

<fb-card header="普通用法">
	<fb-steps :current="1">
		<fb-step label="步骤一"></fb-step>
		<fb-step label="步骤二"></fb-step>
		<fb-step label="步骤三"></fb-step>
	</fb-steps>
</fb-card>

<br/>
<fb-card header="带说明">
	<fb-steps :current="2">
		<fb-step label="步骤一"  description="步骤一描述"></fb-step>
		<fb-step label="步骤二"  description="步骤二描述"></fb-step>
		<fb-step label="步骤三"  description="步骤三描述"></fb-step>
	</fb-steps>
</fb-card>

<br/>
<fb-card header="尺寸小号">
	<fb-steps :current="2" size="s">
		<fb-step label="步骤一"  description="步骤一描述"></fb-step>
		<fb-step label="步骤二"  description="步骤二描述"></fb-step>
		<fb-step label="步骤三"  description="步骤三描述"></fb-step>
	</fb-steps>
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

### 流程控制

```html run {title:'试一试'}
<template>
<div>
	<fb-steps :current="process">
		<fb-step v-for="item in steps" :key="item.label" :label="item.label"/>
	</fb-steps>
	<div class="steps-content">
		{{ steps[process].description }}
	</div>
	<div class="steps-action">
		<fb-button v-if="process > 0"
			@on-click="prev"
		>
			上一步
		</fb-button>
		<fb-button
			v-if="process == steps.length - 1"
			type="primary"
			style="margin-left: 8px"
			@on-click="$message.success('恭喜, 配对成功!')"
		>
			完成
		</fb-button>

		<fb-button v-if="process < steps.length - 1"
			type="primary"
			style="margin-left: 8px"
			@on-click="next"
	   >
			下一步
		</fb-button>
	</div>
</div>
</template>
<script>
  export default {
    data () {
      return {
			process: 0,
			steps: [
				{
					label: '第一步',
					description: '1 步骤一描述',
				},
				{
					label: '第二步',
					description: '2 步骤二描述',
				},
				{
					label: '第三步',
					description: '3 步骤三描述',
				},
			],
      }
    },
	methods: {
		next () {
			this.process++
		},
		prev () {
			this.process--
		},
	},
  }
</script>
<style>
.steps-content {
    margin-top: 16px;
    border: 1px dashed #e9e9e9;
    border-radius: 6px;
    background-color: #fafafa;
    min-height: 200px;
    text-align: center;
    padding-top: 80px;
}

.steps-action {
    margin-top: 24px;
}

</style>
```

### 点选控制

```html run {title:'试一试'}
<template>
<div>
	<fb-steps :current="process" @on-step-click="index=>{process=index}">
		<fb-step v-for="item in steps" :key="item.label" :label="item.label" :icon="item.icon"/>
	</fb-steps>

	<div class="steps-content">
		<component :is="steps[process].component"></component>
	</div>
</div>
</template>
<script>
  export default {
	components: {
		form_0: {
			template: `
				<div>
					<fb-form>
						<fb-form-item label="体重">
							<fb-input></fb-input>
						</fb-form-item>
					</fb-form>
				</div>
			`,
		},
		form_1: {
			template: `
				<div>
					<fb-form>
						<fb-form-item label="年龄">
							<fb-input></fb-input>
						</fb-form-item>
					</fb-form>
				</div>
			`,
		},
		form_2: {
			template: `
				<div>
					<fb-form>
						<fb-form-item label="长度">
							<fb-input></fb-input>
						</fb-form-item>
					</fb-form>
				</div>
			`,
		},
	},
    data () {
      return {
			process: 0,
			steps: [
				{
					label: '第一步',
					icon: "chart-line",
					component: 'form_0',
				},
				{
					label: '第二步',
					icon: "progressbar",
					component: 'form_1',
				},
				{
					label: '第三步',
					icon: "chart-bar",
					component: 'form_2',
				},
			],
      }
    },
	methods: {
		next () {
			this.process++
		},
		prev () {
			this.process--
		},
	},
  }
</script>
<style>
.steps-content {
    margin-top: 16px;
    border: 1px dashed #e9e9e9;
    border-radius: 6px;
    background-color: #fafafa;
    min-height: 200px;
    text-align: center;
    padding-top: 80px;
}

.steps-action {
    margin-top: 24px;
}

</style>
```

## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-click | 当鼠标指针点击进度条时触发，可以是方法名，或方法体 | (...args) => void
| on-step-click | 当鼠标指针点击进度条时触发，可以是方法名，或方法体 | (index) => void

## 属性列表

- fb-step

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| label | 显示文本 | String |
| description | 描述 | String |
| icon |  |  |
| status | 状态，可选值：ready, doing, done, wait, error | Boolean | ready


- fb-steps

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| current | 当前步骤 | String, Number | 0
| size | 尺寸，可选值:s | String |
| vertical | 垂直显示 | Boolean | false
