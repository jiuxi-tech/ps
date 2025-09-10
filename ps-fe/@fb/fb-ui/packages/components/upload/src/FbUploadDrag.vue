<template>
	<fb-upload mode="drag">
		<slot></slot>
	</fb-upload>
</template>

<script>
	/**
	 * FbUploadDrag
	 * (c) 2020 lincong1987
	 */

	import { prefix } from '../../../../src/config'
	import FbUpload from './FbUpload'

	export default {

		name: 'FbUploadDrag',
		components: {FbUpload},
	}
</script>

<style scoped>

</style>
