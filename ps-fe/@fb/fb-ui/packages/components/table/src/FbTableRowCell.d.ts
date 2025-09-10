import { DefineComponent } from 'vue';

interface FbTableRowCellProps {
  freeze?: boolean | string;
  column?: any;
  row?: any;
  rowIndex?: number;
}

interface FbTableRowCellMethods {
  [key: string]: any;
}

interface FbTableRowCellComputed {
  [key: string]: any;
}

interface FbTableRowCellData {
  cls: string;
  prefix: string;
  [key: string]: any;
}

type FbTableRowCellComponent = DefineComponent<
  FbTableRowCellProps,
  FbTableRowCellData,
  FbTableRowCellComputed,
  {},
  FbTableRowCellMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbTableRowCell: FbTableRowCellComponent;

export default FbTableRowCell;