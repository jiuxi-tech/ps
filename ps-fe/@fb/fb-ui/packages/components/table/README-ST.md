[comment]: <> (fb-docs: docsify/fb-ui/04/simple-table/README.md)

# 表格-Table

用于展示行列数据



## 注意：示例是旧的，请参考，下方得参数列表是最新得。



## 基础用法

- 当有大量结构化数据需要展示时使用；
- 当需要对数据进行排序、搜索、分页、自定义操作等复杂行为时使用

```html run {title:'示例演示'}
<template>
<fb-simple-table :columns="table.columns"
	 pk="userId"
	:data="table.data"
	:multiple="false"/>
</div>
</template>
<script>
  export default {
    data () {
      return {
				table: {
					columns: [
						{name: 'userId',label: 'id',hidden: true,width: 1,	disabled: true,},
						{name: 'userName',label: '姓名',sortable: false,width: 120,align: 'left',},
						{name: 'userGander',label: '性别',sortable: false,width: 100,align: 'left',},
						{name: 'userTel',label: '电话',sortable: false,align: 'left',},
					],
					data: [
						{userId: '11',userName:'用户11',userGander: '1',userTel: '16722299',tags: [1, 2, 3],},
						{userId: '12',userName:'用户12',userGander: '1',userTel: '16722299',},
						{userId: '13',userName:'用户13',userGander: '1',userTel: '16722299',},
						{userId: '14',userName:'用户14',userGander: '1',userTel: '16722299',},
						{userId: '15',userName:'用户15',userGander: '1',userTel: '16722299',},
						{userId: '16',userName:'用户16',userGander: '1',userTel: '16722299',},
					]
				},
      }
    },
  }
</script>
<style>
</style>
```

## 更多用法

### 表格分页

- 通过`service`或`url`加载数据时，支持数据分页

```html run {title:'试一试'}
<template>
<div style="height:410px">
<fb-simple-table  pk="userId" :columns="table.columns"
	 :multiple="false"
	 :service="xxService"></fb-simple-table>
</div>
</template>
<script>
  export default {
    data () {
      return {
				table: {
					columns: [
						{name: 'userId',label: 'id',hidden: true,width: 1,	disabled: true,},
						{name: 'userName',label: '姓名',sortable: false,width: 120,align: 'left',},
						{name: 'userGander',label: '性别',sortable: false,width: 100,align: 'left',},
						{name: 'userTel',label: '电话',sortable: false,align: 'left',},
					],
				},
				xxService: {
					list (param) {
						param.current = param.current || 1;
						return new Promise((resolve, reject) => {
							setTimeout(() => {
								resolve(
									{
										'code': 1,
										'message': '成功',
										'data': {
											'records': [
												{userId: param.current + '1',userName: '骚朱'+ param.current +'1',userGander: '1',userTel: '16722299',tags: [1, 2, 3],},
												{userId: param.current + '2',userName: '骚朱'+ param.current +'2',userGander: '1',userTel: '16722299',},
												{userId: param.current + '3',userName: '骚朱'+ param.current +'2',userGander: '1',userTel: '16722299',},
												{userId: param.current + '4',userName: '骚朱'+ param.current +'2',userGander: '1',userTel: '16722299',},
												{userId: param.current + '5',userName: '骚朱'+ param.current +'2',userGander: '1',userTel: '16722299',},
												{userId: param.current + '6',userName: '骚朱'+ param.current +'2',userGander: '1',userTel: '16722299',},
												{userId: param.current + '7',userName: '骚朱'+ param.current +'2',userGander: '1',userTel: '16722299',},
												{userId: param.current + '8',userName: '骚朱'+ param.current +'2',userGander: '1',userTel: '16722299',},
											],
											'total': 27,
											'size': 10,
											'current': param.current,
											'orders': [],
											'searchCount': true,
											'pages': 3,
										},
										'expand': '',
									},
								)
							}, 1000)
						})
					},
				},
      }
    },
  }
</script>
<style>
</style>
```

### 表格多选

