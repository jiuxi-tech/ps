[comment]: <> (fb-docs: docsify/fb-ui/04/property/README.md)

# 文本对应布局-Property

多个字段在一个样式容器内展示，例如一个label: value   
默认两个 Property-item 标签展示时在一行。   
如需一个 Property-item 占一行，配合span=2属性。但要关注split属性   

## 基础用法

```html run {title:'示例演示'}
<template>
<div>
    <fb-fieldset label="基础用法"/>
    <fb-property label-width="140px">

        <fb-property-item label="aaa">111</fb-property-item>
        <fb-property-item label="aaa">
            asdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdas
        </fb-property-item>


        <fb-property-item label="aaa">111</fb-property-item>
        <fb-property-item label="aaa">
            asdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdas
        </fb-property-item>
        <fb-property-item label="aaa">111</fb-property-item>

	</fb-property>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
  }
</script>
```

## bordered 样式

```html run {title:'示例演示'}
<template>
<div>
    <fb-fieldset label="bordered"/>
    <fb-property label-width="140px" bordered>

        <fb-property-item label="aaa">111</fb-property-item>
        <fb-property-item label="aaa">
            asdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdas
        </fb-property-item>


        <fb-property-item label="aaa">111</fb-property-item>
        <fb-property-item label="aaa">
            asdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdasasdadasdasdasdadasdasdadssdaasdasdasdasdas
        </fb-property-item>
        <fb-property-item label="aaa" span="2">111</fb-property-item>

	</fb-property>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
  }
</script>
```

## 行列合并

```html run {title:'示例演示'}
<template>
<div>
    <fb-fieldset label="单元格合并"/>
    <fb-property bordered :colon="false" split="4">

            <fb-property-item :label-style="{display: 'none'}" :content-style="{  color: 'red'}" span="4">部门
            </fb-property-item>
            <fb-property-item label="111">111</fb-property-item>
            <fb-property-item label="222">222</fb-property-item>
            <fb-property-item label="111">111</fb-property-item>
            <fb-property-item label="222">222</fb-property-item>


            <fb-property-item :label-style="{display: 'none'}" :content-style="{  color: 'red'}" span="4">部门
            </fb-property-item>
            <fb-property-item label="111" span="2">111</fb-property-item>
            <fb-property-item label="111">111</fb-property-item>
            <fb-property-item label="222">222</fb-property-item>


            <fb-property-item :label-style="{display: 'none'}" :content-style="{  color: 'red'}" span="4">部门
            </fb-property-item>
            <fb-property-item label="111">111</fb-property-item>
            <fb-property-item label="222">222</fb-property-item>
            <fb-property-item label="111" span="2">111</fb-property-item>




            <fb-property-item :label-style="{display: 'none'}" :content-style="{  color: 'red'}" span="2"
            >部门111
            </fb-property-item>
            <fb-property-item label="111">111</fb-property-item>
            <fb-property-item label="222">222</fb-property-item>

            <fb-property-item :label-style="{display: 'none'}" :content-style="{  color: 'red'}" span="2"
                              row-span="2" >部门222
            </fb-property-item>
            <fb-property-item label="111">111</fb-property-item>
            <fb-property-item label="222">222</fb-property-item>

            <fb-property-item style="display: none" :label-style="{display: 'none'}" :content-style="{  color: 'red'}" span="2"
            >部门222
            </fb-property-item>
            <fb-property-item label="333">333</fb-property-item>
            <fb-property-item label="444">444</fb-property-item>


            <fb-property-item :label-style="{display: 'none'}" :content-style="{  color: 'red'}"
            >部门111
            </fb-property-item>
            <fb-property-item label="111">111</fb-property-item>
            <fb-property-item label="222">222</fb-property-item>
            <fb-property-item label="333">33</fb-property-item>

            <fb-property-item label="主管部门" :content-style="{  color: 'red'}"
                              row-span="2" >鲁尚毅 15967109957
            </fb-property-item>
            <fb-property-item label="111">111</fb-property-item>
            <fb-property-item label="222">222</fb-property-item>
            <fb-property-item label="333">33</fb-property-item>

            <fb-property-item style="display: none" :label-style="{display: 'none'}" :content-style="{  color: 'red'}"
            >部门222
            </fb-property-item>
            <fb-property-item label="333">333</fb-property-item>
            <fb-property-item label="444">444</fb-property-item>
            <fb-property-item label="333">33</fb-property-item>




            <fb-property-item :label-style="{display: 'none'}" :content-style="{  color: 'red'}"
                              row-span="2" >部门222
            </fb-property-item>
            <fb-property-item label="111">111</fb-property-item>
            <fb-property-item :label-style="{display: 'none'}" :content-style="{  color: 'red'}"
                              row-span="2" >部门222
            </fb-property-item>
            <fb-property-item label="333">33</fb-property-item>

            <fb-property-item style="display: none" :label-style="{display: 'none'}" :content-style="{  color: 'red'}"
            >部门222
            </fb-property-item>
            <fb-property-item label="333">333</fb-property-item>
            <fb-property-item style="display: none" :label-style="{display: 'none'}" :content-style="{  color: 'red'}"
            >部门222
            </fb-property-item>
            <fb-property-item label="333">33</fb-property-item>


        </fb-property>
</div>
</template>
<script>
  export default {
    data () {
      return {
      }
    },
  }
</script>
```

## FbProperty 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| size | 尺寸  | String | s, m, l, xl |
| title | 标题 | String | null |
| split | 一行分割几个item,默认一行容纳两个item | Number, String | 2 |
| bordered | 边框样式 | Boolean | false |
| colon | 冒号 | Boolean | false |
| vertical | 是否上下显示 | Boolean | false |
| labelWidth | 文本宽度 | Number, String | 120 |
| labelAlign | 文本左右排列方式 | String | right |
| contentAlign | 内容左右排列方式 | String | right |
| ellipsis | 文字超出省略号展示 | Boolean | false |
| mode | 模式 | String |  |
| verticalAlign | 文字上下排列方式 | String | top middle bottom |
| bodyStyle | 容器样式 | Object |  |
| labelStyle | 文本样式 | Object |  |
| contentStyle | 内容样式 | Object |  |
| value | 展示数组 | Array, Object | null |
| sort | 排序 | String | 'sort' |

## FbPropertyItem 属性列表

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| size | 尺寸  | String | s, m, l, xl |
| type | 类型 | String | label |
| label | 文本 | String |  |
| value | 内容 | String |  |
| span | 占据split的几份 | String, Number | 1 |
| colon | 冒号 | Boolean | false |
| labelWidth | 文本宽度 | Number, String | 120 |
| labelAlign | 文本左右排列方式 | String | right |
| contentAlign | 内容左右排列方式 | String | right |
| ellipsis | 文字超出省略号展示 | Boolean | false |
| verticalAlign | 文字上下排列方式 | String | top middle bottom |
| bodyStyle | 容器样式 | Object |  |
| labelStyle | 文本样式 | Object |  |
| contentStyle | 内容样式 | Object |  |
| rowIndex | 行下标 | String, Number | undefined |
| colSpan | 列合并份数 | String, Number | undefined |
| rowSpan | 行合并份数 | String, Number | undefined |


