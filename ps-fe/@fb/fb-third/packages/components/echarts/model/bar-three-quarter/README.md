[comment]: <> (fb-docs: docsify/fb-screen/echarts/03/08/README.md)
# FbecBarThreeQuarter-08-玉珏图

## 案例展示
```html run {title:'试一试', open: true, row:true,}
<template>
<div class="ec-screen-dark-card">
    <div><i>08-玉珏图</i> <i style="color: red">画布大小影响布局</i></div>
    <fbec-bar-three-quarter @click="handleClick" ref="bar-quarter8"></fbec-bar-three-quarter>
</div>
</template>
<script>
import { FbecBarThreeQuarter } from "fb-third";
  export default {
    components: {
        FbecBarThreeQuarter,
    },
    data () {
      return {}
    },
    mounted() {
        this.$nextTick(() => {
            this.updateBarBase8()
        })
    },
	methods: {
        updateBarBase8() {
            let val = {
                customValue: {
                    data: [
                        { name: '机关干部', value: 70 },
                        { name: '专家学者', value: 90 },
                        { name: '单位职工', value: 110 },
                        { name: '农村居民', value: 120 },
                        { name: '其他', value: 130 },
                    ]
                }
            }
            this.$refs['bar-quarter8'].updateOptions(val)
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
import { FbecBarThreeQuarter } from "fb-third";
export default {
    name: '',
    components: {
        FbecBarThreeQuarter,
    },
}
```

## 数据函数
** `执行函数命名按需，注意 ref 名字统一` **
``` js
updateBarBase8() {
    let val = {
        customValue: {
            data: [
                { name: '机关干部', value: 70 },
                { name: '专家学者', value: 90 },
                { name: '单位职工', value: 110 },
                { name: '农村居民', value: 120 },
                { name: '其他', value: 130 },
            ]
        }
    }
    this.$refs['bar-quarter8'].updateOptions(val)
},
```


