<template>
    <div class="tp-dialog">
        <div class="tp-dialog-top">
            <div id="viewTdtMap" style="width: 100%; height: 95%; margin-bottom: 15px"></div>
        </div>

        <div class="tp-dialog-bottom">
            <fb-button @on-click="handleClose">关闭</fb-button>
        </div>
    </div>
</template>

<script>




    export default {
        name: 'viewTdtMap',

        // 接收父组件的传参
        props: {
            param: {
                type: Object,
                require: false
            },
            parentPage: {
                type: Object,
                default: null
            }
        },
        // 组件
        components: {
            // 'component-a': ComponentA,
        },
        // 创建方法
        created() {
            // 记录原来的默认值，用于表单重置
        },
        // 初始化方法
        mounted() {
            this.initTDT(this.param);
        },
        data() {
            return {}
        },

        // 方法
        methods: {
            initTDT(param) {
                let initiaLongitude = '';
                let initiaLatitude = '';
                if (param.longitude && param.latitude) {
                    initiaLongitude = param.longitude;
                    initiaLatitude = param.latitude;
                }

                const a = new Promise((resolve, reject) => {
                    console.log(reject);
                    // 如果已加载直接返回
                    if (window.T) {
                        console.log('地图脚本初始化成功...');
                        resolve(window.T);
                    }
                });

                // 显示
                const map = new window.T.Map('viewTdtMap');
                if (initiaLatitude == '' || initiaLongitude == '') {
                    // 初始化地图对象
                    map.centerAndZoom(new window.T.LngLat(app.projectConfig.centerPoint[0], app.projectConfig.centerPoint[1]), 12);
                } else {
                    // 初始化地图对象
                    map.centerAndZoom(new window.T.LngLat(initiaLongitude, initiaLatitude), 12);
                    // 默认向地图上添加标注
                    var marker = new T.Marker(new T.LngLat(initiaLongitude, initiaLatitude));
                    map.addOverLay(marker);
                }
            },

            // 取消
            handleClose() {
                this.closeTpDialog();
            },
        }
    }
</script>

<style lang="less" scoped>

</style>
