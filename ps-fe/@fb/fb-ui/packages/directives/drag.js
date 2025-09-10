import { defaultsDeep } from 'lodash-es'
import { prefix } from '../../src/config'

/**
 *
 */
export default {
	bind (el, binding, vnode) {

		// return

		let {value} = binding

		value = value || {}

		// let options = defaultsDeep()

		// 待支持
		// 贴合 snap, snapMode,
		// 对齐网格 grid,

		let handle = value.handle || `.${prefix}-dialog-header`
		//let  = value.el el|| '.fb-dialog'
		const dialogHeaderEl = el.querySelector(handle)
		//  (value.el && el.querySelector(value.el)) || el //
		const dragDom = el.querySelector(value.el || `.${prefix}-dialog`)
		dialogHeaderEl.style.cssText += ';cursor:move;'
		//dragDom.style.cssText += ';top:0px;'

		// 获取原有属性 ie dom元素.currentStyle 火狐谷歌 window.getComputedStyle(dom元素,
		// null);
		const getStyle = (function () {
			if (window.document.currentStyle) {
				return (dom, attr) => dom.currentStyle[attr]
			} else {
				return (dom, attr) => getComputedStyle(dom, false)[attr]
			}
		})()

		dialogHeaderEl.onmousedown = (e) => {

			// console.log('onmousedown')
			// 鼠标按下，计算当前元素距离可视区的距离
			const disX = e.clientX - dragDom.offsetLeft
			const disY = e.clientY - dragDom.offsetTop


			// console.log(e.clientX, e.clientY)

			// console.log('dragDom offsetLeft offsetTop',
			//	dragDom.offsetLeft, dragDom.offsetTop)

			// console.log("disX, disY", disX, disY)

			const dragDomWidth = dragDom.offsetWidth
			const dragDomHeight = dragDom.offsetHeight

			const screenWidth = document.body.clientWidth
			const screenHeight = document.body.clientHeight

			const minDragDomLeft = dragDom.offsetLeft
			const maxDragDomLeft = screenWidth - dragDom.offsetLeft -
				dragDomWidth

			const minDragDomTop = dragDom.offsetTop
			const maxDragDomTop = screenHeight - dragDom.offsetTop -
				dragDomHeight

			// console.log(e.clientX, e.clientY)

			// 获取到的值带px 正则匹配替换
//			let styL = getStyle(dragDom, 'left')
//			let styT = getStyle(dragDom, 'top')
//
//			if (styL.includes('%')) {
//				styL = +document.body.clientWidth *
//					(+styL.replace(/\%/g, '') / 100)
//				styT = +document.body.clientHeight *
//					(+styT.replace(/\%/g, '') / 100)
//			} else {
//				styL = +styL.replace(/\px/g, '')
//				styT = +styT.replace(/\px/g, '')
//			}

			//// console.log(styL, styT)

			document.onmousemove = function (e) {
				// 通过事件委托，计算移动的距离
				let left = e.clientX - disX
				let top = e.clientY - disY

				// console.log(e.clientX, e.clientY)
				// console.log(left, top)

				dragDom.style.cssText +=
					`;left:${left }px;top:${top }px;`


				// 边界处理
				if (-(left) > minDragDomLeft) {
					left = -minDragDomLeft
				} else if (left > maxDragDomLeft) {
					left = maxDragDomLeft
				}

				if (-(top) > minDragDomTop) {
					top = -minDragDomTop
				} else if (top > maxDragDomTop) {
					top = maxDragDomTop
				}

				// 移动当前元素
//				dragDom.style.cssText +=
//					`;left:${left + styL}px;top:${top + styT}px;`

				// emit onDrag event
//				vnode.child.$emit('on-drag')
			}

			document.onmouseup = function (e) {
				document.onmousemove = null
				document.onmouseup = null
			}
		}
	},
}
