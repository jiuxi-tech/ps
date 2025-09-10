/*!
 * ripple
 * (c) 2022 lincong1987
 */

import {
	addClass,
	removeClass,
	getHeight,
	getWidth,
	getOuterHeight,
	getOuterWidth,
	getOffset
} from '../utils/dom'

function bindEvents (el) {
	el.addEventListener('mouseup', onMouseUp)
}

function unbindEvents (el) {
	el.removeEventListener('mouseup', onMouseUp)
}

function create (el) {
	let ripple = document.createElement('span')
	ripple.className = 'jpx-ripple'
	el.appendChild(ripple)

	ripple.addEventListener('animationend', onAnimationEnd)
}

function remove (el) {
	let ripple = getRipple(el)
	if (ripple) {
		unbindEvents(el)
		ripple.removeEventListener('animationend', onAnimationEnd)
		ripple.remove()
	}
}

function onMouseUp (event) {
	let target = event.currentTarget
	let ripple = getRipple(target)
	if (!ripple || getComputedStyle(ripple, null).display === 'none') {
		return
	}

	removeClass(ripple, 'jpx-ripple--active')
	if (!getHeight(ripple) && !getWidth(ripple)) {
		let d = Math.max(getOuterWidth(target),
			getOuterHeight(target))
		ripple.style.height = d + 'px'
		ripple.style.width = d + 'px'
	}

	let offset = getOffset(target)
	let x = event.pageX - offset.left + document.body.scrollTop -
		getWidth(ripple) / 2
	let y = event.pageY - offset.top + document.body.scrollLeft -
		getHeight(ripple) / 2

	ripple.style.top = y + 'px'
	ripple.style.left = x + 'px'
	addClass(ripple, 'jpx-ripple--active')
}

function onAnimationEnd (event) {
	removeClass(event.currentTarget, 'jpx-ripple--active')
}

function getRipple (el) {
	for (let i = 0; i < el.children.length; i++) {
		if (typeof el.children[i].className === 'string' &&
			el.children[i].className.indexOf('jpx-ripple') !== -1) {
			return el.children[i]
		}
	}
	return null
}


export default {

	inserted (el, binding, VNode, oldVNode) {
		create(el)
		bindEvents(el)
	},
	update (el, binding, VNode, oldVNode) {
	},
	unbind (el, binding, VNode, oldVNode) {
		remove(el)
	},

}
