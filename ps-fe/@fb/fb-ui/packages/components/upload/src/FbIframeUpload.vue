<template>
	<component :class="`${prefix}-iframe-upload`" v-bind="$attrs" >

	</component>
</template>

<script>

	import { defaultsDeep, assign, noop } from 'lodash-es'
	import { Props } from './props'
	import FbDivider from '../../divider/src/FbDivider'
	import { uid } from './utils'
	import { prefix } from '../../../../src/config'

	/**
	 * FbIframeUpload
	 * (c) 2020 lincong1987
	 */
	export default {

		name: 'FbIframeUpload',
		components: {FbDivider},
		props: {

//			componentTag:null,
//			prefixCls: null,
			service: null,
			directory: null,
			onProgress: null,
			accept: null,
			disabled: null,
//			customRequest: null,

			data: {},
//			headers: {},
			name: 'file',
			multipart: false,
			onReady: noop,
			onStart: noop,
			onError: noop,
			onSuccess: noop,
			supportServerRender: false,
			multiple: false,
			beforeUpload: noop,
			withCredentials: false,
			openFileDialogOnClick: true,
		},

		data () {

			this.reqs = {}
			return {

				prefix,
				uid: uid(),
			}
		},

		computed: {

			getClass () {
				let arr = [`${prefix}-upload`]

				return arr
			},

		},

		watch: {
			fileList (value) {
				this.myFileList = value || []
			},
		},

		methods: {
			clearProgressTimer () {
				clearInterval(this.progressTimer)
			},
		},

		destroyed () {
			this.clearProgressTimer()
		},

		mounted () {
			this._isMounted = true
		},
	}
</script>

<style scoped>

</style>
