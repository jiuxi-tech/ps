<template>
	<div ref="wrapper" :class="[`${prefix}-ajax-upload`, {
		     [`${prefix}-ajax-upload--${dragStatus}`]: dragStatus,
		     [`${prefix}-ajax-upload--${pasteStatus}`]: pasteStatus,
	}]"
		 @paste.stop.prevent="onPaste"
		 @dragenter.stop.prevent="onDragEnter"
		 @dragover.stop.prevent="onDragOver"
		 @dragleave.stop.prevent="onDragLeave"
		 @drop.stop.prevent="onDrop"
		 @focus.stop.prevent="onFocus"
		 @blur.stop.prevent="onBlur"
		 @mouseover.stop.prevent="onMouseOver"
		 @mouseleave.stop.prevent="onMouseLeave"
		 contenteditable="true"
		 @keydown="onKeyDown"
		 :title="getTitle"

	>
		<input
			:class="`${prefix}-ajax-upload__el`"
			v-show="true"
			type="file"
			ref="uploadEl"
			:id="id"
			:key="uid"
			:accept="accept"
			:directory="directory ? 'directory' : null"
			:webkitdirectory="directory ? 'webkitdirectory' : null"
			:multiple="multiple"
			@change="onChange"
			@click.stop="(e)=>{return e.stopPropagation()}"
		/>

		<template v-if="view === 'list'">
			<fb-text :class="`${prefix}-upload-helper`">
				<fb-tag solid type="primary" v-if="dragStatus || pasteStatus">
					{{ dragStatus === 'drag-over' ? '放开鼠标后上传' : '' }}
					{{ pasteStatus === 'in-paste' ? '粘贴后上传' : '' }}
				</fb-tag>
			</fb-text>
			<fb-button :icon="buttonIcon||'add-circle'"
					   @on-click="handleWrapperClick"
			>{{ buttonText || '文件上传' }}
			</fb-button>
			<span>{{ desc }}</span>
		</template>

		<template v-if="view === 'image'">
			<fb-text :class="`${prefix}-upload-helper`">
				<fb-tag solid type="primary" v-if="dragStatus || pasteStatus">
					{{ dragStatus === 'drag-over' ? '放开鼠标后上传' : '' }}
					{{ pasteStatus === 'in-paste' ? '粘贴后上传' : '' }}
				</fb-tag>
			</fb-text>
			<fb-icon :class="`${prefix}-upload-icon`" size="32" :name="buttonIcon||'image'"
					 @on-click="handleWrapperClick"></fb-icon>
			<fb-text :class="`${prefix}-upload-text`"
					 @on-click="handleWrapperClick"
			>{{ buttonText || '图片上传' }}
			</fb-text>
			<span>{{ desc }}</span>
		</template>

		<template v-if="view === 'avatar'" @on-click="handleWrapperClick">
			<fb-text :class="`${prefix}-upload-helper`">
				<fb-tag solid type="primary" v-if="dragStatus || pasteStatus">
					{{ dragStatus === 'drag-over' ? '放开鼠标后上传' : '' }}
					{{ pasteStatus === 'in-paste' ? '粘贴后上传' : '' }}
				</fb-tag>
			</fb-text>
			<slot name="avatarButton"></slot>
		</template>

		<slot name="default"></slot>

		<!--	 <fb-flex height="300px" width="300px" border="2px solid red">-->
		<!--		 <img :src="previewBase64" alt="" style="height: 100%; width: 100%">-->
		<!--	 </fb-flex>-->

	</div>
</template>

<script>

import { defaultsDeep, assign, noop, each } from 'lodash-es'
import FbDivider from '../../divider/src/FbDivider'
import FbIcon from '../../icon/src/FbIcon'
import FbButton from '../../button/src/FbButton'
import FbText from '../../text/src/FbText'
import { closest } from '../../../utils/componentUtils'
import fileTypes from './fileTypes'
import { prefix } from '../../../../src/config'
import FbContainer from '../../container/src/FbContainer'
import { uuid } from '../../../utils/utils'
import FbTag from '../../tag/src/FbTag.vue'

