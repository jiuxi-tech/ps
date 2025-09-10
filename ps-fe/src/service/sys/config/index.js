// 系统配置API服务
export default {
	// 获取所有系统配置
	getAllConfigs() {
		return app.service.get('/sys/config/all')
	},

	// 根据配置键获取配置值
	getConfigValue(configKey) {
		return app.service.get('/sys/config/get', { params: { configKey } })
	},

	// 批量获取多个配置值
	getMultipleConfigs(configKeys) {
		const promises = configKeys.map(key => 
			this.getConfigValue(key).catch(err => ({ key, error: err }))
		)
		return Promise.all(promises)
	},

	// 获取登录页面相关配置
	getLoginConfigs() {
		const configKeys = [
			'login.title',
			'login.logo', 
			'system.copyright'
		]
		return this.getMultipleConfigs(configKeys)
	},

	// 分页查询配置列表
	list(queryData) {
		return app.service.get('/sys/config/list', { params: queryData })
	},

	// 添加配置
	add(formData) {
		return app.service.request({
			url: '/sys/config/add',
			method: 'post',
			headers: { 'Content-Type': 'application/json' },
			data: formData,
			timeout: 5000
		})
	},

	// 更新配置
	update(formData) {
		return app.service.request({
			url: '/sys/config/update',
			method: 'put',
			headers: { 'Content-Type': 'application/json' },
			data: formData,
			timeout: 5000
		})
	},

	// 获取配置详情
	get(configKey) {
		return app.service.get('/sys/config/view', { params: { configKey } })
	},

	// 删除配置
	delete(configKey) {
		return app.service.get('/sys/config/delete', { params: { configKey } })
	}
}