/*!
* resize - 元素尺寸变化监听工具
* (c) 2020 lincong1987
*/

/**
 * @namespace Resize
 * @desc 元素尺寸变化监听工具函数集合
 */

const requestFrame = (function () {
	const raf = window.requestAnimationFrame
		|| window.mozRequestAnimationFrame
		|| window.webkitRequestAnimationFrame
		|| function fallbackRAF(func) {
			return setTimeout(() => {
				func && func();
			}, 20);
		};
	return function requestFrameFunction(func) {
		return raf(func);
	};
}());

const cancelFrame = (function () {
	const cancel = window.cancelAnimationFrame
		|| window.mozCancelAnimationFrame
		|| window.webkitCancelAnimationFrame
		|| window.clearTimeout;
	return function cancelFrameFunction(id) {
		return cancel(id);
	};
}());

/**
 * @desc 尺寸变化监听器
 * @param {Event} e - 事件对象
 */
function resizeListener(e) {
	const win = e.target || e.srcElement;
	if (win.__resizeRAF__) {
		cancelFrame(win.__resizeRAF__);
	}
	win.__resizeRAF__ = requestFrame(() => {
		const trigger = win.__resizeTrigger__;
		const listeners = trigger && trigger.__resizeListeners__;
		if (listeners) {
			listeners.forEach((fn) => {
				fn.call(trigger, e);
			});
		}
	});
}

/**
 * @desc 监听元素尺寸变化
 * @param {Element} element - 要监听的元素
 * @param {Function} fn - 尺寸变化回调函数
 * @example
 * // 监听元素尺寸变化
 * resize(element, function(e) {
 *   console.log('元素尺寸发生了变化');
 * });
 */
let resize = function (element, fn) {
	const document = window.document;
	const attachEvent = document.attachEvent;
	function objectLoad() {
		this.contentDocument.defaultView.__resizeTrigger__ = this.__resizeElement__;
		this.contentDocument.defaultView.addEventListener('resize', resizeListener);
	}

	if (!element.__resizeListeners__) {
		element.__resizeListeners__ = [];
		if (attachEvent) {
			element.__resizeTrigger__ = element;
			element.attachEvent('onresize', resizeListener);
		} else {
			if (getComputedStyle(element).position === 'static') {
				element.style.position = 'relative';
			}
			const obj = (element.__resizeTrigger__ = document.createElement('object'));
			obj.setAttribute(
				'style',
				'display: block; position: absolute; top: 0; left: 0; height: 100%; width: 100%; overflow: hidden; pointer-events: none; z-index: -1; opacity: 0;'
			);
			obj.setAttribute('class', 'resize-sensor');
			obj.__resizeElement__ = element;
			obj.onload = objectLoad;
			obj.type = 'text/html';
			obj.data = 'about:blank';
			element.appendChild(obj);
		}
	}
	element.__resizeListeners__.push(fn);
};

resize = (typeof window === 'undefined' ? resize : resize.bind(window));

/**
 * @desc 解除元素尺寸变化监听
 * @param {Element} element - 要解除监听的元素
 * @param {Function} [fn] - 要解除的回调函数，如果不传则解除所有监听
 * @example
 * // 解除特定回调函数的监听
 * resize.unbind(element, callback);
 * 
 * // 解除所有监听
 * resize.unbind(element);
 */
resize.unbind = function (element, fn) {
	const attachEvent = document.attachEvent;
	let listeners = element.__resizeListeners__ || [];
	if (fn) {
		const index = listeners.indexOf(fn);
		if (index !== -1) {
			listeners.splice(index, 1);
		}
	} else {
		listeners = element.__resizeListeners__ = [];
	}
	if (!listeners.length) {
		if (attachEvent) {
			element.detachEvent('onresize', resizeListener);
		} else if (element.__resizeTrigger__) {
			if (element.__resizeTrigger__.contentDocument) {
				element.__resizeTrigger__.contentDocument.defaultView.removeEventListener(
					'resize',
					resizeListener
				);
				delete element.__resizeTrigger__.contentDocument.defaultView.__resizeTrigger__;
			}
			element.__resizeTrigger__ = !element.removeChild(
				element.__resizeTrigger__
			);
		}
		delete element.__resizeListeners__;
	}
};

export default resize;