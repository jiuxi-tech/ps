<!-- 机构人员管理页面 - 左侧树形结构，右侧人员列表 -->
<template>
    <div>
        <!-- 页面主体：树表结构布局 -->
        <fb-page-tree-table title="机构树">
            <!-- 左侧部门树 -->
            <template slot="tree">
                <!-- 部门树组件：同步加载，显示组织架构，支持节点选择 -->
                <fb-tree
                    style="overflow: auto"
                    v-autoheight="152"
                    ref="deptTree"
                    :data="deptData"
                    :reader="{value: 'id', label: 'text'}"
                    @on-select-change="handleSelectChange"></fb-tree>
            </template>
            <!-- 树操作按钮 -->
            <template slot="tree-actions">
                <!-- 树展开/收起按钮 -->
                <fb-button :icon="treeExpand ? 'tree-expansion': 'tree-closed'"
                           @on-click="handleTreeExpand"></fb-button>
            </template>

            <!-- 查询条件区域 -->
            <template slot="query">
                <!-- 查询表单：用户名、手机号、性别筛选 -->
                <fb-form ref="query-form" mode="query">
                    <fb-row>
                        <fb-col span="8">
                            <fb-form-item label="用户名">
                                <fb-input v-model="formData.personName"></fb-input>
                            </fb-form-item>
                        </fb-col>
                        <fb-col span="8">
                            <fb-form-item label="手机号码">
                                <fb-input v-model="formData.phone"></fb-input>
                            </fb-form-item>
                        </fb-col>
                        <fb-col span="8">
                            <fb-form-item label="性别">
                                <fb-select v-model="formData.sex"
                                           :data="defaultForm.sex"
                                           :placeholder="'请选择'"
                                           clearable/>
                            </fb-form-item>
                        </fb-col>
                    </fb-row>
                </fb-form>
            </template>

            <!-- 操作按钮区域 -->
            <template slot="buttons">
                <!-- 新增人员按钮 -->
                <fb-button ref="buttonAdd" @on-click="handleAdd" v-permission="'SYS_ORG_PERSON_ADD'" icon="add-circle"
                           :disabled="selectNode.deptId === ''">
                    新增
                </fb-button>
                <!-- 导出Excel按钮：支持选中用户或全部用户导出 -->
                <fb-button ref="buttonExport" @on-click="handleExportExcel" icon="download"
                           :disabled="selectNode.deptId === ''" :loading="exportLoading">
                    导出Excel {{
                        table.selectRow && table.selectRow.length > 0 ? '（' + table.selectRow.length + '）' : ''
                    }}
                </fb-button>
                <!-- 导入Excel按钮：批量导入用户信息 -->
                <fb-button ref="buttonImport" @on-click="handleImportExcel" icon="upload"
                           :disabled="selectNode.deptId === ''" :loading="importLoading">
                    导入Excel
                </fb-button>
                <!-- 下载模板按钮：下载用户导入模板文件 -->
                <fb-button ref="buttonDownloadTemplate" @on-click="handleDownloadTemplate" icon="download"
                           :loading="templateLoading">
                    下载模板
                </fb-button>
                <!--				<fb-button ref="buttonDel" @on-click="handleDels" v-permission="'SYS_ORG_PERSON_DELETE'"-->
                <!--						   icon="reduce-circle" :disabled="table.selectRow.length === 0">-->
                <!--					批量删除-->
                <!--				</fb-button>-->
            </template>

            <!-- 查询操作按钮 -->
            <template slot="actions">
                <!-- 查询按钮：根据选中部门和筛选条件查询人员 -->
                <fb-button type="primary" icon="search" @on-click="handleQuery" :disabled="selectNode.deptId == ''">
                    查询
                </fb-button>
            </template>

            <!-- 人员列表表格 -->
            <template slot="table">
                <!-- 人员数据表格：显示选中部门的人员信息，支持多选和操作 -->
                <fb-simple-table
                    ref="table"
                    v-model="table.selectRow"
                    :service="table.service.org.list"
                    :param="formData"
                    :pk="'personId'"
                    :columns="table.columns"
                    :auto-load="false"
                    multiple
                    autoSelect
                    :scroll="{x:1000, y: 368, autoHeight: true}"
                    @on-row-select="handleTableSelect">

                    <!-- 表格行操作按钮 -->
                    <template v-slot:actions="props">
                        <fb-space>
                            <!-- 修改按钮：仅主部门人员可修改 -->
                            <fb-button @on-click="handleEdit(props.row)" v-permission="'SYS_ORG_PERSON_UPDATE'"
                                       v-if="props.row.defaultDept == 1"
                                       editor size="s">修改
                            </fb-button>
                            <!-- 账号管理按钮 -->
                            <fb-button @on-click="handleAccount(props.row)" v-permission="'SYS_ORG_PERSON_ACCOUNT'"
                                       editor size="s">账号
                            </fb-button>
                            <!-- 角色授权按钮 -->
                            <fb-button @on-click="handleAuth(props.row)" v-permission="'SYS_ORG_PERSON_AUTHADD'"
                                       editor size="s">授权
                            </fb-button>
                            <!-- 标签管理按钮 v-permission="'SYS_ORG_PERSON_TAG'"-->
                            <fb-button @on-click="handleTag(props.row)"
                                       editor size="s">标签
                            </fb-button>
                            <!-- 兼职管理按钮：仅主部门人员可设置兼职 -->
                            <fb-button @on-click="handleParttimejob(props.row)"
                                       v-permission="'SYS_ORG_PERSON_PARTTIMEADD'" v-if="props.row.defaultDept == 1"
                                       editor size="s">兼职
                            </fb-button>
                            <!-- 数据权限设置按钮 -->
