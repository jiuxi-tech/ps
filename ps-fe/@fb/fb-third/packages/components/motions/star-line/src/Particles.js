/*!
 * particles-js
 * (c) 2021 lincong1987
 */

/* -----------------------------------------------
 /* Author : Vincent Garreau  - vincentgarreau.com
 /* MIT license: http://opensource.org/licenses/MIT
 /* Demo / Generator : vincentgarreau.com/particles.js
 /* GitHub : github.com/VincentGarreau/particles.js
 /* How to use? : Check the GitHub README
 /* v2.0.0
 /* ----------------------------------------------- */

export function Particles (el, params) {

	var canvas_el = el//document.querySelector('#'+tag_id+' >' +'
	                   // .particles-js-canvas-el');

	/* particles.js variables with default values */
	this.Particles = {
		canvas: {
			el: canvas_el,
			w: canvas_el.offsetWidth,
			h: canvas_el.offsetHeight,
		},
		particles: {
			number: {
				value: 400,
				density: {
					enable: true,
					value_area: 800,
				},
			},
			color: {
				value: '#fff',
			},
			shape: {
				type: 'circle',
				stroke: {
					width: 0,
					color: '#ff0000',
				},
				polygon: {
					nb_sides: 5,
				},
				image: {
					src: '',
					width: 100,
					height: 100,
				},
			},
			opacity: {
				value: 1,
				random: false,
				anim: {
					enable: false,
					speed: 2,
					opacity_min: 0,
					sync: false,
				},
			},
			size: {
				value: 20,
				random: false,
				anim: {
					enable: false,
					speed: 20,
					size_min: 0,
					sync: false,
				},
			},
			line_linked: {
				enable: true,
				distance: 100,
				color: '#fff',
				opacity: 1,
				width: 1,
			},
			move: {
				enable: true,
				speed: 2,
				direction: 'none',
				random: false,
				straight: false,
				out_mode: 'out',
				bounce: false,
				attract: {
					enable: false,
					rotateX: 3000,
					rotateY: 3000,
				},
			},
			array: [],
		},
		interactivity: {
			detect_on: 'canvas',
			events: {
				onhover: {
					enable: true,
					mode: 'grab',
				},
				onclick: {
					enable: true,
					mode: 'push',
				},
				resize: true,
			},
			modes: {
				grab: {
					distance: 100,
					line_linked: {
						opacity: 1,
					},
				},
				bubble: {
					distance: 200,
					size: 80,
					duration: 0.7,
				},
				repulse: {
					distance: 200,
					duration: 0.4,
				},
				push: {
					particles_nb: 4,
				},
				remove: {
					particles_nb: 2,
				},
			},
			mouse: {},
		},
		retina_detect: false,
		fn: {
			interact: {},
			modes: {},
			vendors: {},
		},
		tmp: {},
	}

	var Particles = this.Particles

	/* params settings */
	if (params) {
		Object.deepExtend(Particles, params)
	}

	Particles.tmp.obj = {
		size_value: Particles.particles.size.value,
		size_anim_speed: Particles.particles.size.anim.speed,
		move_speed: Particles.particles.move.speed,
		line_linked_distance: Particles.particles.line_linked.distance,
		line_linked_width: Particles.particles.line_linked.width,
		mode_grab_distance: Particles.interactivity.modes.grab.distance,
		mode_bubble_distance: Particles.interactivity.modes.bubble.distance,
		mode_bubble_size: Particles.interactivity.modes.bubble.size,
		mode_repulse_distance: Particles.interactivity.modes.repulse.distance,
	}

	Particles.fn.retinaInit = function () {

		if (Particles.retina_detect && window.devicePixelRatio > 1) {
			Particles.canvas.pxratio = window.devicePixelRatio
			Particles.tmp.retina = true
		} else {
			Particles.canvas.pxratio = 1
			Particles.tmp.retina = false
		}

		Particles.canvas.w = Particles.canvas.el.offsetWidth * Particles.canvas.pxratio
		Particles.canvas.h = Particles.canvas.el.offsetHeight * Particles.canvas.pxratio

		Particles.particles.size.value = Particles.tmp.obj.size_value * Particles.canvas.pxratio
		Particles.particles.size.anim.speed =
			Particles.tmp.obj.size_anim_speed * Particles.canvas.pxratio
		Particles.particles.move.speed = Particles.tmp.obj.move_speed * Particles.canvas.pxratio
		Particles.particles.line_linked.distance =
			Particles.tmp.obj.line_linked_distance * Particles.canvas.pxratio
		Particles.interactivity.modes.grab.distance =
			Particles.tmp.obj.mode_grab_distance * Particles.canvas.pxratio
		Particles.interactivity.modes.bubble.distance =
			Particles.tmp.obj.mode_bubble_distance * Particles.canvas.pxratio
		Particles.particles.line_linked.width =
			Particles.tmp.obj.line_linked_width * Particles.canvas.pxratio
		Particles.interactivity.modes.bubble.size =
			Particles.tmp.obj.mode_bubble_size * Particles.canvas.pxratio
		Particles.interactivity.modes.repulse.distance =
			Particles.tmp.obj.mode_repulse_distance * Particles.canvas.pxratio

	}

	/* ---------- Particles functions - canvas ------------ */

	Particles.fn.canvasInit = function () {
		Particles.canvas.ctx = Particles.canvas.el.getContext('2d')
	}

	Particles.fn.canvasSize = function () {

		Particles.canvas.el.width = Particles.canvas.w
		Particles.canvas.el.height = Particles.canvas.h

		if (Particles && Particles.interactivity.events.resize) {

			window.addEventListener('resize', function () {

				Particles.canvas.w = Particles.canvas.el.offsetWidth
				Particles.canvas.h = Particles.canvas.el.offsetHeight

				/* resize canvas */
				if (Particles.tmp.retina) {
					Particles.canvas.w *= Particles.canvas.pxratio
					Particles.canvas.h *= Particles.canvas.pxratio
				}

				Particles.canvas.el.width = Particles.canvas.w
				Particles.canvas.el.height = Particles.canvas.h

				/* repaint canvas on anim disabled */
				if (!Particles.particles.move.enable) {
					Particles.fn.particlesEmpty()
					Particles.fn.particlesCreate()
					Particles.fn.particlesDraw()
					Particles.fn.vendors.densityAutoParticles()
				}

				/* density particles enabled */
				Particles.fn.vendors.densityAutoParticles()

			})

		}

	}

	Particles.fn.canvasPaint = function () {
		Particles.canvas.ctx.fillRect(0, 0, Particles.canvas.w, Particles.canvas.h)
	}

	Particles.fn.canvasClear = function () {
		Particles.canvas.ctx.clearRect(0, 0, Particles.canvas.w, Particles.canvas.h)
	}

	/* --------- Particles functions - particles ----------- */

	Particles.fn.particle = function (color, opacity, position) {

		/* size */
		this.radius = (Particles.particles.size.random ? Math.random() : 1) *
			Particles.particles.size.value
		if (Particles.particles.size.anim.enable) {
			this.size_status = false
			this.vs = Particles.particles.size.anim.speed / 100
			if (!Particles.particles.size.anim.sync) {
				this.vs = this.vs * Math.random()
			}
		}

		/* position */
		this.x = position ? position.x : Math.random() * Particles.canvas.w
		this.y = position ? position.y : Math.random() * Particles.canvas.h

		/* check position  - into the canvas */
		if (this.x > Particles.canvas.w - this.radius * 2) this.x =
			this.x - this.radius
		else if (this.x < this.radius * 2) this.x = this.x + this.radius
		if (this.y > Particles.canvas.h - this.radius * 2) this.y =
			this.y - this.radius
		else if (this.y < this.radius * 2) this.y = this.y + this.radius

		/* check position - avoid overlap */
		if (Particles.particles.move.bounce) {
			Particles.fn.vendors.checkOverlap(this, position)
		}

		/* color */
		this.color = {}
		if (typeof (color.value) == 'object') {

			if (color.value instanceof Array) {
				var color_selected = color.value[Math.floor(
					Math.random() * Particles.particles.color.value.length)]
				this.color.rgb = hexToRgb(color_selected)
			} else {
				if (color.value.r != undefined && color.value.g != undefined &&
					color.value.b != undefined) {
					this.color.rgb = {
						r: color.value.r,
						g: color.value.g,
						b: color.value.b,
					}
				}
				if (color.value.h != undefined && color.value.s != undefined &&
					color.value.l != undefined) {
					this.color.hsl = {
						h: color.value.h,
						s: color.value.s,
						l: color.value.l,
					}
				}
			}

		} else if (color.value == 'random') {
			this.color.rgb = {
				r: (Math.floor(Math.random() * (255 - 0 + 1)) + 0),
				g: (Math.floor(Math.random() * (255 - 0 + 1)) + 0),
				b: (Math.floor(Math.random() * (255 - 0 + 1)) + 0),
			}
		} else if (typeof (color.value) == 'string') {
			this.color = color
			this.color.rgb = hexToRgb(this.color.value)
		}

		/* opacity */
		this.opacity = (Particles.particles.opacity.random ? Math.random() : 1) *
			Particles.particles.opacity.value
		if (Particles.particles.opacity.anim.enable) {
			this.opacity_status = false
			this.vo = Particles.particles.opacity.anim.speed / 100
			if (!Particles.particles.opacity.anim.sync) {
				this.vo = this.vo * Math.random()
			}
		}

		/* animation - velocity for speed */
		var velbase = {}
		switch (Particles.particles.move.direction) {
			case 'top':
				velbase = {
					x: 0,
					y: -1,
				}
				break
			case 'top-right':
				velbase = {
					x: 0.5,
					y: -0.5,
				}
				break
			case 'right':
				velbase = {
					x: 1,
					y: -0,
				}
				break
			case 'bottom-right':
				velbase = {
					x: 0.5,
					y: 0.5,
				}
				break
			case 'bottom':
				velbase = {
					x: 0,
					y: 1,
				}
				break
			case 'bottom-left':
				velbase = {
					x: -0.5,
					y: 1,
				}
				break
			case 'left':
				velbase = {
					x: -1,
					y: 0,
				}
				break
			case 'top-left':
				velbase = {
					x: -0.5,
					y: -0.5,
				}
				break
			default:
				velbase = {
					x: 0,
					y: 0,
				}
				break
		}

		if (Particles.particles.move.straight) {
			this.vx = velbase.x
			this.vy = velbase.y
			if (Particles.particles.move.random) {
				this.vx = this.vx * (Math.random())
				this.vy = this.vy * (Math.random())
			}
		} else {
			this.vx = velbase.x + Math.random() - 0.5
			this.vy = velbase.y + Math.random() - 0.5
		}

		// var theta = 2.0 * Math.PI * Math.random();
		// this.vx = Math.cos(theta);
		// this.vy = Math.sin(theta);

		this.vx_i = this.vx
		this.vy_i = this.vy

		/* if shape is image */

		var shape_type = Particles.particles.shape.type
		if (typeof (shape_type) == 'object') {
			if (shape_type instanceof Array) {
				var shape_selected = shape_type[Math.floor(
					Math.random() * shape_type.length)]
				this.shape = shape_selected
			}
		} else {
			this.shape = shape_type
		}

		if (this.shape == 'image') {
			var sh = Particles.particles.shape
			this.img = {
				src: sh.image.src,
				ratio: sh.image.width / sh.image.height,
			}
			if (!this.img.ratio) this.img.ratio = 1
			if (Particles.tmp.img_type == 'svg' && Particles.tmp.source_svg != undefined) {
				Particles.fn.vendors.createSvgImg(this)
				if (Particles.tmp.pushing) {
					this.img.loaded = false
				}
			}
		}

	}

	Particles.fn.particle.prototype.draw = function () {

		var p = this

		if (p.radius_bubble != undefined) {
			var radius = p.radius_bubble
		} else {
			var radius = p.radius
		}

		if (p.opacity_bubble != undefined) {
			var opacity = p.opacity_bubble
		} else {
			var opacity = p.opacity
		}

		if (p.color.rgb) {
			var color_value = 'rgba(' + p.color.rgb.r + ',' + p.color.rgb.g +
				',' + p.color.rgb.b + ',' + opacity + ')'
		} else {
			var color_value = 'hsla(' + p.color.hsl.h + ',' + p.color.hsl.s +
				'%,' + p.color.hsl.l + '%,' + opacity + ')'
		}

		Particles.canvas.ctx.fillStyle = color_value
		Particles.canvas.ctx.beginPath()

		switch (p.shape) {

			case 'circle':
				Particles.canvas.ctx.arc(p.x, p.y, radius, 0, Math.PI * 2, false)
				break

			case 'edge':
				Particles.canvas.ctx.rect(p.x - radius, p.y - radius, radius * 2,
					radius * 2)
				break

			case 'triangle':
				Particles.fn.vendors.drawShape(Particles.canvas.ctx, p.x - radius,
					p.y + radius / 1.66, radius * 2, 3, 2)
				break

			case 'polygon':
				Particles.fn.vendors.drawShape(
					Particles.canvas.ctx,
					p.x - radius / (Particles.particles.shape.polygon.nb_sides / 3.5), // startX
					p.y - radius / (2.66 / 3.5), // startY
					radius * 2.66 / (Particles.particles.shape.polygon.nb_sides / 3), // sideLength
					Particles.particles.shape.polygon.nb_sides, // sideCountNumerator
					1, // sideCountDenominator
				)
				break

			case 'star':
				Particles.fn.vendors.drawShape(
					Particles.canvas.ctx,
					p.x - radius * 2 /
					(Particles.particles.shape.polygon.nb_sides / 4), // startX
					p.y - radius / (2 * 2.66 / 3.5), // startY
					radius * 2 * 2.66 /
					(Particles.particles.shape.polygon.nb_sides / 3), // sideLength
					Particles.particles.shape.polygon.nb_sides, // sideCountNumerator
					2, // sideCountDenominator
				)
				break

			case 'image':

			function draw () {
				Particles.canvas.ctx.drawImage(
					img_obj,
					p.x - radius,
					p.y - radius,
					radius * 2,
					radius * 2 / p.img.ratio,
				)
			}

				if (Particles.tmp.img_type == 'svg') {
					var img_obj = p.img.obj
				} else {
					var img_obj = Particles.tmp.img_obj
				}

				if (img_obj) {
					draw()
				}

				break

		}

		Particles.canvas.ctx.closePath()

		if (Particles.particles.shape.stroke.width > 0) {
			Particles.canvas.ctx.strokeStyle = Particles.particles.shape.stroke.color
			Particles.canvas.ctx.lineWidth = Particles.particles.shape.stroke.width
			Particles.canvas.ctx.stroke()
		}

		Particles.canvas.ctx.fill()

	}

	Particles.fn.particlesCreate = function () {
		for (var i = 0; i < Particles.particles.number.value; i++) {
			Particles.particles.array.push(new Particles.fn.particle(Particles.particles.color,
				Particles.particles.opacity.value))
		}
	}

	Particles.fn.particlesUpdate = function () {

		for (var i = 0; i < Particles.particles.array.length; i++) {

			/* the particle */
			var p = Particles.particles.array[i]

			// var d = ( dx = Particles.interactivity.mouse.click_pos_x - p.x ) * dx
			// + ( dy = Particles.interactivity.mouse.click_pos_y - p.y ) * dy; var f
			// = -BANG_SIZE / d; if ( d < BANG_SIZE ) { var t = Math.atan2( dy,
			// dx ); p.vx = f * Math.cos(t); p.vy = f * Math.sin(t); }

			/* move the particle */
			if (Particles.particles.move.enable) {
				var ms = Particles.particles.move.speed / 2
				p.x += p.vx * ms
				p.y += p.vy * ms
			}

			/* change opacity status */
			if (Particles.particles.opacity.anim.enable) {
				if (p.opacity_status == true) {
					if (p.opacity >=
						Particles.particles.opacity.value) p.opacity_status = false
					p.opacity += p.vo
				} else {
					if (p.opacity <=
						Particles.particles.opacity.anim.opacity_min) p.opacity_status =
						true
					p.opacity -= p.vo
				}
				if (p.opacity < 0) p.opacity = 0
			}

			/* change size */
			if (Particles.particles.size.anim.enable) {
				if (p.size_status == true) {
					if (p.radius >= Particles.particles.size.value) p.size_status =
						false
					p.radius += p.vs
				} else {
					if (p.radius <=
						Particles.particles.size.anim.size_min) p.size_status = true
					p.radius -= p.vs
				}
				if (p.radius < 0) p.radius = 0
			}

			/* change particle position if it is out of canvas */
			if (Particles.particles.move.out_mode == 'bounce') {
				var new_pos = {
					x_left: p.radius,
					x_right: Particles.canvas.w,
					y_top: p.radius,
					y_bottom: Particles.canvas.h,
				}
			} else {
				var new_pos = {
					x_left: -p.radius,
					x_right: Particles.canvas.w + p.radius,
					y_top: -p.radius,
					y_bottom: Particles.canvas.h + p.radius,
				}
			}

			if (p.x - p.radius > Particles.canvas.w) {
				p.x = new_pos.x_left
				p.y = Math.random() * Particles.canvas.h
			} else if (p.x + p.radius < 0) {
				p.x = new_pos.x_right
				p.y = Math.random() * Particles.canvas.h
			}
			if (p.y - p.radius > Particles.canvas.h) {
				p.y = new_pos.y_top
				p.x = Math.random() * Particles.canvas.w
			} else if (p.y + p.radius < 0) {
				p.y = new_pos.y_bottom
				p.x = Math.random() * Particles.canvas.w
			}

			/* out of canvas modes */
			switch (Particles.particles.move.out_mode) {
				case 'bounce':
					if (p.x + p.radius > Particles.canvas.w) p.vx = -p.vx
					else if (p.x - p.radius < 0) p.vx = -p.vx
					if (p.y + p.radius > Particles.canvas.h) p.vy = -p.vy
					else if (p.y - p.radius < 0) p.vy = -p.vy
					break
			}

			/* events */
			if (isInArray('grab', Particles.interactivity.events.onhover.mode)) {
				Particles.fn.modes.grabParticle(p)
			}

			if (isInArray('bubble', Particles.interactivity.events.onhover.mode) ||
				isInArray('bubble', Particles.interactivity.events.onclick.mode)) {
				Particles.fn.modes.bubbleParticle(p)
			}

			if (isInArray('repulse', Particles.interactivity.events.onhover.mode) ||
				isInArray('repulse', Particles.interactivity.events.onclick.mode)) {
				Particles.fn.modes.repulseParticle(p)
			}

			/* interaction auto between particles */
			if (Particles.particles.line_linked.enable ||
				Particles.particles.move.attract.enable) {
				for (var j = i + 1; j < Particles.particles.array.length; j++) {
					var p2 = Particles.particles.array[j]

					/* link particles */
					if (Particles.particles.line_linked.enable) {
						Particles.fn.interact.linkParticles(p, p2)
					}

					/* attract particles */
					if (Particles.particles.move.attract.enable) {
						Particles.fn.interact.attractParticles(p, p2)
					}

					/* bounce particles */
					if (Particles.particles.move.bounce) {
						Particles.fn.interact.bounceParticles(p, p2)
					}

				}
			}

		}

	}

	Particles.fn.particlesDraw = function () {

		/* clear canvas */
		Particles.canvas.ctx.clearRect(0, 0, Particles.canvas.w, Particles.canvas.h)

		/* update each particles param */
		Particles.fn.particlesUpdate()

		/* draw each particle */
		for (var i = 0; i < Particles.particles.array.length; i++) {
			var p = Particles.particles.array[i]
			p.draw()
		}

	}

	Particles.fn.particlesEmpty = function () {
		Particles.particles.array = []
	}

	Particles.fn.particlesRefresh = function () {

		/* init all */
		cancelRequestAnimFrame(Particles.fn.checkAnimFrame)
		cancelRequestAnimFrame(Particles.fn.drawAnimFrame)
		Particles.tmp.source_svg = undefined
		Particles.tmp.img_obj = undefined
		Particles.tmp.count_svg = 0
		Particles.fn.particlesEmpty()
		Particles.fn.canvasClear()

		/* restart */
		Particles.fn.vendors.start()

	}

	/* ---------- Particles functions - particles interaction ------------ */

	Particles.fn.interact.linkParticles = function (p1, p2) {

		var dx = p1.x - p2.x,
			dy = p1.y - p2.y,
			dist = Math.sqrt(dx * dx + dy * dy)

		/* draw a line between p1 and p2 if the distance between them is under the config distance */
		if (dist <= Particles.particles.line_linked.distance) {

			var opacity_line = Particles.particles.line_linked.opacity -
				(dist / (1 / Particles.particles.line_linked.opacity)) /
				Particles.particles.line_linked.distance

			if (opacity_line > 0) {

				/* style */
				var color_line = Particles.particles.line_linked.color_rgb_line
				Particles.canvas.ctx.strokeStyle =
					'rgba(' + color_line.r + ',' + color_line.g + ',' +
					color_line.b + ',' + opacity_line + ')'
				Particles.canvas.ctx.lineWidth = Particles.particles.line_linked.width
				//Particles.canvas.ctx.lineCap = 'round'; /* performance issue */

				/* path */
				Particles.canvas.ctx.beginPath()
				Particles.canvas.ctx.moveTo(p1.x, p1.y)
				Particles.canvas.ctx.lineTo(p2.x, p2.y)
				Particles.canvas.ctx.stroke()
				Particles.canvas.ctx.closePath()

			}

		}

	}

	Particles.fn.interact.attractParticles = function (p1, p2) {

		/* condensed particles */
		var dx = p1.x - p2.x,
			dy = p1.y - p2.y,
			dist = Math.sqrt(dx * dx + dy * dy)

		if (dist <= Particles.particles.line_linked.distance) {

			var ax = dx / (Particles.particles.move.attract.rotateX * 1000),
				ay = dy / (Particles.particles.move.attract.rotateY * 1000)

			p1.vx -= ax
			p1.vy -= ay

			p2.vx += ax
			p2.vy += ay

		}

	}

	Particles.fn.interact.bounceParticles = function (p1, p2) {

		var dx = p1.x - p2.x,
			dy = p1.y - p2.y,
			dist = Math.sqrt(dx * dx + dy * dy),
			dist_p = p1.radius + p2.radius

		if (dist <= dist_p) {
			p1.vx = -p1.vx
			p1.vy = -p1.vy

			p2.vx = -p2.vx
			p2.vy = -p2.vy
		}

	}

	/* ---------- Particles functions - modes events ------------ */

	Particles.fn.modes.pushParticles = function (nb, pos) {

		Particles.tmp.pushing = true

		for (var i = 0; i < nb; i++) {
			Particles.particles.array.push(
				new Particles.fn.particle(
					Particles.particles.color,
					Particles.particles.opacity.value,
					{
						'x': pos ? pos.pos_x : Math.random() * Particles.canvas.w,
						'y': pos ? pos.pos_y : Math.random() * Particles.canvas.h,
					},
				),
			)
			if (i == nb - 1) {
				if (!Particles.particles.move.enable) {
					Particles.fn.particlesDraw()
				}
				Particles.tmp.pushing = false
			}
		}

	}

	Particles.fn.modes.removeParticles = function (nb) {

		Particles.particles.array.splice(0, nb)
		if (!Particles.particles.move.enable) {
			Particles.fn.particlesDraw()
		}

	}

	Particles.fn.modes.bubbleParticle = function (p) {

		/* on hover event */
		if (Particles.interactivity.events.onhover.enable &&
			isInArray('bubble', Particles.interactivity.events.onhover.mode)) {

			var dx_mouse = p.x - Particles.interactivity.mouse.pos_x,
				dy_mouse = p.y - Particles.interactivity.mouse.pos_y,
				dist_mouse = Math.sqrt(
					dx_mouse * dx_mouse + dy_mouse * dy_mouse),
				ratio = 1 - dist_mouse /
					Particles.interactivity.modes.bubble.distance

			function init () {
				p.opacity_bubble = p.opacity
				p.radius_bubble = p.radius
			}

			/* mousemove - check ratio */
			if (dist_mouse <= Particles.interactivity.modes.bubble.distance) {

				if (ratio >= 0 && Particles.interactivity.status == 'mousemove') {

					/* size */
					if (Particles.interactivity.modes.bubble.size !=
						Particles.particles.size.value) {

						if (Particles.interactivity.modes.bubble.size >
							Particles.particles.size.value) {
							var size = p.radius +
								(Particles.interactivity.modes.bubble.size * ratio)
							if (size >= 0) {
								p.radius_bubble = size
							}
						} else {
							var dif = p.radius -
									Particles.interactivity.modes.bubble.size,
								size = p.radius - (dif * ratio)
							if (size > 0) {
								p.radius_bubble = size
							} else {
								p.radius_bubble = 0
							}
						}

					}

					/* opacity */
					if (Particles.interactivity.modes.bubble.opacity !=
						Particles.particles.opacity.value) {

						if (Particles.interactivity.modes.bubble.opacity >
							Particles.particles.opacity.value) {
							var opacity = Particles.interactivity.modes.bubble.opacity *
								ratio
							if (opacity > p.opacity && opacity <=
								Particles.interactivity.modes.bubble.opacity) {
								p.opacity_bubble = opacity
							}
						} else {
							var opacity = p.opacity -
								(Particles.particles.opacity.value -
									Particles.interactivity.modes.bubble.opacity) *
								ratio
							if (opacity < p.opacity && opacity >=
								Particles.interactivity.modes.bubble.opacity) {
								p.opacity_bubble = opacity
							}
						}

					}

				}

			} else {
				init()
			}

			/* mouseleave */
			if (Particles.interactivity.status == 'mouseleave') {
				init()
			}

		}

		/* on click event */
		else if (Particles.interactivity.events.onclick.enable &&
			isInArray('bubble', Particles.interactivity.events.onclick.mode)) {

			if (Particles.tmp.bubble_clicking) {
				var dx_mouse = p.x - Particles.interactivity.mouse.click_pos_x,
					dy_mouse = p.y - Particles.interactivity.mouse.click_pos_y,
					dist_mouse = Math.sqrt(
						dx_mouse * dx_mouse + dy_mouse * dy_mouse),
					time_spent = (new Date().getTime() -
						Particles.interactivity.mouse.click_time) / 1000

				if (time_spent > Particles.interactivity.modes.bubble.duration) {
					Particles.tmp.bubble_duration_end = true
				}

				if (time_spent > Particles.interactivity.modes.bubble.duration * 2) {
					Particles.tmp.bubble_clicking = false
					Particles.tmp.bubble_duration_end = false
				}
			}

			function process (
				bubble_param, particles_param, p_obj_bubble, p_obj, id) {

				if (bubble_param != particles_param) {

					if (!Particles.tmp.bubble_duration_end) {
						if (dist_mouse <=
							Particles.interactivity.modes.bubble.distance) {
							if (p_obj_bubble !=
								undefined) var obj = p_obj_bubble
							else var obj = p_obj
							if (obj != bubble_param) {
								var value = p_obj -
									(time_spent * (p_obj - bubble_param) /
										Particles.interactivity.modes.bubble.duration)
								if (id == 'size') p.radius_bubble = value
								if (id == 'opacity') p.opacity_bubble = value
							}
						} else {
							if (id == 'size') p.radius_bubble = undefined
							if (id == 'opacity') p.opacity_bubble = undefined
						}
					} else {
						if (p_obj_bubble != undefined) {
							var value_tmp = p_obj -
									(time_spent * (p_obj - bubble_param) /
										Particles.interactivity.modes.bubble.duration),
								dif = bubble_param - value_tmp
							value = bubble_param + dif
							if (id == 'size') p.radius_bubble = value
							if (id == 'opacity') p.opacity_bubble = value
						}
					}

				}

			}

			if (Particles.tmp.bubble_clicking) {
				/* size */
				process(Particles.interactivity.modes.bubble.size,
					Particles.particles.size.value, p.radius_bubble, p.radius,
					'size')
				/* opacity */
				process(Particles.interactivity.modes.bubble.opacity,
					Particles.particles.opacity.value, p.opacity_bubble, p.opacity,
					'opacity')
			}

		}

	}

	Particles.fn.modes.repulseParticle = function (p) {

		if (Particles.interactivity.events.onhover.enable &&
			isInArray('repulse', Particles.interactivity.events.onhover.mode) &&
			Particles.interactivity.status == 'mousemove') {

			var dx_mouse = p.x - Particles.interactivity.mouse.pos_x,
				dy_mouse = p.y - Particles.interactivity.mouse.pos_y,
				dist_mouse = Math.sqrt(
					dx_mouse * dx_mouse + dy_mouse * dy_mouse)

			var normVec = {
					x: dx_mouse / dist_mouse,
					y: dy_mouse / dist_mouse,
				},
				repulseRadius = Particles.interactivity.modes.repulse.distance,
				velocity = 100,
				repulseFactor = clamp((1 / repulseRadius) *
					(-1 * Math.pow(dist_mouse / repulseRadius, 2) + 1) *
					repulseRadius * velocity, 0, 50)

			var pos = {
				x: p.x + normVec.x * repulseFactor,
				y: p.y + normVec.y * repulseFactor,
			}

			if (Particles.particles.move.out_mode == 'bounce') {
				if (pos.x - p.radius > 0 && pos.x + p.radius <
					Particles.canvas.w) p.x = pos.x
				if (pos.y - p.radius > 0 && pos.y + p.radius <
					Particles.canvas.h) p.y = pos.y
			} else {
				p.x = pos.x
				p.y = pos.y
			}

		} else if (Particles.interactivity.events.onclick.enable &&
			isInArray('repulse', Particles.interactivity.events.onclick.mode)) {

			if (!Particles.tmp.repulse_finish) {
				Particles.tmp.repulse_count++
				if (Particles.tmp.repulse_count == Particles.particles.array.length) {
					Particles.tmp.repulse_finish = true
				}
			}

			if (Particles.tmp.repulse_clicking) {

				var repulseRadius = Math.pow(
					Particles.interactivity.modes.repulse.distance / 6, 3)

				var dx = Particles.interactivity.mouse.click_pos_x - p.x,
					dy = Particles.interactivity.mouse.click_pos_y - p.y,
					d = dx * dx + dy * dy

				var force = -repulseRadius / d * 1

				function process () {

					var f = Math.atan2(dy, dx)
					p.vx = force * Math.cos(f)
					p.vy = force * Math.sin(f)

					if (Particles.particles.move.out_mode == 'bounce') {
						var pos = {
							x: p.x + p.vx,
							y: p.y + p.vy,
						}
						if (pos.x + p.radius > Particles.canvas.w) p.vx = -p.vx
						else if (pos.x - p.radius < 0) p.vx = -p.vx
						if (pos.y + p.radius > Particles.canvas.h) p.vy = -p.vy
						else if (pos.y - p.radius < 0) p.vy = -p.vy
					}

				}

				// default
				if (d <= repulseRadius) {
					process()
				}

				// bang - slow motion mode
				// if(!Particles.tmp.repulse_finish){
				//   if(d <= repulseRadius){
				//     process();
				//   }
				// }else{
				//   process();
				// }

			} else {

				if (Particles.tmp.repulse_clicking == false) {

					p.vx = p.vx_i
					p.vy = p.vy_i

				}

			}

		}

	}

	Particles.fn.modes.grabParticle = function (p) {

		if (Particles.interactivity.events.onhover.enable &&
			Particles.interactivity.status == 'mousemove') {

			var dx_mouse = p.x - Particles.interactivity.mouse.pos_x,
				dy_mouse = p.y - Particles.interactivity.mouse.pos_y,
				dist_mouse = Math.sqrt(
					dx_mouse * dx_mouse + dy_mouse * dy_mouse)

			/* draw a line between the cursor and the particle if the distance between them is under the config distance */
			if (dist_mouse <= Particles.interactivity.modes.grab.distance) {

				var opacity_line = Particles.interactivity.modes.grab.line_linked.opacity -
					(dist_mouse / (1 /
						Particles.interactivity.modes.grab.line_linked.opacity)) /
					Particles.interactivity.modes.grab.distance

				if (opacity_line > 0) {

					/* style */
					var color_line = Particles.particles.line_linked.color_rgb_line
					Particles.canvas.ctx.strokeStyle =
						'rgba(' + color_line.r + ',' + color_line.g + ',' +
						color_line.b + ',' + opacity_line + ')'
					Particles.canvas.ctx.lineWidth = Particles.particles.line_linked.width
					//Particles.canvas.ctx.lineCap = 'round'; /* performance issue */

					/* path */
					Particles.canvas.ctx.beginPath()
					Particles.canvas.ctx.moveTo(p.x, p.y)
					Particles.canvas.ctx.lineTo(Particles.interactivity.mouse.pos_x,
						Particles.interactivity.mouse.pos_y)
					Particles.canvas.ctx.stroke()
					Particles.canvas.ctx.closePath()

				}

			}

		}

	}

	/* ---------- Particles functions - vendors ------------ */

	Particles.fn.vendors.eventsListeners = function () {

		/* events target element */
		if (Particles.interactivity.detect_on == 'window') {
			Particles.interactivity.el = window
		} else {
			Particles.interactivity.el = Particles.canvas.el
		}

		/* detect mouse pos - on hover / click event */
		if (Particles.interactivity.events.onhover.enable ||
			Particles.interactivity.events.onclick.enable) {

			/* el on mousemove */
			Particles.interactivity.el.addEventListener('mousemove', function (e) {

				if (Particles.interactivity.el == window) {
					var pos_x = e.clientX,
						pos_y = e.clientY
				} else {
					var pos_x = e.offsetX || e.clientX,
						pos_y = e.offsetY || e.clientY
				}

				Particles.interactivity.mouse.pos_x = pos_x
				Particles.interactivity.mouse.pos_y = pos_y

				if (Particles.tmp.retina) {
					Particles.interactivity.mouse.pos_x *= Particles.canvas.pxratio
					Particles.interactivity.mouse.pos_y *= Particles.canvas.pxratio
				}

				Particles.interactivity.status = 'mousemove'

			})

			/* el on onmouseleave */
			Particles.interactivity.el.addEventListener('mouseleave', function (e) {

				Particles.interactivity.mouse.pos_x = null
				Particles.interactivity.mouse.pos_y = null
				Particles.interactivity.status = 'mouseleave'

			})

		}

		/* on click event */
		if (Particles.interactivity.events.onclick.enable) {

			Particles.interactivity.el.addEventListener('click', function () {

				Particles.interactivity.mouse.click_pos_x =
					Particles.interactivity.mouse.pos_x
				Particles.interactivity.mouse.click_pos_y =
					Particles.interactivity.mouse.pos_y
				Particles.interactivity.mouse.click_time = new Date().getTime()

				if (Particles.interactivity.events.onclick.enable) {

					switch (Particles.interactivity.events.onclick.mode) {

						case 'push':
							if (Particles.particles.move.enable) {
								Particles.fn.modes.pushParticles(
									Particles.interactivity.modes.push.particles_nb,
									Particles.interactivity.mouse)
							} else {
								if (Particles.interactivity.modes.push.particles_nb ==
									1) {
									Particles.fn.modes.pushParticles(
										Particles.interactivity.modes.push.particles_nb,
										Particles.interactivity.mouse)
								} else if (Particles.interactivity.modes.push.particles_nb >
									1) {
									Particles.fn.modes.pushParticles(
										Particles.interactivity.modes.push.particles_nb)
								}
							}
							break

						case 'remove':
							Particles.fn.modes.removeParticles(
								Particles.interactivity.modes.remove.particles_nb)
							break

						case 'bubble':
							Particles.tmp.bubble_clicking = true
							break

						case 'repulse':
							Particles.tmp.repulse_clicking = true
							Particles.tmp.repulse_count = 0
							Particles.tmp.repulse_finish = false
							setTimeout(function () {
								Particles.tmp.repulse_clicking = false
							}, Particles.interactivity.modes.repulse.duration * 1000)
							break

					}

				}

			})

		}

	}

	Particles.fn.vendors.densityAutoParticles = function () {

		if (Particles.particles.number.density.enable) {

			/* calc area */
			var area = Particles.canvas.el.width * Particles.canvas.el.height / 1000
			if (Particles.tmp.retina) {
				area = area / (Particles.canvas.pxratio * 2)
			}

			/* calc number of particles based on density area */
			var nb_particles = area * Particles.particles.number.value /
				Particles.particles.number.density.value_area

			/* add or remove X particles */
			var missing_particles = Particles.particles.array.length - nb_particles
			if (missing_particles < 0) Particles.fn.modes.pushParticles(
				Math.abs(missing_particles))
			else Particles.fn.modes.removeParticles(missing_particles)

		}

	}

	Particles.fn.vendors.checkOverlap = function (p1, position) {
		for (var i = 0; i < Particles.particles.array.length; i++) {
			var p2 = Particles.particles.array[i]

			var dx = p1.x - p2.x,
				dy = p1.y - p2.y,
				dist = Math.sqrt(dx * dx + dy * dy)

			if (dist <= p1.radius + p2.radius) {
				p1.x = position ? position.x : Math.random() * Particles.canvas.w
				p1.y = position ? position.y : Math.random() * Particles.canvas.h
				Particles.fn.vendors.checkOverlap(p1)
			}
		}
	}

	Particles.fn.vendors.createSvgImg = function (p) {

		/* set color to svg element */
		var svgXml = Particles.tmp.source_svg,
			rgbHex = /#([0-9A-F]{3,6})/gi,
			coloredSvgXml = svgXml.replace(rgbHex, function (m, r, g, b) {
				if (p.color.rgb) {
					var color_value = 'rgba(' + p.color.rgb.r + ',' +
						p.color.rgb.g + ',' + p.color.rgb.b + ',' + p.opacity +
						')'
				} else {
					var color_value = 'hsla(' + p.color.hsl.h + ',' +
						p.color.hsl.s + '%,' + p.color.hsl.l + '%,' +
						p.opacity + ')'
				}
				return color_value
			})

		/* prepare to create img with colored svg */
		var svg = new Blob([coloredSvgXml],
				{type: 'image/svg+xml;charset=utf-8'}),
			DOMURL = window.URL || window.webkitURL || window,
			url = DOMURL.createObjectURL(svg)

		/* create particle img obj */
		var img = new Image()
		img.addEventListener('load', function () {
			p.img.obj = img
			p.img.loaded = true
			DOMURL.revokeObjectURL(url)
			Particles.tmp.count_svg++
		})
		img.src = url

	}

	Particles.fn.vendors.destroyParticles = function () {
		cancelAnimationFrame(Particles.fn.drawAnimFrame)
		canvas_el.remove()
		ParticlesDom = null
	}

	Particles.fn.vendors.drawShape =
		function (c, startX, startY, sideLength, sideCountNumerator,
		          sideCountDenominator) {

			// By Programming Thomas -
			// https://programmingthomas.wordpress.com/2013/04/03/n-sided-shapes/
			var sideCount = sideCountNumerator * sideCountDenominator
			var decimalSides = sideCountNumerator / sideCountDenominator
			var interiorAngleDegrees = (180 * (decimalSides - 2)) /
				decimalSides
			var interiorAngle = Math.PI - Math.PI * interiorAngleDegrees / 180 // convert to radians
			c.save()
			c.beginPath()
			c.translate(startX, startY)
			c.moveTo(0, 0)
			for (var i = 0; i < sideCount; i++) {
				c.lineTo(sideLength, 0)
				c.translate(sideLength, 0)
				c.rotate(interiorAngle)
			}
			//c.stroke();
			c.fill()
			c.restore()

		}

	Particles.fn.vendors.exportImg = function () {
		window.open(Particles.canvas.el.toDataURL('image/png'), '_blank')
	}

	Particles.fn.vendors.loadImg = function (type) {

		Particles.tmp.img_error = undefined

		if (Particles.particles.shape.image.src != '') {

			if (type == 'svg') {

				var xhr = new XMLHttpRequest()
				xhr.open('GET', Particles.particles.shape.image.src)
				xhr.onreadystatechange = function (data) {
					if (xhr.readyState == 4) {
						if (xhr.status == 200) {
							Particles.tmp.source_svg = data.currentTarget.response
							Particles.fn.vendors.checkBeforeDraw()
						} else {
							console.log('Error Particles - Image not found')
							Particles.tmp.img_error = true
						}
					}
				}
				xhr.send()

			} else {

				var img = new Image()
				img.addEventListener('load', function () {
					Particles.tmp.img_obj = img
					Particles.fn.vendors.checkBeforeDraw()
				})
				img.src = Particles.particles.shape.image.src

			}

		} else {
			console.log('Error Particles - No image.src')
			Particles.tmp.img_error = true
		}

	}

	Particles.fn.vendors.draw = function () {

		if (Particles.particles.shape.type == 'image') {

			if (Particles.tmp.img_type == 'svg') {

				if (Particles.tmp.count_svg >= Particles.particles.number.value) {
					Particles.fn.particlesDraw()
					if (!Particles.particles.move.enable) cancelRequestAnimFrame(
						Particles.fn.drawAnimFrame)
					else Particles.fn.drawAnimFrame =
						requestAnimFrame(Particles.fn.vendors.draw)
				} else {
					//console.log('still loading...');
					if (!Particles.tmp.img_error) Particles.fn.drawAnimFrame =
						requestAnimFrame(Particles.fn.vendors.draw)
				}

			} else {

				if (Particles.tmp.img_obj != undefined) {
					Particles.fn.particlesDraw()
					if (!Particles.particles.move.enable) cancelRequestAnimFrame(
						Particles.fn.drawAnimFrame)
					else Particles.fn.drawAnimFrame =
						requestAnimFrame(Particles.fn.vendors.draw)
				} else {
					if (!Particles.tmp.img_error) Particles.fn.drawAnimFrame =
						requestAnimFrame(Particles.fn.vendors.draw)
				}

			}

		} else {
			Particles.fn.particlesDraw()
			if (!Particles.particles.move.enable) cancelRequestAnimFrame(
				Particles.fn.drawAnimFrame)
			else Particles.fn.drawAnimFrame = requestAnimFrame(Particles.fn.vendors.draw)
		}

	}

	Particles.fn.vendors.checkBeforeDraw = function () {

		// if shape is image
		if (Particles.particles.shape.type == 'image') {

			if (Particles.tmp.img_type == 'svg' && Particles.tmp.source_svg == undefined) {
				Particles.tmp.checkAnimFrame = requestAnimFrame(check)
			} else {
				//console.log('images loaded! cancel check');
				cancelRequestAnimFrame(Particles.tmp.checkAnimFrame)
				if (!Particles.tmp.img_error) {
					Particles.fn.vendors.init()
					Particles.fn.vendors.draw()
				}

			}

		} else {
			Particles.fn.vendors.init()
			Particles.fn.vendors.draw()
		}

	}

	Particles.fn.vendors.init = function () {

		/* init canvas + particles */
		Particles.fn.retinaInit()
		Particles.fn.canvasInit()
		Particles.fn.canvasSize()
		Particles.fn.canvasPaint()
		Particles.fn.particlesCreate()
		Particles.fn.vendors.densityAutoParticles()

		/* particles.line_linked - convert hex colors to rgb */
		Particles.particles.line_linked.color_rgb_line =
			hexToRgb(Particles.particles.line_linked.color)

	}

	Particles.fn.vendors.start = function () {

		if (isInArray('image', Particles.particles.shape.type)) {
			Particles.tmp.img_type = Particles.particles.shape.image.src.substr(
				Particles.particles.shape.image.src.length - 3)
			Particles.fn.vendors.loadImg(Particles.tmp.img_type)
		} else {
			Particles.fn.vendors.checkBeforeDraw()
		}

	}

	/* ---------- Particles - start ------------ */

	Particles.fn.vendors.eventsListeners()

	Particles.fn.vendors.start()

}

