<template>
	<view
		class="cu-item goods"
		:class="modalName == 'move-box-' + 1 ? 'move-cur' : ''"
		@touchstart="ListTouchStart"
		@touchmove="ListTouchMove"
		@touchend="ListTouchEnd"
		:data-target="'move-box-' + 1"
		@tap="tapNews(1)"
	>
		<view class="cu-avatar round" style="background-image:url(/static/images/avatar/1.jpg);"><view class="cu-tag badge"></view></view>
		<view class="content">
			<view class="text-black">
				<text class="margin-right-xs">仔仔</text>
				<text class="cu-tag bg-pink sm radius"><text class="cuIcon-female"></text></text>
			</view>
			<view class="text-gray text-sm text-cut">
				预约了你的物品:
				<text style="text-align: right;width: 100%;color: red;">华为手机</text>
			</view>
			<view class="text-gray text-sm">4小时前</view>
		</view>
		<view class="action"><view class="cu-avatar radius" style="background-image:url(/static/images/home/goods/11.png);" /></view>
		<view class="move"><view class="bg-red">已读</view></view>
	</view>

	<view
		class="cu-item goods"
		:class="modalName == 'move-box-' + 1 ? 'move-cur' : ''"
		@touchstart="ListTouchStart"
		@touchmove="ListTouchMove"
		@touchend="ListTouchEnd"
		:data-target="'move-box-' + 1"
		@tap="tapNews(1)"
	>
		<view class="cu-avatar round" style="background-image:url(/static/images/avatar/1.jpg);"><view class="cu-tag badge"></view></view>
		<view class="content">
			<view class="text-black">
				<text class="margin-right-xs">仔仔</text>
				<text class="cu-tag bg-pink sm radius"><text class="cuIcon-male"></text></text>
			</view>
			<view class="text-gray text-sm text-cut">
				评论了你的物品:
				<text style="text-align: right;width: 100%;color: red;">一加手机</text>
			</view>
			<view class="text-gray text-sm">4小时前</view>
		</view>
		<view class="action"><view class="cu-avatar radius" style="background-image:url(/static/images/home/goods/11.png);" /></view>
		<view class="move"><view class="bg-red">已读</view></view>
	</view>

	<view
		class="cu-item goods"
		:class="modalName == 'move-box-' + 1 ? 'move-cur' : ''"
		@touchstart="ListTouchStart"
		@touchmove="ListTouchMove"
		@touchend="ListTouchEnd"
		:data-target="'move-box-' + 1"
		@tap="tapNews(1)"
	>
		<view class="cu-avatar round" style="background-image:url(/static/images/avatar/1.jpg);"><view class="cu-tag badge"></view></view>
		<view class="content">
			<view class="text-black">
				<text class="margin-right-xs">仔仔</text>
				<text class="cu-tag bg-pink sm radius"><text class="cuIcon-female"></text></text>
			</view>
			<view class="text-gray text-sm text-cut">
				在
				<text style="text-align: right;width: 100%;color: red;">华为手机</text>
				里回复了你的评论
			</view>
			<view class="text-gray text-sm">4小时前</view>
		</view>
		<view class="action"><text style="vertical-align: middle;">去看看 ></text></view>
		<view class="move"><view class="bg-red">已读</view></view>
	</view>

	<view
		class="cu-item goods"
		:class="modalName == 'move-box-' + 2 ? 'move-cur' : ''"
		@touchstart="ListTouchStart"
		@touchmove="ListTouchMove"
		@touchend="ListTouchEnd"
		:data-target="'move-box-' + 2"
		@tap="tapNews(2)"
	>
		<view class="cu-avatar round" style="background-image:url(/static/images/avatar/1.jpg);"><view class="cu-tag badge"></view></view>
		<view class="content">
			<view class="text-black">
				<text class="margin-right-xs">仔仔</text>
				<text class="cu-tag bg-pink sm radius"><text class="cuIcon-female"></text></text>
			</view>
			<view class="text-gray text-sm text-cut">确认了你的预约:</view>
			<view class="text-gray text-sm">4小时前</view>
		</view>
		<view class="action"><text style="vertical-align: middle;">去看看 ></text></view>
		<view class="move"><view class="bg-red">已读</view></view>
	</view>

	<view
		class="cu-item goods"
		:class="modalName == 'move-box-' + 3 ? 'move-cur' : ''"
		@touchstart="ListTouchStart"
		@touchmove="ListTouchMove"
		@touchend="ListTouchEnd"
		:data-target="'move-box-' + 3"
		@tap="tapNews(2)"
	>
		<view class="cu-avatar round" style="background-image:url(../../static/images/news/az3.png);"><view class="cu-tag badge"></view></view>
		<view class="content">
			<view class="text-orange"><text class="margin-right-xs">失效通知</text></view>
			<view class="text-gray text-sm text-cut">
				您预约的物品
				<text class="text-red">小米手机</text>
				已失效
			</view>
			<view class="text-gray text-sm">4小时前</view>
		</view>
		<view class="action">
			<text style="vertical-align: middle;">
				去看看
				<text class="cuIcon-right"></text>
			</text>
		</view>
		<view class="move"><view class="bg-red">已读</view></view>
	</view>

	<!-- 通告 已失效 -->
	<view
		class="cu-item goods"
		:class="modalName == 'move-box-' + 3 ? 'move-cur' : ''"
		@touchstart="ListTouchStart"
		@touchmove="ListTouchMove"
		@touchend="ListTouchEnd"
		:data-target="'move-box-' + 3"
		@tap="tapNews(2)"
	>
		<view class="cu-avatar round" style="background-image:url(../../static/images/news/az3.png);"><view class="cu-tag badge"></view></view>
		<view class="content">
			<view class="text-orange"><text class="margin-right-xs">失效通知</text></view>
			<view class="text-gray text-sm text-cut">您的通告已失效</view>
			<view class="text-gray text-sm">4小时前</view>
		</view>
		<view class="action">
			<text style="vertical-align: middle;">
				去看看
				<text class="cuIcon-right"></text>
			</text>
		</view>
		<view class="move"><view class="bg-red">已读</view></view>
	</view>

	<!-- 交易已完成/失败 -->
	<view
		class="cu-item goods"
		:class="modalName == 'move-box-' + 3 ? 'move-cur' : ''"
		@touchstart="ListTouchStart"
		@touchmove="ListTouchMove"
		@touchend="ListTouchEnd"
		:data-target="'move-box-' + 3"
		@tap="tapNews(2)">
		<view class="cu-avatar round" style="background-image:url(../../static/images/news/azg.png);"><view class="cu-tag badge"></view></view>
		<view class="content">
			<view class="text-orange"><text class="margin-right-xs">交易通知</text></view>
			<view class="text-gray text-sm text-cut">您的交易已成功完成/失败</view>
			<view class="text-gray text-sm">4小时前</view>
		</view>
		<view class="action">
			<text style="vertical-align: middle;">
				去看看
				<text class="cuIcon-right"></text>
			</text>
		</view>
		<view class="move"><view class="bg-red">已读</view></view>
	</view>
	
</template>

<script></script>

<style></style>