<!--                            <fb-button @on-click="handleDataPermission(props.row)"-->
<!--                                       v-permission="'SYS_ORG_PERSON_DATA_PERM'"-->
<!--                                       editor size="s">数据权限-->
<!--                            </fb-button>-->
                            <!-- 删除按钮 -->
                            <fb-button @on-click="handleDel(props.row)" v-permission="'SYS_ORG_PERSON_DELETE'"
                                       danger size="s">删除
                            </fb-button>

                        </fb-space>
                    </template>

                    <!-- 用户名链接：点击查看详情 -->
                    <template v-slot:view="props">
                        <fb-link-group>
                            <fb-link :click="()=>handleView(props.row)" :label="props.row.personName"
                                     type="primary"></fb-link>
                        </fb-link-group>
                    </template>
                </fb-simple-table>
            </template>
        </fb-page-tree-table>

        <!-- 弹窗组件 -->
        <!-- 单页面弹窗：用于查看、账号管理等 -->
        <tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
        <!-- 多标签页弹窗：用于新增、修改等复杂表单 -->
        <tp-dialog-tab ref="TpDialogTab" @closeTpDialog="closeDialogTab"></tp-dialog-tab>
        
        <!-- 导入错误详情对话框 -->
        <fb-dialog ref="importErrorDialog" 
                   title="数据导入失败" 
                   width="700px"
                   :close-on-click-modal="false">
            <div style="max-height: 500px; overflow-y: auto;">
                <!-- 错误统计信息 -->
                <div style="margin-bottom: 15px; padding: 10px; background: #FFF7E6; border-left: 4px solid #E6A23C; border-radius: 4px;">
                    <p style="margin: 0; font-weight: bold; color: #E6A23C;">
                        共 {{ importErrors.totalRows }} 行数据，发现 {{ importErrors.errorCount }} 个错误，请修正后重新导入
                    </p>
                </div>
                
                <!-- 错误详情列表 -->
                <div style="background: #FEF0F0; padding: 15px; border-radius: 4px; border-left: 4px solid #F56C6C;">
                    <div v-for="(errors, rowNum) in importErrors.errorsByRow" :key="rowNum" 
                         style="margin-bottom: 15px;">
                        <!-- 行号 -->
                        <div style="font-weight: bold; color: #303133; margin-bottom: 8px; font-size: 14px;">
                            第 {{ rowNum }} 行：
                        </div>
                        <!-- 该行的所有错误 -->
                        <div v-for="(error, index) in errors" :key="index" 
                             style="padding-left: 20px; margin-bottom: 5px; color: #606266; line-height: 1.6;">
                            <span style="color: #909399;">•</span>
                            <span v-if="error.field" style="color: #909399;">[字段: {{ error.field }}]</span>
                            <span v-if="error.value" style="color: #909399;">[值: {{ error.value }}]</span>
                            <span style="color: #F56C6C;">{{ error.message }}</span>
                        </div>
                    </div>
                </div>
            </div>
            
            <template slot="footer-right">
                <fb-button type="primary" @on-click="closeImportErrorDialog">我知道了</fb-button>
            </template>
        </fb-dialog>
    </div>
