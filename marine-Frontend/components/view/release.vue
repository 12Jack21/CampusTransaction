<template>
	<view class="release_box" :class="show ? 'show' : ''">
		<view class="zaiui-release-box" v-if="scene == -1">
			<view class="zaiui-bar-view-box">
				<!--小程序端的标题-->
				<!-- #ifdef MP -->
				<view class="text-center text-black zaiui-small-routine-title">卖二手</view>
				<!-- #endif -->

				<!--标题栏-->
				<view class="text-gray zaiui-bar-box">
					<text class="text-red text-large">发布通告</text>
					<text class="margin-left-sm">及时发布 及时交易</text>
					<text class="cuIcon-close text-right close" @tap="closeTap"></text>
				</view>
			</view>

			<!--占位的-->
			<view class="zaiui-seat-height"></view>

			<!--中间内容区域-->
			<view class="zaiui-view-content"><type-list :list_data="noticeTypeListData" 
			@listTap="typeListTap"></type-list></view>
		</view>

		<view class="margin-status" v-else>
			<view v-show="focus == 0">
				<!--标题栏-->
				<view class="text-gray detail-topbox">
					<view class="cuIcon-back text-back" @tap="backTap">返回</view>
					<view class="text-black text-large">通告详细信息</view>
				</view>
				<form @submit="noticeSubmit">
					<!-- 标题 -->
					<view class="cu-form-group margin-top" >
						<view class="title">标题</view>
						<input name="title" :placeholder="noticeTitle" key="title" />
					</view>
					<!-- end -->

					<!-- 描述 -->
					<view class="cu-form-group margin-top"><textarea name="description" maxlength="1000" 
					:placeholder="noticeDesc" key="description"></textarea></view>
					<!-- end -->

					<!-- 条件 -->
					<view class="cu-form-group"><textarea name="condition" maxlength="300" style="height: 3em;" placeholder="限定条件说明"></textarea></view>
					<!-- end -->
					
					<!-- 任务的出价钱 -->
					<view class="cu-form-group" v-if="scene == 2">
						<view class="title">出价:</view>
						<input type="digit" key="demandPrice" placeholder="请输入出价金额" maxlength="7" 
						name="demandPrice" />
					</view>
					<!-- end -->
					<!-- 地址选择 -->
					<view class="cu-form-group margin-top">
						<view class="title">地址</view>
						<input type="text" name="address" :value="userAddress"/>
					</view>
					<!-- end -->

					<!-- 失效时间选择 -->
					<view class="cu-form-group">
						<view class="title">失效日期</view>
						<input type="text" @focus="showPicker" :value="outdatedTime" name="outdatedTime" placeholder="日期选择" />
					</view>
					<mx-date-picker
						:show="isShowPicker"
						type="datetime"
						:value="outdatedTime"
						:show-tips="true"
						:begin-text="'选择'"
						:show-seconds="false"
						@confirm="comfirmDatetime"
						@cancel="isShowPicker = false"
					/>
					<!-- end -->

					<!-- 物品列表 -->
					<view class="comList" v-show="scene !== 2">
						<view class="commodity" v-for="(com, index) in comList" :key="index">
							<text style="margin-right: 20rpx;">{{ com.name }}</text>
							<text class="cuIcon-close" @tap="delCom(index)"></text>
						</view>
					</view>
					<view v-if="scene !== 2">
						<view class="add-com-btn" @tap="addComTap">
							<text>添加新物品</text>
							<text class="cuIcon-add"></text>
						</view>
					</view>

					<!-- 确定发布 -->
					<view class="padding flex flex-direction">
						<button class="cu-btn lg release_btn" form-type="submit">
							确定发布
						</button>
						<!-- <button class="cu-load bg-red loading">确定发布</button> -->
					</view>
					<!-- end -->
				</form>
			</view>

			<view v-if="focus != 0">
				<!--标题栏-->
				<view class="text-gray detail-topbox">
					<view class="text-black text-large">物品详细信息</view>
					<view class="cuIcon-back text-back" @tap="backTap">返回</view>
				</view>
				<form @submit="comFormSubmit" @reset="comFormReset">
					<!-- 物品名称 -->
					<view class="cu-form-group margin-top">
						<view class="title">名称</view>
						<input name="comName" placeholder="输入物品的名称" key="com_name"  />
					</view>
					<!-- 描述 -->
					<view class="cu-form-group margin-top">
						<textarea name="comDescription" maxlength="1000" :placeholder="descPlaceholder" key="comDescription"></textarea>
					</view>
					<!-- end -->

					<!-- 价钱 -->
					<view class="cu-form-group">
						<view class="title">出售价:</view>
						<input type="digit" key="newPrice" placeholder="请输入价钱" maxlength="7" name="newPrice" />

						<view class="title">原价:</view>
						<input type="digit" key="oriPrice" :value="oriPrice" placeholder="请输入原价" maxlength="7" name="oriPrice" />
					</view>
					<!-- end -->

					<!-- 物品数量 -->
					<view class="cu-form-group">
						<view class="title">数量</view>
						<input type="number" name="count" key="count"/>
					</view>
					<!-- 选择分类  -->
					<view class="cu-form-group">
						<view class="title">分类:</view>
						<input disabled="true" name="comType" :value="comType" />
						<button class="cu-btn" plain type="warn" role="button" aria-disabled="false" @tap="showModal" data-target="DrawerModal">选择</button>
					</view>
					<!-- end -->

					<!-- 新旧 -->
					<view class="cu-form-group" v-if="scene == 0">
						<view class="title">新旧:</view>
						<input disabled="true" name="newness" :value="newnessList[newnessListIndex]" />
						<button class="cu-btn" plain type="warn" role="button" aria-disabled="false" @tap="newnessState">选择</button>
					</view>
					<!-- end -->
					<!-- 图片 -->
					<view class="cu-bar bg-white margin-top">
						<view class="action">图片上传
							<text class="text-grey margin-left-xs" v-if="scene == 0">(物品图片)</text>
							<text class="text-grey margin-left-xs" v-else-if="scene == 1">(示例的类似物品图片)</text>
						</view>
						<view class="action">{{ imgList.length }}/5</view>
					</view>
					<view class="cu-form-group">
						<view class="grid col-4 grid-square flex-sub">
							<view class="bg-img" v-for="(item, index) in imgList" :key="index" @tap="ViewImage" :data-url="imgList[index]">
								<image :src="imgList[index]" mode="aspectFill"></image>
								<view class="cu-tag bg-red" @tap.stop="DelImg" :data-index="index"><text class="cuIcon-close"></text></view>
							</view>
							<view class="solids" @tap="ChooseImage" v-if="imgList.length != 5"><text class="cuIcon-cameraadd"></text></view>
						</view>
					</view>
					<!-- end -->
					<!-- 添加 -->
					<view class="padding flex flex-direction"><button class="cu-btn bg-red margin-tb-sm lg" form-type="submit">添加</button></view>
				</form>
			</view>
		</view>

		<!-- 分类选择的模态框 -->
		<view @touchmove.stop="modeMove" class=" cu-modal drawer-modal justify-start " :class="modalName == 'DrawerModal' ? 'show' : ''" @tap="hideModal">
			<scroll-view scroll-with-animation="true" scroll-y="true" class="cu-dialog basis-df">
				<view class="cu-list menu text-left">
					<view class="cu-item com-type-item" v-for="(item, index) in comTypeList" :key="index" @tap="getComType" :data-name="item">{{ item }}</view>
				</view>
			</scroll-view>
		</view>
		<!-- free space for tabbar -->
		<view style="height: 100rpx;"></view>
	</view>
