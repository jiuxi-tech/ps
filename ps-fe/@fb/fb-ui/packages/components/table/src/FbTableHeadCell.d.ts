import { DefineComponent } from 'vue';

interface FbTableHeadCellProps {
  columns?: any[];
  freeze?: boolean | string;
}

interface FbTableHeadCellMethods {
  getStyle(col: any): any;
}

interface FbTableHeadCellComputed {
  getClass: any[];
}

interface FbTableHeadCellData {
  showHeader: boolean;
  cls: string;
  prefix: string;
}

type FbTableHeadCellComponent = DefineComponent<
  FbTableHeadCellProps,
  FbTableHeadCellData,
  FbTableHeadCellComputed,
  {},
  FbTableHeadCellMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbTableHeadCell: FbTableHeadCellComponent;

export default FbTableHeadCell;