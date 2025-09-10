<template>
	<div class="tp-dialog">
		<fb-property class="tp-dialog-top">
			<fb-property bordered label-width="130px" mode="form">
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="参数名称">
							{{formData.pmName}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="参数key">
							{{formData.pmKey}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="参数值">
							{{formData.pmVal}}
						</fb-property-item>
					</fb-col>
			    </fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="参数配置描述">
							{{formData.pmDesc}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="排序号">
							{{formData.orderIndex}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="是否启用">
							{{formData.enabled}}
						</fb-property-item>
					</fb-col>
				</fb-row>
			</fb-property>
		</fb-property>
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
				service: app.$svc.sys.parameterconfig,

				formData: {
					pmId: '',
					pmKey: '',
					pmName: '',
					pmVal: '',
					pmDesc: '',
					orderIndex: '',
					actived: '',
					enabled: '',
					creator: '',
					extend01: '',
					extend02: '',
					extend03: '',
					createTime: '',
					updator: '',
					updateTime: '',
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
				this.service.view({"pmId": this.param.id, "passKey": this.param.passKey}).then((result) => {
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
