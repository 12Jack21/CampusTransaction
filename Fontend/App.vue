<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
			// TODO: Handle token in storage
			// get token and update value in server, store that in vuex state, mount on request header 
			
			// this.$api.testAPI().catch(err=>console.log('test err',err))
		},
		onShow: function() {
			// console.log('App Show')
		},
		onHide: function() {
			// console.log('App Hide')
		}
	}
</script>

<style lang="scss">
	// H5端引用。玄学问题，我这边在这里引入的css，在APP上无效。前面加/也一样。原因未知...
	// 可自行测试在APP上是否有效，如果有效，可在vue里删除css引入的代码。
	/* #ifndef APP-PLUS */

	/* #endif */
	@import "static/colorui/main.css";
	@import "static/colorui/icon.css";
	@import "static/zaiui/style/app.scss";
</style>
