/*!
 * mouseoutside
 * (c) 2020 lincong1987
 */

function contains (a, b) {
	const adown = a.nodeType === 9 ? a.documentElement : a
	const bup = b && b.parentNode
	return a === bup || !!(bup && bup.nodeType === 1 && adown.contains(bup))
}

const cache = {};
let key = 1;
export default {
	inserted(el, binding) {
		el.outsideKey = key++;
		const self = {};
		self.documentHandler = (e) => {
			if (contains(el, e.target)) {
				return false;
			}
			if (binding.value) {
				binding.value();
			}
		};
		cache[el.outsideKey] = self;
		document.addEventListener('mousedown', self.documentHandler);
	},
	unbind(el) {
		const self = cache[el.outsideKey];
		document.removeEventListener('mousedown', self.documentHandler);
	}
};
