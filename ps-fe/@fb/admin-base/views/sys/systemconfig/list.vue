<template>
    <div>
        <fb-page-search>
            <template slot="query">
                <fb-form ref="query-form" mode="query" :label-width="160">
                    <fb-row>
                        <fb-col span="11">
                            <fb-form-item label="配置键">
                                <fb-input v-model="formData.configKey" placeholder="请输入配置键" clearable></fb-input>
                            </fb-form-item>
                        </fb-col>
                        <fb-col span="11">
                            <fb-form-item label="配置描述">
                                <fb-input v-model="formData.description" clearable placeholder="请输入配置描述"></fb-input>
                            </fb-form-item>
                        </fb-col>
                    </fb-row>
                </fb-form>
            </template>

            <template slot="buttons">
                <fb-button ref="buttonAdd" @on-click="handleAdd" icon="add-circle">新增</fb-button>
            </template>

            <template slot="actions">
                <fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
            </template>

            <template slot="table">
                <fb-simple-table ref="table" :service="table.service.search" :param="formData" :pk="table.primaryKey"
                    :columns="table.columns" :multiple="false" :formatters="table.formatters"
                    :scroll="{ x: 1000, y: 372, autoHeight: true }" auto-load>

                    <template v-slot:actions="props">
                        <fb-space>
                            <fb-button @on-click="handleEdit(props.row)" editor size="s">修改</fb-button>
                            <fb-button @on-click="handleDel(props.row)" danger size="s">删除</fb-button>
                        </fb-space>
                    </template>

                    <template v-slot:configKey="props">
                        <fb-link-group>
                            <fb-link :click="() => handleView(props.row)" :label="props.row.configKey"
                                type="primary"></fb-link>
                        </fb-link-group>
                    </template>
                </fb-simple-table>
            </template>
        </fb-page-search>
        <tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"> </tp-dialog>
        <tp-dialog-tab ref="TpDialogTab"></tp-dialog-tab>
    </div>
</template>

<script>

export default {

    // 组件
    components: {
        // 'component-a': ComponentA,
    },
    // 初始化方法
    mounted() {
        // 执行界面初始化方法
    },
    data() {
        return {
            formData: {
                configKey: '',
                description: '',
            },
            // Table列
            table: {
                primaryKey: 'configKey',
                service: {
                    search: this.$svc.sys.systemconfig.list
                },
                columns: [
                    {
                        label: '配置键',
                        name: 'configKey',
                        slot: 'configKey',
                        width: 200
                    },
                    {
                        label: '配置值',
                        name: 'configValue',
                        width: 300,
                        ellipsis: true
                    },
                    {
                        label: '配置描述',
                        name: 'description',
                        ellipsis: true
                    },
                    // {
                    //     label: '创建时间',
                    //     name: 'createTime',
                    //     width: 160
                    // },
                    // {
                    //     label: '更新时间',
                    //     name: 'updateTime',
                    //     width: 160
                    // },
                    {
                        label: '操作',
                        name: 'actions',
                        slot: 'actions',
                        width: 120,
                        freeze: 'right'
                    }
                ],
                formatters: {

                }
            }
        }
    },
    methods: {
        // 查询
        handleQuery() {
            this.$refs.table.doSearch()
        },
        // 新增
        handleAdd() {
            let param = { mode: 'add' };
            let options = { "height": 350 };

            this.$refs.TpDialog.show(import('./add.vue'), param, "新增", options, { action: 'add' });
        },
        // 修改
        handleEdit(row) {
            let param = { "configKey": row.configKey, "passKey": row.passKey, mode: 'edit' };
            let options = { "height": 350 };

            this.$refs.TpDialog.show(import('./add.vue'), param, "修改", options, { action: 'edit' });
        },
        // 查看
        handleView(row) {
            let param = { "configKey": row.configKey, }
            let options = { "height": 350 };
            this.$refs.TpDialog.show(import('./view.vue'), param, "查看", options, { action: 'view' });
        },


        // 删除
        handleDel(row) {
            this.$confirm(`确定要删除配置项 "${row.configKey}" 吗？`, () => {
                this.$svc.sys.config.delete(row.configKey).then(data => {
                    if (data.code == 1) {
                        this.$message.success('删除成功')
                        this.$refs.table.doReload()
                    } else {
                        this.$message.error(data.msg || '删除失败')
                    }
                })
            })
        },
        closeDialog(result) {
            // 根据不同操作类型决定列表刷新方式
            if (!result || !result.success) {
                // 直接关闭或操作失败，不刷新列表
                return
            }
            
            if (result.action === 'add') {
                // 新增成功：重新查询（定位到第一页）
                this.$refs.table.doSearch()
            } else if (result.action === 'edit') {
                // 修改成功：刷新当前页
                this.$refs.table.doReload()
            }
            // 查看操作（action === 'view'）不刷新列表
        }
    }
}

</script>

<style scoped></style>