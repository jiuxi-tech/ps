/*!
 * datax
 * (c) 2020 lincong1987
 */

/**
 * 全局状态管理
 */
import { Manager } from '../storage'
import { each, isFunction, isArray, isObject } from 'lodash-es'

let customVue

/**
 * 数据状态管理器
 *
 * 默认的实例 存储前缀为 datax_default_[prop]
 *
 * 如果
 *
 */
class Datax {

	constructor (storage, Vue, name = 'default') {
		this.storage = storage || new Manager()
		customVue = Vue
		this.name = name

		Object.defineProperty(this, 'name', {
			value: this.name,
			enumerable: false,
		})
		Object.defineProperty(this, 'pre', {
			value: `datax_${this.name}_`,
			enumerable: false,
		})

		this._map = {}

		const all = this.storage.getAll()
		for (let key in all) {
			if (key.indexOf(this.pre) === 0) {
				customVue.set(this, key.slice(this.pre.length),
					storage.get(key))
			}
		}
	}

	/**
	 * 获取存储内容
	 * @param prop
	 * @returns {*}
	 */
	get (prop, prefix) {

		let key = (prefix || this.pre) + prop

		if (prefix && prefix != this.pre) {
			return this.storage.get(key)
		}

		if (!this[prop]) {
			this.set(prop, this.storage.get(prop))
		}

		if (this.storage.get(key) !== this[prop] &&
			Object.prototype.toString.call(this[prop]) !== '[object Object]') { // storage 和 vue 缓存不一致以 storage 为准
			return this.storage.get(key)
		}
		return this[prop]
	}

	/**
	 * 获取全部
	 * @param prefix
	 * @returns {{}}
	 */
	getAll (prefix) {
		const all = this.storage.getAll()
		let cache = {}
		for (let key in all) {
			if (key.indexOf(prefix || this.pre) === 0) {
				cache[key.slice((prefix || this.pre).length)] = all[key]
			}
		}
		return cache
	}

	/**
	 * 设置存储内容，
	 *
	 * @example
	 * $datax.set({a:"aaa", b:"bbb"})
	 *
	 * $datax.get("a")
	 * // "aaa"
	 *
	 * $datax.set("c", "lc")
	 * $data.get("c")
	 * // lc
	 *
	 *
	 * @param prop
	 * @param value
	 * @returns {Datax}
	 */
	set (prop, value, prefix) {

		if (isObject(prop)) {

			each(prop, (v, k) => {
				this.set(k, v, prefix)
			})

			return this
		}

		let key = (prefix || this.pre) + prop

		if (prefix && prefix !== this.pre) {
		} else {
			customVue.set(this, prop, value)
		}

		if (!isFunction(value)) {
			this.storage.set(key, value)
		}

		return this
	}

	remove (prop, prefix) {
		if (prefix && prefix !== this.pre) {
			this.storage.remove(prefix + prop)
		} else {
			this.set(prop, undefined, prefix)
		}

		return this

	}

	clear (prefix) {
		const all = this.storage.getAll()
		for (let key in all) {
			if (key.indexOf(prefix || this.pre) === 0) {
				this.storage.remove(key)
				if (prefix && prefix !== this.pre) {
				} else {
					customVue.set(this, key.slice(this.pre.length), undefined)
				}
			}
		}
	}

	destroy (prefix) {
		const all = this.storage.getAll()

		for (let key in all) {
			if (key.indexOf(prefix || this.pre) === 0) {
				// this.storage.remove(key)
				if (prefix && prefix !== this.pre) {
				} else {
					customVue.set(this, key.slice(this.pre.length), undefined)
				}
			}
		}

		customVue = null
	}
}

export default Datax
