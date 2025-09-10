/*!
 * dialogDnd
 * (c) 2022 lincong1987
 */

const getRealPxValue = (style) => {
	if (style === '') {
		return 0
	}
	return style.includes('%')
		? (+document.body.clientWidth *
			(+style.replace(/%/g, '') / 100))
		: (+style.replace(/px/g, ''))
}

export default {

	bind (el, binding, VNode, oldVNode) {

		let enable = false

		if (typeof binding.value === 'object') {
			if (typeof binding.value.enable !== 'undefined'
				&& binding.value.enable === true) {
				enable = true
			}
		}

		if (typeof binding.value === 'boolean') {
			enable = binding.value
		}

		if (!enable) {
			return
		}

		let dialogHeaderEl = el.querySelector('.jpx-dialog-header')
		let dragDom = el.querySelector('.jpx-dialog')
		if (!dragDom) {
			dragDom = el
		}
		dialogHeaderEl.style.cursor = 'move'
		dialogHeaderEl.style.userSelect = 'none'

		let sty = dragDom.currentStyle ||
			window.getComputedStyle(dragDom, null)

		let minLeft = 0
		let minTop = 0

		dialogHeaderEl.onmousedown = (e) => {
			let transition = sty.transition || ''
			if (transition) {
				dragDom.setAttribute('drag-transition', transition)
				dragDom.style.transition = 'none'
			}

			let wrapperStyle = window.getComputedStyle(dragDom.parentElement,
				null)

			const lastX = e.clientX// - dragDom.offsetLeft
			const lastY = e.clientY// - dragDom.offsetTop

			// 获取到的值带px 正则匹配替换
			let currentLeft = getRealPxValue(sty.left)
			let currentTop = getRealPxValue(sty.top)

			let dialogWidth = getRealPxValue(sty.width)
			let dialogHeight = getRealPxValue(sty.height)

			let maxLeft = getRealPxValue(wrapperStyle.width) - dialogWidth
			let maxTop = getRealPxValue(wrapperStyle.height) - dialogHeight

			document.onmousemove = function (e) {

				let movedX = e.clientX - lastX
				let movedY = e.clientY - lastY

				let targetLeft = movedX + currentLeft
				let targetTop = movedY + currentTop

				targetLeft = Math.max(minLeft, Math.min(maxLeft, targetLeft))
				targetTop = Math.max(minTop, Math.min(maxTop, targetTop))

//				console.log(
//					`left>> 移动的距离 (${currentLeft},${currentTop}) (${minLeft} < ${targetLeft} < ${maxLeft})`)

				// 移动当前元素
				dragDom.style.left = `${targetLeft}px`
				dragDom.style.top = `${targetTop}px`

				// 将此时的位置传出去
				// binding.value({x:e.pageX,y:e.pageY})

//				console.log('onmousedown transition', dragDom.style.transition)
			}

			document.onmouseup = function (e) {
				e.preventDefault()
				document.onmousemove = null
				document.onmouseup = null

				let transition = dragDom.getAttribute('drag-transition')
				if (transition) {
					dragDom.style.transition = transition
				}

				dragDom.setAttribute('drag-transition', '')

				//console.log('onmousedown transition', dragDom.style.transition)
			}
		}
	},

}
