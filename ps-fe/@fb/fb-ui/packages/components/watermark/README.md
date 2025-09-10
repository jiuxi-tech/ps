[comment]: <> (fb-docs: docsify/fb-ui/04/watermark/README.md)

# 水印-Watermark

## 基础用法

```html run {title:'示例演示'}
<template>

	<div>
        <fb-card header="size with size map">
            <fb-container display="flex">
                <fb-container mr="8px" style="flex: 1;">
                    <fb-card shadow="off">
                        <fb-watermark :content="content" :color="color" :background-color="background"
                                      :mask-opacity="1" :rotate="rotate" :size="size" :height="height"
                                      :family="family" :direction="direction" :align="align"
                                      :width="width"  :mask="mask"
                        >
                            <fb-text size="xs">红藕香残玉簟秋。轻解罗裳，独上兰舟。云中谁寄锦书来？雁字回时，月满西楼。</fb-text>
                            <fb-text size="xs">花自飘零水自流。一种相思，两处闲愁。此情无计可消除，才下眉头，却上心头。</fb-text>
                            <br/>
                            <fb-text size="m">红藕香残玉簟秋。轻解罗裳，独上兰舟。云中谁寄锦书来？雁字回时，月满西楼。</fb-text>
                            <fb-text size="m">花自飘零水自流。一种相思，两处闲愁。此情无计可消除，才下眉头，却上心头。</fb-text>
                            <br/>

                            <fb-card>
                                带有背景色的内容
                            </fb-card>
                            <fb-text size="m">红藕香残玉簟秋。轻解罗裳，独上兰舟。云中谁寄锦书来？雁字回时，月满西楼。</fb-text>
                            <fb-text size="m">花自飘零水自流。一种相思，两处闲愁。此情无计可消除，才下眉头，却上心头。</fb-text>
                            <br/>
                            <fb-text size="l">红藕香残玉簟秋。轻解罗裳，独上兰舟。云中谁寄锦书来？雁字回时，月满西楼。</fb-text>
                            <fb-text size="l">花自飘零水自流。一种相思，两处闲愁。此情无计可消除，才下眉头，却上心头。</fb-text>
                            <br/>

                            <fb-card>
                                带有背景色的内容
                            </fb-card>
                            <fb-text size="xl">红藕香残玉簟秋。轻解罗裳，独上兰舟。云中谁寄锦书来？雁字回时，月满西楼。</fb-text>
                            <fb-text size="xl">花自飘零水自流。一种相思，两处闲愁。此情无计可消除，才下眉头，却上心头。</fb-text>
                            <br/>
                            <fb-text size="xxl">红藕香残玉簟秋。轻解罗裳，独上兰舟。云中谁寄锦书来？雁字回时，月满西楼。</fb-text>
                            <fb-text size="xxl">花自飘零水自流。一种相思，两处闲愁。此情无计可消除，才下眉头，却上心头。</fb-text>
                            <br/>
                            <fb-text size="xxxl">红藕香残玉簟秋。轻解罗裳，独上兰舟。云中谁寄锦书来？雁字回时，月满西楼。</fb-text>
                            <fb-text size="xxxl">花自飘零水自流。一种相思，两处闲愁。此情无计可消除，才下眉头，却上心头。</fb-text>
                        </fb-watermark>

                    </fb-card>
                </fb-container>
                <fb-container width="200px">
                    <fb-card shadow="off">
                        超级水印
                        <br/>
                        <fb-switch v-model="mask"  ></fb-switch>
                        <br/>
                        <fb-button @on-click="content = ['骚艳', '13999999999', '浙江卧槽卧槽卧槽科技有限公司']">多行
                        </fb-button>
                        <br/>
                        <fb-textarea rows="5" :value="Array.isArray(content) ? content.join('\n') : content"
                                     @input="(value)=>{
                                         content = value.split('\n')
                                     }"
                        ></fb-textarea>
                        <br/>
                        size: {{ size }}
                        <fb-slider v-model="size" :min="0" :max="64"></fb-slider>
                        <br/>
                        align: {{ align }}
                        <fb-radio-group v-model="align" :data="[
                            {label:'start', value:'start'}, {label:'left', value:'left'},
                            {label:'center', value:'center'}, {label:'right', value:'right'},
                             {label:'end', value:'end'}
                            ]"></fb-radio-group>
                        <br/>
                        direction: {{ direction }}
                        <fb-radio-group v-model="direction" :data="[
                            {label:'inherit', value:'inherit'}, {label:'ltr', value:'ltr'},
                            {label:'rtl', value:'rtl'}
                            ]"></fb-radio-group>
                        <br/>
                        <br/>
                        family: {{ family }}
                        <fb-select v-model="family" :data="[
                            {label:'sans-serif', value:'sans-serif'}, {label:'Segoe UI', value:'Segoe UI'}, {label:'Helvetica Neue', value:'left'},
                            {label:'Roboto', value:'Roboto'}, {label:'Microsoft YaHei', value:'Microsoft YaHei'}

                            ]"></fb-select>
                        <br/>
                        color:
                        <br/>{{ color }} <br/>
                        <fb-color-picker v-model="color" show-alpha color-format=""></fb-color-picker>
                        <br/>
                        background: <br/>{{ background }}
                        <br/>
                        <fb-color-picker v-model="background" show-alpha></fb-color-picker>

                        <br/>
                        height: {{ height }}
                        <fb-slider v-model="height" :min="0" :max="400"></fb-slider>
                        <br/>
                        width: {{ width }}
                        <fb-slider v-model="width" :min="0" :max="400"></fb-slider>
                        <br/>
                        rotate: {{ rotate }}
                        <fb-slider v-model="rotate" :min="-180" :max="180"></fb-slider>
                        <br/>
                        image:
                        <fb-upload @input="(e)=>{
                                 debugger
                        }"></fb-upload>
                    </fb-card>
                </fb-container>
            </fb-container>
        </fb-card>
	</div>

</template>

<script>

export default {
	components: {
 
	},
	data () {
		return {
			content: '朱红掏',
            color: 'rgba(211, 211, 211, 1)',
            background: '#000000',
            height: 64,
            width: 120,
            rotate: -22,
            size: 16,

            family: 'sans-serif',
            align: 'start',
            direction: 'ltr',

            mask: false
		}

	},
	methods: {},
	mounted () {

	},
}
</script>

```

