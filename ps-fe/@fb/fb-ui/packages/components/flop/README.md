[comment]: <> (fb-docs: docsify/fb-ui/05/flop/README.md)

# 翻牌器 flop
展示数字的牌子



## 大屏常用使用基础用法
```html run {title:'示例演示'}
<template>
<div>
    <fb-card header="大屏主题相关样式">
        <div>
            <fb-flop :num="999" minLength="3" theme="screen-red-s">
                <div slot="prefix">screen-red-s</div>
            </fb-flop>
            <fb-flop :num="32" minLength="3" theme="screen-tomato-m">
                <div slot="prefix">screen-tomato-m</div>
            </fb-flop>
            <fb-flop :num="32" minLength="3" theme="screen-carrot-l">
                <div slot="prefix">screen-carrot-l</div>
            </fb-flop>
            <fb-flop :num="32" minLength="3" theme="screen-orange-xl">
                <div slot="prefix">screen-orange-xl</div>
            </fb-flop>
            <fb-flop :num="32" minLength="3" theme="screen-brown-xl">
                <div slot="prefix">screen-brown-xl</div>
            </fb-flop>
        </div>
        <div>
            <fb-flop :num="32" minLength="3" theme="screen-yellow-s">
                <div slot="prefix">screen-yellow-s</div>
            </fb-flop>
            <fb-flop :num="32" minLength="3" theme="screen-green-m">
                <div slot="prefix">screen-green-m</div>
            </fb-flop>
            <fb-flop :num="32" minLength="3" theme="screen-cyan-l">
                <div slot="prefix">screen-cyan-l</div>
            </fb-flop>
            <fb-flop :num="32" minLength="3" theme="screen-lightblue-xl">
                <div slot="prefix">screen-lightblue-xl</div>
            </fb-flop>
            <fb-flop :num="32" minLength="3" theme="screen-blue-xl">
                <div slot="prefix">screen-blue-xl</div>
            </fb-flop>
        </div>
        <div>
            <fb-flop :num="32" minLength="3" theme="screen-ultramarine-s">
                <div slot="prefix">screen-ultramarine-s</div>
            </fb-flop>
            <fb-flop :num="32" minLength="3" theme="screen-purple-m">
                <div slot="prefix">screen-purple-m</div>
            </fb-flop>
            <fb-flop :num="32" minLength="3" theme="screen-magenta-l">
                <div slot="prefix">screen-magenta-l</div>
            </fb-flop>
            <fb-flop :num="32" minLength="3" theme="screen-grey-xl">
                <div slot="prefix">screen-grey-xl</div>
            </fb-flop>
        </div>
    </fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
        sum: 100,
        num: 999,
      }
    },
    methods: {
    },
  }
</script>
```

## 基础用法

