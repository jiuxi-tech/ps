/*!
 * mime-types
 * Copyright(c) 2014 Jonathan Ong
 * Copyright(c) 2015 Douglas Christopher Wilson
 * MIT Licensed
 */

'use strict'

/**
 * Module dependencies.
 * @private
 */

import db from './db_build.json'

//let extname = require('path').extname

const extname = (filePath) => {
	let startIndex = filePath.lastIndexOf('.')
	if (startIndex !== -1) {
		return '.' + filePath.substring(startIndex + 1, filePath.length).
			toLowerCase()
	} else {
		return ''
	}
}

/**
 * Module variables.
 * @private
 */

const EXTRACT_TYPE_REGEXP = /^\s*([^;\s]*)(?:;|\s|$)/
const TEXT_TYPE_REGEXP = /^text\//i

/**
 * export
 * @public
 */

export const charsets = {lookup: charset}
export const extensions = Object.create(null)
export const types = Object.create(null)

export default {
	charsets,
	extensions,
	types,
	contentType,
	lookup,
	db,
	extname,
}

// Populate the extensions/types maps
populateMaps(extensions, types)

/**
 * Get the default charset for a MIME type.
 *
 * @param {string} type
 * @return {boolean|string}
 */

export function charset (type) {
	if (!type || typeof type !== 'string') {
		return false
	}

	// TODO: use media-type
	let match = EXTRACT_TYPE_REGEXP.exec(type)
	let mime = match && db[match[1].toLowerCase()]

	if (mime && mime.charset) {
		return mime.charset
	}

	// default text/* to utf-8
	if (match && TEXT_TYPE_REGEXP.test(match[1])) {
		return 'UTF-8'
	}

	return false
}

/**
 * Create a full Content-Type header given a MIME type or extension.
 *
 * @param {string} str
 * @return {boolean|string}
 */

export function contentType (str) {
	// TODO: should this even be in this module?
	if (!str || typeof str !== 'string') {
		return false
	}

	let mime = str.indexOf('/') === -1
		? lookup(str)
		: str

	if (!mime) {
		return false
	}

	// TODO: use content-type or other module
	if (mime.indexOf('charset') === -1) {
		let charset = charset(mime)
		if (charset) mime += '; charset=' + charset.toLowerCase()
	}

	return mime
}

/**
 * Get the default extension for a MIME type.
 *
 * @param {string} type
 * @return {boolean|string}
 */

export function extension (type) {
	if (!type || typeof type !== 'string') {
		return false
	}

	// TODO: use media-type
	let match = EXTRACT_TYPE_REGEXP.exec(type)

	// get extensions
	let exts = match && extensions[match[1].toLowerCase()]

	if (!exts || !exts.length) {
		return false
	}

	return exts[0]
}

/**
 * Lookup the MIME type for a file path/extension.
 *
 * @param {string} path
 * @return {boolean|string}
 */

export function lookup (path) {
	if (!path || typeof path !== 'string') {
		return false
	}

	// get the extension ("ext" or ".ext" or full path)
	let extension = extname('x.' + path).toLowerCase().substr(1)
	 
	if (!extension) {
		return false
	}

	return types[extension] || false
}

/**
 * Populate the extensions and types maps.
 * @private
 */

export function populateMaps (extensions, types) {
	// source preference (least -> most)
	let preference = ['nginx', 'apache', undefined, 'iana']

	Object.keys(db).forEach(function forEachMimeType (type) {
		let mime = db[type]
		let exts = mime.extensions

		if (!exts || !exts.length) {
			return
		}

		// mime -> extensions
		extensions[type] = exts

		// extension -> mime
		for (let i = 0; i < exts.length; i++) {
			let extension = exts[i]

			if (types[extension]) {
				let from = preference.indexOf(db[types[extension]].source)
				let to = preference.indexOf(mime.source)

				if (types[extension] !== 'application/octet-stream' &&
					(from > to ||
						(from === to && types[extension].substr(0, 12) ===
							'application/'))) {
					// skip the remapping
					continue
				}
			}

			// set the extension -> mime
			types[extension] = type
		}
	})
}
