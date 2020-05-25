<template>
	<view class="zaiui-my-box" :class="show ? 'show' : ''" >
		<view   v-show="scene=='my'">		
			<view class="bg-gradual-red zaiui-head-box">
				<!--标题栏-->
				<!--小程序端不显示-->
				<bar-title :isBack="false" :fixed="false">
					#<!-- #ifdef MP -->
					<block slot="left">
						<text style="font-size: 1.5em;" class="cuIcon-settings"/>
					</block>
					<!-- #endif -->
					#<!-- #ifndef MP -->
					<block slot="right">
						<text style="font-size: 1.5em;" class="cuIcon-settings"/>
					</block>
					<!-- #endif -->					
				</bar-title> 
 
				<!--用户信息-->
				<view class="zaiui-user-info-box">
					<view class="cu-list menu-avatar">
						<view class="flex" style="justify-content: flex-start;">
							<view class="cu-avatar round userAvatar" :style="{ backgroundImage:avatar}" />
							<view class=" text-xl text-white flex" style="align-items: center;">
								<text class="margin-right">{{account.username}}</text>
							</view>
						</view>
					</view>
				</view>
			</view>

			<view class="zaiui-view-content">
				<!--用户的交易数据-->
				<view class="padding-xs bg-white zaiui-user-info-order-box">
					<view class="text-black text-lg text-bold padding-sm">我的交易</view>
					<view class="cu-list grid col-3 no-border">
						<view class="cu-item" @tap="order_list_tap">
							<view class="text-xxl text-black">0</view>
							<text class="text-sm">我发布的</text>
						</view>
						<view class="cu-item">
							<view class="text-xxl text-black">1</view>
							<text class="text-sm">我卖出的</text>
						</view>
						<view class="cu-item">
							<view class="text-xxl text-black">2</view>
							<text class="text-sm">我买到的</text>
						</view>
					</view>
				</view>

				<!--设置列表-->
				<view class="cu-list menu margin-top">
					<view class="cu-item arrow">
						<view class="content">头像</view>
						<view class="action" @tap="updateInfo('avatar')">
							<view class="cu-avatar round sm" :style="{ backgroundImage:account.avatar.length===0? 'url(/static/images/avatar/default.png)' : account.avatar }" />
						</view>
					</view>
					<view class="cu-item arrow" @tap="updateInfo('username')">
						<view class="content">昵称</view>
						<view class="action"><text class="text-gray">{{account.username}}</text></view>
					</view>
					<view class="cu-item arrow">
						<view class="content">性别</view>
						<view class="action">
							<picker @change="genderPickerChange" :range="['女','男']" :value="account.gender">
								<view class="picker text-gray">{{ account.gender===0 ? '女' : '男' }}</view>
							</picker>
						</view>
					</view>
					<view class="cu-item arrow" @tap="updateInfo('address')">
						<view class="content">地址</view>
						<view class="action">
							<picker @change="addrPickerChange" :range="addrs" :value="addrs.indexOf(account.address)">
								<view class="picker text-gray">{{ account.address}}</view>
							</picker>
						</view>
					</view>
				</view>

				<view class="cu-list menu margin-top">			
					<view class="cu-item arrow" @tap="scene='intro'">
						<view class="content">个人简介</view>
						<view class="action"><text class="text-gray">{{account.introduction}}</text></view>
					</view>
					<view class="cu-item arrow" @tap="scene='contact'">
						<view class="content">联系卡</view>
						<view class="action"></view>
					</view>
				</view>

				<view class="cu-list menu margin-top">
					<view class="cu-item arrow logOutItem" @tap="logOut">
						<view class="content text-red">注销账户</view>
					</view>
				</view>
			</view>

		
			<!--占位底部距离-->
			<view class="cu-tabbar-height"></view>
			
			<!--弹出框-->
			<view class="cu-modal bottom-modal zaiui-bottom-modal-box" :class="modal.show ? 'show' : ''">
				<view class="cu-dialog bg-white updateDialog">
					<view class="text-black text-center margin-tb text-lg title-bar">
						<text>{{ modal.title }}</text>
						<text class="cuIcon-close close-icon" @tap="modal.show=false"></text>		
					</view>
			
					<!-- 模态框内容区域 -->
					<view class="zaiui-modal-content">
						<!-- 修改头像 -->
						<view class="zaiui-view-box select" v-if="modal.type == 'avatar'">
							<!-- 上传 -->
							<view class="avatar">
								<avatar selWidth="200upx" selHeight="200upx" @upload="upload" :avatarSrc="url"
								        avatarStyle="width: 200upx; height: 200upx; border-radius: 100%;">
								    </avatar>
								<loading v-if="uploading">上传中</loading>
								<text class="text-gray" v-else>点击头像选择</text>
							</view>
							<!-- end -->
						</view>
			
						<!-- 修改昵称 -->
						<view class="zaiui-view-box select" v-if="modal.type == 'username'">										
							<!-- 新昵称 -->
							<view class="usernameItem">
								<view class="flex new">
									<view class="text-black text-lg text-bold">新昵称:</view>
									<input type="text" v-model="username" placeholder="输入新昵称"  maxlength="12"/>
								</view>
								<view class="flex old text-lg">
									<view class="text-gray ">原昵称:</view>
									<text class="text-gray ">{{account.username}}</text>
								</view>
							</view>
							<!-- end -->
						</view>						
						<!-- 保存更新 -->
						<view class="zaiui-footer-fixed" style="z-index: 10;">
							<view class="flex flex-direction">
								<button class="cu-btn bg-red" style="font-size: 1.4em;padding: 10rpx 0;" @tap="confirm" >{{modal.type=='username'? '保存':'上传'}}</button>
							</view> 
						</view>
						
					</view>
				</view>
			</view>
			
		</view>
		<contact-cards :contact="contact" v-if="scene=='contact'" @close="close" @updateContact="updateContact"></contact-cards>
		<edit-synopsis v-if="scene=='intro'" @close="close" @updateIntro="updateIntro"></edit-synopsis>
		<!-- TODO: 修改密码 -->
		
		
		<mpopup ref="mpopup" :isdistance="true"></mpopup>
	</view>
