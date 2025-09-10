<template>
	<div :class="getClass">

		<div v-if="hasBreadcrumb" :class="`${prefix}-page-header--breadcrumb`">
			xxx\xxx\xxx
		</div>

		<div :class="`${prefix}-page-header__head`">

			<!--			<div :class="`${prefix}-page-header__head__back`" v-if="$slots.back">-->
			<!--				<fb-icon name="" @on-click="handleBack"></fb-icon>-->
			<!--			</div>-->
			<div :class="`${prefix}-page-header__head__title`" v-if="title" :style="titleStyle">
				{{title}}
			</div>
			<div :class="`${prefix}-page-header__head__sub-title`" v-if="subTitle">
				{{subTitle}}
			</div>
			<div :class="`${prefix}-page-header__head__tags`" v-if="tags || $slots.tags">
				<template v-if="tags">
					<fb-tags :data="tags"></fb-tags>
				</template>
				<template v-if="$slots.tags">
					<slot name="tags"></slot>
				</template>
			</div>
			<div :class="`${prefix}-page-header__head__actions`" v-if="$slots.actions">
				<slot name="actions"></slot>
			</div>

		</div>

		<div :class="`${prefix}-page-header__body`">
			<slot></slot>
		</div>

	</div>
</template>

<script>
	/**
	 * FbPageHeader
	 * (c) 2020 lincong1987
	 */

	import { prefix } from '../../../../src/config'

	import { isObject, isArray, isPlainObject } from 'lodash-es'
	import FbIcon from '../../icon/src/FbIcon'

	export default {
		name: 'FbPageHeader',
		components: {FbIcon},
		props: {
			// 面包屑
			breadcrumb: {
				type: [Object, Array],
				default () {
					return []
				},
			},
			// 返回图标
			back: {
				type: [String],
				default: 'left',
			},

			// 标题
			title: {
				type: [String, Number],
				default: '',
			},

			// 副标题
			subTitle: {
				type: [String, Number],
				default: undefined,
			},

			// 标签组
			tags: {
				type: [Array],
				default: undefined,
			},

			titleStyle: {
				type: [String, Object],
				default: undefined,
			},
		},
		data () {
			return {
				prefix,
				myBreadcrumb: isPlainObject(this.breadcrumb) ? [this.breadcrumb] : this.breadcrumb,
			}
		},
		computed: {

			hasBreadcrumb () {
				return this.myBreadcrumb.length > 0
			},

			getClass () {
				let arr = [`${prefix}-page-header`]

				if (this.hasBreadcrumb) {
					arr.push(`${prefix}-page-header--breadcrumb`)
				}

				return arr

			},
		},

		methods: {
			handleBack () {
				this.$emit('on-back')
			},
		},
	}
</script>

<style scoped>

</style>
