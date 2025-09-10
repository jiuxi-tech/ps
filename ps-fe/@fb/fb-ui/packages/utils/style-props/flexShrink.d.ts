/**
 * flexShrink
 * (c) 2024 lincong1987
 */

export interface FlexShrinkProps {
  [key: string]: any;
}

export interface FlexShrinkStyle {
  (): {
    [key: string]: any;
  };
}

export const props: FlexShrinkProps;

export const style: FlexShrinkStyle;

export default {
  props,
  style
};