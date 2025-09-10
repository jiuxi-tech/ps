/**
 * @fb/tp-components 包入口文件
 * */

import pkg from '../package.json'

// 报表服务
import reportSdk from "./util/report-sdk";
// 混入
import Page from "./util/Page";

// 组件
import TpDatepicker from './components/tp-datepicker/tp-datepicker'
import TpDialog from './components/tp-dialog/tp-dialog'
import TpDialogTab from './components/tp-dialog-tab/tp-dialog-tab'
import TpDialogFlowTab from './components/tp-dialog-flow-tab/tp-dialog-flow-tab'
import TpDialogStepTab from './components/tp-dialog-step-tab/tp-dialog-step-tab'
import TpUpload from './components/tp-upload/tp-upload'
import TpUploadPath from './components/tp-upload-path/tp-upload-path'

const componentList = [
	TpDatepicker,
	TpDialog,
	TpDialogTab,
	TpDialogFlowTab,
	TpDialogStepTab,
	TpUpload,
	TpUploadPath,
]

export const install = function (Vue, opts = {}) {
	([
		opts.noSDK ? undefined : registerSDK, // 配置项控制sdk是否引入
		registerComponents
	]).map((fn) => {
		if (!fn) {
			return
		}
		if (typeof opts[fn.name] !== 'undefined') {
			opts[fn.name](Vue, opts)
		} else {
			fn(Vue, opts)
		}
	})

}

/**
 * 注册组件-list
 * @param {Object} Vue - Vue实例
 * */
export function registerComponents (Vue) {
	console.log('%ctp-components registerComponents---组件', 'color: orange')
	componentList.map(component => {
		Vue.component(component.name, component)
	})
}


/**
 * 注册SDK
 * @param {Object} Vue - Vue实例
 * */
export function registerSDK (Vue) {
	// console.log('%ctp-components registerSDK---软件', 'color: orange')
	reportSdk.install(Vue)
}

// console.log(
// 	`%c @fb/tp-components v${pkg.version}  %c docs: xxx`,
// 	`padding: 5px 0; background: #35D0CA; color: #fff; border-radius: 4px 0 0 4px;`,
// 	`padding: 5px 0; background: #fff; color: #35D0CA; border-radius: 0 4px 4px 0;`)

// 支持模块化引入
export {reportSdk, Page}

export default {
	install,
	Page
}