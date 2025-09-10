[comment]: <> (fb-docs: docsify/fb-screen/echarts/03/07/README.md)
# FbecBarHorizontalAbout-07-分面条形图

## 案例展示
```html run {title:'试一试', open: true, row:true,}
<template>
<div class="ec-screen-dark-card">
    <div><i>07-分面条形图</i></div>
    <fbec-bar-horizontal-about @click="handleClick" ref="bar-horizontal7"></fbec-bar-horizontal-about>
</div>
</template>
<script>
import { FbecBarHorizontalAbout } from "fb-third";
  export default {
    components: {
        FbecBarHorizontalAbout,
    },
    data () {
      return {}
    },
    mounted() {
        this.$nextTick(() => {
            this.updateBarBase7()
        })
    },
	methods: {
        updateBarBase7() {
            let val = {
                legend: {
                    show: true
                },
                yAxis: [{}, {
                    data: ['分类1', '分类2', '分类1', '分类4', '分类5', '分类6', '分类7']
                }],
                series: [
                    {
                        name: "图例一",
                        stack: "1",
                        label: {
                            show: true,
                            position: 'left'
                        },
                        data:  [3, 20, 62, 34, 55, 65, 33],
                    },
                    {
                        name: "图例二",
                        xAxisIndex: 2,
                        yAxisIndex: 2,
                        data: [11, 38, 23, 39, 66, 66, 79],
                    },
                ]
            }
            this.$refs['bar-horizontal7'].updateOptions(val)
        },
		handleClick (e) {
			console.log(e)
		}
	},
  }
</script>
```

## 引用结构
** `模块方式引入` **
``` js
import { FbecBarHorizontalAbout } from "fb-third";
export default {
    name: '',
    components: {
        FbecBarHorizontalAbout,
    },
}
```

## 数据函数
** `执行函数命名按需，注意 ref 名字统一` **
``` js
updateBarBase7() {
    let val = {
        legend: {
            show: true
        },
        yAxis: [{}, {
            data: ['分类1', '分类2', '分类1', '分类4', '分类5', '分类6', '分类7']
        }],
        series: [
            {
                name: "图例一",
                stack: "1",
                label: {
                    show: true,
                    position: 'left'
                },
                data:  [3, 20, 62, 34, 55, 65, 33],
            },
            {
                name: "图例二",
                xAxisIndex: 2,
                yAxisIndex: 2,
                data: [11, 38, 23, 39, 66, 66, 79],
            },
        ]
    }
    this.$refs['bar-horizontal7'].updateOptions(val)
},
```

