/**
 * gridColumn
 * (c) 2024 lincong1987
 */

export interface GridColumnProps {
  [key: string]: any;
}

export interface GridColumnStyle {
  (): {
    [key: string]: any;
  };
}

export const props: GridColumnProps;

export const style: GridColumnStyle;

export default {
  props,
  style
};