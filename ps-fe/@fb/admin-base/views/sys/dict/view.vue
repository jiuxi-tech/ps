<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-page-tree title="字典树">
				<template slot="tree">
					<div style="height:240px; overflow-y: auto; margin: -20px; padding: 20px;">
						<fb-tree
							v-autoheight="350"
							ref="dictTree"
							:data="dictData"
							:reader="{value: 'id', label: 'text'}"
							@on-select-change="handleSelectChange"></fb-tree>
					</div>
				</template>

				<template slot="form">
					<fb-form ref="fbform" v-autoheight="350">
						<fb-row>
							<fb-col span="12">
								<fb-form-item label="字典编码">
									<fb-input v-model="formData.dicCode" readonly></fb-input>
								</fb-form-item>
							</fb-col>
							<fb-col span="12">
								<fb-form-item label="字典名称">
									<fb-input v-model="formData.dicName" readonly></fb-input>
								</fb-form-item>
							</fb-col>
						</fb-row>
						<fb-row>
							<fb-col span="12">
								<fb-form-item label="字典类型">
									<fb-select v-model="formData.dicType"
											   :data="[
													{value:'-1', label: '系统'},
													{value:'1', label: '业务'}
												]"
											   readonly/>
								</fb-form-item>
							</fb-col>
							<fb-col span="12">
								<fb-form-item label="排序">
									<fb-input v-model.number="formData.orderIndex" readonly></fb-input>
								</fb-form-item>
							</fb-col>
						</fb-row>

						<fb-row>
							<fb-col span="24">
								<fb-form-item label="字典描述">
									<fb-textarea rows="2"
												 v-model="formData.dicDesc"
												 type="text"
												 readonly></fb-textarea>
								</fb-form-item>
							</fb-col>
						</fb-row>

						<fb-row>
							<fb-col span="12">
								<fb-form-item label="是否启用">
									<fb-radio-group v-model="formData.enabled"
													:data="[{id: 1, name: '是',}, {id: 0, name: '否',}]"
													:reader="{label:'name', value:'id'}" disabled></fb-radio-group>
								</fb-form-item>
							</fb-col>
						</fb-row>
					</fb-form>
				</template>
			</fb-page-tree>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>


	export default {
		name: 'dict-view',
		mixins: [

		],
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
		components: {},
		// 创建方法
		created() {
			// 记录原来的默认值，用于表单重置
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化方法
			this.init(this.param);
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.dict,
				// 字典树参数
				dictData: [],
				// 根节点
				rootNode: {
					dicId: '',
					dicCode: '',
				},
				// 选中节点
				selectNode: {
					dicId: '',
					pdicId: '',
					dicCode: '',
				},
				// 父字典code
				pdicCode: '',
				// 部门详情参数
				formData: {
					dicId: '',
					pdicId: '',
					dicCode: '',
					dicName: '',
					dicType: '',
					orderIndex: '',
					dicDesc: '',
					enabled: 1,
				},
			}
		},

		// 方法
		methods: {
			init(param) {
				if (param.id) {
					// 1. 查询树数据，初始化树
					this.initDictTreeData(param.dicCode);
				} else {
					// 不存在根节点，即类似新增
					this.$message.error('请选择查看节点');
					return;
				}
			},

			// 初始化树数据
			initDictTreeData(dicCode) {
				this.service.tree({"dicCode": dicCode}).then((result) => {
					if (result.code == 1) {
						this.dictData = result.data;
						// 默认选中根节点
						this.$nextTick(() => {
							this.$refs.dictTree.selectNodeByValue(result.data[0].id);
						})
					} else {
						// 服务器返回失败
						this.$message.error('字典树加载失败' + result.message)
					}
				})
			},

			// 查看方法
			handleView(dicId) {
				let that = this;
				this.service.view({"dicId": dicId}).then((result) => {
					// 判断code
					if (result.code == 1) {
						that.formData = result.data
					} else {
						// 服务器返回失败
						that.$message.error('查询失败')
					}
				}).catch((err) => {
					// 服务器返回失败
					console.log(err);
				});
			},

			handleSelectChange(node) {
				this.pdicCode = '';
				if (node) {
					// 查询节点信息,extend01是字典的id
					this.handleView(node.extend01);
				} else {
					// 取消标记

				}
			},

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog("xxxx");
			},

		}
	}
</script>

<style lang="less" scoped>

</style>
