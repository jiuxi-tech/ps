<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="120">
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="经度">
							<fb-input v-model="formData.longitude" @on-click="longitude()" readonly></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="纬度">
							<fb-input v-model="formData.latitude" @on-click="longitude()" readonly></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="地址">
							<fb-input v-model="formData.address" type="text" placeholder="请输入内容"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="Email">
							<fb-input
								type="text"
								v-model="formData.email"
								placeholder="请输入email">
							</fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

			</fb-form>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="add">保存</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>

		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
	</div>
</template>

<script>


	export default {
		name: 'add-exinfo',
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
			}
		},
		// 创建方法
		created() {
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化
			this.init(this.param);
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.dept,

				// 文本域行数
				rows: 2,
				// 表单数据
				formData: {
					deptId: '',
					// 行政区划ID
					cityId: '',
					// 租户id
					tenantId: '',
					// 机构坐标_经度
					longitude: '',
					// 机构坐标_纬度
					latitude: '',
					// 地址
					address: '',
					// 部门邮箱
					email: '',
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
			// 初始化参数
			init(param) {
				if (param.id) {
					// 修改
					let deptId = param.id;
					this.formData.deptId = deptId;
					this.view(deptId);
				} else {
					// 新增
					this.formData.pdeptId = param.pdeptId;
					this.formData.ascnId = param.ascnId;
				}
			},

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog(this.formData.deptId);
			},
			// 新增
			add() {
				// 界面校验
				let that = this;
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						if (!that.formData.deptId) {
							if (JSON.stringify(that.getPageParam()) == "{}") {
								this.$message.error('请先保存部门基本信息！')
								return;
							}
							that.formData.deptId = that.getPageParam();
						}
						that.service.expAdd(that.formData).then((result) => {
							// 判断code
							if (result.code == 1) {
								that.$message.success('保存成功');
							} else {
								// 服务器返回失败
								that.$message.error('保存失败')
							}
						})
					}
				})
			},

			view(deptId) {
				// 调用新增service方法
				let that = this;
				this.service.expView({"deptId": deptId}).then((result) => {
					// 判断code
					if (result.code == 1) {
						if (result.data !== "") {
							that.formData = result.data
						}
					} else {
						// 服务器返回失败
						//console.log(result.data.message);
						this.$message.error('查询失败')
					}

				})
			},

			longitude() {
				var param = {'longitude': this.formData.longitude, 'latitude': this.formData.latitude}
				this.$refs.TpDialog.show(import('../../../../views/common/map/pickup-map.vue'), param, "天地图");
			},

			closeDialog(param) {
				// 坐标回显
				if (param) {
					this.formData.longitude = param.longitude;
					this.formData.latitude = param.latitude;
				}
			},
		}
	}
</script>

<style lang="less" scoped>

</style>
