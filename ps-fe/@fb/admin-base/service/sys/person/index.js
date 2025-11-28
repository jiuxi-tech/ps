let index = {}

export default {

	org: {
		list(formData) {
			return app.service.request({
				url: '/sys/person/org/list',
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
				headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
				// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
				responseType: 'json', // 默认的
				// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
				// 如果请求话费了超过 `timeout` 的时间，请求将被中断
				timeout: 5000,
			})
		},
		add(formData) {
			return app.service.post('/sys/person/org/add', formData)
		},
	},

	ent: {
		list(formData) {
			return app.service.get('/sys/person/ent/list', { params: formData })
		},

		add(formData) {
			return app.service.post('/sys/person/ent/add', formData)
		},
	},


	expAdd(formData) {
		// 添加扩展信息
		return app.service.request({
			url: '/sys/person/exp-add',
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
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},

	accountManage(formData) {
		// 账号新增, 如果开启加密，需要将账号密码加密传输
		let sysconfig = app.projectConfig.sysconfig;
		if (sysconfig.passwordEncryption && sysconfig.passwordEncryption === true) {

			const sm2 = require('sm-crypto').sm2
			// 1 - C1C3C2，0 - C1C2C3，默认为 1
			const cipherMode = 1
			// 公钥
			let publicKey = sysconfig.publicKey;
			// sm2加密
			formData.username = sm2.doEncrypt(formData.username, publicKey, cipherMode)

			if (typeof (formData.userpwd) === 'undefined' || formData.userpwd === "") {
				formData.userpwd = ""
			} else {
				formData.userpwd = sm2.doEncrypt(formData.userpwd, publicKey, cipherMode)
			}
		}

		return app.service.request({
			url: '/sys/person/account-manage',
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
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},

	update(formData) {
		return app.service.post('/sys/person/update', formData)
	},
	accountResetpwd(formData) {
		// 重置密码
		return app.service.request({
			url: '/sys/person/account-resetpwd',
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
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},
	updatePwd(formData) {
		// 账号新增, 如果开启加密，需要将账号密码加密传输
		let sysconfig = app.projectConfig.sysconfig;
		if (sysconfig.passwordEncryption && sysconfig.passwordEncryption === true) {

			const sm2 = require('sm-crypto').sm2
			// 1 - C1C3C2，0 - C1C2C3，默认为 1
			const cipherMode = 1
			// 公钥
			let publicKey = sysconfig.publicKey;
			// sm2加密
			formData.oldUserpwd = sm2.doEncrypt(formData.oldUserpwd, publicKey, cipherMode)
			formData.userpwd = sm2.doEncrypt(formData.userpwd, publicKey, cipherMode)
			formData.confirmUserPwd = sm2.doEncrypt(formData.confirmUserPwd, publicKey, cipherMode)
		}
		// 修改密码
		return app.service.request({
			url: '/sys/person/update-pwd',
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
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},

	accountLocked(formData) {
		// 冻结/解冻 账号
		return app.service.request({
			url: '/sys/person/account-locked',
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
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},
	accountEnabled(formData) {
		// 启动/禁用 账号
		return app.service.request({
			url: '/sys/person/account-enabled',
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
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},
	view(formData) {
		// 查看用户信息
		return app.service.get('/sys/person/view', { params: formData })
	},
	expView(formData) {
		// 查看用户扩展信息
		return app.service.get('/sys/person/exp-view', { params: formData })
	},
	accountView(formData) {
		// 查看账号信息
		return app.service.get('/sys/person/account-view', { params: formData })
	},
	personRoles(formData) {
		// 根据用户id查询用户已经拥有的角色
		return app.service.get('/sys/person/person-roles', { params: formData })
	},
	parttimeAdd(formData) {
		// 兼职部门
		return app.service.request({
			url: '/sys/person/parttime-add',
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
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},
	delete(formData) {
		return app.service.request({
			url: '/sys/person/delete',
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
			// `data` 是作为请求主体被发送的数据
			data: formData,
			// `headers` 是即将被发送的自定义请求头
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},

	authAdd(formData) {
		// 确保formData存在
		if (!formData) {
			formData = {};
		}
		
		// 确保roleIds不为null或undefined
		if (formData.roleIds === null || formData.roleIds === undefined) {
			formData.roleIds = '';
		}
		
		return app.service.request({
			url: '/sys/person/auth-add',
			method: 'post', // 请求方式 post,get, 默认是 get,
			// `data` 是作为请求主体被发送的数据， post 采用
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
			// `headers` 是即将被发送的自定义请求头
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		}).catch(error => {
			console.error('授权请求失败:', error);
			return Promise.reject(error);
		})
	},

	accountFindpwd(formData) {
		return app.service.request({
			url: '/sys/person/account-findpwd',
			method: 'post', // 请求方式 post,get, 默认是 get,
			// `data` 是作为请求主体被发送的数据， post 采用
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
			// `headers` 是即将被发送的自定义请求头
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},

	accountFindpwdByEmail(formData) {
		return app.service.request({
			url: '/sys/person/account-findpwd-by-email',
			method: 'post', // 请求方式 post,get, 默认是 get,
			// `data` 是作为请求主体被发送的数据， post 采用
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
			// `headers` 是即将被发送的自定义请求头
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},

	accountFindpwdByPhone(formData) {
		return app.service.request({
			url: '/sys/person/account-findpwd-by-phone',
			method: 'post', // 请求方式 post,get, 默认是 get,
			// `data` 是作为请求主体被发送的数据， post 采用
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
			// `headers` 是即将被发送的自定义请求头
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},

	checkVcode(formData) {
		// 校验验证码
		return app.service.request({
			url: '/sys/person/check-vcode',
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
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			// `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
			responseType: 'json', // 默认的
			// `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
			// 如果请求话费了超过 `timeout` 的时间，请求将被中断
			timeout: 5000,
		})
	},

	exportExcel(formData) {
		// 导出Excel
		return app.service.request({
			url: '/sys/person/export-excel',
			method: 'post',
			data: formData,
			headers: { 'Content-Type': 'application/json' },
			responseType: 'blob', // 重要：设置为blob以处理文件下载
			timeout: 300000, // 导出超时时间：5分钟
		}).then(e => {
			return e

		}).catch(e => {

			return e
		})
	},

	importExcel(formData) {
		// 导入Excel
		return app.service.request({
			url: '/sys/person/import-excel',
			method: 'post',
			data: formData,
			headers: { 'Content-Type': 'multipart/form-data' },
			timeout: 60000, // 导入可能需要更长时间
		}).then(e => {
			return e
		}).catch(e => {
			return e
		})
	},

	downloadTemplate() {
		// 下载Excel导入模板
		return app.service.request({
			url: '/sys/person/download-template',
			method: 'get',
			responseType: 'blob', // 重要：设置为blob以处理文件下载
			timeout: 30000,
		}).then(e => {
			return e
		}).catch(e => {
			return e
		})
	},

	// 同步账号到Keycloak
	syncAccountToKeycloak(formData) {
		return app.service.request({
			url: '/sys/person/sync-account-to-keycloak',
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
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			responseType: 'json',
			timeout: 10000,
		})
	},
}


