/**
 * ellipsis
 * (c) 2024 lincong1987
 */

export interface EllipsisProps {
  [key: string]: any;
}

export interface EllipsisStyle {
  (): {
    [key: string]: any;
  };
}

export const props: EllipsisProps;

export const style: EllipsisStyle;

export default {
  props,
  style
};