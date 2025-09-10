<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-property bordered label-width="130px" mode="form">

				<fb-row>
					<fb-col span="12">
						<fb-property-item label="记录id">
							{{formData.recordId}}
						</fb-property-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-property-item label="收件人姓名">
							{{formData.personName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="收件人邮箱">
							{{formData.email}}
						</fb-property-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-property-item label="发送时间">
							{{formatterTime(formData.sendTime)}}
						</fb-property-item>
					</fb-col>

				</fb-row>

				<fb-row>

					<fb-col span="12">
						<fb-property-item label="发送状态">
							{{formatterStatus(formData.status)}}
						</fb-property-item>
					</fb-col>
				</fb-row>

				<fb-row>

					<fb-col span="24">
						<fb-property-item label="发送内容">
							{{formData.message}}
						</fb-property-item>
					</fb-col>
				</fb-row>

			</fb-property>
		</div>
		<div class="tp-dialog-bottom">
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>
	import dayjs from "dayjs";
	export default {
		name: 'core_view',

		// 接收父组件的传参
		props: {
			param: {
				type: Object,
				require: false
			},
			parentPage: {
				type: Object,
				default: null
			}
		},
		// 组件
		components: {

		},
		// 创建方法
		created() {
			// 记录原来的默认值，用于表单重置
		},
		// 初始化方法
		mounted() {
			this.init(this.param);
		},
		data() {
			return {
				// 请求的 service
				service: app.$svc.sys.mailrecord,

				formData: {
					recordId: '',
					personName: '',
					email: '',
					sendTime: '',
					message: '',
					status: '',
					tenantId: '',
					actived: '',
					creator: '',
					createTime: '',
					updator: '',
					updateTime: '',
					extend01: '',
					extend02: '',
					extend03: '',
					extend04: '',
					extend05: '',
				},
			}
		},

		// 方法
		methods: {
			init() {
				this.view();
			},
			// 取消
			handleClose() {
				let param = {};
				this.closeTpDialog(param);
			},
			// 查看
			view() {
				// 调用新增service方法
				this.service.view({"recordId": this.param.id, "passKey": this.param.passKey}).then((result) => {
					// 判断code
					if (result && result.code == 1) {
						this.formData = result.data
					} else {
						// 服务器返回失败
						this.$message.error('错误提示:' + result.message)
					}
				})
			},
            formatterTime(val) {
				return val ? dayjs(val).format('YYYY-MM-DD HH:mm:ss') : val;
			},
			formatterStatus(val) {
				return val === 1 ? '成功' : '失败'
			}
		}
	}
</script>

<style lang="less" scoped>

</style>
