import Vue from 'vue'
import { Application } from '@fb/fb-core/src/index'
import VueRouter from 'vue-router'
import Vuex from 'vuex'
//import FbUI from 'fb-ui'
import FbUI from '@fb/fb-ui/src/index'
//import 'fb-ui/lib/fb-ui.css'

import FbThird from '@fb/fb-third/src/index'
import projectConfig from '../project.config'
import SystemApplicationRoot from './App'

import { setRouterBefore } from '@fb/fb-core/src/util/routerConfig'
import { eventBuryPointMonitor, mountedAppEvent } from '@fb/admin-base/util/buryPointMonitor'
import { setServiceReqRes } from '@fb/admin-base/util/serviceConfig'
import { setKeycloakConfig } from '@fb/fb-core/src/util/keycloak'

import TpComponents, { Page } from '@fb/tp-components/src/index'
import TpSchedule from '@fb/schedule-ui/src/index'
import AdminBase, { AdminBaseScreenUtil } from '@fb/admin-base'
import './assets/styles/main.less';
import MonitorUi from '@fb/monitor-ui'
// 导入组件
import LogCenterUi from '@fb/log-center-ui'
import { setKeycloakInitConfig } from '../@fb/fb-core/src/util/keycloak'

import './assets/styles/shanxi-main.less';

window.app = new Application({
	projectConfig: projectConfig,
	// 建议使用组件局部注册 --- 在 mixins: [Page]
	mixins: [Page],
	use: {
		Vue,
		Vuex,
		MonitorUi,
		VueRouter: {
			plugin: VueRouter,
			beforeEach(to, from, next) {
				// 设置 router 访问前规则
				setRouterBefore(to, from, next)
			}
		},
		// 添加组件
		LogCenterUi: {
			plugin: LogCenterUi,
			options: {

			},
		},
		FbUI: {
			plugin: FbUI,
			options: {
				theme: projectConfig.theme,
				// 隐藏table的loading效果
				defaultProps: {
					showLoading: false,
					
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
		TpComponents,
		AdminBase,
		TpSchedule
	},

	render: h => h(SystemApplicationRoot),
	warnHandler(msg, vm, trace) {
		app.root.$alert(`${msg} ${trace}`, null, null, {
			title: '警告',
			icon: 'warning',
			width: trace ? 800 : 420,
		})
	},
	errorHandler(err, vm, info) {
		// 去除脚本错误提示
		if ((err + '').toLowerCase().indexOf('script error') !== -1) {
			return
		}

		try {
			// 获取当前登录用户信息
			let userInfo = app.$datax.get('userInfo')

			let data = {
				// 模块编码
				moduleCode: 'error',
				// 模块名称
				moduleName: '错误日志收集',
				// 操作类型： login/logout/add/delete/edit/query/pass/unpass, 可以自己定义
				operateType: 'error',
				// 操作人id
				operterId: userInfo.personId,
				// 操作人名称
				operterName: userInfo.personName,
				// 错误日志信息
				message: err.stack
			}
			// 日志埋点
			app.$current.$logger.send(data);
		} catch (e) {

		}

		// app.root.$alert(`${err} \n[${info}]`, null, null, {
		// 	title: '异常',
		// 	icon: 'error',
		// })
	},
}).start(function () {
	app.$datax.set('src.theme', 'classic_blue')
	app.$datax.set('theme', 'classic_blue')
	app.$datax.set('TX_THEME', 'classic_blue')

	this.$datax.set('GLOBAL_CONFIG', {
		config: projectConfig,
		loginPath: projectConfig.router.loginPath,
		mainPath: projectConfig.router.mainPath,
	})
	// **** 暴漏全局接口 $AdminBaseScreenUtil ****
	this.$AdminBaseScreenUtil = Vue.prototype.$AdminBaseScreenUtil = new AdminBaseScreenUtil(projectConfig.pages.src.config)

	// 纯事件埋点监听
	eventBuryPointMonitor(this)
	// 挂载全局接口事件
	mountedAppEvent(this)
	// 设置 axios 请求头和响应头
	setServiceReqRes(this)

	// 配置 Keycloak
	setKeycloakConfig({
		url: 'http://localhost:8180/',
		
		realm: 'ps-realm',
		clientId: 'ps-be'
	})
	setKeycloakInitConfig({
		responseMode: 'fragment',
		onLoad: 'check-sso',
		silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
		checkLoginIframe: false,
		
	})
})
