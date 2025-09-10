<template>
	<fb-card
		header="控件属性"
		:body-style="{height: `calc(100% - 60px)`, overflowY: 'auto'}">

		<template v-if="!activeComponent.type">
			<fb-empty text="请选中一个控件"/>
		</template>

		<fb-form label-position="top"
		         :label-style="{lineHeight: 'normal', paddingBottom: '2px'}"
		>

			<template v-if="activeComponent.type === 'fb-fieldset'">
				<fb-form-item label="分主标题名称"
				              :label-text-style="{width: '80px'}"
				              :label-extra-style="{textAlign: 'right'}"
				              label-fixed
				              prop="label">

					<fb-tag slot="label-extra" type="primary" effect="light"
					        :label="getComponentNameByType(activeComponent.type)"></fb-tag>
					<fb-input :value="col.props.label"
					          @input="(value)=>{updateCol(value, 'props', 'label')}"/>
				</fb-form-item>
			</template>

			<!--			<template v-if="['fb-input', 'fb-textarea', 'fb-select', 'fb-tree-select',-->
			<!--			'fb-radio-group', 'fb-checkbox-group', 'fb-checkbox', 'tp-datepicker', 'tp-upload', 'fb-editor',-->
			<!--			'biz-person-select',-->
			<!--			'biz-ent-select'-->
			<!--			].includes(activeComponent.type)">-->

			<template v-if="activeComponent.type !== 'fb-fieldset'">

				<fb-fieldset label="表单属性"/>

				<fb-form-item label="控件ID(数据绑定)" prop="name">
					<!--					<fb-tooltip placement="top" slot="label-extra">-->
					<!--						<fb-icon name="information"/>-->
					<!--						<fb-container slot="content">-->
					<!--&lt;!&ndash;							若此项为空，则校验不生效<br>&ndash;&gt;-->
					<!--&lt;!&ndash;							该信息可以使用${ctx.value}获取&ndash;&gt;-->
					<!--						</fb-container>-->
					<!--					</fb-tooltip>-->
					<fb-input :value="col.formItemProps.name" readonly
					          @input="(value)=>{updateCol(value, 'formItemProps', 'name')}"/>
				</fb-form-item>
				<fb-form-item label="控件名称"
				              :label-text-style="{width: '56px'}"
				              :label-extra-style="{textAlign: 'right'}"
				              label-fixed
				              prop="label">
					<fb-tag slot="label-extra" type="primary" effect="light" style="margin-right: 0; margin-bottom: 4px"
					        :label="getComponentNameByType(activeComponent.type)"></fb-tag>
					<fb-input :value="col.formItemProps.label"
					          @input="(value)=>{updateCol(value, 'formItemProps', 'label')}"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="show">
					<fb-checkbox label="是否显示当前字段" :value="col.formItemProps.show"
					             @input="(value)=>{updateCol(value, 'formItemProps', 'show')}"/>
				</fb-form-item>

				<fb-form-item v-if="col.formItemProps.show === true" :label-width="160" prop="showLabel">
					<fb-checkbox label="显示标题" :value="col.formItemProps.showLabel"
					             @input="(value)=>{updateCol(value, 'formItemProps', 'showLabel')}"/>
				</fb-form-item>

				<fb-form-item v-if="col.formItemProps.show === true" label="标题宽度(px)" :label-width="160"
				              prop="labelWidth">
					<fb-input-number :value="col.formItemProps.labelWidth"
					                 @input="(value)=>{updateCol(value, 'formItemProps', 'labelWidth')}"
					                 append="px"

					>
					</fb-input-number>
				</fb-form-item>

				<fb-form-item v-if="col.formItemProps.show === true" label="标题位置" :label-width="160"
				              prop="labelPosition">
					<fb-radio-group
						button
						:value="col.formItemProps.labelPosition"
						@input="(value)=>{updateCol(value, 'formItemProps', 'labelPosition')}"
						:data="[
									   {label: '默认', value:''},
									   {label: '左', value:'left'},
									   {label: '右', value:'right'},
									   {label: '上', value:'top'}
								   ]"
					></fb-radio-group>
				</fb-form-item>

				<fb-form-item label="栅格占用" :label-width="160"
				>

					<fb-table-layout>
						<fb-table-layout-cell padding="0 2px 0 0">
							<fb-container>
								<fb-input :value="col.formItemProps.colSpan"
								          @input="(value)=>{updateIntegerCol(value, 'formItemProps', 'colSpan')}"
								          prepend="占位"
								          append="/24"
								/>
							</fb-container>
							<fb-container pl="6px" pr="10px">
								<fb-slider
									:value="col.formItemProps.colSpan"
									@input="(value)=>{updateIntegerCol(value, 'formItemProps', 'colSpan')}"
									:step="1" :min="0" :max="24"/>
							</fb-container>
						</fb-table-layout-cell>
						<fb-table-layout-cell padding="0 0 0 2px">
							<fb-container>
								<fb-input :value="col.formItemProps.colOffset"
								          @input="(value)=>{updateIntegerCol(value, 'formItemProps', 'colOffset')}">
									<template slot="prepend">偏移</template>
									<template slot="append">/24</template>
								</fb-input>
							</fb-container>

							<fb-container pl="10px" pr="6px">
								<fb-slider
									:value="col.formItemProps.colOffset"
									@input="(value)=>{updateIntegerCol(value, 'formItemProps', 'colOffset')}"
									:step="1" :min="0" :max="24"/>
							</fb-container>
						</fb-table-layout-cell>
					</fb-table-layout>


				</fb-form-item>

				<fb-form-item label="检验规则" prop="rules" v-if="getComponentByType(activeComponent.type).rules">
					<fb-select
						multiple
						:value="col.formItemProps.rules"
						@input="(value)=>{updateCol(value, 'formItemProps', 'rules')}"
						:data="getComponentByType(activeComponent.type).rules"
					/>
				</fb-form-item>
				<template v-if="col.formItemProps.rules && col.formItemProps.rules.includes('array')">
					<fb-container margin="4px 0">复选校验配置</fb-container>
					<fb-table-layout>
						<fb-table-layout-cell padding="0 2px 0 0">
							<fb-input :value="col.formItemProps.ruleArrayLen" clearable
							          @input="(value)=>{updateIntegerCol(value, 'formItemProps', 'ruleArrayLen')}"
							          :el-style="{padding: '0 4px', textAlign: 'center'}">
								<template slot="prepend">总数</template>
							</fb-input>
						</fb-table-layout-cell>
						<fb-table-layout-cell>
							<fb-input :value="col.formItemProps.ruleArrayMax" clearable
							          @input="(value)=>{updateIntegerCol(value, 'formItemProps', 'ruleArrayMax')}"
							          :el-style="{padding: '0 4px', textAlign: 'center'}">
								<template slot="prepend">最多</template>
							</fb-input>
						</fb-table-layout-cell>
						<fb-table-layout-cell padding="0 0 0 2px">
							<fb-input :value="col.formItemProps.ruleArrayMin" clearable
							          @input="(value)=>{updateIntegerCol(value, 'formItemProps', 'ruleArrayMin')}"
							          :el-style="{padding: '0 4px', textAlign: 'center'}">
								<template slot="prepend">至少</template>
							</fb-input>
						</fb-table-layout-cell>
					</fb-table-layout>
				</template>

				<template v-if="col.formItemProps.rules && col.formItemProps.rules.includes('number')">
					<fb-container margin="4px 0">数字校验配置</fb-container>
					<fb-table-layout>
						<fb-table-layout-cell>
							<fb-input :value="col.formItemProps.ruleNumberMax" clearable
							          @input="(value)=>{updateNumberCol(value, 'formItemProps', 'ruleNumberMax')}"
							          :el-style="{padding: '0 4px', textAlign: 'center'}">
								<template slot="prepend">最大</template>
							</fb-input>
						</fb-table-layout-cell>
						<fb-table-layout-cell padding="0 0 0 2px">
							<fb-input :value="col.formItemProps.ruleNumberMin" clearable
							          @input="(value)=>{updateNumberCol(value, 'formItemProps', 'ruleNumberMin')}"
							          :el-style="{padding: '0 4px', textAlign: 'center'}">
								<template slot="prepend">最小</template>
							</fb-input>
						</fb-table-layout-cell>
					</fb-table-layout>
				</template>

				<template v-if="col.formItemProps.rules && col.formItemProps.rules.includes('integer')">
					<fb-container margin="4px 0">整数校验配置</fb-container>
					<fb-table-layout>
						<fb-table-layout-cell>
							<fb-input :value="col.formItemProps.ruleIntegerMax" clearable
							          @input="(value)=>{updateIntegerCol(value, 'formItemProps', 'ruleIntegerMax')}"
							          :el-style="{padding: '0 4px', textAlign: 'center'}">
								<template slot="prepend">最大</template>
							</fb-input>
						</fb-table-layout-cell>
						<fb-table-layout-cell padding="0 0 0 2px">
							<fb-input :value="col.formItemProps.ruleIntegerMin" clearable
							          @input="(value)=>{updateIntegerCol(value, 'formItemProps', 'ruleIntegerMin')}"
							          :el-style="{padding: '0 4px', textAlign: 'center'}">
								<template slot="prepend">最小</template>
							</fb-input>
						</fb-table-layout-cell>
					</fb-table-layout>
				</template>

				<template v-if="col.formItemProps.rules && col.formItemProps.rules.includes('money')">
					<fb-container margin="4px 0">金额配置</fb-container>
					<fb-table-layout long>
						<fb-table-layout-cell width="50%">
							<fb-input :value="col.formItemProps.ruleMoneyFixed" clearable
							          @input="(value)=>{updateIntegerCol(value, 'formItemProps', 'ruleMoneyFixed')}"
							          :el-style="{padding: '0 4px', textAlign: 'center'}">
								<template slot="prepend">小数位</template>
							</fb-input>
						</fb-table-layout-cell>
						<fb-table-layout-cell width="50%"></fb-table-layout-cell>
					</fb-table-layout>
				</template>

				<template v-if="col.formItemProps.rules && col.formItemProps.rules.includes('string')">
					<fb-container margin="4px 0">字符校验配置</fb-container>
					<fb-table-layout>
						<fb-table-layout-cell padding="0 2px 0 0">
							<fb-input :value="col.formItemProps.ruleStringLen"
							          @input="(value)=>{updateIntegerCol(value, 'formItemProps', 'ruleStringLen')}"
							          :el-style="{padding: '0 4px', textAlign: 'center'}">
								<template slot="prepend">总数</template>
							</fb-input>
						</fb-table-layout-cell>
						<fb-table-layout-cell>
							<fb-input :value="col.formItemProps.ruleStringMax"
							          @input="(value)=>{updateIntegerCol(value, 'formItemProps', 'ruleStringMax')}"
							          :el-style="{padding: '0 4px', textAlign: 'center'}">
								<template slot="prepend">最多</template>
							</fb-input>
						</fb-table-layout-cell>
						<fb-table-layout-cell padding="0 0 0 2px">
							<fb-input :value="col.formItemProps.ruleStringMin"
							          @input="(value)=>{updateIntegerCol(value, 'formItemProps', 'ruleStringMin')}"
							          :el-style="{padding: '0 4px', textAlign: 'center'}">
								<template slot="prepend">至少</template>
							</fb-input>
						</fb-table-layout-cell>
					</fb-table-layout>

					<fb-checkbox
						label="空格"
						:value="col.formItemProps.ruleStringWhitespace"
						@input="(value)=>{updateCol(value, 'formItemProps', 'ruleStringWhitespace')}"
						:el-style="{padding: '0 4px', textAlign: 'center'}">
					</fb-checkbox>
				</template>

				<template v-if="col.formItemProps.rules && col.formItemProps.rules.includes('enum')">
					<fb-container margin="4px 0">枚举项配置</fb-container>
					<fb-form-editor-object-prop
						:show-label="false"
						:value="col.formItemProps.ruleEnum || []"
						@input="(value)=>{
							if(!col.formItemProps.ruleEnum) {
								 updateProps([], 'rows', activeComponent.rowIndex, 'cols', activeComponent.colIndex, 'formItemProps', 'ruleEnum')
							}
							$nextTick(()=>{
								updateProps(value, 'rows', activeComponent.rowIndex, 'cols', activeComponent.colIndex, 'formItemProps', 'ruleEnum')
							})
						}"
					/>

				</template>

			</template>


			<template v-if="activeComponent.type === 'fb-text'">

				<fb-fieldset label="控件属性"/>

				<fb-form-item label="内容(value)" :label-width="160" prop="defaultValue">
					<fb-input
						:value="col.props.defaultValue"
						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>
				</fb-form-item>

				<fb-form-item label="尺寸(size)" :label-width="160" prop="size">
					<fb-input
						:value="col.props.size"
						@input="(value)=>{ ; updateCol(value, 'props', 'size')}"
					/>

					<fb-container>
						<fb-radio-group radio-space="12"
						                @input="(value)=>{ ; updateCol(value, 'props', 'size')}"
						                :data="getComponentByType(activeComponent.type).sizes"
						/>
					</fb-container>

				</fb-form-item>

				<fb-form-item :label-width="160" prop="bold">
					<fb-checkbox
						label="加粗(bold)"
						:value="col.props.bold"
						@input="(value)=>{updateCol(value, 'props', 'bold')}"/>
				</fb-form-item>

				<fb-form-item label="宽度(width)" :label-width="160" prop="width">

					<fb-space size="4">
						<fb-input-number
							controls-position="right"
							:value="col.props.width"
							@input="(value)=>{updateCol(value, 'props', 'width')}">
						</fb-input-number>
						<fb-select
							style="width: 70px; text-align: left"
							:clearable="false"
							:value="col.props.widthUnit"
							@input="(value)=>{updateCol(value, 'props', 'widthUnit')}"
							:data="[
															 {label:'px', value: 'px'},
															 {label:'%', value: '%'},
															 {label:'em', value: 'em'},
															 {label:'rem', value: 'rem'},
															 {label:'vh', value: 'vh'},
															 {label:'vw', value: 'vw'},
							                             ]"
						></fb-select>
					</fb-space>

				</fb-form-item>

				<fb-form-item :label-width="160" prop="ellipsis">
					<fb-checkbox
						label="溢出控制(ellipsis)，请配合width使用"
						:value="col.props.ellipsis"
						@input="(value)=>{updateCol(value, 'props', 'ellipsis')}"/>
				</fb-form-item>

				<fb-form-item label="左右对齐" :label-width="160" prop="align">
					<fb-radio-group button
					                :value="col.props.align"
					                @input="(value)=>{ ; updateCol(value, 'props', 'align')}"
					                :data="getComponentByType(activeComponent.type).align"
					/>
				</fb-form-item>

				<fb-form-item label="上下对齐" :label-width="160" prop="va">
					<fb-select
						:value="col.props.va"
						@input="(value)=>{ ; updateCol(value, 'props', 'va')}"
						:data="getComponentByType(activeComponent.type).va"
					/>
				</fb-form-item>

				<fb-form-item label="字体(family)" :label-width="160" prop="family">
					<fb-select
						:value="col.props.family"
						@input="(value)=>{ ; updateCol(value, 'props', 'family')}"
						:data="getComponentByType(activeComponent.type).fontFamily"
					/>
				</fb-form-item>

			</template>


			<template v-if="activeComponent.type === 'fb-input'">

				<fb-fieldset label="控件属性"/>

