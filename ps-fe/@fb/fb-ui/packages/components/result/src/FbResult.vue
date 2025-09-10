<template>
	<div :class="`${prefix}-result ${getBodyClass}`">
		<div v-if="type !== 'img'" :class="`${prefix}-result--icon`" :style="getIconStyle">
			<fb-icon :name="getIconName" :style="getFbIconStyle"></fb-icon>
		</div>
		<div v-else-if="type == 'img'" :class="`${prefix}-result--img`">
			<img :src="imgSrc" alt="">
		</div>
		<div :class="`${prefix}-result--message`">{{message}}</div>
		<div :class="`${prefix}-result--describe`">
			<slot name="describe">{{describe}}</slot>
		</div>
	</div>
</template>

<script>
	/**
	 * FbResult
	 * (c) 2020 lincong1987
	 */

	import FbIcon from '../../icon/src/FbIcon'
	import { prefix } from '../../../../src/config'


	export default {
		name: 'FbResult',
		components: {FbIcon},
		props: {
			type: {
				type: String,
				default: 'success'
			},
			imgSrc: {
				type: [String, Object]
			},
			message: {
				type: [String],
				default: '',
			},
			describe: {
				type: [String],
				default: '',
			},
			iconSize: {
				type: [String, Number],
				default: 90,
			},
			iconName: {
				type: [String],
				default: 'selected',
			},
			iconBg: {
				type: [String],
				default: '',
			}
		},
		data () {
			return {
				prefix
			}
		},
		computed: {
			getBodyClass () {
				var classNames = []
				if (this.type == 'success') {
					classNames.push(`${this.prefix}-result-success`)
				} else if (this.type == 'error') {
					classNames.push(`${this.prefix}-result-error`)
				} else if (this.type == 'img') {
					classNames.push(`${this.prefix}-result-img`)
				}
				return classNames
			},
			getIconStyle () {
				var backgroundColor = {}
				if (!this.type && this.iconBg) {
					backgroundColor = {
						background: this.iconBg
					}
				}

				return {
					width: this.iconSize + 'px',
					height: this.iconSize + 'px',
					...backgroundColor
				}
			},
			getIconName () {
				var name = ''
				if (this.type == 'success') {
					name = 'selected'
				} else if (this.type == 'error') {
					name = 'close'
				} else {
					name = this.iconName
				}
				return name
			},
			getFbIconStyle () {
				return {
					fontSize: this.iconSize - 32 + 'px',
					lineHeight: this.iconSize + 'px',
				}
			}
		}
	}
</script>

<style scoped>

</style>
