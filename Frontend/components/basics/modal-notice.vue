<template>
	<view class="zaiui-modal-box" :class="show ? 'show' : ''">
		<view class="dialog">
			
			<form @submit="submit">			
				<!-- 条件 -->
				<view class="cu-form-group br-top">
					<view class="title">新的条件</view>
					<input type="text"  v-model="notice.conditions" name="conditions" placeholder="输入条件" />
				</view>
				<!-- 描述 -->
				<view class="cu-form-group">
					<textarea name="description" v-model="notice.description" maxlength="1000" 
						placeholder="新的通告描述" key="description"></textarea>
				</view>
				<!-- end -->
				
				<!-- 失效时间选择 -->
				<view class="cu-form-group br-bottom">
					<view class="title">新的失效日期</view>
					<input type="text" @focus="isShowPicker = true" v-model="notice.expiredTime" name="expiredTime" 
					placeholder="日期选择" />
				</view>
				<mx-date-picker
					style="z-index: 10;"
					:show="isShowPicker"
					type="datetime"
					:value="notice.expiredTime"
					:show-tips="true"
					:begin-text="'选择'"
					:show-seconds="false"
					@confirm="comfirmDatetime"
					@cancel="isShowPicker = false"
				/>
							
				<!-- 更新 -->
				<view class="flex flex-direction">
					<button class="cu-btn bg-orange margin-tb-sm lg" 
				form-type="submit">更新通告</button>
				</view>
			</form>
			<text class="cuIcon-roundclose close" @tap="$emit('closeModal')"></text>
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
			isShowPicker: false
		}
	},
	props: {
		notice:{},
		show: {
			type: Boolean,
			default: false
		}
	},
	methods: {
		comfirmDatetime(e) {
			this.isShowPicker = false
			if (e) this.notice.expiredTime = e.value.replace(/\//g,'-')
		},
		submit(e){
			uni.showLoading({
				title:'更新中'
			})
			let body = {
				description:this.notice.description,
				expiredTime:this.notice.expiredTime,
				conditions:this.notice.conditions
			}
			console.log('a',body);
			this.$api.updateNotice(this.notice.id, body)
				.then(({data})=>{
					console.log('更新回传值',data);
					uni.hideLoading()
					if(data.success){
						uni.showToast({
							title: '更新成功'
						});
						// update data binding
						this.$emit('updateNoticeA',body)
						this.$emit('closeModal')
					}else
						uni.showToast({
							title:'更新失败',
							icon:'none'
						})
				})
				.catch(()=>{
					uni.hideLoading()
					uni.showToast({
						title:'更新失败,网络异常',
						icon:'none'
					})
				})
			this.$emit('updateNotice',e.detail.value)
		}
	}
}
</script>

<style lang="scss" scoped>

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
</style>
