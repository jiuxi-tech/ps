/*!
 * onresize
 * (c) 2020 lincong1987
 */
import {each, debounce} from 'lodash-es'

import {uuid} from '../util/utils'

class ResizeController {

	static defaults(binding) {
		let resizeConfig = {
			// 是否启用
			enable: true,
			// 设定高度后的回调
			callback: null,
			// 自定义高度表达式
			expression: null,
			minHeight: undefined,
			// other值：0
			value: undefined,
			// 开始过滤动画
			animation: true,
			// 自定义高度计算函数
			calc: null,
		}
		// 识别参数类型
		let value = binding.value

		if (value === '') {
			resizeConfig.enable = false
		}

		if (typeof value === 'number') {
			resizeConfig.value = value
		}

		if (typeof value === 'object') {
			if (typeof value.value === 'number') {
				resizeConfig.value = value.value
			}

			if (typeof value.minHeight === 'number') {
				resizeConfig.minHeight = value.minHeight
			}
			if (typeof value.enable !== 'undefined') {
				resizeConfig.enable = value.enable
			}
			if (typeof value.callback === 'function') {
				resizeConfig.callback = value.callback
			}
			if (value.expression) {
				resizeConfig.expression = value.expression
			}
		}

		return resizeConfig
	}

	constructor() {
		//console.log('[ResizeController] #constructor')
		this.tasks = {}
		this.resizeEvent = debounce((event) => {
			//console.log('[ResizeController] #resizeEvent', event)
			each(this.tasks, function (task, i) {
				task.run(task.uuid)
			})
		}, 160 * 3)
		this.addEvent()
	}

	destroy() {
		this.tasks = {}
		this.resizeEvent()
	}

	add(options = {
		el: null,
		value: null,
		enable: null,
		uuid: null,
	}) {

		let task = {
			...options,
		}

		task.run = debounce((uuid) => {
			this.doResize(uuid)
		}, 160)

		this.tasks[task.uuid] = task
		task.run(task.uuid)
	}

	addEvent() {
		window.removeEventListener('resize', this.resizeEvent, false)
		window.addEventListener('resize', this.resizeEvent, false)
	}

	removeEvent() {
		window.removeEventListener('resize', this.resizeEvent, false)
	}

	update(uuid, binding) {
		let task = this.get(uuid)
		if (task) {

			let resizeConfig = ResizeController.defaults(binding)

			this.tasks[uuid] = {
				...task,
				...resizeConfig,
			}

			task && task.run(uuid)
		}
	}

	remove(uuid) {
		delete this.tasks[uuid]
	}

	get(uuid) {
		return this.tasks[uuid]
	}

	doResize(uuid) {
		let task = this.get(uuid)

		//console.log('doResize', uuid, task)

		if (task) {
			if (task.animation === true) {
				//task.el.style.transition = 'height 0.3s'
			}


			// 判断是否是在对话框中
			if (!task.dialog) {
				let pel = task.el.parentElement
				while (pel && !pel.classList.contains("jpx-dialog-body")) {
					pel = pel.parentElement
				}

				if (pel) {
					task.dialog = pel
				}
			}

			let doc = {}

			if (task.dialog) {
				doc = {
					initHeight: task.dialog.offsetHeight,
					currentHeight: () => {
						if (task.dialog.parentElement.parentElement.classList.contains("full-screen")) {
							return task.dialog.offsetHeight - 60
						}

						// 还原时，返回null，默认使用初始高度
						return null
					}
				}
			} else {
				doc = {
					initHeight: document.documentElement.clientHeight,
					currentHeight: () => { // 使用方法，可以实时计算
						return document.documentElement.clientHeight
					},
				}
			}

			//
			// 页面可视高度
			let documentHeight = doc.currentHeight()
			if (!documentHeight) {
				documentHeight = doc.initHeight
			}

			let resizeHeight = 0

			// 根据最小高度进行自适应
			if (typeof task.minHeight !== 'undefined' &&
				typeof task.minHeight === 'number') {
				resizeHeight =
					task.minHeight - (doc.initHeight - documentHeight)
				task.el.style.height = resizeHeight + 'px'
				////console.log('[ResizeController] #doResize', resizeHeight)
			}

			if (typeof task.value !== 'undefined' && typeof task.value ===
				'number') {
				// 浏览器可视高度 - 控件距离上方控件的位置 - 减去多余的高度
				resizeHeight = documentHeight - task.el.offsetTop - task.value
				task.el.style.height = resizeHeight + 'px'
			//	//console.log('[ResizeController] #doResize', resizeHeight)
			}

			if (typeof task.calc !== 'undefined' &&
				typeof task.calc === 'function') {
				resizeHeight = task.calc({
					documentHeight,
					offsetTop: task.el.offsetTop,
				})
				if (typeof resizeHeight !== 'undefined') {
					task.el.style.height = resizeHeight + 'px'
				}
			}

			if (task.callback) {
				task.callback(resizeHeight, task)
			}

		}

	}
}

const resizeController = new ResizeController()

export default {
//	params: ['value', 'enable', 'expression', 'animation', 'minHeight', 'calc'],
//	acceptStatement: true,
	inserted(el, binding, vnode) {
		let _uuid = uuid()
		// //console.log('autoheight.js inserted', binding)
		el.setAttribute('ah-uuid', _uuid)
		//el.innerHTML = `${_uuid}`
		//vnode.text = _uuid

		let resizeConfig = ResizeController.defaults(binding)

		resizeConfig.uuid = _uuid
		resizeConfig.el = el

		if (resizeConfig.enable) {
			resizeController.add(resizeConfig)
		}

		// vnode.text = _uuid + toRaw(value)

	},
	unbind(el, binding, vnode) {
		let _uuid = el.getAttribute('ah-uuid')
	//	//console.log('autoheight.js unbind', binding)
		_uuid && resizeController.remove(_uuid)

	},
	update(el, binding, vnode) {
		let _uuid = el.getAttribute('ah-uuid')
		if (_uuid) {
			////console.log('autoheight.js update', binding)
			//el.innerHTML = `update _ ${_uuid}`
			//let value = binding.value
			//vnode.text = _uuid + JSON.stringify(value)

			//vnode.text = `update _ ${_uuid}`
			////console.log("xxxxx", el.style.height)
			resizeController.update(_uuid, binding)

		}
		////console.log('autoheight.js update', binding)
	},
	componentUpdated() {

	},

}
