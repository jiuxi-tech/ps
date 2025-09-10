/**
 * utils
 * (c) 2021 lincong1987
 */

export interface Column {
  rowSpan?: number;
  colSpan?: number;
  label?: string;
  className?: string;
  class?: string;
  type?: string;
  width?: number | string;
  freeze?: boolean | 'left' | 'right';
  children?: Column[];
  [key: string]: any;
}

export interface RenderHeaderRowsOptions {
  columns?: Column[];
  currentRow?: number;
  rows?: any[];
  isLast?: boolean;
}

export const INTERNAL_COL_DEFINE: string;

export function renderHeaderRows(options: RenderHeaderRowsOptions): any[];

export function classNames(...args: any[]): string;

export function indexOf(arr: any[], obj: any): number;

export function measureScrollbar(options: { direction?: 'vertical' | 'horizontal'; prefix?: string }): number;

export function debounce<T extends (...args: any[]) => any>(func: T, wait: number, immediate?: boolean): T & { cancel: () => void };

export function remove<T>(array: T[], item: T): T[];