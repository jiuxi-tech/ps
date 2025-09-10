// 主题列表
const themeList = [
	{
		value: 'midnight-blue',
		label: '午夜蓝',
	},
	{
		value: 'sky-blue',
		label: '天空蓝',
	},
	{
		value: 'dark-blue',
		label: '深夜蓝',
	},
	{
		value: 'navy-blue',
		label: '海军蓝',
	},
	{
		value: 'cornflower-blue',
		label: '矢车菊蓝',
	},
	{
		value: 'royal-blue',
		label: '皇室蓝',
	},
	{
		value: 'geekl-blue',
		label: '极客蓝',
	},
	{
		value: 'trans-blue',
		label: '清澈蓝',
	},
	{
		value: 'black-feather-blue',
		label: '黑羽蓝',
	},
	{
		value: 'daishan-green',
		label: '黛山绿',
	},
	{
		value: 'cerulean-blue',
		label: '青山蓝',
	},
]
// 路由过度列表
const transtionList = [
	{
		label: '翻转',
		value: 'flip',
	},
	{
		label: '立体翻转',
		value: 'flip3D',
	},
	{
		label: '淡入淡出',
		value: 'fade',
	},
	{
		label: '渐隐渐显缩放',
		value: 'fade-transform',
	},
	{
		label: '左移淡入',
		value: 'breadcrumb',
	},
	{
		label: '向下延展',
		value: 'slide-to-down',
	},
	{
		label: '显隐',
		value: 'v-modal',
	},
	{
		label: '线性显影',
		value: 'fade-in-linear',
	},
	{
		label: 'fadeIn',
		value: 'fade-in',
	},
	{
		label: 'zoomInCenter',
		value: 'zoom-in-center',
	},
	{
		label: 'zoomInTop',
		value: 'zoom-in-top',
	},
	{
		label: 'zoomInBottom',
		value: 'zoom-in-bottom',
	},
	{
		label: 'zoomInLeft',
		value: 'zoom-in-left',
	},
	{
		label: 'list',
		value: 'list',
	},
	{
		label: 'zoomCenterToCorner',
		value: 'zoom-center-to-corner',
	},
	{
		label: 'slideDownZoomTopLeft',
		value: 'slide-down-zoom-top-left',
	},
	{
		label: 'collapseTransition',
		value: 'collapse-transition',
	},
	{
		label: 'slide-top',
		value: 'slide-top',
	},
	{
		label: 'slide-bottom',
		value: 'slide-bottom',
	},
	{
		label: 'slide-left',
		value: 'slide-left',
	},
	{
		label: 'slide-right',
		value: 'slide-right',
	},

	{
		label: 'slide-tr',
		value: 'slide-tr',
	},
	{
		label: 'slide-tl',
		value: 'slide-tl',
	},
	{
		label: 'slide-br',
		value: 'slide-br',
	},
	{
		label: 'slide-bl',
		value: 'slide-bl',
	},
	{
		label: 'ping',
		value: 'ping',
	},
	{
		label: 'pulsate-fwd',
		value: 'pulsate-fwd',
	},
	{
		label: 'heartbeat',
		value: 'heartbeat',
	},
	{
		label: 'scale-up-top',
		value: 'scale-up-top',
	},
	{
		label: 'scale-up-center',
		value: 'scale-up-center',
	},
	{
		label: 'puff-in-center',
		value: 'puff-in-center',
	},
	{
		label: 'puff-in-ver',
		value: 'puff-in-ver',
	},
	{
		label: 'puff-in-hor',
		value: 'puff-in-hor',
	},
	{
		label: 'puff-in-top',
		value: 'puff-in-top',
	},
	{
		label: 'puff-in-bottom',
		value: 'puff-in-bottom',
	},
	{
		label: 'bounce-top',
		value: 'bounce-top',
	},
	{
		label: 'bounce-bottom',
		value: 'bounce-bottom',
	},
	{
		label: 'bounce-left',
		value: 'bounce-left',
	},
	{
		label: 'bounce-right',
		value: 'bounce-right',
	},
	{
		label: 'flip-2-hor-top-1',
		value: 'flip-2-hor-top-1',
	},
	{
		label: 'flip-2-hor-top-2',
		value: 'flip-2-hor-top-2',
	},
	{
		label: 'flip-scale-up-ver',
		value: 'flip-scale-up-ver',
	},
	{
		label: 'flip-scale-down-ver',
		value: 'flip-scale-down-ver',
	},
]
// 开发模式下缩放比例列表
const scaleList = [
	{
		value: '0_25',
		label: '0.25x',
	},
	{
		value: '0_33',
		label: '0.33x',
	},
	{
		value: '0_5',
		label: '0.5x',
	},
	{
		value: '0_66',
		label: '0.66x',
	},
	{
		value: '0_88',
		label: '0.88x',
	},
	{
		value: '1',
		label: '1x',
	},
	{
		value: '1_5',
		label: '1.5x',
	},
	{
		value: '2',
		label: '2x',
	},
	{
		value: '3',
		label: '3x',
	},
	{
		value: '4',
		label: '4x',
	},
	{
		value: '5',
		label: '5x',
	},
]
// 缩放模式列表
const modeList = [
	{
		value: 'original',
		label: '整比缩放',
	},
	{
		value: 'autoHeight',
		label: '填充',
	},
	{
		value: 'adapt',
		label: '适应',
	},
]

