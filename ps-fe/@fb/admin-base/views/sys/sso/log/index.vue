<template>
    <fb-flex direction-column gap="8px" style="  padding: 8px; ">

        <fb-flex style=" background: #FFFFFF;
    border-radius: 4px;
    padding: 0 8px;">
            <fb-tabs :data="tabs" v-model="currentComponent"> </fb-tabs>
        </fb-flex>
        <fb-flex style="height: 100%;  ">
            <component if="currentComponent" :is="currentComponent" />
        </fb-flex>
        <!-- -->
    </fb-flex>
</template>

<script>
import UserLogList from './user/list.vue'
import AdminLogList from './admin/list.vue'

export default {
    name: 'SsoLogIndex',
    components: {
        UserLogList,
        AdminLogList,
    },
    data() {
        return {
            currentComponent: 'UserLogList',
            tabs: [
                {
                    value: 'UserLogList',
                    label: '用户日志',
                    icon: 'user'
                },
                {
                    value: 'AdminLogList',
                    label: '管理员日志',
                    icon: 'admin'
                }
            ]
        }
    },
    computed: {

    },
    methods: {
        closeDialog() {
            debugger
         //   this.parentPage.closeDialog()
        }
    }
}
</script>

<style lang="less" scoped>
.sso-log-page {
    height: 100%;

    :deep(.fb-tabs) {
        height: 100%;

        .fb-tabs-content {
            height: calc(100% - 50px);
            overflow: hidden;
        }
    }
}
</style>