/**
 * flexDirection
 * (c) 2024 lincong1987
 */

export interface FlexDirectionProps {
  [key: string]: any;
}

export interface FlexDirectionStyle {
  (): {
    [key: string]: any;
  };
}

export const props: FlexDirectionProps;

export const style: FlexDirectionStyle;

export default {
  props,
  style
};