<!--				<fb-form-item label="默认值(value)" :label-width="160" prop="defaultValue">-->
<!--					<fb-input-->
<!--						:value="col.props.defaultValue"-->
<!--						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>-->
<!--				</fb-form-item>-->

				<fb-form-item label="占位字符(placeholder)" :label-width="160" prop="placeholder">
					<fb-input :value="col.props.placeholder"
					          @input="(value)=>{updateCol(value, 'props', 'placeholder')}"/>
				</fb-form-item>


				<fb-form-item label="最大输入字符数(maxlength)" :label-width="160" prop="maxlength">
					<fb-input :value="col.props.maxlength"
					          @input="(value)=>{updateCol(value, 'props', 'maxlength')}"/>
				</fb-form-item>

				<fb-form-item label="尺寸(size)" :label-width="160" prop="size">
					<fb-radio-group radio-space="12"
					                :value="col.props.size"
					                @input="(value)=>{ ; updateCol(value, 'props', 'size')}"
					                :data="getComponentByType(activeComponent.type).sizes"
					/>
				</fb-form-item>

				<fb-form-item label="密码(password)" :label-width="160" prop="type">
					<fb-radio-group radio-space="12"
					                :value="col.props.type"
					                @input="(value)=>{updateCol(value, 'props', 'type')}"
					                :data="getComponentByType(activeComponent.type).types"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="disabled">
					<fb-checkbox
						:value="col.props.disabled"
						@input="(value)=>{updateCol(value, 'props', 'disabled')}"
						label="禁用"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="readonly">
					<fb-checkbox label="只读" :value="col.props.readonly"
					             @input="(value)=>{updateCol(value, 'props', 'readonly')}"/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox
						:value="col.props.clearable"
						@input="(value)=>{updateCol(value, 'props', 'clearable')}"
						label="可清除"
					/>
				</fb-form-item>

				<fb-form-item label="前置图标" :label-width="160" prop="prependIcon">
					<fb-space>
						<fb-icon-select :value="col.props.prependIcon"
						                size="s"
						                @input="(value)=>{updateCol(value, 'props', 'prependIcon')}"/>
						<fb-button icon="close-square"
						           @on-click="(value)=>{updateCol('', 'props', 'prependIcon')}"></fb-button>
					</fb-space>
				</fb-form-item>

				<fb-form-item label="后置图标" :label-width="160" prop="icon">
					<fb-space>
						<fb-icon-select
							:value="col.props.icon"
							size="s"
							@input="(value)=>{updateCol(value, 'props', 'icon')}"/>
						<fb-button icon="close-square"
						           @on-click="(value)=>{updateCol('', 'props', 'icon')}"></fb-button>
					</fb-space>
				</fb-form-item>


				<fb-form-item label="前置文本" :label-width="160" prop="prepend">
					<fb-input
						:value="col.props.prepend"
						@input="(value)=>{updateCol(value, 'props', 'prepend')}"
					/>
				</fb-form-item>
				<fb-form-item label="后置文本" :label-width="160" prop="prepend">
					<fb-input
						:value="col.props.append"
						@input="(value)=>{updateCol(value, 'props', 'append')}"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox label="圆角" :value="col.props.round"
					             @input="(value)=>{updateCol(value, 'props', 'round')}"
					/>


				</fb-form-item>

			</template>


			<template v-if="activeComponent.type === 'fb-input-number'">

				<fb-fieldset label="控件属性"/>

