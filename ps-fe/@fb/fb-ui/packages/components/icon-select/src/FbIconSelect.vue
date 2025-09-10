<template>
	<div v-clickoutside="outside" :class="{
		[`${prefix}-icon-select`]:true,
		[`${prefix}-icon-select--no-label`]: !label,
		[`${prefix}-icon-select--${size}`]: !!size,
	}">
		<fb-popup-header
			:clearable="false"
			:disabled="disabled"
			:readonly="readonly"
			@on-click="showPopup"
			:value="true"
		>
			<fb-icon v-if="myValue" :name="myValue" :size="size" :class="`${prefix}-icon-select__icon-preview`"/>
			<fb-text :size="size" :class="`${prefix}-icon-select__label`">{{ label }}</fb-text>
		</fb-popup-header>

		<fb-popup-picker :position="position" v-model="show">

			<div :class="`${prefix}-icon-select__search`">
				<fb-input prepend-icon="search" placeholder="输入中文或英文名进行搜索" @on-change="debounceSearchIcon" clearable/>
			</div>
			<div v-if="show" :class="`${prefix}-icon-select__list`">
				<div v-if="empty" :class="`${prefix}-icon-select__empty`">
					<fb-empty size="s" text="没有查询到图标"></fb-empty>
				</div>
				<fb-container v-for="(icons, name) in types" :key="name">

					<fb-container :class="`${prefix}-icon-select__list-header`">
						<fb-text size="m">{{ `${name} (${icons.length})` }}</fb-text>
					</fb-container>

					<ul>
						<li v-for="(icon, j) in icons" @click="selectIcon(icon)"
						>

							<fb-tooltip :content="`${icon.font_class === myValue ?'当前选择：':''} ${icon.name}`">
								<fb-button :icon="icon.font_class"
								           :type="icon.font_class === myValue ? 'primary' :''"
								></fb-button>
							</fb-tooltip>

							<!--							<fb-icon :name="icon.font_class"-->
							<!--									 size="32"-->
							<!--							>-->
							<!--							</fb-icon>-->
							<!--							<fb-text class="name">{{ icon.name }}</fb-text>-->
							<!--							<fb-text class="cls">{{ icon.font_class }}</fb-text>-->
						</li>
					</ul>

				</fb-container>
			</div>

		</fb-popup-picker>
	</div>
</template>


<script>

import { prefix } from '../../../../src/config'
import clickoutside from '../../../directives/clickoutside'
import { closest } from '../../../utils/componentUtils'
import FbPopupHeader from '../../../components/popup/popup-header/src/FbPopupHeader'
import FbPopupPicker from '../../../components/popup/popup-picker/src/FbPopupPicker'
import FbTooltip from '../../../components/tooltip/src/tooltip'
import { glyphs } from '../../../theme/pc3/src/fonts/iconfont.json'
import { debounce, each } from 'lodash-es'
import FbButton from '../../../components/button/src/FbButton'
import FbContainer from '../../../components/container/src/FbContainer'
import FbInput from '../../../components/input/src/FbInput'
import FbText from '../../../components/text/src/FbText'
import FbIcon from '../../../components/icon/src/FbIcon'
import FbEmpty from '../../../components/empty/src/FbEmpty'

export default {
	name: 'FbIconSelect',
	components: {
		FbEmpty,
		FbIcon,
		FbText,
		FbInput,
		FbContainer,
		FbButton,
		FbPopupPicker,
		FbPopupHeader,
		FbTooltip
	},
	directives: {
		clickoutside,
	},
	props: {
		value: {
			type: String,
			default: undefined,
		},
		label: {
			type: String,
			default: '图标选择',
		},
		placeholder: {
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
		position: {
			type: String,
			default: 'bottomLeft',
		},
		size: {
			type: String,
			default: 'm',
		},
	},

	data () {

		let types = {}
//
		each(glyphs, (icon, i) => {

			let typeName = icon.name.split('-')[0]

			if (!types[typeName]) {
				types[typeName] = []
			}

			types[typeName].push(icon)

		})

		return {
			prefix,
			show: false,
			types,
			glyphs,
			empty: false,
			myValue: this.value,
		}
	},
	watch: {
		value (val) {
			this.myValue = val
		},
		myValue (val) {
			this.$emit('input', val)
			this.$emit('on-change', val)

			this.fbFormItem && this.fbFormItem.$emit('on-form-change', val)
		},
	},

	created () {

		this.fbFormItem = closest(this, 'FbFormItem')
		this.debounceSearchIcon = debounce(this.searchIcon, 320)
	},

	beforeDestroy () {
		this.fbFormItem = null
		this.debounceSearchIcon.cancel()
	},

	methods: {
		outside () {
			if (this.show) {
				this.show = false
				this.fbFormItem && this.fbFormItem.$emit('on-form-blur', this.myValue)
			}
		},
		showPopup () {
			if (this.readonly || this.disabled) return
			this.show = true
		},
		selectBlur () {
			this.outside()
		},
		clear () {
			this.myValue = ''
		},

		selectIcon (icon) {
			this.myValue = icon.font_class
		},

		searchIcon (value) {

			let icons = glyphs.filter(n => {
				return n.name.includes(value.trim()) || n.font_class.includes(value.trim())
			})

			let types = {}
//

			this.empty = icons.length === 0

			each(icons || [], (icon, i) => {

				let typeName = icon.name.split('-')[0]

				if (!types[typeName]) {
					types[typeName] = []
				}

				types[typeName].push(icon)
				//// console.log(icon.name.split('-')[0])
				// types[icon.name.split('-')[0]].push(icon)

			})

			this.types = types

		},
	},

}

</script>
