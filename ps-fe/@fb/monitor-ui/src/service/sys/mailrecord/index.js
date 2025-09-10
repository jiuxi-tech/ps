
var index = {}

export default {
	search(formData) {
		return app.service.get('/sys/mail-record/list', {params: formData})
	},

	view(formData) {
		return app.service.get('/sys/mail-record/view', {params: formData})
	},

	delete(formData) {
		return app.service.get('/sys/mail-record/delete', {params: formData})
	},
}
