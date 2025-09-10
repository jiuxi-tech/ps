/**
 * textShadow
 * (c) 2024 lincong1987
 */

export interface TextShadowProps {
  [key: string]: any;
}

export interface TextShadowStyle {
  (): {
    [key: string]: any;
  };
}

export const props: TextShadowProps;

export const style: TextShadowStyle;

export default {
  props,
  style
};