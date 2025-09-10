

export function setRouterBefore(to, from, next) {
	if (!app.$datax.get('GLOBAL_CONFIG')) {
		console.info("正在加载中...")
		setTimeout(() => {
			setRouterBefore(to, from, next);
		}, 500);
	} else {
		if (!app.$datax.get('GLOBAL_CONFIG').config) {
			console.error("GLOBAL_CONFIG.config 未设置")
			return;
		}

		let token = app.$datax.get('token')
		let auth = app.$datax.get('GLOBAL_CONFIG').config.auth

		if (notTokenWhiteTo(to)) {
			next()
		} else {
			if (token && auth === "token") {
				next()
			} else if (auth === "cookie") {
				next()
			} else {
				next(app.$datax.get('GLOBAL_CONFIG').config.router.loginPath)
			}
		}
	}

	// {"code":-1,"message":"登录凭证已过期，请重新登录！","data":"","expand":""}
	function notTokenWhiteTo(to) {
		if (app.$datax.get('GLOBAL_CONFIG')) {
			let whiteList = app.$datax.get('GLOBAL_CONFIG').config.router.whiteList || []
			for (let i in whiteList) {
				let path = whiteList[i]
				if (path.indexOf('/*') !== -1) {
					let strPath = path.replace(/\/\*/, '')
					if (to.path.indexOf(strPath) !== -1) {
						return true
					}
				}
				if (to.path == path) {
					return true
				}
			}
		}

		return false
	}
}
