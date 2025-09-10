<template>
    <div>
        <fb-flex gap="8px" direction-column class="tab-list">
            <!-- 顶部统计卡片 -->
            <fb-flex grid cols3 gap="8px">
                <div class="card-banner blue">
                    <div class="icon">
                        <fb-icon size="32" color="#fff" name="user-fill"></fb-icon>
                    </div>
                    <div class="icon-right">
                        <fb-icon size="60" color="#6773F426" name="user-fill"></fb-icon>
                    </div>
                    <div class="icon-arrow"></div>
                    <div class="title">用户总数</div>
                    <div class="value">
                        <span v-if="userStats.totalUsers > 0" class="num clickable" @click="clickUsers({})">
                           {{ userStats.totalUsers }}
                        </span>
                        <span v-else class="num">
                           {{ userStats.totalUsers }}
                        </span>
                        <span class="unit">人</span>
                    </div>
                </div>
                <div class="card-banner orange">
                    <div class="icon">
                        <fb-icon size="32" color="#fff" name="team-fill"></fb-icon>
                    </div>
                    <div class="icon-right">
                        <fb-icon size="60" color="#FC9C3B26" name="team-fill"></fb-icon>
                    </div>
                    <div class="icon-arrow"></div>
                    <div class="title">部门总数</div>
                    <div class="value">
                        <span v-if="userStats.totalDepts > 0" class="num clickable" @click="clickDepts({})">
                           {{ userStats.totalDepts }}
                        </span>
                        <span v-else class="num">
                           {{ userStats.totalDepts }}
                        </span>
                        <span class="unit">个</span>
                    </div>
                </div>
                <div class="card-banner purple">
                    <div class="icon">
                        <fb-icon size="32" color="#fff" name="shield-user-fill"></fb-icon>
                    </div>
                    <div class="icon-right">
                        <fb-icon size="60" color="#6773F426" name="shield-user-fill"></fb-icon>
                    </div>
                    <div class="icon-arrow"></div>
                    <div class="title">角色总数</div>
                    <div class="value">
                        <span v-if="userStats.totalRoles > 0" class="num clickable" @click="clickRoles({})">
                           {{ userStats.totalRoles }}
                        </span>
                        <span v-else class="num">
                           {{ userStats.totalRoles }}
                        </span>
                        <span class="unit">个</span>
                    </div>
                </div>
            </fb-flex>

            <!-- 详细统计卡片 -->
            <fb-flex grid cols3 gap="8px">
                <div class="card-wrapper">
                    <div class="header">
                        <fb-icon size="19" color="#0284FE" name="user-fill"></fb-icon>
                        <span class="title">用户状态分布</span>
                    </div>
                    <div class="body">
                        <fb-flex grid cols2 gap="4px">
                            <fb-flex class="card-board blue">
                                <span class="title fs-14">活跃用户</span>
                                <span v-if="userStats.activeUsers > 0" class="value clickable"
                                      @click="clickUsers({'status':'active'})">
                                   {{ userStats.activeUsers }}
                                </span>
                                <span v-else class="value">{{ userStats.activeUsers }}</span>
                            </fb-flex>
                            <fb-flex class="card-board orange">
                                <span class="title fs-14">禁用用户</span>
                                <span v-if="userStats.inactiveUsers > 0" class="value clickable"
                                      @click="clickUsers({'status':'inactive'})">
                                   {{ userStats.inactiveUsers }}
                                </span>
                                <span v-else class="value">{{ userStats.inactiveUsers }}</span>
                            </fb-flex>
                        </fb-flex>
                    </div>
                </div>
                <div class="card-wrapper">
                    <div class="header">
                        <fb-icon size="19" color="#0284FE" name="team-fill"></fb-icon>
                        <span class="title">部门类型分布</span>
                    </div>
                    <div class="body">
                        <fb-flex grid cols2 gap="4px">
                            <fb-flex class="card-board blue">
                                <span class="title">企业部门</span>
                                <span class="value"> {{ userStats.entDepts }}</span>
                            </fb-flex>
                            <fb-flex class="card-board purple">
                                <span class="title">组织部门</span>
                                <span class="value"> {{ userStats.orgDepts }}</span>
                            </fb-flex>
                        </fb-flex>
                    </div>
                </div>
                <div class="card-wrapper">
                    <div class="header">
                        <fb-icon size="19" color="#0284FE" name="shield-user-fill"></fb-icon>
                        <span class="title">角色权限分布</span>
                    </div>
                    <div class="body">
                        <fb-flex grid cols2 gap="4px">
                            <fb-flex class="card-board purple">
                                <span class="title fs-14">系统角色</span>
                                <span v-if="userStats.systemRoles > 0" class="value clickable"
                                      @click="clickRoles({'type':'system'})">
                                 {{ userStats.systemRoles }}
                               </span>
                                <span v-else class="value">
                                {{ userStats.systemRoles }}
                               </span>
                            </fb-flex>
                            <fb-flex class="card-board blue">
                                <span class="title fs-14">业务角色</span>
                                <span v-if="userStats.businessRoles > 0" class="value clickable"
                                      @click="clickRoles({'type':'business'})">
                                 {{ userStats.businessRoles }}
                               </span>
                                <span v-else class="value">
                                {{ userStats.businessRoles }}
                               </span>
                            </fb-flex>
                        </fb-flex>
                    </div>
                </div>
            </fb-flex>

            <!-- 快捷操作区域 -->
            <div class="card-wrapper">
                <div class="header">
                    <fb-icon size="19" color="#0284FE" name="settings-fill"></fb-icon>
                    <span class="title">快捷操作</span>
                </div>
                <div class="body">
                    <fb-flex grid cols4 gap="12px">
                        <fb-button type="primary" @click="goToUserManage">
                            <fb-icon name="user-add-fill" mr="4px"></fb-icon>
                            用户管理
                        </fb-button>
                        <fb-button type="primary" @click="goToDeptManage">
                            <fb-icon name="team-fill" mr="4px"></fb-icon>
                            部门管理
                        </fb-button>
                        <fb-button type="primary" @click="goToRoleManage">
                            <fb-icon name="shield-user-fill" mr="4px"></fb-icon>
                            角色管理
                        </fb-button>
                        <fb-button type="primary" @click="goToMenuManage">
                            <fb-icon name="menu-fill" mr="4px"></fb-icon>
                            菜单管理
                        </fb-button>
                    </fb-flex>
                </div>
            </div>
        </fb-flex>

        <!-- 弹窗组件 -->
        <tp-dialog ref="TpDialog"></tp-dialog>
    </div>
