<template>
	<div :class="[
			`${prefix}-upload-wrapper`,
			`${prefix}-upload-wrapper--${view}`,
			{
				[`${prefix}-upload-wrapper--focus`]: focus,
				[`${prefix}-upload-wrapper--readonly`]: readonly ,

			}
	]"
	>

		<template v-if="view === 'image'">

			<fb-upload-list
				v-show="showUploadList"
				view="image"
				:file-list="myFileList"
				:service="service"
				:show-preview="showPreview"
				:show-download="showDownload"
				:show-remove="showRemove"
				:throwPreviewEvent="throwPreviewEvent"
				@on-remove="handleRemove"
				@on-preview="handlePreview"
				@on-preview-close="handlePreviewClose"
			>
			</fb-upload-list>

			<fb-ajax-upload
				v-show="showUpload"
				view="image"
				:paste="paste"
				:drag="drag"
				:param="param"
				:service="service"
				:accept="accept"
				:multiple="multiple"
				:quality="quality"
				:max-height="maxHeight"
				:max-width="maxWidth"
				:after-select="afterSelect"
				:desc="desc"
				:button-icon="buttonIcon"
				:button-text="buttonText"
				:no-compress="noCompress"
				:no-suffix="noSuffix"
				:blob="blob"
				ref="uploader"
				@on-start="handleStart"
				@on-progress="handleProgress"
				@on-success="handleSuccess"
				@on-error="handleError"
			>
				<slot></slot>
			</fb-ajax-upload>

		</template>

		<template v-if="view === 'avatar'">

			<fb-ajax-upload
				:readonly="readonly"
				view="avatar"
				:paste="paste"
				:drag="drag"
				:param="param"
				:service="service"
				:accept="accept"
				:multiple="false"
				:quality="quality"
				:max-height="maxHeight"
				:max-width="maxWidth"
				:after-select="afterSelect"
				:desc="desc"
				:no-compress="noCompress"
				:no-suffix="noSuffix"
				:blob="blob"
				ref="uploader"
				@on-start="handleStart"
				@on-progress="handleProgress"
				@on-success="handleSuccess"
				@on-error="handleError"
				:class="{'has-avatar': !!avatarSrc, circle: avatarCircle}"
				:style="{height: `${avatarSize}px`, width: `${avatarSize}px`, }"
			>
				<template #avatarButton>
					<fb-avatar
						:size="avatarSize"
						:circle="avatarCircle"
						:src="avatarSrc"
						icon="user"
						style="cursor: pointer;"
						:background-color="'rgba(2, 132, 254, 0.3)'"
						:color="'rgba(255, 255, 255, 1)'"
					>
					</fb-avatar>
					<div class="mask" :style="{height: `${avatarSize}px`, width: `${avatarSize}px`, }"></div>
					<fb-icon color="#fff" size="32" name="add-small" :style="{ width: `${avatarSize}px`}"
							 @on-click="handleWrapperClick"
					></fb-icon>
					<fb-text color="#fff" size="m" :style="{ width: `${avatarSize}px`}" v-show="avatarSize>=100"
							 @on-click="handleWrapperClick"
					>点击上传
					</fb-text>
				</template>
			</fb-ajax-upload>


		</template>

		<template v-if="view === 'list'">
			<fb-ajax-upload
				:paste="paste"
				:drag="drag"
				v-show="showUpload"
				:param="param"
				:service="service"
				:accept="accept"
				:multiple="multiple"
				:button-icon="buttonIcon"
				:button-text="buttonText"
				:after-select="afterSelect"
				:desc="desc"
				:no-compress="noCompress"
				:no-suffix="noSuffix"
				:blob="blob"
				ref="uploader"
				@on-start="handleStart"
				@on-progress="handleProgress"
				@on-success="handleSuccess"
				@on-error="handleError"
			>
				<slot></slot>
			</fb-ajax-upload>
			<fb-upload-list
				v-show="showUploadList"
				:service="service"
				:file-list="myFileList"
				:show-preview="showPreview"
				:show-download="showDownload"
				:show-remove="showRemove"
				:throwPreviewEvent="throwPreviewEvent"
				@on-remove="handleRemove"
				@on-preview="handlePreview"
				@on-preview-close="handlePreviewClose"
			>
			</fb-upload-list>
		</template>


	</div>
</template>

<script>

import { prefix } from '../../../../src/config'
import { includes, findIndex, find } from 'lodash-es'
import FbDivider from '../../divider/src/FbDivider'
import FbUploadList from './FbUploadList'
import { genPercentAdd, getFileItem, removeFileItem } from './utils'
import FbAjaxUpload from './FbAjaxUpload'
import * as mime from './mimeTypes'
import FbAvatar from '../../avatar/src/FbAvatar'
import FbIcon from '../../icon/src/FbIcon'
import FbText from '../../text/src/FbText'

/**
 * FbUpload
 * (c) 2020 lincong1987
 */
