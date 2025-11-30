let index = {}

export default {

	// API调用日志列表（分页查询）
	list(formData) {
		return app.service.get('/sys/api-call-log/list', {params: formData})
	},

	// 查看API调用日志详情
	view(formData) {
		return app.service.get('/sys/api-call-log/view', {params: formData})
	}
}
