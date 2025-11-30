
/**
 * 记录在线人员
 */
export class WebOnline {

    constructor() {

    }

    /**
     * 统计当前登录人数-心跳
     * @param time 定时
     */
    heartbeat(time) {

        if (!app.projectConfig.sysconfig.loggerSwitch) {
            // 如果开关没有开启，这里直接return，不发送请求
            console.log("日志开关未开启sysconfig.loggerSwitch")
            return;
        }

        let token = app.$datax.get('token')
        if (!token) {
            console.error("token is null")
            return;
        }

        if (!time) {
            // 5 分钟请求一次
            time = 300000
        }

        // 定义心跳发送函数
        const sendHeartbeat = () => {
            const currentToken = app.$datax.get('token')
            if (!currentToken) {
                clearInterval(this.ivsession)
                console.warn('[心跳] TOKEN不存在，停止心跳')
                return;
            }
            
            // 改用axios请求以支持TOKEN自动刷新
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

        // 清除原来的
        clearInterval(this.ivsession)

        // 定时请求
        this.ivsession = setInterval(sendHeartbeat, time);

    }

    /**
     * 获取当前登录人数
     * @param callback 回调方法，返回 当前登录人数
     * @param time 定时
     */
    countonline(callback, time) {

        if (typeof time == 'undefined') {
            // 5 分钟请求一次
            time = 300000
            //time = 10000
        }

        let token = app.$datax.get('token')
        if (!token) {
            console.error("token is null")
            return;
        }

        let online = function (callback, jt) {
            // 组装请求地址， 小于 10k
            // 日志服务地址 baseURL

            let query = '/platform/stationline/countonline?jt=' + jt;

            app.service.request({
                url: query,
                method: 'get', // 请求方式 post,get, 默认是 get,
                transformRequest: [
                    // 把json数据序列化成xxx=?&xx=?的格式
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
                // `data` 是作为请求主体被发送的数据， post 采用
                data: {},
                // `headers` 是即将被发送的自定义请求头
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                // `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
                responseType: 'json', // 默认的
                // `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
                // 如果请求话费了超过 `timeout` 的时间，请求将被中断
                timeout: 5000,
            }).then(result => {
                // 判断code
                if (result.code == 1) {
                    callback(result.data);
                }
            })
        };

        // 清楚原来的
        clearInterval(this.ivonline)

        // 立即执行一次
        online(callback, token)
        // 定时请求
        this.ivonline =setInterval(() => {
            let token = app.$datax.get('token')
            if (!token) {
                clearInterval(this.ivonline)
                return;
            }
            online(callback, token)
        }, time);

    }
}

let install = WebOnline.install = (adapter) => {
    app.$online = adapter.prototype.$online = new WebOnline()
}

export default {
    WebOnline,
    install,
}