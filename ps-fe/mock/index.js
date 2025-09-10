const locals = require.context(`./local/`, true, /\.js/)
// const apis = require.context(`./api/`, true, /\.js/)

export function mock(app) {
	// 生产环境不开启
	if (process.env.NODE_ENV == 'production') {
		return;
	}

	//  MOCK模式启动
	if (process.env.VUE_APP_MOCK === 'true') {

		const mocks = locals.keys();

		mocks.forEach((key) => {
			let mock = locals(key).default
			mock.install(app);

			console.info("[mock] local ", key)
		})

		// apis.keys().forEach((key) => {
		// 	if (key.endsWith(".js") && !mocks.includes(key)) {
		// 		let mock = apis(key).default
		// 		mock.install(app);
		//
		// 		console.info("[mock] api ", key)
		// 	}
		// })
	}
}

export default {
	mock
}