/**
 * 大屏工具类 - 一般暴露到全局使用
 * @desc 维护 主题, 缩放模式, 路由过度效果, 开发相关调试
 * @desc 只维护基本变量 - 使用app.$的jquery操控部分html
 * */
class ScreenUtil {
	constructor(params) {
		// 初始化事件收集器
		this.onObj = {}
		this.oneObj = {}
		// 设置屏幕基本信息
		this.setContainer(params.container || '#app')
		this.setWidth(params.width || 1920)
		this.setHeight(params.height || 1080)
		this.setDev(app.$datax.get('screen.dev') || params.dev || false)
		// 设置屏幕缩放模式
		this.setModes(params.modes || modeList)
		this.setMode(app.$datax.get('screen.mode') || params.mode || 'original')
		// 设置开发模式下缩放大小
		this.setScales(params.scales || scaleList)
		this.setScale(app.$datax.get('screen.scale') || params.scale || '1')
		// 设置主题
		this.setThemes(params.themes || themeList)
		this.setTheme(app.$datax.get('screen.theme') || params.theme || 'midnight-blue')
		// 设置路由过度效果
		this.setTransitions(params.transtions || transtionList)
		this.setTransition(app.$datax.get('screen.transtion') || params.transtion || 'slide-to-down')

		// 默认 false 请查看requestFullscreen注释详情
		this.setFullscreen(false)
	}

	/**
	 * 获取画布容器
	 * */
	getContainer() {
		return this.container
	}
	/**
	 * 设置画布容器
	 * @param {String/Object} container
	 * */
	setContainer(container) {
		this.container = container
		this.trigger('screen.container', container)

		// 背景样式类名保险
		app.$('body').addClass('screen-body')
	}

	/**
	 * 获取画布宽度
	 * */
	getWidth() {
		return this.width
	}
	/**
	 * 设置画布宽度
	 * @param {Number} width
	 * */
	setWidth(width) {
		this.width = width
		this.trigger('screen.width', width)
	}

	/**
	 * 获取画布高度
	 * */
	getHeight() {
		return this.height
	}
	/**
	 * 设置画布高度
	 * @param {Number} height
	 * */
	setHeight(height) {
		this.height = height
		this.trigger('screen.height', height)
	}

	/**
	 * 获取是否处于开发模式
	 * */
	getDev() {
		return this.dev
	}
	/**
	 * 设置开发模式
	 * @param {Boolean} dev
	 * */
	setDev(dev) {
		this.dev = dev
		if (dev) {
			app.$('body').addClass('dev')
		} else {
			app.$('body').removeClass('dev')
		}

		app.$datax.set('screen.dev', dev)
		this.trigger('screen.dev', dev)
	}

