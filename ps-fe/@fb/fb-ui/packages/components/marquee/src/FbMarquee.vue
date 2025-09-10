<template>
	<div
		v-if="ready"
		:class="[`${prefix}-marquee`, { vertical: vertical, horizontal: !vertical }]"
		:style="getCurrentStyle"
		:key="componentKey"
		@mouseenter="hoverStarted"
		@mouseleave="hoverEnded"
		@mousedown="mouseDown"
		@mouseup="mouseUp"
	>
		<div
			class="transparent-overlay"
			ref="marqueeOverlayContainer"
			:aria-hidden="true"
		></div>
		<div
			v-if="showGradient"
			:aria-hidden="true"
			class="overlay"
			:class="{ vertical: vertical, horizontal: !vertical }"
		></div>
		<div class="marquee" ref="marqueeContent">

			<slot></slot>
		</div>
		<div class="marquee" :aria-hidden="true">
			<slot></slot>
		</div>

		<div
			:aria-hidden="true"
			class="marquee cloned"
			v-for="num in cloneAmount"
			:key="num"
		>
			<slot></slot>
		</div>
	</div>
</template>

<script>
	/**
	 * FbMarquee
	 * 运用 css3 动画等变量行特性，不支持 ie 浏览器
	 * (c) 2021 lincong1987
	 */

	import {prefix} from '../../../../src/config'

	export default {
		name: 'FbMarquee',
		props: {
			vertical: {
				type: Boolean,
				default: false
			},
			direction: {
				type: String,
				default: 'normal'
			},
			duration: {
				type: Number,
				default: 20,
			},
			delay: {
				type: Number,
				default: 0,
			},
			loop: {
				type: Number,
				default: 0,
			},
			clone: {
				type: Boolean,
				default: false
			},
			gradient: {
				type: Boolean,
				default: false
			},
			gradientColor: {
				type: [Object, Array, String],
				default: () => [255, 255, 255],
			},
			gradientWidth: {
				type: String,
			},
			pauseOnHover: {
				type: Boolean,
				default: false
			},
			pauseOnClick: {
				type: Boolean,
				default: false
			},
		},
		data() {
			return {
				prefix,
				cloneAmount: 0,
				minWidth: '100%',
				minHeight: '100%',
				componentKey: 0,
				pauseAnimation: false,
				containerWidth: 0,
				contentWidth: 0,
				containerHeight: 0,
				contentHeight: 0,
				loopCounter: 0,
				loopInterval: null,
				gradientLength: '200px',
				ready: false,
			}
		},
		computed: {
			getCurrentStyle() {
				const cssVariables = {
					'--duration': `${this.duration}s`,
					'--delay': `${this.delay}s`,
					'--direction': `${this.direction}`,
					'--pauseOnHover': `${this.pauseOnHover ? 'paused' : 'running'}`,
					'--pauseOnClick': `${this.pauseOnClick ? 'paused' : 'running'}`,
					'--pauseAnimation': `${this.pauseAnimation ? 'paused' : 'running'}`,
					'--loops': `${this.loop === 0 ? 'infinite' : this.loop}`,
					'--gradient-color': `rgba(${this.gradientColor[0]}, ${this.gradientColor[1]}, ${this.gradientColor[2]}, 1), rgba(${this.gradientColor[0]}, ${this.gradientColor[1]}, ${this.gradientColor[2]}, 0)`,
					'--gradient-length': `${this.gradientLength}`,
					'--min-width': `${this.minWidth}`,
					'--min-height': `${this.minHeight}`,
				}

				const animationStyles = {
					'--orientation': 'scrollX',
					orientation: 'horizontal',
				}

				if (this.vertical) {
					animationStyles['--orientation'] = 'scrollY'
				}

				const styles = {
					...cssVariables,
					...animationStyles,
				}

				return styles
			},
			showGradient() {
				if (this.gradient) {
					return true
				}
				return false
			}
		},
		watch: {
			contentWidth() {
				if (this.clone) {
					this.ForcesUpdate()
				}
			},
			containerWidth() {
				if (this.clone) {
					this.ForcesUpdate()
				}
			}
		},
		mounted() {
			this.init()
		},
		beforeDestroy() {
			clearInterval(this.loopInterval)
		},
		methods: {
			init() {
				this.setupMarquee()

				this.loopInterval = setInterval(() => {
					this.loopInterval++

					if (this.loop !== 0 && this.loopCounter === this.loop) {
						this.$emit('onComplete')
						clearInterval(this.loopInterval)
					}

					this.$emit('onLoopComplete')

					// Converting the duration into milliseconds here
				}, this.duration * 1000)
			},
			setupMarquee() {
				if (this.vertical) {
					this.minHeight = '100%'
					this.minWidth = 'auto'
				} else {
					this.minHeight = 'auto'
					this.minWidth = '100%'
				}

				// Deprecate the gradientWidth prop in favor of gradientLength
				if (this.gradient) {
					if (this.gradientWidth) {
						this.gradientLength = this.gradientWidth
					} else if (this.gradientLength) {
						this.gradientLength = this.gradientLength
					}
				}

				if (this.clone) {
					this.$nextTick(() => {
						this.ready = true
						this.checkForClone()
						this.ForcesUpdate()
					})
				} else {
					this.ready = true
				}
			},
			async ForcesUpdate() {
				await this.checkForClone()

				this.componentKey++
			},
			checkForClone() {
				if (this.vertical) {
					// pause the animation to prevent flickering
					this.pauseAnimation = true
				}

				setInterval(() => {
					this.$nextTick(() => {
						let marqueeContent = this.$refs['marqueeContent']
						let marqueeOverlayContainer = this.$refs['marqueeOverlayContainer']
						this.minWidth = '0%'
						this.minHeight = '0%'

						if (
							marqueeContent !== null &&
							marqueeOverlayContainer !== null
						) {

							if (marqueeContent && marqueeOverlayContainer) {
								if (
									this.vertical &&
									'clientHeight' in marqueeContent &&
									'clientHeight' in marqueeOverlayContainer
								) {
									this.contentHeight = marqueeContent.clientHeight
									this.containerHeight = marqueeOverlayContainer.clientHeight

									const localCloneAmount = Math.ceil(
										this.containerHeight / this.contentHeight,
									)

									this.cloneAmount = isFinite(localCloneAmount)
										? localCloneAmount
										: 0

									// resume the animation
									this.pauseAnimation = false

									return this.cloneAmount
								} else if (
									!this.vertical &&
									'clientWidth' in marqueeContent &&
									'clientWidth' in marqueeOverlayContainer
								) {

									this.contentWidth = marqueeContent.clientWidth
									this.containerWidth = marqueeOverlayContainer.clientWidth

									console.log(this.contentWidth, this.containerWidth)
									const localCloneAmount = Math.ceil(
										this.containerWidth / this.contentWidth,
									)

									this.cloneAmount = isFinite(localCloneAmount)
										? localCloneAmount
										: 0
									return this.cloneAmount
								} else {
									this.minWidth = '100%'
									this.minHeight = '100%'
									return 0
								}
							} else {
								this.minWidth = '100%'
								this.minHeight = '100%'
								return 0
							}
						} else {
							this.minWidth = '100%'
							this.minHeight = '100%'
							return 0
						}
					})
				}, 100)

			},
			hoverStarted() {
				if (this.pauseOnHover) {
					this.$emit('onPause')
				}
			},
			hoverEnded() {
				if (this.pauseOnHover) {
					this.$emit('onResume')
				}
			},
			mouseDown() {
				if (this.pauseOnClick) {
					this.$emit('onPause')
				}
			},
			mouseUp() {
				if (this.pauseOnClick) {
					this.$emit('onResume')
				}
			}

		}
	}
</script>

<style>

</style>
