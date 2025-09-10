<template>
	<fb-container  >
		<fb-page-search>

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
								<fb-form-item :label="col.formItemProps.label"
								              :label-width="search.query.props.labelWidth">
									<template v-if="['fb-textarea', 'fb-input', 'fb-editor'].includes(col.type)">
										<fb-input
											:placeholder="col.props.placeholder"
											:icon="col.props.icon"
											:clearable="col.props.clearable"
											:value="col.props.value"
											@input="(value)=>{formData[col.formItemProps.name] = value}"
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
											clearable
											:value="(()=>{
												if (typeof formData[col.formItemProps.name] !== 'undefined') {
													return formData[col.formItemProps.name]
												}else if (typeof col.props.value !== 'undefined') {
													return col.props.value
												} else {
													return ''
												}
											})()"
											@input="(value)=>{
												formData[col.formItemProps.name] = value || ''
											}"
										></fb-input-number>
									</template>
									<template v-if="col.type === 'fb-select'">
										<fb-select
											:placeholder="col.props.placeholder"
											:auto-load="col.props.autoLoad"
											:clearable="col.props.clearable"
											:multiple="col.props.multiple"
											:data="uniqAndCleanArray(col.props.data, 'value')"
											:service="getService(col.props.service)"
											:param="arrayToObject(uniqAndCleanArray(col.props.param))"
											:value="col.props.value"
											@input="(value)=>{formData[col.formItemProps.name] = value}"
										></fb-select>
									</template>
									<template v-if="col.type === 'fb-tree-select'">
										<fb-tree-select
											:placeholder="col.props.placeholder"
											:auto-load="col.props.autoLoad"
											:clearable="col.props.clearable"
											:multiple="col.props.multiple"
											:data="uniqAndCleanArray(col.props.data, 'value')"
											:service="getService(col.props.service)"
											:param="arrayToObject(uniqAndCleanArray(col.props.param))"
											:value="col.props.value"
											@input="(value)=>{
												let v = value
												if (value === null) {
													v = ''
												}
												formData[col.formItemProps.name] = v
											}"
										></fb-tree-select>
									</template>
									<template v-if="col.type === 'fb-radio-group'">
										<fb-radio-group
											:radio-space="col.props.radioSpace"
											:vertical="col.props.vertical"
											:button="col.props.button"
											:list="col.props.list"
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
											:data-filter="((data)=>{
												  return (json)=>{
												  	let data = uniqAndCleanArray(json.data, 'value')

													if (data.filter(n=>n.value === '').length > 0) {
													   return json
													}

													if (col.formItemProps.searchOptionAll === true) {
														data.unshift({
															label: '全部',
															value: ''
														})
													}

													return Object.assign(json, {data})
												  }
											})()"
											:service="getService(col.props.service)"
											:param="arrayToObject(uniqAndCleanArray(col.props.param))"
											:value="col.props.value"
											@input="(value)=>{formData[col.formItemProps.name] = value}"

										></fb-radio-group>
									</template>
									<template v-if="col.type === 'fb-checkbox-group'">
										<fb-checkbox-group
											:vertical="col.props.vertical"
											:button="col.props.button"
											:list="col.props.list"
											:data="uniqAndCleanArray(col.props.data, 'value')"
											:service="getService(col.props.service)"
											:param="arrayToObject(uniqAndCleanArray(col.props.param))"
											:value="col.props.value"
											@input="(value)=>{formData[col.formItemProps.name] = value}"
										></fb-checkbox-group>
									</template>
									<template v-if="col.type === 'fb-checkbox'">
										<fb-checkbox
											:value="col.props.value"
											:label="col.formItemProps.label"
											@input="(value)=>{formData[col.formItemProps.name] = value}"
										></fb-checkbox>
									</template>
									<template v-if="col.type === 'tp-datepicker'">
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
												<tp-datepicker
													:mode="col.props.mode"
													:format="col.props.format"
													:value-format="col.props.valueFormat"
													:clearable="col.props.clearable"
													:placeholder="col.props.placeholder"
													:min-date="col.props.minDate"
													:max-date="formData[`${col.formItemProps.name}Min`]"
													:max-range="col.props.maxRange"
													:position="col.props.position"
													:label="col.formItemProps.label"
													@listener="(value, valueRaw)=>{
														formData[`${col.formItemProps.name}Max`] = value
													}"
												></tp-datepicker>
											</fb-table-layout-cell>
											<fb-table-layout-cell align="center" width="20px">
												-
											</fb-table-layout-cell>
											<fb-table-layout-cell>
												<tp-datepicker
													:mode="col.props.mode"
													:format="col.props.format"
													:value-format="col.props.valueFormat"
													:clearable="col.props.clearable"
													:placeholder="col.props.placeholder"
													:min-date="formData[`${col.formItemProps.name}Max`]"
													:max-date="col.props.maxDate"
													:max-range="col.props.maxRange"
													:position="col.props.position"

													:label="col.formItemProps.label"
													@listener="(value)=>{formData[`${col.formItemProps.name}Min`] = value}"
												></tp-datepicker>
											</fb-table-layout-cell>
										</fb-table-layout>
									</template>
									<template v-if="col.type === 'tp-upload'">
										<fb-text size="m" color="red">上传组件不支持查询</fb-text>
									</template>

									<template v-if="col.type === 'biz-ent-person-single-select'">
										<fb-input
											:placeholder="col.props.placeholder"
											:icon="col.props.icon"
											:size="col.props.size"
											:type="col.props.type"
											:disabled="col.props.disabled"
											:readonly="true"
											:clearable="true"
											:maxlength="col.props.maxlength"
											:prependIcon="col.props.prependIcon"
											:width="col.props.width"
											:round="col.props.round"

											:value="formData[`${col.formItemProps.name}$Label`]"
											@on-click="()=>{
												handleEntPersonSingleSelect({
													success:(result)=>{
														if(result &&  result.personId && result.personName) {
															formData[col.formItemProps.name] = result.personId
															formData[`${col.formItemProps.name}$Label`] = result.personName
														}
													}
											})}"
										>
											<fb-button v-if="col.props.clearable === true" slot="append-button"
											           type="text"
											           icon="close-circle"
											           style="background: #F0EFF5; "
											           @on-click="()=>{
															   formData[col.formItemProps.name] = ''
															   formData[`${col.formItemProps.name}$Label`] = ''
														   }"
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
											:clearable="true"
											:maxlength="col.props.maxlength"
											:prependIcon="col.props.prependIcon"
											:width="col.props.width"
											:round="col.props.round"
											:value="formData[`${col.formItemProps.name}$Label`]"
											@on-click="()=>{
													handleOrgPersonMultipleSelect({
														initData: formData[`${col.formItemProps.name}$Raw`],
														success:(result)=>{
															if(result &&  result.length > 0) {
																formData[col.formItemProps.name] = result.map(n=>n.value).join(',')
																formData[`${col.formItemProps.name}$Label`] = result.map(n=>n.label).join(',')
																formData[`${col.formItemProps.name}$Raw`] = result
															}
														}
													})}"

											:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
										>
											<fb-button v-if="col.props.clearable === true" slot="append-button"
											           type="text"
											           icon="close-circle"
											           style="background: #F0EFF5; "
											           @on-click="()=>{
															   formData[col.formItemProps.name] = ''
															   formData[`${col.formItemProps.name}$Label`] = ''
															   formData[`${col.formItemProps.name}$Raw`] = []
														   }"
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
											:clearable="true"
											:maxlength="col.props.maxlength"
											:prependIcon="col.props.prependIcon"
											:width="col.props.width"
											:round="col.props.round"
											:value="formData[`${col.formItemProps.name}$Label`]"
											@on-click="()=>{
													handleEntPersonSingleSelectUsingTableShow({
														success:(result)=>{
															if(result &&  result.personId && result.personName) {
																formData[col.formItemProps.name] = result.personId
																formData[`${col.formItemProps.name}$Label`] = result.personName
															}
														}
													})}"

											:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
										>
											<fb-button v-if="col.props.clearable === true" slot="append-button"
											           type="text"
											           icon="close-circle"
											           style="background: #F0EFF5; "
											           @on-click="()=>{
															   formData[col.formItemProps.name] = ''
															   formData[`${col.formItemProps.name}$Label`] = ''
														   }"
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
											:clearable="true"
											:maxlength="col.props.maxlength"
											:prependIcon="col.props.prependIcon"
											:width="col.props.width"
											:round="col.props.round"
											:value="formData[`${col.formItemProps.name}$Label`]"
											@on-click="()=>{
													handleEntPersonMultipleSelectUsingTableShow({
														initData: formData[`${col.formItemProps.name}$Raw`],
														success:(result)=>{
															if(result &&  result.length > 0) {
																formData[col.formItemProps.name] = result.map(n=>n.value).join(',')
																formData[`${col.formItemProps.name}$Label`] = result.map(n=>n.label).join(',')
																formData[`${col.formItemProps.name}$Raw`] = result
															}
														}
													})}"

											:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
										>
											<fb-button v-if="col.props.clearable === true" slot="append-button"
											           type="text"
											           icon="close-circle"
											           style="background: #F0EFF5; "
											           @on-click="()=>{
															   formData[col.formItemProps.name] = ''
															   formData[`${col.formItemProps.name}$Label`] = ''
															   formData[`${col.formItemProps.name}$Raw`] = []
														   }"
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
											:clearable="true"
											:maxlength="col.props.maxlength"
											:prependIcon="col.props.prependIcon"
											:width="col.props.width"
											:round="col.props.round"
											:value="formData[`${col.formItemProps.name}$Label`]"
											@on-click="()=>{
													handleOrgPersonSingleSelect({
														success:(result)=>{
															if(result &&  result.personId && result.personName) {
																formData[col.formItemProps.name] = result.personId
																formData[`${col.formItemProps.name}$Label`] = result.personName
															}
														}
													})}"

											:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
										>
											<fb-button v-if="col.props.clearable === true" slot="append-button"
											           type="text"
											           icon="close-circle"
											           style="background: #F0EFF5; "
											           @on-click="()=>{
															   formData[col.formItemProps.name] = ''
															   formData[`${col.formItemProps.name}$Label`] = ''
														   }"
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
											:clearable="true"
											:maxlength="col.props.maxlength"
											:prependIcon="col.props.prependIcon"
											:width="col.props.width"
											:round="col.props.round"

											:value="formData[`${col.formItemProps.name}$Label`]"

											@on-click="()=>{
												handleOrgPersonMultipleSelect({
													initData: formData[`${col.formItemProps.name}$Raw`],
													success:(result)=>{
														if(result &&  result.length > 0) {
															formData[col.formItemProps.name] = result.map(n=>n.value).join(',')
															formData[`${col.formItemProps.name}$Label`] = result.map(n=>n.label).join(',')
															formData[`${col.formItemProps.name}$Raw`] = result
														}
													}
											})}"

											:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
										>
											<fb-button v-if="col.props.clearable === true" slot="append-button"
											           type="text"
											           icon="close-circle"
											           style="background: #F0EFF5; "
											           @on-click="()=>{
															   formData[col.formItemProps.name] = ''
															   formData[`${col.formItemProps.name}$Label`] = ''
															   formData[`${col.formItemProps.name}$Raw`] = []
														   }"
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
											:clearable="true"
											:maxlength="col.props.maxlength"
											:prependIcon="col.props.prependIcon"
											:width="col.props.width"
											:round="col.props.round"
											:value="formData[`${col.formItemProps.name}$Label`]"
											@on-click="()=>{
												handleOrgEntSingleSelect({
												success:(result)=>{
													if(result &&  result.personId && result.personName) {
														formData[col.formItemProps.name] = result.personId
														formData[`${col.formItemProps.name}$Label`] = result.personName
													}
												}
											})}"

											:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
										>
											<fb-button v-if="col.props.clearable === true" slot="append-button"
											           type="text"
											           icon="close-circle"
											           style="background: #F0EFF5; "
											           @on-click="()=>{
															   formData[col.formItemProps.name] = ''
															   formData[`${col.formItemProps.name}$Label`] = ''
														   }"
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
											:clearable="true"
											:maxlength="col.props.maxlength"
											:prependIcon="col.props.prependIcon"
											:width="col.props.width"
											:round="col.props.round"

											:value="formData[`${col.formItemProps.name}$Label`]"

											@on-click="()=>{handleOrgEntMultipleSelect({
												initData: formData[`${col.formItemProps.name}$Raw`],
												success:(result)=>{
													if(result &&  result.length > 0) {
														formData[col.formItemProps.name] = result.map(n=>n.value).join(',')
														formData[`${col.formItemProps.name}$Label`] = result.map(n=>n.label).join(',')
														formData[`${col.formItemProps.name}$Raw`] = result
													}
												}
											})}"

											:el-style="col.props.clearable === true ? 'border-right: 0' : ''"
										>
											<fb-button v-if="col.props.clearable === true" slot="append-button"
											           type="text"
											           icon="close-circle"
											           style="background: #F0EFF5; "
											           @on-click="()=>{
															   formData[col.formItemProps.name] = ''
															   formData[`${col.formItemProps.name}$Label`] = ''
															   formData[`${col.formItemProps.name}$Raw`] = []
														   }"
											/>

										</fb-input>
									</template>




									<template v-if="['biz-current-ctx',
													'biz-current-person',
														'biz-current-dept',
														'biz-current-org'
													].includes(col.type)">
										<fb-input
											:placeholder="col.props.placeholder"
											:icon="col.props.icon"
											:clearable="col.props.clearable"
											:value="col.props.value"
											@input="(value)=>{formData[col.formItemProps.name] = value}"
										></fb-input>
									</template>




									<template v-if="col.type === 'biz-current-date-time'">

										<fb-table-layout>
											<fb-table-layout-cell>
												<tp-datepicker
													:format="col.props.format"
													:value-format="col.props.format"
													:clearable="col.props.clearable"
													:placeholder="col.props.placeholder"
													:min-date="col.props.minDate"
													:max-date="formData[`${col.formItemProps.name}Min`]"
													@listener="(value, valueRaw)=>{
														formData[`${col.formItemProps.name}Max`] = value
													}"
												></tp-datepicker>
											</fb-table-layout-cell>
											<fb-table-layout-cell align="center" width="20px">
												-
											</fb-table-layout-cell>
											<fb-table-layout-cell>
												<tp-datepicker
													:format="col.props.format"
													:value-format="col.props.format"
													:placeholder="col.props.placeholder"
													:min-date="formData[`${col.formItemProps.name}Max`]"
													:max-date="col.props.maxDate"
													@listener="(value)=>{formData[`${col.formItemProps.name}Min`] = value}"
												></tp-datepicker>
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
				<fb-button v-if="search.actions.add.show" ref="buttonAdd" :icon="search.actions.add.icon"

				           v-permission="search.actions.add.code"
				           @on-click="handleAdd"
				>{{
						search.actions.add.label
					}}
				</fb-button>

				<fb-button v-if="search.actions.export.show" ref="buttonExport" :icon="search.actions.export.icon"

				           v-permission="search.actions.export.code"
				           @on-click="handleExport"
				>{{
						search.actions.export.label
					}}
				</fb-button>

			</template>

			<template slot="actions" v-if="search.props.show_actions">
				<fb-button v-if="search.actions.query.show" type="primary" :icon="search.actions.query.icon"

				           v-permission="search.actions.query.code"
				           @on-click="handleQuery"
				>{{
						search.actions.query.label
					}}
				</fb-button>
			</template>

			<template slot="table" v-if="search.props.show_table">
				<fb-simple-table
					ref="table"
					:columns="(()=>{
						return search.table.props.columns.filter(c=>c.show).map((n,i)=>{
							let column = []
							if(n.view) {
								n.slot = 'view'
							}

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
						if (typeof search.table.props.scroll.fillY !== 'undefined') {scroll.fillY = search.table.props.scroll.fillY}
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
					:show-pager="search.table.props.showPager"
					:no-pager="search.table.props.noPager"
					:pager="arrayToObject(search.table.props.pager)"
					:auto-scroll="search.table.props.autoScroll"
					:auto-select="search.table.props.autoSelect"
					:auto-load="false"
					:auto-pk="search.table.props.autoPk"
					:row-groups="search.table.props.rowGroups"
					:auto-scroll-speed="search.table.props.autoScrollSpeed"
					:auto-scroll-delay="search.table.props.autoScrollDelay"
					:formatters="search.table.props.formatters"


					:sorters="search.table.props.sorters"


					:data="(()=>{
						return search.table.props.data
					})()"

					:data-parse="dataParse"

					:param="{}"
					:service="service"

				>
					<!--					search.table.props.autoLoad-->

					<template v-slot:op="props">
						<fb-space>
							<fb-button editor size="s"
									   v-show="search.actions.modify.show"
							           @on-click="handleEdit(props.row)"
							           :icon="search.actions.modify.icon"
										v-permission="search.actions.modify.code"
							>
								{{ search.actions.modify.label }}
							</fb-button>
							<fb-button   size="s" v-show="search.actions.delete.show"
							           @on-click="handleDel(props.row)"
							           danger
							           :icon="search.actions.delete.icon"
							           v-permission="search.actions.delete.code"
							>
								{{ search.actions.delete.label }}
							</fb-button>
						</fb-space>
					</template>

					<template v-slot:view="props">
						<fb-link :label="props.row.detail.label[props.column.name]"
						         :click="()=>{

									 handleView(props.row)

								 }"
						         type="primary"></fb-link>
					</template>

					<template #fb-color-picker="props">
						<fb-container display="flex">
							<fb-container height="20px" width="20px" radius="4px" mr="4px"
							              :background="props.row.detail.label[props.column.name]"
							></fb-container>
							<fb-container height="20px" valign="center">{{
									(() => {
										if (props.row.detail.label[props.column.name]) {
											return props.row.detail.label[props.column.name]
										} else {
											return '无颜色'
										}
									})()
								}}
							</fb-container>
						</fb-container>
					</template>

					<template #tp-upload="props">

						fb-upload

					</template>

				</fb-simple-table>
			</template>

		</fb-page-search>
		<tp-dialog ref="TpDialog" @closeTpDialog="closeDialog"></tp-dialog>
	</fb-container>
