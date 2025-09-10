/**
 * ColumnManager
 * (c) 2021 lincong1987
 */

export interface Column {
  freeze?: boolean | 'left' | 'right';
  children?: Column[];
  rowSpan?: number;
  colSpan?: number;
  [key: string]: any;
}

export function toRaw<T>(obj: T): T;

export default class ColumnManager {
  constructor(columns: Column[]);
  
  columns: Column[];
  _cached: { [key: string]: any };
  
  isAnyColumnsFreeze(): boolean;
  isAnyColumnsLeftFreeze(): boolean;
  isAnyColumnsRightFreeze(): boolean;
  leftColumns(): Column[];
  rightColumns(): Column[];
  leafColumns(): Column[];
  leftLeafColumns(): Column[];
  rightLeafColumns(): Column[];
  groupedColumns(): Column[];
  reset(columns: Column[]): void;
  _cache(name: string, fn: () => any): any;
  _leafColumns(columns: Column[]): Column[];
}