/*!
 * index
 * (c) 2021 lincong1987
 */

let index = {}

import {defaultsDeep} from 'lodash-es'

export default {

	upload(options) {
		return app.$svc.service.request(defaultsDeep({}, {
			url: '/platform/file/upload',
			token: encodeURIComponent(app.$datax.get("token")),
			// 毫秒
			timeout: 300000,
		}, options))
	},

	download(file) {
		if (file.downloagFlag == 'path') {
			return app.$service.service.defaults.baseURL + `/platform/file/download-by-path?relaPath=${file.savePath}&downFileName=${encodeURIComponent(file.attachName)}&token=${encodeURIComponent(app.$datax.get("token"))}`
		} else if (file.attachId) {
			return app.$service.service.defaults.baseURL + `/platform/file/download?id=${file.attachId}&token=${encodeURIComponent(app.$datax.get("token"))}`
		}
	},

	byReferId(formData) {
		formData.token = encodeURIComponent(app.$datax.get("token"));
		return app.$svc.service.get('/platform/file/refer-id', {params: formData})
	},

	previewPDF(file) {
		if (file.attachId) {
			return app.$service.service.defaults.baseURL + `/platform/file/preview-pdf?id=${file.attachId}&token=${encodeURIComponent(app.$datax.get("token"))}`
		}
	},

}
