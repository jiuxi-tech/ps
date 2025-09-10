<template>
	<div :class="getCardClass" :style="getCardStyle" @click.stop="handleClick">
		<div :class="`${prefix}-cardx-breath--title`" v-if="title">{{title}}</div>
		<div :class="`${prefix}-cardx-breath--content`">
			<slot></slot>
		</div>
		<slot name="box"></slot>
	</div>
</template>

<script>

	import { prefix } from '../../../../src/config'
	import { generateId } from '../../../utils/utils'
	/**
	 * FbCard
	 * (c) 2020 lincong1987
	 */

	import { isArray, isObjectLike, isString } from 'lodash-es'

	export default {
		name: 'FbCardxBreath',
		props: {
			title: {
				type: [String],
				default: null,
			},
			// 文字省略
			ellipsis: {
				type: Boolean,
				default: false,
			},

			// 四种类型 primary success danger warning
			type: {
				type: [String],
				default: 'primary',
			},
			// 设计稿 class 命名专用
			cardStatus: {
				type: [String],
				default: '',
			},

			bgColor: {
				type: [String],
				default: null,
			},

			textColor: {
				type: [String],
				default: null,
			},
			// 呼吸动画开启？
			breath: {
				type: Boolean,
				default: false,
			},
			// 呼吸动画 花费的时间 n/s
			breathDuration: {
				type: [String],
				default: '1s',
			},
			// 呼吸动画 延迟时间执行 n/s
			breathDelay: {
				type: [String],
				default: '0s',
			},
			// 呼吸动画次数
			breathCount: {
				type: [String, Number],
				default: '5',
			},
			// 呼吸 一直高亮
			breathLight: {
				default: undefined
			},

			// 呼吸动画名称
			breathName: {
				type: [String],
				default: '',
			},
			// 隐藏功能 呼吸颜色 <不推荐使用--随机数插入key 多了可能产生样式污染>
			breathColor: {
				type: [String],
				default: null,
			},
		},

		data () {
			return {
				prefix,
				typeObj: {
					aniName: {
						primary: 'breath__primary',
						success: 'breath__success',
						danger: 'breath__danger',
						warning: 'breath__warning',
					},
				}
			}
		},

		computed: {
			getCardClass () {
				let cl = [`${prefix}-cardx-breath`]

				if (this.ellipsis) {
					cl.push(`ellipsis`)
				}

				if (this.type) {
					cl.push(`${prefix}-cardx-breath__${this.type}`)
				}

				if (this.cardStatus) {
					cl.push(`${prefix}-cardx-breath__${this.cardStatus}`)
				}

				if (this.breathLight) {
					cl.push(`${prefix}-cardx-breath__light`)
				}



				return cl
			},

			getCardStyle () {
				let style = {}
				let aniName = ''

				if (this.type) {
					aniName = this.typeObj.aniName[this.type]
				}

				if (this.bgColor) {
					style['backgroundColor'] = this.bgColor
				}

				if (this.textColor) {
					style['color'] = this.textColor
				}

				if (this.breathName) {
					aniName = this.breathName
				}

				if (this.breathColor) { // 不推荐
					try {
						let styleSheet = document.styleSheets[1]
						let name = 'breath__' + generateId()
						if (styleSheet && styleSheet.insertRule) {
							styleSheet.insertRule(
								`@keyframes ${name}{ 100% {box-shadow: 0px 0px 10px 0px ${this.breathColor};border:1px solid ${this.breathColor};}}`,
								styleSheet.cssRules.length
							)
						}
						aniName = name
					} catch (e) {
						// console.log(e)
					}
				}

				if (this.breath) {
					style['animation'] = `${aniName} ${this.breathDuration} ease-in ${this.breathDelay} ${this.breathCount}`
				}

				// if (this.breathLight) {
				// 	style['animation'] = `breath__light ease-in 1`
				// 	style['animation-fill-mode'] = `forwards`
				// }

				return style
			},
		},

		methods: {
			handleClick () {
				this.$emit('on-click')
			}
		}
	}
</script>

<style scoped>

</style>
