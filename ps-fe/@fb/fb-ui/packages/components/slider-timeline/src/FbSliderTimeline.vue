<template>
	<div :class="[`${prefix}-slider-timeline-container`]" ref="containerRef">
		<canvas ref="canvasRef"/>
	</div>
</template>

<script>
	import {prefix} from '../../../../src/config'
	import {throttle} from "lodash-es";
	import dayjs from "dayjs";

	// 转时间格式
	const formatTimeLine = (time, format) => {
		return dayjs(time || new Date()).format(format || "YYYY-MM-DD HH:mm:ss");
	};
	const getPixelRatio = function (context) {
		const backingStore =
			context.backingStorePixelRatio ||
			context.webkitBackingStorePixelRatio ||
			context.mozBackingStorePixelRatio ||
			context.msBackingStorePixelRatio ||
			context.oBackingStorePixelRatio ||
			context.backingStorePixelRatio ||
			1;
		return (window.devicePixelRatio || 1) / backingStore;
	};

	export default {
		name: 'FbSliderTimeline',
		props: {
			value: {
				type: Number,
			},
			// 布局模式 top/center/bottom/center-triangle
			mode: {
				type: String,
				default: 'top',
				validator (value) {
					// 这个值必须匹配下列字符串中的一个
					return ['top', 'center', 'bottom', 'center-triangle'].indexOf(value) !== -1
				},
			},
			// 标记事件区间
			timeParts: {
				type: Array,
				default: [],
			},
			// 标记事件区间 阻止移动
			timePartPrevent: {
				type: Boolean,
				default: false,
			},
			// 自动移动
			autoMove: {
				type: Boolean,
				default: false,
			},
			// 自动移动部署 毫秒时
			autoMoveStep: {
				type: Number,
				default: 1000,
			},
			nowTime: {
				type: Number,
				default: new Date().getTime(),
			},

			// 刻度长度
			markHeight: {
				type: Number,
				default: 8,
			},
			// 小刻度长度
			minMarkHeight: {
				type: Number,
				default: undefined,
			},
			// 大刻度长度
			maxMarkHeight: {
				type: Number,
				default: undefined,
			},

			// 游标颜色
			drawCursorColor: {
				type: String,
				default: "#3B5EFF",
			},
			// 游标颜色
			drawCursorTextColor: {
				type: String,
				default: "",
			},
			// 游标显示文字 --- 强制修改
			drawCursorText: {
				type: String,
				default: undefined,
			},
			// 游标显示文字 --- 时间格式化，遵守dayjs规则
			drawCursorFormat: {
				type: String,
				default: undefined,
			},
			// 游标字体
			drawCursorFont: {
				type: String,
				default: "18px serif",
			},
			// 游标线宽
			drawCursorWidth: {
				type: Number,
				default: 2,
			},
			// 游标绘制距离画布上部 y 偏移量
			drawCursorOffsetY: {
				type: Number,
				default: undefined,
			},
			// 游标绘制中线 x 偏移量
			drawCursorOffsetX: {
				type: Number,
				default: undefined,
			},
			// 游标中线 三角 宽度
			drawCursorTriangleWidth: {
				type: Number,
				default: 9,
			},
			// 游标中线 三角 宽度
			drawCursorTriangleHeight: {
				type: Number,
				default: 5,
			},
			// 游标中线 三角 颜色
			drawCursorTriangleColor: {
				type: String,
				default: '',
			},
			// 刻度处背景颜色
			fillScaleBgColor: {
				type: String,
				default: "#DCDFE6",
			},
			// 刻度处背景高度
			fillScaleHeight: {
				type: Number,
				default: 12,
			},
			// 刻度格式化
			scaleTextFormat: {
				type: [String, Function],
				default: "HH:mm:ss",
			},
			// 刻度颜色
			drawScaleColor: {
				type: String,
				default: "#606266",
			},
			// 刻度字体
			drawScaleFont: {
				type: String,
				default: "14px serif",
			},
			// 刻度 绘制距离长分割线 x 偏移量
			drawScaleOffsetX: {
				type: Number,
				default: undefined,
			},
			// 刻度 绘制距离长分割线 y 偏移量
			drawScaleOffsetY: {
				type: Number,
				default: undefined,
			},
			// 鼠标悬浮移动 游标颜色
			hoverCursorColor: {
				type: String,
				default: "#5674fd",
			},
			// 鼠标悬浮移动显示文字 --- 强制修改
			hoverCursorText: {
				type: String,
				default: undefined,
			},
			// 鼠标悬浮移动显示文字 --- 时间格式化，遵守dayjs规则
			hoverCursorFormat: {
				type: String,
				default: undefined,
			},
			// 鼠标悬浮移动 游标字体
			hoverCursorFont: {
				type: String,
				default: "16px serif",
			},
			// 鼠标悬浮移动 游标宽度
			hoverCursorWidth: {
				type: Number,
				default: 1,
			},
			// 鼠标悬浮移动绘制距离画布上部 y 偏移量
			hoverCursorOffsetY: {
				type: Number,
				default: undefined,
			},
			// 鼠标悬浮移动绘制中线 x 偏移量
			hoverCursorOffsetX: {
				type: Number,
				default: undefined,
			},
			// 录像时间块颜色
			fillTimePartsColor: {
				type: String,
				default: "rgba(140, 158, 238 , .5)",
			},
			// 录像时间块高度
			fillTimePartHeight: {
				type: Number,
				default: 20,
			},

			// 最小刻度间距
			minScaleSpacing: {
				type: Number,
				default: 20,
			},
			// 允许的最小大格长度px值 如果调小 大格会变密集
			minLargeScaleSpacing: {
				type: Number,
				default: 100,
			},
			// 缩放层级
			zoomLevel: {
				type: Number,
				default: 24,
			},
			zoomStep: {
				type: Number,
				default: 2,
			}
		},

		data() {
			return {
				prefix,
				// 屏幕像素比
				ratio: getPixelRatio(window),
				canvasRef: null,
				containerRef: null,
				ctxRef: null,
				// 可选的每个间隔代表多少分钟
				minutePerStep: [
					1, 2, 5, 10, 15, 20, 30, 60, 120, 180, 240, 360, 720, 1440,
				],
				currentTime: this.value || this.nowTime,
				// timeParts :this.timeParts,
				isMove: false,
				moveTimer: null,
				startTimestamp: 0,
				// 鼠标是否被按下 用来确认时hover事件还是拖拽事件
				isMouseDownFlag: false,
				// 是否拖拽 用来确认mouseup时是点击事件还是拖拽事件
				isDragFlag: false,
				// 鼠标按下时鼠标x位置 在处理拖拽事件中用来比对
				mousedownX: 0,
				// 缩放层级
				zoom: this.zoomLevel,
				// 整个时间轴表示的时间长度
				totalRuler: this.zoomLevel,
				// 整个时间轴计算倍率
				totalRulerMultiple: 60 * 60 * 1000
			}
		},

		computed: {
			smallMarkHeight() {
				return this.minMarkHeight || this.markHeight
			},
			bigMarkHeight() {
				return this.maxMarkHeight || this.markHeight + 4
			},
		},

		watch: {
			timeParts() {
				this.init()
			},
			value(newValue) {
				this.currentTime = newValue
				this.init()
			},
			nowTime(newValue) {
				this.currentTime = newValue
				this.init()
			},
			autoMove: {
				handler(newValue) {
					this.setIsMove(newValue)
				},
				immediate: true
			}
		},

		mounted() {

			this.setCanvasWH = throttle(() => {
				if (this.$refs['canvasRef'] && this.$refs['containerRef']) {
					this.$refs['canvasRef'].width = this.$refs['containerRef'].clientWidth * this.ratio;
					this.$refs['canvasRef'].height = this.$refs['containerRef'].clientHeight * this.ratio;

				}
				this.init();
			}, 1000);

			this.ctxRef = this.$refs['canvasRef'].getContext("2d");
			this.calcTotalRulerMultiple()
			this.init();
			this.radio = getPixelRatio(this.ctxRef)
			this.setCanvasWH();
			window.addEventListener("resize", this.setCanvasWH);
			this.$refs['canvasRef'].addEventListener("wheel", this.wheel);
			this.$refs['canvasRef'].addEventListener("dblclick", this.mousedblclick);
			this.$refs['canvasRef'].addEventListener("mousedown", this.mousedown);
			this.$refs['canvasRef'].addEventListener("mousemove", this.mousemove);
			this.$refs['canvasRef'].addEventListener("mouseup", this.mouseup);
			this.$refs['canvasRef'].addEventListener("mouseleave", this.mouseleave);
		},

		beforeDestroy() {
			window.removeEventListener("resize", this.setCanvasWH);
			if (this.$refs['canvasRef']) {
				this.$refs['canvasRef'].removeEventListener("wheel", this.wheel);
				this.$refs['canvasRef'].removeEventListener("dblclick", this.mousedblclick);
				this.$refs['canvasRef'].removeEventListener("mousedown", this.mousedown);
				this.$refs['canvasRef'].removeEventListener("mousemove", this.mousemove);
				this.$refs['canvasRef'].removeEventListener("mouseup", this.mouseup);
				this.$refs['canvasRef'].removeEventListener("mouseleave", this.mouseleave);
				this.clearCanvas();
			}

			if (this.moveTimer) {
				clearInterval(this.moveTimer);
				this.moveTimer = null;
			}
		},

		methods: {
			init() {
				this.refreshStartTimestamp();
				// 清空画布
				this.clearCanvas();
				// 画刻度处背景
				this.fillScaleBg();
				// 画刻度
				this.drawScale();

				if (this.timeParts.length) {
					this.timeParts.forEach((element) => {
						this.fillTimeParts(element);
					});
				}
				// 画游标
				this.drawCursor();
			},
			refreshStartTimestamp() {
				// 当currentTime改变或者整条时间轴代表的totalHours改变的时候 就刷新左边开始时间
				this.startTimestamp =
					this.currentTime - (this.totalRuler * this.totalRulerMultiple) / 2;
			},

			clearCanvas() {
				if (!this.ctxRef || !this.$refs['canvasRef']) return;
				this.ctxRef.clearRect(
					0,
					0,
					this.$refs['canvasRef'].width || 0,
					this.$refs['canvasRef'].height || 0
				);
			},

			fillScaleBg() {
				if (this.ctxRef) {
					// 区间外禁止拖拽 --- 默认只做一个区间
					if (this.timePartPrevent) {
						let part = this.timeParts[0]
						let height = this.fillTimePartHeight
						let onePxsMS =
							this.$refs['canvasRef'].width / (this.totalRuler * this.totalRulerMultiple);
						let beginX = (part.start - this.startTimestamp) * onePxsMS;
						let partWidth = (part.end - part.start) * onePxsMS;
						let y = this.yAxisCancelNum(height)
						this.ctxRef.fillStyle = this.fillScaleBgColor;
						this.ctxRef.fillRect(0, y, beginX, height);
						this.ctxRef.fillRect(beginX + partWidth, y, this.$refs['canvasRef'].width || 0, height);
					} else {
						let height = this.fillScaleHeight
						let y = this.yAxisCancelNum(height)
						this.ctxRef.fillStyle = this.fillScaleBgColor;
						this.ctxRef.fillRect(0, y, this.$refs['canvasRef'].width || 0, height);
					}
				}
			},

			createScaleText(time) {
				if (
					time.getHours() === 0 &&
					time.getMinutes() === 0 &&
					time.getMilliseconds() === 0
				) {
					return dayjs(time).format("YYYY-MM-DD");
				}

				if (typeof this.scaleTextFormat === 'function') {
					return this.scaleTextFormat(time)
				} else {
					return dayjs(time).format(this.scaleTextFormat);
				}
			},

			drawScale() {
				if (!this.$refs['canvasRef'] || !this.ctxRef) return;
				// 一分钟多少像素
				let oneMinutePx = this.$refs['canvasRef'].width / (this.totalRuler * 60);
				// 一毫秒多少像素
				let oneMSPx = oneMinutePx / (60 * 1000);
				// 刻度间隔 默认20px
				let scaleSpacing = this.minScaleSpacing;
				// 每格代表多少分钟 / 秒
				let scaleUnit = scaleSpacing / oneMinutePx;
				let len = this.minutePerStep.length;
				for (let i = 0; i < len; i += 1) {
					if (scaleUnit < this.minutePerStep[i]) {
						// 选择正确的刻度单位分钟
						scaleUnit = this.minutePerStep[i];
						// 每刻度之间的距离 = 一分钟多少像素 * 刻度单位
						// 即 scaleUnit = scaleSpacing / oneMinutePx 的变形
						// 主要是 totalRuler 会变化 需要根据这个的变化来计算...
						scaleSpacing = oneMinutePx * scaleUnit;
						break;
					}
				}

				// 有刻度文字的大格相当于多少分钟 相当于直尺上的1cm
				let mediumStep = 30;
				for (let i = 0; i < len; i++) {
					if (this.minLargeScaleSpacing / oneMinutePx <= this.minutePerStep[i]) {
						mediumStep = this.minutePerStep[i];
						break;
					}
				}

				let totalScales = this.$refs['canvasRef'].width / scaleSpacing;
				// 某个刻度距离最左端得距离
				let graduationLeft;
				// 某个刻度得时间
				let graduationTime;
				let lineHeight;
				// 开始时间 = 中间时间 - 一半得整条时间
				this.startTimestamp =
					this.currentTime - (this.totalRuler * this.totalRulerMultiple) / 2;
				// 因为中间点是currentTime.value是固定的 最右边不一定在某个刻度上 会有一定的偏移量
				let leftOffsetMs =
					scaleUnit * 60 * 1000 - (this.startTimestamp % (scaleUnit * 60 * 1000));
				if (this.totalRuler === 1) {
					leftOffsetMs =
						scaleUnit * 1000 - (this.startTimestamp % (scaleUnit * 1000)) - 1000;
				}
				// 开始时间偏移距离(px)
				let leftOffsetPx = leftOffsetMs * oneMSPx;
				// 一刻度多少毫秒
				let oneScalesMS = this.totalRuler === 1 ? (scaleSpacing / oneMSPx / 60) : (scaleSpacing / oneMSPx);
				// 文字颜色
				this.ctxRef.fillStyle = this.drawScaleColor;
				this.ctxRef.font = this.calcRadioFont(this.drawScaleFont);

				// 刻度线颜色
				this.ctxRef.strokeStyle = this.drawScaleColor;
				this.ctxRef.beginPath();


				for (let i = 0; i < totalScales; i++) {
					// 距离 = 开始得偏移距离 + 格数 * 每格得px;
					graduationLeft = leftOffsetPx + i * scaleSpacing;
					// 时间 = 左侧开始时间 + 偏移时间 + 格数 * 一格多少毫秒
					graduationTime = this.startTimestamp + leftOffsetMs + i * oneScalesMS;
					let date = new Date(graduationTime);
					if (
						((graduationTime / (60 * 1000)) % mediumStep == 0) ||
						(this.totalRuler === 1 && (graduationTime / 1000) % mediumStep == 0 || (graduationTime / 1000) % mediumStep == 7)
					) {
						// 大格刻度
						lineHeight = this.bigMarkHeight;
						let offsetX = this.drawScaleOffsetX || -28
						let offsetY = this.drawScaleOffsetY || -6
						let x = graduationLeft + (offsetX * this.radio)
						let y = this.yAxisCancelNum(lineHeight)
						if (this.mode === 'top') {
							y = lineHeight + 30
						} else if (this.mode === 'center' || this.mode === 'center-triangle') {
							y = y + lineHeight + 30
						}
						let scaleText = this.createScaleText(date);
						this.ctxRef.fillText(scaleText, x, y + offsetY);
					} else {
						// 小格刻度
						lineHeight = this.smallMarkHeight;
					}
					this.drawScaleLine(graduationLeft, lineHeight);
				}
				this.ctxRef.stroke();
			},
			drawScaleLine(left, height) {
				let y = this.yAxisCancelNum(height)
				this.ctxRef.moveTo(left, y);
				this.ctxRef.lineTo(left, y + height);
				this.ctxRef.lineWidth = 1;
			},

			yAxisCancelNum(height) {
				// 根据模式计算 y 坐标
				// top 默认为 0
				let y = 0;
				if (this.mode === 'center' || this.mode === 'center-triangle') {
					y = (this.$refs['canvasRef'].height - height) / 2
				} else if (this.mode === 'bottom') {
					y = this.$refs['canvasRef'].height - height
				}
				return y
			},

			fillTimeParts(part) {
				let height = this.fillTimePartHeight
				let onePxsMS =
					this.$refs['canvasRef'].width / (this.totalRuler * this.totalRulerMultiple);
				let beginX = (part.start - this.startTimestamp) * onePxsMS;
				let partWidth = (part.end - part.start) * onePxsMS;
				if (part.style && part.style.background) {
					this.ctxRef.fillStyle = part.style.background;
				} else {
					this.ctxRef.fillStyle = this.fillTimePartsColor;
				}
				if (part.style && part.style.height) {
					height = part.style.height
				}
				let y = this.yAxisCancelNum(height)
				this.ctxRef.fillRect(beginX, y, partWidth, height);

				let textStyle = part.textStyle || {}
				if (part.showText) {
					let textFormat = textStyle.format || 'HH:mm:ss'
					let offsetX = textStyle.offsetX || -36
					let offsetY = textStyle.offsetY || -5
					let startText = this.createFillPartText(part.start, textFormat)
					let endText = this.createFillPartText(part.end, textFormat)
					// 文字颜色
					this.ctxRef.fillStyle = textStyle.color || this.drawScaleColor;
					this.ctxRef.font = this.calcRadioFont(textStyle.font || this.drawScaleFont);
					this.ctxRef.fillText(startText, beginX + offsetX, y + offsetY);
					this.ctxRef.fillText(endText, beginX + partWidth + offsetX, y + offsetY);
				}
			},

			createFillPartText(time, format = 'HH:mm:ss') {
				if (typeof format === 'function') {
					return format(time)
				} else {
					return dayjs(time).format(format);
				}
			},

			drawCursor() {
				if (!this.$refs['canvasRef'] || !this.ctxRef) return;
				let x = this.$refs['canvasRef'].width / 2;
				let y = 0;
				if (this.mode === 'center-triangle') {
					y = this.yAxisCancelNum(parseInt(this.drawCursorFont))
					this.drawCursorTriangle(x, y)
				}
				this.ctxRef.beginPath();
				this.ctxRef.moveTo(x, y);
				this.ctxRef.lineTo(x, this.$refs['canvasRef'].height || 110);
				this.ctxRef.strokeStyle = this.drawCursorColor;
				this.ctxRef.lineWidth = this.drawCursorWidth || 2;
				this.ctxRef.stroke();
				this.ctxRef.fillStyle = this.drawCursorTextColor || this.drawCursorColor;
				this.ctxRef.font = this.calcRadioFont(this.drawCursorFont);
				let offsetX = this.drawCursorOffsetX || -95
				let textY = 0
				let text = this.drawCursorText || formatTimeLine(this.currentTime, this.drawCursorFormat)
				if (this.mode === 'top') {
					textY = this.$refs['canvasRef'].height - 20
				} else if (this.mode === 'center') {
					textY = this.yAxisCancelNum(parseInt(this.drawCursorFont))
				} else if (this.mode === 'bottom') {
					textY = this.yAxisCancelNum(parseInt(this.drawCursorFont)) - 40
				} else if (this.mode === 'center-triangle') {
					textY = this.yAxisCancelNum(parseInt(this.drawCursorFont)) - this.drawCursorTriangleHeight - 5
				}

				this.ctxRef.fillText(
					text,
					x + offsetX * this.radio,
					this.drawCursorOffsetY || textY
				);
			},
			drawCursorTriangle(x, y) {
				let widthHalf = parseInt(this.drawCursorTriangleWidth / 2)
				this.ctxRef.beginPath();
				this.ctxRef.moveTo(x, y + 1);
				this.ctxRef.lineTo(x - widthHalf, y - this.drawCursorTriangleHeight);
				this.ctxRef.lineTo(x + widthHalf, y - this.drawCursorTriangleHeight);
				// 改变填充颜色
				this.ctxRef.fillStyle = this.drawCursorTriangleColor || this.drawCursorColor;
				//填充这个图形
				this.ctxRef.fill();
			},

			//  addTimeParts (timeParts) {
			//    this.setTimeParts(this.timeParts.concat(timeParts));
			// },
			setIsMove(Move) {
				const clearTimer = () => {
					if (this.moveTimer) {
						clearInterval(this.moveTimer);
						this.moveTimer = null;
					}
				};
				clearTimer();

				if (this.isMove === Move) return;
				this.isMove = Move;
				if (this.isMove) {
					// 先清除之前得timer 否则会有两个timer通知进行...
					if (this.moveTimer) {
						clearTimer();
					}

					this.moveTimer = setInterval(() => {
						if (this.timePartPrevent && this.timeParts[0] && this.currentTime >= this.timeParts[0].end) {
							clearTimer()
						}
						this.currentTime += this.autoMoveStep || 1000;
						this.$emit('on-change', new Date(this.currentTime), this.currentTime)
						this.$emit('input', this.currentTime)
						this.init();
					}, 1000);
				} else {
					clearTimer();
				}
			},

			dragMove(event) {
				let posX = this.getMouseXRelativePos(event);
				let diffX = posX - this.mousedownX;
				let onePxsMS =
					this.$refs['canvasRef'].width / (this.totalRuler * this.totalRulerMultiple);

				let t = this.currentTime - Math.round(diffX / onePxsMS);
				if (this.timePartPrevent && t < this.timeParts[0].start) {
					this.$emit('on-time-prevent')
					this.$emit('on-time-under')
					return
				}
				if (this.timePartPrevent && t > this.timeParts[0].end) {
					this.$emit('on-time-prevent')
					this.$emit('on-time-above')
					return
				}
				this.currentTime = t
				this.init();
				// 👇因为重新设置了currentTime 所以要重新设置鼠标按下位置
				// 否则偏移时间会进行累加 越拖越快越拖越快...
				this.mousedownX = posX;
			},

			hoverMove(event) {
				const posX = this.getMouseXRelativePos(event);
				const t = this.getMousePosTime(event);
				this.init();
				this.ctxRef.beginPath();
				this.ctxRef.moveTo(posX * 1 * this.ratio + 1, 0);
				this.ctxRef.lineTo(posX * 1 * this.ratio + 1, this.$refs['canvasRef'].height * this.ratio);
				this.ctxRef.strokeStyle = this.hoverCursorColor;
				this.ctxRef.lineWidth = this.hoverCursorWidth;
				this.ctxRef.stroke();
				this.ctxRef.fillStyle = this.hoverCursorColor;
				this.ctxRef.font = this.calcRadioFont(this.hoverCursorFont);
				// this.ctxRef.textAlign = 'center'

				let offsetX = this.hoverCursorOffsetX || -85
				let y = 0
				let text = this.hoverCursorText || formatTimeLine(t, this.hoverCursorFormat)
				if (this.mode === 'top') {
					y = this.$refs['canvasRef'].height - 5
				} else if (this.mode === 'center') {
					y = this.yAxisCancelNum(parseInt(this.hoverCursorFont)) + 55
				} else if (this.mode === 'bottom') {
					y = this.yAxisCancelNum(parseInt(this.hoverCursorFont)) - 60
				} else if (this.mode === 'center-triangle') {
					y = this.yAxisCancelNum(parseInt(this.hoverCursorFont)) - 25
				}


				this.ctxRef.fillText(
					text,
					// posX * 1 * this.ratio + (offsetX * this.ratio),
					(posX * 1 + offsetX) * this.ratio,
					y
				);
			},

			calcRadioFont(font) {
				let arr = font.split(' ')
				for (let i = 0; i < arr.length; i++) {
					let item = arr[i]
					let num = parseInt(item)
					if (num) {
						arr[i] = num * this.radio + 'px'
					}
				}
				return arr.join(' ')
			},
			getMousePosTime(event) {
				let posX = this.getMouseXRelativePos(event) * this.ratio;
				// 每像素多少毫秒
				let onePxsMS =
					this.$refs['canvasRef'].width / (this.totalRuler * this.totalRulerMultiple);
				let time = new Date(this.startTimestamp + posX / onePxsMS);
				return time;
			},
			clickEvent(event) {
				let time = this.getMousePosTime(event).getTime();
			},
			wheelEvent(event) {
				event.preventDefault();
				// 是放大一倍还是缩小一倍
				let delta = Math.max(-1, Math.min(1, event.wheelDelta));
				if (delta < 0) {
					this.zoom = this.zoom + this.zoomStep;
					if (this.zoom >= 24) {
						//放大最大24小时
						this.zoom = 24;
					}
					this.totalRuler = this.zoom;
				} else if (delta > 0) {
					// 放大
					this.zoom = this.zoom - this.zoomStep;
					if (this.zoom <= 1) {
						// 缩小最小1小时
						// 缩小最小 1 分钟
						this.zoom = 1;
					}
					this.totalRuler = this.zoom;
				}
				this.calcTotalRulerMultiple()
				this.init();
			},
			calcTotalRulerMultiple() {
				if (this.totalRuler > 1) {
					this.totalRulerMultiple = 60 * 60 * 1000
				} else {
					this.totalRulerMultiple = 60 * 1000
				}
			},
			getMouseXRelativePos(event) {
				let scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
				let x = event.pageX || event.clientX + scrollX;
				// canvasRef.value元素距离窗口左侧距离
				let baseLeft = this.$refs['canvasRef'].getBoundingClientRect().x;
				return x - baseLeft;
			},
			wheel(event) {
				this.wheelEvent(event);
				this.hoverMove(event);
			},
			mousedown(event) {
				this.isMouseDownFlag = true;
				this.mousedownX = this.getMouseXRelativePos(event);
			},
			mousedblclick(event) {
				const t = this.getMousePosTime(event);
				if (this.timePartPrevent && t < this.timeParts[0].start) {
					this.$emit('on-time-prevent')
					this.$emit('on-time-under')
					return
				}
				if (this.timePartPrevent && t > this.timeParts[0].end) {
					this.$emit('on-time-prevent')
					this.$emit('on-time-above')
					return
				}
				this.currentTime = t
				this.init()
			},
			mousemove(event) {
				if (this.isMouseDownFlag) {
					this.isDragFlag = true;
					this.dragMove(event);
				} else {
					this.hoverMove(event);
				}
			},
			mouseup(event) {
				if (!this.isDragFlag) {
					this.clickEvent(event);
					this.hoverMove(event);
				}
				this.$emit("on-change", new Date(this.currentTime), this.currentTime);
				this.$emit('input', this.currentTime)
				// 初始化这俩值以免影响下次事件判断
				this.isMouseDownFlag = false;
				this.isDragFlag = false;
			},
			mouseleave(event) {
				this.init();
				// 初始化这俩值以免影响下次事件判断
				this.isMouseDownFlag = false;
				this.isDragFlag = false;
			},
		}

	}
	// watchEffect(() => {
	//   if (props.currentTime) {
	//     //this.currentTime = props.currentTime;
	//   }
	//   if (props.timeParts) {
	//     this.timeParts = props.timeParts;
	//   }
	//   setIsMove(props.isMove);
	// });

</script>


<!--<style lang="less" scoped>-->
<!--	.jpx-slider-timeline-container {-->
<!--		height: 100%;-->
<!--		position: relative;-->

<!--		canvas {-->
<!--			width: 100%;-->
<!--			height: 100%;-->
<!--			background: #ebeef5;-->
<!--		}-->
<!--	}-->
<!--</style>-->
