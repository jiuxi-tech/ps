<template>
    <div class="user-profile" v-if="true">
        <div class="profile-container">
            <!-- 个人资料头部 -->
            <div class="profile-header">
                <div class="header-background"></div>
                <div class="header-content">
                    <div class="avatar-section">
                        <div class="avatar-wrapper">
                            <tp-upload readonly view="avatar" v-model="file.fileList" :service="$svc.sys.file"
                                       :param="{ referType: file.referType }" :referid="userInfo.personId"
                                       :accept="'.png,.jpeg,.jpg'"></tp-upload>
                        </div>
                        <div class="user-info">
                            <h1 class="user-name">
                                {{ userInfo.personName || userInfo.username }}
                                <span v-if="userInfo.userRoles && userInfo.userRoles.length > 0" class="role-name">
                  （{{ userInfo.userRoles.map(role => role.roleName).join('，') }}）
                </span>
                            </h1>
                            <div class="user-details">
                                <div class="detail-item">
                                    <fb-icon name="team" class="detail-icon"></fb-icon>
                                    <span>{{ userInfo.deptFullName || '暂无部门' }}</span>
                                </div>
                                <div class="detail-item">
                                    <fb-icon name="user" class="detail-icon"></fb-icon>
                                    <span>{{ userInfo.office || userInfo.position || '暂无职位' }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="header-actions">
                        <fb-button type="primary" size="large" @on-click="editProfile" class="edit-btn">
                            <fb-icon name="edit"></fb-icon>
                            编辑资料
                        </fb-button>
                    </div>
                </div>
            </div>

            <!-- 信息卡片网格 -->
            <div class="info-grid">
                <!-- 基本信息卡片 -->
                <div class="info-card">
                    <div class="card-header">
                        <div class="card-icon basic-info">
                            <i class="iconfont jpx-icon-information-card"></i>
                        </div>
                        <h3 class="card-title">基本信息</h3>
                    </div>
                    <div class="card-body">
                        <div class="info-row">
                            <div class="info-label">
                                <i class="iconfont jpx-icon-user label-icon"></i>
                                <span>姓名</span>
                            </div>
                            <div class="info-value">{{ userInfo.personName || '-' }}</div>
                        </div>
                        <div class="info-row">
                            <div class="info-label">
                                <i class="iconfont jpx-icon-user label-icon"></i>
                                <span>性别</span>
                            </div>
                            <div class="info-value">{{ userInfo.sexName || getSexText(userInfo.sex) }}</div>
                        </div>
                        <div class="info-row">
                            <div class="info-label">
                                <fb-icon name="information-card" class="label-icon"></fb-icon>
                                <span>证件类型</span>
                            </div>
                            <div class="info-value">{{ userInfo.idtypeName || getIdTypeText(userInfo.idtype) }}</div>
                        </div>
                        <div class="info-row">
                            <div class="info-label">
                                <i class="iconfont jpx-icon-information-card label-icon"></i>
                                <span>证件号</span>
                            </div>
                            <div class="info-value">{{ userInfo.idcard || '-' }}</div>
                        </div>
                        <div class="info-row">
                            <div class="info-label">
                                <fb-icon name="calendar" class="label-icon"></fb-icon>
                                <span>出生日期</span>
                            </div>
                            <div class="info-value">{{ formatDate(userInfo.birthday) }}</div>
                        </div>
                        <div class="info-row">
                            <div class="info-label">
                                <fb-icon name="user-groups" class="label-icon"></fb-icon>
                                <span>民族</span>
                            </div>
                            <div class="info-value">{{
                                    userInfo.safeprinNationName || getNationText(userInfo.safeprinNation)
                                }}
                            </div>
                        </div>

                    </div>
                </div>

                <!-- 联系信息卡片 -->
                <div class="info-card">
                    <div class="card-header">
                        <div class="card-icon contact-info">
                            <i class="iconfont jpx-icon-phone"></i>
                        </div>
                        <h3 class="card-title">联系信息</h3>
                    </div>
                    <div class="card-body">
                        <div class="info-row">
                            <div class="info-label">
                                <i class="iconfont jpx-icon-mobilephone label-icon"></i>
                                <span>手机号</span>
                            </div>
                            <div class="info-value">{{ userInfo.phone || '-' }}</div>
                        </div>
                        <div class="info-row">
                            <div class="info-label">
                                <i class="iconfont jpx-icon-phone label-icon"></i>
                                <span>联系电话</span>
                            </div>
                            <div class="info-value">{{ userInfo.tel || '-' }}</div>
                        </div>
                        <div class="info-row">
                            <div class="info-label">
                                <i class="iconfont jpx-icon-mail label-icon"></i>
                                <span>邮箱</span>
                            </div>
                            <div class="info-value">{{ userInfo.email || '-' }}</div>
                        </div>
                        <!--            <div class="info-row full-width">-->
                        <!--              <div class="info-label">-->
                        <!--                <fb-icon name="environment" class="label-icon"></fb-icon>-->
                        <!--                <span>通信地址</span>-->
                        <!--              </div>-->
                        <!--              <div class="info-value address">{{ userInfo.maddress || '-' }}</div>-->
                        <!--            </div>-->
                    </div>
                </div>

                <!-- 工作信息卡片 -->
                <div class="info-card">
                    <div class="card-header">
                        <div class="card-icon work-info">
                            <i class="iconfont jpx-icon-work-notes"></i>
                        </div>
                        <h3 class="card-title">工作信息</h3>
                    </div>
                    <div class="card-body">
                        <div class="info-row">
                            <div class="info-label">
                                <i class="iconfont jpx-icon-home label-icon"></i>
                                <span>部门</span>
                            </div>
                            <div class="info-value">{{ userInfo.deptFullName || '-' }}</div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">
                                <i class="iconfont jpx-icon-work-parameter label-icon"></i>
                                <span>职位</span>
                            </div>
                            <div class="info-value">{{ userInfo.office || '-' }}</div>
                        </div>
                        <div class="info-row">
                            <div class="info-label">
                                <i class="iconfont jpx-icon-user-management label-icon"></i>
                                <span>人员编号</span>
                            </div>
                            <div class="info-value">{{ userInfo.personNo || '-' }}</div>
                        </div>
<!--                        <div class="info-row">
                            <div class="info-label">
                                <fb-icon name="calendar" class="label-icon"></fb-icon>
                                <span>参加工作时间</span>
                            </div>
                            <div class="info-value">{{ formatDate(userInfo.partWorkDate) }}</div>
                        </div>
                        <div class="info-row">
                            <div class="info-label">
                                <fb-icon name="crown" class="label-icon"></fb-icon>
                                <span>职称</span>
                            </div>
                            <div class="info-value">{{ getTitleText(userInfo.titleCode) }}</div>
                        </div>-->
                    </div>
                </div>

                <!-- 教育信息卡片 -->
<!--                <div class="info-card">-->
<!--                    <div class="card-header">-->
<!--                        <div class="card-icon education-info">-->
<!--                            <i class="iconfont jpx-icon-education"></i>-->
<!--                        </div>-->
<!--                        <h3 class="card-title">教育信息</h3>-->
<!--                    </div>-->
<!--                    <div class="card-body">-->
<!--                        <div class="info-row">-->
<!--                            <div class="info-label">-->
<!--                                <i class="iconfont jpx-icon-school label-icon"></i>-->
<!--                                <span>毕业学校</span>-->
<!--                            </div>-->
<!--                            <div class="info-value">{{ userInfo.school || '-' }}</div>-->
<!--                        </div>-->
<!--                        <div class="info-row">-->
<!--                            <div class="info-label">-->
<!--                                <i class="iconfont jpx-icon-education label-icon"></i>-->
<!--                                <span>所学专业</span>-->
<!--                            </div>-->
<!--                            <div class="info-value">{{ userInfo.sepcSubject || '-' }}</div>-->
<!--                        </div>-->
<!--                        <div class="info-row">-->
<!--                            <div class="info-label">-->
<!--                                <i class="iconfont jpx-icon-education label-icon"></i>-->
<!--                                <span>最高学历</span>-->
<!--                            </div>-->
<!--                            <div class="info-value">{{ getDiplomaText(userInfo.diplomaCode) }}</div>-->
<!--                        </div>-->
<!--                        <div class="info-row">-->
<!--                            <div class="info-label">-->
<!--                                <i class="iconfont jpx-icon-school label-icon"></i>-->
<!--                                <span>最高学位</span>-->
<!--                            </div>-->
<!--                            <div class="info-value">{{ userInfo.degree || '-' }}</div>-->
<!--                        </div>-->
<!--                    </div>-->
<!--                </div>-->

                <!-- 其他信息卡片 -->
                <div class="info-card full-width">
                    <div class="card-header">
                        <div class="card-icon other-info">
                            <i class="iconfont jpx-icon-user-management"></i>
                        </div>
                        <h3 class="card-title">其他信息</h3>
                    </div>
                    <div class="card-body">
                        <!-- <div class="info-row">
                          <div class="info-label">
                            <i class="iconfont jpx-icon-user-police label-icon"></i>
                            <span>执法证号</span>
                          </div>
                          <div class="info-value">{{ userInfo.checkcardNo || '-' }}</div>
                        </div> -->
                        <!-- <div class="info-row">
                          <div class="info-label">
                            <i class="iconfont jpx-icon-user-police label-icon"></i>
                            <span>执法证有效期</span>
                          </div>
                          <div class="info-value">{{ formatDate(userInfo.checkcardLimitdate) }}</div>
                        </div> -->
<!--                        <div class="info-row">
                            <div class="info-label">
                                <i class="iconfont jpx-icon-user-government label-icon"></i>
                                <span>政治面貌</span>
                            </div>
                            <div class="info-value">{{ getPoliticsText(userInfo.politicsCode) }}</div>
                        </div>-->
                        <div class="info-row full-width">
                            <div class="info-label">
                                <i class="iconfont jpx-icon-information label-icon"></i>
                                <span>个人简介</span>
                            </div>
                            <div class="info-value address">{{ userInfo.resume || '暂无个人简介' }}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 编辑弹框 -->
        <tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>


    </div>
</template>

<script>
export default {
    name: 'Profile',
    data () {
        return {

            file: {
                referType: 'SYS1014',
                fileList: [],
            },
            // 请求的 service
            service: this.$svc.sys.person,
            userInfo: {
                username: '',
                personName: '',
                personId: '',
                email: '',
                phone: '',
                tel: '',
                deptFullName: '',
                deptId: '',
                position: '',
                office: '',
                roleName: '',
                userRoles: [],
                avatar: '',
                profilePhoto: '',
                sex: '',
                sexName: '',
                idtype: '',
                idtypeName: '',
                idcard: '',
                birthday: '',
                safeprinNation: '',
                safeprinNationName: '',
                nativePlace: '',
                politicsCode: '',
                personNo: '',
                partWorkDate: '',
                titleCode: '',
                diplomaCode: '',
                degree: '',
                school: '',
                sepcSubject: '',
                checkcardNo: '',
                checkcardLimitdate: '',
                maddress: '',
                resume: '',
                actived: '',
                category: '',
                ascnId: '',
                ascnName: '',
                creator: '',
                createTime: '',
                updator: '',
                updateTime: '',
                tenantId: '',
                extend01: '',
                extend02: '',
                extend03: '',
                defaultDept: '',
                passKey: '',
                deptIds: '',
                deptFullNames: '',
            },
            defaultAvatar: '/assets/images/default-avatar.png',
        }
    },
    mounted () {
        this.loadUserInfo()
    },
    methods: {
        // 加载用户信息
        loadUserInfo () {
            // 获取当前登录用户信息
            let userInfo = app.$datax.get('userInfo') || {}
            let personId = userInfo.personId
            let deptId = userInfo.deptId || ''

            if (!personId) {
                this.$message.error('无法获取用户ID，请重新登录')
                return
            }

            // 调用person service的view方法获取用户基本信息
            this.service.view({
                'personId': personId,
                'deptId': deptId,
            }).then((result) => {
                // 判断code
                if (result.code == 1) {
                    // 合并数据而不是直接覆盖，保留预定义的userRoles数组
                    Object.assign(this.userInfo, result.data)
                    // 查询用户角色信息
                    this.loadUserRoles(personId, deptId)
                    // 查询用户扩展信息
                    this.loadUserExpInfo(personId)
                } else {
                    // 服务器返回失败
                    this.$message.error('获取用户信息失败: ' + result.message)
                }
            }).catch((err) => {
                // 服务器返回失败
                console.error('获取用户信息失败:', err)
                this.$message.error('获取用户信息失败')
            })
        },

        // 加载用户角色信息
        loadUserRoles (personId, deptId) {
            this.service.personRoles({
                'personId': personId,
                'deptId': deptId,
            }).then((result) => {
                if (result.code == 1 && result.data && result.data.length > 0) {
                    // 保存所有角色信息用于标签显示
                    this.userInfo.userRoles = result.data
                    // 保留第一个角色名称用于兼容性
                    this.userInfo.roleName = result.data[0].roleName
                }
            }).catch((err) => {
                console.error('获取用户角色失败:', err)
            })
        },

        // 加载用户扩展信息
        loadUserExpInfo (personId) {
            this.service.expView({'personId': personId}).then((result) => {
                if (result.code == 1 && result.data) {
                    // 合并扩展信息到userInfo中
                    Object.assign(this.userInfo, result.data)
                }
            }).catch((err) => {
                console.error('获取用户扩展信息失败:', err)
            })
        },

        // 上传头像
        uploadAvatar () {
            // 创建文件输入元素
            const input = document.createElement('input')
            input.type = 'file'
            input.accept = 'image/*'
            input.onchange = (e) => {
                const file = e.target.files[0]
                if (file) {
                    this.handleAvatarUpload(file)
                }
            }
            input.click()
        },

        // 处理头像上传
        async handleAvatarUpload (file) {
            const formData = new FormData()
            formData.append('file', file)

            try {
                const response = await app.api.post('/sys/user/upload-avatar', formData)
                if (response.success) {
                    this.userInfo.avatar = response.data.url
                    this.$message.success('头像更新成功')
                }
            } catch (error) {
                this.$message.error('头像上传失败')
            }
        },

        // 编辑个人信息
        editProfile () {
            let param = {'userInfo': this.userInfo}
            let options = {
                'height': 600,
                'width': 900,
            }

            this.$refs.TpDialog.show(import('./profile_edit.vue'), param, '编辑个人信息', options)
        },

        // 处理保存成功
        handleSaveSuccess (data) {
            this.loadUserInfo() // 重新加载用户信息
        },

        // 处理弹框关闭事件
        closeDialog (param) {
            // 如果有参数传递，说明是保存成功后关闭，需要刷新数据
            if (param) {
                this.loadUserInfo()
            }
        },

        // 格式化日期
        formatDate (dateStr) {
            if (!dateStr) return ''
            if (dateStr.length === 8) {
                // YYYYMMDD格式转换为YYYY-MM-DD
                return `${dateStr.substring(0, 4)}-${dateStr.substring(4, 6)}-${dateStr.substring(6, 8)}`
            }
            return dateStr
        },

        // 获取性别文本
        getSexText (sex) {
            const sexMap = {
                '1': '男',
                '2': '女',
            }
            return sexMap[sex] || sex
        },

        // 获取证件类型文本
        getIdTypeText (idtype) {
            const idTypeMap = {
                'Y2401': '身份证',
                'Y2402': '护照',
                'Y2403': '军官证',
                'Y2404': '其他',
            }
            return idTypeMap[idtype] || idtype
        },

        // 获取民族文本
        getNationText (nation) {
            // 这里可以根据实际的字典数据进行映射
            return nation
        },

        // 获取政治面貌文本
        getPoliticsText (politics) {
            const politicsMap = {
                'SYS1301': '中共党员',
                'SYS1302': '中共预备党员',
                'SYS1303': '共青团员',
                'SYS1304': '民革党员',
                'SYS1305': '民盟盟员',
                'SYS1306': '民建会员',
                'SYS1307': '民进会员',
                'SYS1308': '农工党党员',
                'SYS1309': '致公党党员',
                'SYS1310': '九三学社社员',
                'SYS1311': '台盟盟员',
                'SYS1312': '无党派人士',
                'SYS1313': '群众',
            }
            return politicsMap[politics] || politics
        },

        // 获取职称文本
        getTitleText (title) {
            const titleMap = {
                'SYS1201': '正高级',
                'SYS1202': '副高级',
                'SYS1203': '中级',
                'SYS1204': '初级',
                'SYS1205': '员级',
            }
            return titleMap[title] || title
        },

        // 获取学历文本
        getDiplomaText (diploma) {
            const diplomaMap = {
                'SYS1401': '博士研究生',
                'SYS1402': '硕士研究生',
                'SYS1403': '大学本科',
                'SYS1404': '大学专科',
                'SYS1405': '中等专业学校',
                'SYS1406': '技工学校',
                'SYS1407': '高中',
                'SYS1408': '初中',
                'SYS1409': '小学',
                'SYS1410': '文盲或半文盲',
                'SYS1411': '其他',
            }
            return diplomaMap[diploma] || diploma
        },

    },
}
</script>

<style scoped lang="less">
@import "./profile.less";
</style>