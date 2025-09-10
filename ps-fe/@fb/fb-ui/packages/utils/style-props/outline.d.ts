/**
 * outline
 * (c) 2024 lincong1987
 */

export interface OutlineProps {
  [key: string]: any;
}

export interface OutlineStyle {
  (): {
    [key: string]: any;
  };
}

export const props: OutlineProps;

export const style: OutlineStyle;

export default {
  props,
  style
};