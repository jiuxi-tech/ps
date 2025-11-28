<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query">
					<fb-row>
						<fb-col span="8">
							<fb-form-item label="证书名称">
								<fb-input v-model="formData.certName" placeholder="请输入证书名称"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="使用状态">
								<fb-select v-model="formData.isInUse" placeholder="请选择使用状态" clearable
									:data="useStatusOptions">
								</fb-select>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="过期状态">
								<fb-select v-model="formData.isExpired" placeholder="请选择过期状态" clearable
									:data="expiredStatusOptions">
								</fb-select>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="buttons">
				<fb-button ref="buttonAdd" @on-click="handleAdd" icon="add-circle">
					新增
				</fb-button>
				<fb-button @on-click="handleUpdateExpired" icon="refresh">
					更新过期状态
				</fb-button>
			</template>

			<template slot="actions">
				<fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
				<fb-button @on-click="handleReset" icon="reset">重置</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table
					ref="table"
					:service="table.service.list"
					:param="formData"
					:pk="table.primaryKey"
					:columns="table.columns"
					:multiple="false"
					auto-load
					:formatters="formatters"
					:scroll="{x:1800, y: 400, autoHeight: true}"
					@on-row-select="handleTableSelect">

					<template v-slot:actions="props">
						<fb-space>
							<fb-button v-if="props.row.status == 0" 
									   @on-click="handleApply(props.row)" 
									   type="primary" size="s">应用</fb-button>
							<fb-button v-if="props.row.status == 1" 
									   @on-click="handleRestart(props.row)" 
									   type="warning" size="s">重启</fb-button>
							<fb-button @on-click="handleEdit(props.row)" 
									   editor size="s">修改</fb-button>
							<fb-button @on-click="handleDel(props.row)" 
									   :disabled="props.row.isInUse == 1"
									   danger size="s">删除</fb-button>
						</fb-space>
					</template>

					<template v-slot:certName="props">
						<fb-link-group>
							<fb-link :click="()=>handleView(props.row)" :label="props.row.certName" type="primary"></fb-link>
						</fb-link-group>
					</template>

					<template v-slot:status="props">
						<fb-tag v-if="props.row.status == 1" type="success">已应用</fb-tag>
						<fb-tag v-else type="default">未应用</fb-tag>
					</template>

					<template v-slot:isInUse="props">
						<fb-tag v-if="props.row.isInUse == 1" type="success">使用中</fb-tag>
						<fb-tag v-else type="default">未使用</fb-tag>
					</template>

					<template v-slot:isExpired="props">
						<fb-tag v-if="props.row.isExpired == 1" type="danger">已过期</fb-tag>
						<fb-tag v-else type="success">正常</fb-tag>
					</template>

				</fb-simple-table>
			</template>
		</fb-page-search>

		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
	</div>
</template>

