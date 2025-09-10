<template>
    <span
        :class="[
	    	`${prefix}-popup-header`,
	    	{
	    		[`${prefix}-popup-header--no-icon`]: !icon && !showClearIcon,
	    		[`${prefix}-popup-header--multiple`]: multiple,
	    	    [`${prefix}-popup-header--readonly`]: readonly,
	    	    [`${prefix}-popup-header--disabled`]: disabled,
	        }
	    ]"
        :tabindex="tabindex"
        @mouseenter="showClear = true"
        @mouseleave="showClear = false"
        @click="$emit('on-click')"
        @focus="$emit('on-focus', $event)"
        @blur="$emit('on-blur', $event)"
        @keydown="$emit('on-keydown', $event)"
    >
        <span :class="[
        	`${prefix}-popup-header__content`,
            {
            	[`${prefix}-popup-header__content--placeholder`]: !value
            }
        ]">
            <slot>{{ value || placeholder }}</slot>
        </span>
        <fb-icon v-if="icon" v-show="!showClearIcon" @on-click.stop="$emit('on-icon-click')" :name="icon"/>
        <fb-icon v-show="showClearIcon" @on-click.stop="$emit('on-clear')" name="close-circle"/>
    </span>
</template>

<script>
/**
 * FbPopupHeader
 * (c) 2020 lincong1987
 */
import FbIcon from '../../../icon/src/FbIcon'
import { prefix } from '../../../../../src/config'

export default {
    name: 'FbPopupHeader',
    components: {FbIcon},
    props: {
        value: {
            type: [String, Number, Array, Boolean],
            default: '',
        },
        placeholder: {
            type: [String, Number],
            default: '',
        },
        icon: {
            type: String,
            default: '',
        },
        disabled: {
            type: Boolean,
            default: false,
        },
        clearable: {
            type: Boolean,
            default: false,
        },
        readonly: {
            type: Boolean,
            default: false,
        },
        multiple: {
            type: Boolean,
            default: false,
        },
    },
    data () {
        return {
            prefix,
            showClear: false,
        }
    },
    computed: {
        showClearIcon () {
            return this.clearable && !this.disabled && !this.readonly && this.value && this.showClear
        },
        tabindex () {
            if (this.disabled || this.readonly) {
                return -1
            }
            return 0
        },
    },
}
</script>

<style scoped>

</style>
