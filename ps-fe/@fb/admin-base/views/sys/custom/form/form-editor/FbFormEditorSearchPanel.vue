<template>
	<fb-page-search style="padding: 0">

		<template slot="query">
			<fb-form v-if="search.props.show_query" ref="query-form" mode="query">
				<fb-row :key="i" v-for="(n, i) in (()=>{
					let arr = []
					let size = search.query.props.layout === 'row-12' ? 2 : 3
					for (let i = 0; i <= (search.query.rows.filter(n=>n.formItemProps.show === true).length / size); i++){
						let row = []
						for(let j = 0; j <= size-1; j++){
							row.push(i * size + j)
						}
						arr.push(row)
					}
					return arr
				})()">
					<template v-for="(col, j) in search.query.rows.filter(n=>n.formItemProps.show === true)">
						<fb-col v-if="n.includes(j)"
						        :span="search.query.props.layout === 'row-12' ? '12' : '8'">
							<fb-form-item :label="col.formItemProps.label" :label-width="search.query.props.labelWidth">

								<template v-if="['fb-textarea', 'fb-input', 'fb-editor'].includes(col.type)">
									<fb-input
										:placeholder="col.props.placeholder"
										:icon="col.props.icon"
										:clearable="col.props.clearable"
									></fb-input>
								</template>
								<template v-if="col.type === 'fb-text'">
									不支持
								</template>
								<template v-if="col.type === 'fb-color-picker'">
									不支持
								</template>
								<template v-if="col.type === 'fb-input-number'">
									<fb-input-number
										:placeholder="col.props.placeholder"
										:clearable="col.props.clearable"
									></fb-input-number>
								</template>
								<template v-if="col.type === 'fb-select'">
									<fb-select
										:placeholder="col.props.placeholder"
										:auto-load="col.props.autoLoad"
										:clearable="col.props.clearable"
										:data="uniqAndCleanArray(col.props.data)"
										:param="arrayToObject(uniqAndCleanArray(col.props.param))"
									></fb-select>
								</template>
								<template
									v-if="col.type === 'fb-tree-select'">
									<fb-tree-select
										:placeholder="col.props.placeholder"
										:auto-load="col.props.autoLoad"
										:clearable="col.props.clearable"
										:data="uniqAndCleanArray(col.props.data)"
										:param="arrayToObject(uniqAndCleanArray(col.props.param))"
									></fb-tree-select>
								</template>
								<template
									v-if="col.type === 'fb-radio-group'">
									<fb-radio-group
										:data="(()=>{
											let data = uniqAndCleanArray(col.props.data, 'value')

											if (data.filter(n=>n.value === '').length > 0) {
											   return data
											}

											if (col.formItemProps.searchOptionAll === true) {
												data.unshift({
													label: '全部',
													value: ''
												})
											}

											return data
										})()"
										:vertical="col.props.vertical"
										:button="col.props.button"
										:list="col.props.list"
										:radio-space="col.props.radioSpace"
									></fb-radio-group>
								</template>
								<template
									v-if="col.type === 'fb-checkbox-group'">
									<fb-checkbox-group
										:data="uniqAndCleanArray(col.props.data, 'value')"
										:vertical="col.props.vertical"
										:button="col.props.button"
										:list="col.props.list"
									></fb-checkbox-group>
								</template>
								<template v-if="col.type === 'fb-checkbox'">
									<fb-checkbox
										:value="col.props.value"
										:label="col.props.label"
									></fb-checkbox>
								</template>
								<template
									v-if="col.type === 'tp-datepicker'">
									<!--									<tp-datepicker-->
									<!--										:mode="col.props.mode"-->
									<!--										:format="col.props.format"-->
									<!--										:readonly="col.props.readonly"-->
									<!--										:disabled="col.props.disabled"-->
									<!--										:clearable="col.props.clearable"-->
									<!--										:placeholder="col.props.placeholder"-->
									<!--										:min-date="col.props.minDate"-->
									<!--										:max-date="col.props.maxDate"-->
									<!--										:max-range="col.props.maxRange"-->
									<!--										:position="col.props.position"-->
									<!--									></tp-datepicker>-->

									<fb-table-layout>
										<fb-table-layout-cell>
											<tp-datepicker></tp-datepicker>
										</fb-table-layout-cell>
										<fb-table-layout-cell align="center" width="20px">
											-
										</fb-table-layout-cell>
										<fb-table-layout-cell>
											<tp-datepicker></tp-datepicker>
										</fb-table-layout-cell>
									</fb-table-layout>
								</template>


								<template v-if="col.type === 'biz-ent-person-single-select'">
									<fb-input
										:placeholder="col.props.placeholder"
										:icon="col.props.icon"
										:size="col.props.size"
										:type="col.props.type"
										:disabled="col.props.disabled"
										:readonly="true"
										:clearable="col.props.clearable"
										:maxlength="col.props.maxlength"
										:prependIcon="col.props.prependIcon"
										:width="col.props.width"
										:round="col.props.round"

									>
										<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
										           icon="close-circle"
										           style="background: #F0EFF5; "
										/>
									</fb-input>
								</template>

								<template v-if="col.type === 'biz-ent-person-multiple-select'">
									<fb-input
										:placeholder="col.props.placeholder"
										:icon="col.props.icon"
										:size="col.props.size"
										:type="col.props.type"
										:disabled="col.props.disabled"
										:readonly="true"
										:clearable="col.props.clearable"
										:maxlength="col.props.maxlength"
										:prependIcon="col.props.prependIcon"
										:width="col.props.width"
										:round="col.props.round"
									>
										<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
										           icon="close-circle"
										           style="background: #F0EFF5; "
										/>
									</fb-input>
								</template>

								<template v-if="col.type === 'biz-ent-person-single-select-using-table-show'">
									<fb-input
										:placeholder="col.props.placeholder"
										:icon="col.props.icon"
										:size="col.props.size"
										:type="col.props.type"
										:disabled="col.props.disabled"
										:readonly="true"
										:clearable="col.props.clearable"
										:maxlength="col.props.maxlength"
										:prependIcon="col.props.prependIcon"
										:width="col.props.width"
										:round="col.props.round"
									>
										<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
										           icon="close-circle"
										           style="background: #F0EFF5; "
										/>
									</fb-input>
								</template>

								<template v-if="col.type === 'biz-ent-person-multiple-select-using-table-show'">
									<fb-input
										:placeholder="col.props.placeholder"
										:icon="col.props.icon"
										:size="col.props.size"
										:type="col.props.type"
										:disabled="col.props.disabled"
										:readonly="true"
										:clearable="col.props.clearable"
										:maxlength="col.props.maxlength"
										:prependIcon="col.props.prependIcon"
										:width="col.props.width"
										:round="col.props.round"
									>
										<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
										           icon="close-circle"
										           style="background: #F0EFF5; "
										/>
									</fb-input>
								</template>


								<template v-if="col.type === 'biz-org-person-single-select'">
									<fb-input
										:placeholder="col.props.placeholder"
										:icon="col.props.icon"
										:size="col.props.size"
										:type="col.props.type"
										:disabled="col.props.disabled"
										:readonly="true"
										:clearable="col.props.clearable"
										:maxlength="col.props.maxlength"
										:prependIcon="col.props.prependIcon"
										:width="col.props.width"
										:round="col.props.round"
									>
										<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
										           icon="close-circle"
										           style="background: #F0EFF5; "
										/>
									</fb-input>
								</template>

								<template v-if="col.type === 'biz-org-person-multiple-select'">
									<fb-input
										:placeholder="col.props.placeholder"
										:icon="col.props.icon"
										:size="col.props.size"
										:type="col.props.type"
										:disabled="col.props.disabled"
										:readonly="true"
										:clearable="col.props.clearable"
										:maxlength="col.props.maxlength"
										:prependIcon="col.props.prependIcon"
										:width="col.props.width"
										:round="col.props.round"
									>
										<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
										           icon="close-circle"
										           style="background: #F0EFF5; "
										/>
									</fb-input>
								</template>
								<template v-if="col.type === 'biz-org-ent-single-select'">
									<fb-input
										:placeholder="col.props.placeholder"
										:icon="col.props.icon"
										:size="col.props.size"
										:type="col.props.type"
										:disabled="col.props.disabled"
										:readonly="true"
										:clearable="col.props.clearable"
										:maxlength="col.props.maxlength"
										:prependIcon="col.props.prependIcon"
										:width="col.props.width"
										:round="col.props.round"
									>
										<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
										           icon="close-circle"
										           style="background: #F0EFF5; "
										/>
									</fb-input>
								</template>
								<template v-if="col.type === 'biz-org-ent-multiple-select'">
									<fb-input
										:placeholder="col.props.placeholder"
										:icon="col.props.icon"
										:size="col.props.size"
										:type="col.props.type"
										:disabled="col.props.disabled"
										:readonly="true"
										:clearable="col.props.clearable"
										:maxlength="col.props.maxlength"
										:prependIcon="col.props.prependIcon"
										:width="col.props.width"
										:round="col.props.round"
									>
										<fb-button v-if="col.props.clearable === true" slot="append-button" type="text"
										           icon="close-circle"
										           style="background: #F0EFF5; "
										/>
									</fb-input>
								</template>


								<template v-if="col.type === 'biz-current-ctx'">
									<fb-input
										:placeholder="col.props.placeholder"
										:icon="col.props.icon"
										:clearable="col.props.clearable"
									></fb-input>
								</template>
								<template v-if="col.type === 'biz-current-person'">
									<fb-input
										:placeholder="col.props.placeholder"
										:icon="col.props.icon"
										:clearable="col.props.clearable"
									></fb-input>
								</template>
								<template v-if="col.type === 'biz-current-dept'">
									<fb-input
										:placeholder="col.props.placeholder"
										:icon="col.props.icon"
										:clearable="col.props.clearable"
									></fb-input>
								</template>
								<template v-if="col.type === 'biz-current-org'">
									<fb-input
										:placeholder="col.props.placeholder"
										:icon="col.props.icon"
										:clearable="col.props.clearable"
									></fb-input>
								</template>
								<template v-if="col.type === 'biz-current-date-time'">
									<fb-table-layout>
										<fb-table-layout-cell>
											<tp-datepicker></tp-datepicker>
										</fb-table-layout-cell>
										<fb-table-layout-cell align="center" width="20px">
											-
										</fb-table-layout-cell>
										<fb-table-layout-cell>
											<tp-datepicker></tp-datepicker>
										</fb-table-layout-cell>
									</fb-table-layout>
								</template>



								<!--								<template-->
								<!--									v-if="['like', '=', '>', '<', '>=', '<=', '!=', 'in'].includes(col.formItemProps.searchType)">-->
								<!--									<fb-input></fb-input>-->
								<!--								</template>-->

								<!--								<template v-if="col.formItemProps.searchType === 'dict'">-->
								<!--									<fb-select></fb-select>-->
								<!--								</template>-->
								<!--								<template v-if="col.formItemProps.searchType === 'dept'">-->
								<!--									<fb-select></fb-select>-->
								<!--								</template>-->
								<!--								<template v-if="col.formItemProps.searchType === 'org'">-->
								<!--									<fb-select></fb-select>-->
								<!--								</template>-->
								<!--								<template v-if="col.formItemProps.searchType === 'city'">-->
								<!--									<fb-select></fb-select>-->
								<!--								</template>-->

								<!--								<template v-if="col.formItemProps.searchType === 'date-range'">-->
								<!--									<fb-table-layout>-->
								<!--										<fb-table-layout-cell>-->
								<!--											<tp-datepicker></tp-datepicker>-->
								<!--										</fb-table-layout-cell>-->
								<!--										<fb-table-layout-cell align="center" width="20px">-->
								<!--											- -->
								<!--										</fb-table-layout-cell>-->
								<!--										<fb-table-layout-cell>-->
								<!--											<tp-datepicker></tp-datepicker>-->
								<!--										</fb-table-layout-cell>-->
								<!--									</fb-table-layout>-->
								<!--								</template>-->

							</fb-form-item>
						</fb-col>
					</template>
				</fb-row>
			</fb-form>
		</template>

		<template slot="buttons" v-if="search.props.show_buttons">
			<fb-button v-if="search.actions.add.show" ref="buttonAdd"
			           :icon="search.actions.add.icon"
			>{{
					search.actions.add.label
				}}
			</fb-button>

			<fb-button v-if="search.actions.export.show" ref="buttonExport"
			           :icon="search.actions.export.icon"
			>{{
					search.actions.export.label
				}}
			</fb-button>

		</template>

		<template slot="actions" v-if="search.props.show_actions">
			<fb-button v-if="search.actions.query.show" type="primary"
			           :icon="search.actions.query.icon"
			>{{
					search.actions.query.label
				}}
			</fb-button>
		</template>

		<template slot="table" v-if="search.props.show_table">
			<fb-container display="none">{{ search.table }}</fb-container>
			<fb-simple-table
				ref="table"
				:columns="(()=>{

					return  search.table.props.columns.filter(c=>c.show).map((n,i)=>{
						//n.slot = n.view ? 'view' : (n.slot === 'op' ? n.slot : null)
						//return n


							let column = []
							if(n.view) {
								n.slot = 'view'
							}

//							if (n.slot === 'op') {
//
//							}
							//n.slot = n.view ? 'view' : (n.slot === 'op' ? n.slot : null) ;

//							if (n && n.item && n.item.type === 'fb-color-picker') {
//								n.slot = 'fb-color-picker'
//							}

							return JSON.parse(JSON.stringify(n))
					})
				})()"
				:pk="search.table.props.pk"
				:rownum="search.table.props.rownum"
				:rownum-title="search.table.props.rownumTitle"
				:rownum-width="search.table.props.rownumWidth"
				:scroll="(()=>{
					//{x:1000, y: 330, autoHeight: true}
					let scroll = {}
					if (typeof search.table.props.scroll.x !== 'undefined') {scroll.x = search.table.props.scroll.x}
					if (typeof search.table.props.scroll.y !== 'undefined') {scroll.y = search.table.props.scroll.y}
					if (typeof search.table.props.scroll.autoHeight !== 'undefined') {scroll.autoHeight = search.table.props.scroll.autoHeight}
					return scroll
				})()"

				:size="search.table.props.size"
				:bordered="search.table.props.bordered"
				:head-bordered="search.table.props.headBordered"
				:show-header="search.table.props.showHeader"
				:fixed="search.table.props.fixed"
				:no-empty="search.table.props.noEmpty"
				:loading-text="search.table.props.loadingText"
				:no-data-text="search.table.props.noDataText"
				:no-head-splitter="search.table.props.noHeadSplitter"
				:long-head-splitter="search.table.props.longHeadSplitter"
				:show-pager="false"
				:no-pager="true"
				:pager="(()=>{
					let pager = {}
					return pager
				})()"
				:auto-scroll="search.table.props.autoScroll"
				:auto-select="search.table.props.autoSelect"
				:auto-load="search.table.props.autoLoad"
				:auto-pk="search.table.props.autoPk"
				:row-groups="search.table.props.rowGroups"
				:auto-scroll-speed="search.table.props.autoScrollSpeed"
				:auto-scroll-delay="search.table.props.autoScrollDelay"

				:data="(()=>{
					return search.table.props.mockData
				})()"
				:param="arrayToObject(uniqAndCleanArray(search.table.props.param))"

				:sorters="(()=>{
					let sorters = {};
					search.table.props.columns.filter(c=>c.sort).map((n,i)=>{
						sorters[n.name] = {}
					})
					return  sorters
				})()"
			>
				<!--				:service="search.table.props.service"-->

				<template v-slot:op="props">
					<fb-space>
						<fb-button editor size="s" v-if="search.actions.modify.show"
						           :icon="search.actions.modify.icon">
							{{ search.actions.modify.label }}
						</fb-button>
						<fb-button editor size="s" v-if="search.actions.delete.show"
						           :icon="search.actions.delete.icon">
							{{ search.actions.delete.label }}
						</fb-button>
					</fb-space>
				</template>

				<template v-slot:view="props">
					<fb-link :label="props.row[props.column.name]"
					         type="primary"></fb-link>
				</template>
			</fb-simple-table>
			<fb-container v-if="!search.table.props.noPager">
				<fb-container padding="8px 0" v-show="search.table.props.showPager">
					<fb-pager
						v-show="true"
						:align="(()=>{
								let _pager = arrayToObject(uniqAndCleanArray(search.table.props.pager))
								return _pager.align  || 'right'
							})()"
						:simple="(()=>{
								let _pager = arrayToObject(uniqAndCleanArray(search.table.props.pager))
								return _pager.simple
							})()"
						:show-total-info="(()=>{
								let _pager = arrayToObject(uniqAndCleanArray(search.table.props.pager))
								return _pager.showTotalInfo
							})()"
					></fb-pager>
				</fb-container>
			</fb-container>

		</template>
	</fb-page-search>
