/**
 * grid
 * (c) 2024 lincong1987
 */

export interface GridProps {
  [key: string]: any;
}

export interface GridStyle {
  (): {
    [key: string]: any;
  };
}

export const props: GridProps;

export const style: GridStyle;

export default {
  props,
  style
};