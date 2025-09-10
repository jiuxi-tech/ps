/*!
 * types
 * (c) 2021 lincong1987
 */

import { find } from 'lodash-es'

const fileTypes = {
	// key: [type, desc, icon]
	'*': ['unknown', '全部类型', 'unknown', '*'],

	'': ['unknown', '未定义的文件类型', 'unknown', '*'],

	'unknown': ['unknown', '未定义的文件类型', 'unknown', '*'],

	'7z': ['rar', '7-Zip 压缩文件', 'rar', 'application/octet-stream'],
	zip: ['rar', 'Zip 压缩文件', 'rar', 'application/zip'],
	rar: ['rar', 'Roshal Archive 压缩文件', 'rar', 'application/octet-stream'],
	gz: ['rar', 'tar.gz 压缩文件', 'rar', 'application/x-gzip'],
	war: [
		'rar',
		'Web Application Archive 压缩文件',
		'rar',
		'application/octet-stream',
	],
	ear: [
		'rar',
		'Enterprise Application Archive 压缩文件',
		'rar',
		'application/octet-stream',
	],

	docx: [
		'word',
		'MS OFFICE 2013 电子文档',
		'doc',
		'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
	],
	doc: ['word', 'MS OFFICE 电子文档', 'doc', 'application/msword'],
	rtf: ['word', 'MS OFFICE 电子文档', 'doc', 'text/rtf'],
	wps: ['wps', 'WPS 电子文档', 'wps', 'application/kswps'],
	wpt: ['wps', 'WPS 电子文档', 'wps', 'application/kswps'],

	xls: ['excel', 'MS OFFICE 2013 电子表格', 'xls', 'application/vnd.ms-excel'],
	xlsx: [
		'excel',
		'MS OFFICE 电子表格',
		'xls',
		'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
	],
	csv: ['csv', 'WPS 电子表格', 'xls', 'text/csv'],
	et: ['et', 'WPS 电子表格', 'et', 'application/kset'],
	ett: ['et', 'WPS 电子表格', 'et', 'application/kset'],

	pptx: [
		'ppt',
		'MS OFFICE 2013 演示文稿',
		'ppt',
		'application/vnd.openxmlformats-officedocument.presentationml.presentation',
	],
	ppt: ['ppt', 'MS OFFICE 演示文稿', 'ppt', 'application/vnd.ms-powerpoint'],
	pps: ['ppt', 'MS OFFICE 演示文稿', 'ppt', 'application/vnd.ms-powerpoint'],
	dps: ['dps', 'WPS 演示文稿', 'dps', 'application/ksdps'],
	dpt: ['dps', 'WPS 演示文稿', 'dps', 'application/ksdps'],

	msi: ['exe', 'windows 应用程序', 'exe', 'application/octet-stream'],
	exe: ['exe', 'windows 应用程序', 'exe', 'application/octet-stream'],
	bat: ['app', 'bat 脚本', 'command', 'application/octet-stream'],
	apk: ['app', 'apk 应用程序', 'exe', 'application/vnd.android.package-archive'],

	pdf: ['pdf', 'ADOBE 电子文稿', 'pdf', 'application/pdf'],

	js: ['js', 'JavaScript脚本', 'txt', 'text/javascript'],
	json: ['js', 'JavaScript脚本', 'txt', 'text/json'],
	geojson: ['js', '地理数据结构编码', 'txt', 'text/json'],
	code: ['js', 'JavaScript脚本', 'txt', 'text/plain'],

	txt: ['txt', '文本文档', 'txt', 'text/plain'],
	text: ['txt', '文本文档', 'txt', 'text/plain'],
	srt: ['txt', '字幕', 'txt', 'text/plain'],
	lrc: ['txt', '歌词', 'txt', 'text/plain'],

	html: ['txt', '网页', 'html', 'text/html'],
	htm: ['txt', '网页', 'html', 'text/html'],
	jsp: ['txt', 'JSP 网页', 'jsp', 'text/html'],
	php: ['txt', 'PHP 网页', 'php', 'text/html'],
	asp: ['txt', 'ASP 网页', 'asp', 'text/html'],
	aspx: ['txt', 'ASPX 网页', 'aspx', 'text/html'],
	ashx: ['txt', 'HttpHandler', 'html', 'text/html'],
	xml: ['txt', 'XML 可扩展标记语言', 'html', 'text/xml'],

	as: ['txt', 'ActionScript 脚本', 'txt', 'text/plain'],
	rb: ['txt', 'Ruby 脚本', 'txt', 'text/plain'],
	sh: ['txt', 'Shell 命令', 'txt', 'text/plain'],
	jar: [
		'txt',
		'Java Application Archive 归档文件',
		'rar',
		'application/octet-stream',
	],
	sql: ['txt', 'SQL 结构化查询语言', 'sql', 'text/plain'],
	log: ['txt', '日志', 'txt', 'text/plain'],

	css: ['css', '级联样式表', 'txt', 'text/css'],
	less: ['css', 'LESS 预处理语言', 'txt', 'text/css'],
	sass: ['css', 'SASS 预处理语言', 'txt', 'text/css'],
	scss: ['css', 'SCSS 预处理语言', 'txt', 'text/css'],

	wma: ['music', '微软音频格式', 'cd', 'audio/x-ms-wma'],
	mp3: ['music', '动态影像专家压缩标准音频层面3', 'mp3', 'audio/x-mpeg'],
	amr: ['music', 'amr 音乐格式', 'cd', 'audio/amr'],
	ogg: ['music', 'OGG Vorbis', 'cd', 'audio/aac', 'video/ogg'],
	ape: ['music', 'APE无损压缩音乐格式', 'cd', 'audio/octet-stream'],
	flac: ['music', 'FLAC无损音频压缩编码', 'cd', 'audio/x-flac'],
	wav: ['music', '微软开发的一种声音文件格式', 'cd', 'audio/wav'],
	aac: ['music', 'Advanced Audio Coding', 'cd', 'audio/aac'],

	mdb: ['bin', 'Microsoft Access 存储格式', 'mdb', 'application/x-msaccess'],
	ttf: ['bin', 'TrueTypeFont', 'ttf', 'font/ttf'],
	sfnt: ['bin', 'TrueTypeFont', 'sfnt', 'font/sfnt'],
	woff: ['bin', 'TrueTypeFont', 'woff', 'font/woff'],
	woff2: ['bin', 'TrueTypeFont', 'woff2', 'font/woff2'],

	jpg: ['image', 'JPG 图片', 'jpg', 'image/jpeg'],
	gif: ['image', 'GIF 动画', 'gif', 'image/gif'],
	jpeg: ['image', 'JPEG 图片', 'jpg', 'image/jpeg'],
	png: ['image', 'PNG图像', 'png', 'image/png'],
	bmp: ['image', 'Bitmap 图像', 'bmp', 'image/jpeg'],
	tif: ['image', '标签图像文件格式', 'jpg', 'image/tiff'],
	tiff: ['image', '标签图像文件格式', 'jpg', 'image/tiff'],

	swf: ['video', 'Flash动画文件', 'swf', 'application/x-shockwave-flash'],
	flv: ['video', 'FLASH VIDEO 流媒体', 'flv', 'flv-application/octet-stream'],
	fla: ['video', 'JavaScript脚本', 'fla', 'application/octet-stream'],
	mkv: ['video', 'Matroska多媒体容器', 'avi', 'video/x-matroska'],
	wmv: ['video', 'Windows Media Video 视频文件', 'wmv', 'video/x-ms-wmv'],
	mov: ['video', 'QuickTime影片格式', 'mov', 'video/quicktime'],
	mp4: ['video', 'MPEG-4标准视频', 'mp4', 'video/mp4'],
	mpeg: ['video', '动态图像专家组视频', 'avi', 'video/mpeg'],
	mpg: ['video', 'MPG视频', 'avi', 'video/mpeg'],
	mpg2: ['video', 'MPG2视频', 'avi', 'video/mpeg'],
	m3u8: ['video', '视频', 'avi', 'application/x-mpegURL'],
	rm: ['video', 'RealMedia 视频文件', 'rm', 'application/octet-stream'],
	rmvb: ['video', 'Real Networks 视频', 'rm', 'application/octet-stream'],
	ts: ['video', '高清电影封装格式 MPEG2-TS 视频', 'avi', 'video/MP2T'],
	vob: ['video', 'DVD视频媒体', 'avi', 'video/x-ms-vob'],
	avi: ['video', 'Audio Video Interleaved 视频', 'avi', 'video/x-msvideo'],
	'3gp': ['video', 'Audio Video Interleaved 视频', 'avi', 'video/3gpp'],
}

/**
 *
 */
export class FileType {

	constructor (types) {

	}

	getType (ext) {
		return fileTypes[ext]
	}

	getCategoryByMime (mime) {

		return find(fileTypes, (fileType) => {

			return fileType[3] === mime

		})

	}

}

export default new FileType()