</template>

<script>
import typeList from '@/components/list/type-list'
import MxDatePicker from '@/components/mx-datepicker/mx-datepicker.vue'
import _release_data from '@/static/zaiui/data/release.js' //虚拟数据
import _tool from '@/static/zaiui/util/tools.js' //工具函数
import {mapState} from 'vuex'

export default {
	name: 'release',
	components: {
		typeList,
		MxDatePicker
	},
	data() {
		return {
			isShowPicker: false,
			outdatedTime: '',
			focus: 0, //0 为通告信息，1为物品详细信息填写
			scene: -1, // 0,1,2 分别代表发布带物品的闲置通告、需求通告、任务通告
			noticeTypeListData: [],
			commodity:{
				name:'',
				description:''
			},
			modalName: '', //模态框开关
			comList: [], // commodity addition list
			newnessListIndex: 0, //几层新下标（默认全新）
			newnessList: [], //几成新
			comType: '电子产品', //分类选择默认
			comTypeList: [], // 分类列表
			newPrice: '', //出售价
			oriPrice: '', //原价
			imgList: [] ,//图片 url 列表
			imgError: false
		}
	},
	props: {
		show: {
			type: Boolean,
			default: true
		}
	},
	watch: {
		scene(newVal,oldVal){
			// console.log('scene new value',newVal);
			if(oldVal== -1 && newVal != -1) this.$emit('switchTab',false) // close tabbar
			else if(oldVal != -1 && newVal == -1) this.$emit('switchTab',true)
		}
	},
	computed:{
		descPlaceholder(){
			if(this.scene == 0) return '描述物品的转手原因,入手渠道和使用感受,或者其他的一些描述'
			else if(this.scene == 1) return '描述所需物品的一些特征、条件和用途'
		},
		noticeTitle(){
			if(this.scene == 0) return '给通告起个好名字'
			else if(this.scene == 1) return '需求通告的名称'
			else return '指派的任务名称'
		},
		noticeDesc(){
			if(this.scene == 0) return '通告整体的描述'
			else return '描述具体的任务，如到某地取快递等'
		},
		...mapState(['userAddress'])
	},
	created() {
		// 加载备选的菜单 List
		this.noticeTypeListData = _release_data.noticeTypeListData()
		this.comTypeList = _release_data.comTypeData()
		this.newnessList = _release_data.newnessData()
	},
	mounted() {
		_tool.setBarColor(true)
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
	},
	methods: {
		comfirmDatetime(e) {
			this.isShowPicker = false
			if (e) this.outdatedTime = e.value
		},
		showPicker() {
			this.isShowPicker = true
		},
		// 新旧程度
		newnessState: function(e) {
			var that = this
			uni.showActionSheet({
				itemList: that.newnessList,
				success(e) {
					that.newnessListIndex = e.tapIndex
				}
			})
		},
		// 图片上传
		ChooseImage() {
			uni.chooseImage({
				count: 5, //默认9
				sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
				sourceType: ['album'], //从相册选择
				success: res => {
					console.log('choose imga',res)
					res.tempFilePaths.forEach(e => this.imgList.push(e))
				}
			})
		},
		ViewImage(e) {
			uni.previewImage({
				urls: this.imgList,
				current: e.currentTarget.dataset.url
			})
		},
		// 删除照片
		DelImg(e) {
			uni.showModal({
				title: '提示',
				content: '确定要删除这个照片吗？',
				cancelText: '再看看',
				confirmText: '删除',
				success: res => {
					if (res.confirm) {
						this.imgList.splice(e.currentTarget.dataset.index, 1)
						this.imgList = this.imgList
					}
				}
			})
		},
		async uploadImage(){
			// upload one by one, get responsed image url sychronously
			for(let i = 0;i < this.comList.length;i++){
				let c = this.comList[i]
				c.images = []
				for(let j = 0;j < c.imgList.length;j++){
					await this.$api.uploadImage(c.imgList[j])
					.then(res=>{c.images.push(res.data)}) //未判断返回的是否是个 URL 字符串
					.catch(err=>{
						this.imgError = true
						console.log('一张图片上传失败',this.imgError);
					})
				}
			}
		},
		validateNotice(data){// 验证通告表单
			if(data.title.length === 0){
			 uni.showToast({
				title: '通告标题不能为空',
				icon: 'none',
			 });
				return false
			}
			else if(data.title.length > 15){
				uni.showToast({
					title: '通告标题不能超过15个字',
					icon: 'none'
				});
				return false
			}
			
			// validate address
			let prefixs = ['信息学部','文理学部','工学部','医学部']
			if(data.address.length === 0){
				uni.showToast({
					title: '地址不能为空',
					icon: 'none'
				});
				return false
			}
			else if(!prefixs.some(v=>data.address.startsWith(v))){
				uni.showToast({
					title: '地址需要以某个学部开头',
					icon: 'none'
				});
				return false
			}
			
			// validate outdatedTime
			let now = new Date()
			let val = new Date(data.outdatedTime)
			if(data.outdatedTime.length === 0){
				uni.showToast({
					title: '失效日期不能为空',
					icon: 'none'
				});
				return false
			}
			else if(val < now){
				uni.showToast({
					title: '失效日期不能小于当前日期',
					icon: 'none'
				});
				return false
			}
			return true
		},
		validateCommodity(data){ // 验证物品表单
			// validate name
			if(data.name.length === 0){
			 uni.showToast({
				title: '物品名称不能为空',
				icon: 'none',
			 });
				return false
			}
			
			// validate price
			if(isNaN(data.newPrice)){
				uni.showToast({
					title: '预期价格不能为空',
					icon: 'none'
				});
				return false
			}
			
			// validate count
			if(isNaN(data.count)){
				uni.showToast({
					title: '数量不能为空',
					icon: 'none'
				})
				return false
			}
			else if( data.count.toString().includes('.')){
				uni.showToast({
					title: '数量应为整数',
					icon: 'none'
				});
				return false
			}
			
			return true
		},
		async noticeSubmit(e){
			let notice = e.detail.value
			console.log('notice data',notice);
			if(!this.validateNotice(notice)) return

			// upload all commodity first and get image url to set property
			uni.showLoading({
				title: '发布中',
				mask: true,
			});
			if(this.scene === 2){
				// 任务通告
				console.log('任务通告发布');
				delete notice.demandPrice
				let commodity = {
					name: notice.title,
					description: notice.description,
					count: 1,
					oriPrice: -1,
					newPrice: parseFloat(notice.demandPrice) || -1,
					type: '代领',
					newness: '',
					imgList: ''
				}
				this.comList.push(commodity)
			}
			else{		
				await this.uploadImage()
				console.log('图片处理完毕，isError:',this.imgError)
				if(this.imgError){
					console.log('存在失败上传的图片，请重新发布通告')
					this.imgError = false
					uni.hideLoading()
					uni.showToast({
						title:'存在失败上传的图片，请重新发布通告',
						icon:'none',
						duration: 3000
					})
					return
				}
			}
			
			// submit notice data to server
			this.$api.addNotice({
				...notice,
				type: this.scene === 0 ? 0:1,
				comList: this.comList
				})
				.then(res=>{
					console.log('通告上传成功');
					uni.hideLoading()
					uni.showToast({
						title: '发布成功',
						duration: 2000
					});
					this.scene = -1
				})
				.catch(err =>{
					console.log('通告上传失败');
					uni.hideLoading()
					uni.showToast({
						title: '发布失败',
						duration: 2000,
						icon:'none'
					});
				})
		},
		comFormSubmit(e) { // 添加物品
			let comFormData = e.detail.value
			console.log('commodityform data',comFormData)
			let commodity = {
				name: comFormData.comName,
				description: comFormData.comDescription,
				count:  parseFloat(comFormData.count),
				oriPrice: parseFloat(comFormData.oriPrice) || -1,
				newPrice: parseFloat(comFormData.newPrice),
				type: comFormData.comType,
				newness: comFormData.newness || '',
				imgList: this.imgList
			}
			if(!this.validateCommodity(commodity)) return
			this.comList.push(commodity)
			console.log('Commodity',commodity)
			this.focus = 0
			this.imgList = []
		},
		delCom(index) {
			uni.showModal({
				title: '提示',
				content: '确认删除该物品吗',
				showCancel: true,
				cancelText: '取消',
				confirmText: '确认',
				success: res => {
					if (res.confirm) this.comList.splice(index, 1)
				}
			})
		},
		addComTap() {
			let nameList = ['毛巾', 'Sam', 'Water']
			// this.comList.push({name:nameList[parseInt(Math.random() * 3)],age:11})
			this.focus = 1
		},
		backTap() {
			if (this.focus == 0) this.scene = -1
			else this.focus = 0
		},
		closeTap() {
			this.$emit('closeReleaseTap')
		},
		typeListTap(e) {
			this.scene = e.index
			if(this.scene != 0) this.comList = []
		},
		// 显示分类模态框
		showModal(e) {
			this.modalName = e.currentTarget.dataset.target
		},
		// 隐藏分类模态框
		hideModal(e) {
			this.modalName = null
		},
		// 得到物品分类的值
		getComType: function(e) {
			console.log('com type list', e)
			this.comType = e.currentTarget.dataset.name
			this.hideModal()
		}
	}
}
</script>

