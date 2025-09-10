/*!
 * index
 * (c) 2020 lincong1987
 */

import FbNotification from './src/FbNotification'
import FbNotificationWrapper from './src/FbNotificationWrapper'
import Vue from 'vue/dist/vue.esm.js'
import { assign } from 'lodash-es'

let wrapperTopLeft
let wrapperTopRight
let wrapperBottomLeft
let wrapperBottomRight

/**
 * 初始化 4个容器
 */
const init = function () {
	if (!wrapperTopLeft) {
		wrapperTopLeft = initWrapper('top-left')
	}
	if (!wrapperTopRight) {
		wrapperTopRight = initWrapper('top-right')
	}
	if (!wrapperBottomLeft) {
		wrapperBottomLeft = initWrapper('bottom-left')
	}
	if (!wrapperBottomRight) {
		wrapperBottomRight = initWrapper('bottom-right')
	}
}

const initWrapper = function (placement) {

	let wrapper = new Vue({
		parent: notification.parent,
		components: {
			FbNotificationWrapper,
		},
		template: `
			<fb-notification-wrapper
				:placement="placement"></fb-notification-wrapper>
		`,
		data () {
			return {
				placement,
			}
		},
	})
	wrapper.$mount()
	document.body.appendChild(wrapper.$el)

	return wrapper
}

const create = function (options) {

	init()

	let parent
	switch ((options.placement || 'bottom-right').toLowerCase()) {
		case 'top-left':
			parent = wrapperTopLeft
			break
		case 'top-right':
			parent = wrapperTopRight
			break
		case 'bottom-left':
			parent = wrapperBottomLeft
			break
		case 'bottom-right':
			parent = wrapperBottomRight
	}

	let transitionName = options.transitionName ? options.transitionName : 'slide-to-down'
	let notify = new Vue({
		parent,
		components: {
			FbNotification,
		},
		template: `
			<transition name="${transitionName}">
				<fb-notification
					:type="type"
					:title="title"
					:message="message"
					:icon="icon"
					:iconColor="iconColor"
					:iconShow="iconShow"
					v-show="show"
					:show-ok-button="showOkButton"
					:okButtonText="okButtonText || '确定'"
					:callback="callback"
					@on-close="close"
				></fb-notification>
			</transition>
		`,
		data () {

			// if (options.showOkButton) {
			// 	options.duration = 0
			// }

			return {
				...options,
				show: false,
			}
		},
		methods: {
			close () {
				this.remove()
			},
			remove () {
				clearTimeout(this.timer)
				this.show = false
				setTimeout(() => {
					this.$destroy()
				}, 600)

			},
		},
		mounted () {
			this.timer = null

			this.show = true

			if (this.duration > 0) {
				this.timer = setTimeout(() => {
					this.remove()
				}, this.duration)
			}
		},
		destroyed () {
			this.onClose && this.onClose()
			// this.$el.remove()
			if (this.$el.remove) {
				this.$el.remove()
			} else {
				this.$el.parentNode.removeChild(this.$el)
			}
		},
	})

	notify.$mount()
	parent.$el.appendChild(notify.$el)

	return function () {
		notify.$destroy()
		notify = null
	}

}

let notification = function (options) {
	return create(assign({}, notification.defaults, {}, options))
}

notification.defaults = {
	title: null,
	message: null,
	type: null,
	width: null,
	icon: null,
	iconColor: null,
	iconShow: true,
	placement: null,
	showOkButton: null,
	okButtonText: null,
	callback: null,
	duration: 5000,
	onClose: function () {

	},
}

notification.setDefaults = (defaults) => {
	Object.assign(notification.defaults, defaults)
}

notification.success = function (options) {
	return create(assign({}, notification.defaults, {
		type: 'alert',
		title: '提示',
		showOkButton: true,
	}, options))
}
notification.error = function () {
}
notification.info = function () {
}
notification.warning = function () {
}
notification.warn = function () {
}
notification.open = function () {
}
notification.close = function (id) {
}
notification.destroy = function () {
}

export default notification

export { notification }
