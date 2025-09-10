/*!
* index
* (c) 2024 lincong1987
*/

import component from './src/FbecMapGlBase.vue'

component.install = function (adapter) {
  adapter.component(component.name, component)
}

export default component


