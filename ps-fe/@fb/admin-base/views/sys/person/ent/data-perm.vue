<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-form ref="fbform" :label-width="60" autocomplete="off">
				<fb-row>
					<fb-col span="24">
						<fb-form-item label="范围" prop="dataScope" :rule="[{required: true}]">
							<fb-radio-group @change="handleChange" :data="scopeOptions" :value="formData.dataScope" :radioSpace="20" />
						</fb-form-item>
					</fb-col>
				</fb-row>
				<div v-if="inited" v-show="formData.dataScope == 'SYS4803'">
					<fb-row>
						<fb-col span="24">
							<div>
								<span style="font-weight: bold"> 已选择 {{tags.length}} 项 </span>
							</div>
							<div class="tags-area">
								<fb-tags v-model="tags" type="primary" effect="dark" @on-remove="handleTagRemove">
									<template v-slot="{node}">
										{{ node.label }}
									</template>
								</fb-tags>
							</div>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col span="24">
							<fb-card header="选择部门" :body-style="{height: '251px'}" body-overflow="auto">
								<fb-tree
									multiple
									ref="tree"
									:service="$svc.sys.dept.ent.tree"
									:reader="{value:'id', label: 'text'}"
									:param="{sync: 0}"
									:load-data="loadEntTreeData"
									@on-check-change="handleCheckChange"
									@on-data-update="handleDataUpdate"
									:height="216"
									:do-check="''"
									:do-un-check="''"
								></fb-tree>
							</fb-card>
						</fb-col>
					</fb-row>
				</div>
				<div v-if="inited" v-show="formData.dataScope == 'SYS4804'">
					<fb-row>
						<fb-col span="24">
							<div class="tag-area">
								<span class="tag-area-title" style="font-weight: bold"> 已选择：</span>
								<fb-tag type="primary"
										v-show="selectDeptId != ''"
										closable>{{selectDeptName}}</fb-tag>
							</div>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col span="24">
							<fb-card header="选择部门" :body-style="{height: '341px'}" body-overflow="auto">
								<fb-tree
									ref="select-tree"
									:service="$svc.sys.dept.ent.tree"
									:reader="{value:'id', label: 'text'}"
									:param="{sync: 0}"
									:load-data="loadEntTreeData"
									@on-select-change="handleSelectChange"
									:height="306"
								></fb-tree>
							</fb-card>
						</fb-col>
					</fb-row>
				</div>

			</fb-form>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="add">保存</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>

		<tp-dialog ref="TpDialog"></tp-dialog>
	</div>
</template>

