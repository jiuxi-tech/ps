import component from './src/MotionStarLine.vue'

component.install = function (adapter) {
	adapter.component(component.name, component)
}

export default component
