import { DefineComponent } from 'vue';

interface FbTableRowProps {
  freeze?: boolean | string;
  columns?: any[];
  row?: any;
  rowIndex?: number;
}

interface FbTableRowMethods {
  [key: string]: any;
}

interface FbTableRowComputed {
  [key: string]: any;
}

interface FbTableRowData {
  cls: string;
  prefix: string;
  [key: string]: any;
}

type FbTableRowComponent = DefineComponent<
  FbTableRowProps,
  FbTableRowData,
  FbTableRowComputed,
  {},
  FbTableRowMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbTableRow: FbTableRowComponent;

export default FbTableRow;