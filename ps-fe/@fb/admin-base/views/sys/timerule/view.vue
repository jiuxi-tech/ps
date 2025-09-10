<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-property bordered label-width="140px" mode="form">
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="规则名称">
							{{ formData.ruleName }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="是否允许登录">
							<fb-tag :type="getAllowLoginType(formData.allowLogin)">{{ getAllowLoginText(formData.allowLogin) }}</fb-tag>
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="开始时间">
							{{ formatDateTime(formData.startTime) }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="结束时间">
							{{ formatDateTime(formData.endTime) }}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="适用人员">
							<div class="users-display">
								<fb-tag v-for="user in usersDisplay" :key="user" type="success" style="margin-right: 8px; margin-bottom: 4px;">{{ user }}</fb-tag>
								<span v-if="!usersDisplay.length" class="empty-text">无</span>
							</div>
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="适用角色">
							<div class="roles-display">
								<fb-tag v-for="role in rolesDisplay" :key="role" type="primary" style="margin-right: 8px; margin-bottom: 4px;">{{ role }}</fb-tag>
								<span v-if="!rolesDisplay.length" class="empty-text">无</span>
							</div>
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="24">
						<fb-property-item label="备注">
							{{ formData.remark || '无' }}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="是否启用">
							<fb-tag :type="getStatusType(formData.status)">{{ getStatusText(formData.status) }}</fb-tag>
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<!-- 空列，保持布局平衡 -->
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="创建人">
							{{ formData.createUserName || '未知' }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="创建时间">
							{{ formatDateTime(formData.createTime) || '未知' }}
						</fb-property-item>
					</fb-col>
				</fb-row>
				<fb-row>
					<fb-col span="12">
						<fb-property-item label="修改人">
							{{ formData.updateUserName || '未知' }}
						</fb-property-item>
					</fb-col>
					<fb-col span="12">
						<fb-property-item label="修改时间">
							{{ formatDateTime(formData.updateTime) || '未知' }}
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
		name: 'timerule-view',
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
				service: this.$svc.sys.timerule,

				// 表单数据
				formData: {
					id: '',
					ruleName: '',
					allowLogin: '',
					startTime: '',
					endTime: '',
					roleIds: '',
					roleNames: '',
					userIds: '',
					userNames: '',
					status: '',
					remark: '',
					createUserName: '',
					createTime: '',
					updateUserName: '',
					updateTime: ''
				}
			}
		},
		computed: {
			// 角色显示
			rolesDisplay() {
				if (!this.formData.roleNames) return [];
				return this.formData.roleNames.split(',').filter(name => name.trim());
			},
			// 人员显示
			usersDisplay() {
				if (!this.formData.userNames) return [];
				return this.formData.userNames.split(',').filter(name => name.trim());
			}
		},
		// 方法
		methods: {

			// 设置标题
			init(param) {
				if (param.id) {
					let ruleId = param.id;
					this.formData.id = ruleId;
					this.view(ruleId, param.passKey);
				}
			},

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog("xxxx");
			},

			view(ruleId, passKey) {
				// 调用查看service方法
				this.service.view({"id": ruleId, "passKey": passKey}).then((result) => {
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
			// 获取状态类型
			getStatusType(status) {
				return 'default';
				switch (status) {
					case '1':
						return 'success'; // 启用
					case '0':
						return 'danger'; // 禁用
					default:
						return 'default';
				}
			},
			// 获取状态文本
			getStatusText(status) {
				switch (status+"") {
					case '1':
						return '启用';
					case '0':
						return '禁用';
					default:
						return '未知';
				}
			},
			// 获取是否允许登录样式
			getAllowLoginType(allowLogin) {
				switch (allowLogin) {
					case 1:
					case '1':
						return 'success'; // 允许
					case 0:
					case '0':
						return 'danger'; // 拒绝
					default:
						return 'default';
				}
			},
			// 获取是否允许登录文本
			getAllowLoginText(allowLogin) {
				switch (allowLogin) {
					case 1:
					case '1':
						return '允许';
					case 0:
					case '0':
						return '拒绝';
					default:
						return '未知';
				}
			},
			// 格式化日期时间
			formatDateTime(timeStr) {
				if (!timeStr || timeStr.length !== 14) return timeStr;
				const year = timeStr.substring(0, 4);
				const month = timeStr.substring(4, 6);
				const day = timeStr.substring(6, 8);
				const hour = timeStr.substring(8, 10);
				const minute = timeStr.substring(10, 12);
				const second = timeStr.substring(12, 14);
				return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
			},

		}
	}
</script>

<style lang="less" scoped>
	.roles-display,
	.users-display {
		min-height: 32px;
		line-height: 32px;
		
		.fb-tag {
			margin-bottom: 8px;
		}
	}

	.empty-text {
		color: #999;
		font-style: italic;
	}
</style>