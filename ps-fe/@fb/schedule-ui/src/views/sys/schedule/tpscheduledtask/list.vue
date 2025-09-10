<template>
    <div>
        <fb-page-search>
            <template slot="query">
                <fb-form ref="query-form" mode="query" :label-width="160">
                  <fb-row>
                    <fb-col span="22">
                      <fb-form-item label="应用名称">
                        <fb-input v-model="formData.appName"></fb-input>
                      </fb-form-item>
                    </fb-col>
                  </fb-row>
                    <fb-row>
                        <fb-col span="11">
                            <fb-form-item label="任务名称">
                                <fb-input v-model="formData.taskName"></fb-input>
                            </fb-form-item>
                        </fb-col>
                        <fb-col span="11">
                            <fb-form-item label="是否启用">
                                <fb-select :data="[{value: 1, label: '是'}
                                                   ,{value: 0, label: '否'}
                                                   ]"
                                           clearable
                                           placeholder="请选择"
                                           v-model="formData.enabled">
                                </fb-select>
                            </fb-form-item>
                        </fb-col>
                    </fb-row>
                    <fb-row>
                        <fb-col span="11">
                            <fb-form-item label="上次执行结果">
                                <fb-select :data="[{value: 1, label: '执行成功'}
                                                  ,{value: 2, label: '执行失败'}
                                                  ,{value: 3, label: '执行超时'}
                                                  ]"
                                           clearable
                                           placeholder="请选择"
                                           v-model="formData.lastResult">
                                </fb-select>
                            </fb-form-item>
                        </fb-col>
                        <fb-col span="11">
                            <fb-form-item label="任务状态">
                                <fb-select :data="[{value: 1, label: '未执行'}
                                                     , {value: 2, label: '执行中'}
                                                     , {value: 3, label: '执行成功'}
                                                     , {value: 4, label: '执行失败'}
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
                <fb-button ref="buttonAdd" @on-click="handleAdd"  v-permission="'TP_SCHEDULED_TASK_ADD'" icon="add-circle">新增</fb-button>
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
                            <fb-button @on-click="handleEdit(props.row)" v-permission="'TP_SCHEDULED_TASK_UPDATE'" editor size="s">修改</fb-button>
                            <fb-button @on-click="handleDel(props.row)" v-permission="'TP_SCHEDULED_TASK_DELETE'" danger size="s">删除</fb-button>
                            <fb-button @on-click="handleExecute(props.row)" v-permission="'TP_SCHEDULED_TASK_EXECUTE'" danger size="s">执行</fb-button>
                        </fb-space>
                    </template>

                    <template v-slot:view="props">
                        <fb-link-group>
                            <fb-link :click="()=>handleView(props.row)" :label="props.row.taskName" type="primary"></fb-link>
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
    import Page from "@fb/schedule-ui/src/util/Page";
    import dayjs from "dayjs";
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
        },
        data() {
            return {
                formData: {
                  appName:'',
                  taskName: '',
                  enabled: '',
                  lastResult: '',
                  status: '',
                },
                // Table列
                table: {
                    // 请求的 url
                    service: app.$svc.sys.schedule.tpscheduledtask,
                    primaryKey: "taskId",
                    columns: [
                        {
                          name: 'appName',
                          label: '应用名称',
                          sortable: false,
                          width: 150,
                         },
                        {
                            name: 'taskName',
                            label: '任务名称',
                            sortable: false,
                            width: 150,
                            slot: 'view',
                        },
                        {
                            name: 'className',
                            label: '目标类名',
                            sortable: false,
                            width: 150,
                        },
                        {
                            name: 'methodName',
                            label: '目标方法名',
                            sortable: false,
                            width: 150,
                        },
                        {
                            name: 'cronExpression',
                            label: 'cron表达式',
                            sortable: false,
                            width: 150,
                        },
                        {
                            name: 'remark',
                            label: '任务描述',
                            sortable: false,
                            width: 150,
                        },
                        {
                            name: 'lastTime',
                            label: '上次执行时间',
                            sortable: false,
                            width: 150,
                        },
                        {
                            name: 'lastResult',
                            label: '上次执行结果',
                            sortable: false,
                            width: 100,
                        },
                        {
                            name: 'nextTime',
                            label: '下次执行时间',
                            sortable: false,
                            width: 150,
                        },
                        {
                            name: 'status',
                            label: '任务状态',
                            sortable: false,
                            width: 150,
                        },
                        {
                            name: 'enabled',
                            label: '是否启用',
                            sortable: false,
                            width: 100,
                        },
                        {
                            name: '',
                            label: '操作',
                            sortable: false,
                            slot: 'actions',
                            width: 200,
                            // 冻结操作列
                            freeze: 'right'
                        }
                    ],
                    formatters: {
                        lastTime(val, row) {

                            if (val != '') {
                                return dayjs(val).format('YYYY-MM-DD HH:mm:ss')
                            } else {
                                return val
                            }
                        },
                        nextTime(val, row) {

                            if (val != '') {
                                return dayjs(val).format('YYYY-MM-DD HH:mm:ss')
                            } else {
                                return val
                            }

                        },
                        enabled(val, row) {
                            if (val == 1) {
                                return '是'
                            } else if (val == 0) {
                                return '否'
                            } else {
                                return val;
                            }
                        },
                        lastResult(val, row){
                            if (val == 1) {
                                return '执行成功'
                            } else if (val == 2) {
                                return '执行失败'
                            } else if (val == 3) {
                                return '执行超时'
                            } else {
                                return "";
                            }
                        },
                        status(val, row){
                            if (val == 1) {
                                return '未执行'
                            } else if (val == 2) {
                                return '执行中'
                            } else if (val == 3) {
                                return '执行成功'
                            } else if (val == 4) {
                                return '执行失败'
                            } else {
                                return "";
                            }
                        }
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
            // 新增方法
            handleAdd() {
                let param = {}
                let that = this;

                let options = {
                    tabChangeConfirm: false,
                    callBack: function (result) {
                        that.$refs.table.doReload()
                    }
                }
                this.$refs.TpDialog.show(import('@fb/schedule-ui/src/views/sys/schedule/tpscheduledtask/add.vue'), param, "新增", options);
            },

            // 修改方法
            handleEdit(row) {
                let param = {"id": row.taskId};
                let that = this;

                let options = {
                    tabChangeConfirm: false,
                    callBack: function (result) {
                        that.$refs.table.doReload()
                    }
                }
                this.$refs.TpDialog.show(import('@fb/schedule-ui/src/views/sys/schedule/tpscheduledtask/add.vue'), param, "修改", options);
            },
            // 删除方法
            handleDel(row) {
                this.$confirm('确定要删除吗？', () => {
                    this.table.service.delete({"taskIds": row.taskId}).then((result) => {
                        if (result.code == 1) {
                            this.$message.success('删除成功');
                            this.$refs.table.doReload()
                        } else {
                            // 服务器返回失败
                            this.$message.error('删除失败: ' + result.message)
                        }
                    });
                })
            },
            // 手工执行
            handleExecute(row) {
                this.$confirm('确定要执行一次定时任务吗？', () => {
                    this.table.service.execute({"taskId": row.taskId}).then((result) => {
                        if (result.code == 1) {
                            this.$message.success('操作成功');
                        } else {
                            // 服务器返回失败
                            this.$message.error('操作失败: ' + result.message)
                        }
                    });
                })

            },
            // 查看方法
            handleView(row) {
                let param = {"id": row.taskId}

                let options = {
                    tabChangeConfirm: false,
                    callBack: function (result) {
                        console.log("=============回调查看" + result)
                    }
                }
                this.$refs.TpDialog.show(import('@fb/schedule-ui/src/views/sys/schedule/tpscheduledtask/view.vue'), param, "查看", options);

            },
        }
    }
</script>

<style lang="less" scoped>
</style>
