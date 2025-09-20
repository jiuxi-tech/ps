<template>
	<div>
		<fb-page-tree-table title="企业列表">
			<template slot="tree">
				<div style="overflow-y: auto; margin: -18px; padding: 0 16px;" v-autoheight="140">
					<div style="padding: 15px 0">
						<fb-input placeholder="请输入企业名称" v-model="entName">
							<fb-button @on-click="handleQuery(1)" icon="search" slot="append-button"
								type="primary"></fb-button>
						</fb-input>
					</div>

					<fb-list :pager-align="'center'" :pager-max-length="3" :pager-simple="true" :service="service.list"
						ref="list">
						<fb-list-item :style="{ background: formData.entId === item.entId ? '#B8E6FF' : '' }" slot="item"
							slot-scope="{item}">
							<fb-list-item-meta>
								<fb-container pl="4px" slot="title">
									<fb-link :click="() => doClick(item)" :label="`${item.entFullName}`"
										type="primary"></fb-link>
								</fb-container>
							</fb-list-item-meta>
						</fb-list-item>
					</fb-list>
				</div>
			</template>

			<template slot="query">
				<fb-form mode="query" ref="query-form">
					<fb-row>
						<fb-col span="8">
							<fb-form-item label="人员姓名">
								<fb-input v-model="formData.personName"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="用户名">
								<fb-input v-model="formData.userName"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="手机号码">
								<fb-input v-model="formData.phone"></fb-input>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="buttons">
				<fb-button @on-click="handleAdd" icon="add-circle" ref="buttonAdd"
					v-permission="'SYS_ENT_PERSON_ADD'">新增
				</fb-button>
			</template>

			<template slot="actions">
				<fb-button :disabled="formData.entId === ''" @on-click="handleQuery" icon="search" type="primary">
					查 询
				</fb-button>
				<fb-button @on-click="formReset" icon="search">重 置</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table :auto-load="false" :columns="table.columns" :formatters="formatters" :param="formData"
					:pk="table.primaryKey" :scroll="{ x: 1000, y: 368, autoHeight: true }" :service="service.entAdminList"
					autoSelect ref="table" v-model="table.selectRow">

					<template v-slot:actions="props">
						<fb-space>
							<fb-button @on-click="handleAccount(props.row)" editor size="s"
								v-if="props.row.accountId === ''" v-permission="'SYS_ENT_PERSON_ACCOUNT'">开通账号
							</fb-button>
							<fb-button @on-click="handleAccount(props.row)" editor size="s"
								v-if="props.row.accountId !== ''" v-permission="'SYS_ENT_PERSON_ACCOUNT'">账号
							</fb-button>
							<fb-button @on-click="resetpwd(props.row)" size="s" type="primary"
								v-if="props.row.accountId !== ''">
								密码重置
							</fb-button>
							<fb-button @on-click="locked(props.row)" danger size="s"
								v-if="props.row.accountId !== '' && props.row.locked === 0">锁定
							</fb-button>
							<fb-button @on-click="locked(props.row)" size="s" type="link"
								v-if="props.row.accountId !== '' && props.row.locked === 1">
								解锁
							</fb-button>
							<fb-button @on-click="enabled(props.row)" danger size="s"
								v-if="props.row.accountId !== '' && props.row.enabled === 1">
								停用
							</fb-button>
							<fb-button @on-click="enabled(props.row)" size="s" type="link"
								v-if="props.row.accountId !== '' && props.row.enabled === 0">启用
							</fb-button>
							<fb-button @on-click="handleAuth(props.row)" editor size="s"
								v-permission="'SYS_ENT_PERSON_AUTHADD'">授权
							</fb-button>
						</fb-space>
					</template>

				</fb-simple-table>
			</template>
		</fb-page-tree-table>

		<tp-dialog @closeTpDialog="closeDialog" ref="TpDialog"></tp-dialog>
		<tp-dialog-tab @closeTpDialog="closeDialogTab" ref="TpDialogTab"></tp-dialog-tab>
	</div>
</template>

<script>
import dayjs from "dayjs";


