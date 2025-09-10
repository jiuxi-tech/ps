/**
 *  @fb/log-center-ui     包入口文件
 * */

import pkg from '../package.json'

export const install = function (Vue, opts = {}) {
	([
		generator,
	]).map((fn) => {
		if (typeof opts[fn.name] !== 'undefined') {
			opts[fn.name](Vue, opts)
		} else {
			fn(Vue, opts)
		}
	})

}

// 混入路由页面
export function generator(Vue) {
	Vue.prototype.generator = Vue.prototype.generator || {
		service: [],
		router: [],
		store: []
	}

	Vue.prototype.generator.service.push(require.context('./service', true, /\.js/))
	Vue.prototype.generator.router.push(require.context('./router', true, /\.js/))
	Vue.prototype.generator.store.push(require.context('./store', true, /\.js/))
}


// console.log(
// 	`%c @fb/schedule-ui v${pkg.version}  %c docs: xxx`,
// 	`padding: 5px 0; background: #35D0CA; color: #fff; border-radius: 4px 0 0 4px;`,
// 	`padding: 5px 0; background: #fff; color: #35D0CA; border-radius: 0 4px 4px 0;`)

export default {
	install
}