<!--				<fb-form-item label="默认值(value)" :label-width="160" prop="defaultValue">-->
<!--					<fb-input-->
<!--						:value="col.props.defaultValue"-->
<!--						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>-->
<!--				</fb-form-item>-->

				<fb-form-item label="占位字符(placeholder)" :label-width="160" prop="placeholder">
					<fb-input :value="col.props.placeholder"
					          @input="(value)=>{updateCol(value, 'props', 'placeholder')}"/>
				</fb-form-item>

				<fb-form-item label="尺寸(size)" :label-width="160" prop="size">
					<fb-radio-group radio-space="12"
					                :value="col.props.size"
					                @input="(value)=>{ ; updateCol(value, 'props', 'size')}"
					                :data="getComponentByType(activeComponent.type).sizes"
					/>
				</fb-form-item>


				<fb-form-item :label-width="160" prop="controls">
					<fb-space>
						<fb-container>
							<fb-checkbox
								label="显示控制按钮"
								:value="col.props.controls"
								@input="(value)=>{ ; updateCol(value, 'props', 'controls')}"
							/>
						</fb-container>
						<fb-container height="30px" valign="center" v-if="col.props.controls === true">
							<fb-text>位置</fb-text>
						</fb-container>
						<fb-container v-if="col.props.controls === true">
							<fb-radio-group button

							                :value="col.props.controlsPosition"
							                @input="(value)=>{ ; updateCol(value, 'props', 'controlsPosition')}"
							                :data="getComponentByType(activeComponent.type).controlsPosition"
							/>
						</fb-container>
					</fb-space>
				</fb-form-item>

				<fb-form-item label="步进" :label-width="160" prop="controls">
					<fb-space>
						<fb-container>
							<fb-input-number
								:value="(()=>{
									let step = 1;
									if (typeof col.props.step !== 'undefined' && isNumber(Number(col.props.step))) {
										 return col.props.step
									}
									return step
								})()"
								:precision="2"
								:step="0.01"
								@input="(value)=>{  ; updateCol(value, 'props', 'step')}"
								:el-style="{padding: '0 6px'}"
							/>
						</fb-container>

						<fb-container>
							<fb-checkbox
								label="严格模式"
								:value="col.props.stepStrictly"
								@input="(value)=>{ ; updateCol(value, 'props', 'stepStrictly')}"
							/>
						</fb-container>

					</fb-space>
				</fb-form-item>

				<fb-form-item label="精度(precision)" :label-width="160" prop="precision">
					<fb-input-number
						:value="col.props.precision"
						@input="(value)=>{ ; updateCol(value, 'props', 'precision')}"
					/>
				</fb-form-item>


				<fb-form-item label="最小值(min)" :label-width="160" prop="min">
					<fb-input-number
						:value="(()=>{
							let min = -Infinity;
							if (typeof col.props.min !== 'undefined' && isNumber(Number(col.props.min))) {
								min = col.props.min
							}
							return min
						})()"
						@input="(value)=>{ ; updateCol(value, 'props', 'min')}"
					/>
				</fb-form-item>

				<fb-form-item label="最大值(max)" :label-width="160" prop="max">
					<fb-input-number
						:value="(()=>{
							let max = Infinity;
							if (typeof col.props.max !== 'undefined' && isNumber(Number(col.props.max))) {
								max = col.props.max
							}
							return max
						})()"
						@input="(value)=>{ ; updateCol(value, 'props', 'max')}"
					/>
				</fb-form-item>


				<fb-form-item :label-width="160" prop="disabled">
					<fb-checkbox
						:value="col.props.disabled"
						@input="(value)=>{updateCol(value, 'props', 'disabled')}"
						label="禁用"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="readonly">
					<fb-checkbox label="只读" :value="col.props.readonly"
					             @input="(value)=>{updateCol(value, 'props', 'readonly')}"/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox
						:value="col.props.clearable"
						@input="(value)=>{updateCol(value, 'props', 'clearable')}"
						label="可清除"
					/>
				</fb-form-item>


				<fb-form-item label="前置图标" :label-width="160" prop="prependIcon">
					<fb-space>
						<fb-icon-select :value="col.props.prependIcon"
						                size="s"
						                @input="(value)=>{updateCol(value, 'props', 'prependIcon')}"/>
						<fb-button icon="close-square"
						           @on-click="(value)=>{updateCol('', 'props', 'prependIcon')}"></fb-button>
					</fb-space>
				</fb-form-item>

				<fb-form-item label="后置图标" :label-width="160" prop="icon">
					<fb-space>
						<fb-icon-select
							:value="col.props.icon"
							size="s"
							@input="(value)=>{updateCol(value, 'props', 'icon')}"/>
						<fb-button icon="close-square"
						           @on-click="(value)=>{updateCol('', 'props', 'icon')}"></fb-button>
					</fb-space>
				</fb-form-item>


				<fb-form-item label="前置文本" :label-width="160" prop="prepend">
					<fb-input
						:value="col.props.prepend"
						@input="(value)=>{updateCol(value, 'props', 'prepend')}"
					/>
				</fb-form-item>
				<fb-form-item label="后置文本" :label-width="160" prop="prepend">
					<fb-input
						:value="col.props.append"
						@input="(value)=>{updateCol(value, 'props', 'append')}"
					/>
				</fb-form-item>


			</template>


			<template v-if="activeComponent.type === 'fb-textarea'">

				<fb-fieldset label="控件属性"/>

<!--				<fb-form-item label="默认值(value)" :label-width="160" prop="defaultValue">-->
<!--					<fb-input-->
<!--						:value="col.props.defaultValue"-->
<!--						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>-->
<!--				</fb-form-item>-->

				<fb-form-item label="占位字符(placeholder)" :label-width="160" prop="placeholder">
					<fb-input
						:value="col.props.placeholder"
						@input="(value)=>{updateCol(value, 'props', 'placeholder')}"/>
				</fb-form-item>

				<fb-form-item label="行数" :label-width="160" prop="rows">
					<fb-input
						:value="col.props.rows"
						@input="(value)=>{updateCol(value, 'props', 'rows')}"
					/>
				</fb-form-item>

				<fb-form-item label="最大输入字符数(maxlength)" :label-width="160" prop="maxlength">
					<fb-input :value="col.props.maxlength"
					          @input="(value)=>{updateCol(value, 'props', 'maxlength')}"/>
				</fb-form-item>

				<fb-form-item label="尺寸(size)" :label-width="160" prop="size">
					<fb-radio-group radio-space="12"
					                :value="col.props.size"
					                @input="(value)=>{ ; updateCol(value, 'props', 'size')}"
					                :data="getComponentByType(activeComponent.type).sizes"
					/>
				</fb-form-item>


			</template>

			<template v-if="activeComponent.type === 'fb-select'">

				<fb-fieldset label="控件属性"/>

				<fb-form-item label="快速配置业务字段" :label-width="160">
					<fb-tooltip placement="top" slot="label-extra">
						<fb-icon name="information" color="red"/>
						<fb-container slot="content">
							快速配置业务字段<br>
							快速配置业务字段
						</fb-container>
					</fb-tooltip>
					<fb-select
						:data="getComponentByType(activeComponent.type).fastBizProps"
						:value="col.props.bizType"
						@input="(value)=>{
							updateBizType(value, 'props', 'bizType')
						}"/>
				</fb-form-item>

