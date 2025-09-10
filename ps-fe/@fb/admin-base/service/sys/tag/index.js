let index = {}

export default {

	// 标签列表（分页查询）
	list(formData) {
		return app.service.get('/sys/tag/list', {params: formData})
	},

	// 标签列表（不分页）
	allList(formData) {
		return app.service.get('/sys/tag/all-list', {params: formData})
	},

	// 根据类别查询标签列表
	listByCategory(formData) {
		return app.service.get('/sys/tag/list-by-category', {params: formData})
	},

	// 新增标签
	add(formData) {
		return app.service.request({
			url: '/sys/tag/add',
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
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			responseType: 'json',
			timeout: 5000,
		})
	},

	// 修改标签
	update(formData) {
		return app.service.request({
			url: '/sys/tag/update',
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
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			responseType: 'json',
			timeout: 5000,
		})
	},

	// 查看标签信息
	view(formData) {
		return app.service.get('/sys/tag/view', {params: formData})
	},

	// 删除标签
	delete(formData) {
		return app.service.get('/sys/tag/delete', {params: formData})
	},

	// 人员标签管理
	person: {
		// 为人员分配标签
		assignTags(formData) {
			return app.service.request({
				url: '/sys/tag/assign-person-tags',
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
				headers: {'Content-Type': 'application/x-www-form-urlencoded'},
				responseType: 'json',
				timeout: 5000,
			})
		},

		// 查询人员标签列表
		getTags(formData) {
			return app.service.get('/sys/tag/person-tags', {params: formData})
		},

		// 移除人员指定标签
		removeTag(formData) {
			return app.service.get('/sys/tag/remove-person-tag', {params: formData})
		}
	}
}