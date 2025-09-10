import { componentList } from "./components";

// lib 查看 common.less
import "./assets/styles/index.less";

export const install = function (Vue, opts = {}) {

	([
		// generator,
		registerComponents
	]).map((fn) => {
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
	componentList.map(component => {
		Vue.component(component.name, component)
	})
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

/* istanbul ignore if */
if (typeof window !== 'undefined' && window.Vue) {
	console.log('Auto install screen-base')
	install(window.Vue)
}

export default {
	install
}

