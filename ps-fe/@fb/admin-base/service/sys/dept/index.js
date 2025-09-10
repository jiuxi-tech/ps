let index = {}

export default {

	org: {
		tree(deptData) {
			return app.service.get('/sys/dept/org/tree', {params: deptData})
		},
		allTree(deptData) {
			return app.service.get('/sys/dept/org/all-tree', {params: deptData})
		},
		parttimejobTree(deptData) {
			return app.service.get('/sys/dept/org/parttimejob-tree', {params: deptData})
		},
		// 组织机构历史查看相关API
		history: {
			list(queryData) {
				return app.service.post('/api/org-tree-change-history/list', null, {params: queryData})
			},
			export(queryData) {
				return app.service.request({
					url: '/api/org-tree-change-history/export',
					method: 'post',
					data: queryData,
					headers: {'Content-Type': 'application/json'},
					responseType: 'blob'
				})
			},
			latest() {
				return app.service.get('/api/org-tree-change-history/latest')
			},
			historyView(queryData) {
				return app.service.post('/api/org-tree-change-history/view', null, {params: queryData})
			}
		},
		// 历史详情查看API - 直接在org对象下提供，供view.vue调用
		historyView(queryData) {
			return app.service.post('/api/org-tree-change-history/view', null, {params: queryData})
		},
		add(formData) {
			return app.service.request({
				url: '/sys/dept/org/add',
				method: 'post', // 请求方式 post,get, 默认是 get,
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
				// `data` 是作为请求主体被发送的数据， post 采用
				data: formData,

				// `headers` 是即将被发送的自定义请求头
				headers: {'Content-Type': 'application/x-www-form-urlencoded'},
				// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
				responseType: 'json', // 默认的
				// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
				// 如果请求话费了超过 `timeout` 的时间，请求将被中断
				timeout: 5000,
			})
		},
	},

	ent: {
		tree(formData) {
			return app.service.get('/sys/dept/ent/tree', {params: formData})
		},
		allTree(deptData) {
			return app.service.get('/sys/dept/ent/all-tree', {params: deptData})
		},
		parttimejobTree(deptData) {
			return app.service.get('/sys/dept/ent/parttimejob-tree', {params: deptData})
		},
		add(formData) {
			return app.service.request({
				url: '/sys/dept/ent/add',
				method: 'post', // 请求方式 post,get, 默认是 get,
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
				// `data` 是作为请求主体被发送的数据， post 采用
				data: formData,
				// `headers` 是即将被发送的自定义请求头
				headers: {'Content-Type': 'application/x-www-form-urlencoded'},
				// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
				responseType: 'json', // 默认的
				// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
				// 如果请求话费了超过 `timeout` 的时间，请求将被中断
				timeout: 5000,
			})
		},
	},

	expAdd(formData) {
		// 新增扩展信息
		return app.service.request({
			url: '/sys/dept/exp-add',
			method: 'post', // 请求方式 post,get, 默认是 get,
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
			// `data` 是作为请求主体被发送的数据， post 采用
			data: formData,
			// `headers` 是即将被发送的自定义请求头
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},
	update(formData) {
		return app.service.request({
			url: '/sys/dept/update',
			method: 'post', // 请求方式 post,get, 默认是 get,
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
			// `data` 是作为请求主体被发送的数据， post 采用
			data: formData,
			// `headers` 是即将被发送的自定义请求头
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},
	view(formData) {
		return app.service.get('/sys/dept/view', {params: formData})
	},
	expView(formData) {
		return app.service.get('/sys/dept/exp-view', {params: formData})
	},
	selectBindByDeptId(formData) {
		// 查询部门人员绑定关系
		return app.service.get('/sys/dept/select/deptid', {params: formData})
	},

	deptCityTree(formData) {
		// 查询部门人员绑定关系
		return app.service.get('/sys/dept/org/dept-city-tree', {params: formData})
	},

	delete(formData) {
		return app.service.get('/sys/dept/delete', {params: formData})
	},
}


