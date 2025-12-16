import {defaultsDeep} from "lodash-es";

var index = {}

export default {
	search(formData) {
		return app.service.get('/schedule/tpscheduledtask/list', {params: formData})
	},

	add(formData) {
		return app.service.request({
			url: '/schedule/tpscheduledtask/add',
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
			 
			loading: true,
		})
	},

	update(formData) {
		return app.service.request({
			url: '/schedule/tpscheduledtask/update',
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
			 
			loading: true,
		})
	},

	view(formData) {
		return app.service.get('/schedule/tpscheduledtask/view', {params: formData})
	},

	validateCron(formData) {
		return app.service.get('/schedule/tpscheduledtask/parse-cron', {params: formData})
	},

	delete(formData) {
		return app.service.get('/schedule/tpscheduledtask/delete', {params: formData})
	},

	execute(formData) {
		return app.service.get('/schedule/tpscheduledtask/manual-execute', {params: formData})
	},

	import(options) {
		return app.service.request(defaultsDeep({}, {
			url: '/schedule/tpscheduledtask/import',
			timeout: 60000,
		}, options))
	},

	erroroutput(formData) {
		return app.service.request({
			url: '/schedule/tpscheduledtask/excel-erroroutput',
			method: 'post', // 请求方式 post,get, 默认是 get,
			// `data` 是作为请求主体被发送的数据， post 采用
			data: formData,
			// `headers` 是即将被发送的自定义请求头
			headers: {'Content-Type': 'application/json'},
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'blob', // 返回服务器返回的数据类型为流
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			 
		}).then(response => {
			app.download(response)
		}, err => {
			console.log(err)
		})
	},

}
