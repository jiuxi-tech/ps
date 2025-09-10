/*!
 * index
 * (c) 2020 lincong1987
 */

import FbMessageBox from './src/FbMessageBox'
import FbMessageBoxWrapper from './src/FbMessageBoxWrapper'
import Vue from 'vue/dist/vue.esm.js'
import { assign } from 'lodash-es'

let wrapper

/**
 * 初始化外框
 */
function init (options) {
	if (wrapper) {
		wrapper.hide()
	}

	wrapper = initWrapper(options)
}

const initWrapper = function (options) {
	wrapper = new Vue({
		parent: msgbox.parent,
		components: {
			FbMessageBoxWrapper,
			FbMessageBox,
		},
		template: `
			<fb-message-box-wrapper v-if="show">
				<fb-message-box
					:type="type"
					:message="message"
					:title="title"
					:titleStyle="titleStyle"
					:callback="callback"
					:fallback="fallback"
					:show-cancel-button="showCancelButton"
					:show-confirm-button="showConfirmButton"
					:show-ok-button="showOkButton"
					:show-close="showClose"
					:confirm-button-text="confirmButtonText"
					:cancel-button-text="cancelButtonText"
					:ok-button-text="okButtonText"
					:width="width"
					:icon="icon"
				/>
			</fb-message-box-wrapper>
		`,
		data () {
			return {
				...options,
				show: true,
			}
		},

		methods: {
			hide () {
				this.show = false
				wrapper.$destroy()
			},
		},
		destroyed () {
			if (this.$el.remove) {
				this.$el.remove()
			} else {
				this.$el.parentNode.removeChild(this.$el)
			}
			wrapper = null
		},
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

	init(options)

	// console.log(options)
	//
	// let msg = new Vue({
	// 	parent: wrapper,
	// 	components: {
	// 		FbMessageBox,
	// 	},
	// 	template: `
	// 		<fb-message-box
	// 			:type="type"
	// 			:message="message"
	// 			:title="title"
	// 			:callback="callback"
	// 			:fallback="fallback"
	// 			:show-cancel-button="showCancelButton"
	// 			:show-confirm-button="showConfirmButton"
	// 			:show-ok-button="showOkButton"
	//
	// 		></fb-message-box>
	// 	`,
	// 	data () {
	// 		return options
	// 	},
	// 	mounted () {
	//
	// 	},
	// 	destroyed () {
	// 		this.onClose && this.onClose()
	// 		this.$el.remove()
	// 	},
	// })
	//
	// msg.$mount()
	// wrapper.$el.appendChild(msg.$el)

}

let msgbox = function (options) {
	return create(assign({}, msgbox.defaults, {
		title: '提示',
		message: '提示',
		type: 'alert',
		showOkButton: true,
	}, options))
}

msgbox.defaults = {
	title: '',
	titleStyle: {},
	message: '',
	type: 'alert',

	width: 420,

	icon: 'info',

	showOkButton: true,
	okButtonText: '确定',

	showConfirmButton: false,
	confirmButtonText: '确认',

	showCancelButton: true,
	cancelButtonText: '取消',

	showClose: false,
	callback: null,
	fallback: null,
	onClose: function () {

	},
}

msgbox.alert = function (text, callback, fallback, options) {
	if (!text) {
		return
	}
	return create(assign({}, msgbox.defaults, {
		message: text,
		type: 'alert',
		title: '确认信息',
		showCancelButton: false,
		callback: callback,
	}, options))
}

msgbox.confirm = function (text, callback, fallback, options) {
	if (!text) {
		return
	}
	return create(assign({}, msgbox.defaults, {
		title: '警告信息',
		message: text,
		type: 'confirm',
		showOkButton: false,
		showConfirmButton: true,
		showCancelButton: true,
		callback: callback,
		fallback: fallback,
	}, options))
}

msgbox.close = () => {
	wrapper.hide()
}

msgbox.show = msgbox.alert

export default msgbox

export { msgbox }
