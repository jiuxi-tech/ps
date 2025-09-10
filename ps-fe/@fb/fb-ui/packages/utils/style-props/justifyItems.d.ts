/**
 * justifyItems
 * (c) 2024 lincong1987
 */

export interface JustifyItemsProps {
  [key: string]: any;
}

export interface JustifyItemsStyle {
  (): {
    [key: string]: any;
  };
}

export const props: JustifyItemsProps;

export const style: JustifyItemsStyle;

export default {
  props,
  style
};