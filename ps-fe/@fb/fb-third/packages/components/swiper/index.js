/*
* VueAwesomeSwiper
* Author: surmon@foxmail.com
* Github: https://github.com/surmon-china/vue-awesome-swiper
*/


import _Swiper from 'swiper'
// import * as _Swiper from 'swiper/dist/js/swiper.esm.js'
import SlideComponent from './src/slide.vue'
import SwiperComponent from './src/swiper.vue'

const Swiper = window.Swiper || _Swiper
const swiper = SwiperComponent
const swiperSlide = SlideComponent
const install = function (Vue, globalOptions) {
	if (globalOptions) {
		SwiperComponent.props.globalOptions.default = () => globalOptions
	}
	Vue.component(SwiperComponent.name, SwiperComponent)
	Vue.component(SlideComponent.name, SlideComponent)
}
const VueAwesomeSwiper = { Swiper, swiper, swiperSlide, install }

export default VueAwesomeSwiper
export { Swiper, swiper, swiperSlide, install }

