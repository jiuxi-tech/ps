import { prefix } from '../../../../src/config'
export default {
	name: 'FbMarker',

	props: {
		mark: {
			type: [String, Object]
		}
	},
	data() {
		return {
			prefix
		}
	},
	render(h) {
		let label = typeof this.mark === 'string' ? this.mark : this.mark.label;

		return  h('div', {
			class: `${prefix}-slider__marks-text`,
			style: [ this.mark.style || {} ]
		}, [
			label
		])
	}
};
