import {merge, assign} from "lodash-es";

export default {
  props: {
    options: {
      type: Object,
      default: () => {
        return  {
          animationEasing: 'cubicOut',
          animationDelayUpdate (idx) {
            return idx * 100 + 300;
          },
          animationDelay (idx) {
            return idx * 100 + 100;
          }
        }
      }
    },
    theme: [String, Object],
    initOptions: Object,
    group: String,
    autoresize: Boolean,
    watchShallow: Boolean,
    manualUpdate: Boolean,
    aria: Boolean
  },
  watch: {
    options: {
      deep: true,
      handler () {
        this.init()
      }
    },
    aria (newVal) {
      this.ariaOpt.aria.enabled = newVal
      // this.ariaOpt.aria.decal.show = newVal
      this.opt = merge({}, this.opt, this.ariaOpt)
    }
  },
  mounted () {
    this.init()
    this.chart = this.$children[0].chart
  },
  methods: {
    init () {
      // 监听 @ 事件
      Object.keys(this.$listeners).forEach(event => {
        const handler = this.$listeners[event]

        if (event.indexOf('zr:') === 0) {
          // this.$children[0].chart.getZr().on(event.slice(3), handler)
          this.$children[0].chart.getZr().on(event, handler)
        } else {
          this.$children[0].chart.on(event, handler)
        }
      })
      // 开启无障碍花纹
      if (this.aria) {
        this.opt = merge(this.opt, this.ariaOpt)
      }
      // 合并数据
      if (this.options) {
        this.opt = merge(this.opt, this.options)
      }
    },
    // 设置初数据选项的其他 字段
    setOptionsOther (val) {
      if (val.dataZoom) {
        this.updateDataZoom(val)
      }
      if (val.yAxis) { // 更新y轴
        this.updateyAxis(val)
      }
      if (val.xAxis) { // 更新x轴
        this.updatexAxis(val)
      }
      if (val.visualMap) {
        this.updateVisualMap(val)
      }
      if (val.title) {
        this.updateTitle(val)
      }
      if (val.legend) {
        this.updateLegend(val)
      }
      if (val.grid) {
        this.updateGrid(val)
      }
      if (val.tooltip) {
        this.updateTooltip(val)
      }
      if (val.radar) {
        this.updateRadar(val)
      }
      if (val.timeline) {
          this.updateTimeline(val)
      }
      if (val.polar) {
        this.opt.polar = Object.assign(this.opt.polar, val.polar)
      }
      // this.chart.setOption(this.opt)
    },
    updateDataZoom (val) {
      this.opt.dataZoom[0] = merge(this.opt.dataZoom[0], val.dataZoom)
    },
    updateyAxis (val) {
      if (val.yAxis[0]) {
        this.opt.yAxis[0] = merge(this.opt.yAxis[0], val.yAxis[0])
      }
      if (val.yAxis[1]) {
        this.opt.yAxis[1] = merge(this.opt.yAxis[1], val.yAxis[1])
      }
    },
    updatexAxis (val) {
      this.opt.xAxis = merge(this.opt.xAxis, val.xAxis)
    },
    updateVisualMap (val) {
      if (val.visualMap[0]) {
        this.opt.visualMap[0] = merge(this.opt.visualMap[0], val.visualMap[0])
      }
      if (val.visualMap[1]) {
        this.opt.visualMap[1] = merge(this.opt.visualMap[1], val.visualMap[1])
      }
    },
    updateTitle (val) {
      this.opt.title = merge(this.opt.title, val.title)
    },
    updateLegend (val) {
      this.opt.legend = merge(this.opt.legend, val.legend)
    },
    updateGrid (val) {
      this.opt.grid = assign(this.opt.grid, val.grid)
    },
    updateTooltip (val) {
      this.opt.tooltip = merge(this.opt.tooltip, val.tooltip)
    },
    updateRadar (val) {
      this.opt.radar = assign(this.opt.radar, val.radar)
    },
    updateTimeline (val) {
        this.opt.timeline = merge(this.opt.timeline, val.timeline)
    },
  }

};


