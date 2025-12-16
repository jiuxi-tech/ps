let index = {}

export default {
	// 获取人员标签列表
	personTags(formData) {
		return app.service.get('/sys/person-tag/person-tags', {params: formData})
	},

	// 获取标签下的人员列表
	tagPersons(formData) {
		return app.service.get('/sys/person-tag/tag-persons', {params: formData})
	},

	// 保存人员标签关系
	save(formData) {
		return app.service.request({
			url: '/sys/person-tag/save',
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

	// 删除人员标签关系
	delete(formData) {
		return app.service.get('/sys/person-tag/delete', {params: formData})
	},

	// 根据标签ID删除所有关联的人员标签关系
	deleteByTagId(formData) {
		return app.service.get('/sys/person-tag/delete-by-tag-id', {params: formData})
	}
}