```html run {title:'试一试'}
<template>
<div style="height:440px">
<fb-page-search>
	<template slot="query">
		<fb-simple-table pk="userId" :columns="table.columns"
			:data="table.data"
			@on-row-select="handleRowSelect"
		/>
	</template>
	<template slot="buttons">
		选择项：{{userIds}}
	</template>
</fb-page-search>
</div>
</template>
<script>
  export default {
    data () {
      return {
      			userIds:[],
				table: {
					columns: [
						{name: 'userId',label: 'id',hidden: true,width: 1,	disabled: true,},
						{name: 'userName',label: '姓名',sortable: false,width: 120,align: 'left',},
						{name: 'userGander',label: '性别',sortable: false,width: 100,align: 'left',},
						{name: 'userTel',label: '电话',sortable: false,align: 'left',},
					],
					data: [
						{userId: '11',userName:'用户11',userGander: '1',userTel: '16722299',tags: [1, 2, 3],},
						{userId: '12',userName:'用户12',userGander: '1',userTel: '16722299',},
						{userId: '13',userName:'用户13',userGander: '1',userTel: '16722299',},
						{userId: '14',userName:'用户14',userGander: '1',userTel: '16722299',},
						{userId: '15',userName:'用户15',userGander: '1',userTel: '16722299',},
						{userId: '16',userName:'用户16',userGander: '1',userTel: '16722299',},
					]
				},
      }
    },
	methods: {
		handleRowSelect (row, rows, index) {
			this.userIds = rows.map(row => row.userId);
		},
	},
  }
</script>
<style>
</style>
```

[comment]: <> "### 筛选和排序"

[comment]: <> "### 树形表格"

[comment]: <> "### 行内编辑"

[comment]: <> "### 分组表头"

### 表格单选

```html run {title:'试一试'}
<template>
<div style="height:440px">
<fb-page-search>
	<template slot="query">
		<fb-simple-table pk="userId" :columns="table.columns"
			:radio="true"
			:data="table.data"
			@on-row-select="handleRowSelect"
		/>
	</template>
	<template slot="buttons">
		选择项：{{userIds}}
	</template>
</fb-page-search>
</div>
</template>
<script>
  export default {
    data () {
      return {
      			userIds:[],
				table: {
					columns: [
						{name: 'userId',label: 'id',hidden: true,width: 1,	disabled: true,},
						{name: 'userName',label: '姓名',sortable: false,width: 120,align: 'left',},
						{name: 'userGander',label: '性别',sortable: false,width: 100,align: 'left',},
						{name: 'userTel',label: '电话',sortable: false,align: 'left',},
					],
					data: [
						{userId: '11',userName:'用户11',userGander: '1',userTel: '16722299',tags: [1, 2, 3],},
						{userId: '12',userName:'用户12',userGander: '1',userTel: '16722299',},
						{userId: '13',userName:'用户13',userGander: '1',userTel: '16722299',},
						{userId: '14',userName:'用户14',userGander: '1',userTel: '16722299',},
						{userId: '15',userName:'用户15',userGander: '1',userTel: '16722299',},
						{userId: '16',userName:'用户16',userGander: '1',userTel: '16722299',},
					]
				},
      }
    },
	methods: {
		handleRowSelect (row, rows, index) {
			this.userIds = rows.map(row => row.userId);
		},
	},
  }
</script>
<style>
</style>
```

[comment]: <> "### 筛选和排序"

[comment]: <> "### 树形表格"

[comment]: <> "### 行内编辑"

[comment]: <> "### 分组表头"

### 列显示隐藏

