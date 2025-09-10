/*!
 * Application.js
 * (c) 2020 lincong1987
 */
import pkg from '../../package.json'
import { each, isFunction, toArray, assign, isArray } from 'lodash-es'
import * as filters from '../filter'
import SystemApplicationRoot from '../views/SystemApplicationRoot'
import Datax from '../api/datax'
import { installService } from '../service'
import eventProxy from '../util/event'
import jQuery from 'jquery'
import axios from 'axios'
import dayjs from '../util/dayjs'
import { xssFilter } from '../util/xss'
import { utils } from '../util/index'
import serviceTool from '../util/serviceTool'
import storeTool from '../util/storeTool'
import routerTool from '../util/routerTool'
import { Generator } from './Generator'
import { Manager } from '../storage'
import { directives } from '../directive'
import {
	hasPermissionSync,
	hasPermission,
} from '../directive/permission/permission'
import { uuid } from '../util/utils'
import { getKeycloakInstance } from '../util/keycloak'

// console.log(
// 	`%c fb-core v${pkg.version}  %c docs: http://10.10.0.36/fb/fb-docs/#/fb-core/ `,
// 	`padding: 5px 0; background: #0284FE; color: #fff; border-radius: 4px 0 0 4px;`,
// 	`padding: 5px 0; background: #fff; color: #0284FE; border-radius: 0 4px 4px 0;`)

let generator = null
let projectVue
let projectVuex
let projectVueRouter
let root

/**
 * 项目启动类
 *
 */
class Application {

	static config (opts) {
	}

	constructor (applicationConfig) {

		//this.app = this

		this.applicationConfig = assign({}, {
			appName: 'app',
			autoMount: true,
			mixins: [],
			use: {},
			plugins: {},
			warnHandler () {
			},
			errorHandler () {
			},
		}, applicationConfig)

		window[this.applicationConfig.appName] = this

		if (!this.applicationConfig.use && !this.applicationConfig.use.Vue) {
			// console.log('[ERROR] applicationConfig.use 必须包含 Vue 构造器')
		}

		// 暴露 Vue
		// window.Vue = this.Vue = projectVue

		projectVue = this.applicationConfig.use.Vue

		this.applicationConfig.use.Vue = null

		projectVue.config.productionTip = false

		this.plugins = {}

		if (this.applicationConfig.use) {
			each(this.applicationConfig.use, (obj, name) => {

				let plugin = obj

				//this[name] = obj

				if (name !== 'Vue') {
					// 插件命名，扩展options
					if (obj.plugin && obj.options) {
						plugin = obj.plugin
						projectVue.use(plugin, obj.options)
					} else if (obj.plugin) {
						projectVue.use(obj.plugin)
					} else {
						projectVue.use(plugin)
					}
				}
				if (name === 'Vuex') {
					projectVuex = plugin
				} else if (name === 'VueRouter') {
					projectVueRouter = plugin
				}

			})
		}

		// 多模块项目加载方式
		projectVue.prototype.generator = projectVue.prototype.generator || {
			service: [],
			router: [],
			store: [],
		}

		// 历史项目兼容
		if (this.applicationConfig.projectConfig.generator) {
			let g = this.applicationConfig.projectConfig.generator()

			projectVue.prototype.generator.service.push(g.service)
			projectVue.prototype.generator.router.push(g.router)
			projectVue.prototype.generator.store.push(g.store)
		} else if (this.applicationConfig.generator) {
			let g = this.applicationConfig.generator()

			projectVue.prototype.generator.service.push(g.service)
			projectVue.prototype.generator.router.push(g.router)
			projectVue.prototype.generator.store.push(g.store)
		} else {
			// 加载当前项目
			projectVue.prototype.generator.service.push(
				require.context(`@/service`, true, /\.js/))
			projectVue.prototype.generator.router.push(
				require.context(`@/router`, true, /\.js/))
			projectVue.prototype.generator.store.push(
				require.context(`@/store`, true, /\.js/))
		}

		generator = new Generator(projectVue.prototype.generator)

		// window.onerror = (message, source, lineno, colno, error) => {

		// 	let msg = [
		// 		'Message: ' + message,
		// 		'URL: ' + source,
		// 		'Line: ' + lineno,
		// 		'Column: ' + colno,
		// 		'Error object: ' + JSON.stringify(error),
		// 	].join(' - ')

		// 	message && console.group('[FB ERROR]' + message)
		// 	msg && console.log('[INFO]', msg)
		// 	error && console.error(error)
		// 	message && console.groupEnd()

		// 	this.applicationConfig.errorHandler(message, source, lineno, colno,
		// 		error)

		// 	return false
		// }

		// projectVue.config.errorHandler = (err, vm, info) => {
		// 	console.group('[FB ERROR]' + info)
		// 	console.log('[INFO]', info)
		// 	console.error(err)
		// 	console.log('[VM]', vm)
		// 	console.groupEnd()
		// 	this.applicationConfig.errorHandler(err, vm, info)
		// }

		// projectVue.config.warnHandler = (msg, vm, trace) => {
		// 	console.warn(msg, vm, trace)
		// 	this.applicationConfig.errorHandler(msg, vm, trace)
		// }

		projectVue.config.performance = true

		// 开放创建服务对象
		//this.createService = createService

		if (!this.applicationConfig.render) {
			// console.log('[FBJS Application] 未定义 root render, 将使用系统默认 Root')
			this.applicationConfig.render = h => h(SystemApplicationRoot)
		}
		// applicationConfig.render = h => h(SystemApplicationRoot)

		this.projectConfig = this.applicationConfig.projectConfig || {}

		// console.log(`[FBJS Application] 初始化`)

//		if (applicationConfig.loginView) {
//
//		}

		this.addUtils()

		this.createEventbus()

		this.installService()

		this.setStorage()

		// datax 基于 Storage 跟 Vux 所以要先实例化一个存储对象
		this.setDatax()

		this.mixin()

		this.createStore()

		each(eventProxy, (value, key) => {
			this[key] = value
		})

//		console.log('new FB CORE version: ${pkg.version}')

		// this.start()

	}

