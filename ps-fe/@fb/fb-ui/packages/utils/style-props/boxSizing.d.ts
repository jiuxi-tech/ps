/**
 * boxSizing
 * (c) 2024 lincong1987
 */

export interface BoxSizingProps {
  [key: string]: any;
}

export interface BoxSizingStyle {
  (): {
    [key: string]: any;
  };
}

export const props: BoxSizingProps;

export const style: BoxSizingStyle;

export default {
  props,
  style
};