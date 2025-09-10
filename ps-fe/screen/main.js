import Vue from 'vue'
import {Application} from '@fb/fb-core/src/index'
import VueRouter from 'vue-router'
import Vuex from 'vuex'
import FbUI from '@fb/fb-ui/src/index'
import Qs from "qs";

import FbThird from '@fb/fb-third/src/index'
import projectConfig from '../project.config'

import {setServiceReqRes} from '@fb/fb-core/src/util/serviceConfig'
import {setRouterBefore} from '@fb/fb-core/src/util/routerConfig'


import ScreenApp from './ScreenApp'

import ScreenBase, {ScreenUtil} from '@fb/screen-base/src/index'
import './assets/styles/main.less';

window.app = new Application({
	projectConfig: projectConfig,
	// 自动化配置
	generator() {
		return {
			service: require.context('./service', true, /\.js/),
			router: require.context('./router', true, /\.js$/),
			store: require.context('./store', true, /\.js$/)
		}
	},
	use: {
		Vue,
		ScreenBase,
		VueRouter: {
			plugin: VueRouter,
			beforeEach(to, from, next) {
				// 设置 router 访问前规则
				setRouterBefore(to, from, next)
			}
		},
		Vuex,
		FbUI: {
			plugin: FbUI,
			options: {theme: projectConfig.theme}
		},
		FbThird: {
			plugin: FbThird,
			options: {
				tdt: {
					v: "4.0",
					// tk: "7f013d0186775b063d6a046977bbefc6",
					// plugins: ["CarTrack"]
				},
				swiper: {}
			}
		},
	},
	render: h => h(ScreenApp),
	warnHandler(msg, vm, trace) {
		// app.root.$alert(`${msg} ${trace}`, null, null, {
		// 	title: '警告',
		// 	icon: 'warning',
		// 	width: trace ? 800 : 420,
		// })
	},
	errorHandler(err, vm, info) {
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
	this.$screenUtil = Vue.prototype.$screenUtil = new ScreenUtil(projectConfig.pages.screen.config)

	this.$utils.jumpAdmin = function (path, params, target = '_blank') {
		let pStr = ``
		for (let key in params) {
			pStr += `${key}=${params[key]}&`
		}

		let str = ``
		if (pStr.length === 0) {
			str = `${projectConfig.router.adminPath}#${path}`
		} else {
			pStr = pStr.slice(0, pStr.length - 1)
			str = `${projectConfig.router.adminPath}#${path}?${pStr}`
		}

		window.open(str, target)
	}
//	// 纯事件埋点监听
//	eventBuryPointMonitor(this)
	// 设置 axios 请求头和响应头
	setServiceReqRes(this)


})

window.app.Qs = Qs;
//Vue.prototype.$moment = moment
//moment.locale('zh-cn'); // 这里是进行了汉化 不写这句默认格式是外国的

// 文件下载方法
window.app.download = function (response) {

	let blob = new Blob([response.data])
	let contentDisposition = response.headers['content-disposition'];

	let fileName = ""
	// 附件下载
	if (contentDisposition && contentDisposition.indexOf("filename*") != -1) {
		let file = contentDisposition.split("utf-8''")[1]
		fileName = decodeURI(file);
	} else if (contentDisposition && contentDisposition.indexOf("filename=") != -1) {
		let file = contentDisposition.split("=")[1]
		fileName = decodeURI(file);
	}

	if (window.navigator.msSaveOrOpenBlob) {
		// console.log(2)
		navigator.msSaveBlob(blob, fileName)
	} else {
		// console.log(3)
		let link = document.createElement('a')
		link.style.display = "none";
		link.href = window.URL.createObjectURL(blob)
		link.download = fileName
		link.click()
		window.URL.revokeObjectURL(link.href)
		//释放内存
		document.body.removeChild(link); //下载完成移除元素
	}
}

