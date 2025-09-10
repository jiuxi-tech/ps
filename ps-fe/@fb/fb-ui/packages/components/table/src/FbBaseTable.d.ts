import { DefineComponent } from 'vue';

interface FbBaseTableProps {
  freeze?: boolean | string;
  columns?: any[];
  store?: object;
  hasSubRow?: boolean;
  isAnyColumnsFreeze?: boolean;
  hasHead?: boolean;
  hasBody?: boolean;
}

interface FbBaseTableMethods {
  getColumns(cols?: any[]): any[];
}

interface FbBaseTableComputed {
  [key: string]: any;
}

interface FbBaseTableData {
  cls: string;
}

type FbBaseTableComponent = DefineComponent<
  FbBaseTableProps,
  FbBaseTableData,
  FbBaseTableComputed,
  {},
  FbBaseTableMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbBaseTable: FbBaseTableComponent;

export default FbBaseTable;