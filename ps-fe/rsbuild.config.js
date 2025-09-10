import { RsDefineConfig } from './@fb/fb-core/src/util/RsConfig';
import pkg from './package.json';
// 不同项目配置文件名称和位置不同
import projectConfig from './project.config.js';

export default RsDefineConfig(__dirname, pkg, projectConfig);