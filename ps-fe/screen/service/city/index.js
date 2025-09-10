export default {

	deptCityTree(formData) {
		// 查询部门人员绑定关系
		return app.service.get('/whscfxtsent/stdentriskindex/org/dept-city-tree', {params: formData})
	},
	// 单选下拉
	selectDict (formData) {
		return  app.service.get('/sys/dict/select', {params: formData})
	},
}