<!--				<fb-form-item label="默认值(value)" :label-width="160" prop="defaultValue">-->
<!--					<fb-input-->
<!--						:value="col.props.defaultValue"-->
<!--						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>-->
<!--				</fb-form-item>-->

				<fb-form-item label="占位字符(placeholder)" prop="placeholder">
					<fb-input
						:value="col.props.placeholder"
						@input="(value)=>{updateCol(value, 'props', 'placeholder')}"/>
				</fb-form-item>

				<fb-form-item prop="multiple"
				>
					<fb-checkbox
						label="多选模式"
						:value="col.props.multiple"
						@input="(value)=>{updateCol(value, 'props', 'multiple')}"/>

				</fb-form-item>

				<fb-form-item label="静态数据(data)" prop="data">
					<fb-form-editor-object-editor
						:col="col"
						view-type="add"
					/>
				</fb-form-item>

				<fb-form-item label="接口服务(service)" prop="service">

					<fb-tree-select
						:value="col.props.service"
						@input="(value)=>{updateCol(value, 'props', 'service')}"
						:data="formEditor.editor.services"
						:header-format="(node)=>{
								return node && node.label
							}"
					/>
				</fb-form-item>


				<fb-form-item label="查询参数(param)" prop="param">
					<fb-container slot="label-extra">
						<fb-button size="s" @on-click="showDictDialog">查询字典</fb-button>
					</fb-container>
					<fb-form-editor-object-editor
						:col="col"
						view-type="add"
						:reader="{
						 	data: 'param',
						 }"
					/>
				</fb-form-item>
				<fb-form-item label="数据适配器(reader)" prop="reader">
					<fb-form-editor-object-prop
						:value="col.props.reader"
						@input="(value)=>{updateCol(value, 'props', 'reader')}"
						:remove-button="false"
						:add-button="false"
					/>
				</fb-form-item>

				<fb-form-item label="尺寸(size)" :label-width="160" prop="size">
					<fb-radio-group radio-space="12"
					                :value="col.props.size"
					                @input="(value)=>{ ; updateCol(value, 'props', 'size')}"
					                :data="getComponentByType(activeComponent.type).sizes"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="disabled">
					<fb-checkbox
						:value="col.props.disabled"
						@input="(value)=>{updateCol(value, 'props', 'disabled')}"
						label="禁用"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="readonly">
					<fb-checkbox
						:value="col.props.readonly"
						@input="(value)=>{updateCol(value, 'props', 'readonly')}"
						label="只读"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox
						:value="col.props.clearable"
						@input="(value)=>{updateCol(value, 'props', 'clearable')}"
						label="可清除"
					/>
				</fb-form-item>


				<fb-form-item :label-width="160" prop="filterable">
					<fb-checkbox
						:value="col.props.filterable"
						@input="(value)=>{updateCol(value, 'props', 'filterable')}"
						label="搜索框"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="autoLoad">
					<fb-checkbox
						:value="col.props.autoLoad"
						@input="(value)=>{updateCol(value, 'props', 'autoLoad')}"
						label="autoLoad"
					/>
				</fb-form-item>


			</template>

			<template v-if="activeComponent.type === 'fb-tree-select'">

				<fb-fieldset label="控件属性"/>

				<fb-form-item label="快速配置业务字段" :label-width="160">
					<fb-select
						:data="getComponentByType(activeComponent.type).fastBizProps"
						:value="col.props.bizType"
						@input="(value)=>{
							updateBizType(value, 'props', 'bizType')
						}"/>
				</fb-form-item>

<!--				<fb-form-item label="默认值(value)" :label-width="160" prop="defaultValue">-->
<!--					<fb-input-->
<!--						:value="col.props.defaultValue"-->
<!--						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>-->
<!--				</fb-form-item>-->

				<fb-form-item label="占位字符(placeholder)" prop="placeholder">
					<fb-input
						:value="col.props.placeholder"
						@input="(value)=>{updateCol(value, 'props', 'placeholder')}"/>
				</fb-form-item>

				<fb-form-item prop="multiple"
				>
					<fb-checkbox
						label="多选模式"
						:value="col.props.multiple"
						@input="(value)=>{updateCol(value, 'props', 'multiple')}"/>

				</fb-form-item>

				<fb-form-item label="静态数据(data)" prop="data">
					<fb-form-editor-object-editor
						:col="col"
						view-type="add"
					/>
				</fb-form-item>

				<fb-form-item label="接口服务(service)" prop="service">
					<fb-tree-select
						:value="col.props.service"
						@input="(value)=>{updateCol(value, 'props', 'service')}"
						:data="formEditor.editor.services"
						:header-format="(node)=>{
								return node && node.label
							}"
					/>
				</fb-form-item>

				<fb-form-item label="查询参数(param)" prop="param">
					<fb-container slot="label-extra">
						<fb-button size="s" @on-click="showDictDialog">查询字典</fb-button>
					</fb-container>
					<fb-form-editor-object-editor
						:col="col"
						view-type="add"
						:reader="{
						 	data: 'param',
						 }"
					/>
				</fb-form-item>

				<fb-form-item label="数据适配器(reader)" prop="reader">
					<fb-form-editor-object-prop
						:value="col.props.reader"
						@input="(value)=>{updateCol(value, 'props', 'reader')}"
						:remove-button="false"
						:add-button="false"
					/>
				</fb-form-item>


				<fb-form-item label="勾选影响范围" :label-width="160" prop="doCheck">
					<fb-radio-group radio-space="12"
					                button
					                :data="[
							{label:'父子', value: 'ps'},
							{label:'父', value: 'p'},
							{label:'子', value: 's'},
							{label:'无', value: ''},
						]"
					                :value="col.props.doCheck"
					                @input="(value)=>{updateCol(value, 'props', 'doCheck')}"
					/>
				</fb-form-item>

				<fb-form-item label="取消勾选影响范围" :label-width="160" prop="doUnCheck">
					<fb-radio-group radio-space="12"
					                button
					                :data="[
							{label:'父子', value: 'ps'},
							{label:'父', value: 'p'},
							{label:'子', value: 's'},
							{label:'无', value: ''},
						]"
					                :value="col.props.doUnCheck"
					                @input="(value)=>{updateCol(value, 'props', 'doUnCheck')}"
					/>
				</fb-form-item>

				<fb-form-item label="尺寸(size)" :label-width="160" prop="size">
					<fb-radio-group radio-space="12"
					                :value="col.props.size"
					                @input="(value)=>{ ; updateCol(value, 'props', 'size')}"
					                :data="getComponentByType(activeComponent.type).sizes"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="onlyLeaf">
					<fb-checkbox
						:value="col.props.onlyLeaf"
						@input="(value)=>{updateCol(value, 'props', 'onlyLeaf')}"
						label="只能选择叶子节点"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="disabled">
					<fb-checkbox

						:value="col.props.disabled"
						@input="(value)=>{updateCol(value, 'props', 'disabled')}"
						label="禁用"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="readonly">
					<fb-checkbox

						:value="col.props.readonly"
						@input="(value)=>{updateCol(value, 'props', 'readonly')}"
						label="只读"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox
						:value="col.props.clearable"
						@input="(value)=>{updateCol(value, 'props', 'clearable')}"
						label="可清除"
					/>
				</fb-form-item>


				<fb-form-item :label-width="160" prop="filterable">
					<fb-checkbox

						:value="col.props.filterable"
						@input="(value)=>{updateCol(value, 'props', 'filterable')}"
						label="显示搜索框"
					/>
				</fb-form-item>


			</template>

			<template v-if="activeComponent.type === 'fb-radio-group'">

				<fb-fieldset label="控件属性"/>

				<fb-form-item label="快速配置业务字段" :label-width="160">
					<fb-select

						:data="getComponentByType(activeComponent.type).fastBizProps"
						:value="col.props.bizType"
						@input="(value)=>{
							updateBizType(value, 'props', 'bizType')
						}"/>
				</fb-form-item>

