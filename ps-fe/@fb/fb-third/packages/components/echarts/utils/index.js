import { isArray } from 'lodash-es'


/**
 * 文字换行显示
 * @param val
 * @param feedNum
 * @param feedStr
 * @returns {string}
 */
export function lineFeed(val, feedNum, feedStr) {
    feedStr = feedStr || '\n'
    var strs = val.split(''); //字符串数组
    var str = ''
    for (var i = 0, s; s = strs[i++];) { //遍历字符串数组
        str += s;
        if (!(i % feedNum)) str += feedStr; //按需要求余
    }
    return str
}

/**
 * 渲染渐变颜色
 * @param direction 方向 y2 自顶向下 y 自下向顶 x2 自左向右 x 自右向左
 * @param c1 第一颜色 or colorStops []
 * @param c2
 * @returns {{x: number, y: number, y2: number, x2: number, global: boolean, colorStops: [{offset: number, color: *}, {offset: number, color: *}], type: string}}
 */
export function renderLinear(direction, c1, c2) {
    return {
        type: 'linear',
        x: direction === 'x' ? 1 : 0,
        y: direction === 'y' ? 1 : 0,
        x2: direction === 'x2' ? 1 : 0,
        y2: direction === 'y2' ? 1 : 0,
        global: false, // 缺省为 false
        colorStops: isArray(c1) ? c1 : [
            {
                offset: 0,
                color: c1, // 0% 处的颜色
            }, {
                offset: 1,
                color: c2, // 100% 处的颜色
            },
        ],
    }
}

