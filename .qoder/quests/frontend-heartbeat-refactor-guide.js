// ==================================================================
// 前端心跳改造指南
// ==================================================================
//
// 目的：将Image图片请求方式的心跳改为axios请求，以支持TOKEN自动刷新功能
//
// 原因：Image请求不会触发axios响应拦截器，无法接收newToken
//
// ==================================================================

// ============================================================
// 示例1：AdminHeader.vue 中的心跳改造
// ============================================================

// -------- 改造前 --------
// 文件：D:\projects\ps\ps-fe\@fb\admin-base\views\main\components\AdminHeader.vue

heartbeat() {
    let token = app.$datax.get('token')
    let baseURL = app.service.defaults.baseURL
    // 使用Image请求
    let query = baseURL + '/platform/stationline/heartbeat?jt=' + token
    
    let img = new Image()
    img.onerror = img.onload = function() {}
    img.src = query
    
    clearTimeout(this.heartbeatTimer)
    this.heartbeatTimer = setTimeout(() => {
        this.heartbeat()
    }, 300000)  // 5分钟
}

// -------- 改造后（推荐） --------

heartbeat() {
    let token = app.$datax.get('token')
    
    // 改用axios GET请求
    app.service.get('/platform/stationline/heartbeat', {
        params: { jt: token },
        loading: false  // 关键：不显示loading，避免影响用户体验
    }).then(response => {
        // 响应拦截器会自动处理newToken更新
        // 这里可以添加额外的处理逻辑
        if (response && response.data && response.data.newToken) {
            console.log('[心跳] TOKEN已自动刷新')
        }
    }).catch(error => {
        // 心跳失败不影响用户操作，仅记录日志
        console.error('[心跳] 心跳请求失败', error)
    })
    
    // 清除并重新设置定时器
    clearTimeout(this.heartbeatTimer)
    this.heartbeatTimer = setTimeout(() => {
        this.heartbeat()
    }, 300000)  // 5分钟
}


// ============================================================
// 示例2：online-sdk.js 中的心跳改造
// ============================================================

// -------- 改造前 --------
// 文件：D:\projects\ps\ps-fe\@fb\log-center-ui\src\util\online-sdk.js

heartbeat(time) {
    if (!app.projectConfig.sysconfig.loggerSwitch) {
        console.log("日志开关未开启")
        return;
    }
    
    let token = app.$datax.get('token')
    if (!token) {
        console.error("token is null")
        return;
    }
    
    this.baseURL = app.service.defaults.baseURL
    if (!time) {
        time = 300000  // 5分钟
    }
    
    let img = new Image()
    img.onerror = img.onload = function () {}
    
    let query = this.baseURL + '/platform/stationline/heartbeat?jt=' + token;
    img.src = query
    
    clearInterval(this.ivsession)
    
    this.ivsession = setInterval(()=>{
        token = app.$datax.get('token')
        if (!token) {
            clearInterval(this.ivsession)
            return;
        }
        let query = this.baseURL + '/platform/stationline/heartbeat?jt=' + token;
        img.src = query
    }, time);
}

// -------- 改造后（推荐） --------

heartbeat(time) {
    if (!app.projectConfig.sysconfig.loggerSwitch) {
        console.log("日志开关未开启")
        return;
    }
    
    let token = app.$datax.get('token')
    if (!token) {
        console.error("token is null")
        return;
    }
    
    if (!time) {
        time = 300000  // 5分钟
    }
    
    // 定义心跳发送函数
    const sendHeartbeat = () => {
        const currentToken = app.$datax.get('token')
        if (!currentToken) {
            clearInterval(this.ivsession)
            console.warn('[心跳] TOKEN不存在，停止心跳')
            return;
        }
        
        // 改用axios请求
        app.service.get('/platform/stationline/heartbeat', {
            params: { jt: currentToken },
            loading: false  // 不显示loading
        }).then(response => {
            // 响应拦截器会自动处理TOKEN更新
            console.log('[心跳] 心跳成功')
        }).catch(error => {
            console.error('[心跳] 心跳失败', error)
        })
    }
    
    // 立即发送一次心跳
    sendHeartbeat()
    
    // 清除旧的定时器
    clearInterval(this.ivsession)
    
    // 设置新的定时器
    this.ivsession = setInterval(sendHeartbeat, time);
}


// ============================================================
// 示例3：logger-sdk.js 中的心跳改造（如果存在）
// ============================================================

// 类似上面的改造方式，将Image请求改为axios请求


// ============================================================
// 改造要点总结
// ============================================================

/**
 * 1. 使用 axios 请求方式
 *    - GET 请求：app.service.get(url, { params, loading: false })
 *    - POST 请求：app.service.post(url, data, { loading: false })
 * 
 * 2. 关键参数
 *    - loading: false  // 必须设置，避免显示loading动画
 *    - params: { jt: token }  // TOKEN参数
 * 
 * 3. 错误处理
 *    - 使用 .catch() 捕获错误
 *    - 心跳失败不应影响用户操作
 *    - 仅记录日志，不弹出错误提示
 * 
 * 4. 响应处理
 *    - 响应拦截器会自动处理newToken
 *    - 无需手动更新TOKEN
 *    - 可以添加日志记录
 * 
 * 5. 兼容性
 *    - 确保所有使用心跳的地方都改造
 *    - 保持定时器逻辑不变
 *    - 保持心跳频率不变（通常5分钟）
 */


// ============================================================
// 测试验证
// ============================================================

/**
 * 测试步骤：
 * 
 * 1. 改造代码后重启前端应用
 * 2. 登录系统
 * 3. 打开浏览器控制台（F12）
 * 4. 切换到Network标签
 * 5. 筛选请求：/platform/stationline/heartbeat
 * 6. 等待5分钟观察心跳请求
 * 7. 查看响应数据：
 *    - 正常情况：无newToken字段
 *    - TOKEN快过期：有newToken字段
 * 8. 查看Console日志：
 *    - 应该看到：[TOKEN自动刷新] 检测到新TOKEN，已自动更新
 */


// ============================================================
// 常见问题
// ============================================================

/**
 * Q1: 为什么必须使用 loading: false？
 * A1: 避免每次心跳都显示loading动画，影响用户体验
 * 
 * Q2: 心跳失败会怎样？
 * A2: 仅记录日志，不影响用户操作，下次定时器触发时会重试
 * 
 * Q3: TOKEN何时会自动刷新？
 * A3: 当TOKEN剩余有效期<20%时（例如60分钟有效期，剩余<12分钟）
 * 
 * Q4: 如何确认TOKEN已刷新？
 * A4: 查看控制台日志：[TOKEN自动刷新] 检测到新TOKEN，已自动更新
 * 
 * Q5: 改造后会影响现有功能吗？
 * A5: 不会，仅改变请求方式，保持所有其他逻辑不变
 */


// ============================================================
// 建议的改造顺序
// ============================================================

/**
 * 1. 先改造 AdminHeader.vue（优先级最高）
 *    - 这是主要的心跳入口
 *    - 影响所有登录用户
 * 
 * 2. 再改造 online-sdk.js
 *    - 如果项目使用了在线统计功能
 * 
 * 3. 最后改造其他心跳相关文件
 *    - 根据实际使用情况决定
 * 
 * 4. 每改造一个文件，立即测试验证
 *    - 确保改造后功能正常
 *    - 确保TOKEN自动刷新生效
 */
