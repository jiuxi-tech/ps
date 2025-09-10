import Vue from 'vue';
import { addClass, removeClass } from '../dom';

let hasModal = false;
let hasInitZIndex = false;
let zIndex;

/**
 * @desc 获取遮罩层元素
 * @returns {HTMLElement|undefined} 返回遮罩层元素或 undefined
 */
const getModal = function() {
  if (Vue.prototype.$isServer) return;
  let modalDom = PopupManager.modalDom;
  if (modalDom) {
    hasModal = true;
  } else {
    hasModal = false;
    modalDom = document.createElement('div');
    PopupManager.modalDom = modalDom;

    modalDom.addEventListener('touchmove', function(event) {
      event.preventDefault();
      event.stopPropagation();
    });

    modalDom.addEventListener('click', function() {
      PopupManager.doOnModalClick && PopupManager.doOnModalClick();
    });
  }

  return modalDom;
};

const instances = {};

/**
 * @namespace PopupManager
 * @desc 弹出层管理器
 * @description 管理弹出层实例和遮罩层的工具
 */

const PopupManager = {
  /**
   * @member {Boolean} modalFade
   * @desc 遮罩层是否淡入淡出
   * @default true
   */
  modalFade: true,

  /**
   * @desc 获取弹出层实例
   * @param {String} id - 实例 ID
   * @returns {Object} 返回弹出层实例
   * @example
   * // 获取弹出层实例
   * const instance = PopupManager.getInstance('popup-1');
   */
  getInstance: function(id) {
    return instances[id];
  },

  /**
   * @desc 注册弹出层实例
   * @param {String} id - 实例 ID
   * @param {Object} instance - 弹出层实例
   * @example
   * // 注册弹出层实例
   * PopupManager.register('popup-1', popupInstance);
   */
  register: function(id, instance) {
    if (id && instance) {
      instances[id] = instance;
    }
  },

  /**
   * @desc 注销弹出层实例
   * @param {String} id - 实例 ID
   * @example
   * // 注销弹出层实例
   * PopupManager.deregister('popup-1');
   */
  deregister: function(id) {
    if (id) {
      instances[id] = null;
      delete instances[id];
    }
  },

  /**
   * @desc 获取下一个 z-index 值
   * @returns {Number} 返回下一个 z-index 值
   * @example
   * // 获取下一个 z-index 值
   * const zIndex = PopupManager.nextZIndex();
   */
  nextZIndex: function() {
    return PopupManager.zIndex++;
  },

  /**
   * @member {Array} modalStack
   * @desc 遮罩层堆栈
   */
  modalStack: [],

  /**
   * @desc 处理遮罩层点击事件
   * @example
   * // 处理遮罩层点击事件
   * PopupManager.doOnModalClick();
   */
  doOnModalClick: function() {
    const topItem = PopupManager.modalStack[PopupManager.modalStack.length - 1];
    if (!topItem) return;

    const instance = PopupManager.getInstance(topItem.id);
    if (instance && instance.closeOnClickModal) {
      instance.close();
    }
  },

  /**
   * @desc 打开遮罩层
   * @param {String} id - 实例 ID
   * @param {Number} zIndex - z-index 值
   * @param {HTMLElement} dom - DOM 元素
   * @param {String} modalClass - 遮罩层类名
   * @param {Boolean} modalFade - 是否淡入淡出
   * @example
   * // 打开遮罩层
   * PopupManager.openModal('popup-1', 2000, element, 'custom-class', true);
   */
  openModal: function(id, zIndex, dom, modalClass, modalFade) {
    if (Vue.prototype.$isServer) return;
    if (!id || zIndex === undefined) return;
    this.modalFade = modalFade;

    const modalStack = this.modalStack;

    for (let i = 0, j = modalStack.length; i < j; i++) {
      const item = modalStack[i];
      if (item.id === id) {
        return;
      }
    }

    const modalDom = getModal();

    addClass(modalDom, 'v-modal');
    if (this.modalFade && !hasModal) {
      addClass(modalDom, 'v-modal-enter');
    }
    if (modalClass) {
      let classArr = modalClass.trim().split(/\s+/);
      classArr.forEach(item => addClass(modalDom, item));
    }
    setTimeout(() => {
      removeClass(modalDom, 'v-modal-enter');
    }, 200);

    if (dom && dom.parentNode && dom.parentNode.nodeType !== 11) {
      dom.parentNode.appendChild(modalDom);
    } else {
      document.body.appendChild(modalDom);
    }

    if (zIndex) {
      modalDom.style.zIndex = zIndex;
    }
    modalDom.tabIndex = 0;
    modalDom.style.display = '';

    this.modalStack.push({ id: id, zIndex: zIndex, modalClass: modalClass });
  },

  /**
   * @desc 关闭遮罩层
   * @param {String} id - 实例 ID
   * @example
   * // 关闭遮罩层
   * PopupManager.closeModal('popup-1');
   */
  closeModal: function(id) {
    const modalStack = this.modalStack;
    const modalDom = getModal();

    if (modalStack.length > 0) {
      const topItem = modalStack[modalStack.length - 1];
      if (topItem.id === id) {
        if (topItem.modalClass) {
          let classArr = topItem.modalClass.trim().split(/\s+/);
          classArr.forEach(item => removeClass(modalDom, item));
        }

        modalStack.pop();
        if (modalStack.length > 0) {
          modalDom.style.zIndex = modalStack[modalStack.length - 1].zIndex;
        }
      } else {
        for (let i = modalStack.length - 1; i >= 0; i--) {
          if (modalStack[i].id === id) {
            modalStack.splice(i, 1);
            break;
          }
        }
      }
    }

    if (modalStack.length === 0) {
      if (this.modalFade) {
        addClass(modalDom, 'v-modal-leave');
      }
      setTimeout(() => {
        if (modalStack.length === 0) {
          if (modalDom.parentNode) modalDom.parentNode.removeChild(modalDom);
          modalDom.style.display = 'none';
          PopupManager.modalDom = undefined;
        }
        removeClass(modalDom, 'v-modal-leave');
      }, 200);
    }
  }
};

Object.defineProperty(PopupManager, 'zIndex', {
  configurable: true,
  get() {
    if (!hasInitZIndex) {
      zIndex = zIndex || (Vue.prototype.$ELEMENT || {}).zIndex || 2000;
      hasInitZIndex = true;
    }
    return zIndex;
  },
  set(value) {
    zIndex = value;
  }
});

/**
 * @desc 获取顶层弹出层
 * @returns {Object|undefined} 返回顶层弹出层实例或 undefined
 */
const getTopPopup = function() {
  if (Vue.prototype.$isServer) return;
  if (PopupManager.modalStack.length > 0) {
    const topPopup = PopupManager.modalStack[PopupManager.modalStack.length - 1];
    if (!topPopup) return;
    const instance = PopupManager.getInstance(topPopup.id);

    return instance;
  }
};

if (!Vue.prototype.$isServer) {
  // handle `esc` key when the popup is shown
  window.addEventListener('keydown', function(event) {
    if (event.keyCode === 27) {
      const topPopup = getTopPopup();

      if (topPopup && topPopup.closeOnPressEscape) {
        topPopup.handleClose
          ? topPopup.handleClose()
          : (topPopup.handleAction ? topPopup.handleAction('cancel') : topPopup.close());
      }
    }
  });
}

export default PopupManager;