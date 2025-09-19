<template>
    <div class="tp-dialog">
        <div class="tp-dialog-top">
            <fb-form ref="editForm" :model="formData">
                <!-- 基本信息和头像 -->
                <fb-row>
                    <fb-col span="12">
                        <fb-row>
                            <fb-col span="24">
                                <fb-form-item label="姓名" prop="personName" :rule="[{ required: true, message: '请输入姓名', trigger: 'blur' }]">
                                    <fb-input v-model="formData.personName" placeholder="请输入姓名"></fb-input>
                                </fb-form-item>
                            </fb-col>
                        </fb-row>
                        <fb-row>
                            <fb-col span="24">
                                <fb-form-item label="性别" prop="sex" :rule="[{ required: true, message: '请选择性别', trigger: 'change' }]">
                                    <fb-select v-model="formData.sex" :data="defaultForm.sex" :placeholder="'请选择'"
                                        clearable />
                                </fb-form-item>
                            </fb-col>
                        </fb-row>
                        <fb-row>
                            <fb-col span="24">
                                <fb-form-item label="证件类型" prop="idtype" :rule="[{ required: true, message: '请选择证件类型', trigger: 'change' }]">
                                    <fb-select v-model="formData.idtype" :placeholder="'请选择证件类型'"
                                        :service="$svc.sys.dict.select" :param="{ 'pdicCode': 'Y24' }"
                                        @on-change="idtypeChange" />
                                </fb-form-item>
                            </fb-col>
                        </fb-row>
                        <fb-row>
                            <fb-col span="24">
                                <fb-form-item label="证件号" prop="idcard" :rule="[{ required: true, message: '请输入证件号', trigger: 'blur' }]">
                                    <fb-input v-model="formData.idcard" placeholder="请输入证件号"
                                        @on-blur="birthdayFormat"></fb-input>
                                </fb-form-item>
                            </fb-col>
                        </fb-row>
                    </fb-col>
                    <fb-col span="12">
                        <fb-row>
                            <fb-col span="17">
                                <fb-form-item label="头像">
                                    <tp-upload view="avatar" v-model="file.fileList" :service="$svc.sys.file"
                                        :param="{ 'referType': file.referType }" :referid="formData.personId"
                                        :accept="'.png,.jpeg,.jpg'"></tp-upload>
                                </fb-form-item>
                            </fb-col>
                        </fb-row>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="12">
                        <fb-form-item label="出生日期" prop="birthday">
                            <tp-datepicker v-model="formData.birthday" format="YYYY-MM-DD" value-format="YYYYMMDD"
                                clearable></tp-datepicker>
                        </fb-form-item>
                    </fb-col>
                    <fb-col span="12">
                        <fb-form-item label="民族" prop="safeprinNation">
                            <fb-select v-model="formData.safeprinNation" :placeholder="'请选择'"
                                :service="$svc.sys.dict.select" :param="{ 'pdicCode': 'Y26' }" filterable clearable />
                        </fb-form-item>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="12">
                        <fb-form-item label="手机号" prop="phone" :rule="[{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }]">
                            <fb-input v-model="formData.phone" placeholder="请输入手机号"></fb-input>
                        </fb-form-item>
                    </fb-col>
                    <fb-col span="12">
                        <fb-form-item label="联系电话" prop="tel" :rule="[{ required: false, type: 'tel' }]">
                            <fb-input v-model="formData.tel" placeholder="请输入联系电话"></fb-input>
                        </fb-form-item>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="24">
                        <fb-form-item label="邮箱" prop="email" :rule="[{ type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }]">
                            <fb-input v-model="formData.email" placeholder="请输入邮箱"></fb-input>
                        </fb-form-item>
                    </fb-col>
                </fb-row>

                <fb-row>
                    <fb-col span="12">
                        <fb-form-item label="职位" prop="office">
                            <fb-input type="text" v-model="formData.office" placeholder="请输入职位">
                            </fb-input>
                        </fb-form-item>
                    </fb-col>
                    <fb-col span="12">
                        <fb-form-item label="人员编号" prop="personNo">
                            <fb-input v-model="formData.personNo" placeholder="请输入人员编号"></fb-input>
                        </fb-form-item>
                    </fb-col>
                    <fb-col span="12">
                        <!-- 空列，保持布局平衡 -->
                    </fb-col>
