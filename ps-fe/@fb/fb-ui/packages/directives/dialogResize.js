/*!
 * dialogResize.js
 * (c) 2022 lincong1987
 */

function setElStyle (el, prop, style, priority) {
	el.setAttribute(prop, el.style[prop])
	el.style.setProperty(prop, style, priority)
}

function resetElStyle (el, prop, style) {
	el.style[prop] = typeof style !== 'undefined'
		? style
		: el.getAttribute(prop)
	el.removeAttribute(prop)
}

export default {

	update (el, binding, VNode, oldVNode) {

		el.onmousedown = (e) => {
			e.preventDefault()
			let $el = binding.value.$refs.dialog
			let dialog = binding.value

			if (!dialog || !$el) {
				return
			}
			let sty = $el.currentStyle ||
				window.getComputedStyle($el, null)

			setElStyle($el, 'transition', 'none')
			setElStyle($el, 'opacity', '0.618')
			setElStyle($el, 'outline', '2px dashed #56D100', 'important')

			let dialogWidth = parseFloat(dialog.width)
			let dialogHeight = parseFloat(dialog.height)

			let mouseX = e.clientX
			let mouseY = e.clientY
			let dialogBody = $el.querySelector('.jpx-dialog-body')
			if (dialogBody) {
				setElStyle(dialogBody, 'transition', 'none')
				setElStyle(dialogBody, 'opacity', 0)
			}

			document.onmousemove = function (e) {
				e.preventDefault() // 移动时禁用默认事件

				let myWidth = (dialogWidth + (e.clientX - mouseX))
				let myHeight = (dialogHeight + (e.clientY - mouseY))

				dialog.myWidth = myWidth <= 160 ? 160 : myWidth
				dialog.myHeight = myHeight <= 160 ? 160 : myHeight
			}

			document.onmouseup = function (e) {

				e.preventDefault()

				document.onmousemove = null
				document.onmouseup = null

				resetElStyle($el, 'transition')
				resetElStyle($el, 'opacity')
				resetElStyle($el, 'outline')

				if (dialogBody) {
					resetElStyle(dialogBody, 'opacity')
					resetElStyle(dialogBody, 'transition',
						'opacity 0.46s ease-in-out 0s')
				}

				window.dispatchEvent(new Event('resize'))

			}

			dialogWidth = parseFloat(dialog.myWidth)
			dialogHeight = parseFloat(dialog.myHeight)

		}

	},

}
