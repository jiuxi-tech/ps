/*!
* index
* (c) 2022 lincong1987
*/

import component from './src/FbPdfViewer'

component.install = function (adapter) {
  adapter.component(component.name, component)
}

export default component