<!--                    <fb-col span="12">-->
<!--                        <fb-form-item label="职称" prop="titleCode">-->
<!--                        -->
<!--                        <fb-select v-model="formData.titleCode"-->
<!--									   :service="$svc.sys.dict.select"-->
<!--									   :param="{'pdicCode': 'SYS12'}"-->
<!--									   placeholder="请选择"-->
<!--									   clearable>-->
<!--			</fb-select>-->
<!--                        </fb-form-item>-->
<!--                    </fb-col>-->
                </fb-row>

<!--                <fb-row>-->
<!--                    <fb-col span="12">-->
<!--                        <fb-form-item label="政治面貌" prop="politicsCode">-->
<!--                            <fb-select v-model="formData.politicsCode"-->
<!--                                       :service="$svc.sys.dict.select"-->
<!--                                       :param="{'pdicCode': 'SYS13'}"-->
<!--                                       placeholder="请选择"-->
<!--                                       clearable>-->
<!--                            </fb-select>-->
<!--                        </fb-form-item>-->
<!--                    </fb-col>-->
<!--                    <fb-col span="12">-->
<!--                        &lt;!&ndash; 空列，保持布局平衡 &ndash;&gt;-->
<!--                    </fb-col>-->
<!--                </fb-row>-->


<!--                <fb-row>-->
<!--                    <fb-col span="12">-->
<!--                        <fb-form-item label="参加工作时间" prop="partWorkDate">-->
<!--                            <tp-datepicker v-model="formData.partWorkDate" format="YYYY-MM-DD" value-format="YYYYMMDD"-->
<!--                                clearable></tp-datepicker>-->
<!--                        </fb-form-item>-->
<!--                    </fb-col>-->
<!--                    <fb-col span="12">-->
<!--                        <fb-form-item label="通信地址" prop="maddress">-->
<!--                            <fb-input v-model="formData.maddress" placeholder="请输入通信地址"></fb-input>-->
<!--                        </fb-form-item>-->
<!--                    </fb-col>-->
<!--                </fb-row>-->

<!--                <fb-row>-->
<!--                    <fb-col span="12">-->
<!--                        <fb-form-item label="毕业学校" prop="school">-->
<!--                            <fb-input v-model="formData.school" placeholder="请输入毕业学校"></fb-input>-->
<!--                        </fb-form-item>-->
<!--                    </fb-col>-->
<!--                    <fb-col span="12">-->
<!--                        <fb-form-item label="所学专业" prop="sepcSubject">-->
<!--                            <fb-input v-model="formData.sepcSubject" placeholder="请输入所学专业"></fb-input>-->
<!--                        </fb-form-item>-->
<!--                    </fb-col>-->
<!--                </fb-row>-->

<!--                <fb-row>-->
<!--                    <fb-col span="12">-->
<!--                        <fb-form-item label="最高学位" prop="degree">-->
<!--                            <fb-input v-model="formData.degree" placeholder="请输入最高学位"></fb-input>-->
<!--                        </fb-form-item>-->
<!--                    </fb-col>-->
<!--                    <fb-col span="12">-->
<!--                        <fb-form-item label="学历" prop="diplomaCode">-->
<!--                            <fb-select v-model="formData.diplomaCode"-->
<!--                                       :service="$svc.sys.dict.select"-->
<!--                                       :param="{'pdicCode': 'SYS14'}"-->
<!--                                       placeholder="请选择"-->
<!--                                       clearable>-->
<!--                            </fb-select>-->
<!--                        </fb-form-item>-->
<!--                    </fb-col>-->
<!--                </fb-row>-->

<!--                <fb-row>-->
<!--                    <fb-col span="12">-->
<!--                        <fb-form-item label="执法证号" prop="checkcardNo">-->
<!--                            <fb-input v-model="formData.checkcardNo" placeholder="请输入执法证号"></fb-input>-->
<!--                        </fb-form-item>-->
<!--                    </fb-col>-->
<!--                    <fb-col span="12">-->
<!--                        &lt;!&ndash; 空列，保持布局平衡 &ndash;&gt;-->
<!--                    </fb-col>-->
<!--                </fb-row>-->

                <fb-row>
                    <fb-col span="24">
                        <fb-form-item label="个人简介" prop="resume">
                            <fb-textarea v-model="formData.resume" rows="4" placeholder="请输入个人简介"
                                :maxlength="200"></fb-textarea>
                        </fb-form-item>
                    </fb-col>
                </fb-row>
            </fb-form>
        </div>

        <div class="tp-dialog-bottom">
            <fb-button style="margin-right: 12px" type="primary" @on-click="saveProfile">保存</fb-button>
            <fb-button @on-click="handleClose">关闭</fb-button>
        </div>
    </div>
