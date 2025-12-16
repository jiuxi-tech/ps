/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default {

	tree(formData) {
		return app.service.get('/sys/menu/tree', {params: formData})
	},

	getMenus () {
		return app.service.get('sys/menu/main-tree').
			then(res => res.data)
	},

	add(formData) {
		return app.service.request({
			url: '/sys/menu/add',
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
			 
		})
	},

	update(formData) {
		return app.service.request({
			url: '/sys/menu/update',
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
			 
		})
	},

	view(formData) {
		return app.service.get('/sys/menu/view', {params: formData})
	},

	delete(formData) {
		return app.service.get('/sys/menu/delete', {params: formData})
	},

	// 菜单历史记录相关接口
	historyList(formData) {
		return app.service.get('/sys/menu/history/list', {params: formData})
	},

	historyView(formData) {
		return app.service.get('/sys/menu/history/view', {params: formData})
	},

}
