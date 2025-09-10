[comment]: <> (fb-docs: docsify/fb-screen/echarts/03/09/README.md)
# FbecBarBaseTwoy-09-双轴图

## 案例展示
```html run {title:'试一试', open: true, row:true,}
<template>
<div class="ec-screen-dark-card">
    <div><i>09-双轴图</i></div>
    <fbec-bar-base-twoy @click="handleClick" ref="bar-base9"></fbec-bar-base-twoy>
</div>
</template>
<script>
import { FbecBarBaseTwoy } from "fb-third";
  export default {
    components: {
        FbecBarBaseTwoy,
    },
    data () {
      return {}
    },
    mounted() {
        this.$nextTick(() => {
            this.updateBarBase9()
        })
    },
	methods: {
        updateBarBase9() {
            let val = {
                legend: {
                    show: true
                },
                xAxis: {
                    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
                },
                series: [
                    {
                        name: '图例1',
                        data: [820, 932, 901, 934, 1290, 1330, 1320],
                    },
                    {
                        name: '图例2',
                        data: [820, 932, 901, 934, 1290, 1330, 1320],
                    },
                    {
                        name: '图例3',
                        data: [820, 932, 901, 934, 1290, 1330, 1320],
                    },
                    {
                        name: '图例4',
                        type: 'line',
                        yAxisIndex: 1,
                        data: [82, 93, 90, 34, 12, 13, 20],
                    },
                ]
            }
            this.$refs['bar-base9'].updateOptions(val)
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
import { FbecBarBaseTwoy } from "fb-third";
export default {
    name: '',
    components: {
        FbecBarBaseTwoy,
    },
}
```

## 数据函数
** `执行函数命名按需，注意 ref 名字统一` **
``` js
updateBarBase9() {
    let val = {
        legend: {
            show: true
        },
        xAxis: {
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        series: [
            {
                name: '图例1',
                data: [820, 932, 901, 934, 1290, 1330, 1320],
            },
            {
                name: '图例2',
                data: [820, 932, 901, 934, 1290, 1330, 1320],
            },
            {
                name: '图例3',
                data: [820, 932, 901, 934, 1290, 1330, 1320],
            },
            {
                name: '图例4',
                type: 'line',
                yAxisIndex: 1,
                data: [82, 93, 90, 34, 12, 13, 20],
            },
        ]
    }
    this.$refs['bar-base9'].updateOptions(val)
},
```

