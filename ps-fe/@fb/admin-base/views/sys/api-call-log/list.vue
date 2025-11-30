<template>
    <div>
        <fb-page-search>
            <template slot="query">
                <fb-form ref="query-form" mode="query">
                    <fb-row>
                        <fb-col span="8">
                            <fb-form-item label="应用名称">
                                <fb-input v-model="formData.appName" clearable></fb-input>
                            </fb-form-item>
                        </fb-col>
                        <fb-col span="8">
                            <fb-form-item label="API路径">
                                <fb-input v-model="formData.apiPath" clearable></fb-input>
                            </fb-form-item>
                        </fb-col>
                        <fb-col span="8">
                            <fb-form-item label="HTTP方法">
                                <fb-select v-model="formData.httpMethod" clearable
                                           :data="httpMethodOptions"></fb-select>
                            </fb-form-item>
                        </fb-col>
                    </fb-row>
                    <fb-row>
                        <fb-col span="8">
                            <fb-form-item label="响应状态">
                                <fb-select v-model="formData.responseStatus" clearable
                                           :data="responseStatusOptions"></fb-select>
                            </fb-form-item>
                        </fb-col>
                        <fb-col span="8">
                            <fb-form-item label="请求IP">
                                <fb-input v-model="formData.requestIp" clearable></fb-input>
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
                    :service="table.service.list"
                    :param="formData"
                    :pk="table.primaryKey"
                    :columns="table.columns"
                    :multiple="false"
                    auto-load
                    :formatters="formatters"
                    :scroll="{x:1200, y: 364, autoHeight: true}"
                    @on-row-select="handleTableSelect">

                    <template v-slot:view="props">
                        <fb-link-group>
                            <fb-link :click="()=>handleView(props.row)" :label="props.row.appName"
                                     type="primary"></fb-link>
                        </fb-link-group>
                    </template>

                    <template v-slot:responseStatus="props">
						<span :class="getResponseStatusClass(props.row.responseStatus)">
							{{ props.row.responseStatus }}
						</span>
                    </template>

                    <template v-slot:responseTime="props">
                        <span v-if="!props.row.responseTime">-</span>
                        <span v-else :class="getResponseTimeClass(props.row.responseTime)">
							{{ props.row.responseTime }} ms
						</span>
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
    name: 'list',
    mixins: [],

    // 初始化方法
    mounted () {
        // 执行界面初始化方法
    },
    data () {
        return {
            formData: {
                appName: '',
                apiPath: '',
                httpMethod: '',
                responseStatus: null,
                requestIp: '',
            },

            httpMethodOptions: [
                {
                    label: 'GET',
                    value: 'GET',
                },
                {
                    label: 'POST',
                    value: 'POST',
                },
                {
                    label: 'PUT',
                    value: 'PUT',
                },
                {
                    label: 'DELETE',
                    value: 'DELETE',
                },
            ],

            responseStatusOptions: [
                {
                    label: '1 成功',
                    value: 1,
                },
                {
                    label: '-1 异常',
                    value: -1,
                },
                {
                    label: '200 成功',
                    value: 200,
                },
                {
                    label: '400 请求错误',
                    value: 400,
                },
                {
                    label: '401 未授权',
                    value: 401,
                },
                {
                    label: '403 禁止访问',
                    value: 403,
                },
                {
                    label: '404 未找到',
                    value: 404,
                },
                {
                    label: '500 服务器错误',
                    value: 500,
                },
            ],

            formatters: {
                callTime (val) {
                    return val ? dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss') : val
                },
            },

            // Table列
            table: {
                // 请求的 url
                service: app.$svc.sys.apiCallLog,
                primaryKey: 'logId',
                columns: [
                    {
                        name: 'appName',
                        label: '应用名称',
                        slot: 'view',
                        sortable: false,
                        width: 150,
                    }, {
                        name: 'apiPath',
                        label: 'API路径',
                        sortable: false,
                        width: 200,
                    }, {
                        name: 'httpMethod',
                        label: 'HTTP方法',
                        sortable: false,
                        width: 80,
                    }, {
                        name: 'requestIp',
                        label: '请求IP',
                        sortable: false,
                        width: 100,
                    }, {
                        name: 'responseStatus',
                        label: '响应状态',
                        slot: 'responseStatus',
                        sortable: false,
                        width: 80,
                    }, {
                        name: 'responseTime',
                        label: '响应时间',
                        slot: 'responseTime',
                        sortable: false,
                        width: 100,
                    }, {
                        name: 'callTime',
                        label: '调用时间',
                        sortable: false,
                        width: 160,
                    },
                ],
            },
        }
    },

    // 方法
    methods: {
        // 列表方法
        handleQuery () {
            this.$refs.table.doSearch()
        },

        // 查看方法
        handleView (row) {
            let param = {
                'id': row.logId,
                'passKey': row.passKey,
            }
            let options = {'height': 600}
            // 打开查看界面弹出窗，传递 meta 参数标识为查看操作
            this.$refs.TpDialog.show(import('./view.vue'), param, '查看API调用日志', options, {action: 'view'})
        },
        // 下拉回调
        onSelectChange (e) {
            console.log('下拉选择：' + e)
        },
        closeDialog (result) {
            // 日志为只读数据，无需刷新列表
            return
        },
        handleTableSelect (e) {
            console.log('表格选择：' + e)
        },

        // 获取响应状态的样式类
        getResponseStatusClass (val) {
            if (val === 1) {
                return 'text-success'
            }
            if (val === -1) {
                return 'text-error'
            }
            if (val >= 200 && val < 300) {
                return 'text-success'
            } else if (val >= 400 && val < 500) {
                return 'text-warning'
            } else if (val >= 500) {
                return 'text-error'
            }
            return ''
        },

        // 获取响应时间的样式类
        getResponseTimeClass (val) {
            if (val < 500) {
                return ''
            } else if (val < 2000) {
                return 'text-warning'
            } else {
                return 'text-error'
            }
        },
    },
}
</script>

<style lang="less" scoped>
.text-success {
    color:       #52c41a;
    font-weight: bold;
}

.text-warning {
    color:       #faad14;
    font-weight: bold;
}

.text-error {
    color:       #ff4d4f;
    font-weight: bold;
}
</style>
