<template>
	<div :class="getClass">
		<div :class="`${prefix}-empty__image`"></div>

		<div v-if="$slots.text" :class="`${prefix}-empty__text`">
			<slot name="text"></slot>
		</div>

		<div v-if="!$slots.text" :class="`${prefix}-empty__text`">{{text}}</div>

		<slot></slot>

	</div>
</template>

<script>

	import { prefix } from '../../../../src/config'
	/**
	 * FbEmpty
	 * (c) 2020 lincong1987
	 */
	export default {
		name: 'FbEmpty',
		props: {
			size: {
				type: String,
				default: 'm',
			},

			// 3种类型 data notice todo
			type: {
				type: String,
				default: 'data',
			},

			text: {
				type: String,
				default: '暂无数据',
			},

		},
		data () {
			return {
				prefix,
			}
		},
		computed: {
			getClass () {
				var arr = [`${prefix}-empty`]

				arr.push(`${prefix}-empty--${this.size}`)
				arr.push(`${prefix}-empty--${this.type}`)

				return arr
			},
		},
	}
</script>

<style scoped>

</style>
