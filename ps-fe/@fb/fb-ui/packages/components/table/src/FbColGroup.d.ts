import { DefineComponent } from 'vue';

interface FbColGroupProps {
  columns?: any[];
  freeze?: boolean | string;
}

interface FbColGroupMethods {
  [key: string]: any;
}

interface FbColGroupComputed {
  [key: string]: any;
}

interface FbColGroupData {
  [key: string]: any;
}

type FbColGroupComponent = DefineComponent<
  FbColGroupProps,
  FbColGroupData,
  FbColGroupComputed,
  {},
  FbColGroupMethods,
  {},
  {},
  {},
  'transition'
>;

declare const FbColGroup: FbColGroupComponent;

export default FbColGroup;