</template>

<script>
export default {
    name: 'ProfileEdit',
    props: {
        param: {
            type: Object,
            default: () => ({})
        },
        userInfo: {
            type: Object,
            default: () => ({})
        },
        dialogParams: {
            type: Object,
            default: () => ({})
        },
        	 
			parentPage: {
				type: Object,
				default: null
			}
    },
    data() {
        return {
            service: this.$svc.sys.person,
            saving: false,
            validationErrorShown: false, // 防止快速点击时出现多个验证错误提示
            file: {
                referType: 'SYS1014',
                fileList: []
            },
            formData: {
                personId: '',
                personName: '',
                sex: '',
                idtype: '',
                idcard: '',
                birthday: '',
                safeprinNation: '',
                phone: '',
                tel: '',
                email: '',
                maddress: '',
                position: '',
                office: '',
                titleCode: '',
                politicsCode: '',
                personNo: '',
                partWorkDate: '',
                school: '',
                sepcSubject: '',
                degree: '',
                diplomaCode: '',
                checkcardNo: '',
                resume: ''
            },
            defaultForm: {
                sex: [
                    { value: '1', label: '男' },
                    { value: '2', label: '女' }
                ]
            },

        }
    },
    mounted() {
        this.initFormData()
    },
    watch: {
        userInfo: {
            handler(newVal) {
                if (newVal) {
                    this.initFormData()
                }
            },
            deep: true,
            immediate: true
        },
        dialogParams: {
            handler(newVal) {
                if (newVal && newVal.userInfo) {
                    this.initFormData(newVal.userInfo)
                }
            },
            deep: true,
            immediate: true
        }
    },
    methods: {
        // 初始化表单数据
        initFormData(userData) {
            // 优先使用TpDialog传递的param参数，然后是传入的用户数据，再是组件的userInfo，最后是dialogParams中的userInfo
            const userInfo = (this.param && this.param.userInfo) || userData || this.userInfo || (this.dialogParams && this.dialogParams.userInfo)
            if (userInfo && Object.keys(userInfo).length > 0) {
                this.formData = {
                    personId: userInfo.personId || '',
                    personName: userInfo.personName || userInfo.realName || '',
                    sex: userInfo.sex + "" || '',
                    idtype: userInfo.idtype || '',
                    idcard: userInfo.idcard || '',
                    birthday: userInfo.birthday || '',
                    safeprinNation: userInfo.safeprinNation || '',
                    phone: userInfo.phone || '',
                    tel: userInfo.tel || '',
                    email: userInfo.email || '',
                    maddress: userInfo.maddress || '',
                    position: userInfo.position || '',
                    office: userInfo.office || '',
                    titleCode: userInfo.titleCode || '',
                    politicsCode: userInfo.politicsCode || '',
                    personNo: userInfo.personNo || '',
                    partWorkDate: userInfo.partWorkDate || '',
                    school: userInfo.school || '',
                    sepcSubject: userInfo.sepcSubject || '',
                    degree: userInfo.degree || '',
                    diplomaCode: userInfo.diplomaCode || '',
                    checkcardNo: userInfo.checkcardNo || '',
                    resume: userInfo.resume || ''
                }

                // 设置头像文件列表
                if (userInfo.profilePhoto) {
                    this.file.fileList = [{
                        url: userInfo.profilePhoto,
                        name: 'avatar'
                    }]
                }
            }
        },

        // 身份证号自动提取生日
        birthdayFormat() {
            let idcard = this.formData.idcard
            if (this.formData.idtype === 'Y2401' && idcard && (idcard.length === 15 || idcard.length === 18)) {
                this.formData.birthday = idcard.substring(6, 14)
            }
        },

        // 保存个人信息
        saveProfile() {
            // 防止重复提交
            if (this.saving) {
                return
            }
            
            this.$refs.editForm.validate((valid) => {
                if (valid) {
                    this.saving = true

                    // 准备基本信息数据
                    const basicData = {
                        personId: this.formData.personId,
                        personName: this.formData.personName,
                        sex: this.formData.sex,
                        idtype: this.formData.idtype,
                        idcard: this.formData.idcard,
                        birthday: this.formData.birthday,
                        safeprinNation: this.formData.safeprinNation,
                        phone: this.formData.phone,
                        tel: this.formData.tel,
                        email: this.formData.email,
                        position: this.formData.position,
                        office: this.formData.office,
                        personNo: this.formData.personNo,
                        file: this.file
                    }

                    // 准备扩展信息数据
                    const expData = {
                        personId: this.formData.personId,
                        titleCode: this.formData.titleCode,
                        politicsCode: this.formData.politicsCode,
                        maddress: this.formData.maddress,
                        partWorkDate: this.formData.partWorkDate,
                        school: this.formData.school,
                        sepcSubject: this.formData.sepcSubject,
                        degree: this.formData.degree,
                        diplomaCode: this.formData.diplomaCode,
                        checkcardNo: this.formData.checkcardNo,
                        resume: this.formData.resume
                    }

                    // 先保存基本信息，再保存扩展信息
                    this.service.update(basicData).then((result) => {
                        if (result.code === 1) {
                            // 基本信息保存成功，继续保存扩展信息
                            return this.service.expAdd(expData)
                        } else {
                            throw new Error(result.message || '基本信息保存失败')
                        }
                    }).then((expResult) => {
                        this.saving = false
                        if (expResult.code === 1) {
                            this.$message.success('保存成功')
                            // 使用 closeTpDialog 方法关闭弹框并传递数据给父组件
                            this.closeTpDialog(expResult.data)
                        } else {
                            this.$message.error('扩展信息保存失败：' + (expResult.message || '未知错误'))
                        }
                    }).catch((error) => {
                        this.saving = false
                        this.$message.error('保存失败：' + error.message)
                    })
                } else {
                    // 防止快速点击时出现多个错误提示
                    if (!this.validationErrorShown) {
                        this.validationErrorShown = true
                        this.$message.error('请检查表单填写是否正确')
                        // 1秒后重置标志，允许再次显示错误提示
                        setTimeout(() => {
                            this.validationErrorShown = false
                        }, 1000)
                    }
                }
            })
        },

        // 证件类型改变时清空证件号
        idtypeChange() {
            this.formData.idcard = ''
        },

        // 取消编辑
        cancelEdit() {
            this.$emit('cancel-edit')
            this.handleClose()
        },

        // 关闭对话框
        handleClose() {
            // 关闭弹框 - 使用TpDialog的正确关闭方法
            if (this.$parent && this.$parent.close) {
                this.$parent.close();
            } else if (this.$parent && this.$parent.$parent && this.$parent.$parent.close) {
                this.$parent.$parent.close();
            } else {
                // 发送关闭事件给父组件
                this.$emit('close');
            }
        },
    }
}
</script>

