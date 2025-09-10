<template>
	<div :class="getClass">
		<fb-tag
			v-for="(node, i) in myData"
			:key="node[reader.value]"
			v-model="node[reader.value]"
			:color="node.color || color"
			:background-color="node.backgroundColor || backgroundColor"
			:border-color="node.borderColor || borderColor"
			:label-color="node.labelColor || labelColor"
			:effect="node.effect || effect"
			:type="node.type || type"
			:round="node.round || round"
			:closable="node.closable || closable"
			:label="node[reader.label]"
			:solid="node[reader.solid] || solid"
			:light="node[reader.light] || light"
			:dark="node[reader.dark] || dark"
			:size="node[reader.size] || size"
			:icon="node[reader.icon] || icon"
			:plain="node[reader.plain] || plain"

			@on-click="handleClick"
		>
			<slot name="node" :node="node"></slot>
		</fb-tag>

	</div>
</template>

<script>
import FbTag from './FbTag'
import { prefix } from '../../../../src/config'
import { remove } from 'lodash-es'
import { uuid } from '../../../utils/utils'

/**
 * FbTags
 * (c) 2020 lincong1987
 */
export default {
	name: 'FbTags',
	components: {FbTag},
	props: {
		value: {
			type: Array,
			default () {
				return []
			},
		},

		// 如果传入一个数组
		data: {
			type: Array,
			default () {
				return []
			},
		},

		// 是否垂直
		vertical: {
			type: Boolean,
			default: false,
		},

		reader: {
			type: Object,
			default () {
				return {
					value: 'value',
					label: 'label',
					solid: 'solid',
					light: 'light',
					dark: 'dark',
					round: 'round',
					type: 'type',
					effect: 'effect',
					size: 'size',
					icon: 'icon',
					plain: 'plain',
					color: 'color',
					backgroundColor: 'backgroundColor',
					borderColor: 'borderColor',
					labelColor: 'labelColor',
					closable: 'closable',
				}
			},
		},

		closable: {
			type: Boolean,
			default: true,
		},

		disabled: {
			type: Boolean,
			default: false,
		},
		readonly: {
			type: Boolean,
			default: false,
		},

		round: {
			type: Boolean,
			default: null,
		},
		type: {
			type: String,
			default: null,
		},
		color: {
			type: String,
			default: null,
		},

		backgroundColor: {
			type: String,
			default: undefined,
		},

		borderColor: {
			type: String,
			default: undefined,
		},

		labelColor: {
			type: String,
			default: undefined,
		},

		effect: {
			type: String,
			default: null,
		},

		light: {
			type: Boolean,
			default: false,
		},
		plain: {
			type: Boolean,
			default: false,
		},
		dark: {
			type: Boolean,
			default: false,
		},
		solid: {
			type: Boolean,
			default: false,
		},

		size: {
			type: String,
			default: 'm',

		},

		icon: {
			type: String,
			default: null,
		},

	},

	data () {
		return {
			prefix,
			myData: this.mixMyData(this.data.length > 0 ? this.data : (this.value || [])),
		}
	},

	methods: {
		handleClose (value, tag) {

			this.myData.splice(this.myData.map(data => data[this.reader.value]).indexOf(value), 1)

			this.$emit('input', this.myData, tag)
			this.$emit('on-remove', value, tag)
		},

		handleChange () {
			this.$emit('on-change')
		},

		handleClick (event, tag) {
			this.$emit('on-click', event, tag)
		},

		mixMyData (data) {
			return data.map(item => {
				if (typeof item === 'string') {
					return {
						value: item,
						label: item,
					}
				}
				return {
					...item,
					value: item[this.reader.value] || uuid(),
				}
			})
		},
	},
	computed: {
		getClass () {
			let arr = [
				`${prefix}-tags`,
			]

			if (this.vertical) {
				arr.push(`${prefix}-tags--vertical`)
			}

			return arr
		},
	},

	watch: {
		data (value) {
			this.myData = this.mixMyData(value)
		},

		value (value) {
			this.myData = this.mixMyData(value)
		},
	},
}
</script>

<style scoped>

</style>
