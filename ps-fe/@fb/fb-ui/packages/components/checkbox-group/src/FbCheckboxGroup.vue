<script>

import { prefix } from '../../../../src/config'
/*!
 * FbCheckboxGroup
 * (c) 2020 lincong1987
 */

import { each, assign, isEqual, findIndex } from 'lodash-es'
import { closest, find } from '../../../utils/componentUtils'
import FbCheckbox from '../../checkbox/src/FbCheckbox'
import { uuid } from '../../../utils/utils'

export default {

	render (h, context) {
		return h('div', {
			class: this.getClass,
		}, [
			this.$slots.default,
			this.myData.map((node, i) => {
				return h('fb-checkbox', {
					style: this.checkboxStyle,
					props: {
						key: uuid(),
						value: node[this.reader.value],
						label: node[this.reader.label],
						disabled: node.disabled,
						readonly: node.readonly,
						size: node.size || this.size,
						node,
						index: i,
					},
					scopedSlots: this.$scopedSlots,
				})
			}),

		])
	},

	name: 'FbCheckboxGroup',
	components: {
		FbCheckbox,
	},
	props: {
		value: {
			type: Array,
			default () {
				return []
			},
		}, // 如果传入一个数组
		data: {
			type: Array,
			default () {
				return []
			},
		}, // 是否垂直
		vertical: {
			type: Boolean,
			default: false,
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

		disabled: {
			type: Boolean,
			default: false,
		},

		readonly: {
			type: Boolean,
			default: false,
		},

		checkboxStyle: {
			type: [Object, Array, String],
		},

		size: {
			type: String,
			default: undefined,
		},
	},
	data () {
		return {
			prefix,
			myData: this.data,
			currentValue: this.value,
			name: `fb_checkbox_${uuid()}`,
		}
	},

	computed: {
		getClass () {
			let arr = [`${this.prefix}-checkbox-group`]
			if (this.vertical) {
				arr.push(`${this.prefix}-checkbox-group--vertical`)
			}
			return arr
		},

	},

	watch: {
		value (val) {
			this.currentValue = val
			this.updateChild()
		},
		data (value, old) {
			//	this.checkboxs = this.$children
			this.myData = value
		},
	},

	methods: {
		assign,

		updateChild () {
			this.checkboxs().forEach((child) => {
				child.checked = this.currentValue.includes(child.value)
			})
		},
		addItem (checkbox) {
//			console.log('addItem')
			checkbox.name = this.name
			checkbox.group = true
			//this.checkboxs.push(checkbox)
		},

		removeItem (checkbox) {
//			console.log('removeItem')
//			let index = findIndex(this.checkboxs, {value: checkbox.value})
//
			//this.checkboxs.splice(index, 1)
		},

		change (value, checked, node, e) {

			let checkedValues = []
			let checkedNodes = []
			each(this.checkboxs(), (checkbox) => {
				if (checkbox.checked) {
					checkedValues.push(checkbox.value)
					checkedNodes.push(checkbox)
				}
			})
//
			this.currentValue = checkedValues
			this.changeValue(value, checked, node, checkedNodes, e)
		},
		changeValue (value, checked, node, checkedNodes, e) {
			this.$emit('input', this.currentValue, value, checked, node, checkedNodes, e)
			this.$emit('change', this.currentValue, value, checked, node, checkedNodes, e)
			this.$emit('on-change', this.currentValue, value, checked, node, checkedNodes, e)
			if (this.formItem && e) {
				this.formItem.$emit('on-form-change', this.currentValue, value, checked, node, checkedNodes, e)
			}
		},

		checkboxs () {
			return this.$children.filter((item) => {
				return item.$options.name === 'FbCheckbox'
			})
		},
	},

	created () {
//		this.checkboxs = []
	},

	mounted () {
	},

	beforeDestroy () {
//		this.checkboxs = []
	},
}
</script>

<style scoped>

</style>
