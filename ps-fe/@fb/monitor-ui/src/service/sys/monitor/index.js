
var index = {}

export default {
	search(formData) {
		return app.service.get('/sys/monitor/list', {params: formData})
	},

	add(formData) {
		return app.service.request({
			url: '/sys/monitor/add',
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

	update(formData) {
		return app.service.request({
			url: '/sys/monitor/update',
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
		return app.service.get('/sys/monitor/view', {params: formData})
	},

	delete(formData) {
		return app.service.get('/sys/monitor/delete', {params: formData})
	},

	viewHeartbeat(formData) {
		return app.service.get('/sys/monitor/view-heartbeat', {params: formData})
	},
}
