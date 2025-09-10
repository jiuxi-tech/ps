<template>
    <div class="tp-dialog">
        <div class="tp-dialog-top">
            <fb-property bordered label-width="120px" mode="form">
                <fb-fieldset label="许可证信息"/>
                <fb-row>
                    <fb-col span="12">
                        <fb-property-item label="系统名称">
                            {{ formData.systemName }}
                        </fb-property-item>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="12">
                        <fb-property-item label="系统序列号">
                            {{ formData.licenceId }}
                        </fb-property-item>
                    </fb-col>
                </fb-row>
            </fb-property>

            <fb-form ref="fbform" style="padding-top: 20px" :label-width="120">
                <fb-fieldset label="更新许可证"/>
                <fb-row>
                    <fb-col span="12">
                        <fb-form-item label="许可证">
                            <tp-upload
                                    v-model="formData.file"
                                    :service="$svc.sys.licence"
                                    :param="{'referType':'jpx'}"
                                    :accept="'.txt'"
                                    buttonText="上传许可证"
                                    @on-change="fileListChange"
                                    @on-success="onSuccess"
                                    @on-error="onError"></tp-upload>
                        </fb-form-item>
                    </fb-col>
                </fb-row>

            </fb-form>
        </div>

    </div>
</template>


<script>


    export default {
        name: 'licence',
        mixins: [

        ],
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
        // 组件
        components: {
            // 'component-a': ComponentA,
        },
        // 创建方法
        created() {
            // 记录原来的默认值，用于表单重置
        },
        // 初始化方法
        mounted() {
            // 执行界面初始化
            this.init(this.param);

        },
        data() {
            return {
                // 附件列表
                fileList: [],

                // 请求的 service
                service: app.$svc.sys.licence,

                // 表单数据
                formData: {
                    systemName: '',
                    licenceId: '',
                    expiringDate: '',
                    hashCode: '',
                    file: ''
                }

            }
        },

        // 方法
        methods: {
            /**
             * 显示窗口
             * param 参数
             * title 标题
             */
            init(param) {
                // 查询系统许可证序列号
                this.view();
            },

            // 取消
            handleClose() {

                let param = {};
                this.closeTpDialog(param);
            },

            onSuccess() {
                let param = {};
                this.closeTpDialog(param);
            },

            onError() {

            },

            fileListChange(info) {
                console.log(info)
                if (info.file.res) {
                    this.fileList.push(info.file.res.data);
                }
            },

            // 查看许可证
            view() {

                let that = this;
                // 调用新增service方法
                this.service.view({}).then((result) => {
                    // 判断code
                    if (result.code == 1) {
                        that.formData = result.data
                    } else {
                        // 服务器返回失败
                        this.$message.error('错误提示:' + result.message)
                    }
                })
            },

        }
    }
</script>

<style lang="less" scoped>

</style>