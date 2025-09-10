/*!
* serviceTool
* (c) 2020 lincong1987
*/

import {
    trimStart,
    trimEnd,
    set,
    defaults,
    isArray,
    each,
    isObjectLike,
	flattenDeep
} from 'lodash-es'

export function injectServiceProvide(serviceProvide, serviceRoot, path) {

    if (isObjectLike(serviceProvide)) {
        let requestMapping = path.split('.').join('/')
        defaults(serviceProvide, {
            list(model) {
                return app.$svc.service.post(`/${requestMapping}/list`, null, {params: model})
            },
            save(model) {
                return app.$svc.service.post(`/${requestMapping}/add`, null, {params: model})
            },
            update(model) {
                return app.$svc.service.post(`/${requestMapping}/update`, null, {params: model})
            },
            delete(model) {
                return app.$svc.service.post(`/${requestMapping}/delete`, null, {params: model})
            },
            get(model) {
                return app.$svc.service.get(`/${requestMapping}/view`, {params: model})
            }
        })

        each(serviceProvide, (sp, name) => {
            if (isObjectLike(sp)) {
                injectServiceProvide(sp, serviceRoot, path + "." + name)
            }
        })
    }

}

export function parsePathToNamespace(serviceRoot, path, serviceProvide) {
    injectServiceProvide(serviceProvide, serviceRoot, path)

    set(serviceRoot, path, serviceProvide)
}

export const handle = function (service, files) {
    if (!isArray(files)) {
        files = [files];
    }
	files = flattenDeep(files)

    for (let i = 0; i < files.length; i++) {
		let mappedServices = files[i];

        mappedServices.keys().forEach((key) => {
            let path = trimStart(trimEnd(key, '.js'), './').split('/')

            if (path.length > 0 && path[path.length - 1] == 'index') {
                path.pop()
            }

            console.info('[serviceTool] handle ' + path.join('.'))
            parsePathToNamespace(service, path.join('.'), mappedServices(key).default)
        })
    }
}

export default {
    handle,
    parsePathToNamespace,
    injectServiceProvide
}
