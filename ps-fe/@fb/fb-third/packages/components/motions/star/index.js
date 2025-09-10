import component from './src/MotionStar.vue'

component.install = function (adapter) {
	adapter.component(component.name, component)
}

export default component