<script>


	export default {
		name: 'account-add',
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
		// 创建方法
		created() {
		},
		// 初始化方法
		mounted() {
			// 执行界面初始化
			this.init(this.param);
		},
		data() {
			return {
				// 请求的 service
				service: this.$svc.sys.dataperm,
				scopeOptions: [
					{value: 'SYS4801', label: '本部门'},
					{value: 'SYS4802', label: '本部门及下级部门'},
					{value: 'SYS4803', label: '指定部门'},
					{value: 'SYS4804', label: '指定部门及下级部门'}
				],
				inited: false,
				// 表单数据
				formData: {
					personId: '',
					deptId: '',
					dataScope: 'SYS4802',
					deptIds: ''
				},
				// 选择的多个指定部门
				tags: [],
				// 选择的单个指定部门id
				selectDeptId: '',
				// 选择的单个指定部门名称
				selectDeptName: '',
			}
		},

		// 方法
		methods: {
			// 初始化参数
			init(param) {
				let personId = param.personId;
				let deptId = param.deptId;
				this.formData.personId = personId;
				this.formData.deptId = deptId;
				this.view(personId, deptId);
			},

			// 取消
			handleClose() {
				// 关闭，并传递参数
				this.closeTpDialog();
			},

            // 处理change事件
			handleChange(value) {
				this.formData.dataScope = value;
			},

			// 选中部门(多选)
			handleCheckChange(checkNodes, node) {
				let inTags = this.tags.map(sel => sel.value).indexOf(node.value)
				if (node.checked) {
					if (inTags === -1) {
						this.tags.push(node);
					}
				} else {
					if (inTags !== -1) {
						this.tags.splice(inTags, 1)
					}
				}
			},

			// 回显树节点
			handleDataUpdate(){
				for (let i = 0; i < this.tags.length; i++) {
					let tag = this.tags[i];
					this.$refs.tree.checkNodeByValue(tag.value)
				}
			},

			// 选中部门 （单选）
			handleSelectChange(node) {
				this.selectDeptId = node.value;
				this.selectDeptName = node.label;
			},

			// 删除标签的监听
			handleTagRemove(value) {
				this.$refs.tree.checkNodeByValue(value, false)
			},

			// 部门树异步加载，查询除顶层之外的下级，一层一层查
			loadEntTreeData(item, callback) {
				if (item.children.length >= 1) {
					callback();
				} else {
					this.$svc.sys.dept.ent.tree({deptId: item.id, sync: 0}).then((result) => {
						if (result.code == 1) {
							if (result.data[0].children.length >= 1) {
								callback(result.data[0].children);
								this.handleDataUpdate();
							}else{
								callback();
							}
						} else {
							// 服务器返回失败
							this.$message.error('企业部门树加载失败' + result.message)
						}
					})
				}

			},

			view(personId, deptId) {
				// 调用新增service方法
				this.service.view({"personId": personId, "deptId": deptId, "passKey": this.param.passKey}).then((result) => {
					// 判断code
					if (result.code === 1) {
						let data = result.data;
						if (data) {
							this.formData.dataScope = data.dataScope;
							let nodes = data.deptNodes;
							if (nodes && nodes.length > 0) {
								if (data.dataScope === 'SYS4803') {
									this.tags = nodes;
								} else
								if (data.dataScope === 'SYS4804') {
									this.selectDeptId = nodes[0].value;
									this.selectDeptName = nodes[0].label;
								}
							}
						}
						this.inited = true;
					} else {
						// 服务器返回失败
						this.$message.error('查询失败')
					}
				})
			},

			// 新增
			add() {
				// 界面校验
				this.$refs.fbform.validate((result) => {
					if (result === true) {
						// 指定多个部门
						if (this.formData.dataScope === 'SYS4803') {
							if (this.tags.length ===0) {
								this.$message.warn("请选择一个或多个部门");
								return;
							}
							let max = 100;
							if (this.tags.length > max) {
								this.$message.warn("选择部门个数不能超过" + max + "个");
								return;
							}
							this.formData.deptIds = this.tags.map((node)=> {return node.id}).join(",");
						} else
						// 	指定一个部门
						if (this.formData.dataScope === 'SYS4804') {
							if (!this.selectDeptId) {
								this.$message.warn("请选择一个部门");
								return;
							}
							this.formData.deptIds = this.selectDeptId;
						} else {
							// 其它情况时，deptIds部门为空
							this.formData.deptIds = '';
						}
						this.formData.passKey = this.param.passKey;
						this.service.add(this.formData).then((result) => {
							// 判断code
							if (result.code == 1) {
								this.$message.success('保存成功！');
								// 关闭，并传递参数
								this.handleClose();
							} else {
								// 服务器返回失败
								this.$message.error('新增失败')
							}
						})
					}
				})
			},

		}
	}
</script>

<style lang="less" scoped>

	/**
	 * 选择多个指定部门的区域
	 */
    .tags-area {
		width: 100%;
		height: 108px;
		background: #FFFFFF;
		border-radius: 4px;
		border: 1px solid #D3D3D3;
		padding: 8px;
		overflow: auto;
		margin-bottom: 12px;
	}
	/**
	 * 选择单个指定部门的区域
	 */
	.tag-area {
		margin-bottom: 12px;
	}

	.tag-area-title {
		display: inline-block;
		height: 26px;
		margin-top: 10px;
	}
</style>
