
var index = {}

export default {

	update(formData) {
		return app.service.request({
			url: '/sys/monitor-config/update',
			method: 'post',
			data: formData,
			headers: {'Content-Type': 'application/json'},
			responseType: 'json',
			 
			loading: true,
		})
	},

	view(formData) {
		return app.service.get('/sys/monitor-config/view', {params: formData})
	},

}
