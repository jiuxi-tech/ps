/**
 * gridTemplate
 * (c) 2024 lincong1987
 */

export interface GridTemplateProps {
  [key: string]: any;
}

export interface GridTemplateStyle {
  (): {
    [key: string]: any;
  };
}

export const props: GridTemplateProps;

export const style: GridTemplateStyle;

export default {
  props,
  style
};