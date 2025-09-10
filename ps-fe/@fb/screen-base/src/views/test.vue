<template>
	<div style="padding: 20px">
		<fb-radio-group v-model="radioValue" button :data="[
			{label: '父子传参', value: 1},
			{label: '动态编辑表格', value: 2},
			{label: '动态复杂表格', value: 3},
		]"></fb-radio-group>
		<fb-container mt="20px"></fb-container>

		<fb-card v-show="radioValue === 1" header="父子传参">
			<fb-datepicker enableTime></fb-datepicker>
			<Parent></Parent>
		</fb-card>
		<fb-card v-show="radioValue === 2" header="动态编辑表格">
			<!--			<pre>{{ data }}</pre>-->

			<fb-form ref="form">
				<fb-container mb="10px">
					<fb-button @on-click="addTableRow">加一行</fb-button>
				</fb-container>
				<fb-simple-table :columns="table.columns"
								 pk="userId"
								 :data="table.data"
								 :multiple="false">
					<template #userName="props">
						<fb-input v-model="table.data[props.rowIndex].userName"></fb-input>
					</template>
					<template #userTel="props">
						<fb-input v-model="table.data[props.rowIndex].userTel"></fb-input>
					</template>
					<template #userImg="props">
						<fb-upload
							:service="xxService"
							:file-list="table.data[props.rowIndex].fileList"
							multiple
						>
						</fb-upload>
					</template>
				</fb-simple-table>
			</fb-form>

		</fb-card>





		<fb-card v-show="radioValue === 3" header="动态复杂表格">
			<fb-container mb="10px">
				<fb-button @on-click="add">添加</fb-button>
<!--				<fb-button @on-click="huixian">huixian</fb-button>-->
				<fb-button @on-click="resetTable">{{ !isReadonly ? '查看' : '编辑' }}</fb-button>
			</fb-container>

			<fb-form ref="form">
				<fb-simple-table
					:columns="[{
						label:'复杂单元格',
						name: 'cs',
						slot: 'cs',
						ellipsis:false
					}, {
						label:'是否校验',
						name: 'shifou' ,
						slot:'shifou',
						width: 100 ,
						ellipsis:false
					}]"
					pk="id"
					:data="data"
				>
					<template #cs="props">
						<fb-container display="flex" style="align-items: center">
							<template v-for="(comp, i) in getTempAndComp(props.value, props.row.comps)">
								<template v-if=" comp.type === 'string'">
									<fb-container inline>{{ comp.text }}</fb-container>
								</template>

								<template v-if="!isReadonly">

									<template v-if=" comp.type === 'biz-unit'">
										<fb-container inline>
											<fb-form-item style="padding: 0" :prop="`${comp.type}-${props.rowIndex}-${comp.index}`"
														  :rule="[{required: data[props.rowIndex].shifou}, ...comp.rule]"
											>
												<fb-select style="width: 100px"
													v-model="data[props.rowIndex].comps[comp.index].value"
													:data="comp.data"
												></fb-select>
											</fb-form-item>
										</fb-container>
									</template>
									<template v-if=" comp.type === 'biz-integer'">
										<fb-container inline>
											<fb-form-item style="padding: 0" :prop="`${comp.type}-${props.rowIndex}-${comp.index}`"
														  :rule="[{required: data[props.rowIndex].shifou}, ...comp.rule]"
											>
												<fb-input-number
													v-model="data[props.rowIndex].comps[comp.index].value"
												></fb-input-number>
											</fb-form-item>
										</fb-container>
									</template>
								</template>
								<template v-else>
<!--									<fb-link type="primary" v-if="comp.type === 'biz-integer'" :label="comp.value"></fb-link>-->
									<fb-container inline>{{ comp.value }}</fb-container>
								</template>

							</template>
						</fb-container>
					</template>


					<template #shifou="props">
						<fb-radio-group v-model="data[props.rowIndex].shifou"
										:data="[{label: '是', value: true}, {label: '否', value: false}]"></fb-radio-group>
					</template>
				</fb-simple-table>
			</fb-form>

		</fb-card>

	</div>


</template>

<script>
import Parent from "@/components/test/Parent";

