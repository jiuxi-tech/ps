import Mock from "mockjs";

export default {
	install: function (app) {
		Mock.mock(app.service.defaults.baseURL + '/sys/person/getUserInfo', {
			"code": 1,
			"message": "Mock",
			"data": {
				"personId": "1111111111111111111",
				"personName": "admin",
				"userName": "admin",
				"profilePhoto": "",
				"personNo": "[NULL]",
				"sexName": "男",
				"idcard": "[NULL]",
				"phone": "[NULL]",
				"email": "[NULL]",
				"office": "[NULL]",
				"ascnId": "DEPT000000000900000",
				"ascnName": "余杭区安全生产委员会",
				"category": 0,
				"deptId": "1111111111111111111",
				"deptFullName": "顶级部门节点",
				"roleIds": "",
				"cityCode": "3",
				"cityName": "中国",
				"updatePwdFlag": ""
			},
			"expand": ""
		})
	}
}