```html run {title:'示例演示'}
<template>
<div>
    <fb-card header="type brand">
        <fb-flop :num="num" minLength="6">
            <div slot="prefix">共</div>
            <div slot="suffix">家</div>
        </fb-flop>
        <fb-button @on-click="resetNum" >play</fb-button>
        <br/>
        <br/>
        <fb-flop num="9999999999777" minLength="16">
            <div slot="prefix">共</div>
            <div slot="suffix">家</div>
        </fb-flop>
        <br/>
        <br/>
        <br/>
        <br/>
        <fb-row gutter="8" vertical-gutter="8">
            <fb-col span="12">
                <fb-flop num="abcd" minLength="6" brandSize="s" brandStatus="danger">
                    <div slot="prefix">字符串</div>
                </fb-flop>
            </fb-col>
            <fb-col span="12">
                <fb-flop :num="12.12" minLength="3" brandSize="s" brandStatus="success" :duration="100">
                    <div slot="prefix">浮点数</div>
                </fb-flop>
            </fb-col>
            <fb-col span="12">
                <fb-flop :num="sum" minLength="4" brandSize="s" brandStatus="success" @on-click="random">
                    <div slot="prefix">整数</div>
                </fb-flop>
            </fb-col>
            <fb-col span="12">
                <fb-flop :num="sum" minLength="3" :brandStyle="{
                    background: 'linear-gradient(#7D2FD1, #A371EA)',
                    width: '32px',
                    height: '42px',
                    lineHeight: '42px',
                    fontSize: '24px', color: '#fff', cursor: 'pointer'}" @on-click="random">
                    <div slot="prefix">自定义 点我</div>
                </fb-flop>
            </fb-col>
            <fb-col span="12">
                <fb-flop :num="sum" noScroll minLength="3" :brandStyle="{
                    background: 'linear-gradient(#7D2FD1, #A371EA)',
                    width: '32px',
                    height: '42px',
                    lineHeight: '42px',
                    fontSize: '24px', color: '#fff', cursor: 'pointer'}" @on-click="random">
                    <div slot="prefix">noScroll</div>
                </fb-flop>
            </fb-col>
        </fb-row>

    </fb-card>

    <fb-card header="type flip">
        <fb-row gutter="16">
            <fb-col span="12">
                <fb-card>
                    <fb-flop num="你好中国" type="flip" flipType="up" :duration="600">
                        <div slot="prefix">字符串</div>
                    </fb-flop>
                </fb-card>
            </fb-col>
            <fb-col span="12">
                <fb-card>
                    <fb-flop :num="sum" minLength="4" type="flip">
                        <div slot="prefix">整数</div>
                    </fb-flop>
                </fb-card>
            </fb-col>
            <fb-col span="12">
                <fb-card>
                    <fb-flop :num="sum" type="flip" :brandStyle="{
                    background: 'linear-gradient(#7D2FD1, #A371EA)',
                    width: '32px',
                    height: '42px',
                    lineHeight: '42px',
                    fontSize: '24px', color: '#fff', cursor: 'pointer'}">
                        <div slot="prefix">自定义</div>
                    </fb-flop>
                </fb-card>
            </fb-col>
            <fb-col span="12">
                <fb-card>
                    <fb-flop :num="sum" type="flip" :duration="300" :brandStyle="{
                    background: 'linear-gradient(#7D2FD1, #A371EA)',
                    width: '32px',
                    height: '42px',
                    lineHeight: '42px',
                    fontSize: '24px', color: '#fff', cursor: 'pointer'}">
                        <div slot="prefix">自定义 300ms</div>
                    </fb-flop>
                </fb-card>
            </fb-col>
        </fb-row>
    </fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
        sum: 100,
        num: 999,
      }
    },
    methods: {
        random() {
            this.sum = Math.floor(Math.random() * 1000)
            console.log(this.sum)
        },
        resetNum() {
			this.num=0;
			this.$nextTick(()=>{
				this.num=12345
			})
		},
    },
  }
</script>
```

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| type | 类型(brand / flip)  | String | brand |
| num | 数值 | Number, String | 0 |
| brandPrefix | 牌子前缀 | String | 0 |
| brandSuffix | 牌子后缀 | String | 0 |
| minLength | 补位 length | Number, String | 0 |
| maxLength | 限制 length | Number, String | 0 |
| brandSize | 牌子大小 m/s | String | m |
| brandStatus | 牌子状态 dafault/success/warning/danger/info | String | dafault |
| brandStyle | 牌子style  | Object | {} |
| noPlay | 停止动画  | Boolean | false |
| duration | 动画时间  | Number | 500 |
| flipType | flip类型上下翻牌(down / up)  | String | down |
| reverse | 翻牌顺序  | Boolean | true |
| theme | 主题样式（高于brandStatus属性）  | String | - |

## 插槽选项

| 名称 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| prefix | 前缀 | html，组件 |
| suffix | 后缀 | html，组件 |

## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-click | 点击牌子事件 | (num) => void


# 翻牌器 flopper
扩容展示数字的牌子，例如时钟

## 基础用法

