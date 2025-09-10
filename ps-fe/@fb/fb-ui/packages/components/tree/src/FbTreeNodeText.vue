<script>
import { assign, each } from 'lodash-es'

import { prefix } from '../../../../src/config'

/**
 * FbTreeNodeText
 * (c) 2020 lincong1987
 */

export default {
	name: 'FbTreeNodeText',
	props: {
		root: {
			type: Object,
			default: undefined,
		},
		node: {
			type: Object,
			default: undefined,
		},
		render: {
			type: Function,
			default: undefined,
		},
		reader: {
			type: Object,
			default () {
				return {
					value: 'value',
					label: 'label',
				}
			},
		},
	},
	data () {
		return {
			prefix,
			myReader: assign({}, {
				label: 'label',
			}, this.reader),
		}
	},
	computed: {
		getClass () {
			let arr = [`${prefix}-tree-node-text`]

			if (this.node.selected) {
				arr.push('selected')
			}

			return arr
		},
	},
	methods: {
		handleClick (e) {
			this.$emit('on-click', e)
		},
	},
	render (h) {
		const son = []
		let scopedSlots = {}

		for (let slot in this.root.$scopedSlots) {
			if (slot === 'node') {
				scopedSlots.node = this.root.$scopedSlots[slot]
			}
		}

		// 如果是通过
		if (this.root.scopedSlots) {
			for (let slot in this.root.scopedSlots) {
				if (slot === 'node') {
					scopedSlots.node = this.root.scopedSlots[slot]
				}
			}
		}

		if (scopedSlots.node) {
			son.push(scopedSlots.node({
				node: this.node,
				root: this.root,
			}))
		} else {
			son.push(this.node[this.myReader.label || 'label'])
		}

//			if (this.root.$scopedSlots.default) {
//				son.push(this.root.$scopedSlots.default({
//					node: this.node,
//					root: this.root,
//				}))
//			} else {
//				son.push(this.node[this.myReader.label || 'label'])
//			}
//
		return h('span', {
			class: {
				[`${prefix}-tree-node-text`]: true,
				selected: this.node.selected,
			},
			on: {
				click: this.handleClick,
			},
		}, son)
	},
}
</script>

<style lang="less" scoped>

</style>
