<template>
	<div :class="getClass" :style="getStyle">
		<div v-show="showLine" :class="`${prefix}-step__line`">
			<i></i>
		</div>
		<div :class="`${prefix}-step-header-wrapper`">
			<div :class="`${prefix}-step-header`" @click="handleStepClick">
				<template v-if="!$slots.default">

					<template v-if="icon">
						<fb-icon :class="`${prefix}-step-header__icon`"
								 size="m"
								 :name="icon"/>
					</template>

					<template v-else>

						<fb-icon :class="`${prefix}-step-header__icon`"
								 size="m" v-if="myStatus == 'done'"
								 name="selected"/>
						<span :class="`${prefix}-step-header__icon`" v-else>
                        {{index+1}}
                    </span>
					</template>


				</template>
				<template v-else>
					<slot/>
				</template>
			</div>
		</div>
		<div :class="`${prefix}-step-body`" @click="handleStepClick">
			<div :class="`${prefix}-step-label`">
				<slot name="label">{{label}}</slot>
			</div>
			<div :class="`${prefix}-step-description`">
				<slot name="description">{{description}}</slot>
			</div>
		</div>
	</div>
</template>

<script>/**
 * FbStep
 * (c) 2020 lincong1987
 */

import {closest} from '../../../utils/componentUtils'
import {includes} from 'lodash-es'
import FbIcon from '../../icon/src/FbIcon'
import {prefix} from '../../../../src/config'

export default {
	name: 'FbStep',
	components: {FbIcon},
	props: {
		label: {
			type: String,
			default: '',
		},
		description: {
			type: String,
			default: undefined,
		},
		icon: {
			type: String,
			default: undefined,
		},
		width: {
			type: String,
			default: undefined,
		},
		status: {
			type: String,
			default: 'ready',
			validate(value) {
				return includes(['ready', 'doing', 'done', 'wait', 'error'], value)
			},
		},
	},

	data() {
		return {
			prefix,
			index: 0,
			myStatus: this.status,
			fbSteps: closest(this, 'FbSteps'),

		}
	},

	watch: {
		status() {
			console.log(this.status)
		},
	},

	computed: {
		getClass() {
			return `${prefix}-step ${prefix}-step--${this.myStatus}`
		},

		getStyle() {
			return {width: this.width || this.fbSteps.childWidth}
		},

		showLine() {
			return this.fbSteps.totalSteps - 1 != this.index
		},

	},

	methods: {

		handleStepClick(...args) {
			this.$emit('on-click', ...args)
			this.$emit('on-step-click', this.index)
			this.fbSteps && this.fbSteps.$emit('on-step-click', this.index)
		},

	},

}
</script>

<style scoped>

</style>
