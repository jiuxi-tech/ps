/**
 * backdropFilter
 * (c) 2024 lincong1987
 */

export interface BackdropFilterProps {
  [key: string]: any;
}

export interface BackdropFilterStyle {
  (): {
    [key: string]: any;
  };
}

export const props: BackdropFilterProps;

export const style: BackdropFilterStyle;

export default {
  props,
  style
};