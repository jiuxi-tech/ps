
/*!
 * index
 * (c) 2021 lincong1987
 */

let index = {}

import {defaultsDeep} from 'lodash-es'

export default {
    /**
     * 更新许可证，非登录用户操作
     * @param options
     * @returns {*}
     */
    upload(options) {
        return app.$svc.service.request(defaultsDeep({}, {
            url: '/platform/licence/upload',
            // 毫秒
            timeout: 300000,
        }, options))
    },

    /**
     * 更新许可证，需登录后操作
     * @param options
     * @returns {*}
     */
    sys_upload(options) {
        return app.$svc.service.request(defaultsDeep({}, {
            url: '/sys/licence/upload',
            // 毫秒
            timeout: 300000,
        }, options))
    },

    view(formData) {
        return app.service.get('/platform/licence/view', {params: formData})
    },
}