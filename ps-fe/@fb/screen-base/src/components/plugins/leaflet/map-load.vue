<template>
    <!--  地图瓦片类型切换  -->
    <div class="jpx-map-load" :class="{show: isShow}">
        <div class="jpx-map-load-center">
            <div class="text">{{ loadText }}</div>
            <fb-progress :percent="percent" text-inside stroke-width="20" stroke-bg-color="#ccc" style="width: 200px"></fb-progress>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            loadText: {
                type: [String],
                default: ''
            },
            percent: {
                default: 0
            }
        },
        data() {
            return {
                isShow: false,
            }
        },
        methods: {
            show() {
                this.isShow = true
            },
            hide() {
                this.isShow = false
            }
        }
    }
</script>

<style scoped lang="less">

</style>