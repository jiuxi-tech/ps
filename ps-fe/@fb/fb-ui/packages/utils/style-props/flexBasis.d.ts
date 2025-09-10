/**
 * flexBasis
 * (c) 2024 lincong1987
 */

export interface FlexBasisProps {
  [key: string]: any;
}

export interface FlexBasisStyle {
  (): {
    [key: string]: any;
  };
}

export const props: FlexBasisProps;

export const style: FlexBasisStyle;

export default {
  props,
  style
};