// 渐变色板
export const linearColors = {
    // 自定向下
    y2: {
		blue: renderLinear('y2', 'rgba(92, 207, 255, 1)', 'rgba(47, 161, 254, 1)'), // 纯色-天空蓝
		green: renderLinear('y2', 'rgba(17, 245, 215, 1)', 'rgba(7, 232, 174, 1)'), // 纯色-荧光绿
		yellow: renderLinear('y2', 'rgba(255, 231, 94, 1)', 'rgba(253, 202, 48, 1)'), // 纯色-旭日黄
		red: renderLinear('y2', 'rgba(253, 141, 134, 1)', 'rgba(251, 84, 78, 1)'), // 纯色-石榴红
		geekBlue: renderLinear('y2', 'rgba(161, 171, 250, 1)', 'rgba(103, 115, 244, 1)'), // 纯色-极客蓝
		purple: renderLinear('y2', 'rgba(208, 170, 246, 1)', 'rgba(163, 113, 234, 1)'), // 纯色-罗兰紫
		orange: renderLinear('y2', 'rgba(254, 203, 110, 1)', 'rgba(252, 156, 59, 1)'), // 纯色-落日橘
		cyan: renderLinear('y2', 'rgba(75, 210, 227, 1)', 'rgba(22, 153, 169, 1)'), // 纯色-浅海青
		pink: renderLinear('y2', 'rgba(254, 192, 233, 1)', 'rgba(252, 141, 206, 1)'), // 纯色-桃花粉
		grey: renderLinear('y2', 'rgba(150, 169, 196, 1)', 'rgba(93, 112, 146, 1)'), // 纯色-商务灰
        // 棕黄
        brown: renderLinear('y2', '#C9904A', '#E2C392'),
        blue2: renderLinear('y2', '#61E4FF', '#32C5FF'),
        blue3: renderLinear('y2', '#3B51FF', '#588AFF'),
        red2: renderLinear('y2', '#DA0007', '#B30003'),
        orange2: renderLinear('y2', '#EA9402', '#D05B01'),
        yellow2: renderLinear('y2', '#FFDB00', '#FFB500'),
        yellow3: renderLinear('y2', '#F9FD26', '#FDFF80'),
        green2: renderLinear('y2', '#45B57D', '#217E47'),
        green3: renderLinear('y2', '#469961', '#94C190'),
        purple2: renderLinear('y2', '#838DF7', '#4C54EB'),
        purple3: renderLinear('y2', '#9B66FF', '#6236FF'),
    },
    y2trans: {
		blue: renderLinear('y2', 'rgba(47, 161, 254, 0.8)', 'rgba(47, 161, 254, 0)'),
		green: renderLinear('y2', 'rgba(7, 232, 174, 0.8)', 'rgba(7, 232, 174, 0)'),
		yellow: renderLinear('y2', 'rgba(253, 202, 48, 0.8)', 'rgba(253, 202, 48, 0)'),
		red: renderLinear('y2', 'rgba(251, 84, 78, 0.8)', 'rgba(251, 84, 78, 0)'),
		geekBlue: renderLinear('y2', 'rgba(103, 115, 244, 0.8)', 'rgba(103, 115, 244, 0)'),
		purple: renderLinear('y2', 'rgba(163, 113, 234, 0.8)', 'rgba(163, 113, 234,  0)'),
		orange: renderLinear('y2', 'rgba(252, 156, 59, 0.8)', 'rgba(252, 156, 59, 0)'),
		cyan: renderLinear('y2', 'rgba(22, 153, 169, 0.8)', 'rgba(22, 153, 169, 0)'),
		pink: renderLinear('y2', 'rgba(252, 141, 206, 0.8)', 'rgba(252, 141, 206, 0)'),
		grey: renderLinear('y2', 'rgba(93, 112, 146, 0.8)', 'rgba(93, 112, 146, 0)'),
    },
    // 自左向右
    x2: {
		blue: renderLinear('x2', 'rgba(92, 207, 255, 1)', 'rgba(47, 161, 254, 1)'), // 纯色-天空蓝
		green: renderLinear('x2', 'rgba(17, 245, 215, 1)', 'rgba(7, 232, 174, 1)'), // 纯色-荧光绿
		yellow: renderLinear('x2', 'rgba(255, 231, 94, 1)', 'rgba(253, 202, 48, 1)'), // 纯色-旭日黄
		red: renderLinear('x2', 'rgba(253, 141, 134, 1)', 'rgba(251, 84, 78, 1)'), // 纯色-石榴红
		geekBlue: renderLinear('x2', 'rgba(161, 171, 250, 1)', 'rgba(103, 115, 244, 1)'), // 纯色-极客蓝
		purple: renderLinear('x2', 'rgba(208, 170, 246, 1)', 'rgba(163, 113, 234, 1)'), // 纯色-罗兰紫
		orange: renderLinear('x2', 'rgba(254, 203, 110, 1)', 'rgba(252, 156, 59, 1)'), // 纯色-落日橘
		cyan: renderLinear('x2', 'rgba(75, 210, 227, 1)', 'rgba(22, 153, 169, 1)'), // 纯色-浅海青
		pink: renderLinear('x2', 'rgba(254, 192, 233, 1)', 'rgba(252, 141, 206, 1)'), // 纯色-桃花粉
		grey: renderLinear('x2', 'rgba(150, 169, 196, 1)', 'rgba(93, 112, 146, 1)'), // 纯色-商务灰
        // 棕黄
        blue2: renderLinear('x2', '#05BAFF', '#0284FE'),
        red2: renderLinear('x2', '#DA0007', '#B30003'),
        orange2: renderLinear('x2', '#EA9402', '#D05B01'),
        yellow2: renderLinear('x2', '#FFDB00', '#D2BF13'),
    },
    x2trans: {
		blue: renderLinear('x2', 'rgba(47, 161, 254, 0.8)', 'rgba(47, 161, 254, 0)'),
		green: renderLinear('x2', 'rgba(7, 232, 174, 0.8)', 'rgba(7, 232, 174, 0)'),
		yellow: renderLinear('x2', 'rgba(253, 202, 48, 0.8)', 'rgba(253, 202, 48, 0)'),
		red: renderLinear('x2', 'rgba(251, 84, 78, 0.8)', 'rgba(251, 84, 78, 0)'),
		geekBlue: renderLinear('x2', 'rgba(103, 115, 244, 0.8)', 'rgba(103, 115, 244, 0)'),
		purple: renderLinear('x2', 'rgba(163, 113, 234, 0.8)', 'rgba(163, 113, 234, 0)'),
		orange: renderLinear('x2', 'rgba(252, 156, 59, 0.8)', 'rgba(252, 156, 59, 0)'),
		cyan: renderLinear('x2', 'rgba(22, 153, 169, 0.8)', 'rgba(22, 153, 169, 0)'),
		pink: renderLinear('x2', 'rgba(252, 141, 206, 0.8)', 'rgba(252, 141, 206, 0)'),
		grey: renderLinear('x2', 'rgba(93, 112, 146, 0.8)', 'rgba(93, 112, 146, 0)'),
    },
	liquidFill: {
		red: renderLinear('y2', 'rgba(173, 45, 45, 1)', 'rgba(173, 45, 45, .1)'),
		orange: renderLinear('y2', 'rgba(254, 121, 2, 1)', 'rgba(254, 121, 2, .1)'),
		yellow: renderLinear('y2', 'rgba(250, 254, 130, 1)', 'rgba(250, 254, 130, .1)'),
		blue: renderLinear('y2', 'rgba(0, 145, 255, 1)', 'rgba(0, 145, 255, .1)'),
		green: renderLinear('y2', 'rgba(7, 232, 174, 1)', 'rgba(7, 232, 174, .1)'),

		border: {
			red: renderLinear('y2', 'rgba(253, 32, 28, 1)', 'rgba(251, 120, 4, 1)'),
			orange: renderLinear('y2', 'rgba(254, 120, 2, 1)', 'rgba(240, 236, 0, 1)'),
			yellow: renderLinear('y2', 'rgba(252, 255, 128, 1)', 'rgba(0, 145, 255, 1)'),
			blue: renderLinear('y2', 'rgba(0, 145, 255, 1)', 'rgba(7, 232, 174, 1)'),
			green: renderLinear('y2', 'rgba(17, 245, 215, 1)', 'rgba(7, 232, 174, 1)'),
		}
	},
	// 自右向左
	x: {
		blue: renderLinear('x', 'rgba(92, 207, 255, 1)', 'rgba(47, 161, 254, 1)'), // 纯色-天空蓝
		green: renderLinear('x', 'rgba(17, 245, 215, 1)', 'rgba(7, 232, 174, 1)'), // 纯色-荧光绿
		yellow: renderLinear('x', 'rgba(255, 231, 94, 1)', 'rgba(253, 202, 48, 1)'), // 纯色-旭日黄
		red: renderLinear('x', 'rgba(253, 141, 134, 1)', 'rgba(251, 84, 78, 1)'), // 纯色-石榴红
		geekBlue: renderLinear('x', 'rgba(161, 171, 250, 1)', 'rgba(103, 115, 244, 1)'), // 纯色-极客蓝
		purple: renderLinear('x', 'rgba(208, 170, 246, 1)', 'rgba(163, 113, 234, 1)'), // 纯色-罗兰紫
		orange: renderLinear('x', 'rgba(254, 203, 110, 1)', 'rgba(252, 156, 59, 1)'), // 纯色-落日橘
		cyan: renderLinear('x', 'rgba(75, 210, 227, 1)', 'rgba(22, 153, 169, 1)'), // 纯色-浅海青
		pink: renderLinear('x', 'rgba(254, 192, 233, 1)', 'rgba(252, 141, 206, 1)'), // 纯色-桃花粉
		grey: renderLinear('x', 'rgba(150, 169, 196, 1)', 'rgba(93, 112, 146, 1)'), // 纯色-商务灰
		// 棕黄
		blue2: renderLinear('x', '#05BAFF', '#0284FE'),
		red2: renderLinear('x', '#DA0007', '#B30003'),
		orange2: renderLinear('x', '#EA9402', '#D05B01'),
		yellow2: renderLinear('x', '#FFDB00', '#D2BF13'),
	},
	xtrans: {
		blue: renderLinear('x', 'rgba(47, 161, 254, 0.8)', 'rgba(47, 161, 254, 0)'),
		green: renderLinear('x', 'rgba(7, 232, 174, 0.8)', 'rgba(7, 232, 174, 0)'),
		yellow: renderLinear('x', 'rgba(253, 202, 48, 0.8)', 'rgba(253, 202, 48, 0)'),
		red: renderLinear('x', 'rgba(251, 84, 78, 0.8)', 'rgba(251, 84, 78, 0)'),
		geekBlue: renderLinear('x', 'rgba(103, 115, 244, 0.8)', 'rgba(103, 115, 244, 0)'),
		purple: renderLinear('x', 'rgba(163, 113, 234, 0.8)', 'rgba(163, 113, 234, 0)'),
		orange: renderLinear('x', 'rgba(252, 156, 59, 0.8)', 'rgba(252, 156, 59, 0)'),
		cyan: renderLinear('x', 'rgba(22, 153, 169, 0.8)', 'rgba(22, 153, 169, 0)'),
		pink: renderLinear('x', 'rgba(252, 141, 206, 0.8)', 'rgba(252, 141, 206, 0)'),
		grey: renderLinear('x', 'rgba(93, 112, 146, 0.8)', 'rgba(93, 112, 146, 0)'),
	},
	// 自下向顶
	y: {
		blue: renderLinear('y', 'rgba(92, 207, 255, 1)', 'rgba(47, 161, 254, 1)'), // 纯色-天空蓝
		green: renderLinear('y', 'rgba(17, 245, 215, 1)', 'rgba(7, 232, 174, 1)'), // 纯色-荧光绿
		yellow: renderLinear('y', 'rgba(255, 231, 94, 1)', 'rgba(253, 202, 48, 1)'), // 纯色-旭日黄
		red: renderLinear('y', 'rgba(253, 141, 134, 1)', 'rgba(251, 84, 78, 1)'), // 纯色-石榴红
		geekBlue: renderLinear('y', 'rgba(161, 171, 250, 1)', 'rgba(103, 115, 244, 1)'), // 纯色-极客蓝
		purple: renderLinear('y', 'rgba(208, 170, 246, 1)', 'rgba(163, 113, 234, 1)'), // 纯色-罗兰紫
		orange: renderLinear('y', 'rgba(254, 203, 110, 1)', 'rgba(252, 156, 59, 1)'), // 纯色-落日橘
		cyan: renderLinear('y', 'rgba(75, 210, 227, 1)', 'rgba(22, 153, 169, 1)'), // 纯色-浅海青
		pink: renderLinear('y', 'rgba(254, 192, 233, 1)', 'rgba(252, 141, 206, 1)'), // 纯色-桃花粉
		grey: renderLinear('y', 'rgba(150, 169, 196, 1)', 'rgba(93, 112, 146, 1)'), // 纯色-商务灰
	},
	ytrans: {
		blue: renderLinear('y', 'rgba(47, 161, 254, 0.8)', 'rgba(47, 161, 254, 0)'),
		green: renderLinear('y', 'rgba(7, 232, 174, 0.8)', 'rgba(7, 232, 174, 0)'),
		yellow: renderLinear('y', 'rgba(253, 202, 48, 0.8)', 'rgba(253, 202, 48, 0)'),
		red: renderLinear('y', 'rgba(251, 84, 78, 0.8)', 'rgba(251, 84, 78, 0)'),
		geekBlue: renderLinear('y', 'rgba(103, 115, 244, 0.8)', 'rgba(103, 115, 244, 0)'),
		purple: renderLinear('y', 'rgba(163, 113, 234, 0.8)', 'rgba(163, 113, 234, 0)'),
		orange: renderLinear('y', 'rgba(252, 156, 59, 0.8)', 'rgba(252, 156, 59, 0)'),
		cyan: renderLinear('y', 'rgba(22, 153, 169, 0.8)', 'rgba(22, 153, 169, 0)'),
		pink: renderLinear('y', 'rgba(252, 141, 206, 0.8)', 'rgba(252, 141, 206, 0)'),
		grey: renderLinear('y', 'rgba(93, 112, 146, 0.8)', 'rgba(93, 112, 146, 0)'),
	}
}

export default {
	lineFeed,
	renderLinear,
	linearColors
}
