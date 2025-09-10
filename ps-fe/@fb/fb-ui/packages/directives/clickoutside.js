function contains (a, b) {
	const adown = a.nodeType === 9 ? a.documentElement : a
	const bup = b && b.parentNode
	return a === bup || !!(bup && bup.nodeType === 1 && adown.contains(bup))
}

const cache = {}
let key = 1

export default {
	inserted (el, binding) {

		el.outsideKey = key++
		const self = {}
		self.documentHandler = (e) => {
			if (contains(el, e.target)) {
				return false
			}
			if (binding && binding.value) {
				binding.value()
			}
		}
		cache[el.outsideKey] = self
		document.addEventListener('click', self.documentHandler)
	},
	unbind (el) {
		const self = cache[el.outsideKey]
		if (self) {
			document.removeEventListener('click', self.documentHandler)
			delete cache[el.outsideKey]
		}
	},
}