</template>

<script>
/**
 * 机构人员管理页面
 * 功能：
 * 1. 左侧显示部门树形结构
 * 2. 右侧显示选中部门的人员列表
 * 3. 支持人员的增删改查操作
 * 4. 支持Excel导入导出功能
 * 5. 支持人员账号管理、角色授权、兼职设置等
 */
export default {
    name: 'list',
    mixins: [
        // 可在此处添加混入
    ],
    // 组件创建时执行
    created () {
        // 组件创建时的初始化逻辑
    },
    // 组件挂载后执行
    mounted () {
        // 执行界面初始化方法：加载部门树数据
        this.initDeptTreeData('', '')
    },
    // 组件数据
    data () {
        return {
            // 部门树数据：存储组织架构树形数据
            deptData: [],
            // 树展开状态：控制部门树的展开/收起
            treeExpand: true,
            // 导出Excel加载状态：防止重复点击
            exportLoading: false,
            // 导入Excel加载状态：防止重复点击
            importLoading: false,
            // 下载模板加载状态：防止重复点击
            templateLoading: false,
            // 导入错误信息：存储导入失败的错误详情
            importErrors: {
                totalRows: 0,
                errorCount: 0,
                errorsByRow: {}
            },
            // 当前选中的部门节点信息
            selectNode: {
                deptId: '', // 部门ID
                ascnId: '', // 单位ID
            },
            // 查询表单数据：用于人员列表筛选
            formData: {
                deptId: '', // 部门ID
                // 层级标识：0-本级，1-本级及下级
                levelFlag: 0,
                deptLevelcode: '', // 部门层级编码
                personName: '', // 用户名筛选
                sex: '', // 性别筛选
                phone: '', // 手机号筛选
                office: '', // 职务
                ascnId: '', // 单位ID
            },
            // 部门服务：用于调用部门相关API
            service_dept: this.$svc.sys.dept,
            // 表格配置：人员列表表格的相关配置
            table: {
                // 选中的行数据：支持多选
                selectRow: [],
                // 数据服务：人员相关API服务
                service: app.$svc.sys.person,
                // 主键字段
                primaryKey: 'id',
                // 表格列配置：定义显示的字段和操作
                columns: [
                    // 用户名列：支持点击查看详情
                    {
                        name: 'personName',
                        label: '用户名称',
                        sortable: false,
                        slot: 'view',
                        width: 120,
                    },
                    // 单位名称列
                    {
                        name: 'ascnName',
                        label: '单位名称',
                        sortable: false,
                        width: 120,
                    },
                    // 部门名称列
                    {
                        name: 'deptSimpleName',
                        label: '部门名称',
                        sortable: false,
                        width: 120,
                    },
                    // 兼职状态列
                    {
                        name: 'defaultDeptName',
                        label: '是否兼职',
                        sortable: false,
                        width: 100,
                    },
                    // 人员编号列
                    {
                        name: 'personNo',
                        label: '人员编号',
                        sortable: false,
                        width: 100,
                    },
                    // 性别列
                    {
                        name: 'sexName',
                        label: '性别',
                        sortable: false,
                    },
                    // 手机号码列
                    {
                        name: 'phone',
                        label: '手机号码',
                        sortable: false,
                        width: 120,
                    },
                    // 操作列：固定在右侧
                    {
                        name: '',
                        label: '操作',
                        sortable: false,
                        slot: 'actions',
                        width: 370,
                        freeze: 'right',
                    },
                ],
            },
        }
    },

    // 组件方法
    methods: {
        /**
         * 初始化部门树数据(同步加载完整树)
         * @param {string} deptId - 部门ID，空字符串表示加载根节点
         * @param {string} selectDeptId - 需要选中的部门ID
         */
        initDeptTreeData (deptId, selectDeptId) {
            this.service_dept.org.tree({
                deptId: deptId,
                'sync': 1,  // 同步加载完整树
            }).then((result) => {
                if (result.code == 1) {
                    if (result.data.length > 0) {
                        this.deptData = result.data
                        // 默认展开所有节点并选中根节点
                        this.$nextTick(() => {
                            // 默认展开所有节点
                            this.$refs.deptTree.expandAll(true)
                            this.treeExpand = true
                            
                            if (selectDeptId) {
                                this.$refs.deptTree.selectNodeByValue(selectDeptId)
                            } else {
                                this.$refs.deptTree.selectNodeByValue(result.data[0].id)
                            }
                        })
                    }
                } else {
                    // 服务器返回失败
                    this.$message.error('部门树加载失败' + result.message)
                }
            })
        },

        /**
         * 查询人员列表
         * 根据选中的部门和筛选条件查询人员数据
         */
        handleQuery () {
            // 必须选中部门才能查询
            if (this.selectNode.deptId !== '') {
                this.$refs.table.doSearch()
            }
        },

        /**
         * 重新加载人员列表
         */
        handleReload () { 
            this.$refs.table.reload()
        },

        /**
         * 表格行选择事件处理
         * @param {Object} row - 当前选中的行数据
         * @param {Array} selectedRows - 所有选中的行数据
         */
        handleTableSelect (row, selectedRows) {
            // 根据选中行数量控制批量操作按钮状态
            this.buttons.del.disabled = selectedRows.length == 0
        },
        /**
         * 新增人员
         * 打开新增人员的多标签页弹窗
         */
        handleAdd () {
            if (!this.selectNode.deptId) {
                this.$message.error('请选择部门添加人员')
                return
            }


            let param = {
                'id': '',
                'deptId': this.selectNode.deptId,
                'ascnId': this.selectNode.ascnId,
            }
            this.$refs.TpDialog.show(import('../../../../views/sys/person/org/add-basicinfo.vue'), param, '新增')


            // 界面跳转
//            let param = {
//                'id': '',
//                'deptId': this.selectNode.deptId,
//                'ascnId': this.selectNode.ascnId,
//            }
//            let tabArry = [
//                {
//                    url: import('../../../../views/sys/person/org/add-basicinfo.vue'),
//                    label: '人员基本信息',
//                    icon: 'chart-line',
//                },
////					{
////						url: import('../../../../views/sys/person/org/add-exinfo.vue'),
////						label: '人员扩展信息',
////						icon: "progressbar"
////					}
//            ]
//
//            this.$refs.TpDialogTab.show(tabArry, param, '新增', {
//                callBack: () => {
//                },
//            })
        },
        /**
         * 修改人员信息
         * @param {Object} row - 要修改的人员数据
         */
        handleEdit (row) {

            let param = {
                'id': row.personId,
                'deptId': row.deptId,
                'passKey': row.passKey,
            }
            this.$refs.TpDialog.show(import('../../../../views/sys/person/org/add-basicinfo.vue'), param, '修改')

//            let param = {
//                'id': row.personId,
//                'deptId': row.deptId,
//                'passKey': row.passKey,
//            }
//            let tabArry = [
//                {
//                    url: import('../../../../views/sys/person/org/add-basicinfo.vue'),
//                    label: '人员基本信息',
//                    icon: 'chart-line',
//                },
////					{
////						url: import('../../../../views/sys/person/org/add-exinfo.vue'),
////						label: '人员扩展信息',
////						icon: "progressbar"
////					}
//            ]
//
//            this.$refs.TpDialogTab.show(tabArry, param, '修改', {
//                callBack: () => {
//
//                },
//            })
        },
        /**
         * 删除人员（单个）
         * @param {Object} row - 要删除的人员数据
         */
        handleDel (row) {
            this.delete(row.deptId, row.personId, row.passKey)
        },
        /**
         * 执行删除操作
         * @param {string} deptIds - 部门ID
         * @param {string} personIds - 人员ID
         * @param {string} passKey - 验证密钥
         */
        delete (deptIds, personIds, passKey) {
            this.$confirm('确定要删除吗？', () => {
                app.$svc.sys.person.delete({
                    'deptIds': deptIds,
                    'personIds': personIds,
                    'passKey': passKey,
                }).then((result) => {
                    // 判断code
                    if (result.code == 1) {
                        this.handleReload()
                        this.$message.success('删除成功')
                    } else {
                        // 服务器返回失败
                        this.$message.error('删除失败')
                    }
                }).catch((err) => {
                    // 服务器返回失败
                    console.log(err)
                })
            })
        },

        /**
         * 查看人员详情
         * @param {Object} row - 要查看的人员数据
         */
        handleView (row) {
            let param = {
                'id': row.personId,
                'deptId': row.deptId,
                'passKey': row.passKey,
            }
            this.$refs.TpDialog.show(import('../../../../views/sys/person/org/view.vue'), param, '查看')
        },
        /**
         * 角色授权管理
         * @param {Object} row - 要授权的人员数据
         */
        handleAuth (row) {
            let param = {
                'id': row.personId,
                'deptId': row.deptId,
                'passKey': row.passKey,
            }
            this.$refs.TpDialog.show(import('../../../../views/sys/person/org/auth.vue'), param, '授权')
        },
        /**
         * 用户标签管理
         * @param {Object} row - 要设置标签的人员数据
         */
        handleTag (row) {
            let param = {
                'id': row.personId,
                'deptId': row.deptId,
                'passKey': row.passKey,
            }
            this.$refs.TpDialog.show(import('../../../../views/sys/person/org/tag.vue'), param, '标签')
        },
        /**
         * 兼职部门管理
         * @param {Object} row - 要设置兼职的人员数据
         */
        handleParttimejob (row) {
            let param = {
                'id': row.personId,
                'passKey': row.passKey,
            }
            this.$refs.TpDialog.show(import('../../../../views/sys/person/org/parttime-job.vue'), param, '兼职部门')
        },

        /**
         * 账号管理（开通/管理登录账号）
         * @param {Object} row - 要管理账号的人员数据
         */
        handleAccount (row) {
            let param = {
                'id': row.personId,
                'passKey': row.passKey,
            }
            this.$refs.TpDialog.show(import('../../../../views/sys/person/org/account-add.vue'), param, '账号')
        },

        /**
         * 数据权限设置
         * @param {Object} row - 要设置数据权限的人员数据
         */
        handleDataPermission (row) {
            let param = {
                'personId': row.personId,
                'deptId': row.deptId,
                'passKey': row.passKey,
            }
            this.$refs.TpDialog.show(import('../../../../views/sys/person/org/data-perm.vue'), param, '数据权限')
        },
        /**
         * 树数据加载完成回调（备用方法）
         * @param {Object} node - 树节点数据
         */
        callBack (node) {
            // 树的回调方法, 默认选中根节点
            this.$nextTick(() => {
                if (node.data.length > 0) {
                    // 默认选择第一个根节点
                    this.$refs.tree.selectNodeByValue(node.data[0].id)
                }
            })

            // 默认查询条件为所有根节点本级及下级的数据
            // this.formData.levelFlag = 1;
            // 查询根节点本级的列表数据
            // this.handleQuery();

        },
        /**
         * 部门树节点选择变化事件
         * @param {Object} node - 选中的树节点
         */
        handleSelectChange (node) {
            if (node) {
                // 选中那个节点，查询哪个节点本级的数据
                // this.formData.levelFlag = 0
                this.formData.deptId = node.value
                this.formData.ascnId = node.extend01
                this.selectNode.deptId = node.value
                this.selectNode.ascnId = node.extend01
                this.handleQuery()
            } else {
                // this.formData.levelFlag = 1;
                // 选中节点置空
                this.selectNode.deptId = ''
                this.selectNode.ascnId = ''
                // 没有选中的节点，查询数据为所有根节点下本级及下级的全部数据
                this.formData.deptId = ''
                this.formData.ascnId = ''
            }
        },
        /**
         * 单页面弹窗关闭回调
         * @param {Object} param - 弹窗返回参数
         */
        closeDialog (param) {
            this.handleReload()
            console.log(param)
        },
        /**
         * 多标签页弹窗关闭回调
         * @param {Object} param - 弹窗返回参数
         */
        closeDialogTab (param) {
            // 弹窗关闭后刷新列表数据
            this.handleReload()
        },

        /**
         * 切换树的展开/收起状态
         */
        handleTreeExpand () {
            this.treeExpand = !this.treeExpand
            this.$refs.deptTree.expandAll(this.treeExpand)
        },
        /**
         * 导出Excel功能
         * 支持导出选中用户或当前部门所有用户的信息
         */
        handleExportExcel () {

            if (!this.selectNode.deptId) {
                this.$message.error('请选择部门导出人员信息')
                return
            }

            this.exportLoading = true

            // 构建导出参数
            const exportParams = {
                ...this.formData,
            }

            // 如果有选中的用户，则只导出选中的用户
            if (this.table.selectRow && this.table.selectRow.length > 0) {
                exportParams.selectedUserIds = this.table.selectRow
                this.$message.info(`将导出选中的 ${this.table.selectRow.length} 个用户`)
            } else {
                this.$message.info('将导出当前部门的所有用户')
            }

            // 调用导出API
            app.$svc.sys.person.exportExcel(exportParams).then((response) => {
                // 创建下载链接
                const blob = new Blob([response.data], {
                    type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
                })
                blob.text().then(text => {

                    console.log(' excel text', text)

                })

                const url = window.URL.createObjectURL(blob)
                const link = document.createElement('a')
                link.href = url

                // 从响应头获取文件名，如果没有则使用默认名称
                let contentDisposition = ''
                let fileName = '人员信息.xlsx'
                if (response.headers && response.headers['content-disposition']) {
                    contentDisposition = response.headers['content-disposition']
                }
                if (contentDisposition) {
                    const fileNameMatch = contentDisposition.match(/filename=(.+)/)
                    if (fileNameMatch) {
                        fileName = decodeURIComponent(fileNameMatch[1])
                    }
                }

                link.download = fileName
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link)
                window.URL.revokeObjectURL(url)

                this.$message.success('导出成功')
            }).catch((error) => {
                console.error('导出失败:', error)
                this.$message.error('导出失败，请稍后重试')
            }).finally(() => {
                this.exportLoading = false
            })
        },
        /**
         * 导入Excel功能
         * 批量导入用户信息到选中的部门
         */
        handleImportExcel () {
            if (!this.selectNode.deptId) {
                this.$message.error('请选择部门导入人员信息')
                return
            }

            // 创建文件输入元素
            const input = document.createElement('input')
            input.type = 'file'
            input.accept = '.xlsx,.xls'
            input.style.display = 'none'

            input.onchange = (event) => {
                const file = event.target.files[0]
                if (!file) {
                    return
                }

                // 验证文件类型
                const allowedTypes = [
                    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
                    'application/vnd.ms-excel',
                ]
                if (!allowedTypes.includes(file.type)) {
                    this.$message.error('请选择Excel文件（.xlsx或.xls格式）')
                    return
                }

                // 验证文件大小（限制为10MB）
                if (file.size > 10 * 1024 * 1024) {
                    this.$message.error('文件大小不能超过10MB')
                    return
                }

                this.importLoading = true

                // 创建FormData对象
                const formData = new FormData()
                formData.append('file', file)
                formData.append('deptId', this.selectNode.deptId)
                formData.append('ascnId', this.selectNode.ascnId)

                // 调用导入API
                app.$svc.sys.person.importExcel(formData).then((response) => {
                    if (response.code === 1) {
                        // 导入成功
                        const result = response.data
                        if (result && result.successRows > 0) {
                            this.$message.success(`导入成功！共导入 ${result.successRows} 条数据`)
                        } else {
                            this.$message.success('导入成功')
                        }
                        // 刷新列表
                        this.handleQuery()
                    } else {
                        // 导入失败，展示详细错误信息
                        this.showImportErrors(response)
                    }
                }).catch((error) => {
                    console.error('导入失败:', error)
                    this.$message.error('导入失败，请稍后重试')
                }).finally(() => {
                    this.importLoading = false
                    // 清理input元素
                    document.body.removeChild(input)
                })
            }

            // 添加到DOM并触发点击
            document.body.appendChild(input)
            input.click()
        },
        /**
         * 下载Excel导入模板
         * 提供标准的用户信息导入模板文件
         */
        handleDownloadTemplate () {
            this.templateLoading = true

            try {
                // 直接下载静态文件
                const link = document.createElement('a')
                link.href = '/download_template/用户导入模板.xlsx'
                link.download = '用户导入模板.xlsx'
                link.style.display = 'none'

                // 触发下载
                document.body.appendChild(link)
                link.click()

                // 清理
                document.body.removeChild(link)

                this.$message.success('模板下载成功')
            } catch (error) {
                console.error('模板下载失败:', error)
                this.$message.error('模板下载失败，请稍后重试')
            } finally {
                this.templateLoading = false
            }
        },
        /**
         * 展示导入错误信息
         * 将错误详情以友好的方式展示给用户
         */
        showImportErrors(response) {
            const result = response.data
            
            // 如果没有结构化数据，使用简单错误提示
            if (!result || !result.errors || result.errors.length === 0) {
                this.$message.error(response.message || '导入失败，请检查数据格式')
                return
            }
            
            // 按行号分组错误
            const errorsByRow = {}
            result.errors.forEach(error => {
                const rowNum = error.row || '?'
                if (!errorsByRow[rowNum]) {
                    errorsByRow[rowNum] = []
                }
                errorsByRow[rowNum].push(error)
            })
            
            // 更新错误数据
            this.importErrors = {
                totalRows: result.totalRows || 0,
                errorCount: result.errors.length,
                errorsByRow: errorsByRow
            }
            
            // 显示错误对话框
            this.$refs.importErrorDialog.show()
        },
        /**
         * 关闭导入错误对话框
         */
        closeImportErrorDialog() {
            this.$refs.importErrorDialog.hide()
        },
    },
}
</script>

<!-- 组件样式 -->
<style lang="less" scoped>
/*
 * 机构人员管理页面样式
 * 使用scoped确保样式只作用于当前组件
 * 如需添加自定义样式，请在此处编写
 */
</style>
