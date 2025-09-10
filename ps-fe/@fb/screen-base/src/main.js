/**
 * 项目入口文件
 * */
import Vue from 'vue'
import { Application } from '@fb/fb-core/src/index'
import VueRouter from 'vue-router'
import Vuex from 'vuex'
import FbUI from '@fb/fb-ui/src/index'

import FbThird from '@fb/fb-third/src/index'
import projectConfig from '../../../project.config'

//import {setServiceReqRes} from 'fb-core/src/util/serviceConfig'
import { setRouterBefore } from '@fb/fb-core/src/util/routerConfig'

import ScreenApp from './ScreenApp'
import ScreenBase from './index'
import ScreenUtil from './util/ScreenUtil'
import './assets/styles/index.less'

window.app = new Application({
	projectConfig: projectConfig,
	// 自动化配置
	generator () {
		return {
			service: require.context('./service', true, /\.js/),
			router: require.context('./router', true, /\.js$/),
			store: require.context('./store', true, /\.js$/),
		}
	},
	use: {
		Vue,
		ScreenBase,
		VueRouter: {
			plugin: VueRouter,
			beforeEach (to, from, next) {
				// 设置 router 访问前规则
				setRouterBefore(to, from, next)
			},
		},
		Vuex,
		FbUI: {
			plugin: FbUI,
			options: {
				theme: projectConfig.theme,
				defaultProps: {
					FbFlop: {
						type: 'simple',
					},
				},
			},

		},
		FbThird: {
			plugin: FbThird,
			options: {
				tdt: {
					v: '4.0',
					// tk: "7f013d0186775b063d6a046977bbefc6",
					// plugins: ["CarTrack"]
				},
				swiper: {},
			},
		},
	},
	render: h => h(ScreenApp),
	warnHandler (msg, vm, trace) {
		// app.root.$alert(`${msg} ${trace}`, null, null, {
		// 	title: '警告',
		// 	icon: 'warning',
		// 	width: trace ? 800 : 420,
		// })
	},
	errorHandler (err, vm, info) {
		// 去除脚本错误提示
		// if ((err + '').toLowerCase().indexOf('script error') !== -1) {
		// 	return
		// }
		//
		// app.root.$alert(`${err} \n[${info}]`, null, null, {
		// 	title: '异常',
		// 	icon: 'error',
		// })
	},
}).start(function () {
	this.$datax.set('GLOBAL_CONFIG', {
		config: projectConfig,
		loginPath: projectConfig.router.loginPath,
		mainPath: projectConfig.router.mainPath,
	})

	// ****暴漏全局接口****
	this.$screenUtil = Vue.prototype.$screenUtil =
		new ScreenUtil(projectConfig.pages.src.config)
	// 设置 axios 请求头和响应头
	// setServiceReqRes(this)
})