export default {
	components: {
		Parent
	},
	data () {

		return {
			radioValue: 1,

			data: [],
			isReadonly: false,

			table: {
				columns: [
					{name: 'userId',label: 'id',hidden: true,width: 1,	disabled: true,},
					{name: 'userName',label: '姓名',slot: 'userName', sortable: false,align: 'left'},
					{name: 'userTel',label: '电话' ,slot: 'userTel', sortable: false,align: 'left'},
					{name: 'userImg',label: '附件' ,slot: 'userImg', sortable: false,align: 'left'},
				],
				data: []
			},
			xxService: {
				download (file) {
					return `http://192.168.90.77:8080/jpx-admin/sys/file/download?id=${file.attachId}`
				},
				upload (file) {
					return new Promise((resolve, reject) => {
						setTimeout(() => {
							resolve({
								'code': 1,
								'data': {
									'attachId': '1356578926939340800',
									'attachName': 'a80f8b014a90f603912c67353b12b31bb151edb6.jpg',
									'attachSize': 32057.0,
									'createTime': '20210202202314',
									'referId': '',
									'referType': '',
									'savePath': '2021/2/2021_02_02_20_23_14856.jpg',
								},
								'message': '成功',
							})
						}, 1000)
					})
				},
			},
		}

	},

	methods: {
		addTableRow() {

			this.table.data.push({
				userId: new Date().getTime(),
				userName: '',
				userTel: '',
				fileList: []
			})
		},
		getTempAndComp (content, comps) {
			// 现场设置警示标志，现场配备消防水带（0）根，灭火器（1）具，水管是否接好(是)
			let arr = []

			content.split(/（\d）/).forEach((n, i) => {
				arr.push({
					type: 'string',
					text: n,
				})
				comps[i] && arr.push(comps[i])
			})
			return arr

		},

		add () {
			this.data.push({
				id: new Date().getTime(),
				cs: '现场设置警示标志，现场配备灭火器（0）（1）',

				comps: [
					{
						id: '444',
						type: 'biz-integer',
						rule: [{type: 'integer'}],
						index: 0,
						value: 100,
					},

					{
						id: '333',
						type: 'biz-unit',
						rule: [],
						data: [
							{
								value: '个',
								label: '个',
							},
							{
								value: '套',
								label: '套',
							},
							{
								value: 'xxx',
								label: 'xxx',
							},
						],
						index: 1,
						value: '',
					},
				],
				shifou: false,
			})
		},

		resetTable() {
			this.$refs['form'].validate((result, error) => {
				if (result) {
					this.isReadonly = !this.isReadonly
				}
			})
		},

		jQ() {
			let row = ''
			let rowHtml = `
				<tr>
					<td>1</td>
					<td data-type="edit">现场设置警示标志，现场配备灭火器（0）（1）</td>
					<td>

						<span class="edit-radio">
							<input type="radio" >
							<lable>是</lable>
							<input type="radio" >
							<lable>否</lable>
						</span>
					</td>
				</tr>
			`
			$('tbody').append(rowHtml)

			let editTd = `
				<span>
					现场设置警示标志，现场配备灭火器
					<input type="number">
					<select>
						<option value="个">个</option>
						<option value="套">套</option>
					</select>
				</span>
			`

			$('tbody').find('td[data-type="edit"]').html(editTd)

			$('.edit-radio').click(function () {
				$(this).parent().parent().find('td[data-type="edit"]').find('输入框校验').dhsidfjoisdf
			})

			$('.watch').click(function () {
				$(this).find('各种复杂查找替换')
			})
		},


		huixian () {
			setTimeout(() => {
				let data = [
					{
						id: 1,
						cs: '现场设置警示标志，现场配备灭火器（0）（1），水管是否接好(是)',

						comps: [
							{
								id: '444',
								type: 'biz-integer',
								rule: [{type: 'integer'}],
								index: 0,
								value: 100,
							},

							{
								id: '333',
								type: 'biz-unit',
								rule: [],
								data: [
									{
										value: '个',
										label: '个',
									},
									{
										value: '具',
										label: '具',
									},
								],
								index: 1,
								value: '',
							},
						],

						values: [0, ''],

						shifou: 1,
					},
					{
						id: 2,
						cs: '',
						comps: [],
						values: [],
						shifou: 0,
					},
				]

				this.data = data
			}, 2000)

		},

	},
	mounted () {

	},

}
</script>

<style scoped lang="less">
</style>
