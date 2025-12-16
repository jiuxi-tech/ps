import loading from '@fb/fb-ui/packages/components/spin'
import message from "@fb/fb-ui/packages/components/message"

// 弹出窗计数器
let loadingCount = 0;

export function setServiceReqRes(_this) {
	_this = _this || app

	// 超时时间设置为5分钟
	_this.service.timeout = 1000 * 60 * 5
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
		if (contentDisposition && contentDisposition.indexOf("attachment") != -1) {
			// 关闭遮罩
			loading.hide()
			return res
		}

		if (res.data.code === -99) {
			// 未选择部门登陆
			app.$router.replace(_this.$datax.get('GLOBAL_CONFIG').loginPath)
			return
		}

		// token过期处理
		if (res.data.code === -44) {
			_this.$datax.set('token', '');
			_this.$store.state.menu.loadingStauts = false
			app.$router.replace(_this.$datax.get('GLOBAL_CONFIG').loginPath);
			return
		}

		// 处理IP访问拒绝错误
		if (res.data.code === 403 && res.data.message && res.data.message.includes('IP地址不在允许访问范围内')) {
			// 显示IP访问拒绝的友好界面
			showIpAccessDeniedPage(res.data.message)
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

		// 
		res.data._response_ = res

		
		
		// 关闭遮罩
		return res.data
	}, (e) => {

		// 关闭遮罩
		loading.hide()
		loadingCount = 0;
		
		if (e.response && e.response.status === 500) {
			// 尝试从多个可能的字段提取错误信息
			let errorMsg = '服务器内部错误,请稍后重试';
			
			if (e.response && e.response.data) {
				const data = e.response.data;
				// 优先级: message > error > msg > errmsg > 默认提示
				errorMsg = data.message || data.error || data.msg || data.errmsg || data.errMsg || errorMsg;
				
				// 如果data是字符串,直接使用
				if (typeof data === 'string' && data.length > 0) {
					errorMsg = data;
				}
			}
			
			message.error(errorMsg);
			console.error('500错误详情:', e.response);
		}
		

		try {
			// 检查是否为403 IP访问拒绝错误
			if (e.response && e.response.status === 403) {
				const responseData = e.response.data
				if (responseData && responseData.message && responseData.message.includes('您的IP地址不在允许访问范围内')) {
					// 显示IP访问拒绝页面
					showIpAccessDeniedPage(responseData.message)
					return Promise.reject(e)
				}
			}
			
			//message.error(`请求异常，错误码：${e.response.status}`)
			// 打印服务异常
			console.log(`请求异常，URL：${e.config.url}, 错误码：${e.response.status}`)
		} catch (error) {

		}

		console.log(`请求异常，异常信息: ${e}`)
	})
}

