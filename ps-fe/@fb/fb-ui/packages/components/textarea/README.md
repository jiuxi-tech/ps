[comment]: <> (fb-docs: docsify/fb-ui/03/textarea/README.md)

# 文本域-Textarea
同文本框，是文本框的多行输入。

## 基础用法

- 需要用户输入的文本过长时；
- 右下角支持拖拽自定义文本域的大小；

```html run {title:'示例演示'}
<template>
<div>
控件类型：
	<br/>
	<br/>
	<fb-textarea placeholder="普通输入框"></fb-textarea>
	<br/>
	<br/>
	<fb-textarea placeholder="输入字符最大长度限制":maxlength="10"></fb-textarea>
	<br/>
	<fb-textarea placeholder="带内置“清除”按钮" clearable></fb-textarea>
	<br/>
	<br/>
控件样式：
	<br/>
	<br/>
	<fb-textarea placeholder="自定义宽度：30%" width="30%"></fb-textarea>
	<fb-textarea placeholder="自定义宽度：150px" width="150px"></fb-textarea>
	<fb-textarea placeholder="自定义宽度：150" width="150"></fb-textarea>
	<br/>
	<br/>
	<fb-textarea placeholder="边框圆角" width="30%"  round></fb-textarea>
	<br/>
	<br/>
	<fb-textarea placeholder="输入框只读" width="30%" readonly></fb-textarea>
	<fb-textarea placeholder="输入框禁用" width="30%" disabled></fb-textarea>
	<br/>
	<br/>
控件尺寸：
	<br/>
	<br/>
	<fb-textarea placeholder="小" width="30%" size="s"></fb-textarea>
	<fb-textarea placeholder="中" width="30%" size="m"></fb-textarea>
	<fb-textarea placeholder="大" width="30%" size="l"></fb-textarea>
</div>
</template>

<script>
	export default {
		data () {
			return {
			}
		}
	}
</script>
```

## 更多用法

### 内置控件事件

```html run {title:'试一试'}
<template>
<div>
	<fb-textarea placeholder="点击时触发" width="33%" @on-click="handleClick"></fb-textarea>
	<br/>
	<br/>
	<fb-textarea placeholder="按下回车键时触发" width="33%" @on-enter="handleEnter"></fb-textarea>
	<fb-textarea placeholder="获取焦点时触发" width="33%" @on-focus="handleFocus"></fb-textarea>
	<fb-textarea placeholder="失去焦点时触发" width="33%" @on-blur="handleBlur"></fb-textarea>
	<br/>
	<br/>
	<fb-textarea placeholder="内容输入时触发" width="33%" @on-input="handleInput"></fb-textarea>
	<fb-textarea placeholder="内容变更时触发" width="33%" @on-change="handleInput"></fb-textarea>
</div>
</template>

<script>
	export default {
		data () {
			return {
				i:0,
			}
		},
		methods: {
			handleClick (e) {
				this.$message.success('你点中了我 '+(++this.i)+' 次！', {time: 1000})
			},
			handleEnter (e) {
				this.$message.success('你按了回车键！', {time: 1000})
			},
			handleFocus (e) {
				this.$message.success('我获得了焦点！', {time: 1000})
			},
			handleBlur (e) {
				this.$message.success('我失去了焦点！', {time: 1000})
			},
			handleInput (val) {
				this.$message.success('你输入了：'+ val, {time: 1000})
			}
		}
	}
</script>
```

## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-click | 当鼠标指针点击输入框时触发，可以是方法名，或方法体 | (event) => void
| on-enter | 当键盘按下回车键时触发，可以是方法名，或方法体 | (event) => void
| on-focus | 当输入框获取焦点时触发，可以是方法名，或方法体 | (event) => void
| on-blur | 当输入框失去焦点时触发，可以是方法名，或方法体 | (event) => void
| on-input | 当输入框内容变化时触发，可以是方法名，或方法体 | (val) => void
| on-change | 当输入框内容变化时触发，可以是方法名，或方法体 | (val) => void


## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 当前值 | String, Number |
| type | 显示类型，可选 text password | String | text
| size | 尺寸大小 可选值是 s、m、l | String | m
| placeholder | 占位提示 | String |
| disabled | 禁用状态 | Boolean | false
| readonly | 只读状态 | Boolean | false
| clearable | 是否可清除 | Boolean | false
| maxlength | 最大长度 | String, Number |
| rows | 占用行数 | String，Number |2
| width | 宽度，支持 px,% | String, Number | 100%
| name | 显示名称 | String |
| autocomplete | 是否自动完成，可选值是 off,on | String | off
| round | 边框是否圆角 | Boolean | false


