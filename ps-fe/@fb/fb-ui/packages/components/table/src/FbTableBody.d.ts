import { DefineComponent } from 'vue';

interface FbTableBodyProps {
  columns?: any[];
  freeze?: boolean | string;
}

interface FbTableBodyMethods {
  handleRowHover(isHover: boolean, rowIndex: number): void;
  renderRows(h: any, myData: any[], indent: number, ancestorKeys?: any[]): any[];
  renderSubRows(): void;
}

interface FbTableBodyComputed {
  [key: string]: any;
}

interface FbTableBodyData {
  cls: string;
  prefix: string;
}

type FbTableBodyComponent = DefineComponent<
  FbTableBodyProps,
  FbTableBodyData,
  FbTableBodyComputed,
  {},
  FbTableBodyMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbTableBody: FbTableBodyComponent;

export default FbTableBody;