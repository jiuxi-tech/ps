/**
 * gridRow
 * (c) 2024 lincong1987
 */

export interface GridRowProps {
  [key: string]: any;
}

export interface GridRowStyle {
  (): {
    [key: string]: any;
  };
}

export const props: GridRowProps;

export const style: GridRowStyle;

export default {
  props,
  style
};