```html run {title:'试一试'}
<template>
<div style="height:440px">
<fb-page-search>
	<template slot="actions">
		<fb-popup-select v-model="show" position="bottomRight">
			<fb-button slot="header" @on-click="show = !show" icon="jpx-icon-setting-gear-fill">显示列</fb-button>
			<fb-card slot="picker" header="列展示" style="width: 168px">
				<fb-container max-height="180px" overflow="auto">
					<fb-checkbox-group
						:value="table.selectedColumns"
						:data="checkColumns"
						@on-change="handleChange"
						vertical
					></fb-checkbox-group>
				</fb-container>
			</fb-card>
		</fb-popup-select>
		<fb-button  icon="search" type="primary">查询</fb-button>
	</template>
	<template slot="table">
		<fb-simple-table pk="userId" :columns="showColumns"
			:data="table.data"
		/>
	</template>
</fb-page-search>
</div>
</template>
<script>
  export default {
    data () {
      return {
				show:false,
				table: {
					selectedColumns:["userName","userGander","userTel"],
					columns: [
						{name: 'userId',label: '标识符',hidden: true,width: 1},
						{name: 'userName',label: '姓名',sortable: false,width: 120,align: 'left',},
						{name: 'userGander',label: '性别',sortable: false,width: 100,align: 'left',},
						{name: 'userTel',label: '电话',sortable: false,align: 'left',},
					],
					data: [
						{userId: '11',userName:'用户11',userGander: '1',userTel: '16722299',tags: [1, 2, 3],},
						{userId: '12',userName:'用户12',userGander: '1',userTel: '16722299',},
						{userId: '13',userName:'用户13',userGander: '1',userTel: '16722299',},
						{userId: '14',userName:'用户14',userGander: '1',userTel: '16722299',},
						{userId: '15',userName:'用户15',userGander: '1',userTel: '16722299',},
						{userId: '16',userName:'用户16',userGander: '1',userTel: '16722299',},
					]
				},
      }
    },
	computed: {
		checkColumns () {
			let items=[];

			this.table.columns.map(column=>{
				if(!(column.hidden||false)){
					column.value = column.name;
					items.push(column)
				}
			})

			return items;
		},
		showColumns() {
			return this.table.columns.filter(col => this.table.selectedColumns.includes(col.name))
		},
	},
	methods: {
		handleChange (chks,value,checked) {
			if(checked){
				this.table.selectedColumns.push(value);
			}else{
				this.table.selectedColumns.splice(this.table.selectedColumns.indexOf(value),1)
			}
		},
	},
  }
</script>
<style>
</style>
```

### 列冻结

- 当有需要固定某一列时使用，对 columns 中列对象添加 freeze 属性
- 切记使用时给scroll属性配置{x: 列width*列数}，避免产生不必要的样式麻烦

```html run {title:'示例演示'}
<template>
<div style="width: 500px">
<fb-simple-table :columns="table.columns"
	 pk="userId"
    :scroll="{x:680, y: 324}"
	:data="table.data"
	:multiple="false"/>
</div>
</div>

</template>
<script>
  export default {
    data () {
      return {
				table: {
					columns: [
						{name: 'userId',label: 'id',hidden: true,width: 1,	disabled: true,},
						{name: 'userName',label: '姓名',sortable: false,width: 120,align: 'left', freeze: 'left'},
						{name: 'cardId',label: 'cardId',sortable: false,width: 120,align: 'left',},
						{name: 'address',label: '住址',sortable: false,width: 120,align: 'left',},
						{name: 'userGander',label: '性别',sortable: false,width: 120,align: 'left',},
						{name: 'userTel',label: '电话',sortable: false,width: 100,align: 'left', freeze: 'right'},
					],
					data: [
						{userId: '11',userName:'用户11',userGander: '1',userTel: '16722299',tags: [1, 2, 3],},
						{userId: '12',userName:'用户12',userGander: '1',userTel: '16722299',},
						{userId: '13',userName:'用户13',userGander: '1',userTel: '16722299',},
						{userId: '14',userName:'用户14',userGander: '1',userTel: '16722299',},
						{userId: '15',userName:'用户15',userGander: '1',userTel: '16722299',},
						{userId: '16',userName:'用户16',userGander: '1',userTel: '16722299',},
					]
				},
      }
    },
  }
</script>
<style>
</style>
```

### 表头合并
- 在 columns 配置对象上使用 children 属性

