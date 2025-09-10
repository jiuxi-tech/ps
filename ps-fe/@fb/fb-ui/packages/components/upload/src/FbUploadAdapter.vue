<template>
	<div style="border: 3px solid red; padding: 5px">
		adapter
		<component ref="uploadAdapter"
		           :is="adapter"
		           v-bind="{...$attrs, ...$props}">
			<slot></slot>
		</component>
	</div>
</template>

<script>

	import { defaultsDeep, assign, noop } from 'lodash-es'
	import { Props, UploadAdapterProps } from './props'
	import { prefix } from '../../../../src/config'

	/**
	 * FbUploadAdapter
	 * (c) 2021 lincong1987
	 */

	export default {
		name: 'FbUploadAdapter',

		props: defaultsDeep({}, UploadAdapterProps, {}),

		data () {

			return {
				prefix,
				// 确定用哪个适配器
				adapter: typeof File !== 'undefined' ? 'fb-ajax-upload' : 'fb-iframe-upload',
			}
		},

		methods: {
			// 终止
			abort (file) {
				this.$refs.uploadAdapter.abort(file)
			},
		},

		mounted () {
			// console.log('$attrs', this.$attrs)
			// console.log('$props', this.$props)
		},

	}
</script>

<style lang="less" scoped>

</style>
