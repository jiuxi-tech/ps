/**
 * inline
 * (c) 2024 lincong1987
 */

export interface InlineProps {
  [key: string]: any;
}

export interface InlineStyle {
  (): {
    [key: string]: any;
  };
}

export const props: InlineProps;

export const style: InlineStyle;

export default {
  props,
  style
};