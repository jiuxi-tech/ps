/*!
 * Point
 * (c) 2021 lincong1987
 */

//构造点类
export class Point {

	// 可调参数
	static POINT_COLOR = 'rgba(255,255,255,0.7)'  // 点的颜色

	//随机数函数
	static randomInt (min, max) {
		return Math.floor((max - min + 1) * Math.random() + min)
	}

	static randomFloat (min, max) {
		return (max - min) * Math.random() + min
	}

	constructor (options) {

		this.options = options
		this.el = options.el
		this.ctx = this.el.getContext('2d')

		this.width = options.width
		this.height = options.height

		this.x = Point.randomFloat(0, this.width)
		this.y = Point.randomFloat(0, this.height)

		let speed = Point.randomFloat(0.3, 1.4)
		let angle = Point.randomFloat(0, 2 * Math.PI)

		this.dx = Math.sin(angle) * speed
		this.dy = Math.cos(angle) * speed

		this.r = 1.2

		this.color = this.options.pointColor || Point.POINT_COLOR
		console.log(this.x, this.y)
	}

	move () {
		this.x += this.dx
		if (this.x < 0) {
			this.x = 0
			this.dx = -this.dx
		} else if (this.x > this.width) {
			this.x = this.width
			this.dx = -this.dx
		}
		this.y += this.dy
		if (this.y < 0) {
			this.y = 0
			this.dy = -this.dy
		} else if (this.y > this.height) {
			this.y = this.height
			this.dy = -this.dy
		}
	}

	draw () {
		this.ctx.fillStyle = this.color
		this.ctx.beginPath()
		this.ctx.arc(this.x, this.y, this.r, 0, Math.PI * 2)
		this.ctx.closePath()
		this.ctx.fill()
	}

	remove () {
		this.ctx.reset()
		this.el = null
		this.options = null
	}

}

export class Points {

	static BACKGROUND = 'rgba(0,43,54,1)'   // 背景颜色
	static POINTS_NUM = 100                        // 星星数目
	static LINE_LENGTH = 10000                    // 点之间连线长度(的平方)
	static POINT_COLOR = 'rgba(255,255,255,0.7)'  // 点的颜色

	constructor (options) {
		this.points = []
		this.options = options
		this.el = options.el
		this.height = options.height
		this.width = options.width
		this.ctx = this.el.getContext('2d')
		this.lineLength = options.lineLength || Points.LINE_LENGTH
		this.pointsNum = options.pointsNum || Points.POINTS_NUM
		this.background = options.background || Points.BACKGROUND
		this.pointColor = options.pointColor || Points.POINT_COLOR

		this.timer = null

		this.p0 = new Point({
			el: this.el,
			pointColor: this.pointColor,
			height: this.height,
			width: this.width,
		})

		this.p0.dx = this.p0.dy = 0
		this.degree = 2.5

		//this.attachEvents()

		if (options.pointsNum) {
			this.initPoints(options.pointsNum)
			this.drawFrame()
		}

	}

	initPoints (num) {
		for (let i = 0; i < num; ++i) {
			this.points.push(new Point({
				el: this.el,
				pointColor: this.pointColor,
				height: this.height,
				width: this.width,
			}))
		}
	}

	mousemove (ev) {
		this.p0.x = ev.clientX
		this.p0.y = ev.clientY
	}

	mousedown (ev) {
		this.degree = 5.0
		this.p0.x = ev.clientX
		this.p0.y = ev.clientY
	}

	mouseup (ev) {
		this.degree = 2.5
		this.p0.x = ev.clientX
		this.p0.y = ev.clientY
	}

	mouseout () {
		this.p0.x = null
		this.p0.y = null
	}

	attachEvents () {
		document.addEventListener('mousemove', this.mousemove, false)
		document.addEventListener('mousedown', this.mousedown, false)
		document.addEventListener('mouseup', this.mouseup, false)
		window.addEventListener('mouseout', this.mouseout, false)
	}

	removeEvents () {
		document.removeEventListener('mousemove', this.mousemove, false)
		document.removeEventListener('mousedown', this.mousedown, false)
		document.removeEventListener('mouseup', this.mouseup, false)
		window.removeEventListener('mouseout', this.mouseout, false)

		window.cancelAnimationFrame(this.timer)
	}

	remove () {
		window.cancelAnimationFrame(this.timer)
		this.removeEvents()
		this.points = []
		this.ctx.reset()
	}

	drawLine (p1, p2, deg) {
		let dx = p1.x - p2.x
		let dy = p1.y - p2.y
		let dis2 = dx * dx + dy * dy
		if (dis2 < 2 * this.lineLength) {
			if (dis2 > this.lineLength) {
				if (p1 === this.p0) {
					p2.x += dx * 0.03
					p2.y += dy * 0.03
				} else return
			}
			let t = (1.05 - dis2 / this.lineLength) * 0.2 * deg
			this.ctx.strokeStyle = 'rgba(255,255,255,' + t + ')'
			this.ctx.beginPath()
			this.ctx.lineWidth = 1.5
			this.ctx.moveTo(p1.x, p1.y)
			this.ctx.lineTo(p2.x, p2.y)
			this.ctx.closePath()
			this.ctx.stroke()
		}
	}

	drawFrame () {

		this.ctx.fillStyle = "rgba(0,0,0,0)"
		this.ctx.fillRect(0, 0, this.width, this.height)

		let arr = (this.p0.x == null ? this.points : [this.p0].concat(
			this.points))
		for (let i = 0; i < arr.length; ++i) {
			for (let j = i + 1; j < arr.length; ++j) {
				this.drawLine(arr[i], arr[j], 1.0)
			}
			arr[i] && arr[i].draw()
			arr[i] && arr[i].move()
		}

		this.timer = window.requestAnimationFrame(() => {
			this.drawFrame()
		})
	}

}

export default {
	Points,
	Point,
}
