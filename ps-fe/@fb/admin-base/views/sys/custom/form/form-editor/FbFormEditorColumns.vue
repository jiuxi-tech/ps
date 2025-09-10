<template>
	<fb-container>

		<fb-button @on-click="$refs.drawer.show()" type="primary" icon="list">配置</fb-button>

		<fb-drawer ref="drawer" position="bottom" height="500px" lock mt="4px">


			<fb-simple-table
				size="s"
				:rownum="false"
				:no-pager="true"
				:no-empty="true"
				:columns="myColumns"
				:data="myData"
				:scroll="{y: 460}"
				:auto-pk="false"
				pk="rownum"
			>

				<template v-slot:show="props">
					<fb-checkbox size="s" :value="props.row.show"
					             @input="(value)=>{
									myData[props.rowIndex].show = value
									updateRow()
					}"></fb-checkbox>
				</template>

				<template v-slot:order="props">
					<fb-table-layout>
						<fb-table-layout-cell align="center">
							<fb-button v-show="props.rowIndex !== 0" size="s" icon="up-arrow"
							           @on-click="()=>{moveRow('up', props.rowIndex)}"></fb-button>
						</fb-table-layout-cell>
						<fb-table-layout-cell align="center">
							<fb-button v-show="props.rowIndex !== (myData.length - 1)" size="s" icon="down-arrow"
							           @on-click="()=>{moveRow('down', props.rowIndex)}"></fb-button>
						</fb-table-layout-cell>
					</fb-table-layout>


				</template>


				<template v-slot:name="props">
					<fb-input size="s" :value="props.row.name"
					          :disabled="props.row.name === 'op'"
					          @input="(value)=>{
								myData[props.rowIndex].name = value
								updateRow()
					}"></fb-input>
				</template>

				<template v-slot:label="props">
					<fb-input size="s" :value="props.row.label"
					          @input="(value)=>{
							  	myData[props.rowIndex].label = value
								updateRow()
					}"></fb-input>
				</template>
				<template v-slot:width="props">
					<fb-input size="s" :value="props.row.width"
					          @input="(value)=>{
									if (checkInteger(value)) {
										if (typeof myData[props.rowIndex].width === 'undefined') {
											$set(myData[props.rowIndex], 'width', toInteger(value))
										}
										if (toInteger(value) > 0) {
											myData[props.rowIndex].width = toInteger(value)
										} else{
											myData[props.rowIndex].width = null
										}
										updateRow()
									}
								}">
						<template slot="append">px</template>
					</fb-input>
				</template>

				<template v-slot:align="props">
					<fb-radio-group
						button
						size="s"
						:value="props.row.align"
						:data="[{label:'左', value:'left'}, {label:'中', value:'center'}, {label:'右', value:'right'}, ]"
						@input="(value)=>{
						myData[props.rowIndex].align = value
						updateRow()
					}"
					></fb-radio-group>
				</template>

				<template v-slot:titleAlign="props">
					<fb-radio-group
						button
						size="s"
						:value="props.row.titleAlign"
						:data="[{label:'左', value:'left'}, {label:'中', value:'center'}, {label:'右', value:'right'}, ]"
						@input="(value)=>{
						myData[props.rowIndex].titleAlign = value
						updateRow()
					}"
					></fb-radio-group>
				</template>

				<template v-slot:ellipsis="props">
					<fb-checkbox size="s" :value="props.row.ellipsis"
					             @input="(value)=>{
									myData[props.rowIndex].ellipsis = value
									updateRow()
								}">
					</fb-checkbox>
				</template>

				<template v-slot:titleEllipsis="props">
					<fb-checkbox size="s" :value="props.row.titleEllipsis"
					             @input="(value)=>{
									myData[props.rowIndex].titleEllipsis = value
									updateRow()
								}">
					</fb-checkbox>
				</template>

				<template v-slot:view="props">
					<fb-checkbox size="s" :value="props.row.view"
					             @input="(value)=>{
									myData[props.rowIndex].view = value
									updateRow()
								}">
					</fb-checkbox>
				</template>


				<template v-slot:sort="props">


					<fb-checkbox size="s"
					             :value="props.row.sort"
					             @input="(value)=>{
														myData[props.rowIndex].sort = value
														updateRow()
													}">
					</fb-checkbox>
				</template>


				<!--				<template v-slot:op="props">-->
				<!--					<fb-icon-->
				<!--						name="delete"-->
				<!--						@on-click="myData.splice(index, 1)"-->
				<!--					/>-->
				<!--				</template>-->

			</fb-simple-table>

			<!--			<fb-container mt="4px">-->
			<!--				<fb-button size="s" icon="add-circle"-->
			<!--						   @on-click="propPush">-->
			<!--					添加-->
			<!--				</fb-button>-->
			<!--			</fb-container>-->
		</fb-drawer>
	</fb-container>