```html run {title:'示例演示'}
<template>
<fb-simple-table :columns="table.columns"
	 pk="userId"
	:data="table.data"
	:multiple="false"/>
</div>
</template>
<script>
  export default {
    data () {
      return {
				table: {
					columns: [
						{label: '区域', name: 'cityName', width: 150},
                        {label: '浇铸炉数(台)', name: 'furnacesNum',width: 100},
                        {label: '浇铸炉企业数(家)', name: 'entNum',width: 100},
                        {label: '浇铸炉-倾动炉', titleEllipsis :false,
                            children: [
                                {
                                    label: '浇铸炉-倾动炉数(台)',
                                    name: 'tiltingFurnacesNum',
                                    width: 80,
                                    slot: 'a'
                                },
                                {
                                    label: '涉及企业(家)',
                                    name: 'tiltingFurnacesEntNum',
                                    width: 80,
                                    slot: 'b'
                                },
                            ]
                        },
                        {label: '浇铸炉-固定炉',titleEllipsis :false,
                            children: [
                                {
                                    label: '浇铸炉-固定炉数(台)',
                                    name: 'stationaryFurnacesNum',
                                    width: 80,
                                    slot: 'c'
                                },
                                {
                                    label: '涉及企业(家)',
                                    name: 'stationaryFurnacesEntNum',
                                    width: 80,
                                    slot: 'd'
                                },
                            ]
                        },
					],
					data: [
						{userId: '11',cityName:'用户11',entNum: '1',entNum: '16722299',stationaryFurnacesNum: [1, 2, 3],},
						{userId: '12',cityName:'用户12',entNum: '1',entNum: '16722299',},
						{userId: '13',cityName:'用户13',entNum: '1',entNum: '16722299',},
						{userId: '14',cityName:'用户14',entNum: '1',entNum: '16722299',},
						{userId: '15',cityName:'用户15',entNum: '1',entNum: '16722299',},
						{userId: '16',cityName:'用户16',entNum: '1',entNum: '16722299',},
					]
				},
      }
    },
  }
</script>
<style>
</style>
```

### 行列合并

- rowGroups 属性数组用于快捷行合并，当邻近行内容一致，组件内部自动实现合并
- cellSpans 属性对象更加详细的针对单元格合并，属性名回调函数需要返回值 {rowSpan: 1, colSpan: 1}
- 只针对行合并推荐使用 rowGroups 属性，更精细化单元格合并 推荐使用 cellSpans

