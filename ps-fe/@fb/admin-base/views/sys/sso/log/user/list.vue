<template>
	<div>
		<fb-page-search style="padding: 0">
			<template slot="query">
				<fb-form :label-width="160" mode="query" ref="query-form">
					<fb-row>
						<fb-col offset="1" span="10">
							<fb-form-item label="用户名">
								<fb-input v-model="queryData.userId" placeholder="请输入用户名" clearable />
							</fb-form-item>
						</fb-col>
						<fb-col offset="1" span="10">
							<fb-form-item label="事件类型">
								<fb-select v-model="queryData.type" placeholder="请选择事件类型" clearable
									:data="eventTypes" />
							</fb-form-item>
						</fb-col>
						<fb-col span="4"></fb-col>
					</fb-row>
					<fb-row>
						<fb-col offset="1" span="10">
							<fb-form-item label="应用ID">
								<fb-input v-model="queryData.clientId" placeholder="请输入应用ID" clearable />
							</fb-form-item>
						</fb-col>
						<fb-col offset="1" span="10">
							<fb-form-item label="时间范围">
								<fb-datepicker v-model="queryData.dateRange"
								
									mode="range"  placeholder="选择时间范围"
									format="YYYY-MM-DD HH:mm:ss" clearable style="width: 100%" />
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
				<fb-simple-table ref="table" 
				pk="rownum" :columns="columns" :loading="loading"
				:param="queryData"
				:service="$svc.sys.sso.admin.userEvent.list" 
				:pager="pagination"
				:reader="{
							pagerSize: 'size',
							pagerCurrent: 'current',
							pagerTotal: 'total',
							pagerPages: 'pages',
				}"
				:scroll="{
					y: 380,
					autoheight: true
				}">
					<template #userId="{ row }">
						<fb-link-group>
							<fb-link :click="()=>handleViewDetail(row)" :label="row.userId || '-'" type="primary"></fb-link>
						</fb-link-group>
					</template>


					
					<template #time="{ row }">
						{{ formatTime(row.time) }}
					</template>
					<template #type="{ row }">
						<fb-tag :type="getEventTypeColor(row.type)">{{ getEventTypeLabel(row.type) }}</fb-tag>
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
	name: 'UserLogList',
	components: {
	},
	data() {
		return {
			loading: false,
			tableData: [],
			selectedRow: null,
			queryData: {
				userId: '',
				type: '',
				clientId: '',
				dateRange: [null,null]
			},
			pagination: {
				current: 1,
				size: 20,
				total: 0
			},
			columns: [
				{
					label: '时间',
					name: 'time',
					width: 140,
					slot: 'time'
				},
				{
					label: '用户名',
					name: 'userId',
					width: 150,
					slot: 'userId'
				},
				{
					label: '事件类型',
					name: 'type',
					width: 120,
					slot: 'type'
				},
				{
					label: '应用ID',
					name: 'clientId',
					width: 150
				},
				{
					label: 'IP地址',
					name: 'ipAddress',
					width: 130
				},
				{
					label: '会话ID',
					name: 'sessionId',
					width: 200,
					ellipsis: true
				},
				{
					label: '错误信息',
					name: 'error',
					width: 200,
					ellipsis: true
				}
			],
			eventTypes: [
				{ value: 'LOGIN', label: '登录' },
				{ value: 'LOGOUT', label: '登出' },
				{ value: 'REGISTER', label: '注册' },
				{ value: 'UPDATE_PROFILE', label: '更新资料' },
				{ value: 'UPDATE_PASSWORD', label: '修改密码' },
				{ value: 'LOGIN_ERROR', label: '登录失败' },
				{ value: 'CODE_TO_TOKEN', label: '获取令牌' },
				{ value: 'REFRESH_TOKEN', label: '刷新令牌' }
			]
		}
	},
	mounted() {
	},
	methods: {


		// 查询
		handleQuery() {
	 	this.$ref.table.doSearch()
		},

		// 重置
		handleReset() {
			this.$refs['query-form'].resetFields()
			this.queryData = {
				userId: '',
				type: '',
				clientId: '',
			dateRange: [null,null]
			}
			this.handleQuery()
		},

 

		// 查看详情
		handleViewDetail(row) {
			let param = { data: row, };
	
				let options = {"width": 800, "height": 600};

			this.$refs.TpDialog.show(import('./view.vue'), param, "查看", options);

		},

		// 格式化时间
		formatTime(timestamp) {
			if (!timestamp) return '-'
			return app.$dayjs(timestamp).format('YYYY-MM-DD HH:mm:ss')
		},

		// 获取事件类型颜色
		getEventTypeColor(type) {
			const colorMap = {
				'LOGIN': 'success',
				'LOGOUT': 'info',
				'REGISTER': 'primary',
				'UPDATE_PROFILE': 'warning',
				'UPDATE_PASSWORD': 'warning',
				'LOGIN_ERROR': 'danger',
				'CODE_TO_TOKEN': 'success',
				'REFRESH_TOKEN': 'info'
			}
			return colorMap[type] || 'default'
		},

		// 获取事件类型中文标签
		getEventTypeLabel(type) {
			const labelMap = {
				'LOGIN': '登录',
				'LOGOUT': '登出',
				'REGISTER': '注册',
				'UPDATE_PROFILE': '更新资料',
				'UPDATE_PASSWORD': '修改密码',
				'LOGIN_ERROR': '登录失败',
				'CODE_TO_TOKEN': '获取令牌',
				'REFRESH_TOKEN': '刷新令牌'
			}
			return labelMap[type] || type
		}
		,
		// 关闭弹窗
		closeDialog() {
			// 弹窗关闭回调
		},
	}
}
</script>

<style lang="less" scoped></style>