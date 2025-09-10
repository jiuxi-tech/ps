/**
 * flexAlign
 * (c) 2024 lincong1987
 */

export interface FlexAlignProps {
  [key: string]: any;
}

export interface FlexAlignStyle {
  (): {
    [key: string]: any;
  };
}

export const props: FlexAlignProps;

export const style: FlexAlignStyle;

export default {
  props,
  style
};