	destroy () {

		window[this.applicationConfig.appName] = null

		each(this.applicationConfig.use, (value, key) => {
			this[key] = null
		})

		this.applicationConfig.use = {}

		this.$router = null
		this.$options = null
		this.$store = null
		this.$ = null
		this.xss = this.$xss = null
		this.$service = this.$svc = null
		this.$axios = null
		this.dayjs = this.$dayjs = null
		this.$utils = null
		this.$storage = null
		this.$datax.destroy()
		this.$datax = null
		this.$cookie = null
		this.$ebus.$off()
		this.$ebus = null
		this.plugins = {}

		projectVue.prototype.$ = null
		projectVue.prototype.$dayjs = null
		projectVue.prototype.$datax = null
		projectVue.prototype.$cookie = null
		projectVue.prototype.$service = null
		projectVue.prototype.$svc = null
		projectVue.prototype.$storage = null
		projectVue.prototype.$utils = null

		projectVue = null
		projectVuex = null
		projectVueRouter = null

		root.$destroy()
		root.$el.parentNode.removeChild(root.$el)
		root = null

		this.applicationConfig = null
		this.projectConfig = null

		each(eventProxy, (value, key) => {
			this[key] = null
		})

	}

	addUtils () {
		this.$uuid = projectVue.prototype.$uuid = uuid
		this.$ = projectVue.prototype.$ = jQuery
		this.dayjs = this.$dayjs =
			projectVue.prototype.$dayjs = dayjs
		this.xss = this.$xss =
			projectVue.prototype.$xss = (html) => {
				return xssFilter.process.call(xssFilter, html)
			}

		this.$axios = projectVue.prototype.$axios = axios
		utils.hasPermission = hasPermission
		utils.hasPermissionSync = hasPermissionSync
		this.$utils = utils
		
		// 注册 Keycloak 单例方法
		this.$kc = projectVue.prototype.$kc = getKeycloakInstance
	}

	installService () {
		this.$service =
			this.$svc = projectVue.prototype.$svc =
				projectVue.prototype.$service =
					installService(this, this.projectConfig.service)

//		this.applicationConfig.plugins.handleProjectService(this.$service)

		serviceTool.handle(this.$service, generator.service)
	}

	setStorage () {
		// 默认改为 Session
		this.$storage =
			projectVue.prototype.$storage =
				new Manager(this.projectConfig.storage_type || 'session')
	}

	setDatax () {
		this.$datax = projectVue.prototype.$datax = new Datax(
			this.$storage, projectVue,
			this.projectConfig.token.name || this.projectConfig.name ||
			'default',
		)

		this.$cookie = projectVue.prototype.$cookie = new Manager('cookie')
	}

	extend () {

		// projectVue.prototype.$app = this
		// Vue.prototype.$utils = util;
		// Vue.prototype.$storage = storage;
		// Vue.prototype.$api = api;

		// Vue.prototype.$lsdata = {}
	}

