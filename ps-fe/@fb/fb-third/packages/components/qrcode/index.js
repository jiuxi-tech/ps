/*!
 * index
 * (c) 2021 lincong1987
 */

import component from './src/FbQrcode.vue'

component.install = function (adapter) {
	adapter.component(component.name, component)
}

export default component
