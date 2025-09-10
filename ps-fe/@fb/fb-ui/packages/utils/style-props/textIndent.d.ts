/**
 * textIndent
 * (c) 2024 lincong1987
 */

export interface TextIndentProps {
  [key: string]: any;
}

export interface TextIndentStyle {
  (): {
    [key: string]: any;
  };
}

export const props: TextIndentProps;

export const style: TextIndentStyle;

export default {
  props,
  style
};