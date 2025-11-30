import loading from 'fb-ui/packages/components/spin'
import message from "fb-ui/packages/components/message"

// 弹出窗计数器
let loadingCount = 0;

export function setServiceReqRes(_this) {
	_this = _this || app
	// 请求的全局连接
	_this.service.setRequestInterceptor((req) => {
		// debugger
		// loading 加载
		if (typeof (req.loading) === 'undefined' || !req.loading) {
			if(loadingCount == 0){
				loading.show({
					text: '数据加载中',
					// spinner: 'loading2',
					background: 'rgba(255,255,255,0.1)',
					transition: 'fadeIn',
				})
			}
			loadingCount ++;
		}

		// 请求的时候，如果自己在请求头添加了token，使用自己添加的token，如果没有添加，使用前端缓存里的token。如强制修改密码。
		let reqToken = req.token;
		let Token = _this.$datax.get('token');
		if (reqToken){
			Token = reqToken;
		}

		let header = {
			'X-Powered-By': 'Firebird',
			'Token': Token,
			passKey: '',
		}

		// 横线越权的设置
		let passKeyId = app.service.passKeyId
		if (passKeyId) {
			let passKey = app.$datax.get(passKeyId, 'passKey')
			if (passKey) {
				header.passKey = passKey
				app.service.passKeyId = ''
			}
		}

		// 设置头部
		req.headers.token = header.Token
		req.headers.passKey = header.passKey

		console.log(`请求 [${req.method.toUpperCase()}] - ${req.url}`)

		return req
	})
	// 响应的全局连接
	_this.service.setResponseInterceptor((res) => {

		loadingCount--;
		if(loadingCount <= 0){
			loadingCount = 0;
			// 关闭遮罩
			loading.hide()
		}

		// IE 8-9
		if (res.data == null && res.config.responseType === 'json' && res.request.responseText != null) {
			try {
				// eslint-disable-next-line no-param-reassign
				res.data = JSON.parse(res.request.responseText);
			} catch (e) {
				// ignored
			}
		}
		// 判断是附件下载
		let contentDisposition = res.headers['content-disposition'];
		if (contentDisposition && (contentDisposition.indexOf("attachment") != -1 || contentDisposition.indexOf("inline") != -1)) {
			// 关闭遮罩
			loading.hide()
			return res
		}

		if (res.data.code === -99) {
			// 未选择部门登陆
			app.$router.replace(_this.$datax.get('GLOBAL_CONFIG').loginPath)
			return
		}

		if (res.data.code <= -1) {
			message.error(res.data.message)
			return
		}

		console.log(`响应 ${res.config.url} ${res.status} `)
		
		// ================== TOKEN自动刷新逻辑 ==================
		// 检查响应数据中是否包含新TOKEN（心跳接口会返回）
		if (res.data && res.data.data && res.data.data.newToken) {
			const newToken = res.data.data.newToken;
			// 更新本地存储的TOKEN
			_this.$datax.set('token', newToken);
			console.log('[TOKEN自动刷新] 检测到新TOKEN，已自动更新');
		}
		// ================== END TOKEN自动刷新逻辑 ==================
		
		// 全局拦截，将头部token放入缓存中
		if (res.headers.token) {
			_this.$datax.set('token', res.headers.token)
		}

		// 有横线越权，则保存
		if (res.headers.passKey) {

			// 判断passKey，如果是json则解析
			try {
				let jsonArr = JSON.parse(passKey)
				// 循环 保存 json
				for (var item in jsonArr) {
					let id = jsonArr[item].id
					let val = jsonArr[item].value
					// 第三个是key的前缀
					_this.$datax.set(id, val, 'passKey')
				}

			} catch (error) {
				console.log(error)
			}

		}

		// 关闭遮罩
		return res.data
	}, (e) => {

		// 关闭遮罩
		loading.hide()
		loadingCount = 0;

		try {
			message.error(`请求异常，错误码：${e.response.status}`)
			// 打印服务异常
			console.log(`请求异常，URL：${e.config.url}, 错误码：${e.response.status}`)
		} catch (error) {

		}

		console.log(`请求异常，异常信息: ${e}`)
	})
}

