/*!
* index
* (c) 2020 lincong1987
*/

import Vue from 'vue'

import FbLoadingBar from './src/FbLoadingBar'
import { prefix } from '../../../src/config'

FbLoadingBar.install = function (adapter) {
	adapter.component(FbLoadingBar.name, FbLoadingBar)
}

let instance

var loadingBar = function (option) {

}

loadingBar.parent = null

loadingBar.defaults = {
	type: 'primary',
	height: 2,
	color: null,
	process: 0,
	show: false,
}

var remove = function () {
	instance.show = false
}

loadingBar.start = function (options) {
	if (instance) {
		remove()
	}
	return create(options)
}

loadingBar.update = function (prop) {

	if (!instance) {
		instance = create()
	}

	if (instance && prop) {
		instance.show = true
		for (let key in prop) {
			instance[key] = prop[key]
		}
	}
}

loadingBar.process = function (process) {

	if (!instance) {
		instance = create()
	}

	if (instance) {
		instance.show = true
		instance.process = process
	}
}

loadingBar.finish = function () {

	if (!instance) {
		instance = create()
	}

	if (instance) {
		instance.show = true
		instance.process = 100
		setTimeout(function () {
			remove()
		}, 1000)
	}
}

const create = function (options = {}) {

	if (loadingBar.parent) {

	}

	if (!instance) {
		instance = new Vue({
			parent: loadingBar.parent,
			components: {
				FbLoadingBar,
			},
		//	el: '#fb-loading-bar-wrapper',
			template: `
				<div class="${prefix}-loading-bar-wrapper" id="${prefix}-loading-bar-wrapper">
					<FbLoadingBar v-if="show" :type="type" :height="height"
								  :color="color"
								  :process="process"/>
				</div>
			`,
			data () {
				return Object.assign(loadingBar.defaults, options)
			},
			beforeDestroy() {
				document.body.removeChild(instance.$el);
			}
			// render (createElement) {
			// 	return createElement('FbLoadingBar', {
			// 		props: {...this.$data},
			// 	})
			// },
		})
		instance.$mount()
		document.body.appendChild(instance.$el);
		//const vm = instance.$mount('#fb-loading-bar-wrapper')
		//document.body.appendChild(vm.$el)
	}

	return instance
}

export default loadingBar


