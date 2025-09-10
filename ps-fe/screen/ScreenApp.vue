<template>
    <div id="app"
         ref="screenShell"
         class="screen-shell">

        <!--    mode="out-in" 解决代码热更新bug   -->
        <transition :name="routerViewTransition" mode="out-in">
            <router-view :key="$route.fullPath"/>
        </transition>

        <screen-shell-toolbar @on-change="handleToolbarChange"/>
    </div>
</template>

<script>

    /**
     * ScreenApp
     * (c) 2021 lincong1987
     */

    export default {
        name: 'ScreenApp',
        components: {},
        data() {
            return {
                routerViewTransition: 'slide-to-down',
                themeClass: ''
            }
        },

        created() {


        },

        mounted() {

            // app.service.post('/qyaqfxpc/cbienterpriseinfo/listForGov', {}).then((res) => {
            //     console.log(11111111111111111, res)
            // })
        },

        methods: {
            handleToolbarChange (prop, value) {
                if (prop === 'transition') {
                    this.routerViewTransition = value
                }
            },
        },

    }
</script>

<!-- 全局样式 -->
<style lang="less">
</style>
