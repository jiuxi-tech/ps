<template>
	<div :class="[
		`${prefix}-avatar-box`,
		`${prefix}-avatar-box__${layoutType}`,
		{[`${prefix}-avatar-box__img_fill`]: srcFill}
	]">
		<fb-avatar
			v-if="icon || src"
			:size="size"
			:circle="circle"
			:icon="icon"
			:color="color"
			:backgroundColor="backgroundColor"
			:text="avatarText"
			:src="src"
			:alt="alt"
		></fb-avatar>
		<div :class="`${prefix}-avatar-box-content`">
			<slot></slot>
		</div>
		<div :class="`${prefix}-avatar-box__icon`" :style="linearStyle" v-if="linearIcon">
			<fb-icon :class="`icon-linear-box icon-linear-box-${linearType}`"
					 :name="linearIcon"
					 :size="linearSize"
					 :color="linearColor || backgroundColor">
			</fb-icon>
		</div>
		<div :class="`${prefix}-avatar-box__img`"  :style="linearStyle" v-if="linearSrc">
			<img :src="linearSrc" alt="">
		</div>
	</div>
</template>

<script>/**
 * FbAvatar
 * (c) 2020 lincong1987
 */

import { getSizeStyle } from '../../../utils/propUtils'
import FbAvatar from '../../avatar/src/FbAvatar'
import FbIcon from '../../icon/src/FbIcon'
import { prefix } from '../../../../src/config'

export default {
	name: 'FbAvatarBox',
	components: {FbAvatar, FbIcon},
	props: {
		layoutType: {
			type: [String],
			default: 'left',
		},
		size: {
			type: [String, Number],
			default: 'm',
		},
		// 圆型
		circle: {
			type: Boolean,
			default: true,
		},
		icon: {
			type: [String],
			default: undefined,
		},
		color: {
			type: [String],
			default: undefined,
		},
		backgroundColor: {
			type: [String],
			default: undefined,
		},
		avatarText: {
			type: [String, Number],
			default: undefined,
		},
		src: {
			type: [String],
			default: undefined,
		},
		srcFill: {
			type: [Boolean],
			default: false,
		},
		alt: {
			type: [String],
			default: undefined,
		},
		// 线性展示 icon name
		linearIcon: {
			type: [String],
			default: undefined,
		},
		linearSize: {
			type: [String, Number],
			default: '70',
		},
		linearColor: {
			type: [String],
			default: '',
		},
		linearType: {
			type: [String],
			default: 'default',
		},
		linearSrc: {
			type: [String],
			default: '',
		},
		linearStyle: {
			type: [Object, String],
			default: ''
		}
	},
	data () {
		return {
			prefix,
		}
	},
	computed: {



	},
	watch: {

	},
	methods: {

	},
	mounted () {
	},
	updated () {
	},

}
</script>

<style scoped>

</style>