/**
 * FbAjaxUpload
 * (c) 2020 lincong1987
 */
export default {

	name: 'FbAjaxUpload',

	components: {
		FbTag,
		FbContainer,
		FbDivider,
		FbIcon,
		FbButton,
		FbText,
	},

	props: {
		// 文件上传时的表单字段名
		name: {
			type: String,
			default: 'file',
		},

		// 显示模式，可选值：list, image, avatar
		view: {
			type: String,
			default: 'list',
		},
		// 上传参数
		param: {
			type: Object,
			default () {
				return {}
			},
		},
		// 是否多选
		multiple: {
			type: Boolean,
			default: false,
		},
		// 允许选择文件夹
		directory: {
			type: Boolean,
			default: false,
		},
		disabled: {
			type: Boolean,
			default: false,
		},
		// 接受的文件类型
		accept: {
			type: String,
		},

		data: null,

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

		// 描述文字
		desc: {
			type: String,
			default: null,
		},

		// 上传服务，可以是函数，也可以是对象
		service: {
			type: [Object, Function],
			default () {
				return () => {
				}
			},
		},

		// 文件选择后，返回false，则不触发上传
		// 返回true，则继续上传，返回false，则不继续上传
		afterSelect: {
			type: [Function],
			default () {
				return (files) => {
					return true
				}
			},
		},

		withCredentials: {
			type: Boolean,
			default: true,
		},

		// 快速选择
		fastSelect: {
			type: Boolean,
			default: true,
		},

		// 压缩质量
		quality: {
			type: Number,
			default: 0.7,
		},

		// 不压缩
		noCompress: {
			type: Boolean,
			default: false,
		},

		maxWidth: {
			type: [Number, Boolean],
			default: 1200,
		},

		maxHeight: {
			type: [Number, Boolean],
			default: 800,
		},

		readonly: {
			type: Boolean,
			default: false,
		},

		// 支持拖拽
		drag: {
			type: Boolean,
			default: false,
		},

		// 支持粘贴
		paste: {
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
	},

	data () {

		const {
			$props,
			$attrs,
		} = this

		this.reqs = {}
		return {
			prefix,
			uid: uuid(),
			id: $attrs.id,

			dragStatus: '',
			pasteStatus: '',

			previewBase64: '',
		}
	},
	created () {
		this.fbUpload = closest(this, 'FbUpload')
	},

	beforeDestroy () {
		this.fbUpload = null
	},

	computed: {

		getClass () {
			let arr = [`${prefix}-ajax-upload`]

			return arr
		},

		getTitle () {
			let title = []
			let desc = []
			// `${paste?'支持剪贴板\n':''}${drag?'支持拖拽\n':''}`
			if (this.readonly) {
				return ''
			}
			if (this.paste) {
				title.push('剪贴板粘贴')
				desc.push('使用剪贴板粘贴上传，请先点击上传框，然后按Ctrl+v或Command+v。')
			}
			if (this.drag) {
				title.push('拖拽')
				desc.push('使用拖拽上传，请将桌面、资源管理器中的文件直接拖到上传框中。')
			}
			if (title.length === 0) {
				return '点击上传'
			}
			if (title.length > 0) {
				return `支持点击、${title.join('、')}上传。\n注意：\n${desc.join('\n')}`
			}

		},

	},

	methods: {

		// 压缩图片
		imageCompress (file, type) {
			let targetType = file.type || 'image/jpeg'
			return new Promise((resolve, reject) => {
				let reader = new FileReader()

				reader.readAsDataURL(file)

				reader.onload = (e) => {

					let img = new Image()
					img.src = e.target.result

					img.onerror = (e) => {
						reject(e)
					}
					img.onload = (e) => {
						const canvas = document.createElement('canvas')
						const context = canvas.getContext('2d')
						const {
							width: originWidth,
							height: originHeight,
						} = e.target

						// 最大尺寸限制
						const maxWidth = !this.maxWidth ? originWidth : this.maxWidth
						const maxHeight = !this.maxHeight ? originHeight : this.maxHeight
						// 目标尺寸
						let targetWidth = originWidth
						let targetHeight = originHeight
						if (originWidth > maxWidth || originHeight > maxHeight) {
							if (originWidth / originHeight > 1) {
								// 宽图片
								targetWidth = maxWidth
								targetHeight = Math.round(maxWidth * (originHeight / originWidth))
							} else {
								// 高图片
								targetHeight = maxHeight
								targetWidth = Math.round(maxHeight * (originWidth / originHeight))
							}
						}
						canvas.width = targetWidth
						canvas.height = targetHeight
						context.clearRect(0, 0, targetWidth, targetHeight)
						// 图片绘制
						context.drawImage(img, 0, 0, targetWidth, targetHeight)

						if (targetType === 'image/png') {
							targetType = 'image/jpeg'
						}

						let compressed = canvas.toDataURL(targetType, this.quality)

						this.previewBase64 = compressed

						resolve(compressed)
					}

				}
				reader.onerror = (e) => {
					reject(e)
				}
			})
		},

		/**
		 * 拖拽上传
		 * @param e
		 * @returns {boolean}
		 */
		onDragEnter (e) {
			if (!this.drag || this.readonly) {
				return false
			}
			this.dragStatus = 'drag-enter'
		},

		/**
		 * 拖拽上传
		 * @param e
		 * @returns {boolean}
		 */
		onDragOver (e) {
			if (!this.drag || this.readonly) {
				return false
			}

			this.dragStatus = 'drag-over'
		},

		/**
		 * 拖拽上传
		 * @param e
		 * @returns {boolean}
		 */
		onDragLeave (e) {
			if (!this.drag || this.readonly) {
				return false
			}

			this.dragStatus = ''
		},
		/**
		 * 拖拽上传 onDrop 事件
		 * @param e
		 * @returns {boolean}
		 */
		onDrop (e) {
			if (!this.drag || this.readonly) {
				return false
			}
			this.dragStatus = ''

			this.checkFilesSuffix(e.dataTransfer.files).then(() => {
				this.uploadFiles(e.dataTransfer.files)
				this.reset()
			})
		},

		/**
		 * 鼠标移入 粘贴框 显示
		 * @param e
		 */
		onMouseOver (e) {
			if (this.readonly) {
				return
			}
			if (this.paste) {
				this.$refs.wrapper.focus()
			}
		},

		/**
		 * 鼠标移出 粘贴框 隐藏
		 * @param e
		 */
		onMouseLeave (e) {
			if (this.readonly) {
				return
			}
			if (this.paste) {
				this.$refs.wrapper.blur()
			}
		},

		/**
		 * 粘贴上传
		 * @param e
		 * @returns {boolean}
		 */
		onFocus () {
			if (this.paste && !this.readonly) {
				this.pasteStatus = 'in-paste'
			}
		},
		/**
		 * 粘贴上传
		 */
		onBlur () {
			if (this.paste && !this.readonly) {
				this.pasteStatus = ''
			}
		},
		/**
		 *  粘贴上传
		 * @param event
		 * @returns {boolean}
		 */
		onPaste (event) {
			if (!this.paste || this.readonly) {
				return false
			}

			if (event.clipboardData || event.originalEvent) {
				//not for ie11 某些chrome版本使用的是event.originalEvent
				let clipboardData = (event.clipboardData || event.originalEvent.clipboardData)
				if (clipboardData.files) {

					this.checkFilesSuffix(clipboardData.files).then(() => {
						this.uploadFiles(clipboardData.files)
						this.reset()
					})
				}
			}
		},

		/**
		 *  选择文件
		 * @param e
		 */
		onChange (e) {
			const files = e.target.files

			this.checkFilesSuffix(files).then(() => {
				if (this.afterSelect(files)) {
					this.uploadFiles(files)
					this.reset()
				}
			}).catch(() => {
				this.reset()
			})

		},
		/**
		 * 验证文件类型
		 * @param files
		 * @returns {Promise<unknown>}
		 */
		checkFilesSuffix (files) {
			return new Promise((resolve, reject) => {

				let hasError = false
				let message = ''
				// 处理无后缀文件
				if (Array.prototype.some.call(files, item => !item.type)) {
					hasError = true
					message = '不支持此文件类型'
				}
				if (this.noSuffix) {
					hasError = false
				}

				if (hasError === true) {
					this.$message.error(message)
					reject(false, message)
				} else {
					resolve(true)
				}
			})
		},

		onClick () {
			const el = this.$refs.uploadEl
			if (!el) {
				return
			}
			el.click()
		},
		onKeyDown (e) {

			if (this.readonly) {
				return false
			}

			if (e.key === 'Tab') {
				return true
			}

			if (this.paste) {
				if ((e.ctrlKey || e.metaKey) && e.keyCode === 86) {
					return true
				}
			}

			if (e.key === 'Enter') {
				this.onClick()
			}

			e.preventDefault()
			e.stopPropagation()
			return false

		},
		uploadFiles (files) {
			const fileList = Array.prototype.slice.call(files)

			fileList.map(file => {
				file.uid = uuid()
				return file
			}).forEach(file => {
				this.upload(file, fileList)
			})
		},
		upload (file, fileList) {

			let fileCtg = fileTypes.getCategoryByMime(file.type)

			if (fileCtg && fileCtg[0] === 'image' && this.view === 'image') {
				this.imageCompress(file).then((base64) => {
					file.base64 = base64
					this.post(file)
				})

			} else {
				setTimeout(() => this.post(file), 0)
			}

		},
		post (file) {

			let _this = this

			if (!this._isMounted) {
				return
			}
			const {$props: props} = this

			new Promise((resolve, reject) => {

				const {uid} = file
				const service = typeof this.service === 'function'
					? this.service
					: this.service.upload

				let data = new FormData()

				let finallyFile = null

				if (this.blob === true) {
					if (file.base64) {
						const base64 = file.base64.replace(/^data:image\/\w+;base64,/, '')
						const binaryString = window.atob(base64)
						const len = binaryString.length
						const bytes = new Uint8Array(len)
						for (let i = 0; i < len; i++) {
							bytes[i] = binaryString.charCodeAt(i)
						}
						finallyFile = new File([bytes], file.name, {type: file.type || ''})
					} else {
						finallyFile = file
					}
				} else {
					finallyFile = file.base64 ? file.base64 : file
				}

				data.append(this.name || 'file', finallyFile)

				data.append('filename', file.name)

				each(this.param || {}, (value, key) => {
					data.append(key, value)
				})

//					// console.log(data)

				const requestOption = {
					filename: file.name,
					param: this.param,
					data: data,
					headers: this.headers,
					withCredentials: this.withCredentials,
					onUploadProgress: e => {
						_this.$emit('on-progress', e, file)
					},
				}

				this.reqs[uid] = service(requestOption).then((res, xhr) => {
					delete this.reqs[uid]
					_this.$emit('on-success', res, file, xhr)
				}).catch((err, res) => {
					delete this.reqs[uid]
					_this.$emit('on-error', err, res, file)
				})

				_this.$emit('on-start', file)

			})
		},
		reset () {
			this.setState({
				uid: uuid(),
			})
		},
		abort (file) {
			const {reqs} = this
			if (file) {
				let uid = file
				if (file && file.uid) {
					uid = file.uid
				}
				if (reqs[uid] && reqs[uid].abort) {
					reqs[uid].abort()
				}
				delete reqs[uid]
			} else {
				Object.keys(reqs).forEach(uid => {
					if (reqs[uid] && reqs[uid].abort) {
						reqs[uid].abort()
					}

					delete reqs[uid]
				})
			}
		},

		handleWrapperClick () {

			if (this.readonly) {
				return
			}

//				if (this.fastSelect) {
			this.onClick()
//				}
		},
		setState (state = {}, callback) {
			let newState = typeof state === 'function'
				? state(this.$data, this.$props)
				: state
			Object.assign(this.$data, newState)
			this.$nextTick(() => {
				callback && callback()
			})
		},
	},

	destroyed () {
	},

	mounted () {
		this._isMounted = true
	},
}
</script>

<style scoped>

</style>
