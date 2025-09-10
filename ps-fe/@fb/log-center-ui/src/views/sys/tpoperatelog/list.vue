<template>
    <div>
        <fb-page-search>
            <template slot="query">
                <fb-form :label-width="160" mode="query" ref="query-form">
<!--                    <fb-row>-->
<!--                        <fb-col offset="1" span="10">-->
<!--                            <fb-form-item label="系统名称">-->
<!--                                <fb-input v-model="formData.appName"></fb-input>-->
<!--                            </fb-form-item>-->
<!--                        </fb-col>-->
<!--                    </fb-row>-->
                    <fb-row>
                        <fb-col offset="1" span="10">
                            <fb-form-item label="模块">
                                <fb-tree-select :data="$logconstant.moduleCodeData"
                                                clearable
                                                placeholder="请选择"
                                                v-model="formData.moduleCode">
                                </fb-tree-select>
                            </fb-form-item>
                        </fb-col>
                        <fb-col offset="1" span="10">
                            <fb-form-item :content-style="{ }" label="操作时间"
                                          prop="formData.operterTimeStart" style="display: inline-block; width: 56%">
                                <tp-datepicker  :maxDate="maxDateForStart" clearable
                                                format="YYYY-MM-DD HH:mm:ss" v-model="formData.operterTimeStart" value-format="YYYYMMDDHHmmss"></tp-datepicker>
                            </fb-form-item>
                            <fb-form-item  :content-style="{marginLeft: '30px'}" :label-style="{width:'30px', textAlign: 'center', paddingLeft: '5px'}"
                                           label="-"
                                           prop="formData.operterTimeEnd"
                                           style="display: inline-block; width: 44%">
                                <tp-datepicker  :minDate="minDateForEnd" clearable
                                                format="YYYY-MM-DD HH:mm:ss" v-model="formData.operterTimeEnd" value-format="YYYYMMDDHHmmss"></tp-datepicker>
                            </fb-form-item>
                        </fb-col>

                        <fb-col span="4"></fb-col>
                    </fb-row>
<!--                    <fb-row>-->
<!--                        <fb-col offset="1" span="10">-->
<!--                            <fb-form-item label="行政区划">-->
<!--                                <fb-tree-select :header-format="cityTreeHeaderFormat"-->
<!--                                                :param="{'sync': 1, 'expand': false, 'cityId': '-1'}"-->
<!--                                                :placeholder="'请选择'"-->
<!--                                                :reader="{value:'value', label: 'text'}"-->
<!--                                                :service="$svc.sys.city.tree"-->
<!--                                                clearable-->
<!--                                                v-model="formData.cityCode">-->
<!--                                </fb-tree-select>-->
<!--                            </fb-form-item>-->
<!--                        </fb-col>-->
<!--                        <fb-col offset="1" span="10">-->
<!--                            <fb-form-item label="用户名">-->
<!--                                <fb-input v-model="formData.username"></fb-input>-->
<!--                            </fb-form-item>-->
<!--                        </fb-col>-->
<!--                        <fb-col span="4"></fb-col>-->
<!--                    </fb-row>-->
                    <fb-row>
                        <fb-col offset="1" span="10">
                            <fb-form-item label="操作类型">
                                <fb-tree-select :data="$logconstant.operterTypeData"
                                                clearable
                                                placeholder="请选择"
                                                v-model="formData.operterType">
                                </fb-tree-select>
                            </fb-form-item>
                        </fb-col>
                        <fb-col offset="1" span="10">
                            <fb-form-item label="操作结果">
                                <fb-select v-model="formData.operterResult"
                                           :data="[
                                                    {label: '成功', value:'1'},
                                                    {label: '失败', value:'0'}
                                           ]"
                                           placeholder="请选择"
                                           :clearable="true"
                                />
                            </fb-form-item>
                        </fb-col>
                        <fb-col span="4"></fb-col>
                    </fb-row>
                    <fb-row>
                        <fb-col offset="1" span="10">
                            <fb-form-item label="操作记录id">
                                <fb-input v-model="formData.operterRid"></fb-input>
                            </fb-form-item>
                        </fb-col>
                        <fb-col offset="1" span="10">
                            <fb-form-item label="操作信息">
                                <fb-input v-model="formData.operterMsg"></fb-input>
                            </fb-form-item>
                        </fb-col>
                    </fb-row>