</template>

<script>
import app from '@fb/fb-core'

export default {
    name: 'UserHome',
    data() {
        return {
            // 用户统计数据
            userStats: {
                totalUsers: 0,
                totalDepts: 0,
                totalRoles: 0,
                activeUsers: 0,
                inactiveUsers: 0,
                entDepts: 0,
                orgDepts: 0,
                systemRoles: 0,
                businessRoles: 0
            },
            // 查询参数
            formData: {
                startDate: app.$dayjs().subtract(30, 'day').format('YYYYMMDD'),
                endDate: app.$dayjs().format('YYYYMMDD')
            }
        }
    },
    mounted() {
        this.loadUserStats()
    },
    methods: {
        /**
         * 加载用户统计数据
         */
        loadUserStats() {
            // 模拟数据，实际项目中应该调用API
            this.userStats = {
                totalUsers: 156,
                totalDepts: 23,
                totalRoles: 12,
                activeUsers: 142,
                inactiveUsers: 14,
                entDepts: 15,
                orgDepts: 8,
                systemRoles: 5,
                businessRoles: 7
            }
            
            // 实际API调用示例
            // app.$svc.service.get('/sys/user/stats', this.formData)
            //     .then((result) => {
            //         if (result.code == 1) {
            //             this.userStats = result.data
            //         } else {
            //             this.$message.error('查询失败: ' + result.message)
            //         }
            //     })
        },
        
        /**
         * 点击用户统计
         */
        clickUsers(params) {
            const routeParams = {
                ...params,
                startDate: this.formData.startDate,
                endDate: this.formData.endDate
            }
            this.$router.push({
                path: '/sys/person/org/list',
                query: routeParams
            })
        },
        
        /**
         * 点击部门统计
         */
        clickDepts(params) {
            const routeParams = {
                ...params,
                startDate: this.formData.startDate,
                endDate: this.formData.endDate
            }
            this.$router.push({
                path: '/sys/dept/org/tree',
                query: routeParams
            })
        },
        
        /**
         * 点击角色统计
         */
        clickRoles(params) {
            const routeParams = {
                ...params,
                startDate: this.formData.startDate,
                endDate: this.formData.endDate
            }
            this.$router.push({
                path: '/sys/role/org/list',
                query: routeParams
            })
        },
        
        /**
         * 快捷操作 - 用户管理
         */
        goToUserManage() {
            this.$router.push('/sys/person/org/list')
        },
        
        /**
         * 快捷操作 - 部门管理
         */
        goToDeptManage() {
            this.$router.push('/sys/dept/org/tree')
        },
        
        /**
         * 快捷操作 - 角色管理
         */
        goToRoleManage() {
            this.$router.push('/sys/role/org/list')
        },
        
        /**
         * 快捷操作 - 菜单管理
         */
        goToMenuManage() {
            this.$router.push('/sys/menu/tree')
        }
    }
}
</script>

<style lang="less" scoped>
@import "./index.less";
</style>