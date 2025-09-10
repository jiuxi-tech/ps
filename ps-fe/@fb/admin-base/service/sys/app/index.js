// 创建应用service
export default {
	search(formData) {
		return app.service.get('/schedule/tpscheduledtask/list', {params: formData})
	},
}