<!--				<fb-form-item label="默认值(value)" :label-width="160" prop="defaultValue">-->
<!--					<fb-input-->
<!--						:value="col.props.defaultValue"-->
<!--						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>-->
<!--				</fb-form-item>-->

				<fb-form-item label="静态数据(data)" prop="data">
					<fb-form-editor-object-editor
						:col="col"
						view-type="add"
					/>
				</fb-form-item>

				<fb-form-item label="接口服务(service)" prop="service">
					<fb-tree-select
						:value="col.props.service"
						@input="(value)=>{updateCol(value, 'props', 'service')}"
						:data="formEditor.editor.services"
						:header-format="(node)=>{

								return node && node.label
							}"
					/>
				</fb-form-item>

				<fb-form-item label="数据适配器(reader)" prop="reader">
					<fb-form-editor-object-prop
						:value="col.props.reader"
						@input="(value)=>{updateCol(value, 'props', 'reader')}"
						:remove-button="false"
						:add-button="false"
					/>
				</fb-form-item>

				<fb-form-item label="元素间距(radioSpace)" :label-width="160" prop="radioSpace">
					<fb-input-number
						:min="0"
						:value="col.props.radioSpace"
						@input="(value)=>{updateIntegerCol(value, 'props', 'radioSpace')}"
						append="px"
					>
					</fb-input-number>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="disabled">
					<fb-checkbox

						:value="col.props.disabled"
						@input="(value)=>{updateCol(value, 'props', 'disabled')}"
						label="禁用"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="readonly">
					<fb-checkbox label="只读"
					             :value="col.props.readonly"
					             @input="(value)=>{updateCol(value, 'props', 'readonly')}"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="button">
					<fb-checkbox
						label="按钮组"
						:value="col.props.button"
						@input="(value)=>{updateCol(value, 'props', 'button')}"
					/>
				</fb-form-item>
				<fb-form-item :label-width="160" prop="list">
					<fb-checkbox
						label="列表显示"
						:value="col.props.list"
						@input="(value)=>{updateCol(value, 'props', 'list')}"
					/>
				</fb-form-item>
				<fb-form-item :label-width="160" prop="vertical">
					<fb-checkbox
						label="垂直显示"
						:value="col.props.vertical"
						@input="(value)=>{updateCol(value, 'props', 'vertical')}"
					/>
				</fb-form-item>


			</template>

			<template v-if="activeComponent.type === 'fb-checkbox-group'">

				<fb-fieldset label="控件属性"/>

				<fb-form-item label="快速配置业务字段" :label-width="160">
					<fb-select

						:data="getComponentByType(activeComponent.type).fastBizProps"
						:value="col.props.bizType"
						@input="(value)=>{
							updateBizType(value, 'props', 'bizType')
						}"/>
				</fb-form-item>

				<!--				<fb-form-item label="默认值(value)" :label-width="160" prop="defaultValue">-->
				<!--					&lt;!&ndash;					<fb-input&ndash;&gt;-->
				<!--					&lt;!&ndash;						:value="col.props.defaultValue"&ndash;&gt;-->
				<!--					&lt;!&ndash;						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>&ndash;&gt;-->

				<!--					<fb-form-editor-object-prop-->
				<!--						:value="(()=>{-->
				<!--							let data = []-->
				<!--							col.props.data.map((n, i)=>{-->
				<!--								let label = n.label,-->
				<!--								 value = false-->

				<!--								if (col.props.defaultValue && (typeof col.props.defaultValue[i] !== 'undefined')) {-->
				<!--									value = col.props.defaultValue[i].value-->
				<!--								}-->

				<!--								data.push({-->
				<!--									label,-->
				<!--									value-->
				<!--								})-->
				<!--							})-->


				<!--							return data-->

				<!--						})()"-->
				<!--						@input="(value)=>{updateCol(value,'props', 'defaultValue')}"-->
				<!--						:remove-button="false"-->
				<!--						:add-button="false"-->
				<!--					/>-->
				<!--				</fb-form-item>-->

				<fb-form-item label="静态数据(data)" prop="data">
					<fb-form-editor-object-editor
						:col="col"
						view-type="add"
					/>
				</fb-form-item>

				<!--				<fb-form-item label="接口服务(service)" prop="service">-->
				<!--					<fb-tree-select-->
				<!--						:value="col.props.service"-->
				<!--						@input="(value)=>{updateCol(value, 'props', 'service')}"-->
				<!--						:data="formEditor.editor.services"-->
				<!--						:header-format="(node)=>{-->
				<!--								return node && node.value-->
				<!--							}"-->
				<!--					/>-->
				<!--				</fb-form-item>-->

				<fb-form-item label="数据适配器(reader)" prop="reader">
					<fb-form-editor-object-prop
						:value="col.props.reader"
						@input="(value)=>{updateCol(value, 'props', 'reader')}"
						:remove-button="false"
						:add-button="false"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="disabled">
					<fb-checkbox

						:value="col.props.disabled"
						@input="(value)=>{updateCol(value, 'props', 'disabled')}"
						label="禁用"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="readonly">
					<fb-checkbox
						:value="col.props.readonly"
						@input="(value)=>{updateCol(value, 'props', 'readonly')}"
						label="只读"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="vertical">
					<fb-checkbox
						label="垂直显示"
						:value="col.props.vertical"
						@input="(value)=>{updateCol(value, 'props', 'vertical')}"
					/>
				</fb-form-item>


			</template>

			<template v-if="activeComponent.type === 'fb-checkbox'">

				<fb-fieldset label="控件属性"/>

				<!--				<fb-form-item label="默认值(value)" :label-width="160" prop="defaultValue">-->
				<!--					<fb-input-->
				<!--						:value="col.props.defaultValue"-->
				<!--						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>-->
				<!--				</fb-form-item>-->

				<fb-form-item :label-width="160" prop="value">
					<fb-checkbox
						:value="col.props.value"
						@input="(value)=>{updateCol(value, 'props', 'value')}"
						label="选中"
					/>
				</fb-form-item>

				<fb-form-item label="选项名称" :label-width="160" prop="label">
					<fb-input
						:value="col.props.label"
						@input="(value)=>{updateCol(value, 'props', 'label')}"
					/>
				</fb-form-item>

				<fb-form-item label="勾选/不勾选名称" :label-width="160" prop="label">
					<fb-container>
						<fb-input
							:value="col.props.trueLabel"
							@input="(value)=>{updateCol(value, 'props', 'trueLabel')}"
						>
							<template slot="prepend">已勾选显示</template>
						</fb-input>
					</fb-container>
					<fb-container mt="4px">
						<fb-input
							:value="col.props.falseLabel"
							@input="(value)=>{updateCol(value, 'props', 'falseLabel')}"
						>
							<template slot="prepend">未勾选显示</template>
						</fb-input>
					</fb-container>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="disabled">
					<fb-checkbox
						:value="col.props.disabled"
						@input="(value)=>{updateCol(value, 'props', 'disabled')}"
						label="禁用"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="readonly">
					<fb-checkbox
						:value="col.props.readonly"
						@input="(value)=>{updateCol(value, 'props', 'readonly')}"
						label="只读"
					/>
				</fb-form-item>


			</template>


			<template v-if="activeComponent.type === 'fb-color-picker'">

				<fb-fieldset label="控件属性"/>

<!--				<fb-form-item label="默认值(value)" :label-width="160" prop="defaultValue">-->
<!--					<fb-input-->
<!--						:value="col.props.defaultValue"-->
<!--						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>-->
<!--				</fb-form-item>-->

				<!--				<fb-form-item label="占位字符(placeholder)" :label-width="160" prop="placeholder">-->
				<!--					<fb-input :value="col.props.placeholder"-->
				<!--					          @input="(value)=>{updateCol(value, 'props', 'placeholder')}"/>-->
				<!--				</fb-form-item>-->


			</template>


			<template v-if="activeComponent.type === 'tp-datepicker'">

				<fb-fieldset label="控件属性"/>

				<fb-form-item label="快速配置业务字段" :label-width="160">
					<fb-select

						:data="getComponentByType(activeComponent.type).fastBizProps"
						:value="col.props.bizType"
						@input="(value)=>{
							updateBizType(value, 'props', 'bizType')
						}"/>
				</fb-form-item>

<!--				<fb-form-item label="默认值(value)" :label-width="160" prop="defaultValue">-->
<!--					<tp-datepicker-->
<!--						:value="col.props.defaultValue"-->
<!--						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>-->
<!--				</fb-form-item>-->

				<!--				<fb-form-item label="面板模式" :label-width="160" prop="mode">-->
				<!--					<fb-radio-group radio-space="12" button-->
				<!--									:value="col.props.mode"-->
				<!--									@input="(value)=>{updateCol(value, 'props', 'mode')}"-->
				<!--									:data="[-->
				<!--										  {label: '单日历', value:'single'},-->
				<!--//										  {label: '日期多选', value:'multiple'},-->
				<!--//										  {label: '区间日历', value:'range'},-->
				<!--									  ]"-->
				<!--					/>-->
				<!--				</fb-form-item>-->

				<fb-form-item label="格式化字符串" :label-width="160" prop="format">
					<fb-tooltip placement="top" content="format" slot="label-extra">
						<fb-icon name="information"/>
					</fb-tooltip>
					<fb-input
						:value="col.props.format"
						@input="(value)=>{updateCol(value, 'props', 'format')}"
					/>
				</fb-form-item>
				<fb-form-item label="值格式化字符串" :label-width="160" prop="valueFormat">
					<fb-tooltip placement="top" content="valueFormat" slot="label-extra">
						<fb-icon name="information"/>
					</fb-tooltip>
					<fb-input
						:value="col.props.valueFormat"
						@input="(value)=>{updateCol(value, 'props', 'valueFormat')}"
					/>
				</fb-form-item>
				<fb-form-item label="区间格式化字符串" :label-width="160" prop="rangeFormat">
					<fb-tooltip placement="top" content="rangeFormat" slot="label-extra">
						<fb-icon name="information"/>
					</fb-tooltip>
					<fb-input
						:value="col.props.rangeFormat"
						@input="(value)=>{updateCol(value, 'props', 'rangeFormat')}"
					/>
				</fb-form-item>

				<fb-form-item label="maxRange" :label-width="160" prop="maxRange">
					<fb-tooltip placement="top" slot="label-extra">
						<fb-container slot="content" width="300px">
							需要配合range使用，设置可选时间范围，格式为 2D（2天）、2M（2个月）、2Y（2年）
						</fb-container>
						<fb-icon name="information"/>
					</fb-tooltip>

					<fb-datepicker
						:value="col.props.maxRange"
						@input="(value)=>{updateCol(value, 'props', 'maxRange')}"
					/>
				</fb-form-item>
				<fb-form-item label="最小可选日期" :label-width="160" prop="minDate">
					<fb-datepicker
						:value="col.props.minDate"
						@input="(value)=>{updateCol(value, 'props', 'minDate')}"
					/>
				</fb-form-item>
				<fb-form-item label="最大可选日期" :label-width="160" prop="maxDate">
					<fb-datepicker
						:value="col.props.maxDate"
						@input="(value)=>{updateCol(value, 'props', 'maxDate')}"
					/>
				</fb-form-item>

				<fb-form-item label="占位字符(placeholder)" :label-width="160" prop="placeholder">
					<fb-input
						:value="col.props.placeholder"
						@input="(value)=>{updateCol(value, 'props', 'placeholder')}"/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="disabled">
					<fb-checkbox
						:value="col.props.disabled"
						@input="(value)=>{updateCol(value, 'props', 'disabled')}"
						label="禁用"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="readonly">
					<fb-checkbox
						:value="col.props.readonly"
						@input="(value)=>{updateCol(value, 'props', 'readonly')}"
						label="只读"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox

						:value="col.props.clearable"
						@input="(value)=>{updateCol(value, 'props', 'clearable')}"
						label="可清除"
					/>
				</fb-form-item>

				<fb-form-item label="下拉面板位置" :label-width="160" prop="position">
					<fb-select
						:value="col.props.position"
						@input="(value)=>{updateCol(value, 'props', 'position')}"
						:data="[
										  {label: 'bottomLeft', value:'bottomLeft'},
										  {label: 'bottomCenter', value:'bottomCenter'},
										  {label: 'bottomRight', value:'bottomRight'},
										  {label: 'topLeft', value:'topLeft'},
										  {label: 'topCenter', value:'topCenter'},
										  {label: 'topRight', value:'topRight'}
									  ]"
					/>
				</fb-form-item>


			</template>

			<template v-if="activeComponent.type === 'tp-upload'">

				<fb-fieldset label="控件属性"/>

