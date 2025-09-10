
/*!
* index
* (c) 2020 lincong1987
*/

var index = {}

export default [

    {
        path: '/sys/licence/upload',
        meta: {
            title: '许可证管理',
        },
        component: () => import('../../../views/sys/licence/upload.vue'),
    }

]