<style lang="scss" scoped>
$item_lh: 66rpx;
$border_color: #e54d42;
.margin-status{
	margin-top: calc(var(--status-bar-height) + 10rpx); //适配 APP的状态栏
}
.com-type-item {
	text-align: center;
}
.release_btn {
	background-color: $border_color;
	color: white;
}
.add-com-btn {
	text-align: center;
	margin: 0.3em 30rpx;
	border: 3rpx solid $border_color;
	border-radius: 20rpx;
	line-height: $item_lh;
}
.commodity {
	line-height: $item_lh;
	border: 1rpx solid $border_color;
	border-radius: 30rpx;
	padding: 0 20rpx;
	margin: 2rpx;
}
.comList {
	margin: 10rpx;
	background-color: white;
	border-radius: 10rpx;
	display: flex;
	flex-wrap: wrap;
	align-items: center;
}
.detail-topbox {
	position: relative;
	margin-top: 20rpx;
	display: flex;
	align-items: center;
	height: 60rpx;

	.text-back {
		z-index: 10;
		margin-left: 16rpx;
	}

	.text-large {
		position: absolute;
		width: 100%;
		text-align: center;
	}
}

.release_box {
	display: none;
}

.release_box.show {
	display: block;
}

.text-large {
	font-size: 36rpx;
}

