export default {
	// 密码历史列表（分页查询）
	list(formData) {
		return app.service.get('/sys/password-history/list', {params: formData})
	},

	// 查看密码历史详情
	view(formData) {
		return app.service.get('/sys/password-history/view', {params: formData})
	},

	// 导出密码历史（可选）
	export(formData) {
		return app.service.request({
			url: '/sys/password-history/export',
			method: 'post',
			data: formData,
			responseType: 'blob',
			timeout: 30000,
		})
	}
}