## 事件列表

| 事件名      | 说明  | 返回值  |     |
|:---------|:----|:-----|:----|
| on-click | 点击  | void |     |

## 属性列表

| 属性              | 说明                                                                                    | 类型               | 默认值           |
|:----------------|:--------------------------------------------------------------------------------------|:-----------------|:--------------|
| mask            | 超级水印模式                                                                                | Boolean          | false         |
| maskOpacity     | 超级水印透明度                                                                               | Number           | 0.8           |
| content         | 内容<br/>单行 '朱红掏'<br/> 换行 ['章了了', '13999999999', 'zht@cleverlin.com']                   | [String, Array]  | ''            |
| width           | 水印块的宽度                                                                                | Number           | 120           |
| height          | 水印块的高度                                                                                | Number           | 64            |
| rotate          | 旋转角度                                                                                  | Number           | -22           |
| size            | 水印文字尺寸 <br/>尺码 xs,s,m,l,xl,xxl,xxxl<br/>数字 12,13,14,15...<br/>文本 12px, 13px, 14px...  | [String, Number] | 16            |
| color           | 水印文字颜色  <br/>hex #333 #666666 #dddddd01<br/>rgba rgba(1a, 2b, 3c, 0.5)                | String           | '#F5F5F5'     |
| backgroundColor | 水印背景颜色<br/>hex #333 #666666 #dddddd01<br/>rgba rgba(1a, 2b, 3c, 0.5)                  | String           | 'transparent' |
| weight          | 水印文字字重 normal bold                                                                    | String           | 'normal'      |
| lineHeight      | 水印文字行高                                                                                | Number           | 1.2           |
| family          | 水印文字字体'sans-serif' 'Microsoft YaHei'                                                  | String           | 'sans-serif'  |
| align           | 文本对齐选项。可选的值包括：start, end, left, right or center.默认值是 start                            | String           | 'start'       |
| direction       | 文本方向。可能的值包括：ltr, rtl, inherit。默认值是 inherit                                            | String           | 'inherit'     |
| italic          | 斜体。可能的值包括：false， true。默认值是 false                                                      | Boolean          | false         |
| image           | 水印图像图像<br/>链接 http://abc.com/xxx.png<br/>图像导入 require('./xxx.png')base64              | String           | ''            |
| block           |                                                                                       | Boolean          | false         |
| zIndex          |                                                                                       | Number           | 100           |
| markStyle       | 水印样式                                                                                  | Object           | {}            |
| markClassName   | 水印类名                                                                                  | String           | ''            |
| gapX            | 水印之间的水平间距                                                                             | Number           | 212           |
| gapY            | 水印之间的垂直间距                                                                             | Number           | 222           |
| offsetTop       | 水印在canvas 画布上绘制的垂直偏移量，正常情况下，水印绘制在中间位置, 即 offsetTop = gapY / 2                         | Number           | 0             |
| offsetLeft      | 水印图片距离绘制 canvas 单元的顶部距离水印在canvas 画布上绘制的水平偏移量, 正常情况下，水印绘制在中间位置, 即 offsetTop = gapX / 2 | Number           | 0             |          
|                 |                                                                                       |                  |               |
|                 |                                                                                       |                  |               |

