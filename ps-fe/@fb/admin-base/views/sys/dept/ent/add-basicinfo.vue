<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="120">
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="部门全称" prop="deptFullName" :rule="[{required: true}]">
							<fb-input v-model="formData.deptFullName" placeholder="请输入部门全称"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="部门简称" prop="deptSimpleName" :rule="[{required: true}]">
							<fb-input v-model="formData.deptSimpleName" type="text" placeholder="请输入部门简称"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="部门编号">
							<fb-input v-model="formData.deptNo" placeholder="请输入部门编号"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="部门类型" prop="deptType" :rule="[{required: true}]">
							<fb-select v-model="formData.deptType" :service="$svc.sys.dict.select"
									   :param="{'pdicCode': 'SYS05'}" :placeholder="'请选择'" clearable/>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="12">
						<fb-form-item label="部门联系人">
							<fb-input type="text" v-model="formData.principalName" placeholder="请输入部门联系人"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="部门联系电话" :rule="{type: 'telmobile'}">
							<fb-input v-model="formData.principalTel" placeholder="请输入部门联系电话"></fb-input>
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
						<fb-form-item label="行政区划" prop="cityCode" :rule="[{required: true}]">
							<fb-tree-select
								ref="cityTree"
								v-model="formData.cityCode"
								:service="$svc.sys.city.tree"
								:param="{'sync': 1, 'expand': false, 'cityId': '-1'}"
								:reader="{value:'extend02', label: 'text'}"
								:placeholder="'请选择'"
								:header-format="cityTreeHeaderFormat"
								clearable>
							</fb-tree-select>
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

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="部门描述">
							<fb-textarea :rows="rows" v-model="formData.deptDesc" type="text" placeholder="请输入内容"
										 :maxlength="200">
							</fb-textarea>
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
		name: 'add-basicinfo',
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
				service: this.$svc.sys.dept,

				// 文本域行数
				rows: 2,
				// 表单数据
				formData: {
					deptId: '',
					pdeptId: '',
					// 部门编号
					deptNo: '',
					deptFullName: '',
					deptSimpleName: '',
					deptType: '部门',
					cityCode: '',
					principalName: '',
					principalTel: '',
					orderIndex: 0,
					enabled: 1,
					ascnId: '',
					deptDesc: '',
				},
			}
		},

		// 方法
		methods: {
			// 设置标题
			init(param) {
				// deptId赋值
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
				// 隐藏
				this.closeTpDialog(this.formData.deptId);
			},
			// 新增
			add() {
				// 界面校验
				let that = this;
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						if (that.formData.deptType === 'SYS0501') {
							// 新增/修改 成单位，后端自动填充它的deptId做为ascnId值
							that.formData.ascnId = '';
						}
						
						// 过滤掉null值和无效参数，避免参数绑定失败
						let submitData = {};
						Object.keys(that.formData).forEach(key => {
							let value = that.formData[key];
							// 只提交非null、非undefined、非空字符串的值
							if (value !== null && value !== undefined && value !== '') {
								submitData[key] = value;
							}
						});
						
						if (this.formData.deptId) {
							// 修改
							this.service.update(submitData).then((result) => {
								// 判断code
								if (result.code == 1) {
									that.$message.success('修改成功');
								} else {
									// 服务器返回失败
									//console.log(result.data.message);
									that.$message.error('修改失败')
								}
							});
							that.updateCount = 0;
						} else {
							// 新增 调用新增service方法
							this.service.ent.add(submitData).then((result) => {
								// 判断code
								if (result.code == 1) {
									that.$message.success('新增成功');

									let deptId = result.data.deptId;
									this.formData.deptId = deptId;
									this.setPageParam(deptId);
								} else {
									// 服务器返回失败
									that.$message.error('新增失败')
								}
								that.updateCount = 0;
							})
						}
					}
				})
			},

			view(deptId) {
				// 调用新增service方法
				this.service.view({"deptId": deptId}).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.formData = result.data
					} else {
						// 服务器返回失败
						//console.log(result.data.message);
						this.$message.error('查询失败')
					}

				})
			},

		}
	}
</script>

<style lang="less" scoped>

</style>
