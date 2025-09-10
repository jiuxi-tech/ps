
var index = {}

export default {

	update(formData) {
		return app.service.request({
			url: '/sys/monitor-config/update',
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

	view(formData) {
		return app.service.get('/sys/monitor-config/view', {params: formData})
	},

}