/* ---------- global functions - vendors ------------ */

Object.deepExtend = function (destination, source) {
	for (var property in source) {
		if (source[property] && source[property].constructor &&
			source[property].constructor === Object) {
			destination[property] = destination[property] || {}
			// arguments.callee(destination[property], source[property])
		} else {
			destination[property] = source[property]
		}
	}
	return destination
}

window.requestAnimFrame = (function () {
	return window.requestAnimationFrame ||
		window.webkitRequestAnimationFrame ||
		window.mozRequestAnimationFrame ||
		window.oRequestAnimationFrame ||
		window.msRequestAnimationFrame ||
		function (callback) {
			window.setTimeout(callback, 1000 / 60)
		}
})()

window.cancelRequestAnimFrame = (function () {
	return window.cancelAnimationFrame ||
		window.webkitCancelRequestAnimationFrame ||
		window.mozCancelRequestAnimationFrame ||
		window.oCancelRequestAnimationFrame ||
		window.msCancelRequestAnimationFrame ||
		clearTimeout
})()

function hexToRgb (hex) {
	// By Tim Down - http://stackoverflow.com/a/5624139/3493650
	// Expand shorthand form (e.g. "03F") to full form (e.g. "0033FF")
	var shorthandRegex = /^#?([a-f\d])([a-f\d])([a-f\d])$/i
	hex = hex.replace(shorthandRegex, function (m, r, g, b) {
		return r + r + g + g + b + b
	})
	var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex)
	return result ? {
		r: parseInt(result[1], 16),
		g: parseInt(result[2], 16),
		b: parseInt(result[3], 16),
	} : null
}

