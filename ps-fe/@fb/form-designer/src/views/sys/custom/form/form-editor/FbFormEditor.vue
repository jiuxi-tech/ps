<template>
	<fb-container
		class="fb-form-designer"
	>
		<fb-layout theme="blank" style="height: 100%;">
			<fb-layout-header>
				<fb-container class="editor-panel-toolbar" radius="4px">
					<fb-card :title-style="{padding: 0}" no-padding margin="0 0 8px 0">
						<fb-tabs slot="title" v-model="editor.pageTab"
								 style="background: #fff; border-radius: 4px">
							<!--							<fb-tab label="字段设置" name="design"></fb-tab>-->
							<fb-tab no-padding label="表单项" name="add"></fb-tab>
							<!--							<fb-tab no-padding label="查看页" name="view"></fb-tab>-->
							<fb-tab no-padding label="列表查询" name="search"></fb-tab>
							<!--							<fb-tab no-padding label="操作指南" name="guide"></fb-tab>-->
							<!--							<fb-tab no-padding label="历史记录" name="history"></fb-tab>-->
							<!--							<fb-tab no-padding label="预览列表查询页" name="previewSearch"></fb-tab>-->
							<fb-tab no-padding label="权限" name="permission"></fb-tab>
						</fb-tabs>

						<template slot="actions">

							<!--							<fb-button icon="" @on-click="showFormProperties" style="margin-right: 8px">表单属性</fb-button>-->

							<fb-button type="primary"
									   icon="file-text"
									   style="margin-right: 8px;"
									   @on-click="()=>{$refs.formPropertiesDialog.show()}">表单默认值配置
							</fb-button>

							<fb-button type="primary"
									   icon="file-abnormal"
									   style="margin-right: 8px;"
									   @on-click="doTest()">测试
							</fb-button>

							<fb-button type="primary"
									   icon="file-search"
									   style="margin-right: 8px;"
									   @on-click="()=>{showPreviewDialog('add')}">新增预览
							</fb-button>


							<!--								<fb-button type="primary"-->
							<!--								           style="margin-right: 8px;"-->
							<!--								           @on-click="showPreviewAddFormDataDialog()">新增表单结构-->
							<!--								</fb-button>-->

							<fb-button type="primary"
									   icon="routine-inspections"
									   style="margin-right: 8px;"
									   @on-click="()=>{showPreviewDialog('view')}">查看预览
							</fb-button>

							<fb-button type="primary" icon="save" @on-click="doSave">保 存</fb-button>
						</template>
					</fb-card>
				</fb-container>
			</fb-layout-header>

			<fb-layout theme="blank">
				<fb-layout-body style="width: calc(100% - 448px); overflow: hidden"

				>
					<fb-card v-if="editor.pageTab === 'add'"
							 class="editor-panel-designer"
					>

						<fb-page-header :title="form.props.name" :sub-title="form.props.code">

							<template slot="actions">
								<fb-button
									style="margin-right: 8px;"
									@on-click="$confirm('真的要清空吗？', ()=>{revertForm('add')})">清空
								</fb-button>


							</template>
							<fb-divider no-margin/>

							<fb-form
								class="editor-form"
								mode="form"
								:label-width="add.props.labelWidth"
								:label-position="add.props.labelPosition"
							>

								<fb-empty v-if="add.rows.length === 0" size="l" text="你的表单还是空白的哦~快来添加一条吧"/>

								<fb-form-editor-add-row :row-index="-1" view-type="add"
														:always-show="add.rows.length === 0"/>

								<template v-for="(row, rowIndex) in add.rows">

									<!----------------------------------
										 单列 布局
									 ---------------------------------->
									<template v-if="row.type === 'row-24'">
										<fb-row class="editor-row editor-row--row-24 ">
											<fb-form-editor-row-toolbar view-type="add" :row-index="rowIndex"
																		row-type="row-24"/>
											<fb-form-editor-col
												:col="row.cols[0]"
												:col-index="0"
												view-type="add" :row-index="rowIndex" row-type="row-24"
												:active-component="activeComponent"
											/>
											<fb-container absolute left="-50px" top="-10px" width="40px" align="right">
												<fb-text size="xxl" :color="colors.grey_8">{{ rowIndex + 1 }}</fb-text>
											</fb-container>
										</fb-row>
									</template>

									<!----------------------------------
										 双列 布局
									---------------------------------->
									<template v-if="row.type === 'row-12'">
										<fb-row class="editor-row editor-row--row-12 ">
											<fb-form-editor-row-toolbar view-type="add" :row-index="rowIndex"
																		row-type="row-12"/>
											<fb-form-editor-col
												:col="row.cols[0]"
												:col-index="0"
												view-type="add" :row-index="rowIndex" row-type="row-12"
												:active-component="activeComponent"
											/>

											<fb-form-editor-col
												:col="row.cols[1]"
												:col-index="1"
												view-type="add" :row-index="rowIndex" row-type="row-12"
												:active-component="activeComponent"
											/>

											<fb-container absolute left="-50px" top="-10px" width="40px" align="right">
												<fb-text size="xxl" :color="colors.grey_8">{{ rowIndex + 1 }}</fb-text>
											</fb-container>
										</fb-row>
									</template>

									<fb-form-editor-add-row :row-index="rowIndex" view-type="add"/>
								</template>

							</fb-form>

						</fb-page-header>

					</fb-card>

					<fb-card v-if="editor.pageTab === 'view'"
							 class="editor-panel-designer"
					>
						<fb-page-header :title="form.props.name" :sub-title="form.props.code">
							<template slot="actions">
								<fb-button
									style="margin-right: 8px;"
									@on-click="revertForm('view')">清空
								</fb-button>
								<!--								<fb-button-->
								<!--									style="margin-right: 8px;"-->
								<!--									@on-click="syncView">从新增同步-->
								<!--								</fb-button>-->
								<fb-button slot="actions" type="primary" @on-click="showPreviewDialog('view')">预览
								</fb-button>
							</template>
							<fb-divider no-margin/>

							<fb-property colon :body-style="{overflow: 'visible'}">
								<div>
									<fb-empty v-if="view.rows.length === 0" text="查看表单无数据，请先同步">
										<fb-container margin="8px">
											<fb-button @on-click="syncView" icon="refresh">同 步</fb-button>
										</fb-container>
									</fb-empty>

									<template v-for="(row, rowIndex) in view.rows">

										<fb-row v-if="row.type === 'row-24'" class="editor-row editor-row--row-24 ">
											<fb-form-editor-row-view-toolbar
												:row-index="rowIndex"
												row-type="row-24"
												view-type="view"/>

											<fb-form-editor-col-view
												:col="row.cols[0]"
												:col-index="0"
												view-type="view"
												:row-index="rowIndex"
												row-type="row-24"
											/>
										</fb-row>

										<fb-row v-if="row.type === 'row-12'" class="editor-row editor-row--row-12 ">
											<fb-form-editor-row-view-toolbar
												:row-index="rowIndex"
												row-type="row-24"
												view-type="view"/>

											<fb-form-editor-col-view
												:col="row.cols[0]"
												:col-index="0"
												view-type="view"
												:row-index="rowIndex"
												row-type="row-12"
											/>

											<fb-form-editor-col-view
												:col="row.cols[1]"
												:col-index="1"
												view-type="view"
												:row-index="rowIndex"
												row-type="row-12"
											/>
										</fb-row>

									</template>

								</div>

							</fb-property>
						</fb-page-header>
					</fb-card>

					<fb-card v-if="editor.pageTab === 'search'"
							 class="editor-panel-designer" no-padding no-border>

						<fb-form-editor-search-panel :search="search"/>

					</fb-card>

					<fb-card v-if="editor.pageTab === 'previewSearch'"
							 class="editor-panel-designer">
						previewSearch
					</fb-card>

					<fb-card v-if="editor.pageTab === 'code'"
							 class="editor-panel-designer"
					>
						<fb-page-header title="Talk is cheap. Show me the code."
										:title-style="{fontFamily:'monospace'}"
						>
							<fb-divider no-margin>列表</fb-divider>
							<pre>{{ JSON.stringify(search, null, '\t') }}</pre>
							<fb-divider no-margin>新增</fb-divider>
							<pre>{{ JSON.stringify(add, null, '\t') }}</pre>
							<fb-divider no-margin>查看</fb-divider>
							<pre>{{ JSON.stringify(view, null, '\t') }}</pre>

						</fb-page-header>
					</fb-card>

					<fb-card v-if="editor.pageTab === 'guide'">
						<fb-page-header title="请严格按照操作指南进行生产"
										:title-style="{}"
						>

							<p>1、点击《表单项》选项卡，进行表单项编辑。</p>
							<p>2、点击《列表查询》选项卡，再点击《同步查询项》或《清空》按钮完成《表单项》数据同步。</p>
							<p>3、点击《列配置》按钮，配置查询列表。</p>

							<p>注意：在任意时刻，你都可以进行保存操作。</p>

						</fb-page-header>
					</fb-card>

					<fb-card v-if="editor.pageTab === 'permission'">
						<fb-page-header title="权限配置"
										:title-style="{}"
						>

							<fb-button slot="actions" type="primary" @on-click="()=>{
								permission.selectedNode = null
								$refs['add-to-menu'].show()
							}">一键添加菜单
							</fb-button>

							<fb-fieldset label="发布配置 - 菜单配置 - 请复制后使用"></fb-fieldset>
							<fb-property bordered mode="form">
								<fb-row>
									<fb-col span="24">
										<fb-property-item label="菜单编码">
											<fb-textarea
												:value="`C_JSON_${form.props.code}`"
												readonly></fb-textarea>
										</fb-property-item>
									</fb-col>
								</fb-row>
								<fb-row>
									<fb-col span="24">
										<fb-property-item label="菜单名称">
											<fb-textarea
												:value="`${form.props.name}`"
												readonly></fb-textarea>
										</fb-property-item>
									</fb-col>
								</fb-row>
								<fb-row>
									<fb-col span="24">
										<fb-property-item label="菜单Path">
											<fb-textarea
												:value="`/sys/custom/form/runtime-list/C_JSON_${form.props.code}`"
												readonly></fb-textarea>
										</fb-property-item>
									</fb-col>
								</fb-row>

							</fb-property>

							<fb-fieldset label="发布配置 - 按钮权限 - 请复制后使用"></fb-fieldset>
							<fb-property bordered mode="form">
								<fb-row>
									<fb-col span="24">
										<fb-property-item :label="search.actions.add.label">
											<fb-input :value="`${search.actions.add.code}`" readonly></fb-input>
										</fb-property-item>
									</fb-col>
								</fb-row>
								<fb-row>
									<fb-col span="24">
										<fb-property-item :label="search.actions.modify.label">
											<fb-input :value="`${search.actions.modify.code}`" readonly></fb-input>
										</fb-property-item>
									</fb-col>
								</fb-row>
								<fb-row>
									<fb-col span="24">
										<fb-property-item :label="search.actions.delete.label">
											<fb-input :value="`${search.actions.delete.code}`" readonly></fb-input>
										</fb-property-item>
									</fb-col>
								</fb-row>
								<fb-row>
									<fb-col span="24">
										<fb-property-item :label="search.actions.query.label">
											<fb-input :value="`${search.actions.query.code}`" readonly></fb-input>
										</fb-property-item>
									</fb-col>
								</fb-row>
								<fb-row>
									<fb-col span="24">
										<fb-property-item :label="search.actions.export.label">
											<fb-input :value="`${search.actions.export.code}`" readonly></fb-input>
										</fb-property-item>
									</fb-col>
								</fb-row>
							</fb-property>


							<fb-dialog title="添加表单至菜单" ref="add-to-menu">
								<fb-container mb="8px">
									<fb-alert message="请先选中一个菜单"></fb-alert>
								</fb-container>

								<fb-card body-overflow="auto" :body-style="{height: '300px'}">
									<fb-tree
										ref="add-to-menu-tree"
										@on-select-change="(node)=>{
											  permission.selectedNode = {
													pid: node.pid,
													id: node.id,
													label: node.label,
											  }
										}"
										:service="$svc.sys.menu.tree" :param="{menuId: ''}"></fb-tree>
								</fb-card>

								<fb-container mb="8px" padding="8px" align="center" v-if="permission.selectedNode">
									<fb-button type="primary" @on-click="()=>{handleAddToMenu('pid')}">添加{{
											permission.selectedNode
												? `到 [${permission.selectedNode.label}] 的`
												: ''
										}}同级
									</fb-button>
									<fb-container inline width="8px"></fb-container>
									<fb-button type="primary" @on-click="()=>{handleAddToMenu('id')}">添加{{
											permission.selectedNode
												? `到 [${permission.selectedNode.label}] 的`
												: ''
										}}下级
									</fb-button>
								</fb-container>
							</fb-dialog>


							<!--							<fb-tree :data="permissionTree">-->
							<!--								<template v-slot:node="{node}">-->
							<!--									asda-->
							<!--								</template>-->
							<!--							</fb-tree>-->

						</fb-page-header>
					</fb-card>

				</fb-layout-body>

				<fb-layout-sider :width="320"
								 v-if="!['code', 'previewSearch', 'guide', 'permission'].includes(editor.pageTab)">
					<fb-container
						class="editor-panel-prop"
					>
						<template v-if="editor.pageTab === 'add'">
							<fb-form-editor-component-props-panel
								v-if="  activeComponent && activeComponent.type"
								view-type="add"
								:active-component="activeComponent"
								:col="add.rows[activeComponent.rowIndex].cols[activeComponent.colIndex]"
							/>

							<fb-card
								v-if=" !activeComponent || !activeComponent.type"
								header="控件属性"
								:body-style="{height: `calc(100% - 60px)`, overflowY: 'auto'}">
								<fb-empty text="请选中一个控件"/>
							</fb-card>
						</template>

						<fb-card v-if="editor.pageTab === 'view'"
								 header="字段概览"
								 :body-style="{height: `calc(100% - 60px)`, overflowY: 'auto'}">
							<fb-list :data="view.rows">
								<fb-list-item slot="item" slot-scope="{item, index}">

									<template v-if="item.type === 'row-24'">
										<fb-table-layout>
											<fb-table-layout-cell width="30px" align="center">
												{{ index + 1 }}
											</fb-table-layout-cell>
											<template v-if="item.cols[0] && item.cols[0].type">
												<fb-table-layout-cell align="center">
													<fb-text>{{ item.cols[0].formItemProps.label }}</fb-text>
												</fb-table-layout-cell>
											</template>
											<template v-else>
												<fb-table-layout-cell align="center">
													<fb-text>空字段</fb-text>
												</fb-table-layout-cell>
											</template>
										</fb-table-layout>
									</template>

									<template v-if="item.type === 'row-12'">

										<fb-table-layout>
											<fb-table-layout-cell width="30px" align="center">
												{{ index + 1 }}
											</fb-table-layout-cell>
											<template v-if="item.cols[0] && item.cols[0].type">
												<fb-table-layout-cell align="center">
													<fb-text>{{ item.cols[0].formItemProps.label }}</fb-text>
												</fb-table-layout-cell>
											</template>
											<template v-else>
												<fb-table-layout-cell align="center">
													<fb-text>空字段</fb-text>
												</fb-table-layout-cell>
											</template>

											<template v-if="item.cols[1] && item.cols[1].type">
												<fb-table-layout-cell align="center">
													<fb-text>{{ item.cols[1].formItemProps.label }}</fb-text>
												</fb-table-layout-cell>
											</template>
											<template v-else>
												<fb-table-layout-cell align="center">
													<fb-text>空字段</fb-text>
												</fb-table-layout-cell>
											</template>
										</fb-table-layout>
									</template>

								</fb-list-item>
							</fb-list>
						</fb-card>
						<fb-card header="控件属性" v-if="editor.pageTab === 'search'"
								 margin="0 0 8px 0" body-overflow="auto"
								 :body-style="{height: `calc(100% - 60px)`, padding: '8px'}">

							<fb-form-editor-search-props-panel :search="search"/>

						</fb-card>
					</fb-container>
				</fb-layout-sider>


			</fb-layout>
		</fb-layout>


		<fb-dialog
			ref="formPropertiesDialog" title="表单默认值" width="400" height="300">

			<!--			<template slot="footer">-->
			<!--				<fb-container align="center">-->
			<!--					<fb-button type="primary" @on-click="handleSaveFormProps"-->
			<!--							   style="margin-right: 8px"-->
			<!--					>保存-->
			<!--					</fb-button>-->
			<!--					<fb-button @on-click="$refs.formPropertiesDialog.hide()">取消</fb-button>-->
			<!--				</fb-container>-->
			<!--			</template>-->

			<fb-form label-position="top" ref="formProperties">
				<!--				<fb-form-item label="表单编码" prop="code" :rule="{required: true}">-->
				<!--					<fb-input v-model="form.props.code" readonly/>-->
				<!--				</fb-form-item>-->
				<!--				<fb-form-item label="表单名称" prop="name" :rule="{required: true}">-->
				<!--					<fb-input v-model="form.props.name" readonly/>-->
				<!--				</fb-form-item>-->
				<fb-form-item label="标题默认宽度(px)" prop="labelWidth"
							  label-position="left"
							  :rule="[{required: true}, {type: 'number'}]">
					<fb-input-number v-model.number="form.props.labelWidth"
									 append="px">
					</fb-input-number>
				</fb-form-item>
				<fb-form-item label="标题默认位置" prop="labelPosition"
							  label-position="left"
							  :rule="[{required: true}]">
					<fb-radio-group
						button
						v-model="form.props.labelPosition"
						:data="[{label: '左', value:'left'},{label: '右', value:'right'},{label: '上', value:'top'},]"
					></fb-radio-group>
				</fb-form-item>

				<fb-form-item label="业务类型(referType)" prop="referType"
							  label-width="200"
							  :rule="[{required: true}]">
					<fb-radio-group
						:radio-space="12"
						v-model="form.props.referType"
						:data="[
										{label: '部门', value:'dept'},
										{label: '行政区划', value:'city'},
										{label: '组织机构', value:'org'}
										]"
					></fb-radio-group>
				</fb-form-item>

				<fb-form-item label="业务查询模式(referTypePattern)" prop="referTypePattern"
							  label-width="200"
							  :rule="[{required: true}]">
					<fb-radio-group

						:radio-space="12"
						v-model="form.props.referTypePattern"
						:data="[
										{label: '精确', value:'='},
										{label: '模糊', value:'like'}
										]"
					></fb-radio-group>
				</fb-form-item>

				<!--				<fb-form-item label="同步"-->
				<!--							  label-width="200">-->

				<!--					<fb-space direction="vertical">-->
				<!--						<fb-button type="primary" size="s">同步至新增表单</fb-button>-->
				<!--						<fb-button type="primary" size="s" @on-click="syncView">同步至查看表单</fb-button>-->
				<!--						<fb-button type="primary" size="s" @on-click="syncViewAdditional">追加-->
				<!--						</fb-button>-->

				<!--						<fb-button type="primary" size="s">同步至查询列表</fb-button>-->
				<!--					</fb-space>-->
				<!--				</fb-form-item>-->


				<fb-button type="primary" icon="save" @on-click="()=>{
					doSave();
				}">保 存
				</fb-button>
			</fb-form>

		</fb-dialog>

		<fb-dialog ref="previewAddDialog"
				   class="editor-panel-designer"
				   :title="form.props.name"
				   :sub-title="`${form.props.code}`"
		>

			<fb-form ref="previewAddForm">

				<fb-empty v-if="add.rows.length === 0" text="新增表单无数据，请先同步"/>

				<template v-for="(row, rowIndex) in add.rows">

					<!----------------------------------
						 单列 布局
					 ---------------------------------->
					<template v-if="row.type === 'row-24'">
						<fb-row>
							<fb-form-editor-col-add-preview v-if="row.cols && row.cols[0]" :col="row.cols[0]"
															row-type="row-24"/>
						</fb-row>
					</template>

					<!----------------------------------
						 双列 布局
					---------------------------------->
					<template v-if="row.type === 'row-12'">
						<fb-row>
							<fb-form-editor-col-add-preview v-if="row.cols && row.cols[0]" :col="row.cols[0]"
															row-type="row-12"/>
							<fb-form-editor-col-add-preview v-if="row.cols && row.cols[1]" :col="row.cols[1]"
															row-type="row-12"/>
						</fb-row>
					</template>

				</template>

				<fb-row>
					<fb-col span="24" align="center">
						<fb-button type="primary"
								   style="margin-right: 8px;"
								   @on-click="$refs.previewAddForm.validate()"
						>提 交
						</fb-button>
						<fb-button @on-click="$refs.previewAddDialog.close()">关 闭</fb-button>
					</fb-col>
				</fb-row>
			</fb-form>

		</fb-dialog>

		<fb-dialog ref="previewViewDialog"
				   class="editor-panel-designer">
			<fb-property bordered mode="form" :body-style="{overflow: 'visible'}">
				<template v-for="(row, rowIndex) in add.rows">

					<fb-row v-if="row.type === 'row-24'"

							:class="{[`${prefix}-row--property-fieldset`]: row.cols[0]&&row.cols[0].type && row.cols[0].type === 'fb-fieldset'}"
					>
						<fb-form-editor-col-view-preview
							:col="row.cols[0]"
							:col-index="0"
							view-type="view"
							:row-index="rowIndex"
							row-type="row-24"
						/>
					</fb-row>
					<fb-row v-if="row.type === 'row-12'">

						<fb-form-editor-col-view-preview
							:col="row.cols[0]"
							:col-index="0"
							view-type="view"
							:row-index="rowIndex"
							row-type="row-12"
						/>

						<fb-form-editor-col-view-preview
							:col="row.cols[1]"
							:col-index="1"
							view-type="view"
							:row-index="rowIndex"
							row-type="row-12"
						/>
					</fb-row>
				</template>
			</fb-property>
		</fb-dialog>

		<fb-dialog ref="previewAddFormDataDialog"
		>

			<pre>{{ addFormData }}</pre>

		</fb-dialog>


		<tp-dialog ref="TpDialog"></tp-dialog>

	</fb-container>
