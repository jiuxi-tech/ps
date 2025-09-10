<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="120">
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="行政区划code" prop="cityCode" :rule="[{required: true}]">
							<fb-input v-model="formData.cityCode" placeholder="请输入行政区划code"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="行政区划名称" prop="cityName" :rule="[{required: true}]">
							<fb-input v-model="formData.cityName" placeholder="请输入行政区划名称"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="行政区划全称" prop="cityFullName" :rule="[{required: true}]">
							<fb-input v-model="formData.cityFullName" placeholder="请输入行政区划全称"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="行政区划简称">
							<fb-input v-model="formData.citySimpleName" placeholder="请输入行政区划简称"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="排序">
							<fb-input v-model="formData.orderIndex" placeholder="请输入排序号"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="是否启用">
							<fb-radio-group v-model="formData.enabled"
											:data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
											:reader="{label:'name', value:'id'}"></fb-radio-group>
						</fb-form-item>
					</fb-col>
				</fb-row>
			</fb-form>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="add">保存</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>


	export default {
		name: 'add',
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
				service: this.$svc.sys.city,
				// 行政区划详情参数
				formData: {
					cityId: '',
					pcityId: '',
					cityCode: '',
					cityName: '',
					cityFullName: '',
					citySimpleName: '',
					orderIndex: '',
					enabled: 1,
				},
			}
		},

		// 方法
		methods: {

			// 设置标题
			init(param) {
				if (param.id) {
					let cityId = param.id;
					this.formData.cityId = cityId;
					this.view(cityId);
				}
				if (param.pcityId) {
					this.formData.pcityId = param.pcityId
				}
			},

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog();
			},
			// 新增
			add() {
				let that = this
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						if (that.formData.cityId) {
							// 修改
							this.service.update(that.formData).then((result) => {
								// 判断code
								if (result.code == 1) {
									this.$message.success('修改成功');

									this.handleClose();
								} else {
									this.$message.error('修改失败:' + result.data.message)
								}
							})
						} else {
							// 新增，调用新增service方法
							this.service.add(that.formData).then((result) => {
								// 判断code
								if (result.code == 1) {
									this.$message.success('新增成功');

									this.closeTpDialog(result.data);
								} else {
									// 服务器返回失败
									this.$message.error('新增失败' + result.data.message)
								}
							})
						}
					}
				})

			},

			view() {
				// 调用新增service方法
				this.service.view({"cityId": this.formData.cityId}).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.formData = result.data
					} else {
						// 服务器返回失败
						this.$message.error('查询失败' + result.data.message)
					}
				})
			},

		}
	}
</script>

<style lang="less" scoped>

</style>
