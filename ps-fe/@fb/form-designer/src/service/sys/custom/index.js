var index = {}

export default {
	module: {

		search (formData) {
			return app.service.get('/sys/custom/module/list',
				{params: formData})
		},

		add (formData) {
			return app.service.request({
				url: '/sys/custom/module/add',
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

		update (formData) {
			return app.service.request({
				url: '/core/tpcustomform/update',
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

		view (formData) {
			return app.service.get('/sys/custom/module/view',
				{params: formData})
		},

		delete (formData) {
			return app.service.get('/sys/custom/module/delete',
				{params: formData})
		},

	},

	form: {
		add (formData) {
			return app.service.request({
				url: '/sys/custom/form/add',
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

		update (formData) {
			return app.service.request({
				url: '/sys/custom/form/update',
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

		view (formData) {
			return app.service.get('/sys/custom/form/view', {params: formData})
		},

		// prod
		// Query：
		//参数名称	是否必须	示例	备注
		//mcode	是
		//模块编码
		//
		//ftype	是
		//类型,  list/edit/view
		//
		//fSource	是
		//pc/app
		mcode (formData) {
			// 根据模块编码查询设计信息
			return app.service.get('/sys/custom/form/mcode', {params: formData})
		},

		// 测试
//		Query：
//参数名称	是否必须	示例	备注
//mid	是
//1410126960440377344
//
//模块id
//
//ftype	是
//list
//
//表单类型;list/edit/view
//
//fSource	是
//pc
//
//表单归属;app/pc
		mid (formData) {
			// 根据模块编码查询设计信息
			return app.service.get('/sys/custom/form/view', {params: formData})
		},

	},

	'c-json': {
		list (formData) {
			return app.service.request({
				url: '/sys/c-json/list',
				//params: {fid: formData.fid},
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

		add (formData) {
			return app.service.request({
				url: '/sys/c-json/add',
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

		update (formData) {
			return app.service.request({
				url: '/sys/c-json/update',
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

		view (formData) {
			return app.service.get('/sys/c-json/view', {params: formData})
		},

		delete (formData) {
			return app.service.get('/sys/c-json/delete', {params: formData})
		},

	},

}
