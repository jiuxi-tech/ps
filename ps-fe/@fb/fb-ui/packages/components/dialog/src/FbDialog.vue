<template>

	<div v-show="isShow"
		 :class="[`${prefix}-dialog-wrapper`, { scroll: isScroll, 'full-screen': isFullScreen, lock: lock }]"
		 :style="{...wrapperStyle}"
		 @scroll="scroll"
		 ref="dialogWrapper"

	>

		<transition name="zoom-center-to-corner">
			<div ref="dialog"
				 v-dialog-dnd="drag"
				 tabindex="-1"
				 v-if="isShow"
				 :class="[`${prefix}-dialog`, {[`${prefix}-dialog--no-padding`]: noPadding}]"
				 :style="{visibility: visibility?'visible':'hidden',top: (isFullScreen ? 0 :myTop), left: isFullScreen?0: getSizeStyle(myLeft), width: isFullScreen?'100%':getSizeStyle(myWidth), ...dialogStyle}"
			>
				<div :class="`${prefix}-dialog-header`">

					<div :class="`${prefix}-dialog-header__title`">
						<span :class="`${prefix}-dialog-header__title__main`">
							<slot name="title">{{ myTitle }}</slot>
						</span>
						<span :class="`${prefix}-dialog-header__title__sub`">
							<slot name="subTitle">{{ subTitle }}</slot>
						</span>
					</div>

					<div :class="`${prefix}-dialog-header__actions`">
						<fb-icon v-if="canFullScreen" @on-click="fullScreen" name="maximize"/>
						<fb-icon v-if="showCloseBtn" @on-click="handleClose" name="close"/>
					</div>

				</div>
				<div :style="getStyle" :class="`${prefix}-dialog-body`">
					<component v-bind:is="isComponent" :data="myComponentData" :param="myComponentData"></component>
					<slot/>
				</div>
				<div
					v-if="resize === true"
					v-dialog-resize="this"
					style="height: 10px; width: 10px; cursor: nwse-resize; position: absolute; right: 1px; bottom: 1px; border-radius: 3px"></div>
			</div>
		</transition>
	</div>

</template>

<script>
/**
 * FbDialog
 * (c) 2020 lincong1987
 */
import { prefix } from '../../../../src/config'
import { addClass, removeClass } from '../../../utils/utils'
//import resize from '../../../utils/resize'
import FbIcon from '../../icon/src/FbIcon'
import { each, isFunction } from 'lodash-es'
//import drag from '../../../directives/drag'
import { getSizeStyle } from '../../../utils/propUtils'
import FbButton from '../../button/src/FbButton'

import DialogDnd from '../../../directives/dialogDnd'
import DialogResize from '../../../directives/dialogResize'

