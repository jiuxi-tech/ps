export default {

	// 应用列表（分页查询）
	list(formData) {
		return app.service.get('/sys/third-party-app/list', {params: formData})
	},

	// 新增应用
	add(formData) {
		return app.service.request({
			url: '/sys/third-party-app/add',
			method: 'post',
			transformRequest: [
				// 把json数据序列化成xxx=?&xx=?的格式
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
			 
		})
	},

	// 修改应用
	update(formData) {
		return app.service.request({
			url: '/sys/third-party-app/update',
			method: 'post',
			transformRequest: [
				// 把json数据序列化成xxx=?&xx=?的格式
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
			 
		})
	},

	// 查看应用信息
	view(formData) {
		return app.service.get('/sys/third-party-app/view', {params: formData})
	},

	// 删除应用
	delete(formData) {
		return app.service.get('/sys/third-party-app/delete', {params: formData})
	},

	// 重新生成API Secret
	regenerateSecret(formData) {
		return app.service.request({
			url: '/sys/third-party-app/regenerate-secret',
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
			 
		})
	},

	// 启用/禁用应用
	updateStatus(formData) {
		return app.service.request({
			url: '/sys/third-party-app/update-status',
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
			 
		})
	},

	// 获取应用的API权限列表
	getApiPermissions(formData) {
		return app.service.get('/sys/third-party-app/app-apis', {params: formData})
	},

	// 保存应用的API权限
	saveApiPermissions(formData) {
		return app.service.request({
			url: '/sys/third-party-app/config-permissions',
			method: 'post',
			data: formData,
			headers: {'Content-Type': 'application/json'},
			responseType: 'json',
			 
		})
	},

	// 获取可用的API清单
	getApiDefinitions(formData) {
		return app.service.get('/sys/third-party-app/available-apis', {params: formData})
	}
}
