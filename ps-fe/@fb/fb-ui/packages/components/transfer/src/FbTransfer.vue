 
<script>
import FbButton from '../../button/src/FbButton'
import FbCheckbox from '../../checkbox/src/FbCheckbox'
import { includes, map, partition, filter, union, intersection } from 'lodash-es'
import FbEmpty from '../../empty/src/FbEmpty'
import FbCheckboxGroup from '../../checkbox-group/src/FbCheckboxGroup'
import { prefix } from '../../../../src/config'

/**
 * FbTransfer
 * (c) 2020 lincong1987
 */
export default {
	render (h, context) {
		return h('div', {
			class: this.getClass,
		}, [
			this.getSourceRender(h),

			this.getOperationRender(h),

			this.getTargetRender(h),
		])
	},
	name: 'FbTransfer',
	components: {
		FbCheckboxGroup,
		FbEmpty,
		FbCheckbox,
		FbButton,
	},
	props: {
		value: {
			type: Array,
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
		data: {
			type: Array,
			default () {
				return []
			},
		},
		titles: {
			type: Array,
			default () {
				return [null, null]
			},
		},
		buttons: {
			type: Array,
			default () {
				return [null, null]
			},
		},
		listStyle: {
			type: Object,
			default: undefined,
		},

		targetKeys: {
			type: Array,
			default () {
				return []
			},
		},

		selectedKeys: {
			type: Array,
			default () {
				return []
			},
		},

		showFastButtons: {
			type: Boolean,
			default: false,
		},

		sourceEmptyText: {
			type: String,
		},
		sourceEmptyType: {
			type: String,
		},
		targetEmptyText: {
			type: String,
		},
		targetEmptyType: {
			type: String,
		},
	},
	data () {

		let mySourceList = this.data

		let myTargetList = []

		if (this.targetKeys.length > 0) {

			let group = partition(this.data, (node) => {
				return includes(this.targetKeys, node[this.reader.value])
			})

			myTargetList = group[0]
			mySourceList = group[1]

		}
		let localButtons = [...(this.buttons || [null, null])]
		if(!localButtons[0]){
			localButtons[0] = null
		}
		if(!localButtons[1]){
			localButtons[1] = null
		}

		return {
			prefix,
			toLeftButtonDisabled: true,
			toLeftButtonType: '',
			toLeftButtonText: localButtons[0] || '',

			toRightButtonDisabled: true,
			toRightButtonType: '',
			toRightButtonText: localButtons[1] || '',

			mySourceList: mySourceList,
			sourceChecked: this.selectedKeys,
			sourceAllChecked: false,

			myTargetList: myTargetList,
			targetChecked: [],
			targetAllChecked: false,
		}
	},
	computed: {
		getClass () {
			let arr = [`${prefix}-transfer`]
			return arr
		},

		sourceHasCheckedText () {

			if (this.sourceChecked.length === 0) {
				return `共${this.mySourceList.length}项`
			}

			return `已选 ${this.sourceChecked.length}/${this.mySourceList.length}`

		},

		targetHasCheckedText () {

			if (this.targetChecked.length === 0) {
				return `共${this.myTargetList.length}项`
			}

			return `已选 ${this.targetChecked.length}/${this.myTargetList.length}`

		},

		myTargetKeys () {
			return filter(this.myTargetList, (node) => {
				return node[this.reader.value]
			})
		},
		mySourceKeys () {
			return filter(this.mySourceList, (node) => {
				return node[this.reader.value]
			})
		},

	},
	watch: {

		data (value) {
			this.mySourceList = value
		},

		value (val) {
			let group = partition(this.data, (node) => {
				return includes(val, node[this.reader.value])
			})

			this.myTargetList = group[0]
			this.mySourceList = group[1]
		},

		targetKeys (value) {
			if (value && value.length > 0) {

				let group = partition(this.data, (node) => {
					return includes(value, node[this.reader.value])
				})

				this.myTargetList = group[0]
				this.mySourceList = group[1]

			}
		},

		selectedKeys (value) {
			if (intersection(this.mySourceList.map(n => n[this.reader.value]), value).length) {
				this.sourceChecked = value
			}
		},

		mySourceList: {
			handler (value) {

				if (value.length === 0) {
					this.sourceChecked = []
					this.sourceAllChecked = false
				}
			},
			deep: true,
		},

		sourceChecked (value) {

			if (value.length === 0) {
				this.sourceAllChecked = false
			}
		},

		myTargetList (value) {

			if (value.length === 0) {
				this.targetChecked = []
				this.targetAllChecked = false
			}
		},

		targetChecked (value) {

			if (value.length === 0) {
				this.targetAllChecked = false
			}
		},

	},
	methods: {

		getSourceRender (h) {
			let vm = this

			let scopedSlots = {}
			if (this.$scopedSlots.source) {
				scopedSlots.label = this.$scopedSlots.source
			}
			if (this.$scopedSlots['source-checkbox']) {
				scopedSlots.checkbox = this.$scopedSlots['source-checkbox']
			}
			return h('div', {
				class: `${this.prefix}-transfer-list`,
				style: this.listStyle,
			}, [
				h('div', {class: `${this.prefix}-transfer-list-header`}, [
					h('fb-checkbox', {
						props: {
							disabled: this.mySourceList.length === 0,
							value: this.sourceAllChecked,
						},
						on: {
							input (val) {
								vm.sourceAllChecked = val
								vm.handelSourceAllCheck(val)
							},
						},
						scopedSlots: {
							label (props) {
								return vm.sourceHasCheckedText
							},
						},
					}),
					h('span', {
						class: `${this.prefix}-transfer-list-header-selected`,
					}, [
						h('span', {
							class: `${this.prefix}-transfer-list-header-title`,
						}, [this.titles[0]]),
					]),
				]),
				h('div', {class: `${this.prefix}-transfer-list-body`}, [
					this.mySourceList.length === 0
						? h('fb-empty', {
							props: {
								text: this.sourceEmptyText,
								type: this.sourceEmptyType,
							},
						})
						: h('fb-checkbox-group', {
							props: {
								value: this.sourceChecked,
								data: this.mySourceList,
								reader: this.reader,
								vertical: true,
							},
							class: `${this.prefix}-transfer-list-content`,
							on: {
								input: (v) => this.sourceChecked = v,
							},
							scopedSlots,
						}),
				]),
				h('div', {class: `${this.prefix}-transfer-list-footer`}, []),
			])
		},
		getTargetRender (h) {
			let vm = this

			let scopedSlots = {}
			if (this.$scopedSlots.target) {
				scopedSlots.label = this.$scopedSlots.target
			}

			if (this.$scopedSlots['target-checkbox']) {
				scopedSlots.checkbox = this.$scopedSlots['target-checkbox']
			}
			return h('div', {
				class: `${this.prefix}-transfer-list`,
				style: this.listStyle,
			}, [
				h('div', {class: `${this.prefix}-transfer-list-header`}, [
					h('fb-checkbox', {
						props: {
							disabled: this.myTargetList.length === 0,
							value: this.targetAllChecked,
						},
						on: {
							input (val) {
								vm.targetAllChecked = val
								vm.handelTargetAllCheck(val)
							},
						},
						scopedSlots: {
							label (props) {
								return vm.targetHasCheckedText
							},
						},
					}),
					h('span', {
						class: `${this.prefix}-transfer-list-header-selected`,
					}, [
						h('span', {
							class: `${this.prefix}-transfer-list-header-title`,
						}, [this.titles[1]]),
					]),
				]),
				h('div', {class: `${this.prefix}-transfer-list-body`}, [
					this.myTargetList.length === 0
						? h('fb-empty', {
							props: {
								text: this.targetEmptyText,
								type: this.targetEmptyType,
							},
						})
						: h('fb-checkbox-group', {
							props: {
								value: this.targetChecked,
								data: this.myTargetList,
								reader: this.reader,
								vertical: true,
							},
							class: `${this.prefix}-transfer-list-content`,
							on: {
								input: (v) => this.targetChecked = v,
							},
							scopedSlots,
						}),
				]),
				h('div', {class: `${this.prefix}-transfer-list-footer`}, []),
			])
		},
		getOperationRender (h) {
			let vm = this
			return h('div', {class: `${this.prefix}-transfer-operation`}, [
				this.showFastButtons ? h('fb-button', {
					props: {
						size: 's',
						icon: 'previous',
						disabled: this.myTargetList.length === 0,
						type: this.myTargetList.length > 0 ? 'primary' : '',
					},
					on: {
						'on-click' () {
							vm.handleTransAllToLeft()
						},
					},
				}, []) : null,
				h('fb-button', {
					props: {
						size: 's',
						icon: 'left',
						disabled: this.targetChecked.length === 0,
						type: this.targetChecked.length > 0 ? 'primary' : '',
					},
					on: {
						'on-click' () {
							vm.handleTransToLeft()
						},
					},
				}, []),
				h('fb-button', {
					props: {
						size: 's',
						icon: 'right',
						disabled: this.sourceChecked.length === 0,
						type: this.sourceChecked.length > 0 ? 'primary' : '',
					},
					on: {
						'on-click' () {
							vm.handleTransToRight()
						},
					},
				}, []),
				this.showFastButtons ? h('fb-button', {
					props: {
						size: 's',
						icon: 'next',
						disabled: this.mySourceList.length === 0,
						type: this.mySourceList.length > 0 ? 'primary' : '',
					},
					on: {
						'on-click' () {
							vm.handleTransAllToRight()
						},
					},
				}, []) : null,
			])
		},

		handleSourceStatusChange () {


			if (this.sourceChecked.length === 0 || this.mySourceList.length === 0) {
				this.sourceAllChecked = false
				return
			}

			this.sourceAllChecked =
				this.sourceChecked.length === this.mySourceList.length
					? true
					: 'indeterminate'

		},

		handelSourceAllCheck (checked, e) {
			this.sourceChecked = !checked ? [] : map(this.mySourceList, node => node[this.reader.value])
			this.handleStatusChange()
		},

		handleTransToRight () {

			let group = partition(this.mySourceList, (node) => {
				return includes(this.sourceChecked, node[this.reader.value])
			})
			this.myTargetList = this.myTargetList.concat(group[0])
			this.mySourceList = group[1]

			this.sourceChecked = []

			this.handleStatusChange()
		},

		handleTransAllToRight () {
			this.myTargetList = this.data
			this.mySourceList = []

			this.sourceChecked = []

			this.handleStatusChange()
		},

		handleTargetStatusChange () {
			if (this.targetChecked.length === 0 || this.myTargetList.length === 0) {
				this.targetAllChecked = false
				return
			}
			this.targetAllChecked =
				this.targetChecked.length === this.myTargetList.length
					? true
					: 'indeterminate'
		},

		handelTargetAllCheck (checked, e) {
			this.targetChecked = !checked ? [] : map(this.myTargetList, node => node[this.reader.value])
			this.handleStatusChange()
		},

		handleTransToLeft () {

			let group = partition(this.myTargetList, (node) => {
				return includes(this.targetChecked, node[this.reader.value])
			})
			this.mySourceList = this.mySourceList.concat(group[0])
			this.myTargetList = group[1]

			this.targetChecked = []

			this.handleStatusChange()
		},

		handleTransAllToLeft () {
			this.myTargetList = []
			this.mySourceList = this.data

			this.targetChecked = []

			this.handleStatusChange()
		},

		handleStatusChange () {

			this.handleSourceStatusChange()
			this.handleTargetStatusChange()

			this.$emit('on-change', this.myTargetKeys, this.myTargetList, this.mySourceKeys, this.mySourceList)
			this.$emit('input', this.myTargetList.map(n => n[this.reader.value]))
		},
	},

	mounted () {

	},
}
</script>

<style scoped>

</style>
