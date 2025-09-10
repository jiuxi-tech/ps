[comment]: <> (fb-docs: docsify/fb-ui/03/input/README.md)

# 文本框-Input
通过鼠标或者键盘输入内容，是最基础的表单域的包装。

## 基础用法

需要用户输入表单域内容时；

提供了以下几种不同类型的输入框：
- 组合型输入框
- 带搜索的输入框
- 带后缀的输入框

还提供三种尺寸的选择；

```html run {title:'示例演示'}
<template>
<div>
控件类型：
<br/><br/>
<fb-input placeholder="普通输入框"></fb-input>

<br/><br/>
<fb-input placeholder="密码输入框" type="password"></fb-input>

<br/><br/>
<fb-input placeholder="输入字符最大长度限制":maxlength="10"></fb-input>

<br/>
<fb-input placeholder="带内置“清除”按钮" clearable></fb-input>

<br/><br/>
<fb-input placeholder="自定义前内置图标" prependIcon="computer"></fb-input>

<br/><br/>
<fb-input placeholder="自定义前外置描述">
	<template slot="prepend">
		描述
	</template>
</fb-input>

<br/>
<fb-input placeholder="自定义前外置控件">
	<template slot="prepend">
		<select>
			<option value="asd">--请选择--</option>
			<option value="asd">下拉选项一</option>
			<option value="asd">下拉选项二</option>
		</select>
	</template>
</fb-input>

<br/><br/>
<fb-input placeholder="自定义前外置控件">
	<template slot="prepend-button">
		<fb-button type="primary" icon="search" size="s">查 找</fb-button>
	</template>
</fb-input>

<br/><br/>
<fb-input placeholder="自定义前外置控件">
	<template slot="prepend">
		<fb-checkbox>测试</fb-checkbox>
		<fb-checkbox>测试</fb-checkbox>
	</template>
</fb-input>

<br/><br/>
<fb-input placeholder="自定义后内置图标" icon="computer"></fb-input>

<br/><br/>
<fb-input placeholder="自定义后外置图标" >
	<template slot="append">
		<fb-icon name="computer" style="cursor:pointer;"></fb-icon>
	</template>
</fb-input>

<br/>
<fb-input placeholder="自定义后外置描述">
	<template slot="append">
		万元
	</template>
</fb-input>

<br/>
<fb-input placeholder="自定义后外置按钮">
	<div slot="append-button">
		<fb-button type="primary" icon="search">查 找</fb-button>
	</div>
</fb-input>

<br/><br/>
控件样式：

<br/><br/>
<fb-input placeholder="自定义宽度：30%" width="30%"></fb-input>
<fb-input placeholder="自定义宽度：150px" width="150px"></fb-input>
<fb-input placeholder="自定义宽度：150" width="150"></fb-input>

<br/><br/>
<fb-input placeholder="边框圆角" width="30%"  round></fb-input>

<br/><br/>
<fb-input placeholder="输入框只读" width="30%" readonly></fb-input>
<fb-input placeholder="输入框禁用" width="30%" disabled></fb-input>

<br/><br/>
控件尺寸：
<br/><br/>
<fb-input placeholder="小" width="30%" size="s"></fb-input>
<fb-input placeholder="中" width="30%" size="m"></fb-input>
<fb-input placeholder="大" width="30%" size="l"></fb-input>
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

### 外置控件事件
外置控件的事件触发，以外置控件为准

```html run {title:'试一试'}
<template>
<div>
	<fb-input placeholder="点击选择下拉框">
		<template slot="prepend">
			<select @change="handleSelect">
				<option value="asd">--请选择--</option>
				<option value="asd">下拉选项一</option>
				<option value="asd">下拉选项二</option>
			</select>
		</template>
	</fb-input>
	<br/>
	<fb-input placeholder="点击选择复选框">
		<template slot="prepend">
			<fb-checkbox @on-click="handleCheckBox">测试</fb-checkbox>
			<fb-checkbox @on-click="handleCheckBox">测试</fb-checkbox>
		</template>
	</fb-input>
	<br/>
	<fb-input placeholder="点击右侧图标" >
		<template slot="append">
			<fb-icon name="computer" style="cursor:pointer;" @on-click="handleIcon"></fb-icon>
		</template>
	</fb-input>
	<br/>
	<fb-input placeholder="点击左侧查找按钮">
		<template slot="prepend-button">
			<fb-button type="primary" icon="search" size="s" @on-click="handleButton">查 找</fb-button>
		</template>
	</fb-input>
	<br/>
	<fb-input placeholder="点击右侧查找按钮">
		<div slot="append-button">
			<fb-button type="primary" icon="search" @on-click="handleButton">查 找</fb-button>
		</div>
	</fb-input>
