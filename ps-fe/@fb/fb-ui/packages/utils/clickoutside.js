import Vue from 'vue';
import { on } from './dom';

/**
 * clickoutside - 点击元素外部指令
 * @description Vue 自定义指令，用于检测用户是否点击了元素外部区域
 */

/**
 * @namespace ClickOutside
 * @desc 点击元素外部处理工具
 */

const nodeList = [];
const ctx = '@@clickoutsideContext';

let startClick;
let seed = 0;

!Vue.prototype.$isServer && on(document, 'mousedown', e => (startClick = e));

!Vue.prototype.$isServer && on(document, 'mouseup', e => {
  nodeList.forEach(node => node[ctx].documentHandler(e, startClick));
});

/**
 * @desc 创建文档点击处理函数
 * @param {Element} el - 指令绑定的元素
 * @param {Object} binding - 指令绑定对象
 * @param {Object} vnode - Vue 虚拟节点
 * @returns {Function} 返回文档点击处理函数
 */
function createDocumentHandler(el, binding, vnode) {
  return function(mouseup = {}, mousedown = {}) {
    if (!vnode ||
      !vnode.context ||
      !mouseup.target ||
      !mousedown.target ||
      el.contains(mouseup.target) ||
      el.contains(mousedown.target) ||
      el === mouseup.target ||
      (vnode.context.popperElm &&
      (vnode.context.popperElm.contains(mouseup.target) ||
      vnode.context.popperElm.contains(mousedown.target)))) return;

    if (binding.expression &&
      el[ctx].methodName &&
      vnode.context[el[ctx].methodName]) {
      vnode.context[el[ctx].methodName]();
    } else {
      el[ctx].bindingFn && el[ctx].bindingFn();
    }
  };
}

/**
 * v-clickoutside - 点击元素外面才会触发的事件
 * @desc Vue 自定义指令，当用户点击元素外部时触发指定方法
 * @example
 * ```vue
 * <div v-clickoutside="handleClose">
 * ```
 */
export default {
  /**
   * @desc 指令绑定时调用
   * @param {Element} el - 指令绑定的元素
   * @param {Object} binding - 指令绑定对象
   * @param {Object} vnode - Vue 虚拟节点
   */
  bind(el, binding, vnode) {
    nodeList.push(el);
    const id = seed++;
    el[ctx] = {
      id,
      documentHandler: createDocumentHandler(el, binding, vnode),
      methodName: binding.expression,
      bindingFn: binding.value
    };
  },

  /**
   * @desc 指令更新时调用
   * @param {Element} el - 指令绑定的元素
   * @param {Object} binding - 指令绑定对象
   * @param {Object} vnode - Vue 虚拟节点
   */
  update(el, binding, vnode) {
    el[ctx].documentHandler = createDocumentHandler(el, binding, vnode);
    el[ctx].methodName = binding.expression;
    el[ctx].bindingFn = binding.value;
  },

  /**
   * @desc 指令解绑时调用
   * @param {Element} el - 指令绑定的元素
   */
  unbind(el) {
    let len = nodeList.length;

    for (let i = 0; i < len; i++) {
      if (nodeList[i][ctx].id === el[ctx].id) {
        nodeList.splice(i, 1);
        break;
      }
    }
    delete el[ctx];
  }
};