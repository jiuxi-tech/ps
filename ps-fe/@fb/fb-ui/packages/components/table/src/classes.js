/*!
 * classes
 * (c) 2021 lincong1987
 */

import { indexOf } from './utils'
import { isRegExp } from 'lodash-es'

let toString = String.prototype.toString

let re = /\s+/;

export default function (el) {
	return new ClassList(el)
};

/**
 * Initialize a new ClassList for `el`.
 *
 * @param {Element} el
 * @api private
 */

export class ClassList {

	constructor (el) {
		if (!el || !el.nodeType) {
			throw new Error('需要一个dom元素作为实参')
		}
		this.el = el
		this.list = el.classList
	}

	add (name) {
		// classList
		if (this.list) {
			this.list.add(name)
			return this
		}

		// fallback
		let arr = this.array()
		let i = indexOf(arr, name)
		if (!~i) arr.push(name)
		this.el.className = arr.join(' ')
		return this
	}

	remove (name) {

		if (isRegExp(name)) {
			return this.removeMatching(name)
		}

		// classList
		if (this.list) {
			this.list.remove(name)
			return this
		}

		// fallback
		let arr = this.array()
		let i = indexOf(arr, name)
		if (~i) arr.splice(i, 1)
		this.el.className = arr.join(' ')
		return this
	}

	removeMatching (re) {
		let arr = this.array()
		for (let i = 0; i < arr.length; i++) {
			if (re.test(arr[i])) {
				this.remove(arr[i])
			}
		}
		return this
	}

	toggle (name, force) {
		// classList
		if (this.list) {
			if ('undefined' !== typeof force) {
				if (force !== this.list.toggle(name, force)) {
					this.list.toggle(name) // toggle again to correct
				}
			} else {
				this.list.toggle(name)
			}
			return this
		}

		// fallback
		if ('undefined' !== typeof force) {
			if (!force) {
				this.remove(name)
			} else {
				this.add(name)
			}
		} else {
			if (this.has(name)) {
				this.remove(name)
			} else {
				this.add(name)
			}
		}

		return this
	}

	array () {
		let className = this.el.getAttribute('class') || ''
		let str = className.replace(/^\s+|\s+$/g, '')
		let arr = str.split(re)
		if ('' === arr[0]) arr.shift()
		return arr
	}

	has (name) {
		return this.list
			? this.list.contains(name)
			: !!~indexOf(this.array(), name)
	}

	contains (name) {
		return this.has(name)
	}

}
