<template>
	<div class="tp-dialog">
		<div class="tp-dialog-top">
			<fb-dialog-tree-select>

				<template slot="title">
					<span style="font-weight: bold"> 已选择 {{tags.length}} 项 </span>
				</template>

				<template slot="tags">
					<fb-tags v-model="tags" type="primary" effect="dark" @on-remove="handleTagRemove">
						<template v-slot="{node}">
							{{ node.label }}
						</template>

					</fb-tags>
				</template>

				<template slot="tree">
					<fb-card header="所在部门" :body-style="{height: '330px'}" body-overflow="auto">
						<fb-tree style="overflow: auto; height: 296px"
								 ref="tree"
								 :service="$svc.sys.dept.ent.allTree"
								 :param="{}"
								 :reader="{value: 'id', label: 'text'}"
								 @on-data-load="callBack"
								 @on-select-change="handleSelectChange"/>
					</fb-card>
				</template>

				<template slot="checkbox">
					<fb-card header="人员选择" :body-style="{height: '330px'}">
						<div>
							<fb-input v-model="formData.personName" placeholder="搜索" prependIcon="search">
								<div slot="append-button">
									<fb-button type="primary" icon="search" @on-click="handleQuery">搜索</fb-button>
								</div>
							</fb-input>
						</div>
						<div style="padding: 8px 0 0 0; margin-bottom: 4px; border-bottom: 1px solid #E8E8E8;">
							<fb-checkbox :value="allChecked" label="全选" @on-click="checkAll"></fb-checkbox>
						</div>
						<div style="height: 210px; overflow: auto">
							<fb-checkbox-group v-model="checked"
											   :data="nodes"
											   vertical
											   @on-change="onChangeCheckbox">
								<template v-slot:label="{node}">
									<fb-text>{{ node.label }}</fb-text>
									<fb-text> -</fb-text>
									<fb-text>{{ node.phone }}</fb-text>
								</template>
							</fb-checkbox-group>
						</div>
					</fb-card>
				</template>

			</fb-dialog-tree-select>
		</div>

		<div class="tp-dialog-bottom">
			<fb-button style="margin-right: 12px" type="primary" @on-click="add">确定</fb-button>
			<fb-button @on-click="handleClose">关闭</fb-button>
		</div>
	</div>
</template>

<script>


	export default {
		name: 'select-user-multiple',
		mixins: [],
		components: {},
		// 接收父组件的传参
		props: {
			param: {
				type: Object,
				require: false,
			},
			parentPage: {
				type: Object,
				default: null,
			},
		},
		mounted() {
			this.initView(this.param);
		},
		data() {
			return {
				// 已选择的节点
				/*{
                 value: 1,
                 label: '选项一',
                 type: "primary",
                 closable: true
                 }*/
				tags: [],
				checked: [],
				// 人员待选列表
				/*{
                 value: 1,
                 label: '选项一',
                 }*/
				nodes: [],
				formData: {
					personName: '',
					deptId: '',
					size: 1000,
					current: 1,
				},
			}
		},
		methods: {
			// 回显方法
			initView(param) {
				let that = this;
				if (param && param.persons && param.persons.length >= 1) {
					param.persons.forEach(function (node) {
						that.tags.push({
							label: node.personName,
							value: node.personId + ":" + node.deptId,
							deptId: node.deptId,
							type: 'primary',
							closable: true,
						})
					})

				}
			},

			// 取消
			handleClose() {
				this.closeTpDialog()
			},
			// 确定
			add() {
				// 1 单弹出框新增成功，关闭弹出框，param是回传的参数
				let param = this.tags.map(sel => {
					return {
						label: sel.label,
						value: sel.value.split(":")[0],
						deptId: sel.deptId,
						type: sel.type,
						closable: sel.closable,
					}
				})
				this.closeTpDialog(param)
			},
			// 树的回调方法, 默认选中根节点
			callBack(node) {
				this.$nextTick(() => {
					if (node.data.length > 0) {
						// 默认选择第一个根节点的根node
						this.$refs.tree.selectNodeByValue(node.data[0].id)
					}
				})
			},
			// 树点击的选中节点回调方法
			handleSelectChange(tagsNode) {
				// 选中的部门
				this.formData.deptId = tagsNode.id
				this.handleQuery()
			},
			// 列表方法
			handleQuery() {

				app.$svc.common.ent.getPersonByDeptId(this.formData).then((result) => {
					// 判断code
					if (result.code == 1) {
						// 清空原来的数据
						this.nodes = []

						this.checked = []

						// 人员列表
						let persons = result.data.records

						this.$nextTick(() => {

							persons.forEach((node, index) => {
								let n = {
									value: node.personId + ":" + node.deptId,
									label: node.personName,
									deptId: node.deptId,
									phone: node.phone,
								}
								console.log(n)
								this.nodes.push(n)
								if (this.tags.map(sel => sel.value).includes(node.personId + ":" + node.deptId)) {
									this.checked.push(node.personId + ":" + node.deptId)
								}
							})
						})
					} else {
						// 服务器返回失败
						this.$message.error(result.message)
					}
				}).catch((err) => {
					// 服务器返回失败
					console.log(err)
				})
			},
			// 删除的监听
			handleTagRemove(value, node) {
				let inChecked = this.checked.indexOf(value)
				if (inChecked !== -1) {
					this.checked.splice(inChecked, 1)
				}
			},
			// 人员选择框中点击选择某一项
			onChangeCheckbox(chks, value, checked, node) {

				let inTags = this.tags.map(sel => sel.value).indexOf(value)

				// add
				if (checked) {
					if (inTags === -1) {
						this.tags.push({
							label: node.label,
							value: node.value,
							deptId: node.deptId,
							type: 'primary',
							closable: true,
						})
					}
				} else {
					// remove
					if (inTags !== -1) {
						this.tags.splice(inTags, 1)
					}
				}

			},

			// 全选
			checkAll(checked, e) {

				// 全选
				if (checked) {
					this.nodes.forEach((node, index) => {
						let inTags = this.tags.map(sel => sel.value).indexOf(node.value)
						if (inTags === -1) {
							this.tags.push({
								label: node.label,
								value: node.value,
								deptId: node.deptId,
								type: 'primary',
								closable: true,
							})
						}
					})

					this.checked = this.nodes.map(node => node.value)
				} else { // 取消全选
					this.nodes.forEach((node, index) => {
						let inTags = this.tags.map(sel => sel.value).indexOf(node.value)
						if (inTags !== -1) {
							this.tags.splice(inTags, 1)
						}
					})
					this.checked = []
				}
			},
		},
		computed: {
			allChecked: {
				get() {
					return (this.checked.length === 0 || this.nodes.length === 0) ? false : (this.checked.length ===
					this.nodes.length
						? true
						: 'indeterminate')
				},
				set(value) {
					console.log("allChecked setter", value)
				},

			},
		},
	}
</script>

<style scoped lang="less">


</style>
