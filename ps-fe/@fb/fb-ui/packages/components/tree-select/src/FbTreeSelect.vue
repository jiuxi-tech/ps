<template>
	<div ref="reference" v-clickoutside="hidePopup"
		 :class="{
			[`${prefix}-tree-select`]: true,
            [`${prefix}-tree-select--visible`]: showDropdown,
            [`${prefix}-tree-select--disabled`]: disabled,
            [`${prefix}-tree-select--readonly`]: readonly,
            [`${prefix}-tree-select--multiple`]: multiple
        }"
	>
		<div
			ref="selection"
			@click.stop="toggle"
			@mouseenter="focus"
			@mouseleave="blur"
			@focus="focus"
			@blur="blur"
			:class="`${prefix}-tree-select__selection`"

			:tabindex="getTabindex"
		>
			<template v-if="$scopedSlots.header">
					<span :class="getSelectionTextClass">
						<slot name="header" :currentNodes="currentNodes" :multiple="multiple"
							  :placeholder="placeholder"></slot>
					</span>
			</template>
			<template v-else>

				<template v-if="multiple">
               	 		<span v-if="currentNodes.length === 0"
							  :class="`${prefix}-tree-select__placeholder`"
						>
							{{ placeholder }}
						</span>
					<template v-else-if="collapseTags">
						<div :class="`${prefix}-tree-select--multiple--selected`">
							<span v-if="currentNodes.length > 1"
								  :class="`${prefix}-tree-select__selected-value`" style="padding-right: 10px">
								已选：{{ currentNodes.length }}
							</span>
							<span
								:title="currentNodes[0].label"
								:class="`${prefix}-tree-select__selected-value`"
							>
								{{ currentNodes[0].label }}
								<fb-icon
									@on-click="removeSelectedOption(currentNodes[0])"
									name="close"
									size="12"
									:class="`${prefix}-tree-select__selected-value__clear`"
								/>
							</span>
<!--							<span v-if="currentNodes.length > 1"-->
<!--								  :class="`${prefix}-tree-select__selected-value`" style="padding-right: 10px">-->
<!--								+ {{ currentNodes.length - 1 }}...-->
<!--							</span>-->
						</div>
					</template>
					<div v-else :class="`${prefix}-tree-select--multiple--selected`">

	                        <span
								v-for="(node, index) in currentNodes"
								:key="index"
								:title="node.label"
								:class="`${prefix}-tree-select__selected-value`"
							>
	                            {{ myHeaderFormat(node) }}
								<fb-icon
									@on-click="removeSelectedOption(node)"
									name="close"
									size="12"
									:class="`${prefix}-tree-select__selected-value__clear`"
								/>
	                        </span>
					</div>
				</template>

				<template v-else>
						<span :class="getSelectionTextClass">
							{{ myHeaderFormat(currentNodes) || placeholder }}
						</span>
				</template>

			</template>

			<div :class="`${prefix}-tree-select__selection-icons`">
				<fb-icon v-show="!showClear"
						 :class="`${prefix}-tree-select__selection-icons__down`"
						 :name="iconName"/>
				<fb-icon
					v-if="clearable"
					v-show="showClear"
					@on-click="clear"
					:class="`${prefix}-tree-select__selection-icons__clear`"
					name="close-circle-fill"
				/>
			</div>

		</div>

		<transition name="slide-to-down">
			<div ref="popper" v-show="showDropdown" :class="[`${prefix}-popper-no-arrow`, popperClass]">
				<div :class="[`${prefix}-tree-select__dropdown`]"
					 :style="{
					'max-height' : getSizeStyle(maxHeight),
					...listStyle
				}">
					<fb-container v-if="filterable" padding="0 8px 4px 8px">
						<fb-input v-model="filterText" :placeholder="filterPlaceholder" size="s"
								  @input="(e)=>{doFilterNode(e)}"
						></fb-input>
					</fb-container>
					<fb-tree
						ref="tree"
						:data="data"
						:service="service"
						:url="url"
						:param="param"
						:multiple="multiple"
						:show-icon="showIcon"
						:show-title="showTitle"
						:twice-click-selected="twiceClickSelected"
						:reader="reader"
						:data-filter="dataFilter"
						:load-data="loadData"
						:do-check="doCheck"
						:do-un-check="doUnCheck"
						:no-half="noHalf"
						:only-leaf="onlyLeaf"
						:leaf-name="leafName"

						@on-select-change="handleSelectChange"
						@on-check-change="handleCheckChange"
						@on-data-load="handleDataLoad"
						@on-data-update="handleDataUpdate"

						:filter-node-method="filterNodeMethod"

						:scopedSlots="$scopedSlots"
					>
					</fb-tree>
				</div>
			</div>
		</transition>

	</div>