```html run {title:'示例演示'}
<template>
<div>
<fb-simple-table :row-groups="table.rowGroups" :rownum="false" :show-header="false"
         :cell-spans="table.cellSpans"
         :columns="table.columns" :data="table.data"></fb-simple-table>
</div>
</div>

</template>
<script>
  export default {
    data () {
      return {  
		table: {
            columns: [
                {align: 'center', name: 'a1'},
                {align: 'center', name: 'a2'},
                {align: 'center', name: 'a3'},
                {align: 'center', name: 'a4'},
                {align: 'center', name: 'a5'},
                {align: 'center', name: 'a6'},
                {align: 'center', name: 'a7'},
                {align: 'center', name: 'a8'},
                {align: 'center', name: 'a9'},
                {align: 'center', name: 'a10'},
            ],
            data: [
                {a1: '气体分析', a2: '分析项目', a3: '有毒有害气体名称', a4: '有毒有害气体名称', a5: '可燃气体名称', a6: '可燃气体名称', a7: '氧气含量', a8: '取样分析时间', a9: '分析部位', a10: '分析人'},
                {a1: '气体分析', a2: '分析项目', a3: '', a4: '', a5: '', a6: '', a7: '氧气含量', a8: '取样分析时间', a9: '分析部位', a10: '分析人'},
                {a1: '气体分析', a2: '合格标准', a3: '', a4: '', a5: '', a6: '', a7: '19.5%~21%(体积分数)', a8: '取样分析时间', a9: '分析部位', a10: '分析人'},
                {a1: '气体分析', a2: '分析数据', a3: '', a4: '', a5: '', a6: '', a7: '', a8: '', a9: '', a10: ''},
                {a1: '气体分析', a2: '分析数据', a3: '', a4: '', a5: '', a6: '', a7: '', a8: '', a9: '', a10: ''},
                {a1: '气体分析', a2: '分析数据', a3: '', a4: '', a5: '', a6: '', a7: '', a8: '', a9: '', a10: ''},
                {a1: '气体分析', a2: '分析数据', a3: '', a4: '', a5: '', a6: '', a7: '', a8: '', a9: '', a10: ''},
                {a1: '气体分析', a2: '分析数据', a3: '', a4: '', a5: '', a6: '', a7: '', a8: '', a9: '', a10: ''},
            ],
            rowGroups: ['a1', 'a2', 'a7', 'a8', 'a9', 'a10'],
            cellSpans: {
                a3(val, row, attr, rowIndex, colIndex, id) {
                    // console.log(val, row, attr, rowIndex, colIndex, id)
                    if (val === '有毒有害气体名称') {
                        return {rowSpan: 1, colSpan: 2}
                    } else {
                        return {rowSpan: 1, colSpan: 1} //返回一个对象，包含上面两个属性，用于控制单元格的横向及纵向合并
                    }
                },
                a4(val, row, attr, rowIndex, colIndex, id) {
                    // console.log(val, row, attr, rowIndex, colIndex, id)
                    if (val === '有毒有害气体名称') {
                        return {rowSpan: 1, colSpan: 0}
                    } else {
                        return {rowSpan: 1, colSpan: 1} //返回一个对象，包含上面两个属性，用于控制单元格的横向及纵向合并
                    }
                },
                a5(val, row, attr, rowIndex, colIndex, id) {
                    // console.log(val, row, attr, rowIndex, colIndex, id)
                    if (val === '可燃气体名称') {
                        return {rowSpan: 1, colSpan: 2}
                    } else {
                        return {rowSpan: 1, colSpan: 1} //返回一个对象，包含上面两个属性，用于控制单元格的横向及纵向合并
                    }
                },
                a6(val, row, attr, rowIndex, colIndex, id) {
                    // console.log(val, row, attr, rowIndex, colIndex, id)
                    if (val === '可燃气体名称') {
                        return {rowSpan: 1, colSpan: 0}
                    } else {
                        return {rowSpan: 1, colSpan: 1} //返回一个对象，包含上面两个属性，用于控制单元格的横向及纵向合并
                    }
                },
            }
        }
      }
    },
  }
</script>
<style>
</style>
```


### 表头排序

- sorters 表格属性，key 为 columns 对应 name, value为空对象
- 目前组件内部逻辑实现排序，暂不支持代码判断排序，后续补充

```html run {title:'示例演示'}
<template>
<div style="width: 500px">
<fb-simple-table

				:columns="columns"

				:data="[

				{name: 'aaa',age: 14, sex: 'n'},
				{name: 'bbb',age: 2, sex: 'n'},
				{name: 'ccc',age: 56, sex: 'n'},
				{name: 'ddd',age: 22, sex: 'n'},
				{name: 'eee',age: 12, sex: 'n', xl: 'aasda'},

			]"

				auto-select

				radio

				:scroll="{x: 500, y: 300}"

				:sorters="sorters"


			>
				<template v-slot:sss="props">
					{{ props.row.id }}

				</template>
			</fb-simple-table>
</div>
</div>

</template>
<script>
  export default {
    data () {
      return {
		sorters: {
                name: {},

                money: {},

                age: {},

                desc: {},
            },

            columns: [

                {
                    name: 'name',
                    label: '姓名',
                    width: 100,

                },
                {
                    name: 'sex',
                    label: '性别',
                    width: 100,
                },
                {
                    name: 'age',
                    label: '芳龄',
                    width: 100,
                },
                {
                    name: 'money',
                    label: '存款',
                    slot: 'sss',
                    ellipsis: false,
                    width: 100,
                },
                {
                    name: 'desc',
                    label: 'desc',
                    width: 200,

                    children: [
                        {
                            name: 'desc1',
                            label: 'desc1',
                        },
                        {
                            name: 'desc2',
                            label: 'desc2',
                        },
                    ],

                },
                {
                    name: 'beauty',
                    label: '漂亮程度',

                },
                {
                    name: 'xl',
                    label: '学历',
                    width: 100,
                    freeze: 'right',
                },

            ],
            data: [
                {
                    a: '1',
                    b: 'bb',
                    c: 'cc',
                },
                {
                    a: '2',
                    b: 'bb',
                    c: 'cc',
                },
                {
                    a: '3',
                    b: 'bb',
                    c: 'cc',
                },
                {
                    a: '4',
                    b: 'bb',
                    c: 'cc',
                },
                {
                    a: '5',
                    b: 'bb',
                    c: 'cc',
                },
                {
                    a: '6',
                    b: 'bb',
                    c: 'cc',
                },
                {
                    a: '7',
                    b: 'bb',
                    c: 'cc',
                },
                {
                    a: '8',
                    b: 'bb',
                    c: 'cc',
                },
            ],
      }
    },
  }
</script>
<style>
</style>
```

