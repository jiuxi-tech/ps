/**
 * placeItems
 * (c) 2024 lincong1987
 */

export interface PlaceItemsProps {
  [key: string]: any;
}

export interface PlaceItemsStyle {
  (): {
    [key: string]: any;
  };
}

export const props: PlaceItemsProps;

export const style: PlaceItemsStyle;

export default {
  props,
  style
};