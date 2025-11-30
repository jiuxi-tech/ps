<template>
    <div>
        <fb-page-search>
            <template slot="query">
                <fb-form ref="query-form" mode="query">
                    <fb-row>
                        <fb-col span="12">
                            <fb-form-item label="用户名">
                                <fb-input v-model="formData.username" placeholder="请输入用户名" clearable></fb-input>
                            </fb-form-item>
                        </fb-col>
                        <fb-col span="12">
                            <fb-form-item label="人员姓名">
                                <fb-input v-model="formData.personName" placeholder="请输入人员姓名"
                                          clearable></fb-input>
                            </fb-form-item>
                        </fb-col>

                        <!--						<fb-col span="6">-->
                        <!--							<fb-form-item label="操作人">-->
                        <!--								<fb-input v-model="formData.changedByName" placeholder="请输入操作人姓名"></fb-input>-->
                        <!--							</fb-form-item>-->
                        <!--						</fb-col>-->
                    </fb-row>
                    <fb-row>
                        <fb-col span="12">
                            <fb-form-item label="修改类型">
                                <fb-select v-model="formData.changeType" placeholder="请选择修改类型" clearable
                                           :data="changeTypeOptions"></fb-select>
                            </fb-form-item>
                        </fb-col>
                        <!--						<fb-col span="12">-->
                        <!--               -->
                        <!--							<fb-form-item label="修改时间">-->
                        <!--								<fb-datepicker -->
                        <!--									v-model="dateRange" -->
                        <!--									type="datetimerange" -->
                        <!--									placeholder="请选择时间范围"-->
                        <!--									format="yyyy-MM-dd HH:mm:ss"-->
                        <!--									@on-change="handleDateChange"-->
                        <!--									clearable>-->
                        <!--								</fb-datepicker>-->
                        <!--							</fb-form-item>-->
                        <!--						</fb-col>-->
                    </fb-row>
                </fb-form>
            </template>

            <template slot="actions">
                <fb-button type="primary" icon="search" @on-click="handleQuery">查询</fb-button>
                <fb-button icon="refresh" @on-click="handleReset">重置</fb-button>
            </template>

            <template slot="table">
                <fb-simple-table
                    ref="table"
                    :service="table.service.list"
                    :param="formData"
                    :pk="table.primaryKey"
                    :columns="table.columns"
                    :multiple="false"
                    auto-load
                    :formatters="formatters"
                    :scroll="{x:1000, y: 330, autoHeight: true}"
                    @on-row-select="handleTableSelect">

                    <template v-slot:username="props">
                        <fb-link-group>
                            <fb-link :click="()=>handleView(props.row)" :label="props.row.username"
                                     type="primary"></fb-link>
                        </fb-link-group>
                    </template>

                    <template v-slot:changeType="props">
                        <fb-tag :color="getChangeTypeColor(props.row.changeType)">
                            {{ getChangeTypeText(props.row.changeType) }}
                        </fb-tag>
                    </template>

                </fb-simple-table>
            </template>
        </fb-page-search>

        <tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
    </div>
</template>

<script>
import dayjs from 'dayjs'

export default {
    name: 'password-history-list',
    mixins: [],

    mounted () {
        // 执行界面初始化方法
    },
    data () {
        return {
            formData: {
                username: '',
                personName: '',
                changeType: null,
                changedByName: '',
                startTime: '',
                endTime: '',
            },

            changeTypeOptions: [
                {
                    label: '用户主动修改',
                    value: 1,
                },
                {
                    label: '管理员重置',
                    value: 2,
                },
                {
                    label: '密码重置修改',
                    value: 3,
                },
                {
                    label: '其他',
                    value: 4,
                },
            ],

            dateRange: [],

            formatters: {
                changeTime (val) {
                    return val ? dayjs(val).format('YYYY-MM-DD HH:mm:ss') : '-'
                },
                changeReason (val) {
                    return val || '-'
                },
                ipAddress (val) {
                    return val || '-'
                },
                changedByName (val) {
                    return val || '-'
                },
            },

            // Table列
            table: {
                // 请求的 url
                service: app.$svc.sys.passwordHistory,
                primaryKey: 'historyId',
                columns: [
                   
                    {
                        name: 'username',
                        label: '用户名',
                        slot: 'username',
                        sortable: false,
                        width: 120,
                    },
                    {
                        name: 'personName',
                        label: '人员姓名',
                        sortable: false,
                        width: 120,
                    },
                    {
                        name: 'changeType',
                        label: '修改类型',
                        slot: 'changeType',
                        sortable: false,        width: 160
                    },
                    {
                        name: 'changeTime',
                        label: '修改时间',
                        sortable: false,
                        width: 160,
                    },
                    {
                        name: 'ipAddress',
                        label: 'IP地址',
                        sortable: false,
                    },
//                    {
//                        name: 'changedByName',
//                        label: '操作人',
//                        sortable: false,
//                        width: 100,
//                    },
                   
//                    {
//                        name: 'changeReason',
//                        label: '修改原因',
//                        sortable: false,
//                        ellipsis: true,
//                        width: 200,
//                    },
                ],
            },
        }
    },

    methods: {
        // 查询方法
        handleQuery () {
            this.$refs.table.doSearch()
        },

        // 重置方法
        handleReset () {
            this.formData = {
                username: '',
                personName: '',
                changeType: null,
                changedByName: '',
                startTime: '',
                endTime: '',
            }
            this.dateRange = []
            this.$refs.table.doSearch()
        },

        // 时间范围变化处理
        handleDateChange (val) {
            if (val && val.length === 2) {
                this.formData.startTime = dayjs(val[0]).format('YYYY-MM-DD HH:mm:ss')
                this.formData.endTime = dayjs(val[1]).format('YYYY-MM-DD HH:mm:ss')
            } else {
                this.formData.startTime = ''
                this.formData.endTime = ''
            }
        },

        // 查看详情
        handleView (row) {
            let param = {'historyId': row.historyId}
            let options = {
                'height': 600,
                'width': 800,
            }
            this.$refs.TpDialog.show(import('./view.vue'), param, '密码修改历史详情', options, {action: 'view'})
        },

        // 获取修改类型文本
        getChangeTypeText (changeType) {
            const typeMap = {
                1: '用户主动修改',
                2: '管理员重置',
                3: '密码重置修改',
                4: '其他',
            }
            return typeMap[changeType] || '未知'
        },

        // 获取修改类型标签颜色
        getChangeTypeColor (changeType) {
            const colorMap = {
                1: 'success',  // 绿色
                2: 'warning',  // 橙色
                3: 'danger',   // 红色
                4: 'default',   // 灰色
            }
            return colorMap[changeType] || 'default'
        },

        closeDialog (result) {
            // 密码历史是只读数据，关闭弹窗不需要刷新列表
        },

        handleTableSelect (row) {
            // 表格行选择事件
        },
    },
}
</script>

<style lang="less" scoped>
</style>