<!--				<fb-form-item label="默认值(value)" :label-width="160" prop="defaultValue">-->
<!--					<fb-input-->
<!--						:value="col.props.defaultValue"-->
<!--						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>-->
<!--				</fb-form-item>-->

				<fb-form-item label="视图" :label-width="160" prop="view">
					<fb-radio-group radio-space="12" button
					                :value="col.props.view"
					                @input="(value)=>{updateCol(value, 'props', 'view')}"
					                :data="[
										  {label: '文件列表', value:'list'},
										  {label: '图像', value:'image'},
										  {label: '头像', value:'avatar'},
									  ]"
					/>
				</fb-form-item>

				<fb-form-item label="上传按钮文字" :label-width="160" prop="buttonText">
					<fb-input
						:value="col.props.buttonText"
						@input="(value)=>{updateCol(value, 'props', 'buttonText')}"
					/>
				</fb-form-item>

				<fb-form-item label="上传按钮图标" :label-width="160" prop="buttonIcon">
					<fb-input
						:value="col.props.buttonIcon"
						@input="(value)=>{updateCol(value, 'props', 'buttonIcon')}"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="showPreview">
					<fb-checkbox
						:value="col.props.showPreview"
						@input="(value)=>{updateCol(value, 'props', 'showPreview')}"
						label="显示预览图标"
					/>
				</fb-form-item>
				<fb-form-item :label-width="160" prop="showDownload">
					<fb-checkbox
						:value="col.props.showDownload"
						@input="(value)=>{updateCol(value, 'props', 'showDownload')}"
						label="显示下载图标"
					/>
				</fb-form-item>
				<fb-form-item :label-width="160" prop="showRemove">
					<fb-checkbox
						:value="col.props.showRemove"
						@input="(value)=>{updateCol(value, 'props', 'showRemove')}"
						label="显示删除图标"
					/>
				</fb-form-item>

				<fb-form-item label="接口服务(service)[只读]" prop="service">
					<fb-tree-select
						disabled
						:value="col.props.service"
						@input="(value)=>{updateCol(value, 'props', 'service')}"
						:data="formEditor.editor.services"
						:header-format="(node)=>{
								return node && node.label
							}"
					/>
				</fb-form-item>

				<fb-form-item label="附加参数" prop="param">
					<fb-form-editor-object-editor
						:col="col"
						view-type="add"
						:reader="{data: 'param'}"
					/>
				</fb-form-item>

				<!--				<fb-form-item label="referid" :label-width="160" prop="referid">-->
				<!--					<fb-input-->
				<!--						:value="col.props.referid"-->
				<!--						@input="(value)=>{updateCol(value, 'props', 'referid')}"-->
				<!--					/>-->
				<!--				</fb-form-item>-->
				<fb-form-item label="允许上传的文件类型(accept)" :label-width="160" prop="accept">
					<fb-input
						:value="col.props.accept"
						@input="(value)=>{updateCol(value, 'props', 'accept')}"
					/>
				</fb-form-item>
				<fb-form-item label="文件上传数量(maxLength)" :label-width="160" prop="maxLength">
					<fb-input
						:value="col.props.maxLength"
						@input="(value)=>{updateCol(value, 'props', 'maxLength')}"
					/>
				</fb-form-item>

				<fb-form-item v-if="col.props.view === 'image'" label="图像压缩质量(0-1)" :label-width="160" prop="quality">
					<fb-input
						:value="col.props.quality"
						@input="(value)=>{updateNumberCol(value, 'props', 'quality')}"
					>
						<fb-text slot="append">px</fb-text>
					</fb-input>
				</fb-form-item>
				<fb-form-item v-if="col.props.view === 'image'" label="图像处理宽度(px)" :label-width="160" prop="maxWidth">
					<fb-input
						:value="col.props.maxWidth"
						@input="(value)=>{updateNumberCol(value, 'props', 'maxWidth')}"
					>
						<fb-text slot="append">px</fb-text>
					</fb-input>
				</fb-form-item>
				<fb-form-item v-if="col.props.view === 'image'" label="图像处理高度(px)" :label-width="160" prop="maxHeight">
					<fb-input
						:value="col.props.maxHeight"
						@input="(value)=>{updateNumberCol(value, 'props', 'maxHeight')}"
					>
						<fb-text slot="append">px</fb-text>
					</fb-input>
				</fb-form-item>
				<fb-form-item v-if="col.props.view === 'avatar'" label="头像大小(px)" :label-width="160" prop="avatarSize">
					<fb-input
						:value="col.props.avatarSize"
						@input="(value)=>{updateNumberCol(value, 'props', 'avatarSize')}"
					>
						<fb-text slot="append">px</fb-text>
					</fb-input>
				</fb-form-item>

				<fb-form-item v-if="col.props.view === 'avatar'" :label-width="160" prop="avatarCircle">
					<fb-checkbox
						:value="col.props.avatarCircle"
						@input="(value)=>{updateCol(value, 'props', 'avatarCircle')}"
						label="圆形头像"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="disabled">
					<fb-checkbox
						:value="col.props.disabled"
						@input="(value)=>{updateCol(value, 'props', 'disabled')}"
						label="禁用"
					/>
				</fb-form-item>

				<fb-form-item :label-width="160" prop="readonly">
					<fb-checkbox
						:value="col.props.readonly"
						@input="(value)=>{updateCol(value, 'props', 'readonly')}"
						label="只读"
					/>
				</fb-form-item>


			</template>

			<template v-if="activeComponent.type === 'fb-editor'">

				<!--				<fb-fieldset label="控件属性"/>-->

				<!--				<fb-form-item label="默认值(value)" :label-width="160" prop="defaultValue">-->
				<!--					<fb-input-->
				<!--						:value="col.props.defaultValue"-->
				<!--						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>-->
				<!--				</fb-form-item>-->

			</template>
			<!--			biz-ent-person-single-select-->
			<!--			biz-ent-person-multiple-select-->
			<!--			biz-ent-person-single-select-using-table-show-->
			<!--			biz-ent-person-multiple-select-using-table-show-->
			<!--			biz-org-person-single-select-->
			<!--			biz-org-person-multiple-select-->
			<!--			biz-org-ent-single-select-->
			<!--			biz-org-ent-multiple-select-->
			<template v-if="activeComponent.type === 'biz-ent-person-single-select'">
				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox
						:value="col.props.clearable"
						@input="(value)=>{updateCol(value, 'props', 'clearable')}"
						label="可清除"
					/>
				</fb-form-item>
			</template>

			<template v-if="activeComponent.type === 'biz-ent-person-multiple-select'">
				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox
						:value="col.props.clearable"
						@input="(value)=>{updateCol(value, 'props', 'clearable')}"
						label="可清除"
					/>
				</fb-form-item>
			</template>

			<template v-if="activeComponent.type === 'biz-ent-person-single-select-using-table-show'">
				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox
						:value="col.props.clearable"
						@input="(value)=>{updateCol(value, 'props', 'clearable')}"
						label="可清除"
					/>
				</fb-form-item>
			</template>

			<template v-if="activeComponent.type === 'biz-ent-person-multiple-select-using-table-show'">
				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox
						:value="col.props.clearable"
						@input="(value)=>{updateCol(value, 'props', 'clearable')}"
						label="可清除"
					/>
				</fb-form-item>

			</template>

			<template v-if="activeComponent.type === 'biz-org-person-single-select'">
				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox
						:value="col.props.clearable"
						@input="(value)=>{updateCol(value, 'props', 'clearable')}"
						label="可清除"
					/>
				</fb-form-item>

			</template>

			<template v-if="activeComponent.type === 'biz-org-person-multiple-select'">
				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox
						:value="col.props.clearable"
						@input="(value)=>{updateCol(value, 'props', 'clearable')}"
						label="可清除"
					/>
				</fb-form-item>
			</template>

			<template v-if="activeComponent.type === 'biz-org-ent-single-select'">
				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox
						:value="col.props.clearable"
						@input="(value)=>{updateCol(value, 'props', 'clearable')}"
						label="可清除"
					/>
				</fb-form-item>
			</template>

			<template v-if="activeComponent.type === 'biz-org-ent-multiple-select'">
				<fb-form-item :label-width="160" prop="clearable">
					<fb-checkbox
						:value="col.props.clearable"
						@input="(value)=>{updateCol(value, 'props', 'clearable')}"
						label="可清除"
					/>
				</fb-form-item>

			</template>

			<template
				v-if="['biz-current-ctx', 'biz-current-person', 'biz-current-dept',  'biz-current-org', 'biz-current-date-time'].includes(activeComponent.type )">
				<fb-form-item label="表格查询占位符(placeholder)" :label-width="160" prop="placeholder">
					<fb-input
						:value="col.props.placeholder"
						@input="(value)=>{updateCol(value, 'props', 'placeholder')}"
					/>
				</fb-form-item>