</template>

<script>
import barTitle from '@/components/basics/bar-title'
import avatar from "../../components/yq-avatar/yq-avatar.vue";
import contactCards from '@/components/my/contact-cards.vue'
import editSynopsis from '@/components/my/edit-synopsis.vue'

import _my_data from '@/static/zaiui/data/my.js' //虚拟数据
import _tool from '@/static/zaiui/util/tools.js' //工具函数
import {mapState} from 'vuex'

const types = ['success', 'err', 'warn', 'info', 'loading'] // tip message types
const defaultAvatar = 'url(/static/images/avatar/default.png)' 
export default {
	name: 'my',
	components: {
		barTitle,
		contactCards,
		editSynopsis,
		avatar
	},
	data() {
		return {
			uploading: false,
			scene:'my',
			addrs:['信息学部', '文理学部', '工学部', '医学部'],
			modal:{
				title:'修改头像',
				type:'avatar',
				show: false
			},
			store:{}, // 备份的 account
			account:{
				avatar:'',
				username:'FAIR',
				password:'',
				gender: 1,
				address:'文理学部',
				email:'221qqw121@qq.com',
				wechat:'wechatID_12121223',
				qq:'99881231',
				introduction:"我就是我，是不一样的烟火"
			},
			username:'', // 新昵称
			url:''
		}
	},
	computed:{
		...mapState(['userId']),
		contact(){
			return {
				wechat:this.account.wechat,
				qq:this.account.qq,
				email:this.account.email
			}
		},
		avatar(){
			if(this.account.avatar.length ===0) return defaultAvatar;
			return this.account.avatar
		}
	},
	props: {
		show: {
			type: Boolean,
			default: true
		}
	},
	created() {	
		this.getMyAccount()
	},
	mounted() {
		_tool.setBarColor(false)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
		this.url = this.avatar.startsWith('url(') ? this.avatar.slice(4,-1):this.avatar
	},
	methods: {
		updateAccRequest(close){
			this.$api.updateAccount(this.userId, this.account)
				.then(({data})=>{
					if(data.success){
						this.tip(1,'更新成功')
						this.store = {}
					}
					else{
						this.tip(0,'更新失败')
						this.account = this.store
					}
					if(close) this.scene = 'my'
				})
				.catch(()=>{
					this.err()
					this.account = this.store
					if(close) this.scene = 'my'
				})
		},
		upload(rsp){ //切换新头像
			this.url = rsp.path
		},
		confirm(){
			if(this.modal.type == 'username'){
				if(this.username == this.account.username) return
				this.store = Object.assign({},this.account)
				this.account.username = this.username
				this.updateAccRequest()
				this.modal.show = false
			}else if(this.modal.type == 'avatar'){
				// 上传头像
				if(this.url == this.account.avatar) return
				this.uploading = true
				this.$api.uploadAvatar(this.account.id, this.url)
					.then(({data})=>{
						if(data.success){
							this.tip(1,'上传头像成功')
							this.account.avatar = data.avartar
						}else{
							this.tip(0,'更新头像失败')
						}
						this.modal.show = false
						this.uploading = false
					})
					.catch(()=>{
						this.modal.show = false
						this.uploading = false
						this.err()
					})
			}
		},
		addrPickerChange(e){
			let a = e.detail.value
			if(this.addrs[a] == this.account.address) return
			this.store = Object.assign({},this.account) // set store to backup
			this.account.address = this.addrs[a]
			this.updateAccRequest()
		},
		genderPickerChange(e){
			let g = e.detail.value
			if(g === this.account.gender) return
			this.store = Object.assign({},this.account)
			this.account.gender = g
			this.updateAccRequest()
		},
		updateContact({wechat,qq,email}){
			if(this.account.wechat==wechat &&this.account.qq==qq&&this.account.email==email) return
			this.store = {...this.account}
			this.account = Object.assign(this.account,{wechat,qq,email})
			this.updateAccRequest(true)
		},
		updateIntro({introduction}){
			if(this.account.introduction==introduction) return
			this.store = {...this.account}
			this.account = Object.assign(this.account,{introduction})
			this.updateAccRequest(true)
		},
		updateInfo(info){
			switch(info){
				case 'avatar':
					this.modal.type='avatar'
					this.modal.title='修改头像'
					this.modal.show = true
					break
				case 'username':
					this.modal.type='username'
					this.modal.title='修改昵称'
					this.modal.show = true
					break
				default:
					console.log('switch case default !!!');
			}
		},
		getMyAccount(){
			this.$api.getMyAccount(this.userId)
				.then(({data})=>{
					console.log('我的账户',data);
					this.account = data
				})
				.catch(()=>{
					console.log('获取我的账户信息失败');
					uni.showToast({
						title: '网络异常',
						icon: 'none'
					});
				})
		},
		logOut(){
			// 清空 vuex中数据
			this.$store.dispatch('logout')
			uni.reLaunch({
				url:'../../pages/login/login?type=logout'
			})
		},
		//我买到的
		order_list_tap() {
			uni.navigateTo({
				url: '/pages/reservations/reservations'
			})
		},
		gridTap(item) {
			if (item.name == '设置') {
				this.setupTap()
			}
		},
		err() {
			this.tip(1, '网络异常')
		},
		tip(index, content) {
			this.$refs.mpopup.open({
				type: types[index],
				content,
				timeout:2500,
				isClick:false
			})
		},
		close(){
			this.scene = 'my'
		}
	}
}
</script>

