/*!
 * report-sdk.js
 * (c) 2021 Ypp
 */


/**
 * 报表服务
 * @param opts
 *
 *
 */
export class Report {

	constructor() {
	}

	/**
	 * 获取当前登录人数
	 * @param callback 回调方法，返回 当前登录人数
	 * @param time 定时
	 */
	login() {
		app.service.get('/fine/report/login', {}).then((result) => {
			// 判断code
			if (result.code == 1) {
				console.log("报表登录成功")
			} else {
				console.log("报表登录失败")
			}
		})
	}
}

let install = Report.install = (adapter) => {
	app.$report = adapter.prototype.$report = new Report()
}

export default {
	Report,
	install,
}
