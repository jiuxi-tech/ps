<template>
	<fb-flex>
		<fb-page-tree title="菜单|权限树">
			<template slot="tree">
				<fb-tree style="overflow: auto" v-autoheight="152" ref="menuTree" :data="menuData"
					:reader="{ value: 'id', label: 'text' }" @on-select-change="handleSelectChange"></fb-tree>
			</template>
			<template slot="tree-actions">
				<fb-button  
					@on-click="handleTreeHistoryBtnClick">历史</fb-button>

				<fb-button :icon="treeExpand ? 'tree-expansion' : 'tree-closed'"
					@on-click="handleTreeExpand"></fb-button>
			</template>

			<template slot="actions">
				<fb-flex gap="8px">
					<fb-button icon="node" @on-click="addCurrent" v-permission="'SYS_MENUMANAGE_MENU_ADD'"
						:disabled="menuParam.menuId === ''">添加同级
					</fb-button>
					<fb-button icon="node" @on-click="addChild" v-permission="'SYS_MENUMANAGE_MENU_ADD'"
						:disabled="menuParam.menuId === ''">添加下级
					</fb-button>
					<fb-button icon="editor-square" @on-click="update" v-permission="'SYS_MENUMANAGE_MENU_UPDATE'"
						:disabled="menuParam.menuId === ''">修改
					</fb-button>
					<fb-button icon="delete" @on-click="delChild" danger v-permission="'SYS_MENUMANAGE_MENU_DELETE'"
						:disabled="menuParam.menuId === ''">删除本级
					</fb-button>
				</fb-flex>
			</template>

			<template slot="form">
				<fb-property bordered label-width="140px" mode="form" v-autoheight="152">
					<fb-row>
						<fb-col span="20">
							<fb-property-item label="菜单名称">
								{{ formData.menuName }}
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="20">
							<fb-property-item label="菜单编码">
								{{ formData.menuCode }}
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="20">
							<fb-property-item label="菜单类型">
								{{ formData.menuTypeName }}
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="20">
							<fb-property-item label="菜单url">
								{{ formData.menuUri }}
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="20">
							<fb-property-item label="菜单归属 (app/pc)">
								{{ formData.menuSourceName }}
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="20">
							<fb-property-item label="菜单图标">
								<fb-icon :name="formData.menuIcon"></fb-icon>
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="20">
							<fb-property-item label="菜单简介">
								{{ formData.menuDesc }}
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="20">
							<fb-property-item label="排序">
								{{ formData.orderIndex }}
							</fb-property-item>
						</fb-col>
					</fb-row>

					<fb-row>
						<fb-col span="20">
							<fb-property-item label="是否启用">
								{{ formData.enabled === 1 ? "是" : "否" }}
							</fb-property-item>
						</fb-col>
					</fb-row>


					<fb-row>
						<fb-col span="20">
							<fb-property-item label="菜单PID">
								{{ formData.menuPid }}
							</fb-property-item>
						</fb-col>
					</fb-row>
					<fb-row>
						<fb-col span="20">
							<fb-property-item label="菜单ID">
								{{ formData.menuId }}
							</fb-property-item>
						</fb-col>
					</fb-row>

				</fb-property>
			</template>
		</fb-page-tree>
		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
	</fb-flex>
</template>

<script>

import autoheight from '@fb/fb-core/src/directive/autoheight'

