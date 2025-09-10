[comment]: <> (fb-docs: docsify/fb-ui/04/collapse/README.md)

# 折叠面板-Collapse
基础菜单布局


### 默认
```html run {title:'示例演示'}
<template>
	<fb-card header="默认">
    			<fb-collapse>
    				<fb-collapse-item title="111">
    					<p>111</p>
    				</fb-collapse-item>
    				<fb-collapse-item title="222">
    					<p>222</p>
    				</fb-collapse-item>
    			</fb-collapse>
    		</fb-card>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
			}
		},
		methods: {
		},
		mounted() {
		},

	}
</script>
```

### 手风琴
```html run {title:'示例演示'}
<template>
	<fb-card header="手风琴">
    			<fb-collapse accordion>
    				<fb-collapse-item title="111">
    					<p>111</p>
    				</fb-collapse-item>
    				<fb-collapse-item title="222">
    					<p>222</p>
    				</fb-collapse-item>
    			</fb-collapse>
    		</fb-card>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
			}
		},
		methods: {
		},
		mounted() {
		},

	}
</script>
```

### 自定义 collapseIcon expandIcon
```html run {title:'示例演示'}
<template>
	<fb-card header="自定义 collapseIcon expandIcon">
    			<fb-collapse v-model="arr" collapseIcon="add-normal" expandIcon="reduce-normal">
    				<fb-collapse-item title="111" itemKey="111">
    					<p>123123</p>
    				</fb-collapse-item>
    				<fb-collapse-item title="222" itemKey="222">
    					<p>123123</p>
    				</fb-collapse-item>
    			</fb-collapse>
    			<div>选中 itemKey {{ arr }}</div>
    		</fb-card>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
arr: [],
			}
		},
		methods: {
		},
		mounted() {
		},

	}
</script>
```

### 插槽 slot
```html run {title:'示例演示'}
<template>
	<fb-card header="slot">
    			<fb-collapse>
    				<fb-collapse-item itemKey="111" isActions>
    					<fb-text slot="title" color="red">1111</fb-text>
    					<template v-slot:actions="prop">
    						<fb-switch v-model="prop.isActive"></fb-switch>
    					</template>
    					<p>isIconSlot 障眼法模式</p>
    
    				</fb-collapse-item>
    				<fb-collapse-item title="222" itemKey="222" disabled>
    					<p>123123</p>
    				</fb-collapse-item>
    				<fb-collapse-item title="333" itemKey="333">
    					<p>333333</p>
    				</fb-collapse-item>
    			</fb-collapse>
    		</fb-card>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
			}
		},
		methods: {
		},
		mounted() {
		},

	}
</script>
```

### slot 数据驱动
```html run {title:'示例演示'}
<template>
	<fb-card header="slot 数据驱动">
    			<fb-collapse v-model="arr2">
    				<fb-collapse-item itemKey="111">
    					<fb-text slot="title" color="red">1111</fb-text>
    					<template v-slot:actions="prop">
    						<fb-switch :value="prop.isActive" @on-change="(val) => changeArr2(val, '111')"></fb-switch>
    					</template>
    					<p>数据驱动</p>
    				</fb-collapse-item>
    				<fb-collapse-item itemKey="222">
    					<fb-text slot="title" color="green">2222</fb-text>
    					<template v-slot:actions="prop">
    						<fb-switch :value="prop.isActive" @on-change="(val) => changeArr2(val, '222')"></fb-switch>
    					</template>
    					<p>数据驱动</p>
    				</fb-collapse-item>
    			</fb-collapse>
    		</fb-card>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
                arr2: []
			}
		},
		methods: {
            changeArr2(val, itemKey) {
				if (!val) {
					let index = this.arr2.indexOf(itemKey)
					if (index !== -1) {
						this.arr2.splice(index, 1)
					}
				} else {
					this.arr2.push(itemKey)
				}
			}
		},
		mounted() {
		},

	}
</script>
```


## Collapse 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| value/v-model | 默认选中 v-model 双向绑定 | String/Array | '' |
| accordion | 手风琴模式 | boolean | false |
| collapseIcon | 自定义折叠图标 | String | down |
| expandIcon | 自定义展开图标 | String | up |

## Menu Events

| 事件名称 | 说明 | 	回调参数 |
|:-----|:----|:-----|
| on-change | 切换面板的回调 | (activeKey: string/array) |

## Collapse-Item Attribute

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| title / slot:title | 面板头内容 | String | '' |
| itemKey | 唯一标识（绑定到Collapse的v-model中） | String | '' |
| disabled | 禁用 | Boolean | false |
| isActions | 是否是 slot=icon 开启简洁障眼法 | Boolean | false |
| collapseIcon | 自定义折叠图标 | String | down |
| expandIcon | 自定义展开图标 | String | up |

## Collapse-Item Slot

| 属性 | 说明 | 插槽属性 |
|:-----|:----|:-----|
| actions | 右侧控制插槽 | isActive: Boolean |