<script>
	import dayjs from "dayjs";

	export default {
		name: 'list',
		mixins: [],

		// 初始化方法
		mounted() {
			// 执行界面初始化方法
		},

		data() {
			return {
				formData: {},
				useStatusOptions: [
					{ value: 0, label: '未使用' },
					{ value: 1, label: '使用中' }
				],
				expiredStatusOptions: [
					{ value: 0, label: '正常' },
					{ value: 1, label: '已过期' }
				],
				// 表格配置
				table: {
					service: {
						list: app.$svc.sys.serverCert.list
					},
					primaryKey: "certId",
					columns: [
						{
							name: 'certName',
							label: '证书名称',
							slot: 'certName',
							sortable: false,
							width: 180,
						},
						{
							name: 'certDesc',
							label: '证书描述',
							sortable: false,
							width: 200,
						},
						{
							name: 'domainNames',
							label: '绑定域名',
							sortable: false,
							width: 200,
						},
						{
							name: 'subjectCn',
							label: '公用名(CN)',
							sortable: false,
							width: 150,
						},
						{
							name: 'issuer',
							label: '发证机构',
							sortable: false,
							width: 180,
						},
						{
							name: 'issueDate',
							label: '颁发日期',
							sortable: false,
							width: 150,
						},
						{
							name: 'expireDate',
							label: '到期日期',
							sortable: false,
							width: 150,
						},
						{
							name: 'status',
							label: '状态',
							slot: 'status',
							sortable: false,
							width: 100,
						},
						{
							name: 'isInUse',
							label: '使用状态',
							slot: 'isInUse',
							sortable: false,
							width: 100,
						},
						{
							name: 'isExpired',
							label: '过期状态',
							slot: 'isExpired',
							sortable: false,
							width: 100,
						},
						{
							name: 'appliedTime',
							label: '应用时间',
							sortable: false,
							width: 150,
						},
						{
							name: 'createPersonName',
							label: '创建人',
							sortable: false,
							width: 100,
						},
						{
							name: 'createTime',
							label: '创建时间',
							sortable: false,
							width: 150,
						},
						{
							freeze: "right",
							name: '',
							label: '操作',
							sortable: false,
							slot: 'actions',
							width: 200,
						},
					],
				},
				formatters: {}
			}
		},

		// 方法
		methods: {
			// 查询
			handleQuery() {
				this.$refs.table.doSearch()
			},

			// 重置
			handleReset() {
				this.formData = {};
				this.$refs.table.doSearch();
			},

			// 新增
			handleAdd() {
				let param = { mode: 'add' };
				let options = { "width": 800, "height": 600 };

				this.$refs.TpDialog.show(import('./add.vue'), param, "新增服务器证书", options);
			},

			// 修改
			handleEdit(row) {
				let param = { "certId": row.certId, "passKey": row.passKey, mode: 'edit' };
				let options = { "width": 800, "height": 600 };

				this.$refs.TpDialog.show(import('./add.vue'), param, "修改服务器证书", options);
			},

			// 查看
			handleView(row) {
				let param = { "certId": row.certId, "passKey": row.passKey }
				let options = { "width": 800, "height": 600 };
				this.$refs.TpDialog.show(import('./view.vue'), param, "查看服务器证书", options);
			},

			// 删除
			handleDel(row) {
				if (row.isInUse == 1) {
					this.$message.warning('证书正在使用中，无法删除');
					return;
				}

				this.$confirm('确定要删除该证书吗？删除后将无法恢复！', () => {
					this.delete(row.certId);
				})
			},

			delete(certId) {
				// 真正执行删除操作
				app.$svc.sys.serverCert.delete({ certId: certId }).then((result) => {
					if (result.code == 1) {
						this.$message.success('删除成功');
						this.handleQuery();
					} else {
						// 服务器返回失败
						this.$message.error('删除失败: ' + result.message)
					}
				})
			},

			// 应用证书
			handleApply(row) {
				this.$confirm('确定要应用该证书吗？应用后将覆盖现有证书文件。', () => {
					app.$svc.sys.serverCert.apply({ certId: row.certId }).then((result) => {
						if (result.code == 1) {
							this.$message.success('应用成功');
							this.handleQuery();
						} else {
							this.$message.error('应用失败: ' + result.message);
						}
					});
				});
			},

			// 重启Nginx
			handleRestart(row) {
				this.$confirm('确定要重启Nginx服务吗？', () => {
					app.$svc.sys.serverCert.restart().then((result) => {
						if (result.code == 1) {
							this.$message.success('重启成功');
						} else {
							this.$message.error('重启失败: ' + result.message);
						}
					});
				});
			},

			// 更新过期状态
			handleUpdateExpired() {
				app.$svc.sys.serverCert.updateExpiredStatus().then((result) => {
					if (result.code == 1) {
						this.$message.success('更新成功');
						this.handleQuery();
					} else {
						this.$message.error('更新失败: ' + result.message);
					}
				});
			},

			// 表格选择
			handleTableSelect(selection) {
				// 处理表格行选择
			},

			// 关闭弹窗
			closeDialog() {
				this.handleQuery();
			}
		}
	}
</script>

<style>
</style>