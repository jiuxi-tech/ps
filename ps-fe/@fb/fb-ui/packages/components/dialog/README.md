[comment]: <> (fb-docs: docsify/fb-ui/05/dialog/README.md)

# 弹窗-Dialog
以弹出的形式的对话框

## 基础用法

需要用户处理，又不希望跳转页面以致打断当前操作时使用；

提供两种形式，“信息对话框”、“确认对话框”；
- 信息对话框：可以承载比较复杂的信息，如详细信息，编辑信息等；
- 确认对话框：主要以简单信息为主，如删除确认等；


```html run {title:'信息对话框'}
<template>
<div style="height:220px">
	<fb-dialog ref="dialog"  height="100" left="0px" auto-show can-full-screen>
		<fb-form :label-width="120" ref="xxForm">
			<fb-fieldset label="表单信息"/>
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="企业名称" prop="企业名称" :rule="[{required: true}]">
						<fb-input>asda</fb-input>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="企业名称">
						<fb-switch></fb-switch>
					</fb-form-item>
				</fb-col>
			</fb-row>
		</fb-form>

		<template slot="footer-right">
			<fb-button @on-click="handleClose">取消</fb-button>
			<fb-button type="primary" @on-click="handleClose">保存</fb-button>
		</template>
	</fb-dialog>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
    methods:{
        handleClose() {
            this.$refs.dialog.hide()
        },
    }
  }
</script>
<style>
</style>
```


```html run {title:'确认对话框'}
<template>
<div style="height:220px">
	<fb-button @on-click="alert">提示对话框</fb-button>
	<fb-button @on-click="confirm">确认对话框</fb-button>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
	methods: {
		alert () {
			this.$alert('这里是提示信息?',
				() => {
					this.$message.success('你点击了确认')
				}
			)
		},
		confirm () {
			this.$confirm('这里是确认信息?',
				() => {
					this.$message.success('你点击了确认')
				},
				() => {
					this.$message.error('你点击取取消')
				}
			)
		},
	},
  }
</script>
<style>
</style>
```
## 更多用法

### 事件监听

```html run {title:'试一试'}
<template>
<div style="height:220px">
	<fb-dialog ref="dialog"  height="120" left="0px" auto-show can-full-screen :show-close-btn="false"
		@on-close="handleClosing"
		@on-scroll="handleScrolling"
	>
		<fb-form :label-width="120" ref="xxForm">
			<fb-fieldset label="表单信息"/>
			<fb-row>
				<fb-col span="12">
					<fb-form-item label="企业名称" prop="企业名称" :rule="[{required: true}]">
						<fb-input>asda</fb-input>
					</fb-form-item>
				</fb-col>
				<fb-col span="12">
					<fb-form-item label="企业名称">
						<fb-switch></fb-switch>
					</fb-form-item>
				</fb-col>
			</fb-row>
		</fb-form>

		<template slot="footer-right">
			<fb-button @on-click="handleClose">取消</fb-button>
			<fb-button type="primary" @on-click="handleClose">保存</fb-button>
		</template>
	</fb-dialog>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
    methods:{
        handleClose() {
            this.$refs.dialog.hide()
        },
    	handleClosing(){
			this.$message.success('对话框关闭了！', {time: 500})
    	},
    	handleScrolling(){
			this.$message.success('对话框页面滚动了！', {time: 500})
    	}
    }
  }
</script>
<style>
</style>
```

### 打开组件

将.vue文件注册成组件后，通过对话框方式直接打开

```
<template>
<div>
	<fb-button @on-click="handleClick" icon="add-circle">打开对话框</fb-button>
	<fb-dialog ref="dialog" can-full-screen no-padding></fb-dialog>
</div>
</template>
<script>
  export default {
	methods: {
		handleClick () {
			this.$refs.dialog.show({
				// component：要打开的组件名称
				component: 'form_0',
				// data：传递至组件中的数据，需要打开的组件，需要具有data属性，
                data: model,
				title: '新增',
				close: (data) => {
					if (data) {
						this.$message.success('操作成功...', {time: 1000})
					}
				},
			})
		},
	},
  }
</script>
```

### 打开页面文件

通过对话框直接打开.vue页面

```
<template>
<div>
	<fb-button @on-click="handleClick" icon="add-circle">打开对话框</fb-button>
	<fb-dialog ref="dialog" can-full-screen no-padding></fb-dialog>
</div>
</template>
<script>
  export default {
	methods: {
		handleClick () {
			// 假设当前文件为： @/view/user/list.vue
			this.$refs.dialog.show({
				// url：指定要打开的页面路径，文件以.vue为后双缀名，且文件存放在`@/view`根目录下
				// 打开目标文件：@/view/user/edit.vue
                url: "./edit",
				// data：传递至页面中的数据，需要打开的页面，可通过data属性获取数据，
                data: model,
				title: '新增',
				close: (data) => {
					if (data) {
						this.$message.success('操作成功...', {time: 1000})
					}
				},
			})
		},
	},
  }
</script>
```

更多信息，请查看 设计规范/交互规范
>
## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-close | 当对话框关闭时触发 | () => void
| on-scroll | 当对话框在浏览器中滚动时触发 | (event) => void

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| title | 标题 | String | 信息窗口|
| sub-title | 子标题 | String ||
| width | 宽度 | Number, String | 800|
| height | 高度 | Number, String | 600|
| close-on-click-shadow | 点击阴影部分关闭窗口 | Boolean | true|
| disableEsc | 按ESC键关闭窗口 | Boolean | false|
| can-full-screen | 最大化按钮 | Boolean | false|
| show-close-btn | 关闭按钮 | Boolean | true|
| before-close | 关闭前的回调 | Function ||
| auto-show | 自动显示 | Boolean | false|
| lock | 锁屏 | Boolean | true|
| loader |  | Function ||
| no-padding |  | Boolean | false|
| top |  | String, Number | 15vh|
| left |  | String, Number | 0|
| overflow | 当内容溢出元素框时发生的事情，可选值：auto | String ||
| dialog-style | 弹窗窗体样式 | Object |{}|
| wrapper-style | 弹窗wrapper样式 | Object |{}|

