<template>
    <div class="editor-row__toolbar">

        <div class="editor-row__title">{{ rowIndex + 1 }}、{{ rowType === 'row-12' ? '双列布局' : '单列布局' }}</div>

        <div class="editor-row__up" v-if="rowIndex !== 0">
            <fb-button size="s" round icon="up-circle-fill"
                       :color="colors.white"
                       @on-click="formEditor.moveRow(viewType, 'up', rowIndex)"
            >上移
            </fb-button>
        </div>
        <div class="editor-row__down">
            <fb-button size="s" round icon="down-circle-fill"
                       :color="colors.white"
                       @on-click="formEditor.moveRow(viewType, 'down', rowIndex)"
            >
                下移
            </fb-button>
        </div>
        <div class="editor-row__remove">
            <fb-button size="s" round danger icon="reduce-circle-fill"
                       :color="colors.white"
                       @on-click="formEditor.removeRow(viewType, rowIndex)">
                删除行
            </fb-button>
        </div>
    </div>
</template>

<script>
import { closest } from '@/util/componentUtils'

/**
 * FbFormEditorRowToolbar
 * (c) 2021 lincong1987
 */

export default {
    name: 'FbFormEditorRowToolbar',

    props: {
        rowIndex: {
            type: Number,
        },

        viewType: {
            type: String,
        },

        rowType: {
            type: String,
        },
    },
	created () {
		this.formEditor = closest(this, 'FbFormEditor')
	},
	beforeDestroy () {
		this.formEditor = null
	},
    data () {
        return {
        }
    },

    methods: {},
}
</script>

<style lang="less" scoped>

</style>
