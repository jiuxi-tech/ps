[comment]: <> (fb-docs: docsify/fb-ui/04/marquee/README.md)

# 文字跑马灯-Marquee 

用于主要提示滚动展示

## 基础用法
```html run {title:'默认'}
<template>
<fb-card header="Default">
    <fb-marquee>
        <img v-for="i in img_30" :key="i" height="80" :src="i" />
    </fb-marquee>
</fb-card>
</template>
<script>
  export default {
    data() {
		return {
			img_30: this.getImgURLS(30),
			img_5: this.getImgURLS(5),
		}
	},
    methods: {
        generateRandomString() {
            const randomString = Math.random().toString(36).substring(2, 15)
            return 'https://avatars.dicebear.com/api/avataaars/' + randomString + '.svg'
        },
        getImgURLS(num) {
			let array = []
			for (let i = 0; i < num; i++) {
				array.push(this.generateRandomString())
			}
			return array
		},
    }
  }
</script>
<style>
</style>
```

## Duration: 10s
```html run {title:'默认'}
<template>
<fb-card header="Duration: 10s">
			<fb-marquee :duration="10">
				<img v-for="i in img_30" :key="i" height="80" :src="i" />
			</fb-marquee>
		</fb-card>
</template>
<script>
  export default {
    data() {
		return {
			img_30: this.getImgURLS(30),
			img_5: this.getImgURLS(5),
		}
	},
    methods: {
        generateRandomString() {
            const randomString = Math.random().toString(36).substring(2, 15)
            return 'https://avatars.dicebear.com/api/avataaars/' + randomString + '.svg'
        },
        getImgURLS(num) {
			let array = []
			for (let i = 0; i < num; i++) {
				array.push(this.generateRandomString())
			}
			return array
		},
    }
  }
</script>
<style>
</style>
```

## Direction: reverse
```html run {title:'默认'}
<template>
<fb-card header="Direction: reverse">
			<fb-marquee direction="reverse">
				<img v-for="i in img_30" :key="i" height="80" :src="i" />
			</fb-marquee>
		</fb-card>
</template>
<script>
  export default {
    data() {
		return {
			img_30: this.getImgURLS(30),
			img_5: this.getImgURLS(5),
		}
	},
    methods: {
        generateRandomString() {
            const randomString = Math.random().toString(36).substring(2, 15)
            return 'https://avatars.dicebear.com/api/avataaars/' + randomString + '.svg'
        },
        getImgURLS(num) {
			let array = []
			for (let i = 0; i < num; i++) {
				array.push(this.generateRandomString())
			}
			return array
		},
    }
  }
</script>
<style>
</style>
```

## Pause on hover
```html run {title:'默认'}
<template>
<fb-card header="Pause on hover">
			<fb-marquee :pause-on-hover="true">
				<img v-for="i in img_30" :key="i" height="80" :src="i" />
			</fb-marquee>
		</fb-card>
</template>
<script>
  export default {
    data() {
		return {
			img_30: this.getImgURLS(30),
			img_5: this.getImgURLS(5),
		}
	},
    methods: {
        generateRandomString() {
            const randomString = Math.random().toString(36).substring(2, 15)
            return 'https://avatars.dicebear.com/api/avataaars/' + randomString + '.svg'
        },
        getImgURLS(num) {
			let array = []
			for (let i = 0; i < num; i++) {
				array.push(this.generateRandomString())
			}
			return array
		},
    }
  }
</script>
<style>
</style>
```

## Pause on click
```html run {title:'默认'}
<template>
<fb-card header="Pause on click">
			<fb-marquee :pause-on-click="true">
				<img v-for="i in img_30" :key="i" height="80" :src="i" />
			</fb-marquee>
		</fb-card>
</template>
<script>
  export default {
    data() {
		return {
			img_30: this.getImgURLS(30),
			img_5: this.getImgURLS(5),
		}
	},
    methods: {
        generateRandomString() {
            const randomString = Math.random().toString(36).substring(2, 15)
            return 'https://avatars.dicebear.com/api/avataaars/' + randomString + '.svg'
        },
        getImgURLS(num) {
			let array = []
			for (let i = 0; i < num; i++) {
				array.push(this.generateRandomString())
			}
			return array
		},
    }
  }
</script>
<style>
</style>
```

## Gradient
```html run {title:'默认'}
<template>
<fb-card header="Gradient">
			<fb-marquee :gradient="true">
				<img v-for="i in img_30" :key="i" height="80" :src="i" />
			</fb-marquee>
		</fb-card>
</template>
<script>
  export default {
    data() {
		return {
			img_30: this.getImgURLS(30),
			img_5: this.getImgURLS(5),
		}
	},
    methods: {
        generateRandomString() {
            const randomString = Math.random().toString(36).substring(2, 15)
            return 'https://avatars.dicebear.com/api/avataaars/' + randomString + '.svg'
        },
        getImgURLS(num) {
			let array = []
			for (let i = 0; i < num; i++) {
				array.push(this.generateRandomString())
			}
			return array
		},
    }
  }
</script>
<style>
</style>
```

