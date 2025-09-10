import { DefineComponent } from 'vue';

interface FbTableHeadRowProps {
  index?: number;
  row?: any[];
  rows?: any[][];
  freeze?: boolean | string;
  columns?: any[];
}

interface FbTableHeadRowMethods {
  [key: string]: any;
}

interface FbTableHeadRowComputed {
  [key: string]: any;
}

interface FbTableHeadRowData {
  cls: string;
  prefix: string;
}

type FbTableHeadRowComponent = DefineComponent<
  FbTableHeadRowProps,
  FbTableHeadRowData,
  FbTableHeadRowComputed,
  {},
  FbTableHeadRowMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbTableHeadRow: FbTableHeadRowComponent;

export default FbTableHeadRow;