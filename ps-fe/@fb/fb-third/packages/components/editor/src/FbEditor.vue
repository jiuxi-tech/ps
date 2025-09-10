<script>

/**
 * FbEditor
 * (c) 2020 lincong1987
 */
import { prefix } from '../../../../src/config'
// ie10 兼容
import './weakmap-polyfill'
import './matchMedia'
import './matchMedia.addListener'
import * as tinymce from 'tinymce'

import zh_CN from './zh_CN'

// fb 样式
import '../tinymce/skins/ui/fb/skin.css'
import '../tinymce/skins/ui/fb/content.css'
import '../tinymce/skins/content/fb/content.css'
// 样式
// import '../tinymce/skins/ui/oxide/skin.css'
// import '../tinymce/skins/ui/oxide/content.css'
// import '../tinymce/skins/content/default/content.css'

import 'tinymce/icons/default'

//主题
// import 'tinymce/themes/silver'
// ie10 兼容
import './theme'
// 扩展插件
import 'tinymce/plugins/advlist'
//import 'tinymce/plugins/anchor'
import 'tinymce/plugins/autolink'
//import 'tinymce/plugins/autoresize'
import 'tinymce/plugins/autosave'
//import 'tinymce/plugins/bbcode'
import 'tinymce/plugins/charmap'
import 'tinymce/plugins/code'
import 'tinymce/plugins/codesample'
//import 'tinymce/plugins/colorpicker'
//import 'tinymce/plugins/contextmenu'
import 'tinymce/plugins/directionality'
//import 'tinymce/plugins/emoticons'
//import 'tinymce/plugins/fullpage'
import 'tinymce/plugins/fullscreen'
import 'tinymce/plugins/help'
//import 'tinymce/plugins/hr'
import 'tinymce/plugins/image'
//import 'tinymce/plugins/imagetools'
//import 'tinymce/plugins/importcss'
//import 'tinymce/plugins/insertdatetime'
//import 'tinymce/plugins/legacyoutput'
import 'tinymce/plugins/link'
import 'tinymce/plugins/lists'
import 'tinymce/plugins/media'
import 'tinymce/plugins/nonbreaking'
//import 'tinymce/plugins/noneditable'
import 'tinymce/plugins/pagebreak'
//import 'tinymce/plugins/paste'
import 'tinymce/plugins/preview'
//import 'tinymce/plugins/print'
import 'tinymce/plugins/quickbars'
import 'tinymce/plugins/save'
import 'tinymce/plugins/searchreplace'
//import 'tinymce/plugins/spellchecker'
//import 'tinymce/plugins/tabfocus'
import 'tinymce/plugins/table'
//import 'tinymce/plugins/template'
//import 'tinymce/plugins/textcolor'
//import 'tinymce/plugins/textpattern'
//import 'tinymce/plugins/toc'
import 'tinymce/plugins/visualblocks'
import 'tinymce/plugins/visualchars'
import 'tinymce/plugins/wordcount'

//setTimeout(() => {
//	//
//
////	tinymce.util.I18n.setCode("zh_CN")
//
////	tinymce.util.I18n
//
//}, 0)

/**
 * 注：编辑器二次刷新处理
 * 编辑器二次刷新具体效果为输入光标重置到第一行第一个字前。
 * 这种效果根本无法正常录入，其原因是双向绑定数据导致编辑器数据更新所致。
 * 根据编辑器的不同状态做标记，当标记为`INPUT`录入时，数据将不会更新至编辑器，
 * 从而避免二次更新的情况，具体请看`value`部分和`editor event`部分的代码。
 * */

const INIT = 0
const INPUT = 1
const CHANGED = 2

const status = ['INIT', 'INPUT', 'CHANGED']
const changedLog = deBug => {
	if (!deBug) return () => false
	return (e, _status, val, oldVal) => console.log(`来自：%s | 状态：%s \n %s \n %s`, e.type, status[_status], val, oldVal)
}

