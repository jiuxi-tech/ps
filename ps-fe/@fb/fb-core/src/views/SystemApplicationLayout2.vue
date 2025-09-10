<template>
	<div class="fb-admin-layout">

		<div class="fb-admin-header">

			<div class="fb-admin-header__logo">
				SystemApplicationLayout2
			</div>

			<nav class="fb-admin-header__nav fb-admin-header__nav">


			</nav>

			<div class="fb-admin-header__actions">

				<div>
					<router-link tag="a" to="/demo/user/list">
						/demo/user/list
					</router-link>
				</div>

				<div>
					$datax->{{$datax.name}}
				</div>

			</div>
		</div>


		<aside class="fb-admin-menu">asd</aside>

		<div class="fb-admin-tabbar">

			<div>
				<a href="">欢迎</a>
			</div>

		</div>

		<div class="fb-admin-main">
			<keep-alive>
				<router-view/>
			</keep-alive>
		</div>


	</div>
</template>

<script>
  /**
   * DefaultApplicationLayout
   * (c) 2020 lincong1987
   */

  export default {

    data () {

      return {}

    },

    computed: {},

    mounted () {
      this.$datax.set('asd', 'asd')

    },
  }
</script>

<style lang="less" scoped>


	.fb-admin-layout {
		position:         absolute;
		left:             0;
		right:            0;
		top:              0;
		bottom:           0;
		background-color: #fff;

		.fb-admin-header {
			height:     64px;
			background: #0284FE;

			&__nav {
				height:     40px;
				background: #FFFFFF;
				box-shadow: 0px 1px 4px 0px rgba(0, 0, 0, 0.15);
			}
		}


		.fb-admin-menu {
			width:      200px;
			height:     704px;
			background: #0284FE;
		}

	}

</style>