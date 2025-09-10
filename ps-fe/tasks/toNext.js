/*!
 * toNext
 * (c) 2025 lincong1987
 */

// 把 public 下所有html文件中的 <%= BASE_URL %> 替换为 <%= assetPrefix %>
const fs = require('fs')
const path = require('path')

const publicDir = path.join(__dirname, 'public')

// Function to replace content in a file
function replaceInFile (filePath, oldStr, newStr) {
	let content = fs.readFileSync(filePath, 'utf8')
	content = content.replace(new RegExp(oldStr, 'g'), newStr)
	fs.writeFileSync(filePath, content, 'utf8')
}

// Traverse the public directory
fs.readdirSync(publicDir).forEach(file => {
	if (file.endsWith('.html')) {
		const filePath = path.join(publicDir, file)
		replaceInFile(filePath, '<%= BASE_URL %>', '<%= assetPrefix %>')
	}
})


/* 写一段代码， 根目录下新建 rsbuild.config.js 文件， 内容为

*  ` import {RsDefineConfig} from 'fb-core/src/util/RsConfig'
 import pkg from './package.json'
 // 不同项目配置文件名称和位置不同
 import projectConfig from './project.config.js'
 export default RsDefineConfig(__dirname, pkg, projectConfig)
* `

其中 如果根目录下有project.config.js文件则projectConfig来自project.config.js
如果如果根目录下有fb.config.js文件则projectConfig来自fb.config.js
* */

const rsbuildConfigPath = path.join(__dirname, 'rsbuild.config.js');

// Determine which config file to use
let projectConfigPath = '';
if (fs.existsSync(path.join(__dirname, 'project.config.js'))) {
	projectConfigPath = './project.config.js';
} else if (fs.existsSync(path.join(__dirname, 'fb.config.js'))) {
	projectConfigPath = './fb.config.js';
} else {
	throw new Error('No valid config file found (project.config.js or fb.config.js)');
}

const rsbuildConfigContent = `
import { RsDefineConfig } from './@fb/fb-core/src/util/RsConfig';
import pkg from './package.json';
// 不同项目配置文件名称和位置不同
import projectConfig from '${projectConfigPath}';

export default RsDefineConfig(__dirname, pkg, projectConfig);
`;

fs.writeFileSync(rsbuildConfigPath, rsbuildConfigContent.trim(), 'utf8');


//
//写一段代码，
//把 package.json 中的 npm scripts 更新为 Rsbuild 的 CLI 命令。
//请参考以下的内容进行处理，如果scripts已经存在，则在旧scripts的命令名称的前面添加--，最后合并scripts
//
//
//		  "serve": "rsbuild dev",
//		  "build": "rsbuild build",
//		  "serve-dev": "rsbuild dev --env-mode dev",
//		  "serve-local": "rsbuild dev",
//		  "preview": "rsbuild preview"
//


// 读取 package.json 文件
const packageJsonPath = path.join(__dirname, 'package.json');
const packageJson = require(packageJsonPath);

// 新的 Rsbuild CLI 命令
const newScripts = {
	"serve": "rsbuild dev",
	"build": "rsbuild build",
	"serve-dev": "rsbuild dev --env-mode dev",
	"serve-local": "rsbuild dev",
	"preview": "rsbuild preview"
};

// 如果 scripts 已经存在，则处理旧 scripts
if (packageJson.scripts) {
	for (const [key, value] of Object.entries(packageJson.scripts)) {
		// 如果 newScripts 中存在同名脚本，则在旧脚本名称前添加 --
		if (newScripts[key]) {
			packageJson.scripts[`--${key}`] = value;
		}
		// 如果 newScripts 中不存在同名脚本，则保持不变
	}
}

// 合并新的 Rsbuild CLI 命令
packageJson.scripts = { ...packageJson.scripts, ...newScripts };

// 写回 package.json 文件
fs.writeFileSync(packageJsonPath, JSON.stringify(packageJson, null, 2));

console.log('package.json scripts 已更新');


//写一段代码，执行 npm i @rsbuild/core @rsbuild/plugin-babel @rsbuild/plugin-less @rsbuild/plugin-node-polyfill @rsbuild/plugin-vue2 rsbuild-plugin-vue-legacy webpack@5 -D


const { exec } = require('child_process');

// 要安装的依赖包列表
const dependencies = [
	'@rsbuild/core',
	'@rsbuild/plugin-babel',
	'@rsbuild/plugin-less',
	'@rsbuild/plugin-node-polyfill',
	'@rsbuild/plugin-vue2',
	'rsbuild-plugin-vue-legacy',
	'webpack@5'
];

// 构造 npm install 命令
const command = `npm install ${dependencies.join(' ')} -D && fb pull`;

// 执行命令
exec(command, (error, stdout, stderr) => {
	if (error) {
		console.error(`执行出错: ${error.message}`);
		return;
	}
	if (stderr) {
		console.error(`stderr: ${stderr}`);
		return;
	}
	console.log(`stdout: ${stdout}`);
});
