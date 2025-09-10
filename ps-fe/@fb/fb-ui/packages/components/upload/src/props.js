/*!
 * status
 * (c) 2021 lincong1987
 */

import { includes } from 'lodash-es'

export const FILE_STATUS = [
	'error',
	'success',
	'done',
	'uploading',
	'removed',
]

export function File ({id, name}) {
	if (!id && id !== 0) return false
	if (!['string', 'number'].includes(typeof id)) return false
	if (name === '' || typeof name !== 'string') return false
	return true
}

export const UploadProps = {
	// 'drag', 'select'
	mode: {
		type: String,
		default: 'select',
		validate (value) {
			return includes(['drag', 'select'], value)
		},
	},

	name: {
		type: String,
	},

	fileList: {
		type: Array,
		default () {
			return []
		},
	},

	service: null,

	directory: {
		type: Boolean,
		default: false,
	},

	data: {},

	multiple: {
		type: Boolean,
		default: false,
	},

	accept: {
		type: String,
	},

	beforeUpload: {
		type: Function,
	},

	// 'list', 'image', "avatar", "grid"
	view: {
		type: String,
		default: 'list',
	},

	remove: {
		type: Function,
	},

	disabled: {
		type: Boolean,
		default: false,
	},

	withCredentials: {
		type: Boolean,
		default: false,
	},

	fastSelect: {
		type: Boolean,
		default: true,
	},

	height: {
		type: [String, Number],
		default: '100px',
	},

	previewFile: {
		type: Function,
	},
	transformFile: {
		type: Function,
	},
	onChange: {
		type: Function,
	},
	onPreview: {
		type: Function,
	},
	onRemove: {
		type: Function,
	},
	onDownload: {
		type: Function,
	},

}

export const UploadAdapterProps = {

	name: {
		type: String,
		default: 'file',
	},

	multiple: {
		type: Boolean,
		default: false,
	},
	directory: {
		type: Boolean,
		default: false,
	},
	disabled: {
		type: Boolean,
		default: false,
	},
	accept: {type: String},
	data: null,
	service: null,

	beforeUpload: {
		type: Function,
	},
	customRequest: {
		type: Function,
	},

	withCredentials: {
		type: Boolean,
		default: false,
	},
	fastSelect: {
		type: Boolean,
		default: true,
	},
	transformFile: {
		type: Function,
	},
}

export const UploadListProps = {
	view: {
		type: String,
	},

	fileList: {
		type: Array,
		default () {
			return []
		},
	},

	progressAttr: {
		type: Object,
	},

	previewFile: {
		type: Function,
	},

}
export default {
	File,
	FILE_STATUS,
	UploadProps,
	UploadAdapterProps,
	UploadListProps,

}