```html run {title:'示例演示'}
<template>
<div>
    <fb-card header="fb-flopper">
        <fb-row gutter="16">
            <fb-col span="24">
                <fb-card header="Clock">
                    <div class="FlipClock">
                        <fb-flopper ref="flipperHour1"/>
                        <em></em>
                        <fb-flopper ref="flipperHour2"/>
                        <em>:</em>
                        <fb-flopper ref="flipperMinute1"/>
                        <em></em> 
                        <fb-flopper ref="flipperMinute2"/>
                        <em>:</em>
                        <fb-flopper ref="flipperSecond1"/>
                        <em></em>
                        <fb-flopper ref="flipperSecond2"/>
                    </div>
                </fb-card>
            </fb-col>
            <fb-col span="6">
                <fb-card header="自定义">
                    <fb-flopper :flipNum="5" flipType="down" :duration="600"
                                :flipStyle="{
                    background: 'linear-gradient(#7D2FD1, #A371EA)',
                    width: '32px',
                    height: '42px',
                    lineHeight: '42px',
                    fontSize: '24px'}"></fb-flopper>
                </fb-card>
            </fb-col>
            <fb-col span="6">
                <fb-card header="最快 100">
                    <fb-flopper :flipNum="9" flipType="up" :duration="100"></fb-flopper>
                </fb-card>
            </fb-col>
            <fb-col span="6">
                <fb-card header="点击 改变文字">
                    <fb-flopper ref="flip" frontText="你" :backText="backText" flipType="up"
                                :duration="300"></fb-flopper>
                    <fb-button @on-click="changeFlip" danger>你好中国</fb-button>
                </fb-card>
            </fb-col>
        </fb-row>
    </fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
        str: '你好中国',
        backText: '',
        timer: null,
        flipObjs: []
      }
    },
    methods: {
        changeFlip() {
            let str = this.str
            let idx = str.indexOf(this.backText)
            let frontText = str.slice(idx, idx + 1)
            if (idx === str.length - 1) {
                this.backText = '你'
            } else {
                this.backText = str.slice(idx + 1, idx + 2)
            }
            this.$refs['flip'].flipUp(frontText, this.backText)
        },
        // 初始化数字
        init() {
            let now = new Date()
            let nowTimeStr = this.formatDate(new Date(now.getTime()), 'hhiiss')
            for (let i = 0; i < this.flipObjs.length; i++) {
                this.flipObjs[i].setFront(nowTimeStr[i])
            }
        },
        // 开始计时
        run() {
            this.timer = setInterval(() => {
                // 获取当前时间
                let now = new Date()
                let nowTimeStr = this.formatDate(new Date(now.getTime() - 1000), 'hhiiss')
                let nextTimeStr = this.formatDate(now, 'hhiiss')
                console.log(nextTimeStr)
                for (let i = 0; i < this.flipObjs.length; i++) {
                    if (nowTimeStr[i] === nextTimeStr[i]) {
                        continue
                    }
                    this.flipObjs[i].flipDown(
                        nowTimeStr[i],
                        nextTimeStr[i],
                    )
                }
            }, 1000)
        },
        // 正则格式化日期
        formatDate(date, dateFormat) {
            /* 单独格式化年份，根据y的字符数量输出年份
             * 例如：yyyy => 2019
             yy => 19
             y => 9
             */
            if (/(y+)/.test(dateFormat)) {
                dateFormat = dateFormat.replace(
                    RegExp.$1,
                    (date.getFullYear() + '').substr(4 - RegExp.$1.length),
                )
            }
            // 格式化月、日、时、分、秒
            let o = {
                'm+': date.getMonth() + 1,
                'd+': date.getDate(),
                'h+': date.getHours(),
                'i+': date.getMinutes(),
                's+': date.getSeconds(),
            }
            for (let k in o) {
                let a = "("+ k +")"
                if (new RegExp(`(${k})`).test(dateFormat)) {
                    // 取出对应的值
                    let str = o[k] + ''
                    /* 根据设置的格式，输出对应的字符
                     * 例如: 早上8时，hh => 08，h => 8
                     * 但是，当数字>=10时，无论格式为一位还是多位，不做截取，这是与年份格式化不一致的地方
                     * 例如: 下午15时，hh => 15, h => 15
                     */
                    dateFormat = dateFormat.replace(
                        RegExp.$1,
                        RegExp.$1.length === 1 ? str : this.padLeftZero(str),
                    )
                }
            }
            return dateFormat
        },
        // 日期时间补零
        padLeftZero(str) {
            console.log(str)
            return ('00' + str).substr(str.length)
        },
    },
    mounted () {
        this.flipObjs = [
            this.$refs.flipperHour1,
            this.$refs.flipperHour2,
            this.$refs.flipperMinute1,
            this.$refs.flipperMinute2,
            this.$refs.flipperSecond1,
            this.$refs.flipperSecond2
        ]
        this.init()
        this.run()
    },
  }
</script>
<style>
	.FlipClock {
		text-align: center;
	}
	.FlipClock em {
		margin: 0 3px;
		display: inline-block;
		line-height: 102px;
		font-size: 66px;
		font-style: normal;
		vertical-align: top;
	}
</style>

```

## 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| flipNum | 数字会触发动画  | Number | 0 |
| flipType | 动画类型(down / up)  | String | down |
| flipStyle | 牌子style  | Object | / |
| frontText | 前牌文字  | Number, String | 0 |
| backText | 后牌文字  | Number, String | 1 |
| theme | 翻牌器主题  | String | 1 |
| duration | 翻牌动画时间(600, 500, 400, 300, 200, 100)  | Number | 600 |
| brandHeight | 翻牌器高度，防止panel延迟显示拾取不到高度 | Number | 600 | 
| panelDelay |  panel 动画延迟时间  | Number | 350 | 

## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-click | 点击牌子事件 | (frontText, backText) => void