### 动态可编辑表格

```html run {title:'示例演示'}
<template>
<div>
<div>{{data}}</div>
<fb-button @on-click="handleAdd">新增</fb-button>
<fb-simple-table
    :columns="columns"
    :data="data">
        <template v-slot:name="props">
            <fb-input v-model="data[props.rowIndex].name"/>
        </template>
        <template v-slot:age="props">
            <fb-input v-model="data[props.rowIndex].age"/>
        </template>
        <template v-slot:sex="props">
            <fb-input v-model="data[props.rowIndex].sex"/>
        </template>
        <template v-slot:money="props">
            <fb-input v-model="data[props.rowIndex].money"/>
        </template>
</fb-simple-table>
</div>
</div>

</template>
<script>
  export default {
    data () {
      return {
        columns: [

            {
                name: 'name',
                slot: 'name',
                label: '姓名',
                width: 100,

            },
            {
                name: 'sex',
                slot: 'sex',
                label: '性别',
                width: 100,
            },
            {
                name: 'age',
                slot: 'age',
                label: '芳龄',
                width: 100,
            },
            {
                name: 'money',
                label: '存款',
                slot: 'money',
                ellipsis: false,
                width: 100,
            },

        ],
        data: [],
      }
    },

    methods: {
        handleAdd() {
            this.data.push({
                id: new Date().getTime(),
                name: '',
                sex: '',
                age: '',
                money: '',
            })
        }
    }
  }
</script>
<style>
</style>
```

[comment]: <> "### 筛选和排序"

[comment]: <> "### 树形表格"

[comment]: <> "### 行内编辑"

[comment]: <> "### 分组表头"

## 事件列表

| 事件名 | 说明 | 返回值 |
|:-------|:----|:-------|
| on-row-select | 行选中 | (rowPk, selectedPks, row, rowIndex) => void|
| on-cell-click | 列点击事件 | (col, cellIndex, row, rowIndex) => void|
| on-row-click | 行点击事件 |  (row, rowIndex) => void|
| on-data-load | 数据加载触发事件 | (data) => void|
| on-row-select-all | 全选事件 | （checked， selectedPks, data） | 
| on-auto-scroll-bottom | 滚动到底 |  |
| on-auto-scroll-step | 滚动步进 |  |
|  |  |  |
|  |  |  |
|  |  |  |

## 属性列表

- fb-simple-table

