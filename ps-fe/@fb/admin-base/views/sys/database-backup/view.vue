<template>
    <div class="tp-dialog">
        <div class="tp-dialog-top">
            <fb-property bordered label-width="140px" mode="form">
                <fb-row>
                    <fb-col span="12">
                        <fb-property-item label="备份名称">
                            {{ formData.backupName }}
                        </fb-property-item>
                    </fb-col>
                    <fb-col span="12">
                        <fb-property-item label="数据库名称">
                            {{ formData.databaseName }}
                        </fb-property-item>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="12">
                        <fb-property-item label="备份类型">
                            {{ formData.backupTypeName }}
                        </fb-property-item>
                    </fb-col>
                    <fb-col span="12">
                        <fb-property-item label="备份状态">
						<span :class="getStatusClass(formData.backupStatus)">
							{{ formData.backupStatusName }}
						</span>
                        </fb-property-item>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="12">
                        <fb-property-item label="开始时间">
                            {{ formatTime(formData.backupStartTime) }}
                        </fb-property-item>
                    </fb-col>
                    <fb-col span="12">
                        <fb-property-item label="结束时间">
                            {{ formatTime(formData.backupEndTime) }}
                        </fb-property-item>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="12">
                        <fb-property-item label="备份耗时">
                            {{ formData.backupDurationDisplay }}
                        </fb-property-item>
                    </fb-col>
                    <fb-col span="12">
                        <fb-property-item label="文件大小">
                            {{ formData.backupFileSizeDisplay }}
                        </fb-property-item>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="24">
                        <fb-property-item label="备份文件路径">
                            {{ formData.backupFilePath }}
                        </fb-property-item>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="24">
                        <fb-property-item label="备份文件名">
                            {{ formData.backupFileName }}
                        </fb-property-item>
                    </fb-col>
                </fb-row>

                <fb-row v-if="formData.backupCommand">
                    <fb-col span="24">
                        <fb-property-item label="备份命令">
                            <div class="command-text">{{ formData.backupCommand }}</div>
                        </fb-property-item>
                    </fb-col>
                </fb-row>

                <fb-row v-if="formData.errorMessage">
                    <fb-col span="24">
                        <fb-property-item label="错误信息">
                            <div class="error-text">{{ formData.errorMessage }}</div>
                        </fb-property-item>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="12">
                        <fb-property-item label="创建时间">
                            {{ formatTime(formData.createTime) }}
                        </fb-property-item>
                    </fb-col>
                    <fb-col span="12">
                        <fb-property-item label="修改时间">
                            {{ formatTime(formData.updateTime) }}
                        </fb-property-item>
                    </fb-col>
                </fb-row>

                

            </fb-property>

        </div>
        <div class="tp-dialog-bottom">

            <fb-flex jc-center gap="8px">

                <template v-if="formData.backupStatus === 1">
                    <fb-button @on-click="handleStop" danger>停止备份</fb-button>
                    <fb-button @on-click="handleRefresh">刷新状态</fb-button>
                </template>

                <fb-button @on-click="handleClose">关闭</fb-button>
            </fb-flex>


        </div>
    </div>
</template>

<script>
import dayjs from 'dayjs'

export default {
    props: {
        param: {
            type: Object,
            require: false,
        },
        parentPage: {
            type: Object,
            default: null,
        },
    },

    // 初始化方法
    mounted () {
        this.loadData()
    },

    data () {
        return {
            formData: {
                backupId: '',
                backupName: '',
                backupType: null,
                backupStatus: null,
                databaseName: '',
                backupFilePath: '',
                backupFileName: '',
                backupFileSize: null,
                backupStartTime: '',
                backupEndTime: '',
                backupDuration: null,
                backupCommand: '',
                errorMessage: '',
                createTime: '',
                updateTime: '',
                backupTypeName: '',
                backupStatusName: '',
                backupFileSizeDisplay: '',
                backupDurationDisplay: '',
            },
            loading: false,
        }
    },

    // 方法
    methods: {
        // 加载数据
        loadData () {

            this.formData.backupId = this.param.backupId
            if (!this.param.backupId) {
                this.$message.error('缺少备份记录ID')
                return
            }

            this.loading = true
            app.service.request('/sys/database-backup/view', {
                method: 'get',
                params: {
                    'backupId': this.param.backupId,
                    'passKey': this.param.passKey,
                },
                responseType: 'json',
                timeout: 5000,
            }).then((result) => {
                if (result.code == 1) {
                    this.formData = Object.assign(this.formData, result.data)
                } else {
                    this.$message.error('加载数据失败: ' + result.message)
                }
            }).catch((error) => {
                this.$message.error('请求失败: ' + error.message)
            }).finally(() => {
                this.loading = false
            })
        },

        // 停止备份
        handleStop () {
            this.$confirm('确定要停止该备份任务吗？', () => {
                this.stopBackup()
            })
        },

        stopBackup () {
            app.service.request('/sys/database-backup/stop-backup', {
                method: 'post',
                data: {
                    'backupId': this.param.backupId,
                    'passKey': this.param.passKey,
                },
                headers: {'Content-Type': 'application/json'},
                responseType: 'json',
                timeout: 5000,
            }).then((result) => {
                if (result.code == 1) {
                    this.$message.success('备份任务已停止')
                    this.loadData() // 重新加载数据
                } else {
                    this.$message.error('停止备份失败: ' + result.message)
                }
            }).catch((error) => {
                this.$message.error('请求失败: ' + error.message)
            })
        },

        // 刷新数据
        handleRefresh () {
            this.loadData()
        },

        // 关闭弹窗
        handleClose () {
            this.closeTpDialog()
        },

        // 格式化时间
        formatTime (time) {
            return time ? dayjs(time).format('YYYY-MM-DD HH:mm:ss') : ''
        },

        // 获取状态样式
        getStatusClass (status) {
            switch (status) {
                case 1:
                    return 'status-running' // 进行中
                case 2:
                    return 'status-success' // 成功
                case 3:
                    return 'status-failed'  // 失败
                default:
                    return ''
            }
        },
    },
}
</script>

<style lang="less" scoped>
.status-running {
    color:       #1890ff;
    font-weight: bold;
}

.status-success {
    color:       #52c41a;
    font-weight: bold;
}

.status-failed {
    color:       #ff4d4f;
    font-weight: bold;
}

.command-text {
    background-color: #f6f8fa;
    border:           1px solid #d1d9e0;
    border-radius:    4px;
    padding:          8px 12px;
    font-family:      "Courier New", Consolas, monospace;
    font-size:        12px;
    line-height:      1.6;
    white-space:      pre-wrap;
    word-break:       break-all;
    min-height:       60px;
}

.error-text {
    background-color: #fff2f0;
    border:           1px solid #ffccc7;
    border-radius:    4px;
    padding:          8px 12px;
    color:            #ff4d4f;
    font-size:        13px;
    line-height:      1.6;
    white-space:      pre-wrap;
    word-break:       break-all;
    min-height:       80px;
}
</style>