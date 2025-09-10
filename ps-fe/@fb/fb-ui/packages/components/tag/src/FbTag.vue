<template>
	<transition name="zoom-in-center">

		<span v-if="!closed"
			  :class="getClass"
			  :style="{
				color: labelColor,
				backgroundColor:backgroundColor || color ,
				borderColor: borderColor || color,
			}"
			  @click="handleClick">

			<fb-icon
				v-if="icon"
				:name="icon"
				:class="`${prefix}-tag__icon`"
			></fb-icon>

			<template v-if="$slots.default">
			  <slot></slot>
			</template>
			<template v-else>
				{{ label }}
			</template>

            <fb-icon
				v-if="closable  "
				name="close"
				:class="`${prefix}-tag__close`"
				size="12"
				@on-click="handleClose"/>
		</span>


	</transition>
</template>

<script>
import FbIcon from '../../icon/src/FbIcon'
import { closest } from '../../../utils/componentUtils'
import { prefix } from '../../../../src/config'

/**
 * FbTag
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTag',
	components: {FbIcon},
	props: {
		value: {
			type: [String, Number],
			default: null,
		},
		label: {
			type: String,
			default: '',
		},
		round: {
			type: Boolean,
			default: false,
		},
		closable: {
			type: Boolean,
			default: false,
		},
		type: {
			type: String,
			default: 'default',
		},
		disableTransitions: {
			type: Boolean,
			default: false,
		},
		color: {
			type: String,
			default: '',
		},

		backgroundColor: {
			type: String,
			default: undefined,
		},

		borderColor: {
			type: String,
			default: undefined,
		},

		labelColor: {
			type: String,
			default: undefined,
		},

		effect: {
			type: String,
			default: 'dark',

		},

		light: {
			type: Boolean,
			default: false,
		},
		plain: {
			type: Boolean,
			default: false,
		},
		dark: {
			type: Boolean,
			default: false,
		},
		solid: {
			type: Boolean,
			default: false,
		},

		size: {
			type: String,
			default: 'm',

		},

		icon: {
			type: String,
			default: null,
		},

	},
	data () {
		return {
			prefix,
			closed: false,
		}
	},

	created () {
		this.fbTags = closest(this, 'FbTags')
	},

	beforeDestroy () {
		this.fbTags = null
	},

	methods: {
		handleClose (event) {

			if (this.fbTags) {
				this.fbTags.handleClose(this.value, this)
			}

			this.$emit('on-close', event)

			if (!this.fbTags) {
				this.closed = true
			}
		},
		handleClick (event) {
			this.$emit('on-click', event, this)
		},
	},

	computed: {
		getClass () {
			let arr = [
				`${prefix}-tag`,
			]

			if (this.$slots.default && this.$slots.default[0].tag) {
				return []
			}

			if (this.size) {
				arr.push(`${prefix}-tag--size-${this.size}`)
			}

			if (this.round) {
				arr.push(`${prefix}-tag--round`)
			}

			if (this.type) {
				arr.push(`${prefix}-tag--${this.type}`)
			}

			let effect = this.effect

			if (this.light) {
				effect = 'light'
			}
			if (this.plain) {
				effect = 'plain'
			}
			if (this.dark) {
				effect = 'dark'
			}
			if (this.solid) {
				effect = 'solid'
			}

			//if (this.effect) {
			arr.push(`${prefix}-tag--${effect}`)
			//}

			//if (this.effect) {
			//arr.push(`${prefix}-tag--${this.type}--${this.effect}`)
			//}

			return arr
		},
	},

}
</script>

<style scoped>

</style>
