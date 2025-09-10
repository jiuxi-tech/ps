/**
 * column
 * (c) 2024 lincong1987
 */

export interface ColumnProps {
  [key: string]: any;
}

export interface ColumnStyle {
  (): {
    [key: string]: any;
  };
}

export const props: ColumnProps;

export const style: ColumnStyle;

export default {
  props,
  style
};