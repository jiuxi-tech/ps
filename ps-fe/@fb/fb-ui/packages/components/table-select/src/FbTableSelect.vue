<template>
	<div v-clickoutside="close"
		 :class="{
			[`${prefix}-table-select`]: true,
            [`${prefix}-table-select--visible`]: showList,
            [`${prefix}-table-select--disabled`]: disabled,
            [`${prefix}-table-select--readonly`]: readonly,
        }"
	>
		<div
			ref="selection"
			@click="toggle"
			@mouseenter="focus"
			@mouseleave="blur"
			@focus="focus"
			@blur="blur"
			:class="`${prefix}-table-select__selection`"
		>
			<span
				v-if="currentValue.length === 0"
				:class="`${prefix}-table-select__placeholder`"
			>
                    {{ placeholder }}
                </span>
			<template v-else>
				<div :class="`${prefix}-table-select--multiple--selected`">
						<span :class="`${prefix}-table-select__selected-value`">
							已选 {{ currentValue.length }} 项
						</span>
				</div>
			</template>

			<div :class="`${prefix}-table-select__selection-icons`">
				<fb-icon v-show="!showClear"
						 :class="`${prefix}-table-select__selection-icons__down`"
						 name="down"/>
				<fb-icon
					v-if="clearable"
					v-show="showClear"
					@on-click="clear"
					:class="`${prefix}-table-select__selection-icons__clear`"
					name="close-circle-fill"
				/>
			</div>

		</div>

		<transition name="slide-to-down">
			<div
				ref="list"
				v-if="showList"
				:class="[
					`${prefix}-table-select__dropdown`
				]"
				:style="{
					'max-height' : getSizeStyle(maxHeight),
					'min-width': getSizeStyle(tableWidth)
				}"
			>
				<fb-simple-table
					ref="table"
					v-bind="$attrs"
					v-model="currentValue"
					@input="$emit('input', currentValue)"
					:scroll="{fillY: true, y: 200}"
				>
				</fb-simple-table>
			</div>
		</transition>

	</div>
</template>

<script>


import clickoutside from '../../../directives/clickoutside'
import { closest } from '../../../utils/componentUtils'
import FbIcon from '../../icon/src/FbIcon'
import FbPopupPicker from '../../popup/popup-picker/src/FbPopupPicker'
import FbPopupHeader from '../../popup/popup-header/src/FbPopupHeader'
import FbLoading from '../../loading/src/FbLoading'
import FbTags from '../../tag/src/FbTags'
import { getSizeStyle } from '../../../utils/propUtils'
import { prefix } from '../../../../src/config'
import FbSimpleTable from '../../../components/table/src/FbSimpleTable'

/**
 * FbTableSelect
 * (c) 2020 lincong1987
 */
export default {

	name: 'FbTableSelect',
	components: {
		FbSimpleTable,
		FbTags,
		FbLoading,
		FbPopupHeader,
		FbPopupPicker,
		FbIcon,
	},
	directives: {
		clickoutside,
	},

	props: {

		//
		value: {
			type: [Array],
			default: () => {
				return []
			},
		},

		// 边框
		bordered: {
			type: Boolean,
			default: true,
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

		tableWidth: {
			type: [String, Number],
			default: '320',
		},

		showConfirm: {
			type: Boolean,
			default: false,
		},

		clearable: {
			type: Boolean,
			default: false,
		},

		placeholder: {
			type: String,
			default: '',
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


		// 头部格式化
		headerFormat: {
			type: Function,
			default: undefined,
		},


	},

	data () {

		return {
			prefix,
			tableCurrentValue: [],
			currentValue: this.value,
			showClear: false,
			showList: false,
			showLoading: false,
			showEmpty: false,
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
	},
	watch: {

		value (val, old) {
			this.currentValue = val
		},

		currentValue (val) {
			this.changeCurrentValue()
		},

	},
	methods: {

		getSizeStyle,

		hidePopup () {

			if (this.showList) {
				this.fbFormItem && this.fbFormItem.$emit('on-form-blur', this.value)
			}

			this.showList = false
		},

		showPopup () {

			if (!(this.readonly || this.disabled)) {
				this.showList = true
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

			if (!this.disabled && !this.readonly && this.clearable && this.currentValue.length > 0) {
				this.showClear = true
			}
		},

		toggle () {
			this.$emit('on-click')
			if (this.disabled || this.readonly) return
			this.showList = !this.showList
			this.updatePosition()
		},

		updatePosition () {
			this.$nextTick(() => {
				let elStyle = this.$el.getBoundingClientRect()
				this.$refs.list.style.top = `${elStyle.height}px`// '32px'
			})
		},

		clear () {
			if (this.disabled || this.readonly) return
			this.showClear = false
			this.currentValue = []
			this.finished()
			this.changeCurrentValue()
		},

		close () {
			if (this.showList) {
				this.finished()
				this.fbFormItem && this.fbFormItem.$emit('on-form-blur', this.currentValue)
			}
			this.showList = false
		},

		finished () {
			this.$emit('on-finished', this.currentValue)
		},

		changeCurrentValue () {

			this.updatePosition()

			this.$emit('input', this.currentValue)
			this.$emit('change', this.currentValue)
			this.$emit('on-change', this.currentValue)
			this.formItem && this.formItem.$emit('on-form-change', [this.currentValue])

		},

	},

}

</script>


<style scoped>

</style>
