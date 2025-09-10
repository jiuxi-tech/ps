import { each, isFunction } from 'lodash-es'
import AsyncValidator from 'async-validator'

(function addCustomRules () {

	// Validator.prototype.xxx = "asd"
	// Validator.prototype._messages = messages

// Validator.register('required', (rule, value, callback) => {
// 	if (value == '') {
// 		callback(new Error(rule.message || '必填'))
// 	} else {
// 		callback()
// 	}
// })

	// 正则校验
	let rules = {
		// digits: [
		// 	/^-?[0-9]+([.]{1}[0-9]+){0,1}$/,
		// 	'请输入数字'
		// ],
		//digits: [/^[\d+|\-]\d{0,9}\.{0,1}\d+$/, "请输入数字"],
		//digits: [/^[0-9]+([.]{1}[0-9]+){0,1}$/, "请输入数字"],
		// date: [
		// 	/^\d{4}-\d{2}-\d{2}$/,
		// 	'请输入有效的日期，格式:yyyy-mm-dd'
		// ],
		// 'date': function (rule, value, callback) {
		// 	if (value != '' && !/^\d{4}-\d{2}-\d{2}$/.test(value)) {
		// 		callback(new Error(rule.message ||
		// '请输入有效的日期，格式:yyyy-mm-dd'))
		// 	} else {
		// 		callback()
		// 	}
		// },
		// time: [
		// 	/^([01]\d|2[0-3])(:[0-5]\d){1,2}$/,
		// 	'请输入有效的时间，00:00到23:59之间'
		// ],
		// yyyyMMdd: [
		// 	/(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)$/,
		// '请输入有效的时间', ], HHmmss: [ /^([0-5]\d){1,2}\d{2}$/, '请输入有效的时间' ],
		// yyyyMMddHHmmss: [
		// /^\d{4}-\d{2}-\d{2}([01]\d|2[0-3])(:[0-5]\d){1,2}:\d{2}$/,
		// '请输入有效的时间', ], email: [
		// /^[\w\+\-]+(\.[\w\+\-]+)*@[a-z\d\-]+(\.[a-z\d\-]+)*\.([a-z]{2,4})$/i,
		// '请输入有效的邮箱', ], url: [ /^(https?|s?ftp):\/\/\S+$/i, '请输入有效的网址' ],
		letters: [
			/^[a-z]+$/i,
			'请输入字母',
		],
		qq: [
			/^[1-9]\d{4,}$/,
			'请输入有效的QQ号',
		],
		idcard: [
			/^\d{6}(19|2\d)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)?$/,
			'请输入正确的身份证号码',
		],
		/**
		 * 工商注册号
		 * @param value
		 * @returns {boolean|string}
		 */
		bizcode: function (rule, value, callback, source, options) {
			// 共15位：6位首次登记机关代码 + 8位顺序码 + 校验位
			if (!/^[1-6]\d{14}$/.test(value)) {
				return '请输入正确的工商注册号'
			}

			let s = [], p = [10]
			for (let i = 0; i < 15; i++) {
				s[i] = (p[i] % 11) + (+value.charAt(i))
				p[i + 1] = (s[i] % 10 || 10) * 2
			}
			if (1 !== s[14] % 10) {
				return '请输入正确的工商注册号'
			}

			return true
		},
		/**
		 * 组织机构代码证
		 * @param value
		 * @returns {boolean|string}
		 */
		orgcode: function (rule, value, callback, source, options) {
			if (!/^[0-9A-Z]+$/.test(value)) {
				return '请输入正确的组织机构代码'
			}

			let Wi = [3, 7, 9, 10, 5, 8, 4, 2]
			let Ci = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
			// 加权求和
			let sum = 0
			for (let i = 0; i < 8; i++) {
				sum += Ci.indexOf(value.charAt(i)) * Wi[i]
			}
			// 计算校验值： C9 = 11 - MOD ( ∑(Ci*Wi), 11 )
			let C9 = 11 - (sum % 11)
			if (C9 === 10) {
				C9 = 'X'
			} else if (C9 === 11) {
				C9 = 0
			}
			C9 = '' + C9
			// 与校验位比对
			if (C9 !== value.charAt(8)) {
				return '请输入正确的组织机构代码'
			}

			return true
		},
		// 统一社会信用代码
		unicode: function (rule, value, callback, source, options) {
			//18位校验及大写校验
			if ((value.length != 18) || (/^[0-9A-Z]+$/.test(value) == false)) {
				return '请填写正确的统一社会信用代码'
			}

			var Ancode//统一社会信用代码的每一个值
			var Ancodevalue//统一社会信用代码每一个值的权重
			var Wi = [
				1,
				3,
				9,
				27,
				19,
				26,
				16,
				17,
				20,
				29,
				25,
				13,
				8,
				24,
				10,
				30,
				28,
			]//加权因子
			var Ci = '0123456789ABCDEFGHJKLMNPQRTUWXY'
			//不用I、O、S、V、Z
			var sum = 0
			for (var i = 0; i < value.length - 1; i++) {
				Ancode = value.substring(i, i + 1)
				Ancodevalue = Ci.indexOf(Ancode)
				sum = sum + Ancodevalue * Wi[i]
				//权重与加权因子相乘之和
			}
			var C17 = 31 - sum % 31
			if (C17 == 31) {
				C17 = 0
			}
			var Str = '0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,J,K,L,M,N,P,Q,R,T,U,W,X,Y'
			C17 = Str.split(',')[C17]

			if (C17 != value.charAt(17)) {
				return '请填写正确的统一社会信用代码'
			}

			return true
		},
		/**
		 * 银行卡
		 * @param value
		 * @returns {boolean|string}
		 */
		// bankcard: function (rule, value, callback, source, options) {
		// 	value = value.value.replace(/\s/g, '')
		//
		// 	if (!/^[\d]{12,19}$/.test(value)) {
		// 		return "请输入有效的银行卡号";
		// 	}
		//
		// 	let arr = value.split('').reverse(), i = arr.length, temp, sum =
		// 0  while (i--) { if (i % 2 === 0) { sum += +arr[i] } else { temp =
		// +arr[i] * 2 sum += temp % 10 if (temp > 9) sum += 1 } } if (sum % 10
		// !== 0) { return "请输入有效的银行卡号"; } return true; },
		/**
		 * 说明：信用卡
		 * 规则：creditcard(type1, type2, ... typen)
		 * 参数：卡类型（不传参则支持全部类型）
		 * 支持的卡类型：mastercard, visa, amex, dinersclub, enroute, discover, jcb,
		 * unknown
		 *
		 * @param value
		 * @param params
		 * @returns {boolean}
		 */
		// creditcard: function (value, params) {
		// 	let validTypes = 0x0000, types = {
		// 		mastercard: 0x0001,
		// 		visa: 0x0002,
		// 		amex: 0x0004,
		// 		dinersclub: 0x0008,
		// 		enroute: 0x0010,
		// 		discover: 0x0020,
		// 		jcb: 0x0040,
		// 		unknown: 0x0080,
		// 	}
		//
		// 	if (/[^0-9\-]+/.test(value)) {
		// 		return false
		// 	}
		// 	value = value.replace(/\D/g, '')
		//
		// 	if (!params) {
		// 		validTypes =
		// 			0x0001 | 0x0002 | 0x0004 | 0x0008 | 0x0010 | 0x0020 |
		// 			0x0040 | 0x0080
		// 	} else {
		// 		for (let i = 0; i < parmas.length; i++) {
		// 			validTypes |= types[params[i]]
		// 		}
		// 	}
		//
		// 	if (validTypes & 0x0001 && /^(5[12345])/.test(value)) {
		// //mastercard return value.length === 16 } if (validTypes & 0x0002 &&
		// /^(4)/.test(value)) { //visa return value.length === 16 } if
		// (validTypes & 0x0004 && /^(3[47])/.test(value)) { //amex return
		// value.length === 15 } if (validTypes & 0x0008 &&
		// /^(3(0[012345]|[68]))/.test(value)) { //dinersclub return
		// value.length === 14 } if (validTypes & 0x0010 &&
		// /^(2(014|149))/.test(value)) { //enroute return value.length === 15
		// } if (validTypes & 0x0020 && /^(6011)/.test(value)) { //discover
		// return value.length === 16 } if (validTypes & 0x0040 &&
		// /^(3)/.test(value)) { //jcb return value.length === 16 } if
		// (validTypes & 0x0040 && /^(2131|1800)/.test(value)) { //jcb return
		// value.length === 15 } if (validTypes & 0x0080) { //unknown return
		// true } return false },
		tel: [
			/^(?:(?:0\d{2,3}[\- ]?[1-9]\d{6,7})|(?:[48]00[\- ]?[1-9]\d{6}))$/,
			'请输入有效的电话号码',
		],
		/**
		 * 手机号
		 * @param rule
		 * @param value
		 * @param callback
		 */
		mobile: function (rule, value, callback, source, options) {
			if (value != '' && !/^1\d{10}$/.test(value)) {
				return '请输入有效的手机号码'
			}

			return true
		},
		telmobile: function (rule, value, callback, source, options) {
			if (value.indexOf('-') >= 0) {
				return /^(?:(?:0\d{2,3}[\- ]?[1-9]\d{6,7})|(?:[48]00[\- ]?[1-9]\d{6}))$/.test(
					value) ? true : '请输入有效的电话号码'
			}
			// 中国手机
			if (value != '' && !/^1\d{10}$/.test(value)) {
				return '请输入有效的手机号码'
			}

			return true
		},
		zipcode: [
			/^\d{6}$/,
			'请检查邮政编码格式',
		],
		chinese: [
			/^[\u0391-\uFFE5]+$/,
			'请输入中文字符',
		],
		username: [
			/^[a-zA-z][a-zA-Z0-9_]{2,11}$/,
			'请输入3-12位 数字、字母、下划线',
		],
		/**
		 * 密码
		 * @param rule
		 * @param value
		 * @param callback
		 */
		password: function (rule, value, callback, source, options) {
			// if (value != '' && (value.length > 16 || value.length < 8 ||
			// 	!/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,16}$/
			// 		.test(value))
			// ) {
			// 	return '请输入8-16位，必须包含大小写字母及数字';
			// }

			if (value != '' && (value.length < 8 ||
				!/^(?![0-9]+$)(?![a-zA-Z]+$)(?![\W]+$)[a-zA-Z0-9\W]{8,}$/.test(
					value))
			) {
				return '请输入8位以上，包含数字，字母或特殊符号'
			}

			return true
		},
		/**
		 * 密码 + 特殊字符
		 * @param rule
		 * @param value
		 * @param callback
		 */
		passwordSpecial: function (rule, value, callback, source, options) {
			if (value != '' && (value.length < 8 ||
				!/^(?=.*\d)(?=.*[a-z])(?=.*[~!@#$%^&*])[\da-zA-Z~!@#$%^&*]{8,}$/.test(
					value))
			) {
				return '请输入8位以上，包含数字，字母，特殊符号!!!'
			}

			return true
		},
		// strongPassword: [
		// 	/^[a-zA-Z]\w{5,15}$/,
		// 	'请输入6-16位，必须包含英文字母、数字'
		// ],
		// strongPassword2: function (value, params) {
		// 	let range = params && params[0] || '6,20', value =
		// valueValue(value) let reg = new RegExp('^(?![^a-zA-Z]+$)(?!\D+$).{'
		// + range + '}$')  return reg.test(value) ? true :
		// '密码必须包含英文字符及数字，且长度在' + (range.replace(/,/, '-')) + '个字符之间'  },
		// accept: function (value, params) { if (!params) return true let ext
		// = params[0], value = valueValue(value) return (ext === '*') || (new
		// RegExp('.(?:' + ext + ')$', 'i')).test(value) ||
		// this.renderMsg('只接受{1}后缀的文件', ext.replace(/\|/g, ',')) },
		// fileTypeExts: function (el, params) { debugger }, fileSizeLimit:
		// function (el, params) { // KB MB 结尾 // 大小 100MB // valueValue
		// debugger  return false }, requiredOne: function (value) { return
		// !!this.$el.find('input[name="' + value.name + '"]').filter(function
		// () { return !this.disabled && this.value }).length }, numeric:
		// [/^[0-9]*$/, '请填写数值'],
		ip: [
			/^((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})$/i,
			'请填写有效的 IP 地址',
		],

		/**
		 * 中文名
		 * @param rule
		 * @param value
		 * @param callback
		 */
		// 'cname': function (rule, value, callback, source, options) {
		// 	if (value != '' &&
		// !/^[\u4E00-\u9FA50-9a-zA-Z]{2,10}$/.test(value)) { callback(new
		// Error(rule.message || '请输入正确的中文名')) } else { callback() } },
		/**
		 * 金额
		 * @param rule
		 * @param value
		 * @param callback
		 */
		// money: [/^(?:0|[1-9]\d*)(?:\.\d{1,2})?$/, '请填写有效的金额'],
		'money': function (rule, value, callback, source, options) {
			const toFixed = rule.fixed || 2

			if (value != '' &&
				!new RegExp(`(^[1-9]\\d*(\\.\\d{1,${toFixed}})?$)|(^0(\\.\\d{1,${toFixed}})?$)`).test(
					value)) {
				return '请填写有效的金额'
			}

			return true
		},

		// /**
		//  * 相等
		//  * @param rule
		//  * @param value
		//  * @param callback
		//  */
		// 'equal': function (rule, value, callback, source, options) {
		// 	const enqualTo = rule.enqualTo
		// 	if (value != enqualTo) {
		// 		callback(new Error(rule.message))
		// 	} else {
		// 		callback()
		// 	}
		// },

		// /**
		//  * 不小于
		//  * @param rule
		//  * @param value
		//  * @param callback
		//  */
		// 'min': function (rule, value, options) {
		// 	debugger
		// 	const minTo = rule.minTo
		// 	if (value !== '' && value !== null && value !== undefined &&
		// Number(value) < Number(minTo)) { return rule.message; }  return
		// true; },

		// /**
		//  * 不大于
		//  * @param rule
		//  * @param value
		//  * @param callback
		//  */
		// 'max': function (rule, value, options) {
		// 	const maxTo = rule.maxTo
		// 	if (value !== '' && value !== null && value !== undefined &&
		// Number(value) > Number(maxTo)) { return rule.message; } return true;
		// }
	}

	each(rules, (_rule, name) => {
		AsyncValidator.register(name,
			(rule, value, callback, source, options) => {
				if (value == '') {
					callback()
				}

				let result = false
				if (isFunction(_rule)) {
					result = _rule(rule, value, callback, source, options)

					if (result === true) {
						callback()
					} else {
						// result <> true: 表示返回的是错误信息
						callback(new Error(result))
					}

				} else {
					let reg = _rule[0]
					let message = _rule[1]

					if (!reg.test(value)) {
						// 正则校验未通过，返回错误信息
						callback(new Error(message))
					} else {
						callback()
					}
				}
			})
	})

	return AsyncValidator
})()

export default AsyncValidator
