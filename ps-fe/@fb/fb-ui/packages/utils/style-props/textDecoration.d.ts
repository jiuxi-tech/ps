/**
 * textDecoration
 * (c) 2024 lincong1987
 */

export interface TextDecorationProps {
  [key: string]: any;
}

export interface TextDecorationStyle {
  (): {
    [key: string]: any;
  };
}

export const props: TextDecorationProps;

export const style: TextDecorationStyle;

export default {
  props,
  style
};