[comment]: <> (fb-docs: docsify/fb-ui/03/rate/README.md)

# 评分-Rate

用于评分。

## 基础用法

```html run {title:'示例演示'}
<template>
<div>

<fb-card header="默认，双向绑定">
    <fb-rate v-model="value1"></fb-rate>

    <fb-container width="200px">
        <fb-input-number v-model="value1" :max="5" :min="0"></fb-input-number>
    </fb-container>
</fb-card>
<br />
<fb-card header="半星">
    <div>值：{{ valueHalf }}</div>
    <fb-rate v-model="valueHalf" half size="40"></fb-rate>

</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	 value1: 1,
         valueHalf: 2.5,
       }
    },
  }
</script>
<style>
</style>
```

## 更多用法

```html run {title:'示例演示'}
<template>
<div>


		<fb-card header="clearable">
			<fb-rate clearable :max-length="3"></fb-rate>
		</fb-card>

		<fb-card header="size">
			s
			<fb-rate size="s"></fb-rate>
			m
			<fb-rate size="m"></fb-rate>
			l
			<fb-rate size="l"></fb-rate>
			15
			<fb-rate size="15"></fb-rate>
			16
			<fb-rate size="16"></fb-rate>
			30
			<fb-rate size="30"></fb-rate>
		</fb-card>

		<fb-card header="icon">
			<fb-rate clearable :max-length="10" icon="notice-fill"></fb-rate>
		</fb-card>

		<fb-card header="color">
			<fb-container>
				<fb-rate in-active-color="#000" active-color="red" hover-color="green"></fb-rate>
			</fb-container>
			<fb-container>
				<fb-rate active-color="red" :value="5" max-length="10"></fb-rate>
			</fb-container>
		</fb-card>

		<fb-card header="slot#star">
			<fb-rate prepend="全" :value="4">
				<template #star>
					好
				</template>
			</fb-rate>
			<div></div>
			<fb-rate prepend="半个" :value="4" half>
				<template #star>
					好
				</template>
			</fb-rate>
			<div></div>
			<fb-rate prepend="半个" :value="4" half>
				<template #star>
					你好
				</template>
			</fb-rate>
			<div></div>
			<fb-rate prepend="多个字也支持半个" :value="3.5" half desc>
				<template #star>
					你好嘛
				</template>
			</fb-rate>
			<div></div>
			<fb-rate prepend="复杂插槽也支持半个" :value="3.5" half desc active-color="red" size="20">
				<template #star>
					<fb-icon name="thunderstorm-fill"></fb-icon>
					<fb-text>看我</fb-text>
				</template>
			</fb-rate>
			<div></div>
			<fb-rate prepend="复杂作用域插槽" :value="2" desc active-color="green" size="20">
				<template #star="props">
					<fb-icon v-if="props.star.value === 1" name="sun-fill"/>
					<fb-icon v-if="props.star.value === 2" name="hail-fill"/>
					<fb-icon v-if="props.star.value === 3" name="thunderstorm-fill"/>
					<fb-icon v-if="props.star.value === 4" name="cloud-fill"/>
					<fb-icon v-if="props.star.value === 5" name="fog-fill"/>

					{{ props.star.index }} - {{ props.star.value }}
				</template>
			</fb-rate>
		</fb-card>

		<fb-card header="tooltip">
			<fb-rate readonly :value="4" tooltip></fb-rate>
		</fb-card>

		<fb-card header="超复杂定义">
			超复杂
			<fb-rate value="eee" max-length="6"
					 desc
					 :size="['s','m','l','xl','xxl','xxxl']"
					 :labels="['祝红桃','骚攀','小猪猪','骚艳','小芳芳', '杰哥']"
					 :values="['e','ee','eee','eeee','eeeee','eeeeee']"
					 :icon="['hexagram-fill','leaf-fill','setting-gear-fill','like-fill','navigation-fill','target2-fill']"
					 :in-active-color="[colors.grey_3,colors.grey_4,colors.grey_5,colors.grey_6,colors.grey_7]"
					 :active-color="[colors.red_3,colors.orange_3,colors.yellow_3,colors.green_3,colors.blue_3,colors.purple_3]"
					 :hover-color="[colors.red_4,colors.orange_4,colors.yellow_4,colors.green_4,colors.blue_4,colors.purple_4]"

			></fb-rate>

			<fb-container>
				超复杂，半个
				<fb-rate value="eee" max-length="3" half
						 desc
						 :size="[30, 30, 20, 20, 30, 30]"
						 :labels="['半个祝红桃','祝红桃','半个骚攀','骚攀','半个小猪猪','小猪猪']"
						 :values="['a','aa','b','bb','c','cc']"
						 :icon="['hexagram-fill','hexagram-fill','setting-gear-fill','setting-gear-fill','like-fill','like-fill',]"
						 :tooltip="['半个祝红桃xx','祝红桃xx','半个骚攀xx','骚攀xx','半个小猪猪xx','小猪猪xx']"
						 :in-active-color="[colors.grey_3,colors.grey_4,colors.grey_5,colors.grey_6,colors.grey_7]"
						 :active-color="[colors.red_3,colors.orange_3,colors.yellow_3,colors.green_3,colors.blue_3,colors.purple_3]"
						 :hover-color="[colors.red_4,colors.orange_4,colors.yellow_4,colors.green_4,colors.blue_4,colors.purple_4]"

				></fb-rate>
			</fb-container>
		</fb-card>

		<fb-card header="readonly">
			<fb-rate readonly :value="4"></fb-rate>
		</fb-card>

		<fb-card header="prepend">
			<fb-rate clearable :max-length="3" prepend="分数"></fb-rate>
		</fb-card>

		<fb-card header="append">
			<fb-rate clearable :max-length="3" append="分数"></fb-rate>
			<div></div>
			<fb-rate clearable :max-length="30">
				<template #append="props">
					<fb-text v-if="props.star && props.star.value">
						{{ props.star && props.star.value }} 分
					</fb-text>
					<fb-text v-else>
						人的正确思想是从哪里来的?
					</fb-text>
				</template>
			</fb-rate>
		</fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	 value1: 1,
         valueHalf: 2.5,
       }
    },
  }
</script>
<style>
</style>
```

