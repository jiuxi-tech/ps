<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query">
					<fb-row>
						<fb-col span="8">
							<fb-form-item label="备份名称">
								<fb-input v-model="formData.backupName"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="备份类型">
								<fb-select v-model="formData.backupType" placeholder="请选择备份类型" :data="[
									{value: 1, label: '自动备份'},
									{value: 2, label: '手动备份'}
								]">
								</fb-select>
							</fb-form-item>
						</fb-col>
						<fb-col span="8">
							<fb-form-item label="备份状态">
								<fb-select v-model="formData.backupStatus" placeholder="请选择备份状态" :data="[
									{value: 1, label: '进行中'},
									{value: 2, label: '成功'},
									{value: 3, label: '失败'}
								]">
								</fb-select>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="buttons">
				<fb-button ref="buttonAdd" @on-click="handleManualBackup" icon="add-circle">
					手动备份
				</fb-button>
				<!-- <fb-button @on-click="handleCheckConfig" icon="setting">
					配置检查
				</fb-button> -->
			</template>

			<template slot="actions">
				<fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
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
					:scroll="{x:1400, y: 330, autoHeight: true}"
					@on-row-select="handleTableSelect">

					<template v-slot:actions="props">
						<fb-space>
							<fb-button @on-click="handleRestore(props.row)"
									   size="s">恢复</fb-button>
							<fb-button @on-click="handleStop(props.row)" 
									   v-if="props.row.backupStatus === 1"
									   danger size="s">停止</fb-button>
							<fb-button @on-click="handleDel(props.row)"  
									   danger size="s">删除</fb-button>
						</fb-space>
					</template>

					<template v-slot:view="props">
						<fb-link-group>
							<fb-link :click="()=>handleView(props.row)" :label="props.row.backupName" type="primary"></fb-link>
						</fb-link-group>
					</template>

					<template v-slot:backupStatus="props">
						<span :class="getStatusClass(props.row.backupStatus)">
							{{ props.row.backupStatusName }}
						</span>
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
			//this.loadStatistics();
		},
		data() {
			return {
				formData: {
					backupName: '',
					backupType: null,
					backupStatus: null,
					actived: 1, // 只查询有效记录
				},

				formatters: {
					backupStartTime(val) {
						return val ? dayjs(val).format('YYYY-MM-DD HH:mm:ss') : val;
					},
					backupEndTime(val) {
						return val ? dayjs(val).format('YYYY-MM-DD HH:mm:ss') : val;
					},
					backupFileSize(val) {
						if (!val || val <= 0) return '0 B';
						const units = ['B', 'KB', 'MB', 'GB', 'TB'];
						const digitGroups = Math.floor(Math.log10(val) / Math.log10(1024));
						const value = (val / Math.pow(1024, digitGroups)).toFixed(2);
						return value + ' ' + units[digitGroups];
					}
				},

				// Table列
				table: {
					// 请求的 url
					service: app.$svc.sys.databasebackup,
					primaryKey: "backupId",
					columns: [
						{
							name: 'backupName',
							label: '备份名称',
							slot: 'view',
							sortable: false,
							width: 200,
						}, {
							name: 'backupTypeName',
							label: '备份类型',
							sortable: false,
							width: 100,
						}, {
							name: 'backupStatus',
							label: '备份状态',
							slot: 'backupStatus',
							sortable: false,
							width: 100,
						}, {
							name: 'databaseName',
							label: '数据库名称',
							sortable: false,
					 
						}, {
							name: 'backupFileSizeDisplay',
							label: '文件大小',
							sortable: false,
							width: 100,
						}, {
							name: 'backupDurationDisplay',
							label: '备份耗时',
							sortable: false,
							width: 100,
						}, {
							name: 'backupStartTime',
							label: '开始时间',
							sortable: false,
							width: 150,
						}, {
							name: 'backupEndTime',
							label: '结束时间',
							sortable: false,
							width: 150,
						},
						{
							freeze: "right",
							name: '',
							label: '操作',
							sortable: false,
							slot: 'actions',
							width: 180,
						},
					],
				},
			}
		},

		// 方法
		methods: {
			// 列表方法
			handleQuery() {
				this.$refs.table.doSearch()
			},

			// 手动备份方法
			handleManualBackup() {
				this.$confirm('确定要开始手动备份吗？', () => {
					this.executeManualBackup();
				})
			},
			executeManualBackup() {
				app.service.request('/sys/database-backup/manual-backup', {
					method: 'post',
					data: {databaseName: 'ps-bmp'},
					headers: {'Content-Type': 'application/json'},
					responseType: 'json',
					 
				}).then((result) => {
					if (result.code == 1) {
						this.$message.success('备份任务已开始，请稍后查看备份结果');
						this.handleQuery();
					} else {
						this.$message.error('启动备份失败: ' + result.message)
					}
				})
			},

			// 配置检查方法
			handleCheckConfig() {
				app.service.request('/sys/database-backup/check-config', {
					method: 'get',
					responseType: 'json',
					 
				}).then((result) => {
					if (result.code == 1) {
						const isValid = result.data.isValid;
						const configInfo = result.data.configInfo;
						
						const status = isValid ? '配置正常' : '配置异常';
						const type = isValid ? 'success' : 'warn';

						this.$msgbox.alert(status + '\n\n' + configInfo, ()=>{},()=>{}, {
                type,
                width: 500
            });
					} else {
						this.$message.error('检查配置失败: ' + result.message);
					}
				})
			},

			// 停止备份方法
			handleStop(row) {
				this.$confirm('确定要停止该备份任务吗？', () => {
					this.stopBackup(row.backupId, row.passKey);
				})
			},
			stopBackup(backupId, passKey) {
				app.service.request('/sys/database-backup/stop-backup', {
					method: 'post',
					data: {"backupId": backupId, "passKey": passKey},
					headers: {'Content-Type': 'application/json'},
					responseType: 'json',
					 
				}).then((result) => {
					if (result.code == 1) {
						this.$message.success('备份任务已停止');
						this.handleQuery();
					} else {
						this.$message.error('停止备份失败: ' + result.message)
					}
				})
			},

			// 恢复方法
			handleRestore(row) {

          this.$msgbox.alert(`如果误操作将导致无法挽回的损失，请根据实际情况与技术支持人员获取联系后操作。\n请提供如下信息：\nbackupId：${row.backupId}\n
          `, ()=>{},()=>{}, {
              type: 'danger',
              title: '危险操作',
              width: 500
          });
//				this.$confirm('确定要恢复该备份记录吗？', () => {
//					//this.restore(row.backupId, row.passKey);
//				})
			},
			restore(backupId, passKey) {
//				app.service.request('/sys/database-backup/restore', {
//					method: 'post',
//					data: {"backupId": backupId, "passKey": passKey},
//					headers: {'Content-Type': 'application/json'},
//					responseType: 'json',
//					 
//				}).then((result) => {
//					if (result.code == 1) {
//						this.$message.success('恢复成功');
//						this.handleQuery();
//					} else {
//						this.$message.error('恢复失败: ' + result.message)
//					}
//				})
			},

			// 删除方法
			handleDel(row) {
				this.$confirm('确定要删除该备份记录吗？', () => {
					this.delete(row.backupId, row.passKey);
				})
			},
			delete(backupId, passKey) {
				app.service.request('/sys/database-backup/delete', {
					method: 'post',
					data: {"backupId": backupId, "passKey": passKey},
					headers: {'Content-Type': 'application/json'},
					responseType: 'json',
					 
				}).then((result) => {
					if (result.code == 1) {
						this.$message.success('删除成功');
						this.handleQuery();
					} else {
						this.$message.error('删除失败: ' + result.message)
					}
				})
			},

			// 查看方法
			handleView(row) {
				let param = {"backupId": row.backupId, "passKey": row.passKey}
				let options = {"height": 600, "width": 800};
				this.$refs.TpDialog.show(import('./view.vue'), param, "查看备份详情", options);
			},

			// 加载统计信息
			loadStatistics() {
				app.service.request('/sys/database-backup/statistics', {
					method: 'get',
					params: {databaseName: 'ps-bmp', days: 30},
					responseType: 'json',
					 
				}).then((result) => {
					if (result.code == 1) {
						console.log('备份统计信息:', result.data);
					}
				}).catch((error) => {
					console.warn('获取统计信息失败:', error);
				})
			},

			// 获取状态样式
			getStatusClass(status) {
				switch (status) {
					case 1: return 'status-running'; // 进行中
					case 2: return 'status-success'; // 成功
					case 3: return 'status-failed';  // 失败
					default: return '';
				}
			},

			// 关闭弹窗回调
			closeDialog(param) {
				this.handleQuery();
			},

			// 表格行选择
			handleTableSelect(row) {
				// 表格行选择事件
			}
		}
	}
</script>

<style lang="less" scoped>
	.status-running {
		color: #1890ff;
	}

	.status-success {
		color: #52c41a;
	}

	.status-failed {
		color: #ff4d4f;
	}
</style>