## Gradient  color: [78, 205, 196]
```html run {title:'默认'}
<template>
<fb-card header="Gradient  color: [78, 205, 196]">
			<fb-marquee :gradient="true" :gradient-color="[78, 205, 196]">
				<img v-for="i in img_30" :key="i" height="80" :src="i" />
			</fb-marquee>
		</fb-card>
</template>
<script>
  export default {
    data() {
		return {
			img_30: this.getImgURLS(30),
			img_5: this.getImgURLS(5),
		}
	},
    methods: {
        generateRandomString() {
            const randomString = Math.random().toString(36).substring(2, 15)
            return 'https://avatars.dicebear.com/api/avataaars/' + randomString + '.svg'
        },
        getImgURLS(num) {
			let array = []
			for (let i = 0; i < num; i++) {
				array.push(this.generateRandomString())
			}
			return array
		},
    }
  }
</script>
<style>
</style>
```

## Gradient  width
```html run {title:'默认'}
<template>
<fb-card header="Gradient  width">
			<fb-marquee :gradient="true" gradient-width="600px">
				<img v-for="i in img_30" :key="i" height="80" :src="i" />
			</fb-marquee>
		</fb-card>
</template>
<script>
  export default {
    data() {
		return {
			img_30: this.getImgURLS(30),
			img_5: this.getImgURLS(5),
		}
	},
    methods: {
        generateRandomString() {
            const randomString = Math.random().toString(36).substring(2, 15)
            return 'https://avatars.dicebear.com/api/avataaars/' + randomString + '.svg'
        },
        getImgURLS(num) {
			let array = []
			for (let i = 0; i < num; i++) {
				array.push(this.generateRandomString())
			}
			return array
		},
    }
  }
</script>
<style>
</style>
```

## clone
```html run {title:'默认'}
<template>
<fb-card header="clone">
			<fb-marquee clone>
				<img v-for="i in img_5" :key="i" height="80" :src="i" />
			</fb-marquee>
		</fb-card>
</template>
<script>
  export default {
    data() {
		return {
			img_30: this.getImgURLS(30),
			img_5: this.getImgURLS(5),
		}
	},
    methods: {
        generateRandomString() {
            const randomString = Math.random().toString(36).substring(2, 15)
            return 'https://avatars.dicebear.com/api/avataaars/' + randomString + '.svg'
        },
        getImgURLS(num) {
			let array = []
			for (let i = 0; i < num; i++) {
				array.push(this.generateRandomString())
			}
			return array
		},
    }
  }
</script>
<style>
</style>
```

## vertical
```html run {title:'默认'}
<template>
<fb-card header="vertical">
			<div style="height: 500px; width: max-content">
				<fb-marquee :vertical="true">
					<img v-for="i in img_30" :key="i" height="50" :src="i" />
				</fb-marquee>
			</div>
		</fb-card>
</template>
<script>
  export default {
    data() {
		return {
			img_30: this.getImgURLS(30),
			img_5: this.getImgURLS(5),
		}
	},
    methods: {
        generateRandomString() {
            const randomString = Math.random().toString(36).substring(2, 15)
            return 'https://avatars.dicebear.com/api/avataaars/' + randomString + '.svg'
        },
        getImgURLS(num) {
			let array = []
			for (let i = 0; i < num; i++) {
				array.push(this.generateRandomString())
			}
			return array
		},
    }
  }
</script>
<style>
</style>
```

## 简单测试
```html run {title:'默认'}
<template>
<fb-card header="简单测试">
			<fb-marquee>
				<div v-for="i in 10" :key="i" height="80" style="width: 200px; height: 100px;" :style="{background: i%2 ? 'red' : 'green'}">
					{{i}}
				</div>
			</fb-marquee>
		</fb-card>
</template>
<script>
  export default {
  }
</script>
<style>
</style>
```



## FbMarquee 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| vertical | 竖直 | Boolean | false |
| direction | 方向（normal / reverse） | String | normal |
| duration | 动画时间（css的动画时间） | Number | 20 |
| delay | 延迟时间 | Number | 0 |
| loop | 循环次数 | Number | 0 |
| clone | 开启克隆，计算内容宽度/容器宽度循环几次 | Boolean | false |
| gradient | 开启双边遮罩 | Boolean | false |
| gradientColor | 双边遮罩颜色 | Array | [255, 255, 255] |
| gradientWidth | 双边遮罩颜色 | String | 200px |
| pauseOnHover | 触碰暂停 | Boolean | false |
| pauseOnHover | 点击暂停 | Boolean | false |

## FbMarquee 事件列表

| 事件名 | 说明 | 返回值 |  |
| :------- | :------- | :----- | :--- |
| onComplete | 完成循环 |  |  |
| onLoopComplete | 开启循环 |  |  |
| onPause | 暂停 |  |  |
| onResume | 暂停重启 |  |  |

