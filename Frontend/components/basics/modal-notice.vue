<template>
	<view class="zaiui-modal-box" :class="show ? 'show' : ''">
		<view class="dialog">
			<form @submit="submit">
				<!-- 描述 -->
				<view class="cu-form-group margin-top"><textarea name="description" maxlength="1000" 
				placeholder="新的通告描述" key="description"></textarea></view>
				<!-- end -->
				
				<!-- 失效时间选择 -->
				<view class="cu-form-group">
					<view class="title">新失效日期</view>
					<input type="text" @focus="isShowPicker = true" :value="outdatedTime" name="outdatedTime" 
					placeholder="日期选择" />
				</view>
				<mx-date-picker
					style="z-index: 10;"
					:show="isShowPicker"
					type="datetime"
					:value="outdatedTime"
					:show-tips="true"
					:begin-text="'选择'"
					:show-seconds="false"
					@confirm="comfirmDatetime"
					@cancel="isShowPicker = false"
				/>
				
				<!-- 物品列表 -->
<!-- 				<view class="comList" >
					<view class="commodity" v-for="(com, index) in comList" :key="index">
						<text style="margin-right: 20rpx;">{{ com.name }}</text>
					</view>
				</view>
				<view >
					<view class="add-com-btn" @tap="addComTap">
						<text>添加新物品</text>
						<text class="cuIcon-add"></text>
					</view>
				</view> -->
				
				<!-- 更新 -->
				<view class="flex flex-direction"><button class="cu-btn bg-orange margin-tb-sm lg" 
				form-type="submit">更新通告</button></view>
			</form>
			<text class="cuIcon-roundclose close" @tap="closeEvent"></text>
		</view>
	</view>
</template>

<script>
import MxDatePicker from '@/components/mx-datepicker/mx-datepicker.vue'
export default {
	name: 'modal-notice',
	components:{
		MxDatePicker
	},
	data(){
		return {
			isShowPicker: false,
			comList:[{name:'a'},{name:'kk'}]
		}
	},
	props: {
		description: '',
		outdatedTime: '',
		id:{ // notice id
			type:Number,
			default:-1
		},
		src: {
			type: String,
			default: ''
		},
		show: {
			type: Boolean,
			default: false
		}
	},
	methods: {
		comfirmDatetime(e) {
			this.isShowPicker = false
			if (e) this.outdatedTime = e.value
		},
		closeEvent() {
			this.$emit('closeModal')
		},
		submit(e){
			uni.showLoading({
				title:'更新中'
			})
			
			this.$api.updateNotice(this.id,e.detail.value)
				.then(({data})=>{
					uni.hideLoading()
					if(data.success){
						uni.showToast({
							title: '更新成功'
						});
						// TODO: update data binding
						
					}else
						uni.showToast({
							title:'更新失败',
							icon:'none'
						})
				})
				.catch(()=>{
					uni.hideLoading()
					uni.showToast({
						title:'更新失败',
						icon:'none'
					})
				})
			this.$emit('updateNotice',e.detail.value)
		}
	}
}
</script>

<style lang="scss" scoped>
$item_lh: 66rpx;
$border_color: #e54d42;
.zaiui-modal-box {
	position: fixed;
	opacity: 0;
	top: inherit;
	left: inherit;
	right: inherit;
	bottom: inherit;
	z-index: 9;
	text-align: center;
	background: rgba(0, 0, 0, 0.6);
	transition: all 0.3s;
	pointer-events: none;
	&::before {
		content: '\200B';
		display: inline-block;
		height: 100%;
		vertical-align: middle;
	}
	.dialog {
		position: relative;
		display: inline-block;
		vertical-align: middle;
		width: 618.18rpx;
		.close {
			color: #dadada;
			top: 18.18rpx;
			position: relative;
			font-size: 54.54rpx;
		}
	}
}
.zaiui-modal-box.show {
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	opacity: 1;
	pointer-events: auto;
}
.comList {
	padding: 10rpx 10rpx;
	border-top: 1px solid #eee;
	background-color: white;
	display: flex;
	flex-wrap: wrap;
	align-items: center;
}
.commodity {
	line-height: $item_lh;
	border: 1rpx solid $border_color;
	border-radius: 30rpx;
	padding: 0 20rpx;
	margin: 2rpx;
}
.add-com-btn {
	text-align: center;
	margin: 0.3em 30rpx;
	border: 3rpx solid $border_color;
	border-radius: 20rpx;
	line-height: $item_lh;
}
</style>
