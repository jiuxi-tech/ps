<template>
	<div :class='getClass' :style="getStyle">
		<slot></slot>
	</div>
</template>

<script>

import { prefix } from '../../../../src/config'
/**
 * FbCol
 * (c) 2020 lincong1987
 */

import { closest } from '../../../utils/componentUtils'

/**
 * @description asdas
 * @param order flex 时 每个col的 排序值, 从小到大排列
 */
export default {

	name: 'FbCol',

	props: {
		// 拼接, 可选值 从 1-24, 24 的意思是一行占24格, 也就满了
		span: {
			type: [String, Number],
			default: 8,
		},
		// 向右偏移值 可选值 从 1-24, 比如 fb-col span=1 offset=23 那这个格子就在最右边了
		offset: {
			type: [String, Number],
			default: undefined,
		},
		// flex 时 每个col的 排序值, 从小到大排列
		order: {
			type: [String, Number],
			default: undefined,
		},
		/**
		 * flex 填充
		 * 规则 1
		 * fb-col flex=2
		 * fb-col flex=3
		 * 如果是这样, 第一个的宽度就是 2/5 如果有3个col 那就是 a/(a+b+c)
		 *
		 * 规则2
		 * fb-col flex=100px
		 * fb-col flex=auto
		 * 这样的话 前一个定宽,后面的自动撑开
		 *
		 * 规则3 flex属性是 flex-grow, flex-shrink 和 flex-basis的简写，默认值为0 1 auto。后两个属性可选。
		 * flex-grow属性定义项目的放大比例，默认为0，即如果存在剩余空间，也不放大
		 * 如果所有项目的flex-grow属性都为1，则它们将等分剩余空间（如果有的话）。如果一个项目的flex-grow属性为2，其他项目都为1，则前者占据的剩余空间将比其他项多一倍。
		 *
		 * flex-shrink属性定义了项目的缩小比例，默认为1，即如果空间不足，该项目将缩小。
		 * 如果所有项目的flex-shrink属性都为1，当空间不足时，都将等比例缩小。如果一个项目的flex-shrink属性为0，其他项目都为1，则空间不足时，前者不缩小。
		 * 负值对该属性无效。
		 *
		 * flex-basis属性定义了在分配多余空间之前，项目占据的主轴空间（main size）。浏览器根据这个属性，计算主轴是否有多余空间。它的默认值为auto，即项目的本来大小。
		 * 它可以设为跟width或height属性一样的值（比如350px），则项目将占据固定空间。
		 *
		 * fb-col flex=1 1 200px
		 * fb-col flex=1 0 300px
		 *
		 * 看不懂先去看教程
		 * @link http://www.ruanyifeng.com/blog/2015/07/flex-grammar.html
		 */
		flex: {
			type: [String, Number],
			default: undefined,
		},

		// 响应式
		s: {
			type: [String, Number],
			default: undefined,
		},
		m: {
			type: [String, Number],
			default: undefined,
		},
		l: {
			type: [String, Number],
			default: undefined,
		},
		xl: {
			type: [String, Number],
			default: undefined,
		},

	},

	data () {
		return {
			prefix,

			myGutter: 0,
			myVerticalGutter: 0,
		}
	},

	created () {
		// 向上查找第一级
		this.fbRow = closest(this, 'FbRow', 1)
	},
	beforeDestroy () {
		this.fbRow = null
	},
	computed: {

		getStyle () {
			let style = {}
			let gutterGap = 0, verticalGutterGap = 0

			gutterGap = parseInt(this.myGutter, 10) / 2
			verticalGutterGap = parseInt(this.myVerticalGutter, 10) / 2

			style.paddingLeft = `${gutterGap}px`
			style.paddingRight = `${gutterGap}px`

			style.paddingTop = `${verticalGutterGap}px`
			style.paddingBottom = `${verticalGutterGap}px`

			if (this.order) {
				style.order = this.order
			}

			if (this.flex) {
				style.flex = this.flex
			}

			return style
		},

		getClass () {
			const arr = [`${prefix}-col`, `${prefix}-col--span-${this.span}`]

			if (this.offset) {
				arr.push(`${prefix}-col--offset-${this.offset}`)
			}

			if (this.s) {
				arr.push(`${prefix}-col--span-s-${this.s}`)
				if (this.offset) {
					arr.push(`${prefix}-col--offset-s-${this.offset}`)
				}
			}
			if (this.m) {
				arr.push(`${prefix}-col--span-m-${this.m}`)
				if (this.offset) {
					arr.push(`${prefix}-col--offset-m-${this.offset}`)
				}
			}

			if (this.l) {
				arr.push(`${prefix}-col--span-l-${this.l}`)
				if (this.offset) {
					arr.push(`${prefix}-col--offset-l-${this.offset}`)
				}
			}

			if (this.xl) {
				arr.push(`${prefix}-col--span-xl-${this.xl}`)
				if (this.offset) {
					arr.push(`${prefix}-col--offset-xl-${this.offset}`)
				}
			}
			return arr
		},

	},

	watch: {},

	mounted () {

		if (this.fbRow) {
			if (this.fbRow.myGutter) {
				this.myGutter = this.fbRow.myGutter
			}

			if (this.fbRow.myVerticalGutter) {
				this.myVerticalGutter = this.fbRow.myVerticalGutter
			}
		}
	},

}
</script>

<style scoped>

</style>
