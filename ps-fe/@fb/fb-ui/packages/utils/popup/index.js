import Vue from 'vue';
import {merge} from 'lodash-es';
import PopupManager from './popup-manager';
// import getScrollBarWidth from '../scrollbar-width';
import { getStyle, addClass, removeClass, hasClass } from '../dom';

let idSeed = 1;

let scrollBarWidth;

/**
 * @namespace Popup
 * @desc 弹出层组件
 * @description 提供弹出层功能的 Vue 组件
 */

export default {
  props: {
    /**
     * @member {Boolean} visible
     * @desc 弹出层是否可见
     * @default false
     */
    visible: {
      type: Boolean,
      default: false
    },
    
    /**
     * @member {Number} openDelay
     * @desc 打开延迟时间（毫秒）
     */
    openDelay: {},
    
    /**
     * @member {Number} closeDelay
     * @desc 关闭延迟时间（毫秒）
     */
    closeDelay: {},
    
    /**
     * @member {Number} zIndex
     * @desc z-index 层级
     */
    zIndex: {},
    
    /**
     * @member {Boolean} modal
     * @desc 是否显示遮罩层
     * @default false
     */
    modal: {
      type: Boolean,
      default: false
    },
    
    /**
     * @member {Boolean} modalFade
     * @desc 遮罩层是否淡入淡出
     * @default true
     */
    modalFade: {
      type: Boolean,
      default: true
    },
    
    /**
     * @member {String} modalClass
     * @desc 遮罩层自定义类名
     */
    modalClass: {},
    
    /**
     * @member {Boolean} modalAppendToBody
     * @desc 遮罩层是否添加到 body
     * @default false
     */
    modalAppendToBody: {
      type: Boolean,
      default: false
    },
    
    /**
     * @member {Boolean} lockScroll
     * @desc 是否锁定滚动
     * @default true
     */
    lockScroll: {
      type: Boolean,
      default: true
    },
    
    /**
     * @member {Boolean} closeOnPressEscape
     * @desc 是否按下 ESC 键关闭
     * @default false
     */
    closeOnPressEscape: {
      type: Boolean,
      default: false
    },
    
    /**
     * @member {Boolean} closeOnClickModal
     * @desc 是否点击遮罩层关闭
     * @default false
     */
    closeOnClickModal: {
      type: Boolean,
      default: false
    }
  },

  /**
   * @desc 组件挂载前钩子
   */
  beforeMount() {
    this._popupId = 'popup-' + idSeed++;
    PopupManager.register(this._popupId, this);
  },

  /**
   * @desc 组件销毁前钩子
   */
  beforeDestroy() {
    PopupManager.deregister(this._popupId);
    PopupManager.closeModal(this._popupId);

    this.restoreBodyStyle();
  },

  data() {
    return {
      /**
       * @member {Boolean} opened
       * @desc 弹出层是否已打开
       */
      opened: false,
      
      /**
       * @member {String} bodyPaddingRight
       * @desc body 的 padding-right 值
       */
      bodyPaddingRight: null,
      
      /**
       * @member {Number} computedBodyPaddingRight
       * @desc 计算后的 body padding-right 值
       */
      computedBodyPaddingRight: 0,
      
      /**
       * @member {Boolean} withoutHiddenClass
       * @desc 是否没有隐藏类
       */
      withoutHiddenClass: true,
      
      /**
       * @member {Boolean} rendered
       * @desc 是否已渲染
       */
      rendered: false
    };
  },

  watch: {
    /**
     * @desc 监听 visible 属性变化
     * @param {Boolean} val - 可见性值
     */
    visible(val) {
      if (val) {
        if (this._opening) return;
        if (!this.rendered) {
          this.rendered = true;
          Vue.nextTick(() => {
            this.open();
          });
        } else {
          this.open();
        }
      } else {
        this.close();
      }
    }
  },

  methods: {
    /**
     * @desc 打开弹出层
     * @param {Object} options - 打开选项
     * @example
     * // 打开弹出层
     * popup.open();
     * 
     * // 带选项打开弹出层
     * popup.open({ modal: true });
     */
    open(options) {
      if (!this.rendered) {
        this.rendered = true;
      }

      const props = merge({}, this.$props || this, options);

      if (this._closeTimer) {
        clearTimeout(this._closeTimer);
        this._closeTimer = null;
      }
      clearTimeout(this._openTimer);

      const openDelay = Number(props.openDelay);
      if (openDelay > 0) {
        this._openTimer = setTimeout(() => {
          this._openTimer = null;
          this.doOpen(props);
        }, openDelay);
      } else {
        this.doOpen(props);
      }
    },

    /**
     * @desc 执行打开弹出层
     * @param {Object} props - 属性配置
     */
    doOpen(props) {
      if (this.$isServer) return;
      if (this.willOpen && !this.willOpen()) return;
      if (this.opened) return;

      this._opening = true;

      const dom = this.$el;

      const modal = props.modal;

      const zIndex = props.zIndex;
      if (zIndex) {
        PopupManager.zIndex = zIndex;
      }

      if (modal) {
        if (this._closing) {
          PopupManager.closeModal(this._popupId);
          this._closing = false;
        }
        PopupManager.openModal(this._popupId, PopupManager.nextZIndex(), this.modalAppendToBody ? undefined : dom, props.modalClass, props.modalFade);
        if (props.lockScroll) {
          this.withoutHiddenClass = !hasClass(document.body, 'el-popup-parent--hidden');
          if (this.withoutHiddenClass) {
            this.bodyPaddingRight = document.body.style.paddingRight;
            this.computedBodyPaddingRight = parseInt(getStyle(document.body, 'paddingRight'), 10);
          }
          scrollBarWidth = getScrollBarWidth();
          let bodyHasOverflow = document.documentElement.clientHeight < document.body.scrollHeight;
          let bodyOverflowY = getStyle(document.body, 'overflowY');
          if (scrollBarWidth > 0 && (bodyHasOverflow || bodyOverflowY === 'scroll') && this.withoutHiddenClass) {
            document.body.style.paddingRight = this.computedBodyPaddingRight + scrollBarWidth + 'px';
          }
          addClass(document.body, 'el-popup-parent--hidden');
        }
      }

      if (getComputedStyle(dom).position === 'static') {
        dom.style.position = 'absolute';
      }

      dom.style.zIndex = PopupManager.nextZIndex();
      this.opened = true;

      this.onOpen && this.onOpen();

      this.doAfterOpen();
    },

    /**
     * @desc 打开后处理
     */
    doAfterOpen() {
      this._opening = false;
    },

    /**
     * @desc 关闭弹出层
     * @example
     * // 关闭弹出层
     * popup.close();
     */
    close() {
      if (this.willClose && !this.willClose()) return;

      if (this._openTimer !== null) {
        clearTimeout(this._openTimer);
        this._openTimer = null;
      }
      clearTimeout(this._closeTimer);

      const closeDelay = Number(this.closeDelay);

      if (closeDelay > 0) {
        this._closeTimer = setTimeout(() => {
          this._closeTimer = null;
          this.doClose();
        }, closeDelay);
      } else {
        this.doClose();
      }
    },

    /**
     * @desc 执行关闭弹出层
     */
    doClose() {
      this._closing = true;

      this.onClose && this.onClose();

      if (this.lockScroll) {
        setTimeout(this.restoreBodyStyle, 200);
      }

      this.opened = false;

      this.doAfterClose();
    },

    /**
     * @desc 关闭后处理
     */
    doAfterClose() {
      PopupManager.closeModal(this._popupId);
      this._closing = false;
    },

    /**
     * @desc 恢复 body 样式
     */
    restoreBodyStyle() {
      if (this.modal && this.withoutHiddenClass) {
        document.body.style.paddingRight = this.bodyPaddingRight;
        removeClass(document.body, 'el-popup-parent--hidden');
      }
      this.withoutHiddenClass = true;
    }
  }
};

export {
  PopupManager
};