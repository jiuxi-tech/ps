<template>
    <div class="tp-dialog">
        <div class="tp-dialog-top">
            <fb-form ref="fbform">
                <fb-row>
                    <fb-col span="24">
                        <fb-form-item label="证书名称" prop="certName" :rule="[{required: true}]">
                            <fb-input v-model="formData.certName" placeholder="请输入证书名称"
                                      :maxlength="100"></fb-input>
                        </fb-form-item>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="24">
                        <fb-form-item label="证书描述" prop="certDesc">
                            <fb-textarea rows="3" v-model="formData.certDesc"
                                         type="text"
                                         placeholder="请输入证书描述"
                                         :maxlength="500">
                            </fb-textarea>
                        </fb-form-item>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="24">
                        <fb-form-item label="PEM证书文件" 
                                        prop="pemContent" 
                                        :value="formData.pemContent" 
                                      :rule="[{required: true, message: '请上传PEM证书文件'}]">
                            <div>
                                <input
                                    ref="pemFileInput"
                                    type="file"
                                    accept=".pem,.crt,.cer"
                                    style="display: none"
                                    @change="onPemFileSelect"
                                />
                                <fb-button @on-click="selectPemFile" :loading="pemUploading">
                                    <span v-if="!pemFileName">选择PEM文件</span>
                                    <span v-else>重新选择</span>
                                </fb-button>
                                <span v-if="pemFileName" class="file-info">
                                    <fb-icon type="md-document" style="margin-left: 8px; color: #2d8cf0;"></fb-icon>
                                    {{ pemFileName }}
                                    <fb-button size="small" type="text" @on-click="removePemFile" style="margin-left: 8px; color: #ed4014;">
                                        删除
                                    </fb-button>
                                </span>
                            </div>
                        </fb-form-item>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="24">
                        <fb-form-item label="私钥文件" prop="keyContent" 
                                        :value="formData.keyContent" 
                                      :rule="[{required: true, message: '请上传私钥文件'}]">
                            <div>
                                <input
                                    ref="keyFileInput"
                                    type="file"
                                    accept=".key,.pem"
                                    style="display: none"
                                    @change="onKeyFileSelect"
                                />
                                <fb-button @on-click="selectKeyFile" :loading="keyUploading">
                                    <span v-if="!keyFileName">选择私钥文件</span>
                                    <span v-else>重新选择</span>
                                </fb-button>
                                <span v-if="keyFileName" class="file-info">
                                    <fb-icon type="md-key" style="margin-left: 8px; color: #ff9900;"></fb-icon>
                                    {{ keyFileName }}
                                    <fb-button size="small" type="text" @on-click="removeKeyFile" style="margin-left: 8px; color: #ed4014;">
                                        删除
                                    </fb-button>
                                </span>
                            </div>
                        </fb-form-item>
                    </fb-col>
                </fb-row>

                <!-- 证书信息预览区域 -->
                <fb-row v-if="certInfo">
                    <fb-col span="24">
                        <fb-card title="证书信息预览" :bordered="true">
                            <fb-row>
                                <fb-col span="12">
                                    <fb-form-item label="公用名(CN)">
                                        <span>{{ certInfo.subjectCn || '-' }}</span>
                                    </fb-form-item>
                                </fb-col>
                                <fb-col span="12">
                                    <fb-form-item label="组织(O)">
                                        <span>{{ certInfo.subjectO || '-' }}</span>
                                    </fb-form-item>
                                </fb-col>
                            </fb-row>
                            <fb-row>
                                <fb-col span="12">
                                    <fb-form-item label="组织单位(OU)">
                                        <span>{{ certInfo.subjectOu || '-' }}</span>
                                    </fb-form-item>
                                </fb-col>
                                <fb-col span="12">
                                    <fb-form-item label="绑定域名">
                                        <span>{{ certInfo.domainNames || '-' }}</span>
                                    </fb-form-item>
                                </fb-col>
                            </fb-row>
                            <fb-row>
                                <fb-col span="24">
                                    <fb-form-item label="发证机构">
                                        <span>{{ certInfo.issuer || '-' }}</span>
                                    </fb-form-item>
                                </fb-col>
                            </fb-row>
                            <fb-row>
                                <fb-col span="12">
                                    <fb-form-item label="颁发日期">
                                        <span>{{ certInfo.issueDate ? formatDate(certInfo.issueDate) : '-' }}</span>
                                    </fb-form-item>
                                </fb-col>
                                <fb-col span="12">
                                    <fb-form-item label="到期日期">
										<span :class="{'text-danger': isExpiringSoon(certInfo.expireDate)}">
											{{ certInfo.expireDate ? formatDate(certInfo.expireDate) : '-' }}
										</span>
                                    </fb-form-item>
                                </fb-col>
                            </fb-row>
                        </fb-card>
                    </fb-col>
                </fb-row>

            </fb-form>
        </div>

        <div class="tp-dialog-bottom">
            <fb-button style="margin-right: 12px" type="primary" @on-click="save" :loading="saveLoading">保存
            </fb-button>
            <fb-button @on-click="handleClose">关闭</fb-button>
        </div>
    </div>
