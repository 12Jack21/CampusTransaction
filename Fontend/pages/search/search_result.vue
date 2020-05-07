<template>
	<view>
		<view class="zaiui-bar-search-title-box">
			<view class="cu-bar search bg-white fixed no-shadow">
				<view class="action" @tap="BackPage">
					<text class="cuIcon-back"/>
				</view>
				<view class="search-form round">
					<text class="cuIcon-search"/>
					<input @input="searchInput" :value="searchKey" :adjust-position="false" type="text" placeholder="苹果7" confirm-type="search"/>
					<text class="cuIcon-close close-icon" v-if="search_close" @tap="closeInput"/>
				</view>
				<view class="action">
					<text class="text-red">搜索</text>
				</view>
			</view>
			<!--占位的-->
			<view class="zaiui-seat-height"/>
		</view>
		
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			};
		}
	}
</script>

<style lang="scss">

</style>
