<template>
	<view class="zaiui-my-box" :class="show ? 'show' : ''">
		<view class="bg-gradual-red zaiui-head-box">
			<!--标题栏-->
			<!--小程序端不显示-->
			<!-- #ifndef MP -->
			<bar-title :isBack="false" :fixed="false">
				<block slot="right">
					<text class="cuIcon-camera" />
					<!-- <text class="cuIcon-settings" @tap="setupTap"/> -->
				</block>
			</bar-title>
			<!-- #endif -->

			<!--用户信息-->
			<view class="zaiui-user-info-box">
				<!--已登陆-->
				<view class="cu-list menu-avatar">
					<view class="cu-item">
						<view class="cu-avatar round lg" :style="{ backgroundImage: 'url(/static/images/avatar/1.jpg)' }" />
						<view class="content text-xl">
							<view class="text-white"><text class="margin-right">仔仔</text></view>
						</view>
					</view>
				</view>
			</view>

			<view class="cu-list menu sm-border margin-top">
				<view class="cu-item arrow">
					<view class="content">头像</view>
					<view class="action"><view class="cu-avatar round sm" :style="[{ backgroundImage: 'url(' + avatar_img + ')' }]" /></view>
				</view>
				<view class="cu-item arrow" @tap="editNameTap">
					<view class="content">昵称</view>
					<view class="action"><text class="text-gray">仔仔</text></view>
				</view>
				<view class="cu-item arrow">
					<view class="content">性别</view>
					<view class="action">
						<picker @change="sexPickerChange" :value="sexIndex" :range="sexPicker">
							<view class="picker text-gray">{{ sexIndex > -1 ? sexPicker[sexIndex] : '男' }}</view>
						</picker>
					</view>
				</view>
				<view class="cu-item arrow" @tap="synopsisTap">
					<view class="content">个人简介</view>
					<view class="action"><text class="text-gray">交个朋友</text></view>
				</view>
			</view>

			<view class="cu-list menu sm-border margin-top">
				<view class="cu-item arrow" @tap="regionTap">
					<view class="content">地区</view>
					<view class="action"><text class="text-gray">信息学部</text></view>
				</view>
				<view class="cu-item arrow" @tap="editContactCardsTap">
					<view class="content">微信号</view>
				</view>
			</view>
			
			<view class="zaiui-border-view"/>
			<view class="zaiui-recommend-list-box">
				<view class="text-sm">发布者的其他物品</view>
				<!--滑动列表-->
				<view class="recommend-scroll-box">
					<scroll-view class="recommend-scroll" scroll-x>
						<block v-for="(items,indexs) in otherComs" :key="indexs">
							<view :id="['scroll' + (indexs + 1 )]" class="recommend-scroll-item">
								<view class="cu-avatar xl radius" :style="[{backgroundImage:'url('+ items.img +')'}]"/>
								<view class="text-cut-2 text-sm text-black margin-tb-sm">{{items.title}}</view>
								<view class="text-red text-price margin-tb-sm text-lg">{{items.price}}</view>
							</view>
						</block>
					</scroll-view>
				</view>
			
			</view>
			
		</view>
	</view>
</template>

<script>
import {mapState} from 'vuex'
export default {
	data() {
		return {
			account:{}
		}
	},
	computed: {
		...mapState(['userId'])
	},
	onLoad(params) {
		this.getAccount(params.id)
	},
	methods: {
		getAccount(toAccId){
			this.$api.getOtherAccount(toAccId, this.userId)
				.then(({data})=>{
					console.log('其他账户的信息',data);
					this.account = data
				})
				.catch(()=>{
					console.log('获取其他账户的信息失败');
				})
		}
	}
}
</script>

<style></style>
