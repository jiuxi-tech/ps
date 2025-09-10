/**
 * mixBlend
 * (c) 2024 lincong1987
 */

export interface MixBlendProps {
  [key: string]: any;
}

export interface MixBlendStyle {
  (): {
    [key: string]: any;
  };
}

export const props: MixBlendProps;

export const style: MixBlendStyle;

export default {
  props,
  style
};