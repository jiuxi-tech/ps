/*!
 * 服务器证书管理服务配置
 * (c) 2025
 */

export default {
    /**
     * 分页查询服务器证书列表
     */
    list(formData) {
        return app.$svc.service.post('/sys/server-cert/list', formData)
    },

    /**
     * 查询所有服务器证书列表（不分页）
     */
    all(formData) {
        return app.$svc.service.post('/sys/server-cert/all', formData)
    },

    /**
     * 根据ID查询服务器证书详细信息
     */
    view(formData) {
        return app.$svc.service.get('/sys/server-cert/view', { params: formData })
    },

    /**
     * 新增服务器证书
     */
    add(formData) {
        return app.$svc.service.post('/sys/server-cert/add', formData)
    },

    /**
     * 修改服务器证书
     */
    update(formData) {
        return app.$svc.service.post('/sys/server-cert/update', formData)
    },

    /**
     * 删除服务器证书
     */
    delete(formData) {
        return app.$svc.service.post('/sys/server-cert/delete', formData)
    },

    /**
     * 批量删除服务器证书
     */
    batchDelete(formData) {
        return app.$svc.service.post('/sys/server-cert/batch-delete', formData)
    },

    /**
     * 应用证书到文件系统
     */
    apply(formData) {
        return app.$svc.service.post('/sys/server-cert/apply', formData)
    },

    /**
     * 重启Nginx服务
     */
    restart() {
        return app.$svc.service.post('/sys/server-cert/restart', {})
    },

    /**
     * 验证证书名称是否重复
     */
    checkName(formData) {
        return app.$svc.service.get('/sys/server-cert/check-name', { params: formData })
    },

    /**
     * 查询当前正在使用的证书
     */
    getCurrentCert() {
        return app.$svc.service.get('/sys/server-cert/current')
    },

    /**
     * 查询即将过期的证书
     */
    getExpiringCerts(formData) {
        return app.$svc.service.get('/sys/server-cert/expiring', { params: formData })
    },

    /**
     * 解析上传的证书文件
     */
    parse(formData) {
        return app.$svc.service.post('/sys/server-cert/parse', formData)
    },

    /**
     * 验证证书和私钥是否匹配
     */
    validate(formData) {
        return app.$svc.service.post('/sys/server-cert/validate', formData)
    },

    /**
     * 更新证书过期状态（手动触发）
     */
    updateExpiredStatus() {
        return app.$svc.service.post('/sys/server-cert/update-expired-status', {})
    }
}