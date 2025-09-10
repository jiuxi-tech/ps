/**
 * cursor
 * (c) 2024 lincong1987
 */

export interface CursorProps {
  [key: string]: any;
}

export interface CursorStyle {
  (): {
    [key: string]: any;
  };
}

export const props: CursorProps;

export const style: CursorStyle;

export default {
  props,
  style
};