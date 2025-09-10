<template>
	<fb-container class="video-wall-wrapper" :class="{[`video-wall-wrapper--mode-${myMode}`]: myMode}"
				  ref="video-wall-wrapper">


		<!--		<template v-if="urls.length === 0">-->
		<!--			<fb-container class="video-player-empty">-->
		<!--				<fb-empty text="请接入需要查看的视频"></fb-empty>-->
		<!--			</fb-container>-->
		<!--		</template>-->

		<fb-container class="video-wall-container">
			<fb-row v-for="(row, rowIndex) in rows[myMode]" :key="`row-${rowIndex}`" gutter="4" vertical-gutter="4">
				<fb-col v-for="(cell, cellIndex) in rows[myMode]" :key="`cell-${rowIndex}-${cellIndex}`"
						:span="24/(rows[myMode])">

					<fb-container class="video-player-cell"
								  :class="{[`video-player-cell--active`]: ((rowIndex * rows[myMode])+ (cellIndex)) === selectedCellIndex}"
								  @click="()=>{
							handleSelectCell(((rowIndex * rows[myMode])+ (cellIndex)))
						}">
						<fb-container class="video-player" v-if="playerList[((rowIndex * rows[myMode]) + (cellIndex))]">
							<components :is="EasyPlayer"
										:auto-play="autoPlay"
										:stretch="stretch"
										:decodeWasm="decodeWasm"
										:aspect="aspect"
										:video-url="(()=>{
									let url = ''
									if ( getCell(rowIndex, cellIndex).url ) {
										url = getCell(rowIndex, cellIndex).url
									}
									return url
								})()"
										:video-title="getCell(rowIndex, cellIndex) && getCell(rowIndex, cellIndex).title"
										:alt="getCell(rowIndex, cellIndex) && getCell(rowIndex, cellIndex).alt"
										:timeout="getCell(rowIndex, cellIndex) && getCell(rowIndex, cellIndex).timeout"

										@video-url="(e)=>{handleUrl(e, ((rowIndex * rows[myMode])+ (cellIndex)))}"
										@ended="(e)=>{handleEnded(e, ((rowIndex * rows[myMode])+ (cellIndex)))}"
										@timeupdate="(e)=>{handleTimeUpdate(e, ((rowIndex * rows[myMode])+ (cellIndex)))}"
										@pause="(e)=>{handlePause(e, ((rowIndex * rows[myMode])+ (cellIndex)))}"
										@play="(e)=>{handlePlay(e, ((rowIndex * rows[myMode])+ (cellIndex)))}"
										@message="(e)=>{handleMessage(e, ((rowIndex * rows[myMode])+ (cellIndex)))}"
										@update:loading="(e)=>{handleLoading(e, ((rowIndex * rows[myMode])+ (cellIndex)))}"

							></components>
						</fb-container>

						<fb-container class="video-border--tb"></fb-container>
						<fb-container class="video-border--lr"></fb-container>

						<div class="video-player-toolbar">
							<fb-space>
								<!--								<fb-button size="s" danger type="primary"-->
								<!--								           icon="photo"-->
								<!--								           @on-click="playerList[((rowIndex * rows[myMode]) + (cellIndex))].url = ''"-->
								<!--								></fb-button>-->

								<fb-button size="s" danger type="primary"
										   icon="close"
										   @on-click="closeCellVideo(rowIndex, cellIndex)"
								></fb-button>
							</fb-space>
						</div>

					</fb-container>
				</fb-col>
			</fb-row>
		</fb-container>

		<fb-container v-show="!hideTool" class="video-wall-toolbar" align="right">

			<fb-space size="8">

				<fb-tooltip v-if="!hideToolSplit" placement="top-end">
					<template slot="content">
						<fb-space size="8" style="cursor:pointer;">
							<fb-icon @on-click.stop="changeMode(1)" title="一分屏" name="layout-one" size="18"></fb-icon>
							<fb-icon @on-click.stop="changeMode(4)" title="四分屏" name="layout-four" size="18"></fb-icon>
							<fb-icon @on-click.stop="changeMode(9)" title="九分屏" name="layout-nine" size="18"></fb-icon>
						</fb-space>
					</template>
					<span class="icon-box">
						<fb-icon name="layout" size="18"></fb-icon>
						&nbsp;&nbsp;
						<!--						<fb-icon name="down" size="12"></fb-icon>-->
					</span>
				</fb-tooltip>

				<span @click.stop="handleFullscreen" class="icon-box" title="全屏">
					<fb-icon name="fullscreen" size="18"></fb-icon>
				</span>
			</fb-space>


		</fb-container>


	</fb-container>
</template>

<script>/**
 * VideoWall
 * (c) 2021 lincong1987
 */
import { times, cloneDeep } from 'lodash-es'

