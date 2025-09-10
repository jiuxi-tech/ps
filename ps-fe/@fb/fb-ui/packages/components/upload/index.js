/*!
 * index
 * (c) 2020 lincong1987
 */

import FbUpload from './src/FbUpload'
import FbAjaxUpload from './src/FbAjaxUpload'
import FbUploadAdapter from './src/FbUploadAdapter'
import FbUploadList from './src/FbUploadList'
import FbUploadDrag from './src/FbUploadDrag'

FbUpload.install = function (adapter) {
	adapter.component(FbUpload.name, FbUpload)
}
FbAjaxUpload.install = function (adapter) {
	adapter.component(FbAjaxUpload.name, FbAjaxUpload)
}
FbUploadAdapter.install = function (adapter) {
	adapter.component(FbUploadAdapter.name, FbUploadAdapter)
}
FbUploadList.install = function (adapter) {
	adapter.component(FbUploadList.name, FbUploadList)
}
FbUploadDrag.install = function (adapter) {
	adapter.component(FbUploadDrag.name, FbUploadDrag)
}

export default {
	FbUpload,
	FbAjaxUpload,
	FbUploadDrag,
	FbUploadList,
	FbUploadAdapter,
}


