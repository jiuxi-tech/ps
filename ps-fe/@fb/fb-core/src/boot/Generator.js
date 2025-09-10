/*!
 * Generator
 * (c) 2020 lincong1987
 */

import { each } from 'lodash-es'

export class Generator {

	constructor (generator) {
		this.generator = generator
		this.init()
	}

	init () {
		each(this.generator || [], (files, name) => {
			console.log(`[Generator] #init ${name}`)
			this[name] = files
		})
	}
}

export default {Generator}

