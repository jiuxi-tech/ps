<template>
    <div class="tp-dialog">
        <div class="tp-dialog-top">
            <div id="pickupTdtMap" style="width: 100%; height: 85%; margin-bottom: 15px;"></div>
            <fb-form ref="fbform">
                <fb-row>
                    <fb-col span="12">
                        <fb-form-item label="经度" prop="longitude" :rule="{required: true}">
                            <fb-input v-model="formData.longitude" readonly></fb-input>
                        </fb-form-item>
                    </fb-col>
                    <fb-col span="12">
                        <fb-form-item label="纬度" prop="latitude" :rule="{required: true}">
                            <fb-input v-model="formData.latitude" readonly></fb-input>
                        </fb-form-item>
                    </fb-col>
                </fb-row>
            </fb-form>
        </div>

        <div class="tp-dialog-bottom">
            <fb-button style="margin-right: 12px" type="primary" @on-click="add">保存</fb-button>
            <fb-button @on-click="handleClose">关闭</fb-button>
        </div>
    </div>
</template>

<script>




    export default {
        name: 'pickupTdtMap',

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
            return {
                formData: {
                    longitude: '',
                    latitude: '',
                }
            }
        },

        // 方法
        methods: {
            initTDT(param) {
                let that = this;

                let initiaLongitude = app.projectConfig.centerPoint[0];
                let initiaLatitude = app.projectConfig.centerPoint[1];
                if (param.longitude && param.latitude) {
                    initiaLongitude = param.longitude;
                    initiaLatitude = param.latitude;
                }
                that.formData.longitude = initiaLongitude;
                that.formData.latitude = initiaLatitude;


                const a = new Promise((resolve, reject) => {
                    console.log(reject);
                    // 如果已加载直接返回
                    if (window.T) {
                        console.log('地图脚本初始化成功...');
                        resolve(window.T);
                    }
                });

                // 显示
                const map = new window.T.Map('pickupTdtMap');
                // 初始化地图对象
                map.centerAndZoom(new window.T.LngLat(initiaLongitude, initiaLatitude), 12);

                // 默认向地图上添加标注
                var marker = new T.Marker(new T.LngLat(initiaLongitude, initiaLatitude));
                map.addOverLay(marker);


                // 添加点击事件
                let cp = new T.CoordinatePickup(map, {callback: getLngLat})
                cp.addEvent();


                function getLngLat(lnglat) {
                    // 先清除覆盖物
                    map.clearOverLays();
                    that.formData.longitude = lnglat.getLng();
                    that.formData.latitude = lnglat.getLat();
                    var marker = new T.Marker(new T.LngLat(lnglat.getLng(), lnglat.getLat()));
                    //向地图上添加标注
                    map.addOverLay(marker);
                }

            },

            // 数据回填
            add() {
                this.$refs.fbform.validate((result) => {
                    if (result === true) {
                        this.closeTpDialog(this.formData);
                    }
                })
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
