export const lineColor = 'rgba(2, 234, 252, 0.3)'
export const dataZoom = {
    type: 'slider',
    top: '89%',
    bottom: '3%',
    borderColor: 'transparent',
    backgroundColor: 'rgba(2, 46, 125, 1)',
    dataBackground: {
        lineStyle: {
            color: 'rgba(17, 120, 201, 1)'
        }
    },
    handleStyle: {
        color: 'rgba(17, 120, 201, 1)'
    },
    textStyle: {
        color: '#fff',
    },
    brushSelect: false
}
export const intervalColor = ['#FD201C', '#FBAB02', '#F9FD26', '#0284FE']

// 换行计算
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

const linearY2 = {
    type: 'linear',
    x: 0,
    y: 0,
    x2: 0,
    y2: 1,
    global: false, // 缺省为 false
}

function reLinear(type, c1, c2) {
    return {
        type: 'linear',
        x: type === 'x' ? 1 : 0,
        y: type === 'y' ? 1 : 0,
        x2: type === 'x2' ? 1 : 0,
        y2: type === 'y2' ? 1 : 0,
        global: false, // 缺省为 false
        colorStops: [
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
        // 棕黄
        brown: reLinear('y2', '#C9904A', '#E2C392'),
        blue: reLinear('y2', '#05BAFF', '#0284FE'),
        blue2: reLinear('y2', '#61E4FF', '#32C5FF'),
        blue3: reLinear('y2', '#3B51FF', '#588AFF'),
        red: reLinear('y2', '#DA0007', '#B30003'),
        orange: reLinear('y2', '#EA9402', '#D05B01'),
        yellow: reLinear('y2', '#FFDB00', '#FFB500'),
        yellow2: reLinear('y2', '#F9FD26', '#FDFF80'),
        green: reLinear('y2', '#45B57D', '#217E47'),
        green2: reLinear('y2', '#469961', '#94C190'),
        cyan: reLinear('y2', '#25C5C5', '#109393'),
        purple: reLinear('y2', '#838DF7', '#4C54EB'),
        purple2: reLinear('y2', '#9B66FF', '#6236FF'),
    },
    y2trans: {
        yellow: reLinear('y2', 'rgba(252, 239, 57, 0.4)', 'rgba(252, 239, 57, 0)'),
        blue: reLinear('y2', 'rgba(50, 197, 255, 0.4)', 'rgba(50, 197, 255, 0)'),
        orange: reLinear('y2', 'rgba(247, 181, 0, 0.6)', 'rgba(247, 181, 0, 0)'),
        green: reLinear('y2', 'rgba(7, 232, 174, 0.6)', 'rgba(247, 181, 0, 0)'),
        red: reLinear('y2', 'rgba(224, 32, 32, 0.4)', 'rgba(247, 181, 0, 0)'),
    },
    // 自左向右
    x2: {
        // 棕黄
        blue: reLinear('x2', '#05BAFF', '#0284FE'),
        red: reLinear('x2', '#DA0007', '#B30003'),
        orange: reLinear('x2', '#EA9402', '#D05B01'),
        yellow: reLinear('x2', '#FFDB00', '#D2BF13'),
    },
    x2trans: {
        red: reLinear('x2', 'rgba(230, 0, 0, 0)', 'rgba(230, 0, 0, 1)'),
        orange: reLinear('x2', 'rgba(251, 171, 2, 0)', 'rgba(251, 171, 2, 1)'),
        yellow: reLinear('x2', 'rgba(249, 253, 38, 0)', 'rgba(249, 253, 38, 1)'),
        blue: reLinear('x2', 'rgba(2, 132, 254, 0)', 'rgba(2, 132, 254, 1)'),
    },
    liquidFill: {
        red: reLinear('y2', 'rgba(173, 45, 45, 1)', 'rgba(173, 45, 45, .1)'),
        orange: reLinear('y2', 'rgba(254, 121, 2, 1)', 'rgba(254, 121, 2, .1)'),
        yellow: reLinear('y2', 'rgba(250, 254, 130, 1)', 'rgba(250, 254, 130, .1)'),
        blue: reLinear('y2', 'rgba(0, 145, 255, 1)', 'rgba(0, 145, 255, .1)'),

        border: {
            red: reLinear('y2', 'rgba(253, 32, 28, 1)', 'rgba(251, 120, 4, 1)'),
            orange: reLinear('y2', 'rgba(254, 120, 2, 1)', 'rgba(240, 236, 0, 1)'),
            yellow: reLinear('y2', 'rgba(252, 255, 128, 1)', 'rgba(0, 145, 255, 1)'),
            blue: reLinear('y2', 'rgba(0, 145, 255, 1)', 'rgba(7, 232, 174, 1)'),
        }
    }
}