import normalizeWheel from 'normalize-wheel'

const isFirefox = typeof navigator !== 'undefined' &&
	navigator.userAgent.toLowerCase().indexOf('firefox') > -1

const addMousewheel = function (element, callback) {
	if (element && element.addEventListener) {
		element.addEventListener(isFirefox ? 'DOMMouseScroll' : 'mousewheel',
			function (event) {
				const normalized = normalizeWheel(event)
				callback && callback.apply(this, [event, normalized])
			})
	}
}

const removeMousewheel = function (element, callback) {
	if (element && element.removeEventListener) {
		element.removeEventListener(isFirefox ? 'DOMMouseScroll' : 'mousewheel',
			function (event) {
				const normalized = normalizeWheel(event)
				callback && callback.apply(this, [event, normalized])
			})
	}
}

export default {
	bind (el, binding) {
		addMousewheel(el, binding.value)
	},
	unbind (el) {
		removeMousewheel(el, )
	}
}