</div>
</template>

<script>
	export default {
		data () {
			return {
			}
		},
		methods: {
			handleSelect(e){
				this.$message.success('你点击了 Select ！', {time: 1000})
			},
			handleCheckBox(e){
				this.$message.success('你点击了 CheckBox ！', {time: 1000})
			},
			handleButton (e) {
				this.$message.success('你点击了 Button ！', {time: 1000})
			},
			handleIcon (e) {
				this.$message.success('你点击了 Icon ！', {time: 1000})
			},

		},
	}
</script>
```

### 内置控件事件

```html run {title:'试一试'}
<template>
<div>
	<fb-input placeholder="点击时触发" width="33%" @on-click="handleClick"></fb-input>
	<fb-input placeholder="点击后内置图标时触发" width="33%" icon="computer" @on-icon-click="handleClick"></fb-input>
	<fb-input placeholder="点击前内置图标时触发" width="33%" prependIcon="computer" @on-prepend-icon-click="handleClick"></fb-input>
	<br/>
	<br/>
	<fb-input placeholder="按下回车键时触发" width="33%" @on-enter="handleEnter"></fb-input>
	<fb-input placeholder="获取焦点时触发" width="33%" @on-focus="handleFocus"></fb-input>
	<fb-input placeholder="失去焦点时触发" width="33%" @on-blur="handleBlur"></fb-input>
	<br/>
	<br/>
	<fb-input placeholder="内容输入时触发" width="33%" @on-input="handleInput"></fb-input>
	<fb-input placeholder="内容变更时触发" width="33%" @on-change="handleInput"></fb-input>
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
| on-icon-click | 当鼠标指针点击后内置图标时触发，可以是方法名，或方法体 | (event) => void
| on-prepend-icon-click | 当鼠标指针点击前内置图标时触发，可以是方法名，或方法体 | (event) => void
| on-enter | 当键盘按下回车键时触发，可以是方法名，或方法体 | (event) => void
| on-focus | 当输入框获取焦点时触发，可以是方法名，或方法体 | (event) => void
| on-blur | 当输入框失去焦点时触发，可以是方法名，或方法体 | (event) => void
| on-input | 当输入框内容变化时触发，可以是方法名，或方法体 | (val) => void
| on-change | 当输入框内容变化时触发，可以是方法名，或方法体 | (val) => void


## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 当前值 | String, Number ||
| type | 控件类型，可选 text password | String | text|
| size | 尺寸大小 可选值是 s、m、l | String | m|
| placeholder | 占位提示 | String ||
| disabled | 禁用状态 | Boolean | false|
| readonly | 只读状态 | Boolean | false|
| clearable | 是否为可清除 | Boolean | false|
| maxlength | 最大长度 | String, Number ||
| prependIcon | 前置图标 | String ||
| icon | 后置图标 | String ||
| width | 宽度，支持 px,% | String, Number | 100%|
| name | 显示名称 | String ||
| autocomplete | 是否自动完成，可选值是 off,on | String | off|
| round | 边框是否圆角 | Boolean | false|
| elStyle | 用于精细调节input中的el样式 | Object | {} |