	/**
	 * 获取开发模式缩放比例
	 * */
	getScale() {
		return this.scale
	}
	/**
	 * 设置开发模式缩放比例
	 * @param {String} scale
	 * */
	setScale(scale) {
		this.scale = scale

		app.$(this.container).
			removeClass(this.scales.map(n => `scale-${n.value}`).join(' ')).
			addClass(`scale-${scale}`)

		app.$datax.set('screen.scale', scale)
		this.trigger('screen.scale', scale)
	}
	/**
	 * 计算屏幕缩放比例
	 * */
	calcScale() {
		let {
			height,
			width,
			mode,
			container
		} = this

		let $container = app.$(container)
		let $height = app.$(window).height()
		let $width = app.$(window).width()
		let whhw = ''

		if (mode === 'autoHeight') {
			let zoomX = ($width / width).toFixed(4)
			let zoomY = ($height / height).toFixed(4)
			$container.removeClass('wh hw').addClass(whhw).css({
				'transform': '  scale(' + zoomX + ',' + zoomY + ') ',
				opacity: 1,
				left: '0',
				top: '0',
			})
			app.$datax.set('screen.zoomX', zoomX)
			app.$datax.set('screen.zoomY', zoomY)
		} else if (mode === 'adapt') {
			let wh = height / width
			let $wh = $height / $width
			let aZoom = ($height / height).toFixed(4)
			if ($wh > wh) {
				aZoom = ($width / width).toFixed(4)
			}
			$container.removeClass('wh hw').addClass(whhw).css({
				'transform': '  scale(' + aZoom + ') translate(-50%, -50%)',
				opacity: 1,
				left: '50%',
				top: '50%',
			})
			app.$datax.set('screen.aZoom', aZoom)
		} else {
			let zoom = ($width / width).toFixed(4)

			$container.removeClass('wh hw').addClass(whhw).css({
				'transform': '  scale(' + zoom + ') ',
				opacity: 1,
				left: '0',
				top: '0',
			})
			app.$datax.set('screen.zoom', zoom)
		}
	}
	/**
	 * 获取开发模式缩放列表
	 * */
	getScales() {
		return this.scales
	}
	/**
	 * 设置开发模式缩放列表
	 * @param {Array} scales
	 * */
	setScales(scales) {
		this.scales = scales
	}
	/**
	 * 获取开发模式默认缩放列表
	 * */
	getScaleList() {
		return scaleList
	}

	/**
	 * 获取屏幕兼容模式
	 * */
	getMode() {
		return this.mode
	}
	/**
	 * 设置屏幕兼容模式
	 * @param {String} mode
	 * */
	setMode(mode) {
		this.mode = mode

		app.$datax.set('screen.mode', mode)
		this.trigger('screen.mode', mode)
	}
	/**
	 * 获取屏幕兼容模式列表
	 * */
	getModes() {
		return this.modes
	}
	/**
	 * 设置屏幕兼容模式列表
	 * @param {Array} modes
	 * */
	setModes(modes) {
		this.modes = modes
	}
	/**
	 * 获取默认屏幕兼容模式列表
	 * */
	getModeList() {
		return modeList
	}

