<script>
	export default {
		methods:{
			async reLogin(){
				console.log('App Launch, relogin to server...')
				uni.showLoading({
					title:'登录中',
					mask: true
				})
				
				// get token and update value in server, store that in vuex state, mount on request header 
				await this.$api.accountReLogin()
					.then(({data}) => {
						if(data.success){
							console.log('重新登录成功')
							// 存入vuex
							this.$store.commit('relogin',{
								userId: data.userId,
								userAddress: data.userAddress
							})
							uni.hideLoading()
						}else{
							uni.hideLoading()
							uni.navigateTo({
								url:'/pages/login/login?type=1',
								animationType:'fade-in'
							})
						}
					})
					.catch(err=>{
						uni.hideLoading()
						uni.navigateTo({
							url:'/pages/login/login?type=2',
							animationType:'fade-in'
						})
					})		
				console.log('处理 APP 启动的重新登录完成')			
				// this.$api.testAPI().catch(err=>console.log('test err',err))
			}
		},
		onLaunch: async function() {
			// await this.reLogin()
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
