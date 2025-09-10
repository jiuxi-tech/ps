<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform">
				<fb-row>
					<fb-col span="24">
						<fb-transfer
							:list-style="{height: '500px', width: '330px'}"
							:data="sourceData"
							:titles="['待选角色', '已选角色']"
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
			<fb-button style="margin-right: 12px" type="primary" @on-click="confirm">确定</fb-button>
			<fb-button @on-click="handleClose">取消</fb-button>
		</div>
	</div>
</template>

<script>
export default {
	name: 'RoleSelect',
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
	// 页面元素没有实例化之前执行方法
	created() {
		this.init(this.param);
	},
	data() {
		return {
			sourceData: [],
			targetKeys: [],
			selectedKeys: [],
			disabled: false,
			// 已选择的角色数据
			selectedRoles: []
		}
	},
	// 方法
	methods: {
		// 初始化参数
		init(param) {
			// 获取传入的已选角色ID数组
			const selectedRoleIds = param?.selectedRoleIds || [];
			this.targetKeys = [...selectedRoleIds];
			this.initData();
		},

		initData() {
			// 查询所有角色列表
			let that = this;
			that.sourceData = [];
			
			// 使用系统角色服务获取角色列表
			 	this.$svc.sys.role.auth.roleAuthList({"roleType": 0}).then((result) => {	// 判断code
					if (result.code == 1) {
						result.data.forEach(function (data) {
							that.sourceData.push(data);
						});

						 
						// 处理已经拥有的角色
						// 根据selectedRoleIds设置已选中的角色
						const selectedRoleIds = this.targetKeys || [];
						this.selectedRoles = that.sourceData.filter(role => 
							selectedRoleIds.includes(role.roleId)
						);

					} else {
						this.$message.error('可选角色查询失败:' + result.data.message)
					}
				});
		},

		// 取消
		handleClose(a) {
			// 关闭弹窗
			this.closeTpDialog(a);
		},

		// 确定选择
		confirm() {
			// 获取选中的角色数据
			const selectedRoles = this.sourceData.filter(role => 
				this.targetKeys.includes(role.roleId)
			);
			
			// 构造返回数据
			const result = {
				roleIds: this.targetKeys.join(','),
				roleNames: selectedRoles.map(role => role.roleName).join(','),
				roleIdsArray: [...this.targetKeys],
				selectedRoles: selectedRoles
			};
			
			// 调用回调函数
			if (this.param && this.param.callback) {
				this.param.callback(result);
			}
			
			// 关闭弹窗
			this.handleClose({a:"ss"});
		},

		handleChange(targetKeys, targetList, sourceKeys, sourceList) {
			let oldTargetKeys = this.targetKeys;
			try {
				this.targetKeys = [];
				var that = this;
				targetKeys.forEach(function (data) {
					that.targetKeys.push(data.roleId);
				});
			} catch (e) {
				this.targetKeys = oldTargetKeys;
			}
		}
	}
}
</script>

<style lang="less" scoped>
.tp-dialog {
	.tp-dialog-top {
		padding: 20px;
	}
	
	.tp-dialog-bottom {
		padding: 20px;
		text-align: center;
		border-top: 1px solid #e8e8e8;
	}
}
</style>