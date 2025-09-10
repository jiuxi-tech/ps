<!--<template>-->
<!--  <div>-->
<!--    FbRankList.vue-->
<!--    <slot></slot>-->
<!--  </div>-->
<!--</template>-->

<script>
	/**
	 * FbRankList
	 * 运用 scroll 动画等变量行特性，不支持 ie 浏览器
	 * (c) 2021 lincong1987
	 */

	import {prefix} from '../../../../src/config'
	import {merge, cloneDeep} from 'lodash-es'

	export default {
		name: "FbRankList",
		props: {
			config: {
				type: Object,
				default: () => ({})
			},
			data: {
				type: Array,
				default: () => ([])
			},
			// 开启首尾相接
			scroll: {
				type: Boolean,
				default: false
			},
			scrollDirection: {
				type: String,
				default: 'vertical'
			},
			scrollSpeed: {
				type: Number,
				default: 20,
			},
			pauseOnHover: {
				type: Boolean,
				default: false
			},
			forceStart: {
				type: Boolean,
				default: true
			},
			/**
			 * @description Scroll wait time
			 * @type {Number}
			 * @default waitTime = 2000
			 */
			waitTime: {
				type: Number,
				default: 2000,
			}
		},
		data() {
			return {
				ref: 'scroll-ranking-board',

				defaultConfig: {
					/**
					 * @description Board data
					 * @type {Array<Object>}
					 * @default data = []
					 */
					data: [],
					/**
					 * @description Row num
					 * @type {Number}
					 * @default rowNum = 0
					 */
					rowNum: 0,
					/**
					 * @description Scroll wait time
					 * @type {Number}
					 * @default waitTime = 1000
					 */
					// waitTime: 2000,
					/**
					 * @description Carousel type
					 * @type {String}
					 * @default carousel = 'single'
					 * @example carousel = 'single' | 'page'
					 */
					carousel: 'single',
					/**
					 * @description Value unit
					 * @type {String}
					 * @default unit = ''
					 * @example unit = 'ton'
					 */
					unit: '',
					/**
					 * @description Auto sort by value
					 * @type {Boolean}
					 * @default sort = true
					 */
					sort: true,
					/**
					 * @description Value formatter
					 * @type {Function}
					 * @default valueFormatter = null
					 */
					valueFormatter: null
				},

				mergedConfig: null,

				rowsData: [],

				rows: [],

				heights: [],
				opacityArr: [],

				animationIndex: 0,

				animationHandler: '',

				updater: 0,
				start: this.forceStart
			}
		},
		watch: {
			config() {
				this.calcData()
			},
			forceStart(val) {
				this.start = val
				this.calcData()
			},
			data: {
				handler() {
					this.start = false

					this.$nextTick(() => {
						this.start = this.forceStart
						this.calcData()
					})

				},
				deep: true
			}
		},
		mounted() {
			this.$nextTick(() => {
				this.initWH()
			})
		},
		destroyed() {
			this.stopAnimation()
		},
		methods: {
			initWH() {
				const {$nextTick, $refs, ref} = this
				$nextTick(_ => {
					const dom = this.dom = $refs[ref]
					this.width = dom ? dom.clientWidth : 0
					this.height = dom ? dom.clientHeight : 0

					if (!dom) {
						console.warn('FbRankList: Failed to get dom node, component rendering may be abnormal!')
					} else if (!this.width || !this.height) {
						console.warn('FbRankList: Component width or height is 0px, rendering abnormality may occur!')
					}

					this.calcData()
				})
			},
			afterAutoResizeMixinInit() {
				const {calcData} = this

				calcData()
			},
			onResize() {
				const {mergedConfig, calcHeights} = this

				if (!mergedConfig) return

				calcHeights(true)
			},
			calcData() {
				this.stopAnimation()
				this.mergeConfig()
				this.calcRowsData()
				this.calcHeights()
				this.runAnimation()
			},
			mergeConfig() {
				let {config, defaultConfig} = this

				this.mergedConfig = merge(cloneDeep(defaultConfig), config || {})
			},
			calcRowsData() {
				let {data, rowNum, sort} = this.mergedConfig

				if (this.$slots.default) {

					const rankItems = this.$slots.default.filter(item => ['fb-rank-list-item','FbRankListItem'].includes(item.componentOptions.tag))
					const rowLength = rankItems.length
					// test
					data = rankItems.map((node, i) => ({node}))
					// rowNum 为计算高度均分模式
					if (this.forceStart && rowLength > rowNum) {
						data = [...rankItems.map(node => ({node})), ...rankItems.map(node => ({node}))]
					}

					// 强制动画，追加足够多dom
					if (this.forceStart && this.height) {
						let rowSumHeight = this.calcRowsHeight(rankItems, rowLength)
						let divisor = this.height * 2 / rowSumHeight
						if (!rowSumHeight) {
							divisor = 0
						}
						if (divisor > 1) {

							let count = Math.floor(divisor)
							for (let i = 0; i <= count; i++) {
								data.push(...rankItems.map(node => ({node})))
							}
						}

					}

					data = data.map((d, i) => ({...d, scroll: i}))

					this.rowsData = data
					this.rows = data

				} else {
					sort && data.sort(({value: a}, {value: b}) => {
						if (a > b) return -1
						if (a < b) return 1
						if (a === b) return 0
					})

					const value = data.map(({value}) => value)

					const min = Math.min(...value) || 0

					// abs of min
					const minAbs = Math.abs(min)

					const max = Math.max(...value) || 0

					// abs of max
					const maxAbs = Math.abs(max)

					const total = max + minAbs

					data = data.map((row, i) => ({...row, ranking: i + 1, percent: (row.value + minAbs) / total * 100}))

					const rowLength = data.length

					if (rowLength > rowNum && rowLength < 2 * rowNum) {
						data = [...data, ...data]
					}

					data = data.map((d, i) => ({...d, scroll: i}))

					this.rowsData = data
					this.rows = data
				}

			},
			calcRowsHeight(rows, rowLength) {
				let rowSumHeight = 0
				rows.map((item, index) => {
					let itemHeight = 0
					let data = item.data
					let propsData = item.componentOptions.propsData

					if (data.attrs) {
						itemHeight = parseInt(data.attrs.height)
					}
					if (propsData && propsData.height) {
						itemHeight = parseInt(propsData.height)
					}
					if (data.staticStyle) {
						itemHeight = parseInt(data.staticStyle.height)
					}
					rowSumHeight += itemHeight
				}, 0)

				return rowSumHeight
			},
			calcHeights(onresize = false) {
				const rankItems = this.$slots.default && this.$slots.default.filter(item => ['fb-rank-list-item','FbRankListItem'].includes(item.componentOptions.tag)) || []
				const {height, mergedConfig, rowsData, rows} = this

				const {rowNum, data} = mergedConfig

				const avgHeight = height / rowNum

				this.avgHeight = avgHeight
				if (!onresize) this.heights = new Array(data.length || rankItems.length).fill(avgHeight)
				if (rowNum === 0) {
					this.heights = rows.map((item) => {
						let itemHeight = avgHeight
						let data = item.node.data
						let propsData = item.node.componentOptions.propsData
						if (data.attrs) {
							itemHeight = parseInt(data.attrs.height)
						}
						if (propsData && propsData.height) {
							itemHeight = parseInt(propsData.height)
						}
						if (data.staticStyle) {
							itemHeight = parseInt(data.staticStyle.height)
						}
						return itemHeight
					})
				}

				this.opacityArr = new Array(rowsData.length).fill(1)
			},
			runAnimation() {
				if (this.scroll) {
					this.animationScroll(this.start)
				} else {
					this.animation(this.start)
				}
			},
			async animation(start = false) {
				if (!start) {
					this.stopAnimation()
					return
				}
				let {waitTime, avgHeight, animationIndex, mergedConfig, rowsData, animation, updater} = this

				const {carousel, rowNum} = mergedConfig

				const rowLength = rowsData.length

				if (rowNum >= rowLength) return

				if (start) {
					await new Promise(resolve => setTimeout(resolve, waitTime))
					if (updater !== this.updater) return
				}

				const animationNum = carousel === 'single' ? 1 : rowNum

				let rows = rowsData.slice(animationIndex)
				rows.push(...rowsData.slice(0, animationIndex))

				if (rowNum > 0) {
					this.rows = rows.slice(0, rowNum + animationNum)
					this.heights = new Array(rowLength).fill(avgHeight)
				} else {
					this.rows = rows
					this.calcHeights()
				}
				this.opacityArr = new Array(rowLength).fill(1)

				await new Promise(resolve => setTimeout(resolve, 300))
				if (updater !== this.updater) return

				this.heights.splice(0, animationNum, ...new Array(animationNum).fill(0))
				this.opacityArr = this.heights.map(height => height > 0 ? 1 : 0)

				animationIndex += animationNum

				const back = animationIndex - rowLength
				if (back >= 0) animationIndex = back

				this.animationIndex = animationIndex
				this.animationHandler = setTimeout(() => {
					this.animation(this.start)
				}, waitTime - 300)
			},
			async animationScroll(start) {
				let {waitTime, avgHeight, rowsData, updater} = this

				if (start) {
					await new Promise(resolve => setTimeout(resolve, waitTime))
					if (updater !== this.updater) return

					let parent = this.$refs[this.ref]
					let son = this.$refs[this.ref + '-body']

					if (this.scrollDirection === 'vertical') {
						let sumHeight = avgHeight * rowsData.length
						let h = sumHeight - parent.offsetHeight
						if (h < 0) return
						this.animationHandler = setInterval(() => {
							// 正常滚动不断给scrollTop的值+1,当滚动高度大于列表内容高度时恢复为0
							if (parent.scrollTop >= parent.scrollHeight / 2) {
								//注此处高度相当于为俩个tbody高度 需除2
								parent.scrollTop = 0;
							} else {
								parent.scrollTop++;
							}
						}, this.scrollSpeed)
					} else if (this.scrollDirection === 'horizontal') {
						let w = son.offsetWidth - parent.offsetWidth
						if (w < 0) return
						this.animationHandler = setInterval(() => {
							// 正常滚动不断给scrollTop的值+1,当滚动高度大于列表内容高度时恢复为0
							if (parent.scrollLeft >= parent.scrollWidth / 2) {
								//注此处高度相当于为俩个tbody高度 需除2
								parent.scrollLeft = 0;
							} else {
								parent.scrollLeft++;
							}
						}, this.scrollSpeed)
					}

				}
			},
			stopAnimation() {
				const {animationHandler, updater} = this

				this.updater = (updater + 1) % 999999

				if (!animationHandler) return

				clearTimeout(animationHandler)
			},
			hoverStarted() {
				if (this.pauseOnHover) {
					this.stopAnimation()
					this.$emit('onPause')
				}
			},
			hoverEnded() {
				if (this.pauseOnHover) {
					this.runAnimation()
					this.$emit('onResume')
				}
			},
		},
		render: function (h) {

			let rows = this.rows.map((item, index) => {

				return h('div', {
					'class': `${prefix}-scroll-ranking-list-item`,
					'key': `scroll-ranking-boards-${item.scroll}`,
					'style': [{
						height: this.heights[this.scroll ? 0 : index] + 'px',
						opacity: this.opacityArr[index],
					}]
				}, [item.node])
			})

			return h('div', {
				'class': [`${prefix}-scroll-ranking-list`],
				ref: this.ref,
				style: {},
				on: {
					mouseenter: this.hoverStarted,
					mouseleave: this.hoverEnded
				}
			}, [
				h('div', {
					'class': [`${prefix}-scroll-ranking-list-body`],
					ref: this.ref + '-body',
				}, [rows])
			])
		},
	}
</script>

<style scoped>
	.rank-list {

	}
</style>
