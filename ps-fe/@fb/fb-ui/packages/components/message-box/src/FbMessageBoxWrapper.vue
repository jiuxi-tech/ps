<template>
    <div v-if="show"
         :class="[
          `${prefix}-message-box-wrapper`,
          { lock: lock }
		    ]">
        <slot />
    </div>
</template>

<script>

import { prefix } from '../../../../src/config'

/**
 * FbMessageBoxWrapper
 * (c) 2020 lincong1987
 */
export default {
    name: 'FbMessageBoxWrapper',
    props: {
        // 锁定
        lock: {
            type: Boolean,
            default: true,
        },

    },

    data () {

        // let {defaultProps: {FbMessageBoxWrapper: defaultProps}} = this

        return {
            prefix,
            show: true,
        }
    },
    computed: {},
    methods: {
        hide () {
            // this.show = false

			// 触发单独 vue 示例 hide 进行销毁
			this.$parent.hide('hide')
        },
    },
}
</script>

<style scoped>

</style>