<!--				<fb-form-item label="占位符(placeholder)" :label-width="160" prop="placeholder">-->
<!--					<fb-input-->
<!--						:value="col.props.placeholder"-->
<!--						@input="(value)=>{updateCol(value, 'props', 'placeholder')}"-->
<!--					/>-->
<!--				</fb-form-item>-->

<!--				<fb-form-item label="占位符(placeholder)" :label-width="160" prop="placeholder">-->
<!--					<fb-input-->
<!--						:value="col.props.placeholder"-->
<!--						@input="(value)=>{updateCol(value, 'props', 'placeholder')}"-->
<!--					/>-->
<!--				</fb-form-item>-->

				<fb-form-item label="运行时配置" :label-width="160" prop="modifiable">
					<fb-tooltip placement="top" slot="label-extra">
						<fb-icon name="information" color="red"/>
						<fb-container slot="content">
							<p>选中的话，在修改时会被替换为运行时的操作对象值</p>
						</fb-container>
					</fb-tooltip>
					<fb-checkbox
						:value="col.props.modifiable"
						@input="(value)=>{updateCol(value, 'props', 'modifiable')}"
						label="可修改"
					/>
				</fb-form-item>

			</template>
			<template v-if="activeComponent.type === 'biz-current-ctx'">
				<!--				<fb-icon name="identity-card"/>-->


				<fb-form-item label="上下文表达式(expression)" :label-width="160" prop="expression">

					<fb-tooltip placement="top" slot="label-extra">
						<fb-icon name="information" color="red"/>
						<fb-container slot="content">
							<p>这个表达式计算器，可以返回当前上下文中的变量。</p>
							<p>例如：return ctx.sexName 这个语句，会返回当前登录用户的性别。</p>
							<p>你可以把它理解成一个函数，像这样 function (app, ctx){ 你的代码 }，
								其中app是当前的Application实例，ctx是上下文对象，内部的this指向当前表单。</p>
							<p>如果你不知道这个是什么，请慎重使用，如果你对这个配置项非常熟悉，请就尽情使用。</p>
						</fb-container>
					</fb-tooltip>
					<fb-container
						border="1px solid #D3D3D3"
						radius="4px"
						mt="4px"
						padding="8px">
						<fb-container background="#eee" radius="4px 4px 0 0"
						              padding="8px 0 0 8px">
							function (app, ctx) {
						</fb-container>
						<fb-container background="#eee" padding="8px">
							<fb-textarea
								rows="5"
								:value="col.props.expression"
								@input="( value, $event)=>{
									updateCol(value, 'props', 'expression')
								}"
							>
								<!--						<fb-button slot="append-button" type="primary"-->
								<!--							-->
								<!--						>应用</fb-button>-->
							</fb-textarea>
						</fb-container>
						<fb-container background="#eee" radius="0 0 4px 4px"
						              padding="0 0 8px 8px">
							}
						</fb-container>


						<pre>可用的上下文变量如下
personId        用户ID
personName      用户姓名
userName        帐号名称
sexName         用户性别
idcard          用户身份证
phone           用户联系方式
email           用户电子邮件
deptId          部门ID
deptFullName    部门名称
cityName    	所属行政区划
</pre>
					</fb-container>
				</fb-form-item>

			</template>


			<template v-if="activeComponent.type === 'biz-current-date-time'">
				<fb-form-item label="格式化操作符" :label-width="160" prop="format">


					<fb-container
						border="1px solid #D3D3D3"
						radius="4px"
						mt="4px"
						padding="8px"
					>
						<fb-input
							:value="col.props.format"
							@input="(value)=>{
							updateCol(value, 'props', 'format')
						}"
						>
							<!--						<fb-button slot="append-button" type="primary"-->
							<!--							-->
							<!--						>应用</fb-button>-->
						</fb-input>
						<!--						快速选择：-->
						<!--						<fb-tags :closable="false" type="primary" effect="dark" :data="[-->
						<!--							{label: 'yyyy-MM-DD', value: 'yyyy-MM-DD'},-->
						<!--						]"-->
						<!--						@on-click="(e,tag)=>{-->
						<!--							debugger-->
						<!--							 updateCol(tag.value, 'props', 'format')-->
						<!--						}"-->
						<!--						/>-->
						<fb-container
							border="1px solid #D3D3D3"
							radius="4px"
							padding="8px" mt="4px"
						>
						<pre>可用通配符