.zaiui-release-box {
	background: #fafafa;
	position: relative;
	z-index: 99999;
	width: 100%;
	display: block;

	.zaiui-bar-view-box {
		position: fixed;
		top: 0;
		width: 100%;
		z-index: 999999;
		background: #fafafa;

		/* #ifndef MP */
		height: calc(var(--status-bar-height) + 99.99rpx);
		/* #endif */

		/* #ifdef MP */
		height: calc(var(--status-bar-height) + 189.99rpx);
		/* #endif */

		padding: var(--status-bar-height) 27.27rpx 0 27.27rpx;
		align-items: center;

		.zaiui-bar-box {
			position: relative;
			width: 100%;
			align-items: center;
			line-height: 99.99rpx;

			.close {
				position: absolute;
				right: 27.27rpx;
				font-size: 40rpx;
				bottom: 9.09rpx;
			}
		}
	}

	.zaiui-seat-height {
		width: 100%;

		/* #ifndef MP */
		height: calc(var(--status-bar-height) + 139.99rpx);
		/* #endif */

		/* #ifdef MP */
		height: calc(var(--status-bar-height) + 239.99rpx);
		/* #endif */
	}

	.zaiui-view-content {
		padding: 0 27.27rpx 27.27rpx;
	}
}

.zaiui-release-box.show {
	display: block;
}
</style>
