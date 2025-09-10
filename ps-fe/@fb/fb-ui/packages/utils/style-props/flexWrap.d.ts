/**
 * flexWrap
 * (c) 2024 lincong1987
 */

export interface FlexWrapProps {
  [key: string]: any;
}

export interface FlexWrapStyle {
  (): {
    [key: string]: any;
  };
}

export const props: FlexWrapProps;

export const style: FlexWrapStyle;

export default {
  props,
  style
};