<script>
	/*!
	 * text
	 * (c) 2020 lincong1987
	 */

	import {
		isNumber,
		each,
		map,
		isString,
		endsWith,
		indexOf,
		includes,
		keysIn,
		isUndefined,
		isNaN,
		toNumber,
		parseInt,
	} from 'lodash-es'
	import { filterEmpty } from '../../../utils/componentUtils'
	import { prefix } from '../../../../src/config'

	export default {

		name: 'FbSpace',

		/***
		 * 注意,这是一个函数式组件
		 * @returns ****************存在内存泄漏，原因待测****************
		 */
		// functional: true,

		props: {
			/**
			 * 间隔尺寸
			 *
			 * if type string 's', 'm', 'l', 'xl' '1px' '2px'
			 * if type number 1 2 3 4 => 1px 2px 3px 4px
			 */
			size: {
				type: [String, Number],
				default: 's',

			},
			/**
			 * 方向
			 */
			direction: {
				type: String,
				default: 'horizontal',
				validator: function (value) {
					return ['horizontal', 'vertical'].includes(value)
				},
			},
			/**
			 * 对齐方式
			 */
			align: {
				type: String,
				default: 'start',
				validator: function (value) {
					return ['start', 'end', 'center', 'baseline'].includes(value)
				},
			},
		},

		data () {
			return {
				prefix,
				items: []
			}
		},

		render (h, context) {

			// let items = filterEmpty(context.children)
			let items = filterEmpty(this.$slots.default)


			let len = items.length

			if (len === 0) {
				return null
			}

			// 注意: 函数式组件里面 只接受 props 一个成员
			// 并且,建议自己实现 render
			// let _content$props = context.props,
			// 	align = _content$props.align,
			// 	size = _content$props.size,
			// 	direction = _content$props.direction

			let _content$props = this.$props,
				align = _content$props.align,
				size = _content$props.size,
				direction = _content$props.direction

			let spaceSize = {
				s: 8,
				m: 16,
				l: 24,
				xl: 36,
			}

			/**
			 *
			 * @param size
			 * @returns {string|*}
			 */
			let getSize = (size) => {

				if (isUndefined(size)) {
					return 0
				}

				if (isString(size)) {
					if (endsWith(size, 'px')) {
						return size
					}

					if (endsWith(size, '%')) {
						return size
					}

					if (includes(keysIn(spaceSize), size)) {
						return `${spaceSize[size]}px`
					}
				}

				let _size = parseInt(size, 10)

				if (_size.toFixed() == 'NaN') {
					return 0
				}

				if (isNumber(_size)) {
					return `${size}px`
				}

			}

			let mergedAlign = align === undefined && direction === 'horizontal' ? 'center' : align

			let style = {}

			size = getSize(size)

			if (direction === 'vertical') {
				style.marginBottom = size
			} else {
				style.marginRight = size
			}

			let child = items.map((child, i) => {
				return h('div', {
					'class': `${prefix}-space__item`,
					key: `${prefix}-space__item-${i}`,
					style: i === items.length - 1 ? {} : style,

				}, [child])
			})


			return h('div', {
					'class': [
						`${prefix}-space`,
						`${prefix}-space--${direction}`,
						mergedAlign ? `${prefix}-space--align-${mergedAlign}` : '',
						{},
					],
				},
				[
					child,
					// map(items, (child, i) => {
					//
					// 	return h('div', {
					// 		'class': `${prefix}-space__item`,
					// 		key: `${prefix}-space__item-${i}`,
					// 		style: i === items.length - 1 ? {} : style,
					//
					// 	}, [child])
					// }),
				],
			)

		},
	}
</script>
