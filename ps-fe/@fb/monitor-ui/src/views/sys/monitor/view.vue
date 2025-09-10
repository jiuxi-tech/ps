<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-property bordered label-width="130px" mode="form">


				<fb-row>
					<fb-col span="12">
						<fb-property-item label="客户端id">
							{{formData.clientId}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="系统名称">
							{{formData.applicationName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="系统描述">
							{{formData.systemDesc}}
						</fb-property-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-property-item label="mac地址">
							{{formData.macAddr}}
						</fb-property-item>
					</fb-col>

				</fb-row>
				<fb-row>

					<fb-col span="24">
						<fb-property-item label="系统部署的绝对路径">
							{{formData.absolutePath}}
						</fb-property-item>
					</fb-col>
				</fb-row>

				<fb-row>

					<fb-col span="24">
						<fb-property-item label="ip链路">
							{{formData.ip}}
						</fb-property-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-property-item label="系统访问地址">
							{{formData.systemUrl}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="备注">
							{{formData.remark}}
						</fb-property-item>
					</fb-col>

				</fb-row>


				<fb-row>
					<fb-col span="24">
						<fb-property-item label="扩展字段01">
							{{formData.extend01}}
						</fb-property-item>
					</fb-col>

				</fb-row>
				<fb-row>

					<fb-col span="24">
						<fb-property-item label="扩展字段02">
							{{formData.extend02}}
						</fb-property-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-property-item label="扩展字段03">
							{{formData.extend03}}
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
				service: app.$svc.sys.monitor,

				formData: {
					clientId: '',
					applicationName: '',
					macAddr: '',
					absolutePath: '',
					systemDesc: '',
					ip: '',
					remark: '',
					systemUrl: '',
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
				this.service.view({"clientId": this.param.id, "passKey": this.param.passKey}).then((result) => {
					// 判断code
					if (result && result.code == 1) {
						this.formData = result.data
					} else {
						// 服务器返回失败
						this.$message.error('错误提示:' + result.message)
					}
				})
			},
		}
	}
</script>

<style lang="less" scoped>

</style>
