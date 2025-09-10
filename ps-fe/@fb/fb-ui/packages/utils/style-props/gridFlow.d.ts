/**
 * gridFlow
 * (c) 2024 lincong1987
 */

export interface GridFlowProps {
  [key: string]: any;
}

export interface GridFlowStyle {
  (): {
    [key: string]: any;
  };
}

export const props: GridFlowProps;

export const style: GridFlowStyle;

export default {
  props,
  style
};