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

export function generator(Vue) {
	Vue.prototype.generator = Vue.prototype.generator || {
		service: [],
		router: [],
		store: []
	}

	Vue.prototype.generator.service.push(require.context('./service', true, /\.js/))
	Vue.prototype.generator.router.push(require.context('./router', true, /\.js/))
	// Vue.prototype.generator.store.push(require.context('./store', true, /\.js/))
}

export default {
	install
}