const playerDefaultConfig = {
	// 视频地址
	url: '',
	// 视频右上角显示的标题
	title: '',
	// 视频封面图片
	poster: '',
	// 自动播放
	autoPlay: true,
	// 是否直播, 标识要不要显示进度条
	live: true,
	// 是否显示倍速播放按钮。注意：当live为true时，此属性不生效
	speed: true,
	//是否轮播
	loop: false,
	// 视频流地址没有指定情况下, 视频所在区域显示的文字
	alt: '无信号',
	// 是否静音
	muted: false,
	//视频显示区域的宽高比
	aspect: '3:2',
	//   视频显示区域是否强制宽高比
	isAspect: true,
	//   指示加载状态, 支持 sync 修饰符
	loading: '',
	//      流畅模式
	fluent: true,
	//     加载超时(秒)
	timeout: 20,
	//      是否不同分辨率强制铺满窗口
	stretch: true,
	//            是否在工具栏显示自定义按钮 (极速/流畅, 拉伸/标准)
	showCustomButton: true,
	//                  默认播放的清晰度
	resolutiondefault: 'hd',
	//     强制H265解码(支持:HLS/FLV/WS)
	decodeWasm: false,

}

export default {
	name: 'EasyVideoWall',

	components: {},

	props: {

		// 视频地址
		url: {
			type: String,
			default: '',
		},
		// 视频右上角显示的标题
		title: {
			type: String,
			default: '',
		},
		// 视频封面图片
		poster: {
			type: String,
			default: '',
		},
		// 自动播放
		autoPlay: {
			type: Boolean,
			default: true,
		},
		// 是否直播, 标识要不要显示进度条
		live: {
			type: Boolean,
			default: true,
		},
		// 是否显示倍速播放按钮。注意：当live为true时，此属性不生效
		speed: {
			type: Boolean,
			default: true,
		},
		//是否轮播
		loop: {
			type: Boolean,
			default: false,
		},
		// 视频流地址没有指定情况下, 视频所在区域显示的文字
		alt: '无信号',
		// 是否静音
		muted: {
			type: Boolean,
			default: false,
		},
		//视频显示区域的宽高比
		aspect: 'fullscreen',
		//   视频显示区域是否强制宽高比
		isAspect: {
			type: Boolean,
			default: true,
		},
		//   指示加载状态, 支持 sync 修饰符
		loading: '',
		//      流畅模式
		fluent: {
			type: Boolean,
			default: true,
		},
		//     加载超时(秒)
		timeout: {
			type: Number,
			default: 20,
		},
		//      是否不同分辨率强制铺满窗口
		stretch: {
			type: Boolean,
			default: true,
		},
		//            是否在工具栏显示自定义按钮 (极速/流畅, 拉伸/标准)
		showCustomButton: {
			type: Boolean,
			default: true,
		},
		//                  默认播放的清晰度
		resolutionDefault: {
			type: String,
			default: 'hd',
		},
		//     强制H265解码(支持:HLS/FLV/WS)
		decodeWasm: {
			type: Boolean,
			default: false,
		},

		urls: {
			type: Array,
			default () {
				return []
			},
		},

		mode: {
			type: [String, Number],
			default: 1,
		},

		width: {
			type: [String, Number],
			default: '100%',
		},
		height: {
			type: [String, Number],
			default: '100%',
		},

		hideTool: {
			type: [Boolean],
			default: false,
		},
		hideToolSplit: { // 隐藏分屏
			type: [Boolean],
			default: false,
		}
	},

	data () {
		if (!window.videojs) {
			console.warn('请保证public下有EasyPlayer.wasm文件，否则EasyVideoWall无法使用！！！！')
		}
		return {
			EasyPlayer: window.videojs ? () => ({
				// 需要加载的组件 (应该是一个 `Promise` 对象)
				component: import('../easy-player/EasyPlayer'),
				// 异步组件加载时使用的组件
				loading: {template: '<div><fb-spin></fb-spin></div>'},
				// 加载失败时使用的组件
				error: {template: '<div>加载组件失败</div>'},
				// 展示加载时组件的延时时间。默认值是 200 (毫秒)
				delay: 200,
				// 如果提供了超时时间且组件加载也超时了，
				// 则使用加载失败时使用的组件。默认值是：`Infinity`
				timeout: 3000
			}) : '',

			initFlag: false,
			myFullscreen: false,

			myUrls: [],
			myMode: 0,
			playerList: [],

			rows: {
				1: 1,
				4: 2,
				9: 3,
				16: 4,
			},

			selectedCellIndex: -1,

		}
	},

	watch: {
//		urls (value) {
//			this.myUrls = value
//			this.createWall()
//		},
//
//		mode (value) {
//			this.myMode = value
//			this.createWall()
//		},

		myMode (value) {
			this.updateWall()
		},
	},

	created () {
	},

	mounted () {
		this.$nextTick(() => {
			this.myUrls = this.urls
			this.myMode = this.mode
			this.playerList = times(parseInt(this.myMode, 10), (i) => {
				return {url: this.myUrls[i] || ''}
			})
//
			this.updateWall()

			this.initFlag = true

			this.doScale()
			window.addEventListener('resize', this.resize, false)
		})
	},

	beforeDestroy () {
		window.removeEventListener('resize', this.resize, false)
	},

	methods: {

		updateWall () {

			//this.handleSelectCell(-1)

			console.log(`更新视频墙 模式 %d`, this.myMode)

			this.playerList = []

			this.$nextTick(() => {

				this.playerList = times(parseInt(this.myMode, 10), (i) => {

					let cellConfig = this.playerList[i] || {}

					for (let key in playerDefaultConfig) {
						if (typeof cellConfig[key] === 'undefined') {
							cellConfig[key] = this[key]
						}
					}

					if (this.initFlag === false && this.myUrls[i]) {
						if (typeof this.myUrls[i] === 'string') {
							cellConfig.url = this.myUrls[i]
						}
					}

					return cellConfig
				})
			})
		},

		getCell (rowIndex, cellIndex) {
			return this.playerList[((rowIndex * this.rows[this.myMode]) + (cellIndex))]
		},

		getCellProp (rowIndex, cellIndex, prop) {
			let cell = this.getCell(rowIndex, cellIndex)
			return cell && cell.prop
		},

		updateCell () {
		},

		closeCellVideo(rowIndex, cellIndex) {
			let cell = this.getCell(rowIndex, cellIndex)
			this.$emit('on-close-cell', cloneDeep(cell), rowIndex, cellIndex)
			if (cell.url) {
				cell.url = ''
			}
			if (cell.title) {
				cell.title = ''
			}
		},

		handleSelectCell (index) {
			this.selectedCellIndex = index
			this.$emit('on-select-cell', index)
		},

		handleModeChange () {

		},

		changeMode (mode) {
			if (![1, 4, 9].includes(mode)) {
				this.$message.error(`暂时不支持您所选择的模式 [${mode}]`)
				return
			}

			if (this.myMode === mode) {
				//this.$message.warn(`已经处于该模式 [${mode}]`)
			} else {
				this.myMode = mode
				//this.$message.success(`已切换模式 [${mode}]`)
			}

		},

		play (url, targetCellIndex, options = {}) {
			if (this.overstepUrl() && typeof targetCellIndex === 'undefined') {
				return;
			}
			if (url && typeof targetCellIndex === 'undefined') {
				let cells = this.playerList.filter((cell) => cell.url === '')
				if (cells.length) {
					cells[0].url = url
					cells[0].title = options.title
					return
				}
			}

			if (typeof targetCellIndex === 'undefined') {
				this.$message.error(`请指定播放单元`)
				return
			}

			if (targetCellIndex < 0 || (targetCellIndex + 1) >
				this.playerList.length || !this.playerList[targetCellIndex]) {
				this.$message.error(`播放单元 [${targetCellIndex}] 不存在`)
				return
			}

			this.playerList[targetCellIndex].url = url
			this.playerList[targetCellIndex].title = options.title
		},

		handleFullscreen () {
			this.myFullscreen = false
			let element = this.$refs['video-wall-wrapper'].$el
			this.myFullscreen ? document.exitFullscreen ? document.exitFullscreen() : document.webkitCancelFullScreen
				? document.webkitCancelFullScreen()
				: document.mozCancelFullScreen ? document.mozCancelFullScreen() : document.msExitFullscreen &&
					document.msExitFullscreen() : element.requestFullscreen
				? element.requestFullscreen()
				: element.webkitRequestFullScreen ? element.webkitRequestFullScreen() : element.mozRequestFullScreen
					? element.mozRequestFullScreen()
					: element.msRequestFullscreen && element.msRequestFullscreen()
		},

		doScale () {
			if (typeof this.height !== 'number' || typeof this.width !== 'number') {
				return
			}

		},

		resize () {
			// document.webkitIsFullScreen ? this.aspect = '300:166' : this.aspect = '300:200'
		},

		handleUrl (e, index) {

			// console.log('on-video-url', e, index)
			this.$emit('on-video-url', e, index)
		},
		handleEnded (e, index) {
			// console.log('on-ended', e, index)
			this.$emit('on-ended', e, index)
		},
		handleTimeUpdate (e, index) {
			// console.log('on-timeupdate', e, index)
			this.$emit('on-timeupdate', e, index)
		},
		handlePause (e, index) {
			// console.log('on-pause', e, index)
			this.$emit('on-pause', e, index)
		},
		handlePlay (e, index) {
			// console.log('on-play', e, index)
			this.$emit('on-play', e, index)
		},
		handleMessage (e, index) {
			// console.log('on-message', e, index)

			if (e.type === 'error') {
				this.playerList[index].url = ''
				this.playerList[index].alt = e.message
			}

			this.$emit('on-message', e, index)
		},
		handleLoading (e, index) {
			// console.log('update:loading', e, index)
			this.$emit('update:loading', e, index)
		},
		// 超出边界判断
		overstepUrl() {
			let overstep = false
			const arr = this.playerList.filter(item => item.url.indexOf('flv') > 0)
			if (arr.length > 4) {
				overstep = true
				this.$message.warn('当前最多支持播放5路flv视频流！！！')
			}
			return overstep
		}
	},

}
</script>

<style lang="less" scoped>
@import "@fb/screen-base/src/assets/styles/components/easy-video-wall.less";

</style>
