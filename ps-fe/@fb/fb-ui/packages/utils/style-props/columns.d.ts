/**
 * columns
 * (c) 2024 lincong1987
 */

export interface ColumnsProps {
  [key: string]: any;
}

export interface ColumnsStyle {
  (): {
    [key: string]: any;
  };
}

export const props: ColumnsProps;

export const style: ColumnsStyle;

export default {
  props,
  style
};