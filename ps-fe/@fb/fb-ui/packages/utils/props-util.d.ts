/**
 * props-util - Vue 组件属性工具函数集
 * @description 提供 Vue 组件属性相关的工具函数
 */

/**
 * Vue 组件属性工具函数集合
 */

/**
 * Vue 属性类型定义
 * 
 * @property string - 字符串类型定义
 * @property boolean - 布尔类型定义
 * @property number - 数字类型定义
 * @property object - 对象类型定义
 * @property array - 数组类型定义
 * @property func - 函数类型定义
 * @property custom - 自定义类型定义函数
 * @example
 * // 使用预定义的类型
 * props: {
 *   title: vueType.string,
 *   visible: vueType.boolean,
 *   count: vueType.number
 * }
 * 
 * // 使用自定义类型
 * props: {
 *   customProp: vueType.custom(String, 'default')
 * }
 */
export const vueType: {
  /**
   * 字符串类型定义
   * 
   * @property type - 数据类型为 String
   * @property default - 默认值为空字符串
   */
  string: {
    type: StringConstructor;
    default: string;
  };

  /**
   * 布尔类型定义
   * 
   * @property type - 数据类型为 Boolean
   * @property default - 默认值为 false
   */
  boolean: {
    type: BooleanConstructor;
    default: boolean;
  };

  /**
   * 数字类型定义
   * 
   * @property type - 数据类型为 Number
   * @property default - 默认值为空字符串
   */
  number: {
    type: NumberConstructor;
    default: string;
  };

  /**
   * 对象类型定义
   * 
   * @property type - 数据类型为 Object
   * @property default - 默认值为空字符串
   */
  object: {
    type: ObjectConstructor;
    default: string;
  };

  /**
   * 数组类型定义
   * 
   * @property type - 数据类型为 Array
   * @property default - 默认值为空字符串
   */
  array: {
    type: ArrayConstructor;
    default: string;
  };

  /**
   * 函数类型定义
   * 
   * @property type - 数据类型为 Function
   * @property default - 默认值为 null
   */
  func: {
    type: FunctionConstructor;
    default: null;
  };

  /**
   * 自定义类型定义函数
   * 
   * @param type - 自定义数据类型
   * @param def - 自定义默认值
   * @returns 返回包含 type 和 default 属性的对象
   * @example
   * // 自定义类型，默认值为 'default'
   * const customProp = vueType.custom(String, 'default');
   * 
   * // 自定义类型，默认值为 null
   * const customProp2 = vueType.custom(Object, null);
   */
  custom(type?: any, def?: any): {
    type: any;
    default: any;
  };
};

/**
 * 初始化默认属性
 * 
 * @param _props - 初始化类型
 * @param _defaultProps - 默认值
 * @returns 返回处理后的属性对象
 * @example
 * // 初始化组件属性
 * const props = {
 *   title: vueType.string,
 *   visible: vueType.boolean
 * };
 * 
 * const defaultProps = {
 *   title: '默认标题',
 *   visible: true
 * };
 * 
 * const initializedProps = initDefaultProps(props, defaultProps);
 */
export function initDefaultProps(
  _props: Record<string, any>,
  _defaultProps: Record<string, any>
): Record<string, any>;

/**
 * PropsUtil 默认导出
 */
export interface PropsUtil {
  vueType: typeof vueType;
  initDefaultProps: typeof initDefaultProps;
}

declare const propsUtil: PropsUtil;

export default propsUtil;