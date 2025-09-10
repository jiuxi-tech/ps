/*!
 * index
 * (c) 2021 lincong1987
 */

import FbTmap from './src/FbTmap'

let tmapComponents = [
	FbTmap
]

tmapComponents.forEach(comp => {
	comp.install = function (adapter) {
		adapter.component(comp.name, comp)
	}
})

export function install (adapter) {
	tmapComponents.forEach(comp => {
		comp.install(adapter)
	})
}



export default {
	...echartsComponents,
	install,
}
