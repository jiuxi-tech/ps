<template>
	<div :class="getClass">

		<transition :name="transition">
			<div :class="`${prefix}-spin__container`" v-if="show">
				<fb-loading
					:color="color"
					:text-color="textColor"
					:size="size"
					:text="text"
					position="bottom"
					:spin-style="getStyle"
					:spinner="spinner"
				>
				</fb-loading>
			</div>
		</transition>

		<div v-if="$slots.default" :class="[
			`${prefix}-spin__slot`,
			{[`${prefix}-spin__slot--show`]: show}
		]">
			<slot></slot>
		</div>

	</div>
</template>

<script>
import FbLoading from '../../loading/src/FbLoading'
import { prefix } from '../../../../src/config'

/**
 * FbSpin
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbSpin',
	components: {FbLoading},
	props: {
		// 颜色
		color: {
			type: String,
			default: '#0284FE',
		},
		// 颜色
		textColor: {
			type: String,
			default: undefined,
		},
		// 文字大小
		size: {
			type: String,
			default: '24px',
		},
		// 圈的宽度
		borderWidth: {
			type: String,
			default: '2px',
		},
		// 显示文字
		text: {
			type: [String, Number],
			default: undefined,
		},
		// 锁定
		lock: {
			type: Boolean,
			default: false,
		},

		show: {
			type: Boolean,
			default: true,
		},
//			delay: {
//				type: Number,
//				default: 0,
//			},
		// 旋转器 icon
		spinner: {
			type: String,
			default: '',
		},

		transition: {
			type: String,
			default: 'zoom',
		},
	},
	data () {
		return {
			prefix,
//				myShow: this.show,
		}
	},

	watch: {
//			show () {
//				if(this.delay > 0) {
//
//				}
//			},
	},

	computed: {
		getClass () {
			var arr = [`${prefix}-spin`]

			if (this.lock) {
				arr.push(`${prefix}-spin--lock`)
			}

			return arr
		},
		getStyle () {
			// {marginLeft: parseInt(size, 10)}
			const style = {
				marginLeft: `-${parseInt(this.size, 10) / 2}px`,
//					marginTop: `-${parseInt(this.size, 10) / 2}px`,
				marginRight: 0,
			}
			return style
		},
	},
	mounted () {
//			if (this.delay > 0) {
//
//			}
	},
}
</script>

<style scoped>

</style>
