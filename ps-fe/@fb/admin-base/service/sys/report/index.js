import {defaultsDeep} from "lodash-es";

let index = {}

export default {

	upload(options) {
		return app.$svc.service.request(defaultsDeep({}, {
			url: '/fine/report/upload',
		}, options))
	},

	view(formData) {
		return app.service.get('/fine/report/view', {params: formData})
	},

	delete(formData) {
		return app.service.get('/fine/report/delete', {params: formData})
	},

	tree() {
		return app.service.get('/fine/report/tree', {params: {}})
	},
}


