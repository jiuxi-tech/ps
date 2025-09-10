/**
 * indent
 * (c) 2024 lincong1987
 */

export interface IndentProps {
  [key: string]: any;
}

export interface IndentStyle {
  (): {
    [key: string]: any;
  };
}

export const props: IndentProps;

export const style: IndentStyle;

export default {
  props,
  style
};