<style lang="less" scoped>
.profile-edit {
    padding: 20px;
    background: #fff;
    border-radius: 8px;

    .edit-container {
        max-width: 800px;
        margin: 0 auto;
    }

    .avatar-upload-section {
        text-align: center;
        margin-bottom: 30px;
        padding: 20px;
        background: #fafafa;
        border-radius: 8px;

        .avatar-upload-wrapper {
            display: inline-block;
        }
    }

    .form-section {
        margin-bottom: 30px;

        .section-title {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #e8e8e8;
            font-size: 16px;
            font-weight: 600;
            color: #333;

            i {
                margin-right: 8px;
                font-size: 18px;
                color: #1890ff;
            }
        }
    }

    .form-actions {
        text-align: center;
        margin-top: 40px;
        padding-top: 20px;
        border-top: 1px solid #e8e8e8;

        .fb-button {
            margin: 0 10px;
            min-width: 100px;

            .fb-icon {
                margin-right: 5px;
            }
        }
    }
}

// 表单样式优化
.fb-form {
    .fb-form-item {
        margin-bottom: 20px;

        .fb-form-item-label {
            font-weight: 500;
            color: #333;
        }
    }

    .fb-input,
    .fb-select,
    .fb-date-picker,
    .fb-textarea {
        width: 100%;
    }

    .fb-textarea {
        resize: vertical;
    }
}

// 响应式设计
@media (max-width: 768px) {
    .profile-edit {
        padding: 15px;

        .edit-container {
            max-width: 100%;
        }

        .fb-col {
            span: 24 !important;
        }
    }
}
</style>