</template>

<script>
import dayjs from 'dayjs'

export default {
    name: 'add',
    mixins: [],
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
        this.init()
    },

    data() {
        return {
            formData: {
                certId: '',
                certName: '',
                certDesc: '',
                pemContent: '',
                keyContent: '',
                // 证书解析信息字段
                domainNames: '',
                issuer: '',
                subjectCn: '',
                subjectO: '',
                subjectOu: '',
                issueDate: null,
                expireDate: null,
                // 状态字段
                status: 0, // 0-未应用
                isInUse: 0, // 0-否
                isExpired: 0, // 0-否
                appliedTime: null,
                // 系统字段
                createPersonId: '',
                createPersonName: '',
                createTime: null,
                updatePersonId: '',
                updatePersonName: '',
                updateTime: null,
                actived: 1 // 1-有效
            },
            // 文件状态变量
            pemFileName: '',
            keyFileName: '',
            pemUploading: false,
            keyUploading: false,
            certInfo: null,
            saveLoading: false,
            isEdit: false,
        }
    },

    // 方法
    methods: {
        // 初始化
        init() {
            this.isEdit = this.param.mode === 'edit' && this.param.certId

            if (this.isEdit) {
                this.loadCertData()
            }
        },

        // 加载证书数据（修改时）
        loadCertData() {
            app.$svc.sys.serverCert.view({certId: this.param.certId}).then((result) => {
                if (result.code == 1) {
                    this.formData = {
                        certId: result.data.certId,
                        certName: result.data.certName,
                        certDesc: result.data.certDesc,
                        pemContent: result.data.pemContent,
                        keyContent: result.data.keyContent,
                        // 加载完整的证书信息字段
                        domainNames: result.data.domainNames,
                        issuer: result.data.issuer,
                        subjectCn: result.data.subjectCn,
                        subjectO: result.data.subjectO,
                        subjectOu: result.data.subjectOu,
                        issueDate: result.data.issueDate,
                        expireDate: result.data.expireDate,
                        status: result.data.status,
                        isInUse: result.data.isInUse,
                        isExpired: result.data.isExpired,
                        appliedTime: result.data.appliedTime
                    }

                    // 设置证书信息展示（用于预览）
                    this.certInfo = {
                        domainNames: result.data.domainNames,
                        issuer: result.data.issuer,
                        subjectCn: result.data.subjectCn,
                        subjectO: result.data.subjectO,
                        subjectOu: result.data.subjectOu,
                        issueDate: result.data.issueDate,
                        expireDate: result.data.expireDate
                    }

                    // 设置文件名称显示
                    if (result.data.pemContent) {
                        this.pemFileName = '当前证书文件.pem'
                    }

                    if (result.data.keyContent) {
                        this.keyFileName = '当前私钥文件.key'
                    }
                } else {
                    this.$message.error('加载数据失败: ' + result.message)
                }
            })
        },

        // ==== PEM证书文件处理 ====
        // 选择PEM文件
        selectPemFile() {
            this.$refs.pemFileInput.click()
        },

        // PEM文件选择事件
        onPemFileSelect(event) {
            const files = event.target.files
            if (!files || files.length === 0) {
                return
            }

            const file = files[0]
            
            // 文件类型验证
            const isValidType = ['application/x-pem-file', 'application/x-x509-ca-cert', 'text/plain'].includes(file.type) ||
                file.name.toLowerCase().endsWith('.pem') ||
                file.name.toLowerCase().endsWith('.crt') ||
                file.name.toLowerCase().endsWith('.cer')

            if (!isValidType) {
                this.$message.error('只支持 .pem、.crt、.cer 格式的证书文件')
                return
            }

            // 文件大小验证 (10MB)
            if (file.size > 10 * 1024 * 1024) {
                this.$message.error('文件大小不能超过10MB')
                return
            }

            this.pemUploading = true
            this.pemFileName = file.name

            const reader = new FileReader()
            reader.onload = (e) => {
                try {
                    const content = e.target.result
                    // 转换为BASE64编码
                    this.formData.pemContent = btoa(unescape(encodeURIComponent(content)))

                    // 解析证书信息
                    this.parseCertificate()

                    // 验证证书和私钥匹配（如果私钥已上传）
                    if (this.formData.keyContent) {
                        this.validateCertAndKey()
                    }

                    this.pemUploading = false
                } catch (error) {
                    this.$message.error('文件读取失败: ' + error.message)
                    this.pemUploading = false
                    this.removePemFile()
                }
            }

            reader.onerror = () => {
                this.$message.error('文件读取失败')
                this.pemUploading = false
                this.removePemFile()
            }

            reader.readAsText(file)

            // 清空输入框的值，以便能够再次选择同一个文件
            event.target.value = ''
        },

        // 移除PEM文件
        removePemFile() {
            this.formData.pemContent = ''
            this.pemFileName = ''
            this.certInfo = null
        },

        // ==== 私钥文件处理 ====
        // 选择私钥文件
        selectKeyFile() {
            this.$refs.keyFileInput.click()
        },

        // 私钥文件选择事件
        onKeyFileSelect(event) {
            const files = event.target.files
            if (!files || files.length === 0) {
                return
            }

            const file = files[0]
            
            // 文件类型验证
            const isValidType = ['application/x-pem-file', 'text/plain'].includes(file.type) ||
                file.name.toLowerCase().endsWith('.key') ||
                file.name.toLowerCase().endsWith('.pem')

            if (!isValidType) {
                this.$message.error('只支持 .key、.pem 格式的私钥文件')
                return
            }

            // 文件大小验证 (10MB)
            if (file.size > 10 * 1024 * 1024) {
                this.$message.error('文件大小不能超过10MB')
                return
            }

            this.keyUploading = true
            this.keyFileName = file.name

            const reader = new FileReader()
            reader.onload = (e) => {
                try {
                    const content = e.target.result
                    // 转换为BASE64编码
                    this.formData.keyContent = btoa(unescape(encodeURIComponent(content)))

                    // 验证证书和私钥匹配（如果证书已上传）
                    if (this.formData.pemContent) {
                        this.validateCertAndKey()
                    }

                    this.keyUploading = false
                } catch (error) {
                    this.$message.error('文件读取失败: ' + error.message)
                    this.keyUploading = false
                    this.removeKeyFile()
                }
            }

            reader.onerror = () => {
                this.$message.error('文件读取失败')
                this.keyUploading = false
                this.removeKeyFile()
            }

            reader.readAsText(file)

            // 清空输入框的值，以便能够再次选择同一个文件
            event.target.value = ''
        },

        // 移除私钥文件
        removeKeyFile() {
            this.formData.keyContent = ''
            this.keyFileName = ''
        },

        // 解析证书信息
        parseCertificate() {
            if (!this.formData.pemContent) {
                return
            }

            app.$svc.sys.serverCert.parse({pemContent: this.formData.pemContent}).then((result) => {
                if (result.code == 1) {
                    this.certInfo = result.data
                } else {
                    this.$message.error('证书解析失败: ' + result.message)
                    this.certInfo = null
                }
            })
        },

        // 验证证书和私钥匹配
        validateCertAndKey() {
            if (!this.formData.pemContent || !this.formData.keyContent) {
                return
            }

            app.$svc.sys.serverCert.validate({
                pemContent: this.formData.pemContent,
                keyContent: this.formData.keyContent,
            }).then((result) => {
                if (result.code == 1) {
                    if (!result.data) {
                        this.$message.warn('证书文件与私钥文件不匹配，请检查文件')
                    }
                }
            })
        },

        // 保存
        save() {
            this.$refs.fbform.validate((valid) => {
                if (valid) {
                    if (!this.formData.pemContent) {
                        this.$message.error('请上传PEM证书文件')
                        return
                    }

                    if (!this.formData.keyContent) {
                        this.$message.error('请上传私钥文件')
                        return
                    }

                    this.saveLoading = true

                    // 构建完整的提交数据，包含证书解析信息
                    const submitData = {
                        ...this.formData,
                        // 从证书解析信息中获取字段
                        domainNames: this.certInfo?.domainNames || null,
                        issuer: this.certInfo?.issuer || null,
                        subjectCn: this.certInfo?.subjectCn || null,
                        subjectO: this.certInfo?.subjectO || null,
                        subjectOu: this.certInfo?.subjectOu || null,
                        issueDate: this.certInfo?.issueDate || null,
                        expireDate: this.certInfo?.expireDate || null,
                        // 新增证书时的默认状态
                        status: this.isEdit ? this.formData.status : 0, // 0-未应用
                        isInUse: this.isEdit ? this.formData.isInUse : 0, // 0-否
                        isExpired: this.isEdit ? this.formData.isExpired : 0, // 0-否
                        actived: 1 // 1-有效
                    }

                    const apiMethod = this.isEdit ? 'update' : 'add'
                    const successMessage = this.isEdit ? '修改成功' : '新增成功'

                    app.$svc.sys.serverCert[apiMethod](submitData).then((result) => {
                        this.saveLoading = false
                        if (result.code == 1) {
                            this.$message.success(successMessage)
                            this.handleClose()
                        } else {
                            this.$message.error(result.message || '操作失败')
                        }
                    }).catch(() => {
                        this.saveLoading = false
                    })
                } else {
                    this.$message.error('请填写完整信息')
                }
            })
        },

	// 取消
		handleClose() {
			// 关闭，并传递参数
			this.closeTpDialog("xxxx");
		},

        // 格式化日期
        formatDate(date) {
            return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
        },

        // 检查是否即将过期
        isExpiringSoon(expireDate) {
            if (!expireDate) return false
            const expire = dayjs(expireDate)
            const now = dayjs()
            const daysUntilExpire = expire.diff(now, 'day')
            return daysUntilExpire <= 30 // 30天内过期显示为红色
        },
    },
}
</script>

<style scoped>
.text-danger {
    color: #ff4d4f;
    font-weight: bold;
}

.file-info {
    display: inline-flex;
    align-items: center;
    font-size: 14px;
    color: #333;
    margin-left: 8px;
}

.file-info .fb-icon {
    margin-right: 4px;
}
</style>