export default {
	mixins: [

	],
	// 列表自适应高度
	directives: {
		autoheight: autoheight
	},
	// 初始化方法
	mounted() {
		// 执行界面初始化方法
		this.initMenuTreeData('', '');

	},
	data() {
		return {
			// 请求的 service
			service: this.$svc.sys.menu,
			treeExpand: false,
			// 部门树参数
			menuParam: {
				menuPid: '',
				menuId: '',
				menuCode: '',
			},
			// 选中的节点
			selectNode: {
				children: false,
			},
			// 部门树加载数据
			menuData: [],
			// 部门详情参数
			formData: {
				menuId: '',
				menuName: '',
				menuCode: '',
				menuUri: '',
				menuPid: '',
				menuSourceName: '',
				menuTypeName: '',
				menuIcon: '',
				menuDesc: '',
				orderIndex: '',
				enabled: 0,
			},
		}
	},

	// 方法
	methods: {
		// 初始化树数据
		initMenuTreeData(menuId, selectMenuId) {
			this.service.tree({ menuId: menuId }).then((result) => {
				if (result.code == 1) {
					this.menuData = result.data;
					// 默认选中根节点
					this.$nextTick(() => {
						if (selectMenuId) {
							this.$refs.menuTree.selectNodeByValue(selectMenuId);
						} else {
							this.$refs.menuTree.selectNodeByValue(result.data[0].id);
						}
					})
				} else {
					// 服务器返回失败
					this.$message.error('部门树加载失败' + result.message)
				}
			})
		},

		// 新增同级方法
		addCurrent() {
			// 界面跳转
			if (!this.menuParam.menuPid) {
				this.$message.error('请选择节点')
				return
			}
			var param = { "id": "", "menuPid": this.menuParam.menuPid };
			this.$refs.TpDialog.show(import('../../../views/sys/menu/add.vue'), param, "新增");
		},
		// 新增下级
		addChild() {
			// 界面跳转
			if (!this.menuParam.menuId) {
				this.$message.error('请选择节点')
				return
			}
			var param = { "id": "", "menuPid": this.menuParam.menuId, "menuCode": this.menuParam.menuCode };
			this.$refs.TpDialog.show(import('../../../views/sys/menu/add.vue'), param, "新增");
		},
		// 修改
		update() {
			if (!this.menuParam.menuId) {
				this.$message.error('请选择修改节点')
				return
			}
			// 界面跳转
			var param = { "id": this.menuParam.menuId };
			this.$refs.TpDialog.show(import('../../../views/sys/menu/add.vue'), param, "修改");
		},
		// 删除方法
		delChild() {
			if (!this.menuParam.menuId) {
				this.$message.error('请先选择一条记录！')
			} else {
				if (this.selectNode.children) {
					this.$message.error('所选菜单有子菜单，不能删除！请先删除子菜单。');
					return;
				}

				this.$confirm('确定要删除吗？', () => {
					// 删除
					this.service.delete(this.formData).then((result) => {
						if (result.code == 1) {
							this.$message.success('删除成功');
							this.initMenuTreeData('', '');
						} else {
							this.$message.error('删除失败：' + result.message)
						}
					})
				})
			}
		},
		// 新增 / 修改 回调方法
		handleQuery(menuId) {
			// 1、刷新树
			this.initMenuTreeData('', menuId);
			// 2、查询新增 / 修改 的部门信息
			this.handleView(menuId);
		},
		// 查看方法
		handleView(menuId) {
			// 树的回调方法，默认查询node[0]的详情
			this.service.view({ "menuId": menuId }).then((result) => {
				// 判断code
				if (result.code == 1) {
					this.formData = result.data
				} else {
					// 服务器返回失败
					this.$message.error('查询失败')
				}
			}).catch((err) => {
				// 服务器返回失败
				console.log(err);
			})
		},

		handleSelectChange(node) {
			if (node) {
				this.menuParam.menuId = node.id
				this.menuParam.menuPid = node.pid
				this.menuParam.menuCode = node.extend
				if (node.children.length !== 0) {
					this.selectNode.children = true;
				} else {
					this.selectNode.children = false;
				}
				this.handleView(node.id);
			} else {
				this.menuParam.menuId = ""
				this.menuParam.menuPid = ""
				this.menuParam.menuCode = ""
			}
		},

		closeDialog(param) {
			if (param) {
				this.handleQuery(param)
			}
		},

		handleTreeExpand() {
			this.treeExpand = !this.treeExpand
			this.$refs.menuTree.expandAll(this.treeExpand)
		},
		handleTreeHistoryBtnClick() {
			if (!this.menuParam.menuId) {
				this.$message.error('请先选择一个菜单节点');
				return;
			}
			var param = { "menuId": this.menuParam.menuId, "menuName": this.formData.menuName };
			this.$refs.TpDialog.show(import('./history.vue'), param, "菜单历史记录");
		}
	}
}
</script>

<style lang="less" scoped></style>
