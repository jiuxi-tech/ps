<template>
	<div :class="[
	  `${prefix}-link-group`,
	]">
		<template v-if="data">

			<fb-link
				v-for="(item, i) in data"
				:key="i"
				:label="item.label"
				:to="item.to"
				:href="item.href"
				:icon="item.icon"
				:separator="item.separator"
				:replace="item.replace"
				:target="item.target"
				:title="item.title"
				:size="item.size"
				:color="item.color"
				:underline="item.underline"
				:append="item.append"
				:click="item.click"
			></fb-link>

		</template>

		<template v-if="!data">
			<slot></slot>
		</template>
	</div>
</template>

<script>

	import { prefix } from '../../../../src/config'
	import FbLink from './FbLink'
	/**
	 * FbLinkGroup
	 * (c) 2020 lincong1987
	 */
	export default {
		name: 'FbLinkGroup',
		components: {FbLink},
		props: {
			size: {
				type: String,
				default: '',
				validate (val) {
					return ['s', 'm', 'l', 'xl'].includes(val)
				},
			},
			// 分割线
			separator: {
				type: String,
				default: undefined,
			},
			// 可以使用数据
			data: {
				type: [Array],
				default: undefined,
			},
			// 可以传一组路由进来
			routes: {
				type: [Array],
				default: undefined,
			},
			color: {
				type: String,
				default: undefined,
			},
			//  on off hover
			underline: {
				type: String,
				default: 'off',
			},

			append: {
				type: Boolean,
				default: false,
			},
		},
		data () {
			return {
				prefix,
			}
		}
	}
</script>

<style scoped>

</style>