// 显示IP访问拒绝页面
function showIpAccessDeniedPage(errorMessage) {
	try {
		// 创建全屏遮罩
		const overlay = document.createElement('div')
		overlay.id = 'ip-access-denied-overlay'
		overlay.style.cssText = `
			position: fixed;
			top: 0;
			left: 0;
			width: 100%;
			height: 100%;
			z-index: 9999;
			background: linear-gradient(180deg,#fde7d8,#d10000,#ffad99 35%,#fbe6d7 65%,#fdf4ed);
			display: flex;
			align-items: center;
			justify-content: center;
			padding: 20px;
			font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
		`
		
		// 创建内容容器
		const container = document.createElement('div')
		container.style.cssText = `
			background: white;
			border-radius: 16px;
			box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
			padding: 40px;
			max-width: 600px;
			width: 100%;
			text-align: center;
			animation: slideUp 0.6s ease-out;
		`
		
		// 获取当前时间
		const currentTime = new Date().toLocaleString('zh-CN', {
			year: 'numeric',
			month: '2-digit',
			day: '2-digit',
			hour: '2-digit',
			minute: '2-digit',
			second: '2-digit'
		})
		
		// 创建HTML内容
		container.innerHTML = `
			<style>
				@keyframes slideUp {
					from {
						opacity: 0;
						transform: translateY(30px);
					}
					to {
						opacity: 1;
						transform: translateY(0);
					}
				}
				@keyframes pulse {
					0%, 100% { transform: scale(1); }
					50% { transform: scale(1.05); }
				}
				.denied-icon {
					width: 80px;
					height: 80px;
					animation: pulse 2s infinite;
					margin-bottom: 30px;
				}
				.title {
					font-size: 28px;
					font-weight: 600;
					color: #2c3e50;
					margin: 0 0 16px 0;
				}
				.message {
					font-size: 16px;
					color: #7f8c8d;
					line-height: 1.6;
					margin: 0 0 24px 0;
				}
				.details {
					background: #f8f9fa;
					border-radius: 8px;
					padding: 20px;
					margin: 20px 0;
					text-align: left;
				}
				.info-row {
					margin: 8px 0;
					display: flex;
					justify-content: space-between;
					align-items: center;
				}
				.label {
					font-weight: 500;
					color: #495057;
				}
				.value {
					font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
					background: #e9ecef;
					padding: 4px 8px;
					border-radius: 4px;
					font-size: 14px;
					color: #495057;
				}
				.btn {
					padding: 10px 20px;
					margin: 0 8px;
					border: none;
					border-radius: 6px;
					cursor: pointer;
					font-size: 14px;
					transition: all 0.3s ease;
					min-width: 120px;
				}
				.btn-primary {
					background: #007bff;
					color: white;
				}
				.btn-primary:hover {
					background: #0056b3;
				}
				.btn-secondary {
					background: #6c757d;
					color: white;
				}
				.btn-secondary:hover {
					background: #545b62;
				}
				@media (max-width: 768px) {
					.info-row {
						flex-direction: column;
						align-items: flex-start;
						gap: 4px;
					}
					.btn {
						display: block;
						width: 100%;
						margin: 8px 0;
					}
				}
			</style>
			
			<!-- 图标区域 -->
			<svg class="denied-icon" viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
				<circle cx="32" cy="32" r="30" stroke="#ff4757" stroke-width="4" fill="#fff5f5"/>
				<path d="M20 20L44 44M44 20L20 44" stroke="#ff4757" stroke-width="4" stroke-linecap="round"/>
			</svg>
			
			<!-- 主要内容 -->
			<h1 class="title">访问受限</h1>
			<p class="message">${errorMessage}</p>
			
			<div class="details">
				<div class="info-row">
					<span class="label">访问时间：</span>
					<span class="value">${currentTime}</span>
				</div>
				<div class="info-row">
					<span class="label">错误代码：</span>
					<span class="value">403 Forbidden</span>
				</div>
			</div>
			
			<!-- 操作按钮 -->
			<div style="margin: 30px 0;">
				<button class="btn btn-primary" onclick="window.location.reload()">🔄 重新尝试</button>
				<!-- <button class="btn btn-secondary" onclick="handleContact()">📞 联系管理员</button>-->
			</div>
			
			<!-- 帮助信息 -->
			<div style="text-align: left; margin-top: 30px; padding: 20px; background: #f8f9fa; border-radius: 8px; font-size: 14px; color: #6c757d;">
				<p><strong>为什么会出现这个页面？</strong></p>
				<ul style="margin: 12px 0; padding-left: 20px;">
					<li>您的IP地址不在系统允许的访问范围内</li>
					<li>系统管理员已限制从您当前网络位置的访问</li>
					<li>您可能正在使用代理或VPN服务</li>
				</ul>
				<div style="background: #e3f2fd; border-left: 4px solid #2196f3; padding: 12px 16px; margin: 16px 0 0 0; border-radius: 0 4px 4px 0; font-style: italic;">
					如需访问系统，请联系系统管理员将您的IP地址添加到白名单中。
				</div>
			</div>
		`
		
		// 添加联系管理员函数到全局
		window.handleContact = function() {
			const subject = encodeURIComponent('IP访问权限申请')
			const body = encodeURIComponent(
				`尊敬的管理员：\n\n` +
				`我在访问系统时遇到IP访问限制，请协助处理。\n\n` +
				`访问信息：\n` +
				`- 访问时间：${currentTime}\n` +
				`- 错误信息：${errorMessage}\n\n` +
				`谢谢！`
			)
			
			const mailtoLink = `mailto:admin@company.com?subject=${subject}&body=${body}`
			try {
				window.open(mailtoLink)
			} catch (error) {
				alert('请联系系统管理员：\n📧 邮箱：admin@company.com\n📞 电话：400-000-0000')
			}
		}
		
		// 将容器添加到遮罩中
		overlay.appendChild(container)
		
		// 移除已存在的遮罩（如果有）
		const existingOverlay = document.getElementById('ip-access-denied-overlay')
		if (existingOverlay) {
			existingOverlay.remove()
		}
		
		// 添加到页面
		document.body.appendChild(overlay)
		
		// 禁用页面滚动
		document.body.style.overflow = 'hidden'
		
		console.log('IP访问拒绝页面已显示')
		
	} catch (error) {
		console.error('显示IP访问拒绝页面失败:', error)
		// 降级处理：显示普通错误消息
		message.error(errorMessage || '您的IP地址不在允许访问范围内，请联系管理员')
	}
}

