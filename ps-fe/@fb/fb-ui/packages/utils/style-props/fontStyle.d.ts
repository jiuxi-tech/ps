/**
 * fontStyle
 * (c) 2024 lincong1987
 */

export interface FontStyleProps {
  [key: string]: any;
}

export interface FontStyleStyle {
  (): {
    [key: string]: any;
  };
}

export const props: FontStyleProps;

export const style: FontStyleStyle;

export default {
  props,
  style
};