</template>


<script>
/**
 * FbFormEditor
 * (c) 2021 lincong1987
 */

import props from './utils/props'
import {find, slice, get, merge, keys} from 'lodash-es'
import FbFormEditorAddRow from './FbFormEditorAddRowContainer'
import FbFormEditorRowToolbar from './FbFormEditorRowToolbar'
import FbFormEditorCol from './FbFormEditorCol'
import FbFormEditorComponentPropsPanel from './FbFormEditorComponentPropsPanel'
import FbFormEditorRowViewToolbar from './FbFormEditorRowViewToolbar'
import FbFormEditorSearchPropsPanel from './FbFormEditorSearchPropsPanel'
import FbFormEditorSearchPanel from './FbFormEditorSearchPanel'
import FbFormEditorColAddPreview from './FbFormEditorColAddPreview'
import FbFormEditorColViewPreview from './FbFormEditorColViewPreview'
import _schema from './utils/schema'
import services from './utils/services'
import {each} from 'lodash-es/collection'
import {toRaw, uuid} from './utils/utils'

const schema = {
	...JSON.parse(JSON.stringify(_schema)),
	getComponentsNameList: _schema.getComponentsNameList,
}

export default {

	name: 'FbFormEditor',

	components: {
		FbFormEditorColViewPreview,
		FbFormEditorColAddPreview,
		FbFormEditorSearchPanel,
		FbFormEditorSearchPropsPanel,
		FbFormEditorRowViewToolbar,
		FbFormEditorComponentPropsPanel,
		FbFormEditorCol,
		FbFormEditorRowToolbar,
		FbFormEditorAddRow,
	},

	provide() {
		return {
			formEditor: this,
		}
	},

	props: {

		mid: {
			type: [Number, String],
			default() {
				return {}
			},
		},

		service: {
			type: [Object, Function],
			default() {
				return {}
			},
		},
	},

	data() {

		let services = this.getServices(this.$svc)
		let componentsNameList = schema.getComponentsNameList(services)
		let userInfo = this.$datax.get('userInfo')
		let ctx = {
			...toRaw(userInfo),
		}
		return {

			ctx,

			editor: {
				//   add view search list code
				pageTab: 'add',

				// component form
				propTab: 'form',

				searchTab: '',

				viewTab: 'components',

				componentsNameList,

				components: schema.components,

				services,
			},

			form: schema.form,

			add: schema.add,

			view: schema.view,

			search: schema.search,

			activeComponent: {
				rowIndex: -1,
				colIndex: -1,
				uuid: '',
				type: '',
			},

			uuidIndex: 1,

			previewAddForm: {
				props: {
					labelWidth: 120,
					labelPosition: null,
				},
				rows: [],
			},

			addFormData: '',

			history: [],

			permissionTree: [
				{},
			],

			permission: {
				tree: [],
				selectedNode: null,
			},
		}

	},

	watch: {

		'editor.pageTab'() {
			this.activeComponent = {}
		},

	},

	methods: {

		handleAddToMenu(type) {
			if (!this.permission.selectedNode) {
				return
			}

			let menu = {
				menuId: '',
				menuName: this.form.props.name,
				menuCode: this.form.props.code,
				menuUri: encodeURIComponent(`/sys/custom/form/runtime-list/C_JSON_${this.form.props.code}`),
				menuPid: '',
				menuSource: 1,
				menuType: 'SYS1901',
				menuIcon: '',
				menuDesc: '',
				orderIndex: 0,
				enabled: 1,
			}

			// 同级
			if (type === 'pid') {
				menu.menuPid = this.permission.selectedNode.pid
			}

			// 下级
			if (type === 'id') {
				menu.menuPid = this.permission.selectedNode.id
			}

			let buttons = []

			Object.values(this.search.actions).forEach(button => {
				buttons.push({
					menuId: '',
					menuName: button.label,
					menuCode: button.code,
					menuUri: '',
					menuPid: '',
					menuSource: 1,
					menuType: 'SYS1903',
					menuIcon: '',
					menuDesc: '',
					orderIndex: 0,
					enabled: 1,
				})
			})

			this.$svc.sys.menu.add(menu).then((result) => {
				// 判断code
				if (result.code === 1) {

					this.$message.success('新增成功')
					this.$message.info('处理按钮...')

					Promise.all(buttons.map(button => {
						return this.$svc.sys.menu.add({
							...button,
							menuPid: result.data.menuId,
						})
					})).then((rs) => {

						this.$message.success('新增按钮组成功')
						this.$refs['add-to-menu-tree'].doReload()
						this.permission.selectedNode = null

					})

				} else {
					// 服务器返回失败
					this.$message.error('错误提示:' + result.message)
				}
			})

			// SYS1903

		},

		// 本地已定义的service map
		getServices(parent, label) {
			return keys(parent).filter(n => ![
				'service',
				'request',
				'getUri',
				'delete',
				'get',
				'head',
				'options',
				'post',
				'put',
				'patch',
				'defaults',
				'interceptors',
				'setRequestInterceptor',
				'ejectRequestInterceptor',
				'setResponseInterceptor',
				'ejectResponseInterceptor',
				'setHeaders',
				'removeHeaders',
			].includes(n)).map((n, i) => {
				let value = label ? `${label}.${n}` : n
				let fsn = this.getFriendlyServiceName(value)
				return {
					label: fsn ? `${fsn} (${n})` : n,
					value: value,
					icon: 'data-fill',
					children: this.getServices(parent[n], value),
				}
			})

		},

		getFriendlyServiceName(value) {
			return services[value]
		},

		/********************************************
		 * 数据更新操作
		 ********************************************/
		propSplice(viewType, prop, propName, index, length) {

			let {
				activeComponent,
			} = this

			this[viewType].rows[activeComponent.rowIndex].cols[activeComponent.colIndex][prop][propName].splice(
				index, length)

		},

		propPush(viewType, prop, propName, value) {
			let {
				activeComponent,
			} = this

			this[viewType].rows[activeComponent.rowIndex].cols[activeComponent.colIndex][prop][propName].push(
				value)
		},

		updateCol(viewType, value, prop, propName, pathMatcher = (path) => {
			return path
		}) {

			let {
				activeComponent,
			} = this

			this.$set(
				pathMatcher(this[viewType].rows[activeComponent.rowIndex].cols[activeComponent.colIndex][prop]),
				propName,
				value,
			)
			this.$forceUpdate()

		},

		updateProps(viewType, value, prop) {
			this[viewType].props[prop] = value
			this.$forceUpdate()
		},

		updatePathProps() {
			let arr = Array.prototype.slice.call(arguments)
			let viewType = arr[0], value = arr[1]
			let path = slice(arr, 2, arr.length - 1)
			//set(this[viewType], path, value)

			//	console.log('updatePathProps', viewType, path, arr[arr.length - 1], value)

			this.$set(
				get(this[viewType], path),
				arr[arr.length - 1],
				value,
			)

			this.$forceUpdate()
		},

		getComponentByType(type) {
			return find(this.editor.componentsNameList, {value: type})
		},

		getComponentNameByType(type) {
			let component = this.editor.componentsNameList.filter((n) => {
				return n.value === type
			})
			if (component) {
				return component[0].label
			} else {
				return ''
			}
		},

		/********************************************
		 * 属性面板TAB切换操作
		 ********************************************/
		handleDesignTabBeforeChange(tab, tabIndex) {
			if (!this.activeComponent.uuid && tabIndex === 0) {
				this.$message.error('请先选中表单中的控件')
				return false
			}

			if (tabIndex === 1) {
				this.activeComponent = {}
			}
		},
		handleAddTabBeforeChange(tab, tabIndex) {
		},
		handleViewTabBeforeChange(tab, tabIndex) {
		},
		handleSearchTabBeforeChange(tab, tabIndex) {
		},
		handleActiveComponent(rowIndex, colIndex, type, uuid) {

			let {
				activeComponent,
				editor,
			} = this

			if (rowIndex === activeComponent.rowIndex
				&& colIndex === activeComponent.colIndex
				&& uuid === activeComponent.uuid
				&& type === activeComponent.type) {
				//editor.propTab = 'form'
				this.activeComponent = {}
				return
			}

			//editor.propTab = 'component'
			activeComponent = {}

			this.$nextTick(() => {

				this.$message(`已选中 ${this.getComponentNameByType(type)}`)
				this.activeComponent = {
					rowIndex,
					colIndex,
					uuid,
					type,
				}
			})

		},

//		uuid () {
//			return uuid//`id_${new Date().getTime()}_${this.uuidIndex++}`
//		},

		showPreviewDialog(type) {
			if (type === 'add') {
				this.$refs.previewAddDialog.show()
				//this.previewAddForm = this.add
			}
			if (type === 'view') {
				this.$refs.previewViewDialog.show()
			}
		},

		showPreviewAddFormDataDialog() {
			let formData = {}

			this.add.rows.forEach((n, i) => {
				if (n.type === 'row-24' && n.cols[0].type) {
					//data.push(n.cols[0])
					formData[n.cols[0].type && n.cols[0].formItemProps && n.cols[0].formItemProps.name] = ''
				}
				if (n.type === 'row-12') {
					if (n.cols[0].type) {
						// data.push(n.cols[0])
						formData[n.cols[0].type && n.cols[0].formItemProps && n.cols[0].formItemProps.name] = ''
					}
					if (n.cols[1].type) {
						// data.push(n.cols[1])
						formData[n.cols[1].type && n.cols[1].formItemProps && n.cols[1].formItemProps.name] = ''
					}
				}
			})

			this.addFormData = JSON.stringify(formData, null, '\t')
			this.$refs.previewAddFormDataDialog.show()
		},

		doTest() {

			if (!this.form.props.mid) {
				this.$message.error('请先保存')
				return
			}

			this.$router.push({
				path: `/sys/custom/form/runtime-test/${this.form.props.mid}`, // 配置的路由
				query: {
					tabLabel: `测试-${this.form.props.name}`,
				},
				params: {
					mid: this.form.props.mid,
				},
			})
		},

		/********************************************
		 * 行 操作
		 ********************************************/
		// 添加行
		addRow(viewType, index, layout) {

			let {
				editor,
				form,
				add,
				view,
			} = this
			let row = {
				type: layout,
				cols: layout === 'row-24'
					? [
						{
							uuid: uuid(),
							type: null,
							tempType: '',
							props: {},
						},
					]
					: [
						{
							uuid: uuid(),
							type: null,
							tempType: '',
							props: {},
						}, {
							uuid: uuid(),
							type: null,
							tempType: '',
							props: {},
						},
					],
			}

			this.activeComponent = {}

			this.$nextTick(() => {

				if (index < 0) {
					this[viewType].rows.unshift(row)
				} else {
					this[viewType].rows.splice(index + 1, 0, row)
				}

			})

		},
		// 移除行
		removeRow(viewType, index) {

			let {
				form,
				editor,
				activeComponent,
			} = this

//			editor.propTab = 'form'
			this.activeComponent = {}

			this.$nextTick(() => {
				this[viewType].rows.splice(index, 1)
			})

		},
		// 移动行
		moveRow(viewType, position, index) {
			let {
				form,
			} = this

			this.activeComponent = {}

			let maxIndex = this[viewType].rows.length

			if (position === 'up') {
				let row = this[viewType].rows[index - 1]
				this.$set(this[viewType].rows, index - 1, this[viewType].rows[index])
				this.$set(this[viewType].rows, index, row)
			}

			if (position === 'down') {
				if (index + 1 >= maxIndex) {
					this.$message.error('最后一行不得下移！')
					return
				}
				let row = this[viewType].rows[index + 1]
				this.$set(this[viewType].rows, index + 1, this[viewType].rows[index])
				this.$set(this[viewType].rows, index, row)
			}
		},

		/********************************************
		 * 元件 操作
		 ********************************************/
		// 添加元件
		addComponent(viewType, rowIndex, colIndex, type, uuid) {
			let {
				form,
				add,
				getProps,
			} = this

			if (!type) {
				this.$message.error('请选择控件类型')
				return
			}

			let props = getProps(type, rowIndex, colIndex)

			let cell = {
				uuid,
				type,
				props: props.props,
				formItemProps: props.formItemProps,
			}

			if (type === 'fb-fieldset' && this[viewType].rows[rowIndex].type === 'row-12') {
				this.$message.warn('双列不支持标题')
				return
			}

			this[viewType].rows[rowIndex].cols.splice(colIndex, 1, Object.assign({}, cell))

			this.$nextTick(() => {
				this.activeComponent = {}
				this.handleActiveComponent(rowIndex, colIndex, cell.type, cell.uuid)
			})

		},
		// 删除元件
		removeComponent(viewType, rowIndex, colIndex, uuid) {

			let {
				add,
				getProps,
			} = this

			let cell = {
				uuid,
			}

			// this.editor.propTab = 'form'
			this.activeComponent = {}

			this.$nextTick(() => {
				this[viewType].rows[rowIndex].cols.splice(colIndex, 1, Object.assign({}, cell))
			})
		},
		// 左右移动元件
		moveComponent(viewType, rowIndex, colIndex, uuid) {
			let {
				getProps,
			} = this

			this.activeComponent = {}

			let targetColIndex = colIndex === 0 ? 1 : 0,
				targetCol = this[viewType].rows[rowIndex].cols[targetColIndex],
				thisCol = this[viewType].rows[rowIndex].cols[colIndex]

			this[viewType].rows[rowIndex].cols.splice(targetColIndex, 1, Object.assign({}, thisCol))
			this[viewType].rows[rowIndex].cols.splice(colIndex, 1, Object.assign({}, targetCol))
		},

		/********************************************
		 * 查看 行 操作
		 ********************************************/
		// 移除行
		removeViewRow(index) {

			let {
				form,
				editor,
				activeComponent,
			} = this

			this.$nextTick(() => {
				form.view.splice(index, 1)
			})

		},
		// 移动行
		moveViewRow(position, index) {
			let {
				form,
			} = this

			let maxIndex = form.view.length

			if (position === 'up') {
				let row = form.view[index - 1]
				this.$set(form.view, index - 1, form.view[index])
				this.$set(form.view, index, row)
			}

			if (position === 'down') {
				if (index + 1 >= maxIndex) {
					this.$message.error('最后一行不得下移！')
					return
				}
				let row = form.view[index + 1]
				this.$set(form.view, index + 1, form.view[index])
				this.$set(form.view, index, row)
			}
		},

		/********************************************
		 * 查看 元件 操作
		 ********************************************/
		// 删除元件
		removeViewComponent(rowIndex, colIndex, uuid) {

			let {
				form,
				getProps,
			} = this

			let cell = {
				uuid,
			}

			this.$nextTick(() => {
				form.view[rowIndex].cols.splice(colIndex, 1, Object.assign({}, cell))
			})
		},
		// 左右移动元件
		moveViewComponent(rowIndex, colIndex, uuid) {
			let {
				form,
				getProps,
			} = this

			let targetColIndex = colIndex === 0 ? 1 : 0,
				targetCol = form.view[rowIndex].cols[targetColIndex],
				thisCol = form.view[rowIndex].cols[colIndex]

			form.view[rowIndex].cols.splice(targetColIndex, 1, Object.assign({}, thisCol))
			form.view[rowIndex].cols.splice(colIndex, 1, Object.assign({}, targetCol))
		},

		getProps(type, rowIndex, colIndex) {

			let {editor} = this

			let componentName = this.getComponentNameByType(type)

			let label = `${componentName}${(this.getComponentByType(type).index++)}`
			let name = uuid()
			let formItemProps = {
				label,
				name,
				showLabel: true,
				show: true,
				rules: [],
				ruleEnum: [],
				colSpan: this.add.rows[rowIndex].type === 'row-12' ? 12 : 24,
				colOffset: 0,
			}

			// this[viewType].rows[rowIndex].type

			let componentProps = {
				placeholder: '请输入',
			}

			if (type === 'fb-fieldset') {
				componentProps.label = label
			}

			//

			if (props[type]) {
				componentProps = Object.assign({}, componentProps, props[type].defaults)
			}

//			if (type === 'fb-textarea') {
//				componentProps.rows = 3
//				props['fb-textarea'].defaults
//			}

//			if (type === 'fb-radio-group') {
//				componentProps.radioSpace = 16
//				componentProps.data = []
//				componentProps.reader = [
//					{
//						'label': 'label',
//						'value': '',
//					},
//					{
//						'label': 'value',
//						'value': '',
//					},
//				]
//			}

//			if (type === 'fb-checkbox-group') {
//
//				componentProps.defaultValue = []
//				componentProps.data = []
//				componentProps.reader = [
//					{
//						'label': 'label',
//						'value': '',
//					},
//					{
//						'label': 'value',
//						'value': '',
//					},
//				]
//			}

//			if (type === 'fb-select') {
//				componentProps.data = []
//				componentProps.param = []
//				componentProps.placeholder = '请选择'
//				componentProps.reader = [
//					{
//						'label': 'label',
//						'value': '',
//					},
//					{
//						'label': 'value',
//						'value': '',
//					},
//				]
//			}

//			if (type === 'fb-tree-select') {
//				componentProps.data = []
//				componentProps.param = []
//				componentProps.placeholder = '请选择'
//				componentProps.doCheck = 'ps'
//				componentProps.doUnCheck = 'ps'
//				componentProps.onlyLeaf = false
//				componentProps.reader = [
//					{
//						'label': 'label',
//						'value': '',
//					},
//					{
//						'label': 'value',
//						'value': '',
//					},
//				]
//			}

//			if (type === 'tp-datepicker') {
//				componentProps.format = 'YYYY-MM-DD'
//				componentProps.valueFormat = 'YYYYMMDD'
//			}

//			if (type === 'tp-upload') {
//				componentProps.param = [
//					{
//						label: 'referType',
//						value: '',
//					},
//				]
//				componentProps.accept = ''
//				componentProps.view = 'list'
//				componentProps.multiple = false
//				componentProps.quality = 0.7
//				componentProps.maxWidth = 2000
//				componentProps.maxHeight = 2000
//				componentProps.readonly = false
//				componentProps.avatarSize = 120
//				componentProps.avatarCircle = true
//				componentProps.showPreview = true
//				componentProps.showDownload = true
//				componentProps.showRemove = true
//			}

//			if (type === 'biz-person-select') {
//				componentProps.placeholder = '点击选择'
//			}
//
//			if (type === 'biz-ent-select') {
//				componentProps.placeholder = '点击选择'
//			}

//			if (type === 'biz-ent-person-single-select') {
//				componentProps.placeholder = '点击选择'
//			}

			return {
				formItemProps,
				props: componentProps,
			}
		},

		handlePropTableRowAdd(cb) {
			this.$nextTick(() => {
				cb && cb(this)
			})
		},

		showFormProperties() {
			this.$refs.formPropertiesDialog.show()
		},

		handleSaveFormProps() {
			this.$refs.formProperties.validate(() => {

				this.add.props.referType = this.form.props.referType
				this.add.props.referTypePattern = this.form.props.referTypePattern
				this.view.props.referType = this.form.props.referType
				this.view.props.referTypePattern = this.form.props.referTypePattern
				this.search.props.referType = this.form.props.referType
				this.search.props.referTypePattern = this.form.props.referTypePattern

				this.$refs.formPropertiesDialog.hide()
			})
		},

		doSave() {

			let referType = this.form.props.referType
			let referTypePattern = this.form.props.referTypePattern

			let labelWidth = this.form.props.labelWidth
			let labelPosition = this.form.props.labelPosition

			if (!referType || !referTypePattern) {
				this.$refs.formPropertiesDialog.show()
				this.$message.warn('业务类型及业务查询模式是必选项！')
				return
			}

			this.add.props.referType = this.form.props.referType
			this.add.props.referTypePattern = this.form.props.referTypePattern
			this.add.props.labelWidth = this.form.props.labelWidth
			this.add.props.labelPosition = this.form.props.labelPosition

			this.view.props.referType = this.form.props.referType
			this.view.props.referTypePattern = this.form.props.referTypePattern
			this.view.props.labelWidth = this.form.props.labelWidth
			this.view.props.labelPosition = this.form.props.labelPosition

			this.search.props.referType = this.form.props.referType
			this.search.props.referTypePattern = this.form.props.referTypePattern
			this.search.props.labelWidth = this.form.props.labelWidth
			this.search.props.labelPosition = this.form.props.labelPosition

			this.$svc.sys.custom.form.update({
				mid: this.form.props.mid,
				fSource: 'pc',
				ftype: 'edit',
				fjson: JSON.stringify(this.add),
				fid: this.add.props.fid || '',
			}).then(res => {
				if (res.code !== 1) {
					this.$message.error('新增 保存失败')
				} else {
					this.add.props.fid = res.data
					this.$message.success('新增 保存成功')
				}
			})

			this.$svc.sys.custom.form.update({
				mid: this.form.props.mid,
				fSource: 'pc',
				ftype: 'view',
				fjson: JSON.stringify(this.add),
				fid: this.view.props.fid || '',
			}).then(res => {
				if (res.code !== 1) {
					this.$message.error('查看 保存失败')
				} else {
					this.view.props.fid = res.data
					this.$message.success('查看 保存成功')
				}
			})

			this.$svc.sys.custom.form.update({
				mid: this.form.props.mid,
				fSource: 'pc',
				ftype: 'list',
				fjson: JSON.stringify(this.search),
				fid: this.search.props.fid || '',
			}).then(res => {
				if (res.code !== 1) {
					this.$message.error('列表 保存失败')
				} else {
					this.search.props.fid = res.data
					this.$message.success('列表 保存成功')
				}
			})
		},

		/********************************************
		 * 数据同步
		 ********************************************/
		syncAdd() {
		},
		syncView() {

			let {
				form,
			} = this

			let fid = this.view.props.fid
			this.view = JSON.parse(JSON.stringify(this.add))
			this.$nextTick(() => {
				this.view.props.fid = fid
			})
		},

		// 追加模式
		syncViewAdditional() {

			let {
				form,
			} = this

			// form.view = form.view.concat(JSON.parse(JSON.stringify(form.rows)))
		},

		syncColProps() {
			this.add.rows.forEach((n, i) => {
				if (n.type === 'row-24' && n.cols[0].type) {
					this.syncColByUuid(n.cols[0])
				}
				if (n.type === 'row-12') {
					if (n.cols[0].type) {
						this.syncColByUuid(n.cols[0])
					}
					if (n.cols[1].type) {
						this.syncColByUuid(n.cols[1])
					}
				}
			})

		},
		syncColByUuid(col) {

			let queryCol = this.search.query.rows.find(n => col.uuid === n.uuid)
			if (!['fb-fieldset', 'fb-text', 'fb-color-picker', 'tp-upload', ''].includes(col.type)) {
				if (!queryCol) {
					let colModel = toRaw(col)
					colModel.formItemProps && (colModel.formItemProps.show = true)
					colModel.formItemProps && (colModel.formItemProps.searchType = 'like')
					if (colModel.type === 'tp-upload') {
						colModel.formItemProps && (colModel.formItemProps.show = false)
					}
					if (['fb-radio-group'].includes(colModel.type)) {
						colModel.formItemProps && (colModel.formItemProps.searchOptionAll = true)
					}
					this.search.query.rows.push(colModel)
				} else {
					let colModel = toRaw(col)
					let show = queryCol.formItemProps.show
					let searchType = queryCol.formItemProps.show

					queryCol.formItemProps = {
						...queryCol.formItemProps,
						...colModel.formItemProps,
						show,
						searchType,
					}
					queryCol.props = merge({}, queryCol.props, colModel.props)
				}
			}

			let columnsCol = this.search.table.props.columns.find(n => col.uuid === n.item.uuid)
			if (!columnsCol) {
				let colModel = toRaw(col)
				let column = {
					name: colModel.formItemProps.name || '',
					label: colModel.formItemProps.label || '',
					align: 'left',
					titleAlign: 'left',
					ellipsis: true,
					titleEllipsis: true,
					show: true,
					item: colModel,
				}

				if (colModel.type === 'fb-color-picker') {
					column.slot = 'fb-color-picker'
				}
				if (colModel.type === 'tp-upload') {
					column.slot = 'tp-upload'
				}
				this.search.table.props.columns.push(column)
			} else {
				let colModel = toRaw(col)
				columnsCol.item = colModel
				columnsCol.name = colModel.formItemProps.name || ''
				columnsCol.label = colModel.formItemProps.label || ''
			}

			if (!this.search.table.props.columns.find(n => 'op' === n.slot)) {
				this.search.table.props.columns.push({
					name: 'op',
					label: '操作',
					slot: 'op',
					width: 120,
					show: true,
					ellipsis: false,
				})
			}
		},

		syncSearch() {

			let data = []

			// 扁平化表单项
			this.add.rows.forEach((n, i) => {
				if (n.type === 'row-24' && n.cols[0].type) {
					data.push(n.cols[0])
				}
				if (n.type === 'row-12') {
					if (n.cols[0].type) {
						data.push(n.cols[0])
					}
					if (n.cols[1].type) {
						data.push(n.cols[1])
					}
				}
			})

			// 生成表单查询项
			this.search.query.rows = (toRaw(data).map(n => {

				n.formItemProps && (n.formItemProps.show = true)
				n.formItemProps && (n.formItemProps.searchType = 'like')
				if (n.type === 'tp-upload') {
					n.formItemProps && (n.formItemProps.show = false)
				}
				if (['fb-radio-group'].includes(n.type)) {
					n.formItemProps && (n.formItemProps.searchOptionAll = true)
				}

				return n
			})).filter(n => !(['fb-fieldset', 'fb-text', 'fb-color-picker', 'tp-upload', ''].includes(n.type)))

			// 创建表格列对象
			let columns = []

			data.forEach((n, i) => {

				let column = {
					name: n.formItemProps.name || '',
					label: n.formItemProps.label || '',
					align: 'left',
					titleAlign: 'left',
					ellipsis: true,
					titleEllipsis: true,
					show: true,
					item: n,
				}

				if (n.type === 'fb-color-picker') {
					column.slot = 'fb-color-picker'
				}
				if (n.type === 'tp-upload') {
					column.slot = 'tp-upload'
				}
				columns.push(column)
			})

			this.search.table.props.columns = toRaw([
				...columns, ...[
					{
						name: 'op',
						label: '操作',
						slot: 'op',
						width: 120,
						show: true,
						ellipsis: false,
					},
				],
			])

		},

		/**
		 * 复原表单
		 * @param viewType
		 */
		revertForm(viewType) {

			this.activeComponent = {}

			let referType = this.form.props.referType
			let referTypePattern = this.form.props.referTypePattern

			if (viewType === 'add' || viewType === 'view') {
				this[viewType] = {
					props: {
						labelWidth: 140,
						labelPosition: 'left',
						fid: this[viewType].props.fid,
						referType,
						referTypePattern,
					},
					rows: [],
				}
			}
			if (viewType === 'search') {
				this[viewType] = {

					props: {
						...schema.search.props,
						fid: this[viewType].props.fid,
						referType,
						referTypePattern,
					},

					query: schema.search.query,
					table: schema.search.table,
					actions: schema.search.actions,
				}
			}
		},

	},
	async mounted() {
//
//		this.$nextTick(() => {
//			console.log('mounted 修改 => 1336181536956874752')
//			this.checked = [
//				'1336181536956874752',
//			]
//			console.log('mounted 修改OK => 1336181536956874752')
//		})

		let mid = this.mid

		let formProps = await this.$svc.sys.custom.module.view({mid}).then(res => res.data)

		this.form.props = merge({}, this.form.props, {
			mid: formProps.mid,
			name: formProps.mname,
			code: formProps.mcode.replace('C_JSON_', ''),
			mcode: formProps.mcode,//.replace('C_JSON_', ''),
		})

		let add = await this.$svc.sys.custom.form.view({
			mid: formProps.mid,
			ftype: 'edit',
			fSource: 'pc',
		})

		if (add.data === '' || add.data.fjson === '') {
			this.add = toRaw(schema.add)
		} else {
			this.add = JSON.parse(add.data.fjson)
		}

		if (add.data.fid) {
			this.add.props.fid = add.data.fid
		}

		let view = await this.$svc.sys.custom.form.view({
			mid: formProps.mid,
			ftype: 'view',
			fSource: 'pc',
		})

		if (view.data === '' || view.data.fjson === '') {
			this.view = toRaw(schema.view)
		} else {
			this.view = JSON.parse(view.data.fjson)
		}

		if (view.data.fid) {
			this.view.props.fid = view.data.fid
		}

		let list = await this.$svc.sys.custom.form.view({
			mid: formProps.mid,
			ftype: 'list',
			fSource: 'pc',
		})

		if (list.data === '' || list.data.fjson === '') {

			each(schema.search.actions, (action) => {
				action.code = this.form.props.code + '_' + action.code
			})

			this.search = toRaw(schema.search)
		} else {
			this.search = JSON.parse(list.data.fjson)
		}

		if (list.data.fid) {
			this.search.props.fid = list.data.fid
		}

		// 同步业务类型
		this.$nextTick(() => {

			let referType = this.add.props.referType || this.view.props.referType || this.search.props.referType || ''
			let referTypePattern = this.add.props.referTypePattern || this.view.props.referTypePattern ||
				this.search.props.referTypePattern || ''
			let labelWidth = this.add.props.labelWidth || this.view.props.labelWidth || this.search.props.labelWidth ||
				''
			let labelPosition = this.add.props.labelPosition || this.view.props.labelPosition ||
				this.search.props.labelPosition || ''
			if (referType) {
				this.form.props.referType = referType
			}
			if (referTypePattern) {
				this.form.props.referTypePattern = referTypePattern
			}
			if (labelWidth) {
				this.form.props.labelWidth = labelWidth
			}
			if (labelPosition) {
				this.form.props.labelPosition = labelPosition
			}
		})

	},

}
</script>

<style lang="less" scoped>


@import "../../../../../assets/styles/fb-form-designer.less";


</style>
