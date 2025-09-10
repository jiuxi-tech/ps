<template>
	<div :class='getClass'
	     :style="getStyle"
		 :data-pk="rowPk"
	>
		<slot></slot>
	</div>
</template>

<script>
	/**
	 * FbRow
	 * (c) 2020 lincong1987
	 */

	import { prefix } from '../../../../src/config'

	export default {
		name: 'FbRow',
		props: {
			// 间距
			gutter: {
				type: [Number, String],
				default: 0,
			},
			// 垂直间距
			verticalGutter: {
				type: [Number, String],
				default: 0,
			},

			// flex
			flex: {
				type: Boolean,
				default: false,
			},
			/**
			 *  	flex 时 横向对齐, 可选值
			 *
					start	默认值。项目位于容器的开头。
					end	项目位于容器的结尾。
					center	    项目位于容器的中心。
					space-between	项目位于各行之间留有空白的容器内。
					space-around	项目位于各行之前、之间、之后都留有空白的容器内。
			 */
			justify: {
				type: String,
				default: 'start',
			},

			/**
			 * flex 时 纵向对齐 可选值 top middle bottom
			 */
			align: {
				type: String,
				default: 'top',
			},

			//justify="center" align="top"

			rowPk: {
				type: [String, Number],
				default: undefined,
			},
		},
		data () {
			return {
				prefix,
				myGutter: this.gutter,
				myVerticalGutter: this.verticalGutter,
			}
		},

		computed: {

			getClass () {
				let arr = [`${prefix}-row`]

				if (this.rowPk) {
					arr.push(`${prefix}-row--pk-${this.rowPk}`)
				}

				if (this.flex) {
					arr.push(`${prefix}-row--flex`)
					arr.push(`${prefix}-row--flex--${this.justify}`)
					arr.push(`${prefix}-row--flex--${this.align}`)
				}

				return arr
			},
			getStyle () {

				let style = {}
				let gutterGap = 0, verticalGutterGap = 0

				gutterGap = parseInt(this.myGutter, 10) / 2
				verticalGutterGap = parseInt(this.myVerticalGutter, 10) / 2

				style.marginLeft = `-${gutterGap}px`
				style.marginRight = `-${gutterGap}px`

				style.marginTop = `-${verticalGutterGap}px`
				style.marginBottom = `-${verticalGutterGap}px`

				return style
			},
		},

		watch: {
			gutter () {
				this.myGutter = this.gutter
			},
			verticalGutter () {
				this.myVerticalGutter = this.verticalGutter
			},
		},

	}
</script>

<style scoped>

</style>
