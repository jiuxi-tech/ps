import permission from './permission'

// use v-permission='buttonId'
const install = function (adapter) {
	adapter.directive('permission', permission)
}

export default permission
