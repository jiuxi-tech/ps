/*!
 * xss
 * (c) 2021 lincong1987
 */

import xss from 'xss'

let whiteList = {
	a: ['target', 'href', 'title'],
	abbr: ['title'],
	address: [],
	area: ['shape', 'coords', 'href', 'alt'],
	article: [],
	aside: [],
	audio: [
		'autoplay',
		'controls',
		'crossorigin',
		'loop',
		'muted',
		'preload',
		'src',
	],
	b: [],
	bdi: ['dir'],
	bdo: ['dir'],
	big: [],
	blockquote: ['cite'],
	br: [],
	caption: [],
	center: [],
	cite: [],
	code: [],
	col: ['align', 'valign', 'span', 'width'],
	colgroup: ['align', 'valign', 'span', 'width'],
	dd: [],
	del: ['datetime'],
	details: ['open'],
	div: [],
	dl: [],
	dt: [],
	em: [],
	figcaption: [],
	figure: [],
	font: ['color', 'size', 'face'],
	footer: [],
	h1: [],
	h2: [],
	h3: [],
	h4: [],
	h5: [],
	h6: [],
	header: [],
	hr: [],
	i: [],
	img: ['src', 'alt', 'title', 'width', 'height'],
	ins: ['datetime'],
	li: [],
	mark: [],
	nav: [],
	ol: [],
	p: [],
	pre: [],
	s: [],
	section: [],
	small: [],
	span: [],
	sub: [],
	summary: [],
	sup: [],
	strong: [],
	strike: [],
	table: ['width', 'border', 'align', 'valign'],
	tbody: ['align', 'valign'],
	td: ['width', 'rowspan', 'colspan', 'align', 'valign'],
	tfoot: ['align', 'valign'],
	th: ['width', 'rowspan', 'colspan', 'align', 'valign'],
	thead: ['align', 'valign'],
	tr: ['rowspan', 'align', 'valign'],
	tt: [],
	u: [],
	ul: [],
	video: [
		'autoplay',
		'controls',
		'crossorigin',
		'loop',
		'muted',
		'playsinline',
		'poster',
		'preload',
		'src',
		'height',
		'width',
	],
}

for (let name in whiteList) {
	whiteList[name].concat(['style', 'class', 'id'])
}

export const xssFilter = new xss.FilterXSS({
	whiteList,
	onIgnoreTag: function (tag, html, options) {
		if (tag.substr(0, 2) === 'fb-') {
			// 不对其属性列表进行过滤
			return html
		}
	},
	onIgnoreTagAttr: function (tag, name, value, isWhiteAttr) {
		if (name.substr(0, 5) === 'data-') {
			// 通过内置的escapeAttrValue函数来对属性值进行转义
			return name + '="' + xss.escapeAttrValue(value) + '"'
		}
	},
})

export default {
	whiteList,
	xssFilter,
}
