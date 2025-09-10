<template>
	<div :class="[
		`${view}`,
		`${prefix}-upload-list`
	]"
	>

		<template v-for="(file, index) in myFileList"
		>
			<transition name="fade-transform">
				<div
					:class="[
						`${prefix}-upload-list-item`,
						`${prefix}-upload-list-item--status-${file.status}`
						]">

					<template v-if="view === 'list'">


						<fb-icon :class="`${prefix}-upload-list-item__uploading`" v-if="file.status === 'uploading'"
								 name="loading1"
								 rotating></fb-icon>
						<fb-icon :class="`${prefix}-upload-list-item__attachment`" v-else name="attachment"></fb-icon>


						<span :class="`${prefix}-upload-list-item__label`" :title="file.name"
							  @click="handleDownload(file)">{{ file.name }}</span>
						<fb-icon v-if="showPreview" :class="`${prefix}-upload-list-item__download`"
								 @on-click="handlePreview(file)"
								 name="show"></fb-icon>
						<fb-icon v-if="showRemove" :class="`${prefix}-upload-list-item__remove`"
								 @on-click="handleRemove(file)"
								 name="delete"></fb-icon>

						<div v-if="file.status === 'uploading'" :class="`${prefix}-upload-list-item__progress`"></div>
					</template>

					<template v-if="view === 'image'">

						<fb-icon :class="`${prefix}-upload-list-item__uploading`" v-if="file.status === 'uploading'"
								 name="loading1"
								 rotating></fb-icon>

						<fb-icon v-if="showPreview" :class="`${prefix}-upload-list-item__preview`"
								 @on-click="handlePreview(file)"
								 size="24"
								 name="show"></fb-icon>
						<fb-icon v-if="showRemove" :class="`${prefix}-upload-list-item__remove`"
								 @on-click="handleRemove(file)" size="24"
								 name="delete"></fb-icon>

						<div v-if="file.status !== 'uploading'" :class="`${prefix}-upload-list-item__progress`"></div>

						<div v-if="file.status === 'done'" :class="`${prefix}-upload-list-item__image`"
							 :style="{
							backgroundImage: `url('${imageSrc(file)}')`
						}"
						></div>

						<div class="mask"></div>
					</template>

				</div>
			</transition>
		</template>


		<!--		<fb-dialog ref="previewImageDialog" title="查看" can-full-screen overflow="auto"-->
		<!--		           :before-close="handleBeforeClose">-->
		<!--			<img :src="currentImageUrl" style="width: 100%;" alt="">-->
		<!--		</fb-dialog>-->


		<!--		<template v-if="view === 'image'">-->
		<fb-image-viewer
			v-if="showImageViewer"
			:image-index="currentImageIndex"
			:image-urls="imageUrls"
			:before-close="handleBeforeClose"
		></fb-image-viewer>
		<!--		</template>-->

		<fb-pdf-viewer
			:src="pdfSrc"
			ref="fb-pdf-viewer"
		></fb-pdf-viewer>

	</div>
</template>

<script>

import FbDialog from '../../dialog/src/FbDialog'
import fileTypes from './fileTypes'
import { prefix } from '../../../../src/config'
import FbIcon from '../../icon/src/FbIcon'
import FbImageViewer from '../../image-viewer/src/FbImageViewer'
import FbPdfViewer from '../../pdf-viewer/src/FbPdfViewer'

/**
 * FbUploadList
 * (c) 2020 lincong1987
 */
export default {

	name: 'FbUploadList',

	components: {
		FbPdfViewer,
		FbIcon,
		FbDialog,
		FbImageViewer,
	},
	props: {

		view: {
			type: String,
			default: 'list',
		},

		fileList: {
			type: Array,
			default () {
				return []
			},
		},

		progressAttr: {
			type: Object,
		},

		service: {
			type: [Object, Function],
			default () {
				return () => {
					return {
						upload () {
						},
						download () {
						},
					}
				}
			},
		},

		quality: {
			type: Number,
			default: 0.7,
		},

		readonly: {
			type: Boolean,
			default: false,
		},

		showPreview: {
			type: Boolean,
			default: true,
		},

		showDownload: {
			type: Boolean,
			default: true,
		},

		showRemove: {
			type: Boolean,
			default: true,
		},

		// 抛出预览事件
		throwPreviewEvent: {
			type: Boolean,
			default: false,
		},

	},

	data () {
		return {

			prefix,
			myFileList: this.fileList,

			currentImageIndex: 0,

			showImageViewer: false,
			prevOverflow: '',

			pdfSrc: '',

		}
	},

	computed: {

		getClass () {
			let arr = [`${prefix}-upload`]

			return arr
		},

		imageUrls () {

			return this.myFileList.filter(file => {
				let fileCtg = fileTypes.getCategoryByMime(file.type)
				return fileCtg && fileCtg[0] === 'image'
			}).map(this.imageSrc)

		},

	},

	watch: {
		fileList (value) {
			this.myFileList = value
		},
	},

	methods: {
		getBase64 (file) {

			let isFile = file.originFileObj instanceof File

			const reader = isFile ? new FileReader() : new Image()

			return new Promise((resolve, reject) => {

				if (isFile) {
					reader.readAsDataURL(file)
				} else {
					resolve(this.service.download(file))
				}

				reader.onload = () => {
					resolve(reader.result)
				}
				reader.onerror = error => reject(error)
			})

		},

		async handlePreview (file) {

			let fileCtg = fileTypes.getCategoryByMime(file.type)

			if (this.throwPreviewEvent) {
				this.$emit('on-preview', file)
				return
			}

			if (fileCtg && fileCtg[0] === 'image') {
				let currentImageIndex = this.imageUrls.indexOf(this.imageSrc(file))

				this.currentImageIndex = currentImageIndex >= 0 ? currentImageIndex : 0
				this.prevOverflow = document.body.style.overflow
				document.body.style.overflow = 'hidden'
				this.showImageViewer = true
				this.$emit('on-preview', file)
				return this.$emit('preview', file)
				//}

			} else if (fileCtg[0] === 'pdf') {

				this.pdfSrc = this.service.download(file)
				this.showPdfViewer = true
				this.$nextTick(() => {
					this.$refs['fb-pdf-viewer'].show()
				})

				this.$emit('on-preview', file)
				return this.$emit('preview', file)

			} else {
				this.$message.error('无法预览此附件')
			}

		},

		handleBeforeClose () {
			document.body.style.overflow = this.prevOverflow
			this.showImageViewer = false
			this.$emit('on-preview-close')
		},

		handleRemove (file, e) {
			this.$emit('on-remove', file)
		},

		handleDownload (file) {
			console.log(file)
			if (!this.showDownload || file.showDownload === false) {
				return
			}
			window.open(this.service.download(file))
		},

		imageSrc (file) {
			return this.service.download(file)
		},
	},
}
</script>

<style scoped>

</style>
