/**
 * utils
 * (c) 2024 lincong1987
 */

export interface Utils {
  /**
   * 将数值转换为带单位的字符串
   * @param val 输入值，可以是数字或字符串
   * @returns 如果输入是数字则返回带px单位的字符串，否则返回原字符串
   */
  unit(val: number | string): string;
}

export const unit: (val: number | string) => string;

export default Utils;