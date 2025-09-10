/**
 * visibility
 * (c) 2024 lincong1987
 */

export interface VisibilityProps {
  [key: string]: any;
}

export interface VisibilityStyle {
  (): {
    [key: string]: any;
  };
}

export const props: VisibilityProps;

export const style: VisibilityStyle;

export default {
  props,
  style
};