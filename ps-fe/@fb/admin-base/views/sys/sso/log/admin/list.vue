<template>
	<div>
		<fb-page-search style="padding: 0">
			<template slot="query">
				<fb-form :label-width="160" mode="query" ref="query-form">
					<fb-row>
						<fb-col offset="1" span="10">
							<fb-form-item label="操作用户">
								<fb-input v-model="queryData.userId" placeholder="请输入操作用户" clearable />
							</fb-form-item>
						</fb-col>
						<fb-col offset="1" span="10">
							<fb-form-item label="操作类型">
								<fb-select v-model="queryData.operationType" placeholder="请选择操作类型" clearable :data="operationTypes" />
							</fb-form-item>
						</fb-col>
						<fb-col span="4"></fb-col>
					</fb-row>
					<fb-row>
						<fb-col offset="1" span="10">
							<fb-form-item label="资源类型">
								<fb-select v-model="queryData.resourceType" placeholder="请选择资源类型" clearable :data="resourceTypes" />
							</fb-form-item>
						</fb-col>
						<fb-col offset="1" span="10">
							<fb-form-item label="时间范围">
								<fb-datepicker 
									v-model="queryData.dateRange" 
									mode="range" 
									placeholder="选择时间范围"
									format="YYYY-MM-DD HH:mm:ss"
									clearable
									style="width: 100%"
								/>
							</fb-form-item>
						</fb-col>
						<fb-col span="4"></fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="actions">
				<fb-button type="primary" icon="search" @on-click="$refs.table.doSearch()">查询</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table 
					ref="table"
					pk="rownum"
					:columns="columns" 
					:loading="loading"
					:param="queryData"
					:service="$svc.sys.sso.admin.adminEvent.list"
					:pager="pagination"
					:reader="{
						pagerCurrent: 'page'
					}"
					:scroll="{
						y: 380,
						autoheight: true
					}"
				>
				 
				<template #authUserId="{ row }">
						<fb-link-group>
							<fb-link :click="()=>handleViewDetail(row)" :label="(row.authDetails && row.authDetails.userId) || '-' " type="primary"></fb-link>
						</fb-link-group>
					</template>

					<template #time="{ row }">
						{{ formatTime(row.time) }}
					</template>
					<template #operationType="{ row }">
						<fb-tag :type="getOperationTypeColor(row.operationType)">{{ row.operationType }}</fb-tag>
					</template>
					<template #resourceType="{ row }">
						<fb-tag type="info">{{ row.resourceType }}</fb-tag>
					</template>
				</fb-simple-table>
			</template>
		</fb-page-search>

		<!-- 详情弹窗 -->
		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
	</div>
</template>

<script>

export default {
	name: 'AdminLogList',
	components: {
	},
	data() {
		return {
			loading: false,
			queryData: {
				userId: '',
				operationType: '',
				resourceType: '',
				dateRange: []
			},
			columns: [
				{
					label: '时间',
					name: 'time',
					width: 140,
					slot: 'time'
				},
				{
					label: '操作用户',
					name: 'authDetails.userId',
					width: 150,
					slot: 'authUserId',
					resizeable: true
				},
				{
					label: '操作类型',
					name: 'operationType',
					width: 120,
					slot: 'operationType'
				},
				{
					label: '资源类型',
					name: 'resourceType',
					width: 120,
					slot: 'resourceType'
				},
				{
					label: '资源路径',
					name: 'resourcePath',
					width: 200,
					ellipsis: true
				},
				{
					label: 'IP地址',
					name: 'authDetails.ipAddress',
					width: 130,
					resize: true
				},
				{
					label: '应用ID',
					name: 'authDetails.clientId',
					width: 150,
					ellipsis: true
				},
				{
					label: 'Realm ID',
					name: 'realmId',
					width: 150,
					ellipsis: true
				}
			],
			operationTypes: [
				{ value: 'CREATE', label: '创建' },
				{ value: 'UPDATE', label: '更新' },
				{ value: 'DELETE', label: '删除' },
				{ value: 'ACTION', label: '操作' },
				{ value: 'VIEW', label: '查看' }
			],
			resourceTypes: [
				{ value: 'USER', label: '用户' },
				{ value: 'CLIENT', label: '应用' },
				{ value: 'REALM', label: '域' },
				{ value: 'ROLE', label: '角色' },
				{ value: 'GROUP', label: '组' },
				{ value: 'IDENTITY_PROVIDER', label: '身份提供者' },
				{ value: 'CLIENT_SCOPE', label: '应用范围' }
			]
		}
	},
	mounted() {
	},
	methods: {
	 
		
		// 查询
		handleQuery() {
			 
		},
		
		// 重置
		handleReset() {
			this.$refs['query-form'].resetFields()
			this.queryData = {
				userId: '',
				operationType: '',
				resourceType: '',
				dateRange: []
			}
			this.handleQuery()
		},
		
 
		
		// 查看详情
		handleViewDetail(row) {
			let param = {data: row, };
		
				let options = {"width": 800, "height": 600};

			this.$refs.TpDialog.show(import('./view.vue'), param, "查看", options);
			
		},
		
		// 关闭弹窗
		closeDialog() {
			// 弹窗关闭回调
			 
		},
		
		// 格式化时间
		formatTime(timestamp) {
			if (!timestamp) return '-'
			return app.$dayjs(timestamp).format('YYYY-MM-DD HH:mm:ss')
		},
		
		// 获取操作类型颜色
		getOperationTypeColor(type) {
			const colorMap = {
				'CREATE': 'success',
				'UPDATE': 'warning',
				'DELETE': 'danger',
				'ACTION': 'primary',
				'VIEW': 'info'
			}
			return colorMap[type] || 'default'
		}
	}
}
</script>

<style lang="less" scoped>
</style>