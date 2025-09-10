let index = {}

export default {

	agent: {
		list(formData) {
			return app.service.get('/sys/agent/list', {params: formData})
		},

		send(formData) {
			return app.service.request({
				url: '/sys/agent/send',
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

		// 消息提醒
		remindlist(formData) {
			return app.service.get('/sys/agent/remindlist', {params: formData})
		},

		// 点击消息查看后，把消息内容和人员id保存到消息已读报
		// remind(formData) {
		// 	return app.service.get('/sys/agent/remind', {params: formData})
		// },
	},

	agentdeal:{
		list(formData) {
			return app.service.get('/sys/agentdeal/list', {params: formData})
		},

	}
}


