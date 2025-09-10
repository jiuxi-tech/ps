import { DefineComponent } from 'vue';

interface FbTableBodyWrapperProps {
  columns?: any[];
  freeze?: boolean | string;
  [key: string]: any;
}

interface FbTableBodyWrapperMethods {
  [key: string]: any;
}

interface FbTableBodyWrapperComputed {
  [key: string]: any;
}

interface FbTableBodyWrapperData {
  cls: string;
  prefix: string;
  [key: string]: any;
}

type FbTableBodyWrapperComponent = DefineComponent<
  FbTableBodyWrapperProps,
  FbTableBodyWrapperData,
  FbTableBodyWrapperComputed,
  {},
  FbTableBodyWrapperMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbTableBodyWrapper: FbTableBodyWrapperComponent;

export default FbTableBodyWrapper;