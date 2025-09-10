/**
 * fontVariant
 * (c) 2024 lincong1987
 */

export interface FontVariantProps {
  [key: string]: any;
}

export interface FontVariantStyle {
  (): {
    [key: string]: any;
  };
}

export const props: FontVariantProps;

export const style: FontVariantStyle;

export default {
  props,
  style
};