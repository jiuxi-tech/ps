<template>
    <fb-col :span="rowType === 'row-12' ? 12 : 24" v-if="col && col.type && col.formItemProps.show">
        <fb-fieldset
            v-if="col.type === 'fb-fieldset'"
            :label="col.props.label"></fb-fieldset>

        <fb-property-item
            v-if="col.type !== 'fb-fieldset'"
            :label="col.formItemProps.label"
            :label-width="col.formItemProps.labelWidth"
        >
            <template
                v-if="['fb-input','fb-input-number','fb-textarea','fb-select','fb-tree-select','fb-radio-group','fb-checkbox-group', 'fb-checkbox', 'tp-datepicker'].includes(col.type) ">
                {{
                    (() => {
                        if (typeof formData.label[col.formItemProps.name] !== 'undefined') {
                            return formData.label[col.formItemProps.name]
                        } else if (typeof col.props.value !== 'undefined') {
                            return col.props.value
                        } else {
                            return '--'
                        }
                    })()
                }}
            </template>

            <template v-if="col.type === 'tp-text'">
                {{ formData.label[col.formItemProps.name] }}
            </template>

            <template v-if="col.type === 'tp-upload'">
                <tp-upload
                    :view="col.props.view"
                    :service="getService(col.props.service) || $svc.sys.file"
                    :param="arrayToObject(uniqAndCleanArray(col.props.param))"
                    :referid="param.id"
                    :accept="col.props.accept"
                    :max-length="col.props.maxLength"
                    :button-text="col.props.buttonText"
                    :button-icon="col.props.buttonIcon"
                    :show-preview="col.props.showPreview"
                    :show-remove="col.props.showRemove"
                    :show-download="col.props.showDownload"
                    readonly
                    :value="(()=>{
                        if (typeof formData.value[col.formItemProps.name] !== 'undefined') {
                          if (formData.value[col.formItemProps.name].fileList) {
                            return formData.value[col.formItemProps.name].fileList
                          }
                          return []
                        }else if (typeof col.props.value !== 'undefined') {
                          return col.props.value || []
                        } else {
                          return []
                        }
                      })()"
                ></tp-upload>
            </template>
            <template v-if="col.type === 'fb-editor'">
                <fb-container v-html="$xss(formData.label[col.formItemProps.name])"></fb-container>
            </template>

            <template v-if="col.type === 'biz-ent-person-single-select'">
                {{ formData.label[col.formItemProps.name] }}
            </template>

            <template v-if="col.type === 'biz-ent-person-multiple-select'">
                {{ formData.label[col.formItemProps.name] }}
            </template>

            <template v-if="col.type === 	'biz-ent-person-single-select-using-table-show'">
                {{ formData.label[col.formItemProps.name] }}
            </template>

            <template v-if="col.type === 'biz-ent-person-multiple-select-using-table-show'">
                {{ formData.label[col.formItemProps.name] }}
            </template>

            <template v-if="col.type === 'biz-org-person-single-select'">
                {{ formData.label[col.formItemProps.name] }}
            </template>

            <template v-if="col.type === 'biz-org-person-multiple-select'">
                {{ formData.label[col.formItemProps.name] }}
            </template>

            <template v-if="col.type === 'biz-org-ent-single-select'">
                {{ formData.label[col.formItemProps.name] }}
            </template>

            <template v-if="col.type === 'biz-org-ent-multiple-select'">
                {{ formData.label[col.formItemProps.name] }}
            </template>
            <template
                v-if="['biz-current-ctx', 'biz-current-person', 'biz-current-dept',  'biz-current-org', 'biz-current-date-time'].includes(col.type)">
                {{ formData.label[col.formItemProps.name] }}
            </template>
        </fb-property-item>


    </fb-col>
</template>

<script>
import { closest } from '../../../../../util/componentUtils'
import { get } from 'lodash-es'
import { arrayToObject, uniqAndCleanArray } from '../../../../../views/sys/custom/form/form-editor/utils/utils'

/**
 * FbFormEditorColViewRun
 * (c) 2021 lincong1987
 */

export default {
    name: 'FbFormEditorColViewRun',

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

        rowType: {
            type: String,
        },

        activeComponent: {
            type: [Object],
        },
        formData: {
            type: [Object],
        },
        param: {
            type: Object,
            require: false,
            default () {
                return {}
            },
        },
    },

    data () {
        return {
            FbFormRuntimeView: closest(this, 'FbFormRuntimeView'),
        }
    },

    methods: {

        uniqAndCleanArray,

        arrayToObject,

        getService (servicePath) {
            let svc = get(app.$svc, servicePath)
            return svc ? svc : null
        },

    },

}
</script>

<style lang="less" scoped>

</style>
