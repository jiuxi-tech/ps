<!--

使用说明：


<tp-upload view="avatar" v-model="formData.list"  :service="$svc.sys.file" :accept="'.png,.jpeg,.jpg'" :param="{'referType':'SYS1014'}"  referid='' multiple></tp-upload>

-->

<template>
	<div>
		<fb-upload
			ref="tp_upload"
			:view="view"
			:service="service"
			:file-list="formData.list"
			:param="param"
			:accept="accept"
			:multiple="multiple"
			:maxLength="maxLength"
			:quality="quality"
			:maxWidth="maxWidth"
			:maxHeight="maxHeight"
			:readonly="readonly"
			:avatarSize="avatarSize"
			:avatarCircle="avatarCircle"
			:button-text="buttonText"
			:show-preview="defaultShowPreview"
			:show-download="showDownload"
      :show-remove="showRemove"
      :drag="drag"
      :paste="paste"
      :noCompress="noCompress"
			v-show="showUpload"
			@on-change="fileListChange"
			@on-start="onStart"
			@on-progress="onProgress"
			@on-success="onSuccess"
			@on-error="onError"
			@on-reject="onReject"
			@on-remove="onRemove"
			>
      <template v-slot:default>
        <slot></slot>
      </template>
		</fb-upload>
	</div>
</template>

<script>
	export default {
		name: "TpUpload",
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
				default: false,
			},

			avatarSize: {
				type: [Number],
				default: 120,
			},

			avatarCircle: {
				type: Boolean,
				default: true,
			},

			referid: {
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

      drag: {
        type: Boolean,
        default: false,
      },

      paste: {
        type: Boolean,
        default: false,
      },
      noCompress: {
        type: Boolean,
        default: false,
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
					list: this.value || []
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
					return {'attachId': item.attachId}
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

			download(newValue) {
				let that = this;
				// referid 不为空，根据referid和refertype下载附件
				app.$svc.sys.file.byReferId({'referId': newValue, 'referType': this.param.referType}).then((result) => {
					if (result.code == 1) {
						// 返回的附件有附件数据，附件回显
						that.formData.list = result.data
						that.$emit('listener', that.formatFile(that.formData.list));
					} else {
						// 服务器返回失败
						that.$message.error('附件查询失败: ' + result.message)
					}
				});
			}
		},
		watch: {
			/**
			 * 监听父组件的值的变化, 第一种用法watch有一个特点，就是当值第一次绑定的时候，不会执行监听函数，
			 * 只有值发生改变才会执行。如果我们需要在最初绑定值的时候也执行函数，则就需要用到immediate属性。
			 * @param newValue
			 * @param oldValue
			 */
			referid: {
				handler(newValue, oldValue) {
					// 回显的时候，如果referId有值，下载附件进行回显
					if (newValue) {
						this.download(newValue);
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
