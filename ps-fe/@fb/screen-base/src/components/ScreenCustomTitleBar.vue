<template>
    <fb-container class="screen-custom-title-bar">
        <template v-for="(item, index) in data">
            <fb-container @on-click="handleClick(item)" class="screen-custom-title-bar-item"
                          :class="[{'screen-custom-title-bar-item_click': $listeners['on-click']}]"
                          mb="10px">
                <fb-container class="screen-custom-title-bar-title">{{ item.title }}</fb-container>
                <fb-container>
                    <fb-progress class="progress-custom-border" status="active"
                                 :percent="item.percent"
                                 :border-radius="10"
                                 :stroke-color="item.strokeColor ? item.strokeColor :`linear-gradient(270deg, #00DA4F 0%, #2873F9 100%)`"
                                 :stroke-width="12" stroke-bg-color="transparent">
                            <span slot="text" style="color: #fff">
                                <fb-number :end="item.value"></fb-number>
                            </span>
                    </fb-progress>
                </fb-container>
            </fb-container>
        </template>
    </fb-container>
</template>


<script>
    export default {
        name: 'ScreenCustomTitleBar',
        props: {
            data: {
                type: [Array],
                default: () => []
            },
            current: {
                type: [String],
                default: ''
            },
            span: {
                type: [String, Number],
                default: 12
            }
        },
        methods: {
            handleClick(item) {
                this.$emit('on-click', item)
            }
        }
    }
</script>


<style lang="less" scoped>
@import "../assets/styles/components/screeen-custom-title-bar.less";

</style>
