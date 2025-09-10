<template>
	<fb-form-item label="data" prop="data">
		<fb-simple-table
			:auto-pk="false"
			pk="rownum"
			size="s"
			:rownum="false"
			:no-pager="true"
			:no-empty="true"
			:columns="[
															 {
																 label:'label',
																 name: 'label',
																 slot: 'label'
															 },
															 {
																 label:'value',
																 name: 'value',
																 slot: 'value'
															 },
															 {
																 name: 'op',
																 slot: 'op',
																 width: 30
															 },
														 ]"
			:data="add.rows[activeComponent.rowIndex].cols[activeComponent.colIndex].props.data"
		>
			<template v-slot:label="props">
				<fb-input
					size="s"
					:value="col.props.label"
					@input="(value)=>{updateCol(value, 'props', 'label')}"></fb-input>

			</template>
			<template v-slot:value="props">
				<fb-input
					size="s"
					:value="col.props.value"
					@input="(value)=>{updateCol(value, 'props', 'value')}"></fb-input>

			</template>
			<template v-slot:op="props">
				<fb-icon
					name="delete"
					@on-click="propSplice('props', 'data', props.rowIndex, 1)"
				/>
			</template>

		</fb-simple-table>

		<fb-container mt="4px">
			<fb-button size="s" icon="add-circle"
					   @on-click="propPush('props', 'data', {  label: '',  value: ''  })">添加
			</fb-button>
		</fb-container>
	</fb-form-item>
</template>

<script>
/**
 * FbFormEditorDataEditor
 * (c) 2021 lincong1987
 */

export default {
	name: 'FbFormEditorDataEditor',
}
</script>

<style lang="less" scoped>

</style>
