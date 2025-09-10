<template>
	<div>
		<fb-page-search>
			<template slot="query">
				<fb-form ref="query-form" mode="query">
					<fb-row>
						<fb-col span="11">
							<fb-form-item label="人员名称">
								<fb-input v-model="formData.userName"></fb-input>
							</fb-form-item>
						</fb-col>
						<fb-col span="11" offset="1">
							<fb-form-item label="性别">
								<fb-select v-model="formData.userGander"
										   :data="defaultForm.sex"
										   :placeholder="'请选择'"
										   clearable/>
							</fb-form-item>
						</fb-col>
					</fb-row>
				</fb-form>
			</template>

		</fb-page-search>
	</div>
</template>

<script>

	import Page from "@/util/Page"

	export default {
		mixins: [
			Page
		],
		// 组件
		components: {
			// 'component-a': ComponentA,

		},
		// 初始化方法
		mounted() {
			// 执行界面初始化方法
			this.handleInit(this.$route.params.mcode);
		},
		data() {
			return {
				// 请求的 url
				service: app.$svc.sys.custom,
				formData: {
					mcode: '',
					ftype: 'list',
					fSource: 'pc',
				},
				// Table列
				table: {
					primaryKey: "userId",
					columns: [
						{
							name: 'userId',
							hidden: true,
							width: 1,
						}, {
							name: 'attachId',
							hidden: true,
							width: 1,
						}, {
							name: 'userName',
							label: '人员名称',
							sortable: false,
							align: 'left',
						}, {
							name: 'userTel',
							label: '手机号码',
							hidden: true,
							sortable: false,
							align: 'left',
						}, {
							name: 'desc',
							label: '描述',
							sortable: false,
							align: 'left',
							width: '150px'
						}, {
							name: '',
							label: '操作',
							sortable: false,
							align: 'right',
							slot: 'actions',
							width: '25%'
						},

					],
				},
			}
		},

		// 方法
		methods: {
			// 查看方法
			handleInit(mcode) {

				this.formData.mcode = mcode;
				// 查询表单设计数据
				this.service.form.mcode(this.formData).then((result) => {
					// 判断code
					if (result.code == 1) {

						let fjson = JSON.parse(result.data.fjson);
						console.log(fjson)
						this.formData = result.data
					} else {
						// 服务器返回失败
						this.$message.error('错误提示:' + result.message)
					}
				})
			},


		}
	}
</script>

<style lang="less" scoped>
</style>
