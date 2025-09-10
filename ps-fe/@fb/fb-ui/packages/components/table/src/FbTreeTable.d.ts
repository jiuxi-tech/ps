import { DefineComponent } from 'vue';

interface FbTreeTableProps {
  [key: string]: any;
}

interface FbTreeTableMethods {
  [key: string]: any;
}

interface FbTreeTableComputed {
  [key: string]: any;
}

interface FbTreeTableData {
  cls: string;
  prefix: string;
  [key: string]: any;
}

type FbTreeTableComponent = DefineComponent<
  FbTreeTableProps,
  FbTreeTableData,
  FbTreeTableComputed,
  {},
  FbTreeTableMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbTreeTable: FbTreeTableComponent;

export default FbTreeTable;