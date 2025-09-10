import { DefineComponent } from 'vue';

interface FbTableCellProps {
  columns?: any[];
  freeze?: boolean | string;
}

interface FbTableCellMethods {
  getStyle(col: any): any;
}

interface FbTableCellComputed {
  getClass: any[];
}

interface FbTableCellData {
  prefix: string;
}

type FbTableCellComponent = DefineComponent<
  FbTableCellProps,
  FbTableCellData,
  FbTableCellComputed,
  {},
  FbTableCellMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbTableCell: FbTableCellComponent;

export default FbTableCell;