<template>
    <fb-dialog ref="dialog" title="dialog1" width="1000px" height="600px">
        <fb-simple-table ref="entTable" pk="userId" :columns="table.columns" :auto-load="false"
                         :multiple="false"
                         :service="xxService">
            <template v-slot:userName="props">
                <fb-link :label="props.row.userName"></fb-link>
            </template>
        </fb-simple-table>
    </fb-dialog>
</template>

<script>
    import ScreenHeader from '../../ScreenHeader'
    import PanelCorner from '../../PanelCorner'
    import FlopBoard from '../../FlopBoard'
    import txProgressCircle from '../../charts/model/dom/tx-progress-circle'
    import axios from 'axios'
    import Page from '@/util/Page'
    import LeafletMap from '../../mixins/LeafletMap'
    import ScreenPanel from '../../ScreenPanel'
    import ScreenPanelTitle from '../../ScreenPanelTitle'


    /**
     * index
     * (c) 2021 lincong1987
     */


    export default {
        name: 'lyly',
        components: {
            ScreenPanelTitle,
            ScreenPanel,
            FlopBoard,
            PanelCorner,
            ScreenHeader,
        },
        mixins: [Page],

        data() {
            return {
                table: {
                    columns: [
                        {name: 'userId', label: 'id', hidden: true, width: 1, disabled: true,},
                        {name: 'userName', slot: 'userName', label: '姓名', sortable: false, width: 120, align: 'left',},
                        {name: 'userGander', label: '性别', sortable: false, width: 100, align: 'left',},
                        {name: 'userTel', label: '电话', sortable: false, align: 'left',},
                    ],
                },
                xxService: {
                    list(param) {
                        param.current = param.current || 1;
                        return new Promise((resolve, reject) => {
                            setTimeout(() => {
                                resolve(
                                    {
                                        'code': 1,
                                        'message': '成功',
                                        'data': {
                                            'records': [
                                                {
                                                    userId: param.current + '1',
                                                    userName: '骚朱' + param.current + '1',
                                                    userGander: '1',
                                                    userTel: '16722299',
                                                    tags: [1, 2, 3],
                                                },
                                                {
                                                    userId: param.current + '2',
                                                    userName: '骚朱' + param.current + '2',
                                                    userGander: '1',
                                                    userTel: '16722299',
                                                },
                                                {
                                                    userId: param.current + '3',
                                                    userName: '骚朱' + param.current + '2',
                                                    userGander: '1',
                                                    userTel: '16722299',
                                                },
                                                {
                                                    userId: param.current + '4',
                                                    userName: '骚朱' + param.current + '2',
                                                    userGander: '1',
                                                    userTel: '16722299',
                                                },
                                                {
                                                    userId: param.current + '5',
                                                    userName: '骚朱' + param.current + '2',
                                                    userGander: '1',
                                                    userTel: '16722299',
                                                },
                                                {
                                                    userId: param.current + '6',
                                                    userName: '骚朱' + param.current + '2',
                                                    userGander: '1',
                                                    userTel: '16722299',
                                                },
                                                {
                                                    userId: param.current + '7',
                                                    userName: '骚朱' + param.current + '2',
                                                    userGander: '1',
                                                    userTel: '16722299',
                                                },
                                                {
                                                    userId: param.current + '8',
                                                    userName: '骚朱' + param.current + '2',
                                                    userGander: '1',
                                                    userTel: '16722299',
                                                },
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
        mounted() {
        },
        methods: {
            show() {
                this.$refs['dialog'].show()
                this.$nextTick(() => {
                    this.$refs['entTable'].doSearch()
                })
            },
        },

    }
</script>
