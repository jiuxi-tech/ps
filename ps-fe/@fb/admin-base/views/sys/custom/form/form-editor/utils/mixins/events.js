/*!
 * events
 * (c) 2021 lincong1987
 */

let path = '/sys/custom/form/form-editor/utils/dialog'

export default {

	methods: {

		/**
		 * 企业人员单选
		 */
		handleEntPersonSingleSelect ({
			                             success = () => {
			                             },
			                             form = this,
		                             }) {

			let param = {}
			let options = {
				callBack: function (result) {
					console.log('=============回调' + result)
					success && success(result)
				},
			}
			form && form.$refs.TpDialog.show(
				`${path}/ent/EntPersonSingleSelect.vue`,
				param,
				'企业人员单选',
				options)
		},

		/**
		 * 企业人员多选
		 */
		handleEntPersonMultipleSelect ({
			                               success = () => {
			                               },
			                               form = this,
		                               }) {
			let param = {}
			let options = {
				callBack: function (result) {
					console.log('=============回调' + result)
					success && success(result)
				},
			}
			form && form.$refs.TpDialog.show(
				`${path}/ent/EntPersonMultipleSelect.vue`,
				param,
				'企业人员多选',
				options)
		},

		/**
		 * 企业人员单选（列表）
		 */
		handleEntPersonSingleSelectUsingTableShow ({
			                                           success = () => {
			                                           },
			                                           form = this,
		                                           }) {
			let param = {}
			let options = {
				callBack: function (result) {
					console.log('=============回调' + result)
					success && success(result)
				},
			}
			form && form.$refs.TpDialog.show(
				`${path}/ent/EntPersonSingleSelectUsingTableShow.vue`
				, param, '企业人员单选（列表）',
				options)

		},

		/**
		 * 企业人员多选（列表）
		 */
		handleEntPersonMultipleSelectUsingTableShow ({
			                                             success = () => {
			                                             },
			                                             form = this,
		                                             }) {
			let param = {}
			let options = {
				callBack: function (result) {
					console.log('=============回调' + result)
					success && success(result)
				},
			}
			form && form.$refs.TpDialog.show(
				`${path}/ent/EntPersonMultipleSelectUsingTableShow.vue`
				, param, '企业人员多选（列表）',
				options)

		},

		/**
		 * 政府人员单选
		 */
		handleOrgPersonSingleSelect ({
			                             success = () => {
			                             },
			                             form = this,
		                             }) {
			let param = {}
			let options = {
				callBack: function (result) {
					console.log('=============handleEntSelect回调' + result)
					success(result)
				},
			}

			form && form.$refs.TpDialog.show(
				`${path}/org/OrgPersonSingleSelect.vue`
				, param, '政府人员单选',
				options)
		},

		/**
		 *  政府人员多选
		 */
		handleOrgPersonMultipleSelect ({
			                               success = () => {
			                               },
			                               form = this,
			                               initData = [],
		                               }) {
			let param = {
				persons: initData,
			}

			let options = {
				callBack: function (result) {
					console.log('=============回调' + result)

					success && success(result)


//						[
	//						{
	//							deptId: '1364853202066866176',
	//							personId: '1366575600608739328',
	//							personName: 'ge先生',
	//						},
//						]

//					{
//						"label": "张卫强",
//						"value": "1456154554486751232",
//						"deptId": "DEPT000000000900000",
//						"deptFullName": "余杭区安全生产委员会",
//						"type": "primary",
//						"closable": true
//					}


				},
			}

			form && form.$refs.TpDialog.show(
				`${path}/org/OrgPersonMultipleSelect.vue`
				, param, '政府人员多选',
				options)
		},

		/**
		 *  政府企业单选
		 */
		handleOrgEntSingleSelect ({
			                          success = () => {
			                          },
			                          form = this,
		                          }) {
			let param = {}
			let options = {
				callBack: function (result) {
					console.log('=============handleEntSelect回调' + result)
					success && success(result)
				},
			}

			form && form.$refs.TpDialog.show(
				`${path}/org/OrgEntSingleSelect.vue`
				, param, '政府企业单选',
				options)
		},

		/**
		 *  政府企业多选
		 */
		handleOrgEntMultipleSelect ({
			                            success = () => {
			                            },
			                            form = this,
			                            initData = [],
		                            }) {
			let param = {
				'ents': initData

//					[
////					{
////						'entId': '1366270340598071296',
////						'entFullName': '测试',
////					},
//				],
			}
			let options = {
				callBack: function (result) {
					console.log('=============handleEntSelect回调' + result)
					success && success(result)
				},
			}

			form && form.$refs.TpDialog.show(
				`${path}/org/OrgEntMultipleSelect.vue`,
				param,
				'政府企业多选',
				options)
		},

	},

}
