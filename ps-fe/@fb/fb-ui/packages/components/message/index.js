/*!
* index
* (c) 2020 lincong1987
*/

import FbMessage from './src/FbMessage'
import FbMessageWrapper from './src/FbMessageWrapper'
import Vue from 'vue/dist/vue.esm.js'
import { assign } from 'lodash-es'

let wrapperTop
let wrapperCenter

/**
 * 初始化外框
 */
function init () {
	if (!wrapperTop) {
		wrapperTop = initWrapper('top')
	}
	if (!wrapperCenter) {
		wrapperCenter = initWrapper('center')
	}
}

const initWrapper = function (align) {

	let wrapper = new Vue({
		parent: message.parent,
		components: {
			FbMessageWrapper,
		},
		template: `
			<fb-message-wrapper :align="align"></fb-message-wrapper>
		`,
		data: { align: align },
	})
	wrapper.$mount()
	document.body.appendChild(wrapper.$el)

	return wrapper
}

/**
 * 创建msg
 * @param options
 * @returns {(function(...[*]=))|*}
 */

function create (options) {

	init()

	let parent = options.align === 'top' ? wrapperTop : wrapperCenter
	let msg = new Vue({
		parent,
		components: {
			FbMessage,
		},
		template: `
			<fb-message :type="type" :message="message"></fb-message>
		`,
		data () {
			return options
		},
		mounted () {
			setTimeout(() => {
				this.$destroy()
			}, this.duration)
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

	msg.$mount()
	parent.$el.appendChild(msg.$el)

	return function () {
		msg.$destroy()
		msg = null
	}

}

let message = function (text, options) {
	if (!text) {
		return
	}
	return create(assign({}, message.defaults, {
		message: text,
		type: 'info',
	}, options))
}

message.defaults = {
	duration: 3000,
	align: 'center',
	onClose: function () {

	},
}

message.success = function (text, options) {
	if (!text) {
		return
	}
	return create(assign({}, message.defaults, {
		message: text,
		type: 'success',
	}, options))
}

message.info = function (text, options) {
	if (!text) {
		return
	}
	return create(assign({}, message.defaults, {
		message: text,
		type: 'info',
	}, options))
}

message.primary = function (text, options) {
	if (!text) {
		return
	}
	return create(assign({}, message.defaults, {
		message: text,
		type: 'info',
	}, options))
}

message.warn = function (text, options) {
	if (!text) {
		return
	}
	return create(assign({}, message.defaults, {
		message: text,
		type: 'warn',
	}, options))
}

message.danger = function (text, options) {
	if (!text) {
		return
	}
	return create(assign({}, message.defaults, {
		message: text,
		type: 'danger',
	}, options))
}

message.error = function (text, options) {
	if (!text) {
		return
	}
	return create(assign({}, message.defaults, {
		message: text,
		type: 'danger',
	}, options))
}

message.loading = function (text, options) {
	if (!text) {
		return
	}
	return create(assign({}, message.defaults, {
		message: text,
		type: 'loading',
	}, options))
}


export default message

export { message }


