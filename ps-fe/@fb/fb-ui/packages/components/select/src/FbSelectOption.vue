<template>
	<li v-show="show"
	    :class="getClass"
	    :data-key="value"
	    @mouseover.stop="handleFocus"
	    @mouseenter.stop="handleMouseenter"
	>
		<slot>
			<fb-icon size="s" v-if="icon" :name="icon"/>

			<!--			 显示多选图标 -->
			<template v-if="fbSelect.showMultipleIcon">
				<fb-icon size="s" v-if="selected" name="jpx-icon-selected-square-fill" color="#0284fe"/>
				<fb-icon size="s" v-if="!selected" name="jpx-icon-square" color="#0284fe"/>
			</template>

			{{ currentLabel }}
		</slot>
	</li>
</template>

<script>
/**
 * FbSelectOption
 * (c) 2020 lincong1987
 */

import { closest } from '../../../utils/componentUtils'
import { isArray } from 'lodash-es'
import { prefix } from '../../../../src/config'
import FbIcon from '../../icon/src/FbIcon'

export default {
	name: 'FbSelectOption',
	components: {
		FbIcon,
	},
	props: {

		parentId: {
			type: String,
			default: '',
		},
		parent: {},

		// 值绑定
		value: {
			type: [String, Number, Boolean],
			default: undefined,
		}, // 显示文字
		label: {
			type: [String, Number, Boolean],
			default: undefined,
		},
		icon: {
			type: [String, Number, Boolean],
			default: undefined,
		}, // 禁用
		disabled: {
			type: Boolean,
			default: false,
		},
		format: {
			type: Function,
			default: null,
		},
	},

	data () {
		return {
			prefix,
			currentLabel: this.label,

			isFocus: false,
			myFormat: this.format || ((label) => {
				return label
			}),
		}
	},
	computed: {

		getClass () {

			let arr = [`${this.prefix}-select-option`]
			if (this.selected) {
				arr.push(`${this.prefix}-select-option--selected`)
			}
			if (this.disabled || (this.fbSelect.disableLimit && !this.selected)) {
				arr.push(`${this.prefix}-select-option--disabled`)
			}

			if (this.isFocus) {
				arr.push(`${this.prefix}-select-option--focus`)
			}

			return arr
		},
		selected () {
			if (this.fbSelect.multiple) {
				return isArray(this.fbSelect.currentValue) && this.fbSelect.currentValue.indexOf(this.value) != -1
			}
			return this.fbSelect.currentValue == this.value
		},
		show () {
			if (this.fbSelect.searchValue && this.currentLabel) {
				return this.currentLabel.indexOf(this.fbSelect.searchValue) != -1
			}
			return true
		},
	},
	created () {

		if (this.parentId) {
			let $select = document.getElementById(this.parentId)// closest(this, 'FbSelect')
			if ($select) {
				this.fbSelect = $select.__vue__
			}
		}

		if (this.parent) {
			this.fbSelect = this.parent
		}

		if (this.$options
			&& this.$options.parent
			&& this.$options.parent.$vnode
			&& this.$options.parent.$vnode.context
			&& this.$options.parent.$vnode.context.$options.name === 'FbSelect') {

			this.fbSelect = this.$options.parent.$vnode.context
		}

		if (this.fbSelect) {
			this.fbSelect.addOption(this)
		}
	},
	mounted () {
		this.currentLabel = this.label || this.value
	},
	updated () {
		this.currentLabel = this.label || this.value
	},
	beforeDestroy () {
		if (this.fbSelect) {
			this.fbSelect.removeOption(this)
		}

		this.fbSelect = null
	},
	methods: {
		handleMouseenter (e) {
			if (e.target.clientWidth < e.target.scrollWidth) {
				// 因为 currentLabel 可能不是文本，需要加一层判断
				if (typeof this.currentLabel === 'string') {
					e.target.setAttribute('title', this.currentLabel.trim())
				}
			} else {
				e.target.setAttribute('title', '')
			}
		},
		handleFocus () {
			if (this.fbSelect.focusOption) {
				this.fbSelect.focusOption.isFocus = false
			}
			this.isFocus = true
			this.fbSelect.focusOption = this
		},
	},

}
</script>

<style lang="less" scoped>

</style>
