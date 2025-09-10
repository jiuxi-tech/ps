<template>
    <div class="tp-dialog">
        <div class="tp-dialog-top">
            <fb-container position="relative" overflow="auto" v-if="inited">
                <div>
                    <fb-row flex>
                        <fb-col span="24">
                            <fb-container>
                                <fb-card header="服务器信息">
                                    <fb-property labelAlign="left" size="m" label-width="200px" mode="form" :label-style="{'font-size': '14px'}">
                                        <fb-row>
                                            <fb-col span="12">
                                                <fb-property-item label="最后更新时间" :content-style="getOffLineStyle" :label-style="getOffLineStyle">
                                                    {{ formatLastUpdateTime(server.lastUpdateTime) }}
                                                </fb-property-item>
                                            </fb-col>
                                        </fb-row>
                                        <fb-row>
                                            <fb-col span="12">
                                                <fb-property-item label="系统名称">
                                                    {{ server.applicationName }}
                                                </fb-property-item>
                                            </fb-col>
                                            <fb-col span="12">
                                                <fb-property-item label="系统id">
                                                    {{ server.applicationId }}
                                                </fb-property-item>
                                            </fb-col>
                                        </fb-row>
                                        <fb-row>
                                            <fb-col span="12">
                                                <fb-property-item label="服务器名称">
                                                    {{ server.sys.computerName }}
                                                </fb-property-item>
                                            </fb-col>
                                            <fb-col span="12">
                                                <fb-property-item label="操作系统">
                                                    {{ server.sys.osName }}
                                                </fb-property-item>
                                            </fb-col>
                                        </fb-row>
                                        <fb-row>
                                            <fb-col span="12">
                                                <fb-property-item label="服务器IP">
                                                    {{ server.sys.computerIp }}
                                                </fb-property-item>
                                            </fb-col>
                                            <fb-col span="12">
                                                <fb-property-item label="系统架构">
                                                    {{ server.sys.osArch }}
                                                </fb-property-item>
                                            </fb-col>
                                        </fb-row>
                                    </fb-property>
                                </fb-card>
                            </fb-container>
                        </fb-col>
                    </fb-row>
                    <fb-row flex>
                        <fb-col span="24">
                            <fb-container mt="10px">
                                <fb-card header="cpu">
                                    <fb-property labelAlign="left" size="m" label-width="200px" mode="form" :label-style="{'font-size': '14px'}">
                                        <fb-row>
                                            <fb-col span="12">
                                                <fb-property-item label="核心数">
                                                    {{ server.cpu.cpuNum }}
                                                </fb-property-item>
                                            </fb-col>
                                            <fb-col span="12">
                                                <fb-property-item label="用户使用率" :contentStyle="getFontColor(server.cpu.used)">
                                                    {{ server.cpu.used }}%
                                                </fb-property-item>
                                            </fb-col>
                                        </fb-row>
                                        <fb-row>
                                            <fb-col span="12">
                                                <fb-property-item label="系统使用率" :contentStyle="getFontColor(server.cpu.sys)">
                                                    {{ server.cpu.sys }}%
                                                </fb-property-item>
                                            </fb-col>
                                            <fb-col span="12">
                                                <fb-property-item label="当前空闲率" >
                                                    {{ server.cpu.free }}%
                                                </fb-property-item>
                                            </fb-col>
                                        </fb-row>
                                    </fb-property>
                                </fb-card>
                            </fb-container>
                        </fb-col>
                    </fb-row>
                    <fb-row flex>
                        <fb-col span="24">
                            <fb-container mt="10px">
                                <fb-card header="内存">
                                    <fb-property labelAlign="left" size="m" label-width="200px" mode="form" :label-style="{'font-size': '14px'}">
                                        <fb-row>
                                            <fb-col span="12">
                                                <fb-property-item label="服务器总内存">
                                                    {{ server.mem.total }}G
                                                </fb-property-item>
                                            </fb-col>
                                            <fb-col span="12">
                                                <fb-property-item label="JVM总内存">
                                                    {{ server.jvm.total }}M
                                                </fb-property-item>
                                            </fb-col>

                                        </fb-row>
                                        <fb-row>
                                            <fb-col span="12">
                                                <fb-property-item label="服务器已用内存">
                                                    {{ server.mem.used}}G
                                                </fb-property-item>
                                            </fb-col>
                                            <fb-col span="12">
                                                <fb-property-item label="JVM已用内存">
                                                    {{ server.jvm.used}}M
                                                </fb-property-item>
                                            </fb-col>
                                        </fb-row>
                                        <fb-row>
                                            <fb-col span="12">
                                                <fb-property-item label="服务器剩余内存">
                                                    {{ server.mem.free }}G
                                                </fb-property-item>
                                            </fb-col>
                                            <fb-col span="12">
                                                <fb-property-item label="JVM剩余内存">
                                                    {{ server.jvm.free }}M
                                                </fb-property-item>
                                            </fb-col>

                                        </fb-row>

                                        <fb-row>
                                            <fb-col span="12">
                                                <fb-property-item label="服务器内存使用率" :contentStyle="getFontColor(server.mem.usage)">
                                                    {{ server.mem.usage }}%
                                                </fb-property-item>
                                            </fb-col>
                                            <fb-col span="12">
                                                <fb-property-item label="JVM内存使用率" :contentStyle="getFontColor(server.jvm.usage)">
                                                    {{ server.jvm.usage }}%
                                                </fb-property-item>
                                            </fb-col>
                                        </fb-row>
                                    </fb-property>
                                </fb-card>
                            </fb-container>
                        </fb-col>
                    </fb-row>
                    <fb-row flex>
                        <fb-col span="24">
                            <fb-container mt="10px">
                                <fb-card header="Java虚拟机信息">
                                    <fb-property labelAlign="left" size="m" label-width="200px" mode="form" :label-style="{'font-size': '14px'}">
                                        <fb-row>
                                            <fb-col span="12">
                                                <fb-property-item label="Java名称">
                                                    {{ server.jvm.name }}
                                                </fb-property-item>
                                            </fb-col>
                                            <fb-col span="12">
                                                <fb-property-item label="Java版本">
                                                    {{ server.jvm.version }}
                                                </fb-property-item>
                                            </fb-col>
                                        </fb-row>
                                        <fb-row>
                                            <fb-col span="12">
                                                <fb-property-item label="启动时间">
                                                    {{ server.jvm.startTime }}
                                                </fb-property-item>
                                            </fb-col>
                                            <fb-col span="12">
                                                <fb-property-item label="运行时长">
                                                    {{ server.jvm.runTime }}
                                                </fb-property-item>
                                            </fb-col>
                                        </fb-row>
                                        <fb-row>
                                            <fb-col span="24">
                                                <fb-property-item label="安装路径">
                                                    {{ server.jvm.home }}
                                                </fb-property-item>
                                            </fb-col>
                                        </fb-row>
                                        <fb-row>
                                            <fb-col span="24">
                                                <fb-property-item label="项目路径">
                                                    {{ server.sys.userDir }}
                                                </fb-property-item>
                                            </fb-col>
                                        </fb-row>
                                        <fb-row>
                                            <fb-col span="24">
                                                <fb-property-item label="运行参数">
                                                    {{ server.jvm.inputArgs }}
                                                </fb-property-item>
                                            </fb-col>
                                        </fb-row>
                                    </fb-property>
                                </fb-card>
                            </fb-container>
                        </fb-col>
                    </fb-row>
                    <fb-row flex>
                        <fb-col span="24">
                            <fb-container mt="10px">
                                <fb-card header="磁盘状态">
                                    <fb-simple-table
                                            ref="table"
                                            :data="server.sysFiles"
                                            :pk="table.primaryKey"
                                            :columns="table.columns"
                                            :multiple="false"
                                    >
                                    </fb-simple-table>
                                </fb-card>
                            </fb-container>
                        </fb-col>
                    </fb-row>
                </div>
            </fb-container>
        </div>
        <div class="tp-dialog-bottom">
            <fb-button @on-click="handleClose">关闭</fb-button>
        </div>
    </div>
