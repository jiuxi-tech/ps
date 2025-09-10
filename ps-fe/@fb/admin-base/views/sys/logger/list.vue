<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form :label-width="160" mode="query" ref="query-form">
					<fb-row>
						<fb-col offset="1" span="11">
							<fb-form-item label="模块选择">
								<fb-tree-select :data="defaultData.moduleCodeData"
												clearable
												placeholder="请选择"
												v-model="formData.moduleCode">
								</fb-tree-select>
							</fb-form-item>
						</fb-col>
						<fb-col offset="1" span="11">
							<fb-form-item label="用户名">
								<fb-input v-model="formData.operterUserName"></fb-input>
							</fb-form-item>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col offset="1" span="11">
							<fb-form-item label="行政区划">
								<fb-tree-select :header-format="cityTreeHeaderFormat"
												:param="{'sync': 1, 'expand': false, 'cityId': '-1'}"
												:placeholder="'请选择'"
												:reader="{value:'id', label: 'text'}"
												:service="$svc.sys.city.tree"
												clearable
												v-model="formData.cityCode">
								</fb-tree-select>
							</fb-form-item>
						</fb-col>
						<fb-col offset="1" span="11">
							<fb-form-item :content-style="{ }" label="时间选择"
										  prop="formData.operterTimeStart" style="display: inline-block; width: 56%">
								<tp-datepicker  :maxDate="maxDateForStart" clearable
												format="YYYY-MM-DD HH:mm:ss" v-model="formData.operterTimeStart" value-format="YYYYMMDDHHmmss"></tp-datepicker>
							</fb-form-item>
							<fb-form-item  :content-style="{marginLeft: '30px'}" :label-style="{width:'30px', textAlign: 'center', paddingLeft: '5px'}"
										   label="-"
										   prop="formData.operterTimeEnd"
										   style="display: inline-block; width: 44%">
								<tp-datepicker  :minDate="minDateForEnd" clearable
												format="YYYY-MM-DD HH:mm:ss" v-model="formData.operterTimeEnd" value-format="YYYYMMDDHHmmss"></tp-datepicker>
							</fb-form-item>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col offset="1" span="11">
							<fb-form-item label="操作类型">
								<fb-tree-select :data="defaultData.operateTypeData"
												clearable
												placeholder="请选择"
												v-model="formData.operateType">
								</fb-tree-select>
							</fb-form-item>
						</fb-col>
						<fb-col offset="1" span="11">
							<fb-form-item label="用户类别">
								<fb-tree-select :data="defaultData.categoryData"
												clearable
												placeholder="请选择"
												v-model="formData.category">
								</fb-tree-select>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

			<template slot="actions">
				<fb-button @on-click="handleQuery" icon="search" type="primary">查询</fb-button>

				<fb-button @on-click="handleOutput" icon="search" type="primary">导出</fb-button>
			</template>

			<template slot="table">
				<fb-simple-table
					:columns="table.columns"
					:formatters="formatters"
					:multiple="false"
					:param="formData"
					:pk="table.primaryKey"
					:scroll="{x:940, y: 402, autoHeight: true}"
					:service="table.service.list"
					:showTotalInfo="false"
					@on-row-select="handleTableSelect"
					auto-load
					ref="table">
				</fb-simple-table>
			</template>
		</fb-page-search>
	</div>
</template>

<script>



	export default {
		mixins: [

		],
		// 组件
		components: {
			// 'component-a': ComponentA,
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化方法
		},
		computed: {
			maxDateForStart() {
				if (!this.formData.operterTimeEnd) return null;
				return new Date(this.formData.operterTimeEnd);
			},
			minDateForEnd() {
				if (!this.formData.operterTimeStart) return null;
				return new Date(this.formData.operterTimeStart);
			}
		},

		data() {
			let that = this;
			return {
				defaultData: {
					moduleCodeData: app.$constant.moduleCode,
					operateTypeData: app.$constant.operateType,
					categoryData: app.$constant.category
				},
				formData: {
					moduleCode: 'login',
					operterUserName: '',
					cityCode: '',
					operterTimeStart: this.formatDate(new Date(new Date().valueOf() - 30 * 24 * 60 * 60 * 1000), "YYYY-MM-DD HH:mm:ss"),
					operterTimeEnd: this.formatDate(new Date(), "YYYY-MM-DD HH:mm:ss"),
					operateType: 'login',
					category: '0',
				},
				formatters: {
					category(val){
						return val == 0 ? '政府' : '企业';
					},
					operterTime(val){
						val = that.formatDate(val, "YYYY-MM-DD HH:mm:ss");
						return val;
					}
				},
				// Table列
				table: {
					// 请求的 url
					service: app.$svc.sys.logger,

					primaryKey: "id",
					columns: [
						{
							name: 'operterUserName',
							label: '用户名',
							sortable: false,
							width: 150
						}, {
							name: 'category',
							label: '用户类型',
							sortable: false,
							width: 150
						}, {
							name: 'moduleName',
							label: '操作模块',
							sortable: false,
							width: 150
						},
						{
							name: 'operateType',
							label: '操作类型',
							sortable: false,
							width: 120
						},
						{
							name: 'operterIp',
							label: '登陆ip',
							sortable: false,
							width: 150
						},
						{
							name: 'operterTime',
							label: '操作时间',
							sortable: false,
							width: 220
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

			handleOutput() {
				let columns = this.table.columns.map(item => {
					if (item.name && item.name != '' && item.name != 'desc') {
						return item.name
					}
				});

				this.formData.columns = columns;
				this.formData.fileName = "操作日志";

				this.table.service.export(this.formData)

			},
		}
	}
</script>

<style lang="less" scoped>

</style>
