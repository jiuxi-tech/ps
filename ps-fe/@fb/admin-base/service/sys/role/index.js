let index = {}

export default {

	org: {
		list(formData) {
			return app.service.get('/sys/role/org/list', {params: formData})
		},
		add(formData) {
			return app.service.request({
				url: '/sys/role/org/add',
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
		list(formData) {
			return app.service.get('/sys/role/ent/list', {params: formData})
		},
		add(formData) {
			return app.service.request({
				url: '/sys/role/ent/add',
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
	auth: {
		authList(formData) {
			// 政府待选角色列表
			return app.service.get('/sys/role/auth-list', {params: formData})
		},

		roleAuthList(formData) {
			// 政府待选角色列表
			return app.service.get('/sys/role/role-auth-list', {params: formData})
		},
		search(formData) {
			return app.service.request({
				url: '/sys/role/auth-tree',
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
		insert(formData) {
			return app.service.request({
				url: '/sys/role/auth-add',
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

	update(formData) {
		// 	return app.service.post('/sys/role/update', formData)
		return app.service.request({
			url: '/sys/role/update',
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
		return app.service.get('/sys/role/view', {params: formData})
	},
	selectByRoleid(formData) {
		return app.service.get('/sys/role/select-by-roleid', {params: formData})
	},
	delete(formData) {
		return app.service.get('/sys/role/delete', {params: formData})
	}
}


