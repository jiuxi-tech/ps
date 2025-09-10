<template>
    <fb-container mt="4px">
        <fb-simple-table
            size="s"
            bordered
            :wrapper-style="{
                borderRadius: '4px 4px 0 0',
                borderBottom: 0
            }"
            :rownum="false"
            :no-pager="true"
            :no-empty="true"
            :columns="[
				 {
					 label:myReader.labelName,
					 name: myReader.label,
					 slot: 'label'
				 },
				 {
					 label:myReader.valueName,
					 name: myReader.value,
					 slot: 'value'
				 },
				 {
					 name: '-',
					 slot: 'op',
					 width: 30
				 },
			 ]"

            :data="col.props[myReader.data]"
            :auto-pk="false"
            pk="rownum"
        >

            <template v-slot:label="props">
                <fb-input
                    size="s"
                    :value="col.props[myReader.data][props.rowIndex][myReader.label]"
                    @input="(value)=>{updateCol(value, props.rowIndex, 'props', myReader.label)}"></fb-input>

            </template>
            <template v-slot:value="props">

                <template v-if="isBoolean(col.props[myReader.data][props.rowIndex][myReader.value])">
                <fb-checkbox
                    size="s"
                    :value="col.props[myReader.data][props.rowIndex][myReader.value]"
                    @input="(value)=>{updateCol(value, props.rowIndex, 'props', myReader.value)}"></fb-checkbox>
                </template>
                <template v-else>
                    <fb-input
                        size="s"
                        :value="col.props[myReader.data][props.rowIndex][myReader.value]"
                        @input="(value)=>{updateCol(value, props.rowIndex, 'props', myReader.value)}"></fb-input>
                </template>

            </template>
            <template v-slot:op="props">
                <fb-icon
                    name="delete"
                    @on-click="propSplice('props', myReader.data, props.rowIndex, 1)"
                />
            </template>
        </fb-simple-table>

        <fb-container padding="6px 8px" style="border: 1px solid #D3D3D3; border-top: 0" radius="0 0 4px 4px">
            <fb-button size="s" icon="add-circle"
                       @on-click="propPush">
                添加
            </fb-button>
        </fb-container>

    </fb-container>
</template>

<script>
import { closest } from '@/util/componentUtils'
import { isBoolean } from 'lodash-es'

/**
 * FbFormEditorObjectEditor
 * (c) 2021 lincong1987
 */

export default {
    name: 'FbFormEditorObjectEditor',
    props: {
        col: {
            type: [Object],
        },

        rowIndex: {
            type: Number,
        },

        colIndex: {
            type: Number,
        },

        viewType: {
            type: String,
        },

        reader: {

            type: [Object],
            default () {
                return {}
            },
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
            myReader: this.mergeReader(this.reader),
        }
    },

    watch: {
        reader (value) {
            this.myReader = this.mergeReader(value)
        },
    },

    methods: {

        isBoolean,
        mergeReader (reader) {

            return Object.assign({}, {
                label: 'label',
                labelName: '参数名',
                value: 'value',
                valueName: '值',
                data: 'data',
            }, reader)
        },

        propSplice (prop, propName, index, length) {

            let {
                viewType,
            } = this

            this.formEditor.propSplice(viewType, prop, propName, index, length)

        },

        propPush (prop, propName, value) {
            let {
                viewType,
                activeComponent,
                myReader,
            } = this
            let obj = {}
            obj[myReader.label] = ''
            obj[myReader.value] = ''

            this.$nextTick(() => {
                this.formEditor.propPush(viewType, 'props', myReader.data, obj)
            })

        },

        updateCol (value, dataIndex, prop, propName) {

            let {
                viewType,
                activeComponent,
                myReader,
            } = this

            this.formEditor.updateCol(viewType, value, prop, propName, (obj) => {
                return obj[myReader.data][dataIndex]
            })

        },

        getComponentByType (type) {
            return this.formEditor.getComponentByType(type)
        },

        getComponentNameByType (type) {
            return this.formEditor.getComponentNameByType(type)
        },
    },
}
</script>

<style lang="less" scoped>

</style>
