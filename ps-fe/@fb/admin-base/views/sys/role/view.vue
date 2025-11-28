<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">

			<fb-property bordered label-width="140px" mode="form">
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="角色名称">
							{{formData.roleName}}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="排序">
							{{formData.orderIndex}}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="角色介绍">
							{{formData.roleDesc}}
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
		name: 'role-view',
		mixins: [

		],
		// 接收父组件的传参
		props: {
			param: {
				type: Object,
				require: false
			},
			parentPage: {
				type: Object,
				default: null
			},
			meta: {
				type: Object,
				default: () => ({})
			}
		},
		// 组件
		components: {
			// 'component-a': ComponentA,

		},
		// 创建方法
		created() {
			// 记录原来的默认值，用于表单重置
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化
			this.init(this.param);
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.role,

				// 表单数据
				formData: {
					roleId: '',
					roleName: '',
					roleType: '',
					orderIndex: '',
					roleDesc: '',
				},
			}
		},

		// 方法
		methods: {

			// 设置标题
			init(param) {
				if (param.id) {
					let roleId = param.id;
					this.formData.roleId = roleId;
					this.view(roleId, param.passKey);
				}
			},

			// 取消
			handleClose() {
				// 关闭弹窗，不传递参数（表示未成功保存）
				this.closeTpDialog()
			},

			view(roleId, passKey) {
				// 调用新增service方法
				this.service.view({"roleId": roleId, "passKey": passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.formData = result.data
					} else {
						// 服务器返回失败
						this.$message.error('查询失败：' + result.message)
					}
				}).catch((err) => {
					// 服务器返回失败
					console.log(err);
				})
			},

		}
	}
</script>

<style lang="less" scoped>

</style>