</template>

<script>/**
 * FbFormEditorSearchPanel
 * (c) 2021 lincong1987
 */
import { closest } from '../../../../../util/componentUtils'

import { arrayToObject, uniqAndCleanArray, computeCurrentCtx } from '../../../../../views/sys/custom/form/form-editor/utils/utils'

export default {
	name: 'FbFormEditorSearchPanel',

	props: {
		search: {},
	},
	created () {
		this.formEditor = closest(this, 'FbFormEditor')
	},
	beforeDestroy () {
		this.formEditor = null
	},
	data () {

		return {}
	},
	watch: {
		'search.table.props.columns': {
			deep: true,
			handler (columns) {
				this.$nextTick(() => {
					console.log(this.search.table.props.data)
					this.updateSearchProps(JSON.parse(JSON.stringify(this.search.table.props.data)), 'table', 'props',
						'data')
				})
			},
		},
	},
	methods: {
		arrayToObject,
		uniqAndCleanArray,
		computeCurrentCtx (exp) {
			return computeCurrentCtx.apply(this, [exp])
		},

		updateSearchProps (value, prop) {
			this.formEditor.updateProps('search', value, prop)
		},

		handleQueryFormFieldsChange (value) {

		},

		syncQueryFormFields () {
			this.formEditor.syncSearchQueryForm()
		},

		syncTableColumns () {
			this.formEditor.syncSearchTableColumns()
		},

		getQueryFormFields () {
			let data = []

			this.formEditor.add.rows.forEach((n, i) => {
				if (n.type === 'row-24' && n.cols[0].type) {
					data.push({
						value: n.cols[0].formItemProps.name,
						label: n.cols[0].formItemProps.label,
					})
				}
				if (n.type === 'row-12') {
					if (n.cols[0].type) {
						data.push({
							value: n.cols[0].formItemProps.name,
							label: n.cols[0].formItemProps.label,
						})
					}
					if (n.cols[1].type) {
						data.push({
							value: n.cols[1].formItemProps.name,
							label: n.cols[1].formItemProps.label,
						})
					}
				}
			})

			return data
		},

	},

}
</script>

<style lang="less" scoped>

</style>
