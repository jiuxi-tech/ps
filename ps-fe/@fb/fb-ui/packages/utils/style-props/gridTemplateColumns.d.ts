/**
 * gridTemplateColumns
 * (c) 2024 lincong1987
 */

export interface GridTemplateColumnsProps {
  [key: string]: any;
}

export interface GridTemplateColumnsStyle {
  (): {
    [key: string]: any;
  };
}

export const props: GridTemplateColumnsProps;

export const style: GridTemplateColumnsStyle;

export default {
  props,
  style
};