/**
 * layout
 * (c) 2024 lincong1987
 */

export interface LayoutProps {
  /** 宽度 */
  width?: string | number;

  /** 高度 */
  height?: string | number;

  /** 宽度 缩写 */
  w?: string | number;

  /** 高度 缩写 */
  h?: string | number;

  /** 最大高度 */
  maxHeight?: string | number;

  /** 最小高度 */
  minHeight?: string | number;

  /** 最大宽度 */
  maxWidth?: string | number;

  /** 最小宽度 */
  minWidth?: string | number;

  /** 最大高度 缩写 */
  hMax?: string | number;

  /** 最小高度 缩写 */
  hMin?: string | number;

  /** 最大宽度 缩写 */
  wMax?: string | number;

  /** 最小宽度 缩写 */
  wMin?: string | number;
}

export interface LayoutStyle {
  (): {
    width?: string;
    height?: string;
    maxWidth?: string;
    minWidth?: string;
    maxHeight?: string;
    minHeight?: string;
  };
}

export const props: LayoutProps;

export const style: LayoutStyle;

export default {
  props,
  style,
};