| 属性 | 说明 | 类型 | 默认值 |
| ---- | ---- | ---- | ------ |
| v-model | 双向绑定选中行 rowPk | Array | [] |
| pk | 主键 | String | id |
| columns | 列配置 | Array | [] |
| rownum | 显示行号 | Boolean | true|
| rownum-width | 行号列宽 | Number, String | 50|
| rownum-title | 行号 | String | #|
| multiple | 多选 | Boolean | false|
| multiple-width | 多选列宽 | Number, String | 50|
| multiple-title | 多选列名称 | String | #|
| radio | 单选 | Boolean | false|
| radio-width | 单选列宽 | Number, String | 50|
| radio-title | 单选列名称 | String ||
| subrow-width | 子表列宽 | Number, String | 50|
| subrow-title | 子表列名称 | String | null|
| data | 静态数据绑定 | Array |[]]|
| url | URL数据绑定, 暂时取消支持 | String ||
| service | 服务数据绑定，请提供一个Promise实例 | Object, Function ||
| param | 查询参数 | Objec, Function ||
| auto-query | 监听param，当变化时，自动查询表单 | Boolean |false|
| ~~dataFilter~~ | 响应过滤器，取消支持，请使用dataParse | Function ||
| dataParse | 响应数据过滤, 该方法需要返回一个对象，结构为{<br />data:[], //列表数据 <br />pager: {} // 分页数据<br />} | Function |{data:[], pager: {}}|
| bordered | 边框 | Boolean | false|
| size | 表格尺寸， 可选 s,m,l | String | m |
| scroll | 表格最大可视区， {x: 1000, y:300，autoHeight: true, fillY: true}<br />fillY 配置为true时，会将填充 table body的高度 | Object |{}|
| otherHeightProvider | autoHeight时的 外部高度供给 | Object, Window, Element, Function |window|
| striped | 行配色 | Boolean | true|
| hover | 鼠标移动行变色 | Boolean | true|
| hoverStyle |  | Object, Array, String ||
| pager | 分页参数<br />{current: 1,size: 10,page: 1,pages: 0,total: 0,showQuickJumper: false,} | Object | {current: 1,size: 10,page: 1,pages: 0,total: 0,showQuickJumper: false,} |
| autoLoad | 自动加载 | Boolean | showSizeChanger: false,true |
| children | 分列 | Array | maxLength: 6,null |
| formatters | 单元格格式化处理器<br />formatters: {<br />    tel(val){<br />        return '+086-' + val<br />    }<br />} | Object | {} |
| headerFormatters | 表头单元格格式化处理器<br />headerFormatters: {<br />    username(val){<br />        return val<br />    }<br />} | Object | {} |
| cellSpans | 单元格合并处理器<br />cellSpans: {<br />    username(val){<br />        return {rowSpan: 1, colSpan: 1}//返回一个对象，包含上面两个属性，用于控制单元格的横向及纵向合并<br />    }<br />}<br />注意：此内容可被cellAttrs修改 | Object | {} |
| autoPk | 自动生成行主键, 如果没有 | Boolean, Function | true |
| showPager | 显示分页 | Boolean | true |
| keepSelected | 保持选中状态, 不受分页影响 | Boolean | false |
| autoSelect | 点击行 选中当前行 | Boolean | false |
| noDataText | 空数据显示文字 | String | '没有数据' |
| caption | 表格标题 | String | null |
| loadingText | 加载时显示文字 | String | '' |
| showLoading | 显示数据加载中提示 | Boolean | true |
| showHeader | 显示表头 | Boolean | true |
| useFreezeHeader | 自适应表头 | Boolean | false |
| beforeCellClick | 单元格点击回调 | Function | ()=>{} |
| cellStyles | 单元格样式处理器<br />cellStyles: {<br />    address (val){<br />        return {background: 'red',}<br />    }<br />} | Object | {} |
| cellAttrs | 单元格dom处理器<br />cellAttrs: {<br />    address (attrs, val, ){<br />        if(val === '北京') { attrs.title = "首都" }<br />        return attrs<br />    }<br />} | Object | {} |
| rowGroups | 用于行合并，使用方法  :row-groups="['colName1', 'colName2' ]" | Array | [] |
| noHeadSplitter | 不显示表头列间分隔线 | Boolean | false |
| longHeadSplitter | 表头列间分隔线全长（高度100%） | Boolean | false |
| headBordered | 不显示表头边框，请配合bordered 使用 | Boolean | false |
| noDataStyle | 数据为空提示的样式<br />如 :no-data-style="{marginTop: `150px`}" | Object, Array, String | {} |
| wrapperStyle | 表格外壳样式，<br />如:wrapper-style="{fontSize: '30px'}" | Object, Array, String | {} |
| autoScroll | 自动滚动 | Boolean | false |
| autoScrollSpeed | 滚动速度 | Number, String | 100 |
| autoScrollStep | 滚动步进 | Number, String | 1 |
| autoScrollDelay | 滚动首尾延时 | Number, String | 200 |
| autoScrollInfinite | 无限滚动，该模式时，不显示滚动条，无首尾延时 | Boolean | false |
| containerStyle | 表格容器样式 | Object, Array, String | {} |
| sorters | 表格列排序<br /> 如 :sorters="{name: {}}" | Object | {} |
| textAlign | 表格内文字通用配置（控制列配置 align，titleAlign） | String | left |
| enableResize | 启用表头拖拽调节列宽 | Boolean | true |



- 列配置

| 属性 | 说明 | 类型 | 默认值 |
|:-----|:----|:-----|:-------|
| name | 列名 | String ||
| label | 显示列名 |String  ||
| width | 列宽 |Number  ||
| sortable | 排序 | Boolean  | false|
| align | 对齐方式 left center right | String | left |
| titleAlign | 表头单元格对齐方式 left center right | String   | left |
| ~~hidden~~ | 隐藏,已经过时，不建议使用 | Boolean | false|
| ~~formatter~~ | 单元格格式化，已经过时，请使用 table 得formatters属性 | Function |  (val, row) {return val}|
| ellipsis | 当单元格内容溢出显示...<br />如果是slot 建议ellipsis: false | Boolean | true |
| titleEllipsis | 表头单元格溢出 | Boolean | true |
| slot | 单元格插槽名称 | String |  |
| titleSlot | 表头单元格插槽名称 | String |  |
| minWidth | 最小列宽，默认80 | Number | 80 |
| resize | 可调列宽 | Boolean | true |
| showTitle | 显示描述信息，默认为格式化后的内容，如没有则显示单元格文本内容，该内容使用cellAttrs 修改 | Boolean | true |
| headRowSpan | 控制表头的单元格列合并<br /> | Number |  |
| headColSpan | 控制表头的单元格行合并<br />[ <br />   {       label: 'L1',       name: 'l1',       headColSpan: 2,    }, <br />   {       label: 'L2',       name: 'l2',       headColSpan: 0,    },  <br />   {       label: 'L3',       name: 'l3',    }<br />] | Number | |



方法

当配置ref='table'时，可以调用$refs.table[functionName]

| 名称            | 说明               | 类型  | 返回值 |
| :-------------- | :----------------- | :---- | :----- |
| getSelectedRows | 获取当前选中行数组 | Array | []     |
| doSearch | 绑定service时触发再次查询事件 | Array | []     |
|                 |                    |       |        |



  slot

| 名称    | 说明           | 默认值     |
| :------ | :-------   | :--------- |
| no-data | 没有数据        | noDataText |
| caption | 表格标题 |  |
| footer | 表格尾 |  |
| subrow | 子表格 |  |



单元格 slot props

```
<template v-slot:titleSlot_name="props">
   <fb-icon name="like-fill" :color="colors.red"/>
   <fb-text :color="colors.red"> 姓名</fb-text>
</template>
```

| 属性      | 说明       | 类型             | 默认值 |
| :-------- | :--------- | :--------------- | :----- |
| row       | 列数据对象 | Object           |        |
| column    | 列配置对象 | Object           |        |
| rowIndex  | 行索引     | Number           |        |
| cellIndex | 单元格索引 | Number           |        |
| rowPk     | 行主键     | Number \| String |        |





- fb-ui@1.1.205

#feat
1、table组件新增 scroll.autoHeight 属性，如:scroll="{x: 1500, y:300, autoHeight: true}" 这时表格的内容高度为 otherHeightProvider 的高度 - y, 当autoHeight为function类型时，autoHeight的具体值由其返回值提供
2、table组件新增  otherHeightProvider 属性，此属性默认为window，如果传一个DOM，autoHeight的外围高度将由此提供
3、table组件内的pager支持simple、align等属性

#fix

#style
1、调整 page-search 典型页样式，使其更加紧凑
2、调整 table 组件 size： m 时行内边距上下为10px
3、调整 table 组件 header background-color 为grey_2
4、解决 form-item 样式在 label-position： top 时过宽的问题
