[comment]: <> (fb-docs: docsify/fb-ui/04/menu/README.md)

# 菜单-menu
基础菜单布局


### 水平展示 切换主题
```html run {title:'示例演示'}
<template>
	<fb-page-header
		title="menu"
		sub-title=""
	>

		<fb-card header="mode=horizontal menuTrigger=click">
			current: {{ menuActive1 }}
			<fb-container mb="20px">
				<fb-radio-group v-model="theme" button :data="[
					{label: 'light', value: 'light'},
					{label: 'dark', value: 'dark'},
				]"></fb-radio-group>
			</fb-container>

			<fb-menu v-model="menuActive1" mode="horizontal" :theme="theme" menuTrigger="click" @on-open="handleOpen" @on-close="handleClose">

				<fb-menu-item @on-click="handleClickItem" index="1"><fb-icon name="earth"></fb-icon>Navigation One
				</fb-menu-item>
				<fb-menu-item index="2" disabled><fb-icon name="pin"></fb-icon>Navigation Two
				</fb-menu-item>
				<fb-sub-menu index="3">
					<span slot="title">
						<fb-icon name="send"/>
						<span>Navigation Three</span>
					</span>
					<fb-menu-item index="3-1">3-1</fb-menu-item>
					<fb-menu-item index="3-2">3-2</fb-menu-item>
					<fb-sub-menu index="3-3">
						<span slot="title">
							<fb-icon name="send"/>
							<span>3-3</span>
						</span>
						<fb-menu-item index="3-3-1">3-3-1</fb-menu-item>
						<fb-menu-item index="3-3-2">3-3-2</fb-menu-item>
						<fb-menu-item index="3-3-3">3-3-3</fb-menu-item>
					</fb-sub-menu>
				</fb-sub-menu>

			</fb-menu>

		</fb-card>


	</fb-page-header>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
				menuActive1: '1',
				theme: 'light',
			}

		},
		watch: {
			menuActive1(newVal) {
				console.log('双向绑定menuActive1：' + newVal)
			}
		},
		methods: {
			handleClickItem(item) {
				console.log(item)
				this.$message.success('handleClickItem')
			},
			handleSelect(index, indexPath) {
				this.$message.success('handleSelect，index:' + index + 'indexPath:' + indexPath)
			},
			handleOpen(index, indexPath) {
				this.$message.success('handleOpen，index:' + index + 'indexPath:' + indexPath)
			},
			handleClose(index, indexPath) {
				this.$message.success('handleClose，index:' + index + 'indexPath:' + indexPath)
			}
		},
		mounted() {
		},

	}
</script>
```

### mode=vertical
```html run {title:'示例演示'}
<template>
	<fb-page-header
		title="menu"
		sub-title=""
	>

		<fb-card header="mode=vertical">
        
            <fb-menu v-model="menuActive2" mode="vertical" :theme="theme" style="width: 256px">
                <fb-menu-item index="1"><fb-icon name="earth"></fb-icon>Navigation One
                </fb-menu-item>
                <fb-menu-item index="2"><fb-icon name="pin"></fb-icon>Navigation Two
                </fb-menu-item>
                <fb-sub-menu index="3">
                    <span slot="title">
                        <fb-icon name="send"/>
                        <span>Navigation Three</span>
                    </span>
                    <fb-menu-item index="3-1">Navigation Three - 1</fb-menu-item>
                    <fb-menu-item index="3-2">Navigation Three - 1</fb-menu-item>
                    <fb-sub-menu index="3-3">
                        <span slot="title">
                            <fb-icon name="send"/>
                            <span>3-3</span>
                        </span>
                        <fb-menu-item index="3-3-1">3-3-1</fb-menu-item>
                        <fb-menu-item index="3-3-2">3-3-2</fb-menu-item>
                        <fb-menu-item index="3-3-3">3-3-3</fb-menu-item>
                    </fb-sub-menu>
                </fb-sub-menu>
            </fb-menu>

        </fb-card>
	</fb-page-header>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
				menuActive2: '1',
				theme: 'light',
			}

		},
		methods: {

		},
		mounted() {
		},

	}
</script>
```

### mode=inline
```html run {title:'示例演示'}
<template>
	<fb-page-header
		title="menu"
		sub-title=""
	>

		<fb-card header="mode=inline">
        
            <fb-menu mode="inline" :theme="theme"
                     background-color="#666"
                     text-color="rgba(255, 255, 255, 1)"
                     active-text-color="#f00"
                     :default-openeds="['4']" style="width: 256px">
                <fb-menu-item index="1"><fb-icon name="earth"></fb-icon>Navigation One
                </fb-menu-item>
                <fb-menu-item index="2"><fb-icon name="pin"></fb-icon>Navigation Two
                </fb-menu-item>
                <fb-sub-menu index="3">
                    <span slot="title">
                        <fb-icon name="send"/>
                        <span>Navigation 3</span>
                    </span>
                    <fb-menu-item index="3-1">3-1</fb-menu-item>
                    <fb-menu-item index="3-2">3-2</fb-menu-item>
                    <fb-sub-menu index="3-1">
                        <span slot="title">
                            <fb-icon name="send"/>
                            <span>3-1</span>
                        </span>
                        <fb-menu-item index="3-1-1">3-1-1</fb-menu-item>
                        <fb-menu-item index="3-1-2">3-1-2</fb-menu-item>
                    </fb-sub-menu>
                </fb-sub-menu>
                <fb-sub-menu index="4">
                    <span slot="title">
                        <fb-icon name="send"/>
                        <span>Navigation 4</span>
                    </span>
                    <fb-menu-item-group>
                        <span style="color: #ff0" slot="title">分组1</span>
                        <fb-menu-item index="4-1">4-1</fb-menu-item>
                    </fb-menu-item-group>
                    <fb-menu-item-group>
                        <span style="color: #0ff" slot="title">分组1</span>
                        <fb-menu-item index="4-2">4-2</fb-menu-item>
                    </fb-menu-item-group>
                </fb-sub-menu>
                <fb-sub-menu disabled index="5">
                    <span slot="title">
                        <fb-icon name="send"/>
                        <span>Navigation 5</span>
                    </span>
                    <fb-menu-item index="5-1">5-1</fb-menu-item>
                </fb-sub-menu>

            </fb-menu>

        </fb-card>
	</fb-page-header>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
				menuActive2: '1',
				theme: 'light',
			}

		},
		methods: {

		},
		mounted() {
		},

	}
</script>
```

