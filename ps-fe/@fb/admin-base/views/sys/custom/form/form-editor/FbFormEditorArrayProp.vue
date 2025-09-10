<template>
	<fb-container mt="4px">
		<fb-simple-table
			:auto-pk="false"
			pk="rownum"
			size="s"
			:rownum="false"
			:no-pager="true"
			:no-empty="true"
			:columns="[
				 {
					 label:myReader.labelName,
					 name: myReader.label,
					 slot: 'label'
				 },
				 {
					 label:myReader.valueName,
					 name: myReader.value,
					 slot: 'value'
				 },
				 {
					 name: '-',
					 slot: 'op',
					 width: 30
				 },
			 ]"

			:data="myData"

		>

			<template v-slot:label="props">
				<fb-input
					size="s"
					:value="myData[props.rowIndex][myReader.label]"
					@input="(value)=>{
						myData[props.rowIndex][myReader.label] = value
						$nextTick(()=>{
							$emit('input', myData)
						})
					}"></fb-input>

			</template>
			<template v-slot:value="props">
				<fb-input
					size="s"
					:value="myData[props.rowIndex][myReader.value]"
					@input="(value)=>{
						myData[props.rowIndex][myReader.label] = value
						$nextTick(()=>{
							$emit('input', myData)
						})
					}"></fb-input>
			</template>
			<template v-slot:op="props">
				<fb-icon
					name="delete"
					@on-click="myData.splice(index, 1)"
				/>
			</template>

		</fb-simple-table>

		<fb-container mt="4px">
			<fb-button size="s" icon="add-circle"
					   @on-click="propPush">
				添加
			</fb-button>
		</fb-container>
	</fb-container>
</template>

<script>
import { closest } from '../../../../../util/componentUtils'
import { merge } from 'lodash-es'

/**
 * FbFormEditorArrayProp
 * (c) 2021 lincong1987
 */

export default {
	name: 'FbFormEditorArrayProp',
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
	},

	data () {
		return {
			myData: this.mergeData(this.value),
			myReader: this.mergeReader(this.reader),
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

		mergeData (data) {
			return data
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

	},
}
</script>

<style lang="less" scoped>

</style>
