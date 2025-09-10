<template>
    <fb-row class="ranking-bar-list" gutter="10">
        <template v-for="(item, index) in data">
            <fb-col :span="span">
                <fb-tooltip placement="top" :disabled="!$slots.tooltip">
                    <template v-if="$slots.tooltip" v-slot:content>
                        <slot name="tooltip"></slot>
                    </template>
                    <fb-container @on-click="handleClick(item)" class="ranking-list-item"
                                  :class="[{active: current === item.label, 'ranking-list-item_click': $listeners['on-click']}]"
                                  mb="10px" display="flex" height="28px">
						<fb-container class="ranking-list-label" width="50%" :title="item.label">{{ item.label }}</fb-container>
                        <fb-container width="50%" mt="-3px">
                            <fb-progress class="progress-custom-border" status="active"
                                         :percent="item.percent"
                                         :border-radius="2"
                                         :stroke-color="item.strokeColor ? item.strokeColor :`linear-gradient(270deg, #6151F0 0%, #4179FB 100%)`"
                                         text-inside :stroke-width="22" stroke-bg-color="rgba(6, 45, 139, 0.4)">
                                    <span slot="text">
                                        <fb-number :end="item.value"></fb-number>
                                    </span>
                            </fb-progress>
                        </fb-container>
                    </fb-container>
                </fb-tooltip>
            </fb-col>
        </template>
    </fb-row>
</template>


<script>
    export default {
        name: 'ScreenRankBar',
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
	@import "../assets/styles/components/screen-rank-bar.less";
</style>
