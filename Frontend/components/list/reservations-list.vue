<template>
	<view>
		<block v-for="(item) in list_data" :key="item.id">

			<view class="zaiui-goods-list-box" @tap="listTap(item.id)">
				
				<view class="cu-avatar radius" :style="[{backgroundImage:'url('+ item.img +')'}]"/>
				<view class="goods-info-view">
					<view class="text-cut-2 text-black">{{item.title}}</view>
					<view class="goods-info-tools">
						<text class="text-price text-red text-lg">{{item.price}}</text>
					</view>
				</view>
			</view>
		</block>
	</view>

	
</template>

<script>
	import barTitle from '@/components/basics/bar-title';
	import _tool from '@/static/zaiui/util/tools.js';	//工具函数
	
	export default {
		name: 'reservationList',
		
		props: {
			list_data: {
				type: Array,
				default: () => {
					return []
				}
			},
		},
		
		methods: {
			listTap(id) {
				this.$emit('listTap',id)
			},
		}

	}
</script>



<style lang="scss">
	/* #ifdef APP-PLUS */
		@import "../../static/colorui/main.css";
		@import "../../static/colorui/icon.css";
		@import "../../static/zaiui/style/app.scss";
	/* #endif */
	@import "../../static/zaiui/style/footmark.scss";
</style>

