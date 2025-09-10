/*!
 * index
 * (c) 2020 lincong1987
 */

import {cloneDeep} from "lodash-es"

var index = {}

export default {


	// 用户名密码登陆
	login(userName, userpwd) {
		// 加密用户名密码
		let sysconfig = app.projectConfig.sysconfig;
		if (sysconfig.passwordEncryption && sysconfig.passwordEncryption === true) {

			const sm2 = require('sm-crypto').sm2
			// 1 - C1C3C2，0 - C1C2C3，默认为 1
			const cipherMode = 1
			// 公钥
			let publicKey = sysconfig.publicKey;
			// sm2加密
			userName = sm2.doEncrypt(userName, publicKey, cipherMode)
			userpwd = sm2.doEncrypt(userpwd, publicKey, cipherMode)
		}
		return app.service.post('/platform/login', app.$.param({
			userName,
			userpwd,
		}))
	},

	// 密码被重置后，第一次登陆强制修改密码
	update(formData, token){
		return app.service.request('/sys/account/update', {
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
			token: token,
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		});
	},

	// 图片验证码登陆
	captChaLogin(formData) {
		// 深拷贝一个新的对象
		let newFormData = cloneDeep(formData)
		let sysconfig = app.projectConfig.sysconfig;
		if (sysconfig.passwordEncryption && sysconfig.passwordEncryption === true) {
			const sm2 = require('sm-crypto').sm2
			// 1 - C1C3C2，0 - C1C2C3，默认为 1
			const cipherMode = 1
			// 公钥
			let publicKey = sysconfig.publicKey;
			// sm2加密
			newFormData.userName = sm2.doEncrypt(newFormData.userName, publicKey, cipherMode)
			newFormData.userpwd = sm2.doEncrypt(newFormData.userpwd, publicKey, cipherMode)
			newFormData.verification = ""
		}

		return app.service.request({
			url: '/platform/captcha-login',
			method: 'post', // 请求方式 post,get, 默认是 get,
			// `params` 是即将与请求一起发送的 URL 参数, get 采用
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
			data: newFormData,

			// `headers` 是即将被发送的自定义请求头
			headers: {'Content-Type': 'application/x-www-form-urlencoded', "verification": formData.verification},
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob',
			// 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},

	checkToken() {
		return app.service.post('/platform/check-token')
	},

	selectDeptLogin(formData) {
		return app.service.request({
			url: '/platform/select-dept',
			method: 'post', // 请求方式 post,get, 默认是 get,
			// `params` 是即将与请求一起发送的 URL 参数, get 采用
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
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob',
			// 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},

	getUserInfo() {
		return app.service.get("sys/person/getUserInfo")
	},

	// 报表单点登录
	reportLogin(formData){
		return app.service.request('/fine/report/login', {
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
		});
	},

	dwonloadExcel(file) {
		return app.$service.service.defaults.baseURL + `/platform/excel/download?path=${file.path}&fileName=${file.fileName}`
	},

}
