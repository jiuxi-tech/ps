<template>
	<transition name="slide-to-down" @after-leave="doDestroy">
		<div
			:class="`${prefix}-color-dropdown`"
			v-show="showPopper">
			<div :class="`${prefix}-color-dropdown__main-wrapper`">
				<hue-slider ref="hue" :color="color" vertical style="float: right;"></hue-slider>
				<sv-panel ref="sv" :color="color"></sv-panel>
			</div>
			<alpha-slider v-if="alpha" ref="alpha" :color="color"></alpha-slider>
			<predefine v-if="predefine || themePredefine" :color="color" :predefine-colors="predefine"
					   :themePredefine="themePredefine"></predefine>
			<div :class="`${prefix}-color-dropdown__btns`">
        <span :class="`${prefix}-color-dropdown__value`" >
          <fb-input
			  v-model="customInput"
			  @keyup.native.on-enter="handleConfirm"
			  @on-blur="handleConfirm"
			  size="m">
          </fb-input>

<!--			 <fb-flex width="100px">-->
<!--				 <fb-input-number-->
<!--					 step-strictly-->
<!--					 v-if="alpha"-->
<!--					 v-model="alphaInput"-->
<!--					 :max="100"-->
<!--					 :min="0"-->
<!--					 :step="1"-->
<!--					 size="m">-->
<!--          </fb-input-number>-->
<!--			 </fb-flex>-->

        </span>
				<fb-button
					size="m"
					:class="`${prefix}-color-dropdown__link-btn`"
					@on-click="handleClear">
					清空
				</fb-button>
				<fb-button
					v-if="needConfirm"
					priamry
					size="m"
					:class="`${prefix}-color-dropdown__btn`"
					@on-click="confirmValue">
					确定
				</fb-button>
			</div>
		</div>
	</transition>
</template>

<script>
import { prefix } from '../../../../../src/config'
import SvPanel from './sv-panel'
import HueSlider from './hue-slider'
import AlphaSlider from './alpha-slider'
import Predefine from './predefine'
import Popper from '../../../../utils/vue-popper'
import FbInput from '../../../input'
import FbButton from '../../../button'

export default {
	name: 'ColorPickerDropdown',

	mixins: [Popper],

	components: {
		SvPanel,
		HueSlider,
		AlphaSlider,
		FbInput,
		FbButton,
		Predefine,
	},

	props: {
		color: {
			required: true,
		},
		alpha: {
			type: Boolean,
			default: false,
		},
		predefine: Array,
		themePredefine: {
			type: Boolean,
		},
		needConfirm: {
			type: Boolean,
			default: false,
		},
	},

	data () {
		return {
			prefix,
			customInput: '',
			alphaInput: 100
		}
	},

	computed: {
		currentColor () {
			const parent = this.$parent
			return !parent.value && !parent.showPanelColor ? '' : parent.color.value
		},
	},

	methods: {
		confirmValue () {
			this.$emit('on-pick')
		},
		handleConfirm () {
			this.color.fromString(this.customInput)
		},
		handleClear () {
			this.showPopper = false
			this.$nextTick(()=>{
				this.$emit('on-clear')
			})
		},
	},

	mounted () {
		this.$parent.popperElm = this.popperElm = this.$el
		this.referenceElm = this.$parent.$el
	},

	watch: {

		customInput:{
			handler (val) {
				if (!this.needConfirm){

					if (val !== ''){
						this.$nextTick(() => {
							const {
								sv,
								hue,
								alpha,
							} = this.$refs
							sv && sv.update()
							hue && hue.update()
							alpha && alpha.update()

							this.$emit('on-change',val)
						})
					}




				}
			}
		},

		showPopper (val) {
			if (val === true) {
				this.$nextTick(() => {
					const {
						sv,
						hue,
						alpha,
					} = this.$refs
					sv && sv.update()
					hue && hue.update()
					alpha && alpha.update()
				})
			}
		},

		currentColor: {
			immediate: true,
			handler (val) {
				this.customInput = val
				this.alphaInput = this.color.alpha * 100
			},
		},
	},
}
</script>
