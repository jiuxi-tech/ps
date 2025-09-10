/*!
 * search
 * (c) 2021 lincong1987
 */
import props from '../props'

export default function (services) {

	return [
		props['fb-fieldset'],
		props['fb-text'],
		props['fb-input'],
		props['fb-input-number'],
		props['fb-textarea'],
		{
			services,
			...props['fb-select'],
		},
		{
			services,
			...props['fb-tree-select'],
		},
		{
			services,
			...props['fb-radio-group'],
		},
		{
			services,
			...props['fb-checkbox-group'],
		},
		props['fb-checkbox'],
		props['fb-color-picker'],
		props['tp-datepicker'],
		props['tp-upload'],
		props['fb-editor'],
		props['biz-ent-person-single-select'],
		props['biz-ent-person-multiple-select'],
		props['biz-ent-person-single-select-using-table-show'],
		props['biz-ent-person-multiple-select-using-table-show'],
		props['biz-org-person-single-select'],
		props['biz-org-person-multiple-select'],
		props['biz-org-ent-single-select'],
		props['biz-org-ent-multiple-select'],


		props['biz-current-ctx'],
		props['biz-current-person'],
		props['biz-current-dept'],
		props['biz-current-org'],
		props['biz-current-date-time'],
//		props['biz-current-date'],
//		props['biz-current-time'],
	]

}
