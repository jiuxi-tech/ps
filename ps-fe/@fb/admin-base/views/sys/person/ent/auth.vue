<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform">
				<fb-row>
					<fb-col span="24">
						<fb-transfer
							:list-style="{height: '500px', width: '330px'}"
							:data="sourceData"
							:titles="['待选角色', '已拥有角色']"
							:target-keys="targetKeys"
							:selected-keys="selectedKeys"
							:reader="{label: 'roleName', value:'roleId'}"
							@on-change="handleChange"
						></fb-transfer>
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
		name: 'auth',
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
		// 页面元素没有实例化之前执行方法
		created() {
			this.init(this.param);
		},
		// 页面元素实例化完成之后执行方法
		mounted() {
		},
		data() {

			return {
				// 请求的 service
				service: this.$svc.sys.person,
				sourceData: [],
				targetKeys: [],
				selectedKeys: [],
				disabled: false,
				// 表单数据
				formData: {
					personId: '',
					deptId: '',
					roleIds: '',
				},
			}
		},

		// 方法
		methods: {
			// 初始化参数
			init(param) {
				let personId = param.id;
				let deptId = param.deptId;
				this.formData.personId = personId;
				this.formData.deptId = deptId;

				this.initData(deptId, personId);
			},

			initData(deptId, personId) {
				// 查询待选角色

				this.sourceData = [];
				this.targetKeys = [];
				this.$svc.sys.role.auth.roleAuthList({"roleType": 1}).then((result) => {
					// 判断code
					if (result.code == 1) {
						result.data.forEach((data)=> {
							this.sourceData.push(data);
						});

						console.log(this.sourceData)
						// 用户已经拥有的角色
						this.service.personRoles({'deptId': deptId, 'personId': personId, "passKey": this.param.passKey}).then((res) => {
							// 判断code
							if (res.code == 1) {
								res.data.forEach((data)=> {
									this.targetKeys.push(data.roleId);

									// 有一种情况，已选列有数据，待选列没数据，需要把已选列的数据加入到待选列充当原始数据.
									// 另一种情况，待选列没有已选列的数据，没有原始数据，已选列展示不出数据
									let hasFlag = true;
									for (let i = 0; i < this.sourceData.length; i++) {
										if (this.sourceData[i].roleId === data.roleId) {
											// 如果原始数据已经有当前数据，直接结束，否则如果一直到最后都没有，将该数据加入到原始数据中。
											hasFlag = false;
											break;
										}
									}
									if (hasFlag) {
										// 原始数据没有，加入到原始数据中。原始数据是为了显示待选列和已选列。
										this.sourceData.push(data);
									}
								});
							} else {
								this.$message.error('已拥有角色查询失败:' + res.data.message)
							}
						})
					} else {
						this.$message.error('可选角色查询失败:' + result.data.message)
					}
				});
			},

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog("param");
			},
			// 新增
			add() {
				console.log("长度：" + this.targetKeys.length);
				if (this.targetKeys.length == 0){
					this.$confirm('当前人员未分配角色，将无法登录系统，请确认？', () => {
						this.handleAdd();
					})
				} else {
					this.handleAdd();
				}
			},

			handleAdd(){
				// 获取全部选中的node
				this.targetKeys.forEach((node) => {
					this.formData.roleIds += "," + node;
				});
				this.formData.passKey = this.param.passKey;
				// 授权
				this.service.authAdd(this.formData).then((result) => {
					// 判断code
					if (result.code == 1) {
						this.$message.success('授权成功');
						// 关闭，并传递参数
						this.handleClose();
					} else {
						this.$message.error('授权失败:' + result.data.message)
					}
				})
			},
			handleChange(targetKeys, targetList, sourceKeys, sourceList) {
				let oldTargetKeys = this.targetKeys;
				try {
					// console.log(targetKeys, targetList, sourceKeys, sourceList)
					this.targetKeys = [];
					targetKeys.forEach((data) => {
						this.targetKeys.push(data.roleId);
					})
				} catch (e) {
					this.targetKeys = oldTargetKeys;
				}
			},

		}
	}
</script>

<style lang="less" scoped>

</style>
