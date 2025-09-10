/**
 * dropShadow
 * (c) 2024 lincong1987
 */

export interface DropShadowProps {
  [key: string]: any;
}

export interface DropShadowStyle {
  (): {
    [key: string]: any;
  };
}

export const props: DropShadowProps;

export const style: DropShadowStyle;

export default {
  props,
  style
};