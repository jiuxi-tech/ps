/**
 * 组件包入口文件
 * */
import pkg from '../package.json'
// 组件
import { componentList } from "./components";

import _propsUtil from './util/props-util.js'

// 工具类
import ScreenUtil from "./util/ScreenUtil";



export const install = function (Vue, opts = {}) {

	([
		// generator,
		setDefaultProps,
		registerComponents,
		create$env
	]).map((fn) => {
		if (typeof opts[fn.name] !== 'undefined') {
			opts[fn.name](Vue, opts)
		} else {
			fn(Vue, opts)
		}
	})

}
/**
 * 设置组件默认参数
 * @param Vue
 * @param opts
 */
export function setDefaultProps (Vue, opts) {
	window.ScreenBase_defaultProps = Object.assign({}, opts.defaultProps || {})
}
/**
 * 注册组件-list
 * @param {Object} Vue - Vue实例
 * */
export function registerComponents (Vue) {
	componentList.map(component => {
		_propsUtil.initDefaultProps(component.props, window.ScreenBase_defaultProps[component.name] || {})
		Vue.component(component.name, component)
	})
}

/**
 * 创建$env
 * @param {Object} Vue - Vue实例
 * */
export function create$env (Vue) {
	Vue.prototype.$env = (name) => {
		let value = process.env[name]
		if (typeof value !== 'undefined') {

			if ((value + '').trim().indexOf('JSON#') === 0) {
				try {
//				value = (new Function('value',
//					`debugger; \n return (value+'').trim().replace('JSON#', '')`))(
//					value)

					value = (new Function("//以下你看到的代碼是經過包装的。\nreturn " + (value+'').trim().replace('JSON#', '')))();
				} catch (e) {
					console.log(e)
				}
			}
		}
		return value
	}
}

export function generator(Vue) {
	// Vue.prototype.generator = Vue.prototype.generator || {
	// 	service: [],
	// 	router: [],
	// 	store: []
	// }

	// Vue.prototype.generator.service.push(require.context('./service', true, /\.js/))
	// Vue.prototype.generator.router.push(require.context('./router', true, /\.js/))
	// Vue.prototype.generator.store.push(require.context('./store', true, /\.js/))
}

console.log(
	`%c @fb/screen-base v${pkg.version}  %c docs: http://192.168.90.24:8080/fb-docs/#/fb-screen/screen-base/ `,
	`padding: 5px 0; background: #35D0CA; color: #fff; border-radius: 4px 0 0 4px;`,
	`padding: 5px 0; background: #fff; color: #35D0CA; border-radius: 0 4px 4px 0;`)

export { ScreenUtil }
export default {
	install
}

