<template>
	<div @click="handleClick" :class="[`${prefix}-ranking-list-item`]" :style="[getItemStyle]">
		<slot></slot>
	</div>
</template>

<script>
	import {prefix} from '../../../../src/config'

	export default {
		name: "FbRankListItem",
		props: {
			height: {
				type: [Number, String],
				default: null
			}
		},
		data() {
			return {
				prefix
			}
		},
		computed: {
			getItemStyle() {
				let style = {}
				if (this.height) {
					style.height = this.unit(this.height)
				}
				return style
			}
		},
		methods: {
			handleClick (e) {
				this.$emit('on-click', e)
			},

			unit (val) {
				return typeof val === 'number' ? `${val}px` : val
			},
		},
	}
</script>

<style scoped>

</style>
