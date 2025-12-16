// IP访问控制API服务
export default {
	// 获取IP访问控制配置
	getConfig() {
		return app.service.get('/sys/ipaccess/get')
	},

	// 更新IP访问控制配置
	updateConfig(configData) {
		return app.service.request({
			url: '/sys/ipaccess/update',
			method: 'post',
			headers: { 'Content-Type': 'application/json' },
			data: configData,
			 
		})
	},

	// 获取访问日志列表
	getAccessLogs(queryData) {
		return app.service.get('/sys/ipaccess/logs', { params: queryData })
	},

	// 获取访问统计
	getAccessStatistics(queryData) {
		return app.service.get('/sys/ipaccess/statistics', { params: queryData })
	},

	// 测试IP规则
	testIpRule(testData) {
		return app.service.request({
			url: '/sys/ipaccess/test',
			method: 'post',
			headers: { 'Content-Type': 'application/json' },
			data: testData,
			 
		})
	},

	// 清理过期日志
	cleanExpiredLogs(days) {
		return app.service.request({
			url: '/sys/ipaccess/logs/clean',
			method: 'delete',
			params: { days },
			 
		})
	}
}