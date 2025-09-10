import VuePopper from './vue-popper'

/**
 * Popper 弹出层混入工具
 * 
 * @description 提供 Popper 弹出层功能的 Vue 混入工具
 */

/**
 * Popper 弹出层混入配置
 * 
 * @property placement - 弹出层位置，默认为 'bottom-start'
 * @property appendToBody - 是否添加到 body
 * @property visibleArrow - 是否显示箭头，默认为 false
 * @property arrowOffset - 箭头偏移量
 * @property offset - 弹出层偏移量
 * @property boundariesPadding - 边界内边距
 * @property popperOptions - Popper.js 配置选项
 * @example
 * // 在 Vue 组件中使用
 * import PopperMixin from './popper-mixin';
 * 
 * export default {
 *   mixins: [PopperMixin],
 *   // 组件其他配置...
 * }
 */
export interface PopperMixin {
  /**
   * 组件属性
   */
  props: {
    /**
     * 弹出层位置
     * @default 'bottom-start'
     */
    placement: string;
    
    /**
     * 是否添加到 body
     */
    appendToBody: VuePopper['props']['appendToBody'];
    
    /**
     * 是否显示箭头，影响弹窗初始化位置
     * @default false
     */
    visibleArrow: boolean;
    
    /**
     * 箭头偏移量
     */
    arrowOffset: VuePopper['props']['arrowOffset'];
    
    /**
     * 弹出层偏移量
     */
    offset: VuePopper['props']['offset'];
    
    /**
     * 边界内边距
     */
    boundariesPadding: VuePopper['props']['boundariesPadding'];
    
    /**
     * Popper.js 配置选项
     */
    popperOptions: VuePopper['props']['popperOptions'];
  };
  
  /**
   * 组件方法
   */
  methods: VuePopper['methods'];
  
  /**
   * 组件数据
   */
  data: VuePopper['data'];
  
  /**
   * 生命周期钩子
   */
  beforeDestroy: VuePopper['beforeDestroy'];
}

/**
 * PopperMixin 默认导出
 */
declare const popperMixin: PopperMixin;

export default popperMixin;