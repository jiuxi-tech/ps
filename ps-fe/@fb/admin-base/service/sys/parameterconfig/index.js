
var index = {}

export default {
	search(formData) {
		return app.service.get('/sys/parameterconfig/list', {params: formData})
	},

	add(formData) {
		return app.service.request({
			url: '/sys/parameterconfig/add',
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
			 
			loading: true,
		})
	},

	update(formData) {
		return app.service.request({
			url: '/sys/parameterconfig/update',
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
			 
			loading: true,
		})
	},

	view(formData) {
		return app.service.get('/sys/parameterconfig/view', {params: formData})
	},

	delete(formData) {
		return app.service.get('/sys/parameterconfig/delete', {params: formData})
	},
}
