/*!
 * index
 * (c) 2020 lincong1987
 */
import Vue from 'vue/dist/vue.esm.js'
import FbSpin from './src/FbSpin'
import { prefix } from '../../../src/config'

FbSpin.install = function (adapter) {
	adapter.component(FbSpin.name, FbSpin)
}

let instance

const create = function (options) {

	if (!instance) {
		instance = new Vue({
			parent: loading.parent,
			components: {
				FbSpin,
			},
			template: `
				<transition :name="transition">
				<div class="${prefix}-global-loading-wrapper"
				     id="${prefix}-global-loading-wrapper" v-if="show"
				     :style="{background}"
				>
					<fb-spin
						:color="color"
						:text-color="textColor"
						:size="size"
						:border-width="borderWidth"
						:text="text"
						:lock="lock"
						:spinner="spinner"
					></fb-spin>
				</div>
				</transition>
			`,
			data () {
				return Object.assign(loading.defaults, options)
			},
			beforeDestroy () {
				document.body.removeChild(instance.$el)
				// console.log("spin beforeDestroy")

			}
		})
		instance.$mount()
		document.body.appendChild(instance.$el)
	}

	return instance
}

var loading = function (option) {

}

loading.parent = null

loading.defaults = {
	borderWidth: '2px',
	color: '#0284FE',
	textColor: '#0284FE',
	size: '24px',
	text: '请稍等...',
	show: false,
	lock: true,
	spinner: '',
	transition: 'zoom-center-to-corner',
	background: '',
}

loading.show = function (prop = {}) {

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

loading.update = function (prop = {}) {

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

loading.hide = function () {

	if (!instance) {
		instance = create()
	}

	if (instance) {
		instance.show = false
	}
}

export default loading



