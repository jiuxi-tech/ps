<template>
    <fb-container class="screen-panel-tab"
                  v-bind="$attrs"
    >
        <fb-row gutter="4">
            <fb-col v-for="(item, index) in data" :key="index"  :style="{width: `${100 / data.length}%`}">
                <div class="screen-panel-tab-item" :class="[{active: value === item.value, 'no-icon': noIcon}]" @click="handleClick(item)">
                    <fb-text bold class="title">{{ item.label }}</fb-text>
                </div>
            </fb-col>
        </fb-row>
    </fb-container>
</template>

<script>
    /**
     * ScreenPanelTab
     * (c) 2021 lincong1987
     */

    export default {
        name: 'ScreenPanelTab',

        props: {
            data: {
                type: [Array],
                default: () => []
            },
            value: {
                type: [String, Number],
                default: '1'
            },
			noIcon: {
            	type: [Boolean],
				default: false
			}
        },
        watch: {
            value(val) {
                this.$emit('input', val)
                this.$emit('on-change', val)
            }
        },
        methods: {
            handleClick(item) {
                this.$emit('input', item.value)
                this.$emit('on-click', item)
            }
        }
    }
</script>

<style lang="less" scoped>

    @import "../assets/styles/components/screen-panel-tab.less";

</style>
