<template>
	<fb-container>

		<fb-container mb="8px">

		</fb-container>

		<fb-container mb="8px">
			<fb-space>

				<fb-button @on-click="syncColProps" type="primary">更新表单属性</fb-button>
				<fb-button @on-click="syncSearch" danger>同步查询项</fb-button>
				<fb-button @on-click="reventSearch">清空</fb-button>
				<!--				<fb-button @on-click="formEditor.editor.pageTab = 'previewSearch'">预览</fb-button>-->
			</fb-space>
		</fb-container>


		<fb-switch-accordion-group>

			<fb-switch-accordion title="查询表单"
			                     :value="search.props.show_query"
			                     @input="(value)=>{updateProps(value, 'props','show_query')}"
			                     :body-style="{padding: '8px'}"
			>
				<fb-form label-position="left" :label-width="100"
				         :label-style="{lineHeight: 'normal', paddingBottom: '2px'}"
				>

					<fb-form-item label="布局" label-position="top" prop="labelWidth">
						<fb-radio-group
							:radio-space="12"
							:value="search.query.props.layout"
							@input="(value)=>{updateProps(value, 'query','props', 'layout')}"
							:data="[
								   	{label: '一行二列', value: 'row-12'},
								   	{label: '一行三列', value: 'row-8'},
								   ]"
						>
						</fb-radio-group>
					</fb-form-item>

					<fb-form-item label="查询项名称宽度" :label-width="120" label-position="top" prop="labelWidth">
						<fb-input-number
							:value="search.query.props.labelWidth"
							@input="(value)=>{updateProps(value, 'query','props', 'labelWidth')}"
							append="px"
						>
						</fb-input-number>
					</fb-form-item>


				</fb-form>
				<fb-fieldset label="查询项配置"></fb-fieldset>

				<template v-if="search.query.rows.length === 0">
					<fb-empty text="查询项为空, 请先同步" size="s"></fb-empty>
				</template>

				<fb-container padding="4px" border="1px solid #E8E8E8" radius="4px">
					<fb-container>
						<fb-table-layout size="s" long>
							<fb-table-layout-cell width="30px" padding="2px">显示</fb-table-layout-cell>
							<fb-table-layout-cell width="130px" padding="2px">名称</fb-table-layout-cell>
							<fb-table-layout-cell width="80px" padding="2px">查询类型</fb-table-layout-cell>
						</fb-table-layout>
					</fb-container>

					<template v-for="(col, j) in search.query.rows">
						<fb-container :background="(j+1)%2===0 ? '' : '#e6f2ff'" bt="1px solid #eee">
							<fb-table-layout size="s" long border="1px solid red">
								<fb-table-layout-cell width="30px" padding="2px">
									<fb-checkbox
										:value="col.formItemProps.show"
										@input="(value)=>{updateProps(value, 'query','rows', j, 'formItemProps','show')}"/>
								</fb-table-layout-cell>
								<fb-table-layout-cell width="130px" padding="2px" valign="middle">
									<fb-input
										:value="col.formItemProps.label"
										@input="(value)=>{updateProps(value, 'query','rows', j, 'formItemProps','label')}"
									></fb-input>
								</fb-table-layout-cell>
								<fb-table-layout-cell width="80px" padding="2px">
									<fb-select
										:value="col.formItemProps.searchType"
										@input="(value)=>{updateProps(value, 'query','rows', j, 'formItemProps','searchType')}"
										:data="(()=>{  ; return getComponentByType(col.type).searchTypes})()"
									/>
								</fb-table-layout-cell>
							</fb-table-layout>
							<fb-table-layout size="s" long border="1px solid red">
								<fb-table-layout-cell width="30px">
									<fb-container width="30px">
										<fb-table-layout>
											<fb-table-layout-cell align="center">
												<fb-icon v-show="j !== 0" size="s" name="up-arrow"
												         @on-click="()=>{moveQueryRow('up', j)}"
												         style="cursor:pointer;"
												></fb-icon>
											</fb-table-layout-cell>
											<fb-table-layout-cell align="center">
												<fb-icon v-show="j !== ( search.query.rows.length - 1)" size="s"
												         name="down-arrow"
												         @on-click="()=>{moveQueryRow('down', j)}"
												         style="cursor:pointer;"></fb-icon>
											</fb-table-layout-cell>
										</fb-table-layout>
									</fb-container>
								</fb-table-layout-cell>
								<fb-table-layout-cell padding="2px" valign="middle">
									<fb-input
										prepend="占位符"
										:placeholder="col.props.placeholder"
										:value="col.props.placeholder"
										@input="(value)=>{updateProps(value, 'query','rows', j, 'props','placeholder')}"
									></fb-input>
								</fb-table-layout-cell>
							</fb-table-layout>
							<template v-if="['fb-radio-group'].includes(col.type)">
								<fb-table-layout size="s" long>
									<fb-table-layout-cell width="30px" padding="2px"></fb-table-layout-cell>
									<fb-table-layout-cell padding="2px">
										<fb-checkbox label="显示 [全部] 项"
										             :value="col.formItemProps.searchOptionAll"
										             @input="(value)=>{updateProps(value, 'query','rows', j, 'formItemProps','searchOptionAll')}"
										             :data="getComponentByType(col.type).searchOptionAll"
										></fb-checkbox>
									</fb-table-layout-cell>
								</fb-table-layout>
							</template>
						</fb-container>
						<!--					<fb-container v-if="col.formItemProps.searchType === 'dept'" margin="4px 2px 4px 32px">-->
						<!--						<fb-container mb="4px">部门选项</fb-container>-->
						<!--						<fb-textarea placeholder="部门选项"-->
						<!--						             :value="col.formItemProps.searchIn"-->
						<!--						             @input="(value)=>{updateProps(value, 'query','rows', j, 'formItemProps','searchIn')}"-->
						<!--						></fb-textarea>-->
						<!--					</fb-container>-->
						<!--					<fb-container v-if="col.formItemProps.searchType === 'city'" margin="4px 2px 4px 32px">-->
						<!--						<fb-container mb="4px">行政区划选项</fb-container>-->
						<!--						<fb-textarea placeholder="行政区划选项"-->
						<!--						             :value="col.formItemProps.searchIn"-->
						<!--						             @input="(value)=>{updateProps(value, 'query','rows', j, 'formItemProps','searchIn')}"-->
						<!--						></fb-textarea>-->
						<!--					</fb-container>-->
						<!--					<fb-container v-if="col.formItemProps.searchType === 'org'" margin="4px 2px 4px 32px">-->
						<!--						<fb-container mb="4px">组织机构选项</fb-container>-->
						<!--						<fb-textarea placeholder="组织机构选项"-->
						<!--						             :value="col.formItemProps.searchIn"-->
						<!--						             @input="(value)=>{updateProps(value, 'query','rows', j, 'formItemProps','searchIn')}"-->
						<!--						></fb-textarea>-->
						<!--					</fb-container>-->
						<!--					<fb-container v-if="col.formItemProps.searchType === 'dict'" margin="4px 2px 4px 32px">-->
						<!--						<fb-container mb="4px">字典选项</fb-container>-->
						<!--						<fb-textarea placeholder="字典选项"-->
						<!--						             :value="col.formItemProps.searchIn"-->
						<!--						             @input="(value)=>{updateProps(value, 'query','rows', j, 'formItemProps','searchIn')}"-->
						<!--						></fb-textarea>-->
						<!--					</fb-container>-->


						<!--					<fb-container v-if="col.formItemProps.searchType === 'in'" margin="4px 2px 4px 32px">-->
						<!--						<fb-container mb="4px">请输入in条件，每行一个</fb-container>-->
						<!--						<fb-textarea placeholder="in条件"-->
						<!--									 :value="col.formItemProps.searchIn"-->
						<!--									 @input="(value)=>{updateProps(value, 'query','rows', j, 'formItemProps','searchIn')}"-->
						<!--						></fb-textarea>-->
						<!--					</fb-container>-->

						<!--					<fb-container v-if="col.type === 'tp-datepicker'">-->
						<!--						tp-datepicker: 附加-->
						<!--					</fb-container>-->

					</template>

				</fb-container>
			</fb-switch-accordion>

			<fb-switch-accordion title="数据列表"
			                     :value="search.props.show_table"
			                     @input="(value)=>{updateProps(value, 'props','show_table')}"
			>

				<fb-container></fb-container>

				<fb-form label-position="top">

					<fb-fieldset label="基础属性"></fb-fieldset>

					<fb-container margin="4px 0">表头</fb-container>
					<fb-container>
						<fb-checkbox
							:value="search.table.props.showHeader"
							:label="'显示表头'"
							@input="(value)=>{updateProps(value, 'table','props', 'showHeader')}"/>
					</fb-container>
					<fb-table-layout v-if="search.table.props.showHeader" size="s" long>
						<fb-table-layout-cell padding="0 2px 0 0">
							<fb-checkbox
								:value="search.table.props.longHeadSplitter"
								:label="'表头长分割线'"
								@input="(value)=>{updateProps(value, 'table','props', 'longHeadSplitter')}"/>
						</fb-table-layout-cell>
						<fb-table-layout-cell padding="0 0 0 2px">
							<fb-checkbox
								:value="search.table.props.noHeadSplitter"
								label="表头无分割线"
								@input="(value)=>{updateProps(value, 'table','props', 'noHeadSplitter')}"/>
						</fb-table-layout-cell>
					</fb-table-layout>


					<fb-container>
						<fb-checkbox
							:value="search.table.props.rownum"
							:label="'显示行号'"
							@input="(value)=>{updateProps(value, 'table','props', 'rownum')}"/>
					</fb-container>
					<fb-table-layout v-if="search.table.props.rownum" size="s" long>
						<fb-table-layout-cell padding="2px">
							<fb-input
								placeholder="标题"
								:value="search.table.props.rownumTitle"
								@input="(value)=>{updateProps(value, 'table','props','rownumTitle')}"
							>
								<template slot="prepend">名称</template>
							</fb-input>
						</fb-table-layout-cell>
						<fb-table-layout-cell padding="2px">
							<fb-input
								:value="search.table.props.rownumWidth"
								@input="(value)=>{updateIntegerProps(value, 'table','props','rownumWidth')}"
								:el-style="{padding: '0 4px', textAlign: 'center'}"
							>
								<template slot="prepend">宽</template>
								<template slot="append">px</template>
							</fb-input>
						</fb-table-layout-cell>
					</fb-table-layout>

					<fb-container margin="4px 0">滚动条</fb-container>
					<fb-table-layout size="s" long>
						<fb-table-layout-cell padding="2px">
							<fb-input
								:value="search.table.props.scroll.x"
								@input="(value)=>{updateIntegerProps(value, 'table','props','scroll', 'x')}"
								:el-style="{
									padding: '0 4px', textAlign: 'center'
								}"
								clearable
							>
								<template slot="prepend">x</template>
								<template slot="append">px</template>
							</fb-input>
						</fb-table-layout-cell>
						<fb-table-layout-cell padding="2px">
							<fb-input
								:value="search.table.props.scroll.y"
								@input="(value)=>{updateIntegerProps(value, 'table','props','scroll', 'y')}"
								:el-style="{padding: '0 4px', textAlign: 'center'}"
								clearable
							>
								<template slot="prepend">y</template>
								<template slot="append">px</template>
							</fb-input>
						</fb-table-layout-cell>
					</fb-table-layout>
					<fb-table-layout size="s" long>
						<fb-table-layout-cell width="50%">
						</fb-table-layout-cell>
						<fb-table-layout-cell width="50%" padding="0 2px">
							<fb-checkbox
								:value="search.table.props.scroll.autoHeight"
								:label="'高度自适应'"
								@input="(value)=>{
									updateProps(value, 'table','props', 'scroll', 'autoHeight')
								}">
							</fb-checkbox>
						</fb-table-layout-cell>
					</fb-table-layout>
					<fb-table-layout size="s" long>
						<fb-table-layout-cell width="50%">
						</fb-table-layout-cell>
						<fb-table-layout-cell width="50%" padding="0 2px">
							<fb-checkbox
								:value="search.table.props.scroll.fillY"
								:label="'高度填充'"
								@input="(value)=>{
									updateProps(value, 'table','props', 'scroll', 'fillY')
								}"/>
						</fb-table-layout-cell>
					</fb-table-layout>


					<fb-container margin="4px 0">尺寸</fb-container>
					<fb-container>
						<fb-radio-group
							button
							:value="search.table.props.size"
							@input="(value)=>{updateProps(value, 'table','props','size')}">
							<fb-radio label="小" value="s"/>
							<fb-radio label="中" value="m"/>
							<fb-radio label="大" value="l"/>
						</fb-radio-group>
					</fb-container>

					<fb-container margin="4px 0">
						<fb-checkbox
							:value="search.table.props.bordered"
							:label="'边框'"
							@input="(value)=>{updateProps(value, 'table','props', 'bordered')}"/>

						<fb-checkbox
							:value="search.table.props.headBordered"
							label="表头边框"
							@input="(value)=>{updateProps(value, 'table','props', 'headBordered')}"/>

					</fb-container>


					<fb-fieldset label="样式控制"></fb-fieldset>


					<fb-container>
						<fb-checkbox
							:value="search.table.props.fixed"
							:label="'自适应宽度'"
							@input="(value)=>{updateProps(value, 'table','props', 'fixed')}"/>
					</fb-container>
					<fb-container>
						<fb-checkbox
							:value="search.table.props.noEmpty"
							:label="'不显示空数据提示'"
							@input="(value)=>{updateProps(value, 'table','props', 'noEmpty')}"/>
					</fb-container>

					<fb-form-item v-if="!search.table.props.noEmpty" label="空数据提示" label-position="top"
					              prop="noDataText">
						<fb-input
							placeholder="没有数据"
							:value="search.table.props.noDataText"
							@input="(value)=>{updateProps(value, 'table','props','noDataText')}"
						>
						</fb-input>
					</fb-form-item>


					<fb-container>
						<fb-checkbox
							:value="search.table.props.autoScroll"
							:label="'数据行滚动'"
							@input="(value)=>{updateProps(value, 'table','props', 'autoScroll')}"/>
					</fb-container>

					<fb-form-item v-if="search.table.props.autoScroll === true" label="滚动速度" label-position="top"
					              prop="autoScrollSpeed">
						<fb-input
							:value="search.table.props.autoScrollSpeed"
							@input="(value)=>{updateIntegerProps(value, 'table','props', 'autoScrollSpeed')}"/>
					</fb-form-item>
					<fb-form-item v-if="search.table.props.autoScroll === true" label="滚动首尾延时" label-position="top"
					              prop="autoScrollDelay">
						<fb-input
							:value="search.table.props.autoScrollDelay"
							@input="(value)=>{updateIntegerProps(value, 'table','props', 'autoScrollDelay')}">
							<template slot="append">毫秒(ms)</template>
						</fb-input>
					</fb-form-item>
					<fb-form-item v-if="search.table.props.autoScroll === true" label="滚动步进" label-position="top"
					              prop="autoScrollStep">
						<fb-input
							:value="search.table.props.autoScrollStep"
							@input="(value)=>{updateIntegerProps(value, 'table','props', 'autoScrollStep')}">
							<template slot="append">px</template>
						</fb-input>
					</fb-form-item>

					<fb-container>
						<fb-checkbox
							:value="search.table.props.autoSelect"
							:label="'点击行选中'"
							@input="(value)=>{updateProps(value, 'table','props', 'autoSelect')}"/>
					</fb-container>

					<fb-fieldset label="列配置(columns)"></fb-fieldset>
					<fb-container>
						<fb-form-editor-columns
							:value="search.table.props.columns"
							@input="(value)=>{
								updateProps(JSON.parse(JSON.stringify(value)), 'table','props', 'columns')
							}"
						/>
					</fb-container>

					<fb-fieldset label="数据相关"></fb-fieldset>
					<fb-form-item label="行主键(pk)">
						<!--						<fb-input-->
						<!--							placeholder="自动生成"-->
						<!--							:value="search.table.props.pk"-->
						<!--							@input="(value)=>{updateProps(value, 'table','props', 'pk')}"-->
						<!--						/>-->

						<fb-select :data="(()=>{
							let data = []
							data.push({
								value: 'rownum',
								label: '自增行号'
							})

							search.table.props.columns.map((n,i)=>{
								if (n.name !== 'op')  {
									data.push({
									value: n.name,
									label: `${n.label } - ${n.name }`
								  })
								}

							})

							return data
						})()"

						           :value="search.table.props.pk"
						           @input="(value)=>{updateProps(value, 'table','props', 'pk')}"
						>

						</fb-select>
					</fb-form-item>

					<fb-form-item label="模拟数据(data)" prop="data">
						<fb-button
							style="margin-right: 8px"
							@on-click="updateProps((()=>{
							let data = times(10, (n)=>{
								let row = {}
								search.table.props.columns.forEach((m,j)=>{
									row[m.name] = `${m.name}-${n}-${j}`
								})
								return row
							})

							return data
						})(), 'table','props', 'mockData')"
						>10行
						</fb-button>
						<fb-button @on-click="updateProps([], 'table','props', 'mockData')"
						           style="margin-right: 8px">
							空数据
						</fb-button>

					</fb-form-item>

					<!--

					<fb-form-item label="接口数据(service)" prop="service">
						<fb-tree-select
							:value="search.table.props.service"
							@input="(value)=>{updateProps(value, 'table','props', 'service')}"
							:data="formEditor.editor.services"
							:header-format="(node)=>{
								return node && node.value
							}"
						/>
					</fb-form-item>

					-->
					<fb-form-item label="查询参数(param)" prop="param">
						<fb-form-editor-object-prop
							:value="search.table.props.param"
							@input="(value)=>{
								updateProps(value, 'table','props', 'param')
							}"
						/>
					</fb-form-item>

					<fb-form-item label="数据适配器(reader)" prop="reader">
						<fb-form-editor-object-prop
							:friendly-props="{
							      pagerSize: '单页数量',
							      pagerCurrent: '当前页',
							      pagerTotal: '总记录数',
							      pagerPages: '总页数',
							      sortByColumn: '排序字段名',
							      sortByDirection: '排序顺序',
							}"
							:value="search.table.props.reader"
							@input="(value)=>{updateProps(value, 'table','props', 'reader')}"
							:remove-button="false"
							:add-button="false"
						/>
					</fb-form-item>

					<fb-switch-accordion-group>
						<fb-switch-accordion title="分页栏"
						                     :value="!search.table.props.noPager"
						                     @input="(value)=>{updateProps(!value, 'table','props', 'noPager')}"
						                     :body-style="{padding: '4px'}"
						>

							<fb-container v-if="search.table.props.noPager === false">
								<fb-checkbox
									:value="search.table.props.showPager"
									:label="'显示分页栏'"
									@input="(value)=>{updateProps(value, 'table','props', 'showPager')}"/>
							</fb-container>
							<fb-form-item
								:style="{paddingBottom: 0}"
								v-if="search.table.props.showPager === true && search.table.props.noPager === false"
								label="分页栏(pager)" prop="pager">
								<fb-form-editor-object-prop
									:value="search.table.props.pager"
									@input="(value)=>{updateProps(value, 'table','props', 'pager')}"
									:remove-button="false"
									:add-button="false"
									:selects="{
								position: [
									 {label: '底部', value: 'bottom'},
								 ],
								 align: [
									 {label: '左', value: 'left'},
									 {label: '中', value: 'center'},
									 {label: '右', value: 'right'},
								 ]
							}"
									:friendly-props="{
							      position: '位置',
							      align: '对齐方式',
							      simple: '简洁模式',
							      current: '当前页',
							      size: '每页数',
							      pages: '总页数',
							      showTotalInfo: '显示总数',
							}"
								/>
							</fb-form-item>

						</fb-switch-accordion>
					</fb-switch-accordion-group>

					<!--					<fb-container>-->
					<!--						<fb-checkbox-->
					<!--							:value="search.table.props.noPager"-->
					<!--							:label="'不加载分页栏'"-->
					<!--							@input="(value)=>{updateProps(value, 'table','props', 'noPager')}"/>-->
					<!--					</fb-container>-->


					<!--					//search.table.props.param-->

				</fb-form>
			</fb-switch-accordion>
			<fb-card header="操作按钮" no-border shadow="off"

			>

				<fb-table-layout>
					<fb-table-layout-cell width="30px" padding="0 2px 2px 0">显示</fb-table-layout-cell>
					<fb-table-layout-cell width="42px" padding="0 2px 0 2px">图标</fb-table-layout-cell>
					<fb-table-layout-cell padding="0 2px 0 2px">名称</fb-table-layout-cell>
					<fb-table-layout-cell padding="0 0 2px 2px">编码</fb-table-layout-cell>
				</fb-table-layout>

				<fb-table-layout v-for="(value, key) in search.actions" :key="key">
					<fb-table-layout-cell width="30px" align="center" padding="0 2px 2px 0">
						<fb-checkbox
							:value="search.actions[key].show"
							@input="(value)=>{updateProps(value,'actions', key, 'show')}"/>
					</fb-table-layout-cell>
					<fb-table-layout-cell width="42px" valign="middle" padding="0 0 2px 2px">
						<fb-icon-select
							label=""
							size="s"
							position="topRight"
							:value="search.actions[key].icon"
							@input="(value)=>{updateProps(value,'actions', key, 'icon')}"
						></fb-icon-select>
					</fb-table-layout-cell>
					<fb-table-layout-cell padding="0 0 2px 2px">
						<fb-input
							:el-style="{paddingLeft: '6px', paddingRight: '6px'}"
							:value="search.actions[key].label"
							@input="(value)=>{updateProps(value,'actions', key, 'label')}"
						></fb-input>
					</fb-table-layout-cell>
					<fb-table-layout-cell padding="0 0 2px 2px">
						<fb-input
							:el-style="{paddingLeft: '6px', paddingRight: '6px'}"
							:value="search.actions[key].code"
							@input="(value)=>{updateProps(value,'actions', key, 'code')}"
						></fb-input>
					</fb-table-layout-cell>
				</fb-table-layout>


				<!--				<fb-checkbox-->
				<!--					:value="search.actions.add.show"-->
				<!--					:label="'新增'"-->
				<!--					@input="(value)=>{updateProps(value, 'table','props', 'autoSelect')}"/>-->

			</fb-card>
			<!--			<fb-switch-accordion title="操作"-->
			<!--								 :value="search.props.show_actions"-->
			<!--								 @input="(value)=>{updateProps(value, 'props','show_actions')}"-->
			<!--			></fb-switch-accordion>-->
		</fb-switch-accordion-group>


	</fb-container>
