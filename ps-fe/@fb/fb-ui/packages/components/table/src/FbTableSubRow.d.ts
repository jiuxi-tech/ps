import { DefineComponent } from 'vue';

interface FbTableSubRowProps {
  freeze?: boolean | string;
  columns?: any[];
  row?: any;
  rowIndex?: number;
}

interface FbTableSubRowMethods {
  [key: string]: any;
}

interface FbTableSubRowComputed {
  [key: string]: any;
}

interface FbTableSubRowData {
  cls: string;
  prefix: string;
  [key: string]: any;
}

type FbTableSubRowComponent = DefineComponent<
  FbTableSubRowProps,
  FbTableSubRowData,
  FbTableSubRowComputed,
  {},
  FbTableSubRowMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbTableSubRow: FbTableSubRowComponent;

export default FbTableSubRow;