export default {
	name: 'list',
	mixins: [

	],
	// 组件
	components: {},
	// 初始化方法
	mounted() {

		// 执行界面初始化方法
		this.createdPage();
	},
	data() {
		return {
			entName: '',
			personName: '',
			service: app.$svc.sys.ent,
			// 人员列表
			personListData: [],

			// 选中节点
			selectNode: {
				dicId: '',
				pdicId: '',
				dicCode: ''
			},
			userName: '',
			// 表单数据
			formData: {
				entId: '',
				personName: '',
				userName: '',
				phone: '',
			},

			formatters: {
				locked(val) {
					return val === 0 ? '否' : '是';
				},
				enabled(val) {
					return val === 0 ? '否' : '是';
				},
				expiredTime(val) {
					if (val) {
						return dayjs(val).format('YYYY-MM-DD')
					}
					return '';
				}
			},
			// Table列
			table: {
				// 请求的 url
				selectRow: [],
				primaryKey: "personId",
				columns: [
					{
						name: 'personName',
						label: '用户名称',
						sortable: false,
						width: 120
					}, {
						name: 'phone',
						label: '手机号',
						sortable: false,
						width: 100
					}, {
						name: 'userName',
						label: '账号',
						sortable: false,
						width: 100
					}, {
						name: 'locked',
						label: '是否锁定',
						sortable: false,
						width: 80
					}, {
						name: 'enabled',
						label: '是否启动',
						sortable: false,
						width: 80
					}, {
						name: 'expiredTime',
						label: '过期时间',
						sortable: false,
						width: 100
					}, {
						name: 'dingding',
						label: '钉钉',
						sortable: false,
						width: 100
					}, {
						name: 'weixin',
						label: '微信',
						sortable: false,
						width: 100
					}, {
						name: 'threeId',
						label: '第三方认证id',
						sortable: false,
						width: 100
					}, {
						name: '',
						label: '操作',
						sortable: false,
						slot: 'actions',
						width: 292,

					},
				],
			},
		}
	},

	// 方法
	methods: {

		/**
		 * 记录用户操作日志信息
		 * @param {string} operateType - 操作类型：login/logout/add/delete/edit/query/pass/unpass等
		 * @param {string} message - 错误日志信息
		 */
		logInfo(operateType, message) {
			// 获取用户信息
			let userInfo = app.$datax.get('userInfo')

			// 构建日志数据对象
			let data = {
				// 模块编码
				moduleCode: 'user',
				// 模块名称
				moduleName: '用户',
				// 模块编码
				moduleCode: 'user',
				// 模块名称
				// 模块名称
				moduleName: '用户',
				// 操作类型： login/logout/add/delete/edit/query/pass/unpass, 可以自己定义
				operateType,
				// 操作人id
				operterId: userInfo.personId,
				// 操作人名称
				operterName: userInfo.personName,
				// 错误日志信息
				message
			}
			// 日志埋点
			app.$logger.send(data);
		},

		// 列表方法
		handleQuery(val) {
			if (val === 1) {
				this.$refs.list.doSearch({
					'entFullName': this.entName,
					'cityCode': app.$datax.get('userInfo').cityCode
				})
			} else {
				this.$refs.table.doSearch(this.formData);
			}
		},
		// 选择一家企业，查询人员账号
		doClick(item) {
			// 选中的列表
			this.formData.entId = item.entId;
			// 分页查询企业下的所有用户账号
			this.handleQuery();
		},
		// 新增人员方法
		handleAdd() {
			if (!this.formData.entId || this.formData.entId === '') {
				this.$message.error('请选择企业添加人员')
				return
			}

			// 界面跳转
			let param = { "id": "", "deptId": this.formData.entId, "ascnId": this.formData.entId };
			let tabArry = [{
				url: import('../../../../views/sys/person/ent/add-basicinfo.vue'),
				label: '人员基本信息',
				icon: "chart-line"
			},
			{
				url: import('../../../../views/sys/person/ent/add-exinfo.vue'),
				label: '人员扩展信息',
				icon: "progressbar"
			}];

			this.$refs.TpDialogTab.show(tabArry, param, "新增");
		},
		// 开通账号
		handleAccount(row) {
			let param = { "id": row.personId, "passKey": row.passKey };
			let options = {
				callBack: function (result) {
					console.log("=============回调查看" + result)
				}
			}
			this.$refs.TpDialog.show(import('../../../../views/sys/person/ent/account-add.vue'), param, "账号", options);
		},
		resetpwd(row) {
			let that = this;
			app.$svc.sys.person.accountResetpwd({ "accountId": row.accountId }).then((result) => {
				// 判断code
				if (result.code == 1) {
					// this.$message.success('重置成功，新密码：' + result.data);
					that.$refs.TpDialog.show(import('../../../../views/sys/person/ent/account-restpwd.vue'), result, "重置密码");
					that.logInfo('resetpwd', '重置密码');
				} else {
					// 服务器返回失败
					that.$message.error('重置失败')
				}
			}).catch((err) => {
				// 服务器返回失败
				console.log(err);
			})
		},
		locked(row) {
			let that = this
			app.$svc.sys.person.accountLocked({
				"accountId": row.accountId,
				"locked": row.locked === 0 ? 1 : 0
			}).then((result) => {
				// 判断code
				if (result.code == 1) {
					if (row.locked === 0) {
						this.$message.success('已锁定');
						that.logInfo('lock', '锁定账号');
					} else {
						this.$message.success('已解锁');
						that.logInfo('unlock', '解锁账号');
					}
					that.handleQuery();
				} else {
					// 服务器返回失败
					this.$message.error('失败')
				}
			}).catch((err) => {
				// 服务器返回失败
				console.log(err);
			})
		},
		enabled(row) {
			let that = this
			app.$svc.sys.person.accountEnabled({
				"accountId": row.accountId,
				"enabled": row.enabled === 0 ? 1 : 0
			}).then((result) => {
				// 判断code
				if (result.code == 1) {
					if (row.enabled === 0) {
						this.$message.success('已启用');
						that.logInfo('enabled_account', '启用账号');
					} else {
						this.$message.success('已停用');
						that.logInfo('disabled_account', '停用账号');
					}
					that.handleQuery();
				} else {
					// 服务器返回失败
					this.$message.error('失败')
				}
			}).catch((err) => {
				// 服务器返回失败
				console.log(err);
			})
		},
		// 授权角色
		handleAuth(row) {
			let param = { "id": row.personId, "deptId": this.formData.entId };
			this.$refs.TpDialog.show(import('../../../../views/sys/person/ent/auth.vue'), param, "授权", {
				//tabChangeConfirm: false,
				callBack: function (result) {
					console.log("=============回调查看" + result)	
				}
			});
		},

		closeDialog(param) {
			if (param) {
				this.handleQuery();
			}
		},

		closeDialogTab(param) {
			this.handleQuery();
		},
	}
}
</script>

<style lang="less" scoped></style>
