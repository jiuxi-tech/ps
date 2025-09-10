/*!
 * Timer
 * (c) 2021 lincong1987
 */

//import { debounce } from 'lodash-es'

export default {

	data () {
		return {}
	},

	methods: {
		setTimeout (fn, delay = 2000) {
			let idx = this._setTimeoutQueueIndex++
			let timer = window.setTimeout(fn, delay)
			this._setTimeoutQueue[idx] = timer
			return idx
		},
		clearTimeout (idx) {
			if (this._setTimeoutQueue[idx]) {
				clearTimeout(this._setTimeoutQueue[idx])
				delete this._setTimeoutQueue[idx]
			}
		},
		/**
		 *
		 * @param fn
		 * @param delay
		 * @returns {number}
		 */
		setInterval (fn, delay = 2000) {
			let idx = this._setIntervalQueueIndex++
			let timer = window.setInterval(fn, delay)
			this._setIntervalQueue[idx] = timer
			return idx
		},
		clearInterval (idx) {
			if (this._setIntervalQueue[idx]) {
				clearInterval(this._setIntervalQueue[idx])
				delete this._setIntervalQueue[idx]
			}
		},
	},

	created () {
		this._setTimeoutQueueIndex = 1
		this._setTimeoutQueue = {}

		this._setIntervalQueueIndex = 1
		this._setIntervalQueue = {}
	},

	beforeDestroy () {

		for (let idx in this._setTimeoutQueue) {
			clearTimeout(this._setTimeoutQueue[idx])
		}

		this._setTimeoutQueue = {}

		for (let idx in this._setIntervalQueue) {
			clearInterval(this._setIntervalQueue[idx])
		}

		this._setIntervalQueue = {}
	},

}
