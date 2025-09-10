export const cookies = {
	/**
	 *
	 * @param sKey
	 * @returns {string | null}
	 */
	getItem (sKey) {
		return decodeURIComponent(document.cookie.replace(
			new RegExp(`(?:(?:^|.*;)\\s*${encodeURIComponent(sKey).
				replace(/[-.+*]/g, '\\$&')}\\s*\\=\\s*([^;]*).*$)|^.*$`),
			'$1')) || null
	},

	getAll () {
		let all = {}
		let keys = cookies.keys()

		const len = keys.length
		for (let i = 0; i < len; i++) {
			all[keys[i]] = cookies.getItem(keys[i])
		}

		return all
	},
	/**
	 *
	 * @param sKey
	 * @param sValue
	 * @param vEnd
	 * @param sPath
	 * @param sDomain
	 * @param bSecure
	 * @returns {boolean}
	 */
	setItem (sKey, sValue, vEnd, sPath, sDomain, bSecure) {
		if (!sKey || /^(?:expires|max-age|path|domain|secure)$/i.test(sKey)) {
			return false
		}
		let sExpires = ''
		if (vEnd) {
			switch (typeof vEnd) {
				case 'number':
					sExpires = (vEnd === Infinity)
						? '; expires=Fri, 31 Dec 9999 23:59:59 GMT'
						: `; max-age=${vEnd}`
					break
				case 'string':
					sExpires = `; expires=${vEnd}`
					break
				case 'object':
					if (vEnd instanceof Date) {
						sExpires = `; expires=${vEnd.toUTCString()}`
					}
					break
				default:
					break
			}
		}
		document.cookie = `${encodeURIComponent(sKey)}=${encodeURIComponent(
			sValue)}${sExpires}${sDomain ? `; domain=${sDomain}` : ''}${sPath
			? `; path=${sPath}`
			: ''}${bSecure ? '; secure' : ''}`
		return true
	},
	/**
	 *
	 * @param sKey
	 * @param sPath
	 * @param sDomain
	 * @returns {boolean}
	 */
	removeItem (sKey, sPath, sDomain) {
		if (!sKey || !this.hasItem(sKey)) {
			return false
		}
		document.cookie = `${encodeURIComponent(
			sKey)}=; expires=Thu, 01 Jan 1970 00:00:00 GMT${sDomain
			? `; domain=${sDomain}`
			: ''}${sPath ? `; path=${sPath}` : ''}`
		return true
	},
	hasItem (sKey) {
		return (new RegExp(`(?:^|;\\s*)${encodeURIComponent(sKey).
			replace(/[-.+*]/g, '\\$&')}\\s*\\=`)).test(document.cookie)
	}, // eslint-disable-next-line
	keys: /* optional method: you can safely remove it! */ function () {
		const aKeys = document.cookie.replace(
			/((?:^|\s*;)[^=]+)(?=;|$)|^\s*|\s*(?:=[^;]*)?(?:\1|$)/g, '').
			split(/\s*(?:=[^;]*)?;\s*/)
		for (let nIdx = 0; nIdx < aKeys.length; nIdx++) {
			aKeys[nIdx] = decodeURIComponent(aKeys[nIdx])
		}
		return aKeys
	},
}

export const storageManager = {
	set (key, value, storage) {
		try {
			window[storage].setItem(key, JSON.stringify(value))
		} catch (e) {
			console.error(e)
		}
	},
	get (key, storage) {
		try {
			if (window[storage].getItem(key)) {
				if (window[storage].getItem(key) === 'undefined') {
					return null
				}

				// 判断是否是 datax JSON.stringify生成的key
				if (key.indexOf('datax') === 0) {
					return JSON.parse(window[storage].getItem(key))
				} else {
					return window[storage].getItem(key)
				}
			}
			return window[storage].getItem(key)
		} catch (e) {
			console.error(e, key)
			return null
		}
	},
	clear (storage) {
		window[storage].clear()
	},
	remove (key, storage) {
		window[storage].removeItem(key)
	},
	getAll (storage) {

		let all = {}
		let keys = Object.keys(window[storage])
		const len = keys.length
		for (let i = 0; i < len; i++) {
			all[keys[i]] = storageManager.get(keys[i], storage)
		}

		return all

	},
}

export const cookieManager = {
	set (key, value, expired) {
		if (expired) {
			cookies.setItem(key, value, expired)
		} else {
			cookies.setItem(key, value)
		}
	},
	get (key) {
		return cookies.getItem(key)
	},
	clear () {
		cookies.keys().forEach((key) => {
			cookies.removeItem(key)
		})
	},
	remove (key) {
		cookies.removeItem(key)
	},
	getAll () {
		return cookies.getAll()
	},
}

/**
 * 操作cookie、sessionStorage、localStorage、缓存
 */

const SESSION = 'session'
const LOCAL = 'local'
const COOKIE = 'cookie'

/**
 * 统一存储管理器
 */
export class Manager {

	constructor (category = SESSION) {
		this.category = category
	}

	/**
	 * 设置
	 * @param key
	 * @param value
	 * @param category
	 * @param expired 过期时间
	 */
	set (key, value, category , expired) {

		const {storage, isWebStorage = true} = this._map(
			category || this.category)

		if (isWebStorage) {
			storageManager.set(key, value, storage)
		} else {
			cookieManager.set(key, value, expired)
		}
	}

	/**
	 *
	 * @param key
	 * @param category 默认 'session'
	 * @returns {any}
	 */
	get (key, category) {
		const {storage, isWebStorage = true} = this._map(
			category || this.category)

		if (isWebStorage) {
			return storageManager.get(key, storage)
		}
		return cookieManager.get(key)
	}

	getAll (category ) {
		const {storage, isWebStorage = true} = this._map(
			category || this.category)

		if (isWebStorage) {
			return storageManager.getAll(storage)
		}
		return cookieManager.getAll()
	}

	clear (category ) {
		const {storage, isWebStorage = true} = this._map(
			category || this.category)

		if (isWebStorage) {
			storageManager.clear(storage)
		} else {
			cookieManager.clear()
		}
	}

	remove (key, category ) {
		const {storage, isWebStorage = true} = this._map(
			category || this.category)

		if (isWebStorage) {
			storageManager.remove(key, storage)
		} else {
			cookieManager.remove(key)
		}
	}

	_map (category) {
		let isWebStorage = true
		let storage

		switch (category) {
			case SESSION:
				storage = 'sessionStorage'
				break
			case LOCAL:
				storage = 'localStorage'
				break
			case COOKIE:
				storage = 'cookie'
				isWebStorage = false
				break
			default:
				storage = 'sessionStorage'
		}

		return {
			isWebStorage,
			storage,
		}
	}
}

export default {
	Manager,
	storageManager,
	cookieManager,
	cookies,
}