</template>

<script>
import { closest } from '../../../../../util/componentUtils'
import { isNumber, isSafeInteger, merge, toInteger, toNumber } from 'lodash-es'

/**
 * FbFormEditorColumns
 * (c) 2021 lincong1987
 */

export default {
	name: 'FbFormEditorColumns',
	props: {
		value: {
			type: [Object, Array],
		},
	},
	created () {
		this.formEditor = closest(this, 'FbFormEditor')
	},
	beforeDestroy () {
		this.formEditor = null
	},
	data () {

		return {
			myData: this.mergeData(this.value),
			myColumns: [
				{
					label: '显示',
					name: 'show',
					slot: 'show',
					width: 50,
				},
				{
					label: '顺序',
					name: 'order',
					slot: 'order',
					width: 80,
					ellipsis: false,
				},
				{
					label: '列名',
					name: 'label',
					slot: 'label',
				},
				{
					label: '字段名',
					name: 'name',
					slot: 'name',
				},
				{
					label: '列宽',
					name: 'width',
					slot: 'width',
					width: 100,
				},
				{
					label: '对齐',
					name: 'align',
					slot: 'align',
					ellipsis: false,
				},
				{
					label: '表头对齐',
					name: 'titleAlign',
					slot: 'titleAlign',
					ellipsis: false,
				},
				{
					label: '溢出',
					name: 'ellipsis',
					slot: 'ellipsis',
					width: 50,
					align: 'center',
				},
				{
					label: '表头溢出',
					name: 'titleEllipsis',
					slot: 'titleEllipsis',
					width: 80,
					align: 'center',
				},
				{
					label: '查看列',
					name: 'view',
					slot: 'view',
					width: 70,
					align: 'center',
				},
				{
					label: '排序',
					name: 'sort',
					slot: 'sort',
					ellipsis: false,
					width: 50,
					align: 'center',
				},
				{
					name: '-',
					slot: 'op',
					width: 30,
				},
			],
		}
	},

	watch: {
		value (value) {
			this.myData = this.mergeData(value)
		},
	},

	methods: {

		mergeData (value) {

//			let data = []
//
//			return []
//
//			value.forEach((n, i) => {
//
//				let column = merge({}, {
//					name: n.formItemProps.name || '',
//					label: n.formItemProps.label || '',
//					align: 'left',
//					titleAlign: 'left',
//					ellipsis: true,
//					titleEllipsis: true,
//				}, n.column)
//
//				data.push(column)
//			})

			return value
		},

		mergeReader (reader) {

			return merge({}, {
				label: 'label',
				labelName: '参数名',
				value: 'value',
				valueName: '值',
				data: 'data',
			}, reader)
		},

		propPush (prop, propName, value) {
			let {
				myReader,
			} = this
			let obj = {}
			obj[myReader.label] = ''
			obj[myReader.value] = ''
			this.myData.push(obj)
		},
		toInteger,

		checkInteger (value) {
			return isSafeInteger(this.toInteger(value))
		},

		updateRow () {
			this.$nextTick(() => {
				this.$emit('input', this.myData)
			})
		},

		moveRow (position, index) {

			let {
				myData,
			} = this

			let maxIndex = myData.length

			if (position === 'up') {
				if (index === 0) {
					this.$message.error('已经是！')
					return
				}
				let row = myData[index - 1]
				this.$set(myData, index - 1, myData[index])
				this.$nextTick(() => {
					this.$set(myData, index, row)
				})
			}

			if (position === 'down') {
				if (index + 1 >= maxIndex) {
					this.$message.error('最后一行不得下移！')
					return
				}
				let row = myData[index + 1]
				this.$set(myData, index + 1, myData[index])
				this.$nextTick(() => {
					this.$set(myData, index, row)
				})
			}

			this.updateRow()

		},
	},
}
</script>

<style lang="less" scoped>

</style>
