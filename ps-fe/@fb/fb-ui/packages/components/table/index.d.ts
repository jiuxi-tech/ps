/**
 * index
 * (c) 2020 lincong1987
 */

import { PluginFunction } from 'vue';
import FbSimpleTable from './src/FbSimpleTable';

export const install: (adapter: any) => void;

export default {
  install: PluginFunction<any>;
  FbSimpleTable: typeof FbSimpleTable;
};