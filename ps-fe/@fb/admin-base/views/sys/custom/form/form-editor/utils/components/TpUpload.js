/*!
 * fb-textarea
 * (c) 2022 lincong1987
 */

import searchTypes from '../searchTypes'

export default {

	label: '上传组件',
	value: 'tp-upload',
	icon: 'attachment',
	index: 1,



	rules: [
		{
			label: '必填',
			value: 'required',
		},
	],

	searchTypes,

	defaults: {

		param: [
			{
				label: 'referType',
				value: '',
			},
		],

		accept: '',
		view: 'list',
		multiple: false,
		quality: 0.7,
		maxWidth: 2000,
		maxHeight: 2000,
		readonly: false,
		avatarSize: 120,
		avatarCircle: true,
		showPreview: true,
		showDownload: true,
		showRemove: true,

	},
}
