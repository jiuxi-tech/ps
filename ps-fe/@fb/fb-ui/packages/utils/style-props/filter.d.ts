/**
 * filter
 * (c) 2024 lincong1987
 */

export interface FilterProps {
  noBlur?: boolean;

  blur?: string | number;

  blurXs?: boolean;

  blurS?: boolean;

  blurM?: boolean;

  blurL?: boolean;

  blurXl?: boolean;

  blurXxl?: boolean;

  blurXxxl?: boolean;

  blurXxxxl?: boolean;

  dropShadow?: boolean | string;

  noDropShadow?: boolean;

  dropShadowXs?: boolean;

  dropShadowS?: boolean;

  dropShadowM?: boolean;

  dropShadowL?: boolean;

  dropShadowXl?: boolean;

  dropShadowXxl?: boolean;
}

export interface BlurMap {
  blurXs: string;
  blurS: string;
  blurM: string;
  blurL: string;
  blurXl: string;
  blurXxl: string;
  blurXxxl: string;
  blurXxxxl: string;
}

export interface DropShadowMap {
  dropShadowXs: string;
  dropShadowS: string;
  dropShadowM: string;
  dropShadowL: string;
  dropShadowXl: string;
  dropShadowXxl: string;
}

export interface FilterStyle {
  (): {
    [key: string]: any;
  };
}

export const props: FilterProps;

export const blurMap: BlurMap;

export const dropShadowMap: DropShadowMap;

export const style: FilterStyle;

export default {
  props,
  style,
  dropShadowMap,
  blurMap,
};