</template>

<script>
/**
 * FbFormSearch
 * (c) 2021 lincong1987
 */

import { arrayToObject, toRaw, uniqAndCleanArray } from './utils/utils'
import { get } from 'lodash-es'
import schema from './utils/schema'
import events from './utils/mixins/events'

export default {
	name: 'FbFormSearch',

	mixins: [events],

	props: {

		// 是否设计者测试
		mode: {
			type: [String],
			default: 'production',
		},

		mcode: {
			type: [Number, String],
			default: '',
		},

		mid: {
			type: [Number, String],
			default: '',
		},

	},

	provide () {
		return {
			formEditor: this,
		}
	},

	created () {
	},

	beforeDestroy () {
	},

	data () {

		let userInfo = this.$datax.get('userInfo')
		let ctx = {
			...toRaw(userInfo),
		}

		return {

			name: 'xxxxxxxxx',

			ctx,

			form: {
				props: {
					name: '',
					code: '',
				},
			},

			service: this.$svc.sys.custom['c-json'],

			formData: {},

			search: schema.search,
		}
	},

	methods: {
		Promise,
		arrayToObject,
		uniqAndCleanArray,

// 列表方法
		handleQuery () {
			this.$nextTick(() => {
				this.$refs.table.doSearch({
					...this.formData,
					...arrayToObject(uniqAndCleanArray(this.search.table.props.param)),
					fid: this.search.props.fid,
				})
			})
		},
		// 新增方法
		handleAdd () {
			let param = {
				mid: this.mid,
			}
			let options = {
				overflow: 'auto',
			}
			// 打开新增界面弹出窗
			this.$refs.TpDialog.show('/sys/custom/form/runtime-add.vue', param, '新增', options)
		},

		// 导出方法
		handleExport () {
			this.$message('handleExport')
		},

		// 修改方法
		handleEdit (row) {
			let param = {
				mid: this.mid,
				...toRaw(row),
			}
			let options = {
				overflow: 'auto',
			}
			this.$refs.TpDialog.show('/sys/custom/form/runtime-add.vue', param, '修改', options)
		},
		// 查看方法
		handleView (row) {
			let param = {
				mid: this.mid,
				...toRaw(row),
			}
			let options = {
				overflow: 'auto',
			}
			this.$refs.TpDialog.show('/sys/custom/form/runtime-view.vue', param, '查看', options)
		},
		// 删除方法
		handleDel (row) {

			let param = row
			let options = {}

			this.$confirm('确定要删除吗？', () => {
				this.service.delete({
					fid: this.search.props.fid,
					ids: row.id,
				}).then((result) => {
					if (result.code === 1) {
						this.$message.success('删除成功')
						this.handleQuery()
					} else {
						// 服务器返回失败
						this.$message.error('删除失败: ' + result.message)
					}
				})
			})

		},
		closeDialog (param) {
			this.handleQuery()
		},

		getService (servicePath) {
			let svc = get(app.$svc, servicePath)
			return svc ? svc : null
		},

		dataParse (res) {
			if (res.data.total > 0) {
				res.data.records.forEach((value, index, array) => {
					value.detail = JSON.parse(value.dataDetail)
				})
			}

			return {
				data: res.data.records,
				pager: {
					total: res.data.total,
					size: res.data.size,
					current: res.data.current,
					pages: res.data.pages,
				},
			}
		},


	},

	async mounted () {

		let mcode = this.mcode
		let mid = this.mid
		let service
		let list



		if (this.mode === 'test') {
			list = await this.$svc.sys.custom.form.mid({
				mid,
				ftype: 'list',
				fSource: 'pc',
			}).then(res => res.data)

		} else {
			list = await this.$svc.sys.custom.form.mcode({
				mcode,
				ftype: 'list',
				fSource: 'pc',
			}).then(res => res.data)

		}

		this.form.props = {
			mid: list.mid,
			name: 'name',
			code: list.fjson.replace('C_JSON_', ''),
		}

		if (list === '' || list.fjson === '') {
			this.$message.error('表单未定义，请联系管理员')
		} else {
			let search = JSON.parse(list.fjson)

			search.table.props.formatters = search.table.props.formatters || {}

			search.table.props.columns.forEach((value) => {

				if (value.show && value.name !== 'op' && !value.slot) {
					search.table.props.formatters[value.name] = (value, row, column) => {
						return row.detail.label[column.name]
					}
				}

			})

			this.search = search
		}
		if (list.fid) {
			this.search.props.fid = list.fid
		}
		let formData = {}

		this.search.query.rows.forEach((n, i) => {
			let queryName = n.type && n.formItemProps && n.formItemProps.name
			formData[queryName] = ''
			formData[`${queryName}$Raw`] = []
			formData[`${queryName}$Label`] = ''

			if (n.type === 'tp-datepicker' || n.type === 'biz-current-date-time') {
				formData[`${queryName}Max`] = ''
				formData[`${queryName}Min`] = ''
			}
			if (n.type === 'biz-org-person-single-select') {
				formData[queryName] = ''
				formData[`${queryName}$Label`] = ''
			}
		})

		this.formData = formData

		this.$nextTick(() => {
			this.handleQuery()
		})

	},
}
</script>

<style lang="less" scoped>

</style>
