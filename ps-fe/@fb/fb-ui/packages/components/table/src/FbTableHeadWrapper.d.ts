import { DefineComponent } from 'vue';

interface FbTableHeadWrapperProps {
  columns?: any[];
  freeze?: boolean | string;
  [key: string]: any;
}

interface FbTableHeadWrapperMethods {
  [key: string]: any;
}

interface FbTableHeadWrapperComputed {
  [key: string]: any;
}

interface FbTableHeadWrapperData {
  cls: string;
  prefix: string;
  [key: string]: any;
}

type FbTableHeadWrapperComponent = DefineComponent<
  FbTableHeadWrapperProps,
  FbTableHeadWrapperData,
  FbTableHeadWrapperComputed,
  {},
  FbTableHeadWrapperMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbTableHeadWrapper: FbTableHeadWrapperComponent;

export default FbTableHeadWrapper;