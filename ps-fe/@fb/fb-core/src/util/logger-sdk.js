/*!
 * logger-sdk.js
 * (c) 2021 Ypp
 */

/**
 * 日志服务
 * @param opts
 *
 *
 let data = {
	// 模块编码
	moduleCode: 'xxx',
	// 模块名称
	moduleName: '测试的产品小类',
	// 操作类型： login/logout/add/delete/edit/query/pass/unpass, 可以自己定义
	operateType: 'dateTest',
	// 操作人id
	operterId: this.getCurrentUser().personId,
	// 操作人名称
	operterName: this.getCurrentUser().personName,
}

 // 日志埋点
 this.$logger.send(data);

 =================================================

 let data = {
	// 操作人名称
	operterName: this.getCurrentUser().personName,
}

 // session，默认 5 分钟调用一次
 this.$logger.session(token,data);

 // session, 手动指定
 this.$logger.session(token,data, 60000);
 */

export class WebLogger {

	constructor(data) {
		// 日志服务地址
		this.url = process.env['VUE_APP_LOGGER_URL']
		// 当前项目名
		if(data) {
			this.project = data.name
		}
	}

	/**
	 * 发送埋点数据
	 * @param data
	 */
	send(data) {

		return

		if (typeof data.moduleCode === 'undefined') {
			throw new Error('模块编码不能为空！')
		}

		if (typeof data.moduleName === 'undefined') {
			data.moduleName = ''
		}

		if (typeof data.operateType == 'undefined') {
			throw new Error('操作类型不能为空！')
		}

		if (typeof data.operterId == 'undefined') {
			throw new Error('当前登录人id不能为空！')
		}

		if (typeof data.operterName == 'undefined') {
			throw new Error('当前登录人名称不能为空！')
		}

		let token = app.$datax.get('token')

		// 组装请求地址， 小于 10k
		let query = this.url + '/logger/operate/collect?Token=' + token +
			'&project=' + encodeURIComponent(this.project)

		for (let [key, value] of Object.entries(data)) {
			query += '&' + key + '=' + encodeURIComponent(value)
		}

		let img = new Image()
		img.onerror = img.onload = function () {
		}
		img.src = query
	}

	/**
	 * 统计当前登录人数-心跳
	 * @param token
	 * @param data
	 * @param time 定时
	 */
	session(token, data, time) {
		return

		if (typeof data.operterName == 'undefined') {
			throw new Error('当前登录人名称不能为空！')
		}

		if (typeof time == 'undefined') {
			// 5 分钟请求一次
			time = 300000
		}

		// 组装请求地址， 小于 10k
		let query = this.url + '/logger/session/collect?project=' + encodeURIComponent(this.project)

		for (let [key, value] of Object.entries(data)) {
			query += '&' + key + '=' + encodeURIComponent(value)
		}
		query += '&Token=' + token

		let img = new Image()
		img.onerror = img.onload = function () {
		}

		// 立即执行一次
		img.src = query
		// 定时请求
		setInterval(function () {
			img.src = query
		}, time);

	}

	/**
	 * 获取当前登录人数
	 * @param callback 回调方法，返回 当前登录人数
	 * @param time 定时
	 */
	online(callback, time) {



		if (typeof time == 'undefined') {
			// 5 分钟请求一次
			time = 300000
		}

		let that = this

		let online = function (callback) {
			// 组装请求地址， 小于 10k
			let query = that.url + '/logger/session/online'

			app.service.request({
				url: query,
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
				 
			}).then(result => {
				// 判断code
				if (result.code == 1) {
					callback(result.data);
				}
			})
		};

		// 立即执行一次
		online(callback)
		// 定时请求
		setInterval(() => {
			online(callback)
		}, time);

	}

}

let install = WebLogger.install = (adapter) => {
	app.$logger = adapter.prototype.$logger = new WebLogger()
}

export default {
	WebLogger,
	install,
}
