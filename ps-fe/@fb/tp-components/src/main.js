/**
 * 运行项目入口文件
 * */
import Vue from 'vue'
import { Application } from '@fb/fb-core/src/index'
import VueRouter from 'vue-router'
import Vuex from 'vuex'
import FbUI from '@fb/fb-ui/src/index'

import projectConfig from '../../../project.config'
import SystemApp from '@fb/fb-core/src/views/SystemApplicationRoot'

import TpComponents, {reportSdk, Page} from './index'

console.log(reportSdk, 'reportSdk')
console.log(Page, 'Page')

import { setRouterBefore } from '@fb/fb-core/src/util/routerConfig'

window.app = new Application({
	projectConfig: projectConfig,
	mixins: [Page],
	use: {
		Vue,
		TpComponents: {
			plugin: TpComponents,
			options: {
				noSDK: false
			}
		},
		VueRouter: {
			plugin: VueRouter,
			beforeEach(to, from, next) {
				next()
				// 设置 router 访问前规则
				// setRouterBefore(to, from, next)
			}
		},
		Vuex,
		FbUI,
	},

	render: h => h(SystemApp),
	warnHandler (msg, vm, trace) {
		app.root.$alert(`${msg} ${trace}`, null, null, {
			title: '警告',
			icon: 'warning',
			width: trace ? 800 : 420,
		})
	},
	errorHandler (err, vm, info) {
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
		}catch (e){

		}

	},
}).start(function () {

	this.$datax.set('GLOBAL_CONFIG', {
		config: projectConfig,
		loginPath: projectConfig.router.loginPath,
		mainPath: projectConfig.router.mainPath,
	})

})

