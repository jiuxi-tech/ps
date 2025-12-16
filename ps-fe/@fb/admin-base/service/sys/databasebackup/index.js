let index = {}

export default {

	// 标签列表（分页查询）
	list(formData) {
		return app.service.get('/sys/database-backup/list', {params: formData})
	},

	// 标签列表（不分页）
	allList(formData) {
		return app.service.get('/sys/database-backup/all-list', {params: formData})
	},

	// 根据类别查询标签列表
	listByCategory(formData) {
		return app.service.get('/sys/database-backup/list-by-category', {params: formData})
	},

	// 新增标签
	add(formData) {
		return app.service.request({
			url: '/sys/database-backup/add',
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
			 
		})
	},

	// 修改标签
	update(formData) {
		return app.service.request({
			url: '/sys/database-backup/update',
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
			 
		})
	},

	// 查看数据库备份信息
	view(formData) {
		return app.service.get('/sys/database-backup/view', {params: formData})
	},

	// 删除数据库备份
	delete(formData) {
		return app.service.get('/sys/database-backup/delete', {params: formData})
	},

 
}