### color and slot

```html run {title:'示例演示'}
<template>
<div>

    <fb-card header="color">
        <fb-container>
            <fb-rate in-active-color="#000" active-color="red" hover-color="green"></fb-rate>
        </fb-container>
        <fb-container>
            <fb-rate active-color="red" :value="5" max-length="10"></fb-rate>
        </fb-container>
    </fb-card>

    <fb-card header="slot#icon">
        <fb-rate prepend="全" :value="4">
            <template #icon>
                好
            </template>
        </fb-rate>
        <div></div>
        <fb-rate prepend="半个" :value="4" half>
            <template #icon>
                好
            </template>
        </fb-rate>
        <div></div>
        <fb-rate prepend="半个" :value="4" half>
            <template #icon>
                你好
            </template>
        </fb-rate>
        <div></div>
        <fb-rate prepend="多个字也支持半个" :value="3.5" half desc>
            <template #icon>
                你好嘛
            </template>
        </fb-rate>
    </fb-card>


    <fb-card header="tooltip">
        <fb-rate readonly :value="4" tooltip></fb-rate>
    </fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	 value1: 1,
         valueHalf: 2.5,
       }
    },
  }
</script>
<style>
</style>
```

### 超复杂

```html run {title:'示例演示'}
<template>
<div>
    <fb-card header="超复杂定义">
        超复杂
        <fb-rate value="eee" max-length="6"
                 desc
                 :size="['s','m','l','xl','xxl','xxxl']"
                 :labels="['祝红桃','骚攀','小猪猪','骚艳','小芳芳', '杰哥']"
                 :values="['e','ee','eee','eeee','eeeee','eeeeee']"
                 :icon="['hexagram-fill','leaf-fill','setting-gear-fill','like-fill','navigation-fill','target2-fill']"
                 :in-active-color="[colors.grey_3,colors.grey_4,colors.grey_5,colors.grey_6,colors.grey_7]"
                 :active-color="[colors.red_3,colors.orange_3,colors.yellow_3,colors.green_3,colors.blue_3,colors.purple_3]"
                 :hover-color="[colors.red_4,colors.orange_4,colors.yellow_4,colors.green_4,colors.blue_4,colors.purple_4]"

        ></fb-rate>

        <fb-container>
            超复杂，半个
            <fb-rate value="eee" max-length="3" half
                     desc
                     :size="[30, 30, 20, 20, 30, 30]"
                     :labels="['半个祝红桃','祝红桃','半个骚攀','骚攀','半个小猪猪','小猪猪']"
                     :values="['a','aa','b','bb','c','cc']"
                     :icon="['hexagram-fill','hexagram-fill','setting-gear-fill','setting-gear-fill','like-fill','like-fill',]"
                     :in-active-color="[colors.grey_3,colors.grey_4,colors.grey_5,colors.grey_6,colors.grey_7]"
                     :active-color="[colors.red_3,colors.orange_3,colors.yellow_3,colors.green_3,colors.blue_3,colors.purple_3]"
                     :hover-color="[colors.red_4,colors.orange_4,colors.yellow_4,colors.green_4,colors.blue_4,colors.purple_4]"
            ></fb-rate>
        </fb-container>
    </fb-card>


    <fb-card header="readonly">
        <fb-rate readonly :value="4"></fb-rate>
    </fb-card>

    <fb-card header="prepend">
        <fb-rate clearable :max-length="3" prepend="分数"></fb-rate>
    </fb-card>

    <fb-card header="append">
        <fb-rate clearable :max-length="3" append="分数"></fb-rate>
    </fb-card>
</div>
</template>
<script>
  export default {
    data () {
      return {
      	 value1: 1,
         valueHalf: 2.5,
       }
    },
  }
</script>
<style>
</style>
```

