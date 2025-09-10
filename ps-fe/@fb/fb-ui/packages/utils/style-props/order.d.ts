/**
 * order
 * (c) 2024 lincong1987
 */

export interface OrderProps {
  [key: string]: any;
}

export interface OrderStyle {
  (): {
    [key: string]: any;
  };
}

export const props: OrderProps;

export const style: OrderStyle;

export default {
  props,
  style
};