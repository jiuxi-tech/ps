/**
 * 导出启动器
 */

import Application from './boot/Application'
import Generator from './boot/Generator'
import Page from './boot/Page'
import Component from './boot/Component'
import jQuery from 'jquery'
import dayjs from "./util/dayjs"

import { closest, find } from './util/component'
import routerTool from './util/routerTool'
import serviceTool from './util/serviceTool'
import storeTool from './util/storeTool'
import viewTool from './util/viewTool'
import Datax from './api/datax'


const utils = {
	closest,
	find,
}

const fbCore = {
	Application,
	Page,
	Component,
	jQuery,
	dayjs,
	utils,
	routerTool,
	serviceTool,
	storeTool,
	viewTool,
	Datax,
	Generator,
}

export {
	Application,
	Page,
	Component,
	jQuery,
	dayjs,
	utils,
	routerTool,
	serviceTool,
	storeTool,
	viewTool,
	Datax,
	Generator,
}

export default fbCore
