import { DefineComponent } from 'vue';

interface FbTableHeadProps {
  columns?: any[];
  freeze?: boolean | string;
}

interface FbTableHeadMethods {
  [key: string]: any;
}

interface FbTableHeadComputed {
  [key: string]: any;
}

interface FbTableHeadData {
  showHeader: boolean;
  cls: string;
  prefix: string;
}

type FbTableHeadComponent = DefineComponent<
  FbTableHeadProps,
  FbTableHeadData,
  FbTableHeadComputed,
  {},
  FbTableHeadMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbTableHead: FbTableHeadComponent;

export default FbTableHead;