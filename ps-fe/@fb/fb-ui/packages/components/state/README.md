[comment]: <> (fb-docs: docsify/fb-ui/04/state/README.md)

# 状态点-FbState

## 基础用法

```html run {title:'示例演示'}

<template>
	<div>
		<fb-card header="类型 type">
			<fb-space size="16">
				<fb-state></fb-state>
				<fb-state type="primary"></fb-state>
				<fb-state type="success"></fb-state>
				<fb-state type="warning"></fb-state>
				<fb-state type="danger"></fb-state>
				<fb-state type="info"></fb-state>
			</fb-space>
		</fb-card>
	</div>
</template>
<script>
	export default {
		data () {
			return {}
		},
	}
</script>
<style>
</style>
```

## 更多用法

```html run {title:'示例演示'}

<template>
	<div>
		<fb-card header="尺寸 size">
			<fb-space size="16">
				<fb-state></fb-state>
				<fb-state type="info" size="xxs"></fb-state>
				<fb-state type="primary" size="xs"></fb-state>
				<fb-state type="success" size="s"></fb-state>
				<fb-state type="warning" size="m"></fb-state>
				<fb-state type="danger" size="l"></fb-state>
				<fb-state type="info" size="xl"></fb-state>
				<fb-state type="danger" size="xxl"></fb-state>
				<fb-state type="danger" size="5"></fb-state>
				<fb-state type="danger" size="15"></fb-state>
				<fb-state type="danger" size="30"></fb-state>
				<fb-container border="1px solid red" width="100px" height="100px">
					<fb-state type="primary" size="50%" active ml="25%" mt="25%"></fb-state>
				</fb-container>
				<fb-container border="1px solid red" width="40px" height="40px">
					<fb-state type="primary" size="50%" active ml="25%" mt="25%"></fb-state>
				</fb-container>
			</fb-space>
		</fb-card>

		<fb-card header="激活 active">
			<fb-space size="16">
				<fb-state></fb-state>
				<fb-state type="info" size="xxs" active></fb-state>
				<fb-state type="primary" size="xs" active></fb-state>
				<fb-state type="success" size="s" active></fb-state>
				<fb-state type="warning" size="m" active></fb-state>
				<fb-state type="danger" size="l" active></fb-state>
				<fb-state type="info" size="xl" active></fb-state>
				<fb-state type="danger" size="xxl" active></fb-state>

			</fb-space>


		</fb-card>


		<fb-card header="与文本共用">

			active:
			<fb-switch v-model="active" size="s">active</fb-switch>

			<fb-container margin="20px 0">
				<fb-state type="primary" size="8" :active="active" mr="4px"></fb-state>
				<fb-text size="16" va="middle">你如果认识从前的我，也许你会原谅现在的我。</fb-text>
			</fb-container>

			<fb-container margin="20px 0">
				<fb-state size="12" type="warning" :active="active" mr="8px"></fb-state>
				<fb-text size="24" bold va="middle">你问我爱你值不值得，其实你应该知道，爱就是不问值得不值得。</fb-text>
			</fb-container>

			<fb-container margin="20px 0" padding="0 12px" width="240px" ellipsis>
				<fb-state type="danger" :active="active" mt="-4px" mr="4px"></fb-state>
				<fb-text>最遥远的距离不是爱也不是恨，而是熟悉的人渐渐变得陌生。</fb-text>
			</fb-container>

			<fb-container margin="20px 0">
				<fb-state type="info" :active="active" mt="-4px" mr="4px"></fb-state>
				<fb-text>向来心是看客心，奈何人是剧中人。</fb-text>
			</fb-container>

		</fb-card>

		<fb-card header="颜色 color">
			支持 颜色名称如 red，yellow等，页支持HEX，如 #f0c2e9, #000等，同时可以使用rgb格式
			<fb-space size="16">
				<fb-state type="primary" color="red"></fb-state>
				<fb-state color="yellow"></fb-state>
				<fb-state color="blue"></fb-state>
				<fb-state color="#f0c2e9"></fb-state>

				<fb-state size="18" color="rgba(91, 12, 142,66%)" active></fb-state>

			</fb-space>
		</fb-card>

	</div>
</template>
<script>
	export default {
		data () {
			return {
             active: true
            }
		},
	}
</script>
<style>
</style>
```

## 事件列表

| 事件名      | 说明  | 返回值          |     |
|:---------|:----|:-------------|:----|
| on-click | 点击  | (event)=>{}  |     |

## 属性列表

| 属性     | 说明                                                                        | 类型      | 默认值       |
|:-------|:--------------------------------------------------------------------------|:--------|:----------|
| type   | 类型，可选值 <br />'primary','success','warning','danger','info'                | String  | 'default' |
| size   | 尺寸，支持 'xs','s','m','l','xl','xxl'或 5px,7px这样的px单位也支持 数字或百分比               | String  | 'm'       |
| color  | 颜色，支持颜色名称如 red，yellow等，也支持HEX，如 #f0c2e9, #000等，同时可以使用rgb格式，此项的优先级比type属性高 | String  | null      |
| active | 是否激活，激活时会有涟漪效果                                                            | Boolean | false     |
| mr     | 右侧外边距                                                                     | String  | null      |
| ml     | 左侧外边距                                                                     | String  | null      |
| mt     | 上方外边距                                                                     | String  | null      |
| mb     | 下方外边距                                                                     | String  | null      |

