import { DefineComponent } from 'vue';

interface FbTableWrapperProps {
  [key: string]: any;
}

interface FbTableWrapperMethods {
  [key: string]: any;
}

interface FbTableWrapperComputed {
  [key: string]: any;
}

interface FbTableWrapperData {
  cls: string;
  prefix: string;
  [key: string]: any;
}

type FbTableWrapperComponent = DefineComponent<
  FbTableWrapperProps,
  FbTableWrapperData,
  FbTableWrapperComputed,
  {},
  FbTableWrapperMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbTableWrapper: FbTableWrapperComponent;

export default FbTableWrapper;