export default {

	name: 'FbUpload',
	components: {
		FbText,
		FbIcon,
		FbAvatar,
		FbAjaxUpload,
		FbUploadList,
		FbDivider,
	},
	props: {
		mode: {
			type: String,
			default: 'select',
			validate (value) {
				return includes(['drag', 'select'], value)
			},
		},

		name: {
			type: String,
			default: 'file',
		},

		fileList: {
			type: Array,
			default () {
				return []
			},
		},

		service: {
			type: [Object, Function],
			default () {
				return () => {
				}
			},
		},

		param: {
			type: Object,
			default () {
				return {}
			},
		},
		      //
		directory: {
			type: Boolean,
			default: false,
		},

		data: {},

		// 多选
		multiple: {
			type: Boolean,
			default: false,
		},

		maxLength: {
			type: [Number, String],
			default: 1,
		},

		// 可接受的文件类型
		// 参阅: https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/Input/file
		accept: {
			type: String,
			default: '',
		},

		beforeUpload: {
			type: Function,
			default () {
				return () => {
				}
			},
		},
		afterSelect: {
			type: [Function],
			default () {
				return (files) => {
					return true
				}
			},
		},
		beforeRemove: {
			type: Function,
			default () {
				return new Promise((resolve, reject) => {
					resolve()
				})
			},
		},

		// 'list', 'image', "avatar", "grid"
		view: {
			type: String,
			default: 'list',
		},

		disabled: {
			type: Boolean,
			default: false,
		},

		withCredentials: {
			type: Boolean,
			default: false,
		},

		fastSelect: {
			type: Boolean,
			default: true,
		},

		height: {
			type: [String, Number],
			default: '100px',
		},
		// 显示上传列表
		showUploadList: {
			type: Boolean,
			default: true,
		},
		// 显示上传
		showUpload: {
			type: Boolean,
			default: true,
		},
		// 压缩类型
		compressTypes: {
			type: [String],
			default: 'image',
		},

		// 压缩质量
		quality: {
			type: Number,
			default: 0.7,
		},
		maxWidth: {
			type: [Number, Boolean],
			default: 2000,
		},

		maxHeight: {
			type: [Number, Boolean],
			default: 2000,
		},

		readonly: {
			type: Boolean,
			default: false,
		},
		// 头像大小
		avatarSize: {
			type: [Number],
			default: 120,
		},
		// 圆型
		avatarCircle: {
			type: Boolean,
			default: true,
		},
		// 按钮图标
		buttonIcon: {
			type: String,
			default: null,
		},
		// 按钮文字
		buttonText: {
			type: String,
			default: null,
		},
		// 描述
		desc: {
			type: String,
			default: null,
		},
		// 是否显示预览
		showPreview: {
			type: Boolean,
			default: true,
		},
		// 是否显示下载按钮
		showDownload: {
			type: Boolean,
			default: true,
		},

		// 显示删除按钮
		showRemove: {
			type: Boolean,
			default: true,
		},

		// 是否支持拖拽
		drag: {
			type: Boolean,
			default: false,
		},
		// 是否支持粘贴
		paste: {
			type: Boolean,
			default: false,
		},

		// 不压缩
		noCompress: {
			type: Boolean,
			default: false,
		},

		// 支持不带后缀的文件
		noSuffix: {
			type: Boolean,
			default: false,
		},

		// 上传方式, 如果为blob， 上传时为二进制， 否则为base64字符串
		blob: {
			type: Boolean,
			default: false,
		},

		// 抛出预览事件
		throwPreviewEvent: {
			type: Boolean,
			default: false,
		},
	},

	data () {
		this.progressTimer = null

		return {
			prefix,
			focus: false,
			myFileList: this.fileList.map((file) => {
				return {
					...file,
					lastModified: file.createTime,
					lastModifiedDate: file.createTime,
					name: file.attachName,
					uid: file.attachId,
					size: file.attachSize,
					type: mime.lookup(file.attachName),

					attachName: file.attachName,
					attachId: file.attachId,
					attachSize: file.attachSize,

					percent: 0,
					originFileObj: file,
					status: 'done',
				}
			}) || [],

		}
	},

	computed: {

		avatarSrc () {
			if (this.view === 'avatar' && this.myFileList.length > 0 && this.myFileList[0].status === 'done') {
				return this.service.download(this.myFileList[0])
			}
			return null
		},

	},

	watch: {
		fileList (value) {

			this.myFileList = value.map((file) => {

				if (file.attachId) {
					return Object.assign(file, {
						lastModified: file.createTime,
						lastModifiedDate: file.createTime,
						name: file.attachName,
						uid: file.attachId,
						size: file.attachSize,
						type: mime.lookup(file.attachName),

						attachName: file.attachName,
						attachId: file.attachId,
						attachSize: file.attachSize,

						percent: 0,
						originFileObj: file,
						status: 'done',
					})
				} else {
					return file
				}

			}) || []

		},

		myFileList () {
			// console.log('====== ======= ====== =myFileList')
		},
	},

	methods: {

		handleWrapperClick () {
			this.$refs.uploader &&
			this.$refs.uploader.handleWrapperClick()
		},

		clearProgressTimer () {
			clearInterval(this.progressTimer)
		},

		// 开始
		handleStart (file) {

			// console.log('[handleStart]', file.uid)

			const targetItem = {
				...file,
				lastModified: file.lastModified,
				lastModifiedDate: file.lastModifiedDate,
				name: file.name,
				size: file.size,
				type: file.type,
				uid: file.uid,
				percent: 0,
				originFileObj: file,
				status: 'uploading',
			}

			const fileIndex = this.myFileList.map(file => file.uid || file.name).indexOf(targetItem.uid) //findIndex(nextFileList, {uid: targetItem.uid})
			if (fileIndex === -1) {
				this.myFileList.push(targetItem)
			} else {
				this.myFileList[fileIndex] = targetItem
			}

			// this.myFileList =

			this.handleChange({
				file: targetItem,
				fileList: this.myFileList,
			})
			if (!window.File) {
				this.autoUpdateProgress(0, targetItem)
			}

			this.$emit('on-start', targetItem)
		},

		handleProgress (e, file) {

			// console.log('[handleProgress]', file.uid)

			const targetItem = getFileItem(file, this.myFileList)// find(fileList, {uid: file.uid})

			// removed
			if (!targetItem) {
				return
			}
			targetItem.percent = e.percent
			this.handleChange({
				event: e,
				file: {...targetItem},
				fileList: this.myFileList,
			})

			this.$emit('on-progress', e, targetItem)
		},

		// 上传成
		handleSuccess (res, file) {

			// console.log('[handleSuccess]', file.uid)

			this.clearProgressTimer()
			try {
				if (typeof res === 'string') {
					res = JSON.parse(res)
				}
			} catch (e) {
			}

			const fileList = this.myFileList
			const targetItem = getFileItem(file, fileList)
			// 没有的话
			if (!targetItem) {
				return
			}
			targetItem.status = 'done'
			targetItem.res = res

			Object.assign(targetItem, res.data)

			this.handleChange({
				file: {...targetItem},
				fileList,
			})

			this.$emit('on-success', res, file, targetItem)
		},
		handleError (error, res, file) {

			// console.log('[handleError]', file.uid)

			this.clearProgressTimer()
			const fileList = this.myFileList
			const targetItem = getFileItem(file, fileList)
			// removed
			if (!targetItem) {
				return
			}
			targetItem.error = error
			targetItem.res = res
			targetItem.status = 'error'
			this.handleChange({
				file: {...targetItem},
				fileList,
			})

			this.$emit('on-error', error, res, file)
		},
		handleReject (fileList) {
			this.$emit('on-reject', fileList)
		},
		handleRemove (file) {
			// 删除前确认
			this.beforeRemove(file).then(() => {
				if (this.$refs.uploader) {
					this.$refs.uploader.abort(file)
				}

				const removedFileList = removeFileItem(file, this.myFileList)

				if (removedFileList) {
					file.status = 'removed' // eslint-disable-line

					if (this.upload) {
						this.upload.abort(file)
					}

					this.handleChange({
						file,
						fileList: removedFileList,
					})
				}

				this.$emit('on-remove', file)
			})
		},

		handlePreview (e) {
			this.$emit('on-preview', e)
		},
		handlePreviewClose (e) {
			this.$emit('on-preview-close', e)
		},
//			handleManualRemove (file) {
//				if (this.$refs.uploadRef) {
//					this.$refs.uploadRef.abort(file)
//				}
//				this.handleRemove(file)
//			},
		handleChange (info) {
			// console.log('[handleChange]', info)
//				if (!this.fileList) {
//					this.setState({myFileList: info.fileList})
//				}

			this.myFileList = info.fileList
			this.$emit('on-update:fileList', info.fileList)
			this.$emit('on-change', info)
			// this.$emit('input', info.fileList)
		},
		onFileDrop (e) {
			this.setState({
				dragState: e.type,
			})
		},
//			beforeUpload (file, fileList) {
////				const {beforeUpload} = this.$props
////				const {myFileList: stateFileList} = this.$data
////				if (!beforeUpload) {
////					return true
////				}
////				const result = beforeUpload(file, fileList)
////				if (result === false) {
////					this.handleChange({
////						file,
////						fileList: uniqBy(stateFileList.concat(fileList.map(fileToObject)), item => item.uid),
////					})
////					return false
////				}
////				if (result && result.then) {
////					return result
////				}
//				return true
//			},

		autoUpdateProgress (_, file) {
			const getPercent = genPercentAdd()
			let curPercent = 0
			this.clearProgressTimer()
			this.progressTimer = setInterval(() => {
				curPercent = getPercent(curPercent)
				this.handleProgress(
					{
						percent: curPercent * 100,
					},
					file,
				)
			}, 200)
		},

	},
	beforeDestroy () {
		clearInterval(this.timer)
	},

	destroyed () {
		this.clearProgressTimer()
	},
}
</script>

<style scoped>

</style>
