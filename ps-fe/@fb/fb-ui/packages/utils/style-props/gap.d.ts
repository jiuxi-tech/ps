/**
 * gap
 * (c) 2024 lincong1987
 */

export interface GapProps {
  [key: string]: any;
}

export interface GapStyle {
  (): {
    [key: string]: any;
  };
}

export const props: GapProps;

export const style: GapStyle;

export default {
  props,
  style
};