标识	示例	描述
YY	18	年，两位数
YYYY	2018	年，四位数
M	1-12	月，从1开始
MM	01-12	月，两位数字
MMM	Jan-Dec	月，英文缩写
D	1-31	日
DD	01-31	日，两位数
H	0-23	24小时
HH	00-23	24小时，两位数
h	1-12	12小时
hh	01-12	12小时，两位数
m	0-59	分钟
mm	00-59	分钟，两位数
s	0-59	秒
ss	00-59	秒，两位数
</pre>
						</fb-container>
					</fb-container>

				</fb-form-item>

			</template>


			<!--			<template v-if="activeComponent.type === 'dic-select'">-->

			<!--				<fb-fieldset label="控件属性"/>-->

			<!--				<fb-form-item label="快速配置业务字段" :label-width="160">-->
			<!--					<fb-select-->
			<!--						:data="[-->
			<!--							{label: '字典树', value: 'biz-dic-select-tree'},-->
			<!--							{label: '字典下拉框', value: 'biz-dic-select'},-->
			<!--							{label: '政府机构树（完整）', value: 'biz-org-select-tree-full'},-->
			<!--							{label: '政府机构树（本级及下级）', value: 'biz-org-select-tree-ps'},-->
			<!--							{label: '企业部门树（完整）', value: 'biz-ent-select-tree-full'},-->
			<!--							{label: '企业部门树（本级及下级）', value: 'biz-ent-select-tree-ps'},-->
			<!--							{label: '行政区划（完整）', value: 'biz-city-select-tree-full'},-->
			<!--							{label: '行政区划（本级及下级）', value: 'biz-city-select-tree-ps'},-->
			<!--							{label: '行政区划（两级）', value: 'biz-city-select-tree-two-level'},-->
			<!--							{label: '是否启用', value: 'enabled'},-->
			<!--							{label: '是否激活', value: 'active'},-->
			<!--							{label: '性别', value: 'sex'},-->
			<!--							{label: '星期选择', value: 'week'},-->
			<!--						]"-->
			<!--						:value="col.props.bizType"-->
			<!--						@input="(value)=>{-->
			<!--							updateBizType(value, 'props', 'bizType')-->
			<!--						}"/>-->
			<!--				</fb-form-item>-->

			<!--				<fb-form-item label="默认值(value)" :label-width="160" prop="defaultValue">-->
			<!--					<fb-input-->
			<!--						:value="col.props.defaultValue"-->
			<!--						@input="(value)=>{updateCol(value, 'props', 'defaultValue')}"/>-->
			<!--				</fb-form-item>-->

			<!--				<fb-form-item label="占位字符(placeholder)" prop="placeholder">-->
			<!--					<fb-input-->
			<!--						:value="col.props.placeholder"-->
			<!--						@input="(value)=>{updateCol(value, 'props', 'placeholder')}"/>-->
			<!--				</fb-form-item>-->

			<!--				<fb-form-item prop="multiple"-->
			<!--				>-->
			<!--					<fb-checkbox-->
			<!--						label="多选模式"-->
			<!--						:value="col.props.multiple"-->
			<!--						@input="(value)=>{updateCol(value, 'props', 'multiple')}"/>-->

			<!--				</fb-form-item>-->

			<!--				<fb-form-item label="静态数据(data)" prop="data">-->
			<!--					<fb-form-editor-object-editor-->
			<!--						:col="col"-->
			<!--						view-type="add"-->
			<!--					/>-->
			<!--				</fb-form-item>-->

			<!--				<fb-form-item label="接口服务(service)" prop="service">-->
			<!--					<fb-tree-select-->
			<!--						:value="col.props.service"-->
			<!--						@input="(value)=>{updateCol(value, 'props', 'service')}"-->
			<!--						:data="formEditor.editor.services"-->
			<!--						:header-format="(node)=>{-->
			<!--								return node && node.value-->
			<!--							}"-->
			<!--					/>-->
			<!--				</fb-form-item>-->

			<!--				<fb-form-item label="查询参数(param)" prop="param">-->
			<!--					<fb-container slot="label-extra">-->
			<!--						<fb-button size="s" @on-click="showDictDialog">查询字典</fb-button>-->
			<!--					</fb-container>-->
			<!--					<fb-form-editor-object-editor-->
			<!--						:col="col"-->
			<!--						view-type="add"-->
			<!--						:reader="{-->
			<!--						 	data: 'param',-->
			<!--						 }"-->
			<!--					/>-->
			<!--				</fb-form-item>-->
			<!--				<fb-form-item label="数据适配器(reader)" prop="reader">-->
			<!--					<fb-form-editor-object-prop-->
			<!--						:value="col.props.reader"-->
			<!--						@input="(value)=>{updateCol(value, 'props', 'reader')}"-->
			<!--						:remove-button="false"-->
			<!--						:add-button="false"-->
			<!--					/>-->
			<!--				</fb-form-item>-->


			<!--				<fb-form-item label="勾选影响范围" :label-width="160" prop="doCheck">-->
			<!--					<fb-radio-group radio-space="12"-->
			<!--						button-->
			<!--						:data="[-->
			<!--							{label:'父子', value: 'ps'},-->
			<!--							{label:'父', value: 'p'},-->
			<!--							{label:'子', value: 's'},-->
			<!--							{label:'无', value: ''},-->
			<!--						]"-->
			<!--						:value="col.props.doCheck"-->
			<!--						@input="(value)=>{updateCol(value, 'props', 'doCheck')}"-->
			<!--					/>-->
			<!--				</fb-form-item>-->

			<!--				<fb-form-item label="取消勾选影响范围" :label-width="160" prop="doUnCheck">-->
			<!--					<fb-radio-group radio-space="12"-->
			<!--						button-->
			<!--						:data="[-->
			<!--							{label:'父子', value: 'ps'},-->
			<!--							{label:'父', value: 'p'},-->
			<!--							{label:'子', value: 's'},-->
			<!--							{label:'无', value: ''},-->
			<!--						]"-->
			<!--						:value="col.props.doUnCheck"-->
			<!--						@input="(value)=>{updateCol(value, 'props', 'doUnCheck')}"-->
			<!--					/>-->
			<!--				</fb-form-item>-->

			<!--				<fb-form-item :label-width="160" prop="onlyLeaf">-->
			<!--					<fb-checkbox-->
			<!--						:value="col.props.onlyLeaf"-->
			<!--						@input="(value)=>{updateCol(value, 'props', 'onlyLeaf')}"-->
			<!--						label="只能选择叶子节点"-->
			<!--					/>-->
			<!--				</fb-form-item>-->

			<!--				<fb-form-item :label-width="160" prop="disabled">-->
			<!--					<fb-checkbox-->

			<!--						:value="col.props.disabled"-->
			<!--						@input="(value)=>{updateCol(value, 'props', 'disabled')}"-->
			<!--						label="禁用"-->
			<!--					/>-->
			<!--				</fb-form-item>-->

			<!--				<fb-form-item :label-width="160" prop="readonly">-->
			<!--					<fb-checkbox-->

			<!--						:value="col.props.readonly"-->
			<!--						@input="(value)=>{updateCol(value, 'props', 'readonly')}"-->
			<!--						label="只读"-->
			<!--					/>-->
			<!--				</fb-form-item>-->

			<!--				<fb-form-item :label-width="160" prop="clearable">-->
			<!--					<fb-checkbox-->
			<!--						:value="col.props.clearable"-->
			<!--						@input="(value)=>{updateCol(value, 'props', 'clearable')}"-->
			<!--						label="可清除"-->
			<!--					/>-->
			<!--				</fb-form-item>-->


			<!--				<fb-form-item :label-width="160" prop="filterable">-->
			<!--					<fb-checkbox-->

			<!--						:value="col.props.filterable"-->
			<!--						@input="(value)=>{updateCol(value, 'props', 'filterable')}"-->
			<!--						label="显示搜索框"-->
			<!--					/>-->
			<!--				</fb-form-item>-->


			<!--			</template>-->


		</fb-form>

		<dict-dialog ref="dict-dialog"></dict-dialog>

	</fb-card>
</template>

<script>
import { closest } from '../../../../../util/componentUtils'
import { each, extend, isNumber, isSafeInteger, toInteger, toNumber } from 'lodash-es'
import FbFormEditorObjectEditor from '../../../../../views/sys/custom/form/form-editor/FbFormEditorObjectEditor'
import FbFormEditorObjectProp from '../../../../../views/sys/custom/form/form-editor/FbFormEditorObjectProp'
import { bizTypes } from '../../../../../views/sys/custom/form/form-editor/utils/bizTypes'
import DictDialog from '../../../../../views/sys/custom/form/form-editor/utils/dialog/DictDialog'

/**
 * FbFormEditorComponentPropsPanel
 * (c) 2021 lincong1987
 */

export default {
	name: 'FbFormEditorComponentPropsPanel',
	components: {
		DictDialog,
		FbFormEditorObjectProp,
		FbFormEditorObjectEditor,
	},
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

	methods: {

		toInteger,

		toNumber,

		isNumber,

		checkNumber (value) {
			let rs = this.isNumber(this.toNumber(value))
			if (!rs) {
				this.$message.error('请输入数字')
			}
			return rs
		},
		checkInteger (value) {
			let rs = isSafeInteger(this.toInteger(value))
			if (!rs) {
				this.$message.error('请输入整数')
			}
			return rs
		},

		updateProps (value, props, prop) {
			let arr = Array.prototype.slice.call(arguments)
			this.formEditor.updatePathProps.apply(this, ['add'].concat(arr))
		},

		propSplice (prop, propName, index, length) {

			let {
				viewType,
			} = this

			this.formEditor.propSplice(viewType, prop, propName, index, length)

		},

		propPush (prop, propName, value) {
			let {
				viewType,
				activeComponent,
			} = this

			this.formEditor.propPush(viewType, prop, propName, value)
		},

		updateCol (value, prop, propName) {

			let {
				viewType,
				activeComponent,
			} = this

			this.formEditor.updateCol(viewType, value, prop, propName)

		},

		updateIntegerCol (value, prop, propName) {

			let {
				viewType,
				activeComponent,
			} = this

			if (this.checkInteger(value)) {
				this.formEditor.updateCol(viewType, toInteger(value), prop, propName)
			}

		},
		updateNumberCol (value, prop, propName) {

			let {
				viewType,
				activeComponent,
			} = this

			if (this.checkNumber(value)) {
				this.formEditor.updateCol(viewType, toNumber(value), prop, propName)
			}

		},

		updateBizType (value, prop, propName) {
			this.updateCol(value, prop, propName)

//			{label: '字典树', value: 'biz-dic-select-tree'},
//			{label: '字典下拉框', value: 'biz-dic-select'},
//			{label: '政府机构树（完整）', value: 'biz-org-select-tree-full'},
//			{label: '政府机构树（本级及下级）', value: 'biz-org-select-tree-ps'},
//			{label: '企业部门树（完整）', value: 'biz-ent-select-tree-full'},
//			{label: '企业部门树（本级及下级）', value: 'biz-ent-select-tree-ps'},
//			{label: '行政区划（完整）', value: 'biz-city-select-tree-full'},
//			{label: '行政区划（本级及下级）', value: 'biz-city-select-tree-ps'},
//			{label: '行政区划（两级）', value: 'biz-city-select-tree-two-level'},

			if (bizTypes[value]) {
				each(bizTypes[value], (v, k) => {
					this.updateCol(v, 'props', k)
				})
			}
//
//			if (value === 'biz-dic-select-tree') {
//
//				this.updateCol('sys.dict.tree', 'props', 'service')
//				this.updateCol([
//					{
//						label: 'dicCode',
//						value: 'E10',
//					},
//				], 'props', 'param')
//				this.updateCol([
//					{
//						'label': 'label',
//						'value': 'text',
//					},
//					{
//						'label': 'value',
//						'value': 'id',
//					},
//				], 'props', 'reader')
//			}

		},

		getComponentByType (type) {
			return this.formEditor.getComponentByType(type)
		},

		getComponentNameByType (type) {
			return this.formEditor.getComponentNameByType(type)
		},

		defaultValue (value, defaultVal) {
			if (typeof value === 'undefined') {
				return defaultVal
			} else {
				return value
			}
		},

		showDictDialog () {
			this.$refs['dict-dialog'].show()
		},

	},
}
</script>

<style lang="less" scoped>

</style>
