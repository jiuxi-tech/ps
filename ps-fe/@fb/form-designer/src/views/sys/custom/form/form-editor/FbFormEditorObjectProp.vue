<template>
	<fb-container mt="4px" class="fb-form-editor-object-prop">


		<fb-simple-table
			bordered
			:wrapper-style="{
                borderRadius: addButton ? '4px 4px 0 0' :'',
                borderBottom: addButton ? 0 : ''
            }"
			size="s"
			:rownum="false"
			:no-pager="true"
			:no-empty="true"
			:columns="columns"
			:data="myData"
			:auto-pk="false"
			pk="rownum"
		>

						<template v-slot:label="props">
							<template v-if="friendlyProps[props.value]">
								{{ friendlyProps[props.value] }}
							</template>
							<template v-else>

								<fb-input
									size="s"
									:value="myData[props.rowIndex][myReader.label]"
									@input="(value)=>{
												myData[props.rowIndex][myReader.label] = value
												$nextTick(()=>{
													$emit('input', myData)
												})
											}"
								></fb-input>
							</template>
						</template>

						<template v-slot:value="props">

							<template v-if="selects[props.row[myReader.label]]">

								<fb-select
									size="s"
									:data="selects[props.row[myReader.label]]"
									:readonly="readonlyProps.includes(myReader.label)"
									:value="myData[props.rowIndex][myReader.value]"
									@input="(value)=>{
												myData[props.rowIndex][myReader.value] = value
												$nextTick(()=>{
													$emit('input', myData)
												})
											}"></fb-select>

							</template>


							<template v-else-if="isBoolean(myData[props.rowIndex][myReader.value])">
								<fb-checkbox
									size="s"
									:value="myData[props.rowIndex][myReader.value]"
									@input="(value)=>{
												myData[props.rowIndex][myReader.value] = value
												$nextTick(()=>{
													$emit('input', myData)
												})
											}"></fb-checkbox>
							</template>


							<template v-else>
								<fb-input
									size="s"
									:readonly="readonlyProps.includes(myReader.label)"
									:value="myData[props.rowIndex][myReader.value]"
									@input="(value)=>{
												myData[props.rowIndex][myReader.value] = value
												$nextTick(()=>{
													$emit('input', myData)
												})
											}"></fb-input>
							</template>


						</template>

			<template v-slot:op="props">
				<fb-icon
					name="delete"
					@on-click="remove(props.rowIndex)"
				/>
			</template>

		</fb-simple-table>

		<fb-container v-if="addButton" padding="6px 8px" style="border: 1px solid #D3D3D3; "
		              radius="0 0 4px 4px">

			<fb-button size="s" icon="add-circle"
			           @on-click="propPush">
				添加
			</fb-button>
		</fb-container>
	</fb-container>
</template>

<script>
import {isBoolean, isString, merge} from 'lodash-es'

/**
 * FbFormEditorObjectProp
 * (c) 2021 lincong1987
 */

export default {
	name: 'FbFormEditorObjectProp',
	props: {
		value: {
			type: [Object, Array],
		},

		reader: {
			type: [Object],
			default () {
				return {}
			},
		},

		showLabel: {
			type: [Boolean],
			default: true,
		},

		showValue: {
			type: [Boolean],
			default: true,
		},

		addButton: {
			type: [Boolean],
			default: true,
		},

		removeButton: {
			type: [Boolean],
			default: true,
		},

		readonlyProps: {
			type: [Array],
			default () {
				return []
			},
		},
		friendlyProps: {
			type: [Object],
			default () {
				return {}
			},
		},

		selects: {
			type: [Object],
			default () {
				return {}
			},
		},

	},

	data () {

		let myData = this.mergeData(this.value)
		let myReader = this.mergeReader(this.reader)

		let columns = []

		if (this.showLabel) {
			columns.push({
				label: myReader.labelName,
				name: myReader.label,
				slot: 'label',
				ellipsis: false,
			})
		}
		if (this.showValue) {

			columns.push({
				label: myReader.valueName,
				name: myReader.value,
				slot: 'value',
				ellipsis: false,
			})

		}

		if (this.removeButton) {
			columns.push({
				name: '-',
				slot: 'op',
				width: 30,
			})
		}

		return {
			myData,
			myReader,
			columns,
		}
	},

	watch: {
		value (value) {
			this.myData = this.mergeData(value)
		},
		reader (value) {
			this.myReader = this.mergeReader(value)
		},
	},

	methods: {

		isBoolean,

		isString,

		mergeData (value) {

			return value
//
//			if (!obj) {
//				return []
//			}
//
//			return keys(obj).map((key, i) => {
//
//				return {
//					label: key,
//					value: obj[key],
//				}
//
//			})

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

		propPush (e) {
			let {
				myReader,
			} = this
			let obj = {}
			obj[myReader.label] = '参数'
			obj[myReader.value] = '值'

			//this.myData.push(obj)
			this.$emit('input', this.myData.concat(obj))
		},

		remove (index) {

			let data = JSON.parse(JSON.stringify(this.myData))

			data.splice(index, 1)

			this.$emit('input', data)

		},

	},
}
</script>

<style lang="less" scoped>

</style>
