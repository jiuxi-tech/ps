<template>
	<fb-container class="hik-player-wrapper" :class="{
		          [`hik-player-wrapper--${status}`]: !!status
	}" :style="{
		width: typeof width === 'number' ? `${width}px`: width,
		height: typeof height === 'number' ? `${height}px`: height,
	}">


		<fb-container class="hik-player" relative :class="{}" :style="{}">

			<!--<fb-container absolute top="-20px">-->
				<!--{{ height }}, {{ width }}-->
			<!--</fb-container>-->


			<!--			<template v-if="status === 'welcome'">-->
			<!--				<fb-container position="absolute" width="100%" align="center" top="40%" left="0">-->
			<!--					<div style="font-size: 16px; margin-bottom: 10px">-->
			<!--						<fb-text size="l">请点击视频链接开始播放</fb-text>-->
			<!--					</div>-->
			<!--				</fb-container>-->
			<!--			</template>-->


			<template v-if="status === 'checking'">
				<fb-container position="absolute" width="100%" align="center" top="40%" left="0">
					<div style="font-size: 16px; margin-bottom: 10px">
						检测插件是否安装，已完成
						<fb-number :start="(initCount-1) * 20" :end="initCount * 20" :duration="20000" append="%"/>
						， 请稍等...
					</div>
				</fb-container>
			</template>


			<template v-if="status === 'error'">
				<fb-container position="absolute" width="100%" align="center" top="40%" left="0">
					<div style="font-size: 16px; margin-bottom: 10px">
						<fb-text size="l">为了您能正常播放视频，请点击下面的按钮下载并安装插件，安装完成后请刷新页面。</fb-text>
					</div>
					<fb-button type="primary" size="l" @on-click="handleDownloadPluginClick">
						点击此处下载插件
					</fb-button>
				</fb-container>
			</template>

			<template v-if="status === 'ready'">
				<div ref="hik-player-el" :id="myId" class="hik-player__el"

				     :style="[
				     	status === 'ready'? {
		width: typeof width === 'number' ? `${width}px`: width,
		height: typeof height === 'number' ? `${height}px`: height,
	}: {}
				     ]"

				></div>
			</template>
		</fb-container>
	</fb-container>
</template>

<script>
/**
 * HikPlayer
 * (c) 2021 lincong1987
 */
import { debounce } from 'lodash-es'
import { random } from 'lodash-es/number'

