// 保存是否刷新过token
let isRefersh = true;
// 保存上次点击的时间
let preTime = null;
// 保存上次刷新token的时间
let preReferTokenTime = null;

// 1分钟
const ONE_MINUTE = 60 * 1000;

// 登录成功后，开始刷新token，1分钟判断一次，当前时间与上次操作时间差值大于等于30分钟时，调用退出登录接口；每28分钟刷新一次token
let startRefreshToken = ()=> {

	if (!app.$datax.get('token')) {
		console.log("已退出登录")
		return;
	}

	if (preTime == null) {
		preTime = Date.now();
	}

	if (preReferTokenTime == null) {
		preReferTokenTime = Date.now();
	}

	// 取当前时间
	let currentTime = Date.now();

	// 30分钟未操作，则退出登录
	if (currentTime - preTime >= 30 * ONE_MINUTE){
		logoutToken(app.$datax.get('token'));
		return
	}

	// 如果未刷新过token, 则1分钟后刷新一次token
	if (!isRefersh && (currentTime - preReferTokenTime >= ONE_MINUTE)) {
		preReferTokenTime = Date.now();
		// 刷新token
		referToken();
        // 标记已刷新
		isRefersh = true;
	}

	// 28分钟刷新一次token
	if (currentTime - preReferTokenTime >= 28 * ONE_MINUTE) {
		preReferTokenTime = Date.now();
		// 刷新token
		referToken();
	}

	// 再次启动延时任务
	setTimeout(()=>{
		startRefreshToken()
	}, ONE_MINUTE)
};
// post请求
var post = (url, formData)=> {
	return app.service.request({
		url: url,
		method: 'post',
		transformRequest: [
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
		timeout: 5000,
		loading: true,
	})
}

// 将token失效
var invalidateToken = (token)=> {
	if (!token) {
		console.error("执行invalidateToken参数为空：" + token)
		return;
	}
	post('/platform/invalidate-token', {token}).then((result) => {
		// 此处不做任何处理，当前token已在后端失效
		// 此处不能执行 app.$datax.set('token', '')
	})
}

// 退出登录
var logoutToken = (token)=> {
	if (!token) {
		console.error("执行logoutToken参数为空：" + token)
		return;
	}
	post('/platform/logout', {token}).then((result) => {
		// 判断code
		if (result && result.code == 1) {
			app.$datax.set('token', '');
		}
	})
}

// 刷新token
var referToken = ()=> {
	// 原token
	let oldToken = app.$datax.get('token');
	if (oldToken) {
		post('/platform/refresh-token').then((result) => {
			// 判断code
			if (result) {
				if (result.code == 1) {
					app.$datax.set('token', result.data);
					// 将原token失效，失效将影响浏览器复制的页签
					invalidateToken(oldToken);
				}
			}
		})
	}
}

// 页面刷新后，如果存在token, 则启动执行
let reloadPage = ()=>{
	if (app.$datax.get('token')) {
		isRefersh = false;
		startRefreshToken();
	}
}

// 增加全局监听，记录上次页面点击时间
window.addEventListener('click', (e)=>{
	// console.log("--- 点击了 ：", preTime);
	preTime = Date.now();
})

window.addEventListener('mousemove', (e)=>{
	// console.log("--- 点击了 ：", preTime);
	preTime = Date.now();
})

// 页面刷新后执行
reloadPage();

export {startRefreshToken, logoutToken};

// 1. 页面无操作30分钟后退出登录
// 2. 退出登录后，需停止刷新token
// 3. 页面刷新后，需要重新启动刷新
