<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="120">
				<fb-row>
					<fb-col span="12" v-if="pdicCode !== ''">
						<fb-form-item label="父级字典编码">
							<fb-input v-model="pdicCode" readonly></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="字典编码" prop="dicCode" :rule="[{required: true}]">
							<fb-input placeholder="请输入字典编码" v-model="formData.dicCode">
							</fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="字典名称" prop="dicName" :rule="[{required: true}]">
							<fb-input v-model="formData.dicName" placeholder="请输入字典名称"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="字典类型" prop="dicType" :rule="[{required: true}]">
							<fb-select v-model="formData.dicType"
									   :data="[
													{value:'-1', label: '系统'},
													{value:'1', label: '业务'}
												]"
									   :placeholder="'请选择'"/>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="排序">
							<fb-input v-model.number="formData.orderIndex" placeholder="请输入排序号"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="字典描述">
							<fb-textarea rows="2"
										 v-model="formData.dicDesc"
										 type="text"
										 placeholder="请输入内容"
										 :maxlength="200"></fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
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
			<fb-button style="margin-right: 12px" type="primary" v-if="pdicCode !== ''" @on-click="keepAdd">保存并新增</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>


	export default {
		name: 'save',
		mixins: [

		],
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
		components: {},
		// 创建方法
		created() {
			// 记录原来的默认值，用于表单重置
			console.log("执行界面Created方法...")
			this.createdPage();
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化方法
			this.init(this.param);
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.dict,
				// 父字典code
				pdicCode: '',
				// 部门详情参数
				formData: {
					dicId: '',
					pdicId: '',
					dicCode: '',
					dicName: '',
					dicType: '',
					orderIndex: '',
					dicDesc: '',
					enabled: 1,
				},
			}
		},

		// 方法
		methods: {
			init(param) {
				if (param.id) {
					// 修改
					this.formData.dicId = param.id;
					this.handleView(param.id);
				} else if (param.dicCode) {
					// 新增下级
					this.formData.pdicId = param.pdicId;
					this.pdicCode = param.dicCode;
				} else {
					// 新增根节点
					this.formData.pdicId = -1;
				}
			},

			// 保存节点数据
			add() {
				let that = this;
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						this.service.save(this.formData).then((res) => {
							// 判断code
							if (res.code == 1) {
								if (res.data.pdicId === '-1'){
									// 根节点，刷新列表
									that.closeTpDialog(-1);
								} else {
									// 子节点，刷新树
									that.closeTpDialog(that.pdicCode);
								}
								that.$message.success('保存成功');
							} else {
								// 服务器返回失败
								that.$message.error('保存失败：' + res.message)
							}
						})
					}
				})
			},

			// 保存节点数据
			keepAdd() {
				let that = this;
				let pdicCode = that.pdicCode;
				let pdicId = that.formData.pdicId;
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						this.service.save(this.formData).then((res) => {
							// 判断code
							if (res.code == 1) {
								// 重置表单
								that.formReset();
								that.pdicCode = pdicCode;
								that.formData.pdicId = pdicId;
								that.formData.orderIndex = res.data.orderIndex + 1;

								that.$message.success('保存成功，请继续');
							} else {
								// 服务器返回失败
								that.$message.error('保存失败：' + res.message)
							}
						})
					}
				})
			},

			// 查看方法
			handleView(dicId) {
				let that = this;
				this.service.view({"dicId": dicId}).then((result) => {
					// 判断code
					if (result.code == 1) {
						that.formData = result.data
					} else {
						// 服务器返回失败
						that.$message.error('查询失败')
					}
				})
			},

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog();
			},

		}
	}
</script>

<style lang="less" scoped>

</style>
