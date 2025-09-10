import waves from './waves'

export const install = function (adapter) {
	adapter.directive('waves', waves)
}
export default {
	waves,
	install,
}
