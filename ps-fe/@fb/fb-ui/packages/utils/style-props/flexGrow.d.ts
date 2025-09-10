/**
 * flexGrow
 * (c) 2024 lincong1987
 */

export interface FlexGrowProps {
  [key: string]: any;
}

export interface FlexGrowStyle {
  (): {
    [key: string]: any;
  };
}

export const props: FlexGrowProps;

export const style: FlexGrowStyle;

export default {
  props,
  style
};