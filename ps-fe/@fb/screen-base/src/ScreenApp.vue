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

    import ScreenShellToolbar from './components/ScreenShellToolbar'
    /**
     * ScreenApp
     * (c) 2021 lincong1987
     */

    export default {
        name: 'ScreenApp',
        components: {ScreenShellToolbar},
        data() {
            return {
                routerViewTransition: 'slide-to-down',
            }
        },

        created() {

        },

        mounted() {
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
    //@import "assets/styles/main";
</style>
