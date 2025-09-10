<template>
	<div :class="`${prefix}-color-predefine`">
		<div :class="`${prefix}-color-predefine__colors`">
			<div :class="[
			`${prefix}-color-predefine__color-selector`,
		  {selected: item.selected, 'is-alpha': item._alpha < 100}
      ]"
				 v-for="(item, index) in rgbaColors"
				 :key="index"
				 @click="handleSelect(index)">
				<div :style="{'background-color': item.value}"></div>
			</div>


<!--			<div :class="[-->
<!--			`${prefix}-color-predefine__color-selector`]">-->
<!--				<fb-icon name="add-normal" @on-click="addPredefineColor"></fb-icon>-->
<!--			</div>-->
		</div>
	</div>
</template>

<script>
import { prefix } from '../../../../../src/config'
import Color from '../color'
import { colors } from '../../../../theme/pc3/src/common/colors'
import FbIcon from '../../../icon/src/FbIcon.vue'

export default {
	name: 'Predefine',
	components: {FbIcon},
	props: {
		predefineColors: {
			type: Array,
			required: true,
			default: () => [],
		},
		color: {required: true},
		themePredefine: {type: Boolean},
	},
	data () {
		return {
			prefix,
			rgbaColors: [],
			themeDefine: [
				colors.red_5,
				colors.tomato_6,
				colors.carrot_6,
				colors.orange_6,
				colors.yellow_6,
				colors.lime_6,
				colors.green_6,
				colors.cyan_6,
				colors.blue_6,
				colors.ultramarine_6,
				colors.purple_6,
				colors.magenta_6,
				colors.grey_9,
				'#ffffff',
				'#000000',
			],
			defineColors: this.predefineColors, // 实际预选值 themeDefine 和 predefineColors 合集
		}
	},
	mounted () {
		this.init()
	},
	methods: {

		init () {
			if (this.themePredefine) {
				this.defineColors = this.themeDefine.concat(this.predefineColors)
			}
			this.rgbaColors = this.parseColors(this.defineColors, this.color)
		},
		handleSelect (index) {
			this.color.fromString(this.defineColors[index])
		},
		parseColors (colors, color) {
			return colors.map(value => {
				const c = new Color()
				c.enableAlpha = true
				c.format = 'rgba'
				c.fromString(value)
				c.selected = c.value === color.value
				return c
			})
		},

		addPredefineColor () {
			let fbPredefineColors = localStorage.getItem('fbPredefineColors') || "[]"
			// add this.color.value to  fbPredefineColors
			if (fbPredefineColors.indexOf(this.color.value) === -1) {
				fbPredefineColors = JSON.parse(fbPredefineColors)
				fbPredefineColors.push(this.color.value)
				localStorage.setItem('fbPredefineColors', JSON.stringify(fbPredefineColors))
				this.defineColors = this.themeDefine.concat(fbPredefineColors)
				this.rgbaColors = this.parseColors(this.defineColors, this.color)
			}



		},
	},
	watch: {
		'$parent.currentColor' (val) {
			const color = new Color()
			color.fromString(val)

			this.rgbaColors.forEach(item => {
				item.selected = color.compare(item)
			})
		},
		predefineColors (newVal) {
			if (this.themePredefine) {
				this.defineColors = this.themeDefine.concat(newVal)
			}
			this.rgbaColors = this.parseColors(this.defineColors, this.color)
		},
		color (newVal) {
			this.rgbaColors = this.parseColors(this.defineColors, newVal)
		},
	},
}
</script>
