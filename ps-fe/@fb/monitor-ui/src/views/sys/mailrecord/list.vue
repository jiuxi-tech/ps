<template>
    <div>
        <fb-page-search>
            <template slot="query">
                <fb-form ref="query-form" mode="query" :label-width="160">
                    <fb-row>
                          <fb-col span="8">
                            <fb-form-item label="收件人姓名">
                                <fb-input v-model="formData.personName"></fb-input>
                            </fb-form-item>
                        </fb-col>
                          <fb-col span="7">
                            <fb-form-item label="收件人邮箱">
                                <fb-input v-model="formData.email"></fb-input>
                            </fb-form-item>
                        </fb-col>
                        <fb-col span="7">
                            <fb-form-item label="发送状态">
                                <fb-select :data="[{value: 1, label: '成功'}
                                                   ,{value: 0, label: '失败'}
                                                   ]"
                                           clearable
                                           placeholder="请选择"
                                           v-model="formData.status">
                                </fb-select>
                            </fb-form-item>
                        </fb-col>
                    </fb-row>
                    <fb-row>
                        <fb-col span="8">
                            <fb-form-item label="发送内容">
                                <fb-input v-model="formData.message"></fb-input>
                            </fb-form-item>
                        </fb-col>
                        <fb-col span="14">
                            <fb-form-item :content-style="{ }" label="发送时间"
                                          prop="formData.sendTimeStart" style="display: inline-block; width: 55%">
                                <tp-datepicker  :maxDate="maxDateForStart"
                                                :clearable="false"
                                                format="YYYY-MM-DD HH:mm:ss"
                                                v-model="formData.sendTimeStart"
                                                value-format="YYYYMMDDHHmmss"></tp-datepicker>
                            </fb-form-item>
                            <fb-form-item  :content-style="{marginLeft: '30px'}" :label-style="{width:'30px', textAlign: 'center', paddingLeft: '5px'}"
                                           label="-"
                                           prop="formData.sendTimeEnd"
                                           style="display: inline-block; width: 45%">
                                <tp-datepicker  :minDate="minDateForEnd"
                                                :clearable="false"
                                                format="YYYY-MM-DD HH:mm:ss"
                                                v-model="formData.sendTimeEnd"
                                                value-format="YYYYMMDDHHmmss"></tp-datepicker>
                            </fb-form-item>
                        </fb-col>
                    </fb-row>
                </fb-form>
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
                            <fb-button @on-click="handleDel(props.row)" danger size="s">删除</fb-button>
                        </fb-space>
                    </template>

                    <template v-slot:view="props">
                        <fb-link-group>
                            <fb-link :click="()=>handleView(props.row)" :label="props.row.personName" type="primary"></fb-link>
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
    import dayjs from "dayjs";
    export default {

        // 组件
        components: {
            // 'component-a': ComponentA,
        },
        // 初始化方法
        mounted() {
            // 执行界面初始化方法
        },
        computed: {
            maxDateForStart() {
                if (!this.formData.sendTimeEnd) return null;
                return new Date(this.formData.sendTimeEnd);
            },
            minDateForEnd() {
                if (!this.formData.sendTimeStart) return null;
                return new Date(this.formData.sendTimeStart);
            }
        },

        data() {
            return {
                formData: {
                    personName: '',
                    email: '',
                    message: '',
                    sendTimeStart: null,
                    sendTimeEnd: null,
                    status: null
                },
                // Table列
                table: {
                    // 请求的 url
                    service: app.$svc.sys.mailrecord,

                    primaryKey: "recordId",
                    columns: [
                        {
                            name: 'personName',
                            label: '收件人姓名',
                            sortable: false,
                            width: 100,
                            slot: 'view',
                        },
                        {
                            name: 'email',
                            label: '电子邮箱',
                            sortable: false,
                            width: 100,
                        },
                        {
                            name: 'sendTime',
                            label: '发送时间',
                            sortable: false,
                            width: 100,
                        },
                        {
                            name: 'message',
                            label: '发送内容',
                            sortable: false,
                            width: 200,
                        },
                        {
                            name: 'status',
                            label: '发送状态',
                            sortable: false,
                            align: 'center',
                            width: 60,
                        },
                        {
                            name: '',
                            label: '操作',
                            sortable: false,
                            slot: 'actions',
                            width: 60,
                        },
                    ],
                    formatters: {
                        status(val) {
                            return val === 1 ? '成功' : '失败'
                        },
                        sendTime(val) {
                            return val ? dayjs(val).format('YYYY-MM-DD HH:mm:ss') : val;
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

            // 删除方法
            handleDel(row) {
                this.$confirm('确定要删除吗？', () => {
                    this.table.service.delete({"recordId": row.recordId, "passKey": row.passKey}).then((result) => {
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
                let param = {"id": row.recordId, "passKey": row.passKey}

                let options = {
                    tabChangeConfirm: false,
                    callBack: (result) => {
                        console.log("=============回调查看" + result)
                    }
                }
                this.$refs.TpDialog.show(import('@fb/monitor-ui/src/views/sys/mailrecord/view.vue'), param, "查看", options);
            },
        }
    }
</script>

<style lang="less" scoped>
</style>