</template>

<script>


import clickoutside from '../../../utils/clickoutside'
import { closest } from '../../../utils/componentUtils'
import { isArray } from 'lodash-es'
import { isArrayEqual } from '../../../utils/utils'
import FbIcon from '../../icon/src/FbIcon'
import FbTree from '../../tree/src/FbTree'
import FbPopupPicker from '../../popup/popup-picker/src/FbPopupPicker'
import FbPopupHeader from '../../popup/popup-header/src/FbPopupHeader'
import FbLoading from '../../loading/src/FbLoading'
import FbTags from '../../tag/src/FbTags'
import { getSizeStyle } from '../../../utils/propUtils'
import { prefix } from '../../../../src/config'
import PopperMixin from '../../../utils/popper-mixin'
import FbContainer from '../../container/src/FbContainer.vue'
import FbInput from '../../input/src/FbInput.vue'

/**
 * FbTreeSelect
 * (c) 2020 lincong1987
 */
export default {

	name: 'FbTreeSelect',
	components: {
		FbInput,
		FbContainer,
		FbTags,
		FbLoading,
		FbPopupHeader,
		FbPopupPicker,
		FbTree,
		FbIcon,
	},
	directives: {
		clickoutside,
	},

	mixins: [PopperMixin],

	props: {

		//
		value: {
			type: [Array, Number, String],
			default: null,
		},

		placeholder: {
			type: String,
			default: '',
		},

		disabled: {
			type: Boolean,
			default: false,
		},

		readonly: {
			type: Boolean,
			default: false,
		},

		position: {
			type: String,
			default: null,
		},

		// 下拉的最大高度
		maxHeight: {
			type: [String, Number],
			default: '320',
		},

		showConfirm: {
			type: Boolean,
			default: false,
		},

		clearable: {
			type: Boolean,
			default: true,
		},

		data: {
			type: Array,
			default () {
				return []
			},
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

		// 显示图标
		showIcon: {
			type: Boolean,
			default: true,
		},

		// 显示 tooltip
		showTitle: {
			type: Boolean,
			default: true,
		},

		inline: {
			type: Boolean,
			default: false,
		},

		// 多选
		multiple: {
			type: Boolean,
			default: false,
		},

		url: {
			type: String,
			default: undefined,
		},

		// 服务获取节点数据
		service: {
			type: [Object, Array, Function],
			default: undefined,
		},
		// 查询参数
		param: {
			type: [Object, Array, Function],
			default: undefined,
		},
		// 网络数据过滤器
		dataFilter: {
			type: Function,
			default (data) {
				return data
			},
		},

		loadData: {
			type: Function,
			default: null,
		},

		// 二次点击时，是否继续保持选中状态
		twiceClickSelected: {
			type: Boolean,
			default: true,
		},

		// 头部格式化
		headerFormat: {
			type: Function,
			default: undefined,
		},
		// 关于 node.childrenCheckedStatus
		//  "none" : 子节点选中数量为0
		//  "half" : 子节点选中数量大于0且小于子节点总数
		//  "all" : 子节点全部选中

		// checkbox 被勾选后的情况
		// "p" 表示操作会影响父级节点；
		// "s" 表示操作会影响子级节点。
		// 请注意大小写，不要改变
		doCheck: {
			type: String,
			default: 'ps',
		},

		// checkbox 取消勾选后的情况
		// "p" 表示操作会影响父级节点；
		// "s" 表示操作会影响子级节点。
		// 请注意大小写，不要改变
		doUnCheck: {
			type: String,
			default: 'ps',
		},

		// 只有叶子结点的选择模式
		onlyLeaf: {
			type: Boolean,
			default: false,
		},
		// 叶子结点 字段名称，根据数据字段判断是否可以选中
		leafName: {
			type: String,
			default: '',
		},

		// 去除半选的选择模式
		noHalf: {
			type: Boolean,
			default: false,
		},

		// 下拉样式
		listStyle: {
			type: Object,
			default () {
				return {}
			},
		},

		// 当当前选中值发生变化后，隐藏选择框
		hidePickerAfterChange: {
			type: Boolean,
			default: true,
		},
		// 合并 tag +num 形式
		collapseTags: {
			type: Boolean,
			default: false,
		},

		// 搜索框
		filterable: {
			type: Boolean,
			default: false,
		},

		filterPlaceholder: {
			type: String,
			default: '输入标题进行过滤',
		},

		/**
		 * 过滤模式
		 * keydown：keydown 一次查一次，中间 delay 800ms
		 * button: 点击搜索按钮一次查一次
		 * 3、
		 */
		/*	filterMode: {
		 type: [Boolean, String],
		 default: 'keydown',
		 },*/

		// 过滤回调函数
		filterNodeMethod: {
			type: Function,
			default: (value, data) => {
				return !value ? true : data.label.indexOf(value) !== -1
			},
		},

		// 下拉icon图标
		iconName: {
			type: String,
			default: 'down',
		},
		popperClass: String,
	},

	data () {

		// 是否是远程
		let isRemote = !!(this.service || this.url)

		// currentValue = ["1", "2", "3"];
		// currentValue = "666"
		// currentValue = null
		// currentValue = ""
		let currentValue = this.value
		if (this.multiple) {
			if (!isArray(currentValue)) {
				if (currentValue != null) {
					currentValue = [currentValue]
				} else {
					currentValue = []
				}
			}
		} else {
			if (isArray(currentValue) && currentValue.length > 0) {
				currentValue = currentValue[0]
			}
		}

		return {
			prefix,

			filterText: '',

			currentValue: currentValue,
			currentNodes: [],
			showClear: false,
			showDropdown: false,
			showLoading: false,
			showEmpty: false,
			myPager: {},
			myQueryParam: this.param,
			myHeaderFormat: this.headerFormat || function (node) {
				return node && node.label
			},
			isRemote,
			isLoaded: false,

			tempValue: currentValue,
		}
	},

	created () {
		this.fbFormItem = closest(this, 'FbFormItem')
	},
	beforeDestroy () {
		this.fbFormItem = null
	},
	computed: {
		// tab 键
		getTabindex () {
			if (this.disabled || this.readonly) {
				return -1
			}
			return 0
		},

		getSelectionTextClass () {
			if (this.multiple) {
				if (this.currentNodes && this.currentNodes.length > 0) {
					return `${prefix}-tree-select__selected-value`
				} else {
					return `${prefix}-tree-select__placeholder`
				}
			} else {
				if (this.myHeaderFormat(this.currentNodes)) {
					return `${prefix}-tree-select__selected-value`
				} else {
					return `${prefix}-tree-select__placeholder`
				}
			}

//			if (this.value == '') {
//				return `${prefix}-tree-select__placeholder`
//			}
//

//
//			if (this.currentNodes) {
//				return `${prefix}-tree-select__selected-value`
//			}
//			return `${prefix}-tree-select__placeholder`
		},
	},
	watch: {

		// value 需要与 currentNodes 同步
		value (val, old) {

			// console.log(`value val: ${val} old: ${old}`)

			if (this.isRemote && !this.isLoaded) {
				this.tempValue = this.currentValue = val
				// console.log(`下拉树节点未加载完毕, 进行临时存储 val: ${val}, currentValue: ${this.currentValue}`)
				return
			}

			if (this.multiple) {

				// 如果传入的值与当前值一致
				if (isArrayEqual(this.currentValue, val)) {
					return
				}

				// clear nodes check status
				this.$refs.tree.updateNodesByValue(this.currentValue, {checked: false})

				this.$nextTick(() => {
					this.currentValue = val
					this.$refs.tree.updateNodesByValue(this.currentValue, {checked: true})
					this.currentNodes = this.$refs.tree.getNodesByValue(this.currentValue)
				})

			} else {

				if (isArray(this.currentValue) && this.currentValue.length > 0) {
					this.currentValue = this.currentValue[0]
				}
				if (isArray(val) && val.length > 0) {
					val = val[0]
				}

				if (this.currentValue === val) {
					return
				}

				this.$refs.tree.updateNodesByValue(this.currentValue, {selected: false})

				this.$nextTick(() => {
					this.currentValue = val
					this.$refs.tree.updateNodesByValue(this.currentValue, {selected: true})
					let currentNodes = this.$refs.tree.getNodesByValue(this.currentValue)
					if (currentNodes.length > 0) {
						this.currentNodes = currentNodes[0]
					} else {
						this.currentNodes = null
					}
				})

			}
		},

		isLoaded (val) {
			if (val === true) {
				if (!this.multiple) {
					this.$emit('input', '')
				} else {
					this.$emit('input', [])
				}

				this.$nextTick(() => {
					this.$emit('input', this.tempValue)
				})
			}
		},

		currentValue (val, old) {
			// console.log(`currentValue: ${val}`)
		},

	},
	methods: {

		doFilterNode () {
			this.$refs.tree.filter(this.filterText)
		},

		getSizeStyle,

		hidePopup () {
			if (this.showDropdown) {
				this.fbFormItem && this.fbFormItem.$emit('on-form-blur', this.currentValue)
			}
			this.$nextTick(() => {
				this.showDropdown = false
			})
		},

		showPopup () {

			if (!(this.readonly || this.disabled)) {
				this.showDropdown = true
			}
		},

		blur () {
			this.$emit('on-blur')
			if (!this.disabled && !this.readonly && this.clearable) {
				this.showClear = false
			}
		},

		focus () {
			this.$emit('on-focus')

			if (!this.disabled && !this.readonly && this.clearable &&
				((this.multiple && this.currentNodes.length > 0) || (!this.multiple && this.currentValue != ''))) {
				this.showClear = true
			}
		},

		toggle () {
			this.$emit('on-click')
			if (this.disabled || this.readonly) return
			this.showDropdown = !this.showDropdown
			this.updatePosition()
			this.updatePopper()
		},

		updatePosition () {
			this.$nextTick(function () {
				let elStyle = this.$el.getBoundingClientRect()
				// this.$refs.list.style.width = `${elStyle.width}px`
				this.$refs.popper.style.width = `${elStyle.width}px`
			})
		},

		clear (e) {

			if (this.disabled || this.readonly) return

			this.showClear = false
			if (!this.multiple) {
				this.currentValue = ''
			} else {
				this.$refs.tree.checkNodesByValue(this.currentValue, false)
				this.currentValue = []
			}
			this.currentNodes = []
			this.finished()
			this.changeCurrentValue(e)

		},

		close () {
			if (this.showDropdown) {
				this.finished()
				this.fbFormItem && this.fbFormItem.$emit('on-form-blur', this.currentValue)
			}
			this.showDropdown = false
		},

		finished () {
			this.$emit('on-finished', this.currentValue)
		},

		loading (status) {
			this.showLoading = status
		},

		handleSelectChange (node, e) {

			this.currentNodes = node

			if (node.value === this.currentValue) {
				return
			}

			this.currentValue = node.value

			this.changeCurrentValue(e)
			if (this.hidePickerAfterChange === true) {
				this.hidePopup()
			}
			this.$emit('on-select-change', node, e)

		},

		handleCheckChange (nodes, e) {

			this.currentNodes = nodes.filter(item => item.checked)

			let _values = this.currentNodes.map((n) => n.value)
			if (isArrayEqual(_values, this.currentValue)) {
				return
			}

			this.currentValue = _values

			this.changeCurrentValue(e)
			this.$emit('on-check-change', nodes, e)
		},

		handleLoadData (data) {
			this.$emit('on-load-data', data)
			this.showDropdown = true
		},

		handleDataLoad (data) {
			this.$emit('on-data-load', data)
		},

		handleDataUpdate () {
			// console.log('树节点加载完毕', `${this.tempValue}`)
			this.isLoaded = true
			//this.currentValue = this.tempValue
			this.$emit('on-data-update')
		},

		changeCurrentValue (e) {

			this.updatePosition()

			this.$emit('input', this.currentValue, this.currentNodes, e)
			this.$emit('change', this.currentValue, this.currentNodes, e)
			this.$emit('on-change', this.currentValue, this.currentNodes, e)
			this.formItem && this.formItem.$emit('on-form-change', this.currentValue, this.currentNodes)

		},

		updateTreeNodes () {
			this.$nextTick(() => {
				if (this.multiple) {
					this.checkNodesByValue(this.currentValue)
				} else {
					this.selectNodeByValue(this.currentValue)
				}
			})

		},

		selectNodeByValue (value) {
			this.$refs.tree.selectNodeByValue(value, true)
		},

		checkNodesByValue (value) {
			this.$refs.tree.checkNodesByValue(value, true)
		},
		removeSelectedOption (node) {
			if (this.disabled || this.readonly) return
			const index = this.currentValue.indexOf(node.value)
			if (index != -1) {
				this.$refs.tree.checkNodeByValue(this.currentValue[index], false)
				this.currentValue.splice(index, 1)
				this.currentNodes.splice(index, 1)
			}
			// this.finished()
			this.changeCurrentValue()
		},
		calcCollapseTitle (currentNodes) {
			let arr = currentNodes.slice(1)
			let newArr = arr.map(item => item && item.label)
			return newArr.join(',')
		},
	},
}

</script>


<style scoped>

</style>
