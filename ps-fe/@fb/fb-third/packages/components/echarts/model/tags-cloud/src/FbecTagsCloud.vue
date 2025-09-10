<template>
	<!--	动态词云 	-->
	<div ref="wrapper" class="tag-cloud-wrapper"
		 :style="{
            height: `${myHeight}px`,
            width: `${myWidth}px`,
         }"
		 @mousemove="handleMouseMove"
		 @mouseenter="handleMouseEnter"
		 @mouseleave="handleMouseLeave">
		<div v-for="(item, index) in myData"
			 @click="$emit('on-click', item)"
			 :id="`tag-cloud__item_${index}`"
			 class="tag-cloud__item"
			 :key="index"
			 :style="{
                    color: item.color,
                    opacity:item.opacity,
                    top: item.top,
                    left: item.left,
                    zIndex: item.zIndex,
                    fontSize: item.fontSize
                   }">{{ item.label }}
		</div>
	</div>
</template>

<script>
	/**
	 * FbecTagsCloud
	 * (c) 2021 lincong1987
	 */

	export default {
		name: 'FbecTagsCloud',

		props: {
			data: {
				type: Array,
				default () {
					return []
				},
			},
			radius: {
				type: Number,
				default: 100,
			},

			width: {
				type: Number,
				default: 510,
			},

			height: {
				type: Number,
				default: 430,
			},

			left: {
				type: Number,
				default: 0,
			},

			top: {
				type: Number,
				default: 0,
			},

			size: {
				type: Number,
				default: 18,
			},

			speed: {
				type: Number,
				default: 6,
			},
			direction: {
				type: Number,
				default: 180,
			},

		},

		data () {
			return {
				myData: this.data,

				myWidth: this.width,
				myHeight: this.height,

				myLeft: this.left || this.width,
				myTop: this.top || this.height,

				lastX: 0, // 坐标X
				lastY: 0, // 坐标Y
				myDirection: this.direction, // 初始化标签词云角度，默认左上角
				colorList: [
					'#3a96f5',
					'#F1BF10',
					'#E38712',
					'#347D3F',
					'#add2f8',
					'#A35D16',
					'#1acaff',
					'#ffde00',
					'#89fda5',
				],
				countList: [],
				sizeList: [],
			}
		},

		watch: {
			data (val) {
				this.myData = []
				this.$nextTick(() => {
					this.myData = val
					this.init()
				})
			},
		},

		methods: {

			handleMouseMove: function (event) {
				let currentX = event.pageX // 获得X轴坐标
				let currentY = event.pageY // 获得Y轴坐标
				let tx = currentX - this.lastX // 计算X轴偏差值
				let ty = currentY - this.lastY // 计算Y轴偏差值

				// 上下左右方向滑动
				if (tx === 0) { // 上下方向
					if (ty < 0) { // 上滑动
						this.myDirection = 360
					} else if (ty > 0) { // 下滑动
						this.myDirection = 180
					}
				} else if (ty === 0) { // 左右方向
					if (tx < 0) { // 左滑动
						this.myDirection = 270
					} else if (tx > 0) { // 右滑动
						this.myDirection = 90
					}
				} else if (tx < 0 && ty < 0) { // 左上滑动
					this.myDirection = 315
				} else if (tx < 0 && ty > 0) { // 左下滑动
					this.myDirection = 225
				} else if (tx > 0 && ty < 0) { // 右上滑动
					this.myDirection = 45
				} else if (tx > 0 && ty > 0) { // 右下滑动
					this.myDirection = 135
				}

				// 将当前坐标进行保存以进行下一次计算
				this.lastX = currentX
				this.lastY = currentY
			},
			handleMouseEnter: function (event) {
				this.lastX = event.pageX // 获得触摸点X轴坐标
				this.lastY = event.pageY // 获得触摸点Y轴坐标

				if (this.countTime) {
					clearInterval(this.countTime) // 清除计算定时器
					this.countTime = null // 清除计算定时器
				}

			},
			handleMouseLeave () {
				if (this.countList.length > 0) {
					// 下列为主要运算赋值程序，定时器是由于小程序API获取高宽的异步执行，这里暂时没改为同步。即用定时器来做延时运行。
					setTimeout(() => {
						clearInterval(this.countTime) // 清除计算定时器
						this.countTime = setInterval(() => {
							if (this.myData.length === this.countList.length) {
								this.calculation(this.countList) // 调用计算函数
							}
						}, 34) // 每50毫秒执行一次，考虑性能消耗问题，不建议更改时间，要控制速度更改speed值
					}, 300)
				}

			},

			// 初始化标签云特效
			init () {
				clearInterval(this.countTime) // 清除计算定时器
				this.countTime = null // 清除计算定时器

				let countList = [] // 计算列表数据集合
				let radius = this.radius // 初始化滚动半径作用区域
				//  let tagEle = data // 标题元素数组
//        this.setData({ // 首次赋值给到页面用于后续获取高宽值
//            tagEle: tagEle,
//        })
				//   this.myData = tagEle

				this.$nextTick(() => {
					// 首次循环获取所有元素高宽值并计算得出首次计算列表数据
					for (let i = 0; i < this.myData.length; i++) {
						// let query = wx.createSelectorQuery() // 小程序API获得组件对象

						let tagEl = this.$refs.wrapper.querySelector(`#tag-cloud__item_${i}`)

						if (tagEl) {
							let rect = tagEl.getBoundingClientRect()//.exec()

							// 使用选择器获得每个id元素的高宽值
							let acos = Math.acos(-1 + (2 * i + 1) / this.myData.length) // 计算反余弦
							let sqrt = Math.sqrt((this.myData.length + 1) * Math.PI) * acos // 计算平方根
							countList.push({
								size:  this.myData[i].size || 14,
								color: (() => {
									if (this.myData[i].color) {
										return this.myData[i].color
									}
									if (this.colorList[i]) {
										return this.colorList[i]
									}
									let color = '#fff'
									if (!this.colorList[i]) {
										color = this.colorList[((i - 1) % this.colorList.length)]
									}
									return color
								})(),
								offsetWidth: rect.width, // 当前id元素的宽度
								offsetHeight: rect.height, // 当前id元素的高度
								left: radius * 1.5 * Math.cos(sqrt) * Math.sin(acos), // 当前id元素的left值
								top: radius * 1.5 * Math.sin(sqrt) * Math.sin(acos), // 当前id元素的top值
								z: radius * 1.5 * Math.cos(acos), // 计算Z轴值
							})
						}

					}

					this.countList = countList

					// 下列为主要运算赋值程序，定时器是由于小程序API获取高宽的异步执行，这里暂时没改为同步。即用定时器来做延时运行。
					setTimeout(() => {
						clearInterval(this.countTime) // 清除计算定时器
						this.countTime = setInterval(() => {
							if (this.myData.length === countList.length) {
								this.calculation(countList) // 调用计算函数
							}
						}, 34) // 每50毫秒执行一次，考虑性能消耗问题，不建议更改时间，要控制速度更改speed值
//                this.setData({
//                    tagState: false,
//                })
						//this.tagState = false
					}, 300)
				})
			},

			// Style样式计算过程
			calculation (countData) {
				let countList = countData // 计算结果数组
				let radius = this.radius // 滚动区域范围，默认单位为px，数值越大滚动范围越大
				let fontsize = this.size // 字体大小，默认单位为px，后期转换rem。数值越大字体越大
				let depth = 2 * radius // 滚动深度
				let speed = this.speed // 滚动速度，数值越大滚动速度越快，不能小于2
				let myDirection = this.myDirection // 滚动方向, 取值角度(0-360): 0和360对应即从下到上, 90对应垂直X-Y,180对应从上倒下，其他数值随意测试...
				let myDirectionX = speed * Math.sin(myDirection * Math.PI / 180) // 计算X轴Sin值
				let myDirectioneY = -speed * Math.cos(myDirection * Math.PI / 180) // 计算Y轴Cos值
				let a = -(Math.min(Math.max(-myDirectioneY, -radius), radius) / radius) * speed // 计算a值用以后续判断计算
				let b = (Math.min(Math.max(-myDirectionX, -radius), radius) / radius) * speed // 计算b值用以后续判断计算
				let dtr = Math.PI / 180 // 计算圆周率
				let PIList = [ // 计算圆周率数组
					Math.sin(a * dtr),
					Math.cos(a * dtr),
					Math.sin(b * dtr),
					Math.cos(b * dtr),
				]
				// 若ab值太小，即相关配置如速度/范围等太低，直接return不执行动效
				if (Math.abs(a) <= 0.01 && Math.abs(b) <= 0.01) {
					return
				}
				// 循环遍历每个元素前面所计算出来的各值
				for (let j = 0; j < countList.length; j++) {
					let rz1 = countList[j].top * PIList[0] + countList[j].z * PIList[1] // 计算前置数据
					let rz2 = rz1 * PIList[3] - countList[j].left * PIList[2] // 计算前置数据
					let per = depth / (depth + rz2) // 计算前置数据

					countList[j].left = countList[j].left * PIList[3] + rz1 * PIList[2] // 计算left用以后面计算赋值left
					countList[j].top = countList[j].top * PIList[1] + countList[j].z * (-PIList[0]) // 计算top用以后面计算赋值top
					countList[j].z = rz2 // 赋值计算列表中Z值新数据
					countList[j].fontSize = `${countList[j].size || fontsize }px`//(per * 4 + fontsize) // 计算fontsize用以后面计算赋值font-size。注：最后除以30是用以后续rem单位计算，具体rem单位计算可参照官方计算。
					countList[j].alpha = 1.5 * per - 0.7 // 计算alpha用以后面计算赋值opacity
					countList[j].zIndex = Math.ceil(per * 10 - 5) // 计算zIndex用以后面计算赋值z-index
				}

				this.voluation(countList)
			},

			// Style样式赋值运算
			voluation (countList) {
				let itemList = [] // 存储完整渲染列表的文字和样式结构

				for (let i = 0; i < countList.length; i++) {
					itemList.push({
						color: countList[i].color,
						label: this.myData[i].label, // 标题文字内容
						value: this.myData[i].value,
						left: countList[i].left + (this.myLeft - countList[i].offsetWidth) / 2 + 'px',  // 500越大，则距离左边越远
						top: countList[i].top + (this.myTop - countList[i].offsetHeight) / 2 + 'px', // 440越大，则距离上边越远
						zIndex: countList[i].zIndex, // z-index值
						opacity: countList[i].alpha,  // opacity值
						fontSize: countList[i].fontSize, // font-size值。注：不采用rpx值是因为在小程序最后会被改为四舍五入后的px值，不支持小数点单位，在放大缩小中不是很美观。于是采用转换rem值。
					})
				}
				this.myData = itemList
			},

		},

		created () {
			this.countTime = null
		},
		mounted () {
//        let data = [ // 标题元素数组
//            {label: 'Crawling  16:56:44', size: 20},
//            {label: '王武阳  16:53:28'},
//            {label: '孔海钧  16:46:44'},
//            {label: '林倩倩  16:43:24'},
//            {label: '朱宏涛  16:24:34'},
//            {label: '王牌强哥  16:08:20'},
//            {label: '潘登  13:42:26'},
//        ]
//
//        this.myData = data

			if (this.data.length > 0) {
				this.init() // 调用标签云特效
			}
		},

		beforeDestroy () {
			clearInterval(this.countTime) // 清除计算定时器
			this.countTime = null // 清除计算定时器
		},

	}
</script>

<style lang="less" scoped>
	/**index.wxss**/
	page {
		width:  100%;
		height: auto;
	}

	.tag-cloud-wrapper {
		width:    464px;
		height:   232px;
		position: relative;
		top:      0;
		left:     0;

		overflow: hidden;
	}

	.tag-cloud__item {
		position:        absolute;
		top:             60%;
		left:            40%;
		display:         inline-block;
		color:           #fff;
		font-size:       20px;
		text-decoration: none;
		font-weight:     bold;

		&:hover {
			cursor:  pointer;
			opacity: 1;
		}
	}
</style>