	mixin () {
		// 全局混入
		const {mixins} = this.applicationConfig
		if (mixins.length > 0) {
			mixins.forEach(mixinObj => projectVue.mixin(mixinObj))
		}

		// 注入生命周期
		projectVue.mixin({

			data () {
				const data = {
					data_lc: 'data_lc mixin success',
				}
				return data
			},
			created () {
				if (this.$options.appCreated &&
					isFunction(this.$options.appCreated)) {
					this.$options.appCreated.call(this)
				}
			},
			mounted () {
				if (this.$options.appReady &&
					isFunction(this.$options.appReady)) {
					this.$options.appReady.call(this)
				}
			},
			beforeDestroy () {
				if (this.$options.appBeforeDestroy &&
					isFunction(this.$options.appBeforeDestroy)) {
					this.$options.appBeforeDestroy.call(this)
				}
			},
			destroyed () {
				if (this.$options.appDestroyed &&
					isFunction(this.$options.appDestroyed)) {
					this.$options.appDestroyed.call(this)
				}
			},
			beforeRouteEnter (to, from, next) {
				// 在渲染该组件的对应路由被 confirm 前调用
				// 不！能！获取组件实例 `this`
				// 因为当守卫执行前，组件实例还没被创建

//				window.app.$current = null
//
				next((vm) => {
//					window.app.$current = vm
				})
			},
			beforeRouteUpdate (to, from, next) {
				// 在当前路由改变，但是该组件被复用时调用
				// 举例来说，对于一个带有动态参数的路径 /foo/:id，在 /foo/1 和 /foo/2 之间跳转的时候，
				// 由于会渲染同样的 Foo 组件，因此组件实例会被复用。而这个钩子就会在这个情况下被调用。
				// 可以访问组件实例 `this`

				next()
			},
			beforeRouteLeave (to, from, next) {
				// 导航离开该组件的对应路由时调用
				// 可以访问组件实例 `this`

				next()
			},

			methods: {

				// 测试
				app_debug () {
					let args = toArray(arguments)

					let dbg = ['[FB-DEBUG]'].concat(args)

					if (dbg[dbg.length - 1] == true) {
						// 如果最后一个是 true, 则,进入调试
						debugger
					}

					// console.log.apply(console, dbg)

					return args
				},

				// 暂停
				async sleep (ms) {
					return new Promise(resolve => setTimeout(resolve, ms))
				},

			},
		})
	}

	/**
	 * 启动项目
	 * @param afterApplicationStartCallback
	 */
	start (afterApplicationStartCallback) {

		// console.log(`[FBJS Application] start`)

		//Vue.use(VueRouter)

		// 注册全局过滤器
		each(filters, (filter, name) => {
			if (typeof name === 'string' && typeof filter === 'function') {
				projectVue.filter(name, filter)
				// // console.log(`[FBJS 注册全局过滤器] ${name}`)
			}
		})

		//
		// // =====================注册全局组件==================
		// Object.keys(commonCompConfig).forEach((p) => {
		//     Vue.component(p, commonCompConfig[p]);
		// });
		//
		// =====================注册全局指令==================
		Object.keys(directives).forEach((p) => {
			projectVue.directive(p, directives[p])
		})

		this.createRouter()

		// this.applicationConfig.start &&
		// this.applicationConfig.start.apply(this, [this, this.root])

		this.handleViews()

		let startConfig = {

			router: this.applicationConfig.router
				? this.applicationConfig.router
				: this.$router,

			store: this.applicationConfig.store
				? this.applicationConfig.store
				: this.$store,

			// el:"#el", // this.applicationConfig.el || '#app',
			render: this.applicationConfig.render, // extends:
		                                           // this.applicationConfig.extends,

			//router: this.applicationConfig.router, //render: h => h(),

			beforeCreate () {
				// 初始化FB运行环境后，项目

			},

			...this.applicationConfig.plugins,
		}

		this.setRoot(startConfig)

		afterApplicationStartCallback &&
		afterApplicationStartCallback.apply(this, [this, this.getRoot()])

		if (this.applicationConfig.autoMount) {
			this.mount()
		}

		//this.root.$mount(this.applicationConfig.el || '#app')

		return this
	}

	getRoot () {
		return root
	}

	setRoot (startConfig) {
		root = new projectVue(startConfig)
	}

	mount () {
		// 挂载实例
		root.$mount(this.applicationConfig.el || '#app')
	}