## 属性列表

| 属性            | 说明                                  | 类型                    | 默认值       |
|:--------------|:------------------------------------|:----------------------|:----------|
| value         | 当前值                                 | Number, String        | 0         |
| half          | 是否支持半数                              | Boolean               | false     |
| size          | 尺寸，支持单一数值及数组                        | String, Number, Array | 16        |
| maxLength     | 最大数量                                | String, Number        | 5         |
| activeColor   | 激活项颜色，支持单一数值及数组                     | String, Array         | #FADB14   |
| hoverColor    | 鼠标移动颜色，支持单一数值及数组                    | String, Array         | #FADB14   |
| inActiveColor | 不活动项的颜色，支持单一数值及数组                   | String, Array         | #F0EFF5   |
| icon          | 图标，支持单一数值及数组                        | String, Array         | star-fill |
| labels        | 分值描述数组                              | Array                 | null      |
| values        | 分值数组                                | Array                 | null      |
| data          | 暂不支持                                | Array                 | undefined |
| readonly      | 只读                                  | Boolean               | false     |
| clearable     | 可清除，在当前激活分值上再次点击，可以取消分值，也可以点清除按钮    | Boolean               | false     |
| prepend       | 前缀内容                                | String                | undefined |
| append        | 后缀内容                                | String                | undefined |
| desc          | 是否显示评分值描述                           | Boolean               | false     |
| tooltip       | 是否在每个分值上显示气泡窗，当类型为数组时，可以显示每个元素对应的内容 | Boolean               | false     |
| tabindex      | 键盘表单顺序值                             | Number                | 0         |

## 事件列表

| 事件名       | 说明   | 返回值    |
|:----------|:-----|:-------|
| on-click  | 点击事件 | star   |
| on-change | 点击事件 | value  |
| input     | 输入事件 | value  |

## 插槽或作用域插槽

| 名称      | 作用域值 | 说明               | 默认值 |
|:--------|------|:-----------------|:----|
| star    | star | 评分的图标（尽情发挥想象力！！） |     |
| prepend | star | 前缀               |     |
| append  | star | 后缀               |     |
| desc    | star | 描述               |     |


