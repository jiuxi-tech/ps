/**
 * 导出启动器
 */

import Application from './boot/Application';
import Generator from './boot/Generator';
import Page from './boot/Page';
import Component from './boot/Component';
import jQuery from 'jquery';
import dayjs from 'dayjs';

interface Utils {
  closest: Function;
  find: Function;
}

interface FbCore {
  Application: typeof Application;
  Page: typeof Page;
  Component: typeof Component;
  jQuery: typeof jQuery;
  dayjs: typeof dayjs;
  utils: Utils;
  routerTool: any;
  serviceTool: any;
  storeTool: any;
  viewTool: any;
  Datax: any;
  Generator: typeof Generator;
}

declare const fbCore: FbCore;

export {
  Application,
  Page,
  Component,
  jQuery,
  dayjs,
  Utils,
  FbCore,
  Generator,
};

export default fbCore;