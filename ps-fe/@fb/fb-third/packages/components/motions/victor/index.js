import component from './src/MotionVictor.vue'

component.install = function (adapter) {
	adapter.component(component.name, component)
}

export default component
