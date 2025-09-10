<template>
	<div
		:class="{
			[`${prefix}-popup-select`]: true,
            [`${prefix}-popup-select--visible`]: myShow,
            [`${prefix}-popup-select--disabled`]: disabled,
            [`${prefix}-popup-select--multiple`]: multiple
        }"
	>
		<slot name="header"></slot>
		<fb-popup-picker v-model="myShow">
			<slot name="picker"></slot>
		</fb-popup-picker>
	</div>
</template>

<script>

import clickoutside from '../../../../directives/clickoutside'
import FbPopupPicker from '../../popup-picker/src/FbPopupPicker'
import { getSizeStyle } from '../../../../utils/propUtils'
import { prefix } from '../../../../../src/config'

/**
 * FbPopupSelect
 * (c) 2020 lincong1987
 */
export default {

	name: 'FbPopupSelect',
	components: {
		FbPopupPicker,
	},
	directives: {
		clickoutside,
	},

	props: {
		value: {
			type: Boolean,
			default: false,
		},

		disabled: {
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

		// 多选
		multiple: {
			type: Boolean,
			default: false,
		},

		show: {
			type: Boolean,
			default: false,
		},
	},

	data () {

		return {
			prefix,
			myShow: this.value,
		}
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
			this.myShow = val

		},

		myShow () {
			this.$emit('input', this.myShow)
		},
	},
	methods: {

		getSizeStyle,

		hidePopup () {
			this.myShow = false
		},

		showPopup () {

			if (!this.disabled) {
				this.myShow = true
			}
		},

		blur () {
			this.$emit('on-blur')
		},

		focus () {
			this.$emit('on-focus')
		},

		toggle () {
			this.$emit('on-click')
			if (this.disabled) {
				return
			}
			this.myShow = !this.myShow
			this.updatePosition()
		},

		updatePosition () {
//			this.$nextTick(function () {
//				let elStyle = this.$el.getBoundingClientRect()
//				this.$refs.list.style.top = `${elStyle.height}px`// '32px'
//			})
		},

		close () {
			this.myShow = false
		},

	},

}

</script>


<style scoped>

</style>
