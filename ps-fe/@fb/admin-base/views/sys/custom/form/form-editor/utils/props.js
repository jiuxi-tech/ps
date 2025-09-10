/*!
 * props
 * (c) 2021 lincong1987
 */

import FbInput from './components/FbInput'
import FbTextarea from './components/FbTextarea'
import FbSelect from './components/FbSelect'
import FbTreeSelect from './components/FbTreeSelect'
import FbCheckbox from './components/FbCheckbox'
import FbCheckboxGroup from './components/FbCheckboxGroup'
import FbRadioGroup from './components/FbRadioGroup'
import TpDatepicker from './components/TpDatepicker'
import TpUpload from './components/TpUpload'
import FbSimpleTable from './components/FbSimpleTable'
import BizEntPersonSingleSelect from './components/BizEntPersonSingleSelect'
import BizEntPersonMultipleSelect from './components/BizEntPersonMultipleSelect'
import BizEntPersonSingleSelectUsingTableShow
	from './components/BizEntPersonSingleSelectUsingTableShow'
import BizEntPersonMultipleSelectUsingTableShow
	from './components/BizEntPersonMultipleSelectUsingTableShow'
import BizOrgPersonSingleSelect from './components/BizOrgPersonSingleSelect'
import BizOrgPersonMultipleSelect from './components/BizOrgPersonMultipleSelect'
import BizOrgEntSingleSelect from './components/BizOrgEntSingleSelect'
import BizOrgEntMultipleSelect from './components/BizOrgEntMultipleSelect'
import FbEditor from './components/FbEditor'
import BizDicTreeSelect from './components/BizDicTreeSelect'
import FbInputNumber
	from './components/FbInputNumber'
import FbColorPicker
	from './components/FbColorPicker'
import FbText from './components/FbText'
import FbFieldset
	from './components/FbFieldset'
import BizCurrentPerson
	from './components/BizCurrentPerson'
import BizCurrentDept
	from './components/BizCurrentDept'
import BizCurrentOrg
	from './components/BizCurrentOrg'
import BizCurrentDateTime
	from './components/BizCurrentDateTime'
import BizCurrentTime
	from './components/BizCurrentTime'
import BizCurrentDate
	from './components/BizCurrentDate'
import BizCurrentCtx
	from './components/BizCurrentCtx'

export default {
	'fb-fieldset': FbFieldset,
	'fb-text': FbText,
	'fb-input': FbInput,
	'fb-textarea': FbTextarea,
	'fb-select': FbSelect,
	'fb-tree-select': FbTreeSelect,
	'fb-checkbox': FbCheckbox,
	'fb-checkbox-group': FbCheckboxGroup,
	'fb-radio-group': FbRadioGroup,
	'tp-datepicker': TpDatepicker,
	'tp-upload': TpUpload,
	'fb-editor': FbEditor,

	'fb-input-number': FbInputNumber,
	'fb-color-picker': FbColorPicker,

	'fb-simple-table': FbSimpleTable,

	'biz-dic-tree-select': BizDicTreeSelect,

	'biz-ent-person-single-select': BizEntPersonSingleSelect,
	'biz-ent-person-multiple-select': BizEntPersonMultipleSelect,
	'biz-ent-person-single-select-using-table-show': BizEntPersonSingleSelectUsingTableShow,
	'biz-ent-person-multiple-select-using-table-show': BizEntPersonMultipleSelectUsingTableShow,

	'biz-org-person-single-select': BizOrgPersonSingleSelect,
	'biz-org-person-multiple-select': BizOrgPersonMultipleSelect,
	'biz-org-ent-single-select': BizOrgEntSingleSelect,
	'biz-org-ent-multiple-select': BizOrgEntMultipleSelect,

	'biz-current-person': BizCurrentPerson,
	'biz-current-dept': BizCurrentDept,
	'biz-current-org': BizCurrentOrg,
	'biz-current-date-time': BizCurrentDateTime,
	'biz-current-time': BizCurrentTime,
	'biz-current-date': BizCurrentDate,
	'biz-current-ctx': BizCurrentCtx,

}
