<template>
	<div>
		<fb-page-tree title="帆软报表源文件">
			<template slot="tree">
				<fb-tree
					:data="fineData"
					:reader="{value: 'id', label: 'text'}"
					@on-select-change="handleSelectChange"
					ref="fineTree"
					style="overflow: auto"
					v-autoheight="152"></fb-tree>
			</template>
			<template slot="tree-actions">
				<fb-button :icon="treeExpand ? 'tree-expansion': 'tree-closed'"
						   @on-click="handleTreeExpand"></fb-button>
			</template>

			<template slot="actions">
				<fb-button :disabled="selectNode.fileName === ''" @on-click="delChild" danger
						   icon="delete">删除
				</fb-button>
			</template>
			<template slot="form">
				<fb-fieldset label="上传"/>
				<fb-container style="padding-top: 20px; width: 90%">
					<fb-form>
						<fb-form-item :label-width="180" label="帆软报表文件上传">
							<tp-upload
								:accept="'.frm,.cpt'"
								:button-text="'上传'"
								:param="{}"
								:service="$svc.sys.report.upload"
								:show-download="false"
								:show-remove="false"
								@on-error="onError"
								@on-success="onSuccess"
								multiple
							></tp-upload>
						</fb-form-item>
					</fb-form>
				</fb-container>
			</template>

			<template slot="form">
				<fb-fieldset label="访问信息"/>
				<fb-property bordered label-width="120px" mode="form">
					<fb-row>
						<fb-col span="12">
							<fb-property-item label="访问路径">
								{{path}}
							</fb-property-item>
						</fb-col>
					</fb-row>

				</fb-property>
			</template>
		</fb-page-tree>
		<tp-dialog ref="TpDialog"></tp-dialog>
	</div>
</template>

<script>


	export default {
		name: 'tree',
		mixins: [

		],
		// 组件
		components: {},
		// 初始化方法
		mounted() {
			// 执行界面初始化方法
			this.initFineTreeData();
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.report,
				treeExpand: true,
				// 部门树根节点
				selectNode: {
					fileName: '',
				},
				// 目录树加载数据
				fineData: [],
				// 访问路径
				path: '',
			}
		},

		// 方法
		methods: {
			// 初始化树数据
			initFineTreeData() {
				let that = this;
				this.service.tree({}).then((result) => {
					if (result.code == 1) {
						if (result.data) {
							that.fineData = result.data;
						}
					} else {
						// 服务器返回失败
						this.$message.error('部门树加载失败' + result.message)
					}
				})
			},

			// 删除方法
			delChild() {
				let that = this;
				if (that.selectNode.fileName === '') {
					that.$message.error('请先选择一条记录！')
				} else {
					that.$confirm('帆软报表源文件将被删除，确定要删除吗？', () => {
						// 删除
						that.service.delete({"fileName": that.selectNode.fileName}).then((result) => {
							if (result.code == 1) {
								that.$message.success('删除成功');
								that.initFineTreeData();
								that.path = '';
							} else {
								// 服务器返回失败
								that.$message.error('删除失败:' + result.message)
							}
						})
					})
				}
			},

			handleSelectChange(node) {
				let that = this;
				if (node) {
					that.selectNode.fileName = node.text
					that.service.view({"fileName": that.selectNode.fileName}).then((result) => {
						if (result.code == 1) {
							that.path = result.data;
						} else {
							// 服务器返回失败
							that.$message.error('删除失败:' + result.message)
						}
					})
				} else {
					that.selectNode.fileName = ''
				}
			},

			onSuccess(value) {
				this.initFineTreeData();
				this.path = value.data;
				this.$message.success('上传成功');
			},
			onError(value) {
				this.$message.error('上传失败:')
			},

			closeDialog(param) {

			},

			handleTreeExpand() {
				this.treeExpand = !this.treeExpand
				this.$refs.fineTree.expandAll(this.treeExpand)
			}

		}
	}
</script>

<style lang="less" scoped>

</style>