</template>

<script>/**
 * FbFormEditorSearchPropsPanel.vue
 * (c) 2021 lincong1987
 */
import { closest } from '../../../../../util/componentUtils'
import { isNumber, toNumber, toInteger, isSafeInteger, isInteger, times } from 'lodash-es'
import FbFormEditorObjectEditor from '../../../../../views/sys/custom/form/form-editor/FbFormEditorObjectEditor'
import FbFormEditorArrayProp from '../../../../../views/sys/custom/form/form-editor/FbFormEditorArrayProp'
import FbFormEditorColumns from '../../../../../views/sys/custom/form/form-editor/FbFormEditorColumns'
import FbFormEditorObjectProp from '../../../../../views/sys/custom/form/form-editor/FbFormEditorObjectProp'

export default {
	name: 'FbFormEditorSearchPropsPanel',
	components: {
		FbFormEditorObjectProp,
		FbFormEditorColumns,
		FbFormEditorArrayProp,
		FbFormEditorObjectEditor,
	},
	props: {
		search: {},
	},
	inject: ['formEditor'],
	data () {

		return {}
	},
	methods: {
		times,
		toInteger,

		toNumber,

		isNumber,

		checkNumber (value) {
			return this.isNumber(this.toNumber(value))
		},
		checkInteger (value) {
			return isSafeInteger(this.toInteger(value))
		},

		updateProps (value, props, prop) {
			let arr = Array.prototype.slice.call(arguments)
			this.formEditor.updatePathProps.apply(this, ['search'].concat(arr))
		},

		updateIntegerProps (value, props, prop) {
			let arr = Array.prototype.slice.call(arguments)
			if (this.checkInteger(arr[0])) {
				arr[0] = toInteger(arr[0])
				this.formEditor.updatePathProps.apply(this, ['search'].concat(arr))
			}
		},

		handleQueryFormFieldsChange (value) {

		},

		syncColProps () {
			this.$confirm('此操作将更新表单项的基础信息至列表！', () => {
				this.formEditor.syncColProps()
				this.$message.success('已从新增页更新表单项数据至列表查询页')
			})

		},

		syncSearch () {
			this.$confirm('未保存的列表查询页数据将被清空！', () => {
				this.formEditor.syncSearch()
				this.$message.success('已从新增页同步数据至列表查询页')
			})
		},

		reventSearch () {
			this.$confirm('点击[确认]将清空列表查询页数据！', () => {
				this.formEditor.revertForm('search')
				this.$message.success('已清空列表查询页数据')
			})
		},

		handleQueryFormLayoutChange (value) {
			this.formEditor.search.query.props.layout = value
		},

		getComponentByType (type) {
			return this.formEditor.getComponentByType(type)
		},

		moveQueryRow (position, index) {

			let maxIndex = this.search.query.rows.length

			if (position === 'up') {
				if (index === 0) {
					this.$message.error('已经是！')
					return
				}
				let row = this.search.query.rows[index - 1]
				this.$set(this.search.query.rows, index - 1, this.search.query.rows[index])
				this.$nextTick(() => {
					this.$set(this.search.query.rows, index, row)
				})
			}

			if (position === 'down') {
				if (index + 1 >= maxIndex) {
					this.$message.error('最后一行不得下移！')
					return
				}
				let row = this.search.query.rows[index + 1]
				this.$set(this.search.query.rows, index + 1, this.search.query.rows[index])
				this.$nextTick(() => {
					this.$set(this.search.query.rows, index, row)
				})
			}

			//this.updateRow()

		},

	},

}
</script>

<style lang="less" scoped>

</style>