### mode=inline inlineCollapse
```html run {title:'示例演示'}
<template>
	<fb-page-header
		title="menu"
		sub-title=""
	>
        <fb-card header="mode=inline inlineCollapse">
        
        			<fb-container mb="20px">
        				<fb-radio-group v-model="inlineCollapse" button :data="[
        					{label: '展开', value: false},
        					{label: '收起', value: true},
        				]"></fb-radio-group>
        			</fb-container>
        
        			<div style="width: 256px">
        				<fb-menu v-model="menuActive4" mode="inline" :inline-collapse="inlineCollapse" :theme="theme">
        					<fb-menu-item index="1">
        						<fb-icon name="earth"></fb-icon> <span slot="title">Navigation One</span>
        					</fb-menu-item>
        					<fb-menu-item index="2">
        						<fb-icon name="pin"></fb-icon> <span slot="title">Navigation Two</span>
        					</fb-menu-item>
        					<fb-sub-menu index="3">
        						<span slot="title">
        							<fb-icon name="send"/>
        							<span>Navigation Three</span>
        						</span>
        						<fb-menu-item index="3-1">3-1</fb-menu-item>
        						<fb-menu-item index="3-2">3-2</fb-menu-item>
        						<fb-sub-menu index="3-1">
        							<span slot="title">
        								<fb-icon name="send"/>
        								<span>3-1</span>
        							</span>
        							<fb-menu-item index="3-1-1">3-1-1</fb-menu-item>
        							<fb-menu-item index="3-1-2">3-1-2</fb-menu-item>
        						</fb-sub-menu>
        					</fb-sub-menu>
        
        				</fb-menu>
        			</div>
        
        		</fb-card>
	</fb-page-header>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
                menuActive4: '1',
				isShow: false,
				theme: 'light',
				inlineCollapse: true,
			}

		},
	}
</script>
```


## Menu 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value | 默认选中 v-model 双向绑定 | String | '' |
| mode | 模式(vertical, horizontal, inline) | string | vertical |
| inlineCollapse | 是否水平折叠收起菜单（仅在 mode 为 inline 时可用） | boolean | false |
| background-color | 菜单的背景色（仅支持 hex 格式） | String | #ffffff |
| text-color | 菜单的文字颜色（仅支持 hex 格式） | String | '' |
| active-text-color | 当前激活菜单的文字颜色（仅支持 hex 格式） | String | '' |
| default-openeds | 当前打开的 sub-menu 的 index 的数组 | Array | -- |
| unique-opened | 是否只保持一个子菜单的展开 | boolean | false |
| menu-trigger | 子菜单打开的触发方式(只在 mode 为 horizontal 时有效) hover / click | string | hover |
| collapse-transition | 是否开启折叠动画 | boolean | true |

## Menu Methods

| 事件名称 | 说明 | 参数 |
|:-----|:----|:-----|
| open | 展开指定的 sub-menu | index: 需要打开的 sub-menu 的 index |
| close | 收起指定的 sub-menu | index: 需要打开的 sub-menu 的 index |

## Menu Events

| 事件名称 | 说明 | 	回调参数 |
|:-----|:----|:-----|
| on-select | 菜单激活回调| index: 选中菜单项的 index, indexPath: 选中菜单项的 index path |
| on-open | sub-menu 展开的回调 | index: 打开的 sub-menu 的 index， indexPath: 打开的 sub-menu 的 index path |
| on-close | sub-menu 收起的回调 | index: 打开的 sub-menu 的 index， indexPath: 打开的 sub-menu 的 index path |

## SubMenu Attribute

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| index | 唯一标志 | String | '' |
| popper-class | 弹出菜单的自定义类名 | String | '' |
| show-timeout | 展开 sub-menu 的延时 | number | 300 |
| hide-timeout | 收起 sub-menu 的延时 | number | 300 |
| disabled | 是否禁用 | boolean | false |
| popper-append-to-body | 是否将弹出菜单插入至 body 元素。在菜单的定位出现问题时，可尝试修改该属性 | boolean | 一级子菜单：true / 非一级子菜单：false |


## Menu-Item Attribute

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| index | 唯一标志 | String | '' |
| disabled | 是否禁用 | boolean | false |

## Menu-Item-Group Attribute

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| title | 分组标题 | String/$slot | '' |