<style lang="scss" scoped>
$img_size:150rpx;
$bottom_height: 60rpx;
$space: 20rpx;

.status-bar-height{
	height: var(--status-bar-height);
}
.avatar{
	margin-bottom: $bottom_height * 2;
	display: flex;
	flex-direction: column;
	> text{
		text-align: center;
		margin-top: $bottom_height;
	}
}
.usernameItem{
	padding: 20rpx 0 60rpx 0;
	margin-bottom: $bottom_height;
	display: flex;
	flex-direction: column;
	// justify-content: center;
	align-items: center;
	.new{
		align-items: center;
		justify-content: center;
	}
	input{
		border: #FAFAFA 1px solid;
		box-shadow: #e6e6e6 2px 1px 1px;
		border-radius: 10rpx;
		padding: 10rpx;
		margin-left: $space;
		height: 2em;
	}
	.old{
		margin-top: $space;
		justify-self: flex-start;
		align-items: flex-end;
		text{
			margin-left: $space;
		}
	}
}
.updateDialog{
	border-radius: 40rpx 40rpx 0 0 !important;
	.title-bar{
		position: relative;
		.cuIcon-close{
			position: absolute;
			right: 20rpx;
		}
	}
	
}
.userAvatar{
	width: $img_size;
	height: $img_size;
	margin: 0 30rpx 10rpx 30rpx;
}
.logOutItem:before{
	color: red !important;
}
.zaiui-my-box {
	width: 100%;
	display: none;
	.zaiui-head-box {
		padding-top: 0;
		padding-bottom: 72.72rpx;
		.zaiui-user-info-box {
			// #ifdef MP */
			// padding-top: calc(var(--status-bar-height) + 50rpx);
			// /* #endif
			.login-user-view {
				position: relative;
				text-align: center;
				.login-user-avatar-view {
					position: relative;
					margin-bottom: 18.18rpx;
				}
			}
			.cu-list.menu-avatar > .cu-item {
				background-color: inherit;
				.content {
					width: calc(100% - 94.54rpx - 59.99rpx - 20rpx);
					.text-white-bg {
						color: #e8e8e8;
						.text-border-x {
							margin-right: 25.45rpx;
							position: relative;
							&:after {
								position: absolute;
								background: #dddddd;
								top: 5.45rpx;
								width: 1.81rpx;
								right: -12.72rpx;
								height: 16.36rpx;
								content: ' ';
							}
						}
					}
				}
				&:after {
					width: 0;
					height: 0;
					border-bottom: 0;
				}
			}
			.cu-list.menu-avatar > .cu-item .content > view:first-child {
				font-size: 34.54rpx;
			}
		}
		.zaiui-user-info-num-box {
			.cu-list.grid.no-border {
				padding: 0;
			}
			.cu-list.grid.no-border > .cu-item {
				padding-top: 27.27rpx;
				padding-bottom: 9.09rpx;
			}
			.cu-list.grid {
				background-color: inherit;
			}
			.cu-list.grid > .cu-item text {
				color: #e8e8e8;
				font-size: 20rpx;
				line-height: 27.27rpx;
			}
		}
		.zaiui-user-info-tip-box {
			position: relative;
			margin: 18.18rpx 27.27rpx;
			border-radius: 9.09rpx;
			padding: 18.18rpx 27.27rpx;
			background: #ea8d8d;
			background-image: linear-gradient(45deg, #f7615f, #f553b3);
			.text-cut {
				padding-right: 45.45rpx;
			}
			.icon {
				position: absolute;
				right: 27.27rpx;
				top: 23.63rpx;
			}
		}
	}
	
	.zaiui-view-content {
		padding: 0 27.27rpx 54.54rpx;
		margin-top: -63.63rpx;
		.zaiui-user-info-order-box {
			border-radius: 18.18rpx;
			.cu-list.grid.no-border {
				padding: 0;
			}
			.cu-list.grid.no-border > .cu-item {
				padding-bottom: 9.09rpx;
			}
		}
		.cu-list.grid > .cu-item text {
			color: inherit;
		}
		.cu-list{
			border-radius: 9rpx;
		}
		.zaiui-user-info-money-box {
			border-radius: 18.18rpx;
			.money-col {
				padding: 0 9.09rpx 9.09rpx;
				.money-item {
					position: relative;
					padding: 9.09rpx;
					.money-item-view {
						border: 1.81rpx solid #f3f2f3;
						border-radius: 18.18rpx;
						position: relative;
						padding: 9.09rpx;
						.cu-avatar {
							position: absolute;
							left: 9.09rpx;
						}
						.money-content {
							position: relative;
							margin-left: 109.09rpx;
							margin-bottom: 27.27rpx;
							top: 12.72rpx;
						}
					}
				}
			}
		}
		.zaiui-user-info-tools-box {
			border-radius: 18.18rpx;
			.tools-view {
				position: relative;
				.tools-title {
					padding-right: 81.81rpx;
				}
				.tools-right {
					position: absolute;
					right: 9.09rpx;
					bottom: 23.63rpx;
				}
			}
		}
	}
}
.zaiui-my-box.show {
	display: block;
}
</style>
