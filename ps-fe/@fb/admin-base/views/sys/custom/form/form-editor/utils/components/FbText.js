/*!
 * fb-input
 * (c) 2022 lincong1987
 */

export default {
	label: '文本',
	value: 'fb-text',
	icon: 'text',
	index: 1,

	fontFamily: [

		{
			label: '微软雅黑',
			value: 'Microsoft YaHei',
		},
		{
			label: '宋体',
			value: 'SimSun',
		},
		{
			label: '黑体',
			value: 'SimHei',
		},
		{
			label: '仿宋',
			value: 'FangSong',
		},
		{
			label: '楷体',
			value: 'KaiTi',
		},
		{
			label: '隶书',
			value: 'LiSu',
		},
		{
			label: '幼圆',
			value: 'YouYuan',
		},
		{
			label: '带衬线字体',
			value: 'serif',
		},
		{
			label: '无衬线字体',
			value: 'sans-serif',
		},
		{
			label: '等宽字体',
			value: 'monospace',
		},
		{
			label: '草书字体',
			value: 'cursive',
		},
		{
			label: '艺术效果',
			value: 'fantasy',
		},
		{
			label: '表情符号',
			value: 'emoji',
		},
	],

	sizes: [
		{
			label: '特小',
			value: 'xs',
		},
		{
			label: '小',
			value: 's',
		},
		{
			label: '默认',
			value: 'm',
		},
		{
			label: '大',
			value: 'l',
		},
		{
			label: '较大',
			value: 'xl',
		},
		{
			label: '特大',
			value: 'xxl',
		},
		{
			label: '超大',
			value: 'xxxl',
		},
	],

	align: [
		{
			label: '无',
			value: '',
		},
		{
			label: '左',
			value: 'left',
		},
		{
			label: '中',
			value: 'center',
		},
		{
			label: '右',
			value: 'right',
		},
	],

	va: [
		{
			label: '无',
			value: '',
		},
		{
			label: 'baseline',
			value: 'baseline',
		},
		{
			label: 'top',
			value: 'top',
		},
		{
			label: 'middle',
			value: 'middle',
		},
		{
			label: 'bottom',
			value: 'bottom',
		},
		{
			label: 'text-top',
			value: 'text-top',
		},
		{
			label: 'text-bottom',
			value: 'text-bottom',
		},
		{
			label: 'sub',
			value: 'sub',
		},
		{
			label: 'super',
			value: 'super',
		},
	],

	defaults: {
		defaultValue: 'hello',
		size: '',
		ellipsis: false,
		widthUnit: 'px',
	},

}