export default {
	name: 'HikPlayer',

	mixins: [],

	props: {

		id: {
			type: [String],
			default: undefined,
		},

		width: {
			type: [String, Number],
			default: 640,
		},

		height: {
			type: [String, Number],
			default: 480,
		},

		////////////////////////////////// 海康视频插件初始化值	////////////////////////////////////
		//综合安防管理平台提供的appkey，必填
		appkey: {
			type: [String],
			default: undefined,
		},
		//综合安防管理平台提供的secret，必填
		secret: {
			type: [String],
			default: undefined,
		},
		//综合安防管理平台IP地址，必填
		ip: {
			type: [String],
			default: undefined,
		},
		//初始播放模式：0-预览，1-回放
		// 0-预览 1-回放，如未指定则使用默认 值 0，如指定非可选值，返回错误
		playMode: {
			type: [Number, String],
			default: undefined,
		},
		//综合安防管理平台端口，若启用HTTPS协议，默认443
		port: {
			type: [Number, String],
			default: undefined,
		},
		//抓图存储路径
		snapDir: {
			type: [String],
			default: undefined,
		},
		//紧急录像或录像剪辑存储路径
		// 如未指定或指定值无效，
		// 使用默认值: {VideoPlugin 运行目录}/Video，
		// 命名 规则为{监控点名称}_{录像时间 戳 }.mp4 (如 “ 测试监控点 _1543390276324.mp4”)，
		// 请确保磁盘 路径剩余控件大于 1G，否则会影响工 具条上的紧急录像或者录像剪辑功能
		videoDir: {
			type: [String],
			default: undefined,
		},
		//playMode指定模式的布局
		// 如未指定或指定值无效，使用默认值 “2x2”布局，
		// 可选值有“1x1”、“2x2”、 “3x3”、“4x4”、“5x5”、“1x2”、“1+2”、 “1+5”、“1+7”、“1+8”、“1+9”、“1+12”、 “1+16”、“4+9”、“1+1+12”、“3+4”、 “1x4”、“4x6”
		layout: {
			type: [String],
			default: undefined,
		},
		//是否启用HTTPS协议与综合安防管理平台交互，这里总是填1
		enableHTTPS: {
			type: [Number, String],
			default: undefined,
		},
		//加密字段，默认加密领域为secret
		encryptedFields: {
			type: [String],
			default: undefined,
		},
		//是否显示工具栏，0-不显示，非0-显示
		showToolbar: {
			type: [Number, String],
			default: undefined,
		},

		//是否显示智能信息（如配置移动侦测后画面上的线框），0-不显示，非0-显示
		showSmart: {
			type: [Number, String],
			default: undefined,
		},

		showIntelligent: {
			type: [Number, String],
			default: undefined,
		},

		//自定义工具条按钮
		buttonIDs: {
			type: [String],
			default: undefined,
		},
		// 工具栏上，预览回放通用按钮有:
		// 		全部静音，全部抓图，全部关闭，切换 布局，切换全屏，全部自适应，分隔条;
		// 工具栏上仅和回放相关的按钮有:
		// 		同步异步，速度控件，倒放切换按钮， 单帧退，时间，正放切换按钮，单帧进，下载中心;
		// 对应的按钮定义为(括号内为对应 10 进制 ID 值): 0x800(2048)-同步异步 0x801(2049)-全 部静音 0x802(2050) 全部抓图 0x900(2304)速度控件 0x901(2305)倒 放切换按钮 0x902(2306)单帧退 0x903(2307)时间 0x904(2308)正放切 换 按 钮 0x905(2309) 单 帧 进 0x1000(4096)全部关闭 0x1001(4097) 切换布局 0x1002(4098)切换全屏 0x1003(4099) 全 部 自 适 应 0x1004(4100)下载中心 0x1200 (4608) 分隔条 0x1200 (4609)分隔条 2 该字段仅控制工具栏上的按钮显示/隐 藏，不支持自定义顺序，配合 showToolbar 字段使用。显示工具栏时， 不传该字段则默认显示该模式下所有 工具栏按钮;指定为空字符串时，显 示工具栏，但无按钮;指定重复、非 法值将返回失败。指定功能不支持的 按钮 ID 将不显示该按钮(如预览模式 指定下载中心按钮)。预览仅支持分隔 条 1，回放支持分隔条 1 和分隔条 2
		toolBarButtonIDs: {
			type: [String],
			default: undefined,
		},
		////////////////////////////////// 海康视频插件初始化值	////////////////////////////////////
		////////////////////////////////// 海康视频插件播放参数	////////////////////////////////////
		cameraIndexCode: '',                          //获取输入的监控点编号值，必填
		streamMode: 0,                                //主子码流标识：0-主码流，1-子码流
		transMode: 1,                                 //传输协议：0-UDP，1-TCP
		gpuMode: 0,                                   //是否启用GPU硬解，0-不启用，1-启用
		wndId: -1,                                    //播放窗口序号（在2x2以上布局下可指定播放窗口）
		////////////////////////////////// 海康视频插件播放参数	////////////////////////////////////
		enable: true,

	},
	data () {
		return {

			message: '',
			status: '',

			initCount: 1,
			myId: '',
		}
	},

	watch: {},

	created () {
		this.create()
	},

	beforeDestroy () {
		this.remove()
	},

	mounted () {

		this.myId = this.id || `playWnd_${this.uuid()}`

		console.log(this.myId)

		this.status = 'checking'
		this.message = 'mounted'

		this.$nextTick(() => {
			this.getPlayer()
		})
	},

	methods: {

		uuid () {
			let d = Date.now()
			return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
				let r = (d + random(16)) % 16 | 0
				d = Math.floor(d / 16)
				return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16)
			})
		},

		handleDownloadPluginClick () {
			try {
				window.open('/dustexpl-riskwarn-ui/plugin/VideoWebPlugin.exe')
			} catch (e) {
			}
		},

		create () {
			this.defaults = this.$env('VUE_APP_HIK_CONFIG') || {}
			this.player = null
			this.tempPlayer = null
			this.encrypt = new JSEncrypt()

			this.debounceResize = debounce(this.resize, 200)
			window.addEventListener('resize', this.debounceResize, false)
		},

		remove () {
			if (this.player != null) {
				this.player.JS_RequestInterface({
					funcName: 'stopAllPreview',
				})
				this.player.JS_HideWnd()
				this.player.JS_Disconnect().then(() => {
					},
					() => {  // 断开与插件服务连接失败
					})
			}

			this.debounceResize.cancel()
			this.defaults = null
			this.player = null
			this.tempPlayer = null
			this.encrypt = null
			window.removeEventListener('resize', this.debounceResize, false)

		},

		// 创建播放实例
		createPlayer () {

			let me = this

			if (!window.WebControl) {
				return Promise.reject('WebControl')
			}

			if (this.player) {
				return Promise.resolve()
			}
			return new Promise((resolve, reject) => {

				if (this.player) {
					resolve()
				} else {
					me.tempPlayer = new window.WebControl({
						szPluginContainer: this.myId,                       // 指定容器id
						iServicePortStart: 15899,                           // 指定起止端口号，建议使用该值
						iServicePortEnd: 15909,
						szClassId: '23BF3B0A-2C56-4D97-9C03-0CB103AA8F11',   // 用于IE10使用ActiveX的clsid

						oWindowAttr: {
							innerHeight: 0,
							innerWidth: 0,
							outerHeight: 0,
							outerWidth: 0,
							screenLeft: 0,
							screenTop: 0,
							screenX: 0,
							screenY: 0,
						},

						cbConnectSuccess: () => {

							this.$emit('on-connect-success')

							//
							me.status = 'ready'
							me.initCount = 5

							// 创建WebControl实例成功
							me.player = me.tempPlayer
							me.message = 'HIK 实例创建成功，开始创建 窗口服务'
							me.player.JS_StartService('window', {         // WebControl实例创建成功后需要启动服务
								dllPath: './VideoPluginConnect.dll',         // 值"./VideoPluginConnect.dll"写死
							}).then(() => {

								this.$emit('on-start-service-success')

								// 启动插件服务成功
								me.message = 'HIK JS_StartService 成功 '
								me.setCallbacks()
								me.createWindow().then(() => {


									this.$emit('on-create-window-success')

									me.message = 'HIK createWindow 成功 '
									me.getPubKey().then(() => {
										me.status = 'ready'
										me.message = 'HIK getPubKey 创建成功 '
										resolve()
									})
								}).catch(() => {
									me.initCount = 5
									me.status = 'error'

									this.$emit('on-create-window-error')
									reject()
								})

							}, () => { // 启动插件服务失败


								this.$emit('on-start-service-error')

								me.status = 'error'
								me.initCount = 5
								me.message = '启动插件服务失败'
								reject()
							})
						},
						cbConnectError: () => {   // 创建WebControl实例失败
							me.message = '插件未启动，正在尝试启动，请稍候...'
							try {
								window.WebControl.JS_WakeUp('VideoWebPlugin://') // 程序未启动时执行error函数，采用wakeup来启动程序
							} catch (e) {
							}

							if (me.initCount < 5) {
								window.setTimeout(() => {
									me.initCount++
									me.createPlayer().then(() => {
										resolve()
									})
								}, 3000)
							} else {
								me.$message.error('插件启动失败，请检查插件是否安装！')
								me.status = 'error'

								this.$emit('on-connect-error')
								reject()
							}
						},
						cbConnectClose: (bNormalClose) => {

							this.$emit('on-connect-close')
							this.remove()
						},
					})
				}
			})

		},

		createWindow () {
			return new Promise((resolve, reject) => {
				this.player.JS_CreateWnd(this.myId, 1, 1, {
//					bEmbed: true,
//					cbSetDocTitle: (uuid) => {
//						this.player._pendBg = false
//					},
				}).then(() => { //JS_CreateWnd创建视频播放窗口，宽高可设定
					this.$emit('on-init')
					resolve()
				}, (e) => {
					reject(e)
				})
			})
		},

		getPlayer () {
			return new Promise((resolve, reject) => {

				this.createPlayer().then(() => {
					if (this.player) {
						resolve()

						this.$emit('on-ready')
					}
				}).catch(() => {


					// this.$emit('on-ready')
				})
			})
		},

		//获取公钥
		getPubKey () {
			let that = this

			return new Promise((resolve, reject) => {
				this.player.JS_RequestInterface({
					funcName: 'getRSAPubKey',
					argument: JSON.stringify({
						keyLength: 1024,
					}),
				}).then((oData) => {


					this.$emit('on-get-rsa-pubkey-success')

					if (oData.responseMsg.data) {

						let args = JSON.stringify({
							appkey: this.getDefault('appkey'),                            //API网关提供的appkey
							secret: this.getSecret(oData.responseMsg.data, this.getDefault('secret')),//API网关提供的secret
							ip: this.getDefault('ip'),                                    //API网关IP地址
							playMode: this.getDefault('playMode'),                        //播放模式（决定显示预览还是回放界面）
							port: this.getDefault('port'),                                //端口
							snapDir: this.getDefault('snapDir'),                         //抓图存储路径
							videoDir: this.getDefault('videoDir'),                       //紧急录像或录像剪辑存储路径
							layout: this.getDefault('layout'),                           //布局
							enableHTTPS: this.getDefault('enableHTTPS'),                 //是否启用HTTPS协议
							encryptedFields: this.getDefault('encryptedFields'),         //加密字段
							showToolbar: this.getDefault('showToolbar'),                 //是否显示工具栏
							showSmart: this.getDefault('showSmart'),                      //是否显示智能信息
							buttonIDs: this.getDefault('buttonIDs'),                       //自定义工具条按钮
						})

						console.log(args)

						this.player.JS_RequestInterface({
							funcName: 'init',
							argument: args,
						}).then(() => {

							//this.setTimeout(() => {
							that.setVideoSize()


							this.$emit('on-init-success')
							//}, 1000)
							resolve()
						}, () => {
							console.log('error')

							this.$emit('on-init-error')
							reject()
						})

					}
				})
			})

		},

		getSecret (pk, secret) {
			this.encrypt.setPublicKey(pk)
			return this.encrypt.encrypt(secret)
		},

		// 设置窗口控制回调
		setCallbacks () {
			this.player.JS_SetWindowControlCallback({
				cbIntegrationCallBack: (oData) => {
					console.log('[HIK LOG]', '[HIK LOG] JS_SetWindowControlCallback cbIntegrationCallBack ', oData)
					this.$emit('on-message', oData)
				},
			})
		},

		getDefault (name) {

			if (typeof this[name] !== 'undefined') {
				return this[name]
			}

			if (typeof this.defaults[name] !== 'undefined') {
				return this.defaults[name]
			}

			let defs = {
				'playMode': 0,
				'port': 4443,
				'snapDir': 'D:\\SnapDir',
				'videoDir': 'D:\\VideoDir',
				'layout': '3x3',
				'enableHTTPS': 1,
				'showToolbar': 1,
				'showSmart': 1,
				'buttonIDs': '0,16,257,258,259,260,512,513,515,516,517,768,769',
				'encryptedFields': 'secret',
			}

			if (typeof defs[name] !== 'undefined') {
				return defs[name]
			}

			return null
		},

		resize () {
			if (this.player) {
				this.setVideoSize()
			}
		},

		setVideoSize () {
			let me = this

			let defWidth = typeof this.width === 'number' ? this.width : parseInt(this.width, 10)
			let defHeight = typeof this.height === 'number' ? this.height : parseInt(this.height, 10)

			let width = defWidth
			let height = defHeight

			let iWidth = window.innerWidth
			let iHeight = window.innerHeight

			let oDivRect = this.$refs['hik-player-el'].getBoundingClientRect()

			let dev = this.$datax.get('screen.dev')
			let mode = this.$datax.get('screen.mode')
			let zoomY = this.$datax.get('screen.zoomY')
			let zoomX = this.$datax.get('screen.zoomX')
			let aZoom = this.$datax.get('screen.aZoom')
			let scale = this.$datax.get('screen.scale')
			let zoom = this.$datax.get('screen.zoom') || 1

			this.player.oDocOffset = {
				top: 0,
				left: 0,
			}

			// this.player.oDocOffset = {
			//     top: oDivRect.top,
			//     left: oDivRect.left,
			// }

			if (mode === 'autoHeight') {
				width = defWidth * zoomX
				height = defHeight * zoomY
			} else if (mode === 'adapt') {
				width = defWidth * aZoom
				height = defHeight * aZoom
			} else if (mode === 'original') {
				width = defWidth * zoom
				height = defHeight * zoom
			} else {
				width = defWidth * zoom
				height = defHeight * zoom
			}

			if (dev) {
				width = defWidth * scale
				height = defHeight * scale
			}

//			console.log('[HIK LOG]', 'oDocOffset', this.player.oDocOffset)
//			console.log('[HIK LOG]', 'mode', mode)
//			console.log('[HIK LOG]', width, height)

//			this.player.JS_SetDocOffset({
////				top: oDivRect.top,
////				left: oDivRect.left,
//				top: 0,
//				left: 0,
//			})
// 			console.log(width, height)
			this.player.JS_Resize(width, height, {bFixed: true})

//			this.setTimeout(() => {
//				me.player.JS_SetDocOffset({
//					top: 0,
//					left: 0,
//				})
//				me.player.JS_Resize(width, height, {bFixed: true})
//			}, 100)

//			this.player.JS_RepairPartWindow(0, 0, width, height)
//			this.player.JS_CuttingPartWindow(100, 60, 0, 300)

			//this.player.JS_Resize(1000,600)

//			var iCoverLeft = (oDivRect.left < 0) ? Math.abs(oDivRect.left) : 0
//			var iCoverTop = (oDivRect.top < 0) ? Math.abs(oDivRect.top) : 0
//			var iCoverRight = (oDivRect.right - iWidth > 0) ? Math.round(oDivRect.right - iWidth) : 0
//			var iCoverBottom = (oDivRect.bottom - iHeight > 0) ? Math.round(oDivRect.bottom - iHeight) : 0
//
//			iCoverLeft = (iCoverLeft > 1000) ? 1000 : iCoverLeft
//			iCoverTop = (iCoverTop > 600) ? 600 : iCoverTop
//			iCoverRight = (iCoverRight > 1000) ? 1000 : iCoverRight
//			iCoverBottom = (iCoverBottom > 600) ? 600 : iCoverBottom
//
//			this.player.JS_RepairPartWindow(0, 0, 1001, 600)    // 多1个像素点防止还原后边界缺失一个像素条
//			if (iCoverLeft != 0) {
//				this.player.JS_CuttingPartWindow(0, 0, iCoverLeft, 600)
//			}
//			if (iCoverTop != 0) {
//				this.player.JS_CuttingPartWindow(0, 0, 1001, iCoverTop)    // 多剪掉一个像素条，防止出现剪掉一部分窗口后出现一个像素条
//			}
//			if (iCoverRight != 0) {
//				this.player.JS_CuttingPartWindow(1000 - iCoverRight, 0, iCoverRight, 600)
//			}
//			if (iCoverBottom != 0) {
//				this.player.JS_CuttingPartWindow(0, 600 - iCoverBottom, 1000, iCoverBottom)
//			}

		},

		/**
		 * 顺序播放
		 */
		play (code, options = {
			cameraIndexCode: '',                //监控点编号
			streamMode: 1,                         //主子码流标识
			transMode: 1,      // 传输协议:0 UDP; 1:CTP
			gpuMode: 0,        // 是否启用GPU硬解: 0:不启用；1：启用
			wndId: -1,                                     //可指定播放窗口
		}) {

			if (this.status === 'ready') {
				options.cameraIndexCode = code
				this.player.JS_RequestInterface({
					funcName: 'startPreview',
					argument: JSON.stringify(options),
				}).then((data) => {
					console.info(JSON.stringify(data ? data.responseMsg : ''))
				}, (e) => {
					debugger
				})
			} else {
				this.$message('error')
			}

		},

		/**
		 * 按窗口索引播放
		 * @param code 编码
		 * @param index 窗口索引
		 * @param options
		 */
		playByIndex (code, index, options) {
			this.play(code, Object.assign({}, options, {
				wndId: index,
			}))
		},

		/**
		 * 回放
		 * @param code
		 * @param options
		 */
		playback (code, options = {
			recordLocation: 1, // 0：中心存储，1：设备存储
			transMode: 1,      // 传输协议:0 UDP; 1:CTP
			gpuMode: 0,        // 是否启用GPU硬解: 0:不启用；1：启用
			wndId: -1,          // -1:空闲窗口; 0:选中窗口；2：指定窗口
		}) {

			//起始时间为当前日期前三个月
			let now = new Date()
			now.setMonth(now.getMonth() - 1)
			let startTimeStamp = now.getTime()
			let endTimeStamp = new Date().getTime()

			let playDate = new Date()
			playDate.setMinutes(playDate.getMinutes() - 30)
			let playTimeStamp = playDate.getTime();
            playTimeStamp =startTimeStamp;
			// debugger


			options.cameraIndexCode = code

			options.startTimeStamp = options.startTimeStamp || Math.round(startTimeStamp / 1000).toString()
			options.endTimeStamp = options.endTimeStamp || Math.round(endTimeStamp / 1000).toString()
			options.playTimeStamp = options.playTimeStamp || Math.round(playTimeStamp / 1000).toString()

			this.player.JS_RequestInterface({
				funcName: 'startPlayback',
				argument: JSON.stringify(options),
			}).then((data) => {
				console.info(JSON.stringify(data ? data.responseMsg : ''))
			}, (e) => {
				debugger
			})
		},

		stop () {
			// 停止所有预览
			this.player.JS_RequestInterface({
				funcName: 'stopAllPreview',
			}).then((data) => {
				console.info(JSON.stringify(data ? data.responseMsg : ''))
			}, (e) => {
				debugger
			})

		},

		/**
		 * 反初始化
		 * 销毁插件前需要对插件进行反初始化，关闭网页或页面时视频 WEB 插件 V1.1.0 开始不用反初始化，
		 * 视频 WEB 插件内部可捕获到网页或页面的关闭触发自动反初始化。
		 */
		uninit () {
			// 清空
			this.player.JS_RequestInterface({
				funcName: 'uninit',
			}).then((data) => {
				console.info(JSON.stringify(data ? data.responseMsg : ''))
			}, (e) => {
				debugger
			})
		},

	},

}
</script>

<style lang="less" scoped>

.hik-player-wrapper {
	background: rgba(0, 0, 0, 1);
	padding:    4px;

	.hik-player {
		background: #272727;
		height:     100%;
		width:      100%;

		.hik-player__el {
			position: absolute;
			width:    120px;
			height:   80px;
		}

	}

	&--error {
	}

	&--checking {
	}

	&--ready {
		padding: 0;
	}

}

</style>
