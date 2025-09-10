
/**
 * 日志服务 2023-08-07
 *
 // 使用示例：
 let data = {

        // 模块编码
        moduleCode: 'login',
        // 操作类型： login/logout/add/delete/edit/query/pass/unpass, 可以自己定义
        operateType: 'login',
        // 来源, 1:前端  2:后端  3：app前端
                                        source: 1,
        // 操作记录id ,登录和新增时为空。记录业务操作时可填写
        operterRid: 'xxxx'，
                                       // 扩展信息，可选，长度不能超过1000
        extend01: '修改了隐患的等级'
    }
 this.$logger.send(data);

 */

export class WebLogger {

    constructor() {

    }

    /**
     * 发送埋点数据
     * @param data
     */
    send(data) {

        if (!app.projectConfig.sysconfig.loggerSwitch) {
            // 如果开关没有开启，这里直接return，不发送请求
            return;
        }

        // 日志服务地址 baseURL
        this.baseURL = app.service.defaults.baseURL

        if (!data.moduleCode){
            throw new Error('模块编码不能为空！')
        }

        if (data.operateType){
            // 兼容字段operateType
            data.operterType = data.operateType;
        }

        if (!data.operterType){
            throw new Error('操作类型不能为空！')
        }

        // 获取当前登录用户信息
        let userInfo = app.$datax.get('userInfo')
        // 用户名
        data.username = userInfo.userName;
        // 用户类型
        data.category = userInfo.category;
        // 系统名称
        data.appName = data.appName || app.projectConfig.name;
        let token = app.$datax.get('token')


  

        // 组装请求地址， 小于 10k
        let query = this.baseURL + '/platform/logger/collection?jt=' + token;

        for (let [key, value] of Object.entries(data)) {
            query += '&' + key + '=' + encodeURIComponent(value)
        }

        let img = new Image()
        img.onerror = img.onload = function () {
        }
        img.src = query
    }

    /**
     * 发送 登录 埋点数据，也可使用send方法
     * @param data
     */
    login(data) {

        if (!app.projectConfig.sysconfig.loggerSwitch) {
            // 如果开关没有开启，这里直接return，不发送请求
            console.log("日志开关未开启sysconfig.loggerSwitch")
            return;
        }

        // 日志服务地址 baseURL
        this.baseURL = app.service.defaults.baseURL

        if (!data.moduleCode){
            data.moduleCode = 'login'
        }

        if (data.operateType){
            // 兼容字段operateType
            data.operterType = data.operateType;
        }

        if (!data.operterType){
            throw new Error('操作类型不能为空！')
        }

        // 获取当前登录用户信息
        let userInfo = app.$datax.get('userInfo')
        // 用户名
        data.username = userInfo.userName;
        // 用户类型
        data.category = userInfo.category;
        // 系统名称
        data.appName = app.projectConfig.name;
        let token = app.$datax.get('token')

        // 组装请求地址， 小于 10k
        let query = this.baseURL + '/platform/logger/login?jt=' + token;

        for (let [key, value] of Object.entries(data)) {
            query += '&' + key + '=' + encodeURIComponent(value)
        }

        let img = new Image()
        img.onerror = img.onload = function () {
        }
        img.src = query
    }

}

let install = WebLogger.install = (adapter) => {
    app.$logger = adapter.prototype.$logger = new WebLogger()
}

export default {
    WebLogger,
    install,
}