</template>

<script>
    import dayjs from "dayjs";
    var datas = [];
    export default {
        // 组件
        components: {
            dayjs
        },
        // 接收父组件的传参
        props: {
            param: {
                type: Object,
                require: false
            },
            parentPage: {
                type: Object,
                default: null
            }
        },
        // 初始化方法

        mounted() {
            this.getServerData(this.param.id)
        },

        data() {
            return {
                service: app.$svc.sys.monitor,
                inited: false,
                server : {
                    cpu: {},
                    mem: {},
                    jvm: {},
                    sys: {},
                    sysFiles: [],
                },
                tabs: [
                    // {label: `数据1`,name: `tab1`},
                    // {label: `数据2`,name: `tab2`}
                ],
                // Table列
                table: {
                    primaryKey: "dirName",
                    columns: [
                        {
                            name: 'dirName',
                            label: '盘符路径',
                            sortable: false,
                            width: 100,
                        },
                        {
                            name: 'sysTypeName',
                            label: '文件系统',
                            sortable: false,
                            width: 100,

                        },

                        {
                            name: 'typeName',
                            label: '盘符类型',
                            sortable: false,
                            width: 130,
                            slot: 'view',
                        },

                        {
                            name: 'total',
                            label: '总大小',
                            sortable: false,
                            width: 100,
                        },
                        {
                            name: 'free',
                            label: '可用大小',
                            sortable: false,
                            width: 100,
                        },
                        {
                            name: 'used',
                            label: '已用大小',
                            sortable: false,
                            width: 100,
                        },
                        {
                            name: 'usage',
                            label: '已用百分比',
                            sortable: false,
                            width: 100,
                        },
                    ],
                },
            }
        },

        // 方法
        methods: {
            // 取消
            handleClose() {
                let param = {};
                this.closeTpDialog(param);
            },
            getServerData(clientId){
                this.service.viewHeartbeat({"clientId": clientId}).then((result) => {
                    // 判断code
                    if (result.code == 1) {
                        this.server = result.data;
                        this.inited = true;
                    } else {
                        // 服务器返回失败
                        console.error('错误提示:' + result.message);
                    }
                })
            },

            // 格式化时间
            formatLastUpdateTime(val){
                let date = dayjs(val).format('YYYY年MM月DD日 HH:mm:ss')
                return date;
            },

            // 获取字体颜色
            getFontColor(val){
                if (val && parseInt(val) >= 60){
                    return {'color': 'red'};
                }
                return null;
            },

        },
        computed: {
            // 判断系统是否离线，超过1分钟则判定为离线
            ifOffLine(){
                let lastUpdateTime = this.server.lastUpdateTime;
                if (lastUpdateTime){
                    let now = dayjs().add(-3, 'minute').format('YYYYMMDDHHmmss');
                    if (now > lastUpdateTime){
                        return true;
                    }
                }
                return false;
            },  // 判断系统是否离线，超过1分钟则判定为离线
            getOffLineStyle(){
                if (this.ifOffLine){
                    return {'color': 'red'};
                }
                return {};
            }
        }
    }
</script>

<style lang="less" scoped>
    ::v-deep #tab > div.jpx-tabs-body > .jpx-tab--padding{
        padding: 0;
    }
</style>
