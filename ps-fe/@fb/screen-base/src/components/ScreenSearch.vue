<template>
	<fb-container class="screen-search" v-bind="$attrs" width="100%" height="100%">
		<fb-popup-select v-model="showList" style="width: 100%">
			<fb-container display="flex" slot="header">
				<fb-input size="l" v-model="selectName" :placeholder="placeholder"
						  :style="[inputStyle, {marginRight: showSenior ? '4px' : 0}]" v-bind="inputAttrs"
						  clearable @on-enter="doSearch">
					<fb-button v-if="!noAppendButton" slot="append-button" size="l" icon="search" type="primary"
							   @on-click="doSearch"></fb-button>
				</fb-input>
				<fb-button v-if="showSenior" size="l" icon="filter" type="primary" title="高级查询"
						   @on-click="doSeniorSearch"></fb-button>
			</fb-container>

			<fb-list slot="picker" class="screen-search-list" :data="data" :split-line="false">
				<fb-container slot="item" slot-scope="{item, index}" @on-click="handleClick(item)" :title="item[listField]"
							  class="screen-search-list__item" ellipsis>
					{{ item[listField] }}
				</fb-container>
			</fb-list>
		</fb-popup-select>
	</fb-container>

</template>

<script>
/**
 * ScreenSearch.vue
 * (c) 2021 lincong1987
 *
 * 大屏查询框
 *
 */
export default {

	name: 'ScreenSearch',

	props: {
		data: {
			type: [Array],
			default: () => []
		},
		listField: {
			type: [String],
			default: 'label'
		},
		inputAppendText: {
			type: [String],
			default: ''
		},
		showSenior: {
			type: [Boolean],
			default: false
		},
		inputStyle: {
			type: [Object, Array],
			default: () => []
		},
		inputAttrs: {
			type: [Object],
			default: () => ({})
		},
		noAppendButton: {
			type: [Boolean],
			default: false
		},
		searchValue: {
			type: [String],
			default: ''
		},
		placeholder: {
			type: [String],
			default: '输入查询关键词'
		}
	},

	data () {
		return {
			selectName: this.searchValue || '',
			showList: false
		}
	},

	computed: {
	},

	watch: {
		selectName(newVal) {
			this.$emit('update:searchValue', newVal)
			this.$emit('on-change', newVal)
			if (!newVal) {
				this.$emit('on-clear')
			}
		}
	},

	mounted () {

	},
	methods: {
		handleChange(value) {
			this.$emit('on-change', value)
		},
		doSearch() {
			this.showList = true
			this.$emit('on-search', this.selectName)
		},
		handleClick(item) {
			this.showList = false
			this.selectName = item[this.listField]
			this.$emit('on-result-click', item)
		},
		clearValue() {
			this.selectName = ''
		},
		doSeniorSearch() {
			this.$emit('on-senior-click', this.selectName)
		}
	}
}
</script>

<style lang="less" scoped>

@import "../assets/styles/components/screen-search.less";

</style>
