<template>
	<!-- fb-flop 扩展组件兼容大屏小数点形式 -->
	<fb-container>
		<template v-if="type === 'simple'">
			<fb-flop v-bind="{
				    ...startFlopAttr,
					...$attrs,
					num
			}"  >
				<template v-slot:suffix>
					<slot name="endSuffix"></slot>
				</template>
			</fb-flop>

		</template>
		<template v-else-if="type === 'brand'">
			<fb-flex v-if="isArray(flopValue)" :style="wrapperStyle" gap="6px">
				<fb-flop :num="flopValue[0]" v-bind="startFlopAttr"></fb-flop>
				<fb-text :size="dotSize" style="font-weight: bold" :style="dotStyle">.</fb-text>
				<fb-flop :num="flopValue[1]" v-bind="endFlopAttr" :min-length="floatEndLength">
					<template v-slot:suffix>
						<slot name="endSuffix"></slot>
					</template>
				</fb-flop>
			</fb-flex>
			<fb-flop v-else v-bind="$options.propsData" :brand-suffix="unit">
				<template v-slot:suffix>
					<slot name="endSuffix"></slot>
				</template>
			</fb-flop>
		</template>
	</fb-container>
</template>

<script>
import { isArray, cloneDeep } from 'lodash-es'

export default {
	name: 'ScreenFlop',
	props: {
		dotSize: {
			type: [String],
			default: '30',
		},
		dotStyle: {
			type: [Object, Array],
			default: () => ({}),
		},
		wrapperStyle: {
			type: [Object, Array],
			default: () => ({}),
		},
		type: {
			type: [String],
			default: 'brand',
		},
		num: {
			type: [Number, String],
			default: 0,
		},
		brandPrefix: {
			type: [Number, String],
		},
		brandSuffix: {
			type: [Number, String],
		},
		// 补位 length
		minLength: {
			type: [Number, String],
			default: 0,
		},
		// 限制 length
		maxLength: {
			type: [Number, String],
			default: 0,
		},
		brandSize: {
			type: [String],
			default: 'm',
		},
		brandStatus: {
			type: [String],
			default: 'default',
		},
		brandStyle: {
			type: [Object, Array],
			default: () => {
				return {}
			},
		},
		noScroll: {
			type: [Boolean],
			default: false,
		},
		duration: {
			type: Number,
			default: 1200,
		},
		flipType: {
			type: [String],
			default: 'down',
		},
		// 翻牌顺序
		reverse: {
			type: [Boolean],
			default: true,
		},
		theme: {
			type: [String],
			default: undefined,
		},
		size: {
			type: [String],
			default: undefined,
		},
		startFlop: {
			type: [Object],
			default: () => ({}),
		},
		endFlop: {
			type: [Object],
			default: () => ({}),
		},
		unit: {
			type: [String],
			default: '',
		},
	},
	computed: {
		flopValue () {
			let value = this.num + ''
			if (value.indexOf('.') >= 0) {
				value = value.split('.')
				let valueIdx1 = value[1]
				if (valueIdx1.length > 1 && valueIdx1.indexOf('0') === 0) {
					this.floatEndLength = valueIdx1.length
				} else {
					this.floatEndLength = 0
				}
				if (this.endFlop.minLength) {
					this.floatEndLength = this.endFlop.minLength
				}
				value = value.map(item => parseFloat(item))
			} else {
				value = parseFloat(value)
			}
			return value
		},
		startFlopAttr () {
			let attrs = Object.assign(cloneDeep(this.$options.propsData), this.startFlop)
			return attrs
		},
		endFlopAttr () {
			let source1 = {}
			let value = this.num + ''
			if (value.indexOf('.') >= 0) {
				source1.brandSuffix = this.unit
			}
			let attrs = Object.assign(cloneDeep(this.$options.propsData), source1, this.endFlop)
			return attrs
		},
	},
	data () {
		return {
			floatEndLength: 0,
		}
	},
	methods: {
		isArray,
	},
	mounted () {
	},
}
</script>

<style scoped>

</style>
