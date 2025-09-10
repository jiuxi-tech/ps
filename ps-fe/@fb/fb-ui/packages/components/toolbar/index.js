/*!
* index
* (c) 2020 lincong1987
*/

import component from './src/FbToolbar'

component.install = function (adapter) {
  adapter.component(component.name, component)
}

export default component


