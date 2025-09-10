<template>
    <div>
        <fb-page-search>
            <template slot="query">
                <fb-form ref="query-form" mode="query" :label-width="160">
                    <fb-row>
                        <fb-col span="8">
                            <fb-form-item label="系统名称">
                                <fb-input v-model="formData.applicationName"></fb-input>
                            </fb-form-item>

                        </fb-col>
                        <fb-col span="8">
                            <fb-form-item label="系统描述">
                                <fb-input v-model="formData.systemDesc"></fb-input>
                            </fb-form-item>
                        </fb-col>
                        <fb-col span="7">
                            <fb-form-item label="状态">
                                <fb-select :data="[{value: 1, label: '在线'}
                                                   ,{value: 0, label: '离线'}
                                                   ]"
                                           clearable
                                           placeholder="请选择"
                                           v-model="formData.status">
                                </fb-select>
                            </fb-form-item>
                        </fb-col>
                    </fb-row>
                </fb-form>
            </template>
            <template slot="buttons">
                <fb-button ref="buttonAdd" @on-click="handleEditConfig"  icon="editor-square">修改报警配置</fb-button>
            </template>

            <template slot="actions">
                <fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
            </template>

            <template slot="table">
                <fb-simple-table
                        ref="table"
                        :service="table.service.search"
                        :param="formData"
                        :pk="table.primaryKey"
                        :columns="table.columns"
                        :formatters="table.formatters"
                        :multiple="false"
                        :scroll="{x:1100, y: 372, autoHeight: true}"
                        auto-load>

                    <template v-slot:actions="props">
                        <fb-space>
                            <fb-button @on-click="handleEdit(props.row)"  editor size="s">修改</fb-button>
                            <fb-button @on-click="handleDel(props.row)"  danger size="s">删除</fb-button>
                        </fb-space>
                    </template>

                    <template v-slot:alarm="props">
                        <span style="color: red;">
                            {{props.row.alarmMsg}}
                        </span>
                    </template>
                    <template v-slot:view="props">
                        <fb-link-group>
                            <fb-link :click="()=>handleView(props.row)"  :label="props.row.applicationName" type="primary"></fb-link>
                        </fb-link-group>
                    </template>
                    <template v-slot:info="props">
                        <fb-link-group>
                            <fb-link :click="()=>handleViewInfo(props.row)"  :label="props.row.systemDesc" type="primary"></fb-link>
                        </fb-link-group>
                    </template>
                </fb-simple-table>
            </template>
        </fb-page-search>
        <tp-dialog ref="TpDialog"></tp-dialog>
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
                    applicationName: '',
                    systemDesc: '',
                    status: null,
                },
                // Table列
                table: {
                    // 请求的 url
                    service: app.$svc.sys.monitor,

                    primaryKey: "clientId",
                    columns: [
                        {
                            name: 'applicationName',
                            label: '系统名称',
                            sortable: false,
                            width: 150,
                            slot: 'view',
                        },
                        {
                            name: 'systemDesc',
                            label: '系统描述',
                            sortable: false,
                            width: 150,
                            slot: 'info',
                        },
                        {
                            name: 'status',
                            label: '状态',
                            sortable: false,
                            width: 80,
                        },
                        {
                            name: 'alarmMsg',
                            label: '报警信息',
                            sortable: false,
                            width: 260,
                            slot: 'alarm',
                        },
                        {
                            name: 'ip',
                            label: 'ip链路',
                            sortable: false,
                            width: 150,
                        },
                        {
                            name: 'systemUrl',
                            label: '系统访问地址',
                            sortable: false,
                            width: 150,
                        },
                        {
                            name: 'remark',
                            label: '备注',
                            sortable: false,
                            width: 150,
                        },
                        {
                            name: '',
                            label: '操作',
                            sortable: false,
                            slot: 'actions',
                            width: 124,
                        },
                    ],
                    formatters: {
                        enabled(val) {
                            return val === "1" ? '启用' : '停用'
                        },
                        status(val) {
                            return val === 1 ? '在线' : '离线'
                        },
                    },
                },
            }
        },

        // 方法
        methods: {
            // 列表方法
            handleQuery() {
                this.$refs.table.doSearch()
            },
            // 修改配置方法
            handleEditConfig() {
                let param = {};
                let options = {
                    tabChangeConfirm: false,
                    callBack: (result) => {
                        this.$refs.table.doReload();
                    }
                }
                this.$refs.TpDialog.show(import('@fb/monitor-ui/src/views/sys/monitor/config.vue'), param, "修改", options);
            },
            // 修改方法
            handleEdit(row) {
                let param = {"id": row.clientId, "passKey": row.passKey};
                let options = {
                    tabChangeConfirm: false,
                    callBack: (result) => {
                        this.$refs.table.doReload();
                    }
                }
                this.$refs.TpDialog.show(import('@fb/monitor-ui/src/views/sys/monitor/add.vue'), param, "修改", options);
            },
            // 删除方法
            handleDel(row) {
                this.$confirm('确定要删除吗？', () => {
                    this.table.service.delete({"clientId": row.clientId, "passKey": row.passKey}).then((result) => {
                        if (result && result.code == 1) {
                            this.$message.success('删除成功');
                            this.$refs.table.doReload();
                        } else {
                            // 服务器返回失败
                            this.$message.error('删除失败: ' + result.message)
                        }
                    });
                })
            },
            // 查看方法
            handleView(row) {
                let param = {"id": row.clientId, "passKey": row.passKey}
                let options = {
                    tabChangeConfirm: false,
                    callBack: (result) => {
                        console.log("=============回调查看" + result)
                    }
                }
                this.$refs.TpDialog.show(import('@fb/monitor-ui/src/views/sys/monitor/view.vue'), param, "查看", options);
            },
            // 查看服务器详情方法
            handleViewInfo(row) {
                let param = {"id": row.clientId, "passKey": row.passKey}
                let options = {
                    tabChangeConfirm: false,
                    callBack: (result) => {
                        console.log("=============回调查看" + result)
                    },
                    width: '960px'
                }
                this.$refs.TpDialog.show(import('@fb/monitor-ui/src/views/sys/monitor/info.vue'), param, "查看", options);
            },
        }
    }
</script>

<style lang="less" scoped>
</style>