	// 创建路由实例
	createRouter () {
		// 为了路由前置监听，在 Vue 实例化之前挂载。
		// 在 Vue 实例化之后 刷新页面和第一次点击监听不到
		const VueRouter = projectVueRouter.plugin
			? projectVueRouter.plugin
			: projectVueRouter
		const indexPath = this.projectConfig.router.indexPath
		const mainPath = this.projectConfig.router.mainPath
		const startMode = this.projectConfig.router.startMode

		let routes
		let {
			constantRoutes,
			flatRoutes,
		} = routerTool.flatFilesToRouteArr(indexPath, generator.router)
		if (startMode === 'semiAuto') { // 半自动模式 --- 所有路由平铺在 mainPath 的children里
			routes = routerTool.initRoutes(constantRoutes, flatRoutes, mainPath)
		} else if (startMode === 'manual') { // 手动模式
			if (isArray(generator.router)) {
				routes = generator.router[generator.router.length - 1](
					indexPath).default
			} else {
				routes = generator.router(indexPath).default
			}
		} else if (startMode === 'routerBeforeEach') { // 守卫前置生成
			routes = constantRoutes
		} else { // 默认半自动模式
			routes = routerTool.initRoutes(constantRoutes, flatRoutes, mainPath)
		}

		// // 解决路由在 push/replace 了相同地址报错的问题
		const originalPush = VueRouter.prototype.push
		VueRouter.prototype.push = function push (location) {
			return originalPush.call(this, location).catch(err => err)
		}
		const originalReplace = VueRouter.prototype.replace
		VueRouter.prototype.replace = function replace (location) {
			return originalReplace.call(this, location).catch(err => err)
		}
		// 加密
		VueRouter.prototype.encrypt = function (code) {
			if (!code) return code
			code = code.toString()
			var c = String.fromCharCode(code.charCodeAt(0) + code.length)
			for (var i = 1; i < code.length; i++) {
				c += String.fromCharCode(
					code.charCodeAt(i) + code.charCodeAt(i - 1))
			}
			return escape(c)
		}
		// 解密
		VueRouter.prototype.decrypt = function (code) {
			if (!code) return code
			code = unescape(code)
			var c = String.fromCharCode(code.charCodeAt(0) - code.length)
			for (var i = 1; i < code.length; i++) {
				c += String.fromCharCode(
					code.charCodeAt(i) - c.charCodeAt(i - 1))
			}
			return c
		}

		console.log(routes)
		this.$router = new VueRouter({
			mode: this.projectConfig.router.mode || 'hash',
			routes: routes,
			scrollBehavior (to, from, savedPosition) {
				if (savedPosition) {
					return savedPosition
				}
				return {
					x: 0,
					y: 0,
				}
			},
		})

		// 为了路由前置监听，在 Vue 实例化之前挂载。
		// 在 Vue 实例化之后 刷新页面和第一次点击监听不到
		this.$router.beforeEach((to, from, next) => {
			if (projectVueRouter.beforeEach) {
				projectVueRouter.beforeEach(to, from, next, constantRoutes,
					flatRoutes)
			}
		})

		this.$router.afterEach((to, from) => {
			if (projectVueRouter.afterEach) {
				projectVueRouter.afterEach(to, from)
			}
			// console.log(`访问了 ${route.path}`)
//			document.title = (() => {
//				// 项目暂时不需要 route.meta.title 拼接模式
//				return `${this.projectConfig.title || 'JPX Project'}`
//
//				// return route.meta.title
//				// 	? `${route.meta.title} - ${this.projectConfig.title ||
//				// 	'JPX Project'}`
//				// 	: `${this.projectConfig.title || 'JPX Project'}`
//			})()

		})

	}

	createStore () {
		// console.log('[FBJS Application] 创建系统store')
		this.$store = new projectVuex.Store({})

//		this.applicationConfig.plugins.handleProjectStore(this.$store)

		storeTool.handle(this.$store, generator.store)
	}

	createEventbus () {

		this.$ebus = projectVue.prototype.$ebus = new projectVue()

	}

	/**
	 * 处理路由
	 */
	handleRouter () {

//		this.applicationConfig.plugins.handleProjectRouter(this.$router)

		// routerTool.handle(this.$router, this.generator.router)

		this.$router.beforeEach((to, from, next) => {
			// console.log(`将要访问 ${to.path}`)

			// 强制从 /main1 开始
			if (to.path !== '/main1' && from.path === '/') {
				next('/main1')
			} else {
				next()
			}
		})

		this.$router.beforeResolve((to, from, next) => {
			next()
		})

		this.$router.afterEach((route) => {
			// console.log(`访问了 ${route.path}`)
			document.title = (() => {
				return route.meta.title
					? `${route.meta.title} - ${this.projectConfig.title ||
					'JPX Project'}`
					: `${this.projectConfig.title || 'JPX Project'}`
			})()

		})

	}

	handleViews () {
//		this.applicationConfig.plugins.handleProjectViews(this.$router)
// 		viewTool.handle(this.$router,
// 			generator.views)
	}

	handleAssets () {
//		this.applicationConfig.plugins.handleProjectViews(this.$router)
//		viewTool.handle(
//			this.applicationConfig.plugins.generator.views, this.$router)
	}
}

export default Application
