<template>
	<fb-dialog
		v-if="showPdfViewer" ref="dialog" title="查看" can-full-screen
		width="80%"
		:before-close="close">
		<iframe :src="pdfSrc" style="width: 100%; height: 100%;" frameborder="0"></iframe>
	</fb-dialog>
</template>

<script>
import { prefix } from '../../../../src/config'
import FbDialog from '../../dialog/src/FbDialog'

/**
 * FbPdfViewer
 * (c) 2022 lincong1987
 */


export default {
	name: 'FbPdfViewer',
	components: {
		FbDialog,
	},
	props: {
		src: {
			type: String,
			default: '',
		},
		beforeClose: {
			type: Function,
			default: () => {
			},
		},
	},
	data () {
		return {
			prefix,
			showPdfViewer: false,
			pdfSrc: '',
		}
	},
	methods: {
		close () {
			this.beforeClose()
		},
		show () {
			this.pdfSrc = encodeURI(`assets/pdf/web/viewer.html?file=${encodeURIComponent(this.src)}`)
			this.showPdfViewer = true
			this.$nextTick(() => {
				this.$refs.dialog.show()
			})
		},
	},

	mounted () {

	},
	beforeDestroy () {
	},
	destroyed () {
	},
}
</script>

<style lang="less" scoped>
</style>