	/**
	 * 获取当前主题
	 * */
	getTheme() {
		return this.theme
	}
	/**
	 * 设置当前主题
	 * @param {String} theme 主题
	 * */
	setTheme(theme) {
		this.theme = theme
		const htmlEl = document.getElementsByTagName('html')[0]
		htmlEl.className = ''
		htmlEl.className = `theme-${theme}`

		app.$datax.set('screen.theme', theme)
		this.trigger('screen.theme', theme)
	}
	/**
	 * 获取当前主题列表
	 * */
	getThemes() {
		return this.themes
	}
	/**
	 * 设置当前主题列表
	 * @param {Array} themes 主题列表
	 * */
	setThemes(themes) {
		this.themes = themes
	}
	/**
	 * 获取默认提供主题列表
	 * */
	getThemeList() {
		return themeList
	}
	/**
	 * 获取当前路由过度模式
	 * */
	getTransition() {
		return this.transtion
	}
	/**
	 * 设置当前路由过度模式
	 * @param {String} transtion
	 * */
	setTransition(transtion) {
		this.transtion = transtion
		app.$datax.set('screen.transtion', transtion)
		this.trigger('screen.transtion', transtion)
	}
	/**
	 * 获取当前路由过度模式列表
	 * */
	getTransitions() {
		return this.transtions
	}
	/**
	 * 设置当前路由过度模式列表
	 * @param {Array} transtions
	 * */
	setTransitions(transtions) {
		this.transtions = transtions
	}
	/**
	 * 获取默认路由过度模式列表
	 * */
	getTransitionList() {
		return transtionList
	}
	/**
	 * 获取全屏状态
	 * */
	getFullscreen() {
		return this.fullscreen
	}
	/**
	 * 设置全屏状态 - 刷新页面会还原屏幕-建议：提示用户F11按键全屏
	 * @param {Boolean} fullscreen
	 * */
	setFullscreen(fullscreen) {
		if (fullscreen) {
			this.requestFullscreen()
		} else {
			this.exitFullscreen()
		}
		this.fullscreen = fullscreen
		app.$datax.set('screen.fullscreen', fullscreen)
		this.trigger('screen.fullscreen', fullscreen)
	}
	/**
	 * 是否处于全屏状态
	 * */
	isFullscreen () {
		return !!(document.fullscreenElement || document.msFullscreenElement || document.mozFullScreenElement)
	}
	/**
	 * 开启全屏
	 * error 提示：浏览器禁止用户以外的动作开启全屏，所以默认进入系统不能直接全屏
	 * 建议：提示用户F11按键全屏
	 * */
	requestFullscreen () {
		if (this.isFullscreen()) {
			return
		}
		let element = document.documentElement
		if (element.requestFullscreen) {
			element.requestFullscreen()
		} else if (element.msRequestFullscreen) {
			element.msRequestFullscreen()
		} else if (element.mozRequestFullScreen) {
			element.mozRequestFullScreen()
		} else if (element.webkitRequestFullscreen) {
			element.webkitRequestFullscreen()
		}
	}
	/**
	 * 退出全屏
	 * */
	exitFullscreen () {

		if (!this.isFullscreen()) {
			return
		}

		if (document.exitFullscreen) {
			document.exitFullscreen()
		} else if (document.msExitFullscreen) {
			document.msExitFullscreen()
		} else if (document.mozCancelFullScreen) {
			document.mozCancelFullScreen()
		} else if (document.webkitExitFullscreen) {
			document.webkitExitFullscreen()
		}
	}

	on(key, fn) {
		if (fn && typeof (fn) === 'function') {
			if (this.onObj[key] === undefined) {
				this.onObj[key] = [];
			}
			this.onObj[key].push(fn);
		} else {
			throw new Error('请传入正确的回调函数');
		}
	}
	one(key, fn) {
		if (this.oneObj[key] === undefined) {
			this.oneObj[key] = [];
		}

		this.oneObj[key].push(fn);
	}
	off(key) {
		this.onObj[key] = [];
		this.oneObj[key] = [];
	}
	trigger(...args) {
		if (args.length === 0) {
			return false;
		}
		const key = args[0];
		args = [].concat(Array.prototype.slice.call(args, 1));

		if (this.onObj[key] !== undefined
			&& this.onObj[key].length > 0) {
			Object.keys(this.onObj[key]).forEach((i) => {
				this.onObj[key][i].apply(null, args);
			});
		}
		if (this.oneObj[key] !== undefined
			&& this.oneObj[key].length > 0) {
			Object.keys(this.oneObj[key]).forEach((i) => {
				this.oneObj[key][i].apply(null, args);
				this.oneObj[key][i] = undefined;
			});
			this.oneObj[key] = [];
		}
		return null;
	}
}

export default ScreenUtil
