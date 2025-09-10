// 时间段权限规则service
export default {
	// 查询列表
	search(formData) {
		return app.service.get('/sys/timerule/list', {params: formData})
	},
	
	// 新增
	add(formData) {
		return app.service.request({
			url: '/sys/timerule/add',
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
		})
	},
	
	// 修改
	update(formData) {
		return app.service.request({
			url: '/sys/timerule/update',
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
		})
	},
	
	// 删除
	delete(formData) {
		return app.service.request({
			url: '/sys/timerule/delete',
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
		})
	},
	
	// 查看详情
	view(formData) {
		return app.service.get('/sys/timerule/view', {params: formData})
	},
	
	// 启用/禁用（使用update接口）
	toggleStatus(formData) {
		return app.service.request({
			url: '/sys/timerule/update',
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
		})
	}
}