export default {
	name: 'FbEditor',
	props: {
		value: {
			type: String,
			default: '',
		},
		data: {
			type: [String, Object],
			default: null,
		},
		setup: {
			type: Function,
			default: function () {
			},
		},
		service: {
			type: [Function, Object],
			default: null,
		},

		fileReader: {
			type: Function,
			default: function (file) {
				return file
			},
		},

		param: {
			type: Object,
			default () {
				return {}
			},
		},

		disabled: {
			type: Boolean,
			default: false,
		},
		options: {
			type: Object,
			default: function () {
				return {
					automatic_uploads: true,
					images_reuse_filename: true,
					content_css: '/tinymce/skins/ui/oxide/content.css',
					branding: false,
//					menubar: true,
//					toolbar: 'undo redo | fullscreen | formatselect alignleft aligncenter alignright alignjustify | link unlink | numlist bullist | image media table | fontselect fontsizeselect forecolor backcolor | bold italic underline strikethrough | indent outdent | superscript subscript | removeformat |',
//					toolbar_drawer: 'sliding',
//					quickbars_selection_toolbar: 'removeformat | bold italic underline strikethrough | fontsizeselect forecolor backcolor',
//					plugins: 'link image media table lists fullscreen quickbars',
					plugins: '' +
						'print preview paste importcss searchreplace ' +
						'autolink autosave save directionality code visualblocks visualchars fullscreen' +
						' image link media  codesample table charmap hr pagebreak nonbreaking ' +
						' toc insertdatetime advlist lists wordcount imagetools textpattern ' +
						' charmap',

					// anchor template
//					imagetools_cors_hosts: ['picsum.photos'],
					menubar: 'file edit view insert format tools table help',
					toolbar: 'undo redo | bold italic underline strikethrough | fontselect fontsizeselect formatselect | alignleft aligncenter alignright alignjustify | outdent indent |  numlist bullist | forecolor backcolor removeformat | pagebreak | charmap emoticons | fullscreen  preview save print | insertfile image media template link anchor codesample | ltr rtl',
					toolbar_sticky: true,
					autosave_ask_before_unload: true,
					autosave_interval: '30s',
					autosave_prefix: '{path}{query}-{id}-',
					autosave_restore_when_empty: false,
					autosave_retention: '2m',
					image_advtab: true,
//					link_list: [
//						{ title: 'My page 1', value: 'https://www.tiny.cloud' },
//						{ title: 'My page 2', value: 'http://www.moxiecode.com' }
//					],
//					image_list: [
//						{ title: 'My page 1', value: 'https://www.tiny.cloud' },
//						{ title: 'My page 2', value: 'http://www.moxiecode.com' }
//					],
//					image_class_list: [
//						{ title: 'None', value: '' },
//						{ title: 'Some class', value: 'class-name' }
//					],
					importcss_append: true,
//					file_picker_callback: function (callback, value, meta) {
//						/* Provide file and text for the link dialog */
//						if (meta.filetype === 'file') {
//							callback('https://www.google.com/logos/google.jpg', { text: 'My text' });
//						}
//
//						/* Provide image and alt text for the image dialog */
//						if (meta.filetype === 'image') {
//							callback('https://www.google.com/logos/google.jpg', { alt: 'My alt text' });
//						}
//
//						/* Provide alternative source and posted for the media dialog */
//						if (meta.filetype === 'media') {
//							callback('movie.mp4', { source2: 'alt.ogg', poster: 'https://www.google.com/logos/google.jpg' });
//						}
//					},
					templates: [
//						{ title: 'New Table', description: 'creates a new table', content: '<div class="mceTmpl"><table width="98%"  border="0" cellspacing="0" cellpadding="0"><tr><th scope="col"> </th><th scope="col"> </th></tr><tr><td> </td><td> </td></tr></table></div>' },
//						{ title: 'Starting my story', description: 'A cure for writers block', content: 'Once upon a time...' },
//						{ title: 'New list with dates', description: 'New List with dates', content: '<div class="mceTmpl"><span class="cdate">cdate</span><br /><span class="mdate">mdate</span><h2>My List</h2><ul><li></li><li></li></ul></div>' }
					],
					template_cdate_format: '[Date Created (CDATE): %m/%d/%Y : %H:%M:%S]',
					template_mdate_format: '[Date Modified (MDATE): %m/%d/%Y : %H:%M:%S]',
					height: 400,
//					image_caption: true,
					quickbars_selection_toolbar: 'bold italic | quicklink h2 h3 blockquote quickimage quicktable',
//					noneditable_noneditable_class: 'mceNonEditable',
//					toolbar_mode: 'sliding',
					contextmenu: 'link image imagetools table',
//					content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }'

				}
			},
		},
		deBug: {
			type: Boolean,
			default: false,
		},
		quality: {
			type: Number,
			default: 0.7,
		},

		maxWidth: {
			type: [Number, Boolean],
			default: 1200,
		},

		maxHeight: {
			type: [Number, Boolean],
			default: 800,
		},
	},
	render (h) {
		if (typeof tinymce === 'undefined') {
			return h('div', 'tinymce 库不存在')
		}
		return h('div', {
			attrs: {
				id: this.id,
			},
		})
	},
	data () {
		return {
			id: `${prefix}-editor-${new Date().getTime()}`,
			editor: null,
			status: INIT,
			backup: '',
			content: this.data,
		}
	},
	watch: {
		value (val, oldVal) {

			this.changedLog({type: 'propsChanged'}, this.status, val, '--')
			if (this.status === INPUT) return
			if (!this.editor || !this.editor.initialized) return // fix editor plugin is loading and set content will throw error.
			if (val === null) return this.resetContent('')
			this.setContent(val)
		},

		disabled (val) {
			this.editor.setMode(val ? 'readonly' : 'design')
		},
	},
	created () {
		this.changedLog = changedLog(this.deBug)
		if (typeof tinymce === 'undefined') throw new Error('tinymce undefined')
	},

	mounted () {
		this.render()
		this.editor.targetElm = this.$el
		this.editor.render()
	},
	updated () {
		this.editor.render()
	},
	activated: function () {
		if (this.editor) return
		this.render()
		this.editor.targetElm = this.$el
		this.editor.render()
	},
	deactivated () {
		this.editor.remove()
		this.editor = null
	},
	destroyed () {
		if (this.editor) {
			this.editor.remove()
		}
	},

	beforeDestroy () {
		if (this.editor) {
			this.editor.remove()
			this.editor = null
		}
	},

	methods: {
		render () {
			let me = this

			const setting = Object.assign({},
				this.options,
				{
					selector: '#' + this.id,
					setup: (editor) => {
						this.setup(editor)
						// console.log('setup');
						editor.on('init', () => {
							// console.log('init', this.content);
							this.setContent(this.value, editor)

							editor.on('keyup input', e => { //只在编辑器中打字才会触发
								this.status = INPUT       //编辑器录入文字时标记为`INPUT`状态
							})
							editor.on('SetContent', e => { //编辑器在插入图片和撤销/重做时触发，组件content更新数据也会导致触发
								this.changedLog(e, this.status, editor.getContent(), '--')
							})
							editor.on('Blur', e => {
								this.status = INIT
								this.changedLog(e, this.status, editor.getContent(), '--')
							})
							editor.on('input keyup Change Undo Redo ExecCommand NodeChange', e => {
								this.onChanged(e, editor)
							})
						})
					},

//				file_picker_callback: function(callback, value, meta) {
//
//					// Provide file and text for the link dialog
//					if (meta.filetype == 'file') {
//						callback('mypage.html', {text: 'My text'});
//					}
//
//					// Provide image and alt text for the image dialog
//					if (meta.filetype == 'image') {
//						callback('myimage.jpg', {alt: 'My alt text'});
//					}
//
//					// Provide alternative source and posted for the media dialog
//					if (meta.filetype == 'media') {
//						callback('movie.mp4', {source2: 'alt.ogg', poster: 'image.jpg'});
//					}
//				},
					images_upload_credentials: true,
					images_reuse_filename: false,
					images_upload_handler: function (blobInfo, success, failure) {
						me.compress(blobInfo.blob()).then((res) => {
							success(res)
						})
						// console.log('data:image/jpg;base64,' + blobInfo.base64())
						// success(`data:image/jpeg;base64,${blobInfo.base64()}`)

						// let service = typeof me.service === 'function' ? me.service : me.service.upload
						//
						// let file = me.fileReader(Object.assign({}, {
						// 	base64: blobInfo.base64(),
						// 	filename: blobInfo.filename(),
						// 	name: blobInfo.name(),
						// 	file: blobInfo.blob(),
						// 	blobUri: blobInfo.blobUri(),
						// }, me.param))
						//
						// service(file).then(json => {
						// 	success(json.url)
						// }).catch(e => {
						// 	failure('上传错误')
						// })
					},

				},
			)

			this.editor = tinymce.createEditor(setting.selector, setting)

			tinymce.addI18n('en', zh_CN)
		},

		compress (file, type) {
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

						let compressed = canvas.toDataURL('image/jpeg', this.quality)

						resolve(compressed)
					}

				}
				reader.onerror = (e) => {
					reject(e)
				}
			})
		},

		setContent (val, editor) {

			val = val || ''
			if (!editor) editor = this.editor

			return editor.setContent(val)
		},
		resetContent (val, editor) {
			if (!editor) editor = this.editor
			if (!!editor.resetContent) return editor.resetContent(val)
			editor.setContent(val)
			editor.setDirty(false)
			editor.undoManager.clear()
		},
		onChanged (e, editor) {
			if (!editor) editor = this.editor
			const value = editor.getContent()
			this.changedLog(e, this.status, value, '--')
			this.$emit('on-change', value)
			this.$emit('input', value)
		},
	},
}


</script>


<style scoped>

</style>



