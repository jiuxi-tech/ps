/**
 * Application.js
 * (c) 2020 lincong1987
 */

import { Generator } from './Generator';
import Datax from '../api/datax';
import { Manager } from '../storage';

interface ApplicationConfig {
  appName?: string;
  autoMount?: boolean;
  mixins?: any[];
  use?: any;
  plugins?: any;
  warnHandler?: Function;
  errorHandler?: Function;
  projectConfig?: any;
  render?: Function;
  generator?: Function;
}

interface Plugins {
  [key: string]: any;
}

interface ProjectConfig {
  [key: string]: any;
}

export default class Application {
  static config(opts: any): void;
  
  constructor(applicationConfig: ApplicationConfig);
  
  applicationConfig: ApplicationConfig;
  plugins: Plugins;
  projectConfig: ProjectConfig;
  app: any;
  Vue: any;
  Vuex: any;
  VueRouter: any;
  datax: Datax;
  storage: Manager;
  eventbus: any;
  
  addUtils(): void;
  createEventbus(): void;
  installService(): void;
  installFilters(): void;
  installDirectives(): void;
  installComponents(): void;
  createStorage(): Manager;
  createDatax(): Datax;
  createRoot(): any;
  mount(el?: string | Element): any;
  run(): void;
}