export default {
	name: 'FbDialog',
	directives: {
//		drag,

		DialogDnd,
		DialogResize,
	},
	components: {
		FbButton,
		FbIcon,
	},
	props: {
		value: {
			type: Boolean,
			default: false,
		},
		// 标题
		title: {
			type: String,
			default: '信息窗口',
		},
		// 子标题
		subTitle: {
			type: String,
			default: undefined,
		},
		// 宽度
		width: {
			type: [Number, String],
			default: 800,
		},

		// 高度
		height: {
			type: [Number, String],
			default: 600,
		},

		// 点击阴影部分关闭窗口
		closeOnClickShadow: {
			type: Boolean,
			default: false,
		},
		// 按ESC键关闭窗口
		disableEsc: {
			type: Boolean,
			default: false,
		},
		// 最大化按钮
		canFullScreen: {
			type: Boolean,
			default: false,
		},
		// 是否显示关闭按钮
		showCloseBtn: {
			type: Boolean,
			default: true,
		},
		// 关闭前的回调
		beforeClose: {
			type: Function,
			default: undefined,
		},
		// mounted 自动显示
		autoShow: {
			type: Boolean,
			default: false,
		},
		// 锁屏
		lock: {
			type: Boolean,
			default: true,
		},
		loader: {
			type: Function,
			default: undefined,
		},

		noPadding: {
			type: Boolean,
			default: false,
		},

		top: {
			type: [String, Number],
			default: '15vh',
		},

		left: {
			type: [String, Number],
			default: 0,
		},

		overflow: {
			type: [String],
			default: '',
		},

		dialogStyle: {
			type: [String, Array, Object],
			default () {
				return {}
			},
		},
		wrapperStyle: {
			type: [String, Array, Object],
			default () {
				return {}
			},
		},

		drag: {
			type: Boolean,
			default: true,
		},

		resize: {
			type: Boolean,
			default: true,
		},
		inBody: {
			type: Boolean,
			default: true,
		},
	},
	data () {

		// let {defaultProps: {FbDialog: defaultProps}} = this
		let {defaultProps} = this

		let myLeft = 0
		let myTop = 0
		let docScrollLeft = 0
		let docScrollTop = 0

		let left = (window.innerWidth - this.width) / 2 + docScrollLeft
		let top = (window.innerHeight - this.height) * 382 / 1000 + docScrollTop // 黄金比例

		myLeft = Math.max(parseInt(left, 10), docScrollLeft) + 'px'
		myTop = Math.max(parseInt(top, 10), docScrollTop) + 'px'
		return {
			defaultProps,
			prefix,
			isComponent: this.defaultComponent,
			myUrl: null,
			myComponent: null,
			myComponentData: null,
			myTitle: this.title,
			myWidth: this.width,
			myHeight: this.height,
			myBeforeClose: this.beforeClose,
			isShow: false,
			isScroll: false,
			isFullScreen: false,
			visibility: false,
			myTop: myTop,
			myLeft,

//				dragOptions: {
//					handle: `.${this.prefix}-dialog-header`,
//					start () {
//					},
//					drag () {
//					},
//					stop () {
//					},
//				},
		}
	},

	computed: {
		getStyle () {

			let style = {}

			if (this.myWidth) {
				style.width = `${this.myWidth}px`
			}

			if (this.myHeight) {
				style.height = `${this.myHeight}px`
			}

			if (this.isFullScreen) {

				if (!this.isScroll) {
					style.width = `100%`
					style.height = `100%`
				}
				style.width = `100%`
				style.height = `100%`
			}

			if (this.overflow) {
				style.overflow = this.overflow
			}

			return style
		},
	},
	watch: {

		top (value) {

			this.myTop = value
		},

		title (value) {
			this.myTitle = value
		},

		width (value) {
			this.myWidth = value
		},

		height (value) {
			this.myHeight = value
		},

		isShow () {
			const body = document.body
			if (this.isShow) {
				// 必须这样，要不然，这个点击事件会触发
				if (this.closeOnClickShadow) {
					setTimeout(() => {
						this.$el.addEventListener('click', this.clickFn, false)
					}, 0)
				}
				addClass(body, `${prefix}-dialog-open`)

				// 当dialog中的内容超出整个屏幕时，dialog用absolute定位不能撑开滚动，导致看不全，需要特殊处理
				this.$nextTick(this.computeScroll)
			} else {
				removeClass(body, `${prefix}-dialog-open`)
				if (this.closeOnClickShadow) {
					this.$el.removeEventListener('click', this.clickFn, false)
				}
			}
		},
	},
	created () {
		this.$on('on-dialog-footer', this.addFooter)
	},
	mounted () {
		// 监听esc
		document.addEventListener('keydown', this.esc, false)
		//resize(this.$refs.dialog, this.computeScroll)

		if (this.autoShow) {
			this.show()
		}

		// if (this.$root.$el) {
		// 	body.appendChild(this.$el);
		// }

		if (this.inBody) {
			document.body.appendChild(this.$el)
		}
	},
	beforeDestroy () {
		document.removeEventListener('keydown', this.esc, false)
		//.unbind(this.$refs.dialog)

		this.$off('on-dialog-footer')

		// if (this.$root.$el) {
		// 	this.$root.$el.removeChild(this.$el);
		// }
	},
	destroyed () {
		if (this.inBody && this.$el && this.$el.parentNode) {
			console.log('destroyed>removeChild')
			this.$el.parentNode.removeChild(this.$el)
		}
	},
	methods: {
		getSizeStyle,
		computeScroll () {
			const bodyHeight = window.innerHeight
			const dialogElemStyle = window.getComputedStyle(this.$refs.dialog, null)
			const height = /^([0-9]*)/.exec(dialogElemStyle.height)[0]
			if (height > bodyHeight - 50) {
				this.isScroll = true
			} else {
				this.isScroll = false
			}
		},
		esc (event) {
			// 禁止esc键
			if (this.disableEsc) return
			const which = event.which || event.keyCode

			if (this.isShow && which == 27) {
				// 若dialog已全屏，esc先取消全屏
				if (this.isFullScreen) {
					return this.fullScreen()
				}
				this.handleClose()
			}
		},
		clickFn (event) {
			if (event.target === this.$el && this.isShow) {
				this.handleClose()
			}
		},
		async handleClose (data) {
			// myBeforeClose 关闭前钩子函数，可以返回 promise
			let close = true
			if (this.myBeforeClose && isFunction(this.myBeforeClose)) {
				try {
					close = await this.myBeforeClose(data)
					// 默认输出为true
					if (close == undefined) {
						close = true
					}
				} catch (e) {
					// 抛出异常信息
					console.error(e)
					close = false
				}
			}

			if (close) {
				this.close()
			}
		},
		close () {
			this.myComponent = null
			this.isShow = false
			this.$emit('on-close')
		},
		hide (data) {
			this.handleClose(data)
		},
		scroll (event) {
			this.$emit('on-scroll', event)
		},
		show (options) {

			return new Promise((resolve, reject) => {

				this.visibility = false
				this.isShow = true

				this.$nextTick(() => {

					this.defaultProps.zIndex++

					//$refs.dialog.style.visibility = 'hidden'

					if (options) {
						this.myComponentData = options.data

						if (options.component) {
							this.isComponent = options.component
						} else if (options.url) {
							let url = []
							if (options.url.startsWith('.')) {
								// 根据当前路由，计算打开页面的路径
								let routes = this.$route.path.split('/')
								options.url.split('/').forEach((n, i) => {
									// 以./ ../开头，向前进一格
									if (n.startsWith('.')) {
										routes.pop()

										// ../ 第一次，需要向前进两格
										if (n == '..' && i == 0) {
											routes.pop()
										}
									} else {
										url.push(n)
									}
								})

								url = routes.concat(url)
								url = url.join('/')

								// 以.vue结尾
								if (!url.endsWith('.vue')) {
									url += '.vue'
								}

								// console.log('[FbDialog] show `@/views' + url)
							}

							this.isComponent = () => import(`@/views${url}`)

							// 通过URL打开，默认以.vue中定义的为准
							this.myTitle = ''
						}

						this.myTitle = options.title
						this.myBeforeClose = options.close
					}

					//	this.$nextTick(() => {

					let myLeft = 0
					let myTop = 0
					let docScrollLeft = 0
					let docScrollTop = 0

//				let {
//					width: pWidth,
//					height: pHeight,
//				} = this.$refs.dialog.parentElement.getBoundingClientRect()

					let pStyle = this.$refs.dialogWrapper.currentStyle ||
						window.getComputedStyle(this.$refs.dialogWrapper, null)

					let pWidth = parseFloat(pStyle.width)
					let pHeight = parseFloat(pStyle.height)

////             // 此处存在一个问题，当 dialog 嵌套，并且在极短时间内顺序打开

//					console.log('pWidth,\n' +
//						'\t\t\t\t\tpHeight,', pWidth,
//						pHeight)
//
//					setTimeout(()=>{
//						let pStyle = this.$refs.dialogWrapper.currentStyle ||
//							window.getComputedStyle(this.$refs.dialogWrapper, null)
//
//						let pWidth = parseFloat(pStyle.width)
//						let pHeight = parseFloat(pStyle.height)
//						console.log('setTimeout pWidth,\n' +
//							'\t\t\t\t\tpHeight,', pWidth,
//							pHeight)
//					},1000)

					let width = typeof this.myWidth === 'number'
						? this.myWidth
						: (this.myWidth.includes('%')
							? (pWidth * parseFloat(this.myWidth) / 100) //this.$refs.dialog.getBoundingClientRect().width
							: parseFloat(this.myWidth))

					let height = typeof this.myHeight === 'number'
						? this.myHeight
						: (this.myHeight.includes('%')
							? (pHeight * parseFloat(this.myHeight) / 100)
							: parseFloat(this.myHeight))

					let left = (pWidth - width) / 2 + docScrollLeft
					let top = (pHeight - height) * 382 / 1000 + docScrollTop // 黄金比例

					//	// console.log(left, top)
					this.myLeft = Math.max(parseFloat(left), docScrollLeft) + 'px'
					this.myTop = Math.max(parseFloat(top), docScrollTop) + 'px'
					this.visibility = true

					window.dispatchEvent(new Event('resize'))
					this.handleResize()

					resolve()
				})
			})
			//	})

		},
		fullScreen () {
			this.isFullScreen = !this.isFullScreen

			setTimeout(() => {
				window.dispatchEvent(new Event('resize'))
				this.handleResize()
			}, 160)
		},

		handleResize () {
			this.$emit('on-resize', {
				left: this.myLeft,
				top: this.myTop,
				height: this.myHeight,
				width: this.myWidth,
			})
		},
	},
}
</script>

<style scoped>

</style>
