<!--

使用说明：


<tp-upload view="avatar" v-model="formData.list"  :service="$svc.sys.file" :accept="'.png,.jpeg,.jpg'" :param="{'downFileName':'涉爆粉尘企业风险监测预警系统接入规范-tx20211027(1).docx'}"  relaPath='' multiple></tp-upload>

-->

<template>
	<div>
		<fb-upload
			:accept="accept"
			:avatarCircle="avatarCircle"
			:avatarSize="avatarSize"
			:button-text="buttonText"
			:file-list="formData.list"
			:maxHeight="maxHeight"
			:maxLength="maxLength"
			:maxWidth="maxWidth"
			:multiple="multiple"
			:param="param"
			:quality="quality"
			:readonly="readonly"
			:service="service"
			:show-download="showDownload"
			:show-preview="defaultShowPreview"
			:show-remove="showRemove"
			:view="view"
			@on-change="fileListChange"
			@on-error="onError"
			@on-progress="onProgress"
			@on-reject="onReject"
			@on-remove="onRemove"
			@on-start="onStart"
			@on-success="onSuccess"
			ref="tp_upload-path"
			v-show="showUpload"
			>
		</fb-upload>
	</div>
</template>

<script>
	export default {
		name: "TpUploadPath",
		mixins: [],
		// 接收父组件的传参
		props: {
			value: {
				type: [Array],
				require: false
			},

			view: {
				type: String,
				default: 'list',
				require: false
			},

			service: {
				type: [Object, Function],
				default() {
					return () => {
					}
				},
			},

			param: {
				type: Object,
				default() {
					return {}
				},
			},

			accept: {
				type: String,
				default: '',
			},

			// 多选
			multiple: {
				type: Boolean,
				default: false,
			},


			maxLength: {
				type: Number,
				default: 1,
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
				default: true,
			},

			avatarSize: {
				type: [Number],
				default: 120,
			},

			avatarCircle: {
				type: Boolean,
				default: true,
			},

			relaPath: {
				type: String,
				default: '',
				require: false
			},

			showPreview:{
				type: Boolean,
				default: false,
			},


			showDownload:{
				type: Boolean,
				default: true,
			},

			showRemove:{
				type: Boolean,
				default: true,
			},

			buttonText: {
				type: String,
				default: '',
				require: false
			},

			showUploadList: {
				type: Boolean,
				default: true,
			},

			showUpload: {
				type: Boolean,
				default: true,
			},

		},
		model: {
			prop: 'value',
			event: 'listener' //这个事件名可以随意写，它实际上是规定了子组件要更新父组件值需要注册的方法
		},
		// 组件
		components: {
			// 'component-a': ComponentA,
		},
		// 创建方法
		created() {

		},
		// 初始化方法
		mounted() {
			let viewType = this.view

			// || viewType === 'avatar' 头像不支持放大
			if (viewType === "image") {
				this.defaultShowPreview = true;
			}
		},
		data() {
			return {
				formData: {
					list: []
				},
				defaultShowPreview: false,
			}
		},
		// 方法
		methods: {
			clear(info){
				this.$refs.tp_upload.myFileList = [];
			},
			onStart(info){
				this.$emit('on-start', info);
			},
			onProgress(info){
				this.$emit('on-progress', info);
			},
			onSuccess(info){
				this.$emit('on-success', info);
			},
			onError(info){
				this.$emit('on-error', info);
			},
			onReject(info){
				this.$emit('on-reject', info);
			},
			onRemove(info){
				this.$emit('on-remove', info);
			},

			formatFile(list) {
				return list.map(item => {
					return {'attachId': item.savePath}
				});
			},

			fileListChange(info) {

				if (this.view == 'avatar') {
					if (info.file.res) {
						this.formData.list = [info.file.res.data];
						this.$emit('listener', this.formatFile(this.formData.list));
					}
				} else if (this.view == 'image') {
					this.formData.list = info.fileList;
					this.$emit('listener', this.formatFile(this.formData.list));
				} else {
					this.formData.list = info.fileList;
					this.$emit('listener', this.formatFile(this.formData.list));
				}

				this.$emit('on-change', info);
			},
		},
		watch: {
			/**
			 * 监听父组件的值的变化, 第一种用法watch有一个特点，就是当值第一次绑定的时候，不会执行监听函数，
			 * 只有值发生改变才会执行。如果我们需要在最初绑定值的时候也执行函数，则就需要用到immediate属性。
			 * @param newValue
			 * @param oldValue
			 */
			value: {
				handler(newValue, oldValue) {
					// 回显的时候，如果referId有值，下载附件进行回显
					if (newValue) {
						this.formData.list = this.value;
					}
				},
				// 立即生效
				immediate: true
			},

		}
	}
</script>

<style scoped>

</style>
