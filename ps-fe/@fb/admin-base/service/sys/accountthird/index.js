import {defaultsDeep} from "lodash-es";

var index = {}

export default {
	search(formData) {
		return app.service.get('/sys/account-third/list', {params: formData})
	},

	add(formData) {
		return app.service.request({
			url: '/sys/account-third/add',
			method: 'post',
			transformRequest: [
				function (data) {
					let ret = ''
					for (let it in data) {
						ret += encodeURIComponent(it) + '=' +
							encodeURIComponent(data[it]) + '&'
					}
					ret = ret.substring(0, ret.lastIndexOf('&'))
					return ret
				},
			],
			data: formData,
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			responseType: 'json',
			timeout: 5000,
			loading: true,
		})
	},

	reset(formData) {
		return app.service.get('/sys/account-third/reset',{params: formData})
	},

	view(formData) {
		return app.service.get('/sys/account-third/view', {params: formData})
	},

	delete(formData) {
		return app.service.get('/sys/account-third/delete', {params: formData})
	},


}
