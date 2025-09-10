<template>
	<form :class="getClass" :autocomplete="autocomplete"
	      @submit="handleSubmit">

		<div v-if="caption" :class="`${prefix}-form__caption`">
			{{ caption }}
		</div>
		<slot></slot>
	</form>
</template>

<script>

import { prefix } from '../../../../src/config'

/**
 * FbForm
 * (c) 2020 lincong1987
 */

import { each } from 'lodash-es'

export default {
	name: 'FbForm',

	// 让内部的组件都能访问
	provide () {
		return {
			fbForm: this,
			form: this,
		}
	},

	props: {
		// 表单数据对象
		model: {
			type: Object,
		},
		// 标签显示位置 left、right、top
		labelPosition: {
			type: String,
			default: 'right',
		},
		labelWidth: {
			type: [Number, String],
			default: 120,
		},
		rule: {
			type: [Object, Array],
			default: undefined,
		},

		// 模式，一般的表单normal, 查询表单query
		// query 模式不校验
		mode: {
			type: String,
			default: 'normal', // normal、query
		},
		// 显示校验信息
		showMessage: {
			type: Boolean,
			default: true,
		},
		// 自动完成
		autocomplete: {
			type: String,
			default: 'off',
		},
		// 表单标题
		caption: {
			type: [String, Array, Object],
			default: undefined,
		},

		labelStyle: {
			type: Object,
			default: null,
		},

		contentStyle: {
			type: Object,
			default: null,
		},

	},

	data () {
		return {

			prefix,
		}
	},

	computed: {
		getClass () {
			const arr = [`${this.prefix}-form`]
			arr.push(`${this.prefix}-form--mode-${this.mode}`)
			return arr
		},
		currentShowMessage () {
			if (this.mode === 'query') {
				return false
			}
			return this.showMessage
		},
	},

	created () {
		this.validateFormItems = []
	},

	mounted () {
		this.$on('on-item-validate', (errors) => {
			this.$emit('on-validate', errors)
		})
	},

	beforeDestroy () {
		this.$off('on-item-validate')
		this.validateFormItems = []
	},

	methods: {

		addItem (item) {
			if (this.validateFormItems.indexOf(item) === -1) {
				// if (item.prop && item.currentRule.length > 0) {
				// 	this.validateFormItems.push(item)
				// 	// console.log('[FB-UI] fb-form 添加了新的校验项', item.prop)
				// }

				// 追加有 prop 的子项，重置的时候用
				if (item.prop) {
					this.validateFormItems.push(item)
					// console.log('[FB-UI] fb-form 添加了新的校验项', item.prop)
				}
			}
		},
		removeItem (item) {
			const index = this.validateFormItems.indexOf(item)
			if (index !== -1) {
				this.validateFormItems.splice(index, 1)
				// console.log('[FB-UI] fb-form 移除了校验项', item.prop)
			}
		},
		validate (callback) {

			// console.log('[FB-UI] fb-form 开始校验表单')
			let valid = true
			const errorArr = []

			each(this.validateFormItems, (formItem) => {
				formItem.validate('', (errors, i) => {
					if (errors) {
						valid = false
						errorArr.push(errors)
					}
				})
			})

			if (typeof callback === 'function') {
				callback.apply(this, [valid, errorArr])
			} else {
				return new Promise((resolve, reject) => {
					if (valid) {
						resolve(valid)
					} else {
						reject(valid, errorArr)
					}
				})
			}

		},
		/**
		 * 校验表单中的某个字段
		 * @param filed
		 * @param callback
		 */
		validateField (filed, callback) {
			const len = this.validateFormItems.length
			let valid = true
			let errors = []
			for (let i = 0; i < len; i++) {
				const formItem = this.validateFormItems[i]
				if (filed && formItem.prop === filed) {
					// 第一个参数为”“表示校验所有规则
					formItem.validate('', (_errors) => {
						if (_errors) {
							valid = false
							errors = _errors
						}
						if (typeof callback === 'function') {
							callback(valid, _errors)
						}
					})
				}
			}

			return new Promise((resolve, reject) => {
				if (valid) {
					resolve(valid)
				} else {
					reject(valid, errors)
				}
			})

		},
		/**
		 * 重置
		 * @param filed
		 */
		resetFields (filed) {
			this.validateFormItems.forEach((item) => {
				if (!filed) {
					item.resetField()
				} else if (item.prop === filed) {
					item.resetField()
				}
			})
		},
		/**
		 * 处理提交事件
		 */
		handleSubmit () {
			this.validate((valid, errorArr) => {
				this.$emit('on-submit', valid, errorArr)
			})
		},
	},
}
</script>

<style scoped>

</style>
