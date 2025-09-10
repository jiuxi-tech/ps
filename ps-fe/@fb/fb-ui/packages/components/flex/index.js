/*!
 * index
 * (c) 2024 lincong1987
 */

import component from './src/FbFlex'

component.install = function (adapter) {
	adapter.component(component.name, component)
}

export default component