<!--                    <fb-row>-->
<!--                        <fb-col offset="1" span="10">-->
<!--                            <fb-form-item label="操作人id">-->
<!--                                <fb-input v-model="formData.personId"></fb-input>-->
<!--                            </fb-form-item>-->
<!--                        </fb-col>-->
<!--                        <fb-col offset="1" span="10">-->
<!--                            <fb-form-item label="记录id">-->
<!--                                <fb-input v-model="formData.operterRid"></fb-input>-->
<!--                            </fb-form-item>-->
<!--                        </fb-col>-->
<!--                        <fb-col span="4"></fb-col>-->
<!--                    </fb-row>-->
<!--                    <fb-row>-->
<!--                        <fb-col offset="1" span="10">-->
<!--                            <fb-form-item label="操作信息">-->
<!--                                <fb-input v-model="formData.extend01"></fb-input>-->
<!--                            </fb-form-item>-->
<!--                        </fb-col>-->
<!--                        <fb-col offset="1" span="10">-->
<!--                            <fb-form-item label="扩展信息02">-->
<!--                                <fb-input v-model="formData.extend02"></fb-input>-->
<!--                            </fb-form-item>-->
<!--                        </fb-col>-->
<!--                        <fb-col span="4"></fb-col>-->
<!--                    </fb-row>-->
                </fb-form>
            </template>

            <template slot="buttons">
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
                        :formatters="table.formatters"
                        :columns="table.columns"
                        :multiple="false"
                        :scroll="{x:1100, y: 372, autoHeight: true}"
                        auto-load>
                    
                    <template v-slot:view="props">
                        <fb-link-group>
                            <fb-link :click="()=>handleView(props.row)" :label="formatOperterTime(props.row.operterTime)" type="primary"></fb-link>
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
    import Page from '@fb/log-center-ui/src/util/Page'
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
            let that = this
            // this.$online.countonline((data)=> {
            //     that.onlineNum = data
            // });
        },

        computed: {
            maxDateForStart() {
                if (!this.formData.operterTimeEnd) return null;
                return new Date(this.formData.operterTimeEnd);
            },
            minDateForEnd() {
                if (!this.formData.operterTimeStart) return null;
                return new Date(this.formData.operterTimeStart);
            }
        },

        data() {
            return {
                // onlineNum: 0,

                formData: {
                    operterTimeStart: this.formatDate(new Date(new Date().valueOf() - 24 * 60 * 60 * 1000), "YYYY-MM-DD HH:mm:ss"),
                    operterTimeEnd: this.formatDate(new Date(), "YYYY-MM-DD HH:mm:ss"),
                    category: "",
                    appName: "",
                    cityCode: "",
                    moduleCode: "",
                    operterType: "",
                    username: "",
                    operterResult: "",
                },

                // Table列
                table: {

                    formatters: {
                        operterType: (val, row) => {

                            this.$logconstant.operterTypeData.forEach(function (data) {
                                if (val == data.value){
                                    val = data.label;
                                    return val;
                                }
                            })

                            return val;

                        },
                        source: (val, row) => {

                            this.$logconstant.sourceData.forEach(function (data) {
                                if (val == data.value){
                                    val = data.label;
                                    return val;
                                }
                            })
                            return val;
                        },
                        category: (val, row)  => {
                            this.$logconstant.categoryData.forEach(function (data) {
                                if (val == data.value){
                                    val = data.label;
                                    return val;
                                }
                            })
                            return val;
                        },
                        moduleCode: (val, row)  => {
                            this.$logconstant.moduleCodeData.forEach(function (data) {
                                if (val == data.value){
                                    val = data.label;
                                    return val;
                                }
                            })
                            return val;
                        },
                        appName: (val, row)  => {
                            this.$logconstant.appNameData.forEach(function (data) {
                                if (val == data.value){
                                    val = data.label;
                                    return val;
                                }
                            })
                            return val;
                        },
                        operterResult: (val, row)  => {
                            if ('1' === val) {
                                return "成功"
                            }
                            if ('0' === val) {
                                return "失败"
                            }
                            return val;
                        },
                    },

                    // 请求的 url
                    service: app.$svc.sys.tpoperatelog,

                    primaryKey: "logId",

                    columns: [

                        {
                            name: 'operterTime',
                            label: '操作时间',
                            sortable: false,
                            width: 200,
                            slot: 'view',
                        },
                        {
                            name: 'appName',
                            label: '系统名称',
                            sortable: false,
                            width: 150,

                        },
                        {
                            name: 'moduleCode',
                            label: '模块',
                            sortable: false,
                            width: 100,
                        },
                        {
                            name: 'operterType',
                            label: '操作类型',
                            sortable: false,
                            width: 100,
                        },
                        // {
                        //     name: 'personId',
                        //     label: '操作人员id',
                        //     sortable: false,
                        //     width: 150,
                        // },
                        {
                            name: 'operterRid',
                            label: '操作记录ID',
                            sortable: false,
                            width: 150,
                        },
                        {
                            name: 'operterResult',
                            label: '操作结果',
                            sortable: false,
                            width: 100,
                        },
                        {
                            name: 'operterMsg',
                            label: '操作信息',
                            sortable: false,
                            width: 100,
                        },
                        {
                            name: 'username',
                            label: '用户名',
                            sortable: false,
                            width: 150,
                        },
                        // {
                        //     name: 'extend01',
                        //     label: '操作信息',
                        //     sortable: false,
                        //     width: 150,
                        // },
                        // {
                        //     name: 'extend02',
                        //     label: '扩展信息02',
                        //     sortable: false,
                        //     width: 150,
                        // },
                        // {
                        //     name: 'extend03',
                        //     label: '扩展信息03',
                        //     sortable: false,
                        //     width: 150,
                        // },
                        // {
                        //     name: 'ascnId',
                        //     label: '单位ID',
                        //     sortable: false,
                        //     width: 150,
                        // },
                        // {
                        //     name: 'source',
                        //     label: '日志来源',
                        //     sortable: false,
                        //     width: 100,
                        // },
                        //
                        // {
                        //     name: 'category',
                        //     label: '用户类别',
                        //     sortable: false,
                        //     width: 150,
                        // },
                        // {
                        //     name: 'cityCode',
                        //     label: '行政区划',
                        //     sortable: false,
                        //     width: 150,
                        // },
                        // {
                        //     name: 'operterIp',
                        //     label: '操作人IP',
                        //     sortable: false,
                        //     width: 150,
                        // },
                    ],

                },
            }
        },

        // 方法
        methods: {
            // 列表方法
            handleQuery() {
                this.$refs.table.doSearch()
            },
            formatOperterTime(val){
                if (val) {
                    return dayjs(val).format('YYYY-MM-DD HH:mm:ss')
                } else {
                    return val
                }
            },
            // 查看方法
            handleView(row) {
                let param = row
                let options = {
                    tabChangeConfirm: false,
                    callBack: function (result) {
                        console.log("=============回调查看" + result)
                    }
                }

                this.$refs.TpDialog.show(import('@fb/log-center-ui/src/views/sys/tpoperatelog/view.vue'), param, "查看", options);
            },
        }
    }
</script>

<style lang="less" scoped>
</style>