function clamp (number, min, max) {
	return Math.min(Math.max(number, min), max)
}

function isInArray (value, array) {
	return array.indexOf(value) > -1
}

/* ---------- particles.js functions - start ------------ */
//
let ParticlesDom = [];
//
//window.particlesJS = function(tag_id, params){
//
//	//console.log(params);
//
//	/* no string id? so it's object params, and set the id with default id */
//	if(typeof(tag_id) != 'string'){
//		params = tag_id;
//		tag_id = 'particles-js';
//	}
//
//	/* no id? set the id to default id */
//	if(!tag_id){
//		tag_id = 'particles-js';
//	}
//
//	/* Particles elements */
//	var Particles_tag = document.getElementById(tag_id),
//		Particles_canvas_class = 'particles-js-canvas-el',
//		exist_canvas = Particles_tag.getElementsByClassName(Particles_canvas_class);
//
//	/* remove canvas if exists into the Particles target tag */
//	if(exist_canvas.length){
//		while(exist_canvas.length > 0){
//			Particles_tag.removeChild(exist_canvas[0]);
//		}
//	}
//
//	/* create canvas element */
//	var canvas_el = document.createElement('canvas');
//	canvas_el.className = Particles_canvas_class;
//
//	/* set size canvas */
//	canvas_el.style.width = "100%";
//	canvas_el.style.height = "100%";
//
//	/* append canvas */
//	var canvas = document.getElementById(tag_id).appendChild(canvas_el);
//
//	/* launch particle.js */
//	if(canvas != null){
//		ParticlesDom.push(new Particles(tag_id, params));
//	}
//
//};
//
//window.particlesJS.load = function(tag_id, path_config_json, callback){
//
//	/* load json config */
//	var xhr = new XMLHttpRequest();
//	xhr.open('GET', path_config_json);
//	xhr.onreadystatechange = function (data) {
//		if(xhr.readyState == 4){
//			if(xhr.status == 200){
//				var params = JSON.parse(data.currentTarget.response);
//				window.particlesJS(tag_id, params);
//				if(callback) callback();
//			}else{
//				console.log('Error Particles - XMLHttpRequest status: '+xhr.status);
//				console.log('Error Particles - File config not found');
//			}
//		}
//	};
//	xhr.send();
//
//};

// export default {
// 	Particles,
// }
