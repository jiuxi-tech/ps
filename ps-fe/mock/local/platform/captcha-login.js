import Mock from "mockjs";

export default {
	install: function (app) {
		Mock.mock(app.service.defaults.baseURL + '/platform/captcha-login', {
			"code": 1,
			"message": "Mock",
			"data": {
				"tenantId": "[NULL]",
				"ascnId": "1111111111111111111",
				"personId": "1111111111111111111",
				"roleIds": "",
				"deptIds": "1111111111111111111",
				"cityCode": "3",
				"locked": "0",
				"userName": "",
				"phone": "",
				"token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjY2UiOiJlZDg3MTM3Yjg5MzMwMWFlMmJkOGM1YzNlMDYwMTFkZiIsImV4aW5mbyI6IiIsInBpZCI6IjQwYWZlYjc2NWVmMTJhNGNkMzNmNjUyYjNmYzg4MzgzZDc1MmJmYmJlNTUyMTRjOTczMmU4MjAyZGI5OWVlNTgiLCJyaWQiOiIiLCJleHAiOjE2NTU2MjE5MjAsImFpZCI6IjQwYWZlYjc2NWVmMTJhNGNkMzNmNjUyYjNmYzg4MzgzZDc1MmJmYmJlNTUyMTRjOTczMmU4MjAyZGI5OWVlNTgiLCJpYXQiOjE2NTUzNjI3MjAsInRpZCI6IjQxY2Q0ODk5OWRlOWM5ZThhZTJhNDVkNDMxM2Y5MTY1IiwiZGlkIjoiNDBhZmViNzY1ZWYxMmE0Y2QzM2Y2NTJiM2ZjODgzODNkNzUyYmZiYmU1NTIxNGM5NzMyZTgyMDJkYjk5ZWU1OCJ9.6c5Ra7urZBttjDrWQL4xAlHRo3nowqHdSB5ClkSl-y0",
				"restPwd": "",
				"category": 0,
				"deptVOList": ""
			},
			"expand": ""
		})
	}
}
