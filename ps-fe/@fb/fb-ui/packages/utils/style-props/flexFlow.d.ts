/**
 * flexFlow
 * (c) 2024 lincong1987
 */

export interface FlexFlowProps {
  [key: string]: any;
}

export interface FlexFlowStyle {
  (): {
    [key: string]: any;
  };
}

export const props: FlexFlowProps;

export const style: FlexFlowStyle;

export default {
  props,
  style
};