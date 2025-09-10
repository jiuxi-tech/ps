/**
 * placeContent
 * (c) 2024 lincong1987
 */

export interface PlaceContentProps {
  /**
   * Positional alignment
   * align-content does not take left and right values
   * place-content: center start;
   * place-content: start center;
   * place-content: end left;
   * place-content: flex-start center;
   * place-content: flex-end center;
   *
   * Baseline alignment
   * justify-content does not take baseline values
   * place-content: baseline center;
   * place-content: first baseline space-evenly;
   * place-content: last baseline right;
   *
   * Distributed alignment
   * place-content: space-between space-evenly;
   * place-content: space-around space-evenly;
   * place-content: space-evenly stretch;
   * place-content: stretch space-evenly;
   *
   * Global values
   * place-content: inherit;
   * place-content: initial;
   * place-content: unset;
   *
   * @see https://developer.mozilla.org/zh-CN/docs/Web/CSS/place-content
   */
  placeContent?: string;
}

export interface PlaceContentStyle {
  (): {
    [key: string]: any;
  };
}

export const props: PlaceContentProps;

export const style: PlaceContentStyle;

export default {
  props,
  style,
};