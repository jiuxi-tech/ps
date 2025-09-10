<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform">
				<fb-row>
					<fb-col span="12">
						<fb-form-item label="角色名称" prop="roleName" :rule="[{required: true}]">
							<fb-input v-model="formData.roleName" placeholder="请输入角色名称"></fb-input>
						</fb-form-item>
					</fb-col>
					<fb-col span="12">
						<fb-form-item label="排序" prop="orderIndex" :rule='{required: true}'>
							<fb-input v-model="formData.orderIndex" placeholder="请输入排序号"></fb-input>
						</fb-form-item>
					</fb-col>
				</fb-row>

				<fb-row>
					<fb-col span="24">
						<fb-form-item label="角色描述" prop="resume" :rule="[{roleDesc: true}]">
							<fb-textarea rows="2" v-model="formData.roleDesc"
										 type="text"
										 placeholder="请输入内容"
										 :maxlength="200">
							</fb-textarea>
						</fb-form-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12" style="display: none;">
						<fb-form-item label="角色类型">
							<fb-radio-group v-model="formData.roleType"
											:data="[{id: 0, name: '政府',}, {id: 1, name: '企业',}]"
											:reader="{label:'name', value:'id'}">
							</fb-radio-group>
						</fb-form-item>
					</fb-col>
					<fb-col span="12" v-if="createRoleFlag">
						<!-- 是以哪个角色的身份创建的该角色 -->
						<fb-form-item label="指定角色" prop="orderIndex" :rule='{required: true}'>
							<fb-select v-model="formData.createRole"
									   :service="$svc.sys.person.personRoles"
									   :param="createRoleParam"
									   :reader="{value:'roleId', label: 'roleName'}"
									   :placeholder="'请选择'"
									   clearable/>
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
			// 获取当前用户拥有的角色
			let currentUser = this.$datax.get('user');
			if (currentUser) {
				if (currentUser.roleIds === '') {
					// 超级管理员
					this.formData.createRole = '-1';
				} else if (currentUser.roleIds.length === 19) {
					// 普通用户，并且只用有一个角色
					this.formData.createRole = currentUser.roleIds;
				} else {
					// 普通用户，但是拥有多个角色，指定以那个角色身份创建的
					this.createRoleFlag = true;
					this.createRoleParam.deptId = currentUser.deptIds;
					this.createRoleParam.personId = currentUser.personId;
				}
			}
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.role,
				createRoleFlag: false,
				createRoleParam: {
					deptId: '',
					personId: '',
				},
				// 表单数据
				formData: {
					roleId: '',
					roleName: '',
					roleType: 0,
					orderIndex: '',
					roleDesc: '',
					createRole: '',
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
				// 关闭，并传递参数
				this.closeTpDialog("xxxx");
			},
			// 新增
			add() {
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						if (this.formData.roleId) {
							// 修改
							this.formData.passKey = this.param.passKey;
							this.service.update(this.formData).then((result) => {
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
							this.service.org.add(this.formData).then((result) => {
								// 判断code
								if (result.code == 1) {
									this.$message.success('新增成功');

									this.handleClose();
								} else {
									// 服务器返回失败
									this.$message.error('新增失败' + result.data.message)
								}
							})
						}
					}
				})

			},

			view(roleId, passKey) {

				// 调用新增service方法
				this.service.view({"roleId": roleId, "passKey": passKey}).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.formData = result.data
					} else {
						// 服务器返回失败
						this